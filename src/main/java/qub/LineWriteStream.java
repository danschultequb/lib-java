package qub;

public interface LineWriteStream
{
    CharacterEncoding getCharacterEncoding();

    String getLineSeparator();

    /**
     * Write the provided byte to this ByteWriteStream.
     * @param toWrite The byte to write to this stream.
     */
    boolean write(byte toWrite);

    /**
     * Write the provided bytes to this ByteWriteStream.
     * @param toWrite The bytes to write to this stream.
     */
    boolean write(byte[] toWrite);

    /**
     * Write the provided subsection of bytes to this ByteWriteStream.
     * @param toWrite The array of bytes that contains the bytes to write to this stream.
     * @param startIndex The start index of the subsection inside toWrite to write.
     * @param length The number of bytes to write.
     */
    boolean write(byte[] toWrite, int startIndex, int length);

    boolean write(char toWrite);

    boolean write(String toWrite);

    boolean writeLine();

    boolean writeLine(String toWrite);

    ByteWriteStream asByteWriteStream();

    CharacterWriteStream asCharacterWriteStream();
}
