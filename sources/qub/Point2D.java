package qub;

/**
 * A two-dimensional point object.
 */
public class Point2D
{
    public static final Point2D zero = new Point2D(Length.zero, Length.zero);

    private final Length x;
    private final Length y;

    /**
     * Create a new Point2D object.
     * @param x The x-coordinate of the point.
     * @param y The y-coordinate of the point.
     */
    public Point2D(Length x, Length y)
    {
        PreCondition.assertNotNull(x, "x");
        PreCondition.assertNotNull(y, "y");

        this.x = x;
        this.y = y;
    }

    /**
     * Get the x-coordinate of the point.
     * @return The x-coordinate of the point.
     */
    public Length getX()
    {
        return x;
    }

    /**
     * Get the y-coordinate of the point.
     * @return The y-coordinate of the point.
     */
    public Length getY()
    {
        return y;
    }

    @Override
    public String toString()
    {
        return "(" + x + "," + y + ")";
    }

    @Override
    public boolean equals(Object rhs)
    {
        return rhs instanceof Point2D && equals((Point2D)rhs);
    }

    /**
     * Get whether or not this Point2D equals the provided Point2D.
     * @param rhs The Point2D to compare to this Point2D.
     * @return Whether or not this Point2D equals the provided Point2D.
     */
    public boolean equals(Point2D rhs)
    {
        return rhs != null && x.equals(rhs.x) && y.equals(rhs.y);
    }

    @Override
    public int hashCode()
    {
        return x.hashCode() ^ y.hashCode();
    }
}
