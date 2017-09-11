package qub;

/**
 * An interface for writing bytes to a stream.
 */
public interface ByteWriteStream
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
     * Write the provided byte to this ByteWriteStream.
     * @param toWrite The byte to write to this stream.
     */
    void write(byte toWrite);

    /**
     * Write the provided bytes to this ByteWriteStream.
     * @param toWrite The bytes to write to this stream.
     */
    void write(byte[] toWrite);

    /**
     * Write the provided subsection of bytes to this ByteWriteStream.
     * @param toWrite The array of bytes that contains the bytes to write to this stream.
     * @param startIndex The start index of the subsection inside toWrite to write.
     * @param length The number of bytes to write.
     */
    void write(byte[] toWrite, int startIndex, int length);
}