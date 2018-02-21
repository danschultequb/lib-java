package qub;

public class AsyncRunnerTests
{
    public static void test(final TestRunner runner, final Function0<AsyncRunner> createAsyncRunner)
    {
        runner.testGroup("AsyncRunner", () ->
        {
            runner.testGroup("schedule()", () ->
            {
                runner.test("with null Action0", (Test test) ->
                {
                    try (final AsyncRunner runner1 = createAsyncRunner.run())
                    {
                        final AsyncAction asyncAction = runner1.schedule(TestUtils.nullAction0);
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
                        final AsyncFunction<Integer> asyncFunction = runner1.schedule(TestUtils.nullFunction0);
                        test.assertNull(asyncFunction);
                        test.assertEqual(0, runner1.getScheduledTaskCount());
                    }
                    catch (Exception e)
                    {
                        test.fail(e);
                    }
                });
            });
            
            runner.testGroup("await()", () ->
            {
                runner.test("with no Actions", (Test test) ->
                {
                    try (final AsyncRunner runner1 = createAsyncRunner.run())
                    {
                        runner1.await();
                    }
                    catch (Exception e)
                    {
                        test.fail(e);
                    }
                });
                
                runner.test("with one Action", (Test test) ->
                {
                    try (final AsyncRunner runner1 = createAsyncRunner.run())
                    {
                        final Value<Integer> value = new Value<>(0);
                        runner1.schedule(TestUtils.setValueAction0(value, 1));
                        test.assertTrue(runner1.getScheduledTaskCount() <= 1);

                        runner1.await();
                        test.assertEqual(0, runner1.getScheduledTaskCount());
                        test.assertEqual(1, value.get());
                    }
                    catch (Exception e)
                    {
                        test.fail(e);
                    }
                });

                runner.test("with two Action0s", (Test test) ->
                {
                    try (final AsyncRunner runner1 = createAsyncRunner.run())
                    {
                        final Value<Integer> value1 = new Value<>(0);
                        runner1.schedule(TestUtils.setValueAction0(value1, 1));
                        test.assertTrue(runner1.getScheduledTaskCount() <= 1);

                        final Value<Integer> value2 = new Value<>(0);
                        runner1.schedule(TestUtils.setValueAction0(value2, 2));
                        test.assertTrue(runner1.getScheduledTaskCount() <= 2);

                        runner1.await();
                        test.assertEqual(0, runner1.getScheduledTaskCount());
                        test.assertEqual(1, value1.get());
                        test.assertEqual(2, value2.get());
                    }
                    catch (Exception e)
                    {
                        test.fail(e);
                    }
                });

                runner.test("with one Action0 then Action0", (Test test) ->
                {
                    try (final AsyncRunner runner1 = createAsyncRunner.run())
                    {
                        final Value<Integer> value = new Value<>(0);
                        runner1.schedule(TestUtils.emptyAction0)
                            .then(TestUtils.setValueAction0(value, 1));
                        test.assertTrue(runner1.getScheduledTaskCount() <= 1);

                        runner1.await();
                        test.assertEqual(0, runner1.getScheduledTaskCount());
                        test.assertEqual(1, value.get());
                    }
                    catch (Exception e)
                    {
                        test.fail(e);
                    }
                });

                runner.test("with one Action0 then Action0 then Action0", (Test test) ->
                {
                    try (final AsyncRunner runner1 = createAsyncRunner.run())
                    {
                        final Value<Integer> value = new Value<>(0);
                        runner1.schedule(TestUtils.emptyAction0)
                            .then(TestUtils.emptyAction0)
                            .then(TestUtils.setValueAction0(value, 1));
                        test.assertTrue(runner1.getScheduledTaskCount() <= 1);

                        runner1.await();
                        test.assertEqual(0, runner1.getScheduledTaskCount());
                        test.assertEqual(1, value.get());
                    }
                    catch (Exception e)
                    {
                        test.fail(e);
                    }
                });

                runner.test("with one Function0 then an Action1", (Test test) ->
                {
                    try (final AsyncRunner runner1 = createAsyncRunner.run())
                    {
                        final Value<Integer> functionReturnValue = new Value<>();
                        final Value<Integer> actionArgument = new Value<>();
                        runner1.schedule(() ->
                        {
                            functionReturnValue.set(1);
                            return functionReturnValue.get();
                        })
                        .then((Integer arg1) ->
                        {
                            actionArgument.set(arg1);
                        });
                        test.assertTrue(runner1.getScheduledTaskCount() <= 1);

                        runner1.await();
                        test.assertEqual(1, functionReturnValue.get());
                        test.assertEqual(1, actionArgument.get());
                    }
                    catch (Exception e)
                    {
                        test.fail(e);
                    }
                });

                runner.test("with Function0 then Function1", (Test test) ->
                {
                    try (final AsyncRunner runner1 = createAsyncRunner.run())
                    {
                        final Value<Integer> firstFunctionReturn = new Value<>();
                        final Value<Integer> secondFunctionArgument = new Value<>();
                        final Value<Integer> secondFunctionReturnValue = new Value<>();
                        runner1.schedule(() ->
                        {
                            firstFunctionReturn.set(1);
                            return firstFunctionReturn.get();
                        })
                        .then(arg1 ->
                        {
                            secondFunctionArgument.set(arg1);
                            secondFunctionReturnValue.set(arg1 + 1);
                            return secondFunctionReturnValue.get();
                        });
                        test.assertTrue(runner1.getScheduledTaskCount() <= 1);

                        runner1.await();
                        test.assertEqual(1, firstFunctionReturn.get());
                        test.assertEqual(1, secondFunctionArgument.get());
                        test.assertEqual(2, secondFunctionReturnValue.get());
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
