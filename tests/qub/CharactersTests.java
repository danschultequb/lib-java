package qub;

public interface CharactersTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(Characters.class, () ->
        {
            runner.testGroup("join(Iterable<Character>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> Characters.join((Iterable<Character>)null),
                        new PreConditionFailure("values cannot be null."));
                });

                runner.test("with empty", (Test test) ->
                {
                    test.assertEqual("", Characters.join(Iterable.create()));
                });

                runner.test("with non-empty", (Test test) ->
                {
                    test.assertEqual("abc", Characters.join(Iterable.create('a', 'b', 'c')));
                });
            });

            runner.testGroup("join(char,Iterable<Character>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> Characters.join(' ', (Iterable<Character>)null),
                        new PreConditionFailure("values cannot be null."));
                });

                runner.test("with empty", (Test test) ->
                {
                    test.assertEqual("", Characters.join(' ', Iterable.create()));
                });

                runner.test("with non-empty", (Test test) ->
                {
                    test.assertEqual("a b c", Characters.join(' ', Iterable.create('a', 'b', 'c')));
                });
            });

            runner.testGroup("join(String,Iterable<Character>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> Characters.join("--", (Iterable<Character>)null),
                        new PreConditionFailure("values cannot be null."));
                });

                runner.test("with empty", (Test test) ->
                {
                    test.assertEqual("", Characters.join("--", Iterable.create()));
                });

                runner.test("with non-empty", (Test test) ->
                {
                    test.assertEqual("a--b--c", Characters.join("--", Iterable.create('a', 'b', 'c')));
                });
            });
        });
    }
}
