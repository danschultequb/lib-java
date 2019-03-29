package qub;

public class InMemoryCharacterStreamTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(InMemoryCharacterStream.class, () ->
        {
            AsyncDisposableTests.test(runner, (Test test) -> new InMemoryCharacterStream(test.getMainAsyncRunner()));

            runner.testGroup("readCharacter()", () ->
            {
                runner.test("with disposed CharacterReadStream", (Test test) ->
                {
                    final InMemoryCharacterStream characterReadStream = createStream();
                    characterReadStream.dispose();
                    test.assertThrows(characterReadStream::readCharacter, new PreConditionFailure("isDisposed() cannot be true."));
                });

                runner.test("with no characters to read", (Test test) ->
                {
                    final InMemoryCharacterStream characterReadStream = createStream();
                    test.assertSuccess(null, characterReadStream.readCharacter());
                });

                runner.test("with one character to read", (Test test) ->
                {
                    final InMemoryCharacterStream characterReadStream = createStream("d");
                    test.assertSuccess('d', characterReadStream.readCharacter());
                    test.assertSuccess(null, characterReadStream.readCharacter());
                });

                runner.test("with two characters to read", (Test test) ->
                {
                    final InMemoryCharacterStream characterReadStream = createStream("ef");
                    test.assertSuccess('e', characterReadStream.readCharacter());
                    test.assertSuccess('f', characterReadStream.readCharacter());
                    test.assertSuccess(null, characterReadStream.readCharacter());
                });
            });

            runner.testGroup("readCharacterAsync()", () ->
            {
                runner.test("with disposed CharacterReadStream", (Test test) ->
                {
                    final InMemoryCharacterStream characterReadStream = createStream(test.getMainAsyncRunner());
                    characterReadStream.dispose();
                    test.assertThrows(characterReadStream::readCharacterAsync, new PreConditionFailure("isDisposed() cannot be true."));
                });

                runner.test("with no AsyncRunner provided to CharacterReadStream's constructor", (Test test) ->
                {
                    final InMemoryCharacterStream characterReadStream = createStream();
                    test.assertThrows(characterReadStream::readCharacterAsync, new PreConditionFailure("getAsyncRunner() cannot be null."));
                });

                runner.test("with no characters to read", (Test test) ->
                {
                    final InMemoryCharacterStream characterReadStream = createStream(test.getMainAsyncRunner());
                    test.assertSuccessAsync(null, characterReadStream.readCharacterAsync());
                });

                runner.test("with one character to read", (Test test) ->
                {
                    final InMemoryCharacterStream characterReadStream = createStream("d", test.getMainAsyncRunner());
                    test.assertSuccessAsync('d', characterReadStream.readCharacterAsync());
                    test.assertSuccessAsync(null, characterReadStream.readCharacterAsync());
                });

                runner.test("with two characters to read", (Test test) ->
                {
                    final InMemoryCharacterStream characterReadStream = createStream("hi", test.getMainAsyncRunner());
                    test.assertSuccessAsync('h', characterReadStream.readCharacterAsync());
                    test.assertSuccessAsync('i', characterReadStream.readCharacterAsync());
                    test.assertSuccessAsync(null, characterReadStream.readCharacterAsync());
                });
            });

            runner.testGroup("readCharacters(int)", () ->
            {
                runner.test("with disposed CharacterReadStream", (Test test) ->
                {
                    final InMemoryCharacterStream characterReadStream = createStream();
                    characterReadStream.dispose();
                    test.assertThrows(() -> characterReadStream.readCharacters(5), new PreConditionFailure("isDisposed() cannot be true."));
                });

                runner.test("with negative charactersToRead", (Test test) ->
                {
                    final InMemoryCharacterStream characterReadStream = createStream();
                    test.assertThrows(() -> characterReadStream.readCharacters(-1), new PreConditionFailure("charactersToRead (-1) must be greater than 0."));
                });

                runner.test("with zero charactersToRead", (Test test) ->
                {
                    final InMemoryCharacterStream characterReadStream = createStream();
                    test.assertThrows(() -> characterReadStream.readCharacters(0), new PreConditionFailure("charactersToRead (0) must be greater than 0."));
                });

                runner.test("with no characters to read", (Test test) ->
                {
                    final InMemoryCharacterStream characterReadStream = createStream();
                    test.assertSuccess(null, characterReadStream.readCharacters(5));
                });

                runner.test("with fewer available characters than charactersToRead", (Test test) ->
                {
                    final InMemoryCharacterStream characterReadStream = createStream("abc");
                    test.assertEqual(new char[] { 'a', 'b', 'c' }, characterReadStream.readCharacters(5).await());
                    test.assertSuccess(null, characterReadStream.readCharacters(1));
                });

                runner.test("with available characters equal to charactersToRead", (Test test) ->
                {
                    final InMemoryCharacterStream characterReadStream = createStream("abcd");
                    test.assertSuccess(new char[] { 'a', 'b', 'c', 'd' }, characterReadStream.readCharacters(4));
                    test.assertSuccess(null, characterReadStream.readCharacters(1));
                });

                runner.test("with more available characters than charactersToRead", (Test test) ->
                {
                    final InMemoryCharacterStream characterReadStream = createStream("abcdefghi");
                    test.assertSuccess(new char[] { 'a', 'b', 'c' }, characterReadStream.readCharacters(3));
                    test.assertSuccess(new char[] { 'd' }, characterReadStream.readCharacters(1));
                    test.assertSuccess(new char[] { 'e', 'f', 'g', 'h', 'i' }, characterReadStream.readCharacters(1000));
                });
            });

            runner.testGroup("readCharactersAsync(int)", () ->
            {
                runner.test("with disposed CharacterReadStream", (Test test) ->
                {
                    final InMemoryCharacterStream characterReadStream = createStream(test.getMainAsyncRunner());
                    characterReadStream.dispose();
                    test.assertThrows(() -> characterReadStream.readCharactersAsync(5), new PreConditionFailure("isDisposed() cannot be true."));
                });

                runner.test("with no AsyncRunner provided to CharacterReadStream's constructor", (Test test) ->
                {
                    final InMemoryCharacterStream characterReadStream = createStream();
                    test.assertThrows(() -> characterReadStream.readCharactersAsync(20), new PreConditionFailure("getAsyncRunner() cannot be null."));
                });

                runner.test("with no characters to read", (Test test) ->
                {
                    final InMemoryCharacterStream characterReadStream = createStream(test.getMainAsyncRunner());
                    test.assertSuccessAsync(null, characterReadStream.readCharactersAsync(5));
                });

                runner.test("with fewer available characters than charactersToRead", (Test test) ->
                {
                    final InMemoryCharacterStream characterReadStream = createStream("abc", test.getMainAsyncRunner());
                    test.assertSuccessAsync(new char[] { 'a', 'b', 'c' }, characterReadStream.readCharactersAsync(5));
                    test.assertSuccessAsync(null, characterReadStream.readCharactersAsync(1));
                });

                runner.test("with available characters equal to charactersToRead", (Test test) ->
                {
                    final InMemoryCharacterStream characterReadStream = createStream("abcd", test.getMainAsyncRunner());
                    test.assertSuccessAsync(new char[] { 'a', 'b', 'c', 'd' }, characterReadStream.readCharactersAsync(4));
                    test.assertSuccessAsync(null, characterReadStream.readCharactersAsync(1));
                });

                runner.test("with more available characters than charactersToRead", (Test test) ->
                {
                    final InMemoryCharacterStream characterReadStream = createStream("abcdefghi", test.getMainAsyncRunner());
                    test.assertSuccessAsync(new char[] { 'a', 'b', 'c' }, characterReadStream.readCharactersAsync(3));
                    test.assertSuccessAsync(new char[] { 'd' }, characterReadStream.readCharactersAsync(1));
                    test.assertSuccessAsync(new char[] { 'e', 'f', 'g', 'h', 'i' }, characterReadStream.readCharactersAsync(1000));
                });
            });

            runner.testGroup("readCharacters(char[])", () ->
            {
                runner.test("with disposed CharacterReadStream", (Test test) ->
                {
                    final InMemoryCharacterStream characterReadStream = createStream();
                    characterReadStream.dispose();
                    final char[] outputCharacters = new char[5];
                    test.assertThrows(() -> characterReadStream.readCharacters(outputCharacters), new PreConditionFailure("isDisposed() cannot be true."));
                });

                runner.test("with null outputCharacters", (Test test) ->
                {
                    final InMemoryCharacterStream characterReadStream = createStream();
                    final char[] outputCharacters = null;
                    test.assertThrows(() -> characterReadStream.readCharacters(outputCharacters), new PreConditionFailure("outputCharacters cannot be null."));
                });

                runner.test("with empty outputCharacters", (Test test) ->
                {
                    final InMemoryCharacterStream characterReadStream = createStream();
                    final char[] outputCharacters = new char[0];
                    test.assertThrows(() -> characterReadStream.readCharacters(outputCharacters), new PreConditionFailure("outputCharacters cannot be empty."));
                });

                runner.test("with no characters to read.", (Test test) ->
                {
                    final InMemoryCharacterStream characterReadStream = createStream();
                    final char[] outputCharacters = new char[5];
                    test.assertSuccess(null, characterReadStream.readCharacters(outputCharacters));
                });

                runner.test("with fewer characters to read than outputCharacters.length", (Test test) ->
                {
                    final InMemoryCharacterStream characterReadStream = createStream("abc");
                    final char[] outputCharacters = new char[5];
                    test.assertSuccess(3, characterReadStream.readCharacters(outputCharacters));
                    test.assertEqual(new char[] { 'a', 'b', 'c', '\0', '\0' }, outputCharacters);
                    test.assertSuccess(null, characterReadStream.readCharacter());
                });

                runner.test("with equal characters to read to outputCharacters.length", (Test test) ->
                {
                    final InMemoryCharacterStream characterReadStream = createStream("defgh");
                    final char[] outputCharacters = new char[5];
                    test.assertSuccess(5, characterReadStream.readCharacters(outputCharacters));
                    test.assertEqual(new char[] { 'd', 'e', 'f', 'g', 'h' }, outputCharacters);
                    test.assertSuccess(null, characterReadStream.readCharacter());
                });

                runner.test("with more characters to read than outputCharacters.length", (Test test) ->
                {
                    final InMemoryCharacterStream characterReadStream = createStream("defghij");
                    final char[] outputCharacters = new char[5];
                    test.assertSuccess(5, characterReadStream.readCharacters(outputCharacters));
                    test.assertEqual(new char[] { 'd', 'e', 'f', 'g', 'h' }, outputCharacters);
                    test.assertSuccess('i', characterReadStream.readCharacter());
                });
            });

            runner.testGroup("readCharactersAsync(char[])", () ->
            {
                runner.test("with disposed CharacterReadStream", (Test test) ->
                {
                    final InMemoryCharacterStream characterReadStream = createStream(test.getMainAsyncRunner());
                    characterReadStream.dispose();
                    final char[] outputCharacters = new char[5];
                    test.assertThrows(() -> characterReadStream.readCharactersAsync(outputCharacters), new PreConditionFailure("isDisposed() cannot be true."));
                });

                runner.test("with no AsyncRunner provided to CharacterReadStream's constructor", (Test test) ->
                {
                    final InMemoryCharacterStream characterReadStream = createStream();
                    final char[] outputCharacters = new char[5];
                    test.assertThrows(() -> characterReadStream.readCharactersAsync(outputCharacters), new PreConditionFailure("getAsyncRunner() cannot be null."));
                });

                runner.test("with null outputCharacters", (Test test) ->
                {
                    final InMemoryCharacterStream characterReadStream = createStream(test.getMainAsyncRunner());
                    final char[] outputCharacters = null;
                    test.assertThrows(() -> characterReadStream.readCharactersAsync(outputCharacters), new PreConditionFailure("outputCharacters cannot be null."));
                });

                runner.test("with empty outputCharacters", (Test test) ->
                {
                    final InMemoryCharacterStream characterReadStream = createStream(test.getMainAsyncRunner());
                    final char[] outputCharacters = new char[0];
                    test.assertThrows(() -> characterReadStream.readCharactersAsync(outputCharacters), new PreConditionFailure("outputCharacters cannot be empty."));
                });

                runner.test("with no characters to read.", (Test test) ->
                {
                    final InMemoryCharacterStream characterReadStream = createStream(test.getMainAsyncRunner());
                    final char[] outputCharacters = new char[5];
                    test.assertSuccessAsync(null, characterReadStream.readCharactersAsync(outputCharacters));
                });

                runner.test("with fewer characters to read than outputCharacters.length", (Test test) ->
                {
                    final InMemoryCharacterStream characterReadStream = createStream("abc", test.getMainAsyncRunner());
                    final char[] outputCharacters = new char[5];
                    test.assertSuccessAsync(3, characterReadStream.readCharactersAsync(outputCharacters));
                    test.assertEqual(new char[] { 'a', 'b', 'c', '\0', '\0' }, outputCharacters);
                    test.assertSuccess(null, characterReadStream.readCharacter());
                });

                runner.test("with equal characters to read to outputCharacters.length", (Test test) ->
                {
                    final InMemoryCharacterStream characterReadStream = createStream("defgh", test.getMainAsyncRunner());
                    final char[] outputCharacters = new char[5];
                    test.assertSuccessAsync(5, characterReadStream.readCharactersAsync(outputCharacters));
                    test.assertEqual(new char[] { 'd', 'e', 'f', 'g', 'h' }, outputCharacters);
                    test.assertSuccess(null, characterReadStream.readCharacter());
                });

                runner.test("with more characters to read than outputCharacters.length", (Test test) ->
                {
                    final InMemoryCharacterStream characterReadStream = createStream("defghij", test.getMainAsyncRunner());
                    final char[] outputCharacters = new char[5];
                    test.assertSuccessAsync(5, characterReadStream.readCharactersAsync(outputCharacters));
                    test.assertEqual(new char[] { 'd', 'e', 'f', 'g', 'h' }, outputCharacters);
                    test.assertSuccess('i', characterReadStream.readCharacter());
                });
            });

            runner.testGroup("readCharacters(char[],int,int)", () ->
            {
                runner.test("with disposed CharacterReadStream", (Test test) ->
                {
                    final InMemoryCharacterStream characterReadStream = createStream();
                    characterReadStream.dispose();
                    final char[] outputCharacters = new char[5];
                    test.assertThrows(() -> characterReadStream.readCharacters(outputCharacters, 2, 3),
                        new PreConditionFailure("isDisposed() cannot be true."));
                });

                runner.test("with null outputCharacters", (Test test) ->
                {
                    final InMemoryCharacterStream characterReadStream = createStream();
                    final char[] outputCharacters = null;
                    test.assertThrows(() -> characterReadStream.readCharacters(outputCharacters, 2, 3),
                        new PreConditionFailure("outputCharacters cannot be null."));
                });

                runner.test("with empty outputCharacters", (Test test) ->
                {
                    final InMemoryCharacterStream characterReadStream = createStream();
                    final char[] outputCharacters = new char[0];
                    test.assertThrows(() -> characterReadStream.readCharacters(outputCharacters, 2, 3),
                        new PreConditionFailure("outputCharacters cannot be empty."));
                });

                runner.test("with negative startIndex", (Test test) ->
                {
                    final InMemoryCharacterStream characterReadStream = createStream();
                    final char[] outputCharacters = new char[6];
                    test.assertThrows(() -> characterReadStream.readCharacters(outputCharacters, -10, 3),
                        new PreConditionFailure("startIndex (-10) must be between 0 and 5."));
                });

                runner.test("with startIndex equal to outputCharacters.length", (Test test) ->
                {
                    final InMemoryCharacterStream characterReadStream = createStream();
                    final char[] outputCharacters = new char[6];
                    test.assertThrows(() -> characterReadStream.readCharacters(outputCharacters, outputCharacters.length, 3),
                        new PreConditionFailure("startIndex (6) must be between 0 and 5."));
                });

                runner.test("with negative length", (Test test) ->
                {
                    final InMemoryCharacterStream characterReadStream = createStream();
                    final char[] outputCharacters = new char[6];
                    test.assertThrows(() -> characterReadStream.readCharacters(outputCharacters, 2, -50),
                        new PreConditionFailure("length (-50) must be between 1 and 4."));
                });

                runner.test("with zero length", (Test test) ->
                {
                    final InMemoryCharacterStream characterReadStream = createStream();
                    final char[] outputCharacters = new char[6];
                    test.assertThrows(() -> characterReadStream.readCharacters(outputCharacters, 2, 0),
                        new PreConditionFailure("length (0) must be between 1 and 4."));
                });

                runner.test("with length greater than outputCharacters.length - startIndex", (Test test) ->
                {
                    final InMemoryCharacterStream characterReadStream = createStream();
                    final char[] outputCharacters = new char[6];
                    test.assertThrows(() -> characterReadStream.readCharacters(outputCharacters, 2, 5),
                        new PreConditionFailure("length (5) must be between 1 and 4."));
                });

                runner.test("with no characters to read.", (Test test) ->
                {
                    final InMemoryCharacterStream characterReadStream = createStream();
                    final char[] outputCharacters = new char[6];
                    test.assertEqual(null, characterReadStream.readCharacters(outputCharacters, 2, 3).awaitError());
                });

                runner.test("with fewer characters to read than length", (Test test) ->
                {
                    final InMemoryCharacterStream characterReadStream = createStream("a");
                    final char[] outputCharacters = new char[6];
                    test.assertEqual(1, characterReadStream.readCharacters(outputCharacters, 2, 3).awaitError());
                    test.assertEqual(new char[] { '\0', '\0', 'a', '\0', '\0', '\0' }, outputCharacters);
                    test.assertEqual(null, characterReadStream.readCharacter().awaitError());
                });

                runner.test("with equal characters to read to length", (Test test) ->
                {
                    final InMemoryCharacterStream characterReadStream = createStream("def");
                    final char[] outputCharacters = new char[6];
                    test.assertSuccess(3, characterReadStream.readCharacters(outputCharacters, 2, 3));
                    test.assertEqual(new char[] { '\0', '\0', 'd', 'e', 'f', '\0' }, outputCharacters);
                    test.assertSuccess(null, characterReadStream.readCharacter());
                });

                runner.test("with more characters to read than length", (Test test) ->
                {
                    final InMemoryCharacterStream characterReadStream = createStream("defghij");
                    final char[] outputCharacters = new char[6];
                    test.assertSuccess(4, characterReadStream.readCharacters(outputCharacters, 2, 4));
                    test.assertEqual(new char[] { '\0', '\0', 'd', 'e', 'f', 'g' }, outputCharacters);
                    test.assertSuccess('h', characterReadStream.readCharacter());
                });
            });

            runner.testGroup("readCharactersAsync(char[],int,int)", () ->
            {
                runner.test("with disposed CharacterReadStream", (Test test) ->
                {
                    final InMemoryCharacterStream characterReadStream = createStream(test.getMainAsyncRunner());
                    characterReadStream.dispose();
                    final char[] outputCharacters = new char[5];
                    test.assertThrows(() -> characterReadStream.readCharactersAsync(outputCharacters, 2, 3), new PreConditionFailure("isDisposed() cannot be true."));
                });

                runner.test("with no AsyncRunner provided to CharacterReadStream's constructor", (Test test) ->
                {
                    final InMemoryCharacterStream characterReadStream = createStream();
                    final char[] outputCharacters = new char[5];
                    test.assertThrows(() -> characterReadStream.readCharactersAsync(outputCharacters, 2, 3), new PreConditionFailure("getAsyncRunner() cannot be null."));
                });

                runner.test("with null outputCharacters", (Test test) ->
                {
                    final InMemoryCharacterStream characterReadStream = createStream(test.getMainAsyncRunner());
                    final char[] outputCharacters = null;
                    test.assertThrows(() -> characterReadStream.readCharactersAsync(outputCharacters, 2, 3), new PreConditionFailure("characters cannot be null."));
                });

                runner.test("with empty outputCharacters", (Test test) ->
                {
                    final InMemoryCharacterStream characterReadStream = createStream(test.getMainAsyncRunner());
                    final char[] outputCharacters = new char[0];
                    test.assertThrows(() -> characterReadStream.readCharactersAsync(outputCharacters, 2, 3), new PreConditionFailure("characters cannot be empty."));
                });

                runner.test("with negative startIndex", (Test test) ->
                {
                    final InMemoryCharacterStream characterReadStream = createStream(test.getMainAsyncRunner());
                    final char[] outputCharacters = new char[6];
                    test.assertThrows(() -> characterReadStream.readCharactersAsync(outputCharacters, -10, 3), new PreConditionFailure("startIndex (-10) must be between 0 and 5."));
                });

                runner.test("with startIndex equal to outputCharacters.length", (Test test) ->
                {
                    final InMemoryCharacterStream characterReadStream = createStream(test.getMainAsyncRunner());
                    final char[] outputCharacters = new char[6];
                    test.assertThrows(() -> characterReadStream.readCharactersAsync(outputCharacters, outputCharacters.length, 3), new PreConditionFailure("startIndex (6) must be between 0 and 5."));
                });

                runner.test("with negative length", (Test test) ->
                {
                    final InMemoryCharacterStream characterReadStream = createStream(test.getMainAsyncRunner());
                    final char[] outputCharacters = new char[6];
                    test.assertThrows(() -> characterReadStream.readCharactersAsync(outputCharacters, 2, -50), new PreConditionFailure("length (-50) must be between 1 and 4."));
                });

                runner.test("with zero length", (Test test) ->
                {
                    final InMemoryCharacterStream characterReadStream = createStream(test.getMainAsyncRunner());
                    final char[] outputCharacters = new char[6];
                    test.assertThrows(() -> characterReadStream.readCharactersAsync(outputCharacters, 2, 0), new PreConditionFailure("length (0) must be between 1 and 4."));
                });

                runner.test("with length greater than outputCharacters.length - startIndex", (Test test) ->
                {
                    final InMemoryCharacterStream characterReadStream = createStream(test.getMainAsyncRunner());
                    final char[] outputCharacters = new char[6];
                    test.assertThrows(() -> characterReadStream.readCharactersAsync(outputCharacters, 2, 5), new PreConditionFailure("length (5) must be between 1 and 4."));
                });

                runner.test("with no characters to read.", (Test test) ->
                {
                    final InMemoryCharacterStream characterReadStream = createStream(test.getMainAsyncRunner());
                    final char[] outputCharacters = new char[6];
                    test.assertSuccessAsync(null, characterReadStream.readCharactersAsync(outputCharacters, 2, 3));
                });

                runner.test("with fewer characters to read than length", (Test test) ->
                {
                    final InMemoryCharacterStream characterReadStream = createStream("a", test.getMainAsyncRunner());
                    final char[] outputCharacters = new char[6];
                    test.assertSuccessAsync(1, characterReadStream.readCharactersAsync(outputCharacters, 2, 3));
                    test.assertEqual(new char[] { '\0', '\0', 'a', '\0', '\0', '\0' }, outputCharacters);
                    test.assertSuccess(null, characterReadStream.readCharacter());
                });

                runner.test("with equal characters to read to length", (Test test) ->
                {
                    final InMemoryCharacterStream characterReadStream = createStream("def", test.getMainAsyncRunner());
                    final char[] outputCharacters = new char[6];
                    test.assertSuccessAsync(3, characterReadStream.readCharactersAsync(outputCharacters, 2, 3));
                    test.assertEqual(new char[] { '\0', '\0', 'd', 'e', 'f', '\0' }, outputCharacters);
                    test.assertSuccess(null, characterReadStream.readCharacter());
                });

                runner.test("with more characters to read than length", (Test test) ->
                {
                    final InMemoryCharacterStream characterReadStream = createStream("defghij", test.getMainAsyncRunner());
                    final char[] outputCharacters = new char[6];
                    test.assertSuccessAsync(4, characterReadStream.readCharactersAsync(outputCharacters, 2, 4));
                    test.assertEqual(new char[] { '\0', '\0', 'd', 'e', 'f', 'g' }, outputCharacters);
                    test.assertSuccess('h', characterReadStream.readCharacter());
                });
            });

            runner.testGroup("readAllCharacters()", () ->
            {
                runner.test("with disposed CharacterReadStream", (Test test) ->
                {
                    final InMemoryCharacterStream characterReadStream = createStream();
                    characterReadStream.dispose();
                    test.assertThrows(characterReadStream::readAllCharacters, new PreConditionFailure("isDisposed() cannot be true."));
                });

                runner.test("with no characters to read", (Test test) ->
                {
                    final InMemoryCharacterStream characterReadStream = createStream("");
                    test.assertEqual(new char[0], characterReadStream.readAllCharacters().await());
                });

                runner.test("with one character to read", (Test test) ->
                {
                    final InMemoryCharacterStream characterReadStream = createStream("f");
                    test.assertEqual(new char[] { 'f' }, characterReadStream.readAllCharacters().await());
                });

                runner.test("with multiple characters to read", (Test test) ->
                {
                    final InMemoryCharacterStream characterReadStream = createStream("fedcb");
                    test.assertEqual(new char[] { 'f', 'e', 'd', 'c', 'b' }, characterReadStream.readAllCharacters().await());
                });
            });

            runner.testGroup("readAllCharactersAsync()", () ->
            {
                runner.test("with disposed CharacterReadStream", (Test test) ->
                {
                    final InMemoryCharacterStream characterReadStream = createStream(test.getMainAsyncRunner());
                    characterReadStream.dispose().await();
                    test.assertThrows(characterReadStream::readAllCharactersAsync, new PreConditionFailure("isDisposed() cannot be true."));
                });

                runner.test("with null AsyncRunner", (Test test) ->
                {
                    final InMemoryCharacterStream characterReadStream = createStream();
                    test.assertThrows(characterReadStream::readAllCharactersAsync, new PreConditionFailure("getAsyncRunner() cannot be null."));
                });

                runner.test("with no characters to read", (Test test) ->
                {
                    final InMemoryCharacterStream characterReadStream = createStream("", test.getMainAsyncRunner());
                    test.assertSuccessAsync(new char[0], characterReadStream.readAllCharactersAsync());
                });

                runner.test("with one character to read", (Test test) ->
                {
                    final InMemoryCharacterStream characterReadStream = createStream("g", test.getMainAsyncRunner());
                    test.assertSuccessAsync(new char[] { 'g' }, characterReadStream.readAllCharactersAsync());
                });

                runner.test("with multiple characters to read", (Test test) ->
                {
                    final InMemoryCharacterStream characterReadStream = createStream("ghij", test.getMainAsyncRunner());
                    test.assertSuccessAsync(new char[] { 'g', 'h', 'i', 'j' }, characterReadStream.readAllCharactersAsync());
                });
            });

            runner.testGroup("readString(int)", () ->
            {
                runner.test("with disposed CharacterReadStream", (Test test) ->
                {
                    final InMemoryCharacterStream characterReadStream = createStream();
                    characterReadStream.dispose();
                    test.assertThrows(() -> characterReadStream.readString(5), new PreConditionFailure("isDisposed() cannot be true."));
                });

                runner.test("with no characters to read", (Test test) ->
                {
                    final InMemoryCharacterStream characterReadStream = createStream();
                    test.assertSuccess(null, characterReadStream.readString(5));
                });

                runner.test("with fewer available characters than charactersToRead", (Test test) ->
                {
                    final InMemoryCharacterStream characterReadStream = createStream("abc");
                    test.assertSuccess("abc", characterReadStream.readString(5));
                    test.assertSuccess(null, characterReadStream.readString(1));
                });

                runner.test("with available characters equal to charactersToRead", (Test test) ->
                {
                    final InMemoryCharacterStream characterReadStream = createStream("abcd");
                    test.assertSuccess("abcd", characterReadStream.readString(4));
                    test.assertSuccess(null, characterReadStream.readString(1));
                });

                runner.test("with more available characters than charactersToRead", (Test test) ->
                {
                    final InMemoryCharacterStream characterReadStream = createStream("abcdefghi");
                    test.assertSuccess("abc", characterReadStream.readString(3));
                    test.assertSuccess("d", characterReadStream.readString(1));
                    test.assertSuccess("efghi", characterReadStream.readString(1000));
                });
            });

            runner.testGroup("readStringAsync(int)", () ->
            {
                runner.test("with disposed CharacterReadStream", (Test test) ->
                {
                    final InMemoryCharacterStream characterReadStream = createStream(test.getMainAsyncRunner());
                    characterReadStream.dispose();
                    test.assertThrows(() -> characterReadStream.readStringAsync(5), new PreConditionFailure("isDisposed() cannot be true."));
                });

                runner.test("with no AsyncRunner provided to CharacterReadStream's constructor", (Test test) ->
                {
                    final InMemoryCharacterStream characterReadStream = createStream();
                    test.assertThrows(() -> characterReadStream.readStringAsync(3), new PreConditionFailure("getAsyncRunner() cannot be null."));
                });

                runner.test("with no characters to read", (Test test) ->
                {
                    final InMemoryCharacterStream characterReadStream = createStream(test.getMainAsyncRunner());
                    test.assertSuccessAsync(null, characterReadStream.readStringAsync(5));
                });

                runner.test("with fewer available characters than charactersToRead", (Test test) ->
                {
                    final InMemoryCharacterStream characterReadStream = createStream("abc", test.getMainAsyncRunner());
                    test.assertSuccessAsync("abc", characterReadStream.readStringAsync(5));
                    test.assertSuccessAsync(null, characterReadStream.readStringAsync(1));
                });

                runner.test("with available characters equal to charactersToRead", (Test test) ->
                {
                    final InMemoryCharacterStream characterReadStream = createStream("abcd", test.getMainAsyncRunner());
                    test.assertSuccessAsync("abcd", characterReadStream.readStringAsync(4));
                    test.assertSuccessAsync(null, characterReadStream.readStringAsync(1));
                });

                runner.test("with more available characters than charactersToRead", (Test test) ->
                {
                    final InMemoryCharacterStream characterReadStream = createStream("abcdefghi", test.getMainAsyncRunner());
                    test.assertSuccessAsync("abc", characterReadStream.readStringAsync(3));
                    test.assertSuccessAsync("d", characterReadStream.readStringAsync(1));
                    test.assertSuccessAsync("efghi", characterReadStream.readStringAsync(1000));
                });
            });

            runner.testGroup("readLine()", () ->
            {
                runner.test("with disposed", (Test test) ->
                {
                    final InMemoryCharacterStream characterReadStream = createStream();
                    test.assertTrue(characterReadStream.dispose().await());
                    test.assertThrows(characterReadStream::readLine, new PreConditionFailure("isDisposed() cannot be true."));
                });

                final Action2<String,Iterable<String>> readLineTest = (String text, Iterable<String> expectedLines) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(text), (Test test) ->
                    {
                        final InMemoryCharacterStream stream = createStream(text);
                        int lineNumber = 0;
                        for (final String expectedLine : expectedLines)
                        {
                            ++lineNumber;
                            test.assertEqual(expectedLine, stream.readLine().await(), "Wrong line " + lineNumber);
                        }
                        test.assertThrows(() -> stream.readLine().awaitError(), new EndOfStreamException());
                    });
                };

                readLineTest.run("", Iterable.create());
                readLineTest.run("hello", Iterable.create("hello"));
                readLineTest.run("hello\n", Iterable.create("hello"));
                readLineTest.run("a\r", Iterable.create("a\r"));
                readLineTest.run("a\r\n", Iterable.create("a"));
                readLineTest.run("a\r\r\n", Iterable.create("a\r"));
                readLineTest.run("\n\n\n", Iterable.create("", "", ""));
                readLineTest.run("a\nb", Iterable.create("a", "b"));
                readLineTest.run("a\rb", Iterable.create("a\rb"));
            });

            runner.testGroup("readLine(boolean)", () ->
            {
                runner.testGroup("with false includeNewLine", () ->
                {
                    runner.test("with disposed", (Test test) ->
                    {
                        final InMemoryCharacterStream characterReadStream = createStream();
                        test.assertTrue(characterReadStream.dispose().await());
                        test.assertThrows(characterReadStream::readLine, new PreConditionFailure("isDisposed() cannot be true."));
                    });

                    final Action2<String,Iterable<String>> readLineTest = (String text, Iterable<String> expectedLines) ->
                    {
                        runner.test("with " + Strings.escapeAndQuote(text), (Test test) ->
                        {
                            final InMemoryCharacterStream stream = createStream(text);
                            int lineNumber = 0;
                            for (final String expectedLine : expectedLines)
                            {
                                ++lineNumber;
                                test.assertEqual(expectedLine, stream.readLine(false).await(), "Wrong line " + lineNumber);
                            }
                            test.assertThrows(() -> stream.readLine(false).awaitError(), new EndOfStreamException());
                        });
                    };

                    readLineTest.run("", Iterable.create());
                    readLineTest.run("hello", Iterable.create("hello"));
                    readLineTest.run("hello\n", Iterable.create("hello"));
                    readLineTest.run("a\r", Iterable.create("a\r"));
                    readLineTest.run("a\r\n", Iterable.create("a"));
                    readLineTest.run("a\r\r\n", Iterable.create("a\r"));
                    readLineTest.run("\n\n\n", Iterable.create("", "", ""));
                    readLineTest.run("a\nb", Iterable.create("a", "b"));
                    readLineTest.run("a\rb", Iterable.create("a\rb"));
                });

                runner.testGroup("with true includeNewLine", () ->
                {
                    runner.test("with disposed", (Test test) ->
                    {
                        final InMemoryCharacterStream characterReadStream = createStream();
                        test.assertTrue(characterReadStream.dispose().await());
                        test.assertThrows(characterReadStream::readLine, new PreConditionFailure("isDisposed() cannot be true."));
                    });

                    final Action2<String,Iterable<String>> readLineTest = (String text, Iterable<String> expectedLines) ->
                    {
                        runner.test("with " + Strings.escapeAndQuote(text), (Test test) ->
                        {
                            final InMemoryCharacterStream stream = createStream(text);
                            int lineNumber = 0;
                            for (final String expectedLine : expectedLines)
                            {
                                ++lineNumber;
                                test.assertEqual(expectedLine, stream.readLine(true).await(), "Wrong line " + lineNumber);
                            }
                            test.assertThrows(() -> stream.readLine(true).awaitError(), new EndOfStreamException());
                        });
                    };

                    readLineTest.run("", Iterable.create());
                    readLineTest.run("hello", Iterable.create("hello"));
                    readLineTest.run("hello\n", Iterable.create("hello\n"));
                    readLineTest.run("a\r", Iterable.create("a\r"));
                    readLineTest.run("a\r\n", Iterable.create("a\r\n"));
                    readLineTest.run("a\r\r\n", Iterable.create("a\r\r\n"));
                    readLineTest.run("\n\n\n", Iterable.create("\n", "\n", "\n"));
                    readLineTest.run("a\nb", Iterable.create("a\n", "b"));
                    readLineTest.run("a\rb", Iterable.create("a\rb"));
                });
            });
        });
    }

    private static InMemoryCharacterStream createStream()
    {
        return createStream(null, null, true);
    }

    private static InMemoryCharacterStream createStream(AsyncRunner asyncRunner)
    {
        return createStream(asyncRunner, null, true);
    }

    private static InMemoryCharacterStream createStream(String text)
    {
        return createStream(null, text, true);
    }

    private static InMemoryCharacterStream createStream(String text, AsyncRunner asyncRunner)
    {
        return createStream(asyncRunner, text, true);
    }

    private static InMemoryCharacterStream createStream(AsyncRunner asyncRunner, String text, boolean endOfStream)
    {
        InMemoryCharacterStream result = new InMemoryCharacterStream(asyncRunner);
        if (!Strings.isNullOrEmpty(text))
        {
            result.write(text);
        }
        if (endOfStream)
        {
            result = result.endOfStream();
        }
        return result;
    }
}
