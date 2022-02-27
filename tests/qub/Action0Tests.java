package qub;

public interface Action0Tests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(Action0.class, () ->
        {
            runner.test("empty", (Test test) ->
            {
                test.assertNotNull(Action0.empty);
                Action0.empty.run();
            });

            runner.testGroup("add(Class<T>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final Action0 action = Action0.empty;
                    test.assertThrows(() -> action.add(null),
                        new PreConditionFailure("parameterType cannot be null."));
                });

                runner.test("with non-null", (Test test) ->
                {
                    final IntegerValue counter = IntegerValue.create(0);
                    final Action0 action0 = counter::increment;
                    final Action1<String> action1 = action0.add(String.class);
                    test.assertNotNull(action1);

                    action1.run("hello");

                    test.assertEqual(1, counter.get());
                });
            });

            runner.testGroup("add(Class<T1>,Class<T2>)", () ->
            {
                final Action3<Class<?>,Class<?>,Throwable> addParametersErrorTest = (Class<?> parameterType1, Class<?> parameterType2, Throwable expected) ->
                {
                    runner.test("with " + English.andList(parameterType1, parameterType2), (Test test) ->
                    {
                        final Action0 action = Action0.empty;
                        test.assertThrows(() -> action.add(parameterType1, parameterType2),
                            expected);
                    });
                };

                addParametersErrorTest.run(null, Boolean.class, new PreConditionFailure("parameterType1 cannot be null."));
                addParametersErrorTest.run(Integer.class, null, new PreConditionFailure("parameterType2 cannot be null."));

                runner.test("with non-null types", (Test test) ->
                {
                    final IntegerValue counter = IntegerValue.create(0);
                    final Action0 action0 = counter::increment;
                    final Action2<String,Boolean> action2 = action0.add(String.class, Boolean.class);
                    test.assertNotNull(action2);

                    action2.run("hello", true);

                    test.assertEqual(1, counter.get());
                });
            });
        });
    }
}
