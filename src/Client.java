import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by robin.boregrim on 2016-11-28.
 */
public class Client extends JFrame implements ActionListener, Runnable{

    //Network
    Socket so;
    InputStream in;
    OutputStream out;

    ObjectInputStream oin;
    ObjectOutputStream oout;

    //Ui
    JPanel panel;
    JButton newChat,joinChat;
    JTabbedPane ta;

    String name;


    ArrayList<Chat> chats;
    public Client(){
        chats = new ArrayList<>();

        name = "Anonymous";
        String temp = JOptionPane.showInputDialog("What is your name?");
        if(temp != null) name = temp;

        //Set Title
        setTitle(name);
        connect();
        createUi();
    }

    private void connect(){
        try {
            so = new Socket("localhost", 24324);

            oout = new ObjectOutputStream(so.getOutputStream());
            oin  = new ObjectInputStream(so.getInputStream());


        } catch (IOException e) {
            e.printStackTrace();
        }
        while(!so.isConnected()){}
        new Thread(this).start();
        //Packet p = new Packet("Hej",0,true);

       //try {
       //    oout.writeObject(p);
       //} catch (IOException e) {
       //    e.printStackTrace();
       //}
    }

    private void createUi(){
        setSize(new Dimension(900, 500));
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);


        panel = new JPanel();
        newChat = new JButton("New Chat");
        joinChat = new JButton("Join Chat");
        ta = new JTabbedPane();


        //ActionListeners
        newChat.addActionListener(this);
        joinChat.addActionListener(this);

        //Layout
        //setLayout(new BorderLayout());

        ta.setSize(900,500);
        add(ta);

        panel.setBackground(Color.blue);
        ta.addTab("Main menu", panel);

        //panel.setLayout(new GridLayout());

        newChat.setSize(15,20);
        panel.add(newChat);

        panel.add(joinChat);


    }

    public static void main(String[] args) {
        new Client();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == newChat){
            String s = JOptionPane.showInputDialog("Enter a name for the server");
            if(!s.equals(null)) {
                Packet p = new Packet("newChat " + s, name, 0, false);
                send(p);
            }
        }
        if(e.getSource() == joinChat){
            System.out.println("join");
            Packet p = new Packet("getChats", name, 0, false);
            send(p);
        }
    }

    public void send(Packet p){
        try {
            System.out.println("send " + p.getText());
            oout.writeObject(p);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void handlePacket(Packet p){
        if(p.getIsMessage()){
            for (Chat c: chats) {
                if(p.getId() == c.getId()){
                    c.handlePacket(p);
                }
            }

        } else{
            System.out.println(p.getText());
            if (p.getText().equals("join")){

                try {
                    Chat c = new Chat(name,p.getId(),this);

                    c.bg.setSize(900,600);
                    setPanel(p.getSender(),c);
                    chats.add(c);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(p.getText().equals("getChats") && p.getChatRefs() != null){
                new ServerList(this, p.chatRefs);
            }
        }
    }
    private void setPanel(String name,JPanel p){
        p.setSize(900,500);
        ta.addTab(name, p);
    }
    public void join(int id){
        Packet p = new Packet("joinChat",name,id,false);
        send(p);
    }

    @Override
    public void run() {
        while (true) {
            Packet p = null;
            try {
                p = (Packet) oin.readObject();
                handlePacket(p);
            } catch (IOException e) {
                System.out.println("Trying to reconnect to a Server...");
                break;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        }
    }
}
