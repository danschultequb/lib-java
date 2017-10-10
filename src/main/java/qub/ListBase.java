package qub;

public abstract class ListBase<T> extends IndexableBase<T> implements List<T>
{
    @Override
    public void addAll(T... values)
    {
        if (values != null && values.length > 0)
        {
            for (final T value : values)
            {
                add(value);
            }
        }
    }

    @Override
    public void addAll(Iterator<T> values)
    {
        if (values != null && values.any())
        {
            for (final T value : values)
            {
                add(value);
            }
        }
    }

    @Override
    public void addAll(Iterable<T> values)
    {
        if (values != null && values.any())
        {
            for (final T value : values)
            {
                add(value);
            }
        }
    }
}
