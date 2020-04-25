package qub;

public class InMemoryCharacterToByteStream extends BasicCharacterToByteReadStream implements InMemoryCharacterStream, CharacterToByteWriteStream
{
    private final InMemoryByteStream byteStream;
    private CharacterEncoding characterEncoding;
    private String newLine;

    private InMemoryCharacterToByteStream(InMemoryByteStream byteStream, CharacterEncoding characterEncoding)
    {
        super(byteStream, characterEncoding);

        this.byteStream = byteStream;
        this.characterEncoding = characterEncoding;
        this.newLine = "\n";
    }

    public static InMemoryCharacterToByteStream create()
    {
        return InMemoryCharacterToByteStream.create("");
    }

    public static InMemoryCharacterToByteStream create(String text)
    {
        return InMemoryCharacterToByteStream.create(text, CharacterEncoding.UTF_8);
    }

    public static InMemoryCharacterToByteStream create(String text, CharacterEncoding characterEncoding)
    {
        PreCondition.assertNotNull(text, "text");
        PreCondition.assertNotNull(characterEncoding, "characterEncoding");

        final byte[] encodedBytes = characterEncoding.encodeCharacters(text).await();
        final InMemoryByteStream byteStream = InMemoryByteStream.create(encodedBytes);
        return InMemoryCharacterToByteStream.create(byteStream, characterEncoding);
    }

    public static InMemoryCharacterToByteStream create(InMemoryByteStream byteStream, CharacterEncoding characterEncoding)
    {
        PreCondition.assertNotNull(byteStream, "byteStream");
        PreCondition.assertNotNull(characterEncoding, "characterEncoding");

        return new InMemoryCharacterToByteStream(byteStream, characterEncoding);
    }

    public byte[] getBytes()
    {
        return this.byteStream.getBytes();
    }

    public Result<String> getText()
    {
        final byte[] bytes = this.getBytes();
        return bytes.length == 0 ? Result.success("") : this.getCharacterEncoding().decodeAsString(bytes);
    }

    public InMemoryCharacterToByteStream endOfStream()
    {
        byteStream.endOfStream();
        return this;
    }

    @Override
    public InMemoryCharacterToByteStream setCharacterEncoding(CharacterEncoding characterEncoding)
    {
        PreCondition.assertNotNull(characterEncoding, "characterEncoding");

        this.characterEncoding = characterEncoding;

        return this;
    }

    @Override
    public String getNewLine()
    {
        return this.newLine;
    }

    @Override
    public InMemoryCharacterToByteStream setNewLine(char newLine)
    {
        return (InMemoryCharacterToByteStream)CharacterToByteWriteStream.super.setNewLine(newLine);
    }

    @Override
    public InMemoryCharacterToByteStream setNewLine(char[] newLine)
    {
        return (InMemoryCharacterToByteStream)CharacterToByteWriteStream.super.setNewLine(newLine);
    }

    @Override
    public InMemoryCharacterToByteStream setNewLine(String newLine)
    {
        this.newLine = newLine;

        return this;
    }

    @Override
    public Result<Integer> write(char toWrite)
    {
        PreCondition.assertNotDisposed(this, "this");

        return Result.create(() ->
        {
            this.characterEncoding.encodeCharacter(toWrite, this.byteStream).await();
            return 1;
        });
    }

    @Override
    public Result<Integer> write(String toWrite, Object... formattedStringArguments)
    {
        PreCondition.assertNotDisposed(this, "this");

        return Result.create(() ->
        {
            final String formattedString = Strings.format(toWrite, formattedStringArguments);
            this.characterEncoding.encodeCharacters(formattedString, this.byteStream).await();
            return formattedString.length();
        });
    }

    @Override
    public Result<Integer> write(byte toWrite)
    {
        return this.byteStream.write(toWrite);
    }

    @Override
    public Result<Integer> write(byte[] toWrite, int startIndex, int length)
    {
        return this.byteStream.write(toWrite, startIndex, length);
    }
}
