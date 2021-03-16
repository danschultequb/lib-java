package qub;

public interface DisposableActionTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(DisposableAction.class, () ->
        {
            runner.test("dispose()", (Test test) ->
            {
                final Value<Integer> value = Value.create(0);
                final DisposableAction disposable = DisposableAction.create(() -> value.set(value.get() + 1));
                test.assertFalse(disposable.isDisposed());
                test.assertEqual(0, value.get());

                test.assertTrue(disposable.dispose().await());
                test.assertTrue(disposable.isDisposed());
                test.assertEqual(1, value.get());

                test.assertFalse(disposable.dispose().await());
                test.assertTrue(disposable.isDisposed());
                test.assertEqual(1, value.get());
            });
        });
    }
}
