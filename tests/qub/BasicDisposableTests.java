package qub;

public class BasicDisposableTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(BasicDisposable.class, () ->
        {
            runner.test("dispose()", (Test test) ->
            {
                final Value<Integer> value = new Value<>(0);
                final BasicDisposable disposable = new BasicDisposable()
                {
                    @Override
                    protected void onDispose()
                    {
                        value.set(value.get() + 1);
                    }
                };
                test.assertFalse(disposable.isDisposed());
                test.assertEqual(0, value.get());

                test.assertSuccess(true, disposable.dispose());
                test.assertTrue(disposable.isDisposed());
                test.assertEqual(1, value.get());

                test.assertSuccess(false, disposable.dispose());
                test.assertTrue(disposable.isDisposed());
                test.assertEqual(1, value.get());
            });
        });
    }
}
