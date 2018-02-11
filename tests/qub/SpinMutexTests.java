package qub;

public class SpinMutexTests
{
    public static void test(final TestRunner runner)
    {
        runner.testGroup("SpinMutex", new Action0()
        {
            @Override
            public void run()
            {
                runner.test("constructor()", new Action1<Test>()
                {
                    @Override
                    public void run(Test test)
                    {
                        final SpinMutex mutex = new SpinMutex();
                        test.assertFalse(mutex.isAcquired());
                    }
                });
                
                runner.test("acquire() when not locked", new Action1<Test>()
                {
                    @Override
                    public void run(Test test)
                    {
                        final SpinMutex mutex = new SpinMutex();
                        mutex.acquire();
                        test.assertTrue(mutex.isAcquired());
                    }
                });

                runner.testGroup("tryAcquire()", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("when not locked", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final SpinMutex mutex = new SpinMutex();
                                test.assertTrue(mutex.tryAcquire());
                                test.assertTrue(mutex.isAcquired());
                            }
                        });

                        runner.test("when locked", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final SpinMutex mutex = new SpinMutex();
                                mutex.acquire();
                                test.assertFalse(mutex.tryAcquire());
                                test.assertTrue(mutex.isAcquired());
                            }
                        });
                    }
                });

                runner.testGroup("release()", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("when not locked", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final SpinMutex mutex = new SpinMutex();
                                test.assertFalse(mutex.release());
                                test.assertFalse(mutex.isAcquired());
                            }
                        });

                        runner.test("when locked", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final SpinMutex mutex = new SpinMutex();
                                mutex.acquire();
                                test.assertTrue(mutex.release());
                                test.assertFalse(mutex.isAcquired());
                            }
                        });
                    }
                });

                runner.testGroup("criticalSection(Action0)", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("with null action", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final SpinMutex mutex = new SpinMutex();
                                mutex.criticalSection((Action0)null);
                                test.assertFalse(mutex.isAcquired());
                            }
                        });

                        runner.test("with non-null action", new Action1<Test>()
                        {
                            @Override
                            public void run(final Test test)
                            {
                                final SpinMutex mutex = new SpinMutex();
                                final Value<Integer> value = new Value<>();
                                mutex.criticalSection(new Action0()
                                {
                                    @Override
                                    public void run()
                                    {
                                        test.assertTrue(mutex.isAcquired());
                                        value.set(20);
                                    }
                                });
                                test.assertFalse(mutex.isAcquired());
                                test.assertEqual(20, value.get());
                            }
                        });
                    }
                });

                runner.testGroup("criticalSection(Function0<T>)", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("with null function", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final SpinMutex mutex = new SpinMutex();
                                final Integer result = mutex.criticalSection((Function0<Integer>)null);
                                test.assertFalse(mutex.isAcquired());
                                test.assertNull(result);
                            }
                        });

                        runner.test("with non-null function", new Action1<Test>()
                        {
                            @Override
                            public void run(final Test test)
                            {
                                final SpinMutex mutex = new SpinMutex();
                                final Value<Integer> value = new Value<>();
                                final Boolean result = mutex.criticalSection(new Function0<Boolean>()
                                {
                                    @Override
                                    public Boolean run()
                                    {
                                        test.assertTrue(mutex.isAcquired());
                                        value.set(20);
                                        return true;
                                    }
                                });
                                test.assertFalse(mutex.isAcquired());
                                test.assertEqual(20, value.get());
                                test.assertTrue(result);
                            }
                        });
                    }
                });
            }
        });
    }
}
