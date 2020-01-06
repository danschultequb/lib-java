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
                    assertByteReadStream(test, byteReadStream, false, false, null);
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
                    assertByteReadStream(test, readStream, true, false, null);
                });

                runner.test("with bytes to read", (Test test) ->
                {
                    final InputStreamToByteReadStream readStream = getByteReadStream(test, 5);
                    test.assertEqual(0, readStream.readByte().await());
                    assertByteReadStream(test, readStream, false, true, 0);
                });

                runner.test("with no bytes to read", (Test test) ->
                {
                    final InputStreamToByteReadStream readStream = getByteReadStream(test, 0);
                    test.assertThrows(() -> readStream.readByte().await(),
                        new EndOfStreamException());
                    assertByteReadStream(test, readStream, false, true, null);
                });

                runner.test("when exception is thrown", (Test test) ->
                {
                    final TestStubInputStream inputStream = new TestStubInputStream();
                    inputStream.setThrowOnRead(true);

                    final InputStreamToByteReadStream byteReadStream = getByteReadStream(test, inputStream);
                    test.assertThrows(() -> byteReadStream.readByte().await(),
                        new RuntimeException(new java.io.IOException()));
                    assertByteReadStream(test, byteReadStream, false, true, null);
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
                    assertByteReadStream(test, readStream, true, false, null);
                });

                runner.test("with negative", (Test test) ->
                {
                    final InputStreamToByteReadStream byteReadStream = getByteReadStream(test, 5);
                    test.assertThrows(() -> byteReadStream.readBytes(-1),
                        new PreConditionFailure("bytesToRead (-1) must be greater than or equal to 1."));
                    assertByteReadStream(test, byteReadStream, false, false, null);
                });

                runner.test("with zero", (Test test) ->
                {
                    final InputStreamToByteReadStream byteReadStream = getByteReadStream(test, 5);
                    test.assertThrows(() -> byteReadStream.readBytes(0),
                        new PreConditionFailure("bytesToRead (0) must be greater than or equal to 1."));
                    assertByteReadStream(test, byteReadStream, false, false, null);
                });

                runner.test("with fewer bytesToRead than available", (Test test) ->
                {
                    final InputStreamToByteReadStream byteReadStream = getByteReadStream(test, 5);
                    test.assertEqual(new byte[] { 0, 1, 2 }, byteReadStream.readBytes(3).await());
                    assertByteReadStream(test, byteReadStream, false, true, 2);
                });

                runner.test("with bytesToRead equal to available", (Test test) ->
                {
                    final InputStreamToByteReadStream byteReadStream = getByteReadStream(test, 5);
                    test.assertEqual(new byte[] { 0, 1, 2, 3, 4 }, byteReadStream.readBytes(5).await());
                    assertByteReadStream(test, byteReadStream, false, true, 4);
                });

                runner.test("with more bytesToRead than available", (Test test) ->
                {
                    final InputStreamToByteReadStream byteReadStream = getByteReadStream(test, 5);
                    test.assertEqual(new byte[] { 0, 1, 2, 3, 4 }, byteReadStream.readBytes(6).await());
                    assertByteReadStream(test, byteReadStream, false, true, 4);
                });

                runner.test("when exception is thrown", (Test test) ->
                {
                    final TestStubInputStream inputStream = new TestStubInputStream();
                    inputStream.setThrowOnRead(true);

                    final InputStreamToByteReadStream byteReadStream = getByteReadStream(test, inputStream);
                    test.assertThrows(() -> byteReadStream.readBytes(5).await(),
                        new RuntimeException(new java.io.IOException()));
                    assertByteReadStream(test, byteReadStream, false, true, null);
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
                    assertByteReadStream(test, readStream, true, false, null);
                });

                runner.test("with null", (Test test) ->
                {
                    final InputStreamToByteReadStream byteReadStream = getByteReadStream(test, 5);
                    final byte[] outputBytes = null;
                    test.assertThrows(() -> byteReadStream.readBytes(outputBytes),
                        new PreConditionFailure("outputBytes cannot be null."));
                    assertByteReadStream(test, byteReadStream, false, false, null);
                });

                runner.test("with empty", (Test test) ->
                {
                    final InputStreamToByteReadStream byteReadStream = getByteReadStream(test, 5);
                    final byte[] outputBytes = new byte[0];
                    test.assertThrows(() -> byteReadStream.readBytes(outputBytes),
                        new PreConditionFailure("outputBytes cannot be empty."));
                    assertByteReadStream(test, byteReadStream, false, false, null);
                });

                runner.test("with fewer bytesToRead than available", (Test test) ->
                {
                    final InputStreamToByteReadStream byteReadStream = getByteReadStream(test, 5);
                    final byte[] outputBytes = new byte[3];
                    test.assertEqual(3, byteReadStream.readBytes(outputBytes).await());
                    assertByteReadStream(test, byteReadStream, false, true, 2);
                    test.assertEqual(new byte[] { 0, 1, 2 }, outputBytes);
                });

                runner.test("with bytesToRead equal to available", (Test test) ->
                {
                    final InputStreamToByteReadStream byteReadStream = getByteReadStream(test, 5);
                    final byte[] outputBytes = new byte[5];
                    test.assertEqual(5, byteReadStream.readBytes(outputBytes).await());
                    assertByteReadStream(test, byteReadStream, false, true, 4);
                    test.assertEqual(new byte[] { 0, 1, 2, 3, 4 }, outputBytes);
                });

                runner.test("with more bytesToRead than available", (Test test) ->
                {
                    final InputStreamToByteReadStream byteReadStream = getByteReadStream(test, 5);
                    final byte[] outputBytes = new byte[6];
                    test.assertEqual(5, byteReadStream.readBytes(outputBytes).await());
                    assertByteReadStream(test, byteReadStream, false, true, 4);
                    test.assertEqual(new byte[] { 0, 1, 2, 3, 4, 0 }, outputBytes);
                });

                runner.test("when exception is thrown", (Test test) ->
                {
                    final TestStubInputStream inputStream = new TestStubInputStream();
                    inputStream.setThrowOnRead(true);

                    final byte[] outputBytes = new byte[3];
                    final InputStreamToByteReadStream byteReadStream = getByteReadStream(test, inputStream);
                    test.assertThrows(() -> byteReadStream.readBytes(outputBytes).await(),
                        new RuntimeException(new java.io.IOException()));
                    assertByteReadStream(test, byteReadStream, false, true, null);
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
                    assertByteReadStream(test, readStream, true, false, null);
                });

                runner.test("with null", (Test test) ->
                {
                    final InputStreamToByteReadStream byteReadStream = getByteReadStream(test, 5);
                    final byte[] outputBytes = null;
                    test.assertThrows(() -> byteReadStream.readBytes(outputBytes, 0, 1),
                        new PreConditionFailure("outputBytes cannot be null."));
                    assertByteReadStream(test, byteReadStream, false, false, null);
                });

                runner.test("with empty", (Test test) ->
                {
                    final InputStreamToByteReadStream byteReadStream = getByteReadStream(test, 5);
                    final byte[] outputBytes = new byte[0];
                    test.assertThrows(() -> byteReadStream.readBytes(outputBytes, 0, 1),
                        new PreConditionFailure("outputBytes cannot be empty."));
                    assertByteReadStream(test, byteReadStream, false, false, null);
                });

                runner.test("with negative startIndex", (Test test) ->
                {
                    final InputStreamToByteReadStream byteReadStream = getByteReadStream(test, 5);
                    final byte[] outputBytes = new byte[3];
                    test.assertThrows(() -> byteReadStream.readBytes(outputBytes, -1, 1),
                        new PreConditionFailure("startIndex (-1) must be between 0 and 2."));
                    assertByteReadStream(test, byteReadStream, false, false, null);
                });

                runner.test("with too large startIndex", (Test test) ->
                {
                    final InputStreamToByteReadStream byteReadStream = getByteReadStream(test, 5);
                    final byte[] outputBytes = new byte[3];
                    test.assertThrows(() -> byteReadStream.readBytes(outputBytes, 3, 1),
                        new PreConditionFailure("startIndex (3) must be between 0 and 2."));
                    assertByteReadStream(test, byteReadStream, false, false, null);
                });

                runner.test("with negative length", (Test test) ->
                {
                    final InputStreamToByteReadStream byteReadStream = getByteReadStream(test, 5);
                    final byte[] outputBytes = new byte[3];
                    test.assertThrows(() -> byteReadStream.readBytes(outputBytes, 1, -1),
                        new PreConditionFailure("length (-1) must be between 1 and 2."));
                    assertByteReadStream(test, byteReadStream, false, false, null);
                });

                runner.test("with zero length", (Test test) ->
                {
                    final InputStreamToByteReadStream byteReadStream = getByteReadStream(test, 5);
                    final byte[] outputBytes = new byte[3];
                    test.assertThrows(() -> byteReadStream.readBytes(outputBytes, 1, 0),
                        new PreConditionFailure("length (0) must be between 1 and 2."));
                    assertByteReadStream(test, byteReadStream, false, false, null);
                });

                runner.test("with too large length", (Test test) ->
                {
                    final InputStreamToByteReadStream byteReadStream = getByteReadStream(test, 5);
                    final byte[] outputBytes = new byte[3];
                    test.assertThrows(() -> byteReadStream.readBytes(outputBytes, 1, 3),
                        new PreConditionFailure("length (3) must be between 1 and 2."));
                    assertByteReadStream(test, byteReadStream, false, false, null);
                });

                runner.test("with smaller length than available", (Test test) ->
                {
                    final InputStreamToByteReadStream byteReadStream = getByteReadStream(test, 5);
                    final byte[] outputBytes = new byte[6];
                    test.assertEqual(3, byteReadStream.readBytes(outputBytes, 2, 3).await());
                    assertByteReadStream(test, byteReadStream, false, true, 2);
                    test.assertEqual(new byte[] { 0, 0, 0, 1, 2, 0 }, outputBytes);
                });

                runner.test("with bytesToRead equal to available", (Test test) ->
                {
                    final InputStreamToByteReadStream byteReadStream = getByteReadStream(test, 5);
                    final byte[] outputBytes = new byte[7];
                    test.assertEqual(5, byteReadStream.readBytes(outputBytes, 1, 5).await());
                    assertByteReadStream(test, byteReadStream, false, true, 4);
                    test.assertEqual(new byte[] { 0, 0, 1, 2, 3, 4, 0 }, outputBytes);
                });

                runner.test("with more bytesToRead than available", (Test test) ->
                {
                    final InputStreamToByteReadStream byteReadStream = getByteReadStream(test, 5);
                    final byte[] outputBytes = new byte[6];
                    test.assertEqual(5, byteReadStream.readBytes(outputBytes, 0, 6).await());
                    assertByteReadStream(test, byteReadStream, false, true, 4);
                    test.assertEqual(new byte[] { 0, 1, 2, 3, 4, 0 }, outputBytes);
                });

                runner.test("when exception is thrown", (Test test) ->
                {
                    final TestStubInputStream inputStream = new TestStubInputStream();
                    inputStream.setThrowOnRead(true);

                    final byte[] outputBytes = new byte[3];
                    final InputStreamToByteReadStream byteReadStream = getByteReadStream(test, inputStream);
                    test.assertThrows(() -> byteReadStream.readBytes(outputBytes, 0, 3).await(),
                        new RuntimeException(new java.io.IOException()));
                    assertByteReadStream(test, byteReadStream, false, true, null);
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
                    assertByteReadStream(test, readStream, true, false, null);
                });

                runner.test("when empty", (Test test) ->
                {
                    final InputStreamToByteReadStream readStream = getByteReadStream(test, 0);
                    test.assertThrows(() -> readStream.readAllBytes().await(),
                        new EndOfStreamException());
                    assertByteReadStream(test, readStream, false, true, null);
                });

                runner.test("when not empty", (Test test) ->
                {
                    final InputStreamToByteReadStream readStream = getByteReadStream(test, 5);
                    test.assertEqual(new byte[] { 0, 1, 2, 3, 4 }, readStream.readAllBytes().await());
                    assertByteReadStream(test, readStream, false, true, null);
                    test.assertThrows(() -> readStream.readAllBytes().await(),
                        new EndOfStreamException());
                    assertByteReadStream(test, readStream, false, true, null);
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
                    assertByteReadStream(test, readStream, true, false, null);
                });

                runner.test("when empty", (Test test) ->
                {
                    final InputStreamToByteReadStream readStream = getByteReadStream(test, 0);
                    test.assertThrows(() -> readStream.readBytesUntil((byte)5).await(),
                        new EndOfStreamException());
                    assertByteReadStream(test, readStream, false, true, null);
                });

                runner.test("with no match", (Test test) ->
                {
                    final InputStreamToByteReadStream readStream = getByteReadStream(test, 5);
                    test.assertEqual(new byte[] { 0, 1, 2, 3, 4 }, readStream.readBytesUntil((byte)20).await());
                    assertByteReadStream(test, readStream, false, true, null);
                });

                runner.test("with match", (Test test) ->
                {
                    final InputStreamToByteReadStream readStream = getByteReadStream(test, 5);
                    test.assertEqual(new byte[] { 0, 1 }, readStream.readBytesUntil((byte)1).await());
                    assertByteReadStream(test, readStream, false, true, 1);

                    test.assertEqual(new byte[] { 2, 3, 4 }, readStream.readBytesUntil((byte)1).await());
                    assertByteReadStream(test, readStream, false, true, null);
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
                    assertByteReadStream(test, readStream, true, false, null);
                });

                runner.test("when empty", (Test test) ->
                {
                    final InputStreamToByteReadStream readStream = getByteReadStream(test, 0);
                    test.assertThrows(() -> readStream.readBytesUntil(new byte[] { 5 }).await(),
                        new EndOfStreamException());
                    assertByteReadStream(test, readStream, false, true, null);
                });

                runner.test("with null", (Test test) ->
                {
                    final InputStreamToByteReadStream readStream = getByteReadStream(test, 0);
                    test.assertThrows(() -> readStream.readBytesUntil((byte[])null),
                        new PreConditionFailure("stopBytes cannot be null."));
                    assertByteReadStream(test, readStream, false, false, null);
                });

                runner.test("with empty", (Test test) ->
                {
                    final InputStreamToByteReadStream readStream = getByteReadStream(test, 0);
                    test.assertThrows(() -> readStream.readBytesUntil(new byte[0]),
                        new PreConditionFailure("stopBytes cannot be empty."));
                    assertByteReadStream(test, readStream, false, false, null);
                });

                runner.test("with no match", (Test test) ->
                {
                    final InputStreamToByteReadStream readStream = getByteReadStream(test, 5);
                    test.assertEqual(new byte[] { 0, 1, 2, 3, 4 }, readStream.readBytesUntil(new byte[] { 20 }).await());
                    assertByteReadStream(test, readStream, false, true, null);
                });

                runner.test("with match", (Test test) ->
                {
                    final InputStreamToByteReadStream readStream = getByteReadStream(test, 5);
                    test.assertEqual(new byte[] { 0, 1 }, readStream.readBytesUntil(new byte[] { 1 }).await());
                    assertByteReadStream(test, readStream, false, true, 1);

                    test.assertEqual(new byte[] { 2, 3, 4 }, readStream.readBytesUntil(new byte[] { 1 }).await());
                    assertByteReadStream(test, readStream, false, true, null);
                });

                runner.test("with partial match", (Test test) ->
                {
                    final InputStreamToByteReadStream readStream = getByteReadStream(test, 5);
                    test.assertEqual(new byte[] { 0, 1, 2, 3, 4 }, readStream.readBytesUntil(new byte[] { 1, 3 }).await());
                    assertByteReadStream(test, readStream, false, true, null);
                });
            });

            runner.testGroup("readBytesUntil(Iterable<Byte>)", () ->
            {
                runner.test("when disposed", (Test test) ->
                {
                    final InputStreamToByteReadStream readStream = getByteReadStream(test, 5);
                    test.assertTrue(readStream.dispose().await());
                    test.assertThrows(() -> readStream.readBytesUntil(ByteArray.create(3).await()),
                        new PreConditionFailure("isDisposed() cannot be true."));
                    assertByteReadStream(test, readStream, true, false, null);
                });

                runner.test("when empty", (Test test) ->
                {
                    final InputStreamToByteReadStream readStream = getByteReadStream(test, 0);
                    test.assertThrows(() -> readStream.readBytesUntil(ByteArray.create(5).await()).await(),
                        new EndOfStreamException());
                    assertByteReadStream(test, readStream, false, true, null);
                });

                runner.test("with null", (Test test) ->
                {
                    final InputStreamToByteReadStream readStream = getByteReadStream(test, 0);
                    test.assertThrows(() -> readStream.readBytesUntil((Iterable<Byte>)null),
                        new PreConditionFailure("stopBytes cannot be null."));
                    assertByteReadStream(test, readStream, false, false, null);
                });

                runner.test("with empty", (Test test) ->
                {
                    final InputStreamToByteReadStream readStream = getByteReadStream(test, 0);
                    test.assertThrows(() -> readStream.readBytesUntil(ByteArray.create()),
                        new PreConditionFailure("stopBytes cannot be empty."));
                    assertByteReadStream(test, readStream, false, false, null);
                });

                runner.test("with no match", (Test test) ->
                {
                    final InputStreamToByteReadStream readStream = getByteReadStream(test, 5);
                    test.assertEqual(new byte[] { 0, 1, 2, 3, 4 }, readStream.readBytesUntil(ByteArray.create(20).await()).await());
                    assertByteReadStream(test, readStream, false, true, null);
                });

                runner.test("with match", (Test test) ->
                {
                    final InputStreamToByteReadStream readStream = getByteReadStream(test, 5);
                    test.assertEqual(new byte[] { 0, 1 }, readStream.readBytesUntil(ByteArray.create(1).await()).await());
                    assertByteReadStream(test, readStream, false, true, 1);

                    test.assertEqual(new byte[] { 2, 3, 4 }, readStream.readBytesUntil(ByteArray.create(1).await()).await());
                    assertByteReadStream(test, readStream, false, true, null);
                });

                runner.test("with partial match", (Test test) ->
                {
                    final InputStreamToByteReadStream readStream = getByteReadStream(test, 5);
                    test.assertEqual(new byte[] { 0, 1, 2, 3, 4 }, readStream.readBytesUntil(ByteArray.create(1, 3).await()).await());
                    assertByteReadStream(test, readStream, false, true, null);
                });
            });

            runner.testGroup("next()", () ->
            {
                runner.test("when disposed", (Test test) ->
                {
                    final InputStreamToByteReadStream readStream = getByteReadStream(test, 5);
                    test.assertTrue(readStream.dispose().await());
                    test.assertThrows(readStream::next,
                        new PreConditionFailure("isDisposed() cannot be true."));
                    assertByteReadStream(test, readStream, true, false, null);
                });

                runner.test("with bytes to read", (Test test) ->
                {
                    final InputStreamToByteReadStream readStream = getByteReadStream(test, 5);
                    test.assertEqual(true, readStream.next());
                    assertByteReadStream(test, readStream, false, true, 0);
                });

                runner.test("with no bytes to read", (Test test) ->
                {
                    final InputStreamToByteReadStream readStream = getByteReadStream(test, 0);
                    test.assertFalse(readStream.next());
                    assertByteReadStream(test, readStream, false, true, null);
                });

                runner.test("when exception is thrown", (Test test) ->
                {
                    final TestStubInputStream inputStream = new TestStubInputStream();
                    inputStream.setThrowOnRead(true);

                    final InputStreamToByteReadStream byteReadStream = getByteReadStream(test, inputStream);
                    test.assertThrows(byteReadStream::next,
                        new RuntimeException(new java.io.IOException()));
                    assertByteReadStream(test, byteReadStream, false, true, null);
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

    static void assertByteReadStream(Test test, ByteReadStream byteReadStream, boolean isDisposed, boolean hasStarted, int current)
    {
        PreCondition.assertByte(current, "current");

        assertByteReadStream(test, byteReadStream, isDisposed, hasStarted, Byte.valueOf((byte)current));
    }

    static void assertByteReadStream(Test test, ByteReadStream byteReadStream, boolean isDisposed, boolean hasStarted, Byte current)
    {
        test.assertNotNull(byteReadStream);
        test.assertEqual(isDisposed, byteReadStream.isDisposed());
        test.assertEqual(hasStarted, byteReadStream.hasStarted());
        test.assertEqual(current != null, byteReadStream.hasCurrent());
        test.assertEqual(current, byteReadStream.getCurrent());
    }
}
