package qub;

public class IPv4Address
{
    public static final IPv4Address localhost = IPv4Address.create(127, 0, 0, 1);

    private final byte[] bytes;

    private IPv4Address(byte[] bytes)
    {
        PreCondition.assertNotNull(bytes, "bytes");
        PreCondition.assertEqual(4, bytes.length, "bytes.length");

        this.bytes = bytes;
    }

    public static IPv4Address create(int value1, int value2, int value3, int value4)
    {
        PreCondition.assertBetween(0, value1, 255, "value1");
        PreCondition.assertBetween(0, value2, 255, "value2");
        PreCondition.assertBetween(0, value3, 255, "value3");
        PreCondition.assertBetween(0, value4, 255, "value4");

        return new IPv4Address(new byte[] { (byte)value1, (byte)value2, (byte)value3, (byte)value4 });
    }

    @Override
    public boolean equals(Object rhs)
    {
        return rhs instanceof IPv4Address && this.equals((IPv4Address)rhs);
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

    /**
     * Convert this IPv4Address to a 4-byte array.
     * @return The 4-byte array representation of this IPv4Address.
     */
    public byte[] toBytes()
    {
        final byte[] result = new byte[4];
        toBytes(result);
        return result;
    }

    /**
     * Write the byte representation of this IPv4Address to the provided byte[].
     * @param bytes The byte array to writeByte the byte representation of this IPv4Address to.
     */
    public void toBytes(byte[] bytes)
    {
        Array.copy(this.bytes, 0, bytes, 0, 4);
    }

    public static Result<IPv4Address> parse(String text)
    {
        PreCondition.assertNotNull(text, "text");

        return Result.create(() ->
        {
            final Iterator<Character> characters = Strings.iterate(text).start();

            final int value1 = IPv4Address.parseInteger(characters, 1).await();
            IPv4Address.parsePeriod(characters, 1).await();

            final int value2 = IPv4Address.parseInteger(characters, 2).await();
            IPv4Address.parsePeriod(characters, 2).await();

            final int value3 = IPv4Address.parseInteger(characters, 3).await();
            IPv4Address.parsePeriod(characters, 3).await();

            final int value4 = IPv4Address.parseInteger(characters, 4).await();

            if (characters.hasCurrent())
            {
                throw new ParseException("Expected an IPv4 address to end after the fourth value, but found " + Strings.escapeAndQuote(Characters.join(characters.toList())) + " instead.");
            }

            return IPv4Address.create(value1, value2, value3, value4);
        });
    }

    private static Result<Integer> parseInteger(Iterator<Character> characters, int valueNumber)
    {
        PreCondition.assertNotNull(characters, "characters");
        PreCondition.assertTrue(characters.hasStarted(), "characters.hasStarted()");
        PreCondition.assertBetween(1, valueNumber, 4, "valueNumber");

        return Result.create(() ->
        {
            if (!characters.hasCurrent())
            {
                throw new ParseException("Missing " + valueNumber + " value.");
            }
            else if (!Characters.isDigit(characters.getCurrent()))
            {
                throw new ParseException("Expected digit (0 - 9), but found " + Characters.escapeAndQuote(characters.getCurrent()) + " instead.");
            }

            final CharacterList valueText = CharacterList.create();
            int result = 0;
            do
            {
                valueText.add(characters.getCurrent());
                result = (result * 10) + (characters.getCurrent() - '0');
                characters.next();
            }
            while (characters.hasCurrent() && Characters.isDigit(characters.getCurrent()));

            if (result < 0 || 255 < result)
            {
                throw new ParseException("Expected " + valueNumber + " value to be between 0 and 255, but found " + valueText.toString() + " instead.");
            }

            PostCondition.assertBetween(0, result, 255, "result");

            return result;
        });
    }

    private static Result<Void> parsePeriod(Iterator<Character> characters, int periodNumber)
    {
        PreCondition.assertNotNull(characters, "characters");
        PreCondition.assertTrue(characters.hasStarted(), "characters.hasStarted()");
        PreCondition.assertBetween(1, periodNumber, 3, "periodNumber");

        return Result.create(() ->
        {
            if (!characters.hasCurrent())
            {
                throw new ParseException("Missing " + periodNumber + " period ('.').");
            }
            else if (characters.getCurrent() != '.')
            {
                throw new ParseException("Expected period ('.') but found " + Characters.escapeAndQuote(characters.getCurrent()) + " instead.");
            }
            characters.next();
        });
    }
}
