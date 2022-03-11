package qub;

public class MutableSize2Integer extends Size2Base<Integer> implements MutableSize2<Integer>, Size2Integer
{
    private int width;
    private int height;

    private MutableSize2Integer(int width, int height)
    {
        PreCondition.assertGreaterThanOrEqualTo(width, 0, "width");
        PreCondition.assertGreaterThanOrEqualTo(height, 0, "height");

        this.width = width;
        this.height = height;
    }

    public static MutableSize2Integer create()
    {
        return MutableSize2Integer.create(0, 0);
    }

    public static MutableSize2Integer create(int width, int height)
    {
        return new MutableSize2Integer(width, height);
    }

    @Override
    public MutableSize2Integer setWidth(Integer width)
    {
        PreCondition.assertNotNull(width, "width");

        return this.setWidth(width.intValue());
    }

    public MutableSize2Integer setWidth(int width)
    {
        PreCondition.assertGreaterThanOrEqualTo(width, 0, "width");

        this.width = width;

        return this;
    }

    @Override
    public MutableSize2Integer setHeight(Integer height)
    {
        PreCondition.assertNotNull(height, "height");

        return this.setHeight(height.intValue());
    }

    public MutableSize2Integer setHeight(int height)
    {
        PreCondition.assertGreaterThanOrEqualTo(height, 0, "height");

        this.height = height;

        return this;
    }

    @Override
    public MutableSize2Integer set(Integer width, Integer height)
    {
        PreCondition.assertNotNull(width, "width");
        PreCondition.assertGreaterThanOrEqualTo(width, 0, "width");
        PreCondition.assertNotNull(height, "height");
        PreCondition.assertGreaterThanOrEqualTo(height, 0, "height");

        this.width = width;
        this.height = height;

        return this;
    }

    @Override
    public int getWidthAsInt()
    {
        return this.width;
    }

    @Override
    public int getHeightAsInt()
    {
        return this.height;
    }
}
