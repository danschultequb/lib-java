package qub;

/**
 * A point type that contains two values/dimensions.
 * @param <T> The type of values/dimensions that this {@link Point2} contains.
 */
public interface Point2<T>
{
    public static final String xPropertyName = "x";
    public static final String yPropertyName = "y";

    public static MutablePoint2Integer create(int x, int y)
    {
        return Point2Integer.create(x, y);
    }

    public static MutablePoint2Distance create(Distance x, Distance y)
    {
        return Point2Distance.create(x, y);
    }

    /**
     * Get the x-coordinate of this {@link Point2}.
     */
    public T getX();

    /**
     * Get the y-coordinate of this {@link Point2}.
     */
    public T getY();

    /**
     * Get the JSON representation of this {@link Point2}.
     */
    public JSONObject toJson();

    /**
     * Get the {@link String} representation of the provided {@link Point2}.
     * @param point The {@link Point2} to get the {@link String} representation of.
     * @param <T> The type of values stored in the {@link Point2}.
     * @return The {@link String} representation of the provided {@link Point2}.
     */
    public static <T> String toString(Point2<T> point)
    {
        PreCondition.assertNotNull(point, "point");

        return point.toJson().toString();
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
