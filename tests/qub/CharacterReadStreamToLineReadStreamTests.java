package qub;

public class CharacterReadStreamToLineReadStreamTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(CharacterReadStreamToLineReadStream.class, () ->
        {
            AsyncDisposableTests.test(runner, (AsyncRunner asyncRunner) -> new CharacterReadStreamToLineReadStream(new InMemoryCharacterStream(asyncRunner).endOfStream()));
            LineReadStreamTests.test(runner, (String text, Boolean includeNewLines) -> new CharacterReadStreamToLineReadStream(new InMemoryCharacterStream(text).endOfStream(), includeNewLines));
        });
    }
}
