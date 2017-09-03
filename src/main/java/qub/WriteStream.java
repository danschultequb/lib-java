package qub;

public interface WriteStream
{
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
    default void write(String toWrite)
    {
        write(toWrite, CharacterEncoding.ASCII);
    }

    /**
     * Write the provided String with an appended newline character to this WriteStream using the
     * default (ASCII) CharacterEncoding to convert the Stream to bytes.
     * @param toWrite The String to write to this WriteStream.
     */
    default void writeLine(String toWrite)
    {
        write(toWrite + '\n');
    }

    /**
     * Write the provided String to this WriteStream using the provided CharacterEncoding to convert
     * the String to bytes.
     * @param toWrite The String to write.
     * @param encoding The encoding to use to convert the String to bytes.
     */
    default void write(String toWrite, CharacterEncoding encoding)
    {
        write(encoding.encode(toWrite));
    }
}
