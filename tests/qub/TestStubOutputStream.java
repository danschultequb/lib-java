package qub;

import java.io.IOException;
import java.io.OutputStream;

public class TestStubOutputStream extends OutputStream
{
    @Override
    public void close() throws IOException
    {
        throw new IOException();
    }

    @Override
    public void write(int b) throws IOException
    {
        throw new IOException();
    }
}
