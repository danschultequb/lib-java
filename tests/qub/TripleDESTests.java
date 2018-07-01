package qub;

public class TripleDESTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(TripleDES.class, () ->
        {
            runner.testGroup("encrypt(BitArray,BitArray) with 112-bit initializationVector", () ->
            {
                final Action3<String,String,String> encryptTest = (String message, String initializationVector, String expectedCiphertext) ->
                {
                    runner.test("with message " + Strings.quote(message) + " and initialization vector " + Strings.quote(initializationVector), (Test test) ->
                    {
                        final TripleDES tripleDES = new TripleDES();

                        final BitArray plaintextBits = BitArray.fromHexString(message);
                        final BitArray initializationVectorBits = BitArray.fromHexString(initializationVector);

                        final BitArray ciphertextBits = tripleDES.encrypt(initializationVectorBits, plaintextBits);
                        test.assertNotNull(ciphertextBits);
                        test.assertEqual(expectedCiphertext, ciphertextBits.toHexString());
                    });
                };

                encryptTest.run("8787878787878787", "0E329232EA6D0D73133457799BBCDFF1", "25392EDEC93C1CEE");
                encryptTest.run("0123456789ABCDEF", "0E329232EA6D0D73133457799BBCDFF1", "8A1FEBF13B930592");
                encryptTest.run("596F7572206C6970", "0E329232EA6D0D73133457799BBCDFF1", "7DD6F8EFF5CA974C");
                encryptTest.run("732061726520736D", "0E329232EA6D0D73133457799BBCDFF1", "DACD338E796C37BA");
                encryptTest.run("6F6F746865722074", "0E329232EA6D0D73133457799BBCDFF1", "FB291B4A11CE2A70");
                encryptTest.run("68616E2076617365", "0E329232EA6D0D73133457799BBCDFF1", "D5F544CD4BA6068C");
                encryptTest.run("6C696E650D0A0000", "0E329232EA6D0D73133457799BBCDFF1", "793ECD59F846615E");
            });

            runner.testGroup("decrypt(BitArray,BitArray) with 112-bit initializationVector", () ->
            {
                final Action3<String,String,String> decryptTest = (String ciphertext, String initializationVector, String expectedPlaintext) ->
                {
                    runner.test("with ciphertext " + Strings.quote(ciphertext) + " and initialization vector " + Strings.quote(initializationVector), (Test test) ->
                    {
                        final TripleDES tripleDES = new TripleDES();

                        final BitArray ciphertextBits = BitArray.fromHexString(ciphertext);
                        final BitArray initializationVectorBits = BitArray.fromHexString(initializationVector);

                        final BitArray plaintextBits = tripleDES.decrypt(initializationVectorBits, ciphertextBits);
                        test.assertNotNull(plaintextBits);
                        test.assertEqual(expectedPlaintext, plaintextBits.toHexString());
                    });
                };

                decryptTest.run("25392EDEC93C1CEE", "0E329232EA6D0D73133457799BBCDFF1", "8787878787878787");
                decryptTest.run("8A1FEBF13B930592", "0E329232EA6D0D73133457799BBCDFF1", "0123456789ABCDEF");
                decryptTest.run("7DD6F8EFF5CA974C", "0E329232EA6D0D73133457799BBCDFF1", "596F7572206C6970");
                decryptTest.run("DACD338E796C37BA", "0E329232EA6D0D73133457799BBCDFF1", "732061726520736D");
                decryptTest.run("FB291B4A11CE2A70", "0E329232EA6D0D73133457799BBCDFF1", "6F6F746865722074");
                decryptTest.run("D5F544CD4BA6068C", "0E329232EA6D0D73133457799BBCDFF1", "68616E2076617365");
                decryptTest.run("793ECD59F846615E", "0E329232EA6D0D73133457799BBCDFF1", "6C696E650D0A0000");
            });
        });
    }
}
