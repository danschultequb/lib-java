package qub;

import java.io.ByteArrayInputStream;

public class InMemoryByteReadStream extends InputStreamToByteReadStream
{
    public InMemoryByteReadStream()
    {
        this(new byte[0]);
    }

    public InMemoryByteReadStream(byte[] bytes)
    {
        super(new ByteArrayInputStream(bytes));
    }


}
