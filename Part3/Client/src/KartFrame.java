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
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //IMPORTANT!!!

        DisplayPanel dp = new DisplayPanel();
        add(dp);
        addKeyListener(dp);

        pack();
        setLocation(100,100);
        setVisible(true);

    }
}