import java.util.ArrayList;

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



/*import java.awt.*;

public class Track
{
    private int[] boundsX; //the 2 edges on the x
    private int[] boundsY; //the 2 edges on the y
    private int[] walls; //any of the 4 walls that can be collided with
    private int forward; //the 'correct' forward direction wall, this is for AI pathfinding

    //There's 4 walls, each wall can be either 1 or 0 depending on which walls can be passed through
    //0 through 3 can be used to tell with wall is the forward wall

    Track(int[] xBounds, int[] yBounds, int[] wall, int road)
    {
        //boundsX = xBounds;
    }

    public void PaintTrack(Component c, Graphics g)
    {

    }
}*/
