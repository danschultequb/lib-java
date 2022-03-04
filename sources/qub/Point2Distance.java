package qub;

/**
 * A two-dimensional point object.
 */
public interface Point2Distance extends Point2<Distance>
{
    Point2Distance zero = Point2Distance.create(Distance.zero, Distance.zero);

    static MutablePoint2Distance create(Distance x, Distance y)
    {
        return MutablePoint2Distance.create(x, y);
    }
}
