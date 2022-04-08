import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class DisplayPanel extends JPanel implements KeyListener, ActionListener
{
    private final Kart[] racers;
    private final Timer t;
    private final Timer t1;
    private boolean paused = false;
    private boolean countdown = true;
    private boolean started = false;
    private int downCount = 0;
    private final boolean[] lights = {false, false, false, false, false};
    private final JButton b1;
    private final JButton b2;
    private final KartFrame kFrame;
    private final int[] lightsPosition = {300, 325};

    private final int[] displaySize = {850, 650};
    private final int numberRacers = 4;
    private final int numberAI = numberRacers - 2;
    private final int timeStep = 20;
    private int startPos = 375;

    DisplayPanel(KartFrame kf)
    {
        kFrame = kf;

        setPreferredSize(new Dimension(displaySize[0], displaySize[1]));
        this.setLayout(null);

        racers = new Kart[numberRacers];
        for (int i = numberRacers - numberAI; i < racers.length; i++)
        {
            racers[i] = new Kart("Green", startPos, 550);
            startPos -= 60;
        }
        for (int i = 1; i < numberRacers - numberAI; i++)
        {
            racers[i] = new Kart("Blue", startPos, 550);
            startPos -= 60;
        }
        racers[0] = new Kart("Red", startPos, 550);

        //TrackHandler.CreateTracks();
        TrackHandler.LoadTracks();

        t = new Timer(timeStep, this::TimerPerformed);
        t1 = new Timer(1000, this::CountdownPerformed);
        t1.start();

        b1 = new JButton("Resume");
        b2 = new JButton("Exit");
        b1.setBounds(250, 250, 350, 75);
        b2.setBounds(250, 375, 350, 75);
        b1.addActionListener(this);
        b2.addActionListener(this);
        add(b1);
        add(b2);
        b1.setVisible(false);
        b1.setEnabled(false);
        b2.setVisible(false);
        b2.setEnabled(false);
        b1.setFocusable(false);
        b2.setFocusable(false);
    }

    private void PauseTimer()
    {
        t.stop();
        paused = true;

        b1.setVisible(true);
        b1.setEnabled(true);
        b2.setVisible(true);
        b2.setEnabled(true);
    }

    private void ContinueTimer()
    {
        t.start();
        paused = false;

        b1.setVisible(false);
        b1.setEnabled(false);
        b2.setVisible(false);
        b2.setEnabled(false);
    }

    private void Crashed()
    {
        t.stop();
        paused = true;
    }
    private void Win()
    {
        t.stop();
        paused = true;
    }

    public void keyTyped(KeyEvent e)
    {

    }

    public void keyPressed(KeyEvent e)
    {
        if (!started)
        {
            //intentionally left blank, was return; but intelliJ said it was unnecessary so took it out
        }
        else if (!paused)
        {
            switch (e.getKeyCode())
            {
                case KeyEvent.VK_LEFT -> racers[0].setInput(3);
                case KeyEvent.VK_RIGHT -> racers[0].setInput(1);
                case KeyEvent.VK_UP -> racers[0].setInput(0);
                case KeyEvent.VK_DOWN -> racers[0].setInput(2);
                case KeyEvent.VK_A -> racers[1].setInput(3);
                case KeyEvent.VK_D -> racers[1].setInput(1);
                case KeyEvent.VK_W -> racers[1].setInput(0);
                case KeyEvent.VK_S -> racers[1].setInput(2);
                case KeyEvent.VK_P -> PauseTimer();
            }
        }
        else if (e.getKeyCode() == KeyEvent.VK_P)
        {
            ContinueTimer();
        }
    }

    public void keyReleased(KeyEvent e)
    {

    }

    public void CountdownPerformed(ActionEvent e)
    {
        if (started)
        {
            t1.stop();
            countdown = false;
        }
        else if (lights[4])
        {
            started = true;
            t.start();
        }
        else
        {
            lights[downCount] = true;
            downCount++;
        }
        repaint();
    }

    public void TimerPerformed(ActionEvent e)
    {
        for (int i = numberRacers - numberAI; i < racers.length; i++)
        {
            racers[i].AI(timeStep);
        }
        for (Kart racer : racers)
        {
            if (racer.UpdateKart())
            {
                Win();
            }
        }
        for (int i = 0; i < numberRacers; i++)
        {
            for (int j = 0; j < numberRacers; j++)
            {
                if (i != j)
                {
                    if (Math.abs(racers[i].kartPosition[0] - racers[j].kartPosition[0]) < 20 && Math.abs(racers[i].kartPosition[1] - racers[j].kartPosition[1]) < 20)
                    {
                        Crashed();
                    }
                }
            }
        }
        repaint();
    }

    public void actionPerformed(ActionEvent e)
    {
        if(e.getSource() == b1)
        {
            ContinueTimer();
        }
        if(e.getSource() == b2)
        {
            try
            {
                Path trackPath = Path.of("resources/tracks.json");
                GsonBuilder builder = new GsonBuilder();
                Gson gson = builder.create();
                String circuits = gson.toJson(TrackHandler.circuits);
                Files.writeString(trackPath, circuits);
            }
            catch (Exception ee)
            {
                System.out.println(ee.toString());
            }
            kFrame.dispose();
        }
    }

    public void paintComponent(Graphics g)
    {
        //display image
        super.paintComponent(g);

        g.setColor(Color.green);
        g.fillRect(0, 0, 850, 650);

        g.setColor(Color.gray);
        g.fillRect(50, 100, 750, 500);

        Color c1 = Color.green;
        g.setColor( c1 );
        g.fillRect( 150, 200, 550, 300 ); // grass

        Color c2 = Color.black;
        g.setColor( c2 );
        g.drawRect( 50, 100, 750, 500 );  // outer edge
        g.drawRect( 150, 200, 550, 300 ); // inner edge

        Color c3 = Color.yellow;
        g.setColor( c3 );
        g.drawRect( 100, 150, 650, 400 ); // mid-lane marker

        Color c4 = Color.white;
        g.setColor( c4 );
        g.drawLine( 425, 500, 425, 600 ); // start line

        for (Kart racer : racers) //this is new to me, java foreach?
        {
            racer.PaintKart(this, g);
        }

        if (countdown)
        {
            g.setColor(Color.darkGray);
            g.fillRect(lightsPosition[0], lightsPosition[1], 250, 50);

            int lightPosX = lightsPosition[0] + 10;
            int lightPosY = lightsPosition[1] + 10;

            if (!started)
            {
                for (boolean light : lights)
                {
                    if (light)
                    {
                        g.setColor(Color.red);
                        g.fillOval(lightPosX, 335, 30, 30);
                    }
                    else
                    {
                        g.setColor(Color.gray);
                        g.fillOval(lightPosX, 335, 30, 30);
                    }
                    lightPosX += 50;
                }
            }
            else
            {
                for (boolean light : lights)
                {
                    g.setColor(Color.green);
                    g.fillOval(lightPosX, 335, 30, 30);
                    lightPosX += 50;
                }
            }
        }
    }
}
