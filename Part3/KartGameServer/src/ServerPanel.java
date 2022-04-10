import javax.swing.*;
import java.awt.*;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class ServerPanel extends JPanel
{
    ServerPanel()
    {
        setPreferredSize(new Dimension(400, 200));
        setLayout(null);

        InetAddress host = null;

        try
        {
            host = InetAddress.getLocalHost();
        }
        catch (UnknownHostException e)
        {
            System.err.println("UnknownHostException: " + e);
        }

        JLabel IPLabel = new JLabel();
        IPLabel.setText("Server IP address: ");

        JLabel addressLabel = new JLabel();
        addressLabel.setText(host.getHostAddress());

        IPLabel.setSize(150, 50);
        addressLabel.setSize(150, 50);

        IPLabel.setLocation(50, 50);
        addressLabel.setLocation(200, 50);

        add(IPLabel);
        add(addressLabel);
    }
}
