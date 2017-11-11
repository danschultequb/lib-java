package qub;

/**
 * A class for reading lines from a stream.
 */
public interface LineReadStream extends Stream, Iterator<String>
{
    String readLine();

    String readLine(boolean includeNewLine);

    CharacterEncoding getCharacterEncoding();

    boolean getIncludeNewLines();

    ByteReadStream asByteReadStream();

    CharacterReadStream asCharacterReadStream();
}
