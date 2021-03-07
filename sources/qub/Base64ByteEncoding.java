package qub;

public class Base64ByteEncoding implements ByteEncoding
{
    private static byte[] defaultTable;
    private static final byte defaultPadding = '=';

    private static byte[] getDefaultTable()
    {
        if (Base64ByteEncoding.defaultTable == null)
        {
            Base64ByteEncoding.defaultTable = new byte[]
            {
                'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H',
                'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P',
                'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',
                'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f',
                'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',
                'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
                'w', 'x', 'y', 'z', '0', '1', '2', '3',
                '4', '5', '6', '7', '8', '9', '+', '/'
            };
        }
        final byte[] result = Base64ByteEncoding.defaultTable;

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(64, result.length, "result.length");
        PostCondition.assertSame(result, Base64ByteEncoding.defaultTable, "Base64ByteEncoding.defaultTable");

        return result;
    }

    private ByteArray table;
    private byte padding;

    private Base64ByteEncoding(byte[] table, byte padding)
    {
        PreCondition.assertNotNull(table, "table");
        PreCondition.assertEqual(64, table.length, "table.length");
        PreCondition.assertEqual(64, Set.create(ByteArray.create(table)).getCount(), "Set.create(ByteArray.create(table)).getCount()");
        PreCondition.assertFalse(ByteArray.create(table).contains(padding), "ByteArray.create(table).contains(padding)");

        this.table = ByteArray.create(table);
        this.padding = padding;
    }

    public static Base64ByteEncoding create()
    {
        return new Base64ByteEncoding(Base64ByteEncoding.getDefaultTable(), Base64ByteEncoding.defaultPadding);
    }

    public Indexable<Byte> getTable()
    {
        return this.table;
    }

    public byte getPadding()
    {
        return this.padding;
    }

    public byte getTableByte(int tableIndex)
    {
        PreCondition.assertIndexAccess(tableIndex, this.table.getCount(), "value");

        return this.table.get(tableIndex);
    }

    public int getTableIndex(byte tableByte)
    {
        PreCondition.assertTrue(this.table.contains(tableByte), "this.table.contains(tableByte)");

        final int result = this.table.indexOf(tableByte);

        PostCondition.assertBetween(0, result, this.table.getCount(), "result");

        return result;
    }

    /**
     * Encode the provided values and write the encoded bytes to the provided ByteWriteStream.
     * Return the number of bytes that were written.
     * @param values The character array to encode.
     * @param byteWriteStream The ByteWriteStream to write the encoded bytes to.
     * @return The number of bytes that were written.
     */
    @Override
    public Result<Integer> encodeBytes(Iterator<Byte> values, ByteWriteStream byteWriteStream)
    {
        PreCondition.assertNotNull(values, "values");
        PreCondition.assertNotNull(byteWriteStream, "byteWriteStream");

        return Result.create(() ->
        {
            values.start();

            int result = 0;

            while (values.hasCurrent())
            {
                final byte value1 = values.takeCurrent();
                final int tableIndex1 = (value1 & 0xFC) >>> 2;
                int tableIndex2 = (value1 & 0x03) << 4;
                result += byteWriteStream.write(this.getTableByte(tableIndex1)).await();

                if (!values.hasCurrent())
                {
                    result += byteWriteStream.write(this.getTableByte(tableIndex2)).await();
                    result += byteWriteStream.write(this.padding).await();
                    result += byteWriteStream.write(this.padding).await();
                }
                else
                {
                    final byte value2 = values.takeCurrent();
                    tableIndex2 |= (value2 & 0xF0) >>> 4;
                    int tableIndex3 = (value2 & 0x0F) << 2;
                    result += byteWriteStream.write(this.getTableByte(tableIndex2)).await();

                    if (!values.hasCurrent())
                    {
                        result += byteWriteStream.write(this.getTableByte(tableIndex3)).await();
                        result += byteWriteStream.write(this.padding).await();
                    }
                    else
                    {
                        final byte value3 = values.takeCurrent();
                        tableIndex3 |= (value3 & 0xC0) >>> 6;
                        final int tableIndex4 = (value3 & 0x3F);
                        result += byteWriteStream.write(this.getTableByte(tableIndex3)).await();
                        result += byteWriteStream.write(this.getTableByte(tableIndex4)).await();
                    }
                }
            }

            PostCondition.assertGreaterThanOrEqualTo(result, 0, "result");

            return result;
        });
    }

    @Override
    public Iterator<Byte> iterateDecodedBytes(Iterator<Byte> encodedBytes)
    {
        PreCondition.assertNotNull(encodedBytes, "encodedBytes");

        return Base64BytesToDecodedBytesIterator.create(this, encodedBytes);
    }
}
