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
                    final Folder folder = QubFolderTests.getFolder("/qub/");
                    final QubFolder qubFolder = QubFolder.get(folder);
                    test.assertEqual(folder.getPath(), qubFolder.getPath());
                    test.assertFalse(qubFolder.exists().await());
                });

                runner.test("with folder that exists", (Test test) ->
                {
                    final Folder folder = QubFolderTests.createFolder("/qub/");
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
                        final QubFolder qubFolder = QubFolderTests.getQubFolder("/qub/");
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
                        final QubFolder qubFolder = QubFolderTests.getQubFolder("/qub/");
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

            runner.testGroup("iteratePublisherFolders()", () ->
            {
                runner.test("with non-existing QubFolder", (Test test) ->
                {
                    final QubFolder qubFolder = QubFolderTests.getQubFolder("/qub/");
                    test.assertEqual(Iterable.create(), qubFolder.iteratePublisherFolders().toList());
                });

                runner.test("with existing empty QubFolder", (Test test) ->
                {
                    final QubFolder qubFolder = QubFolderTests.createQubFolder("/qub/");
                    test.assertEqual(Iterable.create(), qubFolder.iteratePublisherFolders().toList());
                });

                runner.test("with existing QubFolder with files", (Test test) ->
                {
                    final Folder folder = QubFolderTests.createFolder("/qub/");
                    folder.createFile("shortcut.cmd").await();
                    final QubFolder qubFolder = QubFolder.get(folder);
                    test.assertEqual(Iterable.create(), qubFolder.iteratePublisherFolders().toList());
                });

                runner.test("with existing QubFolder with folders", (Test test) ->
                {
                    final Folder folder = QubFolderTests.createFolder("/qub/");
                    final Folder subFolder = folder.createFolder("publisher").await();
                    final QubFolder qubFolder = QubFolder.get(folder);
                    test.assertEqual(
                        Iterable.create(
                            QubPublisherFolder.get(subFolder)),
                        qubFolder.iteratePublisherFolders().toList());
                });
            });

            runner.testGroup("getPublisherFolder(String)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final QubFolder qubFolder = QubFolderTests.getQubFolder("/qub/");
                    test.assertThrows(() -> qubFolder.getPublisherFolder(null),
                        new PreConditionFailure("publisherName cannot be null."));
                });

                runner.test("with empty", (Test test) ->
                {
                    final QubFolder qubFolder = QubFolderTests.getQubFolder("/qub/");
                    test.assertThrows(() -> qubFolder.getPublisherFolder(""),
                        new PreConditionFailure("publisherName cannot be empty."));
                });

                runner.test("with non-existing publisher", (Test test) ->
                {
                    final QubFolder qubFolder = QubFolderTests.getQubFolder("/qub/");
                    final QubPublisherFolder qubPublisherFolder = qubFolder.getPublisherFolder("spam").await();
                    test.assertNotNull(qubPublisherFolder);
                    test.assertEqual(Path.parse("/qub/spam/"), qubPublisherFolder.getPath());
                });

                runner.test("with existing publisher", (Test test) ->
                {
                    final Folder folder = QubFolderTests.createFolder("/qub/");
                    final QubFolder qubFolder = QubFolder.get(folder);

                    folder.createFolder("spam").await();
                    final QubPublisherFolder qubPublisherFolder = qubFolder.getPublisherFolder("spam").await();
                    test.assertNotNull(qubPublisherFolder);
                    test.assertEqual(Path.parse("/qub/spam/"), qubPublisherFolder.getPath());
                });
            });

            runner.testGroup("iterateProjectFolders(String)", () ->
            {
                runner.test("with null publisherName", (Test test) ->
                {
                    final QubFolder qubFolder = QubFolderTests.getQubFolder("/qub/");
                    test.assertThrows(() -> qubFolder.iterateProjectFolders(null),
                        new PreConditionFailure("publisherName cannot be null."));
                });

                runner.test("with empty publisherName", (Test test) ->
                {
                    final QubFolder qubFolder = QubFolderTests.getQubFolder("/qub/");
                    test.assertThrows(() -> qubFolder.iterateProjectFolders(""),
                        new PreConditionFailure("publisherName cannot be empty."));
                });

                runner.test("with non-existing QubFolder", (Test test) ->
                {
                    final QubFolder qubFolder = QubFolderTests.getQubFolder("/qub/");
                    test.assertEqual(Iterable.create(), qubFolder.iterateProjectFolders("me").toList());
                });

                runner.test("with existing empty QubFolder", (Test test) ->
                {
                    final QubFolder qubFolder = QubFolderTests.createQubFolder("/qub/");
                    test.assertEqual(Iterable.create(), qubFolder.iterateProjectFolders("me").toList());
                });

                runner.test("with existing QubFolder with files", (Test test) ->
                {
                    final Folder folder = QubFolderTests.createFolder("/qub/");
                    folder.createFile("shortcut.cmd").await();
                    final QubFolder qubFolder = QubFolder.get(folder);
                    test.assertEqual(Iterable.create(), qubFolder.iterateProjectFolders("me").toList());
                });

                runner.test("with existing empty QubPublisherFolder", (Test test) ->
                {
                    final Folder folder = QubFolderTests.createFolder("/qub/");
                    folder.createFolder("me").await();
                    final QubFolder qubFolder = QubFolder.get(folder);
                    test.assertEqual(Iterable.create(), qubFolder.iterateProjectFolders("me").toList());
                });

                runner.test("with existing QubProjectFolder", (Test test) ->
                {
                    final Folder folder = QubFolderTests.createFolder("/qub/");
                    final Folder subFolder = folder.createFolder("me/my-project/").await();
                    final QubFolder qubFolder = QubFolder.get(folder);
                    test.assertEqual(
                        Iterable.create(
                            QubProjectFolder.get(subFolder)),
                        qubFolder.iterateProjectFolders("me").toList());
                });
            });

            runner.testGroup("getProjectFolder(String,String)", () ->
            {
                runner.test("with null publisherName", (Test test) ->
                {
                    final QubFolder qubFolder = QubFolderTests.getQubFolder("/qub/");
                    test.assertThrows(() -> qubFolder.getProjectFolder(null, "my-project"),
                        new PreConditionFailure("publisherName cannot be null."));
                });

                runner.test("with empty publisherName", (Test test) ->
                {
                    final QubFolder qubFolder = QubFolderTests.getQubFolder("/qub/");
                    test.assertThrows(() -> qubFolder.getProjectFolder("", "my-project"),
                        new PreConditionFailure("publisherName cannot be empty."));
                });

                runner.test("with null projectName", (Test test) ->
                {
                    final QubFolder qubFolder = QubFolderTests.getQubFolder("/qub/");
                    test.assertThrows(() -> qubFolder.getProjectFolder("me", null),
                        new PreConditionFailure("projectName cannot be null."));
                });

                runner.test("with empty projectName", (Test test) ->
                {
                    final QubFolder qubFolder = QubFolderTests.getQubFolder("/qub/");
                    test.assertThrows(() -> qubFolder.getProjectFolder("me", ""),
                        new PreConditionFailure("projectName cannot be empty."));
                });

                runner.test("with non-existing publisher", (Test test) ->
                {
                    final QubFolder qubFolder = QubFolderTests.getQubFolder("/qub/");
                    final QubProjectFolder projectFolder = qubFolder.getProjectFolder("spam", "my-project").await();
                    test.assertNotNull(projectFolder);
                    test.assertEqual(Path.parse("/qub/spam/my-project/"), projectFolder.getPath());
                });

                runner.test("with non-existing project", (Test test) ->
                {
                    final Folder folder = QubFolderTests.createFolder("/qub/");
                    final QubFolder qubFolder = QubFolder.get(folder);

                    folder.createFolder("spam").await();
                    final QubProjectFolder projectFolder = qubFolder.getProjectFolder("spam", "my-project").await();
                    test.assertNotNull(projectFolder);
                    test.assertEqual(Path.parse("/qub/spam/my-project/"), projectFolder.getPath());
                });

                runner.test("with existing project", (Test test) ->
                {
                    final Folder folder = QubFolderTests.createFolder("/qub/");
                    final QubFolder qubFolder = QubFolder.get(folder);

                    folder.createFolder("spam/my-project/").await();
                    final QubProjectFolder projectFolder = qubFolder.getProjectFolder("spam", "my-project").await();
                    test.assertNotNull(projectFolder);
                    test.assertEqual(Path.parse("/qub/spam/my-project/"), projectFolder.getPath());
                });
            });

            runner.testGroup("getProjectDataFolder(String,String)", () ->
            {
                runner.test("with null publisherName", (Test test) ->
                {
                    final QubFolder qubFolder = QubFolderTests.getQubFolder("/qub/");
                    test.assertThrows(() -> qubFolder.getProjectDataFolder(null, "my-project"),
                        new PreConditionFailure("publisherName cannot be null."));
                });

                runner.test("with empty publisherName", (Test test) ->
                {
                    final QubFolder qubFolder = QubFolderTests.getQubFolder("/qub/");
                    test.assertThrows(() -> qubFolder.getProjectDataFolder("", "my-project"),
                        new PreConditionFailure("publisherName cannot be empty."));
                });

                runner.test("with null projectName", (Test test) ->
                {
                    final QubFolder qubFolder = QubFolderTests.getQubFolder("/qub/");
                    test.assertThrows(() -> qubFolder.getProjectDataFolder("me", null),
                        new PreConditionFailure("projectName cannot be null."));
                });

                runner.test("with empty projectName", (Test test) ->
                {
                    final QubFolder qubFolder = QubFolderTests.getQubFolder("/qub/");
                    test.assertThrows(() -> qubFolder.getProjectDataFolder("me", ""),
                        new PreConditionFailure("projectName cannot be empty."));
                });

                runner.test("with non-existing publisher", (Test test) ->
                {
                    final QubFolder qubFolder = QubFolderTests.getQubFolder("/qub/");
                    final Folder projectDataFolder = qubFolder.getProjectDataFolder("spam", "my-project").await();
                    test.assertNotNull(projectDataFolder);
                    test.assertEqual(Path.parse("/qub/spam/my-project/data/"), projectDataFolder.getPath());
                });

                runner.test("with non-existing project", (Test test) ->
                {
                    final Folder folder = QubFolderTests.createFolder("/qub/");
                    final QubFolder qubFolder = QubFolder.get(folder);

                    folder.createFolder("spam").await();
                    final Folder projectDataFolder = qubFolder.getProjectDataFolder("spam", "my-project").await();
                    test.assertNotNull(projectDataFolder);
                    test.assertEqual(Path.parse("/qub/spam/my-project/data/"), projectDataFolder.getPath());
                });

                runner.test("with existing project", (Test test) ->
                {
                    final Folder folder = QubFolderTests.createFolder("/qub/");
                    final QubFolder qubFolder = QubFolder.get(folder);

                    folder.createFolder("spam/my-project/").await();
                    final Folder projectDataFolder = qubFolder.getProjectDataFolder("spam", "my-project").await();
                    test.assertNotNull(projectDataFolder);
                    test.assertEqual(Path.parse("/qub/spam/my-project/data/"), projectDataFolder.getPath());
                });
            });

            runner.testGroup("iterateProjectVersionFolders(String,String)", () ->
            {
                final Action3<String,String,Throwable> iterateProjectVersionFoldersErrorTest = (String publisherName, String projectName, Throwable expected) ->
                {
                    runner.test("with " + English.andList(Iterable.create(publisherName, projectName).map(Strings::escapeAndQuote)), (Test test) ->
                    {
                        final QubFolder qubFolder = QubFolderTests.getQubFolder("/qub/");
                        test.assertThrows(() -> qubFolder.iterateProjectVersionFolders(publisherName, projectName).toList(), expected);
                    });
                };

                iterateProjectVersionFoldersErrorTest.run(null, null, new PreConditionFailure("publisherName cannot be null."));
                iterateProjectVersionFoldersErrorTest.run("", null, new PreConditionFailure("publisherName cannot be empty."));
                iterateProjectVersionFoldersErrorTest.run("a", null, new PreConditionFailure("projectName cannot be null."));
                iterateProjectVersionFoldersErrorTest.run("a", "", new PreConditionFailure("projectName cannot be empty."));

                runner.test("with non-existing Qub folder", (Test test) ->
                {
                    final QubFolder qubFolder = QubFolderTests.getQubFolder("/qub/");
                    final String publisherName = "a";
                    final String projectName = "b";
                    test.assertEqual(Iterable.create(), qubFolder.iterateProjectVersionFolders(publisherName, projectName).toList());
                });

                runner.test("with non-existing publisher folder", (Test test) ->
                {
                    final QubFolder qubFolder = QubFolderTests.getQubFolder("/qub/");
                    qubFolder.create().await();
                    final String publisherName = "a";
                    final String projectName = "b";
                    test.assertEqual(Iterable.create(), qubFolder.iterateProjectVersionFolders(publisherName, projectName).toList());
                });

                runner.test("with non-existing project folder", (Test test) ->
                {
                    final QubFolder qubFolder = QubFolderTests.getQubFolder("/qub/");
                    final String publisherName = "a";
                    qubFolder.getPublisherFolder(publisherName).await().create().await();
                    final String projectName = "b";
                    test.assertEqual(Iterable.create(), qubFolder.iterateProjectVersionFolders(publisherName, projectName).toList());
                });

                runner.test("with empty project folder", (Test test) ->
                {
                    final QubFolder qubFolder = QubFolderTests.getQubFolder("/qub/");
                    final String publisherName = "a";
                    final String projectName = "b";
                    final QubProjectFolder projectFolder = qubFolder.getProjectFolder(publisherName, projectName).await();
                    projectFolder.create().await();
                    test.assertEqual(Iterable.create(), qubFolder.iterateProjectVersionFolders(publisherName, projectName).toList());
                });

                runner.test("with one version folder", (Test test) ->
                {
                    final QubFolder qubFolder = QubFolderTests.getQubFolder("/qub/");
                    final String publisherName = "a";
                    final String projectName = "b";
                    final QubProjectFolder projectFolder = qubFolder.getProjectFolder(publisherName, projectName).await();
                    projectFolder.createFolder("versions/1").await();
                    test.assertEqual(
                        Iterable.create(
                            qubFolder.getProjectVersionFolder(publisherName, projectName, "1").await()),
                        qubFolder.iterateProjectVersionFolders(publisherName, projectName).toList());
                });
            });

            runner.testGroup("getProjectVersionFolder(String,String,String)", () ->
            {
                final Action4<String,String,String,Throwable> getProjectVersionFolderErrorTest = (String publisherName, String projectName, String version, Throwable expected) ->
                {
                    runner.test("with " + English.andList(Iterable.create(publisherName, projectName, version).map(Strings::escapeAndQuote)), (Test test) ->
                    {
                        final QubFolder qubFolder = QubFolderTests.getQubFolder("/qub/");
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
                    final QubFolder qubFolder = QubFolderTests.getQubFolder("/qub/");
                    final String publisherName = "a";
                    final String projectName = "b";
                    final String version = "1";
                    test.assertEqual(
                        qubFolder.getFolder(publisherName + "/" + projectName + "/versions/" + version).await(),
                        qubFolder.getProjectVersionFolder(publisherName, projectName, version).await());
                });

                runner.test("with non-existing publisher folder", (Test test) ->
                {
                    final QubFolder qubFolder = QubFolderTests.getQubFolder("/qub/");
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
                    final QubFolder qubFolder = QubFolderTests.getQubFolder("/qub/");
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
                    final QubFolder qubFolder = QubFolderTests.getQubFolder("/qub/");
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
                    final QubFolder qubFolder = QubFolderTests.getQubFolder("/qub/");
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
                    final QubFolder qubFolder = QubFolderTests.getQubFolder("/qub/");
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
                    final QubFolder qubFolder = QubFolderTests.getQubFolder("/qub/");
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
                        final QubFolder qubFolder = QubFolderTests.getQubFolder("/qub/");
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
                    final QubFolder qubFolder = QubFolderTests.getQubFolder("/qub/");
                    final String publisherName = "a";
                    final String projectName = "b";
                    final VersionNumber version = VersionNumber.create().setMajor(1);
                    test.assertEqual(
                        qubFolder.getFolder(publisherName + "/" + projectName + "/versions/" + version).await(),
                        qubFolder.getProjectVersionFolder(publisherName, projectName, version).await());
                });

                runner.test("with non-existing publisher folder", (Test test) ->
                {
                    final QubFolder qubFolder = QubFolderTests.getQubFolder("/qub/");
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
                    final QubFolder qubFolder = QubFolderTests.getQubFolder("/qub/");
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
                    final QubFolder qubFolder = QubFolderTests.getQubFolder("/qub/");
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
                    final QubFolder qubFolder = QubFolderTests.getQubFolder("/qub/");
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
                    final QubFolder qubFolder = QubFolderTests.getQubFolder("/qub/");
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
                    final QubFolder qubFolder = QubFolderTests.getQubFolder("/qub/");
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
                        final QubFolder qubFolder = QubFolderTests.getQubFolder("/qub/");
                        test.assertThrows(() -> qubFolder.getProjectVersionFolder(projectSignature), expected);
                    });
                };

                getProjectVersionFolderErrorTest.run(null, new PreConditionFailure("projectSignature cannot be null."));

                runner.test("with non-existing Qub folder", (Test test) ->
                {
                    final QubFolder qubFolder = QubFolderTests.getQubFolder("/qub/");
                    final ProjectSignature projectSignature = ProjectSignature.create("a", "b", "1");
                    test.assertEqual(
                        qubFolder.getFolder(projectSignature.getPublisher() + "/" + projectSignature.getProject() + "/versions/" + projectSignature.getVersion()).await(),
                        qubFolder.getProjectVersionFolder(projectSignature).await());
                });

                runner.test("with non-existing publisher folder", (Test test) ->
                {
                    final QubFolder qubFolder = QubFolderTests.getQubFolder("/qub/");
                    qubFolder.create().await();
                    final ProjectSignature projectSignature = ProjectSignature.create("a", "b", "1");
                    test.assertEqual(
                        qubFolder.getFolder(projectSignature.getPublisher() + "/" + projectSignature.getProject() + "/versions/" + projectSignature.getVersion()).await(),
                        qubFolder.getProjectVersionFolder(projectSignature).await());
                });

                runner.test("with non-existing project folder", (Test test) ->
                {
                    final QubFolder qubFolder = QubFolderTests.getQubFolder("/qub/");
                    final ProjectSignature projectSignature = ProjectSignature.create("a", "b", "1");
                    qubFolder.getPublisherFolder(projectSignature.getPublisher()).await().create().await();
                    test.assertEqual(
                        qubFolder.getFolder(projectSignature.getPublisher() + "/" + projectSignature.getProject() + "/versions/" + projectSignature.getVersion()).await(),
                        qubFolder.getProjectVersionFolder(projectSignature).await());
                });

                runner.test("with non-existing versions folder", (Test test) ->
                {
                    final QubFolder qubFolder = QubFolderTests.getQubFolder("/qub/");
                    final ProjectSignature projectSignature = ProjectSignature.create("a", "b", "1");
                    qubFolder.getProjectFolder(projectSignature.getPublisher(), projectSignature.getProject()).await().create().await();
                    test.assertEqual(
                        qubFolder.getFolder(projectSignature.getPublisher() + "/" + projectSignature.getProject() + "/versions/" + projectSignature.getVersion()).await(),
                        qubFolder.getProjectVersionFolder(projectSignature).await());
                });

                runner.test("with empty versions folder", (Test test) ->
                {
                    final QubFolder qubFolder = QubFolderTests.getQubFolder("/qub/");
                    final ProjectSignature projectSignature = ProjectSignature.create("a", "b", "1");
                    final QubProjectFolder projectFolder = qubFolder.getProjectFolder(projectSignature.getPublisher(), projectSignature.getProject()).await();
                    projectFolder.createFolder("versions").await();
                    test.assertEqual(
                        qubFolder.getFolder(projectSignature.getPublisher() + "/" + projectSignature.getProject() + "/versions/" + projectSignature.getVersion()).await(),
                        qubFolder.getProjectVersionFolder(projectSignature).await());
                });

                runner.test("with non-existing project.json file", (Test test) ->
                {
                    final QubFolder qubFolder = QubFolderTests.getQubFolder("/qub/");
                    final ProjectSignature projectSignature = ProjectSignature.create("a", "b", "1");
                    final QubProjectVersionFolder projectVersionFolder = qubFolder.getProjectVersionFolder(projectSignature.getPublisher(), projectSignature.getProject(), projectSignature.getVersion()).await();
                    projectVersionFolder.create().await();
                    test.assertEqual(
                        qubFolder.getFolder(projectSignature.getPublisher() + "/" + projectSignature.getProject() + "/versions/" + projectSignature.getVersion()).await(),
                        qubFolder.getProjectVersionFolder(projectSignature).await());
                });

                runner.test("with existing project.json file", (Test test) ->
                {
                    final QubFolder qubFolder = QubFolderTests.getQubFolder("/qub/");
                    final ProjectSignature projectSignature = ProjectSignature.create("a", "b", "1");
                    final QubProjectVersionFolder projectVersionFolder = qubFolder.getProjectVersionFolder(projectSignature.getPublisher(), projectSignature.getProject(), projectSignature.getVersion()).await();
                    projectVersionFolder.createFile("project.json").await();
                    test.assertEqual(
                        qubFolder.getFolder(projectSignature.getPublisher() + "/" + projectSignature.getProject() + "/versions/" + projectSignature.getVersion()).await(),
                        qubFolder.getProjectVersionFolder(projectSignature).await());
                });
            });

            runner.testGroup("getLatestProjectVersionFolder(String,String)", () ->
            {
                runner.test("with no existing Qub folder", (Test test) ->
                {
                    final String publisherName = "a";
                    final String projectName = "b";
                    final QubFolder qubFolder = QubFolderTests.getQubFolder("/qub/");
                    test.assertThrows(() -> qubFolder.getLatestProjectVersionFolder(publisherName, projectName).await(),
                        new NotFoundException("No project named a/b has been published."));
                });

                runner.test("with no existing publisher folder", (Test test) ->
                {
                    final String publisherName = "a";
                    final String projectName = "b";
                    final QubFolder qubFolder = QubFolderTests.getQubFolder("/qub/");
                    qubFolder.create().await();
                    test.assertThrows(() -> qubFolder.getLatestProjectVersionFolder(publisherName, projectName).await(),
                        new NotFoundException("No project named a/b has been published."));
                });

                runner.test("with no existing project folder", (Test test) ->
                {
                    final String publisherName = "a";
                    final String projectName = "b";
                    final QubFolder qubFolder = QubFolderTests.getQubFolder("/qub/");
                    qubFolder.getPublisherFolder(publisherName).await().create().await();
                    test.assertThrows(() -> qubFolder.getLatestProjectVersionFolder(publisherName, projectName).await(),
                        new NotFoundException("No project named a/b has been published."));
                });

                runner.test("with no existing versions folder", (Test test) ->
                {
                    final String publisherName = "a";
                    final String projectName = "b";
                    final QubFolder qubFolder = QubFolderTests.getQubFolder("/qub/");
                    qubFolder.getProjectFolder(publisherName, projectName).await().create().await();
                    test.assertThrows(() -> qubFolder.getLatestProjectVersionFolder(publisherName, projectName).await(),
                        new NotFoundException("No project named a/b has been published."));
                });

                runner.test("with no existing version folders", (Test test) ->
                {
                    final String publisherName = "a";
                    final String projectName = "b";
                    final QubFolder qubFolder = QubFolderTests.getQubFolder("/qub/");
                    qubFolder.getProjectVersionsFolder(publisherName, projectName).await().create().await();
                    test.assertThrows(() -> qubFolder.getLatestProjectVersionFolder(publisherName, projectName).await(),
                        new NotFoundException("No project named a/b has been published."));
                });

                runner.test("with one version", (Test test) ->
                {
                    final String publisherName = "a";
                    final String projectName = "b";
                    final QubFolder qubFolder = QubFolderTests.getQubFolder("/qub/");
                    qubFolder.getProjectVersionFolder(publisherName, projectName, "1").await().create().await();
                    test.assertEqual("/qub/" + publisherName + "/" + projectName + "/versions/1/", qubFolder.getLatestProjectVersionFolder(publisherName, projectName).await().toString());
                });

                runner.test("with two versions", (Test test) ->
                {
                    final String publisherName = "a";
                    final String projectName = "b";
                    final QubFolder qubFolder = QubFolderTests.getQubFolder("/qub/");
                    qubFolder.getProjectVersionFolder(publisherName, projectName, "2").await().create().await();
                    qubFolder.getProjectVersionFolder(publisherName, projectName, "3").await().create().await();
                    test.assertEqual("/qub/" + publisherName + "/" + projectName + "/versions/3/", qubFolder.getLatestProjectVersionFolder(publisherName, projectName).await().toString());
                });

                runner.test("with three non-sorted versions", (Test test) ->
                {
                    final String publisherName = "a";
                    final String projectName = "b";
                    final QubFolder qubFolder = QubFolderTests.getQubFolder("/qub/");
                    qubFolder.getProjectVersionFolder(publisherName, projectName, "2").await().create().await();
                    qubFolder.getProjectVersionFolder(publisherName, projectName, "30").await().create().await();
                    qubFolder.getProjectVersionFolder(publisherName, projectName, "13").await().create().await();
                    test.assertEqual("/qub/" + publisherName + "/" + projectName + "/versions/30/", qubFolder.getLatestProjectVersionFolder(publisherName, projectName).await().toString());
                });

                runner.test("with non-integer versions", (Test test) ->
                {
                    final String publisherName = "a";
                    final String projectName = "b";
                    final QubFolder qubFolder = QubFolderTests.getQubFolder("/qub/");
                    qubFolder.getProjectVersionFolder(publisherName, projectName, "2").await().create().await();
                    qubFolder.getProjectVersionFolder(publisherName, projectName, "3.0.3").await().create().await();
                    qubFolder.getProjectVersionFolder(publisherName, projectName, "1.3").await().create().await();
                    test.assertEqual("/qub/" + publisherName + "/" + projectName + "/versions/3.0.3/", qubFolder.getLatestProjectVersionFolder(publisherName, projectName).await().toString());
                });
            });

            runner.testGroup("getAllDependencyFolders(Iterable<ProjectSignature>,Function1<QubProjectVersionFolder,Iterable<ProjectSignature>>,boolean)", () ->
            {
                runner.test("with null projectSignatures", (Test test) ->
                {
                    final QubFolder qubFolder = QubFolderTests.getQubFolder("/qub/");
                    final Iterable<ProjectSignature> projectSignatures = null;
                    final Function1<QubProjectVersionFolder,Result<Iterable<ProjectSignature>>> getDependenciesFunction = (QubProjectVersionFolder folder) -> Result.success(Iterable.create());
                    final boolean validateDependencies = false;
                    test.assertThrows(() -> qubFolder.getAllDependencyFolders(projectSignatures, getDependenciesFunction, validateDependencies),
                        new PreConditionFailure("projectSignatures cannot be null."));
                });

                runner.test("with null getDependenciesFunction", (Test test) ->
                {
                    final QubFolder qubFolder = QubFolderTests.getQubFolder("/qub/");
                    final Iterable<ProjectSignature> projectSignatures = Iterable.create();
                    final Function1<QubProjectVersionFolder,Result<Iterable<ProjectSignature>>> getDependenciesFunction = null;
                    final boolean validateDependencies = false;
                    test.assertThrows(() -> qubFolder.getAllDependencyFolders(projectSignatures, getDependenciesFunction, validateDependencies),
                        new PreConditionFailure("getDependenciesFunction cannot be null."));
                });

                runner.test("with empty projectSignatures", (Test test) ->
                {
                    final QubFolder qubFolder = QubFolderTests.getQubFolder("/qub/");
                    final Iterable<ProjectSignature> projectSignatures = Iterable.create();
                    final Function1<QubProjectVersionFolder,Result<Iterable<ProjectSignature>>> getDependenciesFunction = (QubProjectVersionFolder folder) -> Result.success(Iterable.create());
                    final boolean validateDependencies = false;
                    test.assertEqual(
                        Iterable.create(),
                        qubFolder.getAllDependencyFolders(projectSignatures, getDependenciesFunction, validateDependencies).await());
                });

                runner.test("with projectSignature that doesn't exist and validateDependencies is false", (Test test) ->
                {
                    final QubFolder qubFolder = QubFolderTests.getQubFolder("/qub/");
                    final QubProjectVersionFolder ab1 = qubFolder.getProjectVersionFolder("a", "b", "1").await();
                    final Iterable<ProjectSignature> projectSignatures = Iterable.create(
                        ab1.getProjectSignature().await());
                    final Function1<QubProjectVersionFolder,Result<Iterable<ProjectSignature>>> getDependenciesFunction = (QubProjectVersionFolder folder) -> Result.success(Iterable.create());
                    final boolean validateDependencies = false;
                    test.assertEqual(
                        Iterable.create(
                            ab1),
                        qubFolder.getAllDependencyFolders(projectSignatures, getDependenciesFunction, validateDependencies).await());
                });

                runner.test("with projectSignature that doesn't exist and validateDependencies is true", (Test test) ->
                {
                    final QubFolder qubFolder = QubFolderTests.getQubFolder("/qub/");
                    final QubProjectVersionFolder ab1 = qubFolder.getProjectVersionFolder("a", "b", "1").await();
                    final Iterable<ProjectSignature> projectSignatures = Iterable.create(
                        ab1.getProjectSignature().await());
                    final Function1<QubProjectVersionFolder,Result<Iterable<ProjectSignature>>> getDependenciesFunction = (QubProjectVersionFolder folder) ->
                    {
                        return Result.create(() ->
                        {
                            throw new NotFoundException(folder.toString());
                        });
                    };
                    final boolean validateDependencies = true;
                    test.assertThrows(() -> qubFolder.getAllDependencyFolders(projectSignatures, getDependenciesFunction, validateDependencies).await(),
                        new NotFoundException(ab1.toString()));
                });

                runner.test("with dependency that doesn't exist and validateDependencies is false", (Test test) ->
                {
                    final QubFolder qubFolder = QubFolderTests.getQubFolder("/qub/");
                    final QubProjectVersionFolder ab1 = qubFolder.getProjectVersionFolder("a", "b", "1").await();
                    final QubProjectVersionFolder cd2 = qubFolder.getProjectVersionFolder("c", "d", "2").await();
                    final Iterable<ProjectSignature> projectSignatures = Iterable.create(
                        ab1.getProjectSignature().await());
                    final Function1<QubProjectVersionFolder,Result<Iterable<ProjectSignature>>> getDependenciesFunction = (QubProjectVersionFolder folder) ->
                    {
                        return Result.create(() ->
                        {
                            final List<ProjectSignature> result = List.create();
                            if (folder.equals(ab1))
                            {
                                result.add(cd2.getProjectSignature().await());
                            }
                            else
                            {
                                throw new NotFoundException(folder.toString());
                            }
                            return result;
                        });
                    };
                    final boolean validateDependencies = false;
                    test.assertEqual(
                        Iterable.create(
                            ab1,
                            cd2),
                        qubFolder.getAllDependencyFolders(projectSignatures, getDependenciesFunction, validateDependencies).await());
                });

                runner.test("with dependency that doesn't exist and validateDependencies is true", (Test test) ->
                {
                    final QubFolder qubFolder = QubFolderTests.getQubFolder("/qub/");
                    final QubProjectVersionFolder ab1 = qubFolder.getProjectVersionFolder("a", "b", "1").await();
                    final QubProjectVersionFolder cd2 = qubFolder.getProjectVersionFolder("c", "d", "2").await();
                    final Iterable<ProjectSignature> projectSignatures = Iterable.create(
                        ab1.getProjectSignature().await());
                    final Function1<QubProjectVersionFolder,Result<Iterable<ProjectSignature>>> getDependenciesFunction = (QubProjectVersionFolder folder) ->
                    {
                        return Result.create(() ->
                        {
                            final List<ProjectSignature> result = List.create();
                            if (folder.equals(ab1))
                            {
                                result.add(cd2.getProjectSignature().await());
                            }
                            else
                            {
                                throw new NotFoundException(folder.toString());
                            }
                            return result;
                        });
                    };
                    final boolean validateDependencies = true;
                    test.assertThrows(() -> qubFolder.getAllDependencyFolders(projectSignatures, getDependenciesFunction, validateDependencies).await(),
                        new NotFoundException(cd2.toString()));
                });

                runner.test("with diamond dependencies and validateDependencies is false", (Test test) ->
                {
                    final QubFolder qubFolder = QubFolderTests.getQubFolder("/qub/");
                    final QubProjectVersionFolder ab1 = qubFolder.getProjectVersionFolder("a", "b", "1").await();
                    final QubProjectVersionFolder cd2 = qubFolder.getProjectVersionFolder("c", "d", "2").await();
                    final QubProjectVersionFolder ef3 = qubFolder.getProjectVersionFolder("e", "f", "3").await();
                    final QubProjectVersionFolder gh4 = qubFolder.getProjectVersionFolder("g", "h", "4").await();
                    final Iterable<ProjectSignature> projectSignatures = Iterable.create(
                        ab1.getProjectSignature().await());
                    final Function1<QubProjectVersionFolder,Result<Iterable<ProjectSignature>>> getDependenciesFunction = (QubProjectVersionFolder folder) ->
                    {
                        return Result.create(() ->
                        {
                            final List<ProjectSignature> result = List.create();
                            if (folder.equals(ab1))
                            {
                                result.addAll(
                                    cd2.getProjectSignature().await(),
                                    ef3.getProjectSignature().await());
                            }
                            else if (folder.equals(cd2) || folder.equals(ef3))
                            {
                                result.add(gh4.getProjectSignature().await());
                            }
                            return result;
                        });
                    };
                    final boolean validateDependencies = false;
                    test.assertEqual(
                        Iterable.create(
                            ab1,
                            cd2,
                            gh4,
                            ef3),
                        qubFolder.getAllDependencyFolders(projectSignatures, getDependenciesFunction, validateDependencies).await());
                });

                runner.test("with diamond dependencies and validateDependencies is true", (Test test) ->
                {
                    final QubFolder qubFolder = QubFolderTests.getQubFolder("/qub/");
                    final QubProjectVersionFolder ab1 = qubFolder.getProjectVersionFolder("a", "b", "1").await();
                    final QubProjectVersionFolder cd2 = qubFolder.getProjectVersionFolder("c", "d", "2").await();
                    final QubProjectVersionFolder ef3 = qubFolder.getProjectVersionFolder("e", "f", "3").await();
                    final QubProjectVersionFolder gh4 = qubFolder.getProjectVersionFolder("g", "h", "4").await();
                    final Iterable<ProjectSignature> projectSignatures = Iterable.create(
                        ab1.getProjectSignature().await());
                    final Function1<QubProjectVersionFolder,Result<Iterable<ProjectSignature>>> getDependenciesFunction = (QubProjectVersionFolder folder) ->
                    {
                        return Result.create(() ->
                        {
                            final List<ProjectSignature> result = List.create();
                            if (folder.equals(ab1))
                            {
                                result.addAll(
                                    cd2.getProjectSignature().await(),
                                    ef3.getProjectSignature().await());
                            }
                            else if (folder.equals(cd2) || folder.equals(ef3))
                            {
                                result.add(gh4.getProjectSignature().await());
                            }
                            return result;
                        });
                    };
                    final boolean validateDependencies = true;
                    test.assertEqual(
                        Iterable.create(
                            ab1,
                            cd2,
                            gh4,
                            ef3),
                        qubFolder.getAllDependencyFolders(projectSignatures, getDependenciesFunction, validateDependencies).await());
                });

                runner.test("with conflicting dependency versions and validateDependencies is false", (Test test) ->
                {
                    final QubFolder qubFolder = QubFolderTests.getQubFolder("/qub/");
                    final QubProjectVersionFolder ab1 = qubFolder.getProjectVersionFolder("a", "b", "1").await();
                    final QubProjectVersionFolder cd2 = qubFolder.getProjectVersionFolder("c", "d", "2").await();
                    final QubProjectVersionFolder cd3 = qubFolder.getProjectVersionFolder("c", "d", "3").await();
                    final Iterable<ProjectSignature> projectSignatures = Iterable.create(
                        ab1.getProjectSignature().await());
                    final Function1<QubProjectVersionFolder,Result<Iterable<ProjectSignature>>> getDependenciesFunction = (QubProjectVersionFolder folder) ->
                    {
                        return Result.create(() ->
                        {
                            final List<ProjectSignature> result = List.create();
                            if (folder.equals(ab1))
                            {
                                result.addAll(
                                    cd2.getProjectSignature().await(),
                                    cd3.getProjectSignature().await());
                            }
                            return result;
                        });
                    };
                    final boolean validateDependencies = false;
                    test.assertEqual(
                        Iterable.create(
                            ab1,
                            cd2,
                            cd3),
                        qubFolder.getAllDependencyFolders(projectSignatures, getDependenciesFunction, validateDependencies).await());
                });

                runner.test("with conflicting dependency versions and validateDependencies is true", (Test test) ->
                {
                    final QubFolder qubFolder = QubFolderTests.getQubFolder("/qub/");
                    final QubProjectVersionFolder ab1 = qubFolder.getProjectVersionFolder("a", "b", "1").await();
                    final QubProjectVersionFolder cd2 = qubFolder.getProjectVersionFolder("c", "d", "2").await();
                    final QubProjectVersionFolder cd3 = qubFolder.getProjectVersionFolder("c", "d", "3").await();
                    final Iterable<ProjectSignature> projectSignatures = Iterable.create(
                        ab1.getProjectSignature().await());
                    final Function1<QubProjectVersionFolder,Result<Iterable<ProjectSignature>>> getDependenciesFunction = (QubProjectVersionFolder folder) ->
                    {
                        return Result.create(() ->
                        {
                            final List<ProjectSignature> result = List.create();
                            if (folder.equals(ab1))
                            {
                                result.addAll(
                                    cd2.getProjectSignature().await(),
                                    cd3.getProjectSignature().await());
                            }
                            return result;
                        });
                    };
                    final boolean validateDependencies = true;
                    test.assertThrows(() -> qubFolder.getAllDependencyFolders(projectSignatures, getDependenciesFunction, validateDependencies).await(),
                        new RuntimeException(Strings.join('\n', Iterable.create(
                            "Found more than one required version for package c/d:",
                            "1. c/d@2",
                            "    from a/b@1",
                            "2. c/d@3",
                            "    from a/b@1"))));
                });
            });

            runner.testGroup("equals(Object)", () ->
            {
                runner.test("with /qub/ and null", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = QubFolderTests.createFileSystem();
                    final Folder folder = fileSystem.getFolder("/qub/").await();
                    final QubFolder qubFolder = QubFolder.get(folder);
                    test.assertEqual(false, qubFolder.equals((Object)null));
                });

                runner.test("with /qub/ and \"hello world\"", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = QubFolderTests.createFileSystem();
                    final Folder folder = fileSystem.getFolder("/qub/").await();
                    final QubFolder qubFolder = QubFolder.get(folder);
                    test.assertEqual(false, qubFolder.equals((Object)"hello world"));
                });

                runner.test("with /qub/ and /other/", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = QubFolderTests.createFileSystem();

                    final Folder folder = fileSystem.getFolder("/qub/").await();
                    final QubFolder qubFolder = QubFolder.get(folder);

                    final Folder folder2 = fileSystem.getFolder("/other/").await();
                    final QubFolder qubFolder2 = QubFolder.get(folder2);

                    test.assertEqual(false, qubFolder.equals((Object)qubFolder2));
                });

                runner.test("with /qub/ and /qub/", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = QubFolderTests.createFileSystem();

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
                    final InMemoryFileSystem fileSystem = QubFolderTests.createFileSystem();
                    final Folder folder = fileSystem.getFolder("/qub/").await();
                    final QubFolder qubFolder = QubFolder.get(folder);
                    test.assertEqual(false, qubFolder.equals((QubFolder)null));
                });

                runner.test("with /qub/ and /other/", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = QubFolderTests.createFileSystem();

                    final Folder folder = fileSystem.getFolder("/qub/").await();
                    final QubFolder qubFolder = QubFolder.get(folder);

                    final Folder folder2 = fileSystem.getFolder("/other/").await();
                    final QubFolder qubFolder2 = QubFolder.get(folder2);

                    test.assertEqual(false, qubFolder.equals(qubFolder2));
                });

                runner.test("with /qub/ and /qub/", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = QubFolderTests.createFileSystem();

                    final Folder folder = fileSystem.getFolder("/qub/").await();
                    final QubFolder qubFolder = QubFolder.get(folder);

                    final Folder folder2 = fileSystem.getFolder("/qub/").await();
                    final QubFolder qubFolder2 = QubFolder.get(folder2);

                    test.assertEqual(true, qubFolder.equals(qubFolder2));
                });
            });

            runner.test("toString()", (Test test) ->
            {
                final QubFolder qubFolder = QubFolderTests.getQubFolder("/qub/");
                test.assertEqual("/qub/", qubFolder.toString());
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

        final InMemoryFileSystem fileSystem = QubFolderTests.createFileSystem();
        return fileSystem.getFolder(folderPath).await();
    }

    static Folder createFolder(String folderPath)
    {
        PreCondition.assertNotNullAndNotEmpty(folderPath, "folderPath");

        final InMemoryFileSystem fileSystem = QubFolderTests.createFileSystem();
        return fileSystem.createFolder(folderPath).await();
    }

    static QubFolder getQubFolder(String folderPath)
    {
        return QubFolder.get(QubFolderTests.getFolder(folderPath));
    }

    static QubFolder createQubFolder(String folderPath)
    {
        return QubFolder.get(QubFolderTests.createFolder(folderPath));
    }
}
