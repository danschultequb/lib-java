package qub;

public class Console implements WriteStream
{
    final Value<WriteStream> writeStream;

    public Console()
    {
        writeStream = new Value<>();
    }

    public void setWriteStream(WriteStream writeStream)
    {
        this.writeStream.set(writeStream);
    }

    WriteStream getWriteStream()
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
        final WriteStream writeStream = getWriteStream();
        if (writeStream != null)
        {
            writeStream.write(toWrite);
        }
    }

    @Override
    public void write(byte[] toWrite)
    {
        final WriteStream writeStream = getWriteStream();
        if (writeStream != null)
        {
            writeStream.write(toWrite);
        }
    }

    @Override
    public void write(byte[] toWrite, int startIndex, int length)
    {
        final WriteStream writeStream = getWriteStream();
        if (writeStream != null)
        {
            writeStream.write(toWrite, startIndex, length);
        }
    }

    @Override
    public void write(String toWrite)
    {
        WriteStreamBase.write(this, toWrite);
    }

    @Override
    public void writeLine()
    {
        WriteStreamBase.writeLine(this);
    }

    @Override
    public void writeLine(String toWrite)
    {
        WriteStreamBase.writeLine(this, toWrite);
    }

    @Override
    public void write(String toWrite, CharacterEncoding encoding)
    {
        WriteStreamBase.write(this, toWrite, encoding);
    }
}
