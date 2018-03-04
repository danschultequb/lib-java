package qub;

import java.io.IOException;

public abstract class ByteWriteStreamDecorator implements ByteWriteStream
{
    private final ByteWriteStream innerByteWriteStream;

    protected ByteWriteStreamDecorator(ByteWriteStream innerByteWriteStream)
    {
        this.innerByteWriteStream = innerByteWriteStream;
    }

    @Override
    public void setExceptionHandler(Action1<IOException> exceptionHandler)
    {
        innerByteWriteStream.setExceptionHandler(exceptionHandler);
    }

    @Override
    public boolean write(byte toWrite)
    {
        return innerByteWriteStream.write(toWrite);
    }

    @Override
    public boolean write(byte[] toWrite)
    {
        return innerByteWriteStream.write(toWrite);
    }

    @Override
    public boolean write(byte[] toWrite, int startIndex, int length)
    {
        return innerByteWriteStream.write(toWrite);
    }

    @Override
    public boolean writeAll(ByteReadStream byteReadStream)
    {
        return innerByteWriteStream.writeAll(byteReadStream);
    }

    @Override
    public boolean isOpen()
    {
        return innerByteWriteStream.isOpen();
    }

    @Override
    public void close()
    {
        innerByteWriteStream.close();
    }
}
