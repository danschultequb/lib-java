package qub;

/**
 * A CharacterWriteStream that can be used to write verbose logs.
 */
public class VerboseCharacterWriteStream implements CharacterWriteStream
{
    private final boolean isVerbose;
    private final LinePrefixCharacterWriteStream innerStream;

    public VerboseCharacterWriteStream(boolean isVerbose, CharacterWriteStream innerStream)
    {
        PreCondition.assertTrue(!isVerbose || innerStream != null, "!isVerbose || innerStream != null");

        this.isVerbose = isVerbose;

        if (innerStream == null)
        {
            innerStream = new InMemoryCharacterStream();
        }
        this.innerStream = new LinePrefixCharacterWriteStream(innerStream)
            .setLinePrefix("VERBOSE: ");
    }

    /**
     * Get whether or not this VerboseCharacterWriteStream is active.
     * @return Whether or not this VerboseCharacterWriteStream is active.
     */
    public boolean isVerbose()
    {
        return this.isVerbose;
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
