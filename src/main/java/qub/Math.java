package qub;

/**
 * A collection of math related functions and function objects.
 */
public interface Math {
    /**
     * A function object for determining if an Integer is odd.
     */
    Function1<Integer,Boolean> isOdd = (Integer value) -> value != null && isOdd(value);

    /**
     * Get whether or not the provided value is odd.
     * @param value The value to check.
     * @return Whether or not the provided value is odd.
     */
    static boolean isOdd(int value) {
        return value % 2 != 0;
    }

    /**
     * A function object for determining if an Integer is even.
     */
    Function1<Integer,Boolean> isEven = (Integer value) -> value != null && isEven(value);

    /**
     * Get whether or not the provided value is even.
     * @param value The value to check.
     * @return Whether or not the provided value is even.
     */
    static boolean isEven(int value) {
        return value % 2 == 0;
    }
}
