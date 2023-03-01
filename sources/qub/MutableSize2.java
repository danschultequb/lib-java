package qub;

/**
 * A {@link Size2} object that can change its width and height values.
 * @param <T> The type of the components this {@link MutableSize2} contains.
 */
public interface MutableSize2<T> extends Size2<T>
{
    /**
     * Set the width of this {@link MutableSize2}.
     * @param width The width of this {@link MutableSize2}.
     * @return This object for method chaining.
     */
    public default MutableSize2<T> setWidth(T width)
    {
        return this.set(width, this.getHeight());
    }

    /**
     * Set the height of this {@link MutableSize2}.
     * @param height The height of this {@link MutableSize2}.
     * @return This object for method chaining.
     */
    public default MutableSize2<T> setHeight(T height)
    {
        return this.set(this.getWidth(), height);
    }

    /**
     * Set the width and height of this {@link MutableSize2}.
     * @param width The width of this {@link MutableSize2}.
     * @param height The height of this {@link MutableSize2}.
     * @return This object for method chaining.
     */
    public MutableSize2<T> set(T width, T height);

    /**
     * Set the width and height of this {@link MutableSize2}.
     * @param size The width and height of this {@link MutableSize2}.
     * @return This object for method chaining.
     */
    public default MutableSize2<T> set(Size2<T> size)
    {
        PreCondition.assertNotNull(size, "size");

        return this.set(size.getWidth(), size.getHeight());
    }
}
