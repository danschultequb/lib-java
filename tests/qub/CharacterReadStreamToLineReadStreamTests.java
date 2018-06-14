package qub;

public class CharacterReadStreamToLineReadStreamTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(BasicLineReadStream.class, () ->
        {
            AsyncDisposableTests.test(runner, (AsyncRunner asyncRunner) -> new BasicLineReadStream(new InMemoryCharacterStream(asyncRunner).endOfStream()));
            LineReadStreamTests.test(runner, (String text, Boolean includeNewLines) -> new BasicLineReadStream(new InMemoryCharacterStream(text).endOfStream(), includeNewLines));
        });
    }
}
