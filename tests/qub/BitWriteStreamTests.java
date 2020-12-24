package qub;

public interface BitWriteStreamTests
{
    static void test(TestRunner runner, Function0<? extends BitWriteStream> creator)
    {
        PreCondition.assertNotNull(runner, "runner");
        PreCondition.assertNotNull(creator, "creator");

        runner.testGroup(BitWriteStream.class, () ->
        {
            runner.testGroup("write(int)", () ->
            {
                final Action2<Integer,Throwable> writeErrorTest = (Integer bit, Throwable expected) ->
                {
                    runner.test("with " + bit, (Test test) ->
                    {
                        try (final BitWriteStream stream = creator.run())
                        {
                            test.assertThrows(() -> stream.write(bit.intValue()).await(),
                                expected);
                        }
                    });
                };

                writeErrorTest.run(-1, new PreConditionFailure("bit (-1) must be between 0 and 1."));
                writeErrorTest.run(2, new PreConditionFailure("bit (2) must be between 0 and 1."));
            });
        });
    }
}
