package qub;

/**
 * A segment within some JSON content.
 */
public interface JSONSegment
{
    /**
     * Get the {@link String} representation of this {@link JSONSegment} using the provided
     * {@link JSONFormat}.
     * @param format The {@link JSONFormat} to use when converting this {@link JSONSegment} to a
     *               {@link String}.
     */
    public default String toString(JSONFormat format)
    {
        PreCondition.assertNotNull(format, "format");

        final CharacterList list = CharacterList.create();
        try (final CharacterListWriteStream writeStream = CharacterListWriteStream.create(list))
        {
            this.toString(writeStream, format).await();
        }
        return list.toString();
    }

    /**
     * Write the {@link String} representation of this {@link JSONSegment} to the provided
     * {@link CharacterWriteStream}.
     * @param stream The {@link CharacterWriteStream} to write the {@link String} representation of
     *               this {@link JSONSegment} to.
     * @return The number of characters that were written.
     */
    public default Result<Integer> toString(CharacterWriteStream stream)
    {
        PreCondition.assertNotNull(stream, "stream");

        return this.toString(IndentedCharacterWriteStream.create(stream));
    }

    /**
     * Write the {@link String} representation of this {@link JSONSegment} to the provided
     * {@link IndentedCharacterWriteStream}.
     * @param stream The {@link IndentedCharacterWriteStream} to write the {@link String}
     *               representation of this {@link JSONSegment} to.
     * @return The number of characters that were written.
     */
    public default Result<Integer> toString(IndentedCharacterWriteStream stream)
    {
        return this.toString(stream, JSONFormat.consise);
    }

    /**
     * Write the {@link String} representation of this {@link JSONSegment} to the provided
     * {@link CharacterWriteStream}.
     * @param stream The {@link CharacterWriteStream} to write the {@link String} representation of
     *               this {@link JSONSegment} to.
     * @param format The {@link JSONFormat} to use when converting this {@link JSONSegment} to a
     * {@link String}.
     * @return The number of characters that were written.
     */
    public default Result<Integer> toString(CharacterWriteStream stream, JSONFormat format)
    {
        PreCondition.assertNotNull(stream, "stream");

        return this.toString(IndentedCharacterWriteStream.create(stream), format);
    }

    /**
     * Write the {@link String} representation of this {@link JSONSegment} to the provided
     * {@link IndentedCharacterWriteStream}.
     * @param stream The {@link IndentedCharacterWriteStream} to write the {@link String}
     *               representation of this {@link JSONSegment} to.
     * @param format The {@link JSONFormat} to use when converting this {@link JSONSegment} to a
     * {@link String}.
     * @return The number of characters that were written.
     */
    public Result<Integer> toString(IndentedCharacterWriteStream stream, JSONFormat format);

    /**
     * Get the String representation of the provided JSONSegment.
     * @param segment The JSONSegment to get the String representation of.
     * @return The String representation of the provided JSONSegment.
     */
    public static String toString(JSONSegment segment)
    {
        return JSONSegment.toString((Function1<IndentedCharacterWriteStream,Result<Integer>>)segment::toString);
    }

    public static String toString(Function1<IndentedCharacterWriteStream,Result<Integer>> toStringFunction)
    {
        PreCondition.assertNotNull(toStringFunction, "toStringFunction");

        final CharacterList list = CharacterList.create();
        try (final CharacterListWriteStream writeStream = CharacterListWriteStream.create(list))
        {
            toStringFunction.run(IndentedCharacterWriteStream.create(writeStream)).await();
        }
        return list.toString(true);
    }

    public static String toString(Function2<IndentedCharacterWriteStream,JSONFormat,Result<Integer>> toStringFunction)
    {
        PreCondition.assertNotNull(toStringFunction, "toStringFunction");

        return JSONSegment.toString((IndentedCharacterWriteStream indentedStream) -> toStringFunction.run(indentedStream, JSONFormat.consise));
    }
}
