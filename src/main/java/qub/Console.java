package qub;

import java.io.IOException;

public class Console implements TextWriteStream, TextReadStream
{
    final Value<TextWriteStream> writeStream;
    final Value<TextReadStream> readStream;

    public Console()
    {
        writeStream = new Value<>();
        readStream = new Value<>();
    }

    public void setWriteStream(TextWriteStream writeStream)
    {
        this.writeStream.set(writeStream);
    }

    TextWriteStream getWriteStream()
    {
        if (!writeStream.hasValue())
        {
            writeStream.set(new StandardOutputTextWriteStream());
        }
        return writeStream.get();
    }

    public void setReadStream(TextReadStream readStream)
    {
        this.readStream.set(readStream);
    }

    TextReadStream getReadStream()
    {
        if (!readStream.hasValue())
        {
            readStream.set(new StandardInputTextReadStream());
        }
        return readStream.get();
    }

    @Override
    public boolean isOpen()
    {
        return true;
    }

    @Override
    public boolean close()
    {
        return false;
    }

    @Override
    public void write(byte toWrite)
    {
        final TextWriteStream writeStream = getWriteStream();
        if (writeStream != null)
        {
            writeStream.write(toWrite);
        }
    }

    @Override
    public void write(byte[] toWrite)
    {
        final TextWriteStream writeStream = getWriteStream();
        if (writeStream != null)
        {
            writeStream.write(toWrite);
        }
    }

    @Override
    public void write(byte[] toWrite, int startIndex, int length)
    {
        final TextWriteStream writeStream = getWriteStream();
        if (writeStream != null)
        {
            writeStream.write(toWrite, startIndex, length);
        }
    }

    @Override
    public void write(String toWrite)
    {
        final TextWriteStream writeStream = getWriteStream();
        if (writeStream != null)
        {
            writeStream.write(toWrite);
        }
    }

    @Override
    public void writeLine()
    {
        final TextWriteStream writeStream = getWriteStream();
        if (writeStream != null)
        {
            writeStream.writeLine();
        }
    }

    @Override
    public void writeLine(String toWrite)
    {
        final TextWriteStream writeStream = getWriteStream();
        if (writeStream != null)
        {
            writeStream.writeLine(toWrite);
        }
    }

    @Override
    public byte[] readBytes(int bytesToRead) throws IOException
    {
        byte[] result = null;

        final TextReadStream readStream = getReadStream();
        if (readStream != null)
        {
            result = readStream.readBytes(bytesToRead);
        }

        return result;
    }

    @Override
    public int readBytes(byte[] output) throws IOException
    {
        int result = -1;

        final TextReadStream readStream = getReadStream();
        if (readStream != null)
        {
            result = readStream.readBytes(output);
        }

        return result;
    }

    @Override
    public int readBytes(byte[] output, int startIndex, int length) throws IOException
    {
        int result = -1;

        final TextReadStream readStream = getReadStream();
        if (readStream != null)
        {
            result = readStream.readBytes(output, startIndex, length);
        }

        return result;
    }

    @Override
    public char[] readCharacters(int charactersToRead) throws IOException
    {
        char[] result = null;

        final TextReadStream readStream = getReadStream();
        if (readStream != null)
        {
            result = readStream.readCharacters(charactersToRead);
        }

        return result;
    }

    @Override
    public int readCharacters(char[] output) throws IOException
    {
        int result = -1;

        final TextReadStream readStream = getReadStream();
        if (readStream != null)
        {
            result = readStream.readCharacters(output);
        }

        return result;
    }

    @Override
    public int readCharacters(char[] output, int startIndex, int length) throws IOException
    {
        int result = -1;

        final TextReadStream readStream = getReadStream();
        if (readStream != null)
        {
            result = readStream.readCharacters(output, startIndex, length);
        }

        return result;
    }

    @Override
    public String readString(int stringLength) throws IOException
    {
        String result = null;

        final TextReadStream readStream = getReadStream();
        if (readStream != null)
        {
            result = readStream.readString(stringLength);
        }

        return result;
    }

    @Override
    public String readLine() throws IOException
    {
        String result = null;

        final TextReadStream readStream = getReadStream();
        if (readStream != null)
        {
            result = readStream.readLine();
        }

        return result;
    }

    @Override
    public String readLine(boolean includeNewLineInLine) throws IOException
    {
        String result = null;

        final TextReadStream readStream = getReadStream();
        if (readStream != null)
        {
            result = readStream.readLine(includeNewLineInLine);
        }

        return result;
    }
}
