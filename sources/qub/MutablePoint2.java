package qub;

public interface MutablePoint2<T> extends Point2<T>
{
    /**
     * Set the x-coordinate/dimension of this {@link MutablePoint2}.
     * @param x The x-coordinate/dimension of this {@link MutablePoint2}.
     * @return This object for method chaining.
     */
    MutablePoint2<T> setX(T x);

    /**
     * Set the y-coordinate/dimension of this {@link MutablePoint2}.
     * @param y The y-coordinate/dimension of this {@link MutablePoint2}.
     * @return This object for method chaining.
     */
    MutablePoint2<T> setY(T y);
}
