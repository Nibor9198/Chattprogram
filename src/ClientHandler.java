import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by robin.boregrim on 2016-11-28.
 */
public class ClientHandler implements Runnable{
    Server server;
    Socket so;
    ObjectOutputStream out;
    ObjectInputStream in;

    ArrayList<ChatHandler> chats;
    boolean hasUpdate;

    String name;

    public ClientHandler(Socket socket, Server server){
        name = "Anonumous";
        chats = new ArrayList<>();
        this.server = server;
        hasUpdate = true;

        so = socket;
        try {
            out = new ObjectOutputStream(so.getOutputStream());
            in  = new ObjectInputStream(so.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        while(!so.isConnected()){}
        //new Thread(this).start();
        new Thread(this).start();

    }

    @Override
    public void run() {
        if(!hasUpdate){
            while(true){
                hasUpdate = true;
                try {
                    //Packet p = new Packet()
                    //send();

                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }else {
            while (true) {
                Packet p = null;
                try {
                    p = (Packet) in.readObject();
                    handlePacket(p);
                } catch (IOException e) {
                    System.out.println("Client Disconnected...");
                    server.disconnect(this);
                    break;
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

            }
        }
    }
    private void handlePacket(Packet p){

        name(p);
        if (p.getIsMessage()) {
            boolean notSent = true;
            for (ChatHandler c:chats) {
                if(p.getId() == c.getId()){
                    c.send(p,this);
                    notSent = false;
                    break;
                }
            }
            if(notSent) {
                ChatHandler c = server.getChats().get(p.getId());
                join(c);
                c.send(p,this);
            }

        }else {
            if(testPacketText(p, "newChat")) {
                //System.out.println(p.getText().substring(p.getText().indexOf(" ")+1));
                server.newChat(p.getText().substring(p.getText().indexOf(" ")+1), this);
            }
            else if(p.getText().equals("joinChat"))
                join(p.getId());
            else if(p.getText().equals("getChats"))
                send(new Packet("getChats" , "Server", server.getChatRefs()));
        }
    }
    public void send(Packet p){
        try {
            out.writeObject(p);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void join(ChatHandler c){
        chats.add(c);
        send(new Packet("join", c.getName(), c.getId(),false));
    }
    public void join(int id){
        boolean exists = false;
        for (ChatHandler c: chats) {
            if (c.getId() == id){
                exists = true;
            }
        }
        if(!exists) {
            ChatHandler c = server.getChats().get(id);
            System.out.println(name + " - > "  + c.getId());
            c.join(this);
            //send(new Packet("join", "Server", c.getId(), false));
        }else{
            System.out.println("Already joined");
        }
    }
    public void leave(ChatHandler c){
        c.leave(this);
        chats.remove(chats.indexOf(c));

    }
    private ArrayList<Object> getChatsString(){
        ArrayList<Object> array = new ArrayList<>();
        for (ChatHandler c : chats) {
            array.add(c.getId());
        }
        return array;

    }
    //Uppdaterar namnet
    private void name(Packet p){
        if ((name == null  || !name.equals(p.getSender())) && p.getSender() != null)
            name = p.getSender();
    }

    public String getName() {
        return name;
    }

    private boolean testPacketText(Packet p, String text){
        if(p.getText().indexOf(" ") > 0){
            return p.getText().substring(0, p.getText().indexOf(" ")).equals(text);
        }else{
            return p.getText().equals(text);
        }
    }
}
