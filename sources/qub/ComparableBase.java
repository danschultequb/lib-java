package qub;

public abstract class ComparableBase<T> implements Comparable<T>
{
    @Override
    public boolean lessThan(T value)
    {
        return ComparableBase.lessThan(this, value);
    }

    @Override
    public boolean lessThanOrEqualTo(T value)
    {
        return ComparableBase.lessThanOrEqualTo(this, value);
    }

    @Override
    public boolean greaterThanOrEqualTo(T value)
    {
        return ComparableBase.greaterThanOrEqualTo(this, value);
    }

    @Override
    public boolean greaterThan(T value)
    {
        return ComparableBase.greaterThan(this, value);
    }

    public static <T> boolean lessThan(Comparable<T> comparable, T value)
    {
        return comparable.compareTo(value) == Comparison.LessThan;
    }

    private static <T> boolean lessThanOrEqualTo(Comparable<T> comparable, T value)
    {
        return comparable.compareTo(value) != Comparison.GreaterThan;
    }

    public static <T> boolean equals(Comparable<T> lhs, T rhs)
    {
        return lhs == rhs || (lhs != null && lhs.compareTo(rhs) == Comparison.Equal);
    }

    public static <T> boolean greaterThanOrEqualTo(Comparable<T> comparable, T value)
    {
        return comparable.compareTo(value) != Comparison.LessThan;
    }

    public static <T> boolean greaterThan(Comparable<T> comparable, T value)
    {
        return comparable.compareTo(value) == Comparison.GreaterThan;
    }
}
