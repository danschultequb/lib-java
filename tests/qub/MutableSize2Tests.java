package qub;

public interface MutableSize2Tests
{
    public static void test(TestRunner runner, Function0<? extends MutableSize2<?>> creator)
    {
        runner.testGroup(MutableSize2.class, () ->
        {
            runner.testGroup("setWidth(T)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final MutableSize2<?> size = creator.run();
                    test.assertThrows(() -> size.setWidth(null),
                        new PreConditionFailure("width cannot be null."));
                });
            });

            runner.testGroup("setHeight(T)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final MutableSize2<?> size = creator.run();
                    test.assertThrows(() -> size.setHeight(null),
                        new PreConditionFailure("height cannot be null."));
                });
            });

            runner.testGroup("set(Size2<T>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final MutableSize2<?> size = creator.run();
                    test.assertThrows(() -> size.set(null),
                        new PreConditionFailure("size cannot be null."));
                });
            });
        });
    }
}
