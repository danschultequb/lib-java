package qub;

public class AsyncRunnerTests
{
    public static void test(TestRunner runner, Function0<AsyncRunner> createAsyncRunner)
    {
        runner.testGroup(AsyncRunner.class, () ->
        {
            runner.testGroup("schedule()", () ->
            {
                runner.test("with null Action0", (Test test) ->
                {
                    try (final AsyncRunner runner1 = createAsyncRunner.run())
                    {
                        final AsyncAction asyncAction = runner1.schedule((Action0)null);
                        test.assertNull(asyncAction);
                        test.assertEqual(0, runner1.getScheduledTaskCount());
                    }
                    catch (Exception e)
                    {
                        test.fail(e);
                    }
                });
                
                runner.test("with null Function0", (Test test) ->
                {
                    try (final AsyncRunner runner1 = createAsyncRunner.run())
                    {
                        final AsyncFunction<Integer> asyncFunction = runner1.schedule((Function0<Integer>)null);
                        test.assertNull(asyncFunction);
                        test.assertEqual(0, runner1.getScheduledTaskCount());
                    }
                    catch (Exception e)
                    {
                        test.fail(e);
                    }
                });
            });
        });
    }
}
