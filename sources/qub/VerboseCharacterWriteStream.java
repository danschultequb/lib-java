package qub;

/**
 * A CharacterWriteStream that can be used to write verbose logs.
 */
@Deprecated
public class VerboseCharacterWriteStream implements CharacterWriteStream
{
    private final LinePrefixCharacterWriteStream innerStream;
    private boolean isVerbose;

    @Deprecated
    public VerboseCharacterWriteStream(boolean isVerbose, CharacterWriteStream innerStream)
    {
        PreCondition.assertNotNull(innerStream, "innerStream");

        this.isVerbose = isVerbose;
        this.innerStream = LinePrefixCharacterWriteStream.create(innerStream)
            .setLinePrefix("VERBOSE: ");
    }

    public static VerboseCharacterWriteStream create(CharacterWriteStream innerStream)
    {
        return new VerboseCharacterWriteStream(true, innerStream);
    }

    /**
     * Get whether or not this VerboseCharacterWriteStream is active.
     * @return Whether or not this VerboseCharacterWriteStream is active.
     */
    public boolean isVerbose()
    {
        return this.isVerbose;
    }

    public VerboseCharacterWriteStream setIsVerbose(boolean isVerbose)
    {
        this.isVerbose = isVerbose;
        return this;
    }

    @Override
    public String getNewLine()
    {
        return this.innerStream.getNewLine();
    }

    @Override
    public VerboseCharacterWriteStream setNewLine(String newLine)
    {
        this.innerStream.setNewLine(newLine);

        return this;
    }

    @Override
    public Result<Integer> write(char toWrite)
    {
        return this.isVerbose
            ? innerStream.write(toWrite)
            : Result.successZero();
    }

    @Override
    public Result<Integer> write(char[] toWrite, int startIndex, int length)
    {
        return this.isVerbose
            ? innerStream.write(toWrite, startIndex, length)
            : Result.successZero();
    }

    @Override
    public Result<Integer> write(String toWrite, Object... formattedStringArguments)
    {
        return this.isVerbose
            ? innerStream.write(toWrite, formattedStringArguments)
            : Result.successZero();
    }

    @Override
    public Result<Integer> writeLine()
    {
        return this.isVerbose
            ? innerStream.writeLine()
            : Result.successZero();
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
