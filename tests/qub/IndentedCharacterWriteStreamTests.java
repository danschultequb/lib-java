package qub;

public interface IndentedCharacterWriteStreamTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(IndentedCharacterWriteStream.class, () ->
        {
            runner.testGroup("constructor()", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> new IndentedCharacterWriteStream(null),
                        new PreConditionFailure("innerStream cannot be null."));
                });

                runner.test("with disposed", (Test test) ->
                {
                    final InMemoryCharacterStream innerStream = new InMemoryCharacterStream();
                    innerStream.dispose().await();

                    final IndentedCharacterWriteStream writeStream = new IndentedCharacterWriteStream(innerStream);
                    test.assertTrue(writeStream.isDisposed());
                    test.assertEqual("  ", writeStream.getSingleIndent());
                    test.assertEqual("", writeStream.getCurrentIndent());
                });

                runner.test("with not disposed", (Test test) ->
                {
                    final InMemoryCharacterStream innerStream = new InMemoryCharacterStream();
                    final IndentedCharacterWriteStream writeStream = new IndentedCharacterWriteStream(innerStream);
                    test.assertFalse(writeStream.isDisposed());
                    test.assertEqual("  ", writeStream.getSingleIndent());
                    test.assertEqual("", writeStream.getCurrentIndent());
                });
            });

            runner.testGroup("setCurrentIndent()", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final InMemoryCharacterStream innerStream = new InMemoryCharacterStream();
                    final IndentedCharacterWriteStream writeStream = new IndentedCharacterWriteStream(innerStream);
                    test.assertEqual("", writeStream.getCurrentIndent());
                    test.assertThrows(() -> writeStream.setCurrentIndent(null),
                        new PreConditionFailure("currentIndent cannot be null."));
                    test.assertEqual("", writeStream.getCurrentIndent());
                });

                runner.test("with empty", (Test test) ->
                {
                    final InMemoryCharacterStream innerStream = new InMemoryCharacterStream();
                    final IndentedCharacterWriteStream writeStream = new IndentedCharacterWriteStream(innerStream);
                    test.assertEqual("", writeStream.getCurrentIndent());
                    test.assertSame(writeStream, writeStream.setCurrentIndent(""));
                    test.assertEqual("", writeStream.getCurrentIndent());
                });

                runner.test("with non-empty", (Test test) ->
                {
                    final InMemoryCharacterStream innerStream = new InMemoryCharacterStream();
                    final IndentedCharacterWriteStream writeStream = new IndentedCharacterWriteStream(innerStream);
                    test.assertEqual("", writeStream.getCurrentIndent());
                    test.assertSame(writeStream, writeStream.setCurrentIndent("    "));
                    test.assertEqual("    ", writeStream.getCurrentIndent());
                });
            });

            runner.testGroup("setSingleIndent()", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final InMemoryCharacterStream innerStream = new InMemoryCharacterStream();
                    final IndentedCharacterWriteStream writeStream = new IndentedCharacterWriteStream(innerStream);
                    test.assertEqual("  ", writeStream.getSingleIndent());
                    test.assertThrows(() -> writeStream.setSingleIndent(null),
                        new PreConditionFailure("singleIndent cannot be null."));
                    test.assertEqual("  ", writeStream.getSingleIndent());
                });

                runner.test("with empty", (Test test) ->
                {
                    final InMemoryCharacterStream innerStream = new InMemoryCharacterStream();
                    final IndentedCharacterWriteStream writeStream = new IndentedCharacterWriteStream(innerStream);
                    test.assertEqual("  ", writeStream.getSingleIndent());
                    test.assertSame(writeStream, writeStream.setSingleIndent(""));
                    test.assertEqual("", writeStream.getSingleIndent());
                });

                runner.test("with non-empty", (Test test) ->
                {
                    final InMemoryCharacterStream innerStream = new InMemoryCharacterStream();
                    final IndentedCharacterWriteStream writeStream = new IndentedCharacterWriteStream(innerStream);
                    test.assertEqual("  ", writeStream.getSingleIndent());
                    test.assertSame(writeStream, writeStream.setSingleIndent("    "));
                    test.assertEqual("    ", writeStream.getSingleIndent());
                });
            });

            runner.testGroup("decreaseIndent()", () ->
            {
                runner.test("with no current indent", (Test test) ->
                {
                    final InMemoryCharacterStream innerStream = new InMemoryCharacterStream();
                    final IndentedCharacterWriteStream writeStream = new IndentedCharacterWriteStream(innerStream);
                    test.assertEqual("", writeStream.getCurrentIndent());
                    test.assertSame(writeStream, writeStream.decreaseIndent());
                    test.assertEqual("", writeStream.getCurrentIndent());
                });

                runner.test("with current indent (\" \") that is smaller than a single indent (\"  \")", (Test test) ->
                {
                    final InMemoryCharacterStream innerStream = new InMemoryCharacterStream();
                    final IndentedCharacterWriteStream writeStream = new IndentedCharacterWriteStream(innerStream);
                    test.assertEqual("  ", writeStream.getSingleIndent());
                    test.assertSame(writeStream, writeStream.setCurrentIndent(" "));
                    test.assertSame(writeStream, writeStream.decreaseIndent());
                    test.assertEqual("", writeStream.getCurrentIndent());
                    test.assertEqual("  ", writeStream.getSingleIndent());
                });

                runner.test("with current indent (\"\\t\") that is smaller than a single indent (\"  \")", (Test test) ->
                {
                    final InMemoryCharacterStream innerStream = new InMemoryCharacterStream();
                    final IndentedCharacterWriteStream writeStream = new IndentedCharacterWriteStream(innerStream);
                    test.assertEqual("  ", writeStream.getSingleIndent());
                    test.assertSame(writeStream, writeStream.setCurrentIndent("\t"));
                    test.assertSame(writeStream, writeStream.decreaseIndent());
                    test.assertEqual("", writeStream.getCurrentIndent());
                    test.assertEqual("  ", writeStream.getSingleIndent());
                });

                runner.test("with current indent (\"  \") that is equal to a single indent (\"  \").", (Test test) ->
                {
                    final InMemoryCharacterStream innerStream = new InMemoryCharacterStream();
                    final IndentedCharacterWriteStream writeStream = new IndentedCharacterWriteStream(innerStream);
                    test.assertEqual("  ", writeStream.getSingleIndent());
                    test.assertSame(writeStream, writeStream.setCurrentIndent("  "));
                    test.assertSame(writeStream, writeStream.decreaseIndent());
                    test.assertEqual("", writeStream.getCurrentIndent());
                    test.assertEqual("  ", writeStream.getSingleIndent());
                });

                runner.test("with current indent (\"   \") that is larger than a single indent (\"  \").", (Test test) ->
                {
                    final InMemoryCharacterStream innerStream = new InMemoryCharacterStream();
                    final IndentedCharacterWriteStream writeStream = new IndentedCharacterWriteStream(innerStream);
                    test.assertEqual("  ", writeStream.getSingleIndent());
                    test.assertSame(writeStream, writeStream.setCurrentIndent("   "));
                    test.assertSame(writeStream, writeStream.decreaseIndent());
                    test.assertEqual(" ", writeStream.getCurrentIndent());
                    test.assertEqual("  ", writeStream.getSingleIndent());
                });
            });

            runner.testGroup("indent()", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final InMemoryCharacterStream innerStream = new InMemoryCharacterStream();
                    final IndentedCharacterWriteStream writeStream = new IndentedCharacterWriteStream(innerStream);
                    test.assertThrows(() -> writeStream.indent(null),
                        new PreConditionFailure("action cannot be null."));
                    test.assertEqual("", writeStream.getCurrentIndent());
                    test.assertEqual("  ", writeStream.getSingleIndent());
                    test.assertEqual("", innerStream.getText().await());
                });

                runner.test("with empty action", (Test test) ->
                {
                    final InMemoryCharacterStream innerStream = new InMemoryCharacterStream();
                    final IndentedCharacterWriteStream writeStream = new IndentedCharacterWriteStream(innerStream);
                    writeStream.indent(Action0.empty);
                    test.assertEqual("", writeStream.getCurrentIndent());
                    test.assertEqual("  ", writeStream.getSingleIndent());
                    test.assertEqual("", innerStream.getText().await());
                });

                runner.test("with one write action on empty stream", (Test test) ->
                {
                    final InMemoryCharacterStream innerStream = new InMemoryCharacterStream();
                    final IndentedCharacterWriteStream writeStream = new IndentedCharacterWriteStream(innerStream);
                    writeStream.indent(() ->
                    {
                        writeStream.write("hello").await();
                    });
                    test.assertEqual("", writeStream.getCurrentIndent());
                    test.assertEqual("  ", writeStream.getSingleIndent());
                    test.assertEqual("  hello", innerStream.getText().await());
                });

                runner.test("with one write action on non-empty mid-line stream", (Test test) ->
                {
                    final InMemoryCharacterStream innerStream = new InMemoryCharacterStream();
                    final IndentedCharacterWriteStream writeStream = new IndentedCharacterWriteStream(innerStream);
                    writeStream.write("abc").await();
                    writeStream.indent(() ->
                    {
                        writeStream.write("hello").await();
                    });
                    test.assertEqual("", writeStream.getCurrentIndent());
                    test.assertEqual("  ", writeStream.getSingleIndent());
                    test.assertEqual("abchello", innerStream.getText().await());
                });

                runner.test("with one write action on non-empty new-line stream", (Test test) ->
                {
                    final InMemoryCharacterStream innerStream = new InMemoryCharacterStream();
                    final IndentedCharacterWriteStream writeStream = new IndentedCharacterWriteStream(innerStream);
                    writeStream.writeLine("abc").await();
                    writeStream.indent(() ->
                    {
                        writeStream.write("hello").await();
                    });
                    test.assertEqual("", writeStream.getCurrentIndent());
                    test.assertEqual("  ", writeStream.getSingleIndent());
                    test.assertEqual("abc\n  hello", innerStream.getText().await());
                });

                runner.test("with two write actions on empty stream", (Test test) ->
                {
                    final InMemoryCharacterStream innerStream = new InMemoryCharacterStream();
                    final IndentedCharacterWriteStream writeStream = new IndentedCharacterWriteStream(innerStream);
                    writeStream.indent(() ->
                    {
                        writeStream.write("hello").await();
                        writeStream.write("there").await();
                    });
                    test.assertEqual("", writeStream.getCurrentIndent());
                    test.assertEqual("  ", writeStream.getSingleIndent());
                    test.assertEqual("  hellothere", innerStream.getText().await());
                });

                runner.test("with two write actions on non-empty mid-line stream", (Test test) ->
                {
                    final InMemoryCharacterStream innerStream = new InMemoryCharacterStream();
                    final IndentedCharacterWriteStream writeStream = new IndentedCharacterWriteStream(innerStream);
                    writeStream.write("I'd like to say, ").await();
                    writeStream.indent(() ->
                    {
                        writeStream.write("hello").await();
                        writeStream.write("there").await();
                    });
                    test.assertEqual("", writeStream.getCurrentIndent());
                    test.assertEqual("  ", writeStream.getSingleIndent());
                    test.assertEqual("I'd like to say, hellothere", innerStream.getText().await());
                });

                runner.test("with two write actions on non-empty new-line stream", (Test test) ->
                {
                    final InMemoryCharacterStream innerStream = new InMemoryCharacterStream();
                    final IndentedCharacterWriteStream writeStream = new IndentedCharacterWriteStream(innerStream);
                    writeStream.writeLine("I'd like to say, ").await();
                    writeStream.indent(() ->
                    {
                        writeStream.write("hello").await();
                        writeStream.write("there").await();
                    });
                    test.assertEqual("", writeStream.getCurrentIndent());
                    test.assertEqual("  ", writeStream.getSingleIndent());
                    test.assertEqual("I'd like to say, \n  hellothere", innerStream.getText().await());
                });

                runner.test("with one writeLine action on empty stream", (Test test) ->
                {
                    final InMemoryCharacterStream innerStream = new InMemoryCharacterStream();
                    final IndentedCharacterWriteStream writeStream = new IndentedCharacterWriteStream(innerStream);
                    writeStream.indent(() ->
                    {
                        writeStream.writeLine("hello").await();
                    });
                    test.assertEqual("", writeStream.getCurrentIndent());
                    test.assertEqual("  ", writeStream.getSingleIndent());
                    test.assertEqual("  hello\n", innerStream.getText().await());
                });

                runner.test("with one writeLine action on non-empty mid-line stream", (Test test) ->
                {
                    final InMemoryCharacterStream innerStream = new InMemoryCharacterStream();
                    final IndentedCharacterWriteStream writeStream = new IndentedCharacterWriteStream(innerStream);
                    writeStream.write("abc").await();
                    writeStream.indent(() ->
                    {
                        writeStream.writeLine("hello").await();
                    });
                    test.assertEqual("", writeStream.getCurrentIndent());
                    test.assertEqual("  ", writeStream.getSingleIndent());
                    test.assertEqual("abchello\n", innerStream.getText().await());
                });

                runner.test("with one writeLine action on non-empty new-line stream", (Test test) ->
                {
                    final InMemoryCharacterStream innerStream = new InMemoryCharacterStream();
                    final IndentedCharacterWriteStream writeStream = new IndentedCharacterWriteStream(innerStream);
                    writeStream.writeLine("abc").await();
                    writeStream.indent(() ->
                    {
                        writeStream.writeLine("hello").await();
                    });
                    test.assertEqual("", writeStream.getCurrentIndent());
                    test.assertEqual("  ", writeStream.getSingleIndent());
                    test.assertEqual("abc\n  hello\n", innerStream.getText().await());
                });

                runner.test("with two writeLine actions on empty stream", (Test test) ->
                {
                    final InMemoryCharacterStream innerStream = new InMemoryCharacterStream();
                    final IndentedCharacterWriteStream writeStream = new IndentedCharacterWriteStream(innerStream);
                    writeStream.indent(() ->
                    {
                        writeStream.writeLine("hello").await();
                        writeStream.writeLine("there").await();
                    });
                    test.assertEqual("", writeStream.getCurrentIndent());
                    test.assertEqual("  ", writeStream.getSingleIndent());
                    test.assertEqual("  hello\n  there\n", innerStream.getText().await());
                });

                runner.test("with two writeLine actions on non-empty mid-line stream", (Test test) ->
                {
                    final InMemoryCharacterStream innerStream = new InMemoryCharacterStream();
                    final IndentedCharacterWriteStream writeStream = new IndentedCharacterWriteStream(innerStream);
                    writeStream.write("I'd like to say, ").await();
                    writeStream.indent(() ->
                    {
                        writeStream.writeLine("hello").await();
                        writeStream.writeLine("there").await();
                    });
                    test.assertEqual("", writeStream.getCurrentIndent());
                    test.assertEqual("  ", writeStream.getSingleIndent());
                    test.assertEqual("I'd like to say, hello\n  there\n", innerStream.getText().await());
                });

                runner.test("with two writeLine actions on non-empty new-line stream", (Test test) ->
                {
                    final InMemoryCharacterStream innerStream = new InMemoryCharacterStream();
                    final IndentedCharacterWriteStream writeStream = new IndentedCharacterWriteStream(innerStream);
                    writeStream.writeLine("I'd like to say, ").await();
                    writeStream.indent(() ->
                    {
                        writeStream.writeLine("hello").await();
                        writeStream.writeLine("there").await();
                    });
                    writeStream.write("test").await();
                    test.assertEqual("", writeStream.getCurrentIndent());
                    test.assertEqual("  ", writeStream.getSingleIndent());
                    test.assertEqual("I'd like to say, \n  hello\n  there\ntest", innerStream.getText().await());
                });
            });

            runner.testGroup("write(char)", () ->
            {
                runner.test("when disposed", (Test test) ->
                {
                    final InMemoryCharacterStream innerStream = new InMemoryCharacterStream();
                    innerStream.dispose().await();
                    final IndentedCharacterWriteStream writeStream = new IndentedCharacterWriteStream(innerStream);
                    test.assertThrows(() -> writeStream.write('a'),
                        new PreConditionFailure("isDisposed() cannot be true."));
                    test.assertEqual("", innerStream.getText().await());
                });

                runner.test("with no indent and empty stream", (Test test) ->
                {
                    final InMemoryCharacterStream innerStream = new InMemoryCharacterStream();
                    final IndentedCharacterWriteStream writeStream = new IndentedCharacterWriteStream(innerStream);
                    test.assertEqual(1, writeStream.write('a').await());
                    test.assertEqual("a", innerStream.getText().await());
                });

                runner.test("with no indent and non-empty mid-line stream", (Test test) ->
                {
                    final InMemoryCharacterStream innerStream = new InMemoryCharacterStream();
                    final IndentedCharacterWriteStream writeStream = new IndentedCharacterWriteStream(innerStream);
                    test.assertEqual(1, writeStream.write('a').await());
                    test.assertEqual("a", innerStream.getText().await());
                    test.assertEqual(1, writeStream.write('b').await());
                    test.assertEqual("ab", innerStream.getText().await());
                });

                runner.test("with indent and empty stream", (Test test) ->
                {
                    final InMemoryCharacterStream innerStream = new InMemoryCharacterStream();
                    final IndentedCharacterWriteStream writeStream = new IndentedCharacterWriteStream(innerStream);
                    writeStream.indent(() ->
                    {
                        test.assertEqual(3, writeStream.write('a').await());
                    });
                    test.assertEqual("  a", innerStream.getText().await());
                });

                runner.test("with indent and non-empty mid-line stream", (Test test) ->
                {
                    final InMemoryCharacterStream innerStream = new InMemoryCharacterStream();
                    final IndentedCharacterWriteStream writeStream = new IndentedCharacterWriteStream(innerStream);
                    test.assertEqual(1, writeStream.write('z').await());
                    writeStream.indent(() ->
                    {
                        test.assertEqual(1, writeStream.write('a').await());
                    });
                    test.assertEqual("za", innerStream.getText().await());
                });

                runner.test("with new-line character", (Test test) ->
                {
                    final InMemoryCharacterStream innerStream = new InMemoryCharacterStream();
                    final IndentedCharacterWriteStream writeStream = new IndentedCharacterWriteStream(innerStream);
                    writeStream.write('\n').await();
                    test.assertEqual("\n", innerStream.getText().await());
                });
            });

            runner.testGroup("write(char[],int,int)", () ->
            {
                runner.test("with null characters", (Test test) ->
                {
                    final InMemoryCharacterStream innerStream = new InMemoryCharacterStream();
                    final IndentedCharacterWriteStream writeStream = new IndentedCharacterWriteStream(innerStream);
                    test.assertThrows(() -> writeStream.write(null, 0, 1),
                        new PreConditionFailure("toWrite cannot be null."));
                    test.assertEqual("", innerStream.getText().await());
                });

                runner.test("with negative startIndex", (Test test) ->
                {
                    final InMemoryCharacterStream innerStream = new InMemoryCharacterStream();
                    final IndentedCharacterWriteStream writeStream = new IndentedCharacterWriteStream(innerStream);
                    test.assertThrows(() -> writeStream.write(new char[] { 'a', 'b', 'c' }, -1, 1),
                        new PreConditionFailure("startIndex (-1) must be between 0 and 2."));
                    test.assertEqual("", innerStream.getText().await());
                });

                runner.test("with startIndex equal to toWrite.length", (Test test) ->
                {
                    final InMemoryCharacterStream innerStream = new InMemoryCharacterStream();
                    final IndentedCharacterWriteStream writeStream = new IndentedCharacterWriteStream(innerStream);
                    test.assertThrows(() -> writeStream.write(new char[] { 'a', 'b', 'c' }, 3, 1),
                        new PreConditionFailure("startIndex (3) must be between 0 and 2."));
                    test.assertEqual("", innerStream.getText().await());
                });

                runner.test("with negative length", (Test test) ->
                {
                    final InMemoryCharacterStream innerStream = new InMemoryCharacterStream();
                    final IndentedCharacterWriteStream writeStream = new IndentedCharacterWriteStream(innerStream);
                    test.assertThrows(() -> writeStream.write(new char[] { 'a', 'b', 'c' }, 1, -1),
                        new PreConditionFailure("length (-1) must be between 1 and 2."));
                    test.assertEqual("", innerStream.getText().await());
                });

                runner.test("with zero length", (Test test) ->
                {
                    final InMemoryCharacterStream innerStream = new InMemoryCharacterStream();
                    final IndentedCharacterWriteStream writeStream = new IndentedCharacterWriteStream(innerStream);
                    test.assertThrows(() -> writeStream.write(new char[] { 'a', 'b', 'c' }, 1, 0),
                        new PreConditionFailure("length (0) must be between 1 and 2."));
                    test.assertEqual("", innerStream.getText().await());
                });

                runner.test("with too-large length", (Test test) ->
                {
                    final InMemoryCharacterStream innerStream = new InMemoryCharacterStream();
                    final IndentedCharacterWriteStream writeStream = new IndentedCharacterWriteStream(innerStream);
                    test.assertThrows(() -> writeStream.write(new char[] { 'a', 'b', 'c' }, 1, 3),
                        new PreConditionFailure("length (3) must be between 1 and 2."));
                    test.assertEqual("", innerStream.getText().await());
                });

                runner.test("when disposed", (Test test) ->
                {
                    final InMemoryCharacterStream innerStream = new InMemoryCharacterStream();
                    innerStream.dispose().await();
                    final IndentedCharacterWriteStream writeStream = new IndentedCharacterWriteStream(innerStream);
                    test.assertThrows(() -> writeStream.write(new char[] { 'a', 'b', 'c' }, 1, 2),
                        new PreConditionFailure("isDisposed() cannot be true."));
                    test.assertEqual("", innerStream.getText().await());
                });

                runner.test("with no indent and empty stream", (Test test) ->
                {
                    final InMemoryCharacterStream innerStream = new InMemoryCharacterStream();
                    final IndentedCharacterWriteStream writeStream = new IndentedCharacterWriteStream(innerStream);
                    test.assertEqual(3, writeStream.write(new char[] { 'a', 'b', 'c' }, 0, 3).await());
                    test.assertEqual("abc", innerStream.getText().await());
                });

                runner.test("with no indent and non-empty mid-line stream", (Test test) ->
                {
                    final InMemoryCharacterStream innerStream = new InMemoryCharacterStream();
                    final IndentedCharacterWriteStream writeStream = new IndentedCharacterWriteStream(innerStream);
                    writeStream.write("hello").await();
                    test.assertEqual(2, writeStream.write(new char[] { 'a', 'b', 'c' }, 1, 2).await());
                    test.assertEqual("hellobc", innerStream.getText().await());
                });

                runner.test("with no indent and non-empty mid-line stream", (Test test) ->
                {
                    final InMemoryCharacterStream innerStream = new InMemoryCharacterStream();
                    final IndentedCharacterWriteStream writeStream = new IndentedCharacterWriteStream(innerStream);
                    writeStream.writeLine("hello").await();
                    test.assertEqual(1, writeStream.write(new char[] { 'a', 'b', 'c' }, 2, 1).await());
                    test.assertEqual("hello\nc", innerStream.getText().await());
                });

                runner.test("with indent and empty stream", (Test test) ->
                {
                    final InMemoryCharacterStream innerStream = new InMemoryCharacterStream();
                    final IndentedCharacterWriteStream writeStream = new IndentedCharacterWriteStream(innerStream);
                    writeStream.setCurrentIndent("\t");
                    test.assertEqual(4, writeStream.write(new char[] { 'a', 'b', 'c' }, 0, 3).await());
                    test.assertEqual("\tabc", innerStream.getText().await());
                });

                runner.test("with indent and non-empty mid-line stream", (Test test) ->
                {
                    final InMemoryCharacterStream innerStream = new InMemoryCharacterStream();
                    final IndentedCharacterWriteStream writeStream = new IndentedCharacterWriteStream(innerStream);
                    writeStream.setCurrentIndent(" ");
                    writeStream.write("hello").await();
                    test.assertEqual(2, writeStream.write(new char[] { 'a', 'b', 'c' }, 1, 2).await());
                    test.assertEqual(" hellobc", innerStream.getText().await());
                });

                runner.test("with indent and non-empty mid-line stream", (Test test) ->
                {
                    final InMemoryCharacterStream innerStream = new InMemoryCharacterStream();
                    final IndentedCharacterWriteStream writeStream = new IndentedCharacterWriteStream(innerStream);
                    writeStream.setCurrentIndent("  ");
                    writeStream.writeLine("hello").await();
                    test.assertEqual(3, writeStream.write(new char[] { 'a', 'b', 'c' }, 2, 1).await());
                    test.assertEqual("  hello\n  c", innerStream.getText().await());
                });
            });
        });
    }
}
