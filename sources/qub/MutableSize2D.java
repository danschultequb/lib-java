package qub;

public class MutableSize2D implements Size2D
{
    private Distance width;
    private Distance height;

    private MutableSize2D(Distance width, Distance height)
    {
        PreCondition.assertNotNull(width, "width");
        PreCondition.assertGreaterThanOrEqualTo(width, Distance.zero, "width");
        PreCondition.assertNotNull(height, "height");
        PreCondition.assertGreaterThanOrEqualTo(height, Distance.zero, "height");

        this.width = width;
        this.height = height;
    }

    public static MutableSize2D create()
    {
        return MutableSize2D.create(Distance.zero, Distance.zero);
    }

    public static MutableSize2D create(Distance width, Distance height)
    {
        return new MutableSize2D(width, height);
    }

    @Override
    public Distance getWidth()
    {
        return this.width;
    }

    public MutableSize2D setWidth(Distance width)
    {
        PreCondition.assertNotNull(width, "width");

        this.width = width;

        return this;
    }

    @Override
    public Distance getHeight()
    {
        return this.height;
    }

    public MutableSize2D setHeight(Distance height)
    {
        PreCondition.assertNotNull(height, "height");

        this.height = height;

        return this;
    }

    public MutableSize2D set(Distance width, Distance height)
    {
        PreCondition.assertNotNull(width, "width");
        PreCondition.assertNotNull(height, "height");

        this.width = width;
        this.height = height;

        return this;
    }

    @Override
    public String toString()
    {
        return Size2D.toString(this);
    }

    @Override
    public boolean equals(Object rhs)
    {
        return Size2D.equals(this, rhs);
    }

    @Override
    public int hashCode()
    {
        return Size2D.hashCode(this);
    }
}
