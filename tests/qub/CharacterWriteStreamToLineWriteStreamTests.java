package qub;

public class CharacterWriteStreamToLineWriteStreamTests
{
    public static void test(final TestRunner runner)
    {
        runner.testGroup(CharacterWriteStreamToLineWriteStream.class, () ->
        {
            runner.test("writeByte(char)", (Test test) ->
            {
                final InMemoryCharacterStream characterWriteStream = new InMemoryCharacterStream();
                final CharacterWriteStreamToLineWriteStream lineWriteStream = getLineWriteStream(characterWriteStream);
                test.assertSuccess(true, lineWriteStream.write('z'));
                test.assertEqual(new byte[] { 122 }, characterWriteStream.getBytes());
                test.assertSuccess("z", characterWriteStream.getText());
            });

            runner.test("writeByte(String)", (Test test) ->
            {
                final InMemoryCharacterStream characterWriteStream = new InMemoryCharacterStream();
                final CharacterWriteStreamToLineWriteStream lineWriteStream = getLineWriteStream(characterWriteStream);
                test.assertSuccess(true, lineWriteStream.write("tuv"));
                test.assertEqual(new byte[] { 116, 117, 118 }, characterWriteStream.getBytes());
                test.assertSuccess("tuv", characterWriteStream.getText());
            });

            runner.test("writeByte(String,Object...)", (Test test) ->
            {
                final InMemoryCharacterStream characterWriteStream = new InMemoryCharacterStream();
                final CharacterWriteStreamToLineWriteStream lineWriteStream = getLineWriteStream(characterWriteStream);
                test.assertSuccess(true, lineWriteStream.write("%s == %d", "1", 2));
                test.assertEqual(new byte[] { 49, 32, 61, 61, 32, 50 }, characterWriteStream.getBytes());
                test.assertSuccess("1 == 2", characterWriteStream.getText());
            });

            runner.test("writeLine()", (Test test) ->
            {
                final InMemoryCharacterStream characterWriteStream = new InMemoryCharacterStream();
                final CharacterWriteStreamToLineWriteStream lineWriteStream = getLineWriteStream(characterWriteStream);
                test.assertSuccess(true, lineWriteStream.writeLine());
                test.assertEqual(new byte[] { 10 }, characterWriteStream.getBytes());
                test.assertSuccess("\n", characterWriteStream.getText());
            });

            runner.test("writeLine(String,Object...)", (Test test) ->
            {
                final InMemoryCharacterStream characterWriteStream = new InMemoryCharacterStream();
                final CharacterWriteStreamToLineWriteStream lineWriteStream = getLineWriteStream(characterWriteStream);
                test.assertSuccess(true, lineWriteStream.writeLine("hello"));
                test.assertEqual(new byte[] { 104, 101, 108, 108, 111, 10 }, characterWriteStream.getBytes());
                test.assertSuccess("hello\n", characterWriteStream.getText());
            });
        });
    }

    private static CharacterWriteStreamToLineWriteStream getLineWriteStream(CharacterWriteStream characterWriteStream)
    {
        return new CharacterWriteStreamToLineWriteStream(characterWriteStream);
    }
}
