package qub;

public class CharacterReadStreamToLineReadStreamTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(CharacterReadStreamToLineReadStream.class, () ->
        {
            LineReadStreamTests.test(runner, (String text, Boolean includeNewLines) -> new CharacterReadStreamToLineReadStream(new InMemoryCharacterReadStream(text), includeNewLines));
        });
    }
}
