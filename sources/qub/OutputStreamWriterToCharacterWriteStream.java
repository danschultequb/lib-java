package qub;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

public class OutputStreamWriterToCharacterWriteStream extends CharacterWriteStreamBase
{
    private final ByteWriteStream byteWriteStream;
    private final OutputStreamWriter writer;
    private final CharacterEncoding characterEncoding;

    OutputStreamWriterToCharacterWriteStream(ByteWriteStream byteWriteStream, CharacterEncoding characterEncoding)
    {
        this.byteWriteStream = byteWriteStream;
        final OutputStream outputStream = new ByteWriteStreamToOutputStream(byteWriteStream);
        this.writer = new OutputStreamWriter(outputStream, StandardCharsets.US_ASCII);
        this.characterEncoding = characterEncoding;
    }

    @Override
    public final CharacterEncoding getCharacterEncoding()
    {
        return characterEncoding;
    }

    @Override
    public final Result<Boolean> write(char toWrite)
    {
        Result<Boolean> result;
        try
        {
            writer.write(toWrite);
            writer.flush();
            result = Result.successTrue();
        }
        catch (Exception e)
        {
            result = Result.error(e);
        }
        return result;
    }

    @Override
    public final Result<Boolean> write(String toWrite, Object... formattedStringArguments)
    {
        if (CharacterWriteStreamBase.shouldFormat(toWrite, formattedStringArguments))
        {
            toWrite = String.format(toWrite, formattedStringArguments);
        }

        Result<Boolean> result;
        try
        {
            writer.write(toWrite);
            writer.flush();
            result = Result.successTrue();
        }
        catch (Exception e)
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
