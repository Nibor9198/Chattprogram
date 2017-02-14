import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by robin.boregrim on 2016-12-19.
 */
public class ServerList extends JFrame implements ActionListener{
    String[] columnNames;
    Object[][] data;

    Client client;
    String stringTemp;

    public ServerList(Client client, ArrayList<ChatRef> chatRefs){
        String string = "";
        for (ChatRef c:chatRefs) {
            string += "\n" + c.toString() ;
        }

        System.out.println(string);
        //string = "Whatup Alfred 23 \nSassy Edvin 42 ";




        this.client = client;
        columnNames = new String[]{"Server Name", "Creator", "Join"};

        Scanner sc = new Scanner(string);
        //För att få bort den översta tomma raden
        sc.nextLine();
        ArrayList<ArrayList<Object>> objects = new ArrayList<>();

        while(sc.hasNextLine()) {
            ArrayList<Object> array = new ArrayList<>();
            stringTemp = sc.nextLine();
            array.add(getNext(stringTemp));
            System.out.println(stringTemp);
            array.add(getNext(stringTemp));
            System.out.println(stringTemp);
            //IdButton b = new IdButton("Join", Integer.parseInt(getNext(stringTemp)));
           // b.addActionListener(this);
            array.add("Join " + Integer.parseInt(getNext(stringTemp)));
            objects.add(array);
        }

        data = new Object[objects.size()][3];
        System.out.println("Objects size: " + objects.size());


        for (int i = 0; i < objects.size(); i++) {
            for (int j = 0; j < 3; j++) {
                data[i][j] = objects.get(i).get(j);
            }
        }
        JTable table = new JTable(data,columnNames);




        table.getColumn("Join").setCellRenderer(new JTableButtonRenderer());
        table.getColumn("Join").setCellEditor(
                new ButtonEditor(new JCheckBox(), this)
        );

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane);
        setVisible(true);
        setSize(200,200);


        //FORSTÄTT

        //http://docs.oracle.com/javase/tutorial/uiswing/components/table.html
    }




    private String getNext(String s){
        String string = s.substring(0,s.indexOf(" "));
        stringTemp = s.substring(s.indexOf(" ") + 1);

        return string;
    }
    public void buttonPress(int id){
        System.out.println(id);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() instanceof  IdButton){
            client.join(((IdButton)e.getSource()).getId());
            dispose();
        }
    }
}
