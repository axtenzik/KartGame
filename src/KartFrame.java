import java.awt.*;
import javax.swing.*;

public class KartFrame extends JFrame
{
    KartFrame()
    {
        //Configure frame properties
        setBounds(100, 100, 200, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //IMPORTANT!!!

        //Create object of panel class and add to the frame
        //getContentPane().add(new KartPanel());
        Container cp = getContentPane();
        System.out.println("content pane height: " + cp.getHeight());
        System.out.println("content pane width: " + cp.getWidth());
        KartPanel kp = new KartPanel();
        //kp.setBounds(125, 125, 200, 200);
        cp.add(kp);

        setVisible(true);

        System.out.println("Frame height: " + this.getHeight());
        System.out.println("Frame width: " + this.getWidth());
    }

    public static void main(String[] args)
    {
        new KartFrame();
    }
}
