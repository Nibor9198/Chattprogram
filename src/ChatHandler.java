
import java.util.ArrayList;

/**
 * Created by robin.boregrim on 2016-11-29.
 */
public class ChatHandler {
    private Server server;
    private int id;
    private String name, creator;
    private ArrayList<ClientHandler> clients;
    public ChatHandler(Server server, int id, String name, ClientHandler creator){
        this.server = server;
        this.id = id;
        clients = new ArrayList<>();
        System.out.println("New chat created");

        this.name = name;
        this.creator= creator.getName();
    }


    public void join(ClientHandler c){
        clients.add(c);
        c.join(this);
        Packet p = new Packet(c.getName() + " has Joined", "", id, true);
        send(p);
    }
    public void leave(ClientHandler c){
        if(clients.indexOf(c) > 0) {
            clients.remove(clients.indexOf(c));
            Packet p = new Packet(c.getName() + " has been disconnected...", "", id, true);
            send(p);
        }
    }


    public void send(Packet p){
        for (ClientHandler c: clients) {
            c.send(p);
            System.out.println(p.getText());
        }
    }
    public void send(Packet p, ClientHandler client){
        for (ClientHandler c: clients) {
            if(!c.equals(client)){
                c.send(p);
            }
        }
    }

    public int getId() {
        return id;
    }

    public ArrayList<ClientHandler> getClients() {
        return clients;
    }

    public String getName() {
        return name;
    }

    public String getCreator() {
        return creator;
    }
}
