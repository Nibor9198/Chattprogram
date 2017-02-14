import java.io.Serializable;

/**
 * Created by robin.boregrim on 2016-12-19.
 */
public class ChatRef implements Serializable{

    String name,creator;
    int id, users;
    public ChatRef (ChatHandler c){
        id = c.getId();
        users = c.getClients().size();
        name = c.getName();
        creator = c.getCreator();
    }
    public String toString(){
        String s =  name + " " + creator + " " + id + " ";
        return s;
    }
}
