package qub;

public class CharacterReadStreamToLineReadStreamTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(CharacterReadStreamToLineReadStream.class, () ->
        {
            AsyncDisposableTests.test(runner, (AsyncRunner asyncRunner) -> new CharacterReadStreamToLineReadStream(new InMemoryCharacterReadStream(asyncRunner)));
            LineReadStreamTests.test(runner, (String text, Boolean includeNewLines) -> new CharacterReadStreamToLineReadStream(new InMemoryCharacterReadStream(text), includeNewLines));
        });
    }
}
