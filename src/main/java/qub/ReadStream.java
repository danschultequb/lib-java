package qub;

import java.io.IOException;

/**
 * An interface for reading bytes from a stream.
 */
public interface ReadStream
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
     * Read at most the provided number of bytes from the stream. If the stream is closed, then null
     * will be returned.
     * @param bytesToRead The number of bytes to read.
     * @return The bytes that were read from the stream.
     */
    byte[] readBytes(int bytesToRead) throws IOException;

    /**
     * Read bytes from this stream into the provided byte array, and then return the number of bytes
     * that were read. If this stream is closed, then -1 will be returned.
     * @param output The byte array to read bytes into.
     * @return The number of bytes that were read.
     */
    int readBytes(byte[] output) throws IOException;

    /**
     * Read bytes from this tream into the provided byte array starting at startIndex and reading a
     * maximum number of length bytes.
     * @param output The byte array to read bytes into.
     * @param startIndex The startIndex in the byte array to read bytes into.
     * @param length The number of bytes to read.
     * @return The number of bytes that were read.
     */
    int readBytes(byte[] output, int startIndex, int length) throws IOException;
}
