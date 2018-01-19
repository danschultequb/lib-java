package qub;

public class InMemoryCharacterWriteStreamTests
{
    public static void test(final TestRunner runner)
    {
        runner.testGroup("InMemoryCharacterWriteStream", new Action0()
        {
            @Override
            public void run()
            {
                runner.test("constructor(CharacterEncoding)", new Action1<Test>()
                {
                    @Override
                    public void run(Test test)
                    {
                        final InMemoryCharacterWriteStream writeStream = new InMemoryCharacterWriteStream(CharacterEncoding.US_ASCII);
                        test.assertEqual(CharacterEncoding.US_ASCII, writeStream.getCharacterEncoding());
                    }
                });
            }
        });
    }
}
