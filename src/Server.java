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
 *
 * Type: Server
 * This class is the server.
 */
public class Server implements Runnable{

    //The server socket
    private ServerSocket ss;

    // ArrayLists to keep track off clients and chattrooms
    ArrayList<ClientHandler> clients;
    ArrayList<ChatHandler> chats;

    public Server(){
        //Initiation
        clients = new ArrayList<>();
        chats = new ArrayList<>();
        try{
            ss = new ServerSocket(24324);

        } catch (IOException e) {
            //If initiation of the ServerSocket fails there is already a server on that port
            JOptionPane.showMessageDialog(null,"There is already a server on this port.");
            System.exit(0);
        }
        // Start the client searching process
        new Thread(this).start();
    }

    public static void main(String[] args) {
        new Server();
    }

    @Override
    public void run() {

        while(true){
            try {
                //Wait for a client to connect
                Socket so  = ss.accept();
                //When a client has connected start a new searching loop
                new Thread(this).start();
                //Create a ClientHandler for the client and add it to the ArrayList
                clients.add(new ClientHandler(so, this));

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    //Disconnects the ClientHandler from all its ChatHandlers
    public void disconnect(ClientHandler client){
        for (ChatHandler c:client.chats) {
            c.leave(client);
        }
    }
    //Create a new Chat
    public void newChat(String name, ClientHandler client){
        //Create a new ChatHandler
        ChatHandler chat = new ChatHandler(this, chats.size(), name, client);
        //Connect the creator to the chat
        chat.join(client);
        //Add the chat to the ArrayList
        chats.add(chat);


    }

    //Getter and Setters

    public ArrayList<ChatHandler> getChats() {
        return chats;
    }

    //Is used to get a list of the chats that has just the needed amount of information to make a Server list. See
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
