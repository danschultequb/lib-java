package qub;

public interface FakeDesktopProcessTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(FakeDesktopProcess.class, () ->
        {
            final Function0<FakeDesktopProcess> creator = () ->
            {
                final FakeDesktopProcess process = FakeDesktopProcess.create();

                process.getEnvironmentVariables()
                    .set("path", "fake-path");

                final InMemoryFileSystem fileSystem = process.getFileSystem();
                final QubFolder qubFolder = QubFolder.get(fileSystem.getFolder("/qub/").await());
                final QubProjectVersionFolder projectVersionFolder = qubFolder.getProjectVersionFolder("fake", "main-java", "13").await();
                final File compiledSourcesFile = projectVersionFolder.createFile("main-java.jar").await();

                process.getTypeLoader()
                    .addTypeContainer("fake.MainClassFullName", compiledSourcesFile.toString());

                return process;
            };

            DesktopProcessTests.test(runner, creator);

            runner.test("getOutputWriteStream()", (Test test) ->
            {
                try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                {
                    final InMemoryCharacterToByteStream output = process.getOutputWriteStream();
                    test.assertNotNull(output);
                    test.assertSame(output, process.getOutputWriteStream());
                }
            });

            runner.testGroup("setOutputWriteStream(InMemoryCharacterToByteStream)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final FakeDesktopProcess setOutputWriteStreamResult = process.setOutputWriteStream((InMemoryCharacterToByteStream)null);
                        test.assertSame(process, setOutputWriteStreamResult);
                        test.assertNull(process.getOutputWriteStream());
                    }
                });

                runner.test("with non-null", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final InMemoryCharacterToByteStream output = InMemoryCharacterToByteStream.create();
                        final FakeDesktopProcess setDefaultOutputWriteStreamResult = process.setOutputWriteStream(output);
                        test.assertSame(process, setDefaultOutputWriteStreamResult);
                        test.assertSame(output, process.getOutputWriteStream());
                        test.assertSame(output, process.getOutputWriteStream());
                    }
                });

                runner.test("after getOutputWriteStream() has been called", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final InMemoryCharacterToByteStream output = process.getOutputWriteStream();

                        final InMemoryCharacterToByteStream output2 = InMemoryCharacterToByteStream.create();
                        final FakeDesktopProcess setOutputWriteStreamResult = process.setOutputWriteStream(output2);
                        test.assertSame(process, setOutputWriteStreamResult);
                        test.assertSame(output2, process.getOutputWriteStream());
                    }
                });
            });

            runner.test("getErrorWriteStream()", (Test test) ->
            {
                try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                {
                    final InMemoryCharacterToByteStream error = process.getErrorWriteStream();
                    test.assertNotNull(error);
                    test.assertSame(error, process.getErrorWriteStream());
                }
            });

            runner.testGroup("setErrorWriteStream(InMemoryCharacterToByteStream)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final FakeDesktopProcess setErrorWriteStreamResult = process.setErrorWriteStream((InMemoryCharacterToByteStream)null);
                        test.assertSame(process, setErrorWriteStreamResult);
                        test.assertNull(process.getErrorWriteStream());
                    }
                });

                runner.test("with non-null", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final InMemoryCharacterToByteStream error = InMemoryCharacterToByteStream.create();
                        final FakeDesktopProcess setDefaultErrorWriteStreamResult = process.setErrorWriteStream(error);
                        test.assertSame(process, setDefaultErrorWriteStreamResult);
                        test.assertSame(error, process.getErrorWriteStream());
                        test.assertSame(error, process.getErrorWriteStream());
                    }
                });

                runner.test("after getErrorWriteStream() has been called", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        process.getErrorWriteStream();

                        final InMemoryCharacterToByteStream error2 = InMemoryCharacterToByteStream.create();
                        final FakeDesktopProcess setErrorWriteStream = process.setErrorWriteStream(error2);
                        test.assertSame(process, setErrorWriteStream);
                        test.assertSame(error2, process.getErrorWriteStream());
                    }
                });
            });

            runner.test("getInputReadStream()", (Test test) ->
            {
                try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                {
                    final CharacterToByteReadStream input = process.getInputReadStream();
                    test.assertNotNull(input);
                    test.assertSame(input, process.getInputReadStream());
                }
            });

            runner.testGroup("setInputReadStream(CharacterToByteReadStream)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final FakeDesktopProcess setInputReadStreamResult = process.setInputReadStream((CharacterToByteReadStream)null);
                        test.assertSame(process, setInputReadStreamResult);
                        test.assertNull(process.getInputReadStream());
                    }
                });

                runner.test("with non-null", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final InMemoryCharacterToByteStream input = InMemoryCharacterToByteStream.create();
                        final FakeDesktopProcess setDefaultInputReadStreamResult = process.setInputReadStream(input);
                        test.assertSame(process, setDefaultInputReadStreamResult);
                        test.assertSame(input, process.getInputReadStream());
                        test.assertSame(input, process.getInputReadStream());
                    }
                });

                runner.test("after getInputReadStream() has been called", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        process.getInputReadStream();

                        final InMemoryCharacterToByteStream input2 = InMemoryCharacterToByteStream.create();
                        final FakeDesktopProcess setInputReadStreamResult = process.setInputReadStream(input2);
                        test.assertSame(process, setInputReadStreamResult);
                        test.assertSame(input2, process.getInputReadStream());
                    }
                });
            });
        });
    }
}
