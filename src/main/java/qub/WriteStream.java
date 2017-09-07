package qub;

/**
 * An interface for writing bytes to a stream.
 */
public interface WriteStream
{
    /**
     * Get whether or not this stream is open.
     * @return Whether or not this stream is open.
     */
    boolean isOpen();

    /**
     * Close this stream and return whether or not the stream was closed as a result of this call.
     * @return Whether or not the stream was closed as a result of this call.
     */
    boolean close();

    /**
     * Write the provided byte to this WriteStream.
     * @param toWrite The byte to write to this stream.
     */
    void write(byte toWrite);

    /**
     * Write the provided bytes to this WriteStream.
     * @param toWrite The bytes to write to this stream.
     */
    void write(byte[] toWrite);

    /**
     * Write the provided subsection of bytes to this WriteStream.
     * @param toWrite The array of bytes that contains the bytes to write to this stream.
     * @param startIndex The start index of the subsection inside toWrite to write.
     * @param length The number of bytes to write.
     */
    void write(byte[] toWrite, int startIndex, int length);

    /**
     * Write the provided String to this WriteStream using the default (ASCII) CharacterEncoding to
     * convert the String to bytes.
     * @param toWrite The String to write.
     */
    void write(String toWrite);

    /**
     * Write a newline character to this WriteStream using the default (ASCII) CharacterEncoding to
     * convert the String to bytes.
     */
    void writeLine();

    /**
     * Write the provided String with an appended newline character to this WriteStream using the
     * default (ASCII) CharacterEncoding to convert the String to bytes.
     * @param toWrite The String to write to this WriteStream.
     */
    void writeLine(String toWrite);

    /**
     * Write the provided String to this WriteStream using the provided CharacterEncoding to convert
     * the String to bytes.
     * @param toWrite The String to write.
     * @param encoding The encoding to use to convert the String to bytes.
     */
    void write(String toWrite, CharacterEncoding encoding);
}
