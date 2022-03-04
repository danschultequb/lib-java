package qub;

/**
 * A point type that contains two values/dimensions.
 * @param <T> The type of values/dimensions that this {@link Point2} contains.
 */
public interface Point2<T>
{
    static MutablePoint2Integer create(int x, int y)
    {
        return Point2Integer.create(x, y);
    }

    static MutablePoint2Distance create(Distance x, Distance y)
    {
        return Point2Distance.create(x, y);
    }

    /**
     * Get the x-coordinate of the {@link Point2}.
     * @return The x-coordinate of the {@link Point2}.
     */
    T getX();

    /**
     * Get the y-coordinate of the {@link Point2}.
     * @return The y-coordinate of the {@link Point2}.
     */
    T getY();

    /**
     * Get the {@link String} representation of the provided {@link Point2}.
     * @param point The {@link Point2} to get the {@link String} representation of.
     * @param <T> The type of values stored in the {@link Point2}.
     * @return The {@link String} representation of the provided {@link Point2}.
     */
    static <T> String toString(Point2<T> point)
    {
        PreCondition.assertNotNull(point, "point");

        return JSONObject.create()
            .setString("x", point.getX().toString())
            .setString("y", point.getY().toString())
            .toString();
    }

    /**
     * Get whether the provided {@link Point2} is equal to the provided {@link Object}.
     * @param lhs The {@link Point2} to compare.
     * @param rhs The {@link Object} to compare.
     * @return Whether the provided {@link Point2} is equal to the provided {@link Object}.
     */
    @SuppressWarnings("unchecked")
    static <T> boolean equals(Point2<T> lhs, Object rhs)
    {
        PreCondition.assertNotNull(lhs, "lhs");

        return rhs instanceof Point2 && lhs.equals((Point2<T>)rhs);
    }

    /**
     * Get whether this {@link Point2} equals the provided {@link Point2}.
     * @param rhs The {@link Point2} to compare to this {@link Point2}.
     * @return Whether this {@link Point2} equals the provided {@link Point2}.
     */
    default boolean equals(Point2<T> rhs)
    {
        return rhs != null &&
            this.getX().equals(rhs.getX()) &&
            this.getY().equals(rhs.getY());
    }

    /**
     * Get the hash code of the provided {@link Point2}.
     * @param point The {@link Point2} to get the hash code of.
     * @param <T> The type of values stored in the {@link Point2}.
     * @return The hash code of the provided {@link Point2}.
     */
    static <T> int hashCode(Point2<T> point)
    {
        PreCondition.assertNotNull(point, "point");

        return Hash.getHashCode(point.getX(), point.getY());
    }
}
