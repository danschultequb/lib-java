package qub;

public interface FakeChildProcessRunnerTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(FakeChildProcessRunnerTests.class, () ->
        {
            ChildProcessRunnerTests.test(runner, FakeChildProcessRunner::create);

            runner.testGroup("create(DesktopProcess)", () ->
            {
                runner.test("with null process", (Test test) ->
                {
                    test.assertThrows(() -> FakeChildProcessRunner.create((DesktopProcess)null),
                        new PreConditionFailure("process cannot be null."));
                });

                runner.test("with non-null process",
                    (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                    (Test test, FakeDesktopProcess process) ->
                {
                    final FakeChildProcessRunner processRunner = FakeChildProcessRunner.create(process);
                    test.assertNotNull(processRunner);
                });
            });

            runner.testGroup("add(FakeProcessRun)", () ->
            {
                runner.test("with null",
                    (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                    (Test test, FakeDesktopProcess process) ->
                {
                    final FakeChildProcessRunner processRunner = FakeChildProcessRunner.create(process);
                    test.assertThrows(() -> processRunner.add(null),
                        new PreConditionFailure("fakeProcessRun cannot be null."));
                });

                runner.test("with non-null",
                    (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                    (Test test, FakeDesktopProcess process) ->
                {
                    final FakeChildProcessRunner processRunner = FakeChildProcessRunner.create(process);
                    final FakeChildProcessRunner addResult = processRunner.add(FakeChildProcessRun.create("/executable/file")
                        .setAction((FakeDesktopProcess childProcess) -> childProcess.setExitCode(20)));
                    test.assertSame(processRunner, addResult);
                    test.assertEqual(20, processRunner.run("/executable/file").await());
                });

                runner.test("with non-null executablePath that already exists",
                    (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                    (Test test, FakeDesktopProcess process) ->
                {
                    final FakeChildProcessRunner processRunner = FakeChildProcessRunner.create(process);

                    final FakeChildProcessRunner addResult1 = processRunner.add(FakeChildProcessRun.create("/executable/file")
                        .setAction((FakeDesktopProcess childProcess) -> childProcess.setExitCode(20)));
                    test.assertSame(processRunner, addResult1);

                    final FakeChildProcessRunner addResult2 = processRunner.add(FakeChildProcessRun.create("/executable/file")
                        .setAction((FakeDesktopProcess childProcess) -> childProcess.setExitCode(21)));
                    test.assertSame(processRunner, addResult2);

                    test.assertEqual(21, processRunner.run("/executable/file").await());
                });
            });

            runner.testGroup("run()", () ->
            {
                runner.test("with no fakeProcessRuns",
                    (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                    (Test test, FakeDesktopProcess process) ->
                {
                    final Folder workingFolder = process.getCurrentFolder();
                    final FakeChildProcessRunner processRunner = FakeChildProcessRunner.create(process);
                    test.assertThrows(() -> processRunner.run("/executable/file").await(),
                        new NotFoundException("No fake process run found for \"" + workingFolder + ": /executable/file\"."));
                });

                runner.test("with no matching fakeProcessRuns",
                    (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                    (Test test, FakeDesktopProcess process) ->
                {
                    final Folder workingFolder = process.getCurrentFolder();
                    final FakeChildProcessRunner processRunner = FakeChildProcessRunner.create(process)
                        .add(FakeChildProcessRun.create("/other/exe"));
                    test.assertThrows(() -> processRunner.run("/executable/file").await(),
                        new NotFoundException("No fake process run found for \"" + workingFolder + ": /executable/file\"."));
                });

                runner.test("with not all matching arguments",
                    (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                    (Test test, FakeDesktopProcess process) ->
                {
                    final Folder workingFolder = process.getCurrentFolder();
                    final FakeChildProcessRunner processRunner = FakeChildProcessRunner.create(process)
                        .add(FakeChildProcessRun.create("/executable/file", "a", "b"));
                    test.assertThrows(() -> processRunner.run("/executable/file", "a", "b", "c").await(),
                        new NotFoundException("No fake process run found for \"" + workingFolder + ": /executable/file a b c\"."));
                });

                runner.test("with too many arguments",
                    (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                    (Test test, FakeDesktopProcess process) ->
                {
                    final Folder workingFolder = process.getCurrentFolder();
                    final FakeChildProcessRunner processRunner = FakeChildProcessRunner.create(process)
                        .add(FakeChildProcessRun.create("/executable/file", "a", "b"));
                    test.assertThrows(() -> processRunner.run("/executable/file", "a").await(),
                        new NotFoundException("No fake process run found for \"" + workingFolder + ": /executable/file a\"."));
                });

                runner.test("with fully matching fakeProcessRun with no function set",
                    (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                    (Test test, FakeDesktopProcess process) ->
                {
                    final Folder workingFolder = process.getCurrentFolder();
                    final FakeChildProcessRunner processRunner = FakeChildProcessRunner.create(process)
                        .add(FakeChildProcessRun.create("/other/exe", "apples", "ban anas")
                            .setWorkingFolder(workingFolder));
                    final ChildProcessParameters parameters = ChildProcessParameters.create("/other/exe", "apples", "ban anas")
                        .setWorkingFolder(workingFolder);
                    test.assertEqual(0, processRunner.run(parameters).await());
                });

                runner.test("with fully matching fakeProcessRun", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final Folder workingFolder = process.getCurrentFolder();
                        final FakeChildProcessRunner processRunner = FakeChildProcessRunner.create(process)
                            .add(FakeChildProcessRun.create("/other/exe", "apples", "ban anas")
                                .setWorkingFolder(workingFolder)
                                .setAction((FakeDesktopProcess childProcess) -> childProcess.setExitCode(3)));
                        final ChildProcessParameters parameters = ChildProcessParameters.create("/other/exe", "apples", "ban anas")
                            .setWorkingFolder(workingFolder);
                        test.assertEqual(3, processRunner.run(parameters).await());
                    }
                });

                runner.test("with multiple partial matches",
                    (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                    (Test test, FakeDesktopProcess process) ->
                {
                    final FakeChildProcessRunner processRunner = FakeChildProcessRunner.create(process)
                        .add(FakeChildProcessRun.create("/other/exe", "a", "b")
                            .setAction((FakeDesktopProcess childProcess) -> childProcess.setExitCode(3)))
                        .add(FakeChildProcessRun.create("/other/exe", "a", "b")
                            .setAction((FakeDesktopProcess childProcess) -> childProcess.setExitCode(4)));
                    test.assertEqual(4, processRunner.run("/other/exe", "a", "b").await());
                });

                runner.test("with non-matching working folder path matches",
                    (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                    (Test test, FakeDesktopProcess process) ->
                {
                    final FakeChildProcessRunner processRunner = FakeChildProcessRunner.create(process)
                        .add(FakeChildProcessRun.create("/other/exe", "a", "b")
                            .setAction((FakeDesktopProcess childProcess) -> childProcess.setExitCode(3)))
                        .add(FakeChildProcessRun.create("/other/exe", "a", "b")
                            .setWorkingFolder("/other/working/")
                            .setAction((FakeDesktopProcess childProcess) -> childProcess.setExitCode(4)));
                    test.assertEqual(3, processRunner.run("/other/exe", "a", "b").await());
                });

                runner.test("with function that throws",
                    (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                    (Test test, FakeDesktopProcess process) ->
                {
                    final FakeChildProcessRunner processRunner = FakeChildProcessRunner.create(process)
                        .add(FakeChildProcessRun.create("/executable/file")
                            .setAction(() ->
                            {
                                throw new ParseException("blah");
                            }));
                    test.assertThrows(() -> processRunner.run("/executable/file").await(),
                        new ParseException("blah"));
                });

                runner.test("with function that doesn't throw",
                    (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                    (Test test, FakeDesktopProcess process) ->
                {
                    final IntegerValue value = new IntegerValue(10);
                    final FakeChildProcessRunner processRunner = FakeChildProcessRunner.create(process)
                        .add(FakeChildProcessRun.create("/executable/file")
                            .setAction((FakeDesktopProcess childProcess) ->
                            {
                                value.set(20);
                                childProcess.setExitCode(3);
                            }));
                    test.assertEqual(3, processRunner.run("/executable/file").await());
                    test.assertEqual(20, value.get());
                });

                runner.test("with function that reads from the fake process's standard input",
                    (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                    (Test test, FakeDesktopProcess process) ->
                {
                    final FakeChildProcessRunner processRunner = FakeChildProcessRunner.create(process)
                        .add(FakeChildProcessRun.create("/executable/file")
                            .setAction((FakeDesktopProcess childProcess) ->
                            {
                                final ByteReadStream input = childProcess.getInputReadStream();
                                final byte[] bytes = input.readAllBytes().await();
                                final int bytesLength = bytes.length;
                                childProcess.setExitCode(bytesLength);
                            }));
                    final ChildProcessParameters parameters = ChildProcessParameters.create("/executable/file")
                        .setInputStream(InMemoryByteStream.create(new byte[] { 9, 8, 7, 6, 5, 4 }).endOfStream());
                    test.assertEqual(6, processRunner.run(parameters).await());
                });

                runner.test("with function that writes to the fake process's standard output",
                    (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                    (Test test, FakeDesktopProcess process) ->
                {
                    final FakeChildProcessRunner processRunner = FakeChildProcessRunner.create(process)
                        .add(FakeChildProcessRun.create("/executable/file")
                            .setAction((FakeDesktopProcess childProcess) ->
                            {
                                childProcess.getOutputWriteStream().write(new byte[] { 10, 9, 8, 7 }).await();
                                childProcess.setExitCode(4);
                            }));
                    final InMemoryByteStream output = InMemoryByteStream.create();
                    final InMemoryByteStream error = InMemoryByteStream.create();
                    final ChildProcessParameters parameters = ChildProcessParameters.create("/executable/file")
                        .redirectOutputTo(output)
                        .redirectErrorTo(error);
                    test.assertEqual(4, processRunner.run(parameters).await());
                    test.assertEqual(new byte[]{ 10, 9, 8, 7 }, output.getBytes());
                    test.assertEqual(new byte[0], error.getBytes());
                });

                runner.test("with function that writes to the fake process's standard error",
                    (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                    (Test test, FakeDesktopProcess process) ->
                {
                    final FakeChildProcessRunner processRunner = FakeChildProcessRunner.create(process)
                        .add(FakeChildProcessRun.create("/executable/file")
                            .setAction((FakeDesktopProcess childProcess) ->
                            {
                                childProcess.getErrorWriteStream().write(new byte[]{ 9, 8, 7, 6 }).await();
                                childProcess.setExitCode(5);
                            }));
                    final InMemoryByteStream output = InMemoryByteStream.create();
                    final InMemoryByteStream error = InMemoryByteStream.create();
                    final ChildProcessParameters parameters = ChildProcessParameters.create("/executable/file")
                        .redirectOutputTo(output)
                        .redirectErrorTo(error);
                    test.assertEqual(5, processRunner.run(parameters).await());
                    test.assertEqual(new byte[0], output.getBytes());
                    test.assertEqual(new byte[]{ 9, 8, 7, 6 }, error.getBytes());
                });
            });
        });
    }
}
