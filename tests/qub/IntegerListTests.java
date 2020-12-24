package qub;

public interface IntegerListTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(IntegerList.class, () ->
        {
            runner.testGroup("create(int...)", () ->
            {
                runner.test("with null int array", (Test test) ->
                {
                    test.assertThrows(() -> IntegerList.create((int[])null),
                        new PreConditionFailure("values cannot be null."));
                });

                runner.test("with no arguments", (Test test) ->
                {
                    final IntegerList list = IntegerList.create();
                    test.assertNotNull(list);
                    test.assertEqual(0, list.getCount());
                });

                runner.test("with empty array", (Test test) ->
                {
                    final int[] values = new int[0];
                    final IntegerList list = IntegerList.create(values);
                    test.assertNotNull(list);
                    test.assertEqual(0, list.getCount());
                });

                runner.test("with non-empty array", (Test test) ->
                {
                    final int[] values = new int[] { 1 };
                    final IntegerList list = IntegerList.create(values);
                    test.assertNotNull(list);
                    test.assertEqual(1, list.getCount());
                    test.assertEqual(1, list.get(0));
                    list.set(0, 5);
                    test.assertEqual(1, values[0]);
                    test.assertEqual(5, list.get(0));
                });

                runner.test("with non-empty int arguments", (Test test) ->
                {
                    final IntegerList list = IntegerList.create(1, 2, 3);
                    test.assertNotNull(list);
                    test.assertEqual(3, list.getCount());
                    test.assertEqual(1, list.get(0));
                    test.assertEqual(2, list.get(1));
                    test.assertEqual(3, list.get(2));
                });
            });

            runner.testGroup("insert(int,int)", () ->
            {
                runner.test("with negative index", (Test test) ->
                {
                    final IntegerList list = IntegerList.create();
                    test.assertThrows(() -> list.insert(-1, 5),
                        new PreConditionFailure("insertIndex (-1) must be equal to 0."));
                    test.assertEqual(0, list.getCount());
                });

                runner.test("with zero index on empty list", (Test test) ->
                {
                    final IntegerList list = IntegerList.create();
                    final IntegerList insertResult = list.insert(0, 5);
                    test.assertSame(list, insertResult);
                    test.assertEqual(1, list.getCount());
                    test.assertEqual(5, list.get(0));
                });

                runner.test("with positive index on empty list", (Test test) ->
                {
                    final IntegerList list = IntegerList.create();
                    test.assertThrows(() -> list.insert(1, 5),
                        new PreConditionFailure("insertIndex (1) must be equal to 0."));
                    test.assertEqual(0, list.getCount());
                });

                runner.test("with zero index on non-empty list", (Test test) ->
                {
                    final IntegerList list = IntegerList.create(1, 2, 3);
                    final IntegerList insertResult = list.insert(0, 7);
                    test.assertSame(list, insertResult);
                    test.assertEqual(IntegerList.create(7, 1, 2, 3), list);
                });

                runner.test("with positive index less than count on non-empty list", (Test test) ->
                {
                    final IntegerList list = IntegerList.create(1, 2, 3);
                    list.insert(2, 7);
                    test.assertEqual(IntegerList.create(1, 2, 7, 3), list);
                });

                runner.test("with positive index equal to count on non-empty list", (Test test) ->
                {
                    final IntegerList list = IntegerList.create(1, 2, 3);
                    list.insert(3, 7);
                    test.assertEqual(IntegerList.create(1, 2, 3, 7), list);
                });

                runner.test("with positive index greater than count on non-empty list", (Test test) ->
                {
                    final IntegerList list = IntegerList.create(1, 2, 3);
                    test.assertThrows(() -> list.insert(4, 7),
                        new PreConditionFailure("insertIndex (4) must be between 0 and 3."));
                    test.assertEqual(IntegerList.create(1, 2, 3), list);
                });
            });

            runner.testGroup("insert(int,Integer)", () ->
            {
                runner.test("with negative index", (Test test) ->
                {
                    final IntegerList list = IntegerList.create();
                    test.assertThrows(() -> list.insert(-1, Integer.valueOf(5)),
                        new PreConditionFailure("insertIndex (-1) must be equal to 0."));
                    test.assertEqual(0, list.getCount());
                });

                runner.test("with null value", (Test test) ->
                {
                    final IntegerList list = IntegerList.create();
                    test.assertThrows(() -> list.insert(0, null),
                        new PreConditionFailure("value cannot be null."));
                    test.assertEqual(0, list.getCount());
                });

                runner.test("with zero index on empty list", (Test test) ->
                {
                    final IntegerList list = IntegerList.create();
                    final IntegerList insertResult = list.insert(0, Integer.valueOf(5));
                    test.assertSame(list, insertResult);
                    test.assertEqual(1, list.getCount());
                    test.assertEqual(5, list.get(0));
                });

                runner.test("with positive index on empty list", (Test test) ->
                {
                    final IntegerList list = IntegerList.create();
                    test.assertThrows(() -> list.insert(1, Integer.valueOf(5)),
                        new PreConditionFailure("insertIndex (1) must be equal to 0."));
                    test.assertEqual(0, list.getCount());
                });

                runner.test("with zero index on non-empty list", (Test test) ->
                {
                    final IntegerList list = IntegerList.create(1, 2, 3);
                    final IntegerList insertResult = list.insert(0, Integer.valueOf(7));
                    test.assertSame(list, insertResult);
                    test.assertEqual(IntegerList.create(7, 1, 2, 3), list);
                });

                runner.test("with positive index less than count on non-empty list", (Test test) ->
                {
                    final IntegerList list = IntegerList.create(1, 2, 3);
                    list.insert(2, Integer.valueOf(7));
                    test.assertEqual(IntegerList.create(1, 2, 7, 3), list);
                });

                runner.test("with positive index equal to count on non-empty list", (Test test) ->
                {
                    final IntegerList list = IntegerList.create(1, 2, 3);
                    list.insert(3, Integer.valueOf(7));
                    test.assertEqual(IntegerList.create(1, 2, 3, 7), list);
                });

                runner.test("with positive index greater than count on non-empty list", (Test test) ->
                {
                    final IntegerList list = IntegerList.create(1, 2, 3);
                    test.assertThrows(() -> list.insert(4, Integer.valueOf(7)), new PreConditionFailure("insertIndex (4) must be between 0 and 3."));
                    test.assertEqual(IntegerList.create(1, 2, 3), list);
                });

                runner.test("with index less than count when the list doesn't need to grow", (Test test) ->
                {
                    final IntegerList list = IntegerList.create(1, 2, 3);
                    list.add(4); // Initiate growth.
                    list.insert(0, Integer.valueOf(0));
                    test.assertEqual(IntegerList.create(0, 1, 2, 3, 4), list);
                });
            });

            runner.testGroup("removeAt(int)", () ->
            {
                runner.test("with negative index with an empty list", (Test test) ->
                {
                    final IntegerList list = IntegerList.create();
                    test.assertThrows(() -> list.removeAt(-1),
                        new PreConditionFailure("this cannot be empty."));
                    test.assertEqual(IntegerList.create(), list);
                });

                runner.test("with 0 index with an empty list", (Test test) ->
                {
                    final IntegerList list = IntegerList.create();
                    test.assertThrows(() -> list.removeAt(0),
                        new PreConditionFailure("this cannot be empty."));
                    test.assertEqual(IntegerList.create(), list);
                });

                runner.test("with 1 index with an empty list", (Test test) ->
                {
                    final IntegerList list = IntegerList.create();
                    test.assertThrows(() -> list.removeAt(1),
                        new PreConditionFailure("this cannot be empty."));
                    test.assertEqual(IntegerList.create(), list);
                });

                runner.test("with negative index with a non-empty list", (Test test) ->
                {
                    final IntegerList list = IntegerList.create(1, 2, 3);
                    test.assertThrows(() -> list.removeAt(-1), new PreConditionFailure("index (-1) must be between 0 and 2."));
                    test.assertEqual(IntegerList.create(1, 2, 3), list);
                });

                runner.test("with 0 index with a non-empty list", (Test test) ->
                {
                    final IntegerList list = IntegerList.create(1, 2, 3);
                    test.assertEqual(1, list.removeAt(0));
                    test.assertEqual(IntegerList.create(2, 3), list);
                });

                runner.test("with 1 index with a non-empty list", (Test test) ->
                {
                    final IntegerList list = IntegerList.create(1, 2, 3, 4, 5, 6);
                    test.assertEqual(2, list.removeAt(1));
                    test.assertEqual(IntegerList.create(1, 3, 4, 5, 6), list);
                });

                runner.test("with count - 1 index with a non-empty list", (Test test) ->
                {
                    final IntegerList list = IntegerList.create(1, 2, 3);
                    test.assertEqual(3, list.removeAt(list.getCount() - 1));
                    test.assertEqual(IntegerList.create(1, 2), list);
                });

                runner.test("with count index with a non-empty list", (Test test) ->
                {
                    final IntegerList list = IntegerList.create(1, 2, 3);
                    test.assertThrows(() -> list.removeAt(list.getCount()), new PreConditionFailure("index (3) must be between 0 and 2."));
                    test.assertEqual(IntegerList.create(1, 2, 3), list);
                });

                runner.test("with count + 1 index with a non-empty list", (Test test) ->
                {
                    final IntegerList list = IntegerList.create(1, 2, 3);
                    test.assertThrows(() -> list.removeAt(list.getCount() + 1), new PreConditionFailure("index (4) must be between 0 and 2."));
                    test.assertEqual(IntegerList.create(1, 2, 3), list);
                });
            });

            runner.testGroup("set(int,int)", () ->
            {
                runner.test("with negative index", (Test test) ->
                {
                    final IntegerList list = IntegerList.create(1, 2, 3);
                    test.assertThrows(() -> list.set(-1, 5), new PreConditionFailure("index (-1) must be between 0 and 2."));
                    test.assertEqual(IntegerList.create(1, 2, 3), list);
                });

                runner.test("with zero index", (Test test) ->
                {
                    final IntegerList list = IntegerList.create(1, 2, 3);
                    list.set(0, 5);
                    test.assertEqual(IntegerList.create(5, 2, 3), list);
                });

                runner.test("with count - 1 index", (Test test) ->
                {
                    final IntegerList list = IntegerList.create(1, 2, 3);
                    list.set(2, 5);
                    test.assertEqual(IntegerList.create(1, 2, 5), list);
                });

                runner.test("with count index", (Test test) ->
                {
                    final IntegerList list = IntegerList.create(1, 2, 3);
                    test.assertThrows(() -> list.set(3, 5), new PreConditionFailure("index (3) must be between 0 and 2."));
                    test.assertEqual(IntegerList.create(1, 2, 3), list);
                });

                runner.test("with count + 1 index", (Test test) ->
                {
                    final IntegerList list = IntegerList.create(1, 2, 3);
                    test.assertThrows(() -> list.set(4, 5), new PreConditionFailure("index (4) must be between 0 and 2."));
                    test.assertEqual(IntegerList.create(1, 2, 3), list);
                });
            });

            runner.testGroup("set(int,Integer)", () ->
            {
                runner.test("with negative index", (Test test) ->
                {
                    final IntegerList list = IntegerList.create(1, 2, 3);
                    test.assertThrows(() -> list.set(-1, Integer.valueOf(5)), new PreConditionFailure("index (-1) must be between 0 and 2."));
                    test.assertEqual(IntegerList.create(1, 2, 3), list);
                });

                runner.test("with zero index", (Test test) ->
                {
                    final IntegerList list = IntegerList.create(1, 2, 3);
                    list.set(0, Integer.valueOf(5));
                    test.assertEqual(IntegerList.create(5, 2, 3), list);
                });

                runner.test("with count - 1 index", (Test test) ->
                {
                    final IntegerList list = IntegerList.create(1, 2, 3);
                    list.set(2, Integer.valueOf(5));
                    test.assertEqual(IntegerList.create(1, 2, 5), list);
                });

                runner.test("with count index", (Test test) ->
                {
                    final IntegerList list = IntegerList.create(1, 2, 3);
                    test.assertThrows(() -> list.set(3, Integer.valueOf(5)), new PreConditionFailure("index (3) must be between 0 and 2."));
                    test.assertEqual(IntegerList.create(1, 2, 3), list);
                });

                runner.test("with count + 1 index", (Test test) ->
                {
                    final IntegerList list = IntegerList.create(1, 2, 3);
                    test.assertThrows(() -> list.set(4, Integer.valueOf(5)), new PreConditionFailure("index (4) must be between 0 and 2."));
                    test.assertEqual(IntegerList.create(1, 2, 3), list);
                });
            });

            runner.testGroup("get(int)", () ->
            {
                runner.test("with negative index", (Test test) ->
                {
                    final IntegerList list = IntegerList.create(1, 2, 3);
                    test.assertThrows(() -> list.get(-1), new PreConditionFailure("index (-1) must be between 0 and 2."));
                });

                runner.test("with zero index", (Test test) ->
                {
                    final IntegerList list = IntegerList.create(1, 2, 3);
                    test.assertEqual(1, list.get(0));
                });

                runner.test("with count - 1 index", (Test test) ->
                {
                    final IntegerList list = IntegerList.create(1, 2, 3);
                    test.assertEqual(3, list.get(2));
                });

                runner.test("with count index", (Test test) ->
                {
                    final IntegerList list = IntegerList.create(1, 2, 3);
                    test.assertThrows(() -> list.get(3), new PreConditionFailure("index (3) must be between 0 and 2."));
                });

                runner.test("with count + 1 index", (Test test) ->
                {
                    final IntegerList list = IntegerList.create(1, 2, 3);
                    test.assertThrows(() -> list.get(4), new PreConditionFailure("index (4) must be between 0 and 2."));
                });
            });

            runner.testGroup("toString()", () ->
            {
                runner.test("with empty", (Test test) ->
                {
                    test.assertEqual("[]", IntegerList.create().toString());
                });

                runner.test("with one value", (Test test) ->
                {
                    test.assertEqual("[11]", IntegerList.create(11).toString());
                });

                runner.test("with two values", (Test test) ->
                {
                    test.assertEqual("[11,20]", IntegerList.create(11, 20).toString());
                });
            });
        });
    }
}
