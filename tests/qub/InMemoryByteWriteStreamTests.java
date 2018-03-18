package qub;

public class InMemoryByteWriteStreamTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(InMemoryByteWriteStream.class, () ->
        {
            runner.test("constructor()", (Test test) ->
            {
                final InMemoryByteWriteStream writeStream = new InMemoryByteWriteStream();
                test.assertEqual(new byte[0], writeStream.getBytes());
                test.assertFalse(writeStream.isDisposed());
            });
            
            runner.test("close()", (Test test) ->
            {
                final InMemoryByteWriteStream writeStream = new InMemoryByteWriteStream();
                writeStream.close();
                test.assertTrue(writeStream.isDisposed());
                test.assertNull(writeStream.getBytes());
                writeStream.close();
                test.assertNull(writeStream.getBytes());
            });
            
            runner.test("write(byte)", (Test test) ->
            {
                final InMemoryByteWriteStream writeStream = new InMemoryByteWriteStream();
                writeStream.write((byte)17);
                test.assertEqual(new byte[] { 17 }, writeStream.getBytes());
            });
            
            runner.test("write(byte[])", (Test test) ->
            {
                final InMemoryByteWriteStream writeStream = new InMemoryByteWriteStream();
                writeStream.write(new byte[0]);
                test.assertEqual(new byte[0], writeStream.getBytes());

                writeStream.write(new byte[] { 1, 2, 3, 4 });
                test.assertEqual(new byte[] { 1, 2, 3, 4 }, writeStream.getBytes());
            });
            
            runner.test("write(byte[],int,int)", (Test test) ->
            {
                final InMemoryByteWriteStream writeStream = new InMemoryByteWriteStream();
                writeStream.write(new byte[0], 0, 0);
                test.assertEqual(new byte[0], writeStream.getBytes());

                writeStream.write(new byte[] { 1, 2, 3, 4 }, 1, 0);
                test.assertEqual(new byte[0], writeStream.getBytes());

                writeStream.write(new byte[] { 1, 2, 3, 4 }, 1, 2);
                test.assertEqual(new byte[] { 2, 3 }, writeStream.getBytes());
            });
            
            runner.test("writeAll(ByteReadStream)", (Test test) ->
            {
                final InMemoryByteWriteStream writeStream = new InMemoryByteWriteStream();
                test.assertFalse(writeStream.writeAll(null));
                test.assertEqual(new byte[0], writeStream.getBytes());
            });
            
            runner.test("clear()", (Test test) ->
            {
                final InMemoryByteWriteStream byteWriteStream = new InMemoryByteWriteStream();
                byteWriteStream.clear();
                test.assertEqual(new byte[0], byteWriteStream.getBytes());

                byteWriteStream.write(new byte[] { 1, 2, 3, 4 });
                test.assertEqual(new byte[] { 1, 2, 3, 4 }, byteWriteStream.getBytes());

                byteWriteStream.clear();
                test.assertEqual(new byte[0], byteWriteStream.getBytes());
            });
            
            runner.test("asCharacterWriteStream()", (Test test) ->
            {
                final InMemoryByteWriteStream byteWriteStream = new InMemoryByteWriteStream();
                final CharacterWriteStream characterWriteStream = byteWriteStream.asCharacterWriteStream();
                test.assertNotNull(characterWriteStream);
                test.assertSame(byteWriteStream, characterWriteStream.asByteWriteStream());
            });

            runner.test("asCharacterWriteStream(CharacterEncoding)", (Test test) ->
            {
                final InMemoryByteWriteStream byteWriteStream = new InMemoryByteWriteStream();
                final CharacterWriteStream characterWriteStream = byteWriteStream.asCharacterWriteStream(CharacterEncoding.US_ASCII);
                test.assertNotNull(characterWriteStream);
                test.assertSame(byteWriteStream, characterWriteStream.asByteWriteStream());
            });

            runner.test("asLineWriteStream()", (Test test) ->
            {
                final InMemoryByteWriteStream byteWriteStream = new InMemoryByteWriteStream();
                final LineWriteStream lineWriteStream = byteWriteStream.asLineWriteStream();
                test.assertNotNull(lineWriteStream);
                test.assertSame(byteWriteStream, lineWriteStream.asByteWriteStream());
            });

            runner.test("asLineWriteStream(CharacterEncoding)", (Test test) ->
            {
                final InMemoryByteWriteStream byteWriteStream = new InMemoryByteWriteStream();
                final LineWriteStream lineWriteStream = byteWriteStream.asLineWriteStream(CharacterEncoding.US_ASCII);
                test.assertNotNull(lineWriteStream);
                test.assertSame(byteWriteStream, lineWriteStream.asByteWriteStream());
            });

            runner.test("asLineWriteStream(String)", (Test test) ->
            {
                final InMemoryByteWriteStream byteWriteStream = new InMemoryByteWriteStream();
                final LineWriteStream lineWriteStream = byteWriteStream.asLineWriteStream("\r\n");
                test.assertNotNull(lineWriteStream);
                test.assertEqual("\r\n", lineWriteStream.getLineSeparator());
                test.assertSame(byteWriteStream, lineWriteStream.asByteWriteStream());
            });

            runner.test("asLineWriteStream(CharacterEncoding,String)", (Test test) ->
            {
                final InMemoryByteWriteStream byteWriteStream = new InMemoryByteWriteStream();
                final LineWriteStream lineWriteStream = byteWriteStream.asLineWriteStream(CharacterEncoding.US_ASCII, "\r\n");
                test.assertNotNull(lineWriteStream);
                test.assertSame(CharacterEncoding.US_ASCII, lineWriteStream.getCharacterEncoding());
                test.assertEqual("\r\n", lineWriteStream.getLineSeparator());
                test.assertSame(byteWriteStream, lineWriteStream.asByteWriteStream());
            });
        });
    }
}
