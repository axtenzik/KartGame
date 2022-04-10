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
    //Swing stuff
    private Timer updateTimer;
    private Timer lightsTimer;
    private JButton resumeButton;
    public JButton exitButton;
    private JButton singleButton;
    private JButton multiButton;

    //menu stuff
    private boolean outOfMenu = false;

    //game running stuff
    private Kart[] racers;
    private boolean paused = false;
    private boolean countdown = true;
    private boolean started = false;
    private boolean multi = true;
    private int downCount = 0;
    private final boolean[] lights = {false, false, false, false, false};
    private final int[] lightsPosition = {300, 325};
    private final int[] displaySize = {850, 650};
    private int numberRacers = 4;
    private int numberAI = numberRacers - 2;
    private final int timeStep = 20;
    private int startPos = 375;

    DisplayPanel()
    {
        setPreferredSize(new Dimension(displaySize[0], displaySize[1]));
        this.setLayout(null);

        InitialiseMenus();

        racers = new Kart[1];
        racers[0] = new Kart("Red", startPos, 550);
    }

    private void InitialiseMenus()
    {
        singleButton = new JButton("Single-player");
        singleButton.setBounds(300, 225, 250, 75);
        singleButton.addActionListener(this);
        singleButton.setVisible(true);
        singleButton.setEnabled(true);
        singleButton.setFocusable(false);
        add(singleButton);

        multiButton = new JButton("Multiplayer");
        multiButton.setBounds(300, 325, 250, 75);
        multiButton.addActionListener(this);
        multiButton.setVisible(true);
        multiButton.setEnabled(true);
        multiButton.setFocusable(false);
        add(multiButton);

        exitButton = new JButton("Exit");
        exitButton.setBounds(300, 425, 250, 75);
        exitButton.addActionListener(this);
        exitButton.setVisible(true);
        exitButton.setEnabled(true);
        exitButton.setFocusable(false);
        add(exitButton);
    }

    private void OpenMenu()
    {
        singleButton.setVisible(true);
        singleButton.setEnabled(true);
        multiButton.setVisible(true);
        multiButton.setEnabled(true);
        exitButton.setVisible(true);
        exitButton.setEnabled(true);
    }

    private void CloseMenu()
    {
        singleButton.setVisible(false);
        singleButton.setEnabled(false);
        multiButton.setVisible(false);
        multiButton.setEnabled(false);
        exitButton.setVisible(false);
        exitButton.setEnabled(false);
    }

    private void InitialisePause()
    {
        resumeButton = new JButton("Resume");
        resumeButton.setBounds(250, 250, 350, 75);
        resumeButton.addActionListener(this);
        resumeButton.setVisible(false);
        resumeButton.setEnabled(false);
        resumeButton.setFocusable(false);
        add(resumeButton);

        exitButton.setBounds(250, 375, 350, 75);
    }

    private void OpenPause()
    {
        updateTimer.stop();
        paused = true;

        resumeButton.setVisible(true);
        resumeButton.setEnabled(true);
        exitButton.setVisible(true);
        exitButton.setEnabled(true);
    }

    private void ClosePause()
    {
        updateTimer.start();
        paused = false;

        resumeButton.setVisible(false);
        resumeButton.setEnabled(false);
        exitButton.setVisible(false);
        exitButton.setEnabled(false);
    }

    private void WinScreen()
    {

    }

    private void StartGame()
    {
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

        TrackHandler.LoadTracks();

        updateTimer = new Timer(timeStep, this::TimerPerformed);
        lightsTimer = new Timer(1000, this::CountdownPerformed);
        lightsTimer.start();
    }

    public void Start()
    {
        StartGame();
        repaint();
    }

    private void Crashed()
    {
        updateTimer.stop();
        paused = true;
    }
    private void Win()
    {
        updateTimer.stop();
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
                case KeyEvent.VK_LEFT:
                    racers[0].setInput(3);
                    break;
                case KeyEvent.VK_RIGHT:
                    racers[0].setInput(1);
                    break;
                case KeyEvent.VK_UP:
                    racers[0].setInput(0);
                    break;
                case KeyEvent.VK_DOWN:
                    racers[0].setInput(2);
                    break;
                case KeyEvent.VK_A:
                    if (multi)
                    {
                        racers[1].setInput(3);
                    }
                    break;
                case KeyEvent.VK_D:
                    if (multi)
                    {
                        racers[1].setInput(1);
                    }
                    break;
                case KeyEvent.VK_W:
                    if (multi)
                    {
                        racers[1].setInput(0);
                    }
                    break;
                case KeyEvent.VK_S:
                    if (multi)
                    {
                        racers[1].setInput(2);
                    }
                    break;
                case KeyEvent.VK_P:
                    OpenPause();
                    break;
            }
        }
        else if (e.getKeyCode() == KeyEvent.VK_P)
        {
            ClosePause();
        }
    }

    public void keyReleased(KeyEvent e)
    {

    }

    public void CountdownPerformed(ActionEvent e)
    {
        if (started)
        {
            lightsTimer.stop();
            countdown = false;
        }
        else if (lights[4])
        {
            started = true;
            updateTimer.start();
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
        //can't use switch with e.getSource() apparently, so if else it is!
        if (e.getSource() == singleButton)
        {
            outOfMenu = true;
            numberAI = 3;
            multi = false;

            CloseMenu();
            InitialisePause();
            StartGame();
            repaint();
        }
        else if(e.getSource() == multiButton)
        {
            outOfMenu = true;

            CloseMenu();
            InitialisePause();
            StartGame();
            repaint();
        }
        else if(e.getSource() == resumeButton)
        {
            ClosePause();
        }
        else if(e.getSource() == exitButton)
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
            System.exit(0);
        }
    }

    public void paintComponent(Graphics g)
    {
        //display image
        super.paintComponent(g);

        if (outOfMenu) {
            g.setColor(Color.green);
            g.fillRect(0, 0, 850, 650);

            g.setColor(Color.gray);
            g.fillRect(50, 100, 750, 500);

            Color c1 = Color.green;
            g.setColor(c1);
            g.fillRect(150, 200, 550, 300); // grass

            Color c2 = Color.black;
            g.setColor(c2);
            g.drawRect(50, 100, 750, 500);  // outer edge
            g.drawRect(150, 200, 550, 300); // inner edge

            Color c3 = Color.yellow;
            g.setColor(c3);
            g.drawRect(100, 150, 650, 400); // mid-lane marker

            Color c4 = Color.white;
            g.setColor(c4);
            g.drawLine(425, 500, 425, 600); // start line

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
                            g.fillOval(lightPosX, lightPosY, 30, 30);
                        }
                        else
                        {
                            g.setColor(Color.gray);
                            g.fillOval(lightPosX, lightPosY, 30, 30);
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
}
