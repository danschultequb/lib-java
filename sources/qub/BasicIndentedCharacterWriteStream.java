package qub;

/**
 * A CharacterWriteStream that prepends an indentation to beginning of each line.
 */
public class BasicIndentedCharacterWriteStream implements IndentedCharacterWriteStream
{
    private final LinePrefixCharacterWriteStream innerStream;
    private String singleIndent;

    protected BasicIndentedCharacterWriteStream(CharacterWriteStream innerStream)
    {
        this.innerStream = LinePrefixCharacterWriteStream.create(innerStream);
        this.singleIndent = "  ";
    }

    public static BasicIndentedCharacterWriteStream create(CharacterWriteStream characterWriteStream)
    {
        PreCondition.assertNotNull(characterWriteStream, "characterWriteStream");

        return new BasicIndentedCharacterWriteStream(characterWriteStream);
    }

    /**
     * Get the current indentation.
     * @return The current indentation.
     */
    public String getCurrentIndent()
    {
        final String result = this.innerStream.getLinePrefix();

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    /**
     * Set the current indentation function.
     * @param currentIndentFunction The function that will return the current indentation.
     * @return This object for method chaining.
     */
    public BasicIndentedCharacterWriteStream setCurrentIndent(Function0<String> currentIndentFunction)
    {
        PreCondition.assertNotNull(currentIndentFunction, "currentIndentFunction");

        this.innerStream.setLinePrefix(currentIndentFunction);

        return this;
    }

    /**
     * Set the current indentation.
     * @param currentIndent The current indentation.
     * @return This object for method chaining.
     */
    public BasicIndentedCharacterWriteStream setCurrentIndent(String currentIndent)
    {
        PreCondition.assertNotNull(currentIndent, "currentIndent");

        this.setCurrentIndent(() -> currentIndent);

        return this;
    }

    /**
     * Get the String that makes up a single indentation. When this stream is indented, it will add
     * the single indentation to the current indentation. When this stream is unindented, it will
     * remove a single indentation to the current indentation.
     * @return The String that makes up a single indentation.
     */
    public String getSingleIndent()
    {
        return singleIndent;
    }

    /**
     * Set the String that makes up a single indentation. When this stream is indented, it will add
     * the single indentation to the current indentation. When this stream is unindented, it will
     * remove a single indentation to the current indentation.
     * @param singleIndent The String that makes up a single indentation.
     * @return This object for method chaining.
     */
    public BasicIndentedCharacterWriteStream setSingleIndent(String singleIndent)
    {
        PreCondition.assertNotNull(singleIndent, "singleIndent");

        this.singleIndent = singleIndent;

        return this;
    }

    /**
     * Append the single indent to the current indent.
     * @return This object for method chaining.
     */
    public BasicIndentedCharacterWriteStream increaseIndent()
    {
        return (BasicIndentedCharacterWriteStream)IndentedCharacterWriteStream.super.increaseIndent();
    }

    /**
     * Remove the single indent from the current indent.
     * @return This object for method chaining.
     */
    public BasicIndentedCharacterWriteStream decreaseIndent()
    {
        return (BasicIndentedCharacterWriteStream)IndentedCharacterWriteStream.super.decreaseIndent();
    }

    @Override
    public String getNewLine()
    {
        return this.innerStream.getNewLine();
    }

    @Override
    public BasicIndentedCharacterWriteStream setNewLine(String newLine)
    {
        PreCondition.assertNotNull(newLine, "newLine");

        this.innerStream.setNewLine(newLine);

        return this;
    }

    @Override
    public Result<Integer> write(char toWrite)
    {
        return this.innerStream.write(toWrite);
    }

    @Override
    public Result<Integer> write(char[] toWrite, int startIndex, int length)
    {
        return this.innerStream.write(toWrite, startIndex, length);
    }

    @Override
    public Result<Integer> write(String toWrite, Object... formattedStringArguments)
    {
        return this.innerStream.write(toWrite, formattedStringArguments);
    }

    @Override
    public Result<Integer> writeLine()
    {
        return this.innerStream.writeLine();
    }

    @Override
    public boolean isDisposed()
    {
        return innerStream.isDisposed();
    }

    @Override
    public Result<Boolean> dispose()
    {
        return innerStream.dispose();
    }
}
