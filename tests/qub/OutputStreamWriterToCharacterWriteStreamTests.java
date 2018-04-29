package qub;

import java.io.IOException;

public class OutputStreamWriterToCharacterWriteStreamTests
{
    public static void test(final TestRunner runner)
    {
        runner.testGroup("OutputStreamWriterToCharacterWriteStream", () ->
        {
            runner.test("constructor", (Test test) ->
            {
                final ByteWriteStream byteWriteStream = new InMemoryByteWriteStream();
                final OutputStreamWriterToCharacterWriteStream characterWriteStream = new OutputStreamWriterToCharacterWriteStream(byteWriteStream, CharacterEncoding.US_ASCII);
                test.assertSame(byteWriteStream, characterWriteStream.asByteWriteStream());
                test.assertSame(CharacterEncoding.US_ASCII, characterWriteStream.getCharacterEncoding());
            });
            
            runner.testGroup("write(char)", () ->
            {
                runner.test("with no exception", (Test test) ->
                {
                    InMemoryByteWriteStream byteWriteStream = new InMemoryByteWriteStream();
                    final OutputStreamWriterToCharacterWriteStream characterWriteStream = getCharacterWriteStream(byteWriteStream);
                    test.assertSuccess(true, characterWriteStream.write('a'));
                    test.assertEqual(new byte[] { 97 }, byteWriteStream.getBytes());
                });
                
                runner.test("with exception", (Test test) ->
                {
                    TestStubOutputStream outputStream = new TestStubOutputStream();
                    OutputStreamToByteWriteStream byteWriteStream = new OutputStreamToByteWriteStream(outputStream);
                    final OutputStreamWriterToCharacterWriteStream characterWriteStream = getCharacterWriteStream(byteWriteStream);
                    test.assertError(new IOException(), characterWriteStream.write('a'));
                });
            });
            
            runner.testGroup("write(String)", () ->
            {
                runner.test("with no exception", (Test test) ->
                {
                    InMemoryByteWriteStream byteWriteStream = new InMemoryByteWriteStream();
                    final OutputStreamWriterToCharacterWriteStream characterWriteStream = getCharacterWriteStream(byteWriteStream);
                    test.assertSuccess(true, characterWriteStream.write("test"));
                    test.assertEqual(new byte[] { 116, 101, 115, 116 }, byteWriteStream.getBytes());
                });
                
                runner.test("with exception", (Test test) ->
                {
                    TestStubOutputStream outputStream = new TestStubOutputStream();
                    OutputStreamToByteWriteStream byteWriteStream = new OutputStreamToByteWriteStream(outputStream);
                    final OutputStreamWriterToCharacterWriteStream characterWriteStream = getCharacterWriteStream(byteWriteStream);
                    test.assertError(new IOException(), characterWriteStream.write("test again"));
                });
            });
            
            runner.testGroup("close()", () ->
            {
                runner.test("with no exception", (Test test) ->
                {
                    InMemoryByteWriteStream byteWriteStream = new InMemoryByteWriteStream();
                    final OutputStreamWriterToCharacterWriteStream characterWriteStream = getCharacterWriteStream(byteWriteStream);
                    try
                    {
                        characterWriteStream.close();
                        test.assertTrue(characterWriteStream.isDisposed());
                        test.assertTrue(byteWriteStream.isDisposed());

                        characterWriteStream.close();
                        test.assertTrue(characterWriteStream.isDisposed());
                        test.assertTrue(byteWriteStream.isDisposed());
                    }
                    catch (Exception e)
                    {
                        test.fail(e);
                    }
                });
                
                runner.test("with exception", (Test test) ->
                {
                    TestStubOutputStream outputStream = new TestStubOutputStream();
                    OutputStreamToByteWriteStream byteWriteStream = new OutputStreamToByteWriteStream(outputStream);
                    final OutputStreamWriterToCharacterWriteStream characterWriteStream = getCharacterWriteStream(byteWriteStream);
                    try
                    {
                        characterWriteStream.close();
                        test.fail("Expected an exception to be thrown.");
                    }
                    catch (Exception e)
                    {
                    }
                    test.assertTrue(characterWriteStream.isDisposed());
                    test.assertTrue(byteWriteStream.isDisposed());
                });
            });
            
            runner.test("asByteWriteStream()", (Test test) ->
            {
                final ByteWriteStream byteWriteStream = new InMemoryByteWriteStream();
                final OutputStreamWriterToCharacterWriteStream characterWriteStream = getCharacterWriteStream(byteWriteStream);
                test.assertSame(byteWriteStream, characterWriteStream.asByteWriteStream());
            });
            
            runner.test("asLineWriteStream()", (Test test) ->
            {
                final OutputStreamWriterToCharacterWriteStream characterWriteStream = getCharacterWriteStream();
                final LineWriteStream lineWriteStream = characterWriteStream.asLineWriteStream();
                test.assertNotNull(lineWriteStream);
                test.assertSame(characterWriteStream, lineWriteStream.asCharacterWriteStream());
            });

            runner.test("asLineWriteStream(String)", (Test test) ->
            {
                final OutputStreamWriterToCharacterWriteStream characterWriteStream = getCharacterWriteStream();
                final LineWriteStream lineWriteStream = characterWriteStream.asLineWriteStream("\n");
                test.assertNotNull(lineWriteStream);
                test.assertEqual("\n", lineWriteStream.getLineSeparator());
                test.assertSame(characterWriteStream, lineWriteStream.asCharacterWriteStream());
            });
        });
    }

    private static OutputStreamWriterToCharacterWriteStream getCharacterWriteStream()
    {
        return getCharacterWriteStream(new InMemoryByteWriteStream());
    }

    private static OutputStreamWriterToCharacterWriteStream getCharacterWriteStream(ByteWriteStream byteWriteStream)
    {
        return new OutputStreamWriterToCharacterWriteStream(byteWriteStream, CharacterEncoding.UTF_8);
    }
}
