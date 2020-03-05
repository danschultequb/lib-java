package qub;

/**
 * A property that can change its value.
 * @param <T> The type of value that this property holds.
 */
public class MutableObjectProperty<T> extends ObjectValue<T> implements MutableProperty<T>
{
    private final RunnableEvent2<T,T> event;

    private MutableObjectProperty()
    {
        this.event = Event2.create();
    }

    private MutableObjectProperty(T initialValue)
    {
        super(initialValue);

        this.event = Event2.create();
    }

    public static <T> MutableObjectProperty<T> create()
    {
        return new MutableObjectProperty<>();
    }

    public static <T> MutableObjectProperty<T> create(T initialValue)
    {
        return new MutableObjectProperty<>(initialValue);
    }

    @Override
    public MutableObjectProperty<T> clear()
    {
        if (this.hasValue())
        {
            final T oldValue = this.get();
            super.clear();
            this.event.run(oldValue, null);
        }
        return this;
    }

    @Override
    public MutableObjectProperty<T> set(T value)
    {
        T oldValue;
        boolean changed;
        if (!this.hasValue())
        {
            oldValue = null;
            changed = true;
        }
        else
        {
            oldValue = this.get();
            changed = !Comparer.equal(oldValue, value);
        }

        super.set(value);

        if (changed)
        {
            this.event.run(oldValue, value);
        }

        return this;
    }

    @Override
    public Disposable subscribe(Action2<T,T> callback)
    {
        return this.event.subscribe(callback);
    }
}
