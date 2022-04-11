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

        //Load menu
        InitialiseMenus();
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

        exitButton = new JButton("Exit");
        exitButton.setBounds(300, 425, 250, 75);
        exitButton.addActionListener(this);
        exitButton.setVisible(true);
        exitButton.setEnabled(true);
        exitButton.setFocusable(false);
        add(exitButton);
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
        exitButton.setVisible(false);
        exitButton.setEnabled(false);
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
        //stop the timer so game freezes when paused
        updateTimer.stop();
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
        //start the timer so the game runs
        updateTimer.start();
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
        //Create the karts, First creates the AI karts in green, then player 2 in blue, then player 1 in red
        racers = new Kart[numberRacers];
        for (int i = numberRacers - numberAI; i < racers.length; i++)
        {
            racers[i] = new Kart("Green", startPos, 550);
            startPos -= 60; //Moves start position of the kart along so they don't start in the same place
        }
        for (int i = 1; i < numberRacers - numberAI; i++)
        {
            //Multiple player 2 karts are possible but only 1 will be human drivable currently
            //made purposely so can be scaled with blue karts being human driven karts
            racers[i] = new Kart("Blue", startPos, 550);
            startPos -= 60;
        }
        racers[0] = new Kart("Red", startPos, 550); //User kart

        //Load/Create the track to be driven on
        TrackHandler.LoadTracks();

        //Initialise the timers for play
        updateTimer = new Timer(timeStep, this::TimerPerformed);
        lightsTimer = new Timer(1000, this::CountdownPerformed);
        lightsTimer.start();
    }

    /**
     * Method for handling game over from crashing with another kart
     */
    private void Crashed()
    {
        updateTimer.stop();
        paused = true;
    }

    /**
     * Method called when a player has won the game
     */
    private void Win()
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
                    if (multi) //if playing local multiplayer and not single-player, let player 2 control second kart
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
        else if (e.getKeyCode() == KeyEvent.VK_P) //know at this point the user is in game but is currently paused
        {
            ClosePause();
        }
    }

    public void keyReleased(KeyEvent e)
    {
        //needed for key listener
    }

    /**
     * Method called when the lights timer ticks
     * @param e
     */
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
                        Crashed();
                    }
                }
            }
        }
        repaint();
    }

    /**
     * Method called when a button is pressed
     * @param e
     */
    public void actionPerformed(ActionEvent e)
    {
        //can't use switch with e.getSource() apparently, so if else it is!
        if (e.getSource() == singleButton) //Sets up single-player
        {
            outOfMenu = true;
            numberAI = 3;
            multi = false;

            CloseMenu();
            InitialisePause();
            StartGame();
            repaint();
        }
        else if(e.getSource() == multiButton) //sets up multiplayer
        {
            outOfMenu = true;

            CloseMenu();
            InitialisePause();
            StartGame();
            repaint();
        }
        else if(e.getSource() == resumeButton) //closes the pause menu
        {
            ClosePause();
        }
        else if(e.getSource() == exitButton) //exit the program
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
    }

    public void paintComponent(Graphics g)
    {
        //display image
        super.paintComponent(g);

        if (outOfMenu) //when playing the game
        {
            //Code from part 2 of the assignment
            //draws the track to be raced on
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
                g.fillRect(lightsPosition[0], lightsPosition[1], 250, 50);

                //variables for positioning the lights
                int lightPosX = lightsPosition[0] + 10;
                int lightPosY = lightsPosition[1] + 10;

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
