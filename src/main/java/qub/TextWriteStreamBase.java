package qub;

abstract class TextWriteStreamBase extends ByteWriteStreamBase implements TextWriteStream
{
    protected static final CharacterEncoding defaultCharacterEncoding = CharacterEncoding.ASCII;
    protected static final String defaultNewLine = System.lineSeparator();

    private final ByteWriteStream byteWriteStream;
    private final CharacterEncoding characterEncoding;
    private final String newLine;

    TextWriteStreamBase(ByteWriteStream byteWriteStream)
    {
        this(byteWriteStream, defaultCharacterEncoding, defaultNewLine);
    }

    TextWriteStreamBase(ByteWriteStream byteWriteStream, String newLine)
    {
        this(byteWriteStream, defaultCharacterEncoding, newLine);
    }

    TextWriteStreamBase(ByteWriteStream byteWriteStream, CharacterEncoding characterEncoding)
    {
        this(byteWriteStream, characterEncoding, defaultNewLine);
    }

    TextWriteStreamBase(ByteWriteStream byteWriteStream, CharacterEncoding characterEncoding, String newLine)
    {
        this.byteWriteStream = byteWriteStream;
        this.characterEncoding = characterEncoding;
        this.newLine = newLine;
    }

    protected ByteWriteStream getByteWriteStream()
    {
        return byteWriteStream;
    }

    protected CharacterEncoding getCharacterEncoding()
    {
        return characterEncoding;
    }

    protected String getNewLine()
    {
        return newLine;
    }

    @Override
    public boolean isOpen()
    {
        return byteWriteStream.isOpen();
    }

    @Override
    public boolean close()
    {
        return byteWriteStream.close();
    }

    @Override
    public void write(byte toWrite)
    {
        byteWriteStream.write(toWrite);
    }

    @Override
    public void write(byte[] toWrite)
    {
        byteWriteStream.write(toWrite);
    }

    @Override
    public void write(byte[] toWrite, int startIndex, int length)
    {
        byteWriteStream.write(toWrite, startIndex, length);
    }

    @Override
    public void write(String toWrite)
    {
        byteWriteStream.write(characterEncoding.encode(toWrite));
    }

    @Override
    public void writeLine()
    {
        write(newLine);
    }

    @Override
    public void writeLine(String toWrite)
    {
        if (toWrite == null)
        {
            toWrite = "";
        }
        write(toWrite + newLine);
    }
}
