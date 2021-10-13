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
                    test.assertThrows(() -> QubPublisherFolder.get(null),
                        new PreConditionFailure("publisherFolder cannot be null."));
                });

                runner.test("with root folder", (Test test) ->
                {
                    final Folder folder = QubPublisherFolderTests.getFolder(test, "/");
                    test.assertThrows(() -> QubPublisherFolder.get(folder),
                        new PreConditionFailure("publisherFolder.getPath().getSegments().getCount() (1) must be greater than or equal to 2."));
                });

                runner.test("with folder that doesn't exist", (Test test) ->
                {
                    final Folder folder = QubPublisherFolderTests.getFolder(test, "/qub/me/");
                    final QubPublisherFolder publisherFolder = QubPublisherFolder.get(folder);
                    test.assertEqual(folder.getPath(), publisherFolder.getPath());
                    test.assertFalse(publisherFolder.exists().await());
                });

                runner.test("with folder that exists", (Test test) ->
                {
                    final Folder folder = QubPublisherFolderTests.createFolder(test, "/qub/me/");
                    final QubPublisherFolder publisherFolder = QubPublisherFolder.get(folder);
                    test.assertEqual(folder.getPath(), publisherFolder.getPath());
                    test.assertTrue(publisherFolder.exists().await());
                });
            });

            runner.testGroup("iterateProjectFolders()", () ->
            {
                runner.test("with non-existing QubPublisherFolder", (Test test) ->
                {
                    final QubPublisherFolder publisherFolder = QubPublisherFolderTests.getQubPublisherFolder(test, "/qub/me/");
                    test.assertEqual(Iterable.create(), publisherFolder.iterateProjectFolders().toList());
                });

                runner.test("with existing empty QubPublisherFolder", (Test test) ->
                {
                    final QubPublisherFolder publisherFolder = QubPublisherFolderTests.createQubPublisherFolder(test, "/qub/me/");
                    test.assertEqual(Iterable.create(), publisherFolder.iterateProjectFolders().toList());
                });

                runner.test("with existing QubPublisherFolder with files", (Test test) ->
                {
                    final Folder folder = QubPublisherFolderTests.createFolder(test, "/qub/me/");
                    folder.createFile("shortcut.cmd").await();
                    final QubPublisherFolder publisherFolder = QubPublisherFolder.get(folder);
                    test.assertEqual(Iterable.create(), publisherFolder.iterateProjectFolders().toList());
                });

                runner.test("with existing QubPublisherFolder with folders", (Test test) ->
                {
                    final Folder folder = QubPublisherFolderTests.createFolder(test, "/qub/me/");
                    final Folder subFolder = folder.createFolder("project").await();
                    final QubPublisherFolder publisherFolder = QubPublisherFolder.get(folder);
                    test.assertEqual(
                        Iterable.create(
                            QubProjectFolder.get(subFolder)),
                        publisherFolder.iterateProjectFolders().toList());
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
                    test.assertEqual(Path.parse("/qub/me/spam/"), projectFolder.getPath());
                });

                runner.test("with existing publisher", (Test test) ->
                {
                    final Folder folder = QubPublisherFolderTests.createFolder(test, "/qub/me/");
                    final QubPublisherFolder publisherFolder = QubPublisherFolder.get(folder);

                    folder.createFolder("spam").await();
                    final QubProjectFolder projectFolder = publisherFolder.getProjectFolder("spam").await();
                    test.assertNotNull(projectFolder);
                    test.assertEqual(Path.parse("/qub/me/spam/"), projectFolder.getPath());
                });
            });

            runner.testGroup("iterateProjectVersionFolders(String,String)", () ->
            {
                final Action3<String,String,Throwable> iterateProjectVersionFoldersErrorTest = (String publisherName, String projectName, Throwable expected) ->
                {
                    runner.test("with " + English.andList(Iterable.create(publisherName, projectName).map(Strings::escapeAndQuote)), (Test test) ->
                    {
                        final QubPublisherFolder publisherFolder = QubPublisherFolderTests.getQubPublisherFolder(test, "/qub/" + publisherName + "/");
                        test.assertThrows(() -> publisherFolder.iterateProjectVersionFolders(projectName), expected);
                    });
                };

                iterateProjectVersionFoldersErrorTest.run("a", null, new PreConditionFailure("projectName cannot be null."));
                iterateProjectVersionFoldersErrorTest.run("a", "", new PreConditionFailure("projectName cannot be empty."));

                runner.test("with non-existing Qub folder", (Test test) ->
                {
                    final String publisherName = "a";
                    final String projectName = "b";
                    final QubPublisherFolder publisherFolder = QubPublisherFolderTests.getQubPublisherFolder(test, "/qub/" + publisherName + "/");
                    test.assertEqual(Iterable.create(), publisherFolder.iterateProjectVersionFolders(projectName).toList());
                });

                runner.test("with non-existing publisher folder", (Test test) ->
                {
                    final String publisherName = "a";
                    final String projectName = "b";
                    final QubPublisherFolder publisherFolder = QubPublisherFolderTests.getQubPublisherFolder(test, "/qub/" + publisherName + "/");
                    publisherFolder.getQubFolder().await().create().await();
                    test.assertEqual(Iterable.create(), publisherFolder.iterateProjectVersionFolders(projectName).toList());
                });

                runner.test("with non-existing project folder", (Test test) ->
                {
                    final String publisherName = "a";
                    final String projectName = "b";
                    final QubPublisherFolder publisherFolder = QubPublisherFolderTests.getQubPublisherFolder(test, "/qub/" + publisherName + "/");
                    publisherFolder.create().await();
                    test.assertEqual(Iterable.create(), publisherFolder.iterateProjectVersionFolders(projectName).toList());
                });

                runner.test("with empty project folder", (Test test) ->
                {
                    final String publisherName = "a";
                    final String projectName = "b";
                    final QubPublisherFolder publisherFolder = QubPublisherFolderTests.getQubPublisherFolder(test, "/qub/" + publisherName + "/");
                    publisherFolder.getQubFolder().await().create().await();
                    final QubProjectFolder projectFolder = publisherFolder.getProjectFolder(projectName).await();
                    projectFolder.create().await();
                    test.assertEqual(Iterable.create(), publisherFolder.iterateProjectVersionFolders(projectName).toList());
                });

                runner.test("with one version folder", (Test test) ->
                {
                    final String publisherName = "a";
                    final String projectName = "b";
                    final QubPublisherFolder publisherFolder = QubPublisherFolderTests.getQubPublisherFolder(test, "/qub/" + publisherName + "/");
                    final QubProjectFolder projectFolder = publisherFolder.getProjectFolder(projectName).await();
                    projectFolder.createFolder("versions/1").await();
                    test.assertEqual(
                        Iterable.create(
                            publisherFolder.getProjectVersionFolder(projectName, "1").await()),
                        publisherFolder.iterateProjectVersionFolders(projectName).toList());
                });
            });

            runner.testGroup("getLatestProjectVersionFolder()", () ->
            {
                runner.test("with no existing Qub folder", (Test test) ->
                {
                    final String publisherName = "a";
                    final String projectName = "b";
                    final QubPublisherFolder publisherFolder = QubPublisherFolderTests.getQubPublisherFolder(test, "/qub/" + publisherName + "/");
                    test.assertThrows(() -> publisherFolder.getLatestProjectVersionFolder(projectName).await(),
                        new NotFoundException("No project named a/b has been published."));
                });

                runner.test("with no existing publisher folder", (Test test) ->
                {
                    final String publisherName = "a";
                    final String projectName = "b";
                    final QubPublisherFolder publisherFolder = QubPublisherFolderTests.getQubPublisherFolder(test, "/qub/" + publisherName + "/");
                    publisherFolder.getQubFolder().await().create().await();
                    test.assertThrows(() -> publisherFolder.getLatestProjectVersionFolder(projectName).await(),
                        new NotFoundException("No project named a/b has been published."));
                });

                runner.test("with no existing project folder", (Test test) ->
                {
                    final String publisherName = "a";
                    final String projectName = "b";
                    final QubPublisherFolder publisherFolder = QubPublisherFolderTests.getQubPublisherFolder(test, "/qub/" + publisherName + "/");
                    publisherFolder.create().await();
                    test.assertThrows(() -> publisherFolder.getLatestProjectVersionFolder(projectName).await(),
                        new NotFoundException("No project named a/b has been published."));
                });

                runner.test("with no existing versions folder", (Test test) ->
                {
                    final String publisherName = "a";
                    final String projectName = "b";
                    final QubPublisherFolder publisherFolder = QubPublisherFolderTests.getQubPublisherFolder(test, "/qub/" + publisherName + "/");
                    publisherFolder.getProjectFolder(projectName).await().create().await();
                    test.assertThrows(() -> publisherFolder.getLatestProjectVersionFolder(projectName).await(),
                        new NotFoundException("No project named a/b has been published."));
                });

                runner.test("with no existing version folders", (Test test) ->
                {
                    final String publisherName = "a";
                    final String projectName = "b";
                    final QubPublisherFolder publisherFolder = QubPublisherFolderTests.getQubPublisherFolder(test, "/qub/" + publisherName + "/");
                    publisherFolder.getProjectVersionsFolder(projectName).await().create().await();
                    test.assertThrows(() -> publisherFolder.getLatestProjectVersionFolder(projectName).await(),
                        new NotFoundException("No project named a/b has been published."));
                });

                runner.test("with one version", (Test test) ->
                {
                    final String publisherName = "a";
                    final String projectName = "b";
                    final QubPublisherFolder publisherFolder = QubPublisherFolderTests.getQubPublisherFolder(test, "/qub/" + publisherName + "/");
                    publisherFolder.getProjectVersionFolder(projectName, "1").await().create().await();
                    test.assertEqual("/qub/" + publisherName + "/" + projectName + "/versions/1/", publisherFolder.getLatestProjectVersionFolder(projectName).await().toString());
                });

                runner.test("with two versions", (Test test) ->
                {
                    final String publisherName = "a";
                    final String projectName = "b";
                    final QubPublisherFolder publisherFolder = QubPublisherFolderTests.getQubPublisherFolder(test, "/qub/" + publisherName + "/");
                    publisherFolder.getProjectVersionFolder(projectName, "2").await().create().await();
                    publisherFolder.getProjectVersionFolder(projectName, "3").await().create().await();
                    test.assertEqual("/qub/" + publisherName + "/" + projectName + "/versions/3/", publisherFolder.getLatestProjectVersionFolder(projectName).await().toString());
                });

                runner.test("with three non-sorted versions", (Test test) ->
                {
                    final String publisherName = "a";
                    final String projectName = "b";
                    final QubPublisherFolder publisherFolder = QubPublisherFolderTests.getQubPublisherFolder(test, "/qub/" + publisherName + "/");
                    publisherFolder.getProjectVersionFolder(projectName, "2").await().create().await();
                    publisherFolder.getProjectVersionFolder(projectName, "30").await().create().await();
                    publisherFolder.getProjectVersionFolder(projectName, "13").await().create().await();
                    test.assertEqual("/qub/" + publisherName + "/" + projectName + "/versions/30/", publisherFolder.getLatestProjectVersionFolder(projectName).await().toString());
                });

                runner.test("with non-integer versions", (Test test) ->
                {
                    final String publisherName = "a";
                    final String projectName = "b";
                    final QubPublisherFolder publisherFolder = QubPublisherFolderTests.getQubPublisherFolder(test, "/qub/" + publisherName + "/");
                    publisherFolder.getProjectVersionFolder(projectName, "2").await().create().await();
                    publisherFolder.getProjectVersionFolder(projectName, "3.0.3").await().create().await();
                    publisherFolder.getProjectVersionFolder(projectName, "1.3").await().create().await();
                    test.assertEqual("/qub/" + publisherName + "/" + projectName + "/versions/3.0.3/", publisherFolder.getLatestProjectVersionFolder(projectName).await().toString());
                });
            });

            runner.testGroup("equals(Object)", () ->
            {
                runner.test("with /qub/me/ and null", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = QubPublisherFolderTests.createFileSystem(test);
                    final Folder folder = fileSystem.getFolder("/qub/me/").await();
                    final QubPublisherFolder publisherFolder = QubPublisherFolder.get(folder);
                    test.assertEqual(false, publisherFolder.equals((Object)null));
                });

                runner.test("with /qub/me/ and \"hello world\"", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = QubPublisherFolderTests.createFileSystem(test);
                    final Folder folder = fileSystem.getFolder("/qub/me/").await();
                    final QubPublisherFolder publisherFolder = QubPublisherFolder.get(folder);
                    test.assertEqual(false, publisherFolder.equals((Object)"hello world"));
                });

                runner.test("with /qub/me/ and /other/thing/", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = QubPublisherFolderTests.createFileSystem(test);

                    final Folder folder = fileSystem.getFolder("/qub/me/").await();
                    final QubPublisherFolder publisherFolder = QubPublisherFolder.get(folder);

                    final Folder folder2 = fileSystem.getFolder("/other/thing/").await();
                    final QubPublisherFolder publisherFolder2 = QubPublisherFolder.get(folder2);

                    test.assertEqual(false, publisherFolder.equals((Object)publisherFolder2));
                });

                runner.test("with /qub/me/ and /qub/me/", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = QubPublisherFolderTests.createFileSystem(test);

                    final Folder folder = fileSystem.getFolder("/qub/me/").await();
                    final QubPublisherFolder publisherFolder = QubPublisherFolder.get(folder);

                    final Folder folder2 = fileSystem.getFolder("/qub/me/").await();
                    final QubPublisherFolder publisherFolder2 = QubPublisherFolder.get(folder2);

                    test.assertEqual(true, publisherFolder.equals((Object)publisherFolder2));
                });
            });

            runner.testGroup("equals(QubPublisherFolder)", () ->
            {
                runner.test("with /qub/me/ and null", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = QubPublisherFolderTests.createFileSystem(test);
                    final Folder folder = fileSystem.getFolder("/qub/me/").await();
                    final QubPublisherFolder publisherFolder = QubPublisherFolder.get(folder);
                    test.assertEqual(false, publisherFolder.equals((QubPublisherFolder)null));
                });

                runner.test("with /qub/me/ and /other/thing/", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = QubPublisherFolderTests.createFileSystem(test);

                    final Folder folder = fileSystem.getFolder("/qub/me/").await();
                    final QubPublisherFolder publisherFolder = QubPublisherFolder.get(folder);

                    final Folder folder2 = fileSystem.getFolder("/other/thing/").await();
                    final QubPublisherFolder publisherFolder2 = QubPublisherFolder.get(folder2);

                    test.assertEqual(false, publisherFolder.equals(publisherFolder2));
                });

                runner.test("with /qub/me/ and /qub/me/", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = QubPublisherFolderTests.createFileSystem(test);

                    final Folder folder = fileSystem.getFolder("/qub/me/").await();
                    final QubPublisherFolder publisherFolder = QubPublisherFolder.get(folder);

                    final Folder folder2 = fileSystem.getFolder("/qub/me/").await();
                    final QubPublisherFolder publisherFolder2 = QubPublisherFolder.get(folder2);

                    test.assertEqual(true, publisherFolder.equals(publisherFolder2));
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
        final InMemoryFileSystem fileSystem = InMemoryFileSystem.create();
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
        return QubPublisherFolder.get(QubPublisherFolderTests.getFolder(test, folderPath));
    }

    static QubPublisherFolder createQubPublisherFolder(Test test, String folderPath)
    {
        return QubPublisherFolder.get(QubPublisherFolderTests.createFolder(test, folderPath));
    }
}
