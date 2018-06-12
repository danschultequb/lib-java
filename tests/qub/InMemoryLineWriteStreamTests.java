package qub;

public class InMemoryLineWriteStreamTests
{
    public static void test(final TestRunner runner)
    {
        runner.testGroup("InMemoryLineWriteStream", () ->
        {
            runner.test("constructor(CharacterEncoding)", (Test test) ->
            {
                final InMemoryLineWriteStream writeStream = new InMemoryLineWriteStream(CharacterEncoding.US_ASCII);
                test.assertEqual(CharacterEncoding.US_ASCII, writeStream.getCharacterEncoding());
            });

            runner.test("constructor(CharacterEncoding,String)", (Test test) ->
            {
                final InMemoryLineWriteStream writeStream = new InMemoryLineWriteStream(CharacterEncoding.US_ASCII, "\r\n");
                test.assertEqual(CharacterEncoding.US_ASCII, writeStream.getCharacterEncoding());
                test.assertEqual("\r\n", writeStream.getLineSeparator());
            });

            runner.test("getText()", (Test test) ->
            {
                final InMemoryLineWriteStream writeStream = new InMemoryLineWriteStream();
                test.assertSuccess("", writeStream.getText());

                writeStream.writeLine("hello");
                test.assertSuccess("hello\n", writeStream.getText());
            });
        });
    }
}
