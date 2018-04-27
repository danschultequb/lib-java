package qub;

/**
 * A class for reading lines from a stream.
 */
public interface LineReadStream extends Disposable, Iterator<String>
{
    Result<String> readLine();

    CharacterEncoding getCharacterEncoding();

    boolean getIncludeNewLines();

    ByteReadStream asByteReadStream();

    CharacterReadStream asCharacterReadStream();
}
