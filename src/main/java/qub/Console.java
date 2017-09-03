package qub;

public class Console implements WriteStream
{
    private WriteStream writeStream;

    public Console()
    {
        writeStream = new StandardOutputWriteStream();
    }

    public void setWriteStream(WriteStream writeStream)
    {
        this.writeStream = writeStream;
    }

    @Override
    public void write(byte toWrite)
    {
        writeStream.write(toWrite);
    }

    @Override
    public void write(byte[] toWrite)
    {
        writeStream.write(toWrite);
    }

    @Override
    public void write(byte[] toWrite, int startIndex, int length)
    {
        writeStream.write(toWrite, startIndex, length);
    }

    public static void main(String[] args)
    {
        final Console console = new Console();
        console.writeLine("Hello world!");
    }
}
