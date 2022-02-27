package qub;

public class MutablePoint2D implements Point2D
{
    private Distance x;
    private Distance y;

    private MutablePoint2D(Distance x, Distance y)
    {
        PreCondition.assertNotNull(x, "x");
        PreCondition.assertNotNull(y, "y");

        this.x = x;
        this.y = y;
    }

    public static MutablePoint2D create()
    {
        return MutablePoint2D.create(Distance.zero, Distance.zero);
    }

    public static MutablePoint2D create(Distance x, Distance y)
    {
        return new MutablePoint2D(x, y);
    }

    @Override
    public Distance getX()
    {
        return this.x;
    }

    public MutablePoint2D setX(Distance x)
    {
        PreCondition.assertNotNull(x, "x");

        this.x = x;

        return this;
    }

    @Override
    public Distance getY()
    {
        return this.y;
    }

    public MutablePoint2D setY(Distance y)
    {
        PreCondition.assertNotNull(y, "y");

        this.y = y;

        return this;
    }

    @Override
    public String toString()
    {
        return Point2D.toString(this);
    }

    @Override
    public boolean equals(Object rhs)
    {
        return Point2D.equals(this, rhs);
    }

    @Override
    public int hashCode()
    {
        return Point2D.hashCode(this);
    }
}
