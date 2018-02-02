package qub;

import java.io.ByteArrayOutputStream;

public class InMemoryByteWriteStream extends OutputStreamToByteWriteStream
{
    public InMemoryByteWriteStream()
    {
        super(new ByteArrayOutputStream());
    }

    @Override
    protected ByteArrayOutputStream getOutputStream()
    {
        return (ByteArrayOutputStream)super.getOutputStream();
    }

    public byte[] getBytes()
    {
        return isOpen() ? getOutputStream().toByteArray() : null;
    }

    public void clear()
    {
        getOutputStream().reset();
    }
}
