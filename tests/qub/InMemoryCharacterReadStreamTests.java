package qub;

public class InMemoryCharacterReadStreamTests
{
    public static void test(final TestRunner runner)
    {
        runner.testGroup("InMemoryCharacterReadStream", new Action0()
        {
            @Override
            public void run()
            {
                runner.test("readCharacters(int)", new Action1<Test>()
                {
                    @Override
                    public void run(Test test)
                    {
                        final InMemoryCharacterReadStream characterReadStream = new InMemoryCharacterReadStream("abc");
                        test.assertEqual(new char[] { 'a', 'b', 'c' }, characterReadStream.readCharacters(20));
                        test.assertNull(characterReadStream.readCharacters(1));
                    }
                });

                runner.test("readCharacters(char[])", new Action1<Test>()
                {
                    @Override
                    public void run(Test test)
                    {
                        final InMemoryCharacterReadStream characterReadStream = new InMemoryCharacterReadStream("abc");
                        final char[] characters = new char[2];

                        test.assertEqual(2, characterReadStream.readCharacters(characters));
                        test.assertEqual(new char[] { 'a', 'b' }, characters);

                        test.assertEqual(1, characterReadStream.readCharacters(characters));
                        test.assertEqual(new char[] { 'c', 'b' }, characters);

                        test.assertEqual(-1, characterReadStream.readCharacters(characters));
                        test.assertEqual(new char[] { 'c', 'b' }, characters);
                    }
                });

                runner.test("asLineReadStream()", new Action1<Test>()
                {
                    @Override
                    public void run(Test test)
                    {
                        final InMemoryCharacterReadStream characterReadStream = new InMemoryCharacterReadStream();
                        final LineReadStream lineReadStream = characterReadStream.asLineReadStream();
                        test.assertNotNull(lineReadStream);
                    }
                });
            }
        });
    }
}
