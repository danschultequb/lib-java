package qub;

/**
 * An encoding that converts between Strings and bytes.
 */
public abstract class CharacterEncoding
{
    public static final ASCII ASCII = new ASCII();

    /**
     * Get the encoded bytes for the provided String in the provided byte[]. This function returns
     * how many bytes were written for the encoded value.
     * @param value The String value to encode.
     * @param output The byte array where the encoded bytes will be written.
     * @return The number of encoded bytes that were written.
     */
    public abstract int encode(String value, byte[] output);

    /**
     * Get the encoded bytes for the provided character in the provided byte[]. This function
     * returns how many bytes were written for the encoded value.
     * @param value The character value to encode.
     * @param output The byte array where the encoded bytes will be written.
     * @return The number of encoded bytes that were written.
     */
    public abstract int encode(char value, byte[] output);

    /**
     * Get the encoded bytes for the provided character in the provided byte[].
     * @param value The String value to encode.
     * @return The encoded bytes from the provided String.
     */
    public byte[] encode(String value)
    {
        byte[] result;
        if (value == null)
        {
            result = null;
        }
        else
        {
            final int requiredBytes = encode(value, null);
            result = new byte[requiredBytes];
            encode(value, result);
        }
        return result;
    }

    /**
     * Decode the provided byte array into a String.
     * @param bytes The byte array to decode.
     * @return The decoded characters.
     */
    public abstract char[] decode(byte[] bytes);
}
