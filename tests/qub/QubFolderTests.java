package qub;

public interface QubFolderTests
{
    static void test(TestRunner runner)
    {
        PreCondition.assertNotNull(runner, "runner");

        runner.testGroup(QubFolder.class, () ->
        {
            runner.testGroup("constructor(Folder)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> new QubFolder(null),
                        new PreConditionFailure("folder cannot be null."));
                });

                runner.test("with folder that doesn't exist", (Test test) ->
                {
                    final Folder folder = QubFolderTests.getFolder(test, "/qub/");
                    final QubFolder qubFolder = new QubFolder(folder);
                    test.assertEqual(folder.getPath(), qubFolder.getPath());
                    test.assertFalse(qubFolder.exists().await());
                });

                runner.test("with folder that exists", (Test test) ->
                {
                    final Folder folder = QubFolderTests.createFolder(test, "/qub/");
                    final QubFolder qubFolder = new QubFolder(folder);
                    test.assertEqual(folder.getPath(), qubFolder.getPath());
                    test.assertTrue(qubFolder.exists().await());
                });
            });

            runner.testGroup("getPublisherFolders()", () ->
            {
                runner.test("with non-existing QubFolder", (Test test) ->
                {
                    final QubFolder qubFolder = QubFolderTests.getQubFolder(test, "/qub/");
                    test.assertEqual(Iterable.create(), qubFolder.getPublisherFolders().await());
                });

                runner.test("with existing empty QubFolder", (Test test) ->
                {
                    final QubFolder qubFolder = QubFolderTests.createQubFolder(test, "/qub/");
                    test.assertEqual(Iterable.create(), qubFolder.getPublisherFolders().await());
                });

                runner.test("with existing QubFolder with files", (Test test) ->
                {
                    final Folder folder = QubFolderTests.createFolder(test, "/qub/");
                    folder.createFile("shortcut.cmd").await();
                    final QubFolder qubFolder = new QubFolder(folder);
                    test.assertEqual(Iterable.create(), qubFolder.getPublisherFolders().await());
                });

                runner.test("with existing QubFolder with folders", (Test test) ->
                {
                    final Folder folder = QubFolderTests.createFolder(test, "/qub/");
                    final Folder subFolder = folder.createFolder("publisher").await();
                    final QubFolder qubFolder = new QubFolder(folder);
                    test.assertEqual(
                        Iterable.create(
                            new QubPublisherFolder(subFolder)),
                        qubFolder.getPublisherFolders().await());
                });
            });

            runner.testGroup("getPublisherFolder(String)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final QubFolder qubFolder = QubFolderTests.getQubFolder(test, "/qub/");
                    test.assertThrows(() -> qubFolder.getPublisherFolder(null),
                        new PreConditionFailure("publisherName cannot be null."));
                });

                runner.test("with empty", (Test test) ->
                {
                    final QubFolder qubFolder = QubFolderTests.getQubFolder(test, "/qub/");
                    test.assertThrows(() -> qubFolder.getPublisherFolder(""),
                        new PreConditionFailure("publisherName cannot be empty."));
                });

                runner.test("with non-existing publisher", (Test test) ->
                {
                    final QubFolder qubFolder = QubFolderTests.getQubFolder(test, "/qub/");
                    final QubPublisherFolder qubPublisherFolder = qubFolder.getPublisherFolder("spam").await();
                    test.assertNotNull(qubPublisherFolder);
                    test.assertEqual(Path.parse("/qub/spam"), qubPublisherFolder.getPath());
                });

                runner.test("with existing publisher", (Test test) ->
                {
                    final Folder folder = QubFolderTests.createFolder(test, "/qub/");
                    final QubFolder qubFolder = new QubFolder(folder);

                    folder.createFolder("spam").await();
                    final QubPublisherFolder qubPublisherFolder = qubFolder.getPublisherFolder("spam").await();
                    test.assertNotNull(qubPublisherFolder);
                    test.assertEqual(Path.parse("/qub/spam"), qubPublisherFolder.getPath());
                });
            });

            runner.testGroup("getProjectFolders(String)", () ->
            {
                runner.test("with null publisherName", (Test test) ->
                {
                    final QubFolder qubFolder = QubFolderTests.getQubFolder(test, "/qub/");
                    test.assertThrows(() -> qubFolder.getProjectFolders(null),
                        new PreConditionFailure("publisherName cannot be null."));
                });

                runner.test("with empty publisherName", (Test test) ->
                {
                    final QubFolder qubFolder = QubFolderTests.getQubFolder(test, "/qub/");
                    test.assertThrows(() -> qubFolder.getProjectFolders(""),
                        new PreConditionFailure("publisherName cannot be empty."));
                });

                runner.test("with non-existing QubFolder", (Test test) ->
                {
                    final QubFolder qubFolder = QubFolderTests.getQubFolder(test, "/qub/");
                    test.assertEqual(Iterable.create(), qubFolder.getProjectFolders("me").await());
                });

                runner.test("with existing empty QubFolder", (Test test) ->
                {
                    final QubFolder qubFolder = QubFolderTests.createQubFolder(test, "/qub/");
                    test.assertEqual(Iterable.create(), qubFolder.getProjectFolders("me").await());
                });

                runner.test("with existing QubFolder with files", (Test test) ->
                {
                    final Folder folder = QubFolderTests.createFolder(test, "/qub/");
                    folder.createFile("shortcut.cmd").await();
                    final QubFolder qubFolder = new QubFolder(folder);
                    test.assertEqual(Iterable.create(), qubFolder.getProjectFolders("me").await());
                });

                runner.test("with existing empty QubPublisherFolder", (Test test) ->
                {
                    final Folder folder = QubFolderTests.createFolder(test, "/qub/");
                    folder.createFolder("me").await();
                    final QubFolder qubFolder = new QubFolder(folder);
                    test.assertEqual(Iterable.create(), qubFolder.getProjectFolders("me").await());
                });

                runner.test("with existing QubProjectFolder", (Test test) ->
                {
                    final Folder folder = QubFolderTests.createFolder(test, "/qub/");
                    final Folder subFolder = folder.createFolder("me/my-project/").await();
                    final QubFolder qubFolder = new QubFolder(folder);
                    test.assertEqual(
                        Iterable.create(
                            new QubProjectFolder(subFolder)),
                        qubFolder.getProjectFolders("me").await());
                });
            });

            runner.testGroup("getProjectFolder(String)", () ->
            {
                runner.test("with null publisherName", (Test test) ->
                {
                    final QubFolder qubFolder = QubFolderTests.getQubFolder(test, "/qub/");
                    test.assertThrows(() -> qubFolder.getProjectFolder(null, "my-project"),
                        new PreConditionFailure("publisherName cannot be null."));
                });

                runner.test("with empty publisherName", (Test test) ->
                {
                    final QubFolder qubFolder = QubFolderTests.getQubFolder(test, "/qub/");
                    test.assertThrows(() -> qubFolder.getProjectFolder("", "my-project"),
                        new PreConditionFailure("publisherName cannot be empty."));
                });

                runner.test("with null projectName", (Test test) ->
                {
                    final QubFolder qubFolder = QubFolderTests.getQubFolder(test, "/qub/");
                    test.assertThrows(() -> qubFolder.getProjectFolder("me", null),
                        new PreConditionFailure("projectName cannot be null."));
                });

                runner.test("with empty projectName", (Test test) ->
                {
                    final QubFolder qubFolder = QubFolderTests.getQubFolder(test, "/qub/");
                    test.assertThrows(() -> qubFolder.getProjectFolder("me", ""),
                        new PreConditionFailure("projectName cannot be empty."));
                });

                runner.test("with non-existing publisher", (Test test) ->
                {
                    final QubFolder qubFolder = QubFolderTests.getQubFolder(test, "/qub/");
                    final QubProjectFolder projectFolder = qubFolder.getProjectFolder("spam", "my-project").await();
                    test.assertNotNull(projectFolder);
                    test.assertEqual(Path.parse("/qub/spam/my-project"), projectFolder.getPath());
                });

                runner.test("with non-existing project", (Test test) ->
                {
                    final Folder folder = QubFolderTests.createFolder(test, "/qub/");
                    final QubFolder qubFolder = new QubFolder(folder);

                    folder.createFolder("spam").await();
                    final QubProjectFolder projectFolder = qubFolder.getProjectFolder("spam", "my-project").await();
                    test.assertNotNull(projectFolder);
                    test.assertEqual(Path.parse("/qub/spam/my-project"), projectFolder.getPath());
                });

                runner.test("with existing project", (Test test) ->
                {
                    final Folder folder = QubFolderTests.createFolder(test, "/qub/");
                    final QubFolder qubFolder = new QubFolder(folder);

                    folder.createFolder("spam/my-project/").await();
                    final QubProjectFolder projectFolder = qubFolder.getProjectFolder("spam", "my-project").await();
                    test.assertNotNull(projectFolder);
                    test.assertEqual(Path.parse("/qub/spam/my-project"), projectFolder.getPath());
                });
            });

            runner.testGroup("equals(Object)", () ->
            {
                runner.test("with /qub/ and null", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = QubFolderTests.createFileSystem(test);
                    final Folder folder = fileSystem.getFolder("/qub/").await();
                    final QubFolder qubFolder = new QubFolder(folder);
                    test.assertEqual(false, qubFolder.equals((Object)null));
                });

                runner.test("with /qub/ and \"hello world\"", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = QubFolderTests.createFileSystem(test);
                    final Folder folder = fileSystem.getFolder("/qub/").await();
                    final QubFolder qubFolder = new QubFolder(folder);
                    test.assertEqual(false, qubFolder.equals((Object)"hello world"));
                });

                runner.test("with /qub/ and /other/", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = QubFolderTests.createFileSystem(test);

                    final Folder folder = fileSystem.getFolder("/qub/").await();
                    final QubFolder qubFolder = new QubFolder(folder);

                    final Folder folder2 = fileSystem.getFolder("/other/").await();
                    final QubFolder qubFolder2 = new QubFolder(folder2);

                    test.assertEqual(false, qubFolder.equals((Object)qubFolder2));
                });

                runner.test("with /qub/ and /qub/", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = QubFolderTests.createFileSystem(test);

                    final Folder folder = fileSystem.getFolder("/qub/").await();
                    final QubFolder qubFolder = new QubFolder(folder);

                    final Folder folder2 = fileSystem.getFolder("/qub/").await();
                    final QubFolder qubFolder2 = new QubFolder(folder2);

                    test.assertEqual(true, qubFolder.equals((Object)qubFolder2));
                });
            });

            runner.testGroup("equals(QubFolder)", () ->
            {
                runner.test("with /qub/ and null", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = QubFolderTests.createFileSystem(test);
                    final Folder folder = fileSystem.getFolder("/qub/").await();
                    final QubFolder qubFolder = new QubFolder(folder);
                    test.assertEqual(false, qubFolder.equals((QubFolder)null));
                });

                runner.test("with /qub/ and /other/", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = QubFolderTests.createFileSystem(test);

                    final Folder folder = fileSystem.getFolder("/qub/").await();
                    final QubFolder qubFolder = new QubFolder(folder);

                    final Folder folder2 = fileSystem.getFolder("/other/").await();
                    final QubFolder qubFolder2 = new QubFolder(folder2);

                    test.assertEqual(false, qubFolder.equals((QubFolder)qubFolder2));
                });

                runner.test("with /qub/ and /qub/", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = QubFolderTests.createFileSystem(test);

                    final Folder folder = fileSystem.getFolder("/qub/").await();
                    final QubFolder qubFolder = new QubFolder(folder);

                    final Folder folder2 = fileSystem.getFolder("/qub/").await();
                    final QubFolder qubFolder2 = new QubFolder(folder2);

                    test.assertEqual(true, qubFolder.equals((QubFolder)qubFolder2));
                });
            });

            runner.test("toString()", (Test test) ->
            {
                final QubFolder qubFolder = QubFolderTests.getQubFolder(test, "/qub/");
                test.assertEqual("/qub/", qubFolder.toString());
            });
        });
    }

    static InMemoryFileSystem createFileSystem(Test test)
    {
        final InMemoryFileSystem fileSystem = new InMemoryFileSystem(test.getClock());
        fileSystem.createRoot("/").await();
        return fileSystem;
    }

    static Folder getFolder(Test test, String folderPath)
    {
        PreCondition.assertNotNull(test, "test");
        PreCondition.assertNotNullAndNotEmpty(folderPath, "folderPath");

        final InMemoryFileSystem fileSystem = QubFolderTests.createFileSystem(test);
        return fileSystem.getFolder(folderPath).await();
    }

    static Folder createFolder(Test test, String folderPath)
    {
        PreCondition.assertNotNull(test, "test");
        PreCondition.assertNotNullAndNotEmpty(folderPath, "folderPath");

        final InMemoryFileSystem fileSystem = QubFolderTests.createFileSystem(test);
        return fileSystem.createFolder(folderPath).await();
    }

    static QubFolder getQubFolder(Test test, String folderPath)
    {
        return new QubFolder(QubFolderTests.getFolder(test, folderPath));
    }

    static QubFolder createQubFolder(Test test, String folderPath)
    {
        return new QubFolder(QubFolderTests.createFolder(test, folderPath));
    }
}
