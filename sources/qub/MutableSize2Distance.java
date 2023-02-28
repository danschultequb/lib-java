package qub;

/**
 * A {@link Size2Distance} object that can change its width and height values.
 */
public interface MutableSize2Distance extends MutableSize2<Distance>, Size2Distance
{
    public static MutableSize2Distance create()
    {
        return BasicMutableSize2Distance.create();
    }

    public static MutableSize2Distance create(Distance width, Distance height)
    {
        return BasicMutableSize2Distance.create(width, height);
    }

    @Override
    public MutableSize2Distance setWidth(Distance width);

    @Override
    public MutableSize2Distance setHeight(Distance height);

    @Override
    public MutableSize2Distance set(Distance width, Distance height);

    /**
     * A version of a {@link MutableSize2Distance} that returns its own type from chainable methods.
     * @param <T> The real type of this {@link MutableSize2Distance.Typed}.
     */
    public static interface Typed<T extends MutableSize2Distance> extends MutableSize2Distance
    {
        @Override
        public T setWidth(Distance width);

        @Override
        public T setHeight(Distance height);

        @Override
        public T set(Distance width, Distance height);
    }
}
