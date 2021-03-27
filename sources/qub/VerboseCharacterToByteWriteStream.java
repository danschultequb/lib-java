package qub;

/**
 * A CharacterToByteWriteStream that can be used to write verbose logs.
 */
public class VerboseCharacterToByteWriteStream implements LinePrefixCharacterToByteWriteStream
{
    private static final String defaultPrefix = "VERBOSE: ";

    private final LinePrefixCharacterToByteWriteStream innerStream;
    private boolean isVerbose;

    public VerboseCharacterToByteWriteStream(CharacterToByteWriteStream innerStream)
    {
        PreCondition.assertNotNull(innerStream, "innerStream");

        this.isVerbose = true;
        this.innerStream = LinePrefixCharacterToByteWriteStream.create(innerStream)
            .setLinePrefix(VerboseCharacterToByteWriteStream.defaultPrefix);
    }

    public static VerboseCharacterToByteWriteStream create(CharacterToByteWriteStream innerStream)
    {
        return new VerboseCharacterToByteWriteStream(innerStream);
    }

    /**
     * Get whether or not this VerboseCharacterToByteWriteStream is active.
     * @return Whether or not this VerboseCharacterToByteWriteStream is active.
     */
    public boolean isVerbose()
    {
        return this.isVerbose;
    }

    public VerboseCharacterToByteWriteStream setIsVerbose(boolean isVerbose)
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
    public CharacterEncoding getCharacterEncoding()
    {
        return this.innerStream.getCharacterEncoding();
    }

    @Override
    public VerboseCharacterToByteWriteStream setCharacterEncoding(CharacterEncoding characterEncoding)
    {
        this.innerStream.setCharacterEncoding(characterEncoding);
        return this;
    }

    @Override
    public VerboseCharacterToByteWriteStream setNewLine(String newLine)
    {
        this.innerStream.setNewLine(newLine);

        return this;
    }

    @Override
    public String getLinePrefix()
    {
        return this.innerStream.getLinePrefix();
    }

    @Override
    public VerboseCharacterToByteWriteStream setLinePrefix(Function0<String> linePrefixFunction)
    {
        PreCondition.assertNotNull(linePrefixFunction, "linePrefixFunction");

        this.innerStream.setLinePrefix(linePrefixFunction);

        return this;
    }

    @Override
    public VerboseCharacterToByteWriteStream setLinePrefix(String linePrefix)
    {
        return (VerboseCharacterToByteWriteStream)LinePrefixCharacterToByteWriteStream.super.setLinePrefix(linePrefix);
    }

    @Override
    public Result<Integer> write(char toWrite)
    {
        return this.isVerbose
            ? this.innerStream.write(toWrite)
            : Result.successZero();
    }

    @Override
    public Result<Integer> write(char[] toWrite, int startIndex, int length)
    {
        return this.isVerbose
            ? this.innerStream.write(toWrite, startIndex, length)
            : Result.successZero();
    }

    @Override
    public Result<Integer> write(String toWrite, Object... formattedStringArguments)
    {
        return this.isVerbose
            ? this.innerStream.write(toWrite, formattedStringArguments)
            : Result.successZero();
    }

    @Override
    public Result<Integer> writeLine()
    {
        return this.isVerbose
            ? this.innerStream.writeLine()
            : Result.successZero();
    }

    @Override
    public boolean isDisposed()
    {
        return this.innerStream.isDisposed();
    }

    @Override
    public Result<Boolean> dispose()
    {
        return this.innerStream.dispose();
    }

    @Override
    public Result<Integer> write(byte toWrite)
    {
        return this.isVerbose
            ? this.innerStream.write(toWrite)
            : Result.successZero();
    }

    @Override
    public Result<Integer> write(byte[] toWrite, int startIndex, int length)
    {
        return this.isVerbose
            ? this.innerStream.write(toWrite, startIndex, length)
            : Result.successZero();
    }
}
