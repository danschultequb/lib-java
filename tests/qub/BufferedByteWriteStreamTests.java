package qub;

public interface BufferedByteWriteStreamTests
{
    static void test(TestRunner runner)
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
                    final InMemoryByteStream innerStream = InMemoryByteStream.create();
                    innerStream.dispose().await();
                    final BufferedByteWriteStream byteWriteStream = new BufferedByteWriteStream(innerStream);
                    test.assertTrue(byteWriteStream.isDisposed());
                    test.assertEqual(0, byteWriteStream.getBufferCapacity());
                    test.assertEqual(0, byteWriteStream.getBufferByteCount());
                    test.assertEqual(100000, byteWriteStream.getMaximumBufferSize());
                });

                runner.test("with not-disposed stream", (Test test) ->
                {
                    final InMemoryByteStream innerStream = InMemoryByteStream.create();
                    final BufferedByteWriteStream byteWriteStream = new BufferedByteWriteStream(innerStream);
                    test.assertFalse(byteWriteStream.isDisposed());
                    test.assertEqual(10000, byteWriteStream.getBufferCapacity());
                    test.assertEqual(0, byteWriteStream.getBufferByteCount());
                    test.assertEqual(100000, byteWriteStream.getMaximumBufferSize());
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
                    final InMemoryByteStream innerStream = InMemoryByteStream.create();
                    innerStream.dispose().await();
                    final BufferedByteWriteStream byteWriteStream = new BufferedByteWriteStream(innerStream, 5);
                    test.assertTrue(byteWriteStream.isDisposed());
                    test.assertEqual(0, byteWriteStream.getBufferCapacity());
                    test.assertEqual(0, byteWriteStream.getBufferByteCount());
                    test.assertEqual(5, byteWriteStream.getMaximumBufferSize());
                });

                runner.test("with not-disposed stream", (Test test) ->
                {
                    final InMemoryByteStream innerStream = InMemoryByteStream.create();
                    final BufferedByteWriteStream byteWriteStream = new BufferedByteWriteStream(innerStream, 5, 20);
                    test.assertFalse(byteWriteStream.isDisposed());
                    test.assertEqual(5, byteWriteStream.getBufferCapacity());
                    test.assertEqual(0, byteWriteStream.getBufferByteCount());
                    test.assertEqual(20, byteWriteStream.getMaximumBufferSize());
                });
            });

            runner.testGroup("constructor(ByteWriteStream,int,int)", () ->
            {
                runner.test("with null stream", (Test test) ->
                {
                    test.assertThrows(() -> new BufferedByteWriteStream(null, 5, 10), new PreConditionFailure("byteWriteStream cannot be null."));
                });

                runner.test("with negative initial buffer size", (Test test) ->
                {
                    test.assertThrows(() -> new BufferedByteWriteStream(InMemoryByteStream.create(), -1, 10), new PreConditionFailure("initialBufferSize (-1) must be greater than or equal to 1."));
                });

                runner.test("with zero initial buffer size", (Test test) ->
                {
                    test.assertThrows(() -> new BufferedByteWriteStream(InMemoryByteStream.create(), 0, 10), new PreConditionFailure("initialBufferSize (0) must be greater than or equal to 1."));
                });

                runner.test("with negative maximum buffer size", (Test test) ->
                {
                    test.assertThrows(() -> new BufferedByteWriteStream(InMemoryByteStream.create(), 10, -1), new PreConditionFailure("maximumBufferSize (-1) must be greater than or equal to 10."));
                });

                runner.test("with zero maximum buffer size", (Test test) ->
                {
                    test.assertThrows(() -> new BufferedByteWriteStream(InMemoryByteStream.create(), 10, 0), new PreConditionFailure("maximumBufferSize (0) must be greater than or equal to 10."));
                });

                runner.test("with positive maximum buffer size less than initial buffer size", (Test test) ->
                {
                    test.assertThrows(() -> new BufferedByteWriteStream(InMemoryByteStream.create(), 10, 9), new PreConditionFailure("maximumBufferSize (9) must be greater than or equal to 10."));
                });

                runner.test("with maximum buffer size equal to initial buffer size", (Test test) ->
                {
                    final InMemoryByteStream innerStream = InMemoryByteStream.create();
                    final BufferedByteWriteStream byteWriteStream = new BufferedByteWriteStream(innerStream, 5, 5);
                    test.assertFalse(byteWriteStream.isDisposed());
                    test.assertEqual(5, byteWriteStream.getBufferCapacity());
                    test.assertEqual(0, byteWriteStream.getBufferByteCount());
                    test.assertEqual(5, byteWriteStream.getMaximumBufferSize());
                });

                runner.test("with maximum buffer size greater than initial buffer size", (Test test) ->
                {
                    final InMemoryByteStream innerStream = InMemoryByteStream.create();
                    final BufferedByteWriteStream byteWriteStream = new BufferedByteWriteStream(innerStream, 5, 5);
                    test.assertFalse(byteWriteStream.isDisposed());
                    test.assertEqual(5, byteWriteStream.getBufferCapacity());
                    test.assertEqual(0, byteWriteStream.getBufferByteCount());
                    test.assertEqual(5, byteWriteStream.getMaximumBufferSize());
                });

                runner.test("with disposed stream", (Test test) ->
                {
                    final InMemoryByteStream innerStream = InMemoryByteStream.create();
                    innerStream.dispose().await();
                    final BufferedByteWriteStream byteWriteStream = new BufferedByteWriteStream(innerStream, 5, 10);
                    test.assertTrue(byteWriteStream.isDisposed());
                    test.assertEqual(0, byteWriteStream.getBufferCapacity());
                    test.assertEqual(0, byteWriteStream.getBufferByteCount());
                    test.assertEqual(10, byteWriteStream.getMaximumBufferSize());
                });
            });

            runner.testGroup("write(byte)", () ->
            {
                final int byteCount = 500000;
                runner.speedTest("with " + byteCount + " bytes", Duration.milliseconds(100), (Test test) ->
                {
                    final InMemoryByteStream innerStream = InMemoryByteStream.create();
                    final BufferedByteWriteStream writeStream = new BufferedByteWriteStream(innerStream);

                    for (int i = 0; i < byteCount; ++i)
                    {
                        writeStream.write((byte)42).await();
                    }

                    test.assertTrue(writeStream.dispose().await());
                    test.assertEqual(byteCount, innerStream.getCount());
                });
            });

            runner.testGroup("write(byte[],int,int)", () ->
            {
                runner.test("with null bytes", (Test test) ->
                {
                    final InMemoryByteStream innerStream = InMemoryByteStream.create();
                    final BufferedByteWriteStream byteWriteStream = new BufferedByteWriteStream(innerStream, 1, 20);
                    test.assertThrows(() -> byteWriteStream.write((byte[])null, 0, 0),
                        new PreConditionFailure("bytes cannot be null."));
                    test.assertEqual(1, byteWriteStream.getBufferCapacity());
                    test.assertEqual(0, byteWriteStream.getBufferByteCount());
                    test.assertEqual(0, innerStream.getCount());
                });

                runner.test("with empty bytes", (Test test) ->
                {
                    final InMemoryByteStream innerStream = InMemoryByteStream.create();
                    final BufferedByteWriteStream byteWriteStream = new BufferedByteWriteStream(innerStream, 1, 20);
                    test.assertThrows(() -> byteWriteStream.write(new byte[0], 0, 0),
                        new PreConditionFailure("bytes cannot be empty."));
                    test.assertEqual(1, byteWriteStream.getBufferCapacity());
                    test.assertEqual(0, byteWriteStream.getBufferByteCount());
                    test.assertEqual(0, innerStream.getCount());
                });

                runner.test("with one call with fewer bytes than buffer bytes remaining", (Test test) ->
                {
                    final InMemoryByteStream innerStream = InMemoryByteStream.create();
                    final BufferedByteWriteStream byteWriteStream = new BufferedByteWriteStream(innerStream, 5, 20);
                    test.assertEqual(3, byteWriteStream.write(new byte[] { 1, 2, 3 }).await());
                    test.assertEqual(5, byteWriteStream.getBufferCapacity());
                    test.assertEqual(3, byteWriteStream.getBufferByteCount());
                    test.assertEqual(0, innerStream.getCount());
                });

                runner.test("with multiple calls with fewer bytes than buffer bytes remaining", (Test test) ->
                {
                    final InMemoryByteStream innerStream = InMemoryByteStream.create();
                    final BufferedByteWriteStream byteWriteStream = new BufferedByteWriteStream(innerStream, 5, 20);

                    test.assertEqual(1, byteWriteStream.write(new byte[] { 1 }).await());
                    test.assertEqual(5, byteWriteStream.getBufferCapacity());
                    test.assertEqual(1, byteWriteStream.getBufferByteCount());
                    test.assertEqual(0, innerStream.getCount());

                    test.assertEqual(3, byteWriteStream.write(new byte[] { 2, 3, 4 }).await());
                    test.assertEqual(5, byteWriteStream.getBufferCapacity());
                    test.assertEqual(4, byteWriteStream.getBufferByteCount());
                    test.assertEqual(0, innerStream.getCount());
                });

                runner.test("with one call with bytes equal to buffer bytes remaining", (Test test) ->
                {
                    final InMemoryByteStream innerStream = InMemoryByteStream.create();
                    final BufferedByteWriteStream byteWriteStream = new BufferedByteWriteStream(innerStream, 3, 20);
                    test.assertEqual(3, byteWriteStream.write(new byte[] { 1, 2, 3 }).await());
                    test.assertEqual(6, byteWriteStream.getBufferCapacity());
                    test.assertEqual(0, byteWriteStream.getBufferByteCount());
                    test.assertEqual(new byte[] { 1, 2, 3 }, innerStream.getBytes());
                });

                runner.test("with one call with bytes greater than buffer bytes remaining", (Test test) ->
                {
                    final InMemoryByteStream innerStream = InMemoryByteStream.create();
                    final BufferedByteWriteStream byteWriteStream = new BufferedByteWriteStream(innerStream, 3, 20);
                    test.assertEqual(3, byteWriteStream.write(new byte[] { 1, 2, 3, 4, 5 }).await());
                    test.assertEqual(6, byteWriteStream.getBufferCapacity());
                    test.assertEqual(0, byteWriteStream.getBufferByteCount());
                    test.assertEqual(new byte[] { 1, 2, 3 }, innerStream.getBytes());
                });

                runner.test("with one call with bytes greater than buffer bytes remaining, but inner stream doesn't write entire buffer", (Test test) ->
                {
                    final InMemoryByteStream innerStream = InMemoryByteStream.create();
                    innerStream.setMaxBytesPerWrite(1);
                    final BufferedByteWriteStream byteWriteStream = new BufferedByteWriteStream(innerStream, 3);
                    test.assertEqual(3, byteWriteStream.write(new byte[] { 1, 2, 3, 4, 5 }).await());
                    test.assertEqual(3, byteWriteStream.getBufferCapacity());
                    test.assertEqual(2, byteWriteStream.getBufferByteCount());
                    test.assertEqual(new byte[] { 1 }, innerStream.getBytes());
                });
            });

            runner.testGroup("dispose()", () ->
            {
                runner.test("with no bytes to write", (Test test) ->
                {
                    final InMemoryByteStream innerStream = InMemoryByteStream.create();
                    final BufferedByteWriteStream byteWriteStream = new BufferedByteWriteStream(innerStream, 5);

                    test.assertTrue(byteWriteStream.dispose().await());
                    test.assertEqual(0, byteWriteStream.getBufferCapacity());
                    test.assertEqual(0, byteWriteStream.getBufferByteCount());
                    test.assertEqual(new byte[0], innerStream.getBytes());

                    test.assertFalse(byteWriteStream.dispose().await());
                    test.assertEqual(0, byteWriteStream.getBufferCapacity());
                    test.assertEqual(0, byteWriteStream.getBufferByteCount());
                    test.assertEqual(new byte[0], innerStream.getBytes());
                });

                runner.test("with bytes to write", (Test test) ->
                {
                    final InMemoryByteStream innerStream = InMemoryByteStream.create();
                    final BufferedByteWriteStream byteWriteStream = new BufferedByteWriteStream(innerStream, 5);
                    test.assertEqual(3, byteWriteStream.write(new byte[] { 1, 2, 3 }).await());
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
