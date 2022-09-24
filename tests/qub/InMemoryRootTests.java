package qub;

public interface InMemoryRootTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(InMemoryRoot.class, () ->
        {
            runner.testGroup("create(String,Clock)", () ->
            {
                final Action3<String,Clock,Throwable> createErrorTest = (String name, Clock clock, Throwable expected) ->
                {
                    runner.test("with " + English.andList(Strings.escapeAndQuote(name), (clock == null ? "null" : "non-null") + " clock"), (Test test) ->
                    {
                        test.assertThrows(() -> InMemoryRoot.create(name, clock),
                            expected);
                    });
                };

                createErrorTest.run(null, ManualClock.create(), new PreConditionFailure("name cannot be null."));
                createErrorTest.run("", ManualClock.create(), new PreConditionFailure("name cannot be empty."));
                createErrorTest.run("/", null, new PreConditionFailure("clock cannot be null."));

                final Action2<String,Clock> createTest = (String name, Clock clock) ->
                {
                    runner.test("with " + English.andList(Strings.escapeAndQuote(name), "non-null clock"), (Test test) ->
                    {
                        final InMemoryRoot root = InMemoryRoot.create(name, clock);
                        test.assertNotNull(root);
                        test.assertEqual(name, root.getName());
                        test.assertEqual(name, root.getPath().toString());
                        test.assertNull(root.getTotalDataSize());
                    });
                };

                createTest.run("/", ManualClock.create());
                createTest.run("C:", ManualClock.create());
            });

            runner.testGroup("create(String,Clock,DataSize)", () ->
            {
                final Action4<String,Clock,DataSize,Throwable> createErrorTest = (String name, Clock clock, DataSize totalDataSize, Throwable expected) ->
                {
                    runner.test("with " + English.andList(Strings.escapeAndQuote(name), (clock == null ? "null" : "non-null") + " clock", Objects.toString(totalDataSize)), (Test test) ->
                    {
                        test.assertThrows(() -> InMemoryRoot.create(name, clock, totalDataSize),
                            expected);
                    });
                };

                createErrorTest.run(null, ManualClock.create(), null, new PreConditionFailure("name cannot be null."));
                createErrorTest.run("", ManualClock.create(), null, new PreConditionFailure("name cannot be empty."));
                createErrorTest.run("/", null, null, new PreConditionFailure("clock cannot be null."));
                createErrorTest.run("/", ManualClock.create(), DataSize.bytes(-1), new PreConditionFailure("totalDataSize (-1.0 Bytes) must be null or greater than 0.0 Bytes."));
                createErrorTest.run("/", ManualClock.create(), DataSize.bytes(0), new PreConditionFailure("totalDataSize (0.0 Bytes) must be null or greater than 0.0 Bytes."));

                final Action3<String,Clock,DataSize> createTest = (String name, Clock clock, DataSize totalDataSize) ->
                {
                    runner.test("with " + English.andList(Strings.escapeAndQuote(name), "non-null clock", totalDataSize), (Test test) ->
                    {
                        final InMemoryRoot root = InMemoryRoot.create(name, clock, totalDataSize);
                        test.assertNotNull(root);
                        test.assertEqual(name, root.getName());
                        test.assertEqual(name, root.getPath().toString());
                        test.assertEqual(totalDataSize, root.getTotalDataSize());
                    });
                };

                createTest.run("/", ManualClock.create(), null);
                createTest.run("C:", ManualClock.create(), null);
                createTest.run("C:", ManualClock.create(), DataSize.bytes(128));
            });

            runner.testGroup("getUnusedDataSize()", () ->
            {
                runner.test("with no totalDataSize", (Test test) ->
                {
                    final InMemoryRoot root = InMemoryRoot.create("/", ManualClock.create());
                    test.assertNull(root.getUnusedDataSize());
                });

                runner.test("with null totalDataSize", (Test test) ->
                {
                    final InMemoryRoot root = InMemoryRoot.create("/", ManualClock.create(), null);
                    test.assertNull(root.getUnusedDataSize());
                });

                runner.test("with " + DataSize.bytes(10) + " totalDataSize", (Test test) ->
                {
                    final InMemoryRoot root = InMemoryRoot.create("/", ManualClock.create(), DataSize.bytes(10));
                    test.assertEqual(DataSize.bytes(10), root.getUnusedDataSize());
                });
            });
        });
    }
}
