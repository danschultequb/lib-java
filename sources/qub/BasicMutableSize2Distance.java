package qub;

/**
 * A basic {@link MutableSize2Distance} implementation.
 */
public class BasicMutableSize2Distance extends Size2.Base<Distance> implements MutableSize2Distance.Typed<BasicMutableSize2Distance>
{
    private Distance width;
    private Distance height;

    private BasicMutableSize2Distance(Distance width, Distance height)
    {
        this.set(width, height);
    }

    /**
     * Create a new {@link BasicMutableSize2Distance}.
     */
    public static BasicMutableSize2Distance create()
    {
        return BasicMutableSize2Distance.create(Distance.zero, Distance.zero);
    }

    /**
     * Create a new {@link BasicMutableSize2Distance} with the provided width and height.
     * @param width The width of the new {@link BasicMutableSize2Distance}.
     * @param height The height of the new {@link BasicMutableSize2Distance}.
     */
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
