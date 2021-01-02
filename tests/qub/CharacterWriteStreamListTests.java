package qub;

public interface CharacterWriteStreamListTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(CharacterWriteStreamList.class, () ->
        {
            runner.testGroup("create(CharacterWriteStream...)", () ->
            {
                runner.test("with no arguments", (Test test) ->
                {
                    final CharacterWriteStreamList streamList = CharacterWriteStreamList.create();
                    test.assertNotNull(streamList);
                    test.assertEqual(0, streamList.getCount());

                    test.assertEqual(0, streamList.write('a').await());

                    test.assertEqual(0, streamList.write("bc").await());
                });

                runner.test("with one non-disposed write stream", (Test test) ->
                {
                    final InMemoryCharacterStream innerStream = InMemoryCharacterStream.create();
                    final CharacterWriteStreamList streamList = CharacterWriteStreamList.create(innerStream);
                    test.assertNotNull(streamList);
                    test.assertEqual(Iterable.create(innerStream), streamList);

                    test.assertEqual(1, streamList.write('a').await());
                    test.assertEqual("a", innerStream.getText().await());

                    test.assertEqual(2, streamList.write("bc").await());
                    test.assertEqual("abc", innerStream.getText().await());
                });

                runner.test("with multiple non-disposed write stream", (Test test) ->
                {
                    final InMemoryCharacterStream innerStream1 = InMemoryCharacterStream.create();
                    final InMemoryCharacterStream innerStream2 = InMemoryCharacterStream.create();
                    final CharacterWriteStreamList streamList = CharacterWriteStreamList.create(innerStream1, innerStream2);
                    test.assertNotNull(streamList);
                    test.assertEqual(Iterable.create(innerStream1, innerStream2), streamList);

                    test.assertEqual(2, streamList.write('a').await());
                    test.assertEqual("a", innerStream1.getText().await());
                    test.assertEqual("a", innerStream2.getText().await());

                    test.assertEqual(4, streamList.write("bc").await());
                    test.assertEqual("abc", innerStream1.getText().await());
                    test.assertEqual("abc", innerStream2.getText().await());
                });

                runner.test("with one non-disposed and one disposed write stream", (Test test) ->
                {
                    final InMemoryCharacterStream innerStream1 = InMemoryCharacterStream.create();
                    final InMemoryCharacterStream innerStream2 = InMemoryCharacterStream.create();
                    test.assertTrue(innerStream2.dispose().await());

                    final CharacterWriteStreamList streamList = CharacterWriteStreamList.create(innerStream1, innerStream2);
                    test.assertNotNull(streamList);
                    test.assertEqual(Iterable.create(innerStream1, innerStream2), streamList);

                    test.assertEqual(1, streamList.write('a').await());
                    test.assertEqual("a", innerStream1.getText().await());
                    test.assertEqual("", innerStream2.getText().await());

                    test.assertEqual(2, streamList.write("bc").await());
                    test.assertEqual("abc", innerStream1.getText().await());
                    test.assertEqual("", innerStream2.getText().await());
                });
            });

            runner.testGroup("create(Iterable<CharacterWriteStream>)", () ->
            {
                runner.test("with no arguments", (Test test) ->
                {
                    final CharacterWriteStreamList streamList = CharacterWriteStreamList.create(Iterable.create());
                    test.assertNotNull(streamList);
                    test.assertEqual(0, streamList.getCount());

                    test.assertEqual(0, streamList.write('a').await());

                    test.assertEqual(0, streamList.write("bc").await());
                });

                runner.test("with one non-disposed write stream", (Test test) ->
                {
                    final InMemoryCharacterStream innerStream = InMemoryCharacterStream.create();
                    final CharacterWriteStreamList streamList = CharacterWriteStreamList.create(Iterable.create(innerStream));
                    test.assertNotNull(streamList);
                    test.assertEqual(Iterable.create(innerStream), streamList);

                    test.assertEqual(1, streamList.write('a').await());
                    test.assertEqual("a", innerStream.getText().await());

                    test.assertEqual(2, streamList.write("bc").await());
                    test.assertEqual("abc", innerStream.getText().await());
                });

                runner.test("with multiple non-disposed write stream", (Test test) ->
                {
                    final InMemoryCharacterStream innerStream1 = InMemoryCharacterStream.create();
                    final InMemoryCharacterStream innerStream2 = InMemoryCharacterStream.create();
                    final CharacterWriteStreamList streamList = CharacterWriteStreamList.create(Iterable.create(innerStream1, innerStream2));
                    test.assertNotNull(streamList);
                    test.assertEqual(Iterable.create(innerStream1, innerStream2), streamList);

                    test.assertEqual(2, streamList.write('a').await());
                    test.assertEqual("a", innerStream1.getText().await());
                    test.assertEqual("a", innerStream2.getText().await());

                    test.assertEqual(4, streamList.write("bc").await());
                    test.assertEqual("abc", innerStream1.getText().await());
                    test.assertEqual("abc", innerStream2.getText().await());
                });

                runner.test("with one non-disposed and one disposed write stream", (Test test) ->
                {
                    final InMemoryCharacterStream innerStream1 = InMemoryCharacterStream.create();
                    final InMemoryCharacterStream innerStream2 = InMemoryCharacterStream.create();
                    test.assertTrue(innerStream2.dispose().await());

                    final CharacterWriteStreamList streamList = CharacterWriteStreamList.create(Iterable.create(innerStream1, innerStream2));
                    test.assertNotNull(streamList);
                    test.assertEqual(Iterable.create(innerStream1, innerStream2), streamList);

                    test.assertEqual(1, streamList.write('a').await());
                    test.assertEqual("a", innerStream1.getText().await());
                    test.assertEqual("", innerStream2.getText().await());

                    test.assertEqual(2, streamList.write("bc").await());
                    test.assertEqual("abc", innerStream1.getText().await());
                    test.assertEqual("", innerStream2.getText().await());
                });
            });

            runner.testGroup("setNewLine(char)", () ->
            {
                runner.test("with no inner streams and '\\n'", (Test test) ->
                {
                    final CharacterWriteStreamList streamList = CharacterWriteStreamList.create();
                    final CharacterWriteStreamList setNewLineResult = streamList.setNewLine('\n');
                    test.assertSame(streamList, setNewLineResult);
                    test.assertEqual("\n", streamList.getNewLine());
                });

                runner.test("with no inner streams and 'a'", (Test test) ->
                {
                    final CharacterWriteStreamList streamList = CharacterWriteStreamList.create();
                    final CharacterWriteStreamList setNewLineResult = streamList.setNewLine('a');
                    test.assertSame(streamList, setNewLineResult);
                    test.assertEqual("a", streamList.getNewLine());
                });

                runner.test("with inner streams and '\\n'", (Test test) ->
                {
                    final InMemoryCharacterStream innerStream = InMemoryCharacterStream.create();
                    final CharacterWriteStreamList streamList = CharacterWriteStreamList.create(innerStream);
                    final CharacterWriteStreamList setNewLineResult = streamList.setNewLine('\n');
                    test.assertSame(streamList, setNewLineResult);
                    test.assertEqual("\n", streamList.getNewLine());
                    test.assertEqual("\n", innerStream.getNewLine());
                });

                runner.test("with inner streams and 'a'", (Test test) ->
                {
                    final InMemoryCharacterStream innerStream = InMemoryCharacterStream.create();
                    final CharacterWriteStreamList streamList = CharacterWriteStreamList.create(innerStream);
                    final CharacterWriteStreamList setNewLineResult = streamList.setNewLine('a');
                    test.assertSame(streamList, setNewLineResult);
                    test.assertEqual("a", streamList.getNewLine());
                    test.assertEqual("a", innerStream.getNewLine());
                });

                runner.test("when disposed", (Test test) ->
                {
                    final CharacterWriteStreamList streamList = CharacterWriteStreamList.create();
                    test.assertTrue(streamList.dispose().await());

                    test.assertThrows(() -> streamList.setNewLine('a'),
                        new PreConditionFailure("this.isDisposed() cannot be true."));
                });

                runner.test("with disposed inner stream", (Test test) ->
                {
                    final InMemoryCharacterStream innerStream = InMemoryCharacterStream.create();
                    innerStream.dispose().await();
                    final CharacterWriteStreamList streamList = CharacterWriteStreamList.create(innerStream);
                    final CharacterWriteStreamList setNewLineResult = streamList.setNewLine('a');
                    test.assertSame(streamList, setNewLineResult);
                    test.assertEqual("a", streamList.getNewLine());
                    test.assertEqual("\n", innerStream.getNewLine());
                });
            });

            runner.testGroup("setNewLine(char[])", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final CharacterWriteStreamList streamList = CharacterWriteStreamList.create();
                    test.assertThrows(() -> streamList.setNewLine((char[])null),
                        new PreConditionFailure("newLine cannot be null."));
                });

                runner.test("with empty", (Test test) ->
                {
                    final CharacterWriteStreamList streamList = CharacterWriteStreamList.create();
                    test.assertThrows(() -> streamList.setNewLine(new char[0]),
                        new PreConditionFailure("newLine cannot be empty."));
                });

                runner.test("with no inner streams and ['\\r', '\\n']", (Test test) ->
                {
                    final CharacterWriteStreamList streamList = CharacterWriteStreamList.create();
                    final CharacterWriteStreamList setNewLineResult = streamList.setNewLine(new char[] { '\r', '\n' });
                    test.assertSame(streamList, setNewLineResult);
                    test.assertEqual("\r\n", streamList.getNewLine());
                });

                runner.test("with no inner streams and ['a', 'b']", (Test test) ->
                {
                    final CharacterWriteStreamList streamList = CharacterWriteStreamList.create();
                    final CharacterWriteStreamList setNewLineResult = streamList.setNewLine(new char[] { 'a', 'b' });
                    test.assertSame(streamList, setNewLineResult);
                    test.assertEqual("ab", streamList.getNewLine());
                });

                runner.test("with inner streams and ['\\r', '\\n'", (Test test) ->
                {
                    final InMemoryCharacterStream innerStream = InMemoryCharacterStream.create();
                    final CharacterWriteStreamList streamList = CharacterWriteStreamList.create(innerStream);
                    final CharacterWriteStreamList setNewLineResult = streamList.setNewLine(new char[] { '\r', '\n' });
                    test.assertSame(streamList, setNewLineResult);
                    test.assertEqual("\r\n", streamList.getNewLine());
                    test.assertEqual("\r\n", innerStream.getNewLine());
                });

                runner.test("with inner streams and ['a', 'b']", (Test test) ->
                {
                    final InMemoryCharacterStream innerStream = InMemoryCharacterStream.create();
                    final CharacterWriteStreamList streamList = CharacterWriteStreamList.create(innerStream);
                    final CharacterWriteStreamList setNewLineResult = streamList.setNewLine(new char[] { 'a', 'b' });
                    test.assertSame(streamList, setNewLineResult);
                    test.assertEqual("ab", streamList.getNewLine());
                    test.assertEqual("ab", innerStream.getNewLine());
                });

                runner.test("when disposed", (Test test) ->
                {
                    final CharacterWriteStreamList streamList = CharacterWriteStreamList.create();
                    test.assertTrue(streamList.dispose().await());

                    test.assertThrows(() -> streamList.setNewLine(new char[] { 'a', 'b' }),
                        new PreConditionFailure("this.isDisposed() cannot be true."));
                });

                runner.test("with disposed inner stream", (Test test) ->
                {
                    final InMemoryCharacterStream innerStream = InMemoryCharacterStream.create();
                    innerStream.dispose().await();
                    final CharacterWriteStreamList streamList = CharacterWriteStreamList.create(innerStream);
                    final CharacterWriteStreamList setNewLineResult = streamList.setNewLine(new char[] { 'a' });
                    test.assertSame(streamList, setNewLineResult);
                    test.assertEqual("a", streamList.getNewLine());
                    test.assertEqual("\n", innerStream.getNewLine());
                });
            });

            runner.testGroup("setNewLine(String)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final CharacterWriteStreamList streamList = CharacterWriteStreamList.create();
                    test.assertThrows(() -> streamList.setNewLine((String)null),
                        new PreConditionFailure("newLine cannot be null."));
                });

                runner.test("with empty", (Test test) ->
                {
                    final CharacterWriteStreamList streamList = CharacterWriteStreamList.create();
                    test.assertThrows(() -> streamList.setNewLine(""),
                        new PreConditionFailure("newLine cannot be empty."));
                });

                runner.test("with no inner streams and " + Strings.escapeAndQuote("\r\n"), (Test test) ->
                {
                    final CharacterWriteStreamList streamList = CharacterWriteStreamList.create();
                    final CharacterWriteStreamList setNewLineResult = streamList.setNewLine("\r\n");
                    test.assertSame(streamList, setNewLineResult);
                    test.assertEqual("\r\n", streamList.getNewLine());
                });

                runner.test("with no inner streams and " + Strings.escapeAndQuote("ab"), (Test test) ->
                {
                    final CharacterWriteStreamList streamList = CharacterWriteStreamList.create();
                    final CharacterWriteStreamList setNewLineResult = streamList.setNewLine("ab");
                    test.assertSame(streamList, setNewLineResult);
                    test.assertEqual("ab", streamList.getNewLine());
                });

                runner.test("with inner streams and " + Strings.escapeAndQuote("\r\n"), (Test test) ->
                {
                    final InMemoryCharacterStream innerStream = InMemoryCharacterStream.create();
                    final CharacterWriteStreamList streamList = CharacterWriteStreamList.create(innerStream);
                    final CharacterWriteStreamList setNewLineResult = streamList.setNewLine("\r\n");
                    test.assertSame(streamList, setNewLineResult);
                    test.assertEqual("\r\n", streamList.getNewLine());
                    test.assertEqual("\r\n", innerStream.getNewLine());
                });

                runner.test("with inner streams and " + Strings.escapeAndQuote("ab"), (Test test) ->
                {
                    final InMemoryCharacterStream innerStream = InMemoryCharacterStream.create();
                    final CharacterWriteStreamList streamList = CharacterWriteStreamList.create(innerStream);
                    final CharacterWriteStreamList setNewLineResult = streamList.setNewLine("ab");
                    test.assertSame(streamList, setNewLineResult);
                    test.assertEqual("ab", streamList.getNewLine());
                    test.assertEqual("ab", innerStream.getNewLine());
                });

                runner.test("when disposed", (Test test) ->
                {
                    final CharacterWriteStreamList streamList = CharacterWriteStreamList.create();
                    test.assertTrue(streamList.dispose().await());

                    test.assertThrows(() -> streamList.setNewLine("ab"),
                        new PreConditionFailure("this.isDisposed() cannot be true."));
                });

                runner.test("with disposed inner stream", (Test test) ->
                {
                    final InMemoryCharacterStream innerStream = InMemoryCharacterStream.create();
                    innerStream.dispose().await();
                    final CharacterWriteStreamList streamList = CharacterWriteStreamList.create(innerStream);
                    final CharacterWriteStreamList setNewLineResult = streamList.setNewLine("a");
                    test.assertSame(streamList, setNewLineResult);
                    test.assertEqual("a", streamList.getNewLine());
                    test.assertEqual("\n", innerStream.getNewLine());
                });
            });

            runner.testGroup("write(char)", () ->
            {
                runner.test("with no inner streams", (Test test) ->
                {
                    final CharacterWriteStreamList streamList = CharacterWriteStreamList.create();
                    test.assertEqual(0, streamList.write('a').await());
                });

                runner.test("with non-disposed innerStream", (Test test) ->
                {
                    final InMemoryCharacterStream innerStream = InMemoryCharacterStream.create();
                    final CharacterWriteStreamList streamList = CharacterWriteStreamList.create(innerStream);

                    test.assertEqual(1, streamList.write('a').await());
                    test.assertEqual("a", innerStream.getText().await());
                });

                runner.test("with one disposed innerStream", (Test test) ->
                {
                    final InMemoryCharacterStream innerStream = InMemoryCharacterStream.create();
                    innerStream.dispose().await();
                    final CharacterWriteStreamList streamList = CharacterWriteStreamList.create(innerStream);

                    test.assertEqual(0, streamList.write('a').await());
                    test.assertEqual("", innerStream.getText().await());
                });

                runner.test("with one disposed innerStream and one non-disposed innerStream", (Test test) ->
                {
                    final InMemoryCharacterStream innerStream1 = InMemoryCharacterStream.create();
                    innerStream1.dispose().await();
                    final InMemoryCharacterStream innerStream2 = InMemoryCharacterStream.create();
                    final CharacterWriteStreamList streamList = CharacterWriteStreamList.create(innerStream1, innerStream2);

                    test.assertEqual(1, streamList.write('a').await());
                    test.assertEqual("", innerStream1.getText().await());
                    test.assertEqual("a", innerStream2.getText().await());
                });

                runner.test("with two non-disposed innerStreams", (Test test) ->
                {
                    final InMemoryCharacterStream innerStream1 = InMemoryCharacterStream.create();
                    final InMemoryCharacterStream innerStream2 = InMemoryCharacterStream.create();
                    final CharacterWriteStreamList streamList = CharacterWriteStreamList.create(innerStream1, innerStream2);

                    test.assertEqual(2, streamList.write('a').await());
                    test.assertEqual("a", innerStream1.getText().await());
                    test.assertEqual("a", innerStream2.getText().await());
                });
            });

            runner.testGroup("write(String,Object...)", () ->
            {
                runner.test("with null toWrite", (Test test) ->
                {
                    final CharacterWriteStreamList streamList = CharacterWriteStreamList.create();
                    test.assertThrows(() -> streamList.write(null),
                        new PreConditionFailure("toWrite cannot be null."));
                });

                runner.test("with null formattedStringArguments", (Test test) ->
                {
                    final CharacterWriteStreamList streamList = CharacterWriteStreamList.create();
                    test.assertThrows(() -> streamList.write("hello", (Object[])null),
                        new PreConditionFailure("formattedStringArguments cannot be null."));
                });

                runner.test("with disposed stream list", (Test test) ->
                {
                    final CharacterWriteStreamList streamList = CharacterWriteStreamList.create();
                    test.assertTrue(streamList.dispose().await());
                    test.assertThrows(() -> streamList.write("hello"),
                        new PreConditionFailure("this.isDisposed() cannot be true."));
                });

                runner.test("with disposed inner stream", (Test test) ->
                {
                    final InMemoryCharacterStream innerStream = InMemoryCharacterStream.create();
                    innerStream.dispose().await();
                    final CharacterWriteStreamList streamList = CharacterWriteStreamList.create(innerStream);

                    test.assertEqual(0, streamList.write("hello").await());
                    test.assertEqual("", innerStream.getText().await());
                });

                runner.test("with empty and multiple inner streams", (Test test) ->
                {
                    final InMemoryCharacterStream innerStream1 = InMemoryCharacterStream.create();
                    final InMemoryCharacterStream innerStream2 = InMemoryCharacterStream.create();
                    final CharacterWriteStreamList streamList = CharacterWriteStreamList.create(innerStream1, innerStream2);

                    test.assertEqual(0, streamList.write("").await());

                    test.assertEqual("", innerStream1.getText().await());
                    test.assertEqual("", innerStream2.getText().await());
                });

                runner.test("with non-empty and multiple inner streams", (Test test) ->
                {
                    final InMemoryCharacterStream innerStream1 = InMemoryCharacterStream.create();
                    final InMemoryCharacterStream innerStream2 = InMemoryCharacterStream.create();
                    final CharacterWriteStreamList streamList = CharacterWriteStreamList.create(innerStream1, innerStream2);

                    test.assertEqual(10, streamList.write("hello").await());

                    test.assertEqual("hello", innerStream1.getText().await());
                    test.assertEqual("hello", innerStream2.getText().await());
                });
            });

            runner.testGroup("dispose()", () ->
            {
                runner.test("with no inner streams", (Test test) ->
                {
                    final CharacterWriteStreamList streamList = CharacterWriteStreamList.create();
                    test.assertTrue(streamList.dispose().await());
                    test.assertTrue(streamList.isDisposed());

                    test.assertFalse(streamList.dispose().await());
                    test.assertTrue(streamList.isDisposed());
                });

                runner.test("with non-disposed inner streams", (Test test) ->
                {
                    final InMemoryCharacterStream innerStream1 = InMemoryCharacterStream.create();
                    final InMemoryCharacterStream innerStream2 = InMemoryCharacterStream.create();
                    final CharacterWriteStreamList streamList = CharacterWriteStreamList.create(innerStream1, innerStream2);

                    test.assertTrue(streamList.dispose().await());
                    test.assertTrue(streamList.isDisposed());
                    test.assertTrue(innerStream1.isDisposed());
                    test.assertTrue(innerStream2.isDisposed());

                    test.assertFalse(streamList.dispose().await());
                    test.assertTrue(streamList.isDisposed());
                    test.assertTrue(innerStream1.isDisposed());
                    test.assertTrue(innerStream2.isDisposed());
                });

                runner.test("with disposed inner stream", (Test test) ->
                {
                    final InMemoryCharacterStream innerStream1 = InMemoryCharacterStream.create();
                    innerStream1.dispose().await();
                    final InMemoryCharacterStream innerStream2 = InMemoryCharacterStream.create();
                    final CharacterWriteStreamList streamList = CharacterWriteStreamList.create(innerStream1, innerStream2);

                    test.assertTrue(streamList.dispose().await());
                    test.assertTrue(streamList.isDisposed());
                    test.assertTrue(innerStream1.isDisposed());
                    test.assertTrue(innerStream2.isDisposed());

                    test.assertFalse(streamList.dispose().await());
                    test.assertTrue(streamList.isDisposed());
                    test.assertTrue(innerStream1.isDisposed());
                    test.assertTrue(innerStream2.isDisposed());
                });
            });

            runner.testGroup("set(int,CharacterWriteStream)", () ->
            {
                runner.test("with empty list and negative index", (Test test) ->
                {
                    final CharacterWriteStreamList streamList = CharacterWriteStreamList.create();
                    test.assertThrows(() -> streamList.set(-1, InMemoryCharacterStream.create()),
                        new PreConditionFailure("Indexable length (0) must be greater than or equal to 1."));
                    test.assertEqual(Iterable.create(), streamList);
                });

                runner.test("with empty list and index equal to length", (Test test) ->
                {
                    final CharacterWriteStreamList streamList = CharacterWriteStreamList.create();
                    test.assertThrows(() -> streamList.set(0, InMemoryCharacterStream.create()),
                        new PreConditionFailure("Indexable length (0) must be greater than or equal to 1."));
                    test.assertEqual(Iterable.create(), streamList);
                });

                runner.test("with empty list and index greater than length", (Test test) ->
                {
                    final CharacterWriteStreamList streamList = CharacterWriteStreamList.create();
                    test.assertThrows(() -> streamList.set(1, InMemoryCharacterStream.create()),
                        new PreConditionFailure("Indexable length (0) must be greater than or equal to 1."));
                    test.assertEqual(Iterable.create(), streamList);
                });

                runner.test("with non-empty list and negative index", (Test test) ->
                {
                    final InMemoryCharacterStream innerStream1 = InMemoryCharacterStream.create();
                    final CharacterWriteStreamList streamList = CharacterWriteStreamList.create(innerStream1);

                    final InMemoryCharacterStream innerStream2 = InMemoryCharacterStream.create();
                    test.assertThrows(() -> streamList.set(-1, innerStream2),
                        new PreConditionFailure("index (-1) must be equal to 0."));
                    test.assertEqual(Iterable.create(innerStream1), streamList);
                });

                runner.test("with non-empty list and index less than length", (Test test) ->
                {
                    final InMemoryCharacterStream innerStream1 = InMemoryCharacterStream.create();
                    final CharacterWriteStreamList streamList = CharacterWriteStreamList.create(innerStream1);

                    final InMemoryCharacterStream innerStream2 = InMemoryCharacterStream.create();
                    final CharacterWriteStreamList setResult = streamList.set(0, innerStream2);
                    test.assertSame(streamList, setResult);

                    test.assertEqual(Iterable.create(innerStream2), streamList);
                });

                runner.test("with non-empty list and index equal to length", (Test test) ->
                {
                    final InMemoryCharacterStream innerStream1 = InMemoryCharacterStream.create();
                    final CharacterWriteStreamList streamList = CharacterWriteStreamList.create(innerStream1);

                    final InMemoryCharacterStream innerStream2 = InMemoryCharacterStream.create();
                    test.assertThrows(() -> streamList.set(1, innerStream2),
                        new PreConditionFailure("index (1) must be equal to 0."));
                    test.assertEqual(Iterable.create(innerStream1), streamList);
                });

                runner.test("with non-empty list and index greater than length", (Test test) ->
                {
                    final InMemoryCharacterStream innerStream1 = InMemoryCharacterStream.create();
                    final CharacterWriteStreamList streamList = CharacterWriteStreamList.create(innerStream1);

                    final InMemoryCharacterStream innerStream2 = InMemoryCharacterStream.create();
                    test.assertThrows(() -> streamList.set(2, innerStream2),
                        new PreConditionFailure("index (2) must be equal to 0."));
                    test.assertEqual(Iterable.create(innerStream1), streamList);
                });
            });

            runner.testGroup("insert(int,CharacterWriteStream)", () ->
            {
                runner.test("with empty list and negative index", (Test test) ->
                {
                    final CharacterWriteStreamList streamList = CharacterWriteStreamList.create();
                    test.assertThrows(() -> streamList.insert(-1, InMemoryCharacterStream.create()),
                        new PreConditionFailure("insertIndex (-1) must be equal to 0."));
                    test.assertEqual(Iterable.create(), streamList);
                });

                runner.test("with empty list and index equal to length", (Test test) ->
                {
                    final CharacterWriteStreamList streamList = CharacterWriteStreamList.create();
                    final InMemoryCharacterStream innerStream = InMemoryCharacterStream.create();
                    final CharacterWriteStreamList insertResult = streamList.insert(0, innerStream);
                    test.assertSame(streamList, insertResult);
                    test.assertEqual(Iterable.create(innerStream), streamList);
                });

                runner.test("with empty list and index greater than length", (Test test) ->
                {
                    final CharacterWriteStreamList streamList = CharacterWriteStreamList.create();
                    test.assertThrows(() -> streamList.insert(1, InMemoryCharacterStream.create()),
                        new PreConditionFailure("insertIndex (1) must be equal to 0."));
                    test.assertEqual(Iterable.create(), streamList);
                });
            });

            runner.testGroup("add(CharacterWriteStream)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final CharacterWriteStreamList streamList = CharacterWriteStreamList.create();
                    final CharacterWriteStreamList addResult = streamList.add(null);
                    test.assertSame(streamList, addResult);
                    test.assertEqual(Iterable.create((CharacterWriteStream)null), streamList);
                });

                runner.test("with non-null", (Test test) ->
                {
                    final CharacterWriteStreamList streamList = CharacterWriteStreamList.create();
                    final InMemoryCharacterStream innerStream = InMemoryCharacterStream.create();
                    final CharacterWriteStreamList addResult = streamList.add(innerStream);
                    test.assertSame(streamList, addResult);
                    test.assertEqual(Iterable.create(innerStream), streamList);
                });
            });

            runner.testGroup("addAll(CharacterWriteStream...)", () ->
            {
                runner.test("with no arguments", (Test test) ->
                {
                    final CharacterWriteStreamList streamList = CharacterWriteStreamList.create();
                    final CharacterWriteStreamList addResult = streamList.addAll();
                    test.assertSame(streamList, addResult);
                    test.assertEqual(Iterable.create(), streamList);
                });

                runner.test("with one argument", (Test test) ->
                {
                    final CharacterWriteStreamList streamList = CharacterWriteStreamList.create();
                    final InMemoryCharacterStream innerStream = InMemoryCharacterStream.create();
                    final CharacterWriteStreamList addResult = streamList.addAll(innerStream);
                    test.assertSame(streamList, addResult);
                    test.assertEqual(Iterable.create(innerStream), streamList);
                });

                runner.test("with two arguments", (Test test) ->
                {
                    final CharacterWriteStreamList streamList = CharacterWriteStreamList.create();
                    final InMemoryCharacterStream innerStream1 = InMemoryCharacterStream.create();
                    final InMemoryCharacterStream innerStream2 = InMemoryCharacterStream.create();
                    final CharacterWriteStreamList addResult = streamList.addAll(innerStream1, innerStream2);
                    test.assertSame(streamList, addResult);
                    test.assertEqual(Iterable.create(innerStream1, innerStream2), streamList);
                });
            });

            runner.testGroup("addAll(Iterator<CharacterWriteStream>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final CharacterWriteStreamList streamList = CharacterWriteStreamList.create();
                    test.assertThrows(() -> streamList.addAll((Iterator<CharacterWriteStream>)null),
                        new PreConditionFailure("values cannot be null."));
                    test.assertEqual(Iterable.create(), streamList);
                });

                runner.test("with no values", (Test test) ->
                {
                    final CharacterWriteStreamList streamList = CharacterWriteStreamList.create();
                    final CharacterWriteStreamList addResult = streamList.addAll(Iterator.create());
                    test.assertSame(streamList, addResult);
                    test.assertEqual(Iterable.create(), streamList);
                });

                runner.test("with one value", (Test test) ->
                {
                    final CharacterWriteStreamList streamList = CharacterWriteStreamList.create();
                    final InMemoryCharacterStream innerStream = InMemoryCharacterStream.create();
                    final CharacterWriteStreamList addResult = streamList.addAll(Iterator.create(innerStream));
                    test.assertSame(streamList, addResult);
                    test.assertEqual(Iterable.create(innerStream), streamList);
                });

                runner.test("with two values", (Test test) ->
                {
                    final CharacterWriteStreamList streamList = CharacterWriteStreamList.create();
                    final InMemoryCharacterStream innerStream1 = InMemoryCharacterStream.create();
                    final InMemoryCharacterStream innerStream2 = InMemoryCharacterStream.create();
                    final CharacterWriteStreamList addResult = streamList.addAll(Iterator.create(innerStream1, innerStream2));
                    test.assertSame(streamList, addResult);
                    test.assertEqual(Iterable.create(innerStream1, innerStream2), streamList);
                });
            });

            runner.testGroup("addAll(Iterable<CharacterWriteStream>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final CharacterWriteStreamList streamList = CharacterWriteStreamList.create();
                    test.assertThrows(() -> streamList.addAll((Iterable<CharacterWriteStream>)null),
                        new PreConditionFailure("values cannot be null."));
                    test.assertEqual(Iterable.create(), streamList);
                });

                runner.test("with no values", (Test test) ->
                {
                    final CharacterWriteStreamList streamList = CharacterWriteStreamList.create();
                    final CharacterWriteStreamList addResult = streamList.addAll(Iterable.create());
                    test.assertSame(streamList, addResult);
                    test.assertEqual(Iterable.create(), streamList);
                });

                runner.test("with one value", (Test test) ->
                {
                    final CharacterWriteStreamList streamList = CharacterWriteStreamList.create();
                    final InMemoryCharacterStream innerStream = InMemoryCharacterStream.create();
                    final CharacterWriteStreamList addResult = streamList.addAll(Iterable.create(innerStream));
                    test.assertSame(streamList, addResult);
                    test.assertEqual(Iterable.create(innerStream), streamList);
                });

                runner.test("with two values", (Test test) ->
                {
                    final CharacterWriteStreamList streamList = CharacterWriteStreamList.create();
                    final InMemoryCharacterStream innerStream1 = InMemoryCharacterStream.create();
                    final InMemoryCharacterStream innerStream2 = InMemoryCharacterStream.create();
                    final CharacterWriteStreamList addResult = streamList.addAll(Iterable.create(innerStream1, innerStream2));
                    test.assertSame(streamList, addResult);
                    test.assertEqual(Iterable.create(innerStream1, innerStream2), streamList);
                });
            });

            runner.testGroup("clear()", () ->
            {
                runner.test("with no values", (Test test) ->
                {
                    final CharacterWriteStreamList streamList = CharacterWriteStreamList.create();
                    final CharacterWriteStreamList clearResult = streamList.clear();
                    test.assertSame(streamList, clearResult);
                    test.assertEqual(Iterable.create(), streamList);
                });

                runner.test("with values", (Test test) ->
                {
                    final CharacterWriteStreamList streamList = CharacterWriteStreamList.create(
                        InMemoryCharacterStream.create(),
                        InMemoryCharacterStream.create());
                    final CharacterWriteStreamList clearResult = streamList.clear();
                    test.assertSame(streamList, clearResult);
                    test.assertEqual(Iterable.create(), streamList);
                });
            });
        });
    }
}
