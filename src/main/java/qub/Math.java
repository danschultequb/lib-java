package qub;

/**
 * A collection of math related functions and function objects.
 */
public abstract class Math {
    /**
     * A function object for determining if an Integer is odd.
     */
    public static final Function1<Integer,Boolean> isOdd = new Function1<Integer, Boolean>() {
        @Override
        public Boolean run(Integer value) {
            return value != null && isOdd(value);
        }
    };

    /**
     * Get whether or not the provided value is odd.
     * @param value The value to check.
     * @return Whether or not the provided value is odd.
     */
    public static boolean isOdd(int value) {
        return value % 2 != 0;
    }

    /**
     * A function object for determining if an Integer is even.
     */
    public static final Function1<Integer,Boolean> isEven = new Function1<Integer, Boolean>() {
        @Override
        public Boolean run(Integer value) {
            return value != null && isEven(value);
        }
    };

    /**
     * Get whether or not the provided value is even.
     * @param value The value to check.
     * @return Whether or not the provided value is even.
     */
    public static boolean isEven(int value) {
        return value % 2 == 0;
    }
}
