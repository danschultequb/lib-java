package qub;

/**
 * A TextWriteStream that writes bytes and Strings to the StandardOutput stream of the process.
 */
public class StandardOutputTextWriteStream extends TextWriteStreamBase
{
    private static final StandardOutputByteWriteStream byteWriteStream = new StandardOutputByteWriteStream();

    public StandardOutputTextWriteStream()
    {
        super(byteWriteStream);
    }

    public StandardOutputTextWriteStream(String newLine)
    {
        super(byteWriteStream, newLine);
    }
}
