package qub;

public class Tuple4<T1,T2,T3,T4> implements Tuple
{
    private final T1 value1;
    private final T2 value2;
    private final T3 value3;
    private final T4 value4;

    private Tuple4(T1 value1, T2 value2, T3 value3, T4 value4)
    {
        this.value1 = value1;
        this.value2 = value2;
        this.value3 = value3;
        this.value4 = value4;
    }

    public static <T1,T2,T3,T4> Tuple4<T1,T2,T3,T4> create(T1 value1, T2 value2, T3 value3, T4 value4)
    {
        return new Tuple4<>(value1, value2, value3, value4);
    }

    @Override
    public Indexable<?> getValues()
    {
        return Indexable.create(this.value1, this.value2, this.value3, this.value4);
    }

    public T1 getValue1()
    {
        return this.value1;
    }

    public T2 getValue2()
    {
        return this.value2;
    }

    public T3 getValue3()
    {
        return this.value3;
    }

    public T4 getValue4()
    {
        return this.value4;
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
