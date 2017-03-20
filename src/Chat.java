import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * Created by robin.boregrim on 2016-12-12.
 *
 * Type: Client
 * This is the Chatroom from the Client's perspective.
 */
public class Chat extends JPanel implements ActionListener{

    JPanel textPanel;
    JTextArea ta;
    JScrollPane scroll;
    JPanel p,p2;
    Background bg;
    JTextField tf;
    JButton button;
    String name;


    int id;
    Client client;


    public Chat(String name, int groupId, Client k) throws IOException {
        client = k;
        id = groupId;
        this.name = name;


        //Sätter upp chat JFrame:en
        ta = new JTextArea("");
        ta.setSize(new Dimension(900, 300));
        scroll = new JScrollPane(ta);


        //setResizable(false);
        p = new JPanel();
        p2 = new JPanel();
        tf = new JTextField();
        button = new JButton("Send");
        //img = ImageIO.read(new File("bg.png"));

        //Fixar bakgrundsbilden
        bg = new Background();
        //bg.setSize(new Dimension(900, 300));
        bg.paint(getGraphics());
        ta.setEditable(false);

        //bg.setBackground(Color.blue);

        //Gör saker osynliga så man kan se bakgrunden
        Color transperant = new Color(255, 255, 255, 0);
        p.setBackground(transperant);
        p2.setBackground(transperant);
        ta.setBackground(transperant);

        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        ta.setOpaque(false);
        tf.setOpaque(true);

        //Färger

        Color lime = new Color(25, 198, 15);
        Color darkblue = new Color(0, 45, 102);

        //Background och Foreground färger
        tf.setBackground(Color.BLACK);
        button.setBackground(Color.black);
        p.setBackground(Color.black);

        ta.setForeground(Color.white);
        tf.setForeground(Color.white);
        button.setForeground(Color.white);


        //Border

        Border roundedBorder = new LineBorder(darkblue, 3, true);
        Border noBorder = new LineBorder(Color.white, 0, true);

        tf.setBorder(noBorder);
        //ta.setBorder(roundedBorder);
        scroll.setBorder(noBorder);
        button.setBorder(new MatteBorder(0, 2, 0, 0, Color.white));
        p.setBorder(new MatteBorder(2, 0, 0, 0, Color.white));


        //Font
        Font f = new Font("Serif", Font.BOLD, 18);
        ta.setFont(f);
        tf.setFont(f);
        button.setFont(f);


        //Sätter actionlisteners
        button.addActionListener(this);
        tf.addActionListener(this);

        //Title



        //Addar till framen


        setBackground(Color.BLACK);
        //add(bg);
        setLayout(new BorderLayout());
        add(scroll, BorderLayout.CENTER);
        add(p, BorderLayout.SOUTH);
        p.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.fill = c.HORIZONTAL;
        c.gridwidth = 1;
        c.weightx = 20;
        c.weighty = 1;
        p.add(tf, c);

        c.gridx = 1;
        c.gridwidth = 1;
        c.weightx = 1;
        p.add(button, c);
//
//
        setSize(900, 600);
        bg.setSize(900, 600);

    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == button || e.getSource() == tf){
            ta.append(" You: " + tf.getText()+ "\n");
            send(new Packet(tf.getText(), name, id,true));
            tf.setText("");

        }
    }
    private void send(Packet p){
        client.send(p);
    }
    public void handlePacket(Packet p){
        ta.append(" " + p.getSender() + ": " + p.getText() + "\n");
    }

    public int getId() {
        return id;
    }
}
