package qub;

public interface RunnableEvent2Tests
{
    static void test(TestRunner runner, Function0<? extends RunnableEvent2<Integer,Integer>> creator)
    {
        runner.testGroup(BasicRunnableEvent2.class, () ->
        {
            Event2Tests.test(runner, BasicRunnableEvent2::create);

            runner.testGroup("subscribe(Action0)", () ->
            {
                runner.test("with one callback", (Test test) ->
                {
                    final RunnableEvent2<Integer,Integer> event = creator.run();

                    final IntegerValue value = Value.create(0);
                    final Disposable callbackDisposable = event.subscribe(value::increment);
                    test.assertNotNull(callbackDisposable);
                    test.assertFalse(callbackDisposable.isDisposed());
                    test.assertEqual(0, value.get());

                    event.run(1, 1);
                    test.assertEqual(1, value.get());

                    event.run(5, -1);
                    test.assertEqual(2, value.get());

                    test.assertTrue(callbackDisposable.dispose().await());
                    event.run(-3, 1);
                    test.assertEqual(2, value.get());
                });
            });

            runner.testGroup("subscribe(Action2<T1,T2>)", () ->
            {
                runner.test("with one callback", (Test test) ->
                {
                    final RunnableEvent2<Integer,Integer> event = creator.run();

                    final IntegerValue value = IntegerValue.create(0);
                    final Disposable callbackDisposable = event.subscribe((Integer arg1, Integer arg2) ->
                    {
                        value.plusAssign(arg1 * arg2);
                    });
                    test.assertNotNull(callbackDisposable);
                    test.assertFalse(callbackDisposable.isDisposed());
                    test.assertEqual(0, value.get());

                    event.run(1, 1);
                    test.assertEqual(1, value.get());

                    event.run(5, -1);
                    test.assertEqual(-4, value.get());

                    test.assertTrue(callbackDisposable.dispose().await());
                    event.run(-3, 1);
                    test.assertEqual(-4, value.get());
                });
            });
        });
    }
}
