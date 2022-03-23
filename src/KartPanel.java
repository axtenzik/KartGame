import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class KartPanel extends JPanel
{
    private final static String kartName = "Green";
    private final int kartFrames = 16;
    private int currentKart = 0;
    protected ImageIcon[] kart;


    KartPanel()
    {
        this.setBounds(0, 0, 200, 200);
        this.setLayout(null);
        //Components added here

        //LoadImages
        kart = new ImageIcon[kartFrames];

        for (int i = 0; i < kart.length; i++)
        {
            kart[i] = new ImageIcon(getClass().getResource(/*"D:/Uni/Distributed Programming/Kart/Green/" + */kartName + i + ".png"));
        }

        //Sets timer for 100 milliseconds
        Timer t = new Timer(100, this::actionPerformed);
        t.start();

        System.out.println("Panel height: " + this.getHeight());
        System.out.println("Panel width: " + this.getWidth());
        System.out.println("Panel x: " + this.getX());
        System.out.println("Panel y: " + this.getY());
    }

    public void actionPerformed(ActionEvent e)
    {
        repaint();
        currentKart = (currentKart + 1) % kartFrames;
    }

    public void paintComponent(Graphics g)
    {
        //display image
        super.paintComponent(g);

        kart[currentKart].paintIcon(this, g, 150, 100);

        //reassign the current image to the next sequential image
        //Moved to action performed as paint component is called when resizing and the kart will spin faster than intended
        //currentKart = (currentKart + 1) % kartFrames;
    }
}
