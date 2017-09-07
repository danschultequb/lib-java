package qub;

public class Console implements ByteWriteStream
{
    final Value<ByteWriteStream> writeStream;

    public Console()
    {
        writeStream = new Value<>();
    }

    public void setWriteStream(ByteWriteStream writeStream)
    {
        this.writeStream.set(writeStream);
    }

    ByteWriteStream getWriteStream()
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
        final ByteWriteStream writeStream = getWriteStream();
        if (writeStream != null)
        {
            writeStream.write(toWrite);
        }
    }

    @Override
    public void write(byte[] toWrite)
    {
        final ByteWriteStream writeStream = getWriteStream();
        if (writeStream != null)
        {
            writeStream.write(toWrite);
        }
    }

    @Override
    public void write(byte[] toWrite, int startIndex, int length)
    {
        final ByteWriteStream writeStream = getWriteStream();
        if (writeStream != null)
        {
            writeStream.write(toWrite, startIndex, length);
        }
    }

//    @Override
//    public void write(String toWrite)
//    {
//        ByteWriteStreamBase.write(this, toWrite);
//    }
//
//    @Override
//    public void writeLine()
//    {
//        ByteWriteStreamBase.writeLine(this);
//    }
//
//    @Override
//    public void writeLine(String toWrite)
//    {
//        ByteWriteStreamBase.writeLine(this, toWrite);
//    }
//
//    @Override
//    public void write(String toWrite, CharacterEncoding encoding)
//    {
//        ByteWriteStreamBase.write(this, toWrite, encoding);
//    }
}
