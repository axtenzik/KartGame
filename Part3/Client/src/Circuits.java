import java.util.ArrayList;

//track.json values save here

class TrackRectangles
{
    public int[] boundsX;
    public int[] boundsY;
    public int forward;
    public boolean[] walls;
}

class TrackDetails
{
    public String trackName;
    public int[] trackSize;
    public int totalLaps;
    public int startFinish;
    public int finishDirection;
    public int[] lightsPosition;
    public ArrayList<TrackRectangles> rectangles;
}

public class Circuits
{
    public ArrayList<TrackDetails> tracks;
}