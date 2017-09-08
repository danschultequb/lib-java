package qub;

public class Console implements TextWriteStream
{
    final Value<TextWriteStream> writeStream;

    public Console()
    {
        writeStream = new Value<>();
    }

    public void setWriteStream(TextWriteStream writeStream)
    {
        this.writeStream.set(writeStream);
    }

    TextWriteStream getWriteStream()
    {
        if (!writeStream.hasValue())
        {
            writeStream.set(new StandardOutputWriteStream());
        }
        return writeStream.get();
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
}
