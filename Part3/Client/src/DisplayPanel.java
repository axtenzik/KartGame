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
    private KartFrame kf;
    private Timer updateTimer;
    private Timer lightsTimer;
    private JButton resumeButton;
    public JButton exitButton;
    private JButton singleButton;
    private JButton multiButton;
    private JButton multiSecondButton;
    private JButton onlineButton;
    public JTextField ipBox;
    public JTextField portBox;
    private JButton backButton;
    private JButton connectButton;
    public JLabel waitingLabel;

    //menu stuff
    private boolean outOfMenu = false;

    //game running stuff
    public Kart[] racers;
    public static int player = 0;
    private boolean paused = false;
    private boolean countdown = true;
    private boolean started = false;
    private boolean multi = true;
    private boolean online = false;
    private int downCount = 0;
    private final boolean[] lights = {false, false, false, false, false};
    //private final int[] lightsPosition = {300, 325};
    private final int[] displaySize = {850, 650};
    private int numberRacers = 4;
    private int numberAI = numberRacers - 2;
    private final int timeStep = 20;
    private int startPos = 0;

    DisplayPanel(KartFrame kFrame)
    {
        kf = kFrame;
        setPreferredSize(new Dimension(displaySize[0], displaySize[1]));
        this.setLayout(null);

        //Load menu
        InitialiseMenus();

        //gets changed later, but need to make at least one due to waiting for other players on the online
        //it pains the track and needs to paint a kart
        racers = new Kart[1];
        racers[0] = new Kart("Red", 300, 300); //User kart
    }

    /**
     * Initialise menu buttons for display
     */
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

        multiSecondButton = new JButton("1V1");
        multiSecondButton.setBounds(600, 325, 100, 75);
        multiSecondButton.addActionListener(this);
        multiSecondButton.setVisible(true);
        multiSecondButton.setEnabled(true);
        multiSecondButton.setFocusable(false);
        add(multiSecondButton);

        onlineButton = new JButton("Online play");
        onlineButton.setBounds(300, 425, 250, 75);
        onlineButton.addActionListener(this);
        onlineButton.setVisible(true);
        onlineButton.setEnabled(true);
        onlineButton.setFocusable(false);
        add(onlineButton);

        exitButton = new JButton("Exit");
        exitButton.setBounds(300, 525, 250, 75);
        exitButton.addActionListener(this);
        exitButton.setVisible(true);
        exitButton.setEnabled(true);
        exitButton.setFocusable(false);
        add(exitButton);

        backButton = new JButton("Back");
        backButton.setBounds(300, 525, 250, 75);
        backButton.addActionListener(this);
        backButton.setVisible(false);
        backButton.setEnabled(false);
        backButton.setFocusable(false);
        add(backButton);

        connectButton = new JButton("Connect");
        connectButton.setBounds(300, 425, 250, 75);
        connectButton.addActionListener(this);
        connectButton.setVisible(false);
        connectButton.setEnabled(false);
        connectButton.setFocusable(false);
        add(connectButton);

        ipBox = new JTextField();
        ipBox.setText("localhost");
        ipBox.setBounds(300, 225, 250, 75);
        ipBox.setVisible(false);
        ipBox.setEnabled(false);
        add(ipBox);

        portBox = new JTextField();
        portBox.setText("5000");
        portBox.setBounds(300, 325, 250, 75);
        portBox.setVisible(false);
        portBox.setEnabled(false);
        add(portBox);

        waitingLabel = new JLabel("Waiting for player 2");
        waitingLabel.setBounds(300, 225, 250, 75);
        waitingLabel.setVisible(false);
        add(waitingLabel);
    }

    /**
     * Method for opening the main menu
     */
    private void OpenMenu()
    {
        singleButton.setVisible(true);
        singleButton.setEnabled(true);
        multiButton.setVisible(true);
        multiButton.setEnabled(true);
        multiSecondButton.setVisible(true);
        multiSecondButton.setEnabled(true);
        onlineButton.setVisible(true);
        onlineButton.setEnabled(true);
        exitButton.setVisible(true);
        exitButton.setEnabled(true);
    }

    /**
     * Method for closing the main menu
     */
    private void CloseMenu()
    {
        singleButton.setVisible(false);
        singleButton.setEnabled(false);
        multiButton.setVisible(false);
        multiButton.setEnabled(false);
        multiSecondButton.setVisible(false);
        multiSecondButton.setEnabled(false);
        onlineButton.setVisible(false);
        onlineButton.setEnabled(false);
        exitButton.setVisible(false);
        exitButton.setEnabled(false);
    }

    /**
     * Method for opening the online menu
     */
    private void OpenOnline()
    {
        backButton.setVisible(true);
        backButton.setEnabled(true);
        connectButton.setVisible(true);
        connectButton.setEnabled(true);
        ipBox.setVisible(true);
        ipBox.setEnabled(true);
        portBox.setVisible(true);
        portBox.setEnabled(true);
    }

    /**
     * Method for closing the online menu
     */
    private void CloseOnline()
    {
        backButton.setVisible(false);
        backButton.setEnabled(false);
        connectButton.setVisible(false);
        connectButton.setEnabled(false);
        ipBox.setVisible(false);
        ipBox.setEnabled(false);
        portBox.setVisible(false);
        portBox.setEnabled(false);
    }

    /**
     * Create and set up the buttons used in the pause menu
     */
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

    /**
     * Method for opening the pause menu
     */
    private void OpenPause()
    {
        if (!online)
        {
            //stop the timer so game freezes when paused
            updateTimer.stop();
        }
        paused = true;

        resumeButton.setVisible(true);
        resumeButton.setEnabled(true);
        exitButton.setVisible(true);
        exitButton.setEnabled(true);
    }

    /**
     * Method for closing the pause menu
     */
    private void ClosePause()
    {
        if (!online)
        {
            //start the timer so the game runs
            updateTimer.start();
        }
        paused = false;

        resumeButton.setVisible(false);
        resumeButton.setEnabled(false);
        exitButton.setVisible(false);
        exitButton.setEnabled(false);
    }

    /**
     * Display the win screen
     */
    private void WinScreen()
    {

    }

    /**
     * Method for starting the game
     */
    private void StartGame()
    {
        //Initialise the timers for play
        updateTimer = new Timer(timeStep, this::TimerPerformed);
        lightsTimer = new Timer(1000, this::CountdownPerformed);
        lightsTimer.start();

        if (online)
        {
            SocketSender.SendOwnKart();
        }
    }

    /**
     * Method called by socket for starting the game
     */
    public void Start()
    {
        StartGame();
        repaint();
    }

    public void addKarts()
    {
        //Create the karts, First creates the AI karts in green, then player 2 in blue, then player 1 in red
        racers = new Kart[numberRacers];
        for (int i = numberRacers - numberAI; i < racers.length; i++)
        {
            racers[i] = new Kart("Green", TrackHandler.GetStartLineX() + startPos, TrackHandler.GetStartLineY());
            startPos -= 60; //Moves start position of the kart along so they don't start in the same place
        }
        for (int i = 1; i < numberRacers - numberAI; i++)
        {
            //Multiple player 2 karts are possible but only 1 will be human drivable currently
            //made purposely so can be scaled with blue karts being human driven karts
            racers[i] = new Kart("Blue", TrackHandler.GetStartLineX() + startPos, TrackHandler.GetStartLineY());
            startPos -= 60;
        }
        racers[0] = new Kart("Red", TrackHandler.GetStartLineX() + startPos, TrackHandler.GetStartLineY()); //User kart
    }

    /**
     * Method for handling game over from crashing with another kart
     */
    public void Crashed()
    {
        updateTimer.stop();
        paused = true;
    }

    /**
     * Method called when a player has won the game
     */
    public void Win()
    {
        updateTimer.stop();
        paused = true;
    }

    public void keyTyped(KeyEvent e)
    {
        //needed for key listener
    }

    public void keyPressed(KeyEvent e)
    {
        //Ignore key presses when the game hasn't started
        if (!started)
        {
            return;
        }

        //controls for when not paused and playing the game
        if (!paused)
        {
            switch (e.getKeyCode())
            {
                case KeyEvent.VK_LEFT:
                    racers[player].setInput(3);
                    break;
                case KeyEvent.VK_RIGHT:
                    racers[player].setInput(1);
                    break;
                case KeyEvent.VK_UP:
                    racers[player].setInput(0);
                    break;
                case KeyEvent.VK_DOWN:
                    racers[player].setInput(2);
                    break;
                case KeyEvent.VK_A:
                    if (multi)
                    {
                        racers[1].setInput(3);
                    }
                    break;
                case KeyEvent.VK_D:
                    if (multi) //if playing local multiplayer and not single-player, let player 2 control second kart
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
        //needed for key listener
    }

    public void CountdownPerformed(ActionEvent e)
    {
        if (started)
        {
            //Stop lights timer from ticking
            lightsTimer.stop();
            countdown = false; //set to false so the go lights stop being drawn
        }
        else if (lights[4])
        {
            //Starts the game running
            started = true;
            updateTimer.start();
        }
        else
        {
            //Update which light is on
            lights[downCount] = true;
            downCount++;
        }
        repaint();
    }

    /**
     * Method called when the game update timer ticks
     * @param e
     */
    public void TimerPerformed(ActionEvent e)
    {
        //iterate through the AI players and get the AI to update their input
        for (int i = numberRacers - numberAI; i < racers.length; i++)
        {
            racers[i].AI(timeStep);
        }

        //go through the karts and update their position
        for (Kart racer : racers)
        {
            if (racer.UpdateKart())
            {
                Win();
            }
        }

        //compare kart positions to see if they have collided with each other
        for (int i = 0; i < numberRacers; i++)
        {
            for (int j = 0; j < numberRacers; j++)
            {
                if (i != j) //as long as the two karts selected isn't the same kart
                {
                    //take the 2 kart positions and take one from the other, then absolute the value to get it positive,
                    //then see if it's below a set variable to see if the karts are too close to each other
                    if (Math.abs(racers[i].kartPosition[0] - racers[j].kartPosition[0]) < 20 && Math.abs(racers[i].kartPosition[1] - racers[j].kartPosition[1]) < 20)
                    {
                        if (online)
                        {
                            SocketSender.SendMessage("collision");
                        }
                        Crashed();
                    }
                }
            }
        }
        if (online)
        {
            SocketSender.SendOwnKart();
        }

        repaint();
    }

    public void actionPerformed(ActionEvent e)
    {
        //can't use switch with e.getSource() apparently, so if else it is!
        if (e.getSource() == singleButton) //Sets up single-player
        {
            outOfMenu = true;
            numberAI = 3;
            multi = false;
            TrackHandler.LoadTracks(0);
            addKarts();

            CloseMenu();
            InitialisePause();
            StartGame();
            repaint();
        }
        else if(e.getSource() == multiButton) //sets up multiplayer
        {
            outOfMenu = true;
            //TrackHandler.LoadTracks(0);
            TrackHandler.LoadTracks(0);
            addKarts();

            CloseMenu();
            InitialisePause();
            StartGame();
            repaint();
        }
        else if(e.getSource() == multiSecondButton) //sets up multiplayer
        {
            outOfMenu = true;
            numberRacers = 2;
            numberAI = 0;
            //TrackHandler.LoadTracks(0);
            TrackHandler.LoadTracks(1);
            addKarts();
            setPreferredSize(new Dimension(TrackHandler.GetSizeX(), TrackHandler.GetSizeY()));
            kf.pack();

            CloseMenu();
            InitialisePause();
            StartGame();
            repaint();
        }
        else if(e.getSource() == onlineButton) //opens online menu
        {
            CloseMenu();
            OpenOnline();
        }
        else if(e.getSource() == backButton) //opens main menu
        {
            CloseOnline();
            OpenMenu();
        }
        else if(e.getSource() == connectButton) //connect to the server specified
        {
            outOfMenu = true;
            numberRacers = 2;
            numberAI = 0;
            multi = false;
            online = true;
            TrackHandler.LoadTracks(0);
            addKarts();

            CloseOnline();
            InitialisePause();

            repaint();

            SocketSender sender = new SocketSender();
            Thread t = new Thread(sender);
            SocketSender.displayPanel = this;
            t.start();
        }
        else if(e.getSource() == resumeButton) //closes the pause menu
        {
            ClosePause();
        }
        else if(e.getSource() == exitButton) //exit the program
        {
            if (online)
            {
                SocketSender.SendMessage("CLOSE");
            }
            CloseClient();
        }
    }

    /**
     * Method for closing the client
     */
    public void CloseClient()
    {
        if (outOfMenu) //has to be out of menu otherwise tracks.json = null
        {
            try
            {
                //hope this works in the .jar, had to get rid of other path because they weren't working
                //try saving the tracks as a JSON in resources folder
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
        }
        System.exit(0);
    }

    public void paintComponent(Graphics g)
    {
        //display image
        super.paintComponent(g);

        if (outOfMenu) //when playing the game
        {
            TrackHandler.PaintTrack(g);

            //draws all the karts
            for (Kart racer : racers)
            {
                racer.PaintKart(this, g);
            }

            //draws the start countdown lights
            if (countdown)
            {
                //draw the background for the lights
                g.setColor(Color.darkGray);
                g.fillRect(TrackHandler.GetLightX(), TrackHandler.GetLightY(), 250, 50);

                //variables for positioning the lights
                int lightPosX = TrackHandler.GetLightX() + 10;
                int lightPosY = TrackHandler.GetLightY() + 10;

                if (!started) //before game start
                {
                    for (boolean light : lights)
                    {
                        if (light) //light turned on
                        {
                            g.setColor(Color.red);
                            g.fillOval(lightPosX, lightPosY, 30, 30);
                        }
                        else //light is off
                        {
                            g.setColor(Color.gray);
                            g.fillOval(lightPosX, lightPosY, 30, 30);
                        }
                        lightPosX += 50;
                    }
                }
                else //once games starts lights turn green
                {
                    for (boolean light : lights) //stop telling me I don't use light, I know!
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