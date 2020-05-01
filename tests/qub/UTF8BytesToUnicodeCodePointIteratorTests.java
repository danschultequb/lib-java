package qub;

public interface UTF8BytesToUnicodeCodePointIteratorTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(UTF8BytesToUnicodeCodePointIterator.class, () ->
        {
            runner.testGroup("create(Iterator<Byte>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> UTF8BytesToUnicodeCodePointIterator.create(null),
                        new PreConditionFailure("bytes cannot be null."));
                });

                runner.test("with empty non-started", (Test test) ->
                {
                    final UTF8BytesToUnicodeCodePointIterator iterator = UTF8BytesToUnicodeCodePointIterator.create(Iterator.create());
                    test.assertNotNull(iterator);
                    test.assertFalse(iterator.hasStarted());
                    test.assertFalse(iterator.hasCurrent());
                    test.assertThrows(() -> iterator.getCurrent(),
                        new PreConditionFailure("this.hasCurrent() cannot be false."));
                });

                runner.test("with empty started", (Test test) ->
                {
                    final Iterator<Byte> unicodeCodePoints = Iterator.create();
                    unicodeCodePoints.next();
                    final UTF8BytesToUnicodeCodePointIterator iterator = UTF8BytesToUnicodeCodePointIterator.create(unicodeCodePoints);
                    test.assertNotNull(iterator);
                    test.assertTrue(iterator.hasStarted());
                    test.assertFalse(iterator.hasCurrent());
                    test.assertThrows(() -> iterator.getCurrent(),
                        new PreConditionFailure("this.hasCurrent() cannot be false."));
                });

                runner.test("with non-empty non-started", (Test test) ->
                {
                    Iterator<Byte> unicodeCodePoints = Iterator.create((byte)97, (byte)98, (byte)99);
                    final UTF8BytesToUnicodeCodePointIterator iterator = UTF8BytesToUnicodeCodePointIterator.create(unicodeCodePoints);
                    test.assertNotNull(iterator);
                    test.assertFalse(iterator.hasStarted());
                    test.assertFalse(iterator.hasCurrent());
                    test.assertThrows(() -> iterator.getCurrent(),
                        new PreConditionFailure("this.hasCurrent() cannot be false."));
                });

                runner.test("with non-empty started", (Test test) ->
                {
                    Iterator<Byte> unicodeCodePoints = Iterator.create((byte)97);
                    unicodeCodePoints.next();

                    final UTF8BytesToUnicodeCodePointIterator iterator = UTF8BytesToUnicodeCodePointIterator.create(unicodeCodePoints);
                    test.assertNotNull(iterator);
                    test.assertTrue(iterator.hasStarted());
                    test.assertTrue(iterator.hasCurrent());
                    test.assertEqual('a', iterator.getCurrent());
                    test.assertEqual((byte)97, unicodeCodePoints.getCurrent());
                });
            });

            runner.testGroup("next()", () ->
            {
                final Action3<Iterable<Byte>,Boolean,Throwable> nextErrorTest = (Iterable<Byte> utf8EncodedBytes, Boolean started, Throwable expected) ->
                {
                    runner.test("with " + (started ? "" : "non-") + "started " + utf8EncodedBytes, (Test test) ->
                    {
                        final Iterator<Byte> utf8EncodedBytesIterator = utf8EncodedBytes.iterate();
                        if (started)
                        {
                            utf8EncodedBytesIterator.next();
                        }
                        final UTF8BytesToUnicodeCodePointIterator iterator = UTF8BytesToUnicodeCodePointIterator.create(utf8EncodedBytesIterator);

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

                nextErrorTest.run(Iterable.create((Byte)null), false, new IllegalArgumentException("1st byte in decoded character cannot be null."));
                nextErrorTest.run(Iterable.create((byte)97, null, (byte)98), false, new IllegalArgumentException("1st byte in decoded character cannot be null."));
                nextErrorTest.run(Iterable.create((byte)97, null, (byte)98), true, new IllegalArgumentException("1st byte in decoded character cannot be null."));
                nextErrorTest.run(Iterable.create((byte)0x80), false, new IllegalArgumentException("Expected a leading byte, but found a continuation byte (0x80) instead."));
                nextErrorTest.run(Iterable.create((byte)0xF8), false, new IllegalArgumentException("Found an invalid leading byte (0xF8)."));
                nextErrorTest.run(Iterable.create((byte)0xC0), false, new IllegalArgumentException("Missing 2nd byte of 2 in decoded character."));
                nextErrorTest.run(Iterable.create((byte)0xE0), false, new IllegalArgumentException("Missing 2nd byte of 3 in decoded character."));
                nextErrorTest.run(Iterable.create((byte)0xF0), false, new IllegalArgumentException("Missing 2nd byte of 4 in decoded character."));
                nextErrorTest.run(Iterable.create((byte)0xC0, null), false, new IllegalArgumentException("2nd byte of 2 in decoded character cannot be null."));
                nextErrorTest.run(Iterable.create((byte)0xE0, null), false, new IllegalArgumentException("2nd byte of 3 in decoded character cannot be null."));
                nextErrorTest.run(Iterable.create((byte)0xF0, null), false, new IllegalArgumentException("2nd byte of 4 in decoded character cannot be null."));
                nextErrorTest.run(Iterable.create((byte)0xC0, (byte)0x00), false, new IllegalArgumentException("Expected 2nd byte of 2 to be a continuation byte (10xxxxxx), but found 0x0 instead."));
                nextErrorTest.run(Iterable.create((byte)0xE0, (byte)0x01), false, new IllegalArgumentException("Expected 2nd byte of 3 to be a continuation byte (10xxxxxx), but found 0x1 instead."));
                nextErrorTest.run(Iterable.create((byte)0xF0, (byte)0x02), false, new IllegalArgumentException("Expected 2nd byte of 4 to be a continuation byte (10xxxxxx), but found 0x2 instead."));
                nextErrorTest.run(Iterable.create((byte)0xE0, (byte)0x80), false, new IllegalArgumentException("Missing 3rd byte of 3 in decoded character."));
                nextErrorTest.run(Iterable.create((byte)0xF0, (byte)0x80), false, new IllegalArgumentException("Missing 3rd byte of 4 in decoded character."));
                nextErrorTest.run(Iterable.create((byte)0xE0, (byte)0x80, null), false, new IllegalArgumentException("3rd byte of 3 in decoded character cannot be null."));
                nextErrorTest.run(Iterable.create((byte)0xF0, (byte)0x80, null), false, new IllegalArgumentException("3rd byte of 4 in decoded character cannot be null."));
                nextErrorTest.run(Iterable.create((byte)0xE0, (byte)0x80, (byte)0x01), false, new IllegalArgumentException("Expected 3rd byte of 3 to be a continuation byte (10xxxxxx), but found 0x1 instead."));
                nextErrorTest.run(Iterable.create((byte)0xF0, (byte)0x80, (byte)0x02), false, new IllegalArgumentException("Expected 3rd byte of 4 to be a continuation byte (10xxxxxx), but found 0x2 instead."));
                nextErrorTest.run(Iterable.create((byte)0xF0, (byte)0x80, (byte)0x80), false, new IllegalArgumentException("Missing 4th byte of 4 in decoded character."));
                nextErrorTest.run(Iterable.create((byte)0xF0, (byte)0x80, (byte)0x80, null), false, new IllegalArgumentException("4th byte of 4 in decoded character cannot be null."));
                nextErrorTest.run(Iterable.create((byte)0xF0, (byte)0x80, (byte)0x80, (byte)0x02), false, new IllegalArgumentException("Expected 4th byte of 4 to be a continuation byte (10xxxxxx), but found 0x2 instead."));

                final Action3<Iterable<Byte>,Boolean,Iterable<Integer>> nextTest = (Iterable<Byte> utf8EncodedBytes, Boolean started, Iterable<Integer> expected) ->
                {
                    runner.test("with " + (started ? "" : "non-") + "started " + utf8EncodedBytes, (Test test) ->
                    {
                        final Iterator<Byte> utf8EncodedBytesIterator = utf8EncodedBytes.iterate();
                        if (started)
                        {
                            utf8EncodedBytesIterator.next();
                        }
                        final UTF8BytesToUnicodeCodePointIterator iterator = UTF8BytesToUnicodeCodePointIterator.create(utf8EncodedBytesIterator);

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
                nextTest.run(Iterable.create((byte)97), false, Iterable.create(97));
                nextTest.run(Iterable.create((byte)97), true, Iterable.create());
                nextTest.run(Iterable.create((byte)97, (byte)98, (byte)99), false, Iterable.create(97, 98, 99));
                nextTest.run(Iterable.create((byte)97, (byte)98, (byte)99), true, Iterable.create(98, 99));
                nextTest.run(Iterable.create((byte)0xC3, (byte)0x9F), false, Iterable.create(0x00DF));
                nextTest.run(Iterable.create((byte)0xE6, (byte)0x9D, (byte)0xB1), false, Iterable.create(0x6771));
                nextTest.run(Iterable.create((byte)0xF0, (byte)0x90, (byte)0x90, (byte)0x80), false, Iterable.create(0x10400));
                nextTest.run(Iterable.create((byte)0xEF, (byte)0xBB, (byte)0xBF), false, Iterable.create(0xFEFF));
            });
        });
    }
}
