package qub;

public abstract class WriteStreamBase implements WriteStream
{
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

    static void write(WriteStream writeStream, String toWrite)
    {
        writeStream.write(toWrite, CharacterEncoding.ASCII);
    }

    static void writeLine(WriteStream writeStream)
    {
        writeStream.write("\n");
    }

    static void writeLine(WriteStream writeStream, String toWrite)
    {
        writeStream.write(toWrite + '\n');
    }

    static void write(WriteStream writeStream, String toWrite, CharacterEncoding encoding)
    {
        writeStream.write(encoding.encode(toWrite));
    }
}
