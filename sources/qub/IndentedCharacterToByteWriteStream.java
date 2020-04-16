package qub;

public interface IndentedCharacterToByteWriteStream extends IndentedCharacterWriteStream, CharacterToByteWriteStream
{
    static IndentedCharacterToByteWriteStream create(ByteWriteStream byteWriteStream)
    {
        PreCondition.assertNotNull(byteWriteStream, "byteWriteStream");

        return IndentedCharacterToByteWriteStream.create(CharacterToByteWriteStream.create(byteWriteStream));
    }

    static IndentedCharacterToByteWriteStream create(CharacterToByteWriteStream characterToByteWriteStream)
    {
        PreCondition.assertNotNull(characterToByteWriteStream, "characterToByteWriteStream");

        return BasicIndentedCharacterToByteWriteStream.create(characterToByteWriteStream);
    }

    IndentedCharacterToByteWriteStream setCharacterEncoding(CharacterEncoding characterEncoding);

    IndentedCharacterToByteWriteStream setNewLine(String newLine);

    /**
     * Get the current indentation.
     * @return The current indentation.
     */
    String getCurrentIndent();

    /**
     * Set the current indentation function.
     * @param currentIndentFunction The function that will return the current indentation.
     * @return This object for method chaining.
     */
    IndentedCharacterToByteWriteStream setCurrentIndent(Function0<String> currentIndentFunction);

    /**
     * Set the current indentation.
     * @param currentIndent The current indentation.
     * @return This object for method chaining.
     */
    default IndentedCharacterToByteWriteStream setCurrentIndent(String currentIndent)
    {
        return (IndentedCharacterToByteWriteStream)IndentedCharacterWriteStream.super.setCurrentIndent(currentIndent);
    }

    /**
     * Get the String that makes up a single indentation. When this stream is indented, it will add
     * the single indentation to the current indentation. When this stream is unindented, it will
     * remove a single indentation to the current indentation.
     * @return The String that makes up a single indentation.
     */
    String getSingleIndent();

    /**
     * Set the String that makes up a single indentation. When this stream is indented, it will add
     * the single indentation to the current indentation. When this stream is unindented, it will
     * remove a single indentation to the current indentation.
     * @param singleIndent The String that makes up a single indentation.
     * @return This object for method chaining.
     */
    IndentedCharacterToByteWriteStream setSingleIndent(String singleIndent);

    /**
     * Append the single indent to the current indent.
     * @return This object for method chaining.
     */
    default IndentedCharacterToByteWriteStream increaseIndent()
    {
        return (IndentedCharacterToByteWriteStream)IndentedCharacterWriteStream.super.increaseIndent();
    }

    /**
     * Remove the single indent from the current indent.
     * @return This object for method chaining.
     */
    default IndentedCharacterToByteWriteStream decreaseIndent()
    {
        return (IndentedCharacterToByteWriteStream)IndentedCharacterWriteStream.super.decreaseIndent();
    }
}
