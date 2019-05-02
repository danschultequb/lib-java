package qub;

public class CharacterListTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(CharacterList.class, () ->
        {
            runner.testGroup("constructor(int)", () ->
            {
                runner.test("with negative capacity", (Test test) ->
                {
                    test.assertThrows(() -> new CharacterList(-1),
                        new PreConditionFailure("capacity (-1) must be greater than or equal to 0."));
                });

                runner.test("with zero capacity", (Test test) ->
                {
                    final CharacterList list = new CharacterList(0);
                    test.assertEqual(Iterable.create(), list);
                });

                runner.test("with positive capacity", (Test test) ->
                {
                    final CharacterList list = new CharacterList(10);
                    test.assertEqual(Iterable.create(), list);
                });
            });

            runner.testGroup("constructor(char...)", () ->
            {
                runner.test("with null char array", (Test test) ->
                {
                    final CharacterList list = new CharacterList((char[])null);
                    test.assertNotNull(list);
                    test.assertEqual(0, list.getCount());
                });

                runner.test("with no arguments", (Test test) ->
                {
                    final CharacterList list = new CharacterList();
                    test.assertNotNull(list);
                    test.assertEqual(0, list.getCount());
                });

                runner.test("with empty array", (Test test) ->
                {
                    final char[] values = new char[0];
                    final CharacterList list = new CharacterList(values);
                    test.assertNotNull(list);
                    test.assertEqual(0, list.getCount());
                });

                runner.test("with non-empty array", (Test test) ->
                {
                    final char[] values = new char[] { 'a' };
                    final CharacterList list = new CharacterList(values);
                    test.assertNotNull(list);
                    test.assertEqual(1, list.getCount());
                    test.assertEqual('a', list.get(0));
                    list.set(0, 'b');
                    test.assertEqual('a', values[0]);
                    test.assertEqual('b', list.get(0));
                });

                runner.test("with non-empty char arguments", (Test test) ->
                {
                    final CharacterList list = new CharacterList('a', 'b', 'c');
                    test.assertNotNull(list);
                    test.assertEqual(3, list.getCount());
                    test.assertEqual('a', list.get(0));
                    test.assertEqual('b', list.get(1));
                    test.assertEqual('c', list.get(2));
                });
            });

            runner.testGroup("createFromCharacters(char...)", () ->
            {
                runner.test("with null char array", (Test test) ->
                {
                    final CharacterList list = CharacterList.createFromCharacters((char[])null);
                    test.assertNotNull(list);
                    test.assertEqual(0, list.getCount());
                });

                runner.test("with no arguments", (Test test) ->
                {
                    final CharacterList list = CharacterList.createFromCharacters();
                    test.assertNotNull(list);
                    test.assertEqual(0, list.getCount());
                });

                runner.test("with empty array", (Test test) ->
                {
                    final char[] values = new char[0];
                    final CharacterList list = CharacterList.createFromCharacters(values);
                    test.assertNotNull(list);
                    test.assertEqual(0, list.getCount());
                });

                runner.test("with non-empty array", (Test test) ->
                {
                    final char[] values = new char[] { 'a' };
                    final CharacterList list = CharacterList.createFromCharacters(values);
                    test.assertNotNull(list);
                    test.assertEqual(1, list.getCount());
                    test.assertEqual('a', list.get(0));
                    list.set(0, 'b');
                    test.assertEqual('a', values[0]);
                    test.assertEqual('b', list.get(0));
                });

                runner.test("with non-empty char arguments", (Test test) ->
                {
                    final CharacterList list = CharacterList.createFromCharacters('a', 'b', 'c');
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
                    test.assertFalse(CharacterList.createFromCharacters().any());
                });

                runner.test("when not empty", (Test test) ->
                {
                    test.assertTrue(CharacterList.createFromCharacters('a').any());
                });
            });

            runner.testGroup("insert(int,char)", () ->
            {
                runner.test("with negative index", (Test test) ->
                {
                    final CharacterList list = CharacterList.createFromCharacters();
                    test.assertThrows(() -> list.insert(-1, 'a'), new PreConditionFailure("insertIndex (-1) must be equal to 0."));
                    test.assertEqual(0, list.getCount());
                });

                runner.test("with zero index on empty list", (Test test) ->
                {
                    final CharacterList list = CharacterList.createFromCharacters();
                    list.insert(0, 'a');
                    test.assertEqual(1, list.getCount());
                    test.assertEqual('a', list.get(0));
                });

                runner.test("with positive index on empty list", (Test test) ->
                {
                    final CharacterList list = CharacterList.createFromCharacters();
                    test.assertThrows(() -> list.insert(1, 'a'), new PreConditionFailure("insertIndex (1) must be equal to 0."));
                    test.assertEqual(0, list.getCount());
                });

                runner.test("with zero index on non-empty list", (Test test) ->
                {
                    final CharacterList list = CharacterList.createFromCharacters('a', 'b', 'c');
                    list.insert(0, 'd');
                    test.assertEqual(CharacterList.createFromCharacters('d', 'a', 'b', 'c'), list);
                });

                runner.test("with positive index less than count on non-empty list", (Test test) ->
                {
                    final CharacterList list = CharacterList.createFromCharacters('a', 'b', 'c');
                    list.insert(2, 'd');
                    test.assertEqual(CharacterList.createFromCharacters('a', 'b', 'd', 'c'), list);
                });

                runner.test("with positive index equal to count on non-empty list", (Test test) ->
                {
                    final CharacterList list = CharacterList.createFromCharacters('a', 'b', 'c');
                    list.insert(3, 'd');
                    test.assertEqual(CharacterList.createFromCharacters('a', 'b', 'c', 'd'), list);
                });

                runner.test("with positive index greater than count on non-empty list", (Test test) ->
                {
                    final CharacterList list = CharacterList.createFromCharacters('a', 'b', 'c');
                    test.assertThrows(() -> list.insert(4, 'd'), new PreConditionFailure("insertIndex (4) must be between 0 and 3."));
                    test.assertEqual(CharacterList.createFromCharacters('a', 'b', 'c'), list);
                });
            });

            runner.testGroup("insert(int,char)", () ->
            {
                runner.test("with negative index", (Test test) ->
                {
                    final CharacterList list = CharacterList.createFromCharacters();
                    test.assertThrows(() -> list.insert(-1, (char)5), new PreConditionFailure("insertIndex (-1) must be equal to 0."));
                    test.assertEqual(0, list.getCount());
                });

                runner.test("with zero index on empty list", (Test test) ->
                {
                    final CharacterList list = CharacterList.createFromCharacters();
                    list.insert(0, 'a');
                    test.assertEqual(1, list.getCount());
                    test.assertEqual('a', list.get(0));
                });

                runner.test("with positive index on empty list", (Test test) ->
                {
                    final CharacterList list = CharacterList.createFromCharacters();
                    test.assertThrows(() -> list.insert(1, 'a'), new PreConditionFailure("insertIndex (1) must be equal to 0."));
                    test.assertEqual(0, list.getCount());
                });

                runner.test("with zero index on non-empty list", (Test test) ->
                {
                    final CharacterList list = CharacterList.createFromCharacters('a', 'b', 'c');
                    list.insert(0, 'd');
                    test.assertEqual(CharacterList.createFromCharacters('d', 'a', 'b', 'c'), list);
                });

                runner.test("with positive index less than count on non-empty list", (Test test) ->
                {
                    final CharacterList list = CharacterList.createFromCharacters('a', 'b', 'c');
                    list.insert(2, 'd');
                    test.assertEqual(CharacterList.createFromCharacters('a', 'b', 'd', 'c'), list);
                });

                runner.test("with positive index equal to count on non-empty list", (Test test) ->
                {
                    final CharacterList list = CharacterList.createFromCharacters('a', 'b', 'c');
                    list.insert(3, 'd');
                    test.assertEqual(CharacterList.createFromCharacters('a', 'b', 'c', 'd'), list);
                });

                runner.test("with positive index greater than count on non-empty list", (Test test) ->
                {
                    final CharacterList list = CharacterList.createFromCharacters('a', 'b', 'c');
                    test.assertThrows(() -> list.insert(4, 'd'), new PreConditionFailure("insertIndex (4) must be between 0 and 3."));
                    test.assertEqual(CharacterList.createFromCharacters('a', 'b', 'c'), list);
                });

                runner.test("with index less than count when the list doesn't need to grow", (Test test) ->
                {
                    final CharacterList list = CharacterList.createFromCharacters('a', 'b', 'c');
                    list.add('d'); // Initiate growth.
                    list.insert(0, 'e');
                    test.assertEqual(CharacterList.createFromCharacters('e', 'a', 'b', 'c', 'd'), list);
                });
            });

            runner.testGroup("insert(int,Character)", () ->
            {
                runner.test("with negative index", (Test test) ->
                {
                    final CharacterList list = CharacterList.createFromCharacters();
                    test.assertThrows(() -> list.insert(-1, Character.valueOf('a')), new PreConditionFailure("insertIndex (-1) must be equal to 0."));
                    test.assertEqual(0, list.getCount());
                });

                runner.test("with null value", (Test test) ->
                {
                    final CharacterList list = CharacterList.createFromCharacters();
                    test.assertThrows(() -> list.insert(0, null), new PreConditionFailure("value cannot be null."));
                    test.assertEqual(0, list.getCount());
                });

                runner.test("with zero index on empty list", (Test test) ->
                {
                    final CharacterList list = CharacterList.createFromCharacters();
                    list.insert(0, Character.valueOf('a'));
                    test.assertEqual(1, list.getCount());
                    test.assertEqual('a', list.get(0));
                });

                runner.test("with positive index on empty list", (Test test) ->
                {
                    final CharacterList list = CharacterList.createFromCharacters();
                    test.assertThrows(() -> list.insert(1, Character.valueOf('a')), new PreConditionFailure("insertIndex (1) must be equal to 0."));
                    test.assertEqual(0, list.getCount());
                });

                runner.test("with zero index on non-empty list", (Test test) ->
                {
                    final CharacterList list = CharacterList.createFromCharacters('a', 'b', 'c');
                    list.insert(0, Character.valueOf('d'));
                    test.assertEqual(CharacterList.createFromCharacters('d', 'a', 'b', 'c'), list);
                });

                runner.test("with positive index less than count on non-empty list", (Test test) ->
                {
                    final CharacterList list = CharacterList.createFromCharacters('a', 'b', 'c');
                    list.insert(2, Character.valueOf('d'));
                    test.assertEqual(CharacterList.createFromCharacters('a', 'b', 'd', 'c'), list);
                });

                runner.test("with positive index equal to count on non-empty list", (Test test) ->
                {
                    final CharacterList list = CharacterList.createFromCharacters('a', 'b', 'c');
                    list.insert(3, Character.valueOf('d'));
                    test.assertEqual(CharacterList.createFromCharacters('a', 'b', 'c', 'd'), list);
                });

                runner.test("with positive index greater than count on non-empty list", (Test test) ->
                {
                    final CharacterList list = CharacterList.createFromCharacters('a', 'b', 'c');
                    test.assertThrows(() -> list.insert(4, Character.valueOf('d')), new PreConditionFailure("insertIndex (4) must be between 0 and 3."));
                    test.assertEqual(CharacterList.createFromCharacters('a', 'b', 'c'), list);
                });

                runner.test("with index less than count when the list doesn't need to grow", (Test test) ->
                {
                    final CharacterList list = CharacterList.createFromCharacters('a', 'b', 'c');
                    list.add('d'); // Initiate growth.
                    list.insert(0, Character.valueOf('e'));
                    test.assertEqual(CharacterList.createFromCharacters('e', 'a', 'b', 'c', 'd'), list);
                });
            });

            runner.testGroup("removeAt(int)", () ->
            {
                runner.test("with negative index with an empty list", (Test test) ->
                {
                    final CharacterList list = CharacterList.createFromCharacters();
                    test.assertThrows(() -> list.removeAt(-1), new PreConditionFailure("Indexable length (0) must be greater than or equal to 1."));
                    test.assertEqual(CharacterList.createFromCharacters(), list);
                });

                runner.test("with 0 index with an empty list", (Test test) ->
                {
                    final CharacterList list = CharacterList.createFromCharacters();
                    test.assertThrows(() -> list.removeAt(0), new PreConditionFailure("Indexable length (0) must be greater than or equal to 1."));
                    test.assertEqual(CharacterList.createFromCharacters(), list);
                });

                runner.test("with 1 index with an empty list", (Test test) ->
                {
                    final CharacterList list = CharacterList.createFromCharacters();
                    test.assertThrows(() -> list.removeAt(1), new PreConditionFailure("Indexable length (0) must be greater than or equal to 1."));
                    test.assertEqual(CharacterList.createFromCharacters(), list);
                });

                runner.test("with negative index with a non-empty list", (Test test) ->
                {
                    final CharacterList list = CharacterList.createFromCharacters('a', 'b', 'c');
                    test.assertThrows(() -> list.removeAt(-1), new PreConditionFailure("index (-1) must be between 0 and 2."));
                    test.assertEqual(CharacterList.createFromCharacters('a', 'b', 'c'), list);
                });

                runner.test("with 0 index with a non-empty list", (Test test) ->
                {
                    final CharacterList list = CharacterList.createFromCharacters('a', 'b', 'c');
                    test.assertEqual('a', list.removeAt(0));
                    test.assertEqual(CharacterList.createFromCharacters('b', 'c'), list);
                });

                runner.test("with 1 index with a non-empty list", (Test test) ->
                {
                    final CharacterList list = CharacterList.createFromCharacters('a', 'b', 'c', 'd', 'e', 'f');
                    test.assertEqual('b', list.removeAt(1));
                    test.assertEqual(CharacterList.createFromCharacters('a', 'c', 'd', 'e', 'f'), list);
                });

                runner.test("with count - 1 index with a non-empty list", (Test test) ->
                {
                    final CharacterList list = CharacterList.createFromCharacters('a', 'b', 'c');
                    test.assertEqual('c', list.removeAt(list.getCount() - 1));
                    test.assertEqual(CharacterList.createFromCharacters('a', 'b'), list);
                });

                runner.test("with count index with a non-empty list", (Test test) ->
                {
                    final CharacterList list = CharacterList.createFromCharacters('a', 'b', 'c');
                    test.assertThrows(() -> list.removeAt(list.getCount()), new PreConditionFailure("index (3) must be between 0 and 2."));
                    test.assertEqual(CharacterList.createFromCharacters('a', 'b', 'c'), list);
                });

                runner.test("with count + 1 index with a non-empty list", (Test test) ->
                {
                    final CharacterList list = CharacterList.createFromCharacters('a', 'b', 'c');
                    test.assertThrows(() -> list.removeAt(list.getCount() + 1), new PreConditionFailure("index (4) must be between 0 and 2."));
                    test.assertEqual(CharacterList.createFromCharacters('a', 'b', 'c'), list);
                });
            });

            runner.testGroup("removeFirstCharacters(int)", () ->
            {
                runner.test("with negative", (Test test) ->
                {
                    final CharacterList list = CharacterList.createFromCharacters('a', 'b', 'c');
                    test.assertThrows(() -> list.removeFirstCharacters(-2),
                        new PreConditionFailure("valuesToRemove (-2) must be greater than or equal to 1."));
                    test.assertEqual(Iterable.create('a', 'b', 'c'), list);
                });

                runner.test("with zero", (Test test) ->
                {
                    final CharacterList list = CharacterList.createFromCharacters('a', 'b', 'c');
                    test.assertThrows(() -> list.removeFirstCharacters(0),
                        new PreConditionFailure("valuesToRemove (0) must be greater than or equal to 1."));
                    test.assertEqual(Iterable.create('a', 'b', 'c'), list);
                });

                runner.test("with fewer than count", (Test test) ->
                {
                    final CharacterList list = CharacterList.createFromCharacters('a', 'b', 'c');
                    test.assertEqual(Iterable.create('a', 'b'), list.removeFirstCharacters(2));
                    test.assertEqual(Iterable.create('c'), list);
                });

                runner.test("with equal to count", (Test test) ->
                {
                    final CharacterList list = CharacterList.createFromCharacters('a', 'b', 'c');
                    test.assertEqual(Iterable.create('a', 'b', 'c'), list.removeFirstCharacters(3));
                    test.assertEqual(Iterable.create(), list);
                });

                runner.test("with more than count", (Test test) ->
                {
                    final CharacterList list = CharacterList.createFromCharacters('a', 'b', 'c');
                    test.assertThrows(() -> list.removeFirstCharacters(4),
                        new PreConditionFailure("length (4) must be between 1 and 3."));
                    test.assertEqual(Iterable.create('a', 'b', 'c'), list);
                });
            });

            runner.testGroup("removeFirstCharacters(char[])", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final CharacterList list = CharacterList.createFromCharacters('a', 'b', 'c');
                    test.assertThrows(() -> list.removeFirstCharacters((char[])null),
                        new PreConditionFailure("outputCharacters cannot be null."));
                    test.assertEqual(Iterable.create('a', 'b', 'c'), list);
                });

                runner.test("with empty", (Test test) ->
                {
                    final CharacterList list = CharacterList.createFromCharacters('a', 'b', 'c');
                    test.assertThrows(() -> list.removeFirstCharacters(new char[0]),
                        new PreConditionFailure("outputCharacters cannot be empty."));
                    test.assertEqual(Iterable.create('a', 'b', 'c'), list);
                });

                runner.test("with fewer than count", (Test test) ->
                {
                    final CharacterList list = CharacterList.createFromCharacters('a', 'b', 'c');
                    final char[] outputCharacters = new char[2];
                    list.removeFirstCharacters(outputCharacters);
                    test.assertEqual(Iterable.create('a', 'b'), CharacterList.createFromCharacters(outputCharacters));
                    test.assertEqual(Iterable.create('c'), list);
                });

                runner.test("with equal to count", (Test test) ->
                {
                    final CharacterList list = CharacterList.createFromCharacters('a', 'b', 'c');
                    final char[] outputCharacters = new char[3];
                    list.removeFirstCharacters(outputCharacters);
                    test.assertEqual(Iterable.create('a', 'b', 'c'), CharacterList.createFromCharacters(outputCharacters));
                    test.assertEqual(Iterable.create(), list);
                });

                runner.test("with more than count", (Test test) ->
                {
                    final CharacterList list = CharacterList.createFromCharacters('a', 'b', 'c');
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
                    final CharacterList list = CharacterList.createFromCharacters('a', 'b', 'c');
                    test.assertThrows(() -> list.set(-1, (char)5), new PreConditionFailure("index (-1) must be between 0 and 2."));
                    test.assertEqual(CharacterList.createFromCharacters('a', 'b', 'c'), list);
                });

                runner.test("with zero index", (Test test) ->
                {
                    final CharacterList list = CharacterList.createFromCharacters('a', 'b', 'c');
                    list.set(0, 'd');
                    test.assertEqual(CharacterList.createFromCharacters('d', 'b', 'c'), list);
                });

                runner.test("with count - 1 index", (Test test) ->
                {
                    final CharacterList list = CharacterList.createFromCharacters('a', 'b', 'c');
                    list.set(2, 'd');
                    test.assertEqual(CharacterList.createFromCharacters('a', 'b', 'd'), list);
                });

                runner.test("with count index", (Test test) ->
                {
                    final CharacterList list = CharacterList.createFromCharacters('a', 'b', 'c');
                    test.assertThrows(() -> list.set(3, (char)5), new PreConditionFailure("index (3) must be between 0 and 2."));
                    test.assertEqual(CharacterList.createFromCharacters('a', 'b', 'c'), list);
                });

                runner.test("with count + 1 index", (Test test) ->
                {
                    final CharacterList list = CharacterList.createFromCharacters('a', 'b', 'c');
                    test.assertThrows(() -> list.set(4, (char)5), new PreConditionFailure("index (4) must be between 0 and 2."));
                    test.assertEqual(CharacterList.createFromCharacters('a', 'b', 'c'), list);
                });
            });

            runner.testGroup("set(int,Character)", () ->
            {
                runner.test("with negative index", (Test test) ->
                {
                    final CharacterList list = CharacterList.createFromCharacters('a', 'b', 'c');
                    test.assertThrows(() -> list.set(-1, Character.valueOf('d')), new PreConditionFailure("index (-1) must be between 0 and 2."));
                    test.assertEqual(CharacterList.createFromCharacters('a', 'b', 'c'), list);
                });

                runner.test("with zero index", (Test test) ->
                {
                    final CharacterList list = CharacterList.createFromCharacters('a', 'b', 'c');
                    list.set(0, Character.valueOf('d'));
                    test.assertEqual(CharacterList.createFromCharacters('d', 'b', 'c'), list);
                });

                runner.test("with count - 1 index", (Test test) ->
                {
                    final CharacterList list = CharacterList.createFromCharacters('a', 'b', 'c');
                    list.set(2, Character.valueOf('d'));
                    test.assertEqual(CharacterList.createFromCharacters('a', 'b', 'd'), list);
                });

                runner.test("with count index", (Test test) ->
                {
                    final CharacterList list = CharacterList.createFromCharacters('a', 'b', 'c');
                    test.assertThrows(() -> list.set(3, Character.valueOf('d')), new PreConditionFailure("index (3) must be between 0 and 2."));
                    test.assertEqual(CharacterList.createFromCharacters('a', 'b', 'c'), list);
                });

                runner.test("with count + 1 index", (Test test) ->
                {
                    final CharacterList list = CharacterList.createFromCharacters('a', 'b', 'c');
                    test.assertThrows(() -> list.set(4, Character.valueOf('d')), new PreConditionFailure("index (4) must be between 0 and 2."));
                    test.assertEqual(CharacterList.createFromCharacters('a', 'b', 'c'), list);
                });
            });

            runner.testGroup("get(int)", () ->
            {
                runner.test("with negative index", (Test test) ->
                {
                    final CharacterList list = CharacterList.createFromCharacters('a', 'b', 'c');
                    test.assertThrows(() -> list.get(-1), new PreConditionFailure("index (-1) must be between 0 and 2."));
                });

                runner.test("with zero index", (Test test) ->
                {
                    final CharacterList list = CharacterList.createFromCharacters('a', 'b', 'c');
                    test.assertEqual('a', list.get(0));
                });

                runner.test("with count - 1 index", (Test test) ->
                {
                    final CharacterList list = CharacterList.createFromCharacters('a', 'b', 'c');
                    test.assertEqual('c', list.get(2));
                });

                runner.test("with count index", (Test test) ->
                {
                    final CharacterList list = CharacterList.createFromCharacters('a', 'b', 'c');
                    test.assertThrows(() -> list.get(3), new PreConditionFailure("index (3) must be between 0 and 2."));
                });

                runner.test("with count + 1 index", (Test test) ->
                {
                    final CharacterList list = CharacterList.createFromCharacters('a', 'b', 'c');
                    test.assertThrows(() -> list.get(4), new PreConditionFailure("index (4) must be between 0 and 2."));
                });
            });

            runner.testGroup("toString()", () ->
            {
                runner.test("with empty", (Test test) ->
                {
                    test.assertEqual("[]", CharacterList.createFromCharacters().toString());
                });

                runner.test("with one value", (Test test) ->
                {
                    test.assertEqual("[a]", CharacterList.createFromCharacters('a').toString());
                });

                runner.test("with two values", (Test test) ->
                {
                    test.assertEqual("[x,y]", CharacterList.createFromCharacters('x', 'y').toString());
                });
            });
        });
    }
}