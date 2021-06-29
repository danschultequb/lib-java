package qub;

public interface FakeChildProcessRunnerTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(FakeChildProcessRunnerTests.class, () ->
        {
            ChildProcessRunnerTests.test(runner, FakeChildProcessRunner::create);

            runner.testGroup("create(AsyncRunner,Folder)", () ->
            {
                final Action4<String,AsyncRunner,Folder,Throwable> createErrorTest = (String testName, AsyncRunner parallelAsyncRunner, Folder currentFolder, Throwable expected) ->
                {
                    runner.test(testName, (Test test) ->
                    {
                        test.assertThrows(() -> FakeChildProcessRunner.create(parallelAsyncRunner, currentFolder),
                            expected);
                    });
                };

                createErrorTest.run("with null parallelAsyncRunner",
                    null,
                    InMemoryFileSystem.create().getFolder("/").await(),
                    new PreConditionFailure("parallelAsyncRunner cannot be null."));

                createErrorTest.run("with null currentFolder",
                    ManualAsyncRunner.create(),
                    null,
                    new PreConditionFailure("currentFolder cannot be null."));
            });

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
                        .setFunction(20));
                    test.assertSame(processRunner, addResult);
                    test.assertEqual(20, processRunner.run("/executable/file").await());
                });

                runner.test("with non-null executablePath that already exists",
                    (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                    (Test test, FakeDesktopProcess process) ->
                {
                    final FakeChildProcessRunner processRunner = FakeChildProcessRunner.create(process);

                    final FakeChildProcessRunner addResult1 = processRunner.add(FakeChildProcessRun.create("/executable/file")
                        .setFunction(20));
                    test.assertSame(processRunner, addResult1);

                    final FakeChildProcessRunner addResult2 = processRunner.add(FakeChildProcessRun.create("/executable/file")
                        .setFunction(21));
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
                                .setFunction(() -> 3));
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
                            .setFunction(() -> 3))
                        .add(FakeChildProcessRun.create("/other/exe", "a", "b")
                            .setFunction(() -> 4));
                    test.assertEqual(4, processRunner.run("/other/exe", "a", "b").await());
                });

                runner.test("with non-matching working folder path matches",
                    (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                    (Test test, FakeDesktopProcess process) ->
                {
                    final FakeChildProcessRunner processRunner = FakeChildProcessRunner.create(process)
                        .add(FakeChildProcessRun.create("/other/exe", "a", "b")
                            .setFunction(() -> 3))
                        .add(FakeChildProcessRun.create("/other/exe", "a", "b")
                            .setWorkingFolder("/other/working/")
                            .setFunction(() -> 4));
                    test.assertEqual(3, processRunner.run("/other/exe", "a", "b").await());
                });

                runner.test("with function that throws",
                    (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                    (Test test, FakeDesktopProcess process) ->
                {
                    final FakeChildProcessRunner processRunner = FakeChildProcessRunner.create(process)
                        .add(FakeChildProcessRun.create("/executable/file")
                            .setFunction(() ->
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
                            .setFunction(() ->
                            {
                                value.set(20);
                                return 3;
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
                            .setFunction((ByteReadStream input, ByteWriteStream output, ByteWriteStream error) ->
                            {
                                return input.readAllBytes().await().length;
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
                            .setFunction((ByteWriteStream output) ->
                            {
                                output.write(new byte[] { 10, 9, 8, 7 }).await();
                                return 4;
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
                            .setFunction((ByteWriteStream output, ByteWriteStream error) ->
                            {
                                error.write(new byte[]{ 9, 8, 7, 6 }).await();
                                return 5;
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
