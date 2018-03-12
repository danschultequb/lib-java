package qub;

import java.io.ByteArrayOutputStream;
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
                test.assertTrue(writeStream.isOpen());
            });

            runner.testGroup("close()", () ->
            {
                final Action2<OutputStream,Boolean> closeTest = (OutputStream outputStream, Boolean expectedIsOpen) ->
                {
                    runner.test("with " + outputStream.getClass().getSimpleName(), test ->
                    {
                        final OutputStreamToByteWriteStream writeStream = getWriteStream(outputStream);
                        writeStream.close();
                        test.assertEqual(expectedIsOpen, writeStream.isOpen());
                    });
                };

                closeTest.run(new ByteArrayOutputStream(), false);
                closeTest.run(new TestStubOutputStream(), true);
            });

            runner.testGroup("write(byte)", () ->
            {
                final Action3<OutputStream,Byte,Boolean> writeByteTest = (OutputStream outputStream, Byte toWrite, Boolean expectedWriteResult) ->
                {
                    runner.test("with " + outputStream.getClass().getSimpleName() + " and " + toWrite, test ->
                    {
                        final OutputStreamToByteWriteStream writeStream = getWriteStream(outputStream);
                        test.assertEqual(expectedWriteResult, writeStream.write(toWrite));

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
                final Action3<OutputStream,byte[],Boolean> writeByteArrayTest = (OutputStream outputStream, byte[] toWrite, Boolean expectedWriteResult) ->
                {
                    runner.test("with " + outputStream.getClass().getSimpleName() + " and " + Array.toString(toWrite), test ->
                    {
                        final OutputStreamToByteWriteStream writeStream = getWriteStream(outputStream);
                        test.assertEqual(expectedWriteResult, writeStream.write(toWrite));

                        if (outputStream instanceof ByteArrayOutputStream)
                        {
                            final ByteArrayOutputStream byteArrayOutputStream = (ByteArrayOutputStream)outputStream;
                            test.assertEqual(toWrite == null ? new byte[0] : toWrite, byteArrayOutputStream.toByteArray());
                        }
                    });
                };

                writeByteArrayTest.run(new ByteArrayOutputStream(), null, false);
                writeByteArrayTest.run(new TestStubOutputStream(), null, false);
                writeByteArrayTest.run(new ByteArrayOutputStream(), new byte[0], false);
                writeByteArrayTest.run(new TestStubOutputStream(), new byte[0], false);
                writeByteArrayTest.run(new ByteArrayOutputStream(), new byte[] { 0, 1, 2, 3 }, true);
                writeByteArrayTest.run(new TestStubOutputStream(), new byte[] { 0, 1, 2, 3 }, false);
            });

            runner.testGroup("write(byte[],int,int)", () ->
            {
                final Action5<OutputStream,byte[],Integer,Integer,Boolean> writeByteArrayStartIndexAndLengthTest = (OutputStream outputStream, byte[] toWrite, Integer startIndex, Integer length, Boolean expectedWriteResult) ->
                {
                    runner.test("with " + outputStream.getClass().getSimpleName() + ", " + Array.toString(toWrite) + ", " + startIndex + ", and " + length, test ->
                    {
                        final OutputStreamToByteWriteStream writeStream = getWriteStream(outputStream);
                        test.assertEqual(expectedWriteResult, writeStream.write(toWrite, startIndex, length));

                        if (outputStream instanceof ByteArrayOutputStream)
                        {
                            final ByteArrayOutputStream byteArrayOutputStream = (ByteArrayOutputStream)outputStream;
                            final byte[] expectedWrittenBytes = Array.clone(toWrite, startIndex, length);
                            test.assertEqual(expectedWrittenBytes == null ? new byte[0] : expectedWrittenBytes, byteArrayOutputStream.toByteArray());
                        }
                    });
                };

                writeByteArrayStartIndexAndLengthTest.run(new ByteArrayOutputStream(), null, 0, 0, false);
                writeByteArrayStartIndexAndLengthTest.run(new TestStubOutputStream(), null, 0, 0, false);
                writeByteArrayStartIndexAndLengthTest.run(new ByteArrayOutputStream(), new byte[0], 0, 0, false);
                writeByteArrayStartIndexAndLengthTest.run(new TestStubOutputStream(), new byte[0], 0, 0, false);
                writeByteArrayStartIndexAndLengthTest.run(new ByteArrayOutputStream(), new byte[] { 0, 1, 2 }, 0, 0, false);
                writeByteArrayStartIndexAndLengthTest.run(new TestStubOutputStream(), new byte[] { 0, 1, 2 }, 0, 0, false);
                writeByteArrayStartIndexAndLengthTest.run(new ByteArrayOutputStream(), new byte[] { 0, 1, 2 }, 1, 1, true);
                writeByteArrayStartIndexAndLengthTest.run(new TestStubOutputStream(), new byte[] { 0, 1, 2 }, 1, 1, false);
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
