package qub;

public interface FakeProcessFactoryTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(FakeProcessFactoryTests.class, () ->
        {
            ProcessFactoryTests.test(runner, (Test test) -> new FakeProcessFactory(Path.parse("/working/")));

            runner.testGroup("constructor(String)", () ->
            {
                runner.test("with null working folder path", (Test test) ->
                {
                    test.assertThrows(() -> new FakeProcessFactory((String)null),
                        new PreConditionFailure("pathString cannot be null."));
                });

                runner.test("with empty working folder path", (Test test) ->
                {
                    test.assertThrows(() -> new FakeProcessFactory(""),
                        new PreConditionFailure("pathString cannot be empty."));
                });

                runner.test("with relative working folder path", (Test test) ->
                {
                    test.assertThrows(() -> new FakeProcessFactory("working"),
                        new PreConditionFailure("workingFolderPath.isRooted() cannot be false."));
                });

                runner.test("with rooted working folder path", (Test test) ->
                {
                    final FakeProcessFactory factory = new FakeProcessFactory("/working/folder/");
                    test.assertEqual(Path.parse("/working/folder/"), factory.getWorkingFolderPath());
                });
            });

            runner.testGroup("constructor(Path)", () ->
            {
                runner.test("with null working folder path", (Test test) ->
                {
                    test.assertThrows(() -> new FakeProcessFactory((Path)null),
                        new PreConditionFailure("workingFolderPath cannot be null."));
                });

                runner.test("with relative working folder path", (Test test) ->
                {
                    test.assertThrows(() -> new FakeProcessFactory(Path.parse("working")),
                        new PreConditionFailure("workingFolderPath.isRooted() cannot be false."));
                });

                runner.test("with rooted working folder path", (Test test) ->
                {
                    final FakeProcessFactory factory = new FakeProcessFactory(Path.parse("/working/folder/"));
                    test.assertEqual(Path.parse("/working/folder/"), factory.getWorkingFolderPath());
                });
            });

            runner.testGroup("constructor(Folder)", () ->
            {
                runner.test("with null working folder", (Test test) ->
                {
                    test.assertThrows(() -> new FakeProcessFactory((Folder)null),
                        new NullPointerException());
                });

                runner.test("with non-null working folder", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = new InMemoryFileSystem(test.getClock());
                    final Folder workingFolder = fileSystem.getFolder("/working/folder/here/").await();
                    final FakeProcessFactory factory = new FakeProcessFactory(workingFolder);
                    test.assertEqual(Path.parse("/working/folder/here/"), factory.getWorkingFolderPath());
                });
            });

            runner.testGroup("add(FakeProcessRun)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final FakeProcessFactory factory = new FakeProcessFactory("/working/");
                    test.assertThrows(() -> factory.add(null),
                        new PreConditionFailure("fakeProcessRun cannot be null."));
                });

                runner.test("with non-null", (Test test) ->
                {
                    final FakeProcessFactory factory = new FakeProcessFactory("/working/");
                    test.assertSame(factory, factory.add(new FakeProcessRun("/executable/file").setExitCode(20)));
                    test.assertEqual(20, factory.getProcessBuilder("/executable/file").await().run().await());
                });

                runner.test("with non-null executablePath that already exists", (Test test) ->
                {
                    final FakeProcessFactory factory = new FakeProcessFactory("/working/");
                    test.assertSame(factory, factory.add(new FakeProcessRun("/executable/file").setExitCode(20)));
                    test.assertSame(factory, factory.add(new FakeProcessRun("/executable/file").setExitCode(21)));
                    test.assertEqual(21, factory.getProcessBuilder("/executable/file").await().run().await());
                });
            });

            runner.testGroup("getProcessBuilder(File)", () ->
            {
                runner.test("with non-matching file", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = new InMemoryFileSystem(test.getClock());
                    final File executableFile = fileSystem.getFile("/executable").await();

                    final FakeProcessFactory factory = new FakeProcessFactory(fileSystem.getFolder("/working/").await());
                    final ProcessBuilder builder = factory.getProcessBuilder(executableFile).await();
                    test.assertThrows(() -> builder.run().await(),
                        new NotFoundException("No fake process run found for \"/working/: /executable\"."));
                });
            });

            runner.testGroup("run()", () ->
            {
                runner.test("with no fakeProcessRuns", (Test test) ->
                {
                    final FakeProcessFactory factory = new FakeProcessFactory("/working/");
                    final ProcessBuilder builder = factory.getProcessBuilder("/executable/file").await();
                    test.assertThrows(() -> builder.run().await(),
                        new NotFoundException("No fake process run found for \"/working/: /executable/file\"."));
                });

                runner.test("with no matching fakeProcessRuns", (Test test) ->
                {
                    final FakeProcessFactory factory = new FakeProcessFactory("/working/")
                        .add(new FakeProcessRun("/other/exe"));
                    final ProcessBuilder builder = factory.getProcessBuilder("/executable/file").await();
                    test.assertThrows(() -> builder.run().await(),
                        new NotFoundException("No fake process run found for \"/working/: /executable/file\"."));
                });

                runner.test("with not all matching arguments", (Test test) ->
                {
                    final FakeProcessFactory factory = new FakeProcessFactory("/working/")
                        .add(new FakeProcessRun("/executable/file")
                            .addArguments("a", "b"));
                    final ProcessBuilder builder = factory.getProcessBuilder("/executable/file").await()
                        .addArguments("a", "b", "c");
                    test.assertThrows(() -> builder.run().await(),
                        new NotFoundException("No fake process run found for \"/working/: /executable/file a b c\"."));
                });

                runner.test("with too many arguments", (Test test) ->
                {
                    final FakeProcessFactory factory = new FakeProcessFactory("/working/")
                        .add(new FakeProcessRun("/executable/file")
                            .addArguments("a", "b"));
                    final ProcessBuilder builder = factory.getProcessBuilder("/executable/file").await()
                        .addArguments("a");
                    test.assertThrows(() -> builder.run().await(),
                        new NotFoundException("No fake process run found for \"/working/: /executable/file a\"."));
                });

                runner.test("with fully matching fakeProcessRun", (Test test) ->
                {
                    final FakeProcessFactory factory = new FakeProcessFactory("/working/")
                        .add(new FakeProcessRun("/other/exe")
                            .setWorkingFolder("/working/")
                            .addArguments("apples", "ban anas")
                            .setExitCode(3));
                    final ProcessBuilder builder = factory.getProcessBuilder("/other/exe").await()
                        .addArguments("apples", "ban anas");
                    test.assertEqual(3, builder.run().await());
                });

                runner.test("with multiple partial matches", (Test test) ->
                {
                    final FakeProcessFactory factory = new FakeProcessFactory("/working/")
                        .add(new FakeProcessRun("/other/exe")
                            .addArguments("a", "b")
                            .setExitCode(3))
                        .add(new FakeProcessRun("/other/exe")
                            .addArguments("a", "b")
                            .setExitCode(4));
                    final ProcessBuilder builder = factory.getProcessBuilder("/other/exe").await()
                        .addArguments("a", "b");
                    test.assertEqual(4, builder.run().await());
                });

                runner.test("with non-matching working folder path matches", (Test test) ->
                {
                    final FakeProcessFactory factory = new FakeProcessFactory("/working/")
                        .add(new FakeProcessRun("/other/exe")
                            .addArguments("a", "b")
                            .setExitCode(3))
                        .add(new FakeProcessRun("/other/exe")
                            .setWorkingFolder("/other/working/")
                            .addArguments("a", "b")
                            .setExitCode(4));
                    final ProcessBuilder builder = factory.getProcessBuilder("/other/exe").await()
                        .addArguments("a", "b");
                    test.assertEqual(3, builder.run().await());
                });
            });
        });
    }
}
