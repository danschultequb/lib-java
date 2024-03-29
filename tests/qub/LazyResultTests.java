package qub;

public interface LazyResultTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(LazyResult.class, () ->
        {
            ResultTests.test(runner, LazyResult::create);

            runner.test("create()", (Test test) ->
            {
                final LazyResult<Character> result = LazyResult.create();
                test.assertNotNull(result);
                test.assertFalse(result.isCompleted());
                test.assertTrue(result.isParentResultCompleted());

                for (int i = 0; i < 2; ++i)
                {
                    test.assertNull(result.await());
                    test.assertTrue(result.isCompleted());
                }
            });

            runner.testGroup("create(Function0<T>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> LazyResult.create((Function0<Boolean>)null),
                        new PreConditionFailure("function cannot be null."));
                });

                runner.test("with non-null and non-throwing", (Test test) ->
                {
                    final LazyResult<Character> result = LazyResult.create(() -> 'a');
                    test.assertNotNull(result, "result");
                    test.assertFalse(result.isCompleted());
                    test.assertTrue(result.isParentResultCompleted());

                    test.assertEqual('a', result.await());
                    test.assertTrue(result.isCompleted());
                    test.assertTrue(result.isParentResultCompleted());

                    test.assertEqual('a', result.await());
                    test.assertTrue(result.isCompleted());
                    test.assertTrue(result.isParentResultCompleted());
                });

                runner.test("with non-null and throwing", (Test test) ->
                {
                    final LazyResult<Integer> result = LazyResult.create((Function0<Integer>)() -> { throw new NotFoundException("hello"); });
                    test.assertNotNull(result, "result");
                    test.assertFalse(result.isCompleted());
                    test.assertTrue(result.isParentResultCompleted());

                    test.assertThrows(result::await,
                        new NotFoundException("hello"));
                    test.assertTrue(result.isCompleted());
                    test.assertTrue(result.isParentResultCompleted());

                    test.assertThrows(result::await,
                        new NotFoundException("hello"));
                    test.assertTrue(result.isCompleted());
                    test.assertTrue(result.isParentResultCompleted());
                });
            });

            runner.testGroup("error(Throwable)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> LazyResult.error(null),
                        new PreConditionFailure("error cannot be null."));
                });

                runner.test("with non-null Exception", (Test test) ->
                {
                    final Exception e = new Exception("abc");
                    final LazyResult<Boolean> result = LazyResult.error(e);
                    test.assertNotNull(result, "result");
                    test.assertFalse(result.isCompleted());
                    test.assertTrue(result.isParentResultCompleted());

                    for (int i = 0; i < 2; ++i)
                    {
                        test.assertThrows(result::await, e);
                        test.assertTrue(result.isCompleted());
                    }
                });

                runner.test("with non-null RuntimeException", (Test test) ->
                {
                    final RuntimeException e = new RuntimeException("abc");
                    final LazyResult<Boolean> result = LazyResult.error(e);
                    test.assertNotNull(result, "result");
                    test.assertFalse(result.isCompleted());
                    test.assertTrue(result.isParentResultCompleted());

                    for (int i = 0; i < 2; ++i)
                    {
                        test.assertThrows(result::await, e);
                        test.assertTrue(result.isCompleted());
                    }
                });
            });

            runner.testGroup("then(Action0)", () ->
            {
                runner.test("parent result isn't run until then result is awaited", (Test test) ->
                {
                    final IntegerValue parentCounter = IntegerValue.create(0);
                    final LazyResult<Integer> parentResult = LazyResult.create(() ->
                    {
                        parentCounter.increment();
                        return 5;
                    });
                    test.assertFalse(parentResult.isCompleted());
                    test.assertEqual(0, parentCounter.get());

                    final IntegerValue thenCounter = IntegerValue.create(0);
                    final Result<Void> thenResult = parentResult.then(() -> { thenCounter.increment(); });
                    test.assertFalse(parentResult.isCompleted());
                    test.assertEqual(0, parentCounter.get());
                    test.assertFalse(thenResult.isCompleted());
                    test.assertEqual(0, thenCounter.get());

                    test.assertNull(thenResult.await());
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(1, thenCounter.get());
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(thenResult.isCompleted());

                    test.assertNull(thenResult.await());
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(1, thenCounter.get());
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(thenResult.isCompleted());
                });
            });

            runner.testGroup("then(Action1)", () ->
            {
                runner.test("parent result isn't run until then result is awaited", (Test test) ->
                {
                    final IntegerValue parentCounter = IntegerValue.create(0);
                    final LazyResult<Integer> parentResult = LazyResult.create(() ->
                    {
                        parentCounter.increment();
                        return 5;
                    });
                    test.assertFalse(parentResult.isCompleted());
                    test.assertEqual(0, parentCounter.get());

                    final IntegerValue thenCounter = IntegerValue.create(0);
                    final Result<Void> thenResult = parentResult.then((Integer parentValue) -> { thenCounter.plusAssign(parentValue); });
                    test.assertFalse(parentResult.isCompleted());
                    test.assertEqual(0, parentCounter.get());
                    test.assertFalse(thenResult.isCompleted());
                    test.assertEqual(0, thenCounter.get());

                    test.assertNull(thenResult.await());
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(5, thenCounter.get());
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(thenResult.isCompleted());

                    test.assertNull(thenResult.await());
                    test.assertEqual(1, parentCounter.get());
                    test.assertEqual(5, thenCounter.get());
                    test.assertTrue(parentResult.isCompleted());
                    test.assertTrue(thenResult.isCompleted());
                });
            });

            runner.testGroup("toString()", () ->
            {
                runner.test("with non-completed", (Test test) ->
                {
                    final LazyResult<Integer> result = LazyResult.create(() -> 5);
                    test.assertEqual("{\"type\":\"LazyResult\",\"isCompleted\":false}", result.toString());
                });

                runner.test("with completed value", (Test test) ->
                {
                    final LazyResult<Integer> result = LazyResult.create(() -> 5);
                    result.await();
                    test.assertEqual("{\"type\":\"LazyResult\",\"isCompleted\":true,\"value\":\"5\"}", result.toString());
                });

                runner.test("with completed error", (Test test) ->
                {
                    final LazyResult<Integer> result = LazyResult.create(() -> { throw new EmptyException(); });
                    result.catchError(EmptyException.class).await();
                    test.assertEqual("{\"type\":\"LazyResult\",\"isCompleted\":true,\"error\":\"qub.AwaitException: qub.EmptyException\"}", result.toString());
                });
            });
        });
    }
}
