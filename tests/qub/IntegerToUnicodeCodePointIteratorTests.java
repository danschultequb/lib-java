package qub;

public interface IntegerToUnicodeCodePointIteratorTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(IntegerToUnicodeCodePointIterator.class, () ->
        {
            runner.testGroup("create(int...)", () ->
            {
                runner.test("with no arguments", (Test test) ->
                {
                    final IntegerToUnicodeCodePointIterator iterator = IntegerToUnicodeCodePointIterator.create();
                    test.assertNotNull(iterator);
                    test.assertFalse(iterator.hasStarted());
                    test.assertFalse(iterator.hasCurrent());
                    test.assertFalse(iterator.getReturnByteOrderMark());
                    test.assertNull(iterator.foundByteOrderMark());

                    test.assertEqual(Iterable.create(), iterator.toList());

                    test.assertTrue(iterator.hasStarted());
                    test.assertFalse(iterator.hasCurrent());
                    test.assertFalse(iterator.getReturnByteOrderMark());
                    test.assertFalse(iterator.foundByteOrderMark());
                });

                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> IntegerToUnicodeCodePointIterator.create((int[])null),
                        new PreConditionFailure("integers cannot be null."));
                });

                runner.test("with one integer", (Test test) ->
                {
                    final IntegerToUnicodeCodePointIterator iterator = IntegerToUnicodeCodePointIterator.create(5);
                    test.assertNotNull(iterator);
                    test.assertFalse(iterator.hasStarted());
                    test.assertFalse(iterator.hasCurrent());
                    test.assertFalse(iterator.getReturnByteOrderMark());
                    test.assertNull(iterator.foundByteOrderMark());

                    test.assertEqual(Iterable.create(5), iterator.toList());

                    test.assertTrue(iterator.hasStarted());
                    test.assertFalse(iterator.hasCurrent());
                    test.assertFalse(iterator.getReturnByteOrderMark());
                    test.assertFalse(iterator.foundByteOrderMark());
                });

                runner.test("with multiple integers", (Test test) ->
                {
                    final IntegerToUnicodeCodePointIterator iterator = IntegerToUnicodeCodePointIterator.create(5, 10, 15);
                    test.assertNotNull(iterator);
                    test.assertFalse(iterator.hasStarted());
                    test.assertFalse(iterator.hasCurrent());
                    test.assertFalse(iterator.getReturnByteOrderMark());
                    test.assertNull(iterator.foundByteOrderMark());

                    test.assertEqual(Iterable.create(5, 10, 15), iterator.toList());

                    test.assertTrue(iterator.hasStarted());
                    test.assertFalse(iterator.hasCurrent());
                    test.assertFalse(iterator.getReturnByteOrderMark());
                    test.assertFalse(iterator.foundByteOrderMark());
                });

                runner.test("with empty array", (Test test) ->
                {
                    final IntegerToUnicodeCodePointIterator iterator = IntegerToUnicodeCodePointIterator.create(new int[0]);
                    test.assertNotNull(iterator);
                    test.assertFalse(iterator.hasStarted());
                    test.assertFalse(iterator.hasCurrent());
                    test.assertFalse(iterator.getReturnByteOrderMark());
                    test.assertNull(iterator.foundByteOrderMark());

                    test.assertEqual(Iterable.create(), iterator.toList());

                    test.assertTrue(iterator.hasStarted());
                    test.assertFalse(iterator.hasCurrent());
                    test.assertFalse(iterator.getReturnByteOrderMark());
                    test.assertFalse(iterator.foundByteOrderMark());
                });
            });

            runner.testGroup("create(Iterator<Integer>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> IntegerToUnicodeCodePointIterator.create((Iterator<Integer>)null),
                        new PreConditionFailure("integers cannot be null."));
                });

                runner.test("with empty non-started", (Test test) ->
                {
                    final Iterator<Integer> integers = Iterator.create();
                    final IntegerToUnicodeCodePointIterator iterator = IntegerToUnicodeCodePointIterator.create(integers);
                    test.assertNotNull(iterator);
                    test.assertFalse(iterator.hasStarted());
                    test.assertFalse(iterator.hasCurrent());
                    test.assertFalse(iterator.getReturnByteOrderMark());
                    test.assertNull(iterator.foundByteOrderMark());
                    test.assertTrue(iterator.getThrowOnNullIntegers());

                    test.assertEqual(Iterable.create(), iterator.toList());

                    test.assertTrue(iterator.hasStarted());
                    test.assertFalse(iterator.hasCurrent());
                    test.assertFalse(iterator.getReturnByteOrderMark());
                    test.assertFalse(iterator.foundByteOrderMark());
                });

                runner.test("with empty started", (Test test) ->
                {
                    final Iterator<Integer> integers = Iterator.create();
                    integers.next();
                    final IntegerToUnicodeCodePointIterator iterator = IntegerToUnicodeCodePointIterator.create(integers);
                    test.assertNotNull(iterator);
                    test.assertTrue(iterator.hasStarted());
                    test.assertFalse(iterator.hasCurrent());
                    test.assertFalse(iterator.getReturnByteOrderMark());
                    test.assertFalse(iterator.foundByteOrderMark());
                    test.assertTrue(iterator.getThrowOnNullIntegers());

                    test.assertEqual(Iterable.create(), iterator.toList());

                    test.assertTrue(iterator.hasStarted());
                    test.assertFalse(iterator.hasCurrent());
                    test.assertFalse(iterator.getReturnByteOrderMark());
                    test.assertFalse(iterator.foundByteOrderMark());
                });

                runner.test("with one integer non-started", (Test test) ->
                {
                    final Iterator<Integer> integers = Iterator.create(5);
                    final IntegerToUnicodeCodePointIterator iterator = IntegerToUnicodeCodePointIterator.create(integers);
                    test.assertNotNull(iterator);
                    test.assertFalse(iterator.hasStarted());
                    test.assertFalse(iterator.hasCurrent());
                    test.assertFalse(iterator.getReturnByteOrderMark());
                    test.assertNull(iterator.foundByteOrderMark());
                    test.assertTrue(iterator.getThrowOnNullIntegers());

                    test.assertEqual(Iterable.create(5), iterator.toList());

                    test.assertTrue(iterator.hasStarted());
                    test.assertFalse(iterator.hasCurrent());
                    test.assertFalse(iterator.getReturnByteOrderMark());
                    test.assertFalse(iterator.foundByteOrderMark());
                });

                runner.test("with one integer started", (Test test) ->
                {
                    final Iterator<Integer> integers = Iterator.create(5);
                    integers.next();
                    final IntegerToUnicodeCodePointIterator iterator = IntegerToUnicodeCodePointIterator.create(integers);
                    test.assertNotNull(iterator);
                    test.assertTrue(iterator.hasStarted());
                    test.assertTrue(iterator.hasCurrent());
                    test.assertEqual(5, iterator.getCurrent());
                    test.assertFalse(iterator.getReturnByteOrderMark());
                    test.assertFalse(iterator.foundByteOrderMark());
                    test.assertTrue(iterator.getThrowOnNullIntegers());

                    test.assertEqual(Iterable.create(5), iterator.toList());

                    test.assertTrue(iterator.hasStarted());
                    test.assertFalse(iterator.hasCurrent());
                    test.assertFalse(iterator.getReturnByteOrderMark());
                    test.assertFalse(iterator.foundByteOrderMark());
                });

                runner.test("with multiple integers non-started", (Test test) ->
                {
                    final Iterator<Integer> integers = Iterator.create(5, 10, 15);
                    final IntegerToUnicodeCodePointIterator iterator = IntegerToUnicodeCodePointIterator.create(integers);
                    test.assertNotNull(iterator);
                    test.assertFalse(iterator.hasStarted());
                    test.assertFalse(iterator.hasCurrent());
                    test.assertFalse(iterator.getReturnByteOrderMark());
                    test.assertNull(iterator.foundByteOrderMark());
                    test.assertTrue(iterator.getThrowOnNullIntegers());

                    test.assertEqual(Iterable.create(5, 10, 15), iterator.toList());

                    test.assertTrue(iterator.hasStarted());
                    test.assertFalse(iterator.hasCurrent());
                    test.assertFalse(iterator.getReturnByteOrderMark());
                    test.assertFalse(iterator.foundByteOrderMark());
                });

                runner.test("with multiple integers started", (Test test) ->
                {
                    final Iterator<Integer> integers = Iterator.create(5, 10, 15);
                    integers.next();
                    final IntegerToUnicodeCodePointIterator iterator = IntegerToUnicodeCodePointIterator.create(integers);
                    test.assertNotNull(iterator);
                    test.assertTrue(iterator.hasStarted());
                    test.assertTrue(iterator.hasCurrent());
                    test.assertFalse(iterator.getReturnByteOrderMark());
                    test.assertFalse(iterator.foundByteOrderMark());
                    test.assertEqual(5, iterator.getCurrent());
                    test.assertTrue(iterator.getThrowOnNullIntegers());

                    test.assertEqual(Iterable.create(5, 10, 15), iterator.toList());

                    test.assertTrue(iterator.hasStarted());
                    test.assertFalse(iterator.hasCurrent());
                    test.assertFalse(iterator.getReturnByteOrderMark());
                    test.assertFalse(iterator.foundByteOrderMark());
                });
            });

            runner.testGroup("next()", () ->
            {
                final Action3<Iterable<Integer>,Boolean,Throwable> nextErrorTest = (Iterable<Integer> integers, Boolean started, Throwable expected) ->
                {
                    runner.test("with " + (started ? "" : "non-") + "started " + integers, (Test test) ->
                    {
                        final Iterator<Integer> integersIterator = integers.iterate();
                        if (started)
                        {
                            integersIterator.next();
                        }
                        final IntegerToUnicodeCodePointIterator iterator = IntegerToUnicodeCodePointIterator.create(integersIterator);

                        test.assertThrows(expected, () ->
                        {
                            while (iterator.next())
                            {
                                test.assertTrue(iterator.hasCurrent());

                                iterator.getCurrent();
                            }
                        });
                    });
                };

                nextErrorTest.run(Iterable.create((Integer)null), false, new IllegalArgumentException("Cannot have a null Unicode code point."));
                nextErrorTest.run(Iterable.create(97, null, 98), false, new IllegalArgumentException("Cannot have a null Unicode code point."));
                nextErrorTest.run(Iterable.create(97, null, 98), true, new IllegalArgumentException("Cannot have a null Unicode code point."));

                final Action3<Iterable<Integer>,Boolean,Iterable<Integer>> nextTest = (Iterable<Integer> integers, Boolean started, Iterable<Integer> expected) ->
                {
                    runner.test("with " + (started ? "" : "non-") + "started " + integers.map(value -> Integers.toHexString(value, true)), (Test test) ->
                    {
                        final Iterator<Integer> integerInterator = integers.iterate();
                        if (started)
                        {
                            integerInterator.next();
                        }
                        final IntegerToUnicodeCodePointIterator iterator = IntegerToUnicodeCodePointIterator.create(integerInterator);

                        for (final Integer expectedUnicodeCodePoint : expected)
                        {
                            test.assertTrue(iterator.next());
                            test.assertTrue(iterator.hasStarted());
                            test.assertTrue(iterator.hasCurrent());
                            test.assertEqual(expectedUnicodeCodePoint, iterator.getCurrent());
                        }

                        test.assertFalse(iterator.next());
                        test.assertTrue(iterator.hasStarted());
                        test.assertFalse(iterator.hasCurrent());
                    });
                };

                nextTest.run(Iterable.create(), false, Iterable.create());
                nextTest.run(Iterable.create(), true, Iterable.create());
                nextTest.run(Iterable.create(97), false, Iterable.create(97));
                nextTest.run(Iterable.create(97), true, Iterable.create());
                nextTest.run(Iterable.create(97, 98, 99), false, Iterable.create(97, 98, 99));
                nextTest.run(Iterable.create(0xFEFF), false, Iterable.create());
                nextTest.run(Iterable.create(0xFEFF), true, Iterable.create());
            });

            runner.testGroup("setReturnByteOrderMark(boolean)", () ->
            {
                runner.test("iterate over non-byte order mark when false", (Test test) ->
                {
                    final IntegerToUnicodeCodePointIterator iterator = IntegerToUnicodeCodePointIterator.create(Iterator.create(97))
                        .setReturnByteOrderMark(false);
                    test.assertEqual(
                        Iterable.create(97),
                        iterator.toList());
                    test.assertFalse(iterator.foundByteOrderMark());
                });

                runner.test("iterate over non-byte order mark when true", (Test test) ->
                {
                    final IntegerToUnicodeCodePointIterator iterator = IntegerToUnicodeCodePointIterator.create(Iterator.create(97))
                        .setReturnByteOrderMark(true);
                    test.assertEqual(
                        Iterable.create(97),
                        iterator.toList());
                    test.assertFalse(iterator.foundByteOrderMark());
                });

                runner.test("iterate over byte order mark when false", (Test test) ->
                {
                    final IntegerToUnicodeCodePointIterator iterator = IntegerToUnicodeCodePointIterator.create(Iterator.create(0xFEFF, 97))
                        .setReturnByteOrderMark(false);
                    test.assertEqual(
                        Iterable.create(97),
                        iterator.toList());
                    test.assertTrue(iterator.foundByteOrderMark());
                });

                runner.test("iterate over byte order mark when true", (Test test) ->
                {
                    final IntegerToUnicodeCodePointIterator iterator = IntegerToUnicodeCodePointIterator.create(Iterator.create(0xFEFF, 97))
                        .setReturnByteOrderMark(true);
                    test.assertEqual(
                        Iterable.create(0xFEFF, 97),
                        iterator.toList());
                    test.assertTrue(iterator.foundByteOrderMark());
                });
            });

            runner.testGroup("setThrowOnNullIntegers(boolean)", () ->
            {
                runner.test("with false", (Test test) ->
                {
                    final IntegerToUnicodeCodePointIterator iterator = IntegerToUnicodeCodePointIterator.create(Iterator.create(1, null, 2))
                        .setThrowOnNullIntegers(false);
                    test.assertEqual(Iterable.create(1, null, 2), iterator.toList());
                });

                runner.test("with true", (Test test) ->
                {
                    final IntegerToUnicodeCodePointIterator iterator = IntegerToUnicodeCodePointIterator.create(Iterator.create(1, null, 2))
                        .setThrowOnNullIntegers(true);
                    test.assertThrows(() -> iterator.toList(),
                        new IllegalArgumentException("Cannot have a null Unicode code point."));
                });
            });
        });
    }
}
