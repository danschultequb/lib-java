package qub;

/**
 * A {@link Size2Integer} object that can change its width and height values.
 */
public interface MutableSize2Integer extends MutableSize2<Integer>, Size2Integer
{
    /**
     * Create a new {@link MutableSize2Integer}.
     */
    public static MutableSize2Integer create()
    {
        return BasicMutableSize2Integer.create();
    }

    /**
     * Create a new {@link MutableSize2Integer} with the provided width and height.
     * @param width The width of the new {@link MutableSize2Integer}.
     * @param height The height of the new {@link MutableSize2Integer}.
     */
    public static MutableSize2Integer create(int width, int height)
    {
        return BasicMutableSize2Integer.create(width, height);
    }

    @Override
    public default MutableSize2Integer setWidth(Integer width)
    {
        return (MutableSize2Integer)MutableSize2.super.setWidth(width);
    }

    /**
     * Set the width of this {@link MutableSize2Integer}.
     * @param width The width of this {@link MutableSize2Integer}.
     * @return This object for method chaining.
     */
    public default MutableSize2Integer setWidth(int width)
    {
        return this.set(width, this.getHeightAsInt());
    }

    @Override
    public default MutableSize2Integer setHeight(Integer height)
    {
        return (MutableSize2Integer)MutableSize2.super.setHeight(height);
    }

    /**
     * Set the height of this {@link MutableSize2Integer}.
     * @param height The height of this {@link MutableSize2Integer}.
     * @return This object for method chaining.
     */
    public default MutableSize2Integer setHeight(int height)
    {
        return (MutableSize2Integer)MutableSize2.super.setHeight(height);
    }

    @Override
    public default MutableSize2Integer set(Integer width, Integer height)
    {
        PreCondition.assertNotNull(width, "width");
        PreCondition.assertGreaterThanOrEqualTo(width, 0, "width");
        PreCondition.assertNotNull(height, "height");
        PreCondition.assertGreaterThanOrEqualTo(height, 0, "height");

        return this.set(width.intValue(), height.intValue());
    }

    /**
     * Set the width and height of this {@link MutableSize2Integer}.
     * @param width The width of this {@link MutableSize2Integer}.
     * @param height The height of this {@link MutableSize2Integer}.
     * @return This object for method chaining.
     */
    public MutableSize2Integer set(int width, int height);

    @Override
    public default MutableSize2Integer set(Size2<Integer> size)
    {
        return (MutableSize2Integer)MutableSize2.super.set(size);
    }

    /**
     * A version of a {@link MutableSize2Integer} that returns its own type from chainable methods.
     * @param <T> The real type of this {@link MutableSize2Integer.Typed}.
     */
    public static interface Typed<T extends MutableSize2Integer> extends MutableSize2Integer
    {
        @Override
        @SuppressWarnings("unchecked")
        public default T setWidth(Integer width)
        {
            return (T)MutableSize2Integer.super.setWidth(width);
        }

        @Override
        @SuppressWarnings("unchecked")
        public default T setWidth(int width)
        {
            return (T)MutableSize2Integer.super.setWidth(width);
        }

        @Override
        @SuppressWarnings("unchecked")
        public default T setHeight(Integer height)
        {
            return (T)MutableSize2Integer.super.setHeight(height);
        }

        @Override
        @SuppressWarnings("unchecked")
        public default T setHeight(int height)
        {
            return (T)MutableSize2Integer.super.setHeight(height);
        }

        @Override
        @SuppressWarnings("unchecked")
        public default T set(Integer width, Integer height)
        {
            return (T)MutableSize2Integer.super.set(width, height);
        }

        @Override
        public T set(int width, int height);

        @Override
        @SuppressWarnings("unchecked")
        public default T set(Size2<Integer> size)
        {
            return (T)MutableSize2Integer.super.set(size);
        }
    }
}
