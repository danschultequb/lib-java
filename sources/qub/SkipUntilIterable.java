package qub;

public class SkipUntilIterable<T> extends IterableBase<T>
{
    private final Iterable<T> iterable;
    private final Function1<T,Boolean> condition;

    public SkipUntilIterable(Iterable<T> iterable, Function1<T,Boolean> condition)
    {
        this.iterable = iterable;
        this.condition = condition;
    }

    @Override
    public Iterator<T> iterate()
    {
        return new SkipUntilIterator<>(iterable.iterate(), condition);
    }
}
