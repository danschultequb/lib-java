package qub;

/**
 * A two-dimensional point object.
 */
public interface Point2D
{
    Point2D zero = Point2D.create();

    static MutablePoint2D create()
    {
        return MutablePoint2D.create();
    }

    static MutablePoint2D create(Distance x, Distance y)
    {
        return MutablePoint2D.create(x, y);
    }

    /**
     * Get the x-coordinate of the {@link Point2D}.
     * @return The x-coordinate of the {@link Point2D}.
     */
    Distance getX();

    /**
     * Get the y-coordinate of the {@link Point2D}.
     * @return The y-coordinate of the {@link Point2D}.
     */
    Distance getY();

    static String toString(Point2D point)
    {
        PreCondition.assertNotNull(point, "point");

        return JSONObject.create()
            .setString("x", point.getX().toString())
            .setString("y", point.getY().toString())
            .toString();
    }

    static boolean equals(Point2D lhs, Object rhs)
    {
        PreCondition.assertNotNull(lhs, "lhs");

        return rhs instanceof Point2D && lhs.equals((Point2D)rhs);
    }

    /**
     * Get whether this {@link Point2D} equals the provided {@link Point2D}.
     * @param rhs The {@link Point2D} to compare to this {@link Point2D}.
     * @return Whether this {@link Point2D} equals the provided {@link Point2D}.
     */
    default boolean equals(Point2D rhs)
    {
        return rhs != null &&
            this.getX().equals(rhs.getX()) &&
            this.getY().equals(rhs.getY());
    }

    static int hashCode(Point2D point)
    {
        PreCondition.assertNotNull(point, "point");

        return Hash.getHashCode(point.getX(), point.getY());
    }
}
