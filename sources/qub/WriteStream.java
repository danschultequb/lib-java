package qub;

/**
 * A generic stream interface for writing objects.
 * @param <T> The type of objects that are written to this stream.
 */
public interface WriteStream<T>
{
    /**
     * Get the AsyncRunner that this stream's asynchronous tasks will be scheduled on.
     * @return The AsyncRunner that this stream's asynchronous tasks will be scheduled on.
     */
    AsyncRunner getAsyncRunner();

    /**
     * Write the provided value to this stream.
     * @param value The value to write to this stream.
     * @return Whether or not the write operation was successful.
     */
    Result<Boolean> write(T value);

    /**
     * Write the provided value to this stream.
     * @param value The value to write to this stream.
     * @return Whether or not the write operation was successful.
     */
    AsyncFunction<Result<Boolean>> writeAsync(T value);

    /**
     * Write the provided values to this stream.
     * @param values The values to write to this stream.
     * @return The number of values that were written.
     */
    Result<Integer> write(T[] values);

    /**
     * Write the provided values to this stream.
     * @param values The values to write to this stream.
     * @return The number of values that were written.
     */
    AsyncFunction<Result<Integer>> writeAsync(T[] values);

    /**
     * Write the provided values to this stream.
     * @param values The values to write.
     * @param startIndex The start index in the values array to begin writing from.
     * @param length The number of values to write.
     * @return The number of values that were written.
     */
    Result<Integer> write(T[] values, int startIndex, int length);

    /**
     * Write the provided values to this stream.
     * @param values The values to write.
     * @param startIndex The start index in the values array to begin writing from.
     * @param length The number of values to write.
     * @return The number of values that were written.
     */
    AsyncFunction<Result<Integer>> writeAsync(T[] values, int startIndex, int length);

    /**
     * Write all of the values from the provided readStream.
     * @param readStream The read stream to read values from.
     * @return The number of values that were written.
     */
    Result<Integer> writeAll(ReadStream<T> readStream);

    /**
     * Write all of the values from the provided readStream.
     * @param readStream The read stream to read values from.
     * @return The number of values that were written.
     */
    AsyncFunction<Result<Integer>> writeAllAsync(ReadStream<T> readStream);
}
