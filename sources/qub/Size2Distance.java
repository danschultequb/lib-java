package qub;

/**
 * A {@link Size2} that contains {@link Distance} components.
 */
public interface Size2Distance extends Size2<Distance>
{
    /**
     * An empty {@link Size2Distance}.
     */
    public static final Size2Distance zero = Size2Distance.create();

    /**
     * Create a new {@link MutableSize2Distance}.
     */
    public static MutableSize2Distance create()
    {
        return MutableSize2Distance.create();
    }

    /**
     * Create a new {@link MutableSize2Distance} with the provided width and height.
     * @param width The width of the new {@link MutableSize2Distance}.
     * @param height The height of the new {@link MutableSize2Distance}.
     */
    public static MutableSize2Distance create(Distance width, Distance height)
    {
        return MutableSize2Distance.create(width, height);
    }
}
