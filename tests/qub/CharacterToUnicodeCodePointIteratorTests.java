package qub;

public interface CharacterToUnicodeCodePointIteratorTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(CharacterToUnicodeCodePointIterator.class, () ->
        {
            runner.testGroup("create(Iterator<Character>)", () ->
            {
                final Action2<Iterable<Character>,Throwable> createErrorTest = (Iterable<Character> characters, Throwable expected) ->
                {
                    runner.test("with " + (characters == null ? null : characters.map(c -> c == null ? "null" : "0x" + Integers.toHexString(c, true))), (Test test) ->
                    {
                        test.assertThrows(expected, () ->
                        {
                            final CharacterToUnicodeCodePointIterator iterator = CharacterToUnicodeCodePointIterator.create(characters == null ? null : characters.iterate());
                            while (iterator.next())
                            {
                            }
                        });
                    });
                };

                createErrorTest.run(null, new PreConditionFailure("characters cannot be null."));
                createErrorTest.run(Iterable.create((char)0xD801), new IllegalArgumentException("Missing low-surrogate character (between 0xDC00 and 0xDFFF) after high-surrogate character (between 0xD800 and 0xDBFF)."));
                createErrorTest.run(Iterable.create((char)0xD801, (char)97), new IllegalArgumentException("Expected low-surrogate character (between 0xDC00 and 0xDFFF) after high surrogate character (between 0xD800 and 0xDBFF), but found 0x61 instead."));
                createErrorTest.run(Iterable.create((char)0xDC00), new IllegalArgumentException("Expected to find a non-surrogate character (not between 0xD800 and 0xDFFF) or high-surrogate character (between 0xD800 and 0xDBFF, but found a low surrogate character instead (0xDC00)."));
                createErrorTest.run(Iterable.create((char)0xDC00, null), new IllegalArgumentException("Expected to find a non-surrogate character (not between 0xD800 and 0xDFFF) or high-surrogate character (between 0xD800 and 0xDBFF, but found a low surrogate character instead (0xDC00)."));

                final Action2<Iterable<Character>,Iterable<Integer>> createTest = (Iterable<Character> characters, Iterable<Integer> expected) ->
                {
                    runner.test("with " + characters.map(c -> "0x" + Integers.toHexString(c, true)), (Test test) ->
                    {
                        final CharacterToUnicodeCodePointIterator iterator = CharacterToUnicodeCodePointIterator.create(characters.iterate());
                        test.assertNotNull(iterator);

                        test.assertFalse(iterator.hasStarted());
                        test.assertFalse(iterator.hasCurrent());
                        test.assertThrows(() -> iterator.getCurrent(),
                            new PreConditionFailure("this.hasCurrent() cannot be false."));

                        for (final Integer expectedCodePoint : expected)
                        {
                            test.assertTrue(iterator.next());
                            test.assertTrue(iterator.hasStarted());
                            test.assertTrue(iterator.hasCurrent());
                            test.assertEqual(expectedCodePoint, iterator.getCurrent());
                        }

                        test.assertFalse(iterator.next());
                        test.assertTrue(iterator.hasStarted());
                        test.assertFalse(iterator.hasCurrent());
                        test.assertThrows(() -> iterator.getCurrent(),
                            new PreConditionFailure("this.hasCurrent() cannot be false."));
                    });
                };

                createTest.run(Iterable.create(), Iterable.create());
                createTest.run(Iterable.create('a'), Iterable.create(97));
                createTest.run(Iterable.create('a', 'b'), Iterable.create(97, 98));
                createTest.run(Iterable.create((char)252), Iterable.create(252));
                createTest.run(Iterable.create((char)256), Iterable.create(256));
                createTest.run(Iterable.create((char)0x6771), Iterable.create(0x6771));
                createTest.run(Iterable.create((char)0xD801, (char)0xDC00), Iterable.create(0x10400));
                createTest.run(Iterable.create('a', (char)0xD801, (char)0xDC00, 'b'), Iterable.create(97, 0x10400, 98));

                final Action2<Iterable<Character>,Iterable<Integer>> createWithStartedTest = (Iterable<Character> characters, Iterable<Integer> expected) ->
                {
                    runner.test("with started " + characters.map(c -> "0x" + Integers.toHexString(c, true)), (Test test) ->
                    {
                        final Iterator<Character> characterIterator = characters.iterate();
                        characterIterator.next();

                        final CharacterToUnicodeCodePointIterator iterator = CharacterToUnicodeCodePointIterator.create(characterIterator);
                        test.assertNotNull(iterator);

                        test.assertTrue(iterator.hasStarted());
                        if (!characters.any())
                        {
                            test.assertFalse(iterator.hasCurrent());
                            test.assertThrows(() -> iterator.getCurrent(),
                                new PreConditionFailure("this.hasCurrent() cannot be false."));
                        }
                        else
                        {
                            test.assertTrue(iterator.hasCurrent());
                            test.assertEqual(expected.first().await(), iterator.getCurrent());
                        }

                        for (final Integer expectedCodePoint : expected.skipFirst())
                        {
                            test.assertTrue(iterator.next());
                            test.assertTrue(iterator.hasStarted());
                            test.assertTrue(iterator.hasCurrent());
                            test.assertEqual(expectedCodePoint, iterator.getCurrent());
                        }

                        test.assertFalse(iterator.next());
                        test.assertTrue(iterator.hasStarted());
                        test.assertFalse(iterator.hasCurrent());
                        test.assertThrows(() -> iterator.getCurrent(),
                            new PreConditionFailure("this.hasCurrent() cannot be false."));
                    });
                };

                createWithStartedTest.run(Iterable.create(), Iterable.create());
                createWithStartedTest.run(Iterable.create('a'), Iterable.create(97));
                createWithStartedTest.run(Iterable.create('a', 'b'), Iterable.create(97, 98));
                createWithStartedTest.run(Iterable.create((char)252), Iterable.create(252));
                createWithStartedTest.run(Iterable.create((char)256), Iterable.create(256));
                createWithStartedTest.run(Iterable.create((char)0x6771), Iterable.create(0x6771));
                createWithStartedTest.run(Iterable.create((char)0xD801, (char)0xDC00), Iterable.create(0x10400));
                createWithStartedTest.run(Iterable.create('a', (char)0xD801, (char)0xDC00, 'b'), Iterable.create(97, 0x10400, 98));
            });
        });
    }
}
