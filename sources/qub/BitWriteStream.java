package qub;

/**
 * An interface for writing bits to a stream.
 */
public interface BitWriteStream extends Disposable
{
    /**
     * Write a single bit to the stream.
     * @param bit The bit to write to the stream.
     * @return The number of bits that were written.
     */
    Result<Integer> write(int bit);

    /**
     * Write a single bit to the stream.
     * @param bit The bit to write to the stream.
     * @return The number of bits that were written.
     */
    default Result<Integer> write(Integer bit)
    {
        PreCondition.assertNotNull(bit, "bit");

        return this.write(bit.intValue());
    }

    /**
     * Write the provided bits to the stream.
     * @param bits The bits to write.
     * @return The number of bits that were written.
     */
    default Result<Integer> write(int... bits)
    {
        PreCondition.assertNotNull(bits, "bits");

        return this.write(bits, 0, bits.length);
    }

    /**
     * Write the provided bits to the stream.
     * @param bits The array of bits to write from.
     * @param startIndex The index into the bits array to start from.
     * @param length The number of bits to write.
     * @return The number of bits that were written.
     */
    default Result<Integer> write(int[] bits, int startIndex, int length)
    {
        PreCondition.assertNotNull(bits, "bits");
        PreCondition.assertStartIndex(startIndex, bits.length);
        PreCondition.assertLength(length, startIndex, bits.length);

        return Result.create(() ->
        {
            int result = 0;
            for (int i = startIndex; i <= startIndex + length; ++i)
            {
                result += this.write(bits[i]).await();
            }

            PostCondition.assertBetween(0, result, length, "result");

            return result;
        });
    }

    /**
     * Write the provided bits to the stream.
     * @param bits The bits to write.
     * @return The number of bits that were written.
     */
    default Result<Integer> write(Iterable<Integer> bits)
    {
        PreCondition.assertNotNull(bits, "bits");

        return Result.create(() ->
        {
            int result = 0;
            for (final Integer bit : bits)
            {
                result += this.write(bit).await();
            }

            PostCondition.assertGreaterThanOrEqualTo(result, 0, "result");

            return result;
        });
    }
}
