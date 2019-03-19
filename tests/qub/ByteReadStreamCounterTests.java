package qub;

public class ByteReadStreamCounterTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(ByteReadStreamCounter.class, () ->
        {
            AsyncDisposableTests.test(runner, (Test test) -> new ByteReadStreamCounter(new InMemoryByteStream(test.getMainAsyncRunner())));

            runner.testGroup("constructor(ByteReadStream)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> new ByteReadStreamCounter(null),
                        new PreConditionFailure("innerStream cannot be null."));
                });

                runner.test("with disposed", (Test test) ->
                {
                    final InMemoryByteStream byteStream = new InMemoryByteStream();
                    byteStream.dispose().await();

                    final ByteReadStreamCounter counter = new ByteReadStreamCounter(byteStream);
                    test.assertTrue(counter.isDisposed());
                    test.assertFalse(counter.hasStarted());
                    test.assertEqual(0, counter.getBytesRead());
                    test.assertEqual(0, counter.getBitsRead());
                    test.assertSame(byteStream.getAsyncRunner(), counter.getAsyncRunner());
                });

                runner.test("with not disposed", (Test test) ->
                {
                    final InMemoryByteStream byteStream = new InMemoryByteStream();
                    final ByteReadStreamCounter counter = new ByteReadStreamCounter(byteStream);
                    test.assertFalse(counter.isDisposed());
                    test.assertFalse(counter.hasStarted());
                    test.assertEqual(0, counter.getBytesRead());
                    test.assertEqual(0, counter.getBitsRead());
                    test.assertSame(byteStream.getAsyncRunner(), counter.getAsyncRunner());
                });
            });

            runner.testGroup("readByte()", () ->
            {
                runner.test("when disposed", (Test test) ->
                {
                    final InMemoryByteStream innerStream = new InMemoryByteStream(new byte[] { 0, 1, 2, 3 });
                    innerStream.dispose().await();
                    final ByteReadStreamCounter counter = new ByteReadStreamCounter(innerStream);
                    test.assertThrows(() -> counter.readByte().awaitError(),
                        new PreConditionFailure("isDisposed() cannot be true."));
                    test.assertFalse(counter.hasStarted());
                    test.assertEqual(0, counter.getBytesRead());
                    test.assertEqual(0, counter.getBitsRead());
                    test.assertThrows(counter::getCurrent,
                        new PreConditionFailure("hasCurrent() cannot be false."));
                });

                runner.test("with no bytes to read", (Test test) ->
                {
                    final InMemoryByteStream innerStream = new InMemoryByteStream(new byte[0]).endOfStream();
                    final ByteReadStreamCounter counter = new ByteReadStreamCounter(innerStream);
                    test.assertThrows(() -> counter.readByte().awaitError(),
                        new EndOfStreamException());
                    test.assertTrue(counter.hasStarted());
                    test.assertEqual(0, counter.getBytesRead());
                    test.assertEqual(0, counter.getBitsRead());
                    test.assertThrows(counter::getCurrent,
                        new PreConditionFailure("hasCurrent() cannot be false."));
                });

                runner.test("with bytes to read", (Test test) ->
                {
                    final InMemoryByteStream innerStream = new InMemoryByteStream(new byte[] { 0, 1, 2, 3 }).endOfStream();
                    final ByteReadStreamCounter counter = new ByteReadStreamCounter(innerStream);
                    test.assertEqual(0, counter.readByte().await());
                    test.assertTrue(counter.hasStarted());
                    test.assertEqual(1, counter.getBytesRead());
                    test.assertEqual(8, counter.getBitsRead());
                    test.assertTrue(counter.hasCurrent());
                    test.assertEqual(0, counter.getCurrent());
                });
            });

            runner.testGroup("readBytes(int)", () ->
            {
                runner.test("when disposed", (Test test) ->
                {
                    final InMemoryByteStream innerStream = new InMemoryByteStream(new byte[] { 0, 1, 2, 3 });
                    innerStream.dispose().await();
                    final ByteReadStreamCounter counter = new ByteReadStreamCounter(innerStream);
                    test.assertThrows(() -> counter.readBytes(10).awaitError(),
                        new PreConditionFailure("isDisposed() cannot be true."));
                    test.assertFalse(counter.hasStarted());
                    test.assertEqual(0, counter.getBytesRead());
                    test.assertEqual(0, counter.getBitsRead());
                    test.assertThrows(counter::getCurrent,
                        new PreConditionFailure("hasCurrent() cannot be false."));
                });

                runner.test("with no bytes to read", (Test test) ->
                {
                    final InMemoryByteStream innerStream = new InMemoryByteStream(new byte[0]).endOfStream();
                    final ByteReadStreamCounter counter = new ByteReadStreamCounter(innerStream);
                    test.assertThrows(() -> counter.readBytes(10).awaitError(),
                        new EndOfStreamException());
                    test.assertTrue(counter.hasStarted());
                    test.assertEqual(0, counter.getBytesRead());
                    test.assertEqual(0, counter.getBitsRead());
                    test.assertThrows(counter::getCurrent,
                        new PreConditionFailure("hasCurrent() cannot be false."));
                });

                runner.test("with negative bytes to read", (Test test) ->
                {
                    final InMemoryByteStream innerStream = new InMemoryByteStream(new byte[] { 0, 1, 2, 3 }).endOfStream();
                    final ByteReadStreamCounter counter = new ByteReadStreamCounter(innerStream);
                    test.assertThrows(() -> counter.readBytes(-1),
                        new PreConditionFailure("bytesToRead (-1) must be greater than or equal to 1."));
                    test.assertFalse(counter.hasStarted());
                    test.assertEqual(0, counter.getBytesRead());
                    test.assertEqual(0, counter.getBitsRead());
                    test.assertFalse(counter.hasCurrent());
                    test.assertThrows(counter::getCurrent,
                        new PreConditionFailure("hasCurrent() cannot be false."));
                });

                runner.test("with zero bytes to read", (Test test) ->
                {
                    final InMemoryByteStream innerStream = new InMemoryByteStream(new byte[] { 0, 1, 2, 3 }).endOfStream();
                    final ByteReadStreamCounter counter = new ByteReadStreamCounter(innerStream);
                    test.assertThrows(() -> counter.readBytes(0),
                        new PreConditionFailure("bytesToRead (0) must be greater than or equal to 1."));
                    test.assertFalse(counter.hasStarted());
                    test.assertEqual(0, counter.getBytesRead());
                    test.assertEqual(0, counter.getBitsRead());
                    test.assertFalse(counter.hasCurrent());
                    test.assertThrows(counter::getCurrent,
                        new PreConditionFailure("hasCurrent() cannot be false."));
                });
            });
        });
    }
}
