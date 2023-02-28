package qub;

public class BasicMutableSize2Integer extends Size2.Base<Integer> implements MutableSize2Integer.Typed<BasicMutableSize2Integer>
{
    private int width;
    private int height;

    private BasicMutableSize2Integer(int width, int height)
    {
        PreCondition.assertGreaterThanOrEqualTo(width, 0, "width");
        PreCondition.assertGreaterThanOrEqualTo(height, 0, "height");

        this.width = width;
        this.height = height;
    }

    public static BasicMutableSize2Integer create()
    {
        return BasicMutableSize2Integer.create(0, 0);
    }

    public static BasicMutableSize2Integer create(int width, int height)
    {
        return new BasicMutableSize2Integer(width, height);
    }

    @Override
    public BasicMutableSize2Integer setWidth(Integer width)
    {
        PreCondition.assertNotNull(width, "width");

        return this.setWidth(width.intValue());
    }

    public BasicMutableSize2Integer setWidth(int width)
    {
        PreCondition.assertGreaterThanOrEqualTo(width, 0, "width");

        this.width = width;

        return this;
    }

    @Override
    public BasicMutableSize2Integer setHeight(Integer height)
    {
        PreCondition.assertNotNull(height, "height");

        return this.setHeight(height.intValue());
    }

    public BasicMutableSize2Integer setHeight(int height)
    {
        PreCondition.assertGreaterThanOrEqualTo(height, 0, "height");

        this.height = height;

        return this;
    }

    @Override
    public BasicMutableSize2Integer set(Integer width, Integer height)
    {
        PreCondition.assertNotNull(width, "width");
        PreCondition.assertGreaterThanOrEqualTo(width, 0, "width");
        PreCondition.assertNotNull(height, "height");
        PreCondition.assertGreaterThanOrEqualTo(height, 0, "height");

        return this.set(width.intValue(), height.intValue());
    }

    @Override
    public BasicMutableSize2Integer set(int width, int height)
    {
        PreCondition.assertGreaterThanOrEqualTo(width, 0, "width");
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
