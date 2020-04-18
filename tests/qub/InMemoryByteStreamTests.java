package qub;

public interface InMemoryByteStreamTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(InMemoryByteStream.class, () ->
        {
            runner.test("create()", (Test test) ->
            {
                final InMemoryByteStream stream = InMemoryByteStream.create();
                test.assertFalse(stream.isDisposed());
                test.assertEqual(new byte[0], stream.getBytes());
                test.assertNull(stream.getMaxBytesPerRead());
                test.assertNull(stream.getMaxBytesPerWrite());
            });

            runner.testGroup("create(byte[])", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> InMemoryByteStream.create((byte[])null),
                        new PreConditionFailure("bytes cannot be null."));
                });

                runner.test("with empty", (Test test) ->
                {
                    final InMemoryByteStream stream = InMemoryByteStream.create();
                    test.assertFalse(stream.isDisposed());
                    test.assertEqual(new byte[0], stream.getBytes());
                    test.assertNull(stream.getMaxBytesPerRead());
                    test.assertNull(stream.getMaxBytesPerWrite());
                });
            });
            
            runner.test("close()", (Test test) ->
            {
                final InMemoryByteStream stream = InMemoryByteStream.create();
                stream.close();
                test.assertTrue(stream.isDisposed());
                test.assertEqual(new byte[0], stream.getBytes());
                stream.close();
                test.assertTrue(stream.isDisposed());
                test.assertEqual(new byte[0], stream.getBytes());
            });

            runner.testGroup("setMaxBytesPerRead(Integer)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final InMemoryByteStream stream = InMemoryByteStream.create();
                    test.assertSame(stream, stream.setMaxBytesPerRead(null));
                    test.assertNull(stream.getMaxBytesPerRead());
                });

                runner.test("with -1", (Test test) ->
                {
                    final InMemoryByteStream stream = InMemoryByteStream.create();
                    test.assertThrows(() -> stream.setMaxBytesPerRead(-1),
                        new PreConditionFailure("maxBytesPerRead (-1) == null || maxBytesPerRead (-1) >= 1 cannot be false."));
                    test.assertNull(stream.getMaxBytesPerRead());
                });

                runner.test("with 0", (Test test) ->
                {
                    final InMemoryByteStream stream = InMemoryByteStream.create();
                    test.assertThrows(() -> stream.setMaxBytesPerRead(0),
                        new PreConditionFailure("maxBytesPerRead (0) == null || maxBytesPerRead (0) >= 1 cannot be false."));
                    test.assertNull(stream.getMaxBytesPerRead());
                });

                runner.test("with 1", (Test test) ->
                {
                    final InMemoryByteStream stream = InMemoryByteStream.create();
                    test.assertSame(stream, stream.setMaxBytesPerRead(1));
                    test.assertEqual(1, stream.getMaxBytesPerRead());
                });

                runner.test("with 2", (Test test) ->
                {
                    final InMemoryByteStream stream = InMemoryByteStream.create();
                    test.assertSame(stream, stream.setMaxBytesPerRead(2));
                    test.assertEqual(2, stream.getMaxBytesPerRead());
                });
            });

            runner.testGroup("setMaxBytesPerWrite(Integer)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final InMemoryByteStream stream = InMemoryByteStream.create();
                    test.assertSame(stream, stream.setMaxBytesPerWrite(null));
                    test.assertNull(stream.getMaxBytesPerWrite());
                });

                runner.test("with -1", (Test test) ->
                {
                    final InMemoryByteStream stream = InMemoryByteStream.create();
                    test.assertThrows(() -> stream.setMaxBytesPerWrite(-1),
                        new PreConditionFailure("maxBytesPerWrite (-1) == null || maxBytesPerWrite (-1) >= 1 cannot be false."));
                    test.assertNull(stream.getMaxBytesPerWrite());
                });

                runner.test("with 0", (Test test) ->
                {
                    final InMemoryByteStream stream = InMemoryByteStream.create();
                    test.assertThrows(() -> stream.setMaxBytesPerWrite(0),
                        new PreConditionFailure("maxBytesPerWrite (0) == null || maxBytesPerWrite (0) >= 1 cannot be false."));
                    test.assertNull(stream.getMaxBytesPerWrite());
                });

                runner.test("with 1", (Test test) ->
                {
                    final InMemoryByteStream stream = InMemoryByteStream.create();
                    test.assertSame(stream, stream.setMaxBytesPerWrite(1));
                    test.assertEqual(1, stream.getMaxBytesPerWrite());
                });

                runner.test("with 2", (Test test) ->
                {
                    final InMemoryByteStream stream = InMemoryByteStream.create();
                    test.assertSame(stream, stream.setMaxBytesPerWrite(2));
                    test.assertEqual(2, stream.getMaxBytesPerWrite());
                });
            });

            runner.testGroup("readByte()", () ->
            {
                runner.test("with no bytes to read", (Test test) ->
                {
                    final InMemoryByteStream stream = create(test);

                    test.assertThrows(() -> stream.readByte().await(), new EndOfStreamException());
                });

                runner.test("with one byte to read", (Test test) ->
                {
                    final InMemoryByteStream stream = create(new byte[] { 10 }, test);

                    test.assertEqual(10, stream.readByte().await());
                    test.assertThrows(() -> stream.readByte().await(), new EndOfStreamException());
                });

                runner.test("with two bytes to read", (Test test) ->
                {
                    final InMemoryByteStream stream = create(new byte[] { 10, 20 }, test);

                    test.assertEqual(10, stream.readByte().await());
                    test.assertEqual(20, stream.readByte().await());
                    test.assertThrows(() -> stream.readByte().await(), new EndOfStreamException());
                });

                runner.test("with disposed ByteReadStream", (Test test) ->
                {
                    final InMemoryByteStream readStream = create(test);
                    readStream.dispose().await();

                    test.assertThrows(readStream::readByte, new PreConditionFailure("isDisposed() cannot be true."));
                });
            });
            
            runner.test("readBytes(int)", (Test test) ->
            {
                final InMemoryByteStream readStream1 = create(test);

                test.assertThrows(() -> readStream1.readBytes(-5), new PreConditionFailure("bytesToRead (-5) must be greater than or equal to 0."));
                test.assertEqual(new byte[0], readStream1.readBytes(0).await());
                test.assertThrows(() -> readStream1.readBytes(1).await(), new EndOfStreamException());
                test.assertThrows(() -> readStream1.readBytes(5).await(), new EndOfStreamException());

                final InMemoryByteStream readStream2 = create(new byte[] { 0, 1, 2, 3 }, test);

                test.assertThrows(() -> readStream2.readBytes(-5), new PreConditionFailure("bytesToRead (-5) must be greater than or equal to 0."));
                test.assertEqual(new byte[0], readStream2.readBytes(0).await());
                test.assertEqual(new byte[] { 0 }, readStream2.readBytes(1).await());
                test.assertEqual(new byte[] { 1, 2 }, readStream2.readBytes(2).await());
                test.assertEqual(new byte[] { 3 }, readStream2.readBytes(3).await());
                test.assertThrows(() -> readStream2.readBytes(1).await(), new EndOfStreamException());
                test.assertThrows(() -> readStream2.readBytes(5).await(), new EndOfStreamException());

                test.assertTrue(readStream2.dispose().await());

                test.assertThrows(() -> readStream2.readBytes(1), new PreConditionFailure("isDisposed() cannot be true."));
            });

            runner.testGroup("readBytes(byte[])", () ->
            {
                runner.test("with disposed ByteReadStream", (Test test) ->
                {
                    final InMemoryByteStream stream = create(test);
                    stream.dispose().await();

                    final byte[] outputBytes = new byte[10];
                    test.assertThrows(() -> stream.readBytes(outputBytes),
                        new PreConditionFailure("isDisposed() cannot be true."));
                    test.assertEqual(new byte[10], outputBytes);
                });

                runner.test("with null outputBytes", (Test test) ->
                {
                    final InMemoryByteStream stream = create(test);
                    final byte[] outputBytes = null;
                    test.assertThrows(() -> stream.readBytes(outputBytes),
                        new PreConditionFailure("outputBytes cannot be null."));
                });

                runner.test("with empty outputBytes", (Test test) ->
                {
                    final InMemoryByteStream stream = create(test);
                    final byte[] outputBytes = new byte[0];
                    test.assertEqual(0, stream.readBytes(outputBytes).await());
                });

                runner.test("with no bytes to read", (Test test) ->
                {
                    final InMemoryByteStream stream = create(test);
                    final byte[] outputBytes = new byte[10];
                    test.assertThrows(() -> stream.readBytes(outputBytes).await(),
                        new EndOfStreamException());
                    test.assertEqual(new byte[10], outputBytes);
                });

                runner.test("with fewer bytes to read than outputBytes", (Test test) ->
                {
                    final InMemoryByteStream stream = create(new byte[] { 1, 2, 3, 4 }, test);
                    final byte[] outputBytes = new byte[10];
                    test.assertEqual(4, stream.readBytes(outputBytes).await());
                    test.assertEqual(new byte[] { 1, 2, 3, 4, 0, 0, 0, 0, 0, 0 }, outputBytes);
                    test.assertEqual(new byte[0], stream.getBytes());

                    test.assertThrows(() -> stream.readByte().await(),
                        new EndOfStreamException());
                });

                runner.test("with equal number of bytes to read to outputBytes", (Test test) ->
                {
                    final InMemoryByteStream stream = create(new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 }, test);
                    final byte[] outputBytes = new byte[10];
                    test.assertEqual(10, stream.readBytes(outputBytes).await());
                    test.assertEqual(new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 }, outputBytes);
                    test.assertEqual(new byte[0], stream.getBytes());

                    test.assertThrows(() -> stream.readByte().await(),
                        new EndOfStreamException());
                });

                runner.test("with more bytes to read than outputBytes", (Test test) ->
                {
                    final InMemoryByteStream stream = create(new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11 }, test);
                    final byte[] outputBytes = new byte[10];
                    test.assertEqual(10, stream.readBytes(outputBytes).await());
                    test.assertEqual(new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 }, outputBytes);
                    test.assertEqual(new byte[] { 11 }, stream.getBytes());

                    test.assertEqual((byte)11, stream.readByte().await());
                    test.assertEqual(new byte[0], stream.getBytes());
                });

                runner.test("with fewer bytes to read than limiter", (Test test) ->
                {
                    final InMemoryByteStream stream = create(new byte[] { 0, 1, 2, 3, 4 }, test)
                        .setMaxBytesPerRead(20);
                    final byte[] outputBytes = new byte[10];
                    test.assertEqual(5, stream.readBytes(outputBytes).await());
                    test.assertEqual(new byte[] { 0, 1, 2, 3, 4, 0, 0, 0, 0, 0 }, outputBytes);
                    test.assertEqual(new byte[0], stream.getBytes());

                    test.assertThrows(() -> stream.readByte().await(),
                        new EndOfStreamException());
                });

                runner.test("with equal bytes to read to limiter", (Test test) ->
                {
                    final InMemoryByteStream stream = create(new byte[] { 0, 1, 2, 3, 4 }, test)
                        .setMaxBytesPerRead(5);
                    final byte[] outputBytes = new byte[10];
                    test.assertEqual(5, stream.readBytes(outputBytes).await());
                    test.assertEqual(new byte[] { 0, 1, 2, 3, 4, 0, 0, 0, 0, 0 }, outputBytes);
                    test.assertEqual(new byte[0], stream.getBytes());

                    test.assertThrows(() -> stream.readByte().await(),
                        new EndOfStreamException());
                });

                runner.test("with more bytes to read than limiter", (Test test) ->
                {
                    final InMemoryByteStream stream = create(new byte[] { 0, 1, 2, 3, 4 }, test)
                        .setMaxBytesPerRead(3);
                    final byte[] outputBytes = new byte[10];
                    test.assertEqual(3, stream.readBytes(outputBytes).await());
                    test.assertEqual(new byte[] { 0, 1, 2, 0, 0, 0, 0, 0, 0, 0 }, outputBytes);
                    test.assertEqual(new byte[] { 3, 4 }, stream.getBytes());
                });
            });

            runner.testGroup("readBytes(byte[],int,int)", () ->
            {
                runner.test("with disposed ByteReadStream", (Test test) ->
                {
                    final InMemoryByteStream stream = create(test);
                    test.assertTrue(stream.dispose().await());

                    final byte[] outputBytes = new byte[10];
                    test.assertThrows(() -> stream.readBytes(outputBytes, 1, 3),
                        new PreConditionFailure("isDisposed() cannot be true."));
                    test.assertEqual(new byte[10], outputBytes);
                });

                runner.test("with null outputBytes", (Test test) ->
                {
                    final InMemoryByteStream stream = create(test);
                    final byte[] outputBytes = null;
                    test.assertThrows(() -> stream.readBytes(outputBytes, 1, 3),
                        new PreConditionFailure("outputBytes cannot be null."));
                });

                runner.test("with empty outputBytes", (Test test) ->
                {
                    final InMemoryByteStream stream = create(test);
                    final byte[] outputBytes = new byte[0];
                    test.assertEqual(0, stream.readBytes(outputBytes, 0, 0).await());
                });

                runner.test("with negative startIndex", (Test test) ->
                {
                    final InMemoryByteStream stream = create(test);
                    final byte[] outputBytes = new byte[10];
                    test.assertThrows(() -> stream.readBytes(outputBytes, -2, 3),
                        new PreConditionFailure("startIndex (-2) must be between 0 and 9."));
                });

                runner.test("with startIndex equal to outputBytes.length", (Test test) ->
                {
                    final InMemoryByteStream stream = create(test);
                    final byte[] outputBytes = new byte[10];
                    test.assertThrows(() -> stream.readBytes(outputBytes, outputBytes.length, 3),
                        new PreConditionFailure("startIndex (10) must be between 0 and 9."));
                });

                runner.test("with negative length", (Test test) ->
                {
                    final InMemoryByteStream stream = create(test);
                    final byte[] outputBytes = new byte[10];
                    test.assertThrows(() -> stream.readBytes(outputBytes, 1, -1),
                        new PreConditionFailure("length (-1) must be between 0 and 9."));
                });

                runner.test("with zero length", (Test test) ->
                {
                    final InMemoryByteStream stream = create(test);
                    final byte[] outputBytes = new byte[10];
                    test.assertEqual(0, stream.readBytes(outputBytes, 1, 0).await());
                    test.assertEqual(new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, outputBytes);
                });

                runner.test("with length greater than outputBytes.length - startIndex", (Test test) ->
                {
                    final InMemoryByteStream stream = create(test);
                    final byte[] outputBytes = new byte[10];
                    test.assertThrows(() -> stream.readBytes(outputBytes, 1, 10),
                        new PreConditionFailure("length (10) must be between 0 and 9."));
                });

                runner.test("with no bytes to read", (Test test) ->
                {
                    final InMemoryByteStream stream = create(test);
                    final byte[] outputBytes = new byte[10];
                    test.assertThrows(() -> stream.readBytes(outputBytes, 1, 3).await(),
                        new EndOfStreamException());
                    test.assertEqual(new byte[10], outputBytes);
                });

                runner.test("with fewer bytes to read than length", (Test test) ->
                {
                    final InMemoryByteStream stream = create(new byte[] { 1, 2, 3, 4 }, test);
                    final byte[] outputBytes = new byte[10];
                    test.assertEqual(4, stream.readBytes(outputBytes, 0, 10).await());
                    test.assertEqual(new byte[] { 1, 2, 3, 4, 0, 0, 0, 0, 0, 0 }, outputBytes);
                    test.assertThrows(() -> stream.readByte().await(), new EndOfStreamException());
                });

                runner.test("with equal number of bytes to read to length", (Test test) ->
                {
                    final InMemoryByteStream stream = create(new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 }, test);
                    final byte[] outputBytes = new byte[10];
                    test.assertEqual(10, stream.readBytes(outputBytes, 0, 10).await());
                    test.assertEqual(new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 }, outputBytes);
                    test.assertThrows(() -> stream.readByte().await(), new EndOfStreamException());
                });

                runner.test("with more bytes to read than length", (Test test) ->
                {
                    final InMemoryByteStream stream = create(new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11 }, test);
                    final byte[] outputBytes = new byte[10];
                    test.assertEqual(10, stream.readBytes(outputBytes, 0, 10).await());
                    test.assertEqual(new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 }, outputBytes);
                    test.assertEqual((byte)11, stream.readByte().await());
                });
            });

            runner.testGroup("readAllBytes()", () ->
            {
                runner.test("with disposed ByteReadStream", (Test test) ->
                {
                    final InMemoryByteStream stream = create(test);
                    stream.dispose();
                    test.assertThrows(stream::readAllBytes, new PreConditionFailure("isDisposed() cannot be true."));
                });

                runner.test("with no bytes to read", (Test test) ->
                {
                    final InMemoryByteStream stream = create(test);
                    test.assertThrows(() -> stream.readAllBytes().await(),
                        new EndOfStreamException());
                });
            });

            runner.testGroup("readBytesUntil(byte)", () ->
            {
                runner.test("with disposed ByteReadStream", (Test test) ->
                {
                    final InMemoryByteStream stream = create(test);
                    test.assertTrue(stream.dispose().await());
                    test.assertThrows(() -> stream.readBytesUntil((byte)5), new PreConditionFailure("isDisposed() cannot be true."));
                });

                runner.test("with no bytes to read", (Test test) ->
                {
                    final InMemoryByteStream stream = create(test);
                    test.assertThrows(() -> stream.readBytesUntil((byte)5).await(), new EndOfStreamException());
                });

                runner.test("with bytes but not the stopByte", (Test test) ->
                {
                    final InMemoryByteStream stream = create(new byte[] { 0, 1, 2, 3, 4, 6, 7, 8, 9 }, test);
                    test.assertEqual(new byte[] { 0, 1, 2, 3, 4, 6, 7, 8, 9 }, stream.readBytesUntil((byte)5).await());
                    test.assertEqual(new byte[0], stream.getBytes());
                });

                runner.test("with bytes including the stopByte", (Test test) ->
                {
                    final InMemoryByteStream stream = create(new byte[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 }, test);
                    test.assertEqual(new byte[] { 0, 1, 2, 3, 4, 5 }, stream.readBytesUntil((byte)5).await());
                    test.assertEqual(new byte[] { 6, 7, 8, 9 }, stream.getBytes());
                });
            });

            runner.testGroup("readBytesUntil(Iterable<Byte>)", () ->
            {
                runner.test("with disposed ByteReadStream", (Test test) ->
                {
                    final InMemoryByteStream stream = create(test);
                    test.assertTrue(stream.dispose().await());
                    test.assertThrows(() -> stream.readBytesUntil(ByteArray.create(5).await()),
                        new PreConditionFailure("isDisposed() cannot be true."));
                });

                runner.test("with no bytes to read", (Test test) ->
                {
                    final InMemoryByteStream stream = create(test);
                    test.assertThrows(() -> stream.readBytesUntil(ByteArray.create(5).await()).await(),
                        new EndOfStreamException());
                });

                runner.test("with bytes but not the stopByte", (Test test) ->
                {
                    final InMemoryByteStream stream = create(new byte[] { 0, 1, 2, 3, 4, 6, 7, 8, 9 }, test);
                    test.assertEqual(new byte[] { 0, 1, 2, 3, 4, 6, 7, 8, 9 }, stream.readBytesUntil(ByteArray.create(5).await()).await());
                    test.assertEqual(new byte[0], stream.getBytes());
                });

                runner.test("with bytes including the stopByte", (Test test) ->
                {
                    final InMemoryByteStream stream = create(new byte[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 }, test);
                    test.assertEqual(new byte[] { 0, 1, 2, 3, 4, 5 }, stream.readBytesUntil(ByteArray.create(5).await()).await());
                    test.assertEqual(new byte[] { 6, 7, 8, 9 }, stream.getBytes());
                });
            });

            runner.testGroup("write(byte)", () ->
            {
                runner.test("with 1 byte", (Test test) ->
                {
                    final InMemoryByteStream stream = InMemoryByteStream.create();
                    test.assertEqual(1, stream.write((byte)17).await());
                    test.assertEqual(new byte[] { 17 }, stream.getBytes());
                });

                final int byteCount = 500000;
                runner.speedTest("with " + byteCount + " bytes", Duration.milliseconds(100), (Test test) ->
                {
                    final InMemoryByteStream innerStream = InMemoryByteStream.create();
                    final BufferedByteWriteStream writeStream = new BufferedByteWriteStream(innerStream);

                    for (int i = 0; i < byteCount; ++i)
                    {
                        test.assertEqual(1, writeStream.write((byte)42).await());
                    }
                    test.assertTrue(writeStream.dispose().await());
                    test.assertEqual(byteCount, innerStream.getCount());
                });
            });

            runner.test("write(byte[])", (Test test) ->
            {
                final InMemoryByteStream stream = InMemoryByteStream.create();
                test.assertThrows(() -> stream.write(new byte[0]), new PreConditionFailure("toWrite cannot be empty."));
                test.assertEqual(new byte[0], stream.getBytes());

                test.assertEqual(4, stream.write(new byte[] { 1, 2, 3, 4 }).await());
                test.assertEqual(new byte[] { 1, 2, 3, 4 }, stream.getBytes());
            });

            runner.testGroup("writeBytes(byte[],int,int)", () ->
            {
                runner.test("with null bytes", (Test test) ->
                {
                    final InMemoryByteStream stream = InMemoryByteStream.create();
                    test.assertThrows(() -> stream.write((byte[])null, 0, 0),
                        new PreConditionFailure("bytes cannot be null."));
                    test.assertEqual(new byte[0], stream.getBytes());
                });

                runner.test("with empty bytes, -1 startIndex, and -1 length", (Test test) ->
                {
                    final InMemoryByteStream stream = InMemoryByteStream.create();
                    test.assertThrows(() -> stream.write(new byte[0], -1, -1),
                        new PreConditionFailure("bytes cannot be empty."));
                    test.assertEqual(new byte[0], stream.getBytes());
                });

                runner.test("with empty bytes, -1 startIndex, and 0 length", (Test test) ->
                {
                    final InMemoryByteStream stream = InMemoryByteStream.create();
                    test.assertThrows(() -> stream.write(new byte[0], -1, 0),
                        new PreConditionFailure("bytes cannot be empty."));
                    test.assertEqual(new byte[0], stream.getBytes());
                });

                runner.test("with empty bytes, -1 startIndex, and 1 length", (Test test) ->
                {
                    final InMemoryByteStream stream = InMemoryByteStream.create();
                    test.assertThrows(() -> stream.write(new byte[0], -1, 1),
                        new PreConditionFailure("bytes cannot be empty."));
                    test.assertEqual(new byte[0], stream.getBytes());
                });

                runner.test("with empty bytes, 0 startIndex, and -1 length", (Test test) ->
                {
                    final InMemoryByteStream stream = InMemoryByteStream.create();
                    test.assertThrows(() -> stream.write(new byte[0], 0, -1),
                        new PreConditionFailure("bytes cannot be empty."));
                    test.assertEqual(new byte[0], stream.getBytes());
                });

                runner.test("with empty bytes, 0 startIndex, and 1 length", (Test test) ->
                {
                    final InMemoryByteStream stream = InMemoryByteStream.create();
                    test.assertThrows(() -> stream.write(new byte[0], 0, 1),
                        new PreConditionFailure("bytes cannot be empty."));
                    test.assertEqual(new byte[0], stream.getBytes());
                });

                runner.test("with empty bytes, 1 startIndex, and -1 length", (Test test) ->
                {
                    final InMemoryByteStream stream = InMemoryByteStream.create();
                    test.assertThrows(() -> stream.write(new byte[0], 1, -1),
                        new PreConditionFailure("bytes cannot be empty."));
                    test.assertEqual(new byte[0], stream.getBytes());
                });

                runner.test("with empty bytes, 1 startIndex, and 0 length", (Test test) ->
                {
                    final InMemoryByteStream stream = InMemoryByteStream.create();
                    test.assertThrows(() -> stream.write(new byte[0], 1, 0),
                        new PreConditionFailure("bytes cannot be empty."));
                    test.assertEqual(new byte[0], stream.getBytes());
                });

                runner.test("with empty bytes, 1 startIndex, and 1 length", (Test test) ->
                {
                    final InMemoryByteStream stream = InMemoryByteStream.create();
                    test.assertThrows(() -> stream.write(new byte[0], 1, 1),
                        new PreConditionFailure("bytes cannot be empty."));
                    test.assertEqual(new byte[0], stream.getBytes());
                });

                runner.test("with empty bytes, 0 startIndex, and 0 length", (Test test) ->
                {
                    final InMemoryByteStream stream = InMemoryByteStream.create();
                    test.assertThrows(() -> stream.write(new byte[0], 0, 0),
                        new PreConditionFailure("bytes cannot be empty."));
                    test.assertEqual(new byte[0], stream.getBytes());
                });

                runner.test("with non-empty bytes, valid startIndex, and valid length", (Test test) ->
                {
                    final InMemoryByteStream stream = InMemoryByteStream.create();
                    test.assertEqual(2, stream.write(new byte[] { 0, 1, 2, 3, 4 }, 1, 2).await());
                    test.assertEqual(new byte[] { 1, 2 }, stream.getBytes());
                });

                runner.test("with non-empty bytes, valid startIndex, valid length, and maxBytesPerWrite less than length", (Test test) ->
                {
                    final InMemoryByteStream stream = InMemoryByteStream.create()
                        .setMaxBytesPerWrite(3);
                    test.assertEqual(3, stream.write(new byte[] { 0, 1, 2, 3, 4 }, 1, 4).await());
                    test.assertEqual(new byte[] { 1, 2, 3 }, stream.getBytes());
                });

                runner.test("with non-empty bytes, valid startIndex, valid length, and maxBytesPerWrite equal to length", (Test test) ->
                {
                    final InMemoryByteStream stream = InMemoryByteStream.create()
                        .setMaxBytesPerWrite(3);
                    test.assertEqual(3, stream.write(new byte[] { 0, 1, 2, 3, 4 }, 1, 3).await());
                    test.assertEqual(new byte[] { 1, 2, 3 }, stream.getBytes());
                });

                runner.test("with non-empty bytes, valid startIndex, valid length, and maxBytesPerWrite greater than length", (Test test) ->
                {
                    final InMemoryByteStream stream = InMemoryByteStream.create()
                        .setMaxBytesPerWrite(3);
                    test.assertEqual(2, stream.write(new byte[] { 0, 1, 2, 3, 4 }, 1, 2).await());
                    test.assertEqual(new byte[] { 1, 2 }, stream.getBytes());
                });
            });

            runner.testGroup("writeAll(ByteReadStream)", () ->
            {
                runner.test("with null ByteReadStream", (Test test) ->
                {
                    final InMemoryByteStream stream = InMemoryByteStream.create();
                    test.assertThrows(() -> stream.writeAll((ByteReadStream)null),
                        new PreConditionFailure("byteReadStream cannot be null."));
                    test.assertEqual(new byte[0], stream.getBytes());
                });

                runner.test("with disposed ByteReadStream", (Test test) ->
                {
                    final InMemoryByteStream stream = InMemoryByteStream.create();
                    final InMemoryByteStream readStream = InMemoryByteStream.create();
                    readStream.dispose();
                    test.assertThrows(() -> stream.writeAll(readStream).await(),
                        new PreConditionFailure("byteReadStream.isDisposed() cannot be true."));
                    test.assertEqual(new byte[0], stream.getBytes());
                });

                runner.test("with disposed ByteWriteStream", (Test test) ->
                {
                    final InMemoryByteStream stream = InMemoryByteStream.create();
                    stream.dispose();
                    final InMemoryByteStream readStream = InMemoryByteStream.create().endOfStream();
                    test.assertThrows(() -> stream.writeAll(readStream).await(),
                        new PreConditionFailure("this.isDisposed() cannot be true."));
                    test.assertEqual(new byte[0], stream.getBytes());
                });

                runner.test("with empty ByteReadStream", (Test test) ->
                {
                    final InMemoryByteStream stream = InMemoryByteStream.create();
                    final InMemoryByteStream readStream = InMemoryByteStream.create().endOfStream();
                    test.assertEqual(0, stream.writeAll(readStream).await());
                    test.assertEqual(new byte[0], stream.getBytes());
                });

                runner.test("with non-empty ByteReadStream", (Test test) ->
                {
                    final InMemoryByteStream stream = InMemoryByteStream.create();
                    final InMemoryByteStream readStream = InMemoryByteStream.create(new byte[] { 0, 1, 2, 3 }).endOfStream();
                    test.assertEqual(4, stream.writeAll(readStream).await());
                    test.assertEqual(new byte[] { 0, 1, 2, 3 }, stream.getBytes());
                });

                runner.test("with read limiter", (Test test) ->
                {
                    final InMemoryByteStream writeStream = InMemoryByteStream.create();
                    final InMemoryByteStream readStream = InMemoryByteStream.create(new byte[] { 0, 1, 2, 3, 4, 5, 6 })
                        .setMaxBytesPerRead(5)
                        .endOfStream();
                    test.assertEqual(7, writeStream.writeAll(readStream).await());
                    test.assertEqual(new byte[] { 0, 1, 2, 3, 4, 5, 6 }, writeStream.getBytes());
                });

                runner.test("with write limiter", (Test test) ->
                {
                    final InMemoryByteStream writeStream = InMemoryByteStream.create()
                        .setMaxBytesPerWrite(2);
                    final InMemoryByteStream readStream = InMemoryByteStream.create(new byte[] { 0, 1, 2, 3, 4, 5, 6 })
                        .endOfStream();
                    test.assertEqual(7, writeStream.writeAll(readStream).await());
                    test.assertEqual(new byte[] { 0, 1, 2, 3, 4, 5, 6 }, writeStream.getBytes());
                });

                runner.test("with read limiter less than write limiter", (Test test) ->
                {
                    final InMemoryByteStream writeStream = InMemoryByteStream.create()
                        .setMaxBytesPerWrite(4);
                    final InMemoryByteStream readStream = InMemoryByteStream.create(new byte[] { 0, 1, 2, 3, 4, 5, 6 })
                        .setMaxBytesPerRead(3)
                        .endOfStream();
                    test.assertEqual(7, writeStream.writeAll(readStream).await());
                    test.assertEqual(new byte[] { 0, 1, 2, 3, 4, 5, 6 }, writeStream.getBytes());
                });

                runner.test("with read limiter equal to write limiter", (Test test) ->
                {
                    final InMemoryByteStream writeStream = InMemoryByteStream.create()
                        .setMaxBytesPerWrite(3);
                    final InMemoryByteStream readStream = InMemoryByteStream.create(new byte[] { 0, 1, 2, 3, 4, 5, 6 })
                        .setMaxBytesPerRead(3)
                        .endOfStream();
                    test.assertEqual(7, writeStream.writeAll(readStream).await());
                    test.assertEqual(new byte[] { 0, 1, 2, 3, 4, 5, 6 }, writeStream.getBytes());
                });

                runner.test("with read limiter greater than write limiter", (Test test) ->
                {
                    final InMemoryByteStream writeStream = InMemoryByteStream.create()
                        .setMaxBytesPerWrite(2);
                    final InMemoryByteStream readStream = InMemoryByteStream.create(new byte[] { 0, 1, 2, 3, 4, 5, 6 })
                        .setMaxBytesPerRead(3)
                        .endOfStream();
                    test.assertEqual(7, writeStream.writeAll(readStream).await());
                    test.assertEqual(new byte[] { 0, 1, 2, 3, 4, 5, 6 }, writeStream.getBytes());
                });
            });
        });
    }

    static InMemoryByteStream create(Test test)
    {
        return create(new byte[0], test);
    }

    static InMemoryByteStream create(byte[] contents, Test test)
    {
        return InMemoryByteStream.create(contents).endOfStream();
    }
}