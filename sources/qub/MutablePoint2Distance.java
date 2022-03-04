package qub;

public class MutablePoint2Distance extends Point2Base<Distance> implements MutablePoint2<Distance>, Point2Distance
{
    private Distance x;
    private Distance y;

    private MutablePoint2Distance(Distance x, Distance y)
    {
        PreCondition.assertNotNull(x, "x");
        PreCondition.assertNotNull(y, "y");

        this.x = x;
        this.y = y;
    }

    public static MutablePoint2Distance create()
    {
        return MutablePoint2Distance.create(Distance.zero, Distance.zero);
    }

    public static MutablePoint2Distance create(Distance x, Distance y)
    {
        return new MutablePoint2Distance(x, y);
    }

    @Override
    public Distance getX()
    {
        return this.x;
    }

    @Override
    public MutablePoint2Distance setX(Distance x)
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

    @Override
    public MutablePoint2Distance setY(Distance y)
    {
        PreCondition.assertNotNull(y, "y");

        this.y = y;

        return this;
    }
}
