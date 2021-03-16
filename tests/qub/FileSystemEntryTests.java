package qub;

public interface FileSystemEntryTests
{
    static void test(TestRunner runner, Function0<FileSystemEntry> creator)
    {
        PreCondition.assertNotNull(runner, "runner");
        PreCondition.assertNotNull(creator, "creator");

        runner.testGroup(FileSystemEntry.class, () ->
        {
            runner.test("getFileSystem()", (Test test) ->
            {
                final FileSystemEntry entry = creator.run();
                final FileSystem fileSystem = entry.getFileSystem();
                test.assertNotNull(fileSystem);
                test.assertSame(fileSystem, entry.getFileSystem());
            });

            runner.test("getName()", (Test test) ->
            {
                final FileSystemEntry entry = creator.run();
                final String name = entry.getName();
                test.assertNotNullAndNotEmpty(name);
                test.assertEqual(name, entry.getName());
            });

            runner.test("getPath()", (Test test) ->
            {
                final FileSystemEntry entry = creator.run();
                final Path path = entry.getPath();
                test.assertNotNull(path);
                test.assertEqual(path, entry.getPath());
            });

            runner.testGroup("isDescendantOf(String)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final FileSystemEntry entry = creator.run();
                    test.assertThrows(() -> entry.isDescendantOf((String)null),
                        new PreConditionFailure("possibleAncestorPathString cannot be null."));
                });

                runner.test("with empty", (Test test) ->
                {
                    final FileSystemEntry entry = creator.run();
                    test.assertThrows(() -> entry.isDescendantOf(""),
                        new PreConditionFailure("possibleAncestorPathString cannot be empty."));
                });

                runner.test("with relative path string", (Test test) ->
                {
                    final FileSystemEntry entry = creator.run();
                    test.assertThrows(() -> entry.isDescendantOf("hello"),
                        new PreConditionFailure("possibleAncestorPath.isRooted() cannot be false."));
                });

                runner.test("with non-ancestor path", (Test test) ->
                {
                    final FileSystemEntry entry = creator.run();
                    test.assertFalse(entry.isDescendantOf("/not/an/ancestor/").await());
                });

                runner.test("with ancestor path", (Test test) ->
                {
                    final FileSystemEntry entry = creator.run();
                    final Folder parentFolder = entry.getParentFolder().await();
                    test.assertTrue(entry.isDescendantOf(parentFolder.toString()).await());
                });
            });

            runner.testGroup("isDescendantOf(Path)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final FileSystemEntry entry = creator.run();
                    test.assertThrows(() -> entry.isDescendantOf((Path)null),
                        new PreConditionFailure("possibleAncestorPath cannot be null."));
                });

                runner.test("with relative path string", (Test test) ->
                {
                    final FileSystemEntry entry = creator.run();
                    test.assertThrows(() -> entry.isDescendantOf(Path.parse("hello")),
                        new PreConditionFailure("possibleAncestorPath.isRooted() cannot be false."));
                });

                runner.test("with non-ancestor path", (Test test) ->
                {
                    final FileSystemEntry entry = creator.run();
                    test.assertFalse(entry.isDescendantOf(Path.parse("/not/an/ancestor/")).await());
                });

                runner.test("with ancestor path", (Test test) ->
                {
                    final FileSystemEntry entry = creator.run();
                    final Folder parentFolder = entry.getParentFolder().await();
                    test.assertTrue(entry.isDescendantOf(parentFolder.getPath()).await());
                });
            });

            runner.testGroup("isDescendantOf(Folder)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final FileSystemEntry entry = creator.run();
                    test.assertThrows(() -> entry.isDescendantOf((Folder)null),
                        new PreConditionFailure("possibleAncestorFolder cannot be null."));
                });

                runner.test("with non-ancestor path", (Test test) ->
                {
                    final FileSystemEntry entry = creator.run();
                    final FileSystem fileSystem = entry.getFileSystem();
                    final Folder folder = fileSystem.getFolder("/not/an/ancestor/").await();
                    test.assertFalse(entry.isDescendantOf(folder).await());
                });

                runner.test("with ancestor path", (Test test) ->
                {
                    final FileSystemEntry entry = creator.run();
                    final Folder parentFolder = entry.getParentFolder().await();
                    test.assertTrue(entry.isDescendantOf(parentFolder).await());
                });
            });

            runner.testGroup("isDescendantOf(Root)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final FileSystemEntry entry = creator.run();
                    test.assertThrows(() -> entry.isDescendantOf((Root)null),
                        new PreConditionFailure("possibleAncestorRoot cannot be null."));
                });

                runner.test("with non-ancestor path", (Test test) ->
                {
                    final FileSystemEntry entry = creator.run();
                    final FileSystem fileSystem = entry.getFileSystem();
                    final Root root = fileSystem.getRoot("Z:/").await();
                    test.assertFalse(entry.isDescendantOf(root).await());
                });

                runner.test("with ancestor path", (Test test) ->
                {
                    final FileSystemEntry entry = creator.run();
                    final Root root = entry.getRoot().await();
                    test.assertTrue(entry.isDescendantOf(root).await());
                });
            });

            runner.testGroup("relativeTo(String)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final FileSystemEntry entry = creator.run();
                    test.assertThrows(() -> entry.relativeTo((String)null),
                        new PreConditionFailure("basePath cannot be null."));
                });

                runner.test("with empty", (Test test) ->
                {
                    final FileSystemEntry entry = creator.run();
                    test.assertThrows(() -> entry.relativeTo(""),
                        new PreConditionFailure("basePath cannot be empty."));
                });

                runner.test("with equal path", (Test test) ->
                {
                    final FileSystemEntry entry = creator.run();
                    test.assertThrows(() -> entry.relativeTo(entry.getPath()),
                        new PreConditionFailure("basePath (" + entry + ") must not be " + entry + "."));
                });
            });

            runner.testGroup("relativeTo(Path)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final FileSystemEntry entry = creator.run();
                    test.assertThrows(() -> entry.relativeTo((Path)null),
                        new PreConditionFailure("basePath cannot be null."));
                });

                runner.test("with equal path", (Test test) ->
                {
                    final FileSystemEntry entry = creator.run();
                    test.assertThrows(() -> entry.relativeTo(entry.getPath()),
                        new PreConditionFailure("basePath (" + entry + ") must not be " + entry + "."));
                });
            });

            runner.testGroup("relativeTo(Folder)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final FileSystemEntry entry = creator.run();
                    test.assertThrows(() -> entry.relativeTo((Folder)null),
                        new PreConditionFailure("folder cannot be null."));
                });
            });

            runner.testGroup("relativeTo(Root)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final FileSystemEntry entry = creator.run();
                    test.assertThrows(() -> entry.relativeTo((Root)null),
                        new PreConditionFailure("root cannot be null."));
                });
            });
        });
    }
}
