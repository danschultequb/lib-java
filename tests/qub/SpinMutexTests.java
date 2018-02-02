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
            }
        });
    }
}
