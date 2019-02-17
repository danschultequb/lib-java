package qub;

public class MD5Tests
{
    public static void test(TestRunner runner)
    {
        final MD5 md5 = new MD5();

        runner.testGroup(MD5.class, () ->
        {
            runner.testGroup("getPaddingFromBitCount(long)", () ->
            {
                runner.test("with 0", (Test test) ->
                {
                    final BitArray expectedPadding = new BitArray(512);
                    expectedPadding.setBit(0, 1);
                    test.assertEqual(expectedPadding, md5.getPaddingFromBitCount(0));
                });

                runner.test("with 1", (Test test) ->
                {
                    final BitArray expectedPadding = new BitArray(511);
                    expectedPadding.setBit(0, 1);
                    expectedPadding.setLastBitsFromLong(ByteOrder.toLittleEndianLong(1));
                    test.assertEqual(expectedPadding, md5.getPaddingFromBitCount(1));
                });

                runner.test("with 2", (Test test) ->
                {
                    final BitArray expectedPadding = new BitArray(510);
                    expectedPadding.setBit(0, 1);
                    expectedPadding.setLastBitsFromLong(ByteOrder.toLittleEndianLong(2));
                    test.assertEqual(expectedPadding, md5.getPaddingFromBitCount(2));
                });

                runner.test("with 3", (Test test) ->
                {
                    final BitArray expectedPadding = new BitArray(509);
                    expectedPadding.setBit(0, 1);
                    expectedPadding.setLastBitsFromLong(ByteOrder.toLittleEndianLong(3));
                    test.assertEqual(expectedPadding, md5.getPaddingFromBitCount(3));
                });

                runner.test("with 4", (Test test) ->
                {
                    final BitArray expectedPadding = new BitArray(508);
                    expectedPadding.setBit(0, 1);
                    expectedPadding.setLastBitsFromLong(ByteOrder.toLittleEndianLong(4));
                    test.assertEqual(expectedPadding, md5.getPaddingFromBitCount(4));
                });

                runner.test("with 445", (Test test) ->
                {
                    final BitArray expectedPadding = new BitArray(67);
                    expectedPadding.setBit(0, 1);
                    expectedPadding.setLastBitsFromLong(ByteOrder.toLittleEndianLong(445));
                    test.assertEqual(expectedPadding, md5.getPaddingFromBitCount(445));
                });

                runner.test("with 446", (Test test) ->
                {
                    final BitArray expectedPadding = new BitArray(66);
                    expectedPadding.setBit(0, 1);
                    expectedPadding.setLastBitsFromLong(ByteOrder.toLittleEndianLong(446));
                    test.assertEqual(expectedPadding, md5.getPaddingFromBitCount(446));
                });

                runner.test("with 447", (Test test) ->
                {
                    final BitArray expectedPadding = new BitArray(65);
                    expectedPadding.setBit(0, 1);
                    expectedPadding.setLastBitsFromLong(ByteOrder.toLittleEndianLong(447));
                    test.assertEqual(expectedPadding, md5.getPaddingFromBitCount(447));
                });

                runner.test("with 448", (Test test) ->
                {
                    final BitArray expectedPadding = new BitArray(576);
                    expectedPadding.setBit(0, 1);
                    expectedPadding.setLastBitsFromLong(ByteOrder.toLittleEndianLong(448));
                    test.assertEqual(expectedPadding, md5.getPaddingFromBitCount(448));
                });

                runner.test("with 449", (Test test) ->
                {
                    final BitArray expectedPadding = new BitArray(575);
                    expectedPadding.setBit(0, 1);
                    expectedPadding.setLastBitsFromLong(ByteOrder.toLittleEndianLong(449));
                    test.assertEqual(expectedPadding, md5.getPaddingFromBitCount(449));
                });

                runner.test("with 450", (Test test) ->
                {
                    final BitArray expectedPadding = new BitArray(574);
                    expectedPadding.setBit(0, 1);
                    expectedPadding.setLastBitsFromLong(ByteOrder.toLittleEndianLong(450));
                    test.assertEqual(expectedPadding, md5.getPaddingFromBitCount(450));
                });

                runner.test("with 451", (Test test) ->
                {
                    final BitArray expectedPadding = new BitArray(573);
                    expectedPadding.setBit(0, 1);
                    expectedPadding.setLastBitsFromLong(ByteOrder.toLittleEndianLong(451));
                    test.assertEqual(expectedPadding, md5.getPaddingFromBitCount(451));
                });

                runner.test("with 510", (Test test) ->
                {
                    final BitArray expectedPadding = new BitArray(514);
                    expectedPadding.setBit(0, 1);
                    expectedPadding.setLastBitsFromLong(ByteOrder.toLittleEndianLong(510));
                    test.assertEqual(expectedPadding, md5.getPaddingFromBitCount(510));
                });

                runner.test("with 511", (Test test) ->
                {
                    final BitArray expectedPadding = new BitArray(513);
                    expectedPadding.setBit(0, 1);
                    expectedPadding.setLastBitsFromLong(ByteOrder.toLittleEndianLong(511));
                    test.assertEqual(expectedPadding, md5.getPaddingFromBitCount(511));
                });

                runner.test("with 512", (Test test) ->
                {
                    final BitArray expectedPadding = new BitArray(512);
                    expectedPadding.setBit(0, 1);
                    expectedPadding.setLastBitsFromLong(ByteOrder.toLittleEndianLong(512));
                    test.assertEqual(expectedPadding, md5.getPaddingFromBitCount(512));
                });

                runner.test("with 513", (Test test) ->
                {
                    final BitArray expectedPadding = new BitArray(511);
                    expectedPadding.setBit(0, 1);
                    expectedPadding.setLastBitsFromLong(ByteOrder.toLittleEndianLong(513));
                    test.assertEqual(expectedPadding, md5.getPaddingFromBitCount(513));
                });
            });

            final Action2<String,Function1<String,BitArray>> hashTestGroup = (String hashFunctionSignature, Function1<String,BitArray> hashCall) ->
            {
                runner.testGroup(hashFunctionSignature, runner.skip(), () ->
                {
                    final Action2<String,String> hashTest = (String message, String expectedDigest) ->
                    {
                        runner.test("with " + Strings.escapeAndQuote(message), (Test test) ->
                        {
                            final BitArray actualDigestBits = hashCall.run(message);
                            test.assertEqual(expectedDigest, actualDigestBits.toHexString());
                        });
                    };

                    hashTest.run("", "d41d8cd98f00b204e9800998ecf8427e");
                    hashTest.run("a", "0cc175b9c0f1b6a831c399e269772661");
                    hashTest.run("abc", "900150983cd24fb0d6963f7d28e17f72");
                    hashTest.run("message digest", "f96b697d7cb7938d525a2f31aaf161d0");
                    hashTest.run("abcdefghijklmnopqrstuvwxyz", "c3fcd3d76192e4007dfb496cca67e13b");
                    hashTest.run("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789", "d174ab98d277d9f5a5611c2c9f419d9f");
                    hashTest.run("12345678901234567890123456789012345678901234567890123456789012345678901234567890", "57edf4a22be3c955ac49da2e2107b67a");
                });
            };

            hashTestGroup.run("hash(BitArray)", (String message) ->
            {
                final byte[] bytes = Strings.isNullOrEmpty(message) ? new byte[0] : CharacterEncoding.US_ASCII.encode(message).getValue();
                final BitArray bits = BitArray.fromByteArray(bytes);
                return md5.hash(bits);
            });

            hashTestGroup.run("hash(byte[])", (String message) ->
            {
                final byte[] bytes = Strings.isNullOrEmpty(message) ? new byte[0] : CharacterEncoding.US_ASCII.encode(message).getValue();
                return md5.hash(bytes);
            });

            hashTestGroup.run("hash(Iterator<Byte>)", (String message) ->
            {
                final Iterator<Byte> bytes = Iterator.createFromBytes(Strings.isNullOrEmpty(message) ? new byte[0] : CharacterEncoding.US_ASCII.encode(message).await());
                return md5.hash(bytes);
            });

            hashTestGroup.run("hash(Iterable<Byte>)", (String message) ->
            {
                final Iterable<Byte> bytes = Array.createByte(Strings.isNullOrEmpty(message) ? new byte[0] : CharacterEncoding.US_ASCII.encode(message).await());
                return md5.hash(bytes);
            });
        });
    }
}
