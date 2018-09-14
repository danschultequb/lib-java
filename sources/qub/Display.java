package qub;

public class Display
{
    private final double widthInPixels;
    private final double heightInPixels;
    private final double horizontalDpi;
    private final double verticalDpi;

    public Display(double widthInPixels, double heightInPixels, double horizontalDpi, double verticalDpi)
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
    public double getWidthInPixels()
    {
        return widthInPixels;
    }

    /**
     * Get the number of pixels that are contained by a vertical line across this Display.
     * @return The number of pixels that are contained by a vertical line across this Display.
     */
    public double getHeightInPixels()
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
