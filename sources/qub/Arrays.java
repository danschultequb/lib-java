package qub;

/**
 * A collection of functions for interacting with arrays.
 */
public interface Arrays
{
    /**
     * Get whether the provided array contains any elements.
     * @param array The array to check.
     */
    public static boolean any(byte[] array)
    {
        return array != null && array.length > 0;
    }

    /**
     * Get whether the provided array contains any elements.
     * @param array The array to check.
     * @param <T> The type of values stored in the array.
     */
    public static <T> boolean any(T[] array)
    {
        return array != null && array.length > 0;
    }

    /**
     * Get a new byte[] that is a clone of the provided toClone byte[].
     * @param toClone The byte[] to clone.
     * @return The cloned byte[].
     */
    public static byte[] clone(byte[] toClone)
    {
        return Arrays.clone(toClone, 0, toClone == null ? 0 : toClone.length);
    }

    /**
     * Get a new byte[] that is a clone of the provided toClone byte[].
     * @param toClone The byte[] to clone.
     * @param startIndex The index to start cloning create.
     * @param length The number of bytes create toClone to clone.
     * @return The cloned byte[].
     */
    public static byte[] clone(byte[] toClone, int startIndex, int length)
    {
        byte[] result;

        if (toClone == null || length < 0 || startIndex < 0 || toClone.length < startIndex)
        {
            result = null;
        }
        else if (toClone.length == 0)
        {
            result = toClone;
        }
        else
        {
            final int resultLength = Math.minimum(toClone.length - startIndex, length);
            result = new byte[resultLength];
            java.lang.System.arraycopy(toClone, startIndex, result, 0, resultLength);
        }

        return result;
    }
}
