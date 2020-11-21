package qub;

public interface QubFolderTests
{
    static void test(TestRunner runner)
    {
        PreCondition.assertNotNull(runner, "runner");

        runner.testGroup(QubFolder.class, () ->
        {
            runner.testGroup("get(Folder)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> QubFolder.get(null),
                        new PreConditionFailure("qubFolder cannot be null."));
                });

                runner.test("with folder that doesn't exist", (Test test) ->
                {
                    final Folder folder = QubFolderTests.getFolder(test, "/qub/");
                    final QubFolder qubFolder = QubFolder.get(folder);
                    test.assertEqual(folder.getPath(), qubFolder.getPath());
                    test.assertFalse(qubFolder.exists().await());
                });

                runner.test("with folder that exists", (Test test) ->
                {
                    final Folder folder = QubFolderTests.createFolder(test, "/qub/");
                    final QubFolder qubFolder = QubFolder.get(folder);
                    test.assertEqual(folder.getPath(), qubFolder.getPath());
                    test.assertTrue(qubFolder.exists().await());
                });
            });

            runner.testGroup("getShortcutFile(String)", () ->
            {
                final Action2<String,Throwable> getShortcutFileErrorTest = (String shortcutName, Throwable expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(shortcutName), (Test test) ->
                    {
                        final QubFolder qubFolder = QubFolderTests.getQubFolder(test, "/qub/");
                        test.assertThrows(() -> qubFolder.getShortcutFile(shortcutName),
                            expected);
                    });
                };

                getShortcutFileErrorTest.run(null, new PreConditionFailure("shortcutName cannot be null."));
                getShortcutFileErrorTest.run("", new PreConditionFailure("shortcutName cannot be empty."));

                final Action1<String> getShortcutFileTest = (String shortcutName) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(shortcutName), (Test test) ->
                    {
                        final QubFolder qubFolder = QubFolderTests.getQubFolder(test, "/qub/");
                        final File shortcutFile = qubFolder.getShortcutFile(shortcutName).await();
                        test.assertNotNull(shortcutFile);
                        test.assertEqual(shortcutName, shortcutFile.getName());
                        test.assertEqual(qubFolder.getPath(), shortcutFile.getParentFolder().await().getPath());
                    });
                };

                getShortcutFileTest.run("apple");
                getShortcutFileTest.run("apple.bat");
                getShortcutFileTest.run("qub-pack");
                getShortcutFileTest.run("qub-pack.cmd");
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
                    final QubFolder qubFolder = QubFolder.get(folder);
                    test.assertEqual(Iterable.create(), qubFolder.getPublisherFolders().await());
                });

                runner.test("with existing QubFolder with folders", (Test test) ->
                {
                    final Folder folder = QubFolderTests.createFolder(test, "/qub/");
                    final Folder subFolder = folder.createFolder("publisher").await();
                    final QubFolder qubFolder = QubFolder.get(folder);
                    test.assertEqual(
                        Iterable.create(
                            QubPublisherFolder.get(subFolder)),
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
                    test.assertEqual(Path.parse("/qub/spam/"), qubPublisherFolder.getPath());
                });

                runner.test("with existing publisher", (Test test) ->
                {
                    final Folder folder = QubFolderTests.createFolder(test, "/qub/");
                    final QubFolder qubFolder = QubFolder.get(folder);

                    folder.createFolder("spam").await();
                    final QubPublisherFolder qubPublisherFolder = qubFolder.getPublisherFolder("spam").await();
                    test.assertNotNull(qubPublisherFolder);
                    test.assertEqual(Path.parse("/qub/spam/"), qubPublisherFolder.getPath());
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
                    final QubFolder qubFolder = QubFolder.get(folder);
                    test.assertEqual(Iterable.create(), qubFolder.getProjectFolders("me").await());
                });

                runner.test("with existing empty QubPublisherFolder", (Test test) ->
                {
                    final Folder folder = QubFolderTests.createFolder(test, "/qub/");
                    folder.createFolder("me").await();
                    final QubFolder qubFolder = QubFolder.get(folder);
                    test.assertEqual(Iterable.create(), qubFolder.getProjectFolders("me").await());
                });

                runner.test("with existing QubProjectFolder", (Test test) ->
                {
                    final Folder folder = QubFolderTests.createFolder(test, "/qub/");
                    final Folder subFolder = folder.createFolder("me/my-project/").await();
                    final QubFolder qubFolder = QubFolder.get(folder);
                    test.assertEqual(
                        Iterable.create(
                            QubProjectFolder.get(subFolder)),
                        qubFolder.getProjectFolders("me").await());
                });
            });

            runner.testGroup("getProjectFolder(String,String)", () ->
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
                    test.assertEqual(Path.parse("/qub/spam/my-project/"), projectFolder.getPath());
                });

                runner.test("with non-existing project", (Test test) ->
                {
                    final Folder folder = QubFolderTests.createFolder(test, "/qub/");
                    final QubFolder qubFolder = QubFolder.get(folder);

                    folder.createFolder("spam").await();
                    final QubProjectFolder projectFolder = qubFolder.getProjectFolder("spam", "my-project").await();
                    test.assertNotNull(projectFolder);
                    test.assertEqual(Path.parse("/qub/spam/my-project/"), projectFolder.getPath());
                });

                runner.test("with existing project", (Test test) ->
                {
                    final Folder folder = QubFolderTests.createFolder(test, "/qub/");
                    final QubFolder qubFolder = QubFolder.get(folder);

                    folder.createFolder("spam/my-project/").await();
                    final QubProjectFolder projectFolder = qubFolder.getProjectFolder("spam", "my-project").await();
                    test.assertNotNull(projectFolder);
                    test.assertEqual(Path.parse("/qub/spam/my-project/"), projectFolder.getPath());
                });
            });

            runner.testGroup("getProjectVersionFolders(String,String)", () ->
            {
                final Action3<String,String,Throwable> getProjectVersionFoldersErrorTest = (String publisherName, String projectName, Throwable expected) ->
                {
                    runner.test("with " + English.andList(Iterable.create(publisherName, projectName).map(Strings::escapeAndQuote)), (Test test) ->
                    {
                        final QubFolder qubFolder = QubFolderTests.getQubFolder(test, "/qub/");
                        test.assertThrows(() -> qubFolder.getProjectVersionFolders(publisherName, projectName), expected);
                    });
                };

                getProjectVersionFoldersErrorTest.run(null, null, new PreConditionFailure("publisherName cannot be null."));
                getProjectVersionFoldersErrorTest.run("", null, new PreConditionFailure("publisherName cannot be empty."));
                getProjectVersionFoldersErrorTest.run("a", null, new PreConditionFailure("projectName cannot be null."));
                getProjectVersionFoldersErrorTest.run("a", "", new PreConditionFailure("projectName cannot be empty."));

                runner.test("with non-existing Qub folder", (Test test) ->
                {
                    final QubFolder qubFolder = QubFolderTests.getQubFolder(test, "/qub/");
                    final String publisherName = "a";
                    final String projectName = "b";
                    test.assertEqual(Iterable.create(), qubFolder.getProjectVersionFolders(publisherName, projectName).await());
                });

                runner.test("with non-existing publisher folder", (Test test) ->
                {
                    final QubFolder qubFolder = QubFolderTests.getQubFolder(test, "/qub/");
                    qubFolder.create().await();
                    final String publisherName = "a";
                    final String projectName = "b";
                    test.assertEqual(Iterable.create(), qubFolder.getProjectVersionFolders(publisherName, projectName).await());
                });

                runner.test("with non-existing project folder", (Test test) ->
                {
                    final QubFolder qubFolder = QubFolderTests.getQubFolder(test, "/qub/");
                    final String publisherName = "a";
                    qubFolder.getPublisherFolder(publisherName).await().create().await();
                    final String projectName = "b";
                    test.assertEqual(Iterable.create(), qubFolder.getProjectVersionFolders(publisherName, projectName).await());
                });

                runner.test("with empty project folder", (Test test) ->
                {
                    final QubFolder qubFolder = QubFolderTests.getQubFolder(test, "/qub/");
                    final String publisherName = "a";
                    final String projectName = "b";
                    final QubProjectFolder projectFolder = qubFolder.getProjectFolder(publisherName, projectName).await();
                    projectFolder.create().await();
                    test.assertEqual(Iterable.create(), qubFolder.getProjectVersionFolders(publisherName, projectName).await());
                });

                runner.test("with one version folder", (Test test) ->
                {
                    final QubFolder qubFolder = QubFolderTests.getQubFolder(test, "/qub/");
                    final String publisherName = "a";
                    final String projectName = "b";
                    final QubProjectFolder projectFolder = qubFolder.getProjectFolder(publisherName, projectName).await();
                    projectFolder.createFolder("versions/1").await();
                    test.assertEqual(
                        Iterable.create(
                            qubFolder.getProjectVersionFolder(publisherName, projectName, "1").await()),
                        qubFolder.getProjectVersionFolders(publisherName, projectName).await());
                });
            });

            runner.testGroup("getProjectVersionFolder(String,String,String)", () ->
            {
                final Action4<String,String,String,Throwable> getProjectVersionFolderErrorTest = (String publisherName, String projectName, String version, Throwable expected) ->
                {
                    runner.test("with " + English.andList(Iterable.create(publisherName, projectName, version).map(Strings::escapeAndQuote)), (Test test) ->
                    {
                        final QubFolder qubFolder = QubFolderTests.getQubFolder(test, "/qub/");
                        test.assertThrows(() -> qubFolder.getProjectVersionFolder(publisherName, projectName, version), expected);
                    });
                };

                getProjectVersionFolderErrorTest.run(null, null, null, new PreConditionFailure("publisherName cannot be null."));
                getProjectVersionFolderErrorTest.run("", null, null, new PreConditionFailure("publisherName cannot be empty."));
                getProjectVersionFolderErrorTest.run("a", null, null, new PreConditionFailure("projectName cannot be null."));
                getProjectVersionFolderErrorTest.run("a", "", null, new PreConditionFailure("projectName cannot be empty."));
                getProjectVersionFolderErrorTest.run("a", "b", null, new PreConditionFailure("version cannot be null."));
                getProjectVersionFolderErrorTest.run("a", "b", "", new PreConditionFailure("version cannot be empty."));

                runner.test("with non-existing Qub folder", (Test test) ->
                {
                    final QubFolder qubFolder = QubFolderTests.getQubFolder(test, "/qub/");
                    final String publisherName = "a";
                    final String projectName = "b";
                    final String version = "1";
                    test.assertEqual(
                        qubFolder.getFolder(publisherName + "/" + projectName + "/versions/" + version).await(),
                        qubFolder.getProjectVersionFolder(publisherName, projectName, version).await());
                });

                runner.test("with non-existing publisher folder", (Test test) ->
                {
                    final QubFolder qubFolder = QubFolderTests.getQubFolder(test, "/qub/");
                    qubFolder.create().await();
                    final String publisherName = "a";
                    final String projectName = "b";
                    final String version = "1";
                    test.assertEqual(
                        qubFolder.getFolder(publisherName + "/" + projectName + "/versions/" + version).await(),
                        qubFolder.getProjectVersionFolder(publisherName, projectName, version).await());
                });

                runner.test("with non-existing project folder", (Test test) ->
                {
                    final QubFolder qubFolder = QubFolderTests.getQubFolder(test, "/qub/");
                    final String publisherName = "a";
                    qubFolder.getPublisherFolder(publisherName).await().create().await();
                    final String projectName = "b";
                    final String version = "1";
                    test.assertEqual(
                        qubFolder.getFolder(publisherName + "/" + projectName + "/versions/" + version).await(),
                        qubFolder.getProjectVersionFolder(publisherName, projectName, version).await());
                });

                runner.test("with non-existing versions folder", (Test test) ->
                {
                    final QubFolder qubFolder = QubFolderTests.getQubFolder(test, "/qub/");
                    final String publisherName = "a";
                    final String projectName = "b";
                    qubFolder.getProjectFolder(publisherName, projectName).await().create().await();
                    final String version = "1";
                    test.assertEqual(
                        qubFolder.getFolder(publisherName + "/" + projectName + "/versions/" + version).await(),
                        qubFolder.getProjectVersionFolder(publisherName, projectName, version).await());
                });

                runner.test("with empty versions folder", (Test test) ->
                {
                    final QubFolder qubFolder = QubFolderTests.getQubFolder(test, "/qub/");
                    final String publisherName = "a";
                    final String projectName = "b";
                    final QubProjectFolder projectFolder = qubFolder.getProjectFolder(publisherName, projectName).await();
                    projectFolder.createFolder("versions").await();
                    final String version = "1";
                    test.assertEqual(
                        qubFolder.getFolder(publisherName + "/" + projectName + "/versions/" + version).await(),
                        qubFolder.getProjectVersionFolder(publisherName, projectName, version).await());
                });

                runner.test("with non-existing project.json file", (Test test) ->
                {
                    final QubFolder qubFolder = QubFolderTests.getQubFolder(test, "/qub/");
                    final String publisherName = "a";
                    final String projectName = "b";
                    final String version = "1";
                    final QubProjectVersionFolder projectVersionFolder = qubFolder.getProjectVersionFolder(publisherName, projectName, version).await();
                    projectVersionFolder.create().await();
                    test.assertEqual(
                        qubFolder.getFolder(publisherName + "/" + projectName + "/versions/" + version).await(),
                        qubFolder.getProjectVersionFolder(publisherName, projectName, version).await());
                });

                runner.test("with existing project.json file", (Test test) ->
                {
                    final QubFolder qubFolder = QubFolderTests.getQubFolder(test, "/qub/");
                    final String publisherName = "a";
                    final String projectName = "b";
                    final String version = "1";
                    final QubProjectVersionFolder projectVersionFolder = qubFolder.getProjectVersionFolder(publisherName, projectName, version).await();
                    projectVersionFolder.createFile("project.json").await();
                    test.assertEqual(
                        qubFolder.getFolder(publisherName + "/" + projectName + "/versions/" + version).await(),
                        qubFolder.getProjectVersionFolder(publisherName, projectName, version).await());
                });
            });

            runner.testGroup("getProjectVersionFolder(String,String,VersionNumber)", () ->
            {
                final Action4<String,String,VersionNumber,Throwable> getProjectVersionFolderErrorTest = (String publisherName, String projectName, VersionNumber version, Throwable expected) ->
                {
                    runner.test("with " + English.andList(Iterable.create(publisherName, projectName, version).map(Strings::escapeAndQuote)), (Test test) ->
                    {
                        final QubFolder qubFolder = QubFolderTests.getQubFolder(test, "/qub/");
                        test.assertThrows(() -> qubFolder.getProjectVersionFolder(publisherName, projectName, version), expected);
                    });
                };

                getProjectVersionFolderErrorTest.run(null, null, null, new PreConditionFailure("publisherName cannot be null."));
                getProjectVersionFolderErrorTest.run("", null, null, new PreConditionFailure("publisherName cannot be empty."));
                getProjectVersionFolderErrorTest.run("a", null, null, new PreConditionFailure("projectName cannot be null."));
                getProjectVersionFolderErrorTest.run("a", "", null, new PreConditionFailure("projectName cannot be empty."));
                getProjectVersionFolderErrorTest.run("a", "b", null, new PreConditionFailure("version cannot be null."));

                runner.test("with non-existing Qub folder", (Test test) ->
                {
                    final QubFolder qubFolder = QubFolderTests.getQubFolder(test, "/qub/");
                    final String publisherName = "a";
                    final String projectName = "b";
                    final VersionNumber version = VersionNumber.create().setMajor(1);
                    test.assertEqual(
                        qubFolder.getFolder(publisherName + "/" + projectName + "/versions/" + version).await(),
                        qubFolder.getProjectVersionFolder(publisherName, projectName, version).await());
                });

                runner.test("with non-existing publisher folder", (Test test) ->
                {
                    final QubFolder qubFolder = QubFolderTests.getQubFolder(test, "/qub/");
                    qubFolder.create().await();
                    final String publisherName = "a";
                    final String projectName = "b";
                    final VersionNumber version = VersionNumber.create().setMajor(1);
                    test.assertEqual(
                        qubFolder.getFolder(publisherName + "/" + projectName + "/versions/" + version).await(),
                        qubFolder.getProjectVersionFolder(publisherName, projectName, version).await());
                });

                runner.test("with non-existing project folder", (Test test) ->
                {
                    final QubFolder qubFolder = QubFolderTests.getQubFolder(test, "/qub/");
                    final String publisherName = "a";
                    qubFolder.getPublisherFolder(publisherName).await().create().await();
                    final String projectName = "b";
                    final VersionNumber version = VersionNumber.create().setMajor(1);
                    test.assertEqual(
                        qubFolder.getFolder(publisherName + "/" + projectName + "/versions/" + version).await(),
                        qubFolder.getProjectVersionFolder(publisherName, projectName, version).await());
                });

                runner.test("with non-existing versions folder", (Test test) ->
                {
                    final QubFolder qubFolder = QubFolderTests.getQubFolder(test, "/qub/");
                    final String publisherName = "a";
                    final String projectName = "b";
                    qubFolder.getProjectFolder(publisherName, projectName).await().create().await();
                    final VersionNumber version = VersionNumber.create().setMajor(1);
                    test.assertEqual(
                        qubFolder.getFolder(publisherName + "/" + projectName + "/versions/" + version).await(),
                        qubFolder.getProjectVersionFolder(publisherName, projectName, version).await());
                });

                runner.test("with empty versions folder", (Test test) ->
                {
                    final QubFolder qubFolder = QubFolderTests.getQubFolder(test, "/qub/");
                    final String publisherName = "a";
                    final String projectName = "b";
                    final QubProjectFolder projectFolder = qubFolder.getProjectFolder(publisherName, projectName).await();
                    projectFolder.createFolder("versions").await();
                    final VersionNumber version = VersionNumber.create().setMajor(1);
                    test.assertEqual(
                        qubFolder.getFolder(publisherName + "/" + projectName + "/versions/" + version).await(),
                        qubFolder.getProjectVersionFolder(publisherName, projectName, version).await());
                });

                runner.test("with non-existing project.json file", (Test test) ->
                {
                    final QubFolder qubFolder = QubFolderTests.getQubFolder(test, "/qub/");
                    final String publisherName = "a";
                    final String projectName = "b";
                    final VersionNumber version = VersionNumber.create().setMajor(1);
                    final QubProjectVersionFolder projectVersionFolder = qubFolder.getProjectVersionFolder(publisherName, projectName, version).await();
                    projectVersionFolder.create().await();
                    test.assertEqual(
                        qubFolder.getFolder(publisherName + "/" + projectName + "/versions/" + version).await(),
                        qubFolder.getProjectVersionFolder(publisherName, projectName, version).await());
                });

                runner.test("with existing project.json file", (Test test) ->
                {
                    final QubFolder qubFolder = QubFolderTests.getQubFolder(test, "/qub/");
                    final String publisherName = "a";
                    final String projectName = "b";
                    final VersionNumber version = VersionNumber.create().setMajor(1);
                    final QubProjectVersionFolder projectVersionFolder = qubFolder.getProjectVersionFolder(publisherName, projectName, version).await();
                    projectVersionFolder.createFile("project.json").await();
                    test.assertEqual(
                        qubFolder.getFolder(publisherName + "/" + projectName + "/versions/" + version).await(),
                        qubFolder.getProjectVersionFolder(publisherName, projectName, version).await());
                });
            });

            runner.testGroup("getProjectVersionFolder(ProjectSignature)", () ->
            {
                final Action2<ProjectSignature,Throwable> getProjectVersionFolderErrorTest = (ProjectSignature projectSignature, Throwable expected) ->
                {
                    runner.test("with " + projectSignature, (Test test) ->
                    {
                        final QubFolder qubFolder = QubFolderTests.getQubFolder(test, "/qub/");
                        test.assertThrows(() -> qubFolder.getProjectVersionFolder(projectSignature), expected);
                    });
                };

                getProjectVersionFolderErrorTest.run(null, new PreConditionFailure("projectSignature cannot be null."));

                runner.test("with non-existing Qub folder", (Test test) ->
                {
                    final QubFolder qubFolder = QubFolderTests.getQubFolder(test, "/qub/");
                    final ProjectSignature projectSignature = ProjectSignature.create("a", "b", "1");
                    test.assertEqual(
                        qubFolder.getFolder(projectSignature.getPublisher() + "/" + projectSignature.getProject() + "/versions/" + projectSignature.getVersion()).await(),
                        qubFolder.getProjectVersionFolder(projectSignature).await());
                });

                runner.test("with non-existing publisher folder", (Test test) ->
                {
                    final QubFolder qubFolder = QubFolderTests.getQubFolder(test, "/qub/");
                    qubFolder.create().await();
                    final ProjectSignature projectSignature = ProjectSignature.create("a", "b", "1");
                    test.assertEqual(
                        qubFolder.getFolder(projectSignature.getPublisher() + "/" + projectSignature.getProject() + "/versions/" + projectSignature.getVersion()).await(),
                        qubFolder.getProjectVersionFolder(projectSignature).await());
                });

                runner.test("with non-existing project folder", (Test test) ->
                {
                    final QubFolder qubFolder = QubFolderTests.getQubFolder(test, "/qub/");
                    final ProjectSignature projectSignature = ProjectSignature.create("a", "b", "1");
                    qubFolder.getPublisherFolder(projectSignature.getPublisher()).await().create().await();
                    test.assertEqual(
                        qubFolder.getFolder(projectSignature.getPublisher() + "/" + projectSignature.getProject() + "/versions/" + projectSignature.getVersion()).await(),
                        qubFolder.getProjectVersionFolder(projectSignature).await());
                });

                runner.test("with non-existing versions folder", (Test test) ->
                {
                    final QubFolder qubFolder = QubFolderTests.getQubFolder(test, "/qub/");
                    final ProjectSignature projectSignature = ProjectSignature.create("a", "b", "1");
                    qubFolder.getProjectFolder(projectSignature.getPublisher(), projectSignature.getProject()).await().create().await();
                    test.assertEqual(
                        qubFolder.getFolder(projectSignature.getPublisher() + "/" + projectSignature.getProject() + "/versions/" + projectSignature.getVersion()).await(),
                        qubFolder.getProjectVersionFolder(projectSignature).await());
                });

                runner.test("with empty versions folder", (Test test) ->
                {
                    final QubFolder qubFolder = QubFolderTests.getQubFolder(test, "/qub/");
                    final ProjectSignature projectSignature = ProjectSignature.create("a", "b", "1");
                    final QubProjectFolder projectFolder = qubFolder.getProjectFolder(projectSignature.getPublisher(), projectSignature.getProject()).await();
                    projectFolder.createFolder("versions").await();
                    test.assertEqual(
                        qubFolder.getFolder(projectSignature.getPublisher() + "/" + projectSignature.getProject() + "/versions/" + projectSignature.getVersion()).await(),
                        qubFolder.getProjectVersionFolder(projectSignature).await());
                });

                runner.test("with non-existing project.json file", (Test test) ->
                {
                    final QubFolder qubFolder = QubFolderTests.getQubFolder(test, "/qub/");
                    final ProjectSignature projectSignature = ProjectSignature.create("a", "b", "1");
                    final QubProjectVersionFolder projectVersionFolder = qubFolder.getProjectVersionFolder(projectSignature.getPublisher(), projectSignature.getProject(), projectSignature.getVersion()).await();
                    projectVersionFolder.create().await();
                    test.assertEqual(
                        qubFolder.getFolder(projectSignature.getPublisher() + "/" + projectSignature.getProject() + "/versions/" + projectSignature.getVersion()).await(),
                        qubFolder.getProjectVersionFolder(projectSignature).await());
                });

                runner.test("with existing project.json file", (Test test) ->
                {
                    final QubFolder qubFolder = QubFolderTests.getQubFolder(test, "/qub/");
                    final ProjectSignature projectSignature = ProjectSignature.create("a", "b", "1");
                    final QubProjectVersionFolder projectVersionFolder = qubFolder.getProjectVersionFolder(projectSignature.getPublisher(), projectSignature.getProject(), projectSignature.getVersion()).await();
                    projectVersionFolder.createFile("project.json").await();
                    test.assertEqual(
                        qubFolder.getFolder(projectSignature.getPublisher() + "/" + projectSignature.getProject() + "/versions/" + projectSignature.getVersion()).await(),
                        qubFolder.getProjectVersionFolder(projectSignature).await());
                });
            });

            runner.testGroup("getProjectJSONFile(String,String,String)", () ->
            {
                final Action4<String,String,String,Throwable> getProjectJSONFileErrorTest = (String publisherName, String projectName, String version, Throwable expected) ->
                {
                    runner.test("with " + English.andList(Iterable.create(publisherName, projectName, version).map(Strings::escapeAndQuote)), (Test test) ->
                    {
                        final QubFolder qubFolder = QubFolderTests.getQubFolder(test, "/qub/");
                        test.assertThrows(() -> qubFolder.getProjectJSONFile(publisherName, projectName, version), expected);
                    });
                };

                getProjectJSONFileErrorTest.run(null, null, null, new PreConditionFailure("publisherName cannot be null."));
                getProjectJSONFileErrorTest.run("", null, null, new PreConditionFailure("publisherName cannot be empty."));
                getProjectJSONFileErrorTest.run("a", null, null, new PreConditionFailure("projectName cannot be null."));
                getProjectJSONFileErrorTest.run("a", "", null, new PreConditionFailure("projectName cannot be empty."));
                getProjectJSONFileErrorTest.run("a", "b", null, new PreConditionFailure("version cannot be null."));
                getProjectJSONFileErrorTest.run("a", "b", "", new PreConditionFailure("version cannot be empty."));

                runner.test("with non-existing Qub folder", (Test test) ->
                {
                    final QubFolder qubFolder = QubFolderTests.getQubFolder(test, "/qub/");
                    final String publisherName = "a";
                    final String projectName = "b";
                    final String version = "1";
                    test.assertEqual(
                        qubFolder.getFile(publisherName + "/" + projectName + "/versions/" + version + "/project.json").await(),
                        qubFolder.getProjectJSONFile(publisherName, projectName, version).await());
                });

                runner.test("with non-existing publisher folder", (Test test) ->
                {
                    final QubFolder qubFolder = QubFolderTests.getQubFolder(test, "/qub/");
                    qubFolder.create().await();
                    final String publisherName = "a";
                    final String projectName = "b";
                    final String version = "1";
                    test.assertEqual(
                        qubFolder.getFile(publisherName + "/" + projectName + "/versions/" + version + "/project.json").await(),
                        qubFolder.getProjectJSONFile(publisherName, projectName, version).await());
                });

                runner.test("with non-existing project folder", (Test test) ->
                {
                    final QubFolder qubFolder = QubFolderTests.getQubFolder(test, "/qub/");
                    final String publisherName = "a";
                    qubFolder.getPublisherFolder(publisherName).await().create().await();
                    final String projectName = "b";
                    final String version = "1";
                    test.assertEqual(
                        qubFolder.getFile(publisherName + "/" + projectName + "/versions/" + version + "/project.json").await(),
                        qubFolder.getProjectJSONFile(publisherName, projectName, version).await());
                });

                runner.test("with non-existing versions folder", (Test test) ->
                {
                    final QubFolder qubFolder = QubFolderTests.getQubFolder(test, "/qub/");
                    final String publisherName = "a";
                    final String projectName = "b";
                    qubFolder.getProjectFolder(publisherName, projectName).await().create().await();
                    final String version = "1";
                    test.assertEqual(
                        qubFolder.getFile(publisherName + "/" + projectName + "/versions/" + version + "/project.json").await(),
                        qubFolder.getProjectJSONFile(publisherName, projectName, version).await());
                });

                runner.test("with empty versions folder", (Test test) ->
                {
                    final QubFolder qubFolder = QubFolderTests.getQubFolder(test, "/qub/");
                    final String publisherName = "a";
                    final String projectName = "b";
                    final QubProjectFolder projectFolder = qubFolder.getProjectFolder(publisherName, projectName).await();
                    projectFolder.createFolder("versions").await();
                    final String version = "1";
                    test.assertEqual(
                        qubFolder.getFile(publisherName + "/" + projectName + "/versions/" + version + "/project.json").await(),
                        qubFolder.getProjectJSONFile(publisherName, projectName, version).await());
                });

                runner.test("with non-existing project.json file", (Test test) ->
                {
                    final QubFolder qubFolder = QubFolderTests.getQubFolder(test, "/qub/");
                    final String publisherName = "a";
                    final String projectName = "b";
                    final String version = "1";
                    final QubProjectVersionFolder projectVersionFolder = qubFolder.getProjectVersionFolder(publisherName, projectName, version).await();
                    projectVersionFolder.create().await();
                    test.assertEqual(
                        qubFolder.getFile(publisherName + "/" + projectName + "/versions/" + version + "/project.json").await(),
                        qubFolder.getProjectJSONFile(publisherName, projectName, version).await());
                });

                runner.test("with existing project.json file", (Test test) ->
                {
                    final QubFolder qubFolder = QubFolderTests.getQubFolder(test, "/qub/");
                    final String publisherName = "a";
                    final String projectName = "b";
                    final String version = "1";
                    final QubProjectVersionFolder projectVersionFolder = qubFolder.getProjectVersionFolder(publisherName, projectName, version).await();
                    projectVersionFolder.createFile("project.json").await();
                    test.assertEqual(
                        qubFolder.getFile(publisherName + "/" + projectName + "/versions/" + version + "/project.json").await(),
                        qubFolder.getProjectJSONFile(publisherName, projectName, version).await());
                });
            });

            runner.testGroup("getProjectJSONFile(String,String,VersionNumber)", () ->
            {
                final Action4<String,String,VersionNumber,Throwable> getProjectJSONFileErrorTest = (String publisherName, String projectName, VersionNumber version, Throwable expected) ->
                {
                    runner.test("with " + English.andList(Iterable.create(publisherName, projectName, version).map(Strings::escapeAndQuote)), (Test test) ->
                    {
                        final QubFolder qubFolder = QubFolderTests.getQubFolder(test, "/qub/");
                        test.assertThrows(() -> qubFolder.getProjectJSONFile(publisherName, projectName, version), expected);
                    });
                };

                getProjectJSONFileErrorTest.run(null, null, null, new PreConditionFailure("publisherName cannot be null."));
                getProjectJSONFileErrorTest.run("", null, null, new PreConditionFailure("publisherName cannot be empty."));
                getProjectJSONFileErrorTest.run("a", null, null, new PreConditionFailure("projectName cannot be null."));
                getProjectJSONFileErrorTest.run("a", "", null, new PreConditionFailure("projectName cannot be empty."));
                getProjectJSONFileErrorTest.run("a", "b", null, new PreConditionFailure("version cannot be null."));

                runner.test("with non-existing Qub folder", (Test test) ->
                {
                    final QubFolder qubFolder = QubFolderTests.getQubFolder(test, "/qub/");
                    final String publisherName = "a";
                    final String projectName = "b";
                    final VersionNumber version = VersionNumber.create().setMajor(1);
                    test.assertEqual(
                        qubFolder.getFile(publisherName + "/" + projectName + "/versions/" + version + "/project.json").await(),
                        qubFolder.getProjectJSONFile(publisherName, projectName, version).await());
                });

                runner.test("with non-existing publisher folder", (Test test) ->
                {
                    final QubFolder qubFolder = QubFolderTests.getQubFolder(test, "/qub/");
                    qubFolder.create().await();
                    final String publisherName = "a";
                    final String projectName = "b";
                    final VersionNumber version = VersionNumber.create().setMajor(1);
                    test.assertEqual(
                        qubFolder.getFile(publisherName + "/" + projectName + "/versions/" + version + "/project.json").await(),
                        qubFolder.getProjectJSONFile(publisherName, projectName, version).await());
                });

                runner.test("with non-existing project folder", (Test test) ->
                {
                    final QubFolder qubFolder = QubFolderTests.getQubFolder(test, "/qub/");
                    final String publisherName = "a";
                    qubFolder.getPublisherFolder(publisherName).await().create().await();
                    final String projectName = "b";
                    final VersionNumber version = VersionNumber.create().setMajor(1);
                    test.assertEqual(
                        qubFolder.getFile(publisherName + "/" + projectName + "/versions/" + version + "/project.json").await(),
                        qubFolder.getProjectJSONFile(publisherName, projectName, version).await());
                });

                runner.test("with non-existing versions folder", (Test test) ->
                {
                    final QubFolder qubFolder = QubFolderTests.getQubFolder(test, "/qub/");
                    final String publisherName = "a";
                    final String projectName = "b";
                    qubFolder.getProjectFolder(publisherName, projectName).await().create().await();
                    final VersionNumber version = VersionNumber.create().setMajor(1);
                    test.assertEqual(
                        qubFolder.getFile(publisherName + "/" + projectName + "/versions/" + version + "/project.json").await(),
                        qubFolder.getProjectJSONFile(publisherName, projectName, version).await());
                });

                runner.test("with empty versions folder", (Test test) ->
                {
                    final QubFolder qubFolder = QubFolderTests.getQubFolder(test, "/qub/");
                    final String publisherName = "a";
                    final String projectName = "b";
                    final QubProjectFolder projectFolder = qubFolder.getProjectFolder(publisherName, projectName).await();
                    projectFolder.createFolder("versions").await();
                    final VersionNumber version = VersionNumber.create().setMajor(1);
                    test.assertEqual(
                        qubFolder.getFile(publisherName + "/" + projectName + "/versions/" + version + "/project.json").await(),
                        qubFolder.getProjectJSONFile(publisherName, projectName, version).await());
                });

                runner.test("with non-existing project.json file", (Test test) ->
                {
                    final QubFolder qubFolder = QubFolderTests.getQubFolder(test, "/qub/");
                    final String publisherName = "a";
                    final String projectName = "b";
                    final VersionNumber version = VersionNumber.create().setMajor(1);
                    final QubProjectVersionFolder projectVersionFolder = qubFolder.getProjectVersionFolder(publisherName, projectName, version).await();
                    projectVersionFolder.create().await();
                    test.assertEqual(
                        qubFolder.getFile(publisherName + "/" + projectName + "/versions/" + version + "/project.json").await(),
                        qubFolder.getProjectJSONFile(publisherName, projectName, version).await());
                });

                runner.test("with existing project.json file", (Test test) ->
                {
                    final QubFolder qubFolder = QubFolderTests.getQubFolder(test, "/qub/");
                    final String publisherName = "a";
                    final String projectName = "b";
                    final VersionNumber version = VersionNumber.create().setMajor(1);
                    final QubProjectVersionFolder projectVersionFolder = qubFolder.getProjectVersionFolder(publisherName, projectName, version).await();
                    projectVersionFolder.createFile("project.json").await();
                    test.assertEqual(
                        qubFolder.getFile(publisherName + "/" + projectName + "/versions/" + version + "/project.json").await(),
                        qubFolder.getProjectJSONFile(publisherName, projectName, version).await());
                });
            });

            runner.testGroup("getProjectJSONFile(ProjectSignature)", () ->
            {
                final Action2<ProjectSignature,Throwable> getProjectJSONFileErrorTest = (ProjectSignature projectSignature, Throwable expected) ->
                {
                    runner.test("with " + projectSignature, (Test test) ->
                    {
                        final QubFolder qubFolder = QubFolderTests.getQubFolder(test, "/qub/");
                        test.assertThrows(() -> qubFolder.getProjectJSONFile(projectSignature), expected);
                    });
                };

                getProjectJSONFileErrorTest.run(null, new PreConditionFailure("projectSignature cannot be null."));

                runner.test("with non-existing Qub folder", (Test test) ->
                {
                    final QubFolder qubFolder = QubFolderTests.getQubFolder(test, "/qub/");
                    final ProjectSignature projectSignature = ProjectSignature.create("a", "b", "1");
                    test.assertEqual(
                        qubFolder.getFile(projectSignature.getPublisher() + "/" + projectSignature.getProject() + "/versions/" + projectSignature.getVersion() + "/project.json").await(),
                        qubFolder.getProjectJSONFile(projectSignature).await());
                });

                runner.test("with non-existing publisher folder", (Test test) ->
                {
                    final QubFolder qubFolder = QubFolderTests.getQubFolder(test, "/qub/");
                    qubFolder.create().await();
                    final ProjectSignature projectSignature = ProjectSignature.create("a", "b", "1");
                    test.assertEqual(
                        qubFolder.getFile(projectSignature.getPublisher() + "/" + projectSignature.getProject() + "/versions/" + projectSignature.getVersion() + "/project.json").await(),
                        qubFolder.getProjectJSONFile(projectSignature).await());
                });

                runner.test("with non-existing project folder", (Test test) ->
                {
                    final QubFolder qubFolder = QubFolderTests.getQubFolder(test, "/qub/");
                    final ProjectSignature projectSignature = ProjectSignature.create("a", "b", "1");
                    qubFolder.getPublisherFolder(projectSignature.getPublisher()).await().create().await();
                    test.assertEqual(
                        qubFolder.getFile(projectSignature.getPublisher() + "/" + projectSignature.getProject() + "/versions/" + projectSignature.getVersion() + "/project.json").await(),
                        qubFolder.getProjectJSONFile(projectSignature).await());
                });

                runner.test("with non-existing versions folder", (Test test) ->
                {
                    final QubFolder qubFolder = QubFolderTests.getQubFolder(test, "/qub/");
                    final ProjectSignature projectSignature = ProjectSignature.create("a", "b", "1");
                    qubFolder.getProjectFolder(projectSignature.getPublisher(), projectSignature.getProject()).await().create().await();
                    test.assertEqual(
                        qubFolder.getFile(projectSignature.getPublisher() + "/" + projectSignature.getProject() + "/versions/" + projectSignature.getVersion() + "/project.json").await(),
                        qubFolder.getProjectJSONFile(projectSignature).await());
                });

                runner.test("with empty versions folder", (Test test) ->
                {
                    final QubFolder qubFolder = QubFolderTests.getQubFolder(test, "/qub/");
                    final ProjectSignature projectSignature = ProjectSignature.create("a", "b", "1");
                    final QubProjectFolder projectFolder = qubFolder.getProjectFolder(projectSignature.getPublisher(), projectSignature.getProject()).await();
                    projectFolder.createFolder("versions").await();
                    test.assertEqual(
                        qubFolder.getFile(projectSignature.getPublisher() + "/" + projectSignature.getProject() + "/versions/" + projectSignature.getVersion() + "/project.json").await(),
                        qubFolder.getProjectJSONFile(projectSignature).await());
                });

                runner.test("with non-existing project.json file", (Test test) ->
                {
                    final QubFolder qubFolder = QubFolderTests.getQubFolder(test, "/qub/");
                    final ProjectSignature projectSignature = ProjectSignature.create("a", "b", "1");
                    final QubProjectVersionFolder projectVersionFolder = qubFolder.getProjectVersionFolder(projectSignature.getPublisher(), projectSignature.getProject(), projectSignature.getVersion()).await();
                    projectVersionFolder.create().await();
                    test.assertEqual(
                        qubFolder.getFile(projectSignature.getPublisher() + "/" + projectSignature.getProject() + "/versions/" + projectSignature.getVersion() + "/project.json").await(),
                        qubFolder.getProjectJSONFile(projectSignature).await());
                });

                runner.test("with existing project.json file", (Test test) ->
                {
                    final QubFolder qubFolder = QubFolderTests.getQubFolder(test, "/qub/");
                    final ProjectSignature projectSignature = ProjectSignature.create("a", "b", "1");
                    final QubProjectVersionFolder projectVersionFolder = qubFolder.getProjectVersionFolder(projectSignature.getPublisher(), projectSignature.getProject(), projectSignature.getVersion()).await();
                    projectVersionFolder.createFile("project.json").await();
                    test.assertEqual(
                        qubFolder.getFile(projectSignature.getPublisher() + "/" + projectSignature.getProject() + "/versions/" + projectSignature.getVersion() + "/project.json").await(),
                        qubFolder.getProjectJSONFile(projectSignature).await());
                });
            });

            runner.testGroup("getCompiledSourcesFile(String,String,String)", () ->
            {
                final Action4<String,String,String,Throwable> getCompiledSourcesFileErrorTest = (String publisherName, String projectName, String version, Throwable expected) ->
                {
                    runner.test("with " + English.andList(Iterable.create(publisherName, projectName, version).map(Strings::escapeAndQuote)), (Test test) ->
                    {
                        final QubFolder qubFolder = QubFolderTests.getQubFolder(test, "/qub/");
                        test.assertThrows(() -> qubFolder.getCompiledSourcesFile(publisherName, projectName, version), expected);
                    });
                };

                getCompiledSourcesFileErrorTest.run(null, null, null, new PreConditionFailure("publisherName cannot be null."));
                getCompiledSourcesFileErrorTest.run("", null, null, new PreConditionFailure("publisherName cannot be empty."));
                getCompiledSourcesFileErrorTest.run("a", null, null, new PreConditionFailure("projectName cannot be null."));
                getCompiledSourcesFileErrorTest.run("a", "", null, new PreConditionFailure("projectName cannot be empty."));
                getCompiledSourcesFileErrorTest.run("a", "b", null, new PreConditionFailure("version cannot be null."));
                getCompiledSourcesFileErrorTest.run("a", "b", "", new PreConditionFailure("version cannot be empty."));

                runner.test("with non-existing Qub folder", (Test test) ->
                {
                    final QubFolder qubFolder = QubFolderTests.getQubFolder(test, "/qub/");
                    final String publisherName = "a";
                    final String projectName = "b";
                    final String version = "1";
                    test.assertEqual(
                        qubFolder.getFile(publisherName + "/" + projectName + "/versions/" + version + "/" + projectName + ".jar").await(),
                        qubFolder.getCompiledSourcesFile(publisherName, projectName, version).await());
                });

                runner.test("with non-existing publisher folder", (Test test) ->
                {
                    final QubFolder qubFolder = QubFolderTests.getQubFolder(test, "/qub/");
                    qubFolder.create().await();
                    final String publisherName = "a";
                    final String projectName = "b";
                    final String version = "1";
                    test.assertEqual(
                        qubFolder.getFile(publisherName + "/" + projectName + "/versions/" + version + "/" + projectName + ".jar").await(),
                        qubFolder.getCompiledSourcesFile(publisherName, projectName, version).await());
                });

                runner.test("with non-existing project folder", (Test test) ->
                {
                    final QubFolder qubFolder = QubFolderTests.getQubFolder(test, "/qub/");
                    final String publisherName = "a";
                    qubFolder.getPublisherFolder(publisherName).await().create().await();
                    final String projectName = "b";
                    final String version = "1";
                    test.assertEqual(
                        qubFolder.getFile(publisherName + "/" + projectName + "/versions/" + version + "/" + projectName + ".jar").await(),
                        qubFolder.getCompiledSourcesFile(publisherName, projectName, version).await());
                });

                runner.test("with non-existing versions folder", (Test test) ->
                {
                    final QubFolder qubFolder = QubFolderTests.getQubFolder(test, "/qub/");
                    final String publisherName = "a";
                    final String projectName = "b";
                    qubFolder.getProjectFolder(publisherName, projectName).await().create().await();
                    final String version = "1";
                    test.assertEqual(
                        qubFolder.getFile(publisherName + "/" + projectName + "/versions/" + version + "/" + projectName + ".jar").await(),
                        qubFolder.getCompiledSourcesFile(publisherName, projectName, version).await());
                });

                runner.test("with empty versions folder", (Test test) ->
                {
                    final QubFolder qubFolder = QubFolderTests.getQubFolder(test, "/qub/");
                    final String publisherName = "a";
                    final String projectName = "b";
                    final QubProjectFolder projectFolder = qubFolder.getProjectFolder(publisherName, projectName).await();
                    projectFolder.createFolder("versions").await();
                    final String version = "1";
                    test.assertEqual(
                        qubFolder.getFile(publisherName + "/" + projectName + "/versions/" + version + "/" + projectName + ".jar").await(),
                        qubFolder.getCompiledSourcesFile(publisherName, projectName, version).await());
                });

                runner.test("with non-existing project.json file", (Test test) ->
                {
                    final QubFolder qubFolder = QubFolderTests.getQubFolder(test, "/qub/");
                    final String publisherName = "a";
                    final String projectName = "b";
                    final String version = "1";
                    final QubProjectVersionFolder projectVersionFolder = qubFolder.getProjectVersionFolder(publisherName, projectName, version).await();
                    projectVersionFolder.create().await();
                    test.assertEqual(
                        qubFolder.getFile(publisherName + "/" + projectName + "/versions/" + version + "/" + projectName + ".jar").await(),
                        qubFolder.getCompiledSourcesFile(publisherName, projectName, version).await());
                });

                runner.test("with existing project.json file", (Test test) ->
                {
                    final QubFolder qubFolder = QubFolderTests.getQubFolder(test, "/qub/");
                    final String publisherName = "a";
                    final String projectName = "b";
                    final String version = "1";
                    final QubProjectVersionFolder projectVersionFolder = qubFolder.getProjectVersionFolder(publisherName, projectName, version).await();
                    projectVersionFolder.createFile("project.json").await();
                    test.assertEqual(
                        qubFolder.getFile(publisherName + "/" + projectName + "/versions/" + version + "/" + projectName + ".jar").await(),
                        qubFolder.getCompiledSourcesFile(publisherName, projectName, version).await());
                });
            });

            runner.testGroup("getSourcesFile(String,String,String)", () ->
            {
                final Action4<String,String,String,Throwable> getSourcesFileErrorTest = (String publisherName, String projectName, String version, Throwable expected) ->
                {
                    runner.test("with " + English.andList(Iterable.create(publisherName, projectName, version).map(Strings::escapeAndQuote)), (Test test) ->
                    {
                        final QubFolder qubFolder = QubFolderTests.getQubFolder(test, "/qub/");
                        test.assertThrows(() -> qubFolder.getSourcesFile(publisherName, projectName, version), expected);
                    });
                };

                getSourcesFileErrorTest.run(null, null, null, new PreConditionFailure("publisherName cannot be null."));
                getSourcesFileErrorTest.run("", null, null, new PreConditionFailure("publisherName cannot be empty."));
                getSourcesFileErrorTest.run("a", null, null, new PreConditionFailure("projectName cannot be null."));
                getSourcesFileErrorTest.run("a", "", null, new PreConditionFailure("projectName cannot be empty."));
                getSourcesFileErrorTest.run("a", "b", null, new PreConditionFailure("version cannot be null."));
                getSourcesFileErrorTest.run("a", "b", "", new PreConditionFailure("version cannot be empty."));

                runner.test("with non-existing Qub folder", (Test test) ->
                {
                    final QubFolder qubFolder = QubFolderTests.getQubFolder(test, "/qub/");
                    final String publisherName = "a";
                    final String projectName = "b";
                    final String version = "1";
                    test.assertEqual(
                        qubFolder.getFile(publisherName + "/" + projectName + "/versions/" + version + "/" + projectName + ".sources.jar").await(),
                        qubFolder.getSourcesFile(publisherName, projectName, version).await());
                });

                runner.test("with non-existing publisher folder", (Test test) ->
                {
                    final QubFolder qubFolder = QubFolderTests.getQubFolder(test, "/qub/");
                    qubFolder.create().await();
                    final String publisherName = "a";
                    final String projectName = "b";
                    final String version = "1";
                    test.assertEqual(
                        qubFolder.getFile(publisherName + "/" + projectName + "/versions/" + version + "/" + projectName + ".sources.jar").await(),
                        qubFolder.getSourcesFile(publisherName, projectName, version).await());
                });

                runner.test("with non-existing project folder", (Test test) ->
                {
                    final QubFolder qubFolder = QubFolderTests.getQubFolder(test, "/qub/");
                    final String publisherName = "a";
                    qubFolder.getPublisherFolder(publisherName).await().create().await();
                    final String projectName = "b";
                    final String version = "1";
                    test.assertEqual(
                        qubFolder.getFile(publisherName + "/" + projectName + "/versions/" + version + "/" + projectName + ".sources.jar").await(),
                        qubFolder.getSourcesFile(publisherName, projectName, version).await());
                });

                runner.test("with non-existing versions folder", (Test test) ->
                {
                    final QubFolder qubFolder = QubFolderTests.getQubFolder(test, "/qub/");
                    final String publisherName = "a";
                    final String projectName = "b";
                    qubFolder.getProjectFolder(publisherName, projectName).await().create().await();
                    final String version = "1";
                    test.assertEqual(
                        qubFolder.getFile(publisherName + "/" + projectName + "/versions/" + version + "/" + projectName + ".sources.jar").await(),
                        qubFolder.getSourcesFile(publisherName, projectName, version).await());
                });

                runner.test("with empty versions folder", (Test test) ->
                {
                    final QubFolder qubFolder = QubFolderTests.getQubFolder(test, "/qub/");
                    final String publisherName = "a";
                    final String projectName = "b";
                    final QubProjectFolder projectFolder = qubFolder.getProjectFolder(publisherName, projectName).await();
                    projectFolder.createFolder("versions").await();
                    final String version = "1";
                    test.assertEqual(
                        qubFolder.getFile(publisherName + "/" + projectName + "/versions/" + version + "/" + projectName + ".sources.jar").await(),
                        qubFolder.getSourcesFile(publisherName, projectName, version).await());
                });

                runner.test("with non-existing project.json file", (Test test) ->
                {
                    final QubFolder qubFolder = QubFolderTests.getQubFolder(test, "/qub/");
                    final String publisherName = "a";
                    final String projectName = "b";
                    final String version = "1";
                    final QubProjectVersionFolder projectVersionFolder = qubFolder.getProjectVersionFolder(publisherName, projectName, version).await();
                    projectVersionFolder.create().await();
                    test.assertEqual(
                        qubFolder.getFile(publisherName + "/" + projectName + "/versions/" + version + "/" + projectName + ".sources.jar").await(),
                        qubFolder.getSourcesFile(publisherName, projectName, version).await());
                });

                runner.test("with existing project.json file", (Test test) ->
                {
                    final QubFolder qubFolder = QubFolderTests.getQubFolder(test, "/qub/");
                    final String publisherName = "a";
                    final String projectName = "b";
                    final String version = "1";
                    final QubProjectVersionFolder projectVersionFolder = qubFolder.getProjectVersionFolder(publisherName, projectName, version).await();
                    projectVersionFolder.createFile("project.json").await();
                    test.assertEqual(
                        qubFolder.getFile(publisherName + "/" + projectName + "/versions/" + version + "/" + projectName + ".sources.jar").await(),
                        qubFolder.getSourcesFile(publisherName, projectName, version).await());
                });
            });

            runner.testGroup("getCompiledTestsFile(String,String,String)", () ->
            {
                final Action4<String,String,String,Throwable> getCompiledTestsFileErrorTest = (String publisherName, String projectName, String version, Throwable expected) ->
                {
                    runner.test("with " + English.andList(Iterable.create(publisherName, projectName, version).map(Strings::escapeAndQuote)), (Test test) ->
                    {
                        final QubFolder qubFolder = QubFolderTests.getQubFolder(test, "/qub/");
                        test.assertThrows(() -> qubFolder.getCompiledTestsFile(publisherName, projectName, version), expected);
                    });
                };

                getCompiledTestsFileErrorTest.run(null, null, null, new PreConditionFailure("publisherName cannot be null."));
                getCompiledTestsFileErrorTest.run("", null, null, new PreConditionFailure("publisherName cannot be empty."));
                getCompiledTestsFileErrorTest.run("a", null, null, new PreConditionFailure("projectName cannot be null."));
                getCompiledTestsFileErrorTest.run("a", "", null, new PreConditionFailure("projectName cannot be empty."));
                getCompiledTestsFileErrorTest.run("a", "b", null, new PreConditionFailure("version cannot be null."));
                getCompiledTestsFileErrorTest.run("a", "b", "", new PreConditionFailure("version cannot be empty."));

                runner.test("with non-existing Qub folder", (Test test) ->
                {
                    final QubFolder qubFolder = QubFolderTests.getQubFolder(test, "/qub/");
                    final String publisherName = "a";
                    final String projectName = "b";
                    final String version = "1";
                    test.assertEqual(
                        qubFolder.getFile(publisherName + "/" + projectName + "/versions/" + version + "/" + projectName + ".tests.jar").await(),
                        qubFolder.getCompiledTestsFile(publisherName, projectName, version).await());
                });

                runner.test("with non-existing publisher folder", (Test test) ->
                {
                    final QubFolder qubFolder = QubFolderTests.getQubFolder(test, "/qub/");
                    qubFolder.create().await();
                    final String publisherName = "a";
                    final String projectName = "b";
                    final String version = "1";
                    test.assertEqual(
                        qubFolder.getFile(publisherName + "/" + projectName + "/versions/" + version + "/" + projectName + ".tests.jar").await(),
                        qubFolder.getCompiledTestsFile(publisherName, projectName, version).await());
                });

                runner.test("with non-existing project folder", (Test test) ->
                {
                    final QubFolder qubFolder = QubFolderTests.getQubFolder(test, "/qub/");
                    final String publisherName = "a";
                    qubFolder.getPublisherFolder(publisherName).await().create().await();
                    final String projectName = "b";
                    final String version = "1";
                    test.assertEqual(
                        qubFolder.getFile(publisherName + "/" + projectName + "/versions/" + version + "/" + projectName + ".tests.jar").await(),
                        qubFolder.getCompiledTestsFile(publisherName, projectName, version).await());
                });

                runner.test("with non-existing versions folder", (Test test) ->
                {
                    final QubFolder qubFolder = QubFolderTests.getQubFolder(test, "/qub/");
                    final String publisherName = "a";
                    final String projectName = "b";
                    qubFolder.getProjectFolder(publisherName, projectName).await().create().await();
                    final String version = "1";
                    test.assertEqual(
                        qubFolder.getFile(publisherName + "/" + projectName + "/versions/" + version + "/" + projectName + ".tests.jar").await(),
                        qubFolder.getCompiledTestsFile(publisherName, projectName, version).await());
                });

                runner.test("with empty versions folder", (Test test) ->
                {
                    final QubFolder qubFolder = QubFolderTests.getQubFolder(test, "/qub/");
                    final String publisherName = "a";
                    final String projectName = "b";
                    final QubProjectFolder projectFolder = qubFolder.getProjectFolder(publisherName, projectName).await();
                    projectFolder.createFolder("versions").await();
                    final String version = "1";
                    test.assertEqual(
                        qubFolder.getFile(publisherName + "/" + projectName + "/versions/" + version + "/" + projectName + ".tests.jar").await(),
                        qubFolder.getCompiledTestsFile(publisherName, projectName, version).await());
                });

                runner.test("with non-existing project.json file", (Test test) ->
                {
                    final QubFolder qubFolder = QubFolderTests.getQubFolder(test, "/qub/");
                    final String publisherName = "a";
                    final String projectName = "b";
                    final String version = "1";
                    final QubProjectVersionFolder projectVersionFolder = qubFolder.getProjectVersionFolder(publisherName, projectName, version).await();
                    projectVersionFolder.create().await();
                    test.assertEqual(
                        qubFolder.getFile(publisherName + "/" + projectName + "/versions/" + version + "/" + projectName + ".tests.jar").await(),
                        qubFolder.getCompiledTestsFile(publisherName, projectName, version).await());
                });

                runner.test("with existing project.json file", (Test test) ->
                {
                    final QubFolder qubFolder = QubFolderTests.getQubFolder(test, "/qub/");
                    final String publisherName = "a";
                    final String projectName = "b";
                    final String version = "1";
                    final QubProjectVersionFolder projectVersionFolder = qubFolder.getProjectVersionFolder(publisherName, projectName, version).await();
                    projectVersionFolder.createFile("project.json").await();
                    test.assertEqual(
                        qubFolder.getFile(publisherName + "/" + projectName + "/versions/" + version + "/" + projectName + ".tests.jar").await(),
                        qubFolder.getCompiledTestsFile(publisherName, projectName, version).await());
                });
            });

            runner.testGroup("equals(Object)", () ->
            {
                runner.test("with /qub/ and null", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = QubFolderTests.createFileSystem(test);
                    final Folder folder = fileSystem.getFolder("/qub/").await();
                    final QubFolder qubFolder = QubFolder.get(folder);
                    test.assertEqual(false, qubFolder.equals((Object)null));
                });

                runner.test("with /qub/ and \"hello world\"", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = QubFolderTests.createFileSystem(test);
                    final Folder folder = fileSystem.getFolder("/qub/").await();
                    final QubFolder qubFolder = QubFolder.get(folder);
                    test.assertEqual(false, qubFolder.equals((Object)"hello world"));
                });

                runner.test("with /qub/ and /other/", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = QubFolderTests.createFileSystem(test);

                    final Folder folder = fileSystem.getFolder("/qub/").await();
                    final QubFolder qubFolder = QubFolder.get(folder);

                    final Folder folder2 = fileSystem.getFolder("/other/").await();
                    final QubFolder qubFolder2 = QubFolder.get(folder2);

                    test.assertEqual(false, qubFolder.equals((Object)qubFolder2));
                });

                runner.test("with /qub/ and /qub/", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = QubFolderTests.createFileSystem(test);

                    final Folder folder = fileSystem.getFolder("/qub/").await();
                    final QubFolder qubFolder = QubFolder.get(folder);

                    final Folder folder2 = fileSystem.getFolder("/qub/").await();
                    final QubFolder qubFolder2 = QubFolder.get(folder2);

                    test.assertEqual(true, qubFolder.equals((Object)qubFolder2));
                });
            });

            runner.testGroup("equals(QubFolder)", () ->
            {
                runner.test("with /qub/ and null", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = QubFolderTests.createFileSystem(test);
                    final Folder folder = fileSystem.getFolder("/qub/").await();
                    final QubFolder qubFolder = QubFolder.get(folder);
                    test.assertEqual(false, qubFolder.equals((QubFolder)null));
                });

                runner.test("with /qub/ and /other/", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = QubFolderTests.createFileSystem(test);

                    final Folder folder = fileSystem.getFolder("/qub/").await();
                    final QubFolder qubFolder = QubFolder.get(folder);

                    final Folder folder2 = fileSystem.getFolder("/other/").await();
                    final QubFolder qubFolder2 = QubFolder.get(folder2);

                    test.assertEqual(false, qubFolder.equals((QubFolder)qubFolder2));
                });

                runner.test("with /qub/ and /qub/", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = QubFolderTests.createFileSystem(test);

                    final Folder folder = fileSystem.getFolder("/qub/").await();
                    final QubFolder qubFolder = QubFolder.get(folder);

                    final Folder folder2 = fileSystem.getFolder("/qub/").await();
                    final QubFolder qubFolder2 = QubFolder.get(folder2);

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
        final InMemoryFileSystem fileSystem = InMemoryFileSystem.create(test.getClock());
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
        return QubFolder.get(QubFolderTests.getFolder(test, folderPath));
    }

    static QubFolder createQubFolder(Test test, String folderPath)
    {
        return QubFolder.get(QubFolderTests.createFolder(test, folderPath));
    }
}
