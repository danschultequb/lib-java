package qub;

/**
 * A DynamicDistance implementation that will always have the same distance.
 */
public class FixedDynamicDistance implements DynamicDistance
{
    private final Distance distance;

    private FixedDynamicDistance(Distance distance)
    {
        PreCondition.assertNotNull(distance, "distance");

        this.distance = distance;
    }

    /**
     * Create a new FixedDynamicDistance from the provided Distance.
     * @param distance The Distance that the new FixedDynamicDistance will return.
     * @return The new FixedDynamicDistance.
     */
    public static FixedDynamicDistance create(Distance distance)
    {
        return new FixedDynamicDistance(distance);
    }

    @Override
    public Distance getValue()
    {
        return this.distance;
    }

    @Override
    public Disposable onChanged(Action0 callback)
    {
        PreCondition.assertNotNull(callback, "callback");

        return Disposable.create();
    }
}
