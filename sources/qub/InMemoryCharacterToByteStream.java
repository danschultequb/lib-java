package qub;

public class InMemoryCharacterToByteStream extends BasicCharacterReadStream implements CharacterToByteWriteStream
{
    private final InMemoryByteStream byteStream;
    private CharacterEncoding characterEncoding;
    private String newLine;

    public InMemoryCharacterToByteStream()
    {
        this("");
    }

    public InMemoryCharacterToByteStream(String text)
    {
        this(text, CharacterEncoding.UTF_8);
    }

    public InMemoryCharacterToByteStream(String text, CharacterEncoding characterEncoding)
    {
        this(new InMemoryByteStream(Strings.isNullOrEmpty(text) ? null : characterEncoding.encode(text).await()), characterEncoding);
    }

    public InMemoryCharacterToByteStream(InMemoryByteStream byteStream, CharacterEncoding characterEncoding)
    {
        super(byteStream, characterEncoding);

        this.byteStream = byteStream;
        this.characterEncoding = characterEncoding;
        this.newLine = "\n";
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
    public InMemoryByteStream getByteWriteStream()
    {
        return this.byteStream;
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
        PreCondition.assertNotNull(newLine, "newLine");

        this.newLine = newLine;

        return this;
    }
}
