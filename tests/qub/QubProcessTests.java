package qub;

public interface QubProcessTests
{
    static void test(TestRunner runner, Function0<? extends QubProcess> creator)
    {
        runner.testGroup(QubProcess.class, () ->
        {
            ProcessTests.test(runner, creator);

            runner.test("getQubFolder()", (Test test) ->
            {
                try (final QubProcess qubProcess = creator.run())
                {
                    final QubFolder folder = qubProcess.getQubFolder().await();
                    test.assertNotNull(folder);
                    test.assertTrue(folder.exists().await());
                }
            });

            runner.test("getQubProjectVersionFolder()", (Test test) ->
            {
                try (final QubProcess qubProcess = creator.run())
                {
                    final QubProjectVersionFolder projectVersionFolder = qubProcess.getQubProjectVersionFolder().await();
                    test.assertNotNull(projectVersionFolder);
                    test.assertEqual("qub", projectVersionFolder.getPublisherName().await());
                    test.assertEqual("test-java", projectVersionFolder.getProjectName().await());
                }
            });

            runner.test("getPublisherName()", (Test test) ->
            {
                try (final QubProcess qubProcess = creator.run())
                {
                    test.assertEqual("qub", qubProcess.getPublisherName().await());
                }
            });

            runner.test("getProjectName()", (Test test) ->
            {
                try (final QubProcess qubProcess = creator.run())
                {
                    test.assertEqual("test-java", qubProcess.getProjectName().await());
                }
            });

            runner.test("getVersion()", (Test test) ->
            {
                try (final QubProcess qubProcess = creator.run())
                {
                    test.assertFalse(Strings.isNullOrEmpty(qubProcess.getVersion().await()));
                }
            });

            runner.test("getProjectDataFolder()", (Test test) ->
            {
                try (final QubProcess qubProcess = creator.run())
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
