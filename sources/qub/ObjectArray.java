package qub;

/**
 * An array class that contains a generic type of element.
 */
public class ObjectArray<T> extends Array<T>
{
    private final Object[] values;

    public ObjectArray(int count)
    {
        PreCondition.assertGreaterThanOrEqualTo(count, 0, "count");

        this.values = new Object[count];
    }

    public ObjectArray(Object[] values)
    {
        PreCondition.assertNotNull(values, "values");

        this.values = values;
    }

    @Override
    public int getCount()
    {
        return values.length;
    }

    @Override
    public void set(int index, T value)
    {
        PreCondition.assertIndexAccess(index, getCount(), "index");
        PreCondition.assertNotNull(value, "value");

        values[index] = value;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T get(int index)
    {
        PreCondition.assertIndexAccess(index, getCount(), "index");

        return (T)values[index];
    }
}
