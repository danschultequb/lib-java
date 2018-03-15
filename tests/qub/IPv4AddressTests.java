package qub;

public class IPv4AddressTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(IPv4Address.class, () ->
        {
            runner.testGroup("constructor(byte,byte,byte,byte)", () ->
            {
                runner.test("with 1, 2, 3, 4", (Test test) ->
                {
                    final IPv4Address address = new IPv4Address((byte)1, (byte)2, (byte)3, (byte)4);
                    test.assertEqual("1.2.3.4", address.toString());
                    test.assertEqual(16909060, address.hashCode());
                });

                runner.test("with 1, 2, 3, 5", (Test test) ->
                {
                    final IPv4Address address = new IPv4Address((byte)1, (byte)2, (byte)3, (byte)5);
                    test.assertEqual("1.2.3.5", address.toString());
                    test.assertEqual(16909061, address.hashCode());
                });

                runner.test("with 255, 254, 253, 252", (Test test) ->
                {
                    final IPv4Address address = new IPv4Address((byte)255, (byte)254, (byte)253, (byte)252);
                    test.assertEqual("255.254.253.252", address.toString());
                    test.assertEqual(-66052, address.hashCode());
                });
            });

            runner.testGroup("equals(Object)", () ->
            {
                final Action3<IPv4Address,Object,Boolean> equalsTest = (IPv4Address address, Object rhs, Boolean expected) ->
                {
                    runner.test("with " + address + " and " + rhs, (Test test) ->
                    {
                        test.assertEqual(expected, address.equals(rhs));
                    });
                };

                equalsTest.run(IPv4Address.parse("1.2.3.4"), null, false);
                equalsTest.run(IPv4Address.parse("1.2.3.4"), "", false);
                equalsTest.run(IPv4Address.parse("1.2.3.4"), "1.2.3.4", false);
                equalsTest.run(IPv4Address.parse("1.2.3.4"), 55, false);
                equalsTest.run(IPv4Address.parse("1.2.3.4"), IPv4Address.parse("1.2.3.5"), false);
                equalsTest.run(IPv4Address.parse("1.2.3.4"), IPv4Address.parse("1.2.3.4"), true);
            });

            runner.testGroup("equals(IPv4Address)", () ->
            {
                final Action3<IPv4Address,IPv4Address,Boolean> equalsTest = (IPv4Address address, IPv4Address rhs, Boolean expected) ->
                {
                    runner.test("with " + address + " and " + rhs, (Test test) ->
                    {
                        test.assertEqual(expected, address.equals(rhs));
                    });
                };

                equalsTest.run(IPv4Address.parse("1.2.3.4"), null, false);
                equalsTest.run(IPv4Address.parse("1.2.3.4"), IPv4Address.parse("1.2.3.5"), false);
                equalsTest.run(IPv4Address.parse("1.2.3.4"), IPv4Address.parse("1.2.3.4"), true);
            });

            runner.testGroup("toBytes()", () ->
            {
                final Action2<IPv4Address,byte[]> toBytesTest = (IPv4Address address, byte[] expected) ->
                {
                    runner.test("with " + address, (Test test) ->
                    {
                        test.assertEqual(Array.fromValues(expected), Array.fromValues(address.toBytes()));
                    });
                };

                toBytesTest.run(IPv4Address.parse("0.0.0.0"), new byte[] { 0, 0, 0, 0 });
                toBytesTest.run(IPv4Address.parse("1.2.3.4"), new byte[] { 1, 2, 3, 4 });
            });

            runner.testGroup("parse(String)", () ->
            {
                final Action2<String,IPv4Address> parseTest = (String text, IPv4Address expected) ->
                {
                    runner.test("with " + text, (Test test) ->
                    {
                        final IPv4Address address = IPv4Address.parse(text);
                        test.assertEqual(expected, address);
                    });
                };

                parseTest.run(null, null);
                parseTest.run("", null);
                parseTest.run("abc", null);
                parseTest.run("1", null);
                parseTest.run("1.", null);
                parseTest.run("1.2", null);
                parseTest.run("1.2.", null);
                parseTest.run("1.2.3", null);
                parseTest.run("1.2.3.", null);
                parseTest.run("1.2.3.4", new IPv4Address((byte)1, (byte)2, (byte)3, (byte)4));
                parseTest.run("1.2.3.4.", null);
                parseTest.run("1.2.3.4.5", null);
                parseTest.run("0.0.0.0", new IPv4Address((byte)0, (byte)0, (byte)0, (byte)0));
                parseTest.run("255.255.255.255", new IPv4Address((byte)255, (byte)255, (byte)255, (byte)255));
                parseTest.run("-1.2.3.4", null);
                parseTest.run("256.2.3.4", null);
                parseTest.run("a.2.3.4", null);
                parseTest.run("1a2b3c4d", null);
                parseTest.run("10325987123095816320958712098374109287349871234.2.3.4", null);
            });
        });
    }
}
