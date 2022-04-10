import javax.swing.*;
import java.awt.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public class Kart
{
    private final int kartFrames = 16;
    public final int[] kartPosition = {375, 500};
    public final int[] prevKartPos = {375, 500};
    public int kartLapCount = 0;
    public int kartSpeed = 0;
    private int kartAngle = 4;
    protected ImageIcon[] kartImage;
    private int kartLeft = kartFrames - 1;

    //kart related variables
    private final double speedFactor = 0.2; //Needs to be moved to other class at some point so can be used globally
    private int kartAcceleration = 10;

    //AI related variables
    private int AIDelay = 0;
    private int AIWait = 100;
    private int AISpeed = 30;

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

    Kart(String name, int startX, int startY)
    {
        kartImage = new ImageIcon[kartFrames];

        kartPosition[0] = startX;
        kartPosition[1] = startY;
        prevKartPos[0] = kartPosition[0];
        prevKartPos[1] = kartPosition[1];

        for (int i = 0; i < kartImage.length; i++)
        {
            //Path kartPath = Path.of("resources/" + name + i + ".png");
            //kartImage[i] = new ImageIcon(Objects.requireNonNull(kartPath).toString());
            kartImage[i] = new ImageIcon(Objects.requireNonNull(getClass().getResource(name + i + ".png")));
        }
    }

    private void UpdatePosition()
    {
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

    private void EvaluateCollisions()
    {
        if (760 < kartPosition[0] || kartPosition[0] < 40)
        {
            kartSpeed = 0;
            kartPosition[0] = Math.max(40, Math.min(760, kartPosition[0]));
        }
        if (560 < kartPosition[1] || kartPosition[1] < 90)
        {
            kartSpeed = 0;
            kartPosition[1] = Math.max(90, Math.min(560, kartPosition[1]));
        }

        if (490 <= prevKartPos[1] && prevKartPos[1] <= 560)
        {
            if (prevKartPos[0] <= 425 && kartPosition[0] > 425)
            {
                kartLapCount += 1;
                System.out.println(kartLapCount);
            }
            else if (prevKartPos[0] >= 425 && kartPosition[0] < 425)
            {
                kartLapCount -= 1;
                System.out.println(kartLapCount);
            }
            if (!(490 <= kartPosition[1] && kartPosition[1] <= 560) && 110 < kartPosition[0] && kartPosition[0] < 690)
            {
                kartSpeed = 0;
                kartPosition[1] = Math.max(490, kartPosition[1]);
            }
        }
        else if (690 <= prevKartPos[0] && prevKartPos[0] <= 760)
        {
            if (!(690 <= kartPosition[0]) && 160 < kartPosition[1] && kartPosition[1] < 490)
            {
                kartSpeed = 0;
                kartPosition[0] = 690;
            }
        }
        else if (90 <= prevKartPos[1] && prevKartPos[1] <= 160)
        {
            if(!(kartPosition[1] <= 160) && 110 < kartPosition[0] && kartPosition[0] < 690)
            {
                kartSpeed = 0;
                kartPosition[1] = 160;
            }
        }
        else if (40 <= prevKartPos[0] && prevKartPos[0] <= 110)
        {
            if (!(kartPosition[0] <= 110) && 160 < kartPosition[1] && kartPosition[1] < 490)
            {
                kartSpeed = 0;
                kartPosition[0] = 110;
            }
        }
        else
        {
            System.out.println("Why is this triggering?");
        }
        prevKartPos[0] = kartPosition[0];
        prevKartPos[1] = kartPosition[1];
    }

    public void AI(int timeStep)
    {
        AIDelay += timeStep;

        if (AIDelay < AIWait)
        {
            return;
        }

        int heading = TrackHandler.getAIHeading(this);

        if (kartAngle == heading)
        {
            kartSpeed = Math.min(kartSpeed + kartAcceleration, AISpeed);
        }
        else
        {
            AIDelay = 0;
            kartAngle = (kartAngle + kartLeft) % kartFrames;
        }

        /*
        if (500 <= kartPosition[1] && kartPosition[1] <= 560 && kartPosition[0] < 700)
        {
            if (kartAngle == 4)
            {
                kartSpeed = Math.min(kartSpeed + kartAcceleration, AISpeed);
            }
            else
            {
                AIDelay = 0;
                kartAngle = (kartAngle + kartLeft) % kartFrames;
            }
        }
        else if (700 <= kartPosition[0] && kartPosition[0] <= 760 && kartPosition[1] > 150)
        {
            if (kartAngle == 0)
            {
                kartSpeed = Math.min(kartSpeed + kartAcceleration, AISpeed);
            }
            else
            {
                AIDelay = 0;
                kartAngle = (kartAngle + kartLeft) % kartFrames;
            }
        }
        else if (90 <= kartPosition[1] && kartPosition[1] <= 150 && kartPosition[0] > 100)
        {
            if (kartAngle == 12)
            {
                kartSpeed = Math.min(kartSpeed + kartAcceleration, AISpeed);
            }
            else
            {
                AIDelay = 0;
                kartAngle = (kartAngle + kartLeft) % kartFrames;
            }
        }
        else if (40 <= kartPosition[0] && kartPosition[0] <= 100 && kartPosition[1] < 500)
        {
            if (kartAngle == 8)
            {
                kartSpeed = Math.min(kartSpeed + kartAcceleration, AISpeed);
            }
            else
            {
                AIDelay = 0;
                kartAngle = (kartAngle + kartLeft) % kartFrames;
            }
        }*/
    }

    public boolean UpdateKart()
    {
        UpdatePosition();
        //EvaluateCollisions();
        TrackHandler.CheckCollision(this);
        boolean win = TrackHandler.CheckWin(this);

        prevKartPos[0] = kartPosition[0];
        prevKartPos[1] = kartPosition[1];

        return win;
    }

    public void PaintKart(Component c, Graphics g)
    {
        kartImage[kartAngle].paintIcon(c, g, kartPosition[0] - 25, kartPosition[1] - 25);
        //kartImage[kartAngle].paintIcon(c, g, kartPosition[0], kartPosition[1]);
    }
}
