package qub;

public class Rectangle
{
    private final int topLeftXInPixels;
    private final int topLeftYInPixels;
    private final int widthInPixels;
    private final int heightInPixels;

    public Rectangle(int topLeftXInPixels, int topLeftYInPixels, int widthInPixels, int heightInPixels)
    {
        this.topLeftXInPixels = topLeftXInPixels;
        this.topLeftYInPixels = topLeftYInPixels;
        this.widthInPixels = widthInPixels;
        this.heightInPixels = heightInPixels;
    }

    public int getTopLeftXInPixels()
    {
        return topLeftXInPixels;
    }

    public int getTopLeftYInPixels()
    {
        return topLeftYInPixels;
    }

    public int getWidthInPixels()
    {
        return widthInPixels;
    }

    public int getHeightInPixels()
    {
        return heightInPixels;
    }
}
