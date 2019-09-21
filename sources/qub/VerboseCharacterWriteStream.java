package qub;

/**
 * A CharacterWriteStream that can be used to write verbose logs.
 */
public class VerboseCharacterWriteStream implements CharacterWriteStream
{
    private final boolean isVerbose;
    private final LinePrefixCharacterWriteStream innerStream;

    public VerboseCharacterWriteStream(boolean isVerbose, boolean showTimestamp, Clock clock, CharacterWriteStream innerStream)
    {
        PreCondition.assertNotNull(clock, "clock");
        PreCondition.assertNotNull(innerStream, "innerStream");

        this.isVerbose = isVerbose;
        this.innerStream = new LinePrefixCharacterWriteStream(innerStream)
            .setLinePrefix(showTimestamp
                ? () -> "VERBOSE(" + clock.getCurrentDateTime().getMillisecondsSinceEpoch() + "): "
                : () -> "VERBOSE: ");
    }

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
