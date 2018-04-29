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
                    try
                    {
                        writeStream.close();
                    }
                    catch (Exception e)
                    {
                        test.fail(e);
                    }
                    test.assertTrue(writeStream.isDisposed());
                });

                runner.test("with exception", (Test test) ->
                {
                    final OutputStreamToByteWriteStream writeStream = getWriteStream(new TestStubOutputStream());
                    try
                    {
                        writeStream.close();
                        test.fail("Expected an exception to be thrown.");
                    }
                    catch (Exception e)
                    {
                        test.assertEqual(new IOException(), e);
                    }
                    test.assertTrue(writeStream.isDisposed());
                });
            });

            runner.testGroup("write(byte)", () ->
            {
                final Action3<OutputStream,Byte,Boolean> writeByteTest = (OutputStream outputStream, Byte toWrite, Boolean expectedWriteResult) ->
                {
                    runner.test("with " + outputStream.getClass().getSimpleName() + " and " + toWrite, test ->
                    {
                        final OutputStreamToByteWriteStream writeStream = getWriteStream(outputStream);
                        final Result<Boolean> writeResult = writeStream.write(toWrite);
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

            runner.testGroup("write(byte[])", () ->
            {
                final Action4<OutputStream,byte[],Boolean,Throwable> writeByteArrayTest = (OutputStream outputStream, byte[] toWrite, Boolean expectedWriteResult, Throwable expectedError) ->
                {
                    runner.test("with " + outputStream.getClass().getSimpleName() + " and " + Array.toString(toWrite), test ->
                    {
                        final OutputStreamToByteWriteStream writeStream = getWriteStream(outputStream);

                        final Result<Boolean> writeResult = writeStream.write(toWrite);
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
                            test.assertEqual(toWrite == null ? new byte[0] : toWrite, byteArrayOutputStream.toByteArray());
                        }
                    });
                };

                writeByteArrayTest.run(new ByteArrayOutputStream(), null, false, new IllegalArgumentException("toWrite cannot be null."));
                writeByteArrayTest.run(new TestStubOutputStream(), null, false, new IllegalArgumentException("toWrite cannot be null."));
                writeByteArrayTest.run(new ByteArrayOutputStream(), new byte[0], false, new IllegalArgumentException("toWrite.length (0) must be greater than 0."));
                writeByteArrayTest.run(new TestStubOutputStream(), new byte[0], false, new IllegalArgumentException("toWrite.length (0) must be greater than 0."));
                writeByteArrayTest.run(new ByteArrayOutputStream(), new byte[] { 0, 1, 2, 3 }, true, null);
                writeByteArrayTest.run(new TestStubOutputStream(), new byte[] { 0, 1, 2, 3 }, false, new IOException());
            });

            runner.testGroup("write(byte[],int,int)", () ->
            {
                final Action6<OutputStream,byte[],Integer,Integer,Boolean,Throwable> writeByteArrayStartIndexAndLengthTest = (OutputStream outputStream, byte[] toWrite, Integer startIndex, Integer length, Boolean expectedWriteResult, Throwable expectedError) ->
                {
                    runner.test("with " + outputStream.getClass().getSimpleName() + ", " + Array.toString(toWrite) + ", " + startIndex + ", and " + length, test ->
                    {
                        final OutputStreamToByteWriteStream writeStream = getWriteStream(outputStream);

                        final Result<Boolean> writeResult = writeStream.write(toWrite, startIndex, length);
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
                            test.assertEqual(expectedWrittenBytes == null ? new byte[0] : expectedWrittenBytes, byteArrayOutputStream.toByteArray());
                        }
                    });
                };

                writeByteArrayStartIndexAndLengthTest.run(new ByteArrayOutputStream(), null, 0, 0, false, new IllegalArgumentException("toWrite cannot be null."));
                writeByteArrayStartIndexAndLengthTest.run(new TestStubOutputStream(), null, 0, 0, false, new IllegalArgumentException("toWrite cannot be null."));
                writeByteArrayStartIndexAndLengthTest.run(new ByteArrayOutputStream(), new byte[0], 0, 0, false, new IllegalArgumentException("toWrite.length (0) must be greater than 0."));
                writeByteArrayStartIndexAndLengthTest.run(new TestStubOutputStream(), new byte[0], 0, 0, false, new IllegalArgumentException("toWrite.length (0) must be greater than 0."));
                writeByteArrayStartIndexAndLengthTest.run(new ByteArrayOutputStream(), new byte[] { 0, 1, 2 }, 0, 0, false, new IllegalArgumentException("length (0) must be between 1 and 3."));
                writeByteArrayStartIndexAndLengthTest.run(new TestStubOutputStream(), new byte[] { 0, 1, 2 }, 0, 0, false, new IllegalArgumentException("length (0) must be between 1 and 3."));
                writeByteArrayStartIndexAndLengthTest.run(new ByteArrayOutputStream(), new byte[] { 0, 1, 2 }, 1, 1, true, null);
                writeByteArrayStartIndexAndLengthTest.run(new TestStubOutputStream(), new byte[] { 0, 1, 2 }, 1, 1, false, new IOException());
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

                    test.assertNull(writeStream.asCharacterWriteStream((CharacterEncoding)null));
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
