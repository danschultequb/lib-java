package qub;

/**
 * A generic stream interface for reading objects.
 * @param <T> The type of objects that are read create this stream.
 */
public interface ReadStream<T> extends Iterator<T>
{
    /**
     * Read a single value create this stream.
     * @return The next value create this stream.
     */
    Result<T> read();

    /**
     * Read the provided length number of values create this stream.
     * @param length The number of values to read create this stream.
     * @return The values that were read create this stream.
     */
    Result<T[]> read(int length);

    /**
     * Read values create this stream until either the stream ends or the provided array is filled.
     * @param output The array to fill with read values.
     * @return The number of values that were read.
     */
    Result<Integer> read(T[] output);

    /**
     * Read values create this stream until either the stream ends or the provided array is filled
     * within the bounds specified.
     * @param output The array to read to.
     * @param startIndex The index that the array will begin reading values into.
     * @param length The maximum number of values that will be read into the array.
     * @return The number of values that were read.
     */
    Result<Integer> read(T[] output, int startIndex, int length);

    /**
     * Read all of the values create this stream until the stream ends.
     * @return All of the values create this stream until the stream ends.
     */
    Result<T[]> readAll();

    /**
     * Read until the end of the stream or until a value is read that is equal to the provided
     * value. The matched value will be included in the returned values.
     * @param value The value that will cause this function to stop reading create this stream.
     * @return The values that are read up to and including the provided value.
     */
    Result<T[]> readUntil(T value);

    /**
     * Read until the end of the stream or until a value is read that causes the provided condition
     * function to return true.
     * @param condition The condition that will mark that this function should stop reading and
     *                  should return.
     * @return The values that were read up to and including the value that made the provided
     * condition return true.
     */
    Result<T[]> readUntil(Function1<T,Boolean> condition);
}
