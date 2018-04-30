package qub;

public class InMemoryCharacterReadStreamTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(InMemoryCharacterReadStream.class, () ->
        {
            AsyncDisposableTests.test(runner, InMemoryCharacterReadStream::new);

            runner.test("readCharacters(int)", (Test test) ->
            {
                final InMemoryCharacterReadStream characterReadStream = new InMemoryCharacterReadStream("abc");
                test.assertSuccess(new char[] { 'a', 'b', 'c' }, characterReadStream.readCharacters(20));
                test.assertSuccess(null, characterReadStream.readCharacters(1));
            });

            runner.test("readCharacters(char[])", (Test test) ->
            {
                final InMemoryCharacterReadStream characterReadStream = new InMemoryCharacterReadStream("abc");
                final char[] characters = new char[2];

                test.assertSuccess(2, characterReadStream.readCharacters(characters));
                test.assertEqual(new char[] { 'a', 'b' }, characters);

                test.assertSuccess(1, characterReadStream.readCharacters(characters));
                test.assertEqual(new char[] { 'c', 'b' }, characters);

                test.assertSuccess(null, characterReadStream.readCharacters(characters));
                test.assertEqual(new char[] { 'c', 'b' }, characters);
            });

            runner.test("asLineReadStream()", (Test test) ->
            {
                final InMemoryCharacterReadStream characterReadStream = new InMemoryCharacterReadStream();
                final LineReadStream lineReadStream = characterReadStream.asLineReadStream();
                test.assertNotNull(lineReadStream);
            });
        });
    }
}
