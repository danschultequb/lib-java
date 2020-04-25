package qub;

/**
 * A CharacterWriteStream that wraps a ByteWriteStream so all characters written to this stream will
 * be encoded and written to the inner ByteWriteStream.
 */
public class BasicCharacterToByteWriteStream implements CharacterToByteWriteStream
{
    private final ByteWriteStream byteWriteStream;
    private CharacterEncoding characterEncoding;
    private String newLine;

    public BasicCharacterToByteWriteStream(ByteWriteStream byteWriteStream)
    {
        this(byteWriteStream, CharacterEncoding.UTF_8);
    }

    public BasicCharacterToByteWriteStream(ByteWriteStream byteWriteStream, CharacterEncoding characterEncoding)
    {
        PreCondition.assertNotNull(byteWriteStream, "byteWriteStream");
        PreCondition.assertNotNull(characterEncoding, "characterEncoding");

        this.byteWriteStream = byteWriteStream;
        this.characterEncoding = characterEncoding;
        this.newLine = "\n";
    }

    @Override
    public CharacterEncoding getCharacterEncoding()
    {
        return this.characterEncoding;
    }

    @Override
    public BasicCharacterToByteWriteStream setCharacterEncoding(CharacterEncoding characterEncoding)
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
    public CharacterToByteWriteStream setNewLine(String newLine)
    {
        PreCondition.assertNotNull(newLine, "newLine");

        this.newLine = newLine;

        return this;
    }

    @Override
    public Result<Integer> write(char toWrite)
    {
        PreCondition.assertNotDisposed(this, "this.isDisposed()");

        return Result.create(() ->
        {
            this.characterEncoding.encodeCharacter(toWrite, this.byteWriteStream).await();
            return 1;
        });
    }

    @Override
    public Result<Integer> write(String toWrite, Object... formattedStringArguments)
    {
        PreCondition.assertNotNull(toWrite, "toWrite");
        PreCondition.assertNotDisposed(this, "this.isDisposed()");

        return Result.create(() ->
        {
            final String formattedString = Strings.format(toWrite, formattedStringArguments);
            this.characterEncoding.encodeCharacters(formattedString, this.byteWriteStream).await();
            return formattedString.length();
        });
    }

    @Override
    public Result<Integer> write(byte toWrite)
    {
        PreCondition.assertNotDisposed(this, "this.isDisposed()");

        return this.byteWriteStream.write(toWrite);
    }

    @Override
    public Result<Integer> write(byte[] toWrite, int startIndex, int length)
    {
        PreCondition.assertNotDisposed(this, "this.isDisposed()");

        return this.byteWriteStream.write(toWrite, startIndex, length);
    }

    @Override
    public boolean isDisposed()
    {
        return this.byteWriteStream.isDisposed();
    }

    @Override
    public Result<Boolean> dispose()
    {
        return this.byteWriteStream.dispose();
    }
}
