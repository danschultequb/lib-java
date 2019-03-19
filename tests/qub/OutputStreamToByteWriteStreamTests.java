package qub;

public class OutputStreamToByteWriteStreamTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(OutputStreamToByteWriteStream.class, () ->
        {
            runner.test("constructor()", test ->
            {
                final OutputStreamToByteWriteStream writeStream = new OutputStreamToByteWriteStream(getOutputStream());
                test.assertFalse(writeStream.isDisposed());
            });

            runner.testGroup("close()", () ->
            {
                runner.test("with no exception", (Test test) ->
                {
                    final OutputStreamToByteWriteStream writeStream = getWriteStream(new java.io.ByteArrayOutputStream());
                    writeStream.close();
                    test.assertTrue(writeStream.isDisposed());
                });

                runner.test("with exception", (Test test) ->
                {
                    final OutputStreamToByteWriteStream writeStream = getWriteStream(new TestStubOutputStream());
                    test.assertThrows(writeStream::close,
                        new RuntimeException(new java.io.IOException()));
                    test.assertTrue(writeStream.isDisposed());
                });

                runner.test("when already disposed", (Test test) ->
                {
                    final OutputStreamToByteWriteStream writeStream = getWriteStream(new java.io.ByteArrayOutputStream());
                    test.assertTrue(writeStream.dispose().await());
                    test.assertTrue(writeStream.isDisposed());

                    writeStream.close();
                    test.assertTrue(writeStream.isDisposed());
                });
            });

            runner.testGroup("writeByte(byte)", () ->
            {
                final Action3<java.io.OutputStream,Byte,Boolean> writeByteTest = (java.io.OutputStream outputStream, Byte toWrite, Boolean expectedWriteResult) ->
                {
                    runner.test("with " + outputStream.getClass().getSimpleName() + " and " + toWrite, test ->
                    {
                        final OutputStreamToByteWriteStream writeStream = getWriteStream(outputStream);
                        final Result<Integer> writeResult = writeStream.writeByte(toWrite);
                        if (expectedWriteResult)
                        {
                            test.assertEqual(1, writeResult.awaitError());
                        }
                        else
                        {
                            test.assertThrows(writeResult::awaitError,
                                new RuntimeException(new java.io.IOException()));
                        }

                        if (outputStream instanceof java.io.ByteArrayOutputStream)
                        {
                            final java.io.ByteArrayOutputStream byteArrayOutputStream = (java.io.ByteArrayOutputStream)outputStream;
                            test.assertEqual(new byte[] { toWrite }, byteArrayOutputStream.toByteArray());
                        }
                    });
                };

                writeByteTest.run(new java.io.ByteArrayOutputStream(), (byte)10, true);
                writeByteTest.run(new TestStubOutputStream(), (byte)20, false);
            });

            runner.testGroup("writeByte(byte[])", () ->
            {
                final Action4<java.io.OutputStream,byte[],Integer,Throwable> writeByteArrayTest = (java.io.OutputStream outputStream, byte[] toWrite, Integer expectedWriteResult, Throwable expectedError) ->
                {
                    runner.test("with " + outputStream.getClass().getSimpleName() + " and " + Array.toString(toWrite), test ->
                    {
                        final OutputStreamToByteWriteStream writeStream = getWriteStream(outputStream);

                        if (toWrite == null || toWrite.length == 0)
                        {
                            test.assertThrows(() -> writeStream.writeBytes(toWrite));
                        }
                        else
                        {
                            final Result<Integer> writeResult = writeStream.writeBytes(toWrite);
                            if (expectedError != null)
                            {
                                test.assertThrows(writeResult::awaitError, expectedError);
                            }
                            else
                            {
                                test.assertEqual(expectedWriteResult, writeResult.awaitError());
                            }

                            if (outputStream instanceof java.io.ByteArrayOutputStream)
                            {
                                final java.io.ByteArrayOutputStream byteArrayOutputStream = (java.io.ByteArrayOutputStream)outputStream;
                                test.assertEqual(toWrite, byteArrayOutputStream.toByteArray());
                            }
                        }
                    });
                };

                writeByteArrayTest.run(new java.io.ByteArrayOutputStream(), null, null, new IllegalArgumentException("toWrite cannot be null."));
                writeByteArrayTest.run(new TestStubOutputStream(), null, null, new IllegalArgumentException("toWrite cannot be null."));
                writeByteArrayTest.run(new java.io.ByteArrayOutputStream(), new byte[0], null, new IllegalArgumentException("toWrite.length (0) must be greater than 0."));
                writeByteArrayTest.run(new TestStubOutputStream(), new byte[0], null, new IllegalArgumentException("toWrite.length (0) must be greater than 0."));
                writeByteArrayTest.run(new java.io.ByteArrayOutputStream(), new byte[] { 0, 1, 2, 3 }, 4, null);
                writeByteArrayTest.run(new TestStubOutputStream(), new byte[] { 0, 1, 2, 3 }, null, new RuntimeException(new java.io.IOException()));
            });

            runner.testGroup("writeByte(byte[],int,int)", () ->
            {
                final Action6<java.io.OutputStream,byte[],Integer,Integer,Integer,Throwable> writeByteArrayStartIndexAndLengthTest = (java.io.OutputStream outputStream, byte[] toWrite, Integer startIndex, Integer length, Integer expectedWriteResult, Throwable expectedError) ->
                {
                    runner.test("with " + Types.getTypeName(outputStream) + ", " + Array.toString(toWrite) + ", " + startIndex + ", and " + length, test ->
                    {
                        final OutputStreamToByteWriteStream writeStream = getWriteStream(outputStream);

                        if (expectedError == null)
                        {
                            test.assertEqual(expectedWriteResult, writeStream.writeBytes(toWrite, startIndex, length).awaitError());
                        }
                        else
                        {
                            test.assertThrows(() -> writeStream.writeBytes(toWrite, startIndex, length).awaitError(), expectedError);
                        }

                        if (outputStream instanceof java.io.ByteArrayOutputStream)
                        {
                            final java.io.ByteArrayOutputStream byteArrayOutputStream = (java.io.ByteArrayOutputStream)outputStream;
                            final byte[] expectedWrittenBytes = length <= 0 ? new byte[0] : Array.clone(toWrite, startIndex, length);
                            test.assertEqual(expectedWrittenBytes, byteArrayOutputStream.toByteArray());
                        }
                    });
                };

                writeByteArrayStartIndexAndLengthTest.run(new java.io.ByteArrayOutputStream(), null, 0, 0, null, new PreConditionFailure("toWrite cannot be null."));
                writeByteArrayStartIndexAndLengthTest.run(new TestStubOutputStream(), null, 0, 0, null, new PreConditionFailure("toWrite cannot be null."));
                writeByteArrayStartIndexAndLengthTest.run(new java.io.ByteArrayOutputStream(), new byte[0], 0, 0, null, new PreConditionFailure("toWrite cannot be empty."));
                writeByteArrayStartIndexAndLengthTest.run(new TestStubOutputStream(), new byte[0], 0, 0, null, new PreConditionFailure("toWrite cannot be empty."));
                writeByteArrayStartIndexAndLengthTest.run(new java.io.ByteArrayOutputStream(), new byte[] { 0, 1, 2 }, 0, 0, 0, new PreConditionFailure("length (0) must be between 1 and 3."));
                writeByteArrayStartIndexAndLengthTest.run(new TestStubOutputStream(), new byte[] { 0, 1, 2 }, 0, 0, null, new PreConditionFailure("length (0) must be between 1 and 3."));
                writeByteArrayStartIndexAndLengthTest.run(new java.io.ByteArrayOutputStream(), new byte[] { 0, 1, 2 }, 1, 1, 1, null);
                writeByteArrayStartIndexAndLengthTest.run(new TestStubOutputStream(), new byte[] { 0, 1, 2 }, 1, 1, null, new RuntimeException(new java.io.IOException()));
            });

            runner.test("asCharacterWriteStream()", test ->
            {
                final java.io.ByteArrayOutputStream outputStream = getOutputStream();
                final OutputStreamToByteWriteStream writeStream = getWriteStream(outputStream);

                final CharacterWriteStream characterWriteStream = writeStream.asCharacterWriteStream();
                characterWriteStream.write("abc");

                test.assertEqual(new byte[] { 97, 98, 99 }, outputStream.toByteArray());
            });

            runner.testGroup("asCharacterWriteStream(CharacterEncoding)", () ->
            {
                runner.test("with null encoding", test ->
                {
                    final java.io.ByteArrayOutputStream outputStream = getOutputStream();
                    final OutputStreamToByteWriteStream writeStream = getWriteStream(outputStream);

                    test.assertThrows(() -> writeStream.asCharacterWriteStream((CharacterEncoding)null));
                });
            });
        });
    }

    private static java.io.ByteArrayOutputStream getOutputStream()
    {
        return new java.io.ByteArrayOutputStream();
    }

    private static OutputStreamToByteWriteStream getWriteStream(java.io.OutputStream outputStream)
    {
        return new OutputStreamToByteWriteStream(outputStream);
    }
}
