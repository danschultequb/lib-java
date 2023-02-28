package qub;

public class BasicMutableSize2Distance extends Size2.Base<Distance> implements MutableSize2Distance.Typed<BasicMutableSize2Distance>
{
    private Distance width;
    private Distance height;

    private BasicMutableSize2Distance(Distance width, Distance height)
    {
        PreCondition.assertNotNull(width, "width");
        PreCondition.assertGreaterThanOrEqualTo(width, Distance.zero, "width");
        PreCondition.assertNotNull(height, "height");
        PreCondition.assertGreaterThanOrEqualTo(height, Distance.zero, "height");

        this.width = width;
        this.height = height;
    }

    public static BasicMutableSize2Distance create()
    {
        return BasicMutableSize2Distance.create(Distance.zero, Distance.zero);
    }

    public static BasicMutableSize2Distance create(Distance width, Distance height)
    {
        return new BasicMutableSize2Distance(width, height);
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
    public BasicMutableSize2Distance setWidth(Distance width)
    {
        PreCondition.assertNotNull(width, "width");
        PreCondition.assertGreaterThanOrEqualTo(width, Distance.zero, "width");

        this.width = width;

        return this;
    }

    @Override
    public BasicMutableSize2Distance setHeight(Distance height)
    {
        PreCondition.assertNotNull(height, "height");
        PreCondition.assertGreaterThanOrEqualTo(height, Distance.zero, "height");

        this.height = height;

        return this;
    }

    @Override
    public BasicMutableSize2Distance set(Distance width, Distance height)
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
