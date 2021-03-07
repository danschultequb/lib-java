package qub;

public class Tuple3<T1,T2,T3> implements Tuple
{
    private final T1 value1;
    private final T2 value2;
    private final T3 value3;

    private Tuple3(T1 value1, T2 value2, T3 value3)
    {
        this.value1 = value1;
        this.value2 = value2;
        this.value3 = value3;
    }

    public static <T1,T2,T3> Tuple3<T1,T2,T3> create(T1 value1, T2 value2, T3 value3)
    {
        return new Tuple3<>(value1, value2, value3);
    }

    @Override
    public Indexable<?> getValues()
    {
        return Indexable.create(this.value1, this.value2, this.value3);
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
