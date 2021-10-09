package qub;

public interface JSONPropertyWrapper
{
    /**
     * Get the JSON representation of this object.
     * @return The JSON representation of this object.
     */
    JSONProperty toJson();

    static String toString(JSONPropertyWrapper wrapper)
    {
        PreCondition.assertNotNull(wrapper, "wrapper");

        return wrapper.toJson().toString();
    }

    /**
     * Get the String representation of this object using the provided format.
     * @param format The format to use when converting this object to a string.
     * @return The String representation of this object using the provided format.
     */
    default String toString(JSONFormat format)
    {
        return this.toJson().toString(format);
    }

    /**
     * Write the String representation of this JSONSchema to the provided stream.
     * @param stream The stream to write the String representation of this JSONSchema to.
     * @return The number of characters that were written.
     */
    default Result<Integer> toString(CharacterWriteStream stream)
    {
        return this.toJson().toString(stream);
    }

    /**
     * Write the String representation of this JSONSchema to the provided stream.
     * @param stream The stream to write the String representation of this JSONSchema to.
     * @return The number of characters that were written.
     */
    default Result<Integer> toString(IndentedCharacterWriteStream stream)
    {
        return this.toJson().toString(stream);
    }

    /**
     * Write the String representation of this JSONSchema to the provided stream.
     * @param stream The stream to write the String representation of this JSONSchema to.
     * @param format The format to use when converting this JSONSchema to a string.
     * @return The number of characters that were written.
     */
    default Result<Integer> toString(CharacterWriteStream stream, JSONFormat format)
    {
        return this.toJson().toString(stream, format);
    }

    /**
     * Write the String representation of this JSONSchema to the provided stream.
     * @param stream The stream to write the String representation of this JSONSchema to.
     * @param format The format to use when converting this JSONSchema to a string.
     * @return The number of characters that were written.
     */
    default Result<Integer> toString(IndentedCharacterWriteStream stream, JSONFormat format)
    {
        return this.toJson().toString(stream, format);
    }

    static boolean equals(JSONPropertyWrapper wrapper, Object rhs)
    {
        PreCondition.assertNotNull(wrapper, "wrapper");

        return wrapper.getClass().equals(Types.getType(rhs)) &&
            wrapper.toJson().equals(((JSONPropertyWrapper)rhs).toJson());
    }
}
