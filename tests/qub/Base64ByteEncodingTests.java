package qub;

public interface Base64ByteEncodingTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(Base64ByteEncoding.class, () ->
        {
            runner.test("create()", (Test test) ->
            {
                final Base64ByteEncoding encoding = Base64ByteEncoding.create();
                test.assertNotNull(encoding);
                test.assertEqual(
                    ByteArray.create(
                        'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H',
                        'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P',
                        'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',
                        'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f',
                        'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',
                        'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
                        'w', 'x', 'y', 'z', '0', '1', '2', '3',
                        '4', '5', '6', '7', '8', '9', '+', '/'),
                    encoding.getTable());
                test.assertEqual('=', encoding.getPadding());
            });

            runner.testGroup("encodeByte(byte)", () ->
            {
                final Action2<Byte,byte[]> encodeByteTest = (Byte value, byte[] expected) ->
                {
                    runner.test("with " + value + " (" + Bytes.toHexString(value, true) + ")", (Test test) ->
                    {
                        final Base64ByteEncoding encoding = Base64ByteEncoding.create();
                        test.assertEqual(expected, encoding.encodeByte(value).await());
                    });
                };

                encodeByteTest.run((byte)0, new byte[] { 'A', 'A', '=', '=' });
                encodeByteTest.run((byte)1, new byte[] { 'A', 'Q', '=', '=' });
                encodeByteTest.run((byte)2, new byte[] { 'A', 'g', '=', '=' });
                encodeByteTest.run((byte)3, new byte[] { 'A', 'w', '=', '=' });
                encodeByteTest.run((byte)4, new byte[] { 'B', 'A', '=', '=' });
                encodeByteTest.run((byte)5, new byte[] { 'B', 'Q', '=', '=' });
                encodeByteTest.run((byte)6, new byte[] { 'B', 'g', '=', '=' });
                encodeByteTest.run((byte)7, new byte[] { 'B', 'w', '=', '=' });
                encodeByteTest.run((byte)8, new byte[] { 'C', 'A', '=', '=' });
                encodeByteTest.run((byte)'M', new byte[] { 'T', 'Q', '=', '=' });
                encodeByteTest.run((byte)0xFE, new byte[] { '/', 'g', '=', '=' });
                encodeByteTest.run((byte)0xFF, new byte[] { '/', 'w', '=', '=' });
            });

            runner.testGroup("encodeBytes(byte[])", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final Base64ByteEncoding encoding = Base64ByteEncoding.create();
                    test.assertThrows(() -> encoding.encodeBytes((byte[])null),
                        new PreConditionFailure("values cannot be null."));
                });

                final Action2<byte[],byte[]> encodeBytesTest = (byte[] values, byte[] expected) ->
                {
                    runner.test("with " + ByteArray.create(values), (Test test) ->
                    {
                        final Base64ByteEncoding encoding = Base64ByteEncoding.create();
                        test.assertEqual(expected, encoding.encodeBytes(values).await());
                    });
                };

                encodeBytesTest.run(new byte[0], new byte[0]);
                encodeBytesTest.run(new byte[] { 'M' }, new byte[] { 'T', 'Q', '=', '=' });
                encodeBytesTest.run(new byte[] { 'M', 'a' }, new byte[] { 'T', 'W', 'E', '=' });
                encodeBytesTest.run(new byte[] { 'M', 'a', 'n' }, new byte[] { 'T', 'W', 'F', 'u' });
                encodeBytesTest.run(new byte[] { 'M', 'a', 'n', 'M' }, new byte[] { 'T', 'W', 'F', 'u', 'T', 'Q', '=', '=' });
                encodeBytesTest.run(new byte[] { 'M', 'a', 'n', 'M', 'a' }, new byte[] { 'T', 'W', 'F', 'u', 'T', 'W', 'E', '=' });
                encodeBytesTest.run(new byte[] { 'M', 'a', 'n', 'M', 'a', 'n' }, new byte[] { 'T', 'W', 'F', 'u', 'T', 'W', 'F', 'u' });

                runner.test("with long value", (Test test) ->
                {
                    final String text = "Man is distinguished, not only by his reason, but by this singular passion from other animals, which is a lust of the mind, that by a perseverance of delight in the continued and indefatigable generation of knowledge, exceeds the short vehemence of any carnal pleasure.";
                    final byte[] textBytes = CharacterEncoding.UTF_8.encodeCharacters(text).await();
                    final byte[] base64Bytes = ByteEncoding.Base64.encodeBytes(textBytes).await();
                    final String base64Text = CharacterEncoding.UTF_8.decodeAsString(base64Bytes).await();
                    final String expectedText = "TWFuIGlzIGRpc3Rpbmd1aXNoZWQsIG5vdCBvbmx5IGJ5IGhpcyByZWFzb24sIGJ1dCBieSB0aGlzIHNpbmd1bGFyIHBhc3Npb24gZnJvbSBvdGhlciBhbmltYWxzLCB3aGljaCBpcyBhIGx1c3Qgb2YgdGhlIG1pbmQsIHRoYXQgYnkgYSBwZXJzZXZlcmFuY2Ugb2YgZGVsaWdodCBpbiB0aGUgY29udGludWVkIGFuZCBpbmRlZmF0aWdhYmxlIGdlbmVyYXRpb24gb2Yga25vd2xlZGdlLCBleGNlZWRzIHRoZSBzaG9ydCB2ZWhlbWVuY2Ugb2YgYW55IGNhcm5hbCBwbGVhc3VyZS4=";
                    test.assertEqual(expectedText, base64Text);
                });
            });

            runner.testGroup("decodeAsBytes(byte[])", () ->
            {
                final Action2<byte[],Throwable> decodeAsBytesErrorTest = (byte[] encodedBytes, Throwable expected) ->
                {
                    runner.test("with " + (encodedBytes == null ? null : ByteArray.create(encodedBytes)), (Test test) ->
                    {
                        final Base64ByteEncoding encoding = Base64ByteEncoding.create();
                        test.assertThrows(() -> encoding.decodeAsBytes(encodedBytes).await(), expected);
                    });
                };

                decodeAsBytesErrorTest.run(null, new PreConditionFailure("encodedBytes cannot be null."));
                decodeAsBytesErrorTest.run(new byte[] { 'T' }, new ParseException("Missing 2nd byte in the Base 64 encoded sequence."));
                decodeAsBytesErrorTest.run(new byte[] { 'T', 'Q' }, new ParseException("Missing 3rd byte in the Base 64 encoded sequence."));
                decodeAsBytesErrorTest.run(new byte[] { 'T', 'Q', 'E' }, new ParseException("Missing 4th byte in the Base 64 encoded sequence."));
                decodeAsBytesErrorTest.run(new byte[] { '$' }, new PreConditionFailure("this.table.contains(tableByte) cannot be false."));
                decodeAsBytesErrorTest.run(new byte[] { 'T', 'Q', '=' }, new ParseException("Missing 2nd padding byte in the Base 64 encoded sequence."));
                decodeAsBytesErrorTest.run(new byte[] { 'T', 'Q', '=', 'a' }, new ParseException("Expected 2nd padding byte in the Base 64 encoded sequence, but found 97 (0x61, \"a\") instead."));

                final Action2<byte[],byte[]> decodeAsBytesTest = (byte[] encodedBytes, byte[] expected) ->
                {
                    runner.test("with " + ByteArray.create(encodedBytes), (Test test) ->
                    {
                        final Base64ByteEncoding encoding = Base64ByteEncoding.create();
                        final byte[] decodedBytes = encoding.decodeAsBytes(encodedBytes).await();
                        test.assertEqual(expected, decodedBytes);
                    });
                };

                decodeAsBytesTest.run(
                    new byte[0],
                    new byte[0]);
                decodeAsBytesTest.run(
                    new byte[] { 'T', 'Q', '=', '=' },
                    new byte[] { 'M' });
                decodeAsBytesTest.run(
                    new byte[] { 'T', 'W', 'E', '=' },
                    new byte[] { 'M', 'a' });
                decodeAsBytesTest.run(
                    new byte[] { 'T', 'W', 'F', 'u' },
                    new byte[] { 'M', 'a', 'n' });
                decodeAsBytesTest.run(
                    new byte[] { 'T', 'W', 'F', 'u', 'T', 'Q', '=', '=' },
                    new byte[] { 'M', 'a', 'n', 'M' });
                decodeAsBytesTest.run(
                    new byte[] { 'T', 'W', 'F', 'u', 'T', 'W', 'E', '=' },
                    new byte[] { 'M', 'a', 'n', 'M', 'a' });
                decodeAsBytesTest.run(
                    new byte[] { 'T', 'W', 'F', 'u', 'T', 'W', 'F', 'u' },
                    new byte[] { 'M', 'a', 'n', 'M', 'a', 'n' });
            });
        });
    }
}
