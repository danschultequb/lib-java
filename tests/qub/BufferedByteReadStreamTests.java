package qub;

public interface BufferedByteReadStreamTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(BufferedByteReadStream.class, () ->
        {
            runner.testGroup("constructor(ByteReadStream)", () ->
            {
                runner.test("with null ByteReadStream", (Test test) ->
                {
                    test.assertThrows(() -> BufferedByteReadStream.create(null),
                        new PreConditionFailure("byteReadStream cannot be null."));
                });

                runner.test("with disposed ByteReadStream", (Test test) ->
                {
                    final InMemoryByteStream byteReadStream = InMemoryByteStream.create();
                    byteReadStream.dispose().await();
                    byteReadStream.endOfStream();

                    final BufferedByteReadStream bufferedByteReadStream = BufferedByteReadStream.create(byteReadStream);
                    test.assertTrue(bufferedByteReadStream.isDisposed());
                    test.assertEqual(0, bufferedByteReadStream.getBufferSize());
                    test.assertEqual(100000, bufferedByteReadStream.getMaximumBufferSize());
                    test.assertEqual(0, bufferedByteReadStream.getBufferedByteCount());
                });

                runner.test("with valid ByteReadStream", (Test test) ->
                {
                    final InMemoryByteStream byteReadStream = InMemoryByteStream.create(new byte[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21 });
                    byteReadStream.endOfStream();

                    final BufferedByteReadStream bufferedByteReadStream = BufferedByteReadStream.create(byteReadStream);
                    test.assertFalse(bufferedByteReadStream.isDisposed());
                    test.assertEqual(10000, bufferedByteReadStream.getBufferSize());
                    test.assertEqual(100000, bufferedByteReadStream.getMaximumBufferSize());
                    test.assertEqual(0, bufferedByteReadStream.getBufferedByteCount());
                });
            });

            runner.testGroup("constructor(ByteReadStream,int)", () ->
            {
                runner.test("with null ByteReadStream", (Test test) ->
                {
                    test.assertThrows(() -> BufferedByteReadStream.create(null, 5),
                        new PreConditionFailure("byteReadStream cannot be null."));
                });

                runner.test("with disposed ByteReadStream", (Test test) ->
                {
                    final InMemoryByteStream byteReadStream = InMemoryByteStream.create();
                    byteReadStream.dispose().await();
                    byteReadStream.endOfStream();

                    final BufferedByteReadStream bufferedByteReadStream = BufferedByteReadStream.create(byteReadStream, 5);
                    test.assertTrue(bufferedByteReadStream.isDisposed());
                    test.assertEqual(0, bufferedByteReadStream.getBufferSize());
                    test.assertEqual(5, bufferedByteReadStream.getMaximumBufferSize());
                    test.assertEqual(0, bufferedByteReadStream.getBufferedByteCount());
                });

                runner.test("with -1 initialBufferSize", (Test test) ->
                {
                    test.assertThrows(() -> BufferedByteReadStream.create(InMemoryByteStream.create(), -1),
                        new PreConditionFailure("bufferSize (-1) must be greater than or equal to 1."));
                });

                runner.test("with -2 initialBufferSize", (Test test) ->
                {
                    test.assertThrows(() -> BufferedByteReadStream.create(InMemoryByteStream.create(), -2),
                        new PreConditionFailure("bufferSize (-2) must be greater than or equal to 1."));
                });

                runner.test("with valid ByteReadStream and 2 initialBufferSize", (Test test) ->
                {
                    final InMemoryByteStream byteReadStream = InMemoryByteStream.create(new byte[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21 });
                    byteReadStream.endOfStream();

                    final BufferedByteReadStream bufferedByteReadStream = BufferedByteReadStream.create(byteReadStream, 2);
                    test.assertFalse(bufferedByteReadStream.isDisposed());
                    test.assertEqual(2, bufferedByteReadStream.getBufferSize());
                    test.assertEqual(2, bufferedByteReadStream.getMaximumBufferSize());
                    test.assertEqual(0, bufferedByteReadStream.getBufferedByteCount());
                });
            });

            runner.testGroup("readByte()", () ->
            {
                runner.test("when disposed", (Test test) ->
                {
                    final InMemoryByteStream innerStream = InMemoryByteStream.create(new byte[] { 0, 1, 2, 3, 4, 5, 6, 7 }).endOfStream();
                    final BufferedByteReadStream byteReadStream = BufferedByteReadStream.create(innerStream, 1);
                    test.assertTrue(byteReadStream.dispose().await());
                    test.assertThrows(byteReadStream::readByte,
                        new PreConditionFailure("this.isDisposed() cannot be true."));
                });

                runner.test("when empty", (Test test) ->
                {
                    final InMemoryByteStream innerStream = InMemoryByteStream.create().endOfStream();
                    final BufferedByteReadStream byteReadStream = BufferedByteReadStream.create(innerStream, 1);
                    test.assertThrows(() -> byteReadStream.readByte().await(), new EmptyException());
                });

                runner.test("when buffer size is smaller than inner stream byte count", (Test test) ->
                {
                    final InMemoryByteStream innerStream = InMemoryByteStream.create(new byte[] { 0, 1, 2, 3, 4, 5 }).endOfStream();
                    final BufferedByteReadStream byteReadStream = BufferedByteReadStream.create(innerStream, 1, 10);

                    test.assertEqual((byte)0, byteReadStream.readByte().await());
                    test.assertEqual(1, byteReadStream.getBufferedByteCount());
                    test.assertEqual(1, byteReadStream.getBufferSize());
                    test.assertEqual(true, byteReadStream.getGrowOnNextBufferFill());

                    test.assertEqual((byte)1, byteReadStream.readByte().await());
                    test.assertEqual(2, byteReadStream.getBufferedByteCount());
                    test.assertEqual(2, byteReadStream.getBufferSize());
                    test.assertEqual(true, byteReadStream.getGrowOnNextBufferFill());

                    test.assertEqual((byte)2, byteReadStream.readByte().await());
                    test.assertEqual(1, byteReadStream.getBufferedByteCount());
                    test.assertEqual(2, byteReadStream.getBufferSize());
                    test.assertEqual(true, byteReadStream.getGrowOnNextBufferFill());

                    test.assertEqual((byte)3, byteReadStream.readByte().await());
                    test.assertEqual(3, byteReadStream.getBufferedByteCount());
                    test.assertEqual(4, byteReadStream.getBufferSize());
                    test.assertEqual(false, byteReadStream.getGrowOnNextBufferFill());

                    test.assertEqual((byte)4, byteReadStream.readByte().await());
                    test.assertEqual(2, byteReadStream.getBufferedByteCount());
                    test.assertEqual(4, byteReadStream.getBufferSize());
                    test.assertEqual(false, byteReadStream.getGrowOnNextBufferFill());

                    test.assertEqual((byte)5, byteReadStream.readByte().await());
                    test.assertEqual(1, byteReadStream.getBufferedByteCount());
                    test.assertEqual(4, byteReadStream.getBufferSize());
                    test.assertEqual(false, byteReadStream.getGrowOnNextBufferFill());

                    test.assertThrows(() -> byteReadStream.readByte().await(),
                        new EmptyException());
                    test.assertEqual(0, byteReadStream.getBufferedByteCount());
                    test.assertEqual(0, byteReadStream.getBufferSize());
                    test.assertEqual(false, byteReadStream.getGrowOnNextBufferFill());
                });

                runner.test("when buffer size is larger than inner stream byte count", (Test test) ->
                {
                    final InMemoryByteStream innerStream = InMemoryByteStream.create(new byte[] { 0, 1 }).endOfStream();
                    final BufferedByteReadStream byteReadStream = BufferedByteReadStream.create(innerStream, 100);

                    test.assertEqual((byte)0, byteReadStream.readByte().await());
                    test.assertEqual(2, byteReadStream.getBufferedByteCount());
                    test.assertEqual(100, byteReadStream.getBufferSize());
                    test.assertEqual(false, byteReadStream.getGrowOnNextBufferFill());

                    test.assertEqual((byte)1, byteReadStream.readByte().await());
                    test.assertEqual(1, byteReadStream.getBufferedByteCount());
                    test.assertEqual(100, byteReadStream.getBufferSize());
                    test.assertEqual(false, byteReadStream.getGrowOnNextBufferFill());

                    test.assertThrows(() -> byteReadStream.readByte().await(), new EmptyException());
                    test.assertEqual(0, byteReadStream.getBufferedByteCount());
                    test.assertEqual(0, byteReadStream.getBufferSize());
                    test.assertEqual(false, byteReadStream.getGrowOnNextBufferFill());
                });

                runner.test("when error occurs", (Test test) ->
                {
                    final ByteReadStream innerStream = FakeByteReadStream.create(() -> Result.error(new Exception("BLAH")));
                    final BufferedByteReadStream byteReadStream = BufferedByteReadStream.create(innerStream, 123);

                    test.assertThrows(() -> byteReadStream.readByte().await(),
                        new RuntimeException(new Exception("BLAH")));
                    test.assertEqual(0, byteReadStream.getBufferedByteCount());
                    test.assertEqual(123, byteReadStream.getBufferSize());
                    test.assertEqual(false, byteReadStream.getGrowOnNextBufferFill());
                });
            });
        });
    }
}
