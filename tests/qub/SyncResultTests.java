package qub;

public interface SyncResultTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(SyncResult.class, () ->
        {
            ResultTests.test(runner, SyncResult::create);

            runner.testGroup("create(T)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final SyncResult<Integer> result = SyncResult.create((Integer)null);
                    test.assertNotNull(result, "result");
                    test.assertTrue(result.isCompleted());

                    test.assertNull(result.await());
                    test.assertTrue(result.isCompleted());

                    test.assertNull(result.await());
                    test.assertTrue(result.isCompleted());
                });

                runner.test("with non-null", (Test test) ->
                {
                    final SyncResult<Integer> result = SyncResult.create(20);
                    test.assertNotNull(result, "result");
                    test.assertTrue(result.isCompleted());

                    test.assertEqual(20, result.await());
                    test.assertTrue(result.isCompleted());

                    test.assertEqual(20, result.await());
                    test.assertTrue(result.isCompleted());
                });
            });

            runner.testGroup("create(Action0)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> SyncResult.create((Action0)null),
                        new PreConditionFailure("action cannot be null."));
                });

                runner.test("with non-null and non-throwing", (Test test) ->
                {
                    final SyncResult<Void> result = SyncResult.create(Action0.empty);
                    test.assertNotNull(result, "result");
                    test.assertTrue(result.isCompleted());

                    test.assertNull(result.await());
                    test.assertTrue(result.isCompleted());

                    test.assertNull(result.await());
                    test.assertTrue(result.isCompleted());
                });

                runner.test("with non-null and throwing", (Test test) ->
                {
                    final SyncResult<Void> result = SyncResult.create((Action0)() -> { throw new NotFoundException("hello"); });
                    test.assertNotNull(result, "result");
                    test.assertTrue(result.isCompleted());

                    test.assertThrows(result::await,
                        new NotFoundException("hello"));
                    test.assertTrue(result.isCompleted());

                    test.assertThrows(result::await,
                        new NotFoundException("hello"));
                    test.assertTrue(result.isCompleted());
                });
            });

            runner.testGroup("then(Action0)", () ->
            {
                runner.test("then-result is run immediately", (Test test) ->
                {
                    final IntegerValue parentCounter = IntegerValue.create(0);
                    final SyncResult<Integer> parentResult = SyncResult.create(() ->
                    {
                        parentCounter.increment();
                        return 5;
                    });
                    test.assertTrue(parentResult.isCompleted());
                    test.assertEqual(1, parentCounter.get());

                    final IntegerValue thenCounter = IntegerValue.create(0);
                    final Result<Void> thenResult = parentResult.then(() -> { thenCounter.increment(); });
                    test.assertTrue(parentResult.isCompleted());
                    test.assertEqual(1, parentCounter.get());
                    test.assertTrue(thenResult.isCompleted());
                    test.assertEqual(1, thenCounter.get());

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
                runner.test("then-result is run immediately", (Test test) ->
                {
                    final IntegerValue parentCounter = IntegerValue.create(0);
                    final SyncResult<Integer> parentResult = SyncResult.create(() ->
                    {
                        parentCounter.increment();
                        return 5;
                    });
                    test.assertTrue(parentResult.isCompleted());
                    test.assertEqual(1, parentCounter.get());

                    final IntegerValue thenCounter = IntegerValue.create(0);
                    final Result<Void> thenResult = parentResult.then((Integer parentValue) -> { thenCounter.plusAssign(parentValue); });
                    test.assertTrue(parentResult.isCompleted());
                    test.assertEqual(1, parentCounter.get());
                    test.assertTrue(thenResult.isCompleted());
                    test.assertEqual(5, thenCounter.get());

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
        });
    }
}
