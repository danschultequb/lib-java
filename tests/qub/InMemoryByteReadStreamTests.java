package qub;

public class InMemoryByteReadStreamTests
{
    public static void test(final TestRunner runner)
    {
        runner.testGroup("InMemoryByteReadStream", new Action0()
        {
            @Override
            public void run()
            {
                runner.test("constructor()", new Action1<Test>()
                {
                    @Override
                    public void run(Test test)
                    {
                        final InMemoryByteReadStream readStream = new InMemoryByteReadStream();
                        test.assertTrue(readStream.isOpen());
                    }
                });
                
                runner.test("close()", new Action1<Test>()
                {
                    @Override
                    public void run(Test test)
                    {
                        final InMemoryByteReadStream readStream = new InMemoryByteReadStream();
                        readStream.close();
                        test.assertFalse(readStream.isOpen());
                        readStream.close();
                        test.assertFalse(readStream.isOpen());
                    }
                });
                
                runner.test("readBytes(int)", new Action1<Test>()
                {
                    @Override
                    public void run(Test test)
                    {
                        InMemoryByteReadStream readStream = new InMemoryByteReadStream();

                        test.assertNull(readStream.readBytes(-5));
                        test.assertNull(readStream.readBytes(0));
                        test.assertNull(readStream.readBytes(1));
                        test.assertNull(readStream.readBytes(2));

                        readStream = new InMemoryByteReadStream(new byte[] { 0, 1, 2, 3 });

                        test.assertNull(readStream.readBytes(-5));
                        test.assertNull(readStream.readBytes(0));
                        test.assertEqual(new byte[] { 0 }, readStream.readBytes(1));
                        test.assertEqual(new byte[] { 1, 2 }, readStream.readBytes(2));
                        test.assertEqual(new byte[] { 3 }, readStream.readBytes(3));
                        test.assertNull(readStream.readBytes(1));

                        readStream.close();

                        test.assertNull(readStream.readBytes(1));
                    }
                });

                runner.test("asLineReadStream()", new Action1<Test>()
                {
                    @Override
                    public void run(Test test)
                    {
                        final InMemoryByteReadStream byteReadStream = new InMemoryByteReadStream();
                        final LineReadStream lineReadStream = byteReadStream.asLineReadStream();
                        test.assertEqual(CharacterEncoding.UTF_8, lineReadStream.getCharacterEncoding());
                        test.assertFalse(lineReadStream.getIncludeNewLines());
                        test.assertSame(byteReadStream, lineReadStream.asByteReadStream());
                    }
                });

                runner.test("asLineReadStream(CharacterEncoding)", new Action1<Test>()
                {
                    @Override
                    public void run(Test test)
                    {
                        final InMemoryByteReadStream byteReadStream = new InMemoryByteReadStream();
                        final LineReadStream lineReadStream = byteReadStream.asLineReadStream(CharacterEncoding.US_ASCII);
                        test.assertEqual(CharacterEncoding.US_ASCII, lineReadStream.getCharacterEncoding());
                        test.assertFalse(lineReadStream.getIncludeNewLines());
                        test.assertSame(byteReadStream, lineReadStream.asByteReadStream());
                    }
                });

                runner.test("asLineReadStream(boolean)", new Action1<Test>()
                {
                    @Override
                    public void run(Test test)
                    {
                        final InMemoryByteReadStream byteReadStream = new InMemoryByteReadStream();
                        final LineReadStream lineReadStream = byteReadStream.asLineReadStream(true);
                        test.assertEqual(CharacterEncoding.UTF_8, lineReadStream.getCharacterEncoding());
                        test.assertTrue(lineReadStream.getIncludeNewLines());
                        test.assertSame(byteReadStream, lineReadStream.asByteReadStream());
                    }
                });

                runner.test("asLineReadStream(CharacterEncoding,boolean)", new Action1<Test>()
                {
                    @Override
                    public void run(Test test)
                    {
                        final InMemoryByteReadStream byteReadStream = new InMemoryByteReadStream();
                        final LineReadStream lineReadStream = byteReadStream.asLineReadStream(CharacterEncoding.UTF_8, false);
                        test.assertEqual(CharacterEncoding.UTF_8, lineReadStream.getCharacterEncoding());
                        test.assertFalse(lineReadStream.getIncludeNewLines());
                        test.assertSame(byteReadStream, lineReadStream.asByteReadStream());
                    }
                });
            }
        });
    }
}
