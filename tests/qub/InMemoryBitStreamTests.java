package qub;

public interface InMemoryBitStreamTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(InMemoryBitStream.class, () ->
        {
            BitWriteStreamTests.test(runner, InMemoryBitStream::create);
        });
    }
}
