package qub;

public interface LongArrayIteratorTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(LongArrayIterator.class, () ->
        {
            runner.testGroup("create(long...)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> LongArrayIterator.create((long[])null),
                        new PreConditionFailure("values cannot be null."));
                });

                runner.test("with no arguments", (Test test) ->
                {
                    final LongArrayIterator iterator = LongArrayIterator.create();
                    test.assertNotNull(iterator);
                    LongArrayIteratorTests.assertIteratorWithNoCurrent(test, iterator, false);

                    test.assertFalse(iterator.next());
                    LongArrayIteratorTests.assertIteratorWithNoCurrent(test, iterator, true);

                    test.assertFalse(iterator.next());
                    LongArrayIteratorTests.assertIteratorWithNoCurrent(test, iterator, true);
                });

                runner.test("with one argument", (Test test) ->
                {
                    final LongArrayIterator iterator = LongArrayIterator.create(8);
                    test.assertNotNull(iterator);
                    LongArrayIteratorTests.assertIteratorWithNoCurrent(test, iterator, false);

                    test.assertTrue(iterator.next());
                    LongArrayIteratorTests.assertIteratorWithCurrent(test, iterator, 8);

                    test.assertFalse(iterator.next());
                    LongArrayIteratorTests.assertIteratorWithNoCurrent(test, iterator, true);

                    test.assertFalse(iterator.next());
                    LongArrayIteratorTests.assertIteratorWithNoCurrent(test, iterator, true);
                });

                runner.test("with two arguments", (Test test) ->
                {
                    final LongArrayIterator iterator = LongArrayIterator.create(8, 12);
                    test.assertNotNull(iterator);
                    LongArrayIteratorTests.assertIteratorWithNoCurrent(test, iterator, false);

                    test.assertTrue(iterator.next());
                    LongArrayIteratorTests.assertIteratorWithCurrent(test, iterator, 8L);

                    test.assertTrue(iterator.next());
                    LongArrayIteratorTests.assertIteratorWithCurrent(test, iterator, 12L);

                    test.assertFalse(iterator.next());
                    LongArrayIteratorTests.assertIteratorWithNoCurrent(test, iterator, true);

                    test.assertFalse(iterator.next());
                    LongArrayIteratorTests.assertIteratorWithNoCurrent(test, iterator, true);
                });
            });

            runner.testGroup("create(long[],int,int)", () ->
            {
                final Action4<long[],Integer,Integer,Throwable> createErrorTest = (long[] values, Integer startIndex, Integer length, Throwable expected) ->
                {
                    runner.test("with " + English.andList(values, startIndex, length), (Test test) ->
                    {
                        test.assertThrows(() -> LongArrayIterator.create(values, startIndex, length), expected);
                    });
                };

                createErrorTest.run(null, 0, 0, new PreConditionFailure("values cannot be null."));
                createErrorTest.run(new long[] {}, -1, 0, new PreConditionFailure("startIndex (-1) must be equal to 0."));
                createErrorTest.run(new long[] {}, 1, 0, new PreConditionFailure("startIndex (1) must be equal to 0."));
                createErrorTest.run(new long[] {}, 0, -1, new PreConditionFailure("length (-1) must be equal to 0."));
                createErrorTest.run(new long[] {}, 0, 1, new PreConditionFailure("length (1) must be equal to 0."));
                createErrorTest.run(new long[] { 10, 11, 12 }, -1, 0, new PreConditionFailure("startIndex (-1) must be between 0 and 2."));
                createErrorTest.run(new long[] { 10, 11, 12 }, 3, 0, new PreConditionFailure("startIndex (3) must be between 0 and 2."));
                createErrorTest.run(new long[] { 10, 11, 12 }, 0, -1, new PreConditionFailure("length (-1) must be between 0 and 3."));
                createErrorTest.run(new long[] { 10, 11, 12 }, 0, 4, new PreConditionFailure("length (4) must be between 0 and 3."));
            });

            final Action4<long[],Integer,Integer,long[]> createTest = (long[] values, Integer startIndex, Integer length, long[] expected) ->
            {
                runner.test("with " + English.andList(values, startIndex, length), (Test test) ->
                {
                    final LongArrayIterator iterator = LongArrayIterator.create(values, startIndex, length);
                    LongArrayIteratorTests.assertIteratorWithNoCurrent(test, iterator, false);

                    for (int i = 0; i < expected.length; i++)
                    {
                        test.assertTrue(iterator.next());
                        LongArrayIteratorTests.assertIteratorWithCurrent(test, iterator, expected[i]);
                    }

                    for (int i = 0; i < 2; ++i)
                    {
                        test.assertFalse(iterator.next());
                        LongArrayIteratorTests.assertIteratorWithNoCurrent(test, iterator, true);
                    }
                });
            };

            createTest.run(new long[] {}, 0, 0, new long[] {});
            createTest.run(new long[] { 1 }, 0, 0, new long[] {});
            createTest.run(new long[] { 1 }, 0, 1, new long[] { 1 });
            createTest.run(new long[] { 1, 2 }, 0, 0, new long[] {});
            createTest.run(new long[] { 1, 2 }, 0, 1, new long[] { 1 });
            createTest.run(new long[] { 1, 2 }, 0, 2, new long[] { 1, 2 });
            createTest.run(new long[] { 1, 2 }, 1, 0, new long[] {});
            createTest.run(new long[] { 1, 2 }, 1, 1, new long[] { 2 });
            createTest.run(new long[] { 1, 2, 3 }, 0, 0, new long[] {});
            createTest.run(new long[] { 1, 2, 3 }, 0, 1, new long[] { 1 });
            createTest.run(new long[] { 1, 2, 3 }, 0, 2, new long[] { 1, 2 });
            createTest.run(new long[] { 1, 2, 3 }, 0, 3, new long[] { 1, 2, 3 });
            createTest.run(new long[] { 1, 2, 3 }, 1, 0, new long[] {});
            createTest.run(new long[] { 1, 2, 3 }, 1, 1, new long[] { 2 });
            createTest.run(new long[] { 1, 2, 3 }, 1, 2, new long[] { 2, 3 });
            createTest.run(new long[] { 1, 2, 3 }, 2, 0, new long[] {});
            createTest.run(new long[] { 1, 2, 3 }, 2, 1, new long[] { 3 });
        });
    }

    static void assertIteratorWithNoCurrent(Test test, LongArrayIterator iterator, boolean hasStarted)
    {
        PreCondition.assertNotNull(test, "test");
        PreCondition.assertNotNull(iterator, "iterator");

        test.assertEqual(hasStarted, iterator.hasStarted());
        test.assertFalse(iterator.hasCurrent());
        test.assertThrows(() -> iterator.getCurrent(), new PreConditionFailure("this.hasCurrent() cannot be false."));
    }

    static void assertIteratorWithCurrent(Test test, LongArrayIterator iterator, long expectedCurrent)
    {
        PreCondition.assertNotNull(test, "test");
        PreCondition.assertNotNull(iterator, "iterator");

        test.assertTrue(iterator.hasStarted());
        test.assertTrue(iterator.hasCurrent());
        test.assertEqual(expectedCurrent, iterator.getCurrent());
    }
}
