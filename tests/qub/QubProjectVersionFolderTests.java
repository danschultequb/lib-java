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
                    final InMemoryFileSystem fileSystem = new InMemoryFileSystem(test.getClock());
                    fileSystem.createRoot("/").await();
                    final Folder rootFolder = fileSystem.getFolder("/").await();

                    test.assertThrows(() -> QubProjectVersionFolder.get(rootFolder),
                        new PreConditionFailure("projectVersionFolder.getPath().getSegments().getCount() (1) must be greater than or equal to 4."));
                });

                runner.test("with qub folder", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = new InMemoryFileSystem(test.getClock());
                    fileSystem.createRoot("/").await();
                    final Folder qubFolder = fileSystem.getFolder("/").await();

                    test.assertThrows(() -> QubProjectVersionFolder.get(qubFolder),
                        new PreConditionFailure("projectVersionFolder.getPath().getSegments().getCount() (1) must be greater than or equal to 4."));
                });

                runner.test("with publisher folder", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = new InMemoryFileSystem(test.getClock());
                    fileSystem.createRoot("/").await();
                    final Folder publisherFolder = fileSystem.createFolder("/fake-publisher/").await();

                    test.assertThrows(() -> QubProjectVersionFolder.get(publisherFolder),
                        new PreConditionFailure("projectVersionFolder.getPath().getSegments().getCount() (2) must be greater than or equal to 4."));
                });

                runner.test("with project folder", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = new InMemoryFileSystem(test.getClock());
                    fileSystem.createRoot("/").await();
                    final Folder projectVersionFolder = fileSystem.createFolder("/fake-publisher/fake-project/").await();

                    test.assertThrows(() -> QubProjectVersionFolder.get(projectVersionFolder),
                        new PreConditionFailure("projectVersionFolder.getPath().getSegments().getCount() (3) must be greater than or equal to 4."));
                });

                runner.test("with version folder", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = new InMemoryFileSystem(test.getClock());
                    fileSystem.createRoot("/").await();
                    final Folder versionFolder = fileSystem.createFolder("/fake-publisher/fake-project/fake-version/").await();

                    final QubProjectVersionFolder projectVersionFolder = QubProjectVersionFolder.get(versionFolder);
                    test.assertNotNull(projectVersionFolder);
                    test.assertEqual(projectVersionFolder.getPath(), projectVersionFolder.getPath());
                    test.assertEqual("fake-version", projectVersionFolder.getVersion());
                    test.assertEqual("fake-project", projectVersionFolder.getProjectName().await());
                    test.assertEqual("/fake-publisher/fake-project/", projectVersionFolder.getProjectFolder().await().toString());
                    test.assertEqual("fake-publisher", projectVersionFolder.getPublisherName().await());
                    test.assertEqual("/fake-publisher/", projectVersionFolder.getPublisherFolder().await().toString());
                    test.assertEqual("/", projectVersionFolder.getQubFolder().await().toString());
                    test.assertEqual("/fake-publisher/fake-project/fake-version/project.json", projectVersionFolder.getProjectJSONFile().await().toString());
                    test.assertEqual("/fake-publisher/fake-project/fake-version/fake-project.jar", projectVersionFolder.getCompiledSourcesFile().await().toString());
                    test.assertEqual("/fake-publisher/fake-project/fake-version/fake-project.sources.jar", projectVersionFolder.getSourcesFile().await().toString());
                    test.assertEqual("/fake-publisher/fake-project/fake-version/fake-project.tests.jar", projectVersionFolder.getCompiledTestsFile().await().toString());
                    test.assertEqual(
                        new ProjectSignature("fake-publisher", "fake-project", "fake-version"),
                        projectVersionFolder.getProjectSignature().await());
                });
            });
            
            runner.test("getQubFolder()", (Test test) ->
            {
                final String publisherName = "a";
                final String projectName = "b";
                final String version = "1";
                final QubProjectVersionFolder projectVersionFolder = QubProjectVersionFolderTests.getQubProjectVersionFolder(test, "/qub/" + publisherName + "/" + projectName + "/" + version + "/");
                test.assertEqual("/qub/", projectVersionFolder.getQubFolder().await().toString());
            });

            runner.test("getQubFolder2()", (Test test) ->
            {
                final String publisherName = "a";
                final String projectName = "b";
                final String version = "1";
                final QubProjectVersionFolder projectVersionFolder = QubProjectVersionFolderTests.getQubProjectVersionFolder(test, "/qub/" + publisherName + "/" + projectName + "/versions/" + version + "/");
                test.assertEqual("/qub/", projectVersionFolder.getQubFolder2().await().toString());
            });

            runner.test("getPublisherFolder()", (Test test) ->
            {
                final String publisherName = "a";
                final String projectName = "b";
                final String version = "1";
                final QubProjectVersionFolder projectVersionFolder = QubProjectVersionFolderTests.getQubProjectVersionFolder(test, "/qub/" + publisherName + "/" + projectName + "/" + version + "/");
                test.assertEqual("/qub/" + publisherName + "/", projectVersionFolder.getPublisherFolder().await().toString());
            });

            runner.test("getPublisherFolder2()", (Test test) ->
            {
                final String publisherName = "a";
                final String projectName = "b";
                final String version = "1";
                final QubProjectVersionFolder projectVersionFolder = QubProjectVersionFolderTests.getQubProjectVersionFolder(test, "/qub/" + publisherName + "/" + projectName + "/versions/" + version + "/");
                test.assertEqual("/qub/" + publisherName + "/", projectVersionFolder.getPublisherFolder2().await().toString());
            });

            runner.test("getPublisherName()", (Test test) ->
            {
                final String publisherName = "a";
                final String projectName = "b";
                final String version = "1";
                final QubProjectVersionFolder projectVersionFolder = QubProjectVersionFolderTests.getQubProjectVersionFolder(test, "/qub/" + publisherName + "/" + projectName + "/" + version + "/");
                test.assertEqual(publisherName, projectVersionFolder.getPublisherName().await());
            });

            runner.test("getPublisherName2()", (Test test) ->
            {
                final String publisherName = "a";
                final String projectName = "b";
                final String version = "1";
                final QubProjectVersionFolder projectVersionFolder = QubProjectVersionFolderTests.getQubProjectVersionFolder(test, "/qub/" + publisherName + "/" + projectName + "/versions/" + version + "/");
                test.assertEqual(publisherName, projectVersionFolder.getPublisherName2().await());
            });

            runner.testGroup("getProjectVersionFolders()", () ->
            {
                runner.test("with non-existing Qub folder", (Test test) ->
                {
                    final String publisherName = "a";
                    final String projectName = "b";
                    final String version = "1";
                    final QubProjectVersionFolder projectVersionFolder = QubProjectVersionFolderTests.getQubProjectVersionFolder(test, "/qub/" + publisherName + "/" + projectName + "/" + version + "/");
                    test.assertEqual(Iterable.create(), projectVersionFolder.getProjectVersionFolders().await());
                });

                runner.test("with non-existing publisher folder", (Test test) ->
                {
                    final String publisherName = "a";
                    final String projectName = "b";
                    final String version = "1";
                    final QubProjectVersionFolder projectVersionFolder = QubProjectVersionFolderTests.getQubProjectVersionFolder(test, "/qub/" + publisherName + "/" + projectName + "/" + version + "/");
                    projectVersionFolder.getQubFolder().await().create().await();
                    test.assertEqual(Iterable.create(), projectVersionFolder.getProjectVersionFolders().await());
                });

                runner.test("with non-existing project folder", (Test test) ->
                {
                    final String publisherName = "a";
                    final String projectName = "b";
                    final String version = "1";
                    final QubProjectVersionFolder projectVersionFolder = QubProjectVersionFolderTests.getQubProjectVersionFolder(test, "/qub/" + publisherName + "/" + projectName + "/" + version + "/");
                    projectVersionFolder.getPublisherFolder().await().create().await();
                    test.assertEqual(Iterable.create(), projectVersionFolder.getProjectVersionFolders().await());
                });

                runner.test("with non-existing versions folder", (Test test) ->
                {
                    final String publisherName = "a";
                    final String projectName = "b";
                    final String version = "1";
                    final QubProjectVersionFolder projectVersionFolder = QubProjectVersionFolderTests.getQubProjectVersionFolder(test, "/qub/" + publisherName + "/" + projectName + "/" + version + "/");
                    projectVersionFolder.getProjectFolder().await().create().await();
                    test.assertEqual(Iterable.create(), projectVersionFolder.getProjectVersionFolders().await());
                });

                runner.test("with one version folder", (Test test) ->
                {
                    final String publisherName = "a";
                    final String projectName = "b";
                    final String version = "1";
                    final QubProjectVersionFolder projectVersionFolder = QubProjectVersionFolderTests.getQubProjectVersionFolder(test, "/qub/" + publisherName + "/" + projectName + "/" + version + "/");
                    projectVersionFolder.create().await();
                    test.assertEqual(
                        Iterable.create(
                            projectVersionFolder),
                        projectVersionFolder.getProjectVersionFolders().await());
                });
            });

            runner.testGroup("getProjectVersionFolders2()", () ->
            {
                runner.test("with non-existing Qub folder", (Test test) ->
                {
                    final String publisherName = "a";
                    final String projectName = "b";
                    final String version = "1";
                    final QubProjectVersionFolder projectVersionFolder = QubProjectVersionFolderTests.getQubProjectVersionFolder(test, "/qub/" + publisherName + "/" + projectName + "/versions/" + version + "/");
                    test.assertEqual(Iterable.create(), projectVersionFolder.getProjectVersionFolders2().await());
                });

                runner.test("with non-existing publisher folder", (Test test) ->
                {
                    final String publisherName = "a";
                    final String projectName = "b";
                    final String version = "1";
                    final QubProjectVersionFolder projectVersionFolder = QubProjectVersionFolderTests.getQubProjectVersionFolder(test, "/qub/" + publisherName + "/" + projectName + "/versions/" + version + "/");
                    projectVersionFolder.getQubFolder().await().create().await();
                    test.assertEqual(Iterable.create(), projectVersionFolder.getProjectVersionFolders2().await());
                });

                runner.test("with non-existing project folder", (Test test) ->
                {
                    final String publisherName = "a";
                    final String projectName = "b";
                    final String version = "1";
                    final QubProjectVersionFolder projectVersionFolder = QubProjectVersionFolderTests.getQubProjectVersionFolder(test, "/qub/" + publisherName + "/" + projectName + "/versions/" + version + "/");
                    projectVersionFolder.getPublisherFolder().await().create().await();
                    test.assertEqual(Iterable.create(), projectVersionFolder.getProjectVersionFolders2().await());
                });

                runner.test("with non-existing versions folder", (Test test) ->
                {
                    final String publisherName = "a";
                    final String projectName = "b";
                    final String version = "1";
                    final QubProjectVersionFolder projectVersionFolder = QubProjectVersionFolderTests.getQubProjectVersionFolder(test, "/qub/" + publisherName + "/" + projectName + "/versions/" + version + "/");
                    projectVersionFolder.getProjectFolder2().await().create().await();
                    test.assertEqual(Iterable.create(), projectVersionFolder.getProjectVersionFolders2().await());
                });

                runner.test("with empty versions folder", (Test test) ->
                {
                    final String publisherName = "a";
                    final String projectName = "b";
                    final String version = "1";
                    final QubProjectVersionFolder projectVersionFolder = QubProjectVersionFolderTests.getQubProjectVersionFolder(test, "/qub/" + publisherName + "/" + projectName + "/versions/" + version + "/");
                    projectVersionFolder.getProjectVersionsFolder2().await().create().await();
                    test.assertEqual(Iterable.create(), projectVersionFolder.getProjectVersionFolders2().await());
                });

                runner.test("with one version folder", (Test test) ->
                {
                    final String publisherName = "a";
                    final String projectName = "b";
                    final String version = "1";
                    final QubProjectVersionFolder projectVersionFolder = QubProjectVersionFolderTests.getQubProjectVersionFolder(test, "/qub/" + publisherName + "/" + projectName + "/versions/" + version + "/");
                    projectVersionFolder.create().await();
                    test.assertEqual(
                        Iterable.create(
                            projectVersionFolder),
                        projectVersionFolder.getProjectVersionFolders2().await());
                });
            });

            runner.testGroup("getProjectJSONFile()", () ->
            {
                final Action3<String,String,String> getProjectJSONFileTest = (String publisherName, String projectName, String version) ->
                {
                    runner.test("with " + English.andList(Iterable.create(publisherName, projectName, version).map(Strings::escapeAndQuote)), (Test test) ->
                    {
                        final QubProjectVersionFolder projectVersionFolder = QubProjectVersionFolderTests.getQubProjectVersionFolder(test, "/qub/" + publisherName + "/" + projectName + "/" + version + "/");
                        test.assertEqual(
                            projectVersionFolder.getFile("project.json").await(),
                            projectVersionFolder.getProjectJSONFile().await());
                    });
                };

                getProjectJSONFileTest.run("a", "b", "c");
            });

            runner.testGroup("getCompiledSourcesFile()", () ->
            {
                final Action3<String,String,String> getCompiledSourcesFileTest = (String publisherName, String projectName, String version) ->
                {
                    runner.test("with " + English.andList(Iterable.create(publisherName, projectName, version).map(Strings::escapeAndQuote)), (Test test) ->
                    {
                        final QubProjectVersionFolder projectVersionFolder = QubProjectVersionFolderTests.getQubProjectVersionFolder(test, "/qub/" + publisherName + "/" + projectName + "/" + version + "/");
                        test.assertEqual(
                            projectVersionFolder.getFile(projectName + ".jar").await(),
                            projectVersionFolder.getCompiledSourcesFile().await());
                    });
                };

                getCompiledSourcesFileTest.run("a", "b", "1");
            });

            runner.testGroup("getCompiledSourcesFile2()", () ->
            {
                final Action3<String,String,String> getCompiledSourcesFile2Test = (String publisherName, String projectName, String version) ->
                {
                    runner.test("with " + English.andList(Iterable.create(publisherName, projectName, version).map(Strings::escapeAndQuote)), (Test test) ->
                    {
                        final QubProjectVersionFolder projectVersionFolder = QubProjectVersionFolderTests.getQubProjectVersionFolder(test, "/qub/" + publisherName + "/" + projectName + "/versions/" + version + "/");
                        test.assertEqual(
                            projectVersionFolder.getFile(projectName + ".jar").await(),
                            projectVersionFolder.getCompiledSourcesFile2().await());
                    });
                };

                getCompiledSourcesFile2Test.run("a", "b", "1");
            });

            runner.testGroup("getSourcesFile(String)", () ->
            {
                final Action3<String,String,String> getSourcesFileTest = (String publisherName, String projectName, String version) ->
                {
                    runner.test("with " + English.andList(Iterable.create(publisherName, projectName, version).map(Strings::escapeAndQuote)), (Test test) ->
                    {
                        final QubProjectVersionFolder projectVersionFolder = QubProjectVersionFolderTests.getQubProjectVersionFolder(test, "/qub/" + publisherName + "/" + projectName + "/" + version + "/");
                        test.assertEqual(
                            projectVersionFolder.getFile(projectName + ".sources.jar").await(),
                            projectVersionFolder.getSourcesFile().await());
                    });
                };

                getSourcesFileTest.run("a", "b", "1");
            });

            runner.testGroup("getSourcesFile2(String)", () ->
            {
                final Action3<String,String,String> getSourcesFile2Test = (String publisherName, String projectName, String version) ->
                {
                    runner.test("with " + English.andList(Iterable.create(publisherName, projectName, version).map(Strings::escapeAndQuote)), (Test test) ->
                    {
                        final QubProjectVersionFolder projectVersionFolder = QubProjectVersionFolderTests.getQubProjectVersionFolder(test, "/qub/" + publisherName + "/" + projectName + "/versions/" + version + "/");
                        test.assertEqual(
                            projectVersionFolder.getFile(projectName + ".sources.jar").await(),
                            projectVersionFolder.getSourcesFile2().await());
                    });
                };

                getSourcesFile2Test.run("a", "b", "1");
            });

            runner.testGroup("getCompiledTestsFile(String)", () ->
            {
                final Action3<String,String,String> getCompiledTestsFileTest = (String publisherName, String projectName, String version) ->
                {
                    runner.test("with " + English.andList(Iterable.create(publisherName, projectName, version).map(Strings::escapeAndQuote)), (Test test) ->
                    {
                        final QubProjectVersionFolder projectVersionFolder = QubProjectVersionFolderTests.getQubProjectVersionFolder(test, "/qub/" + publisherName + "/" + projectName + "/" + version + "/");
                        test.assertEqual(
                            projectVersionFolder.getFile(projectName + ".tests.jar").await(),
                            projectVersionFolder.getCompiledTestsFile().await());
                    });
                };

                getCompiledTestsFileTest.run("a", "b", "1");
            });

            runner.testGroup("getCompiledTestsFile2(String)", () ->
            {
                final Action3<String,String,String> getCompiledTestsFile2Test = (String publisherName, String projectName, String version) ->
                {
                    runner.test("with " + English.andList(Iterable.create(publisherName, projectName, version).map(Strings::escapeAndQuote)), (Test test) ->
                    {
                        final QubProjectVersionFolder projectVersionFolder = QubProjectVersionFolderTests.getQubProjectVersionFolder(test, "/qub/" + publisherName + "/" + projectName + "/versions/" + version + "/");
                        test.assertEqual(
                            projectVersionFolder.getFile(projectName + ".tests.jar").await(),
                            projectVersionFolder.getCompiledTestsFile2().await());
                    });
                };

                getCompiledTestsFile2Test.run("a", "b", "1");
            });

            runner.testGroup("getProjectSignature()", () ->
            {
                final Action3<String,String,String> getProjectSignatureTest = (String publisherName, String projectName, String version) ->
                {
                    runner.test("with " + English.andList(Iterable.create(publisherName, projectName, version).map(Strings::escapeAndQuote)), (Test test) ->
                    {
                        final QubProjectVersionFolder projectVersionFolder = QubProjectVersionFolderTests.getQubProjectVersionFolder(test, "/qub/" + publisherName + "/" + projectName + "/" + version + "/");
                        test.assertEqual(
                            new ProjectSignature(publisherName, projectName, version),
                            projectVersionFolder.getProjectSignature().await());
                    });
                };

                getProjectSignatureTest.run("a", "b", "1");
            });

            runner.testGroup("getProjectSignature2()", () ->
            {
                final Action3<String,String,String> getProjectSignature2Test = (String publisherName, String projectName, String version) ->
                {
                    runner.test("with " + English.andList(Iterable.create(publisherName, projectName, version).map(Strings::escapeAndQuote)), (Test test) ->
                    {
                        final QubProjectVersionFolder projectVersionFolder = QubProjectVersionFolderTests.getQubProjectVersionFolder(test, "/qub/" + publisherName + "/" + projectName + "/versions/" + version + "/");
                        test.assertEqual(
                            new ProjectSignature(publisherName, projectName, version),
                            projectVersionFolder.getProjectSignature2().await());
                    });
                };

                getProjectSignature2Test.run("a", "b", "1");
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

            runner.testGroup("getProjectVersionsFolder2()", () ->
            {
                final Action3<String,String,String> getProjectVersionsFolder2Test = (String publisherName, String projectName, String version) ->
                {
                    runner.test("with " + English.andList(Iterable.create(publisherName, projectName, version).map(Strings::escapeAndQuote)), (Test test) ->
                    {
                        final QubProjectVersionFolder projectVersionFolder = QubProjectVersionFolderTests.getQubProjectVersionFolder(test, "/qub/" + publisherName + "/" + projectName + "/versions/" + version + "/");
                        test.assertEqual(
                            projectVersionFolder.getProjectFolder2().await().getProjectVersionsFolder().await(),
                            projectVersionFolder.getProjectVersionsFolder2().await());
                    });
                };

                getProjectVersionsFolder2Test.run("a", "b", "1");
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

            runner.testGroup("getProjectDataFolder2()", () ->
            {
                final Action3<String,String,String> getProjectDataFolder2Test = (String publisherName, String projectName, String version) ->
                {
                    runner.test("with " + English.andList(Iterable.create(publisherName, projectName, version).map(Strings::escapeAndQuote)), (Test test) ->
                    {
                        final QubProjectVersionFolder projectVersionFolder = QubProjectVersionFolderTests.getQubProjectVersionFolder(test, "/qub/" + publisherName + "/" + projectName + "/versions/" + version + "/");
                        test.assertEqual(
                            projectVersionFolder.getProjectFolder2().await().getProjectDataFolder().await(),
                            projectVersionFolder.getProjectDataFolder2().await());
                    });
                };

                getProjectDataFolder2Test.run("a", "b", "1");
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
                runner.test("with /qub/me/spam and null", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = QubPublisherFolderTests.createFileSystem(test);
                    final Folder folder = fileSystem.getFolder("/qub/me/spam/").await();
                    final QubProjectVersionFolder projectVersionFolder = QubProjectVersionFolder.get(folder);
                    test.assertEqual(false, projectVersionFolder.equals((QubProjectVersionFolder)null));
                });

                runner.test("with /qub/me/project1/1/ and /other/thing/project2/2/", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = QubPublisherFolderTests.createFileSystem(test);

                    final Folder folder = fileSystem.getFolder("/qub/me/project1/1/").await();
                    final QubProjectVersionFolder projectVersionFolder = QubProjectVersionFolder.get(folder);

                    final Folder folder2 = fileSystem.getFolder("/other/thing/project2/2/").await();
                    final QubProjectVersionFolder projectVersionFolder2 = QubProjectVersionFolder.get(folder2);

                    test.assertEqual(false, projectVersionFolder.equals((QubProjectVersionFolder)projectVersionFolder2));
                });

                runner.test("with /qub/me/proj/3/ and /qub/me/proj/3/", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = QubPublisherFolderTests.createFileSystem(test);

                    final Folder folder = fileSystem.getFolder("/qub/me/proj/3/").await();
                    final QubProjectVersionFolder projectVersionFolder = QubProjectVersionFolder.get(folder);

                    final Folder folder2 = fileSystem.getFolder("/qub/me/proj/3/").await();
                    final QubProjectVersionFolder projectVersionFolder2 = QubProjectVersionFolder.get(folder2);

                    test.assertEqual(true, projectVersionFolder.equals((QubProjectVersionFolder)projectVersionFolder2));
                });
            });

            runner.test("toString()", (Test test) ->
            {
                final QubProjectVersionFolder projectVersionFolder = QubProjectVersionFolderTests.getQubProjectVersionFolder(test, "/qub/me/proj/4/");
                test.assertEqual("/qub/me/proj/4/", projectVersionFolder.toString());
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

    static QubProjectVersionFolder createQubProjectVersionFolder(Test test, String folderPath)
    {
        return QubProjectVersionFolder.get(QubProjectVersionFolderTests.createFolder(test, folderPath));
    }
}
