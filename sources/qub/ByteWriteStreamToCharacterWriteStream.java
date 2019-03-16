package qub;

public class ByteWriteStreamToCharacterWriteStream implements CharacterWriteStream
{
    private final ByteWriteStream byteWriteStream;
    private final CharacterEncoding characterEncoding;
    private final String newLine;

    private final static CharacterEncoding defaultCharacterEncoding = CharacterEncoding.UTF_8;
    private final static String defaultNewLine = "\n";

    public ByteWriteStreamToCharacterWriteStream(ByteWriteStream byteWriteStream)
    {
        this(byteWriteStream, defaultCharacterEncoding, defaultNewLine);
    }

    public ByteWriteStreamToCharacterWriteStream(ByteWriteStream byteWriteStream, CharacterEncoding characterEncoding)
    {
        this(byteWriteStream, characterEncoding, defaultNewLine);
    }

    public ByteWriteStreamToCharacterWriteStream(ByteWriteStream byteWriteStream, String newLine)
    {
        this(byteWriteStream, defaultCharacterEncoding, newLine);
    }

    public ByteWriteStreamToCharacterWriteStream(ByteWriteStream byteWriteStream, CharacterEncoding characterEncoding, String newLine)
    {
        PreCondition.assertNotNull(byteWriteStream, "byteWriteStream");
        PreCondition.assertNotNull(characterEncoding, "characterEncoding");
        PreCondition.assertNotNull(newLine, "newLine");

        this.byteWriteStream = byteWriteStream;
        this.characterEncoding = characterEncoding;
        this.newLine = newLine;
    }

    @Override
    public Result<Integer> write(char toWrite)
    {
        PreCondition.assertNotDisposed(this);

        return CharacterWriteStream.write(toWrite, characterEncoding, byteWriteStream);
    }

    @Override
    public Result<Integer> write(char[] toWrite, int startIndex, int length)
    {
        PreCondition.assertNotNullAndNotEmpty(toWrite, "toWrite");
        PreCondition.assertStartIndex(startIndex, toWrite.length);
        PreCondition.assertLength(length, startIndex, toWrite.length);
        PreCondition.assertNotDisposed(this);

        return CharacterWriteStream.write(toWrite, startIndex, length, characterEncoding, byteWriteStream);
    }

    @Override
    public Result<Integer> write(String toWrite, Object... formattedStringArguments)
    {
        PreCondition.assertNotNull(toWrite, "toWrite");
        PreCondition.assertNotDisposed(this);

        return CharacterWriteStream.write(toWrite, formattedStringArguments, characterEncoding, byteWriteStream);
    }

    @Override
    public Result<Integer> writeLine()
    {
        PreCondition.assertNotDisposed(this);

        return write(newLine);
    }

    @Override
    public boolean isDisposed()
    {
        return byteWriteStream.isDisposed();
    }

    @Override
    public Result<Boolean> dispose()
    {
        return byteWriteStream.dispose();
    }
}
