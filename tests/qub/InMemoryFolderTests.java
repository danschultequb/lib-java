package qub;

public interface InMemoryFolderTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(InMemoryFolder.class, () ->
        {
            runner.testGroup("create(String)", () ->
            {
                final Action2<String,Throwable> createErrorTest = (String name, Throwable expected) ->
                {
                    runner.test("with " + English.andList(Strings.escapeAndQuote(name)), (Test test) ->
                    {
                        test.assertThrows(() -> InMemoryFolder.create(null, name),
                            expected);
                    });
                };

                createErrorTest.run(null, new PreConditionFailure("name cannot be null."));
                createErrorTest.run("", new PreConditionFailure("name cannot be empty."));

                final Action1<String> createTest = (String name) ->
                {
                    runner.test("with " + English.andList(Strings.escapeAndQuote(name)), (Test test) ->
                    {
                        final InMemoryFolder root = InMemoryFolder.create(null, name);
                        test.assertNotNull(root);
                        test.assertEqual(name, root.getName());
                    });
                };

                createTest.run("/");
                createTest.run("C:");
            });

            runner.testGroup("canDelete()", () ->
            {
                runner.test("with canDelete set to false", (Test test) ->
                {
                    final InMemoryFolder folder = InMemoryFolder.create(null, "hello");
                    folder.setCanDelete(false);

                    test.assertFalse(folder.canDelete());
                });

                runner.test("with canDelete set to true and no child entries", (Test test) ->
                {
                    final InMemoryFolder folder = InMemoryFolder.create(null, "hello");
                    folder.setCanDelete(true);

                    test.assertTrue(folder.canDelete());
                });

                runner.test("with child file that can't be deleted", (Test test) ->
                {
                    final InMemoryRoot root = InMemoryRoot.create("/", ManualClock.create());
                    final InMemoryFolder folder = InMemoryFolder.create(root, "hello");
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
                        final InMemoryFolder folder = InMemoryFolder.create(null, "hello");

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
