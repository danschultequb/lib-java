package qub;

public interface ProcessTests
{
    static void test(TestRunner runner, Function1<Test,? extends Process> creator)
    {
        runner.testGroup(Process.class, () ->
        {
            runner.test("getMainAsyncRunner()", (Test test) ->
            {
                try (final Process process = creator.run(test))
                {
                    final AsyncScheduler mainAsyncRunner = process.getMainAsyncRunner();
                    test.assertNotNull(mainAsyncRunner);
                    test.assertSame(mainAsyncRunner, process.getMainAsyncRunner());
                    test.assertSame(mainAsyncRunner, CurrentThread.getAsyncRunner().await());
                }
            });

            runner.test("getParallelAsyncRunner()", (Test test) ->
            {
                try (final Process process = creator.run(test))
                {
                    final AsyncScheduler parallelAsyncRunner = process.getParallelAsyncRunner();
                    test.assertNotNull(parallelAsyncRunner);
                    test.assertSame(parallelAsyncRunner, process.getParallelAsyncRunner());
                    test.assertNotSame(parallelAsyncRunner, CurrentThread.getAsyncRunner().await());
                }
            });

            runner.test("getOutputWriteStream()", (Test test) ->
            {
                try (final Process process = creator.run(test))
                {
                    final CharacterToByteWriteStream writeStream = process.getOutputWriteStream();
                    test.assertNotNull(writeStream);
                }
            });

            runner.test("getErrorWriteStream()", (Test test) ->
            {
                try (final Process process = creator.run(test))
                {
                    final ByteWriteStream writeStream = process.getErrorWriteStream();
                    test.assertNotNull(writeStream);
                }
            });

            runner.test("getInputByteWriteStream()", (Test test) ->
            {
                final Process process = creator.run(test);
                final ByteReadStream readStream = process.getInputReadStream();
                test.assertNotNull(readStream);
            });

            runner.test("getInputReadStream()", (Test test) ->
            {
                final Process process = creator.run(test);
                final CharacterToByteReadStream readStream = process.getInputReadStream();
                test.assertNotNull(readStream);
            });

            runner.test("getRandom()", (Test test) ->
            {
                final Process process = creator.run(test);
                final Random random = process.getRandom();
                test.assertNotNull(random);
                test.assertSame(random, process.getRandom());
            });

            runner.test("getFileSystem()", (Test test) ->
            {
                final Process process = creator.run(test);
                final FileSystem defaultFileSystem = process.getFileSystem();
                test.assertNotNull(defaultFileSystem);
                test.assertSame(defaultFileSystem, process.getFileSystem());
            });

            runner.test("getNetwork()", (Test test) ->
            {
                final Process process = creator.run(test);
                final Network defaultNetwork = process.getNetwork();
                test.assertNotNull(defaultNetwork);
                test.assertSame(defaultNetwork, process.getNetwork());
            });

            runner.test("getCurrentFolderPathString()", (Test test) ->
            {
                final Process process = creator.run(test);
                final String currentFolderPathString = process.getCurrentFolderPathString();
                test.assertNotNull(currentFolderPathString);
                test.assertFalse(currentFolderPathString.isEmpty());
                test.assertEndsWith(currentFolderPathString, "/");
                test.assertTrue(process.getFileSystem().folderExists(currentFolderPathString).await());
            });

            runner.test("getCurrentFolderPath()", (Test test) ->
            {
                final Process process = creator.run(test);
                final Path currentFolderPath = process.getCurrentFolderPath();
                test.assertNotNull(currentFolderPath);
                test.assertTrue(currentFolderPath.isRooted());
                test.assertEndsWith(currentFolderPath.toString(), "/");
                test.assertTrue(process.getFileSystem().folderExists(currentFolderPath).await());
            });

            runner.test("getCurrentFolder()", (Test test) ->
            {
                try (final Process process = creator.run(test))
                {
                    final Folder currentFolder = process.getCurrentFolder();
                    test.assertNotNull(currentFolder);
                    test.assertTrue(currentFolder.exists().await());
                }
            });

            runner.testGroup("getEnvironmentVariable()", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final Process process = creator.run(test);
                    test.assertThrows(() -> process.getEnvironmentVariable(null),
                        new PreConditionFailure("variableName cannot be null."));
                });

                runner.test("with empty string", (Test test) ->
                {
                    final Process process = creator.run(test);
                    test.assertThrows(() -> process.getEnvironmentVariable(""),
                        new PreConditionFailure("variableName cannot be empty."));
                });

                runner.test("with non-existing variable name", (Test test) ->
                {
                    final Process process = creator.run(test);
                    test.assertThrows(() -> process.getEnvironmentVariable("Can't find me").await(),
                        new NotFoundException("No environment variable named \"Can't find me\" found."));
                });

                runner.test("with existing variable name", (Test test) ->
                {
                    final Process process = creator.run(test);
                    final String path = process.getEnvironmentVariable("path").await();
                    test.assertNotNull(path);
                    test.assertFalse(path.isEmpty());
                });
            });

            runner.test("getClock()", (Test test) ->
            {
                try (final Process process = creator.run(test))
                {
                    test.assertNotNull(process.getClock());
                }
            });

            runner.test("getDisplays()", (Test test) ->
            {
                final Process process = creator.run(test);
                final Iterable<Display> displays = process.getDisplays();
                test.assertNotNull(displays);
                test.assertGreaterThanOrEqualTo(displays.getCount(), 1);
                for (final Display display : displays)
                {
                    test.assertNotNull(display);
                    test.assertGreaterThanOrEqualTo(display.getWidthInPixels(), 1);
                    test.assertGreaterThanOrEqualTo(display.getHeightInPixels(), 1);
                    test.assertGreaterThanOrEqualTo(display.getHorizontalDpi(), 1);
                    test.assertGreaterThanOrEqualTo(display.getVerticalDpi(), 1);
                }
            });

            runner.test("getSystemProperties()", (Test test) ->
            {
                try (final Process process = creator.run(test))
                {
                    final Map<String,String> systemProperties = process.getSystemProperties();
                    test.assertNotNull(systemProperties);
                    test.assertTrue(systemProperties.containsKey("os.name"));
                    test.assertTrue(systemProperties.containsKey("sun.java.command"));
                }
            });

            runner.testGroup("getSystemProperty()", () ->
            {
                runner.test("with null systemPropertyName", (Test test) ->
                {
                    try (final Process process = creator.run(test))
                    {
                        test.assertThrows(() -> process.getSystemProperty(null),
                            new PreConditionFailure("systemPropertyName cannot be null."));
                    }
                });

                runner.test("with empty systemPropertyName", (Test test) ->
                {
                    try (final Process process = creator.run(test))
                    {
                        test.assertThrows(() -> process.getSystemProperty(""),
                            new PreConditionFailure("systemPropertyName cannot be empty."));
                    }
                });

                runner.test("with not-found systemPropertyName", (Test test) ->
                {
                    try (final Process process = creator.run(test))
                    {
                        test.assertThrows(() -> process.getSystemProperty("apples-and-bananas").await(),
                            new NotFoundException("No system property found with the name \"apples-and-bananas\"."));
                    }
                });

                runner.test("with found systemPropertyName", (Test test) ->
                {
                    try (final Process process = creator.run(test))
                    {
                        test.assertNotNullAndNotEmpty(process.getSystemProperty("os.name").await());
                    }
                });
            });
        });
    }
}
