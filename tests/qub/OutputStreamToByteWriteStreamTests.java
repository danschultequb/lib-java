package qub;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

public class OutputStreamToByteWriteStreamTests
{
    public static void test(final TestRunner runner)
    {
        runner.testGroup("OutputStreamToByteWriteStream", new Action0()
        {
            @Override
            public void run()
            {
                runner.test("constructor", new Action1<Test>()
                {
                    @Override
                    public void run(Test test)
                    {
                        final OutputStreamToByteWriteStream writeStream = new OutputStreamToByteWriteStream(getOutputStream());
                        test.assertTrue(writeStream.isOpen());
                    }
                });
                
                runner.testGroup("close()", new Action0()
                {
                    @Override
                    public void run()
                    {
                        final Action2<OutputStream,Boolean> closeTest = new Action2<OutputStream, Boolean>()
                        {
                            @Override
                            public void run(final OutputStream outputStream, final Boolean expectedIsOpen)
                            {
                                runner.test("with " + outputStream.getClass().getSimpleName(), new Action1<Test>()
                                {
                                    @Override
                                    public void run(Test test)
                                    {
                                        final OutputStreamToByteWriteStream writeStream = getWriteStream(outputStream);
                                        writeStream.close();
                                        test.assertEqual(expectedIsOpen, writeStream.isOpen());
                                    }
                                });
                            }
                        };

                        closeTest.run(new ByteArrayOutputStream(), false);
                        closeTest.run(new TestStubOutputStream(), true);
                    }
                });
                
                runner.testGroup("write(byte)", new Action0()
                {
                    @Override
                    public void run()
                    {
                        final Action3<OutputStream,Byte,Boolean> writeByteTest = new Action3<OutputStream, Byte, Boolean>()
                        {
                            @Override
                            public void run(final OutputStream outputStream, final Byte toWrite, final Boolean expectedWriteResult)
                            {
                                runner.test("with " + outputStream.getClass().getSimpleName() + " and " + toWrite, new Action1<Test>()
                                {
                                    @Override
                                    public void run(Test test)
                                    {
                                        final OutputStreamToByteWriteStream writeStream = getWriteStream(outputStream);
                                        test.assertEqual(expectedWriteResult, writeStream.write(toWrite));
                                        
                                        if (outputStream instanceof ByteArrayOutputStream)
                                        {
                                            final ByteArrayOutputStream byteArrayOutputStream = (ByteArrayOutputStream)outputStream;
                                            test.assertEqual(new byte[] { toWrite }, byteArrayOutputStream.toByteArray());
                                        }
                                    }
                                });
                            }
                        };

                        writeByteTest.run(new ByteArrayOutputStream(), (byte)10, true);
                        writeByteTest.run(new TestStubOutputStream(), (byte)20, false);
                    }
                });
                
                runner.testGroup("write(byte[])", new Action0()
                {
                    @Override
                    public void run()
                    {
                        final Action3<OutputStream,byte[],Boolean> writeByteArrayTest = new Action3<OutputStream, byte[], Boolean>()
                        {
                            @Override
                            public void run(final OutputStream outputStream, final byte[] toWrite, final Boolean expectedWriteResult)
                            {
                                runner.test("with " + outputStream.getClass().getSimpleName() + " and " + Array.toString(toWrite), new Action1<Test>()
                                {
                                    @Override
                                    public void run(Test test)
                                    {
                                        final OutputStreamToByteWriteStream writeStream = getWriteStream(outputStream);
                                        test.assertEqual(expectedWriteResult, writeStream.write(toWrite));

                                        if (outputStream instanceof ByteArrayOutputStream)
                                        {
                                            final ByteArrayOutputStream byteArrayOutputStream = (ByteArrayOutputStream)outputStream;
                                            test.assertEqual(toWrite == null ? new byte[0] : toWrite, byteArrayOutputStream.toByteArray());
                                        }
                                    }
                                });
                            }
                        };

                        writeByteArrayTest.run(new ByteArrayOutputStream(), null, false);
                        writeByteArrayTest.run(new TestStubOutputStream(), null, false);
                        writeByteArrayTest.run(new ByteArrayOutputStream(), new byte[0], false);
                        writeByteArrayTest.run(new TestStubOutputStream(), new byte[0], false);
                        writeByteArrayTest.run(new ByteArrayOutputStream(), new byte[] { 0, 1, 2, 3 }, true);
                        writeByteArrayTest.run(new TestStubOutputStream(), new byte[] { 0, 1, 2, 3 }, false);
                    }
                });
                
                runner.testGroup("write(byte[],int,int)", new Action0()
                {
                    @Override
                    public void run()
                    {
                        final Action5<OutputStream,byte[],Integer,Integer,Boolean> writeByteArrayStartIndexAndLengthTest = new Action5<OutputStream, byte[], Integer, Integer, Boolean>()
                        {
                            @Override
                            public void run(final OutputStream outputStream, final byte[] toWrite, final Integer startIndex, final Integer length, final Boolean expectedWriteResult)
                            {
                                runner.test("with " + outputStream.getClass().getSimpleName() + ", " + Array.toString(toWrite) + ", " + startIndex + ", and " + length, new Action1<Test>()
                                {
                                    @Override
                                    public void run(Test test)
                                    {
                                        final OutputStreamToByteWriteStream writeStream = getWriteStream(outputStream);
                                        test.assertEqual(expectedWriteResult, writeStream.write(toWrite, startIndex, length));
                                        
                                        if (outputStream instanceof ByteArrayOutputStream)
                                        {
                                            final ByteArrayOutputStream byteArrayOutputStream = (ByteArrayOutputStream)outputStream;
                                            final byte[] expectedWrittenBytes = Array.clone(toWrite, startIndex, length);
                                            test.assertEqual(expectedWrittenBytes == null ? new byte[0] : expectedWrittenBytes, byteArrayOutputStream.toByteArray());
                                        }
                                    }
                                });
                                
                            }
                        };

                        writeByteArrayStartIndexAndLengthTest.run(new ByteArrayOutputStream(), null, 0, 0, false);
                        writeByteArrayStartIndexAndLengthTest.run(new TestStubOutputStream(), null, 0, 0, false);
                        writeByteArrayStartIndexAndLengthTest.run(new ByteArrayOutputStream(), new byte[0], 0, 0, false);
                        writeByteArrayStartIndexAndLengthTest.run(new TestStubOutputStream(), new byte[0], 0, 0, false);
                        writeByteArrayStartIndexAndLengthTest.run(new ByteArrayOutputStream(), new byte[] { 0, 1, 2 }, 0, 0, false);
                        writeByteArrayStartIndexAndLengthTest.run(new TestStubOutputStream(), new byte[] { 0, 1, 2 }, 0, 0, false);
                        writeByteArrayStartIndexAndLengthTest.run(new ByteArrayOutputStream(), new byte[] { 0, 1, 2 }, 1, 1, true);
                        writeByteArrayStartIndexAndLengthTest.run(new TestStubOutputStream(), new byte[] { 0, 1, 2 }, 1, 1, false);
                    }
                });

                runner.test("asCharacterWriteStream()", new Action1<Test>()
                {
                    @Override
                    public void run(Test test)
                    {
                        final ByteArrayOutputStream outputStream = getOutputStream();
                        final OutputStreamToByteWriteStream writeStream = getWriteStream(outputStream);

                        final CharacterWriteStream characterWriteStream = writeStream.asCharacterWriteStream();
                        characterWriteStream.write("abc");

                        test.assertEqual(new byte[] { 97, 98, 99 }, outputStream.toByteArray());
                    }
                });

                runner.testGroup("asCharacterWriteStream(CharacterEncoding)", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("with null encoding", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final ByteArrayOutputStream outputStream = getOutputStream();
                                final OutputStreamToByteWriteStream writeStream = getWriteStream(outputStream);

                                test.assertNull(writeStream.asCharacterWriteStream((CharacterEncoding)null));
                            }
                        });
                    }
                });
            }
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
