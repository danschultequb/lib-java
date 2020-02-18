package qub;

public interface QubProcessTests
{
    static void test(TestRunner runner, Function0<? extends QubProcess> creator)
    {
        runner.testGroup(QubProcess.class, () ->
        {
            ProcessTests.test(runner, creator);

            runner.test("getQubProjectVersionFolder()", (Test test) ->
            {
                try (final QubProcess qubProcess = creator.run())
                {
                    final QubProjectVersionFolder projectVersionFolder = qubProcess.getQubProjectVersionFolder().await();
                    test.assertNotNull(projectVersionFolder);
                    test.assertEqual("qub", projectVersionFolder.getPublisherName().await());
                    test.assertEqual("qub-java-test", projectVersionFolder.getProjectName().await());
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
                    test.assertEqual("qub-java-test", qubProcess.getProjectName().await());
                }
            });

            runner.test("getVersion()", (Test test) ->
            {
                try (final QubProcess qubProcess = creator.run())
                {
                    test.assertFalse(Strings.isNullOrEmpty(qubProcess.getVersion().await()));
                }
            });

            runner.test("getProjectData()", (Test test) ->
            {
                try (final QubProcess qubProcess = creator.run())
                {
                    final FileSystem projectData = qubProcess.getProjectData().await();
                    test.assertNotNull(projectData);
                    final File testFile = projectData.getFile("/test.txt").await();
                    test.assertEqual("/test.txt", testFile.toString());
                    testFile.setContentsAsString("hello").await();
                    try
                    {
                        final QubProjectFolder projectFolder = qubProcess.getQubProjectVersionFolder().await().getProjectFolder().await();
                        test.assertEqual("hello", projectFolder.getFileContentsAsString("data/test.txt").await());
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
