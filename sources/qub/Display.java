package qub;

public class Display
{
    private final double widthInPixels;
    private final double heightInPixels;
    private final double horizontalDpi;
    private final double verticalDpi;
    private final double horizontalScale;
    private final double verticalScale;

    public Display(double widthInPixels, double heightInPixels, double horizontalDpi, double verticalDpi)
    {
        this(widthInPixels, heightInPixels, horizontalDpi, verticalDpi, 1, 1);
    }

    public Display(double widthInPixels, double heightInPixels, double horizontalDpi, double verticalDpi, double horizontalScale, double verticalScale)
    {
        this.widthInPixels = widthInPixels;
        this.heightInPixels = heightInPixels;
        this.horizontalDpi = horizontalDpi;
        this.verticalDpi = verticalDpi;
        this.horizontalScale = horizontalScale;
        this.verticalScale = verticalScale;
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
        return horizontalDpi / horizontalScale;
    }

    /**
     * Get the number of pixels that fit vertically in an inch on this Display.
     * @return The number of pixels that fit vertically in an inch on this Display.
     */
    public double getVerticalDpi()
    {
        return verticalDpi / verticalScale;
    }

    /**
     * Get the width of this Display.
     * @return The width of this Display.
     */
    public Distance getWidth()
    {
        return Distance.inches(widthInPixels / getHorizontalDpi());
    }

    public Distance getHeight()
    {
        return Distance.inches(heightInPixels / getVerticalDpi());
    }

    public double getHorizontalScale()
    {
        return horizontalScale;
    }

    public double getVerticalScale()
    {
        return verticalScale;
    }
}
