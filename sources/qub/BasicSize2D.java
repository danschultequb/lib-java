package qub;

/**
 * An immutable two-dimensional size.
 */
public class BasicSize2D
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
     * Create a new BasicSize2D based on this BasicSize2D but with the provided width.
     * @param width The width of the new BasicSize2D.
     * @return A new BasicSize2D based on this BasicSize2D but with the provided width.
     */
    public BasicSize2D changeWidth(Distance width)
    {
        PreCondition.assertNotNull(width, "width");
        PreCondition.assertGreaterThanOrEqualTo(width, Distance.zero, "width");

        final BasicSize2D result = (this.getWidth().equals(width) ? this : BasicSize2D.create(width, this.getHeight()));

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(width, result.getWidth(), "result.getWidth()");

        return result;
    }

    /**
     * Get the height of this BasicSize2D.
     * @return The height of this BasicSize2D.
     */
    public Distance getHeight()
    {
        return height;
    }

    /**
     * Create a new BasicSize2D based on this BasicSize2D but with the provided height.
     * @param height The height of the new BasicSize2D.
     * @return A new BasicSize2D based on this BasicSize2D but with the provided height.
     */
    public BasicSize2D changeHeight(Distance height)
    {
        PreCondition.assertNotNull(height, "height");
        PreCondition.assertGreaterThanOrEqualTo(height, Distance.zero, "height");

        final BasicSize2D result = (this.getHeight().equals(height) ? this : new BasicSize2D(this.getWidth(), height));

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(height, result.getHeight(), "result.getHeight()");

        return result;
    }

    @Override
    public String toString()
    {
        return "{\"width\":\"" + this.getWidth() + "\",\"height\":\"" + this.getHeight() + "\"}";
    }

    @Override
    public boolean equals(Object rhs)
    {
        return rhs instanceof BasicSize2D && equals((BasicSize2D)rhs);
    }

    public boolean equals(BasicSize2D rhs)
    {
        return rhs != null &&
            width.equals(rhs.width) &&
            height.equals(rhs.height);
    }

    @Override
    public int hashCode()
    {
        return Hash.getHashCode(width, height);
    }
}
