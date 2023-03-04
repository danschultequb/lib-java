package qub;

/**
 * A two-dimensional point object.
 */
public interface Point2Distance extends Point2<Distance>
{
    Point2Distance zero = Point2Distance.create();

    public static MutablePoint2Distance create()
    {
        return MutablePoint2Distance.create(Distance.zero, Distance.zero);
    }

    public static MutablePoint2Distance create(Distance x, Distance y)
    {
        return MutablePoint2Distance.create(x, y);
    }

    @Override
    public default JSONObject toJson()
    {
        return JSONObject.create()
            .setString(Point2.xPropertyName, this.getX().toString())
            .setString(Point2.yPropertyName, this.getY().toString());
    }
}
