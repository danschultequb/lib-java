package qub;

public class TestStubOutputStream extends java.io.OutputStream
{
    @Override
    public void close() throws java.io.IOException
    {
        throw new java.io.IOException();
    }

    @Override
    public void write(int b) throws java.io.IOException
    {
        throw new java.io.IOException();
    }

    @Override
    public void write(byte[] b) throws java.io.IOException
    {
        throw new java.io.IOException();
    }

    @Override
    public void write(byte[] b, int offset, int len) throws java.io.IOException
    {
        throw new java.io.IOException();
    }
}
