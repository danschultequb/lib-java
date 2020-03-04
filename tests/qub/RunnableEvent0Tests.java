package qub;

public interface RunnableEvent0Tests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(RunnableEvent0.class, () ->
        {
            Event0Tests.test(runner, RunnableEvent0::create);

            runner.testGroup("add(Action0)", () ->
            {
                runner.test("with one callback", (Test test) ->
                {
                    final RunnableEvent0 event = RunnableEvent0.create();

                    final IntegerValue value = IntegerValue.create(0);
                    final Disposable callbackDisposable = event.add(value::increment);
                    test.assertNotNull(callbackDisposable);
                    test.assertFalse(callbackDisposable.isDisposed());
                    test.assertEqual(0, value.get());

                    event.run();
                    test.assertEqual(1, value.get());

                    event.run();
                    test.assertEqual(2, value.get());

                    test.assertTrue(callbackDisposable.dispose().await());
                    event.run();
                    test.assertEqual(2, value.get());
                });
            });
        });
    }
}
