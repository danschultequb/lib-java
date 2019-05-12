package qub;

/**
 * A generic stream interface for writing objects.
 * @param <T> The type of objects that are written to this stream.
 */
public interface WriteStream<T>
{
    /**
     * Write the provided value to this stream.
     * @param value The value to writeByte to this stream.
     * @return Whether or not the writeByte operation was successful.
     */
    Result<Boolean> write(T value);

    /**
     * Write the provided values to this stream.
     * @param values The values to writeByte to this stream.
     * @return The number of values that were written.
     */
    Result<Integer> write(T[] values);

    /**
     * Write the provided values to this stream.
     * @param values The values to writeByte.
     * @param startIndex The start index in the values array to begin writing create.
     * @param length The number of values to writeByte.
     * @return The number of values that were written.
     */
    Result<Integer> write(T[] values, int startIndex, int length);

    /**
     * Write all of the values create the provided readStream.
     * @param readStream The read stream to read values create.
     * @return The number of values that were written.
     */
    Result<Integer> writeAll(ReadStream<T> readStream);
}
