package qub;

public interface InputStreamToByteReadStreamTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(InputStreamToByteReadStream.class, () ->
        {
            runner.testGroup("constructor(InputStream,AsyncRunner)", () ->
            {
                runner.test("with null InputStream", (Test test) ->
                {
                    test.assertThrows(() -> new InputStreamToByteReadStream(null),
                        new PreConditionFailure("inputStream cannot be null."));
                });

                runner.test("with non-null InputStream", (Test test) ->
                {
                    final InputStreamToByteReadStream byteReadStream = new InputStreamToByteReadStream(getInputStream(5));
                    test.assertFalse(byteReadStream.isDisposed());
                });
            });

            runner.testGroup("readByte()", () ->
            {
                runner.test("when disposed", (Test test) ->
                {
                    final InputStreamToByteReadStream readStream = getByteReadStream(test, 5);
                    test.assertTrue(readStream.dispose().await());
                    test.assertThrows(readStream::readByte,
                        new PreConditionFailure("isDisposed() cannot be true."));
                });

                runner.test("with bytes to read", (Test test) ->
                {
                    final InputStreamToByteReadStream readStream = getByteReadStream(test, 5);
                    test.assertEqual(0, readStream.readByte().await());
                    test.assertEqual(1, readStream.readByte().await());
                    test.assertEqual(2, readStream.readByte().await());
                    test.assertEqual(3, readStream.readByte().await());
                    test.assertEqual(4, readStream.readByte().await());
                    test.assertThrows(() -> readStream.readByte().await(),
                        new EndOfStreamException());
                });

                runner.test("with no bytes to read", (Test test) ->
                {
                    final InputStreamToByteReadStream readStream = getByteReadStream(test, 0);
                    test.assertThrows(() -> readStream.readByte().await(),
                        new EndOfStreamException());
                });

                runner.test("when exception is thrown", (Test test) ->
                {
                    final TestStubInputStream inputStream = new TestStubInputStream();
                    inputStream.setThrowOnRead(true);

                    final InputStreamToByteReadStream byteReadStream = getByteReadStream(test, inputStream);
                    test.assertThrows(() -> byteReadStream.readByte().await(),
                        new RuntimeException(new java.io.IOException()));
                });
            });

            runner.testGroup("readBytes(int)", () ->
            {
                runner.test("when disposed", (Test test) ->
                {
                    final InputStreamToByteReadStream readStream = getByteReadStream(test, 5);
                    test.assertTrue(readStream.dispose().await());
                    test.assertThrows(() -> readStream.readBytes(5),
                        new PreConditionFailure("isDisposed() cannot be true."));
                });

                runner.test("with negative", (Test test) ->
                {
                    final InputStreamToByteReadStream byteReadStream = getByteReadStream(test, 5);
                    test.assertThrows(() -> byteReadStream.readBytes(-1),
                        new PreConditionFailure("bytesToRead (-1) must be greater than or equal to 0."));
                });

                runner.test("with zero", (Test test) ->
                {
                    final InputStreamToByteReadStream byteReadStream = getByteReadStream(test, 5);
                    test.assertEqual(new byte[0], byteReadStream.readBytes(0).await());
                    test.assertEqual(new byte[0], byteReadStream.readBytes(0).await());
                });

                runner.test("with fewer bytesToRead than available", (Test test) ->
                {
                    final InputStreamToByteReadStream byteReadStream = getByteReadStream(test, 5);
                    test.assertEqual(new byte[] { 0, 1, 2 }, byteReadStream.readBytes(3).await());
                    test.assertEqual(new byte[] { 3, 4 }, byteReadStream.readBytes(3).await());
                    test.assertThrows(() -> byteReadStream.readBytes(3).await(),
                        new EndOfStreamException());
                });

                runner.test("with bytesToRead equal to available", (Test test) ->
                {
                    final InputStreamToByteReadStream byteReadStream = getByteReadStream(test, 5);
                    test.assertEqual(new byte[] { 0, 1, 2, 3, 4 }, byteReadStream.readBytes(5).await());
                    test.assertThrows(() -> byteReadStream.readBytes(5).await(),
                        new EndOfStreamException());
                });

                runner.test("with more bytesToRead than available", (Test test) ->
                {
                    final InputStreamToByteReadStream byteReadStream = getByteReadStream(test, 5);
                    test.assertEqual(new byte[] { 0, 1, 2, 3, 4 }, byteReadStream.readBytes(6).await());
                    test.assertThrows(() -> byteReadStream.readBytes(6).await(),
                        new EndOfStreamException());
                });

                runner.test("when exception is thrown", (Test test) ->
                {
                    final TestStubInputStream inputStream = new TestStubInputStream();
                    inputStream.setThrowOnRead(true);

                    final InputStreamToByteReadStream byteReadStream = getByteReadStream(test, inputStream);
                    test.assertThrows(() -> byteReadStream.readBytes(5).await(),
                        new RuntimeException(new java.io.IOException()));
                });
            });

            runner.testGroup("readBytes(byte[])", () ->
            {
                runner.test("when disposed", (Test test) ->
                {
                    final InputStreamToByteReadStream readStream = getByteReadStream(test, 5);
                    test.assertTrue(readStream.dispose().await());
                    final byte[] outputBytes = new byte[10];
                    test.assertThrows(() -> readStream.readBytes(outputBytes),
                        new PreConditionFailure("isDisposed() cannot be true."));
                });

                runner.test("with null", (Test test) ->
                {
                    final InputStreamToByteReadStream byteReadStream = getByteReadStream(test, 5);
                    final byte[] outputBytes = null;
                    test.assertThrows(() -> byteReadStream.readBytes(outputBytes),
                        new PreConditionFailure("outputBytes cannot be null."));
                });

                runner.test("with empty", (Test test) ->
                {
                    final InputStreamToByteReadStream byteReadStream = getByteReadStream(test, 5);
                    final byte[] outputBytes = new byte[0];
                    test.assertEqual(0, byteReadStream.readBytes(outputBytes).await());
                    test.assertEqual(0, byteReadStream.readBytes(outputBytes).await());
                });

                runner.test("with fewer bytesToRead than available", (Test test) ->
                {
                    final InputStreamToByteReadStream byteReadStream = getByteReadStream(test, 5);
                    final byte[] outputBytes = new byte[3];
                    test.assertEqual(3, byteReadStream.readBytes(outputBytes).await());
                    test.assertEqual(new byte[] { 0, 1, 2 }, outputBytes);
                    test.assertEqual(2, byteReadStream.readBytes(outputBytes).await());
                    test.assertEqual(new byte[] { 3, 4, 2 }, outputBytes);
                    test.assertThrows(() -> byteReadStream.readBytes(outputBytes).await(),
                        new EndOfStreamException());
                });

                runner.test("with bytesToRead equal to available", (Test test) ->
                {
                    final InputStreamToByteReadStream byteReadStream = getByteReadStream(test, 5);
                    final byte[] outputBytes = new byte[5];
                    test.assertEqual(5, byteReadStream.readBytes(outputBytes).await());
                    test.assertEqual(new byte[] { 0, 1, 2, 3, 4 }, outputBytes);
                    test.assertThrows(() -> byteReadStream.readBytes(outputBytes).await(),
                        new EndOfStreamException());
                });

                runner.test("with more bytesToRead than available", (Test test) ->
                {
                    final InputStreamToByteReadStream byteReadStream = getByteReadStream(test, 5);
                    final byte[] outputBytes = new byte[6];
                    test.assertEqual(5, byteReadStream.readBytes(outputBytes).await());
                    test.assertEqual(new byte[] { 0, 1, 2, 3, 4, 0 }, outputBytes);
                    test.assertThrows(() -> byteReadStream.readBytes(outputBytes).await(),
                        new EndOfStreamException());
                });

                runner.test("when exception is thrown", (Test test) ->
                {
                    final TestStubInputStream inputStream = new TestStubInputStream();
                    inputStream.setThrowOnRead(true);

                    final byte[] outputBytes = new byte[3];
                    final InputStreamToByteReadStream byteReadStream = getByteReadStream(test, inputStream);
                    test.assertThrows(() -> byteReadStream.readBytes(outputBytes).await(),
                        new RuntimeException(new java.io.IOException()));
                    test.assertEqual(new byte[] { 0, 0, 0 }, outputBytes);
                });
            });

            runner.testGroup("readBytes(byte[],int,int)", () ->
            {
                runner.test("when disposed", (Test test) ->
                {
                    final InputStreamToByteReadStream readStream = getByteReadStream(test, 5);
                    test.assertTrue(readStream.dispose().await());
                    final byte[] outputBytes = new byte[10];
                    test.assertThrows(() -> readStream.readBytes(outputBytes, 0, 1),
                        new PreConditionFailure("isDisposed() cannot be true."));
                });

                runner.test("with null", (Test test) ->
                {
                    final InputStreamToByteReadStream byteReadStream = getByteReadStream(test, 5);
                    final byte[] outputBytes = null;
                    test.assertThrows(() -> byteReadStream.readBytes(outputBytes, 0, 0),
                        new PreConditionFailure("outputBytes cannot be null."));
                });

                runner.test("with empty", (Test test) ->
                {
                    final InputStreamToByteReadStream byteReadStream = getByteReadStream(test, 5);
                    final byte[] outputBytes = new byte[0];
                    test.assertEqual(0, byteReadStream.readBytes(outputBytes, 0, 0).await());
                });

                runner.test("with negative startIndex", (Test test) ->
                {
                    final InputStreamToByteReadStream byteReadStream = getByteReadStream(test, 5);
                    final byte[] outputBytes = new byte[3];
                    test.assertThrows(() -> byteReadStream.readBytes(outputBytes, -1, 1),
                        new PreConditionFailure("startIndex (-1) must be between 0 and 2."));
                });

                runner.test("with too large startIndex", (Test test) ->
                {
                    final InputStreamToByteReadStream byteReadStream = getByteReadStream(test, 5);
                    final byte[] outputBytes = new byte[3];
                    test.assertThrows(() -> byteReadStream.readBytes(outputBytes, 3, 1),
                        new PreConditionFailure("startIndex (3) must be between 0 and 2."));
                });

                runner.test("with negative length", (Test test) ->
                {
                    final InputStreamToByteReadStream byteReadStream = getByteReadStream(test, 5);
                    final byte[] outputBytes = new byte[3];
                    test.assertThrows(() -> byteReadStream.readBytes(outputBytes, 1, -1),
                        new PreConditionFailure("length (-1) must be between 0 and 2."));
                });

                runner.test("with zero length", (Test test) ->
                {
                    final InputStreamToByteReadStream byteReadStream = getByteReadStream(test, 5);
                    final byte[] outputBytes = new byte[3];
                    test.assertEqual(0, byteReadStream.readBytes(outputBytes, 1, 0).await());
                    test.assertEqual(new byte[] { 0, 0, 0 }, outputBytes);
                });

                runner.test("with too large length", (Test test) ->
                {
                    final InputStreamToByteReadStream byteReadStream = getByteReadStream(test, 5);
                    final byte[] outputBytes = new byte[3];
                    test.assertThrows(() -> byteReadStream.readBytes(outputBytes, 1, 3),
                        new PreConditionFailure("length (3) must be between 0 and 2."));
                });

                runner.test("with smaller length than available", (Test test) ->
                {
                    final InputStreamToByteReadStream byteReadStream = getByteReadStream(test, 5);
                    final byte[] outputBytes = new byte[6];
                    test.assertEqual(3, byteReadStream.readBytes(outputBytes, 2, 3).await());
                    test.assertEqual(new byte[] { 0, 0, 0, 1, 2, 0 }, outputBytes);

                    test.assertEqual(2, byteReadStream.readBytes(outputBytes, 0, 2).await());
                    test.assertEqual(new byte[] { 3, 4, 0, 1, 2, 0 }, outputBytes);

                    test.assertThrows(() -> byteReadStream.readBytes(outputBytes, 4, 1).await(),
                        new EndOfStreamException());
                });

                runner.test("with bytesToRead equal to available", (Test test) ->
                {
                    final InputStreamToByteReadStream byteReadStream = getByteReadStream(test, 5);
                    final byte[] outputBytes = new byte[7];
                    test.assertEqual(5, byteReadStream.readBytes(outputBytes, 1, 5).await());
                    test.assertEqual(new byte[] { 0, 0, 1, 2, 3, 4, 0 }, outputBytes);
                    test.assertThrows(() -> byteReadStream.readBytes(outputBytes, 1, 5).await(),
                        new EndOfStreamException());
                });

                runner.test("with more bytesToRead than available", (Test test) ->
                {
                    final InputStreamToByteReadStream byteReadStream = getByteReadStream(test, 5);
                    final byte[] outputBytes = new byte[6];
                    test.assertEqual(5, byteReadStream.readBytes(outputBytes, 0, 6).await());
                    test.assertEqual(new byte[] { 0, 1, 2, 3, 4, 0 }, outputBytes);
                    test.assertThrows(() -> byteReadStream.readBytes(outputBytes, 0, 6).await(),
                        new EndOfStreamException());
                });

                runner.test("when exception is thrown", (Test test) ->
                {
                    final TestStubInputStream inputStream = new TestStubInputStream();
                    inputStream.setThrowOnRead(true);

                    final byte[] outputBytes = new byte[3];
                    final InputStreamToByteReadStream byteReadStream = getByteReadStream(test, inputStream);
                    test.assertThrows(() -> byteReadStream.readBytes(outputBytes, 0, 3).await(),
                        new RuntimeException(new java.io.IOException()));
                    test.assertEqual(new byte[] { 0, 0, 0 }, outputBytes);
                });
            });

            runner.testGroup("readAllBytes()", () ->
            {
                runner.test("when disposed", (Test test) ->
                {
                    final InputStreamToByteReadStream readStream = getByteReadStream(test, 5);
                    test.assertTrue(readStream.dispose().await());
                    test.assertThrows(readStream::readAllBytes,
                        new PreConditionFailure("isDisposed() cannot be true."));
                });

                runner.test("when empty", (Test test) ->
                {
                    final InputStreamToByteReadStream readStream = getByteReadStream(test, 0);
                    test.assertThrows(() -> readStream.readAllBytes().await(),
                        new EndOfStreamException());
                });

                runner.test("when not empty", (Test test) ->
                {
                    final InputStreamToByteReadStream readStream = getByteReadStream(test, 5);
                    test.assertEqual(new byte[] { 0, 1, 2, 3, 4 }, readStream.readAllBytes().await());
                    test.assertThrows(() -> readStream.readAllBytes().await(),
                        new EndOfStreamException());
                });
            });

            runner.testGroup("readBytesUntil(byte)", () ->
            {
                runner.test("when disposed", (Test test) ->
                {
                    final InputStreamToByteReadStream readStream = getByteReadStream(test, 5);
                    test.assertTrue(readStream.dispose().await());
                    test.assertThrows(() -> readStream.readBytesUntil((byte)3),
                        new PreConditionFailure("isDisposed() cannot be true."));
                });

                runner.test("when empty", (Test test) ->
                {
                    final InputStreamToByteReadStream readStream = getByteReadStream(test, 0);
                    test.assertThrows(() -> readStream.readBytesUntil((byte)5).await(),
                        new EndOfStreamException());
                });

                runner.test("with no match", (Test test) ->
                {
                    final InputStreamToByteReadStream readStream = getByteReadStream(test, 5);
                    test.assertEqual(new byte[] { 0, 1, 2, 3, 4 }, readStream.readBytesUntil((byte)20).await());
                    test.assertThrows(() -> readStream.readBytesUntil((byte)20).await(),
                        new EndOfStreamException());
                });

                runner.test("with match", (Test test) ->
                {
                    final InputStreamToByteReadStream readStream = getByteReadStream(test, 5);
                    test.assertEqual(new byte[] { 0, 1 }, readStream.readBytesUntil((byte)1).await());
                    test.assertEqual(new byte[] { 2, 3, 4 }, readStream.readBytesUntil((byte)1).await());
                    test.assertThrows(() -> readStream.readBytesUntil((byte)1).await(),
                        new EndOfStreamException());
                });
            });

            runner.testGroup("readBytesUntil(byte[])", () ->
            {
                runner.test("when disposed", (Test test) ->
                {
                    final InputStreamToByteReadStream readStream = getByteReadStream(test, 5);
                    test.assertTrue(readStream.dispose().await());
                    test.assertThrows(() -> readStream.readBytesUntil(new byte[] { 3 }),
                        new PreConditionFailure("isDisposed() cannot be true."));
                });

                runner.test("when empty", (Test test) ->
                {
                    final InputStreamToByteReadStream readStream = getByteReadStream(test, 0);
                    test.assertThrows(() -> readStream.readBytesUntil(new byte[] { 5 }).await(),
                        new EndOfStreamException());
                });

                runner.test("with null", (Test test) ->
                {
                    final InputStreamToByteReadStream readStream = getByteReadStream(test, 0);
                    test.assertThrows(() -> readStream.readBytesUntil((byte[])null),
                        new PreConditionFailure("stopBytes cannot be null."));
                });

                runner.test("with empty", (Test test) ->
                {
                    final InputStreamToByteReadStream readStream = getByteReadStream(test, 0);
                    test.assertThrows(() -> readStream.readBytesUntil(new byte[0]),
                        new PreConditionFailure("stopBytes cannot be empty."));
                });

                runner.test("with no match", (Test test) ->
                {
                    final InputStreamToByteReadStream readStream = getByteReadStream(test, 5);
                    test.assertEqual(new byte[] { 0, 1, 2, 3, 4 }, readStream.readBytesUntil(new byte[] { 20 }).await());
                    test.assertThrows(() -> readStream.readBytesUntil(new byte[] { 20 }).await(),
                        new EndOfStreamException());
                });

                runner.test("with match", (Test test) ->
                {
                    final InputStreamToByteReadStream readStream = getByteReadStream(test, 5);
                    test.assertEqual(new byte[] { 0, 1 }, readStream.readBytesUntil(new byte[] { 1 }).await());
                    test.assertEqual(new byte[] { 2, 3, 4 }, readStream.readBytesUntil(new byte[] { 1 }).await());
                    test.assertThrows(() -> readStream.readBytesUntil(new byte[] { 1 }).await(),
                        new EndOfStreamException());
                });

                runner.test("with partial match", (Test test) ->
                {
                    final InputStreamToByteReadStream readStream = getByteReadStream(test, 5);
                    test.assertEqual(new byte[] { 0, 1, 2, 3, 4 }, readStream.readBytesUntil(new byte[] { 1, 3 }).await());
                    test.assertThrows(() -> readStream.readBytesUntil(new byte[] { 1, 3 }).await(),
                        new EndOfStreamException());
                });
            });

            runner.testGroup("readBytesUntil(Iterable<Byte>)", () ->
            {
                runner.test("when disposed", (Test test) ->
                {
                    final InputStreamToByteReadStream readStream = getByteReadStream(test, 5);
                    test.assertTrue(readStream.dispose().await());
                    test.assertThrows(() -> readStream.readBytesUntil(ByteArray.create(3)),
                        new PreConditionFailure("isDisposed() cannot be true."));
                });

                runner.test("when empty", (Test test) ->
                {
                    final InputStreamToByteReadStream readStream = getByteReadStream(test, 0);
                    test.assertThrows(() -> readStream.readBytesUntil(ByteArray.create(5)).await(),
                        new EndOfStreamException());
                });

                runner.test("with null", (Test test) ->
                {
                    final InputStreamToByteReadStream readStream = getByteReadStream(test, 0);
                    test.assertThrows(() -> readStream.readBytesUntil((Iterable<Byte>)null),
                        new PreConditionFailure("stopBytes cannot be null."));
                });

                runner.test("with empty", (Test test) ->
                {
                    final InputStreamToByteReadStream readStream = getByteReadStream(test, 0);
                    test.assertThrows(() -> readStream.readBytesUntil(ByteArray.create()),
                        new PreConditionFailure("stopBytes cannot be empty."));
                });

                runner.test("with no match", (Test test) ->
                {
                    final InputStreamToByteReadStream readStream = getByteReadStream(test, 5);
                    test.assertEqual(new byte[] { 0, 1, 2, 3, 4 }, readStream.readBytesUntil(ByteArray.create(20)).await());
                    test.assertThrows(() -> readStream.readBytesUntil(ByteArray.create(20)).await(),
                        new EndOfStreamException());
                });

                runner.test("with match", (Test test) ->
                {
                    final InputStreamToByteReadStream readStream = getByteReadStream(test, 5);
                    test.assertEqual(new byte[] { 0, 1 }, readStream.readBytesUntil(ByteArray.create(1)).await());
                    test.assertEqual(new byte[] { 2, 3, 4 }, readStream.readBytesUntil(ByteArray.create(1)).await());
                    test.assertThrows(() -> readStream.readBytesUntil(ByteArray.create(1)).await(),
                        new EndOfStreamException());
                });

                runner.test("with partial match", (Test test) ->
                {
                    final InputStreamToByteReadStream readStream = getByteReadStream(test, 5);
                    test.assertEqual(new byte[] { 0, 1, 2, 3, 4 }, readStream.readBytesUntil(ByteArray.create(1, 3)).await());
                });
            });
        });
    }

    static java.io.ByteArrayInputStream getInputStream(int byteCount)
    {
        final byte[] bytes = new byte[byteCount];
        for (int i = 0; i < byteCount; ++i)
        {
            bytes[i] = (byte)i;
        }
        return new java.io.ByteArrayInputStream(bytes);
    }

    static InputStreamToByteReadStream getByteReadStream(Test test, int byteCount)
    {
        return getByteReadStream(test, getInputStream(byteCount));
    }

    static InputStreamToByteReadStream getByteReadStream(Test test, java.io.InputStream inputStream)
    {
        return new InputStreamToByteReadStream(inputStream);
    }
}
