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
}
