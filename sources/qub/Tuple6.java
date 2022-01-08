package qub;

public class Tuple6<T1,T2,T3,T4,T5,T6> implements Tuple
{
    private final T1 value1;
    private final T2 value2;
    private final T3 value3;
    private final T4 value4;
    private final T5 value5;
    private final T6 value6;

    private Tuple6(T1 value1, T2 value2, T3 value3, T4 value4, T5 value5, T6 value6)
    {
        this.value1 = value1;
        this.value2 = value2;
        this.value3 = value3;
        this.value4 = value4;
        this.value5 = value5;
        this.value6 = value6;
    }

    public static <T1,T2,T3,T4,T5,T6> Tuple6<T1,T2,T3,T4,T5,T6> create(T1 value1, T2 value2, T3 value3, T4 value4, T5 value5, T6 value6)
    {
        return new Tuple6<>(value1, value2, value3, value4, value5, value6);
    }

    @Override
    public Indexable<?> getValues()
    {
        return Indexable.create(this.value1, this.value2, this.value3, this.value4, this.value5, this.value6);
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

    public T6 getValue6()
    {
        return this.value6;
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
