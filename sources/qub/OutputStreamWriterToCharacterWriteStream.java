package qub;

public class OutputStreamWriterToCharacterWriteStream implements CharacterWriteStream
{
    private final ByteWriteStream byteWriteStream;
    private final java.io.OutputStreamWriter writer;
    private final CharacterEncoding characterEncoding;

    OutputStreamWriterToCharacterWriteStream(ByteWriteStream byteWriteStream, CharacterEncoding characterEncoding)
    {
        this.byteWriteStream = byteWriteStream;
        final java.io.OutputStream outputStream = new ByteWriteStreamToOutputStream(byteWriteStream);
        this.writer = new java.io.OutputStreamWriter(outputStream, java.nio.charset.StandardCharsets.US_ASCII);
        this.characterEncoding = characterEncoding;
    }

    @Override
    public final CharacterEncoding getCharacterEncoding()
    {
        return characterEncoding;
    }

    @Override
    public final Result<Void> write(String toWrite, Object... formattedStringArguments)
    {
        toWrite = Strings.format(toWrite, formattedStringArguments);

        Result<Void> result;
        try
        {
            writer.write(toWrite);
            writer.flush();
            result = Result.success();
        }
        catch (Throwable e)
        {
            result = Result.error(e);
        }
        return result;
    }

    @Override
    public final ByteWriteStream asByteWriteStream()
    {
        return byteWriteStream;
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
