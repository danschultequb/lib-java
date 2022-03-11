package qub;

public class MutableSize2Distance extends Size2Base<Distance> implements MutableSize2<Distance>, Size2Distance
{
    private Distance width;
    private Distance height;

    private MutableSize2Distance(Distance width, Distance height)
    {
        PreCondition.assertNotNull(width, "width");
        PreCondition.assertGreaterThanOrEqualTo(width, Distance.zero, "width");
        PreCondition.assertNotNull(height, "height");
        PreCondition.assertGreaterThanOrEqualTo(height, Distance.zero, "height");

        this.width = width;
        this.height = height;
    }

    public static MutableSize2Distance create()
    {
        return MutableSize2Distance.create(Distance.zero, Distance.zero);
    }

    public static MutableSize2Distance create(Distance width, Distance height)
    {
        return new MutableSize2Distance(width, height);
    }

    @Override
    public Distance getWidth()
    {
        return this.width;
    }

    public MutableSize2Distance setWidth(Distance width)
    {
        PreCondition.assertNotNull(width, "width");
        PreCondition.assertGreaterThanOrEqualTo(width, Distance.zero, "width");

        this.width = width;

        return this;
    }

    @Override
    public Distance getHeight()
    {
        return this.height;
    }

    public MutableSize2Distance setHeight(Distance height)
    {
        PreCondition.assertNotNull(height, "height");
        PreCondition.assertGreaterThanOrEqualTo(height, Distance.zero, "height");

        this.height = height;

        return this;
    }

    public MutableSize2Distance set(Distance width, Distance height)
    {
        PreCondition.assertNotNull(width, "width");
        PreCondition.assertGreaterThanOrEqualTo(width, Distance.zero, "width");
        PreCondition.assertNotNull(height, "height");
        PreCondition.assertGreaterThanOrEqualTo(height, Distance.zero, "height");

        this.width = width;
        this.height = height;

        return this;
    }
}
