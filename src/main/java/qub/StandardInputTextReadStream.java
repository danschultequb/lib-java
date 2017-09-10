package qub;

/**
 * A TextReadStream that reads text from the StandardInput stream of the process.
 */
public class StandardInputTextReadStream extends TextReadStreamBase
{
    public StandardInputTextReadStream()
    {
        super(new StandardInputByteReadStream());
    }

    public StandardInputTextReadStream(CharacterEncoding characterEncoding)
    {
        super(new StandardInputByteReadStream(), characterEncoding);
    }
}
