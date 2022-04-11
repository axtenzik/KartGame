import java.awt.*;
import java.util.ArrayList;
import java.nio.file.Files;
import java.nio.file.Path;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class TrackHandler
{
    public static Circuits circuits;
    private static int boundaryOffset = 10; // set to 0 for no clipping issue, at 0 half the kart can go off the track though
    private static int selectedTrack = 1;

    /**
     * method for loading the track data
     */
    public static void LoadTracks(int trackID)
    {
        selectedTrack = trackID;

        try
        {
            Path trackPath = Path.of("resources/tracks.json"); //doesn't like working in .jar
            String trackString = Files.readString(trackPath);
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();

            //String trackString = TrackHandler.class.getResource("tracks.json").getPath(); //works with .jar
            circuits = gson.fromJson(trackString, Circuits.class);
        }
        catch (Exception e)
        {
            System.out.println(e.toString());
        }

        if (circuits == null) //if no track data was loaded, load same track but made programmatically
        {
            CreateTrack();
        }
    }

    public static int GetLightX()
    {
        return circuits.tracks.get(selectedTrack).lightsPosition[0];
    }

    public static int GetLightY()
    {
        return circuits.tracks.get(selectedTrack).lightsPosition[1];
    }

    public static int GetSizeX()
    {
        //Hard coded for now
        return circuits.tracks.get(selectedTrack).trackSize[0];
    }

    public static int GetSizeY()
    {
        //Hard coded for now
        return circuits.tracks.get(selectedTrack).trackSize[1];
    }

    public static int GetStartLineX()
    {
        //Hard coded for now
        return circuits.tracks.get(selectedTrack).startFinish;
    }

    public static int GetStartLineY()
    {
        //Hard codoed for now
        return (circuits.tracks.get(selectedTrack).rectangles.get(0).boundsY[1] + circuits.tracks.get(selectedTrack).rectangles.get(0).boundsY[0]) / 2;
    }

    /**
     * Method for getting the forward direction for that part of the track
     * @param kart the kart wth the ai on
     * @return direction ai needs to head in
     */
    public static int GetAIHeading(Kart kart)
    {
        for (TrackRectangles rectangles : circuits.tracks.get(selectedTrack).rectangles)
        {
            if ((rectangles.boundsX[0] <= kart.kartPosition[0] && kart.kartPosition[0] <= rectangles.boundsX[1]) && (rectangles.boundsY[0] <= kart.kartPosition[1] && kart.kartPosition[1] <= rectangles.boundsY[1]))
            {
                return rectangles.forward;
            }
        }

        return 0; //hopefully shouldn't ever hit here, but if it does ai will just head up
    }

    /**
     * Method for updating the lap count for the kart and to see if it has won
     * @param kart kart that is check to see if its won
     * @return
     */
    public static boolean CheckWin(Kart kart)
    {
        //start finish line always in first rectangle, check to see if kart was in said rectangle
        if ((circuits.tracks.get(selectedTrack).rectangles.get(0).boundsX[0] <= kart.prevKartPos[0] && kart.prevKartPos[0] <= circuits.tracks.get(selectedTrack).rectangles.get(0).boundsX[1]) && (circuits.tracks.get(selectedTrack).rectangles.get(0).boundsY[0] <= kart.prevKartPos[1] && kart.prevKartPos[1] <= circuits.tracks.get(selectedTrack).rectangles.get(0).boundsY[1]))
        {
            //get the direction needed to cross start finish line
            //0 is heading upward
            //1 is going right
            //2 is going down
            //3 is going left
            switch (circuits.tracks.get(selectedTrack).finishDirection)
            {
                case 0:
                    //if kart was below the line for the start finish line, and is now above said line
                    if (kart.prevKartPos[1] >= circuits.tracks.get(0).startFinish && kart.kartPosition[1] < circuits.tracks.get(0).startFinish)
                    {
                        kart.kartLapCount += 1; //add one lap
                    }
                    else if (kart.prevKartPos[1] <= circuits.tracks.get(0).startFinish && kart.kartPosition[1] < circuits.tracks.get(0).startFinish)
                    {
                        kart.kartLapCount -= 1; //take one lap if went through the line in the opposite direction
                    }
                    break;
                case 1:
                    if (kart.prevKartPos[0] <= circuits.tracks.get(0).startFinish && kart.kartPosition[0] > circuits.tracks.get(0).startFinish)
                    {
                        kart.kartLapCount += 1;
                        //System.out.println(kart.kartLapCount);
                    }
                    else if (kart.prevKartPos[0] >= circuits.tracks.get(0).startFinish && kart.kartPosition[0] < circuits.tracks.get(0).startFinish)
                    {
                        kart.kartLapCount -= 1;
                        //System.out.println(kart.kartLapCount);
                    }
                    break;
                case 2:
                    if (kart.prevKartPos[1] >= circuits.tracks.get(0).startFinish && kart.kartPosition[1] < circuits.tracks.get(0).startFinish)
                    {
                        kart.kartLapCount -= 1;
                    }
                    else if (kart.prevKartPos[1] <= circuits.tracks.get(0).startFinish && kart.kartPosition[1] < circuits.tracks.get(0).startFinish)
                    {
                        kart.kartLapCount += 1;
                    }
                    break;
                case 3:
                    if (kart.prevKartPos[0] <= circuits.tracks.get(0).startFinish && kart.kartPosition[0] > circuits.tracks.get(0).startFinish)
                    {
                        kart.kartLapCount -= 1;
                    }
                    else if (kart.prevKartPos[0] >= circuits.tracks.get(0).startFinish && kart.kartPosition[0] < circuits.tracks.get(0).startFinish)
                    {
                        kart.kartLapCount += 1;
                    }
                    break;
            }
        }
        return kart.kartLapCount > circuits.tracks.get(selectedTrack).totalLaps;
    }

    /**
     * Method for checking if the kart has hit a wall
     * @param kart the kart being checked
     */
    public static void CheckCollision(Kart kart)
    {
        for (TrackRectangles rectangles : circuits.tracks.get(selectedTrack).rectangles)
        {
            //check to see if kart was in the rectangle
            if ((rectangles.boundsX[0] <= kart.prevKartPos[0] && kart.prevKartPos[0] <= rectangles.boundsX[1]) && (rectangles.boundsY[0] <= kart.prevKartPos[1] && kart.prevKartPos[1] <= rectangles.boundsY[1]))
            {
                //walls are true if they can stop the kart, false if the can't
                //walls[0] is the top edge of the bounding rectangle
                if (rectangles.walls[0])
                {
                    //if kart position is now above the top edge
                    if (kart.kartPosition[1] < rectangles.boundsY[0] + boundaryOffset)
                    {
                        //stop kart and update y position to the top edge y value
                        kart.kartSpeed = 0;
                        kart.kartPosition[1] = rectangles.boundsY[0] + boundaryOffset;
                    }
                }

                if (rectangles.walls[1])
                {
                    if (rectangles.boundsX[1] - boundaryOffset < kart.kartPosition[0])
                    {
                        kart.kartSpeed = 0;
                        kart.kartPosition[0] = rectangles.boundsX[1] - boundaryOffset;
                    }
                }
                if (rectangles.walls[2])
                {
                    if (rectangles.boundsY[1] - boundaryOffset < kart.kartPosition[1])
                    {
                        kart.kartSpeed = 0;
                        kart.kartPosition[1] = rectangles.boundsY[1] - boundaryOffset;
                    }
                }
                if (rectangles.walls[3])
                {
                    if (kart.kartPosition[0] < rectangles.boundsX[0] + boundaryOffset)
                    {
                        kart.kartSpeed = 0;
                        kart.kartPosition[0] = rectangles.boundsX[0] + boundaryOffset;
                    }
                }
            }
        }
    }

    /**
     * Paints the track based on the track input data
     * @param g
     */
    public static void PaintTrack(Graphics g)
    {
        g.setColor(Color.green);
        g.fillRect(0, 0, circuits.tracks.get(selectedTrack).trackSize[0], circuits.tracks.get(selectedTrack).trackSize[1]);

        for (TrackRectangles rectangles : circuits.tracks.get(selectedTrack).rectangles)
        {
            g.setColor(Color.gray);
            g.fillRect(rectangles.boundsX[0], rectangles.boundsY[0], rectangles.boundsX[1] - rectangles.boundsX[0], rectangles.boundsY[1] - rectangles.boundsY[0]);

            int[] rectCentre = new int[2];
            rectCentre[0] = (rectangles.boundsX[1] + rectangles.boundsX[0]) / 2;
            rectCentre[1] = (rectangles.boundsY[1] + rectangles.boundsY[0]) / 2;

            Color c1 = Color.black;
            Color c2 = Color.yellow;

            if (rectangles.walls[0])
            {
                g.setColor(c1);
                g.drawLine(rectangles.boundsX[0], rectangles.boundsY[0], rectangles.boundsX[1], rectangles.boundsY[0]);
            }
            else
            {
                g.setColor(c2);
                g.drawLine(rectCentre[0], rectangles.boundsY[0], rectCentre[0], rectCentre[1]);
            }

            if (rectangles.walls[1])
            {
                g.setColor(c1);
                g.drawLine(rectangles.boundsX[1], rectangles.boundsY[0], rectangles.boundsX[1], rectangles.boundsY[1]);
            }
            else
            {
                g.setColor(c2);
                g.drawLine(rectCentre[0], rectCentre[1], rectangles.boundsX[1], rectCentre[1]);
            }

            if (rectangles.walls[2])
            {
                g.setColor(c1);
                g.drawLine(rectangles.boundsX[0], rectangles.boundsY[1], rectangles.boundsX[1], rectangles.boundsY[1]);
            }
            else
            {
                g.setColor(c2);
                g.drawLine(rectCentre[0], rectCentre[1], rectCentre[0], rectangles.boundsY[1]);
            }

            if (rectangles.walls[3])
            {
                g.setColor(c1);
                g.drawLine(rectangles.boundsX[0], rectangles.boundsY[0], rectangles.boundsX[0], rectangles.boundsY[1]);
            }
            else
            {
                g.setColor(c2);
                g.drawLine(rectangles.boundsX[0], rectCentre[1], rectCentre[0], rectCentre[1]);
            }
        }

        g.setColor(Color.white);
        if (circuits.tracks.get(selectedTrack).finishDirection == 0 || circuits.tracks.get(selectedTrack).finishDirection == 2)
        {
            g.drawLine(circuits.tracks.get(selectedTrack).rectangles.get(0).boundsX[0], circuits.tracks.get(selectedTrack).startFinish, circuits.tracks.get(selectedTrack).rectangles.get(0).boundsX[1], circuits.tracks.get(selectedTrack).startFinish);
        }
        else if (circuits.tracks.get(selectedTrack).finishDirection == 1 || circuits.tracks.get(selectedTrack).finishDirection == 3)
        {
            g.drawLine(circuits.tracks.get(selectedTrack).startFinish, circuits.tracks.get(selectedTrack).rectangles.get(0).boundsY[0], circuits.tracks.get(selectedTrack).startFinish, circuits.tracks.get(selectedTrack).rectangles.get(0).boundsY[1]);
        }

    }

    /**
     * Method for creating basic rectangle track in case the json file couldn't be read or was null
     */
    public static void CreateTrack()
    {
        circuits = new Circuits();
        circuits.tracks = new ArrayList<>();
        circuits.tracks.add(new TrackDetails());
        circuits.tracks.get(0).trackName = "Track1";
        circuits.tracks.get(0).trackSize = new int[]{850, 650};
        circuits.tracks.get(0).totalLaps = 5;
        circuits.tracks.get(0).startFinish = 425;
        circuits.tracks.get(0).finishDirection = 1;
        circuits.tracks.get(0).lightsPosition = new int[]{300, 325};
        circuits.tracks.get(0).rectangles = new ArrayList<>();
        circuits.tracks.get(0).rectangles.add(new TrackRectangles());
        circuits.tracks.get(0).rectangles.get(0).boundsX = new int[]{150, 700};
        circuits.tracks.get(0).rectangles.get(0).boundsY = new int[]{500, 600};
        circuits.tracks.get(0).rectangles.get(0).forward = 4;
        circuits.tracks.get(0).rectangles.get(0).walls = new boolean[]{true, false, true, false};

        circuits.tracks.get(0).rectangles.add(new TrackRectangles());
        circuits.tracks.get(0).rectangles.get(1).boundsX = new int[]{700, 800};
        circuits.tracks.get(0).rectangles.get(1).boundsY = new int[]{500, 600};
        circuits.tracks.get(0).rectangles.get(1).forward = 0;
        circuits.tracks.get(0).rectangles.get(1).walls = new boolean[]{false, true, true, false};

        circuits.tracks.get(0).rectangles.add(new TrackRectangles());
        circuits.tracks.get(0).rectangles.get(2).boundsX = new int[]{700, 800};
        circuits.tracks.get(0).rectangles.get(2).boundsY = new int[]{200, 500};
        circuits.tracks.get(0).rectangles.get(2).forward = 0;
        circuits.tracks.get(0).rectangles.get(2).walls = new boolean[]{false, true, false, true};

        circuits.tracks.get(0).rectangles.add(new TrackRectangles());
        circuits.tracks.get(0).rectangles.get(3).boundsX = new int[]{700, 800};
        circuits.tracks.get(0).rectangles.get(3).boundsY = new int[]{100, 200};
        circuits.tracks.get(0).rectangles.get(3).forward = 12;
        circuits.tracks.get(0).rectangles.get(3).walls = new boolean[]{true, true, false, false};

        circuits.tracks.get(0).rectangles.add(new TrackRectangles());
        circuits.tracks.get(0).rectangles.get(4).boundsX = new int[]{150, 700};
        circuits.tracks.get(0).rectangles.get(4).boundsY = new int[]{100, 200};
        circuits.tracks.get(0).rectangles.get(4).forward = 12;
        circuits.tracks.get(0).rectangles.get(4).walls = new boolean[]{true, false, true, false};

        circuits.tracks.get(0).rectangles.add(new TrackRectangles());
        circuits.tracks.get(0).rectangles.get(5).boundsX = new int[]{50, 150};
        circuits.tracks.get(0).rectangles.get(5).boundsY = new int[]{100, 200};
        circuits.tracks.get(0).rectangles.get(5).forward = 8;
        circuits.tracks.get(0).rectangles.get(5).walls = new boolean[]{true, false, false, true};

        circuits.tracks.get(0).rectangles.add(new TrackRectangles());
        circuits.tracks.get(0).rectangles.get(6).boundsX = new int[]{50, 150};
        circuits.tracks.get(0).rectangles.get(6).boundsY = new int[]{200, 500};
        circuits.tracks.get(0).rectangles.get(6).forward = 8;
        circuits.tracks.get(0).rectangles.get(6).walls = new boolean[]{false, true, false, true};

        circuits.tracks.get(0).rectangles.add(new TrackRectangles());
        circuits.tracks.get(0).rectangles.get(7).boundsX = new int[]{50, 150};
        circuits.tracks.get(0).rectangles.get(7).boundsY = new int[]{500, 600};
        circuits.tracks.get(0).rectangles.get(7).forward = 4;
        circuits.tracks.get(0).rectangles.get(7).walls = new boolean[]{false, false, true, true};
    }
}
