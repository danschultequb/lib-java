package qub;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class InputStreamToByteReadStreamTests
{
    public static void test(final TestRunner runner)
    {
        runner.testGroup("InputStreamToByteReadStream", new Action0()
        {
            @Override
            public void run()
            {
                runner.test("constructor(InputStream)", new Action1<Test>()
                {
                    @Override
                    public void run(Test test)
                    {
                        final ByteArrayInputStream inputStream = getInputStream(5);
                        final InputStreamToByteReadStream readStream = new InputStreamToByteReadStream(inputStream);
                        assertByteReadStream(test, readStream, true, false, null);
                    }
                });
                
                runner.test("close()", new Action1<Test>()
                {
                    @Override
                    public void run(Test test)
                    {
                        closeTest(test, getInputStream(0), true);
                        closeTest(test, getInputStream(5), true);

                        final ByteArrayInputStream closedInputStream = getInputStream(1);
                        try
                        {
                            closedInputStream.close();
                        }
                        catch (IOException e)
                        {
                            test.fail(e);
                        }
                        closeTest(test, closedInputStream, true);

                        final InputStreamToByteReadStream closedReadStream = getByteReadStream(1);
                        closedReadStream.close();
                        closeTest(test, closedReadStream, false);

                        final TestStubInputStream testStubInputStream = new TestStubInputStream();
                        testStubInputStream.setThrowOnClose(true);
                        closeTest(test, testStubInputStream, false, true);
                    }
                });

                runner.test("readByte()", new Action1<Test>()
                {
                    @Override
                    public void run(Test test)
                    {
                        final InputStreamToByteReadStream byteReadStream = getByteReadStream(2);

                        test.assertEqual((byte)0, byteReadStream.readByte());
                        test.assertEqual((byte)1, byteReadStream.readByte());
                        test.assertEqual(null, byteReadStream.readByte());
                    }
                });
                
                runner.test("readByte() with exception", new Action1<Test>()
                {
                    @Override
                    public void run(Test test)
                    {
                        final TestStubInputStream inputStream = new TestStubInputStream();
                        inputStream.setThrowOnRead(true);
                        
                        final InputStreamToByteReadStream byteReadStream = getByteReadStream(inputStream);
                        test.assertEqual(null, byteReadStream.readByte());
                    }
                });
                
                runner.testGroup("readBytes(byte[])", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("with no bytes", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final InputStreamToByteReadStream byteReadStream = getByteReadStream(0);

                                final byte[] buffer = new byte[10];
                                test.assertEqual(-1, byteReadStream.readBytes(buffer));
                            }
                        });
                        
                        runner.test("with bytes", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final InputStreamToByteReadStream byteReadStream = getByteReadStream(3);

                                final byte[] buffer = new byte[10];
                                test.assertEqual(3, byteReadStream.readBytes(buffer));
                                test.assertEqual(new byte[] { 0, 1, 2, 0, 0, 0, 0, 0, 0, 0 }, buffer);

                                test.assertEqual(-1, byteReadStream.readBytes(buffer));
                            }
                        });
                        
                        runner.test("asCharacterReadStream()", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final InputStreamToByteReadStream byteReadStream = getByteReadStream(new ByteArrayInputStream("abcd".getBytes()));

                                final CharacterReadStream characterReadStream = byteReadStream.asCharacterReadStream();

                                test.assertEqual('a', characterReadStream.readCharacter());
                            }
                        });
                        
                        runner.testGroup("asCharacterReadStream(CharacterEncoding)", new Action0()
                        {
                            @Override
                            public void run()
                            {
                                runner.test("with null encoding", new Action1<Test>()
                                {
                                    @Override
                                    public void run(Test test)
                                    {
                                        final InputStreamToByteReadStream byteReadStream = getByteReadStream(10);

                                        test.assertNull(byteReadStream.asCharacterReadStream((CharacterEncoding)null));
                                    }
                                });

                                runner.test("with non-null encoding", new Action1<Test>()
                                {
                                    @Override
                                    public void run(Test test)
                                    {
                                        final InputStreamToByteReadStream byteReadStream = getByteReadStream(new ByteArrayInputStream("abcd".getBytes()));

                                        final CharacterReadStream characterReadStream = byteReadStream.asCharacterReadStream(CharacterEncoding.US_ASCII);

                                        test.assertEqual('a', characterReadStream.readCharacter());
                                    }
                                });
                            }
                        });

                        runner.test("next()", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                final InputStreamToByteReadStream byteReadStream = getByteReadStream(5);

                                for (int i = 0; i < 5; ++i)
                                {
                                    test.assertTrue(byteReadStream.next());
                                    assertByteReadStream(test, byteReadStream, true, true, (byte)i);
                                }

                                test.assertFalse(byteReadStream.next());
                                assertByteReadStream(test, byteReadStream, true, true, null);
                            }
                        });
                    }
                });
            }
        });
    }

    private static ByteArrayInputStream getInputStream(int byteCount)
    {
        final byte[] bytes = new byte[byteCount];
        for (int i = 0; i < byteCount; ++i)
        {
            bytes[i] = (byte)i;
        }
        return new ByteArrayInputStream(bytes);
    }

    private static InputStreamToByteReadStream getByteReadStream(int byteCount)
    {
        return getByteReadStream(getInputStream(byteCount));
    }

    private static InputStreamToByteReadStream getByteReadStream(InputStream inputStream)
    {
        return new InputStreamToByteReadStream(inputStream);
    }

    private static void assertByteReadStream(Test test, ByteReadStream byteReadStream, boolean isOpen, boolean hasStarted, Byte current)
    {
        test.assertNotNull(byteReadStream);
        test.assertEqual(isOpen, byteReadStream.isOpen());
        test.assertEqual(hasStarted, byteReadStream.hasStarted());
        test.assertEqual(current != null, byteReadStream.hasCurrent());
        test.assertEqual(current, byteReadStream.getCurrent());
    }

    private static void closeTest(Test test, InputStream inputStream, boolean expectedResult)
    {
        final InputStreamToByteReadStream readStream = getByteReadStream(inputStream);
        closeTest(test, readStream, expectedResult);
        assertByteReadStream(test, readStream, false, false, null);
    }

    private static void closeTest(Test test, InputStream inputStream, boolean expectedResult, boolean expectedIsOpen)
    {
        final InputStreamToByteReadStream readStream = getByteReadStream(inputStream);
        closeTest(test, readStream, expectedResult, expectedIsOpen);
    }

    private static void closeTest(Test test, InputStreamToByteReadStream readStream, boolean expectedResult)
    {
        closeTest(test, readStream, expectedResult, false);
    }

    private static void closeTest(Test test, InputStreamToByteReadStream readStream, boolean expectedResult, boolean expectedIsOpen)
    {
        readStream.close();
        test.assertEqual(expectedIsOpen, readStream.isOpen());
    }
}
