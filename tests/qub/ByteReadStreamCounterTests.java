package qub;

public interface ByteReadStreamCounterTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(ByteReadStreamCounter.class, () ->
        {
            runner.testGroup("constructor(ByteReadStream)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> new ByteReadStreamCounter(null),
                        new PreConditionFailure("innerStream cannot be null."));
                });

                runner.test("with disposed", (Test test) ->
                {
                    final InMemoryByteStream byteStream = InMemoryByteStream.create();
                    byteStream.dispose().await();

                    final ByteReadStreamCounter counter = new ByteReadStreamCounter(byteStream);
                    test.assertTrue(counter.isDisposed());
                    test.assertEqual(0, counter.getBytesRead());
                    test.assertEqual(0, counter.getBitsRead());
                });

                runner.test("with not disposed", (Test test) ->
                {
                    final InMemoryByteStream byteStream = InMemoryByteStream.create();
                    final ByteReadStreamCounter counter = new ByteReadStreamCounter(byteStream);
                    test.assertFalse(counter.isDisposed());
                    test.assertEqual(0, counter.getBytesRead());
                    test.assertEqual(0, counter.getBitsRead());
                });
            });

            runner.testGroup("readByte()", () ->
            {
                runner.test("when disposed", (Test test) ->
                {
                    final InMemoryByteStream innerStream = InMemoryByteStream.create(new byte[] { 0, 1, 2, 3 });
                    innerStream.dispose().await();
                    final ByteReadStreamCounter counter = new ByteReadStreamCounter(innerStream);
                    test.assertThrows(() -> counter.readByte().await(),
                        new PreConditionFailure("this.isDisposed() cannot be true."));
                    test.assertEqual(0, counter.getBytesRead());
                    test.assertEqual(0, counter.getBitsRead());
                });

                runner.test("with no bytes to read", (Test test) ->
                {
                    final InMemoryByteStream innerStream = InMemoryByteStream.create(new byte[0]).endOfStream();
                    final ByteReadStreamCounter counter = new ByteReadStreamCounter(innerStream);
                    test.assertThrows(() -> counter.readByte().await(),
                        new EmptyException());
                    test.assertEqual(0, counter.getBytesRead());
                    test.assertEqual(0, counter.getBitsRead());
                });

                runner.test("with bytes to read", (Test test) ->
                {
                    final InMemoryByteStream innerStream = InMemoryByteStream.create(new byte[] { 0, 1, 2, 3 }).endOfStream();
                    final ByteReadStreamCounter counter = new ByteReadStreamCounter(innerStream);
                    test.assertEqual(0, counter.readByte().await());
                    test.assertEqual(1, counter.getBytesRead());
                    test.assertEqual(8, counter.getBitsRead());
                });
            });

            runner.testGroup("readBytes(int)", () ->
            {
                runner.test("when disposed", (Test test) ->
                {
                    final InMemoryByteStream innerStream = InMemoryByteStream.create(new byte[] { 0, 1, 2, 3 });
                    innerStream.dispose().await();
                    final ByteReadStreamCounter counter = new ByteReadStreamCounter(innerStream);
                    test.assertThrows(() -> counter.readBytes(10).await(),
                        new PreConditionFailure("this.isDisposed() cannot be true."));
                    test.assertEqual(0, counter.getBytesRead());
                    test.assertEqual(0, counter.getBitsRead());
                });

                runner.test("with no bytes to read", (Test test) ->
                {
                    final InMemoryByteStream innerStream = InMemoryByteStream.create(new byte[0]).endOfStream();
                    final ByteReadStreamCounter counter = new ByteReadStreamCounter(innerStream);
                    test.assertThrows(() -> counter.readBytes(10).await(),
                        new EmptyException());
                    test.assertEqual(0, counter.getBytesRead());
                    test.assertEqual(0, counter.getBitsRead());
                });

                runner.test("with negative bytes to read", (Test test) ->
                {
                    final InMemoryByteStream innerStream = InMemoryByteStream.create(new byte[] { 0, 1, 2, 3 }).endOfStream();
                    final ByteReadStreamCounter counter = new ByteReadStreamCounter(innerStream);
                    test.assertThrows(() -> counter.readBytes(-1),
                        new PreConditionFailure("bytesToRead (-1) must be greater than or equal to 1."));
                    test.assertEqual(0, counter.getBytesRead());
                    test.assertEqual(0, counter.getBitsRead());
                });

                runner.test("with zero bytes to read", (Test test) ->
                {
                    final InMemoryByteStream innerStream = InMemoryByteStream.create(new byte[] { 0, 1, 2, 3 }).endOfStream();
                    final ByteReadStreamCounter counter = new ByteReadStreamCounter(innerStream);
                    test.assertThrows(() -> counter.readBytes(0),
                        new PreConditionFailure("bytesToRead (0) must be greater than or equal to 1."));
                    test.assertEqual(0, counter.getBytesRead());
                    test.assertEqual(0, counter.getBitsRead());
                });
            });
        });
    }
}
