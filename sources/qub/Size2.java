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
     */
    public T getWidth();

    /**
     * Get the height of this {@link Size2}.
     */
    T getHeight();

    /**
     * Get the {@link String} representation of the provided {@link Size2}.
     * @param size The {@link Size2} to get the {@link String} representation of.
     * @param <T> The type of size components the provided {@link Size2} contains.
     */
    public static <T> String toString(Size2<T> size)
    {
        PreCondition.assertNotNull(size, "size");

        return JSONObject.create()
            .setString("width", size.getWidth().toString())
            .setString("height", size.getHeight().toString())
            .toString();
    }

    /**
     * Get whether the provided {@link Size2} is equal to the provided {@link Object}.
     * @param lhs The {@link Size2} to compare.
     * @param rhs The {@link Object} to compare.
     * @param <T> The type of size components the provided {@link Size2} contains.
     */
    @SuppressWarnings("unchecked")
    public static <T> boolean equals(Size2<T> lhs, Object rhs)
    {
        PreCondition.assertNotNull(lhs, "lhs");

        return rhs instanceof Size2 && lhs.equals((Size2<T>) rhs);
    }

    /**
     * Get whether this {@link Size2} is equal to the provided {@link Size2}.
     * @param rhs The {@link Size2} to compare to this {@link Size2}.
     */
    public default boolean equals(Size2<T> rhs)
    {
        return rhs != null &&
            this.getWidth().equals(rhs.getWidth()) &&
            this.getHeight().equals(rhs.getHeight());
    }

    /**
     * Get the hash code of the provided {@link Size2}.
     * @param size The {@link Size2} to get the hash code of.
     * @param <T> The type of size components the provided {@link Size2} contains.
     */
    public static <T> int hashCode(Size2<T> size)
    {
        PreCondition.assertNotNull(size, "size");

        return Hash.getHashCode(size.getWidth(), size.getHeight());
    }

    /**
     * An abstract base class implementation of {@link Size2} that contains common properties and
     * functions for all {@link Size2} types.
     */
    public static abstract class Base<T> implements Size2<T>
    {
        @Override
        public boolean equals(Object obj)
        {
            return Size2.equals(this, obj);
        }

        @Override
        public boolean equals(Size2<T> rhs)
        {
            return Size2.super.equals(rhs);
        }

        @Override
        public String toString()
        {
            return Size2.toString(this);
        }

        @Override
        public int hashCode()
        {
            return Size2.hashCode(this);
        }
    }
}
