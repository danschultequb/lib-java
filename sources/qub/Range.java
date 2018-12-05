package qub;

public class Range<T>
{
    private final T lowerBound;
    private final T upperBound;
    private final Function2<T,T,Boolean> comparer;

    private Range(T lowerBound, T upperBound, Function2<T,T,Boolean> comparer)
    {
        PreCondition.assertNotNull(comparer, "comparer");

        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
        this.comparer = comparer;
    }

    public static <T> Range<T> greaterThanOrEqualTo(T lowerBound, Function2<T,T,Boolean> comparer)
    {
        PreCondition.assertNotNull(lowerBound, "lowerBound");
        PreCondition.assertNotNull(comparer, "comparer");

        return new Range<>(lowerBound, null, comparer);
    }

    public static Range<Integer> greaterThanOrEqualTo(int lowerBound)
    {
        return greaterThanOrEqualTo(lowerBound, Comparer::lessThanOrEqualTo);
    }

    public static <T> Range<T> lessThanOrEqualTo(T upperBound, Function2<T,T,Boolean> comparer)
    {
        PreCondition.assertNotNull(upperBound, "upperBound");
        PreCondition.assertNotNull(comparer, "comparer");

        return new Range<>(null, upperBound, comparer);
    }

    public static Range<Integer> lessThanOrEqualTo(int lowerBound)
    {
        return lessThanOrEqualTo(lowerBound, Comparer::lessThanOrEqualTo);
    }

    public static Range<Integer> between(int lowerBound, int upperBound)
    {
        return between(lowerBound, upperBound, Comparer::lessThanOrEqualTo);
    }

    public static <T extends Comparable<T>> Range<T> between(T lowerBound, T upperBound)
    {
        return between(lowerBound, upperBound, Comparer::lessThanOrEqualTo);
    }

    public static <T> Range<T> between(T lowerBound, T upperBound, Function2<T,T,Boolean> comparer)
    {
        PreCondition.assertNotNull(lowerBound, "lowerBound");
        PreCondition.assertNotNull(upperBound, "upperBound");
        PreCondition.assertNotNull(comparer, "comparer");

        return new Range<>(lowerBound, upperBound, comparer);
    }

    public T getLowerBound()
    {
        return lowerBound;
    }

    public T getUpperBound()
    {
        return upperBound;
    }

    public boolean contains(T value)
    {
        PreCondition.assertNotNull(value, "value");

        boolean result;
        if (lowerBound == null)
        {
            result = comparer.run(value, upperBound);
        }
        else if (upperBound == null)
        {
            result = comparer.run(lowerBound, value);
        }
        else
        {
            result = comparer.run(lowerBound, value) && comparer.run(value, upperBound);
        }
        return result;
    }

    @Override
    public String toString()
    {
        String result;
        if (lowerBound == null)
        {
            result = "(";
        }
        else
        {
            result = contains(lowerBound) ? "[" : "(";
            result += lowerBound.toString();
        }

        result += "..";

        if (upperBound == null)
        {
            result += ")";
        }
        else
        {
            result += upperBound.toString();
            result += contains(upperBound) ? "]" : ")";
        }

        PostCondition.assertNotNullAndNotEmpty(result, "result");

        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(Object rhs)
    {
        return rhs instanceof Range && equals((Range<T>)rhs);
    }

    public boolean equals(Range<T> rhs)
    {
        return rhs != null &&
           comparer == rhs.comparer &&
           comparer.run(lowerBound, rhs.lowerBound) == comparer.run(rhs.lowerBound, lowerBound) &&
           comparer.run(upperBound, rhs.upperBound) == comparer.run(rhs.upperBound, upperBound);
    }

    @Override
    public int hashCode()
    {
        return Hash.getHashCode(comparer, lowerBound, upperBound);
    }
}
