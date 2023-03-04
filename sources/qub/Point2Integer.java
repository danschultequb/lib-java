package qub;

/**
 * An implementation of {@link Point2} that uses ints.
 */
public interface Point2Integer extends Point2<Integer>
{
    public static final Point2Integer zero = Point2Integer.create(0, 0);

    public static MutablePoint2Integer create()
    {
        return MutablePoint2Integer.create();
    }

    public static MutablePoint2Integer create(int x, int y)
    {
        return MutablePoint2Integer.create(x, y);
    }

    @Override
    public default Integer getX()
    {
        return this.getXAsInt();
    }

    /**
     * Get the x-coordinate of this {@link Point2Integer}.
     */
    public int getXAsInt();

    @Override
    public default Integer getY()
    {
        return this.getYAsInt();
    }

    /**
     * Get the y-coordinate of the {@link Point2Integer}.
     */
    public int getYAsInt();

    @Override
    public default JSONObject toJson()
    {
        return JSONObject.create()
            .setNumber(Point2.xPropertyName, this.getXAsInt())
            .setNumber(Point2.yPropertyName, this.getYAsInt());
    }
}
