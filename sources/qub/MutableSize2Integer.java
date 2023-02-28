package qub;

/**
 * A {@link Size2Integer} object that can change its width and height values.
 */
public interface MutableSize2Integer extends MutableSize2<Integer>, Size2Integer
{
    public static MutableSize2Integer create()
    {
        return BasicMutableSize2Integer.create();
    }

    public static MutableSize2Integer create(int width, int height)
    {
        return BasicMutableSize2Integer.create(width, height);
    }

    @Override
    public MutableSize2Integer setWidth(Integer width);

    public MutableSize2Integer setWidth(int width);

    @Override
    public MutableSize2Integer setHeight(Integer height);

    public MutableSize2Integer setHeight(int height);

    @Override
    public MutableSize2Integer set(Integer width, Integer height);

    public MutableSize2Integer set(int width, int height);

    /**
     * A version of a {@link MutableSize2Integer} that returns its own type from chainable methods.
     * @param <T> The real type of this {@link MutableSize2Integer.Typed}.
     */
    public static interface Typed<T extends MutableSize2Integer> extends MutableSize2Integer
    {
        @Override
        public T setWidth(Integer width);

        @Override
        public T setWidth(int width);

        @Override
        public T setHeight(Integer height);

        @Override
        public T setHeight(int height);

        @Override
        public T set(Integer width, Integer height);

        @Override
        public T set(int width, int height);
    }
}
