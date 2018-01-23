package qub;

public class OutputStreamWriterToCharacterWriteStreamTests
{
    public static void test(final TestRunner runner)
    {
        runner.testGroup("OutputStreamWriterToCharacterWriteStream", new Action0()
        {
            @Override
            public void run()
            {
                runner.test("constructor", new Action1<Test>()
                {
                    @Override
                    public void run(Test test)
                    {
                        final ByteWriteStream byteWriteStream = new InMemoryByteWriteStream();
                        final OutputStreamWriterToCharacterWriteStream characterWriteStream = new OutputStreamWriterToCharacterWriteStream(byteWriteStream, CharacterEncoding.US_ASCII);
                        test.assertSame(byteWriteStream, characterWriteStream.asByteWriteStream());
                        test.assertSame(CharacterEncoding.US_ASCII, characterWriteStream.getCharacterEncoding());
                    }
                });
                
                runner.testGroup("writeByte(byte)", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("with no exception", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                InMemoryByteWriteStream byteWriteStream = new InMemoryByteWriteStream();
                                final OutputStreamWriterToCharacterWriteStream characterWriteStream = getCharacterWriteStream(byteWriteStream);
                                test.assertTrue(characterWriteStream.write((byte)60));
                                test.assertEqual(new byte[] { 60 }, byteWriteStream.getBytes());
                            }
                        });
                        
                        runner.test("with exception", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                TestStubOutputStream outputStream = new TestStubOutputStream();
                                OutputStreamToByteWriteStream byteWriteStream = new OutputStreamToByteWriteStream(outputStream);
                                final OutputStreamWriterToCharacterWriteStream characterWriteStream = getCharacterWriteStream(byteWriteStream);
                                test.assertFalse(characterWriteStream.write((byte)60));
                            }
                        });
                    }
                });
                
                runner.testGroup("write(byte[])", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("with no exception", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                InMemoryByteWriteStream byteWriteStream = new InMemoryByteWriteStream();
                                final OutputStreamWriterToCharacterWriteStream characterWriteStream = getCharacterWriteStream(byteWriteStream);
                                test.assertTrue(characterWriteStream.write(new byte[] { 0, 1, 2 }));
                                test.assertEqual(new byte[] { 0, 1, 2 }, byteWriteStream.getBytes());
                            }
                        });
                        
                        runner.test("with exception", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                TestStubOutputStream outputStream = new TestStubOutputStream();
                                OutputStreamToByteWriteStream byteWriteStream = new OutputStreamToByteWriteStream(outputStream);
                                final OutputStreamWriterToCharacterWriteStream characterWriteStream = getCharacterWriteStream(byteWriteStream);
                                test.assertFalse(characterWriteStream.write(new byte[] { 3, 4, 5, 6 }));
                            }
                        });
                    }
                });
                
                runner.testGroup("write(byte[],int,int)", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("with no exception", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                InMemoryByteWriteStream byteWriteStream = new InMemoryByteWriteStream();
                                final OutputStreamWriterToCharacterWriteStream characterWriteStream = getCharacterWriteStream(byteWriteStream);
                                test.assertTrue(characterWriteStream.write(new byte[] { 0, 1, 2 }, 1, 2));
                                test.assertEqual(new byte[] { 1, 2 }, byteWriteStream.getBytes());
                            }
                        });
                        
                        runner.test("with exception", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                TestStubOutputStream outputStream = new TestStubOutputStream();
                                OutputStreamToByteWriteStream byteWriteStream = new OutputStreamToByteWriteStream(outputStream);
                                final OutputStreamWriterToCharacterWriteStream characterWriteStream = getCharacterWriteStream(byteWriteStream);
                                test.assertFalse(characterWriteStream.write(new byte[] { 3, 4, 5, 6 }, 2, 1));
                            }
                        });
                    }
                });
                
                runner.testGroup("write(char)", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("with no exception", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                InMemoryByteWriteStream byteWriteStream = new InMemoryByteWriteStream();
                                final OutputStreamWriterToCharacterWriteStream characterWriteStream = getCharacterWriteStream(byteWriteStream);
                                test.assertTrue(characterWriteStream.write('a'));
                                test.assertEqual(new byte[] { 97 }, byteWriteStream.getBytes());
                            }
                        });
                        
                        runner.test("with exception", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                TestStubOutputStream outputStream = new TestStubOutputStream();
                                OutputStreamToByteWriteStream byteWriteStream = new OutputStreamToByteWriteStream(outputStream);
                                final OutputStreamWriterToCharacterWriteStream characterWriteStream = getCharacterWriteStream(byteWriteStream);
                                test.assertFalse(characterWriteStream.write('a'));
                            }
                        });
                    }
                });
                
                runner.testGroup("write(String)", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("with no exception", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                InMemoryByteWriteStream byteWriteStream = new InMemoryByteWriteStream();
                                final OutputStreamWriterToCharacterWriteStream characterWriteStream = getCharacterWriteStream(byteWriteStream);
                                test.assertTrue(characterWriteStream.write("test"));
                                test.assertEqual(new byte[] { 116, 101, 115, 116 }, byteWriteStream.getBytes());
                            }
                        });
                        
                        runner.test("with exception", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                TestStubOutputStream outputStream = new TestStubOutputStream();
                                OutputStreamToByteWriteStream byteWriteStream = new OutputStreamToByteWriteStream(outputStream);
                                final OutputStreamWriterToCharacterWriteStream characterWriteStream = getCharacterWriteStream(byteWriteStream);
                                test.assertFalse(characterWriteStream.write("test again"));
                            }
                        });
                    }
                });
                
                runner.testGroup("close()", new Action0()
                {
                    @Override
                    public void run()
                    {
                        runner.test("with no exception", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                InMemoryByteWriteStream byteWriteStream = new InMemoryByteWriteStream();
                                final OutputStreamWriterToCharacterWriteStream characterWriteStream = getCharacterWriteStream(byteWriteStream);
                                characterWriteStream.close();
                                test.assertFalse(characterWriteStream.isOpen());
                                test.assertFalse(byteWriteStream.isOpen());

                                characterWriteStream.close();
                                test.assertFalse(characterWriteStream.isOpen());
                                test.assertFalse(byteWriteStream.isOpen());
                            }
                        });
                        
                        runner.test("with exception", new Action1<Test>()
                        {
                            @Override
                            public void run(Test test)
                            {
                                TestStubOutputStream outputStream = new TestStubOutputStream();
                                OutputStreamToByteWriteStream byteWriteStream = new OutputStreamToByteWriteStream(outputStream);
                                final OutputStreamWriterToCharacterWriteStream characterWriteStream = getCharacterWriteStream(byteWriteStream);
                                characterWriteStream.close();
                                test.assertTrue(characterWriteStream.isOpen());
                                test.assertTrue(byteWriteStream.isOpen());
                            }
                        });
                    }
                });
                
                runner.test("asByteWriteStream()", new Action1<Test>()
                {
                    @Override
                    public void run(Test test)
                    {
                        final ByteWriteStream byteWriteStream = new InMemoryByteWriteStream();
                        final OutputStreamWriterToCharacterWriteStream characterWriteStream = getCharacterWriteStream(byteWriteStream);
                        test.assertSame(byteWriteStream, characterWriteStream.asByteWriteStream());
                    }
                });
                
                runner.test("asLineWriteStream()", new Action1<Test>()
                {
                    @Override
                    public void run(Test test)
                    {
                        final OutputStreamWriterToCharacterWriteStream characterWriteStream = getCharacterWriteStream();
                        final LineWriteStream lineWriteStream = characterWriteStream.asLineWriteStream();
                        test.assertNotNull(lineWriteStream);
                        test.assertSame(characterWriteStream, lineWriteStream.asCharacterWriteStream());
                    }
                });

                runner.test("asLineWriteStream(String)", new Action1<Test>()
                {
                    @Override
                    public void run(Test test)
                    {
                        final OutputStreamWriterToCharacterWriteStream characterWriteStream = getCharacterWriteStream();
                        final LineWriteStream lineWriteStream = characterWriteStream.asLineWriteStream("\n");
                        test.assertNotNull(lineWriteStream);
                        test.assertEqual("\n", lineWriteStream.getLineSeparator());
                        test.assertSame(characterWriteStream, lineWriteStream.asCharacterWriteStream());
                    }
                });
            }
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
