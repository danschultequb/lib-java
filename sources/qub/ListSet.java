package qub;

public class ListSet<T> implements Set<T>
{
    private final List<T> values;

    public ListSet()
    {
        this.values = List.create();
    }

    @Override
    public void add(T value)
    {
        if (!values.contains(value))
        {
            values.add(value);
        }
    }

    @Override
    public Result<Boolean> remove(T value)
    {
        final boolean result = values.contains(value);
        if (result)
        {
            values.remove(value);
        }
        return result ? Result.successTrue() : Result.error(new NotFoundException("Could not find the value " + value + "."));
    }

    @Override
    public void clear()
    {
        values.clear();
    }

    @Override
    public Iterator<T> iterate()
    {
        return values.iterate();
    }

    @Override
    public String toString()
    {
        return Iterable.toString(this);
    }

    @Override
    public boolean equals(Object rhs)
    {
        return Iterable.equals(this, rhs);
    }
}
