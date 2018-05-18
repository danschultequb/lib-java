package qub;

public class CharacterWriteStreamToLineWriteStreamTests
{
    public static void test(final TestRunner runner)
    {
        runner.testGroup("CharacterWriteStreamToLineWriteStream", () ->
        {
            runner.test("write(char)", (Test test) ->
            {
                final InMemoryCharacterWriteStream characterWriteStream = new InMemoryCharacterWriteStream();
                final CharacterWriteStreamToLineWriteStream lineWriteStream = getLineWriteStream(characterWriteStream);
                test.assertSuccess(true, lineWriteStream.write('z'));
                test.assertEqual(new byte[] { 122 }, characterWriteStream.getBytes());
                test.assertEqual("z", characterWriteStream.getText());
            });

            runner.test("write(String)", (Test test) ->
            {
                final InMemoryByteStream byteWriteStream = new InMemoryByteStream();
                final InMemoryCharacterWriteStream characterWriteStream = new InMemoryCharacterWriteStream(byteWriteStream);
                final CharacterWriteStreamToLineWriteStream lineWriteStream = getLineWriteStream(characterWriteStream);
                test.assertSuccess(true, lineWriteStream.write("tuv"));
                test.assertEqual(new byte[] { 116, 117, 118 }, byteWriteStream.getBytes());
                test.assertEqual("tuv", characterWriteStream.getText());
            });

            runner.test("write(String,Object...)", (Test test) ->
            {
                final InMemoryByteStream byteWriteStream = new InMemoryByteStream();
                final InMemoryCharacterWriteStream characterWriteStream = new InMemoryCharacterWriteStream(byteWriteStream);
                final CharacterWriteStreamToLineWriteStream lineWriteStream = getLineWriteStream(characterWriteStream);
                test.assertSuccess(true, lineWriteStream.write("%s == %d", "1", 2));
                test.assertEqual(new byte[] { 49, 32, 61, 61, 32, 50 }, byteWriteStream.getBytes());
                test.assertEqual("1 == 2", characterWriteStream.getText());
            });

            runner.test("writeLine()", (Test test) ->
            {
                final InMemoryByteStream byteWriteStream = new InMemoryByteStream();
                final InMemoryCharacterWriteStream characterWriteStream = new InMemoryCharacterWriteStream(byteWriteStream);
                final CharacterWriteStreamToLineWriteStream lineWriteStream = getLineWriteStream(characterWriteStream);
                test.assertSuccess(true, lineWriteStream.writeLine());
                test.assertEqual(new byte[] { 10 }, byteWriteStream.getBytes());
                test.assertEqual("\n", characterWriteStream.getText());
            });

            runner.test("writeLine(String,Object...)", (Test test) ->
            {
                final InMemoryByteStream byteWriteStream = new InMemoryByteStream();
                final InMemoryCharacterWriteStream characterWriteStream = new InMemoryCharacterWriteStream(byteWriteStream);
                final CharacterWriteStreamToLineWriteStream lineWriteStream = getLineWriteStream(characterWriteStream);
                test.assertSuccess(true, lineWriteStream.writeLine("hello"));
                test.assertEqual(new byte[] { 104, 101, 108, 108, 111, 10 }, byteWriteStream.getBytes());
                test.assertEqual("hello\n", characterWriteStream.getText());
            });
        });
    }

    private static CharacterWriteStreamToLineWriteStream getLineWriteStream(CharacterWriteStream characterWriteStream)
    {
        return new CharacterWriteStreamToLineWriteStream(characterWriteStream);
    }
}
