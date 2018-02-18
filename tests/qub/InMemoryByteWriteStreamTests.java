package qub;

public class InMemoryByteWriteStreamTests
{
    public static void test(final TestRunner runner)
    {
        runner.testGroup("InMemoryByteWriteStream", new Action0()
        {
            @Override
            public void run()
            {
                runner.test("constructor()", new Action1<Test>()
                {
                    @Override
                    public void run(Test test)
                    {
                        final InMemoryByteWriteStream writeStream = new InMemoryByteWriteStream();
                        test.assertEqual(new byte[0], writeStream.getBytes());
                        test.assertTrue(writeStream.isOpen());
                    }
                });
                
                runner.test("close()", new Action1<Test>()
                {
                    @Override
                    public void run(Test test)
                    {
                        final InMemoryByteWriteStream writeStream = new InMemoryByteWriteStream();
                        writeStream.close();
                        test.assertFalse(writeStream.isOpen());
                        test.assertNull(writeStream.getBytes());
                        writeStream.close();
                        test.assertNull(writeStream.getBytes());
                    }
                });
                
                runner.test("write(byte)", new Action1<Test>()
                {
                    @Override
                    public void run(Test test)
                    {
                        final InMemoryByteWriteStream writeStream = new InMemoryByteWriteStream();
                        writeStream.write((byte)17);
                        test.assertEqual(new byte[] { 17 }, writeStream.getBytes());
                    }
                });
                
                runner.test("write(byte[])", new Action1<Test>()
                {
                    @Override
                    public void run(Test test)
                    {
                        final InMemoryByteWriteStream writeStream = new InMemoryByteWriteStream();
                        writeStream.write(new byte[0]);
                        test.assertEqual(new byte[0], writeStream.getBytes());

                        writeStream.write(new byte[] { 1, 2, 3, 4 });
                        test.assertEqual(new byte[] { 1, 2, 3, 4 }, writeStream.getBytes());
                    }
                });
                
                runner.test("write(byte[],int,int)", new Action1<Test>()
                {
                    @Override
                    public void run(Test test)
                    {
                        final InMemoryByteWriteStream writeStream = new InMemoryByteWriteStream();
                        writeStream.write(new byte[0], 0, 0);
                        test.assertEqual(new byte[0], writeStream.getBytes());

                        writeStream.write(new byte[] { 1, 2, 3, 4 }, 1, 0);
                        test.assertEqual(new byte[0], writeStream.getBytes());

                        writeStream.write(new byte[] { 1, 2, 3, 4 }, 1, 2);
                        test.assertEqual(new byte[] { 2, 3 }, writeStream.getBytes());
                    }
                });
                
                runner.test("writeAll(ByteReadStream)", new Action1<Test>()
                {
                    @Override
                    public void run(Test test)
                    {
                        final InMemoryByteWriteStream writeStream = new InMemoryByteWriteStream();
                        test.assertFalse(writeStream.writeAll(null));
                        test.assertEqual(new byte[0], writeStream.getBytes());
                    }
                });
                
                runner.test("clear()", new Action1<Test>()
                {
                    @Override
                    public void run(Test test)
                    {
                        final InMemoryByteWriteStream byteWriteStream = new InMemoryByteWriteStream();
                        byteWriteStream.clear();
                        test.assertEqual(new byte[0], byteWriteStream.getBytes());

                        byteWriteStream.write(new byte[] { 1, 2, 3, 4 });
                        test.assertEqual(new byte[] { 1, 2, 3, 4 }, byteWriteStream.getBytes());

                        byteWriteStream.clear();
                        test.assertEqual(new byte[0], byteWriteStream.getBytes());
                    }
                });
                
                runner.test("asCharacterWriteStream()", new Action1<Test>()
                {
                    @Override
                    public void run(Test test)
                    {
                        final InMemoryByteWriteStream byteWriteStream = new InMemoryByteWriteStream();
                        final CharacterWriteStream characterWriteStream = byteWriteStream.asCharacterWriteStream();
                        test.assertNotNull(characterWriteStream);
                        test.assertSame(byteWriteStream, characterWriteStream.asByteWriteStream());
                    }
                });

                runner.test("asCharacterWriteStream(CharacterEncoding)", new Action1<Test>()
                {
                    @Override
                    public void run(Test test)
                    {
                        final InMemoryByteWriteStream byteWriteStream = new InMemoryByteWriteStream();
                        final CharacterWriteStream characterWriteStream = byteWriteStream.asCharacterWriteStream(CharacterEncoding.US_ASCII);
                        test.assertNotNull(characterWriteStream);
                        test.assertSame(byteWriteStream, characterWriteStream.asByteWriteStream());
                    }
                });

                runner.test("asLineWriteStream()", new Action1<Test>()
                {
                    @Override
                    public void run(Test test)
                    {
                        final InMemoryByteWriteStream byteWriteStream = new InMemoryByteWriteStream();
                        final LineWriteStream lineWriteStream = byteWriteStream.asLineWriteStream();
                        test.assertNotNull(lineWriteStream);
                        test.assertSame(byteWriteStream, lineWriteStream.asByteWriteStream());
                    }
                });

                runner.test("asLineWriteStream(CharacterEncoding)", new Action1<Test>()
                {
                    @Override
                    public void run(Test test)
                    {
                        final InMemoryByteWriteStream byteWriteStream = new InMemoryByteWriteStream();
                        final LineWriteStream lineWriteStream = byteWriteStream.asLineWriteStream(CharacterEncoding.US_ASCII);
                        test.assertNotNull(lineWriteStream);
                        test.assertSame(byteWriteStream, lineWriteStream.asByteWriteStream());
                    }
                });

                runner.test("asLineWriteStream(String)", new Action1<Test>()
                {
                    @Override
                    public void run(Test test)
                    {
                        final InMemoryByteWriteStream byteWriteStream = new InMemoryByteWriteStream();
                        final LineWriteStream lineWriteStream = byteWriteStream.asLineWriteStream("\r\n");
                        test.assertNotNull(lineWriteStream);
                        test.assertEqual("\r\n", lineWriteStream.getLineSeparator());
                        test.assertSame(byteWriteStream, lineWriteStream.asByteWriteStream());
                    }
                });

                runner.test("asLineWriteStream(CharacterEncoding,String)", new Action1<Test>()
                {
                    @Override
                    public void run(Test test)
                    {
                        final InMemoryByteWriteStream byteWriteStream = new InMemoryByteWriteStream();
                        final LineWriteStream lineWriteStream = byteWriteStream.asLineWriteStream(CharacterEncoding.US_ASCII, "\r\n");
                        test.assertNotNull(lineWriteStream);
                        test.assertSame(CharacterEncoding.US_ASCII, lineWriteStream.getCharacterEncoding());
                        test.assertEqual("\r\n", lineWriteStream.getLineSeparator());
                        test.assertSame(byteWriteStream, lineWriteStream.asByteWriteStream());
                    }
                });
            }
        });
    }
}