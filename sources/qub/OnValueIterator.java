package qub;

public class OnValueIterator<T> extends IteratorWrapper<T>
{
    private final Action1<T> onValueAction;

    private OnValueIterator(Iterator<T> innerIterator, Action1<T> onValueAction)
    {
        super(innerIterator);

        PreCondition.assertNotNull(onValueAction, "onValueAction");

        this.onValueAction = onValueAction;
    }

    public static <T> OnValueIterator<T> create(Iterator<T> innerIterator, Action0 onValueAction)
    {
        PreCondition.assertNotNull(innerIterator, "innerIterator");
        PreCondition.assertNotNull(onValueAction, "onValueAction");

        return OnValueIterator.create(innerIterator, (T value) -> { onValueAction.run(); });
    }

    public static <T> OnValueIterator<T> create(Iterator<T> innerIterator, Action1<T> onValueAction)
    {
        return new OnValueIterator<>(innerIterator, onValueAction);
    }

    @Override
    public boolean next()
    {
        final boolean result = super.next();
        if (result && this.hasCurrent())
        {
            this.onValueAction.run(this.getCurrent());
        }
        return result;
    }
}
