package qub;

/**
 * A PainterAction that can draw text at a specified baseline point.
 */
public class DrawTextAction implements PainterAction
{
    private final String text;
    private final Point2D baseline;

    /**
     * Create a new DrawTextAction.
     * @param text The text to draw.
     * @param baseline The baseline point that the text will be drawn at.
     */
    public DrawTextAction(String text, Point2D baseline)
    {
        PreCondition.assertNotNullAndNotEmpty(text, "text");
        PreCondition.assertNotNull(baseline, "baseline");

        this.text = text;
        this.baseline = baseline;
    }

    /**
     * Create a new DrawTextAction.
     * @param text The text to draw.
     * @param baselineX The baseline x-coordinate that the text will be drawn at.
     * @param baselineY The baseline y-coordinate that the text will be drawn at.
     */
    public DrawTextAction(String text, Distance baselineX, Distance baselineY)
    {
        PreCondition.assertNotNullAndNotEmpty(text, "text");
        PreCondition.assertNotNull(baselineX, "baselineX");
        PreCondition.assertNotNull(baselineY, "baselineY");

        this.text = text;
        this.baseline = new Point2D(baselineX, baselineY);
    }

    /**
     * Get the text to draw.
     * @return The text to draw.
     */
    public String getText()
    {
        return text;
    }

    /**
     * Get the baseline point that the text will be drawn at.
     * @return The baseline point that the text will be drawn at.
     */
    public Point2D getBaseline()
    {
        return baseline;
    }

    /**
     * Get the baseline x-coordinate that the text will be drawn at.
     * @return The baseline x-coordinate that the text will be drawn at.
     */
    public Distance getBaselineX()
    {
        return baseline.getX();
    }

    /**
     * Get the baseline y-coordinate that the text will be drawn at.
     * @return The baseline y-coordinate that the text will be drawn at.
     */
    public Distance getBaselineY()
    {
        return baseline.getY();
    }

    @Override
    public boolean equals(Object rhs)
    {
        return rhs instanceof DrawTextAction && equals((DrawTextAction)rhs);
    }

    public boolean equals(DrawTextAction rhs)
    {
        return rhs != null && text.equals(rhs.text) && baseline.equals(rhs.baseline);
    }
}
