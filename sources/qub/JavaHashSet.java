package qub;

public class JavaHashSet<T> implements MutableSet<T>
{
    private final java.util.HashSet<T> javaSet;

    private JavaHashSet()
    {
        this.javaSet = new java.util.HashSet<>();
    }

    public static <T> JavaHashSet<T> create()
    {
        return new JavaHashSet<>();
    }

    @Override
    public Iterator<T> iterate()
    {
        return JavaIteratorToIteratorAdapter.create(this.javaSet.iterator());
    }

    @Override
    public boolean add(T value)
    {
        return this.javaSet.add(value);
    }

    @Override
    public Result<T> remove(T value)
    {
        return Result.create(() ->
        {
            if (!this.javaSet.remove(value))
            {
                throw new NotFoundException("Could not find the value " + value + ".");
            }
            return value;
        });
    }

    @Override
    public void clear()
    {
        this.javaSet.clear();
    }

    @Override
    public boolean equals(Object rhs)
    {
        return Set.equals(this, rhs);
    }

    @Override
    public String toString()
    {
        return Set.toString(this);
    }
}
