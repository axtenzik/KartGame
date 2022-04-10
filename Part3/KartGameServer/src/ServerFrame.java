import java.io.*;
import java.net.*;
import javax.swing.*;

public class ServerFrame extends JFrame
{
    public static void main(String[] args)
    {
        new ServerFrame();
    }

    ServerFrame()
    {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        ServerPanel sp = new ServerPanel();
        add(sp);
        pack();
        setLocation(100, 100);
        setVisible(true);

        ServerHandler.Handler();
    }
}
