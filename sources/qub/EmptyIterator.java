package qub;

public class EmptyIterator<T> implements Iterator<T>
{
    private boolean hasStarted;

    @Override
    public boolean hasStarted()
    {
        return hasStarted;
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
        hasStarted = true;
        return false;
    }
}
