import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by robin.boregrim on 2016-11-28.
 */
public class Server implements Runnable{
    private ServerSocket ss;
    ObjectInputStream oin;
    ObjectOutputStream oout;
    ArrayList<ClientHandler> clients;
    ArrayList<ChatHandler> chats;

    public Server(){
        clients = new ArrayList<>();
        chats = new ArrayList<>();
        try{
            ss = new ServerSocket(24324);

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,"There is already a server on this port.");
            System.exit(0);
        }

        new Thread(this).start();
    }

    public static void main(String[] args) {
        new Server();
    }

    @Override
    public void run() {

        while(true){
            try {
                Socket so  = ss.accept();
                new Thread(this).start();
                clients.add(new ClientHandler(so, this));

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public void disconnect(ClientHandler client){

    }
    public void newChat(String name, ClientHandler client){
        ChatHandler chat = new ChatHandler(this, chats.size(), name, client);
        chat.join(client);

        chats.add(chat);


    }


    public ArrayList<ChatHandler> getChats() {
        return chats;
    }
    public ArrayList<ChatRef> getChatRefs(){
        ArrayList<ChatRef> chatRefs = new ArrayList<>();
        for (ChatHandler c : getChats()) {
            chatRefs.add(new ChatRef(c));
        }
        return chatRefs;
    }

    public ArrayList<ClientHandler> getClients() {
        return clients;
    }
}
