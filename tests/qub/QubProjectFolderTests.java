package qub;

public interface QubProjectFolderTests
{
    static void test(TestRunner runner)
    {
        PreCondition.assertNotNull(runner, "runner");

        runner.testGroup(QubProjectFolder.class, () ->
        {
            runner.testGroup("get(Folder)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> QubProjectFolder.get(null),
                        new PreConditionFailure("projectFolder cannot be null."));
                });

                runner.test("with root folder", (Test test) ->
                {
                    final Folder folder = QubProjectFolderTests.getFolder("/");
                    test.assertThrows(() -> QubProjectFolder.get(folder),
                        new PreConditionFailure("projectFolder.getPath().getSegments().getCount() (1) must be greater than or equal to 3."));
                });

                runner.test("with folder that doesn't exist", (Test test) ->
                {
                    final Folder folder = QubProjectFolderTests.getFolder("/qub/me/project/");
                    final QubProjectFolder projectFolder = QubProjectFolder.get(folder);
                    test.assertEqual(folder.getPath(), projectFolder.getPath());
                    test.assertFalse(projectFolder.exists().await());
                });

                runner.test("with folder that exists", (Test test) ->
                {
                    final Folder folder = QubProjectFolderTests.createFolder("/qub/me/project/");
                    final QubProjectFolder projectFolder = QubProjectFolder.get(folder);
                    test.assertEqual(folder.getPath(), projectFolder.getPath());
                    test.assertTrue(projectFolder.exists().await());
                });
            });

            runner.test("getQubFolder()", (Test test) ->
            {
                final String publisherName = "a";
                final String projectName = "b";
                final QubProjectFolder projectFolder = QubProjectFolderTests.getQubProjectFolder("/qub/" + publisherName + "/" + projectName + "/");
                test.assertEqual("/qub/", projectFolder.getQubFolder().await().toString());
            });

            runner.test("getPublisherFolder()", (Test test) ->
            {
                final String publisherName = "a";
                final String projectName = "b";
                final QubProjectFolder projectFolder = QubProjectFolderTests.getQubProjectFolder("/qub/" + publisherName + "/" + projectName + "/");
                test.assertEqual("/qub/" + publisherName + "/", projectFolder.getPublisherFolder().await().toString());
            });

            runner.test("getPublisherName()", (Test test) ->
            {
                final String publisherName = "a";
                final String projectName = "b";
                final QubProjectFolder projectFolder = QubProjectFolderTests.getQubProjectFolder("/qub/" + publisherName + "/" + projectName + "/");
                test.assertEqual(publisherName, projectFolder.getPublisherName().await());
            });

            runner.testGroup("iterateProjectVersionFolders()", () ->
            {
                runner.test("with non-existing Qub folder", (Test test) ->
                {
                    final String publisherName = "a";
                    final String projectName = "b";
                    final QubProjectFolder projectFolder = QubProjectFolderTests.getQubProjectFolder("/qub/" + publisherName + "/" + projectName + "/");
                    test.assertEqual(Iterable.create(), projectFolder.iterateProjectVersionFolders().toList());
                });

                runner.test("with non-existing publisher folder", (Test test) ->
                {
                    final String publisherName = "a";
                    final String projectName = "b";
                    final QubProjectFolder projectFolder = QubProjectFolderTests.getQubProjectFolder("/qub/" + publisherName + "/" + projectName + "/");
                    projectFolder.getQubFolder().await().create().await();
                    test.assertEqual(Iterable.create(), projectFolder.iterateProjectVersionFolders().toList());
                });

                runner.test("with non-existing project folder", (Test test) ->
                {
                    final String publisherName = "a";
                    final String projectName = "b";
                    final QubProjectFolder projectFolder = QubProjectFolderTests.getQubProjectFolder("/qub/" + publisherName + "/" + projectName + "/");
                    projectFolder.getPublisherFolder().await().create().await();
                    test.assertEqual(Iterable.create(), projectFolder.iterateProjectVersionFolders().toList());
                });

                runner.test("with non-existing versions folder", (Test test) ->
                {
                    final String publisherName = "a";
                    final String projectName = "b";
                    final QubProjectFolder projectFolder = QubProjectFolderTests.getQubProjectFolder("/qub/" + publisherName + "/" + projectName + "/");
                    projectFolder.create().await();
                    test.assertEqual(Iterable.create(), projectFolder.iterateProjectVersionFolders().toList());
                });

                runner.test("with one version folder", (Test test) ->
                {
                    final String publisherName = "a";
                    final String projectName = "b";
                    final QubProjectFolder projectFolder = QubProjectFolderTests.getQubProjectFolder("/qub/" + publisherName + "/" + projectName + "/");
                    projectFolder.createFolder("versions/1").await();
                    test.assertEqual(
                        Iterable.create(
                            projectFolder.getProjectVersionFolder("1").await()),
                        projectFolder.iterateProjectVersionFolders().toList());
                });
            });

            runner.testGroup("getLatestProjectVersionFolder()", () ->
            {
                runner.test("with no existing Qub folder", (Test test) ->
                {
                    final String publisherName = "a";
                    final String projectName = "b";
                    final QubProjectFolder projectFolder = QubProjectFolderTests.getQubProjectFolder("/qub/" + publisherName + "/" + projectName + "/");
                    test.assertThrows(() -> projectFolder.getLatestProjectVersionFolder().await(),
                        new NotFoundException("No project named a/b has been published."));
                });

                runner.test("with no existing publisher folder", (Test test) ->
                {
                    final String publisherName = "a";
                    final String projectName = "b";
                    final QubProjectFolder projectFolder = QubProjectFolderTests.getQubProjectFolder("/qub/" + publisherName + "/" + projectName + "/");
                    projectFolder.getQubFolder().await().create().await();
                    test.assertThrows(() -> projectFolder.getLatestProjectVersionFolder().await(),
                        new NotFoundException("No project named a/b has been published."));
                });

                runner.test("with no existing project folder", (Test test) ->
                {
                    final String publisherName = "a";
                    final String projectName = "b";
                    final QubProjectFolder projectFolder = QubProjectFolderTests.getQubProjectFolder("/qub/" + publisherName + "/" + projectName + "/");
                    projectFolder.getPublisherFolder().await().create().await();
                    test.assertThrows(() -> projectFolder.getLatestProjectVersionFolder().await(),
                        new NotFoundException("No project named a/b has been published."));
                });

                runner.test("with no existing versions folder", (Test test) ->
                {
                    final String publisherName = "a";
                    final String projectName = "b";
                    final QubProjectFolder projectFolder = QubProjectFolderTests.getQubProjectFolder("/qub/" + publisherName + "/" + projectName + "/");
                    projectFolder.create().await();
                    test.assertThrows(() -> projectFolder.getLatestProjectVersionFolder().await(),
                        new NotFoundException("No project named a/b has been published."));
                });

                runner.test("with no existing version folders", (Test test) ->
                {
                    final String publisherName = "a";
                    final String projectName = "b";
                    final QubProjectFolder projectFolder = QubProjectFolderTests.getQubProjectFolder("/qub/" + publisherName + "/" + projectName + "/");
                    projectFolder.getProjectVersionsFolder().await().create().await();
                    test.assertThrows(() -> projectFolder.getLatestProjectVersionFolder().await(),
                        new NotFoundException("No project named a/b has been published."));
                });

                runner.test("with one version", (Test test) ->
                {
                    final String publisherName = "a";
                    final String projectName = "b";
                    final QubProjectFolder projectFolder = QubProjectFolderTests.getQubProjectFolder("/qub/" + publisherName + "/" + projectName + "/");
                    projectFolder.getProjectVersionFolder("1").await().create().await();
                    test.assertEqual("/qub/" + publisherName + "/" + projectName + "/versions/1/", projectFolder.getLatestProjectVersionFolder().await().toString());
                });

                runner.test("with two versions", (Test test) ->
                {
                    final String publisherName = "a";
                    final String projectName = "b";
                    final QubProjectFolder projectFolder = QubProjectFolderTests.getQubProjectFolder("/qub/" + publisherName + "/" + projectName + "/");
                    projectFolder.getProjectVersionFolder("2").await().create().await();
                    projectFolder.getProjectVersionFolder("3").await().create().await();
                    test.assertEqual("/qub/" + publisherName + "/" + projectName + "/versions/3/", projectFolder.getLatestProjectVersionFolder().await().toString());
                });

                runner.test("with three non-sorted versions", (Test test) ->
                {
                    final String publisherName = "a";
                    final String projectName = "b";
                    final QubProjectFolder projectFolder = QubProjectFolderTests.getQubProjectFolder("/qub/" + publisherName + "/" + projectName + "/");
                    projectFolder.getProjectVersionFolder("2").await().create().await();
                    projectFolder.getProjectVersionFolder("30").await().create().await();
                    projectFolder.getProjectVersionFolder("13").await().create().await();
                    test.assertEqual("/qub/" + publisherName + "/" + projectName + "/versions/30/", projectFolder.getLatestProjectVersionFolder().await().toString());
                });

                runner.test("with non-integer versions", (Test test) ->
                {
                    final String publisherName = "a";
                    final String projectName = "b";
                    final QubProjectFolder projectFolder = QubProjectFolderTests.getQubProjectFolder("/qub/" + publisherName + "/" + projectName + "/");
                    projectFolder.getProjectVersionFolder("2").await().create().await();
                    projectFolder.getProjectVersionFolder("3.0.3").await().create().await();
                    projectFolder.getProjectVersionFolder("1.3").await().create().await();
                    test.assertEqual("/qub/" + publisherName + "/" + projectName + "/versions/3.0.3/", projectFolder.getLatestProjectVersionFolder().await().toString());
                });
            });

            runner.test("getProjectDataFolder()", (Test test) ->
            {
                final String publisherName = "a";
                final String projectName = "b";
                final QubProjectFolder projectFolder = QubProjectFolderTests.getQubProjectFolder("/qub/" + publisherName + "/" + projectName + "/");
                test.assertEqual("/qub/" + publisherName + "/" + projectName + "/data/", projectFolder.getProjectDataFolder().await().toString());
            });

            runner.test("getProjectVersionsFolder()", (Test test) ->
            {
                final String publisherName = "a";
                final String projectName = "b";
                final QubProjectFolder projectFolder = QubProjectFolderTests.getQubProjectFolder("/qub/" + publisherName + "/" + projectName + "/");
                test.assertEqual("/qub/" + publisherName + "/" + projectName + "/versions/", projectFolder.getProjectVersionsFolder().await().toString());
            });

            runner.testGroup("equals(Object)", () ->
            {
                runner.test("with /qub/me/grapes/ and null", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = QubProjectFolderTests.createFileSystem();
                    final Folder folder = fileSystem.getFolder("/qub/me/grapes/").await();
                    final QubProjectFolder projectFolder = QubProjectFolder.get(folder);
                    test.assertEqual(false, projectFolder.equals((Object)null));
                });

                runner.test("with /qub/me/grapes/ and \"hello world\"", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = QubProjectFolderTests.createFileSystem();
                    final Folder folder = fileSystem.getFolder("/qub/me/grapes/").await();
                    final QubProjectFolder projectFolder = QubProjectFolder.get(folder);
                    test.assertEqual(false, projectFolder.equals((Object)"hello world"));
                });

                runner.test("with /qub/me/a/ and /other/thing/b/", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = QubProjectFolderTests.createFileSystem();

                    final Folder folder = fileSystem.getFolder("/qub/me/a/").await();
                    final QubProjectFolder projectFolder = QubProjectFolder.get(folder);

                    final Folder folder2 = fileSystem.getFolder("/other/thing/b/").await();
                    final QubProjectFolder projectFolder2 = QubProjectFolder.get(folder2);

                    test.assertEqual(false, projectFolder.equals((Object)projectFolder2));
                });

                runner.test("with /qub/me/c/ and /qub/me/c/", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = QubProjectFolderTests.createFileSystem();

                    final Folder folder = fileSystem.getFolder("/qub/me/c/").await();
                    final QubProjectFolder projectFolder = QubProjectFolder.get(folder);

                    final Folder folder2 = fileSystem.getFolder("/qub/me/c/").await();
                    final QubProjectFolder projectFolder2 = QubProjectFolder.get(folder2);

                    test.assertEqual(true, projectFolder.equals((Object)projectFolder2));
                });
            });

            runner.testGroup("equals(QubProjectFolder)", () ->
            {
                runner.test("with /qub/me/spam and null", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = QubProjectFolderTests.createFileSystem();
                    final Folder folder = fileSystem.getFolder("/qub/me/spam/").await();
                    final QubProjectFolder projectFolder = QubProjectFolder.get(folder);
                    test.assertEqual(false, projectFolder.equals((QubProjectFolder)null));
                });

                runner.test("with /qub/me/project1 and /other/thing/project2", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = QubProjectFolderTests.createFileSystem();

                    final Folder folder = fileSystem.getFolder("/qub/me/project1").await();
                    final QubProjectFolder projectFolder = QubProjectFolder.get(folder);

                    final Folder folder2 = fileSystem.getFolder("/other/thing/project2").await();
                    final QubProjectFolder projectFolder2 = QubProjectFolder.get(folder2);

                    test.assertEqual(false, projectFolder.equals(projectFolder2));
                });

                runner.test("with /qub/me/proj/ and /qub/me/proj/", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = QubProjectFolderTests.createFileSystem();

                    final Folder folder = fileSystem.getFolder("/qub/me/proj/").await();
                    final QubProjectFolder projectFolder = QubProjectFolder.get(folder);

                    final Folder folder2 = fileSystem.getFolder("/qub/me/proj/").await();
                    final QubProjectFolder projectFolder2 = QubProjectFolder.get(folder2);

                    test.assertEqual(true, projectFolder.equals(projectFolder2));
                });
            });

            runner.test("toString()", (Test test) ->
            {
                final QubProjectFolder projectFolder = QubProjectFolderTests.getQubProjectFolder("/qub/me/proj/");
                test.assertEqual("/qub/me/proj/", projectFolder.toString());
            });
        });
    }

    static InMemoryFileSystem createFileSystem()
    {
        final InMemoryFileSystem fileSystem = InMemoryFileSystem.create();
        fileSystem.createRoot("/").await();
        return fileSystem;
    }

    static Folder getFolder(String folderPath)
    {
        PreCondition.assertNotNullAndNotEmpty(folderPath, "folderPath");

        final InMemoryFileSystem fileSystem = QubProjectFolderTests.createFileSystem();
        return fileSystem.getFolder(folderPath).await();
    }

    static Folder createFolder(String folderPath)
    {
        PreCondition.assertNotNullAndNotEmpty(folderPath, "folderPath");

        final InMemoryFileSystem fileSystem = QubProjectFolderTests.createFileSystem();
        return fileSystem.createFolder(folderPath).await();
    }

    static QubProjectFolder getQubProjectFolder(String folderPath)
    {
        return QubProjectFolder.get(QubProjectFolderTests.getFolder(folderPath));
    }
}
