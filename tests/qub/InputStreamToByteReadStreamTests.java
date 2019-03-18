package qub;

public class InputStreamToByteReadStreamTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(InputStreamToByteReadStream.class, () ->
        {
            AsyncDisposableTests.test(runner, (Test test) -> new InputStreamToByteReadStream(getInputStream(10), test.getMainAsyncRunner()));

            runner.test("constructor(InputStream)", (Test test) ->
            {
                final java.io.ByteArrayInputStream inputStream = getInputStream(5);
                final InputStreamToByteReadStream readStream = new InputStreamToByteReadStream(inputStream, test.getMainAsyncRunner());
                assertByteReadStream(test, readStream, false, false, null);
            });
            
            runner.test("close()", (Test test) ->
            {
                closeTest(test, getInputStream(0), true, null);
                closeTest(test, getInputStream(5), true, null);

                final java.io.ByteArrayInputStream closedInputStream = getInputStream(1);
                try
                {
                    closedInputStream.close();
                }
                catch (java.io.IOException e)
                {
                    test.fail(e);
                }
                closeTest(test, closedInputStream, true, null);

                final InputStreamToByteReadStream closedReadStream = getByteReadStream(test, 1);
                closedReadStream.close();
                closeTest(test, closedReadStream, true, null);

                final TestStubInputStream testStubInputStream = new TestStubInputStream();
                testStubInputStream.setThrowOnClose(true);
                closeTest(test, testStubInputStream, true, new RuntimeException(new java.io.IOException()));
            });

            runner.test("readByte()", (Test test) ->
            {
                final InputStreamToByteReadStream byteReadStream = getByteReadStream(test, 2);

                test.assertEqual((byte)0, byteReadStream.readByte().awaitError());
                test.assertEqual((byte)1, byteReadStream.readByte().awaitError());
                test.assertThrows(() -> byteReadStream.readByte().awaitError(),
                    new EndOfStreamException());
            });
            
            runner.test("readByte() with exception", (Test test) ->
            {
                final TestStubInputStream inputStream = new TestStubInputStream();
                inputStream.setThrowOnRead(true);
                
                final InputStreamToByteReadStream byteReadStream = getByteReadStream(test, inputStream);
                test.assertThrows(() -> byteReadStream.readByte().awaitError(),
                    new RuntimeException(new java.io.IOException()));
            });
            
            runner.testGroup("readBytes(byte[])", () ->
            {
                runner.test("with no bytes", (Test test) ->
                {
                    final InputStreamToByteReadStream byteReadStream = getByteReadStream(test, 0);

                    final byte[] buffer = new byte[10];
                    test.assertThrows(() -> byteReadStream.readBytes(buffer).awaitError(),
                        new EndOfStreamException());
                });
                
                runner.test("with bytes", (Test test) ->
                {
                    final InputStreamToByteReadStream byteReadStream = getByteReadStream(test, 3);

                    final byte[] buffer = new byte[10];
                    test.assertEqual(3, byteReadStream.readBytes(buffer).awaitError());
                    test.assertEqual(new byte[] { 0, 1, 2, 0, 0, 0, 0, 0, 0, 0 }, buffer);

                    test.assertThrows(() -> byteReadStream.readBytes(buffer).awaitError(),
                        new EndOfStreamException());
                });
                
                runner.test("asCharacterReadStream()", (Test test) ->
                {
                    final InputStreamToByteReadStream byteReadStream = getByteReadStream(test, new java.io.ByteArrayInputStream("abcd".getBytes()));

                    final CharacterReadStream characterReadStream = byteReadStream.asCharacterReadStream();

                    test.assertEqual('a', characterReadStream.readCharacter().awaitError());
                });
                
                runner.testGroup("asCharacterReadStream(CharacterEncoding)", () ->
                {
                    runner.test("with null encoding", (Test test) ->
                    {
                        final InputStreamToByteReadStream byteReadStream = getByteReadStream(test, 10);

                        test.assertThrows(() -> byteReadStream.asCharacterReadStream((CharacterEncoding)null),
                                          new PreConditionFailure("characterEncoding cannot be null."));
                    });

                    runner.test("with non-null encoding", (Test test) ->
                    {
                        final InputStreamToByteReadStream byteReadStream = getByteReadStream(test, new java.io.ByteArrayInputStream("abcd".getBytes()));

                        final CharacterReadStream characterReadStream = byteReadStream.asCharacterReadStream(CharacterEncoding.US_ASCII);

                        test.assertEqual('a', characterReadStream.readCharacter().awaitError());
                    });
                });

                runner.test("next()", (Test test) ->
                {
                    final InputStreamToByteReadStream byteReadStream = getByteReadStream(test, 5);

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

    private static java.io.ByteArrayInputStream getInputStream(int byteCount)
    {
        final byte[] bytes = new byte[byteCount];
        for (int i = 0; i < byteCount; ++i)
        {
            bytes[i] = (byte)i;
        }
        return new java.io.ByteArrayInputStream(bytes);
    }

    private static InputStreamToByteReadStream getByteReadStream(Test test, int byteCount)
    {
        return getByteReadStream(test, getInputStream(byteCount));
    }

    private static InputStreamToByteReadStream getByteReadStream(Test test, java.io.InputStream inputStream)
    {
        return new InputStreamToByteReadStream(inputStream, test.getMainAsyncRunner());
    }

    private static void assertByteReadStream(Test test, ByteReadStream byteReadStream, boolean isDisposed, boolean hasStarted, Byte current)
    {
        test.assertNotNull(byteReadStream);
        test.assertEqual(isDisposed, byteReadStream.isDisposed());
        test.assertEqual(hasStarted, byteReadStream.hasStarted());
        test.assertEqual(current != null, byteReadStream.hasCurrent());
        test.assertEqual(current, byteReadStream.getCurrent());
    }

    private static void closeTest(Test test, java.io.InputStream inputStream, boolean expectedIsDisposed, Throwable expectedError)
    {
        final InputStreamToByteReadStream readStream = getByteReadStream(test, inputStream);
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
            if (expectedError == null)
            {
                test.fail(e);
            }
            else
            {
                test.assertEqual(expectedError, e);
            }
        }
        test.assertEqual(expectedIsDisposed, readStream.isDisposed());
    }
}
