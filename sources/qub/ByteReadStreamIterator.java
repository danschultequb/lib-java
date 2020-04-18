package qub;

public class ByteReadStreamIterator implements Iterator<Byte>
{
    private final ByteReadStream byteReadStream;
    private boolean hasStarted;
    private Byte current;

    private ByteReadStreamIterator(ByteReadStream byteReadStream)
    {
        PreCondition.assertNotNull(byteReadStream, "byteReadStream");

        this.byteReadStream = byteReadStream;
    }

    public static ByteReadStreamIterator create(ByteReadStream byteReadStream)
    {
        PreCondition.assertNotNull(byteReadStream, "byteReadStream");

        return new ByteReadStreamIterator(byteReadStream);
    }

    @Override
    public boolean hasStarted()
    {
        return this.hasStarted;
    }

    @Override
    public boolean hasCurrent()
    {
        return this.current != null;
    }

    @Override
    public Byte getCurrent()
    {
        return this.getCurrentByte();
    }

    public byte getCurrentByte()
    {
        PreCondition.assertTrue(this.hasCurrent(), "this.hasCurrent()");

        return this.current;
    }

    @Override
    public boolean next()
    {
        this.hasStarted = true;

        this.byteReadStream.readByte()
            .then((Byte readByte) ->
            {
                this.current = readByte;
            })
            .catchError(EndOfStreamException.class, () ->
            {
                this.current = null;
            })
            .await();

        return this.hasCurrent();
    }
}
