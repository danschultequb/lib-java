package qub;

public class Display
{
    private final double widthInPixels;
    private final double heightInPixels;
    private final double horizontalDpi;
    private final double verticalDpi;
    private final double horizontalScale;
    private final double verticalScale;

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
        return getHorizontalDpi(true);
    }

    public double getHorizontalDpi(boolean useDisplayScaling)
    {
        double result = horizontalDpi;
        if (!useDisplayScaling)
        {
            result /= horizontalScale;
        }
        return result;
    }

    /**
     * Get the number of pixels that fit vertically in an inch on this Display.
     * @return The number of pixels that fit vertically in an inch on this Display.
     */
    public double getVerticalDpi()
    {
        return getVerticalDpi(true);
    }

    public double getVerticalDpi(boolean useDisplayScaling)
    {
        double result = verticalDpi;
        if (!useDisplayScaling)
        {
            result /= verticalScale;
        }
        return result;
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

    public double convertHorizontalDistanceToPixels(Distance horizontalDistance, boolean useDisplayScaling)
    {
        PreCondition.assertNotNull(horizontalDistance, "horizontalDistance");

        final Distance horizontalDistanceInInches = horizontalDistance.toInches();
        final double result = horizontalDistanceInInches.getValue() * getHorizontalDpi(useDisplayScaling);

        PostCondition.assertGreaterThanOrEqualTo(result, 0, "result");

        return result;
    }

    public Distance convertHorizontalPixelsToDistance(double horizontalPixels)
    {
        PreCondition.assertGreaterThanOrEqualTo(horizontalPixels, 0, "horizontalPixels");

        final double distanceValue = horizontalPixels / getHorizontalDpi();
        final Distance result = Distance.inches(distanceValue);

        PostCondition.assertGreaterThanOrEqualTo(result, Distance.zero, "result");

        return result;
    }

    public double convertVerticalDistanceToPixels(Distance verticalDistance, boolean useDisplayScaling)
    {
        PreCondition.assertNotNull(verticalDistance, "verticalDistance");

        final Distance verticalDistanceInInches = verticalDistance.toInches();
        final double result = verticalDistanceInInches.getValue() * getVerticalDpi(useDisplayScaling);

        PostCondition.assertGreaterThanOrEqualTo(result, 0, "result");

        return result;
    }

    public Distance convertVerticalPixelsToDistance(double verticalPixels)
    {
        PreCondition.assertGreaterThanOrEqualTo(verticalPixels, 0, "verticalPixels");

        final double distanceValue = verticalPixels / getVerticalDpi();
        final Distance result = Distance.inches(distanceValue);

        PostCondition.assertGreaterThanOrEqualTo(result, Distance.zero, "result");

        return result;
    }
}
