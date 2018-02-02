package qub;

public class AsyncRunnerTests
{
    public static void test(final TestRunner runner, final Function0<AsyncRunner> createAsyncRunner)
    {
        runner.testGroup("AsyncRunner", new Action0()
        {
            @Override
            public void run()
            {
                runner.testGroup("schedule()", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("with null Action0", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final AsyncRunner runner = createAsyncRunner.run();
                                final AsyncAction asyncAction = runner.schedule(TestUtils.nullAction0);
                                test.assertNull(asyncAction);
                                test.assertEqual(0, runner.getScheduledTaskCount());
                            }
                        });
                        
                        runner.test("with null Function0", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final AsyncRunner runner = createAsyncRunner.run();
                                final AsyncFunction<Integer> asyncFunction = runner.schedule(TestUtils.nullFunction0);
                                test.assertNull(asyncFunction);
                                test.assertEqual(0, runner.getScheduledTaskCount());
                            }
                        });
                    }
                });
                
                runner.testGroup("await()", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("with no Actions", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final AsyncRunner runner = createAsyncRunner.run();
                                runner.await();
                            }
                        });
                        
                        runner.test("with one Action", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final AsyncRunner runner = createAsyncRunner.run();

                                final Value<Integer> value = new Value<>(0);
                                runner.schedule(TestUtils.setValueAction0(value, 1));
                                test.assertTrue(runner.getScheduledTaskCount() <= 1);

                                runner.await();
                                test.assertEqual(0, runner.getScheduledTaskCount());
                                test.assertEqual(1, value.get());
                            }
                        });

                        runner.test("with two Action0s", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final AsyncRunner runner = createAsyncRunner.run();

                                final Value<Integer> value1 = new Value<>(0);
                                runner.schedule(TestUtils.setValueAction0(value1, 1));
                                test.assertTrue(runner.getScheduledTaskCount() <= 1);

                                final Value<Integer> value2 = new Value<>(0);
                                runner.schedule(TestUtils.setValueAction0(value2, 2));
                                test.assertTrue(runner.getScheduledTaskCount() <= 2);

                                runner.await();
                                test.assertEqual(0, runner.getScheduledTaskCount());
                                test.assertEqual(1, value1.get());
                                test.assertEqual(2, value2.get());
                            }
                        });

                        runner.test("with one Action0 then Action0", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final AsyncRunner runner = createAsyncRunner.run();

                                final Value<Integer> value = new Value<>(0);
                                runner.schedule(TestUtils.emptyAction0)
                                    .then(TestUtils.setValueAction0(value, 1));
                                test.assertTrue(runner.getScheduledTaskCount() <= 1);

                                runner.await();
                                test.assertEqual(0, runner.getScheduledTaskCount());
                                test.assertEqual(1, value.get());
                            }
                        });

                        runner.test("with one Action0 then Action0 then Action0", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final AsyncRunner runner = createAsyncRunner.run();

                                final Value<Integer> value = new Value<>(0);
                                runner.schedule(TestUtils.emptyAction0)
                                    .then(TestUtils.emptyAction0)
                                    .then(TestUtils.setValueAction0(value, 1));
                                test.assertTrue(runner.getScheduledTaskCount() <= 1);

                                runner.await();
                                test.assertEqual(0, runner.getScheduledTaskCount());
                                test.assertEqual(1, value.get());
                            }
                        });

                        runner.test("with one Function0 then an Action1", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final AsyncRunner runner = createAsyncRunner.run();

                                final Value<Integer> functionReturnValue = new Value<>();
                                final Value<Integer> actionArgument = new Value<>();
                                runner.schedule(new Function0<Integer>()
                                {
                                    @Override
                                    public Integer run()
                                    {
                                        functionReturnValue.set(1);
                                        return functionReturnValue.get();
                                    }
                                })
                                    .then(new Action1<Integer>()
                                    {
                                        @Override
                                        public void run(Integer arg1)
                                        {
                                            actionArgument.set(arg1);
                                        }
                                    });
                                test.assertTrue(runner.getScheduledTaskCount() <= 1);

                                runner.await();
                                test.assertEqual(1, functionReturnValue.get());
                                test.assertEqual(1, actionArgument.get());
                            }
                        });

                        runner.test("with Function0 then Function1", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final AsyncRunner runner = createAsyncRunner.run();

                                final Value<Integer> firstFunctionReturn = new Value<>();
                                final Value<Integer> secondFunctionArgument = new Value<>();
                                final Value<Integer> secondFunctionReturnValue = new Value<>();
                                runner.schedule(new Function0<Integer>()
                                {
                                    @Override
                                    public Integer run()
                                    {
                                        firstFunctionReturn.set(1);
                                        return firstFunctionReturn.get();
                                    }
                                })
                                    .then(new Function1<Integer,Integer>()
                                    {
                                        @Override
                                        public Integer run(Integer arg1)
                                        {
                                            secondFunctionArgument.set(arg1);
                                            secondFunctionReturnValue.set(arg1 + 1);
                                            return secondFunctionReturnValue.get();
                                        }
                                    });
                                test.assertTrue(runner.getScheduledTaskCount() <= 1);

                                runner.await();
                                test.assertEqual(1, firstFunctionReturn.get());
                                test.assertEqual(1, secondFunctionArgument.get());
                                test.assertEqual(2, secondFunctionReturnValue.get());
                            }
                        });
                    }
                });
            }
        });
    }
}
