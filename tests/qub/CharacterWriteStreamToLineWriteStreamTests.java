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
                lineWriteStream.write('z').awaitError();
                test.assertEqual(new byte[] { 122 }, characterWriteStream.getBytes());
                test.assertEqual("z", characterWriteStream.getText().awaitError());
            });

            runner.test("writeByte(String)", (Test test) ->
            {
                final InMemoryCharacterStream characterWriteStream = new InMemoryCharacterStream();
                final CharacterWriteStreamToLineWriteStream lineWriteStream = getLineWriteStream(characterWriteStream);
                lineWriteStream.write("tuv").awaitError();
                test.assertEqual(new byte[] { 116, 117, 118 }, characterWriteStream.getBytes());
                test.assertEqual("tuv", characterWriteStream.getText().awaitError());
            });

            runner.test("writeByte(String,Object...)", (Test test) ->
            {
                final InMemoryCharacterStream characterWriteStream = new InMemoryCharacterStream();
                final CharacterWriteStreamToLineWriteStream lineWriteStream = getLineWriteStream(characterWriteStream);
                lineWriteStream.write("%s == %d", "1", 2).awaitError();
                test.assertEqual(new byte[] { 49, 32, 61, 61, 32, 50 }, characterWriteStream.getBytes());
                test.assertEqual("1 == 2", characterWriteStream.getText().awaitError());
            });

            runner.test("writeLine()", (Test test) ->
            {
                final InMemoryCharacterStream characterWriteStream = new InMemoryCharacterStream();
                final CharacterWriteStreamToLineWriteStream lineWriteStream = getLineWriteStream(characterWriteStream);
                lineWriteStream.writeLine().awaitError();
                test.assertEqual(new byte[] { 10 }, characterWriteStream.getBytes());
                test.assertEqual("\n", characterWriteStream.getText().awaitError());
            });

            runner.test("writeLine(String,Object...)", (Test test) ->
            {
                final InMemoryCharacterStream characterWriteStream = new InMemoryCharacterStream();
                final CharacterWriteStreamToLineWriteStream lineWriteStream = getLineWriteStream(characterWriteStream);
                lineWriteStream.writeLine("hello").awaitError();
                test.assertEqual(new byte[] { 104, 101, 108, 108, 111, 10 }, characterWriteStream.getBytes());
                test.assertEqual("hello\n", characterWriteStream.getText().awaitError());
            });
        });
    }

    private static CharacterWriteStreamToLineWriteStream getLineWriteStream(CharacterWriteStream characterWriteStream)
    {
        return new CharacterWriteStreamToLineWriteStream(characterWriteStream);
    }
}
