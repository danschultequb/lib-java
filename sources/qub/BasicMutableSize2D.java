package qub;

public class BasicMutableSize2D implements MutableSize2D
{
    private final RunnableEvent0 changed;
    private Distance width;
    private Distance height;

    private BasicMutableSize2D(Distance width, Distance height)
    {
        PreCondition.assertNotNull(width, "width");
        PreCondition.assertGreaterThanOrEqualTo(width, Distance.zero, "width");
        PreCondition.assertNotNull(height, "height");
        PreCondition.assertGreaterThanOrEqualTo(height, Distance.zero, "height");

        this.changed = Event0.create();
        this.width = width;
        this.height = height;
    }

    public static BasicMutableSize2D create(Distance width, Distance height)
    {
        return new BasicMutableSize2D(width, height);
    }

    @Override
    public Distance getWidth()
    {
        return this.width;
    }

    @Override
    public Distance getHeight()
    {
        return this.height;
    }

    @Override
    public BasicMutableSize2D set(Distance width, Distance height)
    {
        PreCondition.assertNotNull(width, "width");
        PreCondition.assertGreaterThanOrEqualTo(width, Distance.zero, "width");
        PreCondition.assertNotNull(height, "height");
        PreCondition.assertGreaterThanOrEqualTo(height, Distance.zero, "height");

        if (!this.width.equals(width) || !this.height.equals(height))
        {
            this.width = width;
            this.height = height;
            this.changed.run();
        }

        return this;
    }

    @Override
    public Disposable onChanged(Action0 callback)
    {
        PreCondition.assertNotNull(callback, "callback");

        return this.changed.subscribe(callback);
    }

    @Override
    public String toString()
    {
        return MutableSize2D.toString(this);
    }

    @Override
    public boolean equals(Object rhs)
    {
        return MutableSize2D.equals(this, rhs);
    }

    @Override
    public int hashCode()
    {
        return MutableSize2D.hashCode(this);
    }
}
