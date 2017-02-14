import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by robin.boregrim on 2016-11-08.
 */
public class Packet implements Serializable {
    boolean isMessage;
    String text, sender;
    int id;
    ArrayList<ChatRef> chatRefs;


    public Packet(String text, String sender, int groupId, boolean isMessage){
        this.text = text;
        this.sender = sender;
        id = groupId;
        this.isMessage = isMessage;
    }
    public Packet(String text, String sender, ArrayList<ChatRef> chatRefs){
        this(text, sender, -1, false);
        this.chatRefs = chatRefs;
    }


    public void setMessage(boolean message) {
        isMessage = message;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setId(int id) {
        this.id = id;
    }


    public ArrayList<ChatRef> getChatRefs(){
        return chatRefs;
    }
    public boolean getIsMessage(){return isMessage;}

    public String getText() {return text;}

    public int getId() {return id;
    }

    public String getSender() {
        return sender;
    }
}
