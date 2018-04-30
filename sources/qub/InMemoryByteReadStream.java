package qub;

import java.io.ByteArrayInputStream;

public class InMemoryByteReadStream extends InputStreamToByteReadStream
{
    public InMemoryByteReadStream()
    {
        this(new byte[0], null);
    }

    public InMemoryByteReadStream(AsyncRunner asyncRunner)
    {
        this(new byte[0], asyncRunner);
    }

    public InMemoryByteReadStream(byte[] bytes)
    {
        this(bytes, null);
    }

    public InMemoryByteReadStream(byte[] bytes, AsyncRunner asyncRunner)
    {
        super(new ByteArrayInputStream(bytes == null ? new byte[0] : bytes), asyncRunner);
    }
}
