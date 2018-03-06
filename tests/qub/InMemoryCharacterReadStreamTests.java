package qub;

public class InMemoryCharacterReadStreamTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(InMemoryCharacterReadStream.class, () ->
        {
            runner.test("readCharacters(int)", (Test test) ->
            {
                final InMemoryCharacterReadStream characterReadStream = new InMemoryCharacterReadStream("abc");
                test.assertEqual(new char[] { 'a', 'b', 'c' }, characterReadStream.readCharacters(20));
                test.assertNull(characterReadStream.readCharacters(1));
            });

            runner.test("readCharacters(char[])", (Test test) ->
            {
                final InMemoryCharacterReadStream characterReadStream = new InMemoryCharacterReadStream("abc");
                final char[] characters = new char[2];

                test.assertEqual(2, characterReadStream.readCharacters(characters));
                test.assertEqual(new char[] { 'a', 'b' }, characters);

                test.assertEqual(1, characterReadStream.readCharacters(characters));
                test.assertEqual(new char[] { 'c', 'b' }, characters);

                test.assertEqual(-1, characterReadStream.readCharacters(characters));
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
