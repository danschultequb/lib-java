package qub;

public interface Comparable<T>
{
    Comparison compareTo(T value);

    boolean lessThan(T value);

    boolean lessThanOrEqualTo(T value);

    boolean greaterThanOrEqualTo(T value);

    boolean greaterThan(T value);
}
