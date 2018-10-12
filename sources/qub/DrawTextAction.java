package qub;

/**
 * A PainterAction that can draw text at a specified topLeft point.
 */
public class DrawTextAction implements PainterAction
{
    private final String text;
    private final Point2D topLeft;
    private final Distance fontSize;
    private final Color color;

    /**
     * Create a new DrawTextAction.
     * @param text The text to draw.
     * @param topLeft The top-left point that the text will be drawn at.
     * @param fontSize The size of the font that will be drawn.
     * @param color The color to draw the text with.
     */
    public DrawTextAction(String text, Point2D topLeft, Distance fontSize, Color color)
    {
        PreCondition.assertNotNullAndNotEmpty(text, "text");
        PreCondition.assertNotNull(topLeft, "topLeft");
        PreCondition.assertNotNull(fontSize, "fontSize");
        PreCondition.assertNotNull(color, "color");

        this.text = text;
        this.topLeft = topLeft;
        this.fontSize = fontSize;
        this.color = color;
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

    /**
     * Get the color that the text will be drawn with.
     * @return The color that the text will be drawn with.
     */
    public Color getColor()
    {
        return color;
    }

    @Override
    public String toString()
    {
        return "{type:\"DrawTextAction\",\"text\":\"" + text + "\",\"topLeft\":\"" + topLeft + "\",\"fontSize\":\"" + fontSize + "\",\"color\":\"" + color + "\"}";
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
            fontSize.equals(rhs.fontSize) &&
            color.equals(rhs.color);
    }

    @Override
    public int hashCode()
    {
        return Hash.getHashCode(text, topLeft, fontSize, color);
    }
}
