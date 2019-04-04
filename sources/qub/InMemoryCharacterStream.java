package qub;

public class InMemoryCharacterStream extends BasicCharacterReadStream implements CharacterWriteStream
{
    private final InMemoryByteStream byteStream;
    private final CharacterEncoding characterEncoding;
    private String newLine;

    public InMemoryCharacterStream()
    {
        this((AsyncRunner)null);
    }

    public InMemoryCharacterStream(CharacterEncoding characterEncoding)
    {
        this(characterEncoding, null);
    }

    public InMemoryCharacterStream(CharacterEncoding characterEncoding, AsyncRunner asyncRunner)
    {
        this("", characterEncoding, asyncRunner);
    }

    public InMemoryCharacterStream(AsyncRunner asyncRunner)
    {
        this("", asyncRunner);
    }

    public InMemoryCharacterStream(String text)
    {
        this(text, (AsyncRunner)null);
    }

    public InMemoryCharacterStream(String text, AsyncRunner asyncRunner)
    {
        this(text, CharacterEncoding.UTF_8, asyncRunner);
    }

    public InMemoryCharacterStream(String text, CharacterEncoding characterEncoding)
    {
        this(text, characterEncoding, null);
    }

    public InMemoryCharacterStream(String text, CharacterEncoding characterEncoding, AsyncRunner asyncRunner)
    {
        this(new InMemoryByteStream(Strings.isNullOrEmpty(text) ?null : characterEncoding.encode(text).await(), asyncRunner), characterEncoding);
    }

    public InMemoryCharacterStream(InMemoryByteStream byteStream)
    {
        this(byteStream, CharacterEncoding.UTF_8);
    }

    public InMemoryCharacterStream(InMemoryByteStream byteStream, CharacterEncoding characterEncoding)
    {
        super(byteStream, characterEncoding);

        this.characterEncoding = characterEncoding;
        this.byteStream = byteStream;
        this.newLine = "\n";
    }

    public byte[] getBytes()
    {
        return byteStream.getBytes();
    }

    public Result<String> getText()
    {
        final byte[] bytes = getBytes();
        return bytes == null || bytes.length == 0 ? Result.success("") : characterEncoding.decodeAsString(getBytes());
    }

    /**
     * Set the newLine character sequence that this InMemoryCharacterStream will use to end its
     * lines.
     * @param newLine The newLine character sequence that this InMemoryCharacterStream will use to
     *                ends its lines.
     * @return This object for method chaining.
     */
    public InMemoryCharacterStream setNewLine(String newLine)
    {
        PreCondition.assertNotNull(newLine, "newLine");

        this.newLine = newLine;

        return this;
    }

    @Override
    public Result<Integer> write(char toWrite)
    {
        return CharacterWriteStream.write(toWrite, characterEncoding, byteStream);
    }

    @Override
    public Result<Integer> write(char[] toWrite, int startIndex, int length)
    {
        return CharacterWriteStream.write(toWrite, startIndex, length, characterEncoding, byteStream);
    }

    @Override
    public Result<Integer> write(String toWrite, Object... formattedStringArguments)
    {
        return CharacterWriteStream.write(toWrite, formattedStringArguments, characterEncoding, byteStream);
    }

    @Override
    public Result<Integer> writeLine()
    {
        return write(newLine);
    }

    public InMemoryCharacterStream endOfStream()
    {
        byteStream.endOfStream();
        return this;
    }
}
