package qub;

public interface UnicodeCodePointToCharacterIteratorTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(UnicodeCodePointToCharacterIterator.class, () ->
        {
            runner.testGroup("create(Iterator<Integer>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> UnicodeCodePointToCharacterIterator.create(null),
                        new PreConditionFailure("unicodeCodePoints cannot be null."));
                });

                runner.test("with empty non-started", (Test test) ->
                {
                    final UnicodeCodePointToCharacterIterator iterator = UnicodeCodePointToCharacterIterator.create(Iterator.create());
                    test.assertNotNull(iterator);
                    test.assertFalse(iterator.hasStarted());
                    test.assertFalse(iterator.hasCurrent());
                    test.assertThrows(() -> iterator.getCurrent(),
                        new PreConditionFailure("this.hasCurrent() cannot be false."));
                });

                runner.test("with empty started", (Test test) ->
                {
                    final Iterator<Integer> unicodeCodePoints = Iterator.create();
                    unicodeCodePoints.next();
                    final UnicodeCodePointToCharacterIterator iterator = UnicodeCodePointToCharacterIterator.create(unicodeCodePoints);
                    test.assertNotNull(iterator);
                    test.assertTrue(iterator.hasStarted());
                    test.assertFalse(iterator.hasCurrent());
                    test.assertThrows(() -> iterator.getCurrent(),
                        new PreConditionFailure("this.hasCurrent() cannot be false."));
                });

                runner.test("with non-empty non-started", (Test test) ->
                {
                    Iterator<Integer> unicodeCodePoints = Iterator.create(97, 98, 99);
                    final UnicodeCodePointToCharacterIterator iterator = UnicodeCodePointToCharacterIterator.create(unicodeCodePoints);
                    test.assertNotNull(iterator);
                    test.assertFalse(iterator.hasStarted());
                    test.assertFalse(iterator.hasCurrent());
                    test.assertThrows(() -> iterator.getCurrent(),
                        new PreConditionFailure("this.hasCurrent() cannot be false."));
                });

                runner.test("with non-empty started", (Test test) ->
                {
                    Iterator<Integer> unicodeCodePoints = Iterator.create(97);
                    unicodeCodePoints.next();

                    final UnicodeCodePointToCharacterIterator iterator = UnicodeCodePointToCharacterIterator.create(unicodeCodePoints);
                    test.assertNotNull(iterator);
                    test.assertTrue(iterator.hasStarted());
                    test.assertTrue(iterator.hasCurrent());
                    test.assertEqual('a', iterator.getCurrent());
                    test.assertEqual(97, unicodeCodePoints.getCurrent());
                });
            });

            runner.testGroup("next()", () ->
            {
                final Action3<Iterable<Integer>,Boolean,Throwable> nextErrorTest = (Iterable<Integer> unicodeCodePoints, Boolean started, Throwable expected) ->
                {
                    runner.test("with " + (started ? "" : "non-") + "started " + unicodeCodePoints, (Test test) ->
                    {
                        final Iterator<Integer> unicodeCodePointsIterator = unicodeCodePoints.iterate();
                        if (started)
                        {
                            unicodeCodePointsIterator.next();
                        }
                        final UnicodeCodePointToCharacterIterator iterator = UnicodeCodePointToCharacterIterator.create(unicodeCodePointsIterator);

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

                nextErrorTest.run(Iterable.create((Integer)null), false, new IllegalArgumentException("Can't decode null unicode code point integer."));
                nextErrorTest.run(Iterable.create(97, null, 98), false, new IllegalArgumentException("Can't decode null unicode code point integer."));
                nextErrorTest.run(Iterable.create(97, null, 98), true, new IllegalArgumentException("Can't decode null unicode code point integer."));

                final Action3<Iterable<Integer>,Boolean,Iterable<Character>> nextTest = (Iterable<Integer> unicodeCodePoints, Boolean started, Iterable<Character> expected) ->
                {
                    runner.test("with " + (started ? "" : "non-") + "started " + unicodeCodePoints, (Test test) ->
                    {
                        final Iterator<Integer> unicodeCodePointsIterator = unicodeCodePoints.iterate();
                        if (started)
                        {
                            unicodeCodePointsIterator.next();
                        }
                        final UnicodeCodePointToCharacterIterator iterator = UnicodeCodePointToCharacterIterator.create(unicodeCodePointsIterator);

                        for (final Character expectedCharacter : expected)
                        {
                            test.assertTrue(iterator.next());
                            test.assertTrue(iterator.hasStarted());
                            test.assertTrue(iterator.hasCurrent());
                            test.assertEqual(expectedCharacter, iterator.getCurrent());
                        }

                        test.assertFalse(iterator.next());
                        test.assertTrue(iterator.hasStarted());
                        test.assertFalse(iterator.hasCurrent());
                    });
                };

                nextTest.run(Iterable.create(), false, Iterable.create());
                nextTest.run(Iterable.create(), true, Iterable.create());
                nextTest.run(Iterable.create(97), false, Iterable.create('a'));
                nextTest.run(Iterable.create(97), true, Iterable.create());
                nextTest.run(Iterable.create(97, 98, 99), false, Iterable.create('a', 'b', 'c'));
                nextTest.run(Iterable.create(97, 98, 99), true, Iterable.create('b', 'c'));
                nextTest.run(Iterable.create(0x00DF), false, Iterable.create((char)0x00DF));
                nextTest.run(Iterable.create(0x6771), false, Iterable.create((char)0x6771));
                nextTest.run(Iterable.create(0x6771), false, Iterable.create((char)0x6771));
                nextTest.run(Iterable.create(0x10400), false, Iterable.create((char)0xD801, (char)0xDC00));
                nextTest.run(Iterable.create(0x10400), true, Iterable.create());
            });
        });
    }
}
