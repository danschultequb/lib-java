package qub;

public class ListSet<T> implements Set<T>
{
    private final List<T> values;

    private ListSet()
    {
        this.values = List.create();
    }

    public static <T> ListSet<T> create()
    {
        return new ListSet<>();
    }

    @Override
    public ListSet<T> add(T value)
    {
        if (!this.values.contains(value))
        {
            this.values.add(value);
        }
        return this;
    }

    @Override
    public Result<Void> remove(T value)
    {
        return Result.create(() ->
        {
            if (!this.values.remove(value))
            {
                throw new NotFoundException("Could not find the value " + value + ".");
            }
        });
    }

    @Override
    public ListSet<T> clear()
    {
        this.values.clear();
        return this;
    }

    @Override
    public Iterator<T> iterate()
    {
        return this.values.iterate();
    }

    @Override
    public String toString()
    {
        return Set.toString(this);
    }

    @Override
    public boolean equals(Object rhs)
    {
        return Set.equals(this, rhs);
    }
}
