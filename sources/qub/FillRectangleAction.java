package qub;

public class FillRectangleAction implements PainterAction
{
    private final Point2D topLeft;
    private final Distance width;
    private final Distance height;
    private final Color color;

    public FillRectangleAction(Point2D topLeft, Distance width, Distance height, Color color)
    {
        PreCondition.assertNotNull(topLeft, "topLeft");
        PreCondition.assertNotNull(width, "width");
        PreCondition.assertNotNull(height, "height");
        PreCondition.assertNotNull(color, "color");

        this.topLeft = topLeft;
        this.width = width;
        this.height = height;
        this.color = color;
    }

    public Point2D getTopLeft()
    {
        return topLeft;
    }

    public Distance getWidth()
    {
        return width;
    }

    public Distance getHeight()
    {
        return height;
    }

    public Color getColor()
    {
        return color;
    }

    @Override
    public String toString()
    {
        return "{type:\"FillRectangleAction\",\"topLeft\": \"" + topLeft + "\",\"width\":" + width + "\",\"height\":\"" + height + "\",\"color\":\"" + color + "\"}";
    }

    @Override
    public boolean equals(Object rhs)
    {
        return rhs instanceof FillRectangleAction && equals((FillRectangleAction)rhs);
    }

    public boolean equals(FillRectangleAction rhs)
    {
        return rhs != null &&
            topLeft.equals(rhs.topLeft) &&
            width.equals(rhs.width) &&
            height.equals(rhs.height) &&
            color.equals(rhs.color);
    }

    @Override
    public int hashCode()
    {
        return Hash.getHashCode(topLeft, width, height, color);
    }
}
