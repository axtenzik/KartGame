import java.awt.*;
import javax.swing.*;

public class KartFrame extends JFrame
{
    KartFrame()
    {
        //Configure frame properties
        //setBounds(100, 100, 200, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //IMPORTANT!!!

        //Create object of panel class and add to the frame
        //getContentPane().add(new KartPanel());
        //Container cp = getContentPane();
        //KartPanel kp = new KartPanel();
        //add(kp);
        //addKeyListener(kp);
        DisplayPanel dp = new DisplayPanel(this);
        add(dp);
        addKeyListener(dp);

        pack();
        setLocation(100,100);
        setVisible(true);
    }

    public static void main(String[] args)
    {
        new KartFrame();
    }
}