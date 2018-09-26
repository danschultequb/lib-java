package qub;

/**
 * A PainterAction that can draw a line between two specified points.
 */
public class DrawLineAction implements PainterAction
{
    private final Point2D start;
    private final Point2D end;

    /**
     * Create a new DrawLineAction object.
     * @param start The point that the drawn line starts at.
     * @param end The point that the drawn line ends at.
     */
    public DrawLineAction(Point2D start, Point2D end)
    {
        PreCondition.assertNotNull(start, "start");
        PreCondition.assertNotNull(end, "end");

        this.start = start;
        this.end = end;
    }

    public DrawLineAction(Distance startX, Distance startY, Distance endX, Distance endY)
    {
        PreCondition.assertNotNull(startX, "startX");
        PreCondition.assertNotNull(startY, "startY");
        PreCondition.assertNotNull(endX, "endX");
        PreCondition.assertNotNull(endY, "endY");

        this.start = new Point2D(startX, startY);
        this.end = new Point2D(endX, endY);
    }

    /**
     * Get the point that the drawn line starts at.
     * @return The point that the drawn line starts at.
     */
    public Point2D getStart()
    {
        return start;
    }

    /**
     * Get the x-coordinate of the point that the drawn line starts at.
     * @return The x-coordinate of the point that the drawn line starts at.
     */
    public Distance getStartX()
    {
        return start.getX();
    }

    /**
     * Get the y-coordinate of the point that the drawn line starts at.
     * @return The y-coordinate of the point that the drawn line starts at.
     */
    public Distance getStartY()
    {
        return start.getY();
    }

    /**
     * Get the point that the drawn line ends at.
     * @return The point that the drawn line ends at.
     */
    public Point2D getEnd()
    {
        return end;
    }

    /**
     * Get the x-coordinate of the point that the drawn line ends at.
     * @return The x-coordinate of the point that the drawn line ends at.
     */
    public Distance getEndX()
    {
        return end.getX();
    }

    /**
     * Get the y-coordinate of the point that the drawn line ends at.
     * @return The y-coordinate of the point that the drawn line ends at.
     */
    public Distance getEndY()
    {
        return end.getY();
    }

    @Override
    public boolean equals(Object rhs)
    {
        return rhs instanceof DrawLineAction && equals((DrawLineAction)rhs);
    }

    public boolean equals(DrawLineAction rhs)
    {
        return rhs != null && start.equals(rhs.start) && end.equals(rhs.end);
    }
}
