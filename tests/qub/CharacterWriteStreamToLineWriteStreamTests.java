package qub;

public class CharacterWriteStreamToLineWriteStreamTests
{
    public static void test(final TestRunner runner)
    {
        runner.testGroup("CharacterWriteStreamToLineWriteStream", new Action0()
        {
            @Override
            public void run()
            {
                runner.test("write(byte)", new Action1<Test>()
                {
                    @Override
                    public void run(Test test)
                    {
                        final InMemoryByteWriteStream byteWriteStream = new InMemoryByteWriteStream();
                        final CharacterWriteStreamToLineWriteStream lineWriteStream = getLineWriteStream(byteWriteStream);
                        test.assertTrue(lineWriteStream.write((byte)50));
                        test.assertEqual(new byte[] { 50 }, byteWriteStream.getBytes());
                    }
                });
                
                runner.test("write(byte[])", new Action1<Test>()
                {
                    @Override
                    public void run(Test test)
                    {
                        final InMemoryByteWriteStream byteWriteStream = new InMemoryByteWriteStream();
                        final CharacterWriteStreamToLineWriteStream lineWriteStream = getLineWriteStream(byteWriteStream);
                        test.assertTrue(lineWriteStream.write(new byte[] { 1, 2, 3, 4 }));
                        test.assertEqual(new byte[] { 1, 2, 3, 4 }, byteWriteStream.getBytes());
                    }
                });
                
                runner.test("write(byte[],int,int)", new Action1<Test>()
                {
                    @Override
                    public void run(Test test)
                    {
                        final InMemoryByteWriteStream byteWriteStream = new InMemoryByteWriteStream();
                        final CharacterWriteStreamToLineWriteStream lineWriteStream = getLineWriteStream(byteWriteStream);
                        test.assertTrue(lineWriteStream.write(new byte[] { 1, 2, 3, 4 }, 3, 1));
                        test.assertEqual(new byte[] { 4 }, byteWriteStream.getBytes());
                    }
                });
                
                runner.test("write(char)", new Action1<Test>()
                {
                    @Override
                    public void run(Test test)
                    {
                        final InMemoryCharacterWriteStream characterWriteStream = new InMemoryCharacterWriteStream();
                        final CharacterWriteStreamToLineWriteStream lineWriteStream = getLineWriteStream(characterWriteStream);
                        test.assertTrue(lineWriteStream.write('z'));
                        test.assertEqual(new byte[] { 122 }, characterWriteStream.getBytes());
                        test.assertEqual("z", characterWriteStream.getText());
                    }
                });

                runner.test("write(String)", new Action1<Test>()
                {
                    @Override
                    public void run(Test test)
                    {
                        final InMemoryByteWriteStream byteWriteStream = new InMemoryByteWriteStream();
                        final InMemoryCharacterWriteStream characterWriteStream = new InMemoryCharacterWriteStream(byteWriteStream);
                        final CharacterWriteStreamToLineWriteStream lineWriteStream = getLineWriteStream(characterWriteStream);
                        test.assertTrue(lineWriteStream.write("tuv"));
                        test.assertEqual(new byte[] { 116, 117, 118 }, byteWriteStream.getBytes());
                        test.assertEqual("tuv", characterWriteStream.getText());
                    }
                });

                runner.test("writeLine()", new Action1<Test>()
                {
                    @Override
                    public void run(Test test)
                    {
                        final InMemoryByteWriteStream byteWriteStream = new InMemoryByteWriteStream();
                        final InMemoryCharacterWriteStream characterWriteStream = new InMemoryCharacterWriteStream(byteWriteStream);
                        final CharacterWriteStreamToLineWriteStream lineWriteStream = getLineWriteStream(characterWriteStream);
                        test.assertTrue(lineWriteStream.writeLine());
                        test.assertEqual(new byte[] { 10 }, byteWriteStream.getBytes());
                        test.assertEqual("\n", characterWriteStream.getText());
                    }
                });

                runner.test("writeLine(String)", new Action1<Test>()
                {
                    @Override
                    public void run(Test test)
                    {
                        final InMemoryByteWriteStream byteWriteStream = new InMemoryByteWriteStream();
                        final InMemoryCharacterWriteStream characterWriteStream = new InMemoryCharacterWriteStream(byteWriteStream);
                        final CharacterWriteStreamToLineWriteStream lineWriteStream = getLineWriteStream(characterWriteStream);
                        test.assertTrue(lineWriteStream.writeLine("hello"));
                        test.assertEqual(new byte[] { 104, 101, 108, 108, 111, 10 }, byteWriteStream.getBytes());
                        test.assertEqual("hello\n", characterWriteStream.getText());
                    }
                });
            }
        });
    }

    private static CharacterWriteStreamToLineWriteStream getLineWriteStream(ByteWriteStream byteWriteStream)
    {
        return getLineWriteStream(byteWriteStream.asCharacterWriteStream());
    }

    private static CharacterWriteStreamToLineWriteStream getLineWriteStream(CharacterWriteStream characterWriteStream)
    {
        return new CharacterWriteStreamToLineWriteStream(characterWriteStream);
    }
}
