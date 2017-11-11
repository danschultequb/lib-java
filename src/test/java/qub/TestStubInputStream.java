package qub;

import java.io.IOException;
import java.io.InputStream;

public class TestStubInputStream extends InputStream
{
    private int available;

    private boolean throwOnClose;
    private boolean throwOnRead;

    public void setAvailable(int available)
    {
        this.available = available;
    }

    @Override
    public int available()
    {
        return available;
    }

    public void setThrowOnClose(boolean throwOnClose)
    {
        this.throwOnClose = throwOnClose;
    }

    @Override
    public void close() throws IOException
    {
        if (throwOnClose)
        {
            throw new IOException();
        }
    }

    public void setThrowOnRead(boolean throwOnRead)
    {
        this.throwOnRead = throwOnRead;
    }

    @Override
    public int read() throws IOException
    {
        if (throwOnRead)
        {
            throw new IOException();
        }
        return 0;
    }
}
