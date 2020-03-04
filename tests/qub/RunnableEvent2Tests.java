package qub;

public interface RunnableEvent2Tests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(RunnableEvent2.class, () ->
        {
            Event2Tests.test(runner, RunnableEvent2::create);

            runner.testGroup("add(Action2<T1,T2>)", () ->
            {
                runner.test("with one callback", (Test test) ->
                {
                    final RunnableEvent2<Integer,Boolean> event = RunnableEvent2.create();

                    final IntegerValue value = IntegerValue.create(0);
                    final Disposable callbackDisposable = event.add((Integer arg1, Boolean arg2) ->
                    {
                        if (arg2)
                        {
                            value.plusAssign(arg1);
                        }
                        else
                        {
                            value.minusAssign(arg1);
                        }
                    });
                    test.assertNotNull(callbackDisposable);
                    test.assertFalse(callbackDisposable.isDisposed());
                    test.assertEqual(0, value.get());

                    event.run(1, true);
                    test.assertEqual(1, value.get());

                    event.run(5, false);
                    test.assertEqual(-4, value.get());

                    test.assertTrue(callbackDisposable.dispose().await());
                    event.run(-3, true);
                    test.assertEqual(-4, value.get());
                });
            });
        });
    }
}
