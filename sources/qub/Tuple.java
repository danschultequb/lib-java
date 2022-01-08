package qub;

public interface Tuple
{
    static <T1> Tuple1<T1> create(T1 value1)
    {
        return Tuple1.create(value1);
    }

    static <T1,T2> Tuple2<T1,T2> create(T1 value1, T2 value2)
    {
        return Tuple2.create(value1, value2);
    }

    static <T1,T2,T3> Tuple3<T1,T2,T3> create(T1 value1, T2 value2, T3 value3)
    {
        return Tuple3.create(value1, value2, value3);
    }

    static <T1,T2,T3,T4> Tuple4<T1,T2,T3,T4> create(T1 value1, T2 value2, T3 value3, T4 value4)
    {
        return Tuple4.create(value1, value2, value3, value4);
    }

    static <T1,T2,T3,T4,T5> Tuple5<T1,T2,T3,T4,T5> create(T1 value1, T2 value2, T3 value3, T4 value4, T5 value5)
    {
        return Tuple5.create(value1, value2, value3, value4, value5);
    }

    static <T1,T2,T3,T4,T5,T6> Tuple6<T1,T2,T3,T4,T5,T6> create(T1 value1, T2 value2, T3 value3, T4 value4, T5 value5, T6 value6)
    {
        return Tuple6.create(value1, value2, value3, value4, value5, value6);
    }

    static <T1,T2,T3,T4,T5,T6,T7> Tuple7<T1,T2,T3,T4,T5,T6,T7> create(T1 value1, T2 value2, T3 value3, T4 value4, T5 value5, T6 value6, T7 value7)
    {
        return Tuple7.create(value1, value2, value3, value4, value5, value6, value7);
    }

    /**
     * Get the values contained in this Tuple.
     * @return The values contained in this Tuple.
     */
    Indexable<?> getValues();

    static boolean equals(Tuple tuple, Object rhs)
    {
        PreCondition.assertNotNull(tuple, "tuple");

        return rhs != null &&
            tuple.getClass().equals(rhs.getClass()) &&
            tuple.getValues().equals(((Tuple)rhs).getValues());
    }

    static String toString(Tuple tuple)
    {
        PreCondition.assertNotNull(tuple, "tuple");

        return tuple.getValues().toString();
    }
}
