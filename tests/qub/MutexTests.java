package qub;

public class MutexTests
{
    public static void test(TestRunner runner, Function0<Mutex> creator)
    {
        runner.testGroup(Mutex.class, () ->
        {
            runner.test("constructor()", (Test test) ->
            {
                final Mutex mutex = creator.run();
                test.assertFalse(mutex.isAcquired());
            });

            runner.test("acquire() when not locked", (Test test) ->
            {
                final Mutex mutex = creator.run();
                mutex.acquire();
                test.assertTrue(mutex.isAcquired());
            });

            runner.testGroup("tryAcquire()", () ->
            {
                runner.test("when not locked", (Test test) ->
                {
                    final Mutex mutex = creator.run();
                    test.assertTrue(mutex.tryAcquire());
                    test.assertTrue(mutex.isAcquired());
                });

                runner.test("when locked by same thread", (Test test) ->
                {
                    final Mutex mutex = creator.run();
                    mutex.acquire();
                    test.assertTrue(mutex.tryAcquire());
                    test.assertTrue(mutex.isAcquired());
                });

                runner.test("when locked by different thread", (Test test) ->
                {
                    final Mutex mutex = creator.run();
                    mutex.acquire();

                    test.getParallelAsyncRunner().schedule(new Action0()
                    {
                        @Override
                        public void run()
                        {
                            test.assertFalse(mutex.tryAcquire());
                            test.assertTrue(mutex.isAcquired());
                        }
                    }).await();

                    test.assertTrue(mutex.isAcquired());
                });
            });

            runner.testGroup("release()", () ->
            {
                runner.test("when not locked", (Test test) ->
                {
                    final Mutex mutex = creator.run();
                    mutex.release();
                    test.assertFalse(mutex.isAcquired());
                });

                runner.test("when locked", (Test test) ->
                {
                    final Mutex mutex = creator.run();
                    mutex.acquire();
                    test.assertTrue(mutex.isAcquired());
                    test.assertTrue(mutex.release());
                    test.assertFalse(mutex.isAcquired());
                });
            });

            runner.testGroup("criticalSection(Action0)", () ->
            {
                runner.test("with null action", (Test test) ->
                {
                    final Mutex mutex = creator.run();
                    mutex.criticalSection((Action0)null);
                    test.assertFalse(mutex.isAcquired());
                });

                runner.test("with non-null action", (Test test) ->
                {
                    final Mutex mutex = creator.run();
                    final Value<Integer> value = new Value<>();
                    mutex.criticalSection(() ->
                    {
                        test.assertTrue(mutex.isAcquired());
                        value.set(20);
                    });
                    test.assertFalse(mutex.isAcquired());
                    test.assertEqual(20, value.get());
                });
            });

            runner.testGroup("criticalSection(Function0<T>)", () ->
            {
                runner.test("with null function", (Test test) ->
                {
                    final Mutex mutex = creator.run();
                    final Integer result = mutex.criticalSection((Function0<Integer>)null);
                    test.assertFalse(mutex.isAcquired());
                    test.assertNull(result);
                });

                runner.test("with non-null function", (Test test) ->
                {
                    final Mutex mutex = creator.run();
                    final Value<Integer> value = new Value<>();
                    final Boolean result = mutex.criticalSection(() ->
                    {
                        test.assertTrue(mutex.isAcquired());
                        value.set(20);
                        return true;
                    });
                    test.assertFalse(mutex.isAcquired());
                    test.assertEqual(20, value.get());
                    test.assertTrue(result);
                });
            });

            runner.test("criticalSection()", (Test test) ->
            {
                final Mutex mutex = creator.run();
                try (final Disposable criticalSection = mutex.criticalSection())
                {
                    test.assertNotNull(criticalSection);
                    test.assertTrue(mutex.isAcquired());
                }
                test.assertFalse(mutex.isAcquired());
            });
        });
    }
}
