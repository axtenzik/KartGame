import java.awt.*;
import javax.swing.*;

public class KartFrame extends JFrame
{
    KartFrame()
    {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //IMPORTANT!!!

        DisplayPanel dp = new DisplayPanel();
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