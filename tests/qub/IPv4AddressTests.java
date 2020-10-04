package qub;

public interface IPv4AddressTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(IPv4Address.class, () ->
        {
            runner.testGroup("constructor(byte,byte,byte,byte)", () ->
            {
                runner.test("with 1, 2, 3, 4", (Test test) ->
                {
                    final IPv4Address address = IPv4Address.create(1, 2, 3, 4);
                    test.assertEqual("1.2.3.4", address.toString());
                    test.assertEqual(16909060, address.hashCode());
                });

                runner.test("with 1, 2, 3, 5", (Test test) ->
                {
                    final IPv4Address address = IPv4Address.create(1, 2, 3, 5);
                    test.assertEqual("1.2.3.5", address.toString());
                    test.assertEqual(16909061, address.hashCode());
                });

                runner.test("with 255, 254, 253, 252", (Test test) ->
                {
                    final IPv4Address address = IPv4Address.create(255, 254, 253, 252);
                    test.assertEqual("255.254.253.252", address.toString());
                    test.assertEqual(-66052, address.hashCode());
                });
            });

            runner.testGroup("equals(Object)", () ->
            {
                final Action3<IPv4Address,Object,Boolean> equalsTest = (IPv4Address address, Object rhs, Boolean expected) ->
                {
                    runner.test("with " + English.andList(address, rhs), (Test test) ->
                    {
                        test.assertEqual(expected, address.equals(rhs));
                    });
                };

                equalsTest.run(IPv4Address.create(1, 2, 3, 4), null, false);
                equalsTest.run(IPv4Address.create(1, 2, 3, 4), "", false);
                equalsTest.run(IPv4Address.create(1, 2, 3, 4), "1.2.3.4", false);
                equalsTest.run(IPv4Address.create(1, 2, 3, 4), 55, false);
                equalsTest.run(IPv4Address.create(1, 2, 3, 4), IPv4Address.parse("1.2.3.5"), false);
                equalsTest.run(IPv4Address.create(1, 2, 3, 4), IPv4Address.create(1, 2, 3, 4), true);
            });

            runner.testGroup("equals(IPv4Address)", () ->
            {
                final Action3<IPv4Address,IPv4Address,Boolean> equalsTest = (IPv4Address address, IPv4Address rhs, Boolean expected) ->
                {
                    runner.test("with " + English.andList(address, rhs), (Test test) ->
                    {
                        test.assertEqual(expected, address.equals(rhs));
                    });
                };

                equalsTest.run(IPv4Address.create(1, 2, 3, 4), null, false);
                equalsTest.run(IPv4Address.create(1, 2, 3, 4), IPv4Address.create(1, 2, 3, 5), false);
                equalsTest.run(IPv4Address.create(1, 2, 3, 4), IPv4Address.create(1, 2, 3, 4), true);
            });

            runner.testGroup("toBytes()", () ->
            {
                final Action2<IPv4Address,byte[]> toBytesTest = (IPv4Address address, byte[] expected) ->
                {
                    runner.test("with " + address, (Test test) ->
                    {
                        test.assertEqual(Array.create(expected), Array.create(address.toBytes()));
                    });
                };

                toBytesTest.run(IPv4Address.create(0, 0, 0, 0), new byte[] { 0, 0, 0, 0 });
                toBytesTest.run(IPv4Address.create(1, 2, 3, 4), new byte[] { 1, 2, 3, 4 });
            });

            runner.testGroup("parse(String)", () ->
            {
                final Action2<String,Throwable> parseErrorTest = (String text, Throwable expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(text), (Test test) ->
                    {
                        test.assertThrows(() -> IPv4Address.parse(text).await(), expected);
                    });
                };

                parseErrorTest.run(null, new PreConditionFailure("text cannot be null."));
                parseErrorTest.run("", new ParseException("Missing 1 value."));
                parseErrorTest.run("abc", new ParseException("Expected digit (0 - 9), but found \"a\" instead."));
                parseErrorTest.run("1", new ParseException("Missing 1 period ('.')."));
                parseErrorTest.run("1.", new ParseException("Missing 2 value."));
                parseErrorTest.run("1.2", new ParseException("Missing 2 period ('.')."));
                parseErrorTest.run("1.2.", new ParseException("Missing 3 value."));
                parseErrorTest.run("1.2.3", new ParseException("Missing 3 period ('.')."));
                parseErrorTest.run("1.2.3.", new ParseException("Missing 4 value."));
                parseErrorTest.run("1.2.3.4.", new ParseException("Expected an IPv4 address to end after the fourth value, but found \".\" instead."));
                parseErrorTest.run("1.2.3.4.5", new ParseException("Expected an IPv4 address to end after the fourth value, but found \".5\" instead."));
                parseErrorTest.run("-1.2.3.4", new ParseException("Expected digit (0 - 9), but found \"-\" instead."));
                parseErrorTest.run("256.2.3.4", new ParseException("Expected 1 value to be between 0 and 255, but found 256 instead."));
                parseErrorTest.run("a.2.3.4", new ParseException("Expected digit (0 - 9), but found \"a\" instead."));
                parseErrorTest.run("1a2b3c4d", new ParseException("Expected period ('.') but found \"a\" instead."));
                parseErrorTest.run("10325987123095816320958712098374109287349871234.2.3.4", new ParseException("Expected 1 value to be between 0 and 255, but found 10325987123095816320958712098374109287349871234 instead."));

                final Action2<String,IPv4Address> parseTest = (String text, IPv4Address expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(text), (Test test) ->
                    {
                        test.assertEqual(expected, IPv4Address.parse(text).await());
                    });
                };

                parseTest.run("1.2.3.4", IPv4Address.create(1, 2, 3, 4));
                parseTest.run("0.0.0.0", IPv4Address.create(0, 0, 0, 0));
                parseTest.run("255.255.255.255", IPv4Address.create(255, 255, 255, 255));
            });
        });
    }
}
