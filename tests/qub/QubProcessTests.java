package qub;

public interface QubProcessTests
{
    static void test(TestRunner runner, Function1<Test,? extends QubProcess2> creator)
    {
        runner.testGroup(QubProcess2.class, () ->
        {
            ProcessTests.test(runner, creator);

            runner.test("getQubFolder()", (Test test) ->
            {
                try (final QubProcess2 qubProcess = creator.run(test))
                {
                    final QubFolder folder = qubProcess.getQubFolder().await();
                    test.assertNotNull(folder);
                    test.assertTrue(folder.exists().await());
                }
            });

            runner.test("getQubProjectVersionFolder()", (Test test) ->
            {
                try (final QubProcess2 qubProcess = creator.run(test))
                {
                    final QubProjectVersionFolder projectVersionFolder = qubProcess.getQubProjectVersionFolder().await();
                    test.assertNotNull(projectVersionFolder);
                    test.assertTrue(projectVersionFolder.exists().await());
                }
            });

            runner.test("getPublisherName()", (Test test) ->
            {
                try (final QubProcess2 qubProcess = creator.run(test))
                {
                    final String publisherName = qubProcess.getPublisherName().await();
                    test.assertNotNullAndNotEmpty(publisherName);
                }
            });

            runner.test("getProjectName()", (Test test) ->
            {
                try (final QubProcess2 qubProcess = creator.run(test))
                {
                    final String projectName = qubProcess.getProjectName().await();
                    test.assertNotNullAndNotEmpty(projectName);
                }
            });

            runner.test("getVersion()", (Test test) ->
            {
                try (final QubProcess2 qubProcess = creator.run(test))
                {
                    final VersionNumber version = qubProcess.getVersion().await();
                    test.assertNotNull(version);
                }
            });

            runner.test("getProjectDataFolder()", (Test test) ->
            {
                try (final QubProcess2 qubProcess = creator.run(test))
                {
                    final Folder qubProjectDataFolder = qubProcess.getQubProjectDataFolder().await();
                    test.assertNotNull(qubProjectDataFolder);
                    final File testFile = qubProjectDataFolder.getFile("test.txt").await();
                    testFile.setContentsAsString("hello").await();
                    try
                    {
                        final Folder projectDataFolder = qubProcess.getQubProjectFolder().await().getProjectDataFolder().await();
                        test.assertEqual("hello", projectDataFolder.getFileContentsAsString("test.txt").await());
                    }
                    finally
                    {
                        testFile.delete().await();
                    }
                }
            });
        });
    }
}
