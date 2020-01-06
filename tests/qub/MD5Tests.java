package qub;

public interface MD5Tests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(MD5.class, () ->
        {
            final Action2<String,Function1<String,Result<BitArray>>> hashTestGroup = (String hashFunctionSignature, Function1<String,Result<BitArray>> hashCall) ->
            {
                runner.testGroup(hashFunctionSignature, () ->
                {
                    final Action2<String,String> hashTest = (String message, String expectedDigest) ->
                    {
                        runner.test("with " + Strings.escapeAndQuote(message), (Test test) ->
                        {
                            final Result<BitArray> actualDigestBits = hashCall.run(message);
                            test.assertEqual(expectedDigest, actualDigestBits.await().toHexString().toLowerCase());
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
                final byte[] bytes = Strings.isNullOrEmpty(message) ? new byte[0] : CharacterEncoding.US_ASCII.encode(message).await();
                final BitArray bits = BitArray.createFromBytes(bytes);
                return Result.success(MD5.hash(bits));
            });

            hashTestGroup.run("hash(byte[])", (String message) ->
            {
                final byte[] bytes = Strings.isNullOrEmpty(message) ? new byte[0] : CharacterEncoding.US_ASCII.encode(message).await();
                return Result.success(MD5.hash(bytes));
            });

            hashTestGroup.run("hash(Iterator<Byte>)", (String message) ->
            {
                final Iterator<Byte> bytes = Strings.isNullOrEmpty(message) ? Iterator.empty() : Iterator.createFromBytes(CharacterEncoding.US_ASCII.encode(message).await());
                return MD5.hash(bytes);
            });

            hashTestGroup.run("hash(Iterable<Byte>)", (String message) ->
            {
                final Iterable<Byte> bytes = Strings.isNullOrEmpty(message) ? Iterable.create() : ByteArray.create(CharacterEncoding.US_ASCII.encode(message).await());
                return MD5.hash(bytes);
            });
        });
    }
}
