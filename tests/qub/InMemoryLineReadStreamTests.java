package qub;

public class InMemoryLineReadStreamTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(InMemoryLineReadStream.class, () ->
        {
            runner.testGroup("constructor(String)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final InMemoryLineReadStream lineReadStream = new InMemoryLineReadStream(null);
                    test.assertTrue(lineReadStream.isOpen());
                    test.assertEqual(CharacterEncoding.UTF_8, lineReadStream.getCharacterEncoding());
                    test.assertFalse(lineReadStream.getIncludeNewLines());
                    test.assertEqual(null, lineReadStream.readLine());
                });

                runner.test("with \"\"", (Test test) ->
                {
                    final InMemoryLineReadStream lineReadStream = new InMemoryLineReadStream("");
                    test.assertTrue(lineReadStream.isOpen());
                    test.assertEqual(CharacterEncoding.UTF_8, lineReadStream.getCharacterEncoding());
                    test.assertFalse(lineReadStream.getIncludeNewLines());
                    test.assertEqual(null, lineReadStream.readLine());
                });

                runner.test("with \"abcd\"", (Test test) ->
                {
                    final InMemoryLineReadStream lineReadStream = new InMemoryLineReadStream("abcd");
                    test.assertTrue(lineReadStream.isOpen());
                    test.assertEqual(CharacterEncoding.UTF_8, lineReadStream.getCharacterEncoding());
                    test.assertFalse(lineReadStream.getIncludeNewLines());
                    test.assertEqual("abcd", lineReadStream.readLine());
                    test.assertEqual(null, lineReadStream.readLine());
                });
            });
        });
    }
}
