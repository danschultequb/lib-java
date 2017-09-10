package qub;

import java.io.IOException;

/**
 * An interface for reading text from a stream.
 */
public interface TextReadStream extends ByteReadStream
{
    /**
     * Read at most the provided number of characters from the stream. If the stream is closed, then
     * null will be returned.
     * @param charactersToRead The number of characters to read.
     * @return The characters that were read from the stream or null if the stream is closed.
     */
    char[] readCharacters(int charactersToRead) throws IOException;

    /**
     * Read a String from this stream that is at most the provided number of characters long. If the
     * stream is closed, then null will be returned.
     * @param stringLength The length of the String to read.
     * @return The String that was read from the stream, or null if the stream is closed.
     * @throws IOException if the stream was unable to read.
     */
    String readString(int stringLength) throws IOException;

    /**
     * Read characters from this stream into the provided character array, and then return the
     * number of characters that were read. If this stream is closed, then -1 will be returned.
     * @param output The character array to read characters into.
     * @return The number of characters that were read or -1 if the stream is closed.
     */
    int readCharacters(char[] output) throws IOException;

    /**
     * Read characters from this stream into the provided character array starting at startIndex and
     * reading a maximum number of length characters.
     * @param output The character array to read characters into.
     * @param startIndex The startIndex in the character array to read characters into.
     * @param length The number of characters to read.
     * @return The number of characters that were read or -1 if the stream is closed.
     */
    int readCharacters(char[] output, int startIndex, int length) throws IOException;

    /**
     * Read a line of text from this TextReadStream. If this stream is at its end or is closed, null
     * will be returned.
     * @return The line of text that was read, or null if the stream is at its end or is closed.
     */
    String readLine() throws IOException;

    /**
     * Read a line of text from this TextReadStream. If this stream is at its end or is closed, null
     * will be returned.
     * @param includeNewLineInLine Whether or not to include the new line character(s) in the
     *                             returned line.
     * @return The line of text that was read, or null if the stream is at its end or is closed.
     */
    String readLine(boolean includeNewLineInLine) throws IOException;
}
