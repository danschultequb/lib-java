package qub;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

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
                    final OutputStreamToByteWriteStream writeStream = getWriteStream(new ByteArrayOutputStream());
                    writeStream.close();
                    test.assertTrue(writeStream.isDisposed());
                });

                runner.test("with exception", (Test test) ->
                {
                    final OutputStreamToByteWriteStream writeStream = getWriteStream(new TestStubOutputStream());
                    test.assertThrows(new Action0()
                        {
                            @Override
                            public void run()
                            {
                                writeStream.close();
                            }
                        },
                        new RuntimeException(new IOException()));
                    test.assertTrue(writeStream.isDisposed());
                });
            });

            runner.testGroup("writeByte(byte)", () ->
            {
                final Action3<OutputStream,Byte,Boolean> writeByteTest = (OutputStream outputStream, Byte toWrite, Boolean expectedWriteResult) ->
                {
                    runner.test("with " + outputStream.getClass().getSimpleName() + " and " + toWrite, test ->
                    {
                        final OutputStreamToByteWriteStream writeStream = getWriteStream(outputStream);
                        final Result<Boolean> writeResult = writeStream.writeByte(toWrite);
                        if (expectedWriteResult)
                        {
                            test.assertSuccess(true, writeResult);
                        }
                        else
                        {
                            test.assertError(new java.io.IOException(), writeResult);
                        }

                        if (outputStream instanceof ByteArrayOutputStream)
                        {
                            final ByteArrayOutputStream byteArrayOutputStream = (ByteArrayOutputStream)outputStream;
                            test.assertEqual(new byte[] { toWrite }, byteArrayOutputStream.toByteArray());
                        }
                    });
                };

                writeByteTest.run(new ByteArrayOutputStream(), (byte)10, true);
                writeByteTest.run(new TestStubOutputStream(), (byte)20, false);
            });

            runner.testGroup("writeByte(byte[])", () ->
            {
                final Action4<OutputStream,byte[],Integer,Throwable> writeByteArrayTest = (OutputStream outputStream, byte[] toWrite, Integer expectedWriteResult, Throwable expectedError) ->
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
                                test.assertError(expectedError, writeResult);
                            }
                            else
                            {
                                test.assertSuccess(expectedWriteResult, writeResult);
                            }

                            if (outputStream instanceof ByteArrayOutputStream)
                            {
                                final ByteArrayOutputStream byteArrayOutputStream = (ByteArrayOutputStream)outputStream;
                                test.assertEqual(toWrite, byteArrayOutputStream.toByteArray());
                            }
                        }
                    });
                };

                writeByteArrayTest.run(new ByteArrayOutputStream(), null, null, new IllegalArgumentException("toWrite cannot be null."));
                writeByteArrayTest.run(new TestStubOutputStream(), null, null, new IllegalArgumentException("toWrite cannot be null."));
                writeByteArrayTest.run(new ByteArrayOutputStream(), new byte[0], null, new IllegalArgumentException("toWrite.length (0) must be greater than 0."));
                writeByteArrayTest.run(new TestStubOutputStream(), new byte[0], null, new IllegalArgumentException("toWrite.length (0) must be greater than 0."));
                writeByteArrayTest.run(new ByteArrayOutputStream(), new byte[] { 0, 1, 2, 3 }, 4, null);
                writeByteArrayTest.run(new TestStubOutputStream(), new byte[] { 0, 1, 2, 3 }, null, new IOException());
            });

            runner.testGroup("writeByte(byte[],int,int)", () ->
            {
                final Action6<OutputStream,byte[],Integer,Integer,Integer,Throwable> writeByteArrayStartIndexAndLengthTest = (OutputStream outputStream, byte[] toWrite, Integer startIndex, Integer length, Integer expectedWriteResult, Throwable expectedError) ->
                {
                    runner.test("with " + Types.getTypeName(outputStream) + ", " + Array.toString(toWrite) + ", " + startIndex + ", and " + length, test ->
                    {
                        final OutputStreamToByteWriteStream writeStream = getWriteStream(outputStream);

                        if (toWrite == null || toWrite.length == 0 ||
                            startIndex < 0 || toWrite.length <= startIndex ||
                            length <= 0 || toWrite.length - startIndex < length)
                        {
                            test.assertThrows(() -> writeStream.writeBytes(toWrite, startIndex, length));
                        }
                        else
                        {
                            final Result<Integer> writeResult = writeStream.writeBytes(toWrite, startIndex, length);
                            if (expectedError != null)
                            {
                                test.assertError(expectedError, writeResult);
                            }
                            else
                            {
                                test.assertSuccess(expectedWriteResult, writeResult);
                            }

                            if (outputStream instanceof ByteArrayOutputStream)
                            {
                                final ByteArrayOutputStream byteArrayOutputStream = (ByteArrayOutputStream)outputStream;
                                final byte[] expectedWrittenBytes = Array.clone(toWrite, startIndex, length);
                                test.assertEqual(expectedWrittenBytes, byteArrayOutputStream.toByteArray());
                            }
                        }
                    });
                };

                writeByteArrayStartIndexAndLengthTest.run(new ByteArrayOutputStream(), null, 0, 0, null, null);
                writeByteArrayStartIndexAndLengthTest.run(new TestStubOutputStream(), null, 0, 0, null, null);
                writeByteArrayStartIndexAndLengthTest.run(new ByteArrayOutputStream(), new byte[0], 0, 0, null, null);
                writeByteArrayStartIndexAndLengthTest.run(new TestStubOutputStream(), new byte[0], 0, 0, null, null);
                writeByteArrayStartIndexAndLengthTest.run(new ByteArrayOutputStream(), new byte[] { 0, 1, 2 }, 0, 0, null, null);
                writeByteArrayStartIndexAndLengthTest.run(new TestStubOutputStream(), new byte[] { 0, 1, 2 }, 0, 0, null, null);
                writeByteArrayStartIndexAndLengthTest.run(new ByteArrayOutputStream(), new byte[] { 0, 1, 2 }, 1, 1, 1, null);
                writeByteArrayStartIndexAndLengthTest.run(new TestStubOutputStream(), new byte[] { 0, 1, 2 }, 1, 1, null, new IOException());
            });

            runner.test("asCharacterWriteStream()", test ->
            {
                final ByteArrayOutputStream outputStream = getOutputStream();
                final OutputStreamToByteWriteStream writeStream = getWriteStream(outputStream);

                final CharacterWriteStream characterWriteStream = writeStream.asCharacterWriteStream();
                characterWriteStream.write("abc");

                test.assertEqual(new byte[] { 97, 98, 99 }, outputStream.toByteArray());
            });

            runner.testGroup("asCharacterWriteStream(CharacterEncoding)", () ->
            {
                runner.test("with null encoding", test ->
                {
                    final ByteArrayOutputStream outputStream = getOutputStream();
                    final OutputStreamToByteWriteStream writeStream = getWriteStream(outputStream);

                    test.assertThrows(() -> writeStream.asCharacterWriteStream(null));
                });
            });
        });
    }

    private static ByteArrayOutputStream getOutputStream()
    {
        return new ByteArrayOutputStream();
    }

    private static OutputStreamToByteWriteStream getWriteStream(OutputStream outputStream)
    {
        return new OutputStreamToByteWriteStream(outputStream);
    }
}
