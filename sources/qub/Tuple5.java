package qub;

public class Tuple5<T1,T2,T3,T4,T5> implements Tuple
{
    private final T1 value1;
    private final T2 value2;
    private final T3 value3;
    private final T4 value4;
    private final T5 value5;

    private Tuple5(T1 value1, T2 value2, T3 value3, T4 value4, T5 value5)
    {
        this.value1 = value1;
        this.value2 = value2;
        this.value3 = value3;
        this.value4 = value4;
        this.value5 = value5;
    }

    public static <T1,T2,T3,T4,T5> Tuple5<T1,T2,T3,T4,T5> create(T1 value1, T2 value2, T3 value3, T4 value4, T5 value5)
    {
        return new Tuple5<>(value1, value2, value3, value4, value5);
    }

    @Override
    public Indexable<?> getValues()
    {
        return Indexable.create(this.value1, this.value2, this.value3, this.value4, this.value5);
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

    public T5 getValue5()
    {
        return this.value5;
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
