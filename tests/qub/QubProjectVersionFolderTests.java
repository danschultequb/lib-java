package qub;

public interface QubProjectVersionFolderTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(QubProjectVersionFolder.class, () ->
        {
            runner.testGroup("get(Folder)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() ->  QubProjectVersionFolder.get((Folder)null),
                        new PreConditionFailure("projectVersionFolder cannot be null."));
                });

                runner.test("with root folder", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = InMemoryFileSystem.create();
                    fileSystem.createRoot("/").await();
                    final Folder rootFolder = fileSystem.getFolder("/").await();

                    test.assertThrows(() -> QubProjectVersionFolder.get(rootFolder),
                        new PreConditionFailure("projectVersionFolder.getPath().getSegments().getCount() (1) must be greater than or equal to 5."));
                });

                runner.test("with qub folder", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = InMemoryFileSystem.create();
                    fileSystem.createRoot("/").await();
                    final Folder qubFolder = fileSystem.getFolder("/").await();

                    test.assertThrows(() -> QubProjectVersionFolder.get(qubFolder),
                        new PreConditionFailure("projectVersionFolder.getPath().getSegments().getCount() (1) must be greater than or equal to 5."));
                });

                runner.test("with publisher folder", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = InMemoryFileSystem.create();
                    fileSystem.createRoot("/").await();
                    final Folder publisherFolder = fileSystem.createFolder("/fake-publisher/").await();

                    test.assertThrows(() -> QubProjectVersionFolder.get(publisherFolder),
                        new PreConditionFailure("projectVersionFolder.getPath().getSegments().getCount() (2) must be greater than or equal to 5."));
                });

                runner.test("with project folder", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = InMemoryFileSystem.create();
                    fileSystem.createRoot("/").await();
                    final Folder projectVersionFolder = fileSystem.createFolder("/fake-publisher/fake-project/").await();

                    test.assertThrows(() -> QubProjectVersionFolder.get(projectVersionFolder),
                        new PreConditionFailure("projectVersionFolder.getPath().getSegments().getCount() (3) must be greater than or equal to 5."));
                });

                runner.test("with versions folder", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = InMemoryFileSystem.create();
                    fileSystem.createRoot("/").await();
                    final Folder projectVersionFolder = fileSystem.createFolder("/fake-publisher/fake-project/versions/").await();

                    test.assertThrows(() -> QubProjectVersionFolder.get(projectVersionFolder),
                        new PreConditionFailure("projectVersionFolder.getPath().getSegments().getCount() (4) must be greater than or equal to 5."));
                });

                runner.test("with version folder", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = InMemoryFileSystem.create();
                    fileSystem.createRoot("/").await();
                    final Folder versionFolder = fileSystem.createFolder("/fake-publisher/fake-project/versions/fake-version/").await();

                    final QubProjectVersionFolder projectVersionFolder = QubProjectVersionFolder.get(versionFolder);
                    test.assertNotNull(projectVersionFolder);
                    test.assertEqual(projectVersionFolder.getPath(), projectVersionFolder.getPath());
                    test.assertEqual(VersionNumber.create().setSuffix("fake-version"), projectVersionFolder.getVersion().await());
                    test.assertEqual("fake-project", projectVersionFolder.getProjectName().await());
                    test.assertEqual("/fake-publisher/fake-project/", projectVersionFolder.getProjectFolder().await().toString());
                    test.assertEqual("fake-publisher", projectVersionFolder.getPublisherName().await());
                    test.assertEqual("/fake-publisher/", projectVersionFolder.getPublisherFolder().await().toString());
                    test.assertEqual("/", projectVersionFolder.getQubFolder().await().toString());
                    test.assertEqual(
                        ProjectSignature.create("fake-publisher", "fake-project", "fake-version"),
                        projectVersionFolder.getProjectSignature().await());
                });
            });
            
            runner.test("getQubFolder()", (Test test) ->
            {
                final String publisherName = "a";
                final String projectName = "b";
                final String version = "1";
                final QubProjectVersionFolder projectVersionFolder = QubProjectVersionFolderTests.getQubProjectVersionFolder(test, "/qub/" + publisherName + "/" + projectName + "/versions/" + version + "/");
                test.assertEqual("/qub/", projectVersionFolder.getQubFolder().await().toString());
            });

            runner.test("getPublisherFolder()", (Test test) ->
            {
                final String publisherName = "a";
                final String projectName = "b";
                final String version = "1";
                final QubProjectVersionFolder projectVersionFolder = QubProjectVersionFolderTests.getQubProjectVersionFolder(test, "/qub/" + publisherName + "/" + projectName + "/versions/" + version + "/");
                test.assertEqual("/qub/" + publisherName + "/", projectVersionFolder.getPublisherFolder().await().toString());
            });

            runner.test("getPublisherName()", (Test test) ->
            {
                final String publisherName = "a";
                final String projectName = "b";
                final String version = "1";
                final QubProjectVersionFolder projectVersionFolder = QubProjectVersionFolderTests.getQubProjectVersionFolder(test, "/qub/" + publisherName + "/" + projectName + "/versions/" + version + "/");
                test.assertEqual(publisherName, projectVersionFolder.getPublisherName().await());
            });

            runner.testGroup("getProjectSignature()", () ->
            {
                final Action3<String,String,String> getProjectSignatureTest = (String publisherName, String projectName, String version) ->
                {
                    runner.test("with " + English.andList(Iterable.create(publisherName, projectName, version).map(Strings::escapeAndQuote)), (Test test) ->
                    {
                        final QubProjectVersionFolder projectVersionFolder = QubProjectVersionFolderTests.getQubProjectVersionFolder(test, "/qub/" + publisherName + "/" + projectName + "/versions/" + version + "/");
                        test.assertEqual(
                            ProjectSignature.create(publisherName, projectName, version),
                            projectVersionFolder.getProjectSignature().await());
                    });
                };

                getProjectSignatureTest.run("a", "b", "1");
            });

            runner.testGroup("getProjectVersionsFolder()", () ->
            {
                final Action3<String,String,String> getProjectVersionsFolderTest = (String publisherName, String projectName, String version) ->
                {
                    runner.test("with " + English.andList(Iterable.create(publisherName, projectName, version).map(Strings::escapeAndQuote)), (Test test) ->
                    {
                        final QubProjectVersionFolder projectVersionFolder = QubProjectVersionFolderTests.getQubProjectVersionFolder(test, "/qub/" + publisherName + "/" + projectName + "/" + version + "/");
                        test.assertEqual(
                            projectVersionFolder.getProjectFolder().await().getProjectVersionsFolder().await(),
                            projectVersionFolder.getProjectVersionsFolder().await());
                    });
                };

                getProjectVersionsFolderTest.run("a", "b", "1");
            });

            runner.testGroup("getProjectDataFolder()", () ->
            {
                final Action3<String,String,String> getProjectDataFolderTest = (String publisherName, String projectName, String version) ->
                {
                    runner.test("with " + English.andList(Iterable.create(publisherName, projectName, version).map(Strings::escapeAndQuote)), (Test test) ->
                    {
                        final QubProjectVersionFolder projectVersionFolder = QubProjectVersionFolderTests.getQubProjectVersionFolder(test, "/qub/" + publisherName + "/" + projectName + "/" + version + "/");
                        test.assertEqual(
                            projectVersionFolder.getProjectFolder().await().getProjectDataFolder().await(),
                            projectVersionFolder.getProjectDataFolder().await());
                    });
                };

                getProjectDataFolderTest.run("a", "b", "1");
            });

            runner.testGroup("equals(Object)", () ->
            {
                runner.test("with /qub/me/grapes/1/ and null", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = QubPublisherFolderTests.createFileSystem(test);
                    final Folder folder = fileSystem.getFolder("/qub/me/grapes/1").await();
                    final QubProjectVersionFolder projectVersionFolder = QubProjectVersionFolder.get(folder);
                    test.assertEqual(false, projectVersionFolder.equals((Object)null));
                });

                runner.test("with /qub/me/grapes/2/ and \"hello world\"", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = QubPublisherFolderTests.createFileSystem(test);
                    final Folder folder = fileSystem.getFolder("/qub/me/grapes/2").await();
                    final QubProjectVersionFolder projectVersionFolder = QubProjectVersionFolder.get(folder);
                    test.assertEqual(false, projectVersionFolder.equals((Object)"hello world"));
                });

                runner.test("with /qub/me/a/3/ and /other/thing/b/4/", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = QubPublisherFolderTests.createFileSystem(test);

                    final Folder folder = fileSystem.getFolder("/qub/me/a/3/").await();
                    final QubProjectVersionFolder projectVersionFolder = QubProjectVersionFolder.get(folder);

                    final Folder folder2 = fileSystem.getFolder("/other/thing/b/4/").await();
                    final QubProjectVersionFolder projectVersionFolder2 = QubProjectVersionFolder.get(folder2);

                    test.assertEqual(false, projectVersionFolder.equals((Object)projectVersionFolder2));
                });

                runner.test("with /qub/me/c/5/ and /qub/me/c/5/", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = QubPublisherFolderTests.createFileSystem(test);

                    final Folder folder = fileSystem.getFolder("/qub/me/c/5/").await();
                    final QubProjectVersionFolder projectVersionFolder = QubProjectVersionFolder.get(folder);

                    final Folder folder2 = fileSystem.getFolder("/qub/me/c/5/").await();
                    final QubProjectVersionFolder projectVersionFolder2 = QubProjectVersionFolder.get(folder2);

                    test.assertEqual(true, projectVersionFolder.equals((Object)projectVersionFolder2));
                });
            });

            runner.testGroup("equals(QubProjectVersionFolder)", () ->
            {
                runner.test("with /qub/me/spam/versions/5/ and null", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = QubPublisherFolderTests.createFileSystem(test);
                    final Folder folder = fileSystem.getFolder("/qub/me/spam/versions/5/").await();
                    final QubProjectVersionFolder projectVersionFolder = QubProjectVersionFolder.get(folder);
                    test.assertEqual(false, projectVersionFolder.equals((QubProjectVersionFolder)null));
                });

                runner.test("with /qub/me/project1/versions/1/ and /other/thing/project2/versions/2/", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = QubPublisherFolderTests.createFileSystem(test);

                    final Folder folder = fileSystem.getFolder("/qub/me/project1/versions/1/").await();
                    final QubProjectVersionFolder projectVersionFolder = QubProjectVersionFolder.get(folder);

                    final Folder folder2 = fileSystem.getFolder("/other/thing/project2/versions/2/").await();
                    final QubProjectVersionFolder projectVersionFolder2 = QubProjectVersionFolder.get(folder2);

                    test.assertEqual(false, projectVersionFolder.equals((QubProjectVersionFolder)projectVersionFolder2));
                });

                runner.test("with /qub/me/proj/versions/3/ and /qub/me/proj/versions/3/", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = QubPublisherFolderTests.createFileSystem(test);

                    final Folder folder = fileSystem.getFolder("/qub/me/proj/versions/3/").await();
                    final QubProjectVersionFolder projectVersionFolder = QubProjectVersionFolder.get(folder);

                    final Folder folder2 = fileSystem.getFolder("/qub/me/proj/versions/3/").await();
                    final QubProjectVersionFolder projectVersionFolder2 = QubProjectVersionFolder.get(folder2);

                    test.assertEqual(true, projectVersionFolder.equals((QubProjectVersionFolder)projectVersionFolder2));
                });
            });

            runner.test("toString()", (Test test) ->
            {
                final QubProjectVersionFolder projectVersionFolder = QubProjectVersionFolderTests.getQubProjectVersionFolder(test, "/qub/me/proj/versions/4/");
                test.assertEqual("/qub/me/proj/versions/4/", projectVersionFolder.toString());
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

        final InMemoryFileSystem fileSystem = QubProjectVersionFolderTests.createFileSystem(test);
        return fileSystem.getFolder(folderPath).await();
    }

    static Folder createFolder(Test test, String folderPath)
    {
        PreCondition.assertNotNull(test, "test");
        PreCondition.assertNotNullAndNotEmpty(folderPath, "folderPath");

        final InMemoryFileSystem fileSystem = QubProjectVersionFolderTests.createFileSystem(test);
        return fileSystem.createFolder(folderPath).await();
    }

    static QubProjectVersionFolder getQubProjectVersionFolder(Test test, String folderPath)
    {
        return QubProjectVersionFolder.get(QubProjectVersionFolderTests.getFolder(test, folderPath));
    }
}
