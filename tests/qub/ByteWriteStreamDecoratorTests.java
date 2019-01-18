package qub;

public class ByteWriteStreamDecoratorTests
{
    private static ByteWriteStream createDecorator(ByteWriteStream innerStream, Value<Integer> counter)
    {
        return new ByteWriteStreamDecorator(innerStream)
        {
            @Override
            public Result<Integer> writeBytes(byte[] toWrite, int startIndex, int length)
            {
                return innerStream.writeBytes(toWrite, startIndex, length)
                    .then((Integer writtenByteCount) -> counter.set(counter.get() + writtenByteCount));
            }
        };
    }

    public static void test(TestRunner runner)
    {
        runner.testGroup(ByteWriteStreamDecorator.class, () ->
        {
            runner.test("with null innerStream", (Test test) ->
            {
                test.assertThrows(() -> createDecorator(null, Value.create()), new PreConditionFailure("innerStream cannot be null."));
            });

            runner.test("with non-null innerStream", (Test test) ->
            {
                final Value<Integer> counter = Value.create(0);
                final InMemoryByteStream innerStream = new InMemoryByteStream();
                final ByteWriteStream decorator = createDecorator(innerStream, counter);

                test.assertSuccess(true, decorator.writeByte((byte)5));
                test.assertEqual(1, counter.get());
                test.assertEqual(new byte[] { 5 }, innerStream.getBytes());

                test.assertSuccess(3, decorator.writeBytes(new byte[] { 6, 7, 8 }));
                test.assertEqual(4, counter.get());
                test.assertEqual(new byte[] { 5, 6, 7, 8 }, innerStream.getBytes());

                test.assertSuccess(true, decorator.writeAllBytes(new byte[] { 9, 10 }));
                test.assertEqual(6, counter.get());
                test.assertEqual(new byte[] { 5, 6, 7, 8, 9, 10 }, innerStream.getBytes());

                test.assertSuccess(true, decorator.writeAllBytes(new InMemoryByteStream(new byte[] { 11, 12, 13 }).endOfStream()));
                test.assertEqual(9, counter.get());
                test.assertEqual(new byte[] { 5, 6, 7, 8, 9, 10, 11, 12, 13 }, innerStream.getBytes());

                final CharacterWriteStream characterWriteStreamDecorator1 = decorator.asCharacterWriteStream();
                test.assertNotNull(characterWriteStreamDecorator1);
                test.assertSuccess(true, characterWriteStreamDecorator1.write('a'));
                test.assertEqual(10, counter.get());
                test.assertEqual(new byte[] { 5, 6, 7, 8, 9, 10, 11, 12, 13, 97 }, innerStream.getBytes());

                final CharacterWriteStream characterWriteStreamDecorator2 = decorator.asCharacterWriteStream(CharacterEncoding.US_ASCII);
                test.assertNotNull(characterWriteStreamDecorator2);
                test.assertEqual(CharacterEncoding.US_ASCII, characterWriteStreamDecorator2.getCharacterEncoding());
                test.assertSuccess(true, characterWriteStreamDecorator2.write('b'));
                test.assertEqual(11, counter.get());
                test.assertEqual(new byte[] { 5, 6, 7, 8, 9, 10, 11, 12, 13, 97, 98 }, innerStream.getBytes());

                final LineWriteStream lineWriteStreamDecorator1 = decorator.asLineWriteStream();
                test.assertNotNull(lineWriteStreamDecorator1);
                test.assertSuccess(true, lineWriteStreamDecorator1.writeLine("hello"));
                test.assertEqual(17, counter.get());
                test.assertEqual(new byte[] { 5, 6, 7, 8, 9, 10, 11, 12, 13, 97, 98, 104, 101, 108, 108, 111, 10 }, innerStream.getBytes());

                final LineWriteStream lineWriteStreamDecorator2 = decorator.asLineWriteStream(CharacterEncoding.US_ASCII);
                test.assertNotNull(lineWriteStreamDecorator2);
                test.assertEqual(CharacterEncoding.US_ASCII, lineWriteStreamDecorator2.getCharacterEncoding());
                test.assertSuccess(true, lineWriteStreamDecorator2.writeLine("hey"));
                test.assertEqual(21, counter.get());
                test.assertEqual(new byte[] { 5, 6, 7, 8, 9, 10, 11, 12, 13, 97, 98, 104, 101, 108, 108, 111, 10, 104, 101, 121, 10 }, innerStream.getBytes());

                final LineWriteStream lineWriteStreamDecorator3 = decorator.asLineWriteStream("\r\n");
                test.assertNotNull(lineWriteStreamDecorator3);
                test.assertEqual("\r\n", lineWriteStreamDecorator3.getLineSeparator());
                test.assertSuccess(true, lineWriteStreamDecorator3.writeLine("ab"));
                test.assertEqual(25, counter.get());
                test.assertEqual(new byte[] { 5, 6, 7, 8, 9, 10, 11, 12, 13, 97, 98, 104, 101, 108, 108, 111, 10, 104, 101, 121, 10, 97, 98, 13, 10 }, innerStream.getBytes());

                final LineWriteStream lineWriteStreamDecorator4 = decorator.asLineWriteStream(CharacterEncoding.US_ASCII, "\r\n");
                test.assertNotNull(lineWriteStreamDecorator4);
                test.assertEqual(CharacterEncoding.US_ASCII, lineWriteStreamDecorator4.getCharacterEncoding());
                test.assertEqual("\r\n", lineWriteStreamDecorator4.getLineSeparator());
                test.assertSuccess(true, lineWriteStreamDecorator4.writeLine("c"));
                test.assertEqual(28, counter.get());
                test.assertEqual(new byte[] { 5, 6, 7, 8, 9, 10, 11, 12, 13, 97, 98, 104, 101, 108, 108, 111, 10, 104, 101, 121, 10, 97, 98, 13, 10, 99, 13, 10 }, innerStream.getBytes());

                test.assertFalse(decorator.isDisposed());
                test.assertFalse(innerStream.isDisposed());
                test.assertSuccess(true, decorator.dispose());
                test.assertTrue(decorator.isDisposed());
                test.assertTrue(innerStream.isDisposed());
                test.assertSuccess(false, decorator.dispose());
                test.assertTrue(decorator.isDisposed());
                test.assertTrue(innerStream.isDisposed());
            });
        });
    }
}