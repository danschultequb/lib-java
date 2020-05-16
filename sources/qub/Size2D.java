package qub;

/**
 * An immutable two-dimensional size.
 */
public interface Size2D
{
    /**
     * Create a new Size2D with the provided width and height.
     * @param width The width of the new Size2D.
     * @param height The height of the new Size2D.
     * @return The new Size2D.
     */
    static Size2D create(Distance width, Distance height)
    {
        return BasicSize2D.create(width, height);
    }

    /**
     * Get the width of this Size2D.
     * @return The width of this Size2D.
     */
    Distance getWidth();

    /**
     * Create a new Size2D based on this Size2D but with the provided width.
     * @param width The width of the new Size2D.
     * @return A new Size2D based on this Size2D but with the provided width.
     */
    default Size2D changeWidth(Distance width)
    {
        PreCondition.assertNotNull(width, "width");
        PreCondition.assertGreaterThanOrEqualTo(width, Distance.zero, "width");

        final Size2D result = (this.getWidth().equals(width) ? this : Size2D.create(width, this.getHeight()));

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(width, result.getWidth(), "result.getWidth()");

        return result;
    }

    /**
     * Get the height of this Size2D.
     * @return The height of this Size2D.
     */
    Distance getHeight();

    /**
     * Create a new Size2D based on this Size2D but with the provided height.
     * @param height The height of the new Size2D.
     * @return A new Size2D based on this Size2D but with the provided height.
     */
    default Size2D changeHeight(Distance height)
    {
        PreCondition.assertNotNull(height, "height");
        PreCondition.assertGreaterThanOrEqualTo(height, Distance.zero, "height");

        final Size2D result = (this.getHeight().equals(height) ? this : Size2D.create(this.getWidth(), height));

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(height, result.getHeight(), "result.getHeight()");

        return result;
    }

    static String toString(Size2D size)
    {
        PreCondition.assertNotNull(size, "size");

        return "{\"width\":\"" + size.getWidth() + "\",\"height\":\"" + size.getHeight() + "\"}";
    }

    static boolean equals(Size2D lhs, Object rhs)
    {
        PreCondition.assertNotNull(lhs, "lhs");

        return rhs instanceof Size2D && lhs.equals((Size2D) rhs);
    }

    default boolean equals(Size2D rhs)
    {
        return rhs != null &&
            this.getWidth().equals(rhs.getWidth()) &&
            this.getHeight().equals(rhs.getHeight());
    }

    static int hashCode(Size2D size)
    {
        PreCondition.assertNotNull(size, "size");

        return Hash.getHashCode(size.getWidth(), size.getHeight());
    }
}
