package qub;

public class SpinMutexTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(SpinMutex.class, () ->
        {
            runner.test("constructor()", (Test test) ->
            {
                final SpinMutex mutex = new SpinMutex();
                test.assertFalse(mutex.isAcquired());
            });
            
            runner.test("acquire() when not locked", (Test test) ->
            {
                final SpinMutex mutex = new SpinMutex();
                mutex.acquire();
                test.assertTrue(mutex.isAcquired());
            });

            runner.testGroup("tryAcquire()", () ->
            {
                runner.test("when not locked", (Test test) ->
                {
                    final SpinMutex mutex = new SpinMutex();
                    test.assertTrue(mutex.tryAcquire());
                    test.assertTrue(mutex.isAcquired());
                });

                runner.test("when locked", (Test test) ->
                {
                    final SpinMutex mutex = new SpinMutex();
                    mutex.acquire();
                    test.assertFalse(mutex.tryAcquire());
                    test.assertTrue(mutex.isAcquired());
                });
            });

            runner.testGroup("release()", () ->
            {
                runner.test("when not locked", (Test test) ->
                {
                    final SpinMutex mutex = new SpinMutex();
                    test.assertFalse(mutex.release());
                    test.assertFalse(mutex.isAcquired());
                });

                runner.test("when locked", (Test test) ->
                {
                    final SpinMutex mutex = new SpinMutex();
                    mutex.acquire();
                    test.assertTrue(mutex.release());
                    test.assertFalse(mutex.isAcquired());
                });
            });

            runner.testGroup("criticalSection(Action0)", () ->
            {
                runner.test("with null action", (Test test) ->
                {
                    final SpinMutex mutex = new SpinMutex();
                    mutex.criticalSection((Action0)null);
                    test.assertFalse(mutex.isAcquired());
                });

                runner.test("with non-null action", (Test test) ->
                {
                    final SpinMutex mutex = new SpinMutex();
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
                    final SpinMutex mutex = new SpinMutex();
                    final Integer result = mutex.criticalSection((Function0<Integer>)null);
                    test.assertFalse(mutex.isAcquired());
                    test.assertNull(result);
                });

                runner.test("with non-null function", (Test test) ->
                {
                    final SpinMutex mutex = new SpinMutex();
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
        });
    }
}
