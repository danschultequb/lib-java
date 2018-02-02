package qub;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * An encoding that converts between characters and bytes.
 */
public class CharacterEncoding
{
    public static final CharacterEncoding US_ASCII = new CharacterEncoding(StandardCharsets.US_ASCII);
    public static final CharacterEncoding UTF_8 = new CharacterEncoding(StandardCharsets.UTF_8);

    private final Charset charset;

    private CharacterEncoding(Charset charset)
    {
        this.charset = charset;
    }

    Charset getCharset()
    {
        return charset;
    }

    public byte[] encode(char character)
    {
        return encode(Character.toString(character));
    }

    public byte[] encode(String text)
    {
        byte[] result = null;
        if (text != null)
        {
            final ByteBuffer byteBuffer = charset.encode(text);
            result = new byte[byteBuffer.limit()];
            byteBuffer.get(result);
        }
        return result;
    }

    public String decode(byte[] bytes)
    {
        String result = null;
        if (bytes != null)
        {
            final ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
            final CharBuffer charBuffer = charset.decode(byteBuffer);
            result = charBuffer.toString();
        }
        return result;
    }
}
