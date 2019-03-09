package qub;

public class BufferedByteReadStreamTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(BufferedByteReadStream.class, () ->
        {
            runner.testGroup("constructor(ByteReadStream)", () ->
            {
                runner.test("with null ByteReadStream", (Test test) ->
                {
                    test.assertThrows(() -> new BufferedByteReadStream(null));
                });

                runner.test("with disposed ByteReadStream", (Test test) ->
                {
                    final InMemoryByteStream byteReadStream = new InMemoryByteStream();
                    byteReadStream.dispose();
                    byteReadStream.endOfStream();

                    final BufferedByteReadStream bufferedByteReadStream = new BufferedByteReadStream(byteReadStream);
                    test.assertTrue(bufferedByteReadStream.isDisposed());
                    test.assertFalse(bufferedByteReadStream.hasStarted());
                    test.assertFalse(bufferedByteReadStream.hasCurrent());
                    test.assertEqual(0, bufferedByteReadStream.getBufferCapacity());
                    test.assertEqual(0, bufferedByteReadStream.getBufferedByteCount());
                    test.assertSame(byteReadStream.getAsyncRunner(), bufferedByteReadStream.getAsyncRunner());
                    test.assertFalse(byteReadStream.hasStarted());
                    test.assertFalse(byteReadStream.hasCurrent());
                    test.assertEqual(null, byteReadStream.getCurrent());
                });

                runner.test("with valid ByteReadStream", (Test test) ->
                {
                    final InMemoryByteStream byteReadStream = new InMemoryByteStream(new byte[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21 });
                    byteReadStream.endOfStream();

                    final BufferedByteReadStream bufferedByteReadStream = new BufferedByteReadStream(byteReadStream);
                    test.assertFalse(bufferedByteReadStream.isDisposed());
                    test.assertFalse(bufferedByteReadStream.hasStarted());
                    test.assertFalse(bufferedByteReadStream.hasCurrent());
                    test.assertEqual(1024, bufferedByteReadStream.getBufferCapacity());
                    test.assertEqual(0, bufferedByteReadStream.getBufferedByteCount());
                    test.assertSame(byteReadStream.getAsyncRunner(), bufferedByteReadStream.getAsyncRunner());
                    test.assertFalse(byteReadStream.hasStarted());
                    test.assertFalse(byteReadStream.hasCurrent());
                    test.assertEqual(null, byteReadStream.getCurrent());
                });
            });

            runner.testGroup("constructor(ByteReadStream,int)", () ->
            {
                runner.test("with null ByteReadStream", (Test test) ->
                {
                    test.assertThrows(() -> new BufferedByteReadStream(null, 5));
                });

                runner.test("with disposed ByteReadStream", (Test test) ->
                {
                    final InMemoryByteStream byteReadStream = new InMemoryByteStream();
                    byteReadStream.dispose();
                    byteReadStream.endOfStream();

                    final BufferedByteReadStream bufferedByteReadStream = new BufferedByteReadStream(byteReadStream, 5);
                    test.assertTrue(bufferedByteReadStream.isDisposed());
                    test.assertFalse(bufferedByteReadStream.hasStarted());
                    test.assertFalse(bufferedByteReadStream.hasCurrent());
                    test.assertEqual(0, bufferedByteReadStream.getBufferCapacity());
                    test.assertEqual(0, bufferedByteReadStream.getBufferedByteCount());
                    test.assertSame(byteReadStream.getAsyncRunner(), bufferedByteReadStream.getAsyncRunner());
                    test.assertFalse(byteReadStream.hasStarted());
                    test.assertFalse(byteReadStream.hasCurrent());
                    test.assertEqual(null, byteReadStream.getCurrent());
                });

                runner.test("with -1 initialBufferSize", (Test test) ->
                {
                    test.assertThrows(() -> new BufferedByteReadStream(null, -1));
                });

                runner.test("with -2 initialBufferSize", (Test test) ->
                {
                    test.assertThrows(() -> new BufferedByteReadStream(null, -2));
                });

                runner.test("with valid ByteReadStream and 2 initialBufferSize", (Test test) ->
                {
                    final InMemoryByteStream byteReadStream = new InMemoryByteStream(new byte[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21 });
                    byteReadStream.endOfStream();

                    final BufferedByteReadStream bufferedByteReadStream = new BufferedByteReadStream(byteReadStream, 2);
                    test.assertFalse(bufferedByteReadStream.isDisposed());
                    test.assertFalse(bufferedByteReadStream.hasStarted());
                    test.assertFalse(bufferedByteReadStream.hasCurrent());
                    test.assertEqual(2, bufferedByteReadStream.getBufferCapacity());
                    test.assertEqual(0, bufferedByteReadStream.getBufferedByteCount());
                    test.assertSame(byteReadStream.getAsyncRunner(), bufferedByteReadStream.getAsyncRunner());
                    test.assertFalse(byteReadStream.hasStarted());
                    test.assertFalse(byteReadStream.hasCurrent());
                    test.assertEqual(null, byteReadStream.getCurrent());
                });
            });

            runner.testGroup("readByte()", () ->
            {
                runner.test("when disposed", (Test test) ->
                {
                    final InMemoryByteStream innerStream = new InMemoryByteStream(new byte[] { 0, 1, 2, 3, 4, 5, 6, 7 }).endOfStream();
                    final BufferedByteReadStream byteReadStream = new BufferedByteReadStream(innerStream, 1);
                    test.assertSuccess(true, byteReadStream.dispose());
                    test.assertThrows(byteReadStream::readByte, new PreConditionFailure("isDisposed() cannot be true."));
                    test.assertEqual(null, byteReadStream.getCurrent());
                    test.assertFalse(byteReadStream.hasStarted());
                });

                runner.test("when empty", (Test test) ->
                {
                    final InMemoryByteStream innerStream = new InMemoryByteStream().endOfStream();
                    final BufferedByteReadStream byteReadStream = new BufferedByteReadStream(innerStream, 1);
                    test.assertError(new EndOfStreamException(), byteReadStream.readByte());
                    test.assertEqual(null, byteReadStream.getCurrent());
                    test.assertTrue(byteReadStream.hasStarted());
                });

                runner.test("when buffer size is smaller than inner stream byte count", (Test test) ->
                {
                    final InMemoryByteStream innerStream = new InMemoryByteStream(new byte[] { 0, 1, 2, 3, 4, 5 }).endOfStream();
                    final BufferedByteReadStream byteReadStream = new BufferedByteReadStream(innerStream, 1);

                    test.assertSuccess((byte)0, byteReadStream.readByte());
                    test.assertEqual((byte)0, byteReadStream.getCurrent());
                    test.assertEqual(1, byteReadStream.getBufferedByteCount());
                    test.assertEqual(1, byteReadStream.getBufferCapacity());
                    test.assertEqual(true, byteReadStream.getGrowOnNextBufferFill());
                    test.assertEqual((byte)0, innerStream.getCurrent());
                    test.assertTrue(byteReadStream.hasStarted());

                    test.assertSuccess((byte)1, byteReadStream.readByte());
                    test.assertEqual((byte)1, byteReadStream.getCurrent());
                    test.assertEqual(3, byteReadStream.getBufferedByteCount());
                    test.assertEqual(3, byteReadStream.getBufferCapacity());
                    test.assertEqual(true, byteReadStream.getGrowOnNextBufferFill());
                    test.assertEqual((byte)3, innerStream.getCurrent());
                    test.assertTrue(byteReadStream.hasStarted());

                    test.assertSuccess((byte)2, byteReadStream.readByte());
                    test.assertEqual((byte)2, byteReadStream.getCurrent());
                    test.assertEqual(2, byteReadStream.getBufferedByteCount());
                    test.assertEqual(3, byteReadStream.getBufferCapacity());
                    test.assertEqual(true, byteReadStream.getGrowOnNextBufferFill());
                    test.assertEqual((byte)3, innerStream.getCurrent());
                    test.assertTrue(byteReadStream.hasStarted());

                    test.assertSuccess((byte)3, byteReadStream.readByte());
                    test.assertEqual((byte)3, byteReadStream.getCurrent());
                    test.assertEqual(1, byteReadStream.getBufferedByteCount());
                    test.assertEqual(3, byteReadStream.getBufferCapacity());
                    test.assertEqual(true, byteReadStream.getGrowOnNextBufferFill());
                    test.assertEqual((byte)3, innerStream.getCurrent());
                    test.assertTrue(byteReadStream.hasStarted());

                    test.assertSuccess((byte)4, byteReadStream.readByte());
                    test.assertEqual((byte)4, byteReadStream.getCurrent());
                    test.assertEqual(2, byteReadStream.getBufferedByteCount());
                    test.assertEqual(7, byteReadStream.getBufferCapacity());
                    test.assertEqual(false, byteReadStream.getGrowOnNextBufferFill());
                    test.assertEqual((byte)5, innerStream.getCurrent());
                    test.assertTrue(byteReadStream.hasStarted());

                    test.assertSuccess((byte)5, byteReadStream.readByte());
                    test.assertEqual((byte)5, byteReadStream.getCurrent());
                    test.assertEqual(1, byteReadStream.getBufferedByteCount());
                    test.assertEqual(7, byteReadStream.getBufferCapacity());
                    test.assertEqual(false, byteReadStream.getGrowOnNextBufferFill());
                    test.assertEqual((byte)5, innerStream.getCurrent());
                    test.assertTrue(byteReadStream.hasStarted());

                    test.assertError(new EndOfStreamException(), byteReadStream.readByte());
                    test.assertEqual(null, byteReadStream.getCurrent());
                    test.assertEqual(0, byteReadStream.getBufferedByteCount());
                    test.assertEqual(0, byteReadStream.getBufferCapacity());
                    test.assertEqual(false, byteReadStream.getGrowOnNextBufferFill());
                    test.assertEqual(null, innerStream.getCurrent());
                    test.assertTrue(byteReadStream.hasStarted());
                });

                runner.test("when buffer size is larger than inner stream byte count", (Test test) ->
                {
                    final InMemoryByteStream innerStream = new InMemoryByteStream(new byte[] { 0, 1 }).endOfStream();
                    final BufferedByteReadStream byteReadStream = new BufferedByteReadStream(innerStream, 100);

                    test.assertSuccess((byte)0, byteReadStream.readByte());
                    test.assertEqual((byte)0, byteReadStream.getCurrent());
                    test.assertEqual(2, byteReadStream.getBufferedByteCount());
                    test.assertEqual(100, byteReadStream.getBufferCapacity());
                    test.assertEqual(false, byteReadStream.getGrowOnNextBufferFill());
                    test.assertEqual((byte)1, innerStream.getCurrent());
                    test.assertTrue(byteReadStream.hasStarted());

                    test.assertSuccess((byte)1, byteReadStream.readByte());
                    test.assertEqual((byte)1, byteReadStream.getCurrent());
                    test.assertEqual(1, byteReadStream.getBufferedByteCount());
                    test.assertEqual(100, byteReadStream.getBufferCapacity());
                    test.assertEqual(false, byteReadStream.getGrowOnNextBufferFill());
                    test.assertEqual((byte)1, innerStream.getCurrent());
                    test.assertTrue(byteReadStream.hasStarted());

                    test.assertError(new EndOfStreamException(), byteReadStream.readByte());
                    test.assertEqual(null, byteReadStream.getCurrent());
                    test.assertEqual(0, byteReadStream.getBufferedByteCount());
                    test.assertEqual(0, byteReadStream.getBufferCapacity());
                    test.assertEqual(false, byteReadStream.getGrowOnNextBufferFill());
                    test.assertEqual(null, innerStream.getCurrent());
                    test.assertTrue(byteReadStream.hasStarted());
                });

                runner.test("when error occurs", (Test test) ->
                {
                    final ByteReadStream innerStream = new InMemoryByteStream()
                    {
                        @Override
                        public Result<Integer> readBytes(byte[] bytes)
                        {
                            return Result.error(new Exception("BLAH"));
                        }
                    }.endOfStream();
                    final BufferedByteReadStream byteReadStream = new BufferedByteReadStream(innerStream, 123);

                    test.assertThrows(() -> byteReadStream.readByte().awaitError(),
                        new RuntimeException(new Exception("BLAH")));
                    test.assertEqual(0, byteReadStream.getBufferedByteCount());
                    test.assertEqual(123, byteReadStream.getBufferCapacity());
                    test.assertEqual(false, byteReadStream.getGrowOnNextBufferFill());
                    test.assertEqual(null, innerStream.getCurrent());
                    test.assertTrue(byteReadStream.hasStarted());
                    test.assertFalse(byteReadStream.hasCurrent());
                    test.assertEqual(null, byteReadStream.getCurrent());
                });
            });
        });
    }
}
