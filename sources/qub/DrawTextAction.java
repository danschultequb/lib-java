package qub;

/**
 * A PainterAction that can draw text at a specified topLeft point.
 */
public class DrawTextAction implements PainterAction
{
    private final String text;
    private final Point2D topLeft;
    private final Distance fontSize;

    /**
     * Create a new DrawTextAction.
     * @param text The text to draw.
     * @param topLeft The top-left point that the text will be drawn at.
     * @param fontSize The size of the font that will be drawn.
     */
    public DrawTextAction(String text, Point2D topLeft, Distance fontSize)
    {
        PreCondition.assertNotNullAndNotEmpty(text, "text");
        PreCondition.assertNotNull(topLeft, "topLeft");
        PreCondition.assertNotNull(fontSize, "fontSize");

        this.text = text;
        this.topLeft = topLeft;
        this.fontSize = fontSize;
    }

    /**
     * Create a new DrawTextAction.
     * @param text The text to draw.
     * @param topLeftX The top-left x-coordinate that the text will be drawn at.
     * @param topLeftY The top-left y-coordinate that the text will be drawn at.
     * @param fontSize The size of the font that will be drawn.
     */
    public DrawTextAction(String text, Distance topLeftX, Distance topLeftY, Distance fontSize)
    {
        PreCondition.assertNotNullAndNotEmpty(text, "text");
        PreCondition.assertNotNull(topLeftX, "topLeftX");
        PreCondition.assertNotNull(topLeftY, "topLeftY");
        PreCondition.assertNotNull(fontSize, "fontSize");

        this.text = text;
        this.topLeft = new Point2D(topLeftX, topLeftY);
        this.fontSize = fontSize;
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
     * Get the top-left point that the text will be drawn at.
     * @return The top-left point that the text will be drawn at.
     */
    public Point2D getTopLeft()
    {
        return topLeft;
    }

    /**
     * Get the top-left x-coordinate that the text will be drawn at.
     * @return The top-left x-coordinate that the text will be drawn at.
     */
    public Distance getTopLeftX()
    {
        return topLeft.getX();
    }

    /**
     * Get the top-left y-coordinate that the text will be drawn at.
     * @return The top-left y-coordinate that the text will be drawn at.
     */
    public Distance getTopLeftY()
    {
        return topLeft.getY();
    }

    /**
     * Get the size of the font that will be drawn.
     * @return The size of the font that will be drawn.
     */
    public Distance getFontSize()
    {
        return fontSize;
    }

    @Override
    public String toString()
    {
        return "{type:\"DrawTextAction\",\"text\":\"" + text + "\",\"topLeft\":\"" + topLeft + "\",\"fontSize\":\"" + fontSize + "\"}";
    }

    @Override
    public boolean equals(Object rhs)
    {
        return rhs instanceof DrawTextAction && equals((DrawTextAction)rhs);
    }

    public boolean equals(DrawTextAction rhs)
    {
        return rhs != null &&
            text.equals(rhs.text) &&
            topLeft.equals(rhs.topLeft) &&
            fontSize.equals(rhs.fontSize);
    }

    @Override
    public int hashCode()
    {
        return text.hashCode() ^ topLeft.hashCode() ^ fontSize.hashCode();
    }
}
