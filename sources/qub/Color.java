package qub;

public class Color
{
    private final int red;
    private final int green;
    private final int blue;
    private final int alpha;

    private Color(int red, int green, int blue, int alpha)
    {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
    }

    public int getRed()
    {
        return red;
    }

    public int getGreen()
    {
        return green;
    }

    public int getBlue()
    {
        return blue;
    }

    public int getAlpha()
    {
        return alpha;
    }

    public static Color rgba(int red, int green, int blue, int alpha)
    {
        PreCondition.assertBetween(0, red, 255, "red");
        PreCondition.assertBetween(0, green, 255, "green");
        PreCondition.assertBetween(0, blue, 255, "blue");
        PreCondition.assertBetween(0, alpha, 255, "alpha");

        return new Color(red, green, blue, alpha);
    }
}
