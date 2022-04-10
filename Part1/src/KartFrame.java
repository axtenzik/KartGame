import java.awt.*;
import javax.swing.*;

public class KartFrame extends JFrame
{
    KartFrame()
    {
        //Exit program when hit close
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //add panel to frame
        KartPanel kp = new KartPanel();
        add(kp);

        //sets size to panel size and sets the location on the screen
        pack();
        setLocation(100,100);
        setVisible(true);
    }

    public static void main(String[] args)
    {
        //makes the window for display
        new KartFrame();
    }
}
