package qub;

public interface FakeProcessFactoryTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(FakeProcessFactoryTests.class, () ->
        {
            ProcessFactoryTests.test(runner, (Test test) ->
            {
                final AsyncRunner asyncRunner = test.getParallelAsyncRunner();
                final Clock clock = test.getClock();
                return new FakeProcessFactory(asyncRunner, Path.parse("/working/"))
                    .add(FakeProcessRun.get("javac")
                        .setFunction(() ->
                        {
                            clock.delay(Duration.seconds(0.1)).await();
                            return 2;
                        }));
            });

            runner.testGroup("constructor(String)", () ->
            {
                runner.test("with null working folder path", (Test test) ->
                {
                    test.assertThrows(() -> new FakeProcessFactory(test.getParallelAsyncRunner(), (String)null),
                        new PreConditionFailure("pathString cannot be null."));
                });

                runner.test("with empty working folder path", (Test test) ->
                {
                    test.assertThrows(() -> new FakeProcessFactory(test.getParallelAsyncRunner(), ""),
                        new PreConditionFailure("pathString cannot be empty."));
                });

                runner.test("with relative working folder path", (Test test) ->
                {
                    test.assertThrows(() -> new FakeProcessFactory(test.getParallelAsyncRunner(), "working"),
                        new PreConditionFailure("workingFolderPath.isRooted() cannot be false."));
                });

                runner.test("with rooted working folder path", (Test test) ->
                {
                    final FakeProcessFactory factory = new FakeProcessFactory(test.getParallelAsyncRunner(), "/working/folder/");
                    test.assertEqual(Path.parse("/working/folder/"), factory.getWorkingFolderPath());
                });
            });

            runner.testGroup("constructor(Path)", () ->
            {
                runner.test("with null working folder path", (Test test) ->
                {
                    test.assertThrows(() -> new FakeProcessFactory(test.getParallelAsyncRunner(), (Path)null),
                        new PreConditionFailure("workingFolderPath cannot be null."));
                });

                runner.test("with relative working folder path", (Test test) ->
                {
                    test.assertThrows(() -> new FakeProcessFactory(test.getParallelAsyncRunner(), Path.parse("working")),
                        new PreConditionFailure("workingFolderPath.isRooted() cannot be false."));
                });

                runner.test("with rooted working folder path", (Test test) ->
                {
                    final FakeProcessFactory factory = new FakeProcessFactory(test.getParallelAsyncRunner(), Path.parse("/working/folder/"));
                    test.assertEqual(Path.parse("/working/folder/"), factory.getWorkingFolderPath());
                });
            });

            runner.testGroup("constructor(Folder)", () ->
            {
                runner.test("with null working folder", (Test test) ->
                {
                    test.assertThrows(() -> new FakeProcessFactory(test.getParallelAsyncRunner(), (Folder)null),
                        new NullPointerException());
                });

                runner.test("with non-null working folder", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = new InMemoryFileSystem(test.getClock());
                    final Folder workingFolder = fileSystem.getFolder("/working/folder/here/").await();
                    final FakeProcessFactory factory = new FakeProcessFactory(test.getParallelAsyncRunner(), workingFolder);
                    test.assertEqual(Path.parse("/working/folder/here/"), factory.getWorkingFolderPath());
                });
            });

            runner.testGroup("add(FakeProcessRun)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final FakeProcessFactory factory = new FakeProcessFactory(test.getParallelAsyncRunner(), "/working/");
                    test.assertThrows(() -> factory.add(null),
                        new PreConditionFailure("fakeProcessRun cannot be null."));
                });

                runner.test("with non-null", (Test test) ->
                {
                    final FakeProcessFactory factory = new FakeProcessFactory(test.getParallelAsyncRunner(), "/working/");
                    test.assertSame(factory, factory.add(FakeProcessRun.get("/executable/file").setFunction(20)));
                    test.assertEqual(20, factory.getProcessBuilder("/executable/file").await().run().await());
                });

                runner.test("with non-null executablePath that already exists", (Test test) ->
                {
                    final FakeProcessFactory factory = new FakeProcessFactory(test.getParallelAsyncRunner(), "/working/");
                    test.assertSame(factory, factory.add(FakeProcessRun.get("/executable/file").setFunction(20)));
                    test.assertSame(factory, factory.add(FakeProcessRun.get("/executable/file").setFunction(21)));
                    test.assertEqual(21, factory.getProcessBuilder("/executable/file").await().run().await());
                });
            });

            runner.testGroup("run()", () ->
            {
                runner.test("with no fakeProcessRuns", (Test test) ->
                {
                    final FakeProcessFactory factory = new FakeProcessFactory(test.getParallelAsyncRunner(), "/working/");
                    final ProcessBuilder builder = factory.getProcessBuilder("/executable/file").await();
                    test.assertThrows(() -> builder.run().await(),
                        new NotFoundException("No fake process run found for \"/working/: /executable/file\"."));
                });

                runner.test("with no matching fakeProcessRuns", (Test test) ->
                {
                    final FakeProcessFactory factory = new FakeProcessFactory(test.getParallelAsyncRunner(), "/working/")
                        .add(FakeProcessRun.get("/other/exe"));
                    final ProcessBuilder builder = factory.getProcessBuilder("/executable/file").await();
                    test.assertThrows(() -> builder.run().await(),
                        new NotFoundException("No fake process run found for \"/working/: /executable/file\"."));
                });

                runner.test("with not all matching arguments", (Test test) ->
                {
                    final FakeProcessFactory factory = new FakeProcessFactory(test.getParallelAsyncRunner(), "/working/")
                        .add(FakeProcessRun.get("/executable/file")
                            .addArguments("a", "b"));
                    final ProcessBuilder builder = factory.getProcessBuilder("/executable/file").await()
                        .addArguments("a", "b", "c");
                    test.assertThrows(() -> builder.run().await(),
                        new NotFoundException("No fake process run found for \"/working/: /executable/file a b c\"."));
                });

                runner.test("with too many arguments", (Test test) ->
                {
                    final FakeProcessFactory factory = new FakeProcessFactory(test.getParallelAsyncRunner(), "/working/")
                        .add(FakeProcessRun.get("/executable/file")
                            .addArguments("a", "b"));
                    final ProcessBuilder builder = factory.getProcessBuilder("/executable/file").await()
                        .addArguments("a");
                    test.assertThrows(() -> builder.run().await(),
                        new NotFoundException("No fake process run found for \"/working/: /executable/file a\"."));
                });

                runner.test("with fully matching fakeProcessRun with no function set", (Test test) ->
                {
                    final FakeProcessFactory factory = new FakeProcessFactory(test.getParallelAsyncRunner(), "/working/")
                        .add(FakeProcessRun.get("/other/exe")
                            .setWorkingFolder("/working/")
                            .addArguments("apples", "ban anas"));
                    final ProcessBuilder builder = factory.getProcessBuilder("/other/exe").await()
                        .addArguments("apples", "ban anas");
                    test.assertEqual(0, builder.run().await());
                });

                runner.test("with fully matching fakeProcessRun", (Test test) ->
                {
                    final FakeProcessFactory factory = new FakeProcessFactory(test.getParallelAsyncRunner(), "/working/")
                        .add(FakeProcessRun.get("/other/exe")
                            .setWorkingFolder("/working/")
                            .addArguments("apples", "ban anas")
                            .setFunction(() -> 3));
                    final ProcessBuilder builder = factory.getProcessBuilder("/other/exe").await()
                        .addArguments("apples", "ban anas");
                    test.assertEqual(3, builder.run().await());
                });

                runner.test("with multiple partial matches", (Test test) ->
                {
                    final FakeProcessFactory factory = new FakeProcessFactory(test.getParallelAsyncRunner(), "/working/")
                        .add(FakeProcessRun.get("/other/exe")
                            .addArguments("a", "b")
                            .setFunction(() -> 3))
                        .add(FakeProcessRun.get("/other/exe")
                            .addArguments("a", "b")
                            .setFunction(() -> 4));
                    final ProcessBuilder builder = factory.getProcessBuilder("/other/exe").await()
                        .addArguments("a", "b");
                    test.assertEqual(4, builder.run().await());
                });

                runner.test("with non-matching working folder path matches", (Test test) ->
                {
                    final FakeProcessFactory factory = new FakeProcessFactory(test.getParallelAsyncRunner(), "/working/")
                        .add(FakeProcessRun.get("/other/exe")
                            .addArguments("a", "b")
                            .setFunction(() -> 3))
                        .add(FakeProcessRun.get("/other/exe")
                            .setWorkingFolder("/other/working/")
                            .addArguments("a", "b")
                            .setFunction(() -> 4));
                    final ProcessBuilder builder = factory.getProcessBuilder("/other/exe").await()
                        .addArguments("a", "b");
                    test.assertEqual(3, builder.run().await());
                });

                runner.test("with function that throws", (Test test) ->
                {
                    final FakeProcessFactory factory = new FakeProcessFactory(test.getParallelAsyncRunner(), "/working/")
                        .add(FakeProcessRun.get("/executable/file")
                            .setFunction(() ->
                            {
                                throw new ParseException("blah");
                            }));
                    final ProcessBuilder builder = factory.getProcessBuilder("/executable/file").await();
                    test.assertThrows(() -> builder.run().await(),
                        new ParseException("blah"));
                });

                runner.test("with function that doesn't throw", (Test test) ->
                {
                    final IntegerValue value = new IntegerValue(10);
                    final FakeProcessFactory factory = new FakeProcessFactory(test.getParallelAsyncRunner(), "/working/")
                        .add(FakeProcessRun.get("/executable/file")
                            .setFunction(() ->
                            {
                                value.set(20);
                                return 3;
                            }));
                    final ProcessBuilder builder = factory.getProcessBuilder("/executable/file").await();
                    test.assertEqual(3, builder.run().await());
                    test.assertEqual(20, value.get());
                });

                runner.test("with function that reads from the fake process's standard input", (Test test) ->
                {
                    final FakeProcessFactory factory = new FakeProcessFactory(test.getParallelAsyncRunner(), "/working/")
                        .add(FakeProcessRun.get("/executable/file")
                            .setFunction((ByteReadStream input, ByteWriteStream output, ByteWriteStream error) ->
                            {
                                return input.readAllBytes().await().length;
                            }));
                    final ProcessBuilder builder = factory.getProcessBuilder("/executable/file").await()
                        .redirectInput(InMemoryByteStream.create(new byte[] { 9, 8, 7, 6, 5, 4 }).endOfStream());
                    test.assertEqual(6, builder.run().await());
                });

                runner.test("with function that writes to the fake process's standard output", (Test test) ->
                {
                    final FakeProcessFactory factory = new FakeProcessFactory(test.getParallelAsyncRunner(), "/working/")
                        .add(FakeProcessRun.get("/executable/file")
                            .setFunction((ByteWriteStream output) ->
                            {
                                output.write(new byte[] { 10, 9, 8, 7 }).await();
                                return 4;
                            }));
                    final InMemoryByteStream output = InMemoryByteStream.create();
                    final InMemoryByteStream error = InMemoryByteStream.create();
                    final ProcessBuilder builder = factory.getProcessBuilder("/executable/file").await()
                        .redirectOutput(output)
                        .redirectError(error);
                    test.assertEqual(4, builder.run().await());
                    test.assertEqual(new byte[] { 10, 9, 8, 7 }, output.getBytes());
                    test.assertEqual(new byte[0], error.getBytes());
                });

                runner.test("with function that writes to the fake process's standard error", (Test test) ->
                {
                    final FakeProcessFactory factory = new FakeProcessFactory(test.getParallelAsyncRunner(), "/working/")
                        .add(FakeProcessRun.get("/executable/file")
                            .setFunction((ByteWriteStream output, ByteWriteStream error) ->
                            {
                                error.write(new byte[] { 9, 8, 7, 6 }).await();
                                return 5;
                            }));
                    final InMemoryByteStream output = InMemoryByteStream.create();
                    final InMemoryByteStream error = InMemoryByteStream.create();
                    final ProcessBuilder builder = factory.getProcessBuilder("/executable/file").await()
                        .redirectOutput(output)
                        .redirectError(error);
                    test.assertEqual(5, builder.run().await());
                    test.assertEqual(new byte[0], output.getBytes());
                    test.assertEqual(new byte[] { 9, 8, 7, 6 }, error.getBytes());
                });
            });
        });
    }
}
