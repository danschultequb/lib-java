package qub;

public interface ByteReadStreamIteratorTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(ByteReadStreamIterator.class, () ->
        {
            runner.testGroup("create(ByteReadStream)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> ByteReadStreamIterator.create(null),
                        new PreConditionFailure("byteReadStream cannot be null."));
                });

                runner.test("with disposed", (Test test) ->
                {
                    final InMemoryByteStream byteReadStream = InMemoryByteStream.create().endOfStream();
                    byteReadStream.dispose();
                    final ByteReadStreamIterator iterator = ByteReadStreamIterator.create(byteReadStream);
                    test.assertNotNull(iterator);
                    test.assertFalse(iterator.hasStarted());
                    test.assertFalse(iterator.hasCurrent());
                    test.assertThrows(iterator::getCurrent,
                        new PreConditionFailure("this.hasCurrent() cannot be false."));
                    test.assertThrows(iterator::getCurrentByte,
                        new PreConditionFailure("this.hasCurrent() cannot be false."));

                    test.assertThrows(iterator::next,
                        new PreConditionFailure("this.isDisposed() cannot be true."));
                    test.assertTrue(iterator.hasStarted());
                    test.assertFalse(iterator.hasCurrent());
                    test.assertThrows(iterator::getCurrent,
                        new PreConditionFailure("this.hasCurrent() cannot be false."));
                    test.assertThrows(iterator::getCurrentByte,
                        new PreConditionFailure("this.hasCurrent() cannot be false."));
                });

                runner.test("with empty", (Test test) ->
                {
                    final InMemoryByteStream byteReadStream = InMemoryByteStream.create().endOfStream();
                    final ByteReadStreamIterator iterator = ByteReadStreamIterator.create(byteReadStream);
                    test.assertNotNull(iterator);
                    test.assertFalse(iterator.hasStarted());
                    test.assertFalse(iterator.hasCurrent());
                    test.assertThrows(iterator::getCurrent,
                        new PreConditionFailure("this.hasCurrent() cannot be false."));
                    test.assertThrows(iterator::getCurrentByte,
                        new PreConditionFailure("this.hasCurrent() cannot be false."));

                    test.assertFalse(iterator.next());
                    test.assertTrue(iterator.hasStarted());
                    test.assertFalse(iterator.hasCurrent());
                    test.assertThrows(iterator::getCurrent,
                        new PreConditionFailure("this.hasCurrent() cannot be false."));
                    test.assertThrows(iterator::getCurrentByte,
                        new PreConditionFailure("this.hasCurrent() cannot be false."));
                });

                runner.test("with non-empty", (Test test) ->
                {
                    final byte[] bytes = new byte[] { 0, 1, 2 };
                    final InMemoryByteStream byteReadStream = InMemoryByteStream.create(bytes).endOfStream();
                    final ByteReadStreamIterator iterator = ByteReadStreamIterator.create(byteReadStream);
                    test.assertNotNull(iterator);
                    test.assertFalse(iterator.hasStarted());
                    test.assertFalse(iterator.hasCurrent());
                    test.assertThrows(iterator::getCurrent,
                        new PreConditionFailure("this.hasCurrent() cannot be false."));
                    test.assertThrows(iterator::getCurrentByte,
                        new PreConditionFailure("this.hasCurrent() cannot be false."));

                    for (int i = 0; i < bytes.length; ++i)
                    {
                        test.assertTrue(iterator.next());

                        test.assertTrue(iterator.hasStarted());
                        test.assertTrue(iterator.hasCurrent());

                        final byte expectedByte = bytes[i];
                        test.assertEqual(expectedByte, iterator.getCurrent());
                        test.assertEqual(expectedByte, iterator.getCurrentByte());
                    }

                    test.assertFalse(iterator.next());
                    test.assertTrue(iterator.hasStarted());
                    test.assertFalse(iterator.hasCurrent());
                });
            });
        });
    }
}
