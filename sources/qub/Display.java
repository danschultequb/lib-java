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

    public double convertHorizontalDistanceToPixels(Distance horizontalDistance)
    {
        PreCondition.assertNotNull(horizontalDistance, "horizontalDistance");

        final Distance horizontalDistanceInInches = horizontalDistance.toInches();
        final double result = horizontalDistanceInInches.getValue() * getHorizontalDpi();

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

    public double convertVerticalDistanceToPixels(Distance verticalDistance)
    {
        PreCondition.assertNotNull(verticalDistance, "verticalDistance");

        final Distance verticalDistanceInInches = verticalDistance.toInches();
        final double result = verticalDistanceInInches.getValue() * getVerticalDpi();

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

    public Size2D convertPixelsToSize2D(double horizontalPixels, double verticalPixels)
    {
        PreCondition.assertGreaterThanOrEqualTo(horizontalPixels, 0, "horizontalPixels");
        PreCondition.assertGreaterThanOrEqualTo(verticalPixels, 0, "verticalPixels");

        final Distance horizontalDistance = convertHorizontalPixelsToDistance(horizontalPixels);
        final Distance verticalDistance = convertVerticalPixelsToDistance(verticalPixels);
        final Size2D result = Size2D.create(horizontalDistance, verticalDistance);

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    /**
     * Convert the provided pixels quantities to a Point2D.
     * @param horizontalPixels The horizontal pixel quantity.
     * @param verticalPixels The vertical pixel quantity.
     * @return The converted Point2D.
     */
    public Point2D convertPixelsToPoint2D(double horizontalPixels, double verticalPixels)
    {
        PreCondition.assertGreaterThanOrEqualTo(horizontalPixels, 0, "horizontalPixels");
        PreCondition.assertGreaterThanOrEqualTo(verticalPixels, 0, "verticalPixels");

        final Distance horizontalDistance = this.convertHorizontalPixelsToDistance(horizontalPixels);
        final Distance verticalDistance = this.convertVerticalPixelsToDistance(verticalPixels);
        final Point2D result = new Point2D(horizontalDistance, verticalDistance);

        PostCondition.assertNotNull(result, "result");

        return result;
    }
}
