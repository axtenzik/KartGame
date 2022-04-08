import java.util.ArrayList;
import java.nio.file.Files;
import java.nio.file.Path;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class TrackHandler
{
    public static Circuits circuits;
    private static int boundaryOffset = 0; // set to 0 for no clipping issue, at 0 half the kart can go off the track though

    public static void LoadTracks()
    {
        try
        {
            Path trackPath = Path.of("resources/tracks.json");
            String trackString = Files.readString(trackPath);
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            circuits = gson.fromJson(trackString, Circuits.class);
        }
        catch (Exception e)
        {
            System.out.println(e.toString());
        }
    }

    public static int getAIHeading(Kart kart)
    {
        for (TrackRectangles rectangles : circuits.tracks.get(0).rectangles)
        {
            if ((rectangles.boundsX[0] <= kart.kartPosition[0] && kart.kartPosition[0] <= rectangles.boundsX[1]) && (rectangles.boundsY[0] <= kart.kartPosition[1] && kart.kartPosition[1] <= rectangles.boundsY[1]))
            {
                return rectangles.forward;
            }
        }

        return 0;
    }

    public static boolean CheckWin(Kart kart)
    {
        if ((circuits.tracks.get(0).rectangles.get(0).boundsX[0] <= kart.prevKartPos[0] && kart.prevKartPos[0] <= circuits.tracks.get(0).rectangles.get(0).boundsX[1]) && (circuits.tracks.get(0).rectangles.get(0).boundsY[0] <= kart.prevKartPos[1] && kart.prevKartPos[1] <= circuits.tracks.get(0).rectangles.get(0).boundsY[1]))
        {
            switch (circuits.tracks.get(0).finishDirection)
            {
                case 0:
                    if (kart.prevKartPos[1] >= circuits.tracks.get(0).startFinish && kart.kartPosition[1] < circuits.tracks.get(0).startFinish)
                    {
                        kart.kartLapCount += 1;
                        //System.out.println(kart.kartLapCount);
                    }
                    else if (kart.prevKartPos[1] <= circuits.tracks.get(0).startFinish && kart.kartPosition[1] < circuits.tracks.get(0).startFinish)
                    {
                        kart.kartLapCount -= 1;
                        //System.out.println(kart.kartLapCount);
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
                        //System.out.println(kart.kartLapCount);
                    }
                    else if (kart.prevKartPos[1] <= circuits.tracks.get(0).startFinish && kart.kartPosition[1] < circuits.tracks.get(0).startFinish)
                    {
                        kart.kartLapCount += 1;
                        //System.out.println(kart.kartLapCount);
                    }
                    break;
                case 3:
                    if (kart.prevKartPos[0] <= circuits.tracks.get(0).startFinish && kart.kartPosition[0] > circuits.tracks.get(0).startFinish)
                    {
                        kart.kartLapCount -= 1;
                        //System.out.println(kart.kartLapCount);
                    }
                    else if (kart.prevKartPos[0] >= circuits.tracks.get(0).startFinish && kart.kartPosition[0] < circuits.tracks.get(0).startFinish)
                    {
                        kart.kartLapCount += 1;
                        //System.out.println(kart.kartLapCount);
                    }
                    break;
            }
        }
        return kart.kartLapCount > circuits.tracks.get(0).totalLaps;
    }

    public static void CheckCollision(Kart kart)
    {
        for (TrackRectangles rectangles : circuits.tracks.get(0).rectangles)
        {
            if ((rectangles.boundsX[0] <= kart.prevKartPos[0] && kart.prevKartPos[0] <= rectangles.boundsX[1]) && (rectangles.boundsY[0] <= kart.prevKartPos[1] && kart.prevKartPos[1] <= rectangles.boundsY[1]))
            {
                if (rectangles.walls[0])
                {
                    if (kart.kartPosition[1] < rectangles.boundsY[0] + boundaryOffset)
                    {
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

    public static void CreateTracks()
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
