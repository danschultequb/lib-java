package qub;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class InputStreamToByteReadStreamTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(InputStreamToByteReadStream.class, () ->
        {
            runner.test("constructor(InputStream)", (Test test) ->
            {
                final ByteArrayInputStream inputStream = getInputStream(5);
                final InputStreamToByteReadStream readStream = new InputStreamToByteReadStream(inputStream);
                assertByteReadStream(test, readStream, false, false, null);
            });
            
            runner.test("close()", (Test test) ->
            {
                closeTest(test, getInputStream(0), true, null);
                closeTest(test, getInputStream(5), true, null);

                final ByteArrayInputStream closedInputStream = getInputStream(1);
                try
                {
                    closedInputStream.close();
                }
                catch (IOException e)
                {
                    test.fail(e);
                }
                closeTest(test, closedInputStream, true, null);

                final InputStreamToByteReadStream closedReadStream = getByteReadStream(1);
                try
                {
                    closedReadStream.close();
                }
                catch (Exception e)
                {
                    test.fail(e);
                }
                closeTest(test, closedReadStream, true, null);

                final TestStubInputStream testStubInputStream = new TestStubInputStream();
                testStubInputStream.setThrowOnClose(true);
                closeTest(test, testStubInputStream, true, new IOException());
            });

            runner.test("readByte()", (Test test) ->
            {
                final InputStreamToByteReadStream byteReadStream = getByteReadStream(2);

                test.assertEqual((byte)0, byteReadStream.readByte().getValue());
                test.assertEqual((byte)1, byteReadStream.readByte().getValue());
                test.assertEqual(null, byteReadStream.readByte().getValue());
            });
            
            runner.test("readByte() with exception", (Test test) ->
            {
                final TestStubInputStream inputStream = new TestStubInputStream();
                inputStream.setThrowOnRead(true);
                
                final InputStreamToByteReadStream byteReadStream = getByteReadStream(inputStream);
                final Result<Byte> result = byteReadStream.readByte();
                test.assertError(new IOException(), result);
            });
            
            runner.testGroup("readBytes(byte[])", () ->
            {
                runner.test("with no bytes", (Test test) ->
                {
                    final InputStreamToByteReadStream byteReadStream = getByteReadStream(0);

                    final byte[] buffer = new byte[10];
                    test.assertSuccess(null, byteReadStream.readBytes(buffer));
                });
                
                runner.test("with bytes", (Test test) ->
                {
                    final InputStreamToByteReadStream byteReadStream = getByteReadStream(3);

                    final byte[] buffer = new byte[10];
                    test.assertSuccess(3, byteReadStream.readBytes(buffer));
                    test.assertEqual(new byte[] { 0, 1, 2, 0, 0, 0, 0, 0, 0, 0 }, buffer);

                    test.assertSuccess(null, byteReadStream.readBytes(buffer));
                });
                
                runner.test("asCharacterReadStream()", (Test test) ->
                {
                    final InputStreamToByteReadStream byteReadStream = getByteReadStream(new ByteArrayInputStream("abcd".getBytes()));

                    final CharacterReadStream characterReadStream = byteReadStream.asCharacterReadStream();

                    test.assertSuccess('a', characterReadStream.readCharacter());
                });
                
                runner.testGroup("asCharacterReadStream(CharacterEncoding)", () ->
                {
                    runner.test("with null encoding", (Test test) ->
                    {
                        final InputStreamToByteReadStream byteReadStream = getByteReadStream(10);

                        test.assertNull(byteReadStream.asCharacterReadStream((CharacterEncoding)null));
                    });

                    runner.test("with non-null encoding", (Test test) ->
                    {
                        final InputStreamToByteReadStream byteReadStream = getByteReadStream(new ByteArrayInputStream("abcd".getBytes()));

                        final CharacterReadStream characterReadStream = byteReadStream.asCharacterReadStream(CharacterEncoding.US_ASCII);

                        test.assertSuccess('a', characterReadStream.readCharacter());
                    });
                });

                runner.test("next()", (Test test) ->
                {
                    final InputStreamToByteReadStream byteReadStream = getByteReadStream(5);

                    for (int i = 0; i < 5; ++i)
                    {
                        test.assertTrue(byteReadStream.next());
                        assertByteReadStream(test, byteReadStream, false, true, (byte)i);
                    }

                    test.assertFalse(byteReadStream.next());
                    assertByteReadStream(test, byteReadStream, false, true, null);
                });
            });
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

    private static void assertByteReadStream(Test test, ByteReadStream byteReadStream, boolean isDisposed, boolean hasStarted, Byte current)
    {
        test.assertNotNull(byteReadStream);
        test.assertEqual(isDisposed, byteReadStream.isDisposed());
        test.assertEqual(hasStarted, byteReadStream.hasStarted());
        test.assertEqual(current != null, byteReadStream.hasCurrent());
        test.assertEqual(current, byteReadStream.getCurrent());
    }

    private static void closeTest(Test test, InputStream inputStream, boolean expectedIsDisposed, Throwable expectedError)
    {
        final InputStreamToByteReadStream readStream = getByteReadStream(inputStream);
        closeTest(test, readStream, expectedIsDisposed, expectedError);
    }

    private static void closeTest(Test test, InputStreamToByteReadStream readStream, boolean expectedIsDisposed, Throwable expectedError)
    {
        try
        {
            readStream.close();
            if (expectedError != null)
            {
                test.fail("Expected an Exception to be thrown.");
            }
        }
        catch (Exception e)
        {
            test.assertEqual(expectedError, e);
        }
        test.assertEqual(expectedIsDisposed, readStream.isDisposed());
    }
}
