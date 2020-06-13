package qub;

public interface CharacterReadStreamIteratorTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(CharacterReadStreamIterator.class, () ->
        {
            runner.testGroup("create(CharacterReadStream)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> CharacterReadStreamIterator.create(null),
                        new PreConditionFailure("characterReadStream cannot be null."));
                });

                runner.test("with disposed", (Test test) ->
                {
                    final InMemoryCharacterStream byteReadStream = InMemoryCharacterStream.create().endOfStream();
                    byteReadStream.dispose();
                    final CharacterReadStreamIterator iterator = CharacterReadStreamIterator.create(byteReadStream);
                    test.assertNotNull(iterator);
                    test.assertFalse(iterator.hasStarted());
                    test.assertFalse(iterator.hasCurrent());
                    test.assertThrows(iterator::getCurrent,
                        new PreConditionFailure("this.hasCurrent() cannot be false."));
                    test.assertThrows(iterator::getCurrentChar,
                        new PreConditionFailure("this.hasCurrent() cannot be false."));

                    test.assertThrows(iterator::next,
                        new PreConditionFailure("this.isDisposed() cannot be true."));
                    test.assertTrue(iterator.hasStarted());
                    test.assertFalse(iterator.hasCurrent());
                    test.assertThrows(iterator::getCurrent,
                        new PreConditionFailure("this.hasCurrent() cannot be false."));
                    test.assertThrows(iterator::getCurrentChar,
                        new PreConditionFailure("this.hasCurrent() cannot be false."));
                });

                runner.test("with empty", (Test test) ->
                {
                    final InMemoryCharacterStream byteReadStream = InMemoryCharacterStream.create().endOfStream();
                    final CharacterReadStreamIterator iterator = CharacterReadStreamIterator.create(byteReadStream);
                    test.assertNotNull(iterator);
                    test.assertFalse(iterator.hasStarted());
                    test.assertFalse(iterator.hasCurrent());
                    test.assertThrows(iterator::getCurrent,
                        new PreConditionFailure("this.hasCurrent() cannot be false."));
                    test.assertThrows(iterator::getCurrentChar,
                        new PreConditionFailure("this.hasCurrent() cannot be false."));

                    test.assertFalse(iterator.next());
                    test.assertTrue(iterator.hasStarted());
                    test.assertFalse(iterator.hasCurrent());
                    test.assertThrows(iterator::getCurrent,
                        new PreConditionFailure("this.hasCurrent() cannot be false."));
                    test.assertThrows(iterator::getCurrentChar,
                        new PreConditionFailure("this.hasCurrent() cannot be false."));
                });

                runner.test("with non-empty", (Test test) ->
                {
                    final char[] characters = new char[] { 'a', 'b', 'c' };
                    final InMemoryCharacterStream byteReadStream = InMemoryCharacterStream.create(characters).endOfStream();
                    final CharacterReadStreamIterator iterator = CharacterReadStreamIterator.create(byteReadStream);
                    test.assertNotNull(iterator);
                    test.assertFalse(iterator.hasStarted());
                    test.assertFalse(iterator.hasCurrent());
                    test.assertThrows(iterator::getCurrent,
                        new PreConditionFailure("this.hasCurrent() cannot be false."));
                    test.assertThrows(iterator::getCurrentChar,
                        new PreConditionFailure("this.hasCurrent() cannot be false."));

                    for (int i = 0; i < characters.length; ++i)
                    {
                        test.assertTrue(iterator.next());

                        test.assertTrue(iterator.hasStarted());
                        test.assertTrue(iterator.hasCurrent());

                        final char expectedCharacter = characters[i];
                        test.assertEqual(expectedCharacter, iterator.getCurrent());
                        test.assertEqual(expectedCharacter, iterator.getCurrentChar());
                    }

                    test.assertFalse(iterator.next());
                    test.assertTrue(iterator.hasStarted());
                    test.assertFalse(iterator.hasCurrent());
                });
            });
        });
    }
}
