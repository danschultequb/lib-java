package qub;

public class MutablePoint2Integer extends Point2Base<Integer> implements MutablePoint2<Integer>, Point2Integer
{
    private int x;
    private int y;

    private MutablePoint2Integer(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    public static MutablePoint2Integer create()
    {
        return MutablePoint2Integer.create(0, 0);
    }

    public static MutablePoint2Integer create(int x, int y)
    {
        return new MutablePoint2Integer(x, y);
    }

    public static MutablePoint2Integer create(Point2<Integer> point)
    {
        PreCondition.assertNotNull(point, "point");

        return MutablePoint2Integer.create(point.getX(), point.getY());
    }

    public static MutablePoint2Integer create(Point2Integer point)
    {
        PreCondition.assertNotNull(point, "point");

        return MutablePoint2Integer.create(point.getXAsInt(), point.getYAsInt());
    }

    @Override
    public int getXAsInt()
    {
        return this.x;
    }

    public MutablePoint2Integer setX(int x)
    {
        this.x = x;

        return this;
    }

    @Override
    public MutablePoint2Integer setX(Integer x)
    {
        PreCondition.assertNotNull(x, "x");

        return this.setX(x.intValue());
    }

    @Override
    public int getYAsInt()
    {
        return this.y;
    }

    public MutablePoint2Integer setY(int y)
    {
        this.y = y;

        return this;
    }

    @Override
    public MutablePoint2Integer setY(Integer y)
    {
        PreCondition.assertNotNull(y, "y");

        return this.setY(y.intValue());
    }

    @Override
    public MutablePoint2Integer set(Integer x, Integer y)
    {
        PreCondition.assertNotNull(x, "x");
        PreCondition.assertNotNull(y, "y");

        return this.set(x.intValue(), y.intValue());
    }

    public MutablePoint2Integer set(int x, Integer y)
    {
        PreCondition.assertNotNull(y, "y");

        return this.set(x, y.intValue());
    }

    public MutablePoint2Integer set(Integer x, int y)
    {
        PreCondition.assertNotNull(x, "x");

        return this.set(x.intValue(), y);
    }

    public MutablePoint2Integer set(int x, int y)
    {
        this.x = x;
        this.y = y;

        return this;
    }
}
