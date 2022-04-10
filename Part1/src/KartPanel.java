import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Objects;

public class KartPanel extends JPanel
{
    private final static String kartName = "Green";
    private final int kartFrames = 16;
    private int currentKart = 0;
    protected ImageIcon[] kart;

    KartPanel()
    {
        setPreferredSize(new Dimension(200, 200));
        this.setLayout(null);

        //LoadImages
        kart = new ImageIcon[kartFrames];
        for (int i = 0; i < kart.length; i++)
        {
            kart[i] = new ImageIcon(Objects.requireNonNull(getClass().getResource(kartName + i + ".png")));
        }

        //Sets timer for 100 milliseconds
        Timer t = new Timer(100, this::actionPerformed);
        t.start();
    }

    public void actionPerformed(ActionEvent e)
    {
        //Moved to action performed from paint component as paint component is called when resizing
        //and the kart will spin faster than intended when resizing
        currentKart = (currentKart + 1) % kartFrames;
        repaint();
    }

    public void paintComponent(Graphics g)
    {
        //display graphics
        super.paintComponent(g);

        //displays the kart at panel co-ords 75, 75
        kart[currentKart].paintIcon(this, g, 75, 75);
    }
}
