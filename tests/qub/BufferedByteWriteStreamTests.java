package qub;

public class BufferedByteWriteStreamTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(BufferedByteWriteStream.class, () ->
        {
            runner.testGroup("constructor(ByteWriteStream)", () ->
            {
                runner.test("with null stream", (Test test) ->
                {
                    test.assertThrows(() -> new BufferedByteWriteStream(null), new PreConditionFailure("byteWriteStream cannot be null."));
                });

                runner.test("with disposed stream", (Test test) ->
                {
                    final InMemoryByteStream innerStream = new InMemoryByteStream();
                    innerStream.dispose().await();
                    final BufferedByteWriteStream byteWriteStream = new BufferedByteWriteStream(innerStream);
                    test.assertTrue(byteWriteStream.isDisposed());
                    test.assertEqual(0, byteWriteStream.getBufferCapacity());
                    test.assertEqual(0, byteWriteStream.getBufferByteCount());
                });

                runner.test("with not-disposed stream", (Test test) ->
                {
                    final InMemoryByteStream innerStream = new InMemoryByteStream();
                    final BufferedByteWriteStream byteWriteStream = new BufferedByteWriteStream(innerStream);
                    test.assertFalse(byteWriteStream.isDisposed());
                    test.assertEqual(1024, byteWriteStream.getBufferCapacity());
                    test.assertEqual(0, byteWriteStream.getBufferByteCount());
                });
            });

            runner.testGroup("constructor(ByteWriteStream,int)", () ->
            {
                runner.test("with null stream", (Test test) ->
                {
                    test.assertThrows(() -> new BufferedByteWriteStream(null, 5), new PreConditionFailure("byteWriteStream cannot be null."));
                });

                runner.test("with negative buffer size", (Test test) ->
                {
                    test.assertThrows(() -> new BufferedByteWriteStream(null, -1), new PreConditionFailure("byteWriteStream cannot be null."));
                });

                runner.test("with zero buffer size", (Test test) ->
                {
                    test.assertThrows(() -> new BufferedByteWriteStream(null, 0), new PreConditionFailure("byteWriteStream cannot be null."));
                });

                runner.test("with disposed stream", (Test test) ->
                {
                    final InMemoryByteStream innerStream = new InMemoryByteStream();
                    innerStream.dispose().await();
                    final BufferedByteWriteStream byteWriteStream = new BufferedByteWriteStream(innerStream, 5);
                    test.assertTrue(byteWriteStream.isDisposed());
                    test.assertEqual(0, byteWriteStream.getBufferCapacity());
                    test.assertEqual(0, byteWriteStream.getBufferByteCount());
                });

                runner.test("with not-disposed stream", (Test test) ->
                {
                    final InMemoryByteStream innerStream = new InMemoryByteStream();
                    final BufferedByteWriteStream byteWriteStream = new BufferedByteWriteStream(innerStream, 5);
                    test.assertFalse(byteWriteStream.isDisposed());
                    test.assertEqual(5, byteWriteStream.getBufferCapacity());
                    test.assertEqual(0, byteWriteStream.getBufferByteCount());
                });
            });

            runner.testGroup("writeByte(byte)", () ->
            {
                final int byteCount = 500000;
                runner.speedTest("with " + byteCount + " bytes", Duration.milliseconds(100), (Test test) ->
                {
                    final InMemoryByteStream innerStream = new InMemoryByteStream();
                    final BufferedByteWriteStream writeStream = new BufferedByteWriteStream(innerStream);

                    for (int i = 0; i < byteCount; ++i)
                    {
                        writeStream.writeByte((byte)42).await();
                    }

                    test.assertTrue(writeStream.dispose().await());
                    test.assertEqual(byteCount, innerStream.getCount());
                });
            });

            runner.testGroup("writeBytes(byte[],int,int)", () ->
            {
                runner.test("with null bytes", (Test test) ->
                {
                    final InMemoryByteStream innerStream = new InMemoryByteStream();
                    final BufferedByteWriteStream byteWriteStream = new BufferedByteWriteStream(innerStream, 1);
                    test.assertThrows(() -> byteWriteStream.writeBytes(null, 0, 0), new PreConditionFailure("toWrite cannot be null."));
                    test.assertEqual(1, byteWriteStream.getBufferCapacity());
                    test.assertEqual(0, byteWriteStream.getBufferByteCount());
                    test.assertEqual(0, innerStream.getCount());
                });

                runner.test("with empty bytes", (Test test) ->
                {
                    final InMemoryByteStream innerStream = new InMemoryByteStream();
                    final BufferedByteWriteStream byteWriteStream = new BufferedByteWriteStream(innerStream, 1);
                    test.assertSuccess(0, byteWriteStream.writeBytes(new byte[0], 0, 0));
                    test.assertEqual(1, byteWriteStream.getBufferCapacity());
                    test.assertEqual(0, byteWriteStream.getBufferByteCount());
                    test.assertEqual(0, innerStream.getCount());
                });

                runner.test("with one call with fewer bytes than buffer bytes remaining", (Test test) ->
                {
                    final InMemoryByteStream innerStream = new InMemoryByteStream();
                    final BufferedByteWriteStream byteWriteStream = new BufferedByteWriteStream(innerStream, 5);
                    test.assertSuccess(3, byteWriteStream.writeBytes(new byte[] { 1, 2, 3 }));
                    test.assertEqual(5, byteWriteStream.getBufferCapacity());
                    test.assertEqual(3, byteWriteStream.getBufferByteCount());
                    test.assertEqual(0, innerStream.getCount());
                });

                runner.test("with multiple calls with fewer bytes than buffer bytes remaining", (Test test) ->
                {
                    final InMemoryByteStream innerStream = new InMemoryByteStream();
                    final BufferedByteWriteStream byteWriteStream = new BufferedByteWriteStream(innerStream, 5);

                    test.assertSuccess(1, byteWriteStream.writeBytes(new byte[] { 1 }));
                    test.assertEqual(5, byteWriteStream.getBufferCapacity());
                    test.assertEqual(1, byteWriteStream.getBufferByteCount());
                    test.assertEqual(0, innerStream.getCount());

                    test.assertSuccess(3, byteWriteStream.writeBytes(new byte[] { 2, 3, 4 }));
                    test.assertEqual(5, byteWriteStream.getBufferCapacity());
                    test.assertEqual(4, byteWriteStream.getBufferByteCount());
                    test.assertEqual(0, innerStream.getCount());
                });

                runner.test("with one call with bytes equal to buffer bytes remaining", (Test test) ->
                {
                    final InMemoryByteStream innerStream = new InMemoryByteStream();
                    final BufferedByteWriteStream byteWriteStream = new BufferedByteWriteStream(innerStream, 3);
                    test.assertEqual(3, byteWriteStream.writeBytes(new byte[] { 1, 2, 3 }).await());
                    test.assertEqual(6, byteWriteStream.getBufferCapacity());
                    test.assertEqual(0, byteWriteStream.getBufferByteCount());
                    test.assertEqual(new byte[] { 1, 2, 3 }, innerStream.getBytes());
                });

                runner.test("with one call with bytes greater than buffer bytes remaining", (Test test) ->
                {
                    final InMemoryByteStream innerStream = new InMemoryByteStream();
                    final BufferedByteWriteStream byteWriteStream = new BufferedByteWriteStream(innerStream, 3);
                    test.assertEqual(3, byteWriteStream.writeBytes(new byte[] { 1, 2, 3, 4, 5 }).await());
                    test.assertEqual(6, byteWriteStream.getBufferCapacity());
                    test.assertEqual(0, byteWriteStream.getBufferByteCount());
                    test.assertEqual(new byte[] { 1, 2, 3 }, innerStream.getBytes());
                });

                runner.test("with one call with bytes greater than buffer bytes remaining, but inner stream doesn't write entire buffer", (Test test) ->
                {
                    final InMemoryByteStream innerStream = new InMemoryByteStream();
                    innerStream.setMaxBytesPerWrite(1);
                    final BufferedByteWriteStream byteWriteStream = new BufferedByteWriteStream(innerStream, 3);
                    test.assertEqual(3, byteWriteStream.writeBytes(new byte[] { 1, 2, 3, 4, 5 }).await());
                    test.assertEqual(3, byteWriteStream.getBufferCapacity());
                    test.assertEqual(2, byteWriteStream.getBufferByteCount());
                    test.assertEqual(new byte[] { 1 }, innerStream.getBytes());
                });
            });

            runner.testGroup("dispose()", () ->
            {
                runner.test("with no bytes to write", (Test test) ->
                {
                    final InMemoryByteStream innerStream = new InMemoryByteStream();
                    final BufferedByteWriteStream byteWriteStream = new BufferedByteWriteStream(innerStream, 5);

                    test.assertTrue(byteWriteStream.dispose().await());
                    test.assertEqual(5, byteWriteStream.getBufferCapacity());
                    test.assertEqual(0, byteWriteStream.getBufferByteCount());
                    test.assertEqual(new byte[0], innerStream.getBytes());

                    test.assertFalse(byteWriteStream.dispose().await());
                    test.assertEqual(5, byteWriteStream.getBufferCapacity());
                    test.assertEqual(0, byteWriteStream.getBufferByteCount());
                    test.assertEqual(new byte[0], innerStream.getBytes());
                });

                runner.test("with bytes to write", (Test test) ->
                {
                    final InMemoryByteStream innerStream = new InMemoryByteStream();
                    final BufferedByteWriteStream byteWriteStream = new BufferedByteWriteStream(innerStream, 5);
                    byteWriteStream.writeBytes(new byte[] { 1, 2, 3 }).await();
                    test.assertEqual(5, byteWriteStream.getBufferCapacity());
                    test.assertEqual(3, byteWriteStream.getBufferByteCount());
                    test.assertEqual(new byte[0], innerStream.getBytes());

                    test.assertTrue(byteWriteStream.dispose().await());
                    test.assertEqual(0, byteWriteStream.getBufferCapacity());
                    test.assertEqual(0, byteWriteStream.getBufferByteCount());
                    test.assertEqual(new byte[] { 1, 2, 3 }, innerStream.getBytes());

                    test.assertFalse(byteWriteStream.dispose().await());
                    test.assertEqual(0, byteWriteStream.getBufferCapacity());
                    test.assertEqual(0, byteWriteStream.getBufferByteCount());
                    test.assertEqual(new byte[] { 1, 2, 3 }, innerStream.getBytes());
                });
            });
        });
    }
}
