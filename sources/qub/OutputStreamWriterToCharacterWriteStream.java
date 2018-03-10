package qub;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class OutputStreamWriterToCharacterWriteStream extends CharacterWriteStreamBase
{
    private final ByteWriteStream byteWriteStream;
    private final OutputStreamWriter writer;
    private final CharacterEncoding characterEncoding;

    OutputStreamWriterToCharacterWriteStream(ByteWriteStream byteWriteStream, CharacterEncoding characterEncoding)
    {
        byteWriteStream.setExceptionHandler(new Action1<IOException>()
        {
            @Override
            public void run(IOException e)
            {
                throw new RuntimeException(e);
            }
        });

        this.byteWriteStream = byteWriteStream;
        final OutputStream outputStream = new ByteWriteStreamToOutputStream(byteWriteStream);
        this.writer = new OutputStreamWriter(outputStream, characterEncoding.getCharset());
        this.characterEncoding = characterEncoding;
    }

    @Override
    public final CharacterEncoding getCharacterEncoding()
    {
        return characterEncoding;
    }

    @Override
    public final boolean write(char toWrite)
    {
        boolean result = false;
        try
        {
            writer.write(toWrite);
            writer.flush();
            result = true;
        }
        catch (Exception ignored)
        {
        }
        return result;
    }

    @Override
    public final boolean write(String toWrite, Object... formattedStringArguments)
    {
        if (CharacterWriteStreamBase.shouldFormat(toWrite, formattedStringArguments))
        {
            toWrite = String.format(toWrite, formattedStringArguments);
        }

        boolean result = false;
        try
        {
            writer.write(toWrite);
            writer.flush();
            result = true;
        }
        catch (Exception ignored)
        {
        }
        return result;
    }

    @Override
    public final ByteWriteStream asByteWriteStream()
    {
        return byteWriteStream;
    }

    @Override
    public final boolean isOpen()
    {
        return byteWriteStream.isOpen();
    }

    @Override
    public final void close()
    {
        try
        {
            byteWriteStream.close();
        }
        catch (Exception ignored)
        {
        }
    }
}
