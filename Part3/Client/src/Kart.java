import javax.swing.*;
import java.awt.*;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public class Kart implements Serializable
{
    //kart set variables
    private final int kartFrames = 16;
    public int[] kartPosition = {375, 500};
    public int[] prevKartPos = {375, 500};
    public int kartLapCount = 0;
    public int kartSpeed = 0;
    public int kartAngle = 4;
    protected ImageIcon[] kartImage;
    private int kartLeft = kartFrames - 1;

    //kart related variables
    private final double speedFactor = 0.2; //Needs to be moved to other class at some point so can be used globally
    private int kartAcceleration = 10;

    //AI related variables
    private int AIDelay = 0;
    private int AIWait = 100;
    private int AISpeed = 30;

    /**
     * Method for changing the kart direction
     * @param input 3 is turn left, 1 is right, 0 is forward and 3 is backwards
     */
    public void setInput(int input)
    {
        switch (input)
        {
            case 3 -> kartAngle = (kartAngle + kartLeft) % kartFrames;
            case 1 -> kartAngle = (kartAngle + 1) % kartFrames;
            case 0 -> kartSpeed = Math.min(kartSpeed + kartAcceleration, 100);
            case 2 -> kartSpeed = Math.max(kartSpeed - kartAcceleration, 0);
        }
    }

    /**
     * Constructor for the kart
     * @param name Colour of the kart
     * @param startX Starting x position
     * @param startY Starting Y position
     */
    Kart(String name, int startX, int startY)
    {
        kartImage = new ImageIcon[kartFrames];

        kartPosition[0] = startX;
        kartPosition[1] = startY;
        prevKartPos[0] = kartPosition[0];
        prevKartPos[1] = kartPosition[1];

        //load the images from the resources folder
        for (int i = 0; i < kartImage.length; i++)
        {
            //Path kartPath = Path.of("resources/" + name + i + ".png"); //changed due to .jar not liking it
            //kartImage[i] = new ImageIcon(Objects.requireNonNull(kartPath).toString());
            kartImage[i] = new ImageIcon(Objects.requireNonNull(getClass().getResource(name + i + ".png")));
        }
    }

    /**
     * updates the position of the kart
     */
    private void UpdatePosition()
    {
        //Sets the new co-ordinates based on the speed and angle of the kart
        //due to being on a square grid, the directions other than up/down or left/right have a value multiplied to them
        //this normalises the output so the kart doesn't go faster, magnitude of the vector stays about the same no matter direction
        switch (kartAngle)
        {
            case 0 -> kartPosition[1] = (int)Math.floor(kartPosition[1] - (kartSpeed * speedFactor));
            case 1 -> {
                kartPosition[0] = (int) Math.ceil(kartPosition[0] + (0.383 * kartSpeed * speedFactor));
                kartPosition[1] = (int) Math.floor(kartPosition[1] - (0.924 * kartSpeed * speedFactor));
            }
            case 2 -> {
                kartPosition[0] = (int) Math.ceil(kartPosition[0] + (0.707 * kartSpeed * speedFactor));
                kartPosition[1] = (int) Math.floor(kartPosition[1] - (0.707 * kartSpeed * speedFactor));
            }
            case 3 -> {
                kartPosition[0] = (int) Math.ceil(kartPosition[0] + (0.924 * kartSpeed * speedFactor));
                kartPosition[1] = (int) Math.floor(kartPosition[1] - (0.383 * kartSpeed * speedFactor));
            }
            case 4 -> kartPosition[0] = (int)Math.ceil(kartPosition[0] + (kartSpeed * speedFactor));
            case 5 -> {
                kartPosition[0] = (int) Math.ceil(kartPosition[0] + (0.924 * kartSpeed * speedFactor));
                kartPosition[1] = (int) Math.ceil(kartPosition[1] + (0.383 * kartSpeed * speedFactor));
            }
            case 6 -> {
                kartPosition[0] = (int) Math.ceil(kartPosition[0] + (0.707 * kartSpeed * speedFactor));
                kartPosition[1] = (int) Math.ceil(kartPosition[1] + (0.707 * kartSpeed * speedFactor));
            }
            case 7 -> {
                kartPosition[0] = (int) Math.ceil(kartPosition[0] + (0.383 * kartSpeed * speedFactor));
                kartPosition[1] = (int) Math.ceil(kartPosition[1] + (0.924 * kartSpeed * speedFactor));
            }
            case 8 -> kartPosition[1] = (int)Math.ceil(kartPosition[1] + (kartSpeed * speedFactor));
            case 9 -> {
                kartPosition[0] = (int) Math.floor(kartPosition[0] - (0.383 * kartSpeed * speedFactor));
                kartPosition[1] = (int) Math.ceil(kartPosition[1] + (0.924 * kartSpeed * speedFactor));
            }
            case 10 -> {
                kartPosition[0] = (int) Math.floor(kartPosition[0] - (0.707 * kartSpeed * speedFactor));
                kartPosition[1] = (int) Math.ceil(kartPosition[1] + (0.707 * kartSpeed * speedFactor));
            }
            case 11 -> {
                kartPosition[0] = (int) Math.floor(kartPosition[0] - (0.924 * kartSpeed * speedFactor));
                kartPosition[1] = (int) Math.ceil(kartPosition[1] + (0.383 * kartSpeed * speedFactor));
            }
            case 12 -> kartPosition[0] = (int)Math.floor(kartPosition[0] - (kartSpeed * speedFactor));
            case 13 -> {
                kartPosition[0] = (int) Math.floor(kartPosition[0] - (0.924 * kartSpeed * speedFactor));
                kartPosition[1] = (int) Math.floor(kartPosition[1] - (0.383 * kartSpeed * speedFactor));
            }
            case 14 -> {
                kartPosition[0] = (int) Math.floor(kartPosition[0] - (0.707 * kartSpeed * speedFactor));
                kartPosition[1] = (int) Math.floor(kartPosition[1] - (0.707 * kartSpeed * speedFactor));
            }
            case 15 -> {
                kartPosition[0] = (int) Math.floor(kartPosition[0] - (0.383 * kartSpeed * speedFactor));
                kartPosition[1] = (int) Math.floor(kartPosition[1] - (0.924 * kartSpeed * speedFactor));
            }
        }
    }

    /**
     * basic ai for ai karts
     * @param timeStep how much time has passed since last update
     */
    public void AI(int timeStep)
    {
        AIDelay += timeStep;

        //This gives ai a 'reaction time', just stopes them from doing anything immediately, should probably make a bit random
        if (AIDelay < AIWait)
        {
            return;
        }

        int heading = TrackHandler.GetAIHeading(this);

        if (kartAngle == heading) //go up to AI speed if going in the right direction
        {
            kartSpeed = Math.min(kartSpeed + kartAcceleration, AISpeed);
        }
        else //otherwise, turn left (should add a turn right function in so can choose what would be better)
        {
            AIDelay = 0;
            kartAngle = (kartAngle + kartLeft) % kartFrames;
        }
    }

    /**
     * Updates state of the kart
     * @return eturns true if won
     */
    public boolean UpdateKart()
    {
        UpdatePosition();
        //EvaluateCollisions();
        TrackHandler.CheckCollision(this);
        boolean win = TrackHandler.CheckWin(this);

        //set current position to previous position
        prevKartPos[0] = kartPosition[0];
        prevKartPos[1] = kartPosition[1];

        return win;
    }

    /**
     * Method for painting the kart
     * @param c parent component (where painting is happening)
     * @param g graphics
     */
    public void PaintKart(Component c, Graphics g)
    {
        //added an offset on the cart so its co-ordinate relates to its centre and not its top left corner
        kartImage[kartAngle].paintIcon(c, g, kartPosition[0] - 25, kartPosition[1] - 25);
        //kartImage[kartAngle].paintIcon(c, g, kartPosition[0], kartPosition[1]);
    }
}
