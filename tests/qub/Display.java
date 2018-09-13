package qub;

public class Display
{
    private final int widthInPixels;
    private final int heightInPixels;
    private final double horizontalDpi;
    private final double verticalDpi;

    public Display(int widthInPixels, int heightInPixels, double horizontalDpi, double verticalDpi)
    {
        this.widthInPixels = widthInPixels;
        this.heightInPixels = heightInPixels;
        this.horizontalDpi = horizontalDpi;
        this.verticalDpi = verticalDpi;
    }

    /**
     * Get the number of pixels that are contained by a horizontal line across this Display.
     * @return The number of pixels that are contained by a horizontal line acorss this Display.
     */
    public int getWidthInPixels()
    {
        return widthInPixels;
    }

    /**
     * Get the number of pixels that are contained by a vertical line across this Display.
     * @return The number of pixels that are contained by a vertical line across this Display.
     */
    public int getHeightInPixels()
    {
        return heightInPixels;
    }

    /**
     * Get the number of pixels that fit horizontally in an inch on this Display.
     * @return The number of pixels that fit horizontally in an inch on this Display.
     */
    public double getHorizontalDpi()
    {
        return horizontalDpi;
    }

    /**
     * Get the number of pixels that fit vertically in an inch on this Display.
     * @return The number of pixels that fit vertically in an inch on this Display.
     */
    public double getVerticalDpi()
    {
        return verticalDpi;
    }

    /**
     * Get the width of this Display.
     * @return The width of this Display.
     */
    public Distance getWidth()
    {
        return Distance.inches(widthInPixels / horizontalDpi);
    }

    public Distance getHeight()
    {
        return Distance.inches(heightInPixels / verticalDpi);
    }
}
