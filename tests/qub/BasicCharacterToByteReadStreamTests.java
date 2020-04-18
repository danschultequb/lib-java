package qub;

public interface BasicCharacterToByteReadStreamTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(BasicCharacterToByteReadStream.class, () ->
        {
            runner.testGroup("create(ByteReadStream,CharacterEncoding)", () ->
            {
                runner.test("with null byteReadStream", (Test test) ->
                {
                    test.assertThrows(() -> BasicCharacterToByteReadStream.create(null, CharacterEncoding.UTF_8),
                        new PreConditionFailure("byteReadStream cannot be null."));
                });

                runner.test("with null characterEncoding", (Test test) ->
                {
                    test.assertThrows(() -> BasicCharacterToByteReadStream.create(ByteReadStream.create(), null),
                        new PreConditionFailure("characterEncoding cannot be null."));
                });

                runner.test("with disposed byteReadStream", (Test test) ->
                {
                    final ByteReadStream byteStream = ByteReadStream.create();
                    byteStream.dispose().await();

                    final BasicCharacterToByteReadStream characterReadStream = BasicCharacterToByteReadStream.create(byteStream, CharacterEncoding.UTF_8);
                    test.assertTrue(characterReadStream.isDisposed());
                });

                runner.test("with non-disposed byteReadStream", (Test test) ->
                {
                    final ByteReadStream byteStream = ByteReadStream.create();
                    final BasicCharacterToByteReadStream characterReadStream = BasicCharacterToByteReadStream.create(byteStream, CharacterEncoding.UTF_8);
                    test.assertFalse(characterReadStream.isDisposed());
                });
            });
        });
    }
}
