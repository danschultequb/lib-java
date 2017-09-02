package qub;

/**
 * An Iterator that will iterate over no more than a fixed number of values from an inner Iterator.
 * @param <T> The type of value that this Iterator returns.
 */
class TakeIterator<T> implements Iterator<T>
{
    private final Iterator<T> innerIterator;
    private final int toTake;

    private int taken;

    TakeIterator(Iterator<T> innerIterator, int toTake)
    {
        this.innerIterator = innerIterator;
        this.toTake = toTake;
        taken = innerIterator.hasCurrent() ? 1 : 0;
    }

    @Override
    public boolean hasStarted()
    {
        return taken > 0 || innerIterator.hasStarted();
    }

    @Override
    public boolean hasCurrent() {
        return toTake > 0 && taken <= toTake && innerIterator.hasCurrent();
    }

    @Override
    public T getCurrent() {
        return hasCurrent() ? innerIterator.getCurrent() : null;
    }

    @Override
    public boolean next() {
        ++taken;
        return toTake > 0 && taken <= toTake && innerIterator.next();
    }
}
