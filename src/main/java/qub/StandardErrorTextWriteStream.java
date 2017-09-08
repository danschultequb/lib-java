package qub;

/**
 * A TextWriteStream that writes bytes and Strings to the StandardError stream of the process.
 */
public class StandardErrorTextWriteStream extends TextWriteStreamBase
{
    private static final StandardErrorByteWriteStream byteWriteStream = new StandardErrorByteWriteStream();

    public StandardErrorTextWriteStream()
    {
        super(byteWriteStream);
    }

    public StandardErrorTextWriteStream(String newLine)
    {
        super(byteWriteStream, newLine);
    }
}
