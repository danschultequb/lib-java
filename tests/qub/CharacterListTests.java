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

            runner.testGroup("removeFirst(int)", () ->
            {
                final Action3<CharacterList,Integer,Throwable> removeFirstErrorTest = (CharacterList list, Integer valuesToRemove, Throwable expected) ->
                {
                    runner.test("with " + English.andList(list.toString(false), valuesToRemove), (Test test) ->
                    {
                        final Iterable<Character> backupList = List.create(list);
                        test.assertThrows(() -> list.removeFirst(valuesToRemove).await(),
                            expected);
                        test.assertEqual(backupList, list);
                    });
                };

                removeFirstErrorTest.run(CharacterList.create(), -2, new PreConditionFailure("valuesToRemove (-2) must be greater than or equal to 0."));
                removeFirstErrorTest.run(CharacterList.create(), 1, new EmptyException());
                removeFirstErrorTest.run(CharacterList.create('a', 'b', 'c'), -2, new PreConditionFailure("valuesToRemove (-2) must be greater than or equal to 0."));

                final Action4<CharacterList,Integer,Iterable<Character>,Iterable<Character>> removeFirstTest = (CharacterList list, Integer valuesToRemove, Iterable<Character> expectedResult, Iterable<Character> expectedList) ->
                {
                    runner.test("with " + English.andList(list.toString(false), valuesToRemove), (Test test) ->
                    {
                        final CharacterArray removeFirstResult = list.removeFirst(valuesToRemove).await();
                        test.assertEqual(expectedResult, removeFirstResult);
                        test.assertEqual(expectedList, list);
                    });
                };

                removeFirstTest.run(CharacterList.create(), 0, Iterable.create(), Iterable.create());
                removeFirstTest.run(CharacterList.create('a', 'b', 'c'), 0, Iterable.create(), Iterable.create('a', 'b', 'c'));
                removeFirstTest.run(CharacterList.create('a', 'b', 'c'), 1, Iterable.create('a'), Iterable.create('b', 'c'));
                removeFirstTest.run(CharacterList.create('a', 'b', 'c'), 2, Iterable.create('a', 'b'), Iterable.create('c'));
                removeFirstTest.run(CharacterList.create('a', 'b', 'c'), 3, Iterable.create('a', 'b', 'c'), Iterable.create());
                removeFirstTest.run(CharacterList.create('a', 'b', 'c'), 4, Iterable.create('a', 'b', 'c'), Iterable.create());
            });

            runner.testGroup("removeFirst(char[])", () ->
            {
                final Action3<CharacterList,char[],Throwable> removeFirstErrorTest = (CharacterList list, char[] outputCharacters, Throwable expected) ->
                {
                    runner.test("with " + English.andList(list, Array.toString(outputCharacters)), (Test test) ->
                    {
                        test.assertThrows(() -> list.removeFirst(outputCharacters).await(),
                            expected);
                    });
                };

                removeFirstErrorTest.run(
                    CharacterList.create(),
                    null,
                    new PreConditionFailure("outputCharacters cannot be null."));
                removeFirstErrorTest.run(
                    CharacterList.create(),
                    new char[1],
                    new EmptyException());

                final Action5<CharacterList,char[],Integer,char[],Iterable<Character>> removeFirstTest = (CharacterList list, char[] outputCharacters, Integer expectedResult, char[] expectedOutputCharacters, Iterable<Character> expectedList) ->
                {
                    runner.test("with " + English.andList(list, Array.toString(outputCharacters)), (Test test) ->
                    {
                        final Integer removeFirstResult = list.removeFirst(outputCharacters).await();
                        test.assertEqual(expectedResult, removeFirstResult);
                        test.assertEqual(expectedOutputCharacters, outputCharacters);
                        test.assertEqual(expectedList, list);
                    });
                };

                removeFirstTest.run(
                    CharacterList.create(),
                    new char[0],
                    0,
                    new char[0],
                    Iterable.create());
                removeFirstTest.run(
                    CharacterList.create('a', 'b', 'c'),
                    new char[0],
                    0,
                    new char[0],
                    Iterable.create('a', 'b', 'c'));
                removeFirstTest.run(
                    CharacterList.create('a', 'b', 'c'),
                    new char[1],
                    1,
                    new char[] { 'a' },
                    Iterable.create('b', 'c'));
                removeFirstTest.run(
                    CharacterList.create('a', 'b', 'c'),
                    new char[2],
                    2,
                    new char[] { 'a', 'b' },
                    Iterable.create('c'));
                removeFirstTest.run(
                    CharacterList.create('a', 'b', 'c'),
                    new char[3],
                    3,
                    new char[] { 'a', 'b', 'c' },
                    Iterable.create());
                removeFirstTest.run(
                    CharacterList.create('a', 'b', 'c'),
                    new char[4],
                    3,
                    new char[] { 'a', 'b', 'c', '\0' },
                    Iterable.create());
                removeFirstTest.run(
                    CharacterList.create('a', 'b', 'c'),
                    new char[5],
                    3,
                    new char[] { 'a', 'b', 'c', '\0', '\0' },
                    Iterable.create());
            });

            runner.testGroup("removeFirst(char[],int,int)", () ->
            {
                final Action5<CharacterList,char[],Integer,Integer,Throwable> removeFirstErrorTest = (CharacterList list, char[] outputCharacters, Integer startIndex, Integer length, Throwable expected) ->
                {
                    runner.test("with " + English.andList(list, Array.toString(outputCharacters), startIndex, length), (Test test) ->
                    {
                        test.assertThrows(() -> list.removeFirst(outputCharacters, startIndex, length).await(),
                            expected);
                    });
                };

                removeFirstErrorTest.run(
                    CharacterList.create(),
                    null,
                    0,
                    0,
                    new PreConditionFailure("outputCharacters cannot be null."));
                removeFirstErrorTest.run(
                    CharacterList.create(),
                    new char[0],
                    -1,
                    0,
                    new PreConditionFailure("startIndex (-1) must be equal to 0."));
                removeFirstErrorTest.run(
                    CharacterList.create(),
                    new char[0],
                    1,
                    0,
                    new PreConditionFailure("startIndex (1) must be equal to 0."));
                removeFirstErrorTest.run(
                    CharacterList.create(),
                    new char[1],
                    1,
                    0,
                    new PreConditionFailure("startIndex (1) must be equal to 0."));
                removeFirstErrorTest.run(
                    CharacterList.create(),
                    new char[2],
                    2,
                    0,
                    new PreConditionFailure("startIndex (2) must be between 0 and 1."));
                removeFirstErrorTest.run(
                    CharacterList.create(),
                    new char[0],
                    0,
                    -1,
                    new PreConditionFailure("length (-1) must be equal to 0."));
                removeFirstErrorTest.run(
                    CharacterList.create(),
                    new char[1],
                    0,
                    2,
                    new PreConditionFailure("length (2) must be between 0 and 1."));
                removeFirstErrorTest.run(
                    CharacterList.create(),
                    new char[1],
                    0,
                    1,
                    new EmptyException());

                final Action7<CharacterList,char[],Integer,Integer,Integer,char[],Iterable<Character>> removeFirstTest = (CharacterList list, char[] outputCharacters, Integer startIndex, Integer length, Integer expectedResult, char[] expectedOutputCharacters, Iterable<Character> expectedList) ->
                {
                    runner.test("with " + English.andList(list, Array.toString(outputCharacters), startIndex, length), (Test test) ->
                    {
                        final Integer removeFirstResult = list.removeFirst(outputCharacters, startIndex, length).await();
                        test.assertEqual(expectedResult, removeFirstResult);
                        test.assertEqual(expectedOutputCharacters, outputCharacters);
                        test.assertEqual(expectedList, list);
                    });
                };

                removeFirstTest.run(
                    CharacterList.create(),
                    new char[0],
                    0,
                    0,
                    0,
                    new char[0],
                    Iterable.create());
                removeFirstTest.run(
                    CharacterList.create('a', 'b', 'c'),
                    new char[0],
                    0,
                    0,
                    0,
                    new char[0],
                    Iterable.create('a', 'b', 'c'));
                removeFirstTest.run(
                    CharacterList.create('a', 'b', 'c'),
                    new char[1],
                    0,
                    1,
                    1,
                    new char[] { 'a' },
                    Iterable.create('b', 'c'));
                removeFirstTest.run(
                    CharacterList.create('a', 'b', 'c'),
                    new char[2],
                    0,
                    2,
                    2,
                    new char[] { 'a', 'b' },
                    Iterable.create('c'));
                removeFirstTest.run(
                    CharacterList.create('a', 'b', 'c'),
                    new char[3],
                    0,
                    3,
                    3,
                    new char[] { 'a', 'b', 'c' },
                    Iterable.create());
                removeFirstTest.run(
                    CharacterList.create('a', 'b', 'c'),
                    new char[3],
                    1,
                    2,
                    2,
                    new char[] { '\0', 'a', 'b' },
                    Iterable.create('c'));
                removeFirstTest.run(
                    CharacterList.create('a', 'b', 'c'),
                    new char[4],
                    0,
                    4,
                    3,
                    new char[] { 'a', 'b', 'c', '\0' },
                    Iterable.create());
                removeFirstTest.run(
                    CharacterList.create('a', 'b', 'c'),
                    new char[4],
                    1,
                    3,
                    3,
                    new char[] { '\0', 'a', 'b', 'c' },
                    Iterable.create());
                removeFirstTest.run(
                    CharacterList.create('a', 'b', 'c'),
                    new char[5],
                    0,
                    5,
                    3,
                    new char[] { 'a', 'b', 'c', '\0', '\0' },
                    Iterable.create());
                removeFirstTest.run(
                    CharacterList.create('a', 'b', 'c'),
                    new char[5],
                    1,
                    4,
                    3,
                    new char[] { '\0', 'a', 'b', 'c', '\0' },
                    Iterable.create());
                removeFirstTest.run(
                    CharacterList.create('a', 'b', 'c'),
                    new char[5],
                    2,
                    3,
                    3,
                    new char[] { '\0', '\0', 'a', 'b', 'c' },
                    Iterable.create());
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
