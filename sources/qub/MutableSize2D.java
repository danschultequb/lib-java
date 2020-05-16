package qub;

public interface MutableSize2D extends Size2D
{
    static MutableSize2D create(Distance width, Distance height)
    {
        return BasicMutableSize2D.create(width, height);
    }

    default MutableSize2D setWidth(Distance width)
    {
        return this.set(width, this.getHeight());
    }

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
}
