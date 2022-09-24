package qub;

public interface InMemoryFolderTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(InMemoryFolder.class, () ->
        {
            runner.testGroup("create(String,Clock)", () ->
            {
                final Action3<String,Clock,Throwable> createErrorTest = (String name, Clock clock, Throwable expected) ->
                {
                    runner.test("with " + English.andList(Strings.escapeAndQuote(name), (clock == null ? "null" : "non-null") + " clock"), (Test test) ->
                    {
                        test.assertThrows(() -> InMemoryFolder.create(null, name, clock),
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
                        final InMemoryFolder root = InMemoryFolder.create(null, name, clock);
                        test.assertNotNull(root);
                        test.assertEqual(name, root.getName());
                    });
                };

                createTest.run("/", ManualClock.create());
                createTest.run("C:", ManualClock.create());
            });

            runner.testGroup("canDelete()", () ->
            {
                runner.test("with canDelete set to false", (Test test) ->
                {
                    final InMemoryFolder folder = InMemoryFolder.create(null, "hello", ManualClock.create());
                    folder.setCanDelete(false);

                    test.assertFalse(folder.canDelete());
                });

                runner.test("with canDelete set to true and no child entries", (Test test) ->
                {
                    final InMemoryFolder folder = InMemoryFolder.create(null, "hello", ManualClock.create());
                    folder.setCanDelete(true);

                    test.assertTrue(folder.canDelete());
                });

                runner.test("with child file that can't be deleted", (Test test) ->
                {
                    final InMemoryFolder folder = InMemoryFolder.create(null, "hello", ManualClock.create());
                    final InMemoryFile childFile = folder.createFile("childFile").await();
                    childFile.setCanDelete(false);

                    test.assertFalse(folder.canDelete());
                });
            });

            runner.testGroup("setCanDelete(boolean)", () ->
            {
                final Action1<Boolean> setCanDeleteTest = (Boolean canDelete) ->
                {
                    runner.test("with " + canDelete, (Test test) ->
                    {
                        final InMemoryFolder folder = InMemoryFolder.create(null, "hello", ManualClock.create());

                        folder.setCanDelete(canDelete);

                        test.assertEqual(canDelete, folder.canDelete());
                    });
                };

                setCanDeleteTest.run(false);
                setCanDeleteTest.run(true);
            });
        });
    }
}
