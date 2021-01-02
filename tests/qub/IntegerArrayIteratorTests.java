package qub;

public interface IntegerArrayIteratorTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(IntegerArrayIterator.class, () ->
        {
            runner.testGroup("create(long...)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> IntegerArrayIterator.create((int[])null),
                        new PreConditionFailure("values cannot be null."));
                });

                runner.test("with no arguments", (Test test) ->
                {
                    final IntegerArrayIterator iterator = IntegerArrayIterator.create();
                    test.assertNotNull(iterator);
                    IntegerArrayIteratorTests.assertIteratorWithNoCurrent(test, iterator, false);

                    test.assertFalse(iterator.next());
                    IntegerArrayIteratorTests.assertIteratorWithNoCurrent(test, iterator, true);

                    test.assertFalse(iterator.next());
                    IntegerArrayIteratorTests.assertIteratorWithNoCurrent(test, iterator, true);
                });

                runner.test("with one argument", (Test test) ->
                {
                    final IntegerArrayIterator iterator = IntegerArrayIterator.create(8);
                    test.assertNotNull(iterator);
                    IntegerArrayIteratorTests.assertIteratorWithNoCurrent(test, iterator, false);

                    test.assertTrue(iterator.next());
                    IntegerArrayIteratorTests.assertIteratorWithCurrent(test, iterator, 8);

                    test.assertFalse(iterator.next());
                    IntegerArrayIteratorTests.assertIteratorWithNoCurrent(test, iterator, true);

                    test.assertFalse(iterator.next());
                    IntegerArrayIteratorTests.assertIteratorWithNoCurrent(test, iterator, true);
                });

                runner.test("with two arguments", (Test test) ->
                {
                    final IntegerArrayIterator iterator = IntegerArrayIterator.create(8, 12);
                    test.assertNotNull(iterator);
                    IntegerArrayIteratorTests.assertIteratorWithNoCurrent(test, iterator, false);

                    test.assertTrue(iterator.next());
                    IntegerArrayIteratorTests.assertIteratorWithCurrent(test, iterator, 8);

                    test.assertTrue(iterator.next());
                    IntegerArrayIteratorTests.assertIteratorWithCurrent(test, iterator, 12);

                    test.assertFalse(iterator.next());
                    IntegerArrayIteratorTests.assertIteratorWithNoCurrent(test, iterator, true);

                    test.assertFalse(iterator.next());
                    IntegerArrayIteratorTests.assertIteratorWithNoCurrent(test, iterator, true);
                });
            });

            runner.testGroup("create(int[],int,int)", () ->
            {
                final Action4<int[],Integer,Integer,Throwable> createErrorTest = (int[] values, Integer startIndex, Integer length, Throwable expected) ->
                {
                    runner.test("with " + English.andList(values, startIndex, length), (Test test) ->
                    {
                        test.assertThrows(() -> IntegerArrayIterator.create(values, startIndex, length), expected);
                    });
                };

                createErrorTest.run(null, 0, 0, new PreConditionFailure("values cannot be null."));
                createErrorTest.run(new int[] {}, -1, 0, new PreConditionFailure("startIndex (-1) must be equal to 0."));
                createErrorTest.run(new int[] {}, 1, 0, new PreConditionFailure("startIndex (1) must be equal to 0."));
                createErrorTest.run(new int[] {}, 0, -1, new PreConditionFailure("length (-1) must be equal to 0."));
                createErrorTest.run(new int[] {}, 0, 1, new PreConditionFailure("length (1) must be equal to 0."));
                createErrorTest.run(new int[] { 10, 11, 12 }, -1, 0, new PreConditionFailure("startIndex (-1) must be between 0 and 2."));
                createErrorTest.run(new int[] { 10, 11, 12 }, 3, 0, new PreConditionFailure("startIndex (3) must be between 0 and 2."));
                createErrorTest.run(new int[] { 10, 11, 12 }, 0, -1, new PreConditionFailure("length (-1) must be between 0 and 3."));
                createErrorTest.run(new int[] { 10, 11, 12 }, 0, 4, new PreConditionFailure("length (4) must be between 0 and 3."));
            });

            final Action4<int[],Integer,Integer,int[]> createTest = (int[] values, Integer startIndex, Integer length, int[] expected) ->
            {
                runner.test("with " + English.andList(values, startIndex, length), (Test test) ->
                {
                    final IntegerArrayIterator iterator = IntegerArrayIterator.create(values, startIndex, length);
                    IntegerArrayIteratorTests.assertIteratorWithNoCurrent(test, iterator, false);

                    for (int i = 0; i < expected.length; i++)
                    {
                        test.assertTrue(iterator.next());
                        IntegerArrayIteratorTests.assertIteratorWithCurrent(test, iterator, expected[i]);
                    }

                    for (int i = 0; i < 2; ++i)
                    {
                        test.assertFalse(iterator.next());
                        IntegerArrayIteratorTests.assertIteratorWithNoCurrent(test, iterator, true);
                    }
                });
            };

            createTest.run(new int[] {}, 0, 0, new int[] {});
            createTest.run(new int[] { 1 }, 0, 0, new int[] {});
            createTest.run(new int[] { 1 }, 0, 1, new int[] { 1 });
            createTest.run(new int[] { 1, 2 }, 0, 0, new int[] {});
            createTest.run(new int[] { 1, 2 }, 0, 1, new int[] { 1 });
            createTest.run(new int[] { 1, 2 }, 0, 2, new int[] { 1, 2 });
            createTest.run(new int[] { 1, 2 }, 1, 0, new int[] {});
            createTest.run(new int[] { 1, 2 }, 1, 1, new int[] { 2 });
            createTest.run(new int[] { 1, 2, 3 }, 0, 0, new int[] {});
            createTest.run(new int[] { 1, 2, 3 }, 0, 1, new int[] { 1 });
            createTest.run(new int[] { 1, 2, 3 }, 0, 2, new int[] { 1, 2 });
            createTest.run(new int[] { 1, 2, 3 }, 0, 3, new int[] { 1, 2, 3 });
            createTest.run(new int[] { 1, 2, 3 }, 1, 0, new int[] {});
            createTest.run(new int[] { 1, 2, 3 }, 1, 1, new int[] { 2 });
            createTest.run(new int[] { 1, 2, 3 }, 1, 2, new int[] { 2, 3 });
            createTest.run(new int[] { 1, 2, 3 }, 2, 0, new int[] {});
            createTest.run(new int[] { 1, 2, 3 }, 2, 1, new int[] { 3 });
        });
    }

    static void assertIteratorWithNoCurrent(Test test, IntegerArrayIterator iterator, boolean hasStarted)
    {
        PreCondition.assertNotNull(test, "test");
        PreCondition.assertNotNull(iterator, "iterator");

        test.assertEqual(hasStarted, iterator.hasStarted());
        test.assertFalse(iterator.hasCurrent());
        test.assertThrows(() -> iterator.getCurrent(), new PreConditionFailure("this.hasCurrent() cannot be false."));
    }

    static void assertIteratorWithCurrent(Test test, IntegerArrayIterator iterator, int expectedCurrent)
    {
        PreCondition.assertNotNull(test, "test");
        PreCondition.assertNotNull(iterator, "iterator");

        test.assertTrue(iterator.hasStarted());
        test.assertTrue(iterator.hasCurrent());
        test.assertEqual(expectedCurrent, iterator.getCurrent());
    }
}
