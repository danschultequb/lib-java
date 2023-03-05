package qub;

public interface ActionsTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(Actions.class, () ->
        {
            runner.testGroup("run(Action0)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    Actions.run((Action0)null);
                });

                runner.test("with non-null", (Test test) ->
                {
                    final IntegerValue counter = IntegerValue.create(0);
                    Actions.run(counter::increment);
                    test.assertEqual(1, counter.get());
                });
            });

            runner.testGroup("run(Action1<T1>,T1)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    Actions.run((Action1<Integer>)null, 5);
                });

                runner.test("with non-null", (Test test) ->
                {
                    final IntegerValue counter = IntegerValue.create(0);
                    Actions.run(counter::plusAssign, 5);
                    test.assertEqual(5, counter.get());
                });
            });

            runner.testGroup("run(Action2<T1,T2>,T1,T2)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    Actions.run((Action2<Integer,Boolean>)null, 5, false);
                });

                runner.test("with non-null", (Test test) ->
                {
                    final IntegerValue counter = IntegerValue.create(0);
                    Actions.run((Integer value, Boolean negate) -> { counter.set(value * (negate ? -1 : 1)); }, 5, true);
                    test.assertEqual(-5, counter.get());
                });
            });
        });
    }
}
