package qub;

public interface FakeProcessRunnerTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(FakeProcessRunner.class, () ->
        {
            ProcessRunnerTests.test(runner, (Test test) -> new FakeProcessRunner());

            runner.testGroup("add(FakeProcessRun)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final FakeProcessRunner processRunner = new FakeProcessRunner();
                    test.assertThrows(() -> processRunner.add(null),
                        new PreConditionFailure("fakeProcessRun cannot be null."));
                });
            });

            runner.testGroup("run()", () ->
            {
                runner.test("with no matching fake run", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = new InMemoryFileSystem(test.getClock());
                    fileSystem.createRoot("/").await();

                    final File executableFile = fileSystem.getFile("/working/file").await();
                    final Iterable<String> arguments = Iterable.create("a", "b", "c");
                    final Folder workingFolder = fileSystem.getFolder("/working/").await();

                    final FakeProcessRunner processRunner = new FakeProcessRunner();
                    test.assertThrows(() -> processRunner.run(executableFile, arguments, workingFolder, null, null, null).await(),
                        new NotFoundException("No fake process run found for \"/working/: /working/file a b c\"."));
                });

                runner.test("with matching fake run with no workingFolder specified", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = new InMemoryFileSystem(test.getClock());
                    fileSystem.createRoot("/").await();

                    final File executableFile = fileSystem.getFile("/working/file").await();
                    final Iterable<String> arguments = Iterable.create("a", "b", "c");
                    final Folder workingFolder = fileSystem.getFolder("/working/").await();

                    final FakeProcessRunner processRunner = new FakeProcessRunner()
                        .add(new FakeProcessRun(executableFile)
                            .addArguments("a", "b", "c"));
                    test.assertEqual(0, processRunner.run(executableFile, arguments, workingFolder, null, null, null).await());
                });

                runner.test("with matching fake run with same workingFolder specified", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = new InMemoryFileSystem(test.getClock());
                    fileSystem.createRoot("/").await();

                    final File executableFile = fileSystem.getFile("/working/file").await();
                    final Iterable<String> arguments = Iterable.create("a", "b", "c");
                    final Folder workingFolder = fileSystem.getFolder("/working/").await();

                    final FakeProcessRunner processRunner = new FakeProcessRunner()
                        .add(new FakeProcessRun(executableFile)
                            .addArguments("a", "b", "c")
                            .setWorkingFolder(workingFolder));
                    test.assertEqual(0, processRunner.run(executableFile, arguments, workingFolder, null, null, null).await());
                });

                runner.test("with matching fake run with different workingFolder specified", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = new InMemoryFileSystem(test.getClock());
                    fileSystem.createRoot("/").await();

                    final File executableFile = fileSystem.getFile("/working/file").await();
                    final Iterable<String> arguments = Iterable.create("a", "b", "c");
                    final Folder workingFolder = fileSystem.getFolder("/working/").await();

                    final FakeProcessRunner processRunner = new FakeProcessRunner()
                        .add(new FakeProcessRun(executableFile)
                            .addArguments("a", "b", "c")
                            .setWorkingFolder(fileSystem.getFolder("/other/folder/").await()));
                    test.assertThrows(() -> processRunner.run(executableFile, arguments, workingFolder, null, null, null).await(),
                        new NotFoundException("No fake process run found for \"/working/: /working/file a b c\"."));
                });
            });
        });
    }
}
