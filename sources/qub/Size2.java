package qub;

/**
 * A size type that contains a width and a height.
 */
public interface Size2<T>
{
    /**
     * Create a new {@link MutableSize2Integer} with the provided width and height.
     * @param width The width of the new {@link MutableSize2Integer}.
     * @param height The height of the new {@link MutableSize2Integer}.
     * @return The new {@link MutableSize2Integer}.
     */
    public static MutableSize2Integer create(int width, int height)
    {
        return MutableSize2Integer.create(width, height);
    }

    /**
     * Create a new {@link MutableSize2Distance} with the provided width and height.
     * @param width The width of the new {@link MutableSize2Distance}.
     * @param height The height of the new {@link MutableSize2Distance}.
     * @return The new {@link MutableSize2Distance}.
     */
    public static MutableSize2Distance create(Distance width, Distance height)
    {
        return MutableSize2Distance.create(width, height);
    }

    /**
     * Get the width of this {@link Size2}.
     * @return The width of this {@link Size2}.
     */
    T getWidth();

    /**
     * Get the height of this {@link Size2}.
     * @return The height of this {@link Size2}.
     */
    T getHeight();

    public static <T> String toString(Size2<T> size)
    {
        PreCondition.assertNotNull(size, "size");

        return JSONObject.create()
            .setString("width", size.getWidth().toString())
            .setString("height", size.getHeight().toString())
            .toString();
    }

    @SuppressWarnings("unchecked")
    public static <T> boolean equals(Size2<T> lhs, Object rhs)
    {
        PreCondition.assertNotNull(lhs, "lhs");

        return rhs instanceof Size2 && lhs.equals((Size2<T>) rhs);
    }

    default boolean equals(Size2<T> rhs)
    {
        return rhs != null &&
            this.getWidth().equals(rhs.getWidth()) &&
            this.getHeight().equals(rhs.getHeight());
    }

    public static <T> int hashCode(Size2<T> size)
    {
        PreCondition.assertNotNull(size, "size");

        return Hash.getHashCode(size.getWidth(), size.getHeight());
    }
}
