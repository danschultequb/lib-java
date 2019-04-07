package qub;

public class AsyncDisposableTests
{
    public static void test(TestRunner runner, Function1<Test,AsyncDisposable> creator)
    {
        runner.testGroup(AsyncDisposable.class, () ->
        {
            runner.testGroup("disposeAsync()", () ->
            {
                runner.test("when not disposed", (Test test) ->
                {
                    final AsyncDisposable asyncDisposable = creator.run(test);
                    test.assertNotNull(asyncDisposable);
                    test.assertFalse(asyncDisposable.isDisposed());

                    test.assertTrue(asyncDisposable.disposeAsync().awaitReturn().await());
                    test.assertTrue(asyncDisposable.isDisposed());
                });

                runner.test("when disposed", (Test test) ->
                {
                    final AsyncDisposable asyncDisposable = creator.run(test);
                    test.assertNotNull(asyncDisposable);
                    test.assertTrue(asyncDisposable.dispose().await());
                    test.assertTrue(asyncDisposable.isDisposed());

                    test.assertFalse(asyncDisposable.disposeAsync().awaitReturn().await());
                    test.assertTrue(asyncDisposable.isDisposed());
                });
            });
        });
    }
}
