package qub;

/**
 * An immutable two-dimensional size.
 */
public interface Size2D extends ComparableWithError<Size2D>
{
    /**
     * An empty size.
     */
    Size2D zero = Size2D.create();

    /**
     * Create a new empty {@link MutableSize2D}.
     * @return The new empty {@link MutableSize2D}.
     */
    static MutableSize2D create()
    {
        return MutableSize2D.create();
    }

    /**
     * Create a new {@link MutableSize2D} with the provided width and height.
     * @param width The width of the new {@link MutableSize2D}.
     * @param height The height of the new {@link MutableSize2D}.
     * @return The new {@link MutableSize2D}.
     */
    static MutableSize2D create(Distance width, Distance height)
    {
        return MutableSize2D.create(width, height);
    }

    /**
     * Get the width of this {@link Size2D}.
     * @return The width of this {@link Size2D}.
     */
    Distance getWidth();

    /**
     * Get the height of this Size2D.
     * @return The height of this Size2D.
     */
    Distance getHeight();

    static String toString(Size2D size)
    {
        PreCondition.assertNotNull(size, "size");

        return JSONObject.create()
            .setString("width", size.getWidth().toString())
            .setString("height", size.getHeight().toString())
            .toString();
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

    @Override
    default Comparison compareWith(Size2D value)
    {
        return this.compareTo(value, Size2D.zero);
    }

    default Comparison compareTo(Size2D value, Distance marginOfError)
    {
        PreCondition.assertNotNull(marginOfError, "marginOfError");

        return this.compareTo(value, Size2D.create(marginOfError, marginOfError));
    }

    @Override
    default Comparison compareTo(Size2D value, Size2D marginOfError)
    {
        PreCondition.assertNotNull(marginOfError, "marginOfError");

        Comparison result;
        if (value == null)
        {
            result = Comparison.GreaterThan;
        }
        else
        {
            result = this.getWidth().compareTo(value.getWidth(), marginOfError.getWidth());
            if (result == Comparison.Equal)
            {
                result = this.getHeight().compareTo(value.getHeight(), marginOfError.getHeight());
            }
        }

        PostCondition.assertNotNull(result, "result");

        return result;
    }
}
