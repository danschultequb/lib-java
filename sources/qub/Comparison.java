package qub;

public enum Comparison
{
    LessThan,

    Equal,

    GreaterThan;

    public static Comparison from(double value)
    {
        return value < 0 ? LessThan : value == 0 ? Equal : GreaterThan;
    }
}
