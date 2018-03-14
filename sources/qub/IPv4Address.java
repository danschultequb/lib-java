package qub;

public class IPv4Address
{
    private byte[] bytes;

    public IPv4Address(byte b1, byte b2, byte b3, byte b4)
    {
        this.bytes = new byte[] { b1, b2, b3, b4 };
    }

    @Override
    public boolean equals(Object rhs)
    {
        return rhs instanceof IPv4Address && equals((IPv4Address)rhs);
    }

    public boolean equals(IPv4Address rhs)
    {
        return rhs != null && Comparer.equal(bytes, rhs.bytes);
    }

    @Override
    public int hashCode()
    {
        return
            (Bytes.toUnsignedInt(bytes[0]) << 24) |
            (Bytes.toUnsignedInt(bytes[1]) << 16) |
            (Bytes.toUnsignedInt(bytes[2]) << 8) |
            Bytes.toUnsignedInt(bytes[3]);
    }

    @Override
    public String toString()
    {
        return Bytes.toUnsignedInt(bytes[0]) + "." +
            Bytes.toUnsignedInt(bytes[1]) + "." +
            Bytes.toUnsignedInt(bytes[2]) + "." +
            Bytes.toUnsignedInt(bytes[3]);
    }

    public static IPv4Address parse(String text)
    {
        IPv4Address result = null;

        final Lexer lexer = new Lexer(text);
        final Byte b1 = parseByte(lexer);
        if (b1 != null && parsePeriod(lexer))
        {
            final Byte b2 = parseByte(lexer);
            if (b2 != null && parsePeriod(lexer))
            {
                final Byte b3 = parseByte(lexer);
                if (b3 != null && parsePeriod(lexer))
                {
                    final Byte b4 = parseByte(lexer);
                    if (b4 != null && !lexer.next())
                    {
                        result = new IPv4Address(b1, b2, b3, b4);
                    }
                }
            }
        }

        return result;
    }

    private static Byte parseByte(Lexer lexer)
    {
        Byte result = null;

        if (lexer.next())
        {
            final Lex lex = lexer.getCurrent();
            if (lex.getType() == LexType.Digits)
            {
                try
                {
                    final int integer = Integer.parseInt(lex.toString());
                    if (Comparer.between(0, integer, 255))
                    {
                        result = (byte)integer;
                    }
                }
                catch (NumberFormatException ignored)
                {
                }
            }
        }

        return result;
    }

    private static boolean parsePeriod(Lexer lexer)
    {
        return lexer.next() && lexer.getCurrent().getType() == LexType.Period;
    }
}
