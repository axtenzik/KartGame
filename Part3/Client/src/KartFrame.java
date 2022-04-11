import java.awt.*;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import javax.swing.*;

public class KartFrame extends JFrame
{
    public static void main(String[] args)
    {
        new KartFrame();
    }

    KartFrame()
    {
        //Exit program when hit close
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //IMPORTANT!!!

        //add panel and keyListener to frame
        DisplayPanel dp = new DisplayPanel(this);
        add(dp);
        addKeyListener(dp);

        //sets size to panel size and sets the location on the screen
        pack();
        setLocation(100,100);
        setVisible(true);
    }
}