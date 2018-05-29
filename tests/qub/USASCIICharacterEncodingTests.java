package qub;

public class USASCIICharacterEncodingTests
{
    public static void test(TestRunner runner)
    {
        final USASCIICharacterEncoding encoding = new USASCIICharacterEncoding();

        runner.testGroup(USASCIICharacterEncoding.class, () ->
        {
            runner.testGroup("encode(char)", () ->
            {
                runner.test("with 'a'", (Test test) ->
                {
                    test.assertEqual(new byte[] { 97 }, encoding.encode('a'));
                });

                runner.test("with 'b'", (Test test) ->
                {
                    test.assertEqual(new byte[] { 98 }, encoding.encode('b'));
                });

                runner.test("with 'y'", (Test test) ->
                {
                    test.assertEqual(new byte[] { 121 }, encoding.encode('y'));
                });

                runner.test("with 'z'", (Test test) ->
                {
                    test.assertEqual(new byte[] { 122 }, encoding.encode('z'));
                });

                runner.test("with '\\n'", (Test test) ->
                {
                    test.assertEqual(new byte[] { 10 }, encoding.encode('\n'));
                });

                runner.test("with '~'", (Test test) ->
                {
                    test.assertEqual(new byte[] { 126 }, encoding.encode('~'));
                });
            });

            runner.testGroup("encode(String)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertError(new IllegalArgumentException("text cannot be null."), encoding.encode((String)null));
                });

                runner.test("with \"\"", (Test test) ->
                {
                    test.assertError(new IllegalArgumentException("text cannot be empty."), encoding.encode(""));
                });

                runner.test("with \"a\"", (Test test) ->
                {
                    test.assertSuccess(new byte[] { 97 }, encoding.encode("a"));
                });

                runner.test("with \"b\"", (Test test) ->
                {
                    test.assertSuccess(new byte[] { 98 }, encoding.encode("b"));
                });

                runner.test("with \"ab\"", (Test test) ->
                {
                    test.assertSuccess(new byte[] { 97, 98 }, encoding.encode("ab"));
                });

                runner.test("with \"y\"", (Test test) ->
                {
                    test.assertSuccess(new byte[] { 121 }, encoding.encode("y"));
                });

                runner.test("with \"z\"", (Test test) ->
                {
                    test.assertSuccess(new byte[] { 122 }, encoding.encode("z"));
                });

                runner.test("with \"\\n\"", (Test test) ->
                {
                    test.assertSuccess(new byte[] { 10 }, encoding.encode("\n"));
                });

                runner.test("with \"~\"", (Test test) ->
                {
                    test.assertSuccess(new byte[] { 126 }, encoding.encode("~"));
                });
            });

            runner.testGroup("decode(byte[])", () ->
            {
                runner.test("with [97]", (Test test) ->
                {
                    test.assertSuccess(new char[] { 'a' }, encoding.decode(new byte[] { 97 }));
                });
            });

            runner.testGroup("decodeAsString(byte[])", () ->
            {
                runner.test("with [97]", (Test test) ->
                {
                    test.assertSuccess("a", encoding.decodeAsString(new byte[] { 97 }));
                });
            });
        });
    }
}
