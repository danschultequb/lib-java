package qub;

public interface RunnableEvent1Tests
{
    static void test(TestRunner runner, Function0<? extends RunnableEvent1<Integer>> creator)
    {
        runner.testGroup(BasicRunnableEvent1.class, () ->
        {
            Event1Tests.test(runner, BasicRunnableEvent1::create);

            runner.testGroup("subscribe(Action0)", () ->
            {
                runner.test("with one callback", (Test test) ->
                {
                    final RunnableEvent1<Integer> event = creator.run();

                    final IntegerValue value = Value.create(0);
                    final Disposable callbackDisposable = event.subscribe(value::increment);
                    test.assertNotNull(callbackDisposable);
                    test.assertFalse(callbackDisposable.isDisposed());
                    test.assertEqual(0, value.get());

                    event.run(1);
                    test.assertEqual(1, value.get());

                    event.run(5);
                    test.assertEqual(2, value.get());

                    test.assertTrue(callbackDisposable.dispose().await());
                    event.run(-3);
                    test.assertEqual(2, value.get());
                });
            });

            runner.testGroup("subscribe(Action1<T>)", () ->
            {
                runner.test("with one callback", (Test test) ->
                {
                    final RunnableEvent1<Integer> event = creator.run();

                    final List<Integer> eventValues = List.create();
                    final Disposable callbackDisposable = event.subscribe(eventValues::add);
                    test.assertNotNull(callbackDisposable);
                    test.assertFalse(callbackDisposable.isDisposed());
                    test.assertEqual(Iterable.create(), eventValues);

                    event.run(1);
                    test.assertEqual(Iterable.create(1), eventValues);

                    event.run(5);
                    test.assertEqual(Iterable.create(1, 5), eventValues);

                    test.assertTrue(callbackDisposable.dispose().await());
                    event.run(-3);
                    test.assertEqual(Iterable.create(1, 5), eventValues);
                });
            });
        });
    }
}
