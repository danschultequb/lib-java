package qub;

/**
 * A CharacterWriteStream that prepends a prefix to beginning of each line.
 */
public class LinePrefixCharacterWriteStream implements CharacterWriteStream
{
    private final CharacterWriteStream innerStream;
    private Function0<String> linePrefixFunction;
    private boolean addPrefixBeforeNextCharacter;

    protected LinePrefixCharacterWriteStream(CharacterWriteStream innerStream)
    {
        PreCondition.assertNotNull(innerStream, "innerStream");

        this.innerStream = innerStream;
        this.setLinePrefix("");
        this.addPrefixBeforeNextCharacter = true;
    }

    public static LinePrefixCharacterWriteStream create(CharacterWriteStream innerStream)
    {
        PreCondition.assertNotNull(innerStream, "innerStream");

        return new LinePrefixCharacterWriteStream(innerStream);
    }

    /**
     * Get the current line prefix.
     * @return The current line prefix.
     */
    public String getLinePrefix()
    {
        return linePrefixFunction.run();
    }

    /**
     * Set the line prefix function.
     * @param linePrefixFunction The function that will return the line prefix.
     * @return This object for method chaining.
     */
    public LinePrefixCharacterWriteStream setLinePrefix(Function0<String> linePrefixFunction)
    {
        PreCondition.assertNotNull(linePrefixFunction, "linePrefixFunction");

        this.linePrefixFunction = linePrefixFunction;

        return this;
    }

    /**
     * Set the line prefix.
     * @param linePrefix The line prefix.
     * @return This object for method chaining.
     */
    public LinePrefixCharacterWriteStream setLinePrefix(String linePrefix)
    {
        this.linePrefixFunction = () -> linePrefix;

        return this;
    }

    @Override
    public String getNewLine()
    {
        return this.innerStream.getNewLine();
    }

    @Override
    public LinePrefixCharacterWriteStream setNewLine(char newLine)
    {
        this.innerStream.setNewLine(newLine);

        return this;
    }

    @Override
    public LinePrefixCharacterWriteStream setNewLine(char[] newLine)
    {
        this.innerStream.setNewLine(newLine);

        return this;
    }

    @Override
    public LinePrefixCharacterWriteStream setNewLine(String newLine)
    {
        this.innerStream.setNewLine(newLine);

        return this;
    }

    @Override
    public Result<Integer> write(char toWrite)
    {
        PreCondition.assertNotDisposed(this);

        return Result.create(() ->
        {
            int result = 0;
            final String linePrefix = this.getLinePrefix();
            if (addPrefixBeforeNextCharacter && !Strings.isNullOrEmpty(linePrefix) && toWrite != '\n')
            {
                result += innerStream.write(linePrefix).await();
            }
            result += innerStream.write(toWrite).await();
            addPrefixBeforeNextCharacter = (toWrite == '\n');
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

                final String linePrefix = this.getLinePrefix();
                if (addPrefixBeforeNextCharacter && !Strings.isNullOrEmpty(linePrefix) && lineLength != 1 && (lineLength != 2 || toWrite[lineStartIndex] != '\r'))
                {
                    result += innerStream.write(linePrefix).await();
                }
                result += innerStream.write(toWrite, lineStartIndex, lineLength).await();
                addPrefixBeforeNextCharacter = true;

                lineStartIndex = lineEndIndex;
                lineLength = endIndex - lineStartIndex;
                newLineCharacterIndex = lineLength > 0 ? Array.indexOf(toWrite, lineStartIndex, lineLength, '\n') : -1;
            }

            if (lineStartIndex < endIndex)
            {
                final String linePrefix = this.getLinePrefix();
                if (addPrefixBeforeNextCharacter && !Strings.isNullOrEmpty(linePrefix))
                {
                    result += innerStream.write(linePrefix).await();
                }
                result += innerStream.write(toWrite, lineStartIndex, lineLength).await();
                addPrefixBeforeNextCharacter = false;
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
            final String linePrefix = this.getLinePrefix();
            while (newLineCharacterIndex != -1)
            {
                final int lineEndIndex = newLineCharacterIndex + 1;
                lineLength = lineEndIndex - lineStartIndex;

                if (addPrefixBeforeNextCharacter && !Strings.isNullOrEmpty(linePrefix) && lineLength != 1 && (lineLength != 2 || toWrite.charAt(lineStartIndex) != '\r'))
                {
                    result += innerStream.write(linePrefix).await();
                }
                result += innerStream.write(toWrite.substring(lineStartIndex, lineEndIndex)).await();
                addPrefixBeforeNextCharacter = true;

                lineStartIndex = lineEndIndex;
                lineLength = toWriteLength - lineStartIndex;
                newLineCharacterIndex = lineLength > 0 ? toWrite.indexOf('\n', lineStartIndex) : -1;
            }

            if (lineStartIndex < toWriteLength)
            {
                if (addPrefixBeforeNextCharacter && !Strings.isNullOrEmpty(linePrefix))
                {
                    result += innerStream.write(linePrefix).await();
                }
                result += innerStream.write(toWrite.substring(lineStartIndex, toWriteLength)).await();
                addPrefixBeforeNextCharacter = false;
            }

            PostCondition.assertGreaterThanOrEqualTo(result, 0, "result");

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
