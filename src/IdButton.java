import javax.swing.*;

/**
 * Created by robin.boregrim on 2017-01-09.
 */
public class IdButton extends JButton {
    int id;

    public IdButton(){

    }
    public IdButton(String name, int id){
        super(name);
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
}
