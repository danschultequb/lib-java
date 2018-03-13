package qub;

public class Integers
{
    Integers()
    {
    }

    /**
     * Compare the two provided ints.
     * @param lhs The first int to compare.
     * @param rhs The second int to compare.
     * @return The comparison of the two ints.
     */
    public static Comparison compare(int lhs, int rhs)
    {
        return Comparison.from(lhs - rhs);
    }

    /**
     * Compare the two provided Integers.
     * @param lhs The first Integer to compare.
     * @param rhs The second Integer to compare.
     * @return The comparison of the two Integers.
     */
    public static Comparison compare(Integer lhs, Integer rhs)
    {
        return lhs == rhs ? Comparison.Equal :
                lhs == null ? Comparison.LessThan :
                rhs == null ? Comparison.GreaterThan :
                Integers.compare(lhs.intValue(), rhs.intValue());
    }
}
