package qub;

public interface CharacterArrayTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(CharacterArray.class, () ->
        {
            runner.testGroup("constructor(int)", () ->
            {
                runner.test("with negative", (Test test) ->
                {
                    test.assertThrows(() -> new CharacterArray(-1), new PreConditionFailure("count (-1) must be greater than or equal to 0."));
                });

                runner.test("with zero", (Test test) ->
                {
                    final CharacterArray array = new CharacterArray(0);
                    test.assertEqual(0, array.getCount());
                });

                runner.test("with positive", (Test test) ->
                {
                    final CharacterArray array = new CharacterArray(2);
                    test.assertEqual(2, array.getCount());
                });
            });

            runner.testGroup("constructor(char[])", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> new CharacterArray(null), new PreConditionFailure("characters cannot be null."));
                });

                runner.test("with empty", (Test test) ->
                {
                    final CharacterArray array = new CharacterArray(new char[0]);
                    test.assertEqual(0, array.getCount());
                });

                runner.test("with non-empty", (Test test) ->
                {
                    final CharacterArray array = new CharacterArray(new char[5]);
                    test.assertEqual(5, array.getCount());
                });
            });

            runner.testGroup("constructor(char[],int,int)", () ->
            {
                runner.test("with null values", (Test test) ->
                {
                    test.assertThrows(() -> new CharacterArray(null, 0, 0),
                        new PreConditionFailure("characters cannot be null."));
                });

                runner.test("with negative startIndex", (Test test) ->
                {
                    test.assertThrows(() -> new CharacterArray(new char[] { 'a', 'b', 'c' }, -1, 2),
                        new PreConditionFailure("startIndex (-1) must be between 0 and 2."));
                });

                runner.test("with startIndex equal to value count", (Test test) ->
                {
                    test.assertThrows(() -> new CharacterArray(new char[] { 'a', 'b', 'c' }, 3, 2),
                        new PreConditionFailure("startIndex (3) must be between 0 and 2."));
                });

                runner.test("with negative length", (Test test) ->
                {
                    test.assertThrows(() -> new CharacterArray(new char[] { 'a', 'b', 'c' }, 0, -1),
                        new PreConditionFailure("length (-1) must be between 1 and 3."));
                });

                runner.test("with length greater than array length", (Test test) ->
                {
                    test.assertThrows(() -> new CharacterArray(new char[] { 'a', 'b', 'c' }, 0, 4),
                        new PreConditionFailure("length (4) must be between 1 and 3."));
                });

                runner.test("with empty array", (Test test) ->
                {
                    test.assertThrows(() -> new CharacterArray(new char[0], 0, 0),
                        new PreConditionFailure("characters cannot be empty."));
                });

                runner.test("with complete non-empty array", (Test test) ->
                {
                    final CharacterArray array = new CharacterArray(new char[] { 'a', 'b', 'c' }, 0, 3);
                    test.assertEqual(Iterable.create('a', 'b', 'c'), array);
                });

                runner.test("with partial non-empty array", (Test test) ->
                {
                    final CharacterArray array = new CharacterArray(new char[] { 'a', 'b', 'c' }, 1, 2);
                    test.assertEqual(Iterable.create('b', 'c'), array);
                });
            });

            runner.testGroup("set(int,char)", () ->
            {
                runner.test("with negative index", (Test test) ->
                {
                    final CharacterArray array = new CharacterArray(new char[] { 'a', 'b', 'c' });
                    test.assertThrows(() -> array.set(-1, 'd'), new PreConditionFailure("index (-1) must be between 0 and 2."));
                });

                runner.test("with index equal to length", (Test test) ->
                {
                    final CharacterArray array = new CharacterArray(new char[] { 'a', 'b', 'c' });
                    test.assertThrows(() -> array.set(3, 'd'), new PreConditionFailure("index (3) must be between 0 and 2."));
                });

                runner.test("with valid index", (Test test) ->
                {
                    final CharacterArray array = new CharacterArray(new char[] { 'a', 'b', 'c' });
                    array.set(1, 'z');
                    test.assertEqual(Iterable.create('a', 'z', 'c'), array);
                });
            });

            runner.testGroup("set(int,Character)", () ->
            {
                runner.test("with negative index", (Test test) ->
                {
                    final CharacterArray array = new CharacterArray(new char[] { 'a', 'b', 'c' });
                    test.assertThrows(() -> array.set(-1, Character.valueOf('d')), new PreConditionFailure("index (-1) must be between 0 and 2."));
                });

                runner.test("with index equal to length", (Test test) ->
                {
                    final CharacterArray array = new CharacterArray(new char[] { 'a', 'b', 'c' });
                    test.assertThrows(() -> array.set(3, Character.valueOf('d')), new PreConditionFailure("index (3) must be between 0 and 2."));
                });

                runner.test("with null value", (Test test) ->
                {
                    final CharacterArray array = new CharacterArray(new char[] { 'a', 'b', 'c' });
                    test.assertThrows(() -> array.set(0, null), new PreConditionFailure("value cannot be null."));
                });

                runner.test("with valid index", (Test test) ->
                {
                    final CharacterArray array = new CharacterArray(new char[] { 'a', 'b', 'c' });
                    array.set(1, Character.valueOf('z'));
                    test.assertEqual(Iterable.create('a', 'z', 'c'), array);
                });
            });
        });
    }
}
