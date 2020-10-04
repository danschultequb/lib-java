package qub;

public interface CharacterListTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(CharacterList.class, () ->
        {
            runner.testGroup("create(char...)", () ->
            {
                runner.test("with null char array", (Test test) ->
                {
                    test.assertThrows(() -> CharacterList.create((char[])null),
                        new PreConditionFailure("characters cannot be null."));
                });

                runner.test("with no arguments", (Test test) ->
                {
                    final CharacterList list = CharacterList.create();
                    test.assertNotNull(list);
                    test.assertEqual(0, list.getCount());
                });

                runner.test("with empty array", (Test test) ->
                {
                    final char[] values = new char[0];
                    final CharacterList list = CharacterList.create(values);
                    test.assertNotNull(list);
                    test.assertEqual(0, list.getCount());
                });

                runner.test("with non-empty array", (Test test) ->
                {
                    final char[] values = new char[] { 'a' };
                    final CharacterList list = CharacterList.create(values);
                    test.assertNotNull(list);
                    test.assertEqual(1, list.getCount());
                    test.assertEqual('a', list.get(0));
                    list.set(0, 'b');
                    test.assertEqual('a', values[0]);
                    test.assertEqual('b', list.get(0));
                });

                runner.test("with non-empty char arguments", (Test test) ->
                {
                    final CharacterList list = CharacterList.create('a', 'b', 'c');
                    test.assertNotNull(list);
                    test.assertEqual(3, list.getCount());
                    test.assertEqual('a', list.get(0));
                    test.assertEqual('b', list.get(1));
                    test.assertEqual('c', list.get(2));
                });
            });

            runner.testGroup("any()", () ->
            {
                runner.test("when empty", (Test test) ->
                {
                    test.assertFalse(CharacterList.create().any());
                });

                runner.test("when not empty", (Test test) ->
                {
                    test.assertTrue(CharacterList.create('a').any());
                });
            });

            runner.testGroup("insert(int,char)", () ->
            {
                runner.test("with negative index", (Test test) ->
                {
                    final CharacterList list = CharacterList.create();
                    test.assertThrows(() -> list.insert(-1, 'a'), new PreConditionFailure("insertIndex (-1) must be equal to 0."));
                    test.assertEqual(0, list.getCount());
                });

                runner.test("with zero index on empty list", (Test test) ->
                {
                    final CharacterList list = CharacterList.create();
                    list.insert(0, 'a');
                    test.assertEqual(1, list.getCount());
                    test.assertEqual('a', list.get(0));
                });

                runner.test("with positive index on empty list", (Test test) ->
                {
                    final CharacterList list = CharacterList.create();
                    test.assertThrows(() -> list.insert(1, 'a'), new PreConditionFailure("insertIndex (1) must be equal to 0."));
                    test.assertEqual(0, list.getCount());
                });

                runner.test("with zero index on non-empty list", (Test test) ->
                {
                    final CharacterList list = CharacterList.create('a', 'b', 'c');
                    list.insert(0, 'd');
                    test.assertEqual(CharacterList.create('d', 'a', 'b', 'c'), list);
                });

                runner.test("with positive index less than count on non-empty list", (Test test) ->
                {
                    final CharacterList list = CharacterList.create('a', 'b', 'c');
                    list.insert(2, 'd');
                    test.assertEqual(CharacterList.create('a', 'b', 'd', 'c'), list);
                });

                runner.test("with positive index equal to count on non-empty list", (Test test) ->
                {
                    final CharacterList list = CharacterList.create('a', 'b', 'c');
                    list.insert(3, 'd');
                    test.assertEqual(CharacterList.create('a', 'b', 'c', 'd'), list);
                });

                runner.test("with positive index greater than count on non-empty list", (Test test) ->
                {
                    final CharacterList list = CharacterList.create('a', 'b', 'c');
                    test.assertThrows(() -> list.insert(4, 'd'), new PreConditionFailure("insertIndex (4) must be between 0 and 3."));
                    test.assertEqual(CharacterList.create('a', 'b', 'c'), list);
                });
            });

            runner.testGroup("insert(int,char)", () ->
            {
                runner.test("with negative index", (Test test) ->
                {
                    final CharacterList list = CharacterList.create();
                    test.assertThrows(() -> list.insert(-1, (char)5), new PreConditionFailure("insertIndex (-1) must be equal to 0."));
                    test.assertEqual(0, list.getCount());
                });

                runner.test("with zero index on empty list", (Test test) ->
                {
                    final CharacterList list = CharacterList.create();
                    list.insert(0, 'a');
                    test.assertEqual(1, list.getCount());
                    test.assertEqual('a', list.get(0));
                });

                runner.test("with positive index on empty list", (Test test) ->
                {
                    final CharacterList list = CharacterList.create();
                    test.assertThrows(() -> list.insert(1, 'a'), new PreConditionFailure("insertIndex (1) must be equal to 0."));
                    test.assertEqual(0, list.getCount());
                });

                runner.test("with zero index on non-empty list", (Test test) ->
                {
                    final CharacterList list = CharacterList.create('a', 'b', 'c');
                    list.insert(0, 'd');
                    test.assertEqual(CharacterList.create('d', 'a', 'b', 'c'), list);
                });

                runner.test("with positive index less than count on non-empty list", (Test test) ->
                {
                    final CharacterList list = CharacterList.create('a', 'b', 'c');
                    list.insert(2, 'd');
                    test.assertEqual(CharacterList.create('a', 'b', 'd', 'c'), list);
                });

                runner.test("with positive index equal to count on non-empty list", (Test test) ->
                {
                    final CharacterList list = CharacterList.create('a', 'b', 'c');
                    list.insert(3, 'd');
                    test.assertEqual(CharacterList.create('a', 'b', 'c', 'd'), list);
                });

                runner.test("with positive index greater than count on non-empty list", (Test test) ->
                {
                    final CharacterList list = CharacterList.create('a', 'b', 'c');
                    test.assertThrows(() -> list.insert(4, 'd'), new PreConditionFailure("insertIndex (4) must be between 0 and 3."));
                    test.assertEqual(CharacterList.create('a', 'b', 'c'), list);
                });

                runner.test("with index less than count when the list doesn't need to grow", (Test test) ->
                {
                    final CharacterList list = CharacterList.create('a', 'b', 'c');
                    list.add('d'); // Initiate growth.
                    list.insert(0, 'e');
                    test.assertEqual(CharacterList.create('e', 'a', 'b', 'c', 'd'), list);
                });
            });

            runner.testGroup("insert(int,Character)", () ->
            {
                runner.test("with negative index", (Test test) ->
                {
                    final CharacterList list = CharacterList.create();
                    test.assertThrows(() -> list.insert(-1, Character.valueOf('a')), new PreConditionFailure("insertIndex (-1) must be equal to 0."));
                    test.assertEqual(0, list.getCount());
                });

                runner.test("with null value", (Test test) ->
                {
                    final CharacterList list = CharacterList.create();
                    test.assertThrows(() -> list.insert(0, null), new PreConditionFailure("value cannot be null."));
                    test.assertEqual(0, list.getCount());
                });

                runner.test("with zero index on empty list", (Test test) ->
                {
                    final CharacterList list = CharacterList.create();
                    list.insert(0, Character.valueOf('a'));
                    test.assertEqual(1, list.getCount());
                    test.assertEqual('a', list.get(0));
                });

                runner.test("with positive index on empty list", (Test test) ->
                {
                    final CharacterList list = CharacterList.create();
                    test.assertThrows(() -> list.insert(1, Character.valueOf('a')), new PreConditionFailure("insertIndex (1) must be equal to 0."));
                    test.assertEqual(0, list.getCount());
                });

                runner.test("with zero index on non-empty list", (Test test) ->
                {
                    final CharacterList list = CharacterList.create('a', 'b', 'c');
                    list.insert(0, Character.valueOf('d'));
                    test.assertEqual(CharacterList.create('d', 'a', 'b', 'c'), list);
                });

                runner.test("with positive index less than count on non-empty list", (Test test) ->
                {
                    final CharacterList list = CharacterList.create('a', 'b', 'c');
                    list.insert(2, Character.valueOf('d'));
                    test.assertEqual(CharacterList.create('a', 'b', 'd', 'c'), list);
                });

                runner.test("with positive index equal to count on non-empty list", (Test test) ->
                {
                    final CharacterList list = CharacterList.create('a', 'b', 'c');
                    list.insert(3, Character.valueOf('d'));
                    test.assertEqual(CharacterList.create('a', 'b', 'c', 'd'), list);
                });

                runner.test("with positive index greater than count on non-empty list", (Test test) ->
                {
                    final CharacterList list = CharacterList.create('a', 'b', 'c');
                    test.assertThrows(() -> list.insert(4, Character.valueOf('d')), new PreConditionFailure("insertIndex (4) must be between 0 and 3."));
                    test.assertEqual(CharacterList.create('a', 'b', 'c'), list);
                });

                runner.test("with index less than count when the list doesn't need to grow", (Test test) ->
                {
                    final CharacterList list = CharacterList.create('a', 'b', 'c');
                    list.add('d'); // Initiate growth.
                    list.insert(0, Character.valueOf('e'));
                    test.assertEqual(CharacterList.create('e', 'a', 'b', 'c', 'd'), list);
                });
            });

            runner.testGroup("removeAt(int)", () ->
            {
                runner.test("with negative index with an empty list", (Test test) ->
                {
                    final CharacterList list = CharacterList.create();
                    test.assertThrows(() -> list.removeAt(-1), new PreConditionFailure("Indexable length (0) must be greater than or equal to 1."));
                    test.assertEqual(CharacterList.create(), list);
                });

                runner.test("with 0 index with an empty list", (Test test) ->
                {
                    final CharacterList list = CharacterList.create();
                    test.assertThrows(() -> list.removeAt(0), new PreConditionFailure("Indexable length (0) must be greater than or equal to 1."));
                    test.assertEqual(CharacterList.create(), list);
                });

                runner.test("with 1 index with an empty list", (Test test) ->
                {
                    final CharacterList list = CharacterList.create();
                    test.assertThrows(() -> list.removeAt(1), new PreConditionFailure("Indexable length (0) must be greater than or equal to 1."));
                    test.assertEqual(CharacterList.create(), list);
                });

                runner.test("with negative index with a non-empty list", (Test test) ->
                {
                    final CharacterList list = CharacterList.create('a', 'b', 'c');
                    test.assertThrows(() -> list.removeAt(-1), new PreConditionFailure("index (-1) must be between 0 and 2."));
                    test.assertEqual(CharacterList.create('a', 'b', 'c'), list);
                });

                runner.test("with 0 index with a non-empty list", (Test test) ->
                {
                    final CharacterList list = CharacterList.create('a', 'b', 'c');
                    test.assertEqual('a', list.removeAt(0));
                    test.assertEqual(CharacterList.create('b', 'c'), list);
                });

                runner.test("with 1 index with a non-empty list", (Test test) ->
                {
                    final CharacterList list = CharacterList.create('a', 'b', 'c', 'd', 'e', 'f');
                    test.assertEqual('b', list.removeAt(1));
                    test.assertEqual(CharacterList.create('a', 'c', 'd', 'e', 'f'), list);
                });

                runner.test("with count - 1 index with a non-empty list", (Test test) ->
                {
                    final CharacterList list = CharacterList.create('a', 'b', 'c');
                    test.assertEqual('c', list.removeAt(list.getCount() - 1));
                    test.assertEqual(CharacterList.create('a', 'b'), list);
                });

                runner.test("with count index with a non-empty list", (Test test) ->
                {
                    final CharacterList list = CharacterList.create('a', 'b', 'c');
                    test.assertThrows(() -> list.removeAt(list.getCount()), new PreConditionFailure("index (3) must be between 0 and 2."));
                    test.assertEqual(CharacterList.create('a', 'b', 'c'), list);
                });

                runner.test("with count + 1 index with a non-empty list", (Test test) ->
                {
                    final CharacterList list = CharacterList.create('a', 'b', 'c');
                    test.assertThrows(() -> list.removeAt(list.getCount() + 1), new PreConditionFailure("index (4) must be between 0 and 2."));
                    test.assertEqual(CharacterList.create('a', 'b', 'c'), list);
                });
            });

            runner.testGroup("removeFirstCharacters(int)", () ->
            {
                runner.test("with negative", (Test test) ->
                {
                    final CharacterList list = CharacterList.create('a', 'b', 'c');
                    test.assertThrows(() -> list.removeFirstCharacters(-2),
                        new PreConditionFailure("valuesToRemove (-2) must be greater than or equal to 1."));
                    test.assertEqual(Iterable.create('a', 'b', 'c'), list);
                });

                runner.test("with zero", (Test test) ->
                {
                    final CharacterList list = CharacterList.create('a', 'b', 'c');
                    test.assertThrows(() -> list.removeFirstCharacters(0),
                        new PreConditionFailure("valuesToRemove (0) must be greater than or equal to 1."));
                    test.assertEqual(Iterable.create('a', 'b', 'c'), list);
                });

                runner.test("with fewer than count", (Test test) ->
                {
                    final CharacterList list = CharacterList.create('a', 'b', 'c');
                    test.assertEqual(Iterable.create('a', 'b'), list.removeFirstCharacters(2));
                    test.assertEqual(Iterable.create('c'), list);
                });

                runner.test("with equal to count", (Test test) ->
                {
                    final CharacterList list = CharacterList.create('a', 'b', 'c');
                    test.assertEqual(Iterable.create('a', 'b', 'c'), list.removeFirstCharacters(3));
                    test.assertEqual(Iterable.create(), list);
                });

                runner.test("with more than count", (Test test) ->
                {
                    final CharacterList list = CharacterList.create('a', 'b', 'c');
                    test.assertThrows(() -> list.removeFirstCharacters(4),
                        new PreConditionFailure("length (4) must be between 1 and 3."));
                    test.assertEqual(Iterable.create('a', 'b', 'c'), list);
                });
            });

            runner.testGroup("removeFirstCharacters(char[])", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final CharacterList list = CharacterList.create('a', 'b', 'c');
                    test.assertThrows(() -> list.removeFirstCharacters((char[])null),
                        new PreConditionFailure("outputCharacters cannot be null."));
                    test.assertEqual(Iterable.create('a', 'b', 'c'), list);
                });

                runner.test("with empty", (Test test) ->
                {
                    final CharacterList list = CharacterList.create('a', 'b', 'c');
                    test.assertThrows(() -> list.removeFirstCharacters(new char[0]),
                        new PreConditionFailure("outputCharacters cannot be empty."));
                    test.assertEqual(Iterable.create('a', 'b', 'c'), list);
                });

                runner.test("with fewer than count", (Test test) ->
                {
                    final CharacterList list = CharacterList.create('a', 'b', 'c');
                    final char[] outputCharacters = new char[2];
                    list.removeFirstCharacters(outputCharacters);
                    test.assertEqual(Iterable.create('a', 'b'), CharacterList.create(outputCharacters));
                    test.assertEqual(Iterable.create('c'), list);
                });

                runner.test("with equal to count", (Test test) ->
                {
                    final CharacterList list = CharacterList.create('a', 'b', 'c');
                    final char[] outputCharacters = new char[3];
                    list.removeFirstCharacters(outputCharacters);
                    test.assertEqual(Iterable.create('a', 'b', 'c'), CharacterList.create(outputCharacters));
                    test.assertEqual(Iterable.create(), list);
                });

                runner.test("with more than count", (Test test) ->
                {
                    final CharacterList list = CharacterList.create('a', 'b', 'c');
                    final char[] outputCharacters = new char[4];
                    test.assertThrows(() -> list.removeFirstCharacters(outputCharacters),
                        new PreConditionFailure("length (4) must be between 1 and 3."));
                    test.assertEqual(Iterable.create('a', 'b', 'c'), list);
                });
            });

            runner.testGroup("set(int,char)", () ->
            {
                runner.test("with negative index", (Test test) ->
                {
                    final CharacterList list = CharacterList.create('a', 'b', 'c');
                    test.assertThrows(() -> list.set(-1, (char)5), new PreConditionFailure("index (-1) must be between 0 and 2."));
                    test.assertEqual(CharacterList.create('a', 'b', 'c'), list);
                });

                runner.test("with zero index", (Test test) ->
                {
                    final CharacterList list = CharacterList.create('a', 'b', 'c');
                    list.set(0, 'd');
                    test.assertEqual(CharacterList.create('d', 'b', 'c'), list);
                });

                runner.test("with count - 1 index", (Test test) ->
                {
                    final CharacterList list = CharacterList.create('a', 'b', 'c');
                    list.set(2, 'd');
                    test.assertEqual(CharacterList.create('a', 'b', 'd'), list);
                });

                runner.test("with count index", (Test test) ->
                {
                    final CharacterList list = CharacterList.create('a', 'b', 'c');
                    test.assertThrows(() -> list.set(3, (char)5), new PreConditionFailure("index (3) must be between 0 and 2."));
                    test.assertEqual(CharacterList.create('a', 'b', 'c'), list);
                });

                runner.test("with count + 1 index", (Test test) ->
                {
                    final CharacterList list = CharacterList.create('a', 'b', 'c');
                    test.assertThrows(() -> list.set(4, (char)5), new PreConditionFailure("index (4) must be between 0 and 2."));
                    test.assertEqual(CharacterList.create('a', 'b', 'c'), list);
                });
            });

            runner.testGroup("set(int,Character)", () ->
            {
                runner.test("with negative index", (Test test) ->
                {
                    final CharacterList list = CharacterList.create('a', 'b', 'c');
                    test.assertThrows(() -> list.set(-1, Character.valueOf('d')), new PreConditionFailure("index (-1) must be between 0 and 2."));
                    test.assertEqual(CharacterList.create('a', 'b', 'c'), list);
                });

                runner.test("with zero index", (Test test) ->
                {
                    final CharacterList list = CharacterList.create('a', 'b', 'c');
                    list.set(0, Character.valueOf('d'));
                    test.assertEqual(CharacterList.create('d', 'b', 'c'), list);
                });

                runner.test("with count - 1 index", (Test test) ->
                {
                    final CharacterList list = CharacterList.create('a', 'b', 'c');
                    list.set(2, Character.valueOf('d'));
                    test.assertEqual(CharacterList.create('a', 'b', 'd'), list);
                });

                runner.test("with count index", (Test test) ->
                {
                    final CharacterList list = CharacterList.create('a', 'b', 'c');
                    test.assertThrows(() -> list.set(3, Character.valueOf('d')), new PreConditionFailure("index (3) must be between 0 and 2."));
                    test.assertEqual(CharacterList.create('a', 'b', 'c'), list);
                });

                runner.test("with count + 1 index", (Test test) ->
                {
                    final CharacterList list = CharacterList.create('a', 'b', 'c');
                    test.assertThrows(() -> list.set(4, Character.valueOf('d')), new PreConditionFailure("index (4) must be between 0 and 2."));
                    test.assertEqual(CharacterList.create('a', 'b', 'c'), list);
                });
            });

            runner.testGroup("get(int)", () ->
            {
                runner.test("with negative index", (Test test) ->
                {
                    final CharacterList list = CharacterList.create('a', 'b', 'c');
                    test.assertThrows(() -> list.get(-1), new PreConditionFailure("index (-1) must be between 0 and 2."));
                });

                runner.test("with zero index", (Test test) ->
                {
                    final CharacterList list = CharacterList.create('a', 'b', 'c');
                    test.assertEqual('a', list.get(0));
                });

                runner.test("with count - 1 index", (Test test) ->
                {
                    final CharacterList list = CharacterList.create('a', 'b', 'c');
                    test.assertEqual('c', list.get(2));
                });

                runner.test("with count index", (Test test) ->
                {
                    final CharacterList list = CharacterList.create('a', 'b', 'c');
                    test.assertThrows(() -> list.get(3), new PreConditionFailure("index (3) must be between 0 and 2."));
                });

                runner.test("with count + 1 index", (Test test) ->
                {
                    final CharacterList list = CharacterList.create('a', 'b', 'c');
                    test.assertThrows(() -> list.get(4), new PreConditionFailure("index (4) must be between 0 and 2."));
                });
            });

            runner.testGroup("toString()", () ->
            {
                final Action2<CharacterList,String> toStringTest = (CharacterList list, String expected) ->
                {
                    runner.test("with " + list.toString(), (Test test) ->
                    {
                        test.assertEqual(expected, list.toString());
                    });
                };

                toStringTest.run(CharacterList.create(), "");
                toStringTest.run(CharacterList.create('a'), "a");
                toStringTest.run(CharacterList.create('x', 'y'), "xy");
            });

            runner.testGroup("toString(boolean)", () ->
            {
                final Action3<CharacterList,Boolean,String> toStringTest = (CharacterList list, Boolean asString, String expected) ->
                {
                    runner.test("with " + list.toString(), (Test test) ->
                    {
                        test.assertEqual(expected, list.toString(asString));
                    });
                };

                toStringTest.run(CharacterList.create(), true, "");
                toStringTest.run(CharacterList.create('a'), true, "a");
                toStringTest.run(CharacterList.create('x', 'y'), true, "xy");

                toStringTest.run(CharacterList.create(), false, "[]");
                toStringTest.run(CharacterList.create('a'), false, "[a]");
                toStringTest.run(CharacterList.create('x', 'y'), false, "[x,y]");
            });
        });
    }
}
