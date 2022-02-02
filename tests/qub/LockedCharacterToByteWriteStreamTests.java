package qub;

public interface LockedCharacterToByteWriteStreamTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(LockedCharacterToByteWriteStream.class, () ->
        {
            runner.test("write(byte)",
                (TestResources resources) -> Tuple.create(resources.getParallelAsyncRunner()),
                (Test test, AsyncRunner asyncRunner) ->
            {
                final InMemoryCharacterToByteStream stream = InMemoryCharacterToByteStream.create();
                final LockedCharacterToByteWriteStream lockedStream = LockedCharacterToByteWriteStream.create(stream);

                final List<Result<Integer>> results = List.create();
                for (int i = 0; i < 1000; ++i)
                {
                    final byte byteToWrite = (byte)(i % Bytes.maximum);
                    results.add(asyncRunner.schedule(() ->
                    {
                        return lockedStream.write(byteToWrite).await();
                    }));
                }

                for (final Result<Integer> result : results)
                {
                    test.assertEqual(1, result.await());
                }
                test.assertEqual(1000, stream.getBytes().length);
            });

            runner.test("write(byte[])",
                (TestResources resources) -> Tuple.create(resources.getParallelAsyncRunner()),
                (Test test, AsyncRunner asyncRunner) ->
            {
                final InMemoryCharacterToByteStream stream = InMemoryCharacterToByteStream.create();
                final LockedCharacterToByteWriteStream lockedStream = LockedCharacterToByteWriteStream.create(stream);

                final List<Result<Integer>> results = List.create();
                for (int i = 0; i < 1000; ++i)
                {
                    results.add(asyncRunner.schedule(() ->
                    {
                        return lockedStream.write(new byte[] { 0, 1, 2, 3 }).await();
                    }));
                }

                for (final Result<Integer> result : results)
                {
                    test.assertEqual(4, result.await());
                }
                test.assertEqual(4000, stream.getBytes().length);
            });

            runner.test("write(byte[],int,int)",
                (TestResources resources) -> Tuple.create(resources.getParallelAsyncRunner()),
                (Test test, AsyncRunner asyncRunner) ->
            {
                final InMemoryCharacterToByteStream stream = InMemoryCharacterToByteStream.create();
                final LockedCharacterToByteWriteStream lockedStream = LockedCharacterToByteWriteStream.create(stream);

                final List<Result<Integer>> results = List.create();
                for (int i = 0; i < 1000; ++i)
                {
                    results.add(asyncRunner.schedule(() ->
                    {
                        return lockedStream.write(new byte[] { 0, 1, 2, 3 }, 1, 3).await();
                    }));
                }

                for (final Result<Integer> result : results)
                {
                    test.assertEqual(3, result.await());
                }
                test.assertEqual(3000, stream.getBytes().length);
            });

            runner.test("writeAll(byte[])",
                (TestResources resources) -> Tuple.create(resources.getParallelAsyncRunner()),
                (Test test, AsyncRunner asyncRunner) ->
            {
                final InMemoryCharacterToByteStream stream = InMemoryCharacterToByteStream.create();
                final LockedCharacterToByteWriteStream lockedStream = LockedCharacterToByteWriteStream.create(stream);

                final List<Result<Integer>> results = List.create();
                for (int i = 0; i < 1000; ++i)
                {
                    results.add(asyncRunner.schedule(() ->
                    {
                        return lockedStream.writeAll(new byte[] { 0, 1, 2, 3 }).await();
                    }));
                }

                for (final Result<Integer> result : results)
                {
                    test.assertEqual(4, result.await());
                }
                test.assertEqual(4000, stream.getBytes().length);
            });

            runner.test("writeAll(byte[],int,int)",
                (TestResources resources) -> Tuple.create(resources.getParallelAsyncRunner()),
                (Test test, AsyncRunner asyncRunner) ->
            {
                final InMemoryCharacterToByteStream stream = InMemoryCharacterToByteStream.create();
                final LockedCharacterToByteWriteStream lockedStream = LockedCharacterToByteWriteStream.create(stream);

                final List<Result<Integer>> results = List.create();
                for (int i = 0; i < 1000; ++i)
                {
                    results.add(asyncRunner.schedule(() ->
                    {
                        return lockedStream.writeAll(new byte[] { 0, 1, 2, 3 }, 1, 3).await();
                    }));
                }

                for (final Result<Integer> result : results)
                {
                    test.assertEqual(3, result.await());
                }
                test.assertEqual(3000, stream.getBytes().length);
            });

            runner.test("writeAll(ByteReadStream)",
                (TestResources resources) -> Tuple.create(resources.getParallelAsyncRunner()),
                (Test test, AsyncRunner asyncRunner) ->
            {
                final InMemoryCharacterToByteStream stream = InMemoryCharacterToByteStream.create();
                final LockedCharacterToByteWriteStream lockedStream = LockedCharacterToByteWriteStream.create(stream);

                final List<Result<Long>> results = List.create();
                for (int i = 0; i < 1000; ++i)
                {
                    results.add(asyncRunner.schedule(() ->
                    {
                        return lockedStream.writeAll(InMemoryByteStream.create(new byte[] { 0, 1, 2, 3 }).endOfStream()).await();
                    }));
                }

                for (final Result<Long> result : results)
                {
                    test.assertEqual(4, result.await());
                }
                test.assertEqual(4000, stream.getBytes().length);
            });

            runner.test("writeAll(ByteReadStream,int)",
                (TestResources resources) -> Tuple.create(resources.getParallelAsyncRunner()),
                (Test test, AsyncRunner asyncRunner) ->
            {
                final InMemoryCharacterToByteStream stream = InMemoryCharacterToByteStream.create();
                final LockedCharacterToByteWriteStream lockedStream = LockedCharacterToByteWriteStream.create(stream);

                final List<Result<Long>> results = List.create();
                for (int i = 0; i < 1000; ++i)
                {
                    results.add(asyncRunner.schedule(() ->
                    {
                        return lockedStream.writeAll(InMemoryByteStream.create(new byte[] { 0, 1, 2, 3 }).endOfStream(), 1).await();
                    }));
                }

                for (final Result<Long> result : results)
                {
                    test.assertEqual(4, result.await());
                }
                test.assertEqual(4000, stream.getBytes().length);
            });

            runner.test("write(char)",
                (TestResources resources) -> Tuple.create(resources.getParallelAsyncRunner()),
                (Test test, AsyncRunner asyncRunner) ->
            {
                final InMemoryCharacterToByteStream stream = InMemoryCharacterToByteStream.create();
                final LockedCharacterToByteWriteStream lockedStream = LockedCharacterToByteWriteStream.create(stream);

                final List<Result<Integer>> results = List.create();
                for (int i = 0; i < 1000; ++i)
                {
                    results.add(asyncRunner.schedule(() ->
                    {
                        return lockedStream.write('a').await();
                    }));
                }

                for (final Result<Integer> result : results)
                {
                    test.assertEqual(1, result.await());
                }
                test.assertEqual(1000, stream.getBytes().length);
            });

            runner.test("write(char[])",
                (TestResources resources) -> Tuple.create(resources.getParallelAsyncRunner()),
                (Test test, AsyncRunner asyncRunner) ->
            {
                final InMemoryCharacterToByteStream stream = InMemoryCharacterToByteStream.create();
                final LockedCharacterToByteWriteStream lockedStream = LockedCharacterToByteWriteStream.create(stream);

                final List<Result<Integer>> results = List.create();
                for (int i = 0; i < 1000; ++i)
                {
                    results.add(asyncRunner.schedule(() ->
                    {
                        return lockedStream.write(new char[] { 'a', 'b', 'c' }).await();
                    }));
                }

                for (final Result<Integer> result : results)
                {
                    test.assertEqual(3, result.await());
                }
                test.assertEqual(3000, stream.getBytes().length);
            });

            runner.test("write(char[],int,int)",
                (TestResources resources) -> Tuple.create(resources.getParallelAsyncRunner()),
                (Test test, AsyncRunner asyncRunner) ->
            {
                final InMemoryCharacterToByteStream stream = InMemoryCharacterToByteStream.create();
                final LockedCharacterToByteWriteStream lockedStream = LockedCharacterToByteWriteStream.create(stream);

                final List<Result<Integer>> results = List.create();
                for (int i = 0; i < 1000; ++i)
                {
                    results.add(asyncRunner.schedule(() ->
                    {
                        return lockedStream.write(new char[] { 'a', 'b', 'c' }, 1, 2).await();
                    }));
                }

                for (final Result<Integer> result : results)
                {
                    test.assertEqual(2, result.await());
                }
                test.assertEqual(2000, stream.getBytes().length);
            });

            runner.test("write(String,Object...)",
                (TestResources resources) -> Tuple.create(resources.getParallelAsyncRunner()),
                (Test test, AsyncRunner asyncRunner) ->
            {
                final InMemoryCharacterToByteStream stream = InMemoryCharacterToByteStream.create();
                final LockedCharacterToByteWriteStream lockedStream = LockedCharacterToByteWriteStream.create(stream);

                final List<Result<Integer>> results = List.create();
                for (int i = 0; i < 1000; ++i)
                {
                    results.add(asyncRunner.schedule(() ->
                    {
                        return lockedStream.write("hello %s", "there").await();
                    }));
                }

                for (final Result<Integer> result : results)
                {
                    test.assertEqual(11, result.await());
                }
                test.assertEqual(11000, stream.getBytes().length);
            });

            runner.test("writeLine()",
                (TestResources resources) -> Tuple.create(resources.getParallelAsyncRunner()),
                (Test test, AsyncRunner asyncRunner) ->
            {
                final InMemoryCharacterToByteStream stream = InMemoryCharacterToByteStream.create();
                final LockedCharacterToByteWriteStream lockedStream = LockedCharacterToByteWriteStream.create(stream)
                    .setNewLine('\n');

                final List<Result<Integer>> results = List.create();
                for (int i = 0; i < 1000; ++i)
                {
                    results.add(asyncRunner.schedule(() ->
                    {
                        return lockedStream.writeLine().await();
                    }));
                }

                for (final Result<Integer> result : results)
                {
                    test.assertEqual(1, result.await());
                }
                test.assertEqual(1000, stream.getBytes().length);
            });
        });
    }
}
