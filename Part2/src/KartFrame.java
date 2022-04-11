import java.awt.*;
import javax.swing.*;

public class KartFrame extends JFrame
{
    KartFrame()
    {
        //Exit program when hit close
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //IMPORTANT!!!

        //add panel and keyListener to frame
        DisplayPanel dp = new DisplayPanel();
        add(dp);
        addKeyListener(dp);

        //sets size to panel size and sets the location on the screen
        pack();
        setLocation(100,100);
        setVisible(true);
    }

    public static void main(String[] args)
    {
        new KartFrame();
    }
}