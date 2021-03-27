package qub;

public interface OutputStreamToByteWriteStreamTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(OutputStreamToByteWriteStream.class, () ->
        {
            runner.test("create(java.io.OutputStream)", test ->
            {
                final OutputStreamToByteWriteStream writeStream = OutputStreamToByteWriteStream.create(OutputStreamToByteWriteStreamTests.getOutputStream());
                test.assertFalse(writeStream.isDisposed());
            });

            runner.testGroup("close()", () ->
            {
                runner.test("with no exception", (Test test) ->
                {
                    final OutputStreamToByteWriteStream writeStream = OutputStreamToByteWriteStream.create(new java.io.ByteArrayOutputStream());
                    writeStream.close();
                    test.assertTrue(writeStream.isDisposed());
                });

                runner.test("with exception", (Test test) ->
                {
                    final OutputStreamToByteWriteStream writeStream = OutputStreamToByteWriteStream.create(new TestStubOutputStream());
                    test.assertThrows(writeStream::close,
                        new RuntimeException(new java.io.IOException()));
                    test.assertTrue(writeStream.isDisposed());
                });

                runner.test("when already disposed", (Test test) ->
                {
                    final OutputStreamToByteWriteStream writeStream = OutputStreamToByteWriteStream.create(new java.io.ByteArrayOutputStream());
                    test.assertTrue(writeStream.dispose().await());
                    test.assertTrue(writeStream.isDisposed());

                    writeStream.close();
                    test.assertTrue(writeStream.isDisposed());
                });
            });

            runner.testGroup("write(byte)", () ->
            {
                final Action3<java.io.OutputStream,Byte,Boolean> writeByteTest = (java.io.OutputStream outputStream, Byte toWrite, Boolean expectedWriteResult) ->
                {
                    runner.test("with " + outputStream.getClass().getSimpleName() + " and " + toWrite, test ->
                    {
                        final OutputStreamToByteWriteStream writeStream = OutputStreamToByteWriteStream.create(outputStream);
                        final Result<Integer> writeResult = writeStream.write(toWrite);
                        if (expectedWriteResult)
                        {
                            test.assertEqual(1, writeResult.await());
                        }
                        else
                        {
                            test.assertThrows(writeResult::await,
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
                        final OutputStreamToByteWriteStream writeStream = OutputStreamToByteWriteStream.create(outputStream);

                        if (expectedError != null)
                        {
                            test.assertThrows(() -> writeStream.write(toWrite).await(), expectedError);
                        }
                        else
                        {
                            test.assertEqual(expectedWriteResult, writeStream.write(toWrite).await());

                            if (outputStream instanceof java.io.ByteArrayOutputStream)
                            {
                                final java.io.ByteArrayOutputStream byteArrayOutputStream = (java.io.ByteArrayOutputStream)outputStream;
                                test.assertEqual(toWrite, byteArrayOutputStream.toByteArray());
                            }
                        }
                    });
                };

                writeByteArrayTest.run(new java.io.ByteArrayOutputStream(), null, null, new PreConditionFailure("toWrite cannot be null."));
                writeByteArrayTest.run(new TestStubOutputStream(), null, null, new PreConditionFailure("toWrite cannot be null."));
                writeByteArrayTest.run(new java.io.ByteArrayOutputStream(), new byte[0], null, new PreConditionFailure("toWrite cannot be empty."));
                writeByteArrayTest.run(new TestStubOutputStream(), new byte[0], null, new PreConditionFailure("toWrite cannot be empty."));
                writeByteArrayTest.run(new java.io.ByteArrayOutputStream(), new byte[] { 0, 1, 2, 3 }, 4, null);
                writeByteArrayTest.run(new TestStubOutputStream(), new byte[] { 0, 1, 2, 3 }, null, new RuntimeException(new java.io.IOException()));
            });

            runner.testGroup("writeByte(byte[],int,int)", () ->
            {
                final Action6<java.io.OutputStream,byte[],Integer,Integer,Integer,Throwable> writeByteArrayStartIndexAndLengthTest = (java.io.OutputStream outputStream, byte[] toWrite, Integer startIndex, Integer length, Integer expectedWriteResult, Throwable expectedError) ->
                {
                    runner.test("with " + Types.getTypeName(outputStream) + ", " + Array.toString(toWrite) + ", " + startIndex + ", and " + length, test ->
                    {
                        final OutputStreamToByteWriteStream writeStream = OutputStreamToByteWriteStream.create(outputStream);

                        if (expectedError == null)
                        {
                            test.assertEqual(expectedWriteResult, writeStream.write(toWrite, startIndex, length).await());
                        }
                        else
                        {
                            test.assertThrows(() -> writeStream.write(toWrite, startIndex, length).await(), expectedError);
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
        });
    }

    static java.io.ByteArrayOutputStream getOutputStream()
    {
        return new java.io.ByteArrayOutputStream();
    }
}
