package qub;

/**
 * A CharacterWriteStream that prepends an indentation to beginning of each line.
 */
public class IndentedCharacterWriteStream implements CharacterWriteStream
{
    private final CharacterWriteStream innerStream;
    private String currentIndent;
    private String singleIndent;
    private boolean indentNextCharacter;

    public IndentedCharacterWriteStream(CharacterWriteStream innerStream)
    {
        PreCondition.assertNotNull(innerStream, "innerStream");

        this.innerStream = innerStream;
        this.currentIndent = "";
        this.singleIndent = "  ";
        this.indentNextCharacter = true;
    }

    /**
     * Get the current indentation.
     * @return The current indentation.
     */
    public String getCurrentIndent()
    {
        return currentIndent;
    }

    /**
     * Set the current indentation.
     * @param currentIndent The current indentation.
     * @return This object for method chaining.
     */
    public IndentedCharacterWriteStream setCurrentIndent(String currentIndent)
    {
        PreCondition.assertNotNull(currentIndent, "currentIndent");

        this.currentIndent = currentIndent;

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
    public IndentedCharacterWriteStream setSingleIndent(String singleIndent)
    {
        PreCondition.assertNotNull(singleIndent, "singleIndent");

        this.singleIndent = singleIndent;

        return this;
    }

    /**
     * Append the single indent to the current indent.
     * @return This object for method chaining.
     */
    public IndentedCharacterWriteStream increaseIndent()
    {
        this.currentIndent += this.singleIndent;
        return this;
    }

    /**
     * Remove the single indent from the current indent.
     * @return This object for method chaining.
     */
    public IndentedCharacterWriteStream decreaseIndent()
    {
        final int currentIndentLength = currentIndent.length();
        if (currentIndentLength > 0)
        {
            final int singleIndentLength = singleIndent.length();
            if (currentIndentLength < singleIndentLength)
            {
                currentIndent = "";
            }
            else
            {
                final int newCurrentIndentLength = currentIndentLength - singleIndentLength;
                currentIndent = currentIndent.substring(0, newCurrentIndentLength);
            }
        }
        return this;
    }

    /**
     * Run the provided action with an additional indentation to the current indent.
     * @param action The action to run with an additional indentation to the current indent.
     */
    public void indent(Action0 action)
    {
        PreCondition.assertNotNull(action, "action");

        increaseIndent();
        try
        {
            action.run();
        }
        finally
        {
            decreaseIndent();
        }
    }

    @Override
    public Result<Integer> write(char toWrite)
    {
        PreCondition.assertNotDisposed(this);

        return Result.create(() ->
        {
            int result = 0;
            if (indentNextCharacter && !Strings.isNullOrEmpty(currentIndent))
            {
                result += innerStream.write(currentIndent).await();
            }
            result += innerStream.write(toWrite).await();
            indentNextCharacter = (toWrite == '\n');
            return result;
        });

    }

    @Override
    public Result<Integer> write(char[] toWrite, int startIndex, int length)
    {
        PreCondition.assertNotNull(toWrite, "toWrite");
        PreCondition.assertStartIndex(startIndex, toWrite.length);
        PreCondition.assertLength(length, startIndex, toWrite.length);
        PreCondition.assertNotDisposed(this);

        final int endIndex = startIndex + length;
        return Result.create(() ->
        {
            int result = 0;

            int lineStartIndex = startIndex;
            int lineLength = length;
            int newLineCharacterIndex = Array.indexOf(toWrite, lineStartIndex, lineLength, '\n');
            while (newLineCharacterIndex != -1)
            {
                final int lineEndIndex = newLineCharacterIndex + 1;
                lineLength = lineEndIndex - lineStartIndex;

                if (indentNextCharacter && !Strings.isNullOrEmpty(currentIndent) && lineLength != 1 && (lineLength != 2 || toWrite[lineStartIndex] != '\r'))
                {
                    result += innerStream.write(currentIndent).await();
                }
                result += innerStream.write(toWrite, lineStartIndex, lineLength).awaitError();
                indentNextCharacter = true;

                lineStartIndex = lineEndIndex;
                lineLength = endIndex - lineStartIndex;
                newLineCharacterIndex = Array.indexOf(toWrite, lineStartIndex, lineLength, '\n');
            }

            if (lineStartIndex < endIndex)
            {
                if (indentNextCharacter && !Strings.isNullOrEmpty(currentIndent))
                {
                    result += innerStream.write(currentIndent).await();
                }
                result += innerStream.write(toWrite, lineStartIndex, lineLength).awaitError();
                indentNextCharacter = false;
            }

            PostCondition.assertGreaterThanOrEqualTo(result, 0, "result");

            return result;
        });
    }

    @Override
    public Result<Integer> write(String toWrite, Object... formattedStringArguments)
    {
        PreCondition.assertNotNull(toWrite, "toWrite");
        PreCondition.assertNotDisposed(this);

        return Result.create(() ->
        {
            int result = 0;

            int toWriteLength = toWrite.length();
            int lineStartIndex = 0;
            int lineLength = toWriteLength;
            int newLineCharacterIndex = toWrite.indexOf('\n', lineStartIndex);
            while (newLineCharacterIndex != -1)
            {
                final int lineEndIndex = newLineCharacterIndex + 1;
                lineLength = lineEndIndex - lineStartIndex;

                if (indentNextCharacter && !Strings.isNullOrEmpty(currentIndent) && lineLength != 1 && (lineLength != 2 || toWrite.charAt(lineStartIndex) != '\r'))
                {
                    result += innerStream.write(currentIndent).await();
                }
                result += innerStream.write(toWrite.substring(lineStartIndex, lineEndIndex)).await();
                indentNextCharacter = true;

                lineStartIndex = lineEndIndex;
                lineLength = toWriteLength - lineStartIndex;
                newLineCharacterIndex = toWrite.indexOf('\n', lineStartIndex);
            }

            if (lineStartIndex < toWriteLength)
            {
                if (indentNextCharacter && !Strings.isNullOrEmpty(currentIndent))
                {
                    result += innerStream.write(currentIndent).await();
                }
                result += innerStream.write(toWrite.substring(lineStartIndex, toWriteLength)).await();
                indentNextCharacter = false;
            }

            PostCondition.assertGreaterThanOrEqualTo(result, 0, "result");

            return result;
        });
    }

    @Override
    public Result<Integer> writeLine()
    {
        PreCondition.assertNotDisposed(this);

        return Result.create(() ->
        {
            int result = 0;
            if (indentNextCharacter && !Strings.isNullOrEmpty(currentIndent))
            {
                result += innerStream.write(currentIndent).await();
            }
            result += innerStream.writeLine().await();
            indentNextCharacter = true;
            return result;
        });
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
