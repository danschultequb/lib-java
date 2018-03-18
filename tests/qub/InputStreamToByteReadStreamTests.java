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
                closeTest(test, closedReadStream, true);

                final TestStubInputStream testStubInputStream = new TestStubInputStream();
                testStubInputStream.setThrowOnClose(true);
                closeTest(test, testStubInputStream, true);
            });

            runner.test("readByte()", (Test test) ->
            {
                final InputStreamToByteReadStream byteReadStream = getByteReadStream(2);

                test.assertEqual((byte)0, byteReadStream.readByte());
                test.assertEqual((byte)1, byteReadStream.readByte());
                test.assertEqual(null, byteReadStream.readByte());
            });
            
            runner.test("readByte() with exception", (Test test) ->
            {
                final TestStubInputStream inputStream = new TestStubInputStream();
                inputStream.setThrowOnRead(true);
                
                final InputStreamToByteReadStream byteReadStream = getByteReadStream(inputStream);
                test.assertEqual(null, byteReadStream.readByte());
            });
            
            runner.testGroup("readBytes(byte[])", () ->
            {
                runner.test("with no bytes", (Test test) ->
                {
                    final InputStreamToByteReadStream byteReadStream = getByteReadStream(0);

                    final byte[] buffer = new byte[10];
                    test.assertEqual(-1, byteReadStream.readBytes(buffer));
                });
                
                runner.test("with bytes", (Test test) ->
                {
                    final InputStreamToByteReadStream byteReadStream = getByteReadStream(3);

                    final byte[] buffer = new byte[10];
                    test.assertEqual(3, byteReadStream.readBytes(buffer));
                    test.assertEqual(new byte[] { 0, 1, 2, 0, 0, 0, 0, 0, 0, 0 }, buffer);

                    test.assertEqual(-1, byteReadStream.readBytes(buffer));
                });
                
                runner.test("asCharacterReadStream()", (Test test) ->
                {
                    final InputStreamToByteReadStream byteReadStream = getByteReadStream(new ByteArrayInputStream("abcd".getBytes()));

                    final CharacterReadStream characterReadStream = byteReadStream.asCharacterReadStream();

                    test.assertEqual('a', characterReadStream.readCharacter());
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

                        test.assertEqual('a', characterReadStream.readCharacter());
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

    private static void closeTest(Test test, InputStream inputStream)
    {
        final InputStreamToByteReadStream readStream = getByteReadStream(inputStream);
        closeTest(test, readStream, true);
        assertByteReadStream(test, readStream, false, false, null);
    }

    private static void closeTest(Test test, InputStream inputStream, boolean expectedIsDisposed)
    {
        final InputStreamToByteReadStream readStream = getByteReadStream(inputStream);
        closeTest(test, readStream, expectedIsDisposed);
    }

    private static void closeTest(Test test, InputStreamToByteReadStream readStream, boolean expectedIsDisposed)
    {
        readStream.close();
        test.assertEqual(expectedIsDisposed, readStream.isDisposed());
    }
}
