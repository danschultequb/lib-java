package qub;

public interface RunnableEvent1Tests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(RunnableEvent1.class, () ->
        {
            Event1Tests.test(runner, RunnableEvent1::create);

            runner.testGroup("add(Action1<T>)", () ->
            {
                runner.test("with one callback", (Test test) ->
                {
                    final RunnableEvent1<Integer> event = RunnableEvent1.create();

                    final IntegerValue value = IntegerValue.create(0);
                    final Disposable callbackDisposable = event.add(value::plusAssign);
                    test.assertNotNull(callbackDisposable);
                    test.assertFalse(callbackDisposable.isDisposed());
                    test.assertEqual(0, value.get());

                    event.run(1);
                    test.assertEqual(1, value.get());

                    event.run(5);
                    test.assertEqual(6, value.get());

                    test.assertTrue(callbackDisposable.dispose().await());
                    event.run(-3);
                    test.assertEqual(6, value.get());
                });
            });
        });
    }
}
