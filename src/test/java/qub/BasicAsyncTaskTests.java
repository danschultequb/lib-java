package qub;

public class BasicAsyncTaskTests
{
    public static void test(final TestRunner runner, final Function1<AsyncRunner,BasicAsyncTask> creator)
    {
        runner.testGroup("BasicAsyncTask", new Action0()
        {
            @Override
            public void run()
            {
                runner.testGroup("then(Action0)", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("with null", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final BasicAsyncTask basicAsyncTask = create(creator);
                                final AsyncAction thenAsyncAction = basicAsyncTask.then(TestUtils.nullAction0);
                                test.assertNull(thenAsyncAction);
                                test.assertEqual(0, basicAsyncTask.getPausedTaskCount());
                            }
                        });
                        
                        runner.test("with non-null", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final BasicAsyncTask basicAsyncTask = create(creator);
                                final AsyncAction thenAsyncAction = basicAsyncTask.then(TestUtils.emptyAction0);
                                test.assertNotNull(thenAsyncAction);
                                test.assertEqual(1, basicAsyncTask.getPausedTaskCount());
                            }
                        });
                        
                        runner.test("with non-null when completed", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final CurrentThreadAsyncRunner runner = createCurrentThreadAsyncRunner();
                                final BasicAsyncTask basicAsyncTask = createScheduled(creator, runner);
                                runner.await();
                                test.assertTrue(basicAsyncTask.isCompleted());

                                final AsyncAction thenAsyncAction = basicAsyncTask.then(TestUtils.emptyAction0);

                                test.assertNotNull(thenAsyncAction);
                                test.assertEqual(0, basicAsyncTask.getPausedTaskCount());
                                test.assertEqual(1, runner.getScheduledTaskCount());
                            }
                        });
                    }
                });
                
                runner.testGroup("thenAsyncAction()", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("with null", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final BasicAsyncTask basicAsyncTask = create(creator);
                                final AsyncAction thenAsyncAction = basicAsyncTask.thenAsyncAction(null);
                                test.assertNull(thenAsyncAction);
                                test.assertEqual(0, basicAsyncTask.getPausedTaskCount());
                            }
                        });
                        
                        runner.test("with non-null", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final CurrentThreadAsyncRunner runner = createCurrentThreadAsyncRunner();
                                final BasicAsyncTask basicAsyncTask = createScheduled(creator, runner);

                                final Value<Integer> value = new Value<>();
                                final AsyncAction thenAsyncAction = basicAsyncTask.thenAsyncAction(new Function0<AsyncAction>()
                                {
                                    @Override
                                    public AsyncAction run()
                                    {
                                        return runner.schedule(TestUtils.setValueAction0(value, 5));
                                    }
                                });
                                test.assertNotNull(thenAsyncAction);
                                test.assertEqual(1, basicAsyncTask.getPausedTaskCount());

                                runner.await();
                                test.assertEqual(0, basicAsyncTask.getPausedTaskCount());
                                test.assertTrue(basicAsyncTask.isCompleted());
                                test.assertEqual(0, runner.getScheduledTaskCount());
                                test.assertEqual(5, value.get());
                            }
                        });
                        
                        runner.test("with non-null when completed", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final CurrentThreadAsyncRunner runner = createCurrentThreadAsyncRunner();
                                final BasicAsyncTask basicAsyncTask = createScheduled(creator, runner);
                                runner.await();

                                final Value<Integer> value = new Value<>();
                                final AsyncAction thenAsyncAction = basicAsyncTask.thenAsyncAction(new Function0<AsyncAction>()
                                {
                                    @Override
                                    public AsyncAction run()
                                    {
                                        return runner.schedule(TestUtils.setValueAction0(value, 5));
                                    }
                                });
                                test.assertNotNull(thenAsyncAction);
                                test.assertEqual(0, basicAsyncTask.getPausedTaskCount());
                                test.assertEqual(1, runner.getScheduledTaskCount());

                                runner.await();
                                test.assertEqual(0, basicAsyncTask.getPausedTaskCount());
                                test.assertTrue(basicAsyncTask.isCompleted());
                                test.assertEqual(0, runner.getScheduledTaskCount());
                                test.assertEqual(5, value.get());
                            }
                        });
                    }
                });
                
                runner.testGroup("then(Function0)", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("with null", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final BasicAsyncTask basicAsyncTask = create(creator);
                                final AsyncFunction<Integer> thenAsyncFunction = basicAsyncTask.then(TestUtils.nullFunction0);
                                test.assertNull(thenAsyncFunction);
                                test.assertEqual(0, basicAsyncTask.getPausedTaskCount());
                            }
                        });
                        
                        runner.test("with non-null", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final BasicAsyncTask basicAsyncTask = create(creator);
                                final AsyncFunction<Integer> thenAsyncFunction = basicAsyncTask.then(TestUtils.emptyFunction0);
                                test.assertNotNull(thenAsyncFunction);
                                test.assertEqual(1, basicAsyncTask.getPausedTaskCount());
                            }
                        });
                        
                        runner.test("with non-null when completed", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final CurrentThreadAsyncRunner runner = createCurrentThreadAsyncRunner();
                                final BasicAsyncTask basicAsyncTask = createScheduled(creator, runner);
                                runner.await();
                                test.assertTrue(basicAsyncTask.isCompleted());

                                final AsyncFunction<Integer> thenAsyncFunction = basicAsyncTask.then(TestUtils.emptyFunction0);

                                test.assertNotNull(thenAsyncFunction);
                                test.assertEqual(0, basicAsyncTask.getPausedTaskCount());
                                test.assertEqual(1, runner.getScheduledTaskCount());
                            }
                        });
                    }
                });
                
                runner.testGroup("thenAsyncFunction()", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("with null", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final BasicAsyncTask basicAsyncTask = create(creator);
                                final AsyncAction thenAsyncAction = basicAsyncTask.thenAsyncFunction(null);
                                test.assertNull(thenAsyncAction);
                                test.assertEqual(0, basicAsyncTask.getPausedTaskCount());
                            }
                        });
                        
                        runner.test("with non-null", new Action1<Test>()
                        {
                            @Override
                            public void run(final Test test)
                            {
                                final CurrentThreadAsyncRunner runner = createCurrentThreadAsyncRunner();
                                final BasicAsyncTask basicAsyncTask = createScheduled(creator, runner);

                                final Value<Integer> value = new Value<>();
                                final AsyncAction thenAsyncAction = basicAsyncTask
                                    .thenAsyncFunction(new Function0<AsyncFunction<Integer>>()
                                    {
                                        @Override
                                        public AsyncFunction<Integer> run()
                                        {
                                            return runner.schedule(TestUtils.setValueFunction0(value, 5, 6));
                                        }
                                    })
                                    .then(new Action1<Integer>()
                                    {
                                        @Override
                                        public void run(Integer asyncFunctionReturnValue)
                                        {
                                            test.assertEqual(6, asyncFunctionReturnValue);
                                        }
                                    });
                                test.assertNotNull(thenAsyncAction);
                                test.assertEqual(1, basicAsyncTask.getPausedTaskCount());

                                runner.await();
                                test.assertEqual(0, basicAsyncTask.getPausedTaskCount());
                                test.assertTrue(basicAsyncTask.isCompleted());
                                test.assertEqual(0, runner.getScheduledTaskCount());
                                test.assertEqual(5, value.get());
                            }
                        });
                        
                        runner.test("with non-null when completed", new Action1<Test>()
                        {
                            @Override
                            public void run(final Test test)
                            {
                                final CurrentThreadAsyncRunner runner = createCurrentThreadAsyncRunner();
                                final BasicAsyncTask basicAsyncTask = createScheduled(creator, runner);
                                runner.await();

                                final Value<Integer> value = new Value<>();
                                final AsyncAction thenAsyncAction = basicAsyncTask
                                    .thenAsyncFunction(new Function0<AsyncFunction<Integer>>()
                                    {
                                        @Override
                                        public AsyncFunction<Integer> run()
                                        {
                                            return runner.schedule(TestUtils.setValueFunction0(value, 5, 6));
                                        }
                                    })
                                    .then(new Action1<Integer>()
                                    {
                                        @Override
                                        public void run(Integer asyncFunctionReturnValue)
                                        {
                                            test.assertEqual(6, asyncFunctionReturnValue);
                                        }
                                    });
                                test.assertNotNull(thenAsyncAction);
                                test.assertEqual(0, basicAsyncTask.getPausedTaskCount());
                                test.assertEqual(1, runner.getScheduledTaskCount());

                                runner.await();
                                test.assertEqual(0, basicAsyncTask.getPausedTaskCount());
                                test.assertTrue(basicAsyncTask.isCompleted());
                                test.assertEqual(0, runner.getScheduledTaskCount());
                                test.assertEqual(5, value.get());
                            }
                        });
                    }
                });
                
                runner.testGroup("thenOn(AsyncRunner,Action0)", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("with non-null Action0", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final CurrentThreadAsyncRunner runner1 = createCurrentThreadAsyncRunner();
                                final CurrentThreadAsyncRunner runner2 = createCurrentThreadAsyncRunner();
                                final BasicAsyncTask basicAsyncTask = createScheduled(creator, runner1);

                                final AsyncAction thenOnAsyncAction = basicAsyncTask.thenOn(runner2, TestUtils.emptyAction0);
                                test.assertNotNull(thenOnAsyncAction);
                                test.assertEqual(1, basicAsyncTask.getPausedTaskCount());
                                test.assertEqual(0, runner2.getScheduledTaskCount());

                                runner1.await();
                                test.assertEqual(1, runner2.getScheduledTaskCount());
                            }
                        });
                        
                        runner.test("with non-null Action0 when completed", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final CurrentThreadAsyncRunner runner1 = createCurrentThreadAsyncRunner();
                                final CurrentThreadAsyncRunner runner2 = createCurrentThreadAsyncRunner();
                                final BasicAsyncTask basicAsyncTask = createScheduled(creator, runner1);
                                runner1.await();

                                final AsyncAction thenOnAsyncAction = basicAsyncTask.thenOn(runner2, TestUtils.emptyAction0);
                                test.assertNotNull(thenOnAsyncAction);
                                test.assertEqual(0, basicAsyncTask.getPausedTaskCount());
                                test.assertEqual(1, runner2.getScheduledTaskCount());
                            }
                        });
                    }
                });
                
                runner.testGroup("thenOnAsyncAction()", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("with null AsyncRunner", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final CurrentThreadAsyncRunner runner = createCurrentThreadAsyncRunner();
                                final BasicAsyncTask basicAsyncTask = create(creator);

                                final Value<Integer> value = new Value<>();
                                final AsyncAction thenOnAsyncAction = basicAsyncTask.thenOnAsyncAction(null, new Function0<AsyncAction>()
                                {
                                    @Override
                                    public AsyncAction run()
                                    {
                                        return runner.schedule(TestUtils.setValueAction0(value, 4));
                                    }
                                });
                                test.assertNull(thenOnAsyncAction);
                                test.assertEqual(0, basicAsyncTask.getPausedTaskCount());
                            }
                        });
                        
                        runner.test("with null Function", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final CurrentThreadAsyncRunner runner = createCurrentThreadAsyncRunner();
                                final BasicAsyncTask basicAsyncTask = create(creator);

                                final AsyncAction thenOnAsyncAction = basicAsyncTask.thenOnAsyncAction(runner, null);
                                test.assertNull(thenOnAsyncAction);
                                test.assertEqual(0, basicAsyncTask.getPausedTaskCount());
                            }
                        });
                        
                        runner.test("with non-null Function", new Action1<Test>()
                        {
                            @Override
                            public void run(final Test test)
                            {
                                final CurrentThreadAsyncRunner runner1 = createCurrentThreadAsyncRunner();
                                final CurrentThreadAsyncRunner runner2 = createCurrentThreadAsyncRunner();
                                final CurrentThreadAsyncRunner runner3 = createCurrentThreadAsyncRunner();
                                final BasicAsyncTask basicAsyncTask = createScheduled(creator, runner1);

                                final Value<Integer> value = new Value<>();
                                final AsyncAction thenOnAsyncAction = basicAsyncTask
                                    .thenOnAsyncAction(runner2, new Function0<AsyncAction>()
                                    {
                                        @Override
                                        public AsyncAction run()
                                        {
                                            return runner3.schedule(TestUtils.setValueAction0(value, 4));
                                        }
                                    })
                                    .then(new Action0()
                                    {
                                        @Override
                                        public void run()
                                        {
                                            test.assertEqual(4, value.get());
                                        }
                                    });
                                test.assertNotNull(thenOnAsyncAction);
                                test.assertEqual(1, basicAsyncTask.getPausedTaskCount());
                                test.assertEqual(1, runner1.getScheduledTaskCount());
                                test.assertEqual(0, runner2.getScheduledTaskCount());
                                test.assertEqual(0, runner3.getScheduledTaskCount());
                                test.assertFalse(value.hasValue());

                                runner1.await();
                                test.assertEqual(0, basicAsyncTask.getPausedTaskCount());
                                test.assertEqual(0, runner1.getScheduledTaskCount());
                                test.assertEqual(1, runner2.getScheduledTaskCount());
                                test.assertEqual(0, runner3.getScheduledTaskCount());
                                test.assertFalse(value.hasValue());

                                runner2.await();
                                test.assertEqual(0, basicAsyncTask.getPausedTaskCount());
                                test.assertEqual(0, runner1.getScheduledTaskCount());
                                test.assertEqual(0, runner2.getScheduledTaskCount());
                                test.assertEqual(1, runner3.getScheduledTaskCount());
                                test.assertFalse(value.hasValue());

                                runner3.await();
                                test.assertEqual(0, basicAsyncTask.getPausedTaskCount());
                                test.assertEqual(0, runner1.getScheduledTaskCount());
                                test.assertEqual(0, runner2.getScheduledTaskCount());
                                test.assertEqual(0, runner3.getScheduledTaskCount());
                                test.assertEqual(4, value.get());
                            }
                        });

                        runner.test("with non-null Function when completed", new Action1<Test>()
                        {
                            @Override
                            public void run(final Test test)
                            {
                                final CurrentThreadAsyncRunner runner1 = createCurrentThreadAsyncRunner();
                                final CurrentThreadAsyncRunner runner2 = createCurrentThreadAsyncRunner();
                                final CurrentThreadAsyncRunner runner3 = createCurrentThreadAsyncRunner();
                                final BasicAsyncTask basicAsyncTask = createScheduled(creator, runner1);
                                runner1.await();

                                final Value<Integer> value = new Value<>();
                                final AsyncAction thenOnAsyncAction = basicAsyncTask
                                    .thenOnAsyncAction(runner2, new Function0<AsyncAction>()
                                    {
                                        @Override
                                        public AsyncAction run()
                                        {
                                            return runner3.schedule(TestUtils.setValueAction0(value, 4));
                                        }
                                    })
                                    .then(new Action0()
                                    {
                                        @Override
                                        public void run()
                                        {
                                            test.assertEqual(4, value.get());
                                        }
                                    });
                                test.assertNotNull(thenOnAsyncAction);
                                test.assertEqual(0, basicAsyncTask.getPausedTaskCount());
                                test.assertEqual(0, runner1.getScheduledTaskCount());
                                test.assertEqual(1, runner2.getScheduledTaskCount());
                                test.assertEqual(0, runner3.getScheduledTaskCount());
                                test.assertFalse(value.hasValue());

                                runner2.await();
                                test.assertEqual(0, basicAsyncTask.getPausedTaskCount());
                                test.assertEqual(0, runner1.getScheduledTaskCount());
                                test.assertEqual(0, runner2.getScheduledTaskCount());
                                test.assertEqual(1, runner3.getScheduledTaskCount());
                                test.assertFalse(value.hasValue());

                                runner3.await();
                                test.assertEqual(0, basicAsyncTask.getPausedTaskCount());
                                test.assertEqual(0, runner1.getScheduledTaskCount());
                                test.assertEqual(0, runner2.getScheduledTaskCount());
                                test.assertEqual(0, runner3.getScheduledTaskCount());
                                test.assertEqual(4, value.get());
                            }
                        });
                    }
                });

                runner.testGroup("thenOn(AsyncRunner,Function0)", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("with non-null Function0", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final CurrentThreadAsyncRunner runner1 = createCurrentThreadAsyncRunner();
                                final CurrentThreadAsyncRunner runner2 = createCurrentThreadAsyncRunner();
                                final BasicAsyncTask basicAsyncTask = createScheduled(creator, runner1);

                                final AsyncFunction<Integer> thenOnAsyncFunction = basicAsyncTask.thenOn(runner2, TestUtils.emptyFunction0);
                                test.assertNotNull(thenOnAsyncFunction);
                                test.assertEqual(1, basicAsyncTask.getPausedTaskCount());
                                test.assertEqual(0, runner2.getScheduledTaskCount());

                                runner1.await();
                                test.assertEqual(1, runner2.getScheduledTaskCount());
                            }
                        });

                        runner.test("with non-null Function0 when completed", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final CurrentThreadAsyncRunner runner1 = createCurrentThreadAsyncRunner();
                                final CurrentThreadAsyncRunner runner2 = createCurrentThreadAsyncRunner();
                                final BasicAsyncTask basicAsyncTask = createScheduled(creator, runner1);
                                runner1.await();

                                final AsyncFunction<Integer> thenOnAsyncFunction = basicAsyncTask.thenOn(runner2, TestUtils.emptyFunction0);
                                test.assertNotNull(thenOnAsyncFunction);
                                test.assertEqual(0, basicAsyncTask.getPausedTaskCount());
                                test.assertEqual(1, runner2.getScheduledTaskCount());
                            }
                        });
                    }
                });

                runner.testGroup("thenOnAsyncFunction()", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("with null Function0", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final CurrentThreadAsyncRunner runner = createCurrentThreadAsyncRunner();
                                final BasicAsyncTask basicAsyncTask = create(creator);

                                final AsyncAction thenOnAsyncAction = basicAsyncTask.thenOnAsyncAction(runner, null);
                                test.assertNull(thenOnAsyncAction);
                                test.assertEqual(0, basicAsyncTask.getPausedTaskCount());
                            }
                        });

                        runner.test("with non-null Function0", new Action1<Test>()
                        {
                            @Override
                            public void run(final Test test)
                            {
                                final CurrentThreadAsyncRunner runner1 = createCurrentThreadAsyncRunner();
                                final CurrentThreadAsyncRunner runner2 = createCurrentThreadAsyncRunner();
                                final CurrentThreadAsyncRunner runner3 = createCurrentThreadAsyncRunner();
                                final BasicAsyncTask basicAsyncTask = createScheduled(creator, runner1);

                                final Value<Integer> value = new Value<>();
                                final AsyncAction thenOnAsyncAction = basicAsyncTask
                                    .thenOnAsyncFunction(runner2, new Function0<AsyncFunction<Integer>>()
                                    {
                                        @Override
                                        public AsyncFunction<Integer> run()
                                        {
                                            return runner3.schedule(TestUtils.setValueFunction0(value, 4, 5));
                                        }
                                    })
                                    .then(new Action1<Integer>()
                                    {
                                        @Override
                                        public void run(Integer asyncFunctionReturnValue)
                                        {
                                            test.assertEqual(5, asyncFunctionReturnValue);
                                        }
                                    });
                                test.assertNotNull(thenOnAsyncAction);
                                test.assertEqual(1, basicAsyncTask.getPausedTaskCount());
                                test.assertEqual(1, runner1.getScheduledTaskCount());
                                test.assertEqual(0, runner2.getScheduledTaskCount());
                                test.assertEqual(0, runner3.getScheduledTaskCount());
                                test.assertFalse(value.hasValue());

                                runner1.await();
                                test.assertEqual(0, basicAsyncTask.getPausedTaskCount());
                                test.assertEqual(0, runner1.getScheduledTaskCount());
                                test.assertEqual(1, runner2.getScheduledTaskCount());
                                test.assertEqual(0, runner3.getScheduledTaskCount());
                                test.assertFalse(value.hasValue());

                                runner2.await();
                                test.assertEqual(0, basicAsyncTask.getPausedTaskCount());
                                test.assertEqual(0, runner1.getScheduledTaskCount());
                                test.assertEqual(0, runner2.getScheduledTaskCount());
                                test.assertEqual(1, runner3.getScheduledTaskCount());
                                test.assertFalse(value.hasValue());

                                runner3.await();
                                test.assertEqual(0, basicAsyncTask.getPausedTaskCount());
                                test.assertEqual(0, runner1.getScheduledTaskCount());
                                test.assertEqual(0, runner2.getScheduledTaskCount());
                                test.assertEqual(0, runner3.getScheduledTaskCount());
                                test.assertEqual(4, value.get());
                            }
                        });

                        runner.test("with non-null Function0 when completed", new Action1<Test>()
                        {
                            @Override
                            public void run(final Test test)
                            {
                                final CurrentThreadAsyncRunner runner1 = createCurrentThreadAsyncRunner();
                                final CurrentThreadAsyncRunner runner2 = createCurrentThreadAsyncRunner();
                                final CurrentThreadAsyncRunner runner3 = createCurrentThreadAsyncRunner();
                                final BasicAsyncTask basicAsyncTask = createScheduled(creator, runner1);
                                runner1.await();

                                final Value<Integer> value = new Value<>();
                                final AsyncAction thenOnAsyncAction = basicAsyncTask
                                    .thenOnAsyncFunction(runner2, new Function0<AsyncFunction<Integer>>()
                                    {
                                        @Override
                                        public AsyncFunction<Integer> run()
                                        {
                                            return runner3.schedule(TestUtils.setValueFunction0(value, 4, 5));
                                        }
                                    })
                                    .then(new Action1<Integer>()
                                    {
                                        @Override
                                        public void run(Integer asyncFunctionReturnValue)
                                        {
                                            test.assertEqual(5, asyncFunctionReturnValue);
                                        }
                                    });
                                test.assertNotNull(thenOnAsyncAction);
                                test.assertEqual(0, basicAsyncTask.getPausedTaskCount());
                                test.assertEqual(0, runner1.getScheduledTaskCount());
                                test.assertEqual(1, runner2.getScheduledTaskCount());
                                test.assertEqual(0, runner3.getScheduledTaskCount());
                                test.assertFalse(value.hasValue());

                                runner2.await();
                                test.assertEqual(0, basicAsyncTask.getPausedTaskCount());
                                test.assertEqual(0, runner1.getScheduledTaskCount());
                                test.assertEqual(0, runner2.getScheduledTaskCount());
                                test.assertEqual(1, runner3.getScheduledTaskCount());
                                test.assertFalse(value.hasValue());

                                runner3.await();
                                test.assertEqual(0, basicAsyncTask.getPausedTaskCount());
                                test.assertEqual(0, runner1.getScheduledTaskCount());
                                test.assertEqual(0, runner2.getScheduledTaskCount());
                                test.assertEqual(0, runner3.getScheduledTaskCount());
                                test.assertEqual(4, value.get());
                            }
                        });
                    }
                });
            }
        });
    }

    private static CurrentThreadAsyncRunner createCurrentThreadAsyncRunner()
    {
        final Synchronization synchronization = new Synchronization();
        return new CurrentThreadAsyncRunner(new Function0<Synchronization>()
        {
            @Override
            public Synchronization run()
            {
                return synchronization;
            }
        });
    }

    private static BasicAsyncTask create(Function1<AsyncRunner,BasicAsyncTask> creator)
    {
        return creator.run(createCurrentThreadAsyncRunner());
    }

    private static BasicAsyncTask createScheduled(Function1<AsyncRunner,BasicAsyncTask> creator, AsyncRunner runner)
    {
        final BasicAsyncTask basicAsyncAction = creator.run(runner);
        basicAsyncAction.schedule();
        return basicAsyncAction;
    }
}
