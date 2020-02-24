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
                    final QubPublisherFolder publisherFolder = QubPublisherFolder.get(folder);
                    test.assertEqual(Iterable.create(), publisherFolder.getProjectFolders().await());
                });

                runner.test("with existing QubPublisherFolder with folders", (Test test) ->
                {
                    final Folder folder = QubPublisherFolderTests.createFolder(test, "/qub/me/");
                    final Folder subFolder = folder.createFolder("project").await();
                    final QubPublisherFolder publisherFolder = QubPublisherFolder.get(folder);
                    test.assertEqual(
                        Iterable.create(
                            QubProjectFolder.get(subFolder)),
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
                    final QubPublisherFolder publisherFolder = QubPublisherFolder.get(folder);

                    folder.createFolder("spam").await();
                    final QubProjectFolder projectFolder = publisherFolder.getProjectFolder("spam").await();
                    test.assertNotNull(projectFolder);
                    test.assertEqual(Path.parse("/qub/me/spam"), projectFolder.getPath());
                });
            });

            runner.testGroup("getProjectVersionFolders(String,String)", () ->
            {
                final Action3<String,String,Throwable> getProjectVersionFoldersErrorTest = (String publisherName, String projectName, Throwable expected) ->
                {
                    runner.test("with " + English.andList(Iterable.create(publisherName, projectName).map(Strings::escapeAndQuote)), (Test test) ->
                    {
                        final QubPublisherFolder publisherFolder = QubPublisherFolderTests.getQubPublisherFolder(test, "/qub/" + publisherName + "/");
                        test.assertThrows(() -> publisherFolder.getProjectVersionFolders(projectName), expected);
                    });
                };

                getProjectVersionFoldersErrorTest.run("a", null, new PreConditionFailure("projectName cannot be null."));
                getProjectVersionFoldersErrorTest.run("a", "", new PreConditionFailure("projectName cannot be empty."));

                runner.test("with non-existing Qub folder", (Test test) ->
                {
                    final String publisherName = "a";
                    final String projectName = "b";
                    final QubPublisherFolder publisherFolder = QubPublisherFolderTests.getQubPublisherFolder(test, "/qub/" + publisherName + "/");
                    test.assertEqual(Iterable.create(), publisherFolder.getProjectVersionFolders(projectName).await());
                });

                runner.test("with non-existing publisher folder", (Test test) ->
                {
                    final String publisherName = "a";
                    final String projectName = "b";
                    final QubPublisherFolder publisherFolder = QubPublisherFolderTests.getQubPublisherFolder(test, "/qub/" + publisherName + "/");
                    publisherFolder.getQubFolder().await().create().await();
                    test.assertEqual(Iterable.create(), publisherFolder.getProjectVersionFolders(projectName).await());
                });

                runner.test("with non-existing project folder", (Test test) ->
                {
                    final String publisherName = "a";
                    final String projectName = "b";
                    final QubPublisherFolder publisherFolder = QubPublisherFolderTests.getQubPublisherFolder(test, "/qub/" + publisherName + "/");
                    publisherFolder.create().await();
                    test.assertEqual(Iterable.create(), publisherFolder.getProjectVersionFolders(projectName).await());
                });

                runner.test("with empty project folder", (Test test) ->
                {
                    final String publisherName = "a";
                    final String projectName = "b";
                    final QubPublisherFolder publisherFolder = QubPublisherFolderTests.getQubPublisherFolder(test, "/qub/" + publisherName + "/");
                    publisherFolder.getQubFolder().await().create().await();
                    final QubProjectFolder projectFolder = publisherFolder.getProjectFolder(projectName).await();
                    projectFolder.create().await();
                    test.assertEqual(Iterable.create(), publisherFolder.getProjectVersionFolders(projectName).await());
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
                        publisherFolder.getProjectVersionFolders(projectName).await());
                });
            });

            runner.testGroup("getProjectJSONFile(String,String)", () ->
            {
                final Action4<String,String,String,Throwable> getProjectJSONFileErrorTest = (String publisherName, String projectName, String version, Throwable expected) ->
                {
                    runner.test("with " + English.andList(Iterable.create(publisherName, projectName, version).map(Strings::escapeAndQuote)), (Test test) ->
                    {
                        final QubPublisherFolder publisherFolder = QubPublisherFolderTests.getQubPublisherFolder(test, "/qub/" + publisherName + "/");
                        test.assertThrows(() -> publisherFolder.getProjectJSONFile(projectName, version), expected);
                    });
                };

                getProjectJSONFileErrorTest.run("a", null, null, new PreConditionFailure("projectName cannot be null."));
                getProjectJSONFileErrorTest.run("a", "", null, new PreConditionFailure("projectName cannot be empty."));
                getProjectJSONFileErrorTest.run("a", "b", null, new PreConditionFailure("version cannot be null."));
                getProjectJSONFileErrorTest.run("a", "b", "", new PreConditionFailure("version cannot be empty."));

                runner.test("with non-existing Qub folder", (Test test) ->
                {
                    final String publisherName = "a";
                    final String projectName = "b";
                    final String version = "1";
                    final QubPublisherFolder publisherFolder = QubPublisherFolderTests.getQubPublisherFolder(test, "/qub/" + publisherName + "/");
                    test.assertEqual(
                        publisherFolder.getFile(projectName + "/versions/" + version + "/project.json").await(),
                        publisherFolder.getProjectJSONFile(projectName, version).await());
                });

                runner.test("with non-existing publisher folder", (Test test) ->
                {
                    final String publisherName = "a";
                    final String projectName = "b";
                    final String version = "1";
                    final QubPublisherFolder publisherFolder = QubPublisherFolderTests.getQubPublisherFolder(test, "/qub/" + publisherName + "/");
                    publisherFolder.getQubFolder().await().create().await();
                    test.assertEqual(
                        publisherFolder.getFile(projectName + "/versions/" + version + "/project.json").await(),
                        publisherFolder.getProjectJSONFile(projectName, version).await());
                });

                runner.test("with non-existing project folder", (Test test) ->
                {
                    final String publisherName = "a";
                    final String projectName = "b";
                    final String version = "1";
                    final QubPublisherFolder publisherFolder = QubPublisherFolderTests.getQubPublisherFolder(test, "/qub/" + publisherName + "/");
                    publisherFolder.create().await();
                    test.assertEqual(
                        publisherFolder.getFile(projectName + "/versions/" + version + "/project.json").await(),
                        publisherFolder.getProjectJSONFile(projectName, version).await());
                });

                runner.test("with non-existing project version folder", (Test test) ->
                {
                    final String publisherName = "a";
                    final String projectName = "b";
                    final String version = "1";
                    final QubPublisherFolder publisherFolder = QubPublisherFolderTests.getQubPublisherFolder(test, "/qub/" + publisherName + "/");
                    publisherFolder.getProjectFolder(projectName).await().create().await();
                    test.assertEqual(
                        publisherFolder.getFile(projectName + "/versions/" + version + "/project.json").await(),
                        publisherFolder.getProjectJSONFile(projectName, version).await());
                });

                runner.test("with non-existing project.json file", (Test test) ->
                {
                    final String publisherName = "a";
                    final String projectName = "b";
                    final String version = "1";
                    final QubPublisherFolder publisherFolder = QubPublisherFolderTests.getQubPublisherFolder(test, "/qub/" + publisherName + "/");
                    publisherFolder.getProjectVersionFolder(projectName, version).await().create().await();
                    test.assertEqual(
                        publisherFolder.getFile(projectName + "/versions/" + version + "/project.json").await(),
                        publisherFolder.getProjectJSONFile(projectName, version).await());
                });

                runner.test("with existing project.json file", (Test test) ->
                {
                    final String publisherName = "a";
                    final String projectName = "b";
                    final String version = "1";
                    final QubPublisherFolder publisherFolder = QubPublisherFolderTests.getQubPublisherFolder(test, "/qub/" + publisherName + "/");
                    final QubProjectVersionFolder projectVersionFolder = publisherFolder.getProjectVersionFolder(projectName, version).await();
                    projectVersionFolder.createFile("project.json").await();
                    test.assertEqual(
                        publisherFolder.getFile(projectName + "/versions/" + version + "/project.json").await(),
                        publisherFolder.getProjectJSONFile(projectName, version).await());
                });
            });

            runner.testGroup("getCompiledSourcesFile(String,String)", () ->
            {
                final Action4<String,String,String,Throwable> getCompiledSourcesFileErrorTest = (String publisherName, String projectName, String version, Throwable expected) ->
                {
                    runner.test("with " + English.andList(Iterable.create(publisherName, projectName, version).map(Strings::escapeAndQuote)), (Test test) ->
                    {
                        final QubPublisherFolder publisherFolder = QubPublisherFolderTests.getQubPublisherFolder(test, "/qub/" + publisherName + "/");
                        test.assertThrows(() -> publisherFolder.getCompiledSourcesFile(projectName, version), expected);
                    });
                };

                getCompiledSourcesFileErrorTest.run("a", null, null, new PreConditionFailure("projectName cannot be null."));
                getCompiledSourcesFileErrorTest.run("a", "", null, new PreConditionFailure("projectName cannot be empty."));
                getCompiledSourcesFileErrorTest.run("a", "b", null, new PreConditionFailure("version cannot be null."));
                getCompiledSourcesFileErrorTest.run("a", "b", "", new PreConditionFailure("version cannot be empty."));

                final Action3<String,String,String> getCompiledSourcesFileTest = (String publisherName, String projectName, String version) ->
                {
                    runner.test("with " + English.andList(Iterable.create(publisherName, projectName, version).map(Strings::escapeAndQuote)), (Test test) ->
                    {
                        final QubPublisherFolder publisherFolder = QubPublisherFolderTests.getQubPublisherFolder(test, "/qub/" + publisherName + "/");
                        test.assertEqual(
                            publisherFolder.getFile(projectName + "/versions/" + version + "/" + projectName + ".jar").await(),
                            publisherFolder.getCompiledSourcesFile(projectName, version).await());
                    });
                };

                getCompiledSourcesFileTest.run("a", "b", "1");
            });

            runner.testGroup("getSourcesFile(String,String)", () ->
            {
                final Action4<String,String,String,Throwable> getSourcesFileErrorTest = (String publisherName, String projectName, String version, Throwable expected) ->
                {
                    runner.test("with " + English.andList(Iterable.create(publisherName, projectName, version).map(Strings::escapeAndQuote)), (Test test) ->
                    {
                        final QubPublisherFolder publisherFolder = QubPublisherFolderTests.getQubPublisherFolder(test, "/qub/" + publisherName + "/");
                        test.assertThrows(() -> publisherFolder.getSourcesFile(projectName, version), expected);
                    });
                };

                getSourcesFileErrorTest.run("a", null, null, new PreConditionFailure("projectName cannot be null."));
                getSourcesFileErrorTest.run("a", "", null, new PreConditionFailure("projectName cannot be empty."));
                getSourcesFileErrorTest.run("a", "b", null, new PreConditionFailure("version cannot be null."));
                getSourcesFileErrorTest.run("a", "b", "", new PreConditionFailure("version cannot be empty."));

                final Action3<String,String,String> getSourcesFileTest = (String publisherName, String projectName, String version) ->
                {
                    runner.test("with " + English.andList(Iterable.create(publisherName, projectName, version).map(Strings::escapeAndQuote)), (Test test) ->
                    {
                        final QubPublisherFolder publisherFolder = QubPublisherFolderTests.getQubPublisherFolder(test, "/qub/" + publisherName + "/");
                        test.assertEqual(
                            publisherFolder.getFile(projectName + "/versions/" + version + "/" + projectName + ".sources.jar").await(),
                            publisherFolder.getSourcesFile(projectName, version).await());
                    });
                };

                getSourcesFileTest.run("a", "b", "1");
            });

            runner.testGroup("getCompiledTestsFile(String,String)", () ->
            {
                final Action4<String,String,String,Throwable> getCompiledTestsFileErrorTest = (String publisherName, String projectName, String version, Throwable expected) ->
                {
                    runner.test("with " + English.andList(Iterable.create(publisherName, projectName, version).map(Strings::escapeAndQuote)), (Test test) ->
                    {
                        final QubPublisherFolder publisherFolder = QubPublisherFolderTests.getQubPublisherFolder(test, "/qub/" + publisherName + "/");
                        test.assertThrows(() -> publisherFolder.getCompiledTestsFile(projectName, version), expected);
                    });
                };

                getCompiledTestsFileErrorTest.run("a", null, null, new PreConditionFailure("projectName cannot be null."));
                getCompiledTestsFileErrorTest.run("a", "", null, new PreConditionFailure("projectName cannot be empty."));
                getCompiledTestsFileErrorTest.run("a", "b", null, new PreConditionFailure("version cannot be null."));
                getCompiledTestsFileErrorTest.run("a", "b", "", new PreConditionFailure("version cannot be empty."));

                final Action3<String,String,String> getCompiledTestsFileTest = (String publisherName, String projectName, String version) ->
                {
                    runner.test("with " + English.andList(Iterable.create(publisherName, projectName, version).map(Strings::escapeAndQuote)), (Test test) ->
                    {
                        final QubPublisherFolder publisherFolder = QubPublisherFolderTests.getQubPublisherFolder(test, "/qub/" + publisherName + "/");
                        test.assertEqual(
                            publisherFolder.getFile(projectName + "/versions/" + version + "/" + projectName + ".tests.jar").await(),
                            publisherFolder.getCompiledTestsFile(projectName, version).await());
                    });
                };

                getCompiledTestsFileTest.run("a", "b", "1");
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

                    test.assertEqual(false, publisherFolder.equals((QubPublisherFolder)publisherFolder2));
                });

                runner.test("with /qub/me/ and /qub/me/", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = QubPublisherFolderTests.createFileSystem(test);

                    final Folder folder = fileSystem.getFolder("/qub/me/").await();
                    final QubPublisherFolder publisherFolder = QubPublisherFolder.get(folder);

                    final Folder folder2 = fileSystem.getFolder("/qub/me/").await();
                    final QubPublisherFolder publisherFolder2 = QubPublisherFolder.get(folder2);

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
        return QubPublisherFolder.get(QubPublisherFolderTests.getFolder(test, folderPath));
    }

    static QubPublisherFolder createQubPublisherFolder(Test test, String folderPath)
    {
        return QubPublisherFolder.get(QubPublisherFolderTests.createFolder(test, folderPath));
    }
}
