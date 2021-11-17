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

            runner.testGroup("setDefaultOutputWriteStream(InMemoryCharacterToByteStream)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        test.assertThrows(() -> process.setDefaultOutputWriteStream(null),
                            new PreConditionFailure("output cannot be null."));

                        final InMemoryCharacterToByteStream output = process.getOutputWriteStream();
                        test.assertNotNull(output);
                        test.assertSame(output, process.getOutputWriteStream());
                    }
                });

                runner.test("with non-null", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final InMemoryCharacterToByteStream output = InMemoryCharacterToByteStream.create();
                        final FakeDesktopProcess setDefaultOutputWriteStreamResult = process.setDefaultOutputWriteStream(output);
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
                        test.assertThrows(() -> process.setDefaultOutputWriteStream(output2),
                            new PreConditionFailure("this.output.hasValue() cannot be false."));

                        test.assertSame(output, process.getOutputWriteStream());
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

            runner.testGroup("setDefaultErrorWriteStream(InMemoryCharacterToByteStream)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        test.assertThrows(() -> process.setDefaultErrorWriteStream(null),
                            new PreConditionFailure("error cannot be null."));

                        final InMemoryCharacterToByteStream error = process.getErrorWriteStream();
                        test.assertNotNull(error);
                        test.assertSame(error, process.getErrorWriteStream());
                    }
                });

                runner.test("with non-null", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final InMemoryCharacterToByteStream error = InMemoryCharacterToByteStream.create();
                        final FakeDesktopProcess setDefaultErrorWriteStreamResult = process.setDefaultErrorWriteStream(error);
                        test.assertSame(process, setDefaultErrorWriteStreamResult);
                        test.assertSame(error, process.getErrorWriteStream());
                        test.assertSame(error, process.getErrorWriteStream());
                    }
                });

                runner.test("after getErrorWriteStream() has been called", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final InMemoryCharacterToByteStream error = process.getErrorWriteStream();

                        final InMemoryCharacterToByteStream error2 = InMemoryCharacterToByteStream.create();
                        test.assertThrows(() -> process.setDefaultErrorWriteStream(error2),
                            new PreConditionFailure("this.error.hasValue() cannot be false."));

                        test.assertSame(error, process.getErrorWriteStream());
                    }
                });
            });

            runner.test("getInputReadStream()", (Test test) ->
            {
                try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                {
                    final InMemoryCharacterToByteStream input = process.getInputReadStream();
                    test.assertNotNull(input);
                    test.assertSame(input, process.getInputReadStream());
                }
            });

            runner.testGroup("setDefaultInputWriteStream(InMemoryCharacterToByteStream)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        test.assertThrows(() -> process.setDefaultInputReadStream(null),
                            new PreConditionFailure("input cannot be null."));

                        final InMemoryCharacterToByteStream input = process.getInputReadStream();
                        test.assertNotNull(input);
                        test.assertSame(input, process.getInputReadStream());
                    }
                });

                runner.test("with non-null", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final InMemoryCharacterToByteStream input = InMemoryCharacterToByteStream.create();
                        final FakeDesktopProcess setDefaultInputReadStreamResult = process.setDefaultInputReadStream(input);
                        test.assertSame(process, setDefaultInputReadStreamResult);
                        test.assertSame(input, process.getInputReadStream());
                        test.assertSame(input, process.getInputReadStream());
                    }
                });

                runner.test("after getInputReadStream() has been called", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final InMemoryCharacterToByteStream input = process.getInputReadStream();

                        final InMemoryCharacterToByteStream input2 = InMemoryCharacterToByteStream.create();
                        test.assertThrows(() -> process.setDefaultInputReadStream(input2),
                            new PreConditionFailure("this.input.hasValue() cannot be false."));

                        test.assertSame(input, process.getInputReadStream());
                    }
                });
            });
        });
    }
}
