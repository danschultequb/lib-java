package qub;

public class InMemoryLineStream extends BasicLineReadStream implements LineWriteStream
{
    private final InMemoryCharacterStream characterStream;
    private final String lineSeparator;

    public InMemoryLineStream()
    {
        this("");
    }

    public InMemoryLineStream(String text)
    {
        this(text, (AsyncRunner)null);
    }

    public InMemoryLineStream(String text, AsyncRunner asyncRunner)
    {
        this(text, false, "\n", asyncRunner);
    }

    public InMemoryLineStream(String text, boolean includeNewLines)
    {
        this(text, includeNewLines, (AsyncRunner)null);
    }

    public InMemoryLineStream(String text, boolean includeNewLines, AsyncRunner asyncRunner)
    {
        this(text, includeNewLines, "\n", asyncRunner);
    }

    public InMemoryLineStream(String text, boolean includeNewLines, String lineSeparator, AsyncRunner asyncRunner)
    {
        super(new InMemoryCharacterStream(text, asyncRunner), includeNewLines);

        this.characterStream = (InMemoryCharacterStream)asCharacterReadStream();
        this.lineSeparator = lineSeparator;
    }

    @Override
    public String getLineSeparator()
    {
        return lineSeparator;
    }

    @Override
    public Result<Boolean> write(char toWrite)
    {
        return LineWriteStreamBase.write(this, toWrite);
    }

    @Override
    public Result<Boolean> write(String toWrite, Object... formattedStringArguments)
    {
        return LineWriteStreamBase.write(this, toWrite, formattedStringArguments);
    }

    @Override
    public Result<Boolean> writeLine()
    {
        return LineWriteStreamBase.writeLine(this);
    }

    @Override
    public Result<Boolean> writeLine(String toWrite, Object... formattedStringArguments)
    {
        return LineWriteStreamBase.writeLine(this, toWrite, formattedStringArguments);
    }

    @Override
    public ByteWriteStream asByteWriteStream()
    {
        return LineWriteStreamBase.asByteWriteStream(this);
    }

    @Override
    public CharacterWriteStream asCharacterWriteStream()
    {
        return characterStream;
    }

    public byte[] getBytes()
    {
        return characterStream.getBytes();
    }

    public Result<String> getText()
    {
        return characterStream.getText();
    }

    public InMemoryLineStream endOfStream()
    {
        characterStream.endOfStream();
        return this;
    }
}
