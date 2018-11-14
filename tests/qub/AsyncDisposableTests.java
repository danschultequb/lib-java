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

                    test.assertSuccess(true, asyncDisposable.disposeAsync().awaitReturn());
                    test.assertTrue(asyncDisposable.isDisposed());
                });

                runner.test("when disposed", (Test test) ->
                {
                    final AsyncDisposable asyncDisposable = creator.run(test);
                    test.assertNotNull(asyncDisposable);
                    test.assertSuccess(true, asyncDisposable.dispose());
                    test.assertTrue(asyncDisposable.isDisposed());

                    final Result<Boolean> result = asyncDisposable.disposeAsync().awaitReturn();
                    test.assertSuccess(false, result);
                    test.assertTrue(asyncDisposable.isDisposed());
                });
            });
        });
    }
}
