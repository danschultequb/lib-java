package qub;

public class InMemoryCharacterWriteStreamTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(InMemoryCharacterWriteStream.class, () ->
        {
            runner.test("constructor(CharacterEncoding)", test ->
            {
                final InMemoryCharacterWriteStream writeStream = new InMemoryCharacterWriteStream(CharacterEncoding.US_ASCII);
                test.assertEqual(CharacterEncoding.US_ASCII, writeStream.getCharacterEncoding());
            });
        });
    }
}
