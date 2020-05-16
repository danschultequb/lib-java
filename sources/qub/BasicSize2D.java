package qub;

/**
 * An immutable two-dimensional size.
 */
public class BasicSize2D implements Size2D
{
    public static final BasicSize2D zero = new BasicSize2D(Distance.zero, Distance.zero);

    private final Distance width;
    private final Distance height;

    private BasicSize2D(Distance width, Distance height)
    {
        PreCondition.assertNotNull(width, "width");
        PreCondition.assertGreaterThanOrEqualTo(width, Distance.zero, "width");
        PreCondition.assertNotNull(height, "height");
        PreCondition.assertGreaterThanOrEqualTo(height, Distance.zero, "height");

        this.width = width;
        this.height = height;
    }

    /**
     * Create a new BasicSize2D with the provided width and height.
     * @param width The width of the new BasicSize2D.
     * @param height The height of the new BasicSize2D.
     * @return The new BasicSize2D.
     */
    public static BasicSize2D create(Distance width, Distance height)
    {
        return new BasicSize2D(width, height);
    }

    /**
     * Get the width of this BasicSize2D.
     * @return The width of this BasicSize2D.
     */
    public Distance getWidth()
    {
        return width;
    }

    /**
     * Get the height of this BasicSize2D.
     * @return The height of this BasicSize2D.
     */
    public Distance getHeight()
    {
        return height;
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
