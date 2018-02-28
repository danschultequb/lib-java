package qub;

public interface Comparable<T>
{
    Comparison compareTo(T value);

    default boolean lessThan(T value)
    {
        return compareTo(value) == Comparison.LessThan;
    }

    default boolean lessThanOrEqualTo(T value)
    {
        return compareTo(value) != Comparison.GreaterThan;
    }

    default boolean greaterThanOrEqualTo(T value)
    {
        return compareTo(value) != Comparison.LessThan;
    }

    default boolean greaterThan(T value)
    {
        return compareTo(value) == Comparison.GreaterThan;
    }

    static <T> boolean equalTo(Comparable<T> lhs, T rhs)
    {
        return lhs == rhs || (lhs != null && lhs.compareTo(rhs) == Comparison.Equal);
    }
}
