import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Created by robin.boregrim on 2016-10-18.
 */
public class Background extends JPanel {
    Image img;
    public Background(){
         try {
             BufferedImage wPic = ImageIO.read(this.getClass().getResource("bg.png"));
             img = new ImageIcon(wPic).getImage();
         } catch (IOException e) {}
        repaint();
    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g.drawImage(img,0,0,this);
    }
}
