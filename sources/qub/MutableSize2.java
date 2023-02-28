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
    public MutableSize2<T> setWidth(T width);

    /**
     * Set the height of this {@link MutableSize2}.
     * @param height The height of this {@link MutableSize2}.
     * @return This object for method chaining.
     */
    public MutableSize2<T> setHeight(T height);

    /**
     * Set the width and height of this {@link MutableSize2}.
     * @param width The width of this {@link MutableSize2}.
     * @param height The height of this {@link MutableSize2}.
     * @return This object for method chaining.
     */
    public MutableSize2<T> set(T width, T height);

    /**
     * A version of a {@link MutableSize2} that returns its own type from chainable methods.
     * @param <T> The type of the components this {@link MutableSize2.Typed} contains.
     * @param <SizeT> The real type of this {@link MutableSize2.Typed}.
     */
    public static interface Typed<T, SizeT extends MutableSize2<T>> extends MutableSize2<T>
    {
        @Override
        public SizeT setWidth(T width);

        @Override
        public SizeT setHeight(T height);

        @Override
        public SizeT set(T width, T height);
    }
}
