package qub;

public interface QubProjectVersionFolderTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(QubProjectVersionFolder.class, () ->
        {
            runner.testGroup("create(Folder)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() ->  QubProjectVersionFolder.create(null),
                        new PreConditionFailure("projectVersionFolder cannot be null."));
                });

                runner.test("with root folder", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = new InMemoryFileSystem(test.getClock());
                    fileSystem.createRoot("/").await();
                    final Folder rootFolder = fileSystem.getFolder("/").await();

                    test.assertThrows(() -> QubProjectVersionFolder.create(rootFolder),
                        new PreConditionFailure("projectVersionFolder.getPath().getSegments().getCount() (1) must be greater than or equal to 4."));
                });

                runner.test("with qub folder", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = new InMemoryFileSystem(test.getClock());
                    fileSystem.createRoot("/").await();
                    final Folder qubFolder = fileSystem.getFolder("/").await();

                    test.assertThrows(() -> QubProjectVersionFolder.create(qubFolder),
                        new PreConditionFailure("projectVersionFolder.getPath().getSegments().getCount() (1) must be greater than or equal to 4."));
                });

                runner.test("with publisher folder", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = new InMemoryFileSystem(test.getClock());
                    fileSystem.createRoot("/").await();
                    final Folder publisherFolder = fileSystem.createFolder("/fake-publisher/").await();

                    test.assertThrows(() -> QubProjectVersionFolder.create(publisherFolder),
                        new PreConditionFailure("projectVersionFolder.getPath().getSegments().getCount() (2) must be greater than or equal to 4."));
                });

                runner.test("with project folder", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = new InMemoryFileSystem(test.getClock());
                    fileSystem.createRoot("/").await();
                    final Folder projectFolder = fileSystem.createFolder("/fake-publisher/fake-project/").await();

                    test.assertThrows(() -> QubProjectVersionFolder.create(projectFolder),
                        new PreConditionFailure("projectVersionFolder.getPath().getSegments().getCount() (3) must be greater than or equal to 4."));
                });

                runner.test("with version folder", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = new InMemoryFileSystem(test.getClock());
                    fileSystem.createRoot("/").await();
                    final Folder versionFolder = fileSystem.createFolder("/fake-publisher/fake-project/fake-version/").await();

                    final QubProjectVersionFolder projectVersionFolder = QubProjectVersionFolder.create(versionFolder);
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
                        projectVersionFolder.getProjectSignature());
                });
            });
        });
    }
}
