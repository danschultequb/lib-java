package qub;

public class MutablePoint2Integer extends Point2Base<Integer> implements Point2Integer
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
    public int getYAsInt()
    {
        return this.y;
    }

    public MutablePoint2Integer setY(int y)
    {
        this.y = y;

        return this;
    }
}
