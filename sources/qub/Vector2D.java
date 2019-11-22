package qub;

/**
 * A geometric vector that can be applied to other two-dimensional objects.
 */
public class Vector2D
{
    public static final Vector2D zero = new Vector2D(Length.zero, Length.zero);

    private final Length x;
    private final Length y;

    /**
     * Create a new Vector2D object.
     * @param x The x-coordinate of the vector.
     * @param y The y-coordinate of the vector.
     */
    public Vector2D(Length x, Length y)
    {
        PreCondition.assertNotNull(x, "x");
        PreCondition.assertNotNull(y, "y");

        this.x = x;
        this.y = y;
    }

    /**
     * Get the x-coordinate of the vector.
     * @return The x-coordinate of the vector.
     */
    public Length getX()
    {
        return x;
    }

    /**
     * Get the y-coordinate of the vector.
     * @return The y-coordinate of the vector.
     */
    public Length getY()
    {
        return y;
    }

    public Vector2D negate()
    {
        final Length resultX = x.negate();
        final Length resultY = y.negate();
        Vector2D result;
        if (resultX == x && resultY == y)
        {
            result = this;
        }
        else
        {
            result = new Vector2D(resultX, resultY);
        }

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    /**
     * Add this vector to the provided point.
     * @param rhs The point to add this vector to.
     * @return The sum of adding this vector to the provided point.
     */
    public Point2D plus(Point2D rhs)
    {
        PreCondition.assertNotNull(rhs, "rhs");

        final Length resultX = x.plus(rhs.getX());
        final Length resultY = y.plus(rhs.getY());
        final Point2D result = new Point2D(resultX, resultY);

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    /**
     * Add this vector to the provided point.
     * @param x The x-coordinate of the point to add this vector to.
     * @param y The y-coordinate of the point to add this vector to.
     * @return The sum of adding this vector to the provided point.
     */
    public Point2D plusPoint(Length x, Length y)
    {
        PreCondition.assertNotNull(x, "x");
        PreCondition.assertNotNull(y, "y");

        final Length resultX = this.x.plus(x);
        final Length resultY = this.y.plus(y);
        final Point2D result = new Point2D(resultX, resultY);

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    /**
     * Add this vector to the provided vector coordinates.
     * @param x The x-coordinate of the vector to add.
     * @param y The y-coordinate of the vector to add.
     * @return The sum of adding this vector and the provided vector coordinates.
     */
    public Vector2D plusVector(Length x, Length y)
    {
        PreCondition.assertNotNull(x, "x");
        PreCondition.assertNotNull(y, "y");

        final Length resultX = this.x.plus(x);
        final Length resultY = this.y.plus(y);
        final Vector2D result = new Vector2D(resultX, resultY);

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    /**
     * Add this vector to the provided vector.
     * @param rhs The vector to add to this vector.
     * @return The sum of adding this vector with the provided vector.
     */
    public Vector2D plus(Vector2D rhs)
    {
        PreCondition.assertNotNull(rhs, "rhs");

        final Length resultX = x.plus(rhs.x);
        final Length resultY = y.plus(rhs.y);
        final Vector2D result = new Vector2D(resultX, resultY);

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    @Override
    public String toString()
    {
        return "[" + x + "," + y + "]";
    }

    @Override
    public boolean equals(Object rhs)
    {
        return rhs instanceof Vector2D && equals((Vector2D)rhs);
    }

    /**
     * Get whether or not the provided vector is equal to this vector.
     * @param rhs The provided vector.
     * @return Whether or not the provided vector is equal to this vector.
     */
    public boolean equals(Vector2D rhs)
    {
        return rhs != null && x.equals(rhs.x) && y.equals(rhs.y);
    }

    @Override
    public int hashCode()
    {
        return x.hashCode() ^ y.hashCode();
    }
}
