package qub;

/**
 * A basic {@link MutableSize2Integer} implementation.
 */
public class BasicMutableSize2Integer extends Size2.Base<Integer> implements MutableSize2Integer.Typed<BasicMutableSize2Integer>
{
    private int width;
    private int height;

    private BasicMutableSize2Integer(int width, int height)
    {
        this.set(width, height);
    }

    /**
     * Create a new {@link BasicMutableSize2Integer}.
     */
    public static BasicMutableSize2Integer create()
    {
        return BasicMutableSize2Integer.create(0, 0);
    }

    /**
     * Create a new {@link BasicMutableSize2Integer} with the provided width and height.
     * @param width The width of the new {@link BasicMutableSize2Integer}.
     * @param height The height of the new {@link BasicMutableSize2Integer}.
     */
    public static BasicMutableSize2Integer create(int width, int height)
    {
        return new BasicMutableSize2Integer(width, height);
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
