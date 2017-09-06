package qub;

public class ASCII extends CharacterEncoding
{
    @Override
    public int encode(char value, byte[] output)
    {
        if (output != null && 0 < output.length)
        {
            final int characterIntegerValue = (int)value;
            output[0] = (byte)characterIntegerValue;
        }
        return 1;
    }

    @Override
    public int encode(String value, byte[] output)
    {
        int bytesWritten = 0;
        if (value != null && !value.isEmpty())
        {
            for (int i = 0; i < value.length(); ++i)
            {
                if (output != null && i < output.length)
                {
                    final int characterIntegerValue = (int) value.charAt(i);
                    output[i] = (byte) characterIntegerValue;
                }
                ++bytesWritten;
            }
        }
        return bytesWritten;
    }

    @Override
    public String decode(byte[] bytes) {
        final StringBuilder builder = new StringBuilder(bytes == null ? 0 : bytes.length);
        if (bytes != null && bytes.length > 0)
        {
            for (byte b : bytes)
            {
                builder.append((char) b);
            }
        }
        return builder.toString();
    }
}
