package qub;

/**
 * A {@link Size2} that contains {@link Integer} components.
 */
public interface Size2Integer extends Size2<Integer>
{
    /**
     * An empty {@link Size2Integer}.
     */
    public static final Size2Integer zero = Size2Integer.create(0, 0);

    public static MutableSize2Integer create()
    {
        return MutableSize2Integer.create();
    }

    public static MutableSize2Integer create(int width, int height)
    {
        return MutableSize2Integer.create(width, height);
    }

    @Override
    public default Integer getWidth()
    {
        return this.getWidthAsInt();
    }

    public int getWidthAsInt();

    @Override
    public default Integer getHeight()
    {
        return this.getHeightAsInt();
    }

    public int getHeightAsInt();
}
