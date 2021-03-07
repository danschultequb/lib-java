package qub;

public class Base64BytesToDecodedBytesIterator implements Iterator<Byte>
{
    private final Base64ByteEncoding encoding;
    private final Iterator<Byte> encodedBytesIterator;
    private boolean started;
    private Byte currentDecodedByte;
    private int encodedByteSequenceNumber;
    private int currentTableIndex;

    private Base64BytesToDecodedBytesIterator(Base64ByteEncoding encoding, Iterator<Byte> encodedBytesIterator)
    {
        PreCondition.assertNotNull(encoding, "encoding");
        PreCondition.assertNotNull(encodedBytesIterator, "encodedBytesIterator");

        this.encoding = encoding;
        this.encodedBytesIterator = encodedBytesIterator;
        this.encodedByteSequenceNumber = 1;
    }

    public static Base64BytesToDecodedBytesIterator create(Base64ByteEncoding encoding, Iterator<Byte> encodedBytesIterator)
    {
        return new Base64BytesToDecodedBytesIterator(encoding, encodedBytesIterator);
    }

    @Override
    public boolean hasStarted()
    {
        return this.started;
    }

    @Override
    public boolean hasCurrent()
    {
        return this.currentDecodedByte != null;
    }

    @Override
    public Byte getCurrent()
    {
        PreCondition.assertTrue(this.hasCurrent(), "this.hasCurrent()");

        return this.currentDecodedByte;
    }

    @Override
    public boolean next()
    {
        if (!this.hasStarted())
        {
            this.started = true;
            this.encodedBytesIterator.start();
        }
        else if (this.encodedByteSequenceNumber == 1)
        {
            this.encodedBytesIterator.next();
        }

        if (!this.encodedBytesIterator.hasCurrent())
        {
            this.currentDecodedByte = null;
        }
        else if (this.encodedByteSequenceNumber == 1)
        {
            final byte base64Byte1 = this.encodedBytesIterator.getCurrent();
            final int tableIndex1 = this.encoding.getTableIndex(base64Byte1);
            if (!this.encodedBytesIterator.next())
            {
                this.currentDecodedByte = null;
                throw new ParseException("Missing 2nd byte in the Base 64 encoded sequence.");
            }
            else
            {
                final byte base64Byte2 = this.encodedBytesIterator.getCurrent();
                final int tableIndex2 = this.encoding.getTableIndex(base64Byte2);
                this.currentDecodedByte = (byte)((tableIndex1 << 2) | (tableIndex2 >>> 4));
                this.currentTableIndex = tableIndex2;
                this.encodedByteSequenceNumber = 2;
            }
        }
        else if (this.encodedByteSequenceNumber == 2)
        {
            if (!this.encodedBytesIterator.next())
            {
                this.currentDecodedByte = null;
                throw new ParseException("Missing 3rd byte in the Base 64 encoded sequence.");
            }

            final byte base64Byte3 = this.encodedBytesIterator.getCurrent();
            if (base64Byte3 == this.encoding.getPadding())
            {
                this.currentDecodedByte = null;
                this.currentTableIndex = 0;
                this.encodedByteSequenceNumber = 1;

                if (!this.encodedBytesIterator.next())
                {
                    throw new ParseException("Missing 2nd padding byte in the Base 64 encoded sequence.");
                }
                else
                {
                    final byte base64Byte4 = this.encodedBytesIterator.getCurrent();
                    if (base64Byte4 != this.encoding.getPadding())
                    {
                        throw new ParseException("Expected 2nd padding byte in the Base 64 encoded sequence, but found " + base64Byte4 + " (" + Bytes.toHexString(base64Byte4, true) + ", " + Characters.escapeAndQuote((char)base64Byte4) + ") instead.");
                    }
                    else
                    {
                        this.encodedBytesIterator.next();
                    }
                }
            }
            else
            {
                final int tableIndex2 = this.currentTableIndex;
                final int tableIndex3 = this.encoding.getTableIndex(base64Byte3);
                this.currentDecodedByte = (byte)(((tableIndex2 & 0x0F) << 4) | ((tableIndex3 & 0x3C) >>> 2));
                this.currentTableIndex = tableIndex3;
                this.encodedByteSequenceNumber = 3;
            }
        }
        else
        {
            if (!this.encodedBytesIterator.next())
            {
                this.currentDecodedByte = null;
                throw new ParseException("Missing 4th byte in the Base 64 encoded sequence.");
            }

            final byte base64Byte4 = this.encodedBytesIterator.getCurrent();
            if (base64Byte4 == this.encoding.getPadding())
            {
                this.currentDecodedByte = null;
            }
            else
            {
                final int tableIndex3 = this.currentTableIndex;
                final int tableIndex4 = this.encoding.getTableIndex(base64Byte4);
                this.currentDecodedByte = (byte)(((tableIndex3 & 0x03) << 6) | tableIndex4);
            }

            this.currentTableIndex = 0;
            this.encodedByteSequenceNumber = 1;
        }

        return this.hasCurrent();
    }
}
