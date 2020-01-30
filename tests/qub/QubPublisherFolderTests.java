package qub;

public interface QubPublisherFolderTests
{
    static void test(TestRunner runner)
    {
        PreCondition.assertNotNull(runner, "runner");

        runner.testGroup(QubPublisherFolder.class, () ->
        {
            runner.testGroup("constructor(Folder)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> QubPublisherFolder.create(null),
                        new PreConditionFailure("publisherFolder cannot be null."));
                });

                runner.test("with root folder", (Test test) ->
                {
                    final Folder folder = QubPublisherFolderTests.getFolder(test, "/");
                    test.assertThrows(() -> QubPublisherFolder.create(folder),
                        new PreConditionFailure("publisherFolder.getPath().getSegments().getCount() (1) must be greater than or equal to 2."));
                });

                runner.test("with folder that doesn't exist", (Test test) ->
                {
                    final Folder folder = QubPublisherFolderTests.getFolder(test, "/qub/me/");
                    final QubPublisherFolder publisherFolder = QubPublisherFolder.create(folder);
                    test.assertEqual(folder.getPath(), publisherFolder.getPath());
                    test.assertFalse(publisherFolder.exists().await());
                });

                runner.test("with folder that exists", (Test test) ->
                {
                    final Folder folder = QubPublisherFolderTests.createFolder(test, "/qub/me/");
                    final QubPublisherFolder publisherFolder = QubPublisherFolder.create(folder);
                    test.assertEqual(folder.getPath(), publisherFolder.getPath());
                    test.assertTrue(publisherFolder.exists().await());
                });
            });
            
            runner.testGroup("getProjectFolders()", () ->
            {
                runner.test("with non-existing QubPublisherFolder", (Test test) ->
                {
                    final QubPublisherFolder publisherFolder = QubPublisherFolderTests.getQubPublisherFolder(test, "/qub/me/");
                    test.assertEqual(Iterable.create(), publisherFolder.getProjectFolders().await());
                });

                runner.test("with existing empty QubPublisherFolder", (Test test) ->
                {
                    final QubPublisherFolder publisherFolder = QubPublisherFolderTests.createQubPublisherFolder(test, "/qub/me/");
                    test.assertEqual(Iterable.create(), publisherFolder.getProjectFolders().await());
                });

                runner.test("with existing QubPublisherFolder with files", (Test test) ->
                {
                    final Folder folder = QubPublisherFolderTests.createFolder(test, "/qub/me/");
                    folder.createFile("shortcut.cmd").await();
                    final QubPublisherFolder publisherFolder = QubPublisherFolder.create(folder);
                    test.assertEqual(Iterable.create(), publisherFolder.getProjectFolders().await());
                });

                runner.test("with existing QubPublisherFolder with folders", (Test test) ->
                {
                    final Folder folder = QubPublisherFolderTests.createFolder(test, "/qub/me/");
                    final Folder subFolder = folder.createFolder("project").await();
                    final QubPublisherFolder publisherFolder = QubPublisherFolder.create(folder);
                    test.assertEqual(
                        Iterable.create(
                            QubProjectFolder.create(subFolder)),
                        publisherFolder.getProjectFolders().await());
                });
            });
            
            runner.testGroup("getProjectFolder(String)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final QubPublisherFolder publisherFolder = QubPublisherFolderTests.getQubPublisherFolder(test, "/qub/me/");
                    test.assertThrows(() -> publisherFolder.getProjectFolder(null),
                        new PreConditionFailure("projectName cannot be null."));
                });

                runner.test("with empty", (Test test) ->
                {
                    final QubPublisherFolder publisherFolder = QubPublisherFolderTests.getQubPublisherFolder(test, "/qub/me/");
                    test.assertThrows(() -> publisherFolder.getProjectFolder(""),
                        new PreConditionFailure("projectName cannot be empty."));
                });

                runner.test("with non-existing publisher", (Test test) ->
                {
                    final QubPublisherFolder publisherFolder = QubPublisherFolderTests.getQubPublisherFolder(test, "/qub/me/");
                    final QubProjectFolder projectFolder = publisherFolder.getProjectFolder("spam").await();
                    test.assertNotNull(projectFolder);
                    test.assertEqual(Path.parse("/qub/me/spam"), projectFolder.getPath());
                });

                runner.test("with existing publisher", (Test test) ->
                {
                    final Folder folder = QubPublisherFolderTests.createFolder(test, "/qub/me/");
                    final QubPublisherFolder publisherFolder = QubPublisherFolder.create(folder);

                    folder.createFolder("spam").await();
                    final QubProjectFolder projectFolder = publisherFolder.getProjectFolder("spam").await();
                    test.assertNotNull(projectFolder);
                    test.assertEqual(Path.parse("/qub/me/spam"), projectFolder.getPath());
                });
            });

            runner.testGroup("equals(Object)", () ->
            {
                runner.test("with /qub/me/ and null", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = QubPublisherFolderTests.createFileSystem(test);
                    final Folder folder = fileSystem.getFolder("/qub/me/").await();
                    final QubPublisherFolder publisherFolder = QubPublisherFolder.create(folder);
                    test.assertEqual(false, publisherFolder.equals((Object)null));
                });

                runner.test("with /qub/me/ and \"hello world\"", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = QubPublisherFolderTests.createFileSystem(test);
                    final Folder folder = fileSystem.getFolder("/qub/me/").await();
                    final QubPublisherFolder publisherFolder = QubPublisherFolder.create(folder);
                    test.assertEqual(false, publisherFolder.equals((Object)"hello world"));
                });

                runner.test("with /qub/me/ and /other/thing/", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = QubPublisherFolderTests.createFileSystem(test);

                    final Folder folder = fileSystem.getFolder("/qub/me/").await();
                    final QubPublisherFolder publisherFolder = QubPublisherFolder.create(folder);

                    final Folder folder2 = fileSystem.getFolder("/other/thing/").await();
                    final QubPublisherFolder publisherFolder2 = QubPublisherFolder.create(folder2);

                    test.assertEqual(false, publisherFolder.equals((Object)publisherFolder2));
                });

                runner.test("with /qub/me/ and /qub/me/", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = QubPublisherFolderTests.createFileSystem(test);

                    final Folder folder = fileSystem.getFolder("/qub/me/").await();
                    final QubPublisherFolder publisherFolder = QubPublisherFolder.create(folder);

                    final Folder folder2 = fileSystem.getFolder("/qub/me/").await();
                    final QubPublisherFolder publisherFolder2 = QubPublisherFolder.create(folder2);

                    test.assertEqual(true, publisherFolder.equals((Object)publisherFolder2));
                });
            });

            runner.testGroup("equals(QubPublisherFolder)", () ->
            {
                runner.test("with /qub/me/ and null", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = QubPublisherFolderTests.createFileSystem(test);
                    final Folder folder = fileSystem.getFolder("/qub/me/").await();
                    final QubPublisherFolder publisherFolder = QubPublisherFolder.create(folder);
                    test.assertEqual(false, publisherFolder.equals((QubPublisherFolder)null));
                });

                runner.test("with /qub/me/ and /other/thing/", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = QubPublisherFolderTests.createFileSystem(test);

                    final Folder folder = fileSystem.getFolder("/qub/me/").await();
                    final QubPublisherFolder publisherFolder = QubPublisherFolder.create(folder);

                    final Folder folder2 = fileSystem.getFolder("/other/thing/").await();
                    final QubPublisherFolder publisherFolder2 = QubPublisherFolder.create(folder2);

                    test.assertEqual(false, publisherFolder.equals((QubPublisherFolder)publisherFolder2));
                });

                runner.test("with /qub/me/ and /qub/me/", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = QubPublisherFolderTests.createFileSystem(test);

                    final Folder folder = fileSystem.getFolder("/qub/me/").await();
                    final QubPublisherFolder publisherFolder = QubPublisherFolder.create(folder);

                    final Folder folder2 = fileSystem.getFolder("/qub/me/").await();
                    final QubPublisherFolder publisherFolder2 = QubPublisherFolder.create(folder2);

                    test.assertEqual(true, publisherFolder.equals((QubPublisherFolder)publisherFolder2));
                });
            });

            runner.test("toString()", (Test test) ->
            {
                final QubPublisherFolder publisherFolder = QubPublisherFolderTests.getQubPublisherFolder(test, "/qub/me/");
                test.assertEqual("/qub/me/", publisherFolder.toString());
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

        final InMemoryFileSystem fileSystem = QubPublisherFolderTests.createFileSystem(test);
        return fileSystem.getFolder(folderPath).await();
    }

    static Folder createFolder(Test test, String folderPath)
    {
        PreCondition.assertNotNull(test, "test");
        PreCondition.assertNotNullAndNotEmpty(folderPath, "folderPath");

        final InMemoryFileSystem fileSystem = QubPublisherFolderTests.createFileSystem(test);
        return fileSystem.createFolder(folderPath).await();
    }
    
    static QubPublisherFolder getQubPublisherFolder(Test test, String folderPath)
    {
        return QubPublisherFolder.create(QubPublisherFolderTests.getFolder(test, folderPath));
    }

    static QubPublisherFolder createQubPublisherFolder(Test test, String folderPath)
    {
        return QubPublisherFolder.create(QubPublisherFolderTests.createFolder(test, folderPath));
    }
}
