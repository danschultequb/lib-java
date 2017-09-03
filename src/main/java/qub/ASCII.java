package qub;

public class ASCII implements CharacterEncoding
{
    @Override
    public int encode(String value, byte[] output)
    {
        int bytesWritten = 0;
        for (int i = 0; i < value.length(); ++i)
        {
            if (output != null && i < output.length)
            {
                final int characterIntegerValue = (int)value.charAt(i);
                output[i] = (byte)characterIntegerValue;
            }
            ++bytesWritten;
        }
        return bytesWritten;
    }

    @Override
    public int encode(char value, byte[] output) {
        if (output != null && 0 < output.length)
        {
            final int characterIntegerValue = (int)value;
            output[0] = (byte)characterIntegerValue;
        }
        return 1;
    }

    @Override
    public String decodeString(byte[] bytes) {
        final StringBuilder builder = new StringBuilder(bytes == null ? 0 : bytes.length);
        for (byte b : bytes)
        {
            builder.append((char)b);
        }
        return builder.toString();
    }
}
