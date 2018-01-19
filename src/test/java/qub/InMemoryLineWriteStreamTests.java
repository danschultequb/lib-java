package qub;

public class InMemoryLineWriteStreamTests
{
    public static void test(final TestRunner runner)
    {
        runner.testGroup("InMemoryLineWriteStream", new Action0()
        {
            @Override
            public void run()
            {
                runner.test("constructor(CharacterEncoding)", new Action1<Test>()
                {
                    @Override
                    public void run(Test test)
                    {
                        final InMemoryLineWriteStream writeStream = new InMemoryLineWriteStream(CharacterEncoding.US_ASCII);
                        test.assertEqual(CharacterEncoding.US_ASCII, writeStream.getCharacterEncoding());
                    }
                });

                runner.test("constructor(CharacterEncoding,String)", new Action1<Test>()
                {
                    @Override
                    public void run(Test test)
                    {
                        final InMemoryLineWriteStream writeStream = new InMemoryLineWriteStream(CharacterEncoding.US_ASCII, "\r\n");
                        test.assertEqual(CharacterEncoding.US_ASCII, writeStream.getCharacterEncoding());
                        test.assertEqual("\r\n", writeStream.getLineSeparator());
                    }
                });

                runner.test("getText()", new Action1<Test>()
                {
                    @Override
                    public void run(Test test)
                    {
                        final InMemoryLineWriteStream writeStream = new InMemoryLineWriteStream();
                        test.assertEqual("", writeStream.getText());

                        writeStream.writeLine("hello");
                        test.assertEqual("hello\n", writeStream.getText());
                    }
                });
            }
        });
    }
}
