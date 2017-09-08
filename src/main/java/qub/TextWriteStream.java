package qub;

/**
 * An interface for writing text to a stream.
 */
public interface TextWriteStream extends ByteWriteStream
{
    /**
     * Write the provided String.
     * @param toWrite The String to write.
     */
    void write(String toWrite);

    /**
     * Write a newline String.
     */
    void writeLine();

    /**
     * Write the provided String with an appended newline String.
     * @param toWrite The String to write.
     */
    void writeLine(String toWrite);
}
