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
    public Length getWidth()
    {
        return Length.inches(widthInPixels / getHorizontalDpi());
    }

    public Length getHeight()
    {
        return Length.inches(heightInPixels / getVerticalDpi());
    }

    public double getHorizontalScale()
    {
        return horizontalScale;
    }

    public double getVerticalScale()
    {
        return verticalScale;
    }

    public double convertHorizontalDistanceToPixels(Length horizontalLength)
    {
        PreCondition.assertNotNull(horizontalLength, "horizontalDistance");

        final Length horizontalLengthInInches = horizontalLength.toInches();
        final double result = horizontalLengthInInches.getValue() * getHorizontalDpi();

        PostCondition.assertGreaterThanOrEqualTo(result, 0, "result");

        return result;
    }

    public Length convertHorizontalPixelsToDistance(double horizontalPixels)
    {
        PreCondition.assertGreaterThanOrEqualTo(horizontalPixels, 0, "horizontalPixels");

        final double distanceValue = horizontalPixels / getHorizontalDpi();
        final Length result = Length.inches(distanceValue);

        PostCondition.assertGreaterThanOrEqualTo(result, Length.zero, "result");

        return result;
    }

    public double convertVerticalDistanceToPixels(Length verticalLength)
    {
        PreCondition.assertNotNull(verticalLength, "verticalDistance");

        final Length verticalLengthInInches = verticalLength.toInches();
        final double result = verticalLengthInInches.getValue() * getVerticalDpi();

        PostCondition.assertGreaterThanOrEqualTo(result, 0, "result");

        return result;
    }

    public Length convertVerticalPixelsToDistance(double verticalPixels)
    {
        PreCondition.assertGreaterThanOrEqualTo(verticalPixels, 0, "verticalPixels");

        final double distanceValue = verticalPixels / getVerticalDpi();
        final Length result = Length.inches(distanceValue);

        PostCondition.assertGreaterThanOrEqualTo(result, Length.zero, "result");

        return result;
    }

    public Size2D convertPixelsToSize2D(double horizontalPixels, double verticalPixels)
    {
        PreCondition.assertGreaterThanOrEqualTo(horizontalPixels, 0, "horizontalPixels");
        PreCondition.assertGreaterThanOrEqualTo(verticalPixels, 0, "verticalPixels");

        final Length horizontalLength = convertHorizontalPixelsToDistance(horizontalPixels);
        final Length verticalLength = convertVerticalPixelsToDistance(verticalPixels);
        final Size2D result = new Size2D(horizontalLength, verticalLength);

        PostCondition.assertNotNull(result, "result");

        return result;
    }
}
