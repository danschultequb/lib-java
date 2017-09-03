package qub;

public class WriteStreamList implements WriteStream
{
    private final ArrayList<WriteStream> streams;

    public WriteStreamList()
    {
        streams = new ArrayList<>();
    }

    public void add(WriteStream writeStream)
    {
        streams.add(writeStream);
    }

    @Override
    public void write(byte toWrite)
    {
        for (WriteStream stream : streams)
        {
            stream.write(toWrite);
        }
    }

    @Override
    public void write(byte[] toWrite)
    {
        for (WriteStream stream : streams)
        {
            stream.write(toWrite);
        }
    }

    @Override
    public void write(byte[] toWrite, int startIndex, int length)
    {
        for (WriteStream stream : streams)
        {
            stream.write(toWrite, startIndex, length);
        }
    }
}
