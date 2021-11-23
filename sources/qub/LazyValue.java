package qub;

/**
 * A {@link Value} type that lazily creates its value.
 * @param <T> The type of value returned by this {@link LazyValue}.
 */
public class LazyValue<T> implements Value<T>
{
    private Function0<T> creator;
    private boolean hasCreatorRun;
    private T value;

    private LazyValue(Function0<T> creator)
    {
        this.creator = creator;
    }

    public static <T> LazyValue<T> create()
    {
        return new LazyValue<>(null);
    }

    public static <T> LazyValue<T> create(T value)
    {
        return LazyValue.create(() -> value);
    }

    public static <T> LazyValue<T> create(Function0<T> creator)
    {
        PreCondition.assertNotNull(creator, "creator");

        return new LazyValue<>(creator);
    }

    @Override
    public boolean hasValue()
    {
        return this.creator != null;
    }

    /**
     * Whether the creator function has been run.
     * @return Whether the creator function has been run.
     */
    public boolean hasCreatorRun()
    {
        return this.hasCreatorRun;
    }

    @Override
    public T get()
    {
        PreCondition.assertTrue(this.hasValue(), "this.hasValue()");

        if (!this.hasCreatorRun)
        {
            this.value = this.creator.run();
            this.hasCreatorRun = true;
        }
        return this.value;
    }

    @Override
    public LazyValue<T> clear()
    {
        this.creator = null;
        this.hasCreatorRun = false;
        this.value = null;

        return this;
    }

    @Override
    public LazyValue<T> set(T value)
    {
        return this.set(() -> value);
    }

    public LazyValue<T> set(Function0<T> creator)
    {
        PreCondition.assertNotNull(creator, "creator");

        this.creator = creator;
        this.hasCreatorRun = false;
        this.value = null;

        return this;
    }

    @Override
    public T getOrSet(Function0<T> creator)
    {
        PreCondition.assertNotNull(creator, "creator");

        if (!this.hasValue())
        {
            this.set(creator);
        }
        return this.get();
    }
}
