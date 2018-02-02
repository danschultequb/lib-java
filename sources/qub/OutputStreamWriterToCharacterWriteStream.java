package qub;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class OutputStreamWriterToCharacterWriteStream extends CharacterWriteStreamBase
{
    private final OutputStreamWriter writer;

    OutputStreamWriterToCharacterWriteStream(ByteWriteStream byteWriteStream, CharacterEncoding encoding)
    {
        super(byteWriteStream, encoding);

        byteWriteStream.setExceptionHandler(new Action1<IOException>()
        {
            @Override
            public void run(IOException e)
            {
                throw new RuntimeException(e);
            }
        });

        final OutputStream outputStream = new ByteWriteStreamToOutputStream(byteWriteStream);
        this.writer = new OutputStreamWriter(outputStream, encoding.getCharset());
    }

    @Override
    public boolean write(char toWrite)
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
    public boolean write(String toWrite)
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
}
