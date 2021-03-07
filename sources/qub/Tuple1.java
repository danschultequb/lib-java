package qub;

public class Tuple1<T1> implements Tuple
{
    private final T1 value1;

    private Tuple1(T1 value1)
    {
        this.value1 = value1;
    }

    public static <T1> Tuple1<T1> create(T1 value1)
    {
        return new Tuple1<>(value1);
    }

    @Override
    public Indexable<?> getValues()
    {
        return Indexable.create(this.value1);
    }

    public T1 getValue1()
    {
        return this.value1;
    }

    @Override
    public boolean equals(Object rhs)
    {
        return Tuple.equals(this, rhs);
    }

    @Override
    public String toString()
    {
        return Tuple.toString(this);
    }
}
