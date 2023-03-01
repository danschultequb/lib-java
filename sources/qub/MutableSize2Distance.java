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
    public default MutableSize2Distance setWidth(Distance width)
    {
        return (MutableSize2Distance)MutableSize2.super.setWidth(width);
    }

    @Override
    public default MutableSize2Distance setHeight(Distance height)
    {
        return (MutableSize2Distance)MutableSize2.super.setHeight(height);
    }

    @Override
    public MutableSize2Distance set(Distance width, Distance height);

    @Override
    public default MutableSize2Distance set(Size2<Distance> size)
    {
        return (MutableSize2Distance)MutableSize2.super.set(size);
    }

    /**
     * A version of a {@link MutableSize2Distance} that returns its own type from chainable methods.
     * @param <T> The real type of this {@link MutableSize2Distance.Typed}.
     */
    public static interface Typed<T extends MutableSize2Distance> extends MutableSize2Distance
    {
        @Override
        @SuppressWarnings("unchecked")
        public default T setWidth(Distance width)
        {
            return (T)MutableSize2Distance.super.setWidth(width);
        }

        @Override
        @SuppressWarnings("unchecked")
        public default T setHeight(Distance height)
        {
            return (T)MutableSize2Distance.super.setHeight(height);
        }

        @Override
        public T set(Distance width, Distance height);

        @Override
        @SuppressWarnings("unchecked")
        public default T set(Size2<Distance> size)
        {
            return (T)MutableSize2Distance.super.set(size);
        }
    }
}
