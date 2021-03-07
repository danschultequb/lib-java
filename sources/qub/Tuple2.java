package qub;

public class Tuple2<T1,T2> implements Tuple
{
    private final T1 value1;
    private final T2 value2;

    private Tuple2(T1 value1, T2 value2)
    {
        this.value1 = value1;
        this.value2 = value2;
    }

    public static <T1,T2> Tuple2<T1,T2> create(T1 value1, T2 value2)
    {
        return new Tuple2<>(value1, value2);
    }

    @Override
    public Indexable<?> getValues()
    {
        return Indexable.create(this.value1, this.value2);
    }

    public T1 getValue1()
    {
        return this.value1;
    }

    public T2 getValue2()
    {
        return this.value2;
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
