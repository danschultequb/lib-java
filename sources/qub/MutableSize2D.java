package qub;

public interface MutableSize2D
{
    static MutableSize2D create()
    {
        return MutableSize2D.create(Distance.zero, Distance.zero);
    }

    static MutableSize2D create(Distance width, Distance height)
    {
        return BasicMutableSize2D.create(width, height);
    }

    Distance getWidth();

    default MutableSize2D setWidth(Distance width)
    {
        return this.set(width, this.getHeight());
    }

    Distance getHeight();

    default MutableSize2D setHeight(Distance height)
    {
        return this.set(this.getWidth(), height);
    }

    MutableSize2D set(Distance width, Distance height);

    /**
     * Register the provided callback to be run whenever this MutableSize2D's width and/or height
     * changes.
     * @param callback The callback to run whenever this MutableSize2D's width and/or height
     *                 changes.
     * @return A Disposable that can be disposed to unregister the provided callback from this
     * event.
     */
    Disposable onChanged(Action0 callback);

    static String toString(MutableSize2D size)
    {
        PreCondition.assertNotNull(size, "size");

        return "{\"width\":\"" + size.getWidth() + "\",\"height\":\"" + size.getHeight() + "\"}";
    }

    static boolean equals(MutableSize2D lhs, Object rhs)
    {
        PreCondition.assertNotNull(lhs, "lhs");

        return rhs instanceof BasicMutableSize2D && lhs.equals((MutableSize2D)rhs);
    }

    default boolean equals(MutableSize2D rhs)
    {
        return rhs != null &&
            this.getWidth().equals(rhs.getWidth()) &&
            this.getHeight().equals(rhs.getHeight());
    }

    static int hashCode(MutableSize2D size)
    {
        PreCondition.assertNotNull(size, "size");

        return Hash.getHashCode(size.getWidth(), size.getHeight());
    }
}
