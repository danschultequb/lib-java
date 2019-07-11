package qub;

public interface BasicCharacterReadStreamTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(BasicCharacterReadStream.class, () ->
        {
            runner.testGroup("constructor()", () ->
            {
                runner.test("with null byteReadStream", (Test test) ->
                {
                    test.assertThrows(() -> new BasicCharacterReadStream(null, CharacterEncoding.UTF_8),
                        new PreConditionFailure("byteReadStream cannot be null."));
                });

                runner.test("with null characterEncoding", (Test test) ->
                {
                    test.assertThrows(() -> new BasicCharacterReadStream(new InMemoryByteStream(), null),
                        new PreConditionFailure("characterEncoding cannot be null."));
                });

                runner.test("with disposed byteReadStream", (Test test) ->
                {
                    final InMemoryByteStream byteStream = new InMemoryByteStream();
                    byteStream.dispose().await();

                    final BasicCharacterReadStream characterReadStream = new BasicCharacterReadStream(byteStream, CharacterEncoding.UTF_8);
                    test.assertTrue(characterReadStream.isDisposed());
                    test.assertFalse(characterReadStream.hasStarted());
                    test.assertFalse(characterReadStream.hasCurrent());
                });

                runner.test("with non-disposed byteReadStream", (Test test) ->
                {
                    final InMemoryByteStream byteStream = new InMemoryByteStream();
                    final BasicCharacterReadStream characterReadStream = new BasicCharacterReadStream(byteStream, CharacterEncoding.UTF_8);
                    test.assertFalse(characterReadStream.isDisposed());
                    test.assertFalse(characterReadStream.hasStarted());
                    test.assertFalse(characterReadStream.hasCurrent());
                });
            });
        });
    }
}
