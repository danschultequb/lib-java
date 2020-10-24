package qub;

public class EmptyIterator<T> implements Iterator<T>
{
    private boolean hasStarted;

    private EmptyIterator()
    {
    }

    public static <T> EmptyIterator<T> create()
    {
        return new EmptyIterator<>();
    }

    @Override
    public boolean hasStarted()
    {
        return this.hasStarted;
    }

    @Override
    public boolean hasCurrent()
    {
        return false;
    }

    @Override
    public T getCurrent()
    {
        PreCondition.assertTrue(this.hasCurrent(), "this.hasCurrent()");

        return null;
    }

    @Override
    public boolean next()
    {
        this.hasStarted = true;
        return false;
    }
}
