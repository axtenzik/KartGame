import java.io.Serializable;

public class KartDetails implements Serializable
{
    public int kartSpeed = 0;
    public int kartAngle = 4;
    public final int[] kartPosition = {375, 500};
    public final int[] prevKartPos = {375, 500};
}
