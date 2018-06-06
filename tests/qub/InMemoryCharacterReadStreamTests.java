package qub;

public class InMemoryCharacterReadStreamTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(InMemoryCharacterReadStream.class, () ->
        {
            AsyncDisposableTests.test(runner, InMemoryCharacterReadStream::new);

            runner.testGroup("readCharacter()", () ->
            {
                runner.test("with disposed CharacterReadStream", (Test test) ->
                {
                    final InMemoryCharacterReadStream characterReadStream = new InMemoryCharacterReadStream();
                    characterReadStream.dispose();
                    test.assertError(new IllegalArgumentException("characterReadStream.isDisposed() (true) must be false."), characterReadStream.readCharacter());
                });

                runner.test("with no characters to read", (Test test) ->
                {
                    final InMemoryCharacterReadStream characterReadStream = new InMemoryCharacterReadStream();
                    test.assertSuccess(null, characterReadStream.readCharacter());
                });

                runner.test("with one character to read", (Test test) ->
                {
                    final InMemoryCharacterReadStream characterReadStream = new InMemoryCharacterReadStream("d");
                    test.assertSuccess('d', characterReadStream.readCharacter());
                    test.assertSuccess(null, characterReadStream.readCharacter());
                });

                runner.test("with two characters to read", (Test test) ->
                {
                    final InMemoryCharacterReadStream characterReadStream = new InMemoryCharacterReadStream("ef");
                    test.assertSuccess('e', characterReadStream.readCharacter());
                    test.assertSuccess('f', characterReadStream.readCharacter());
                    test.assertSuccess(null, characterReadStream.readCharacter());
                });
            });

            runner.testGroup("readCharacterAsync()", () ->
            {
                runner.test("with disposed CharacterReadStream", (Test test) ->
                {
                    final InMemoryCharacterReadStream characterReadStream = new InMemoryCharacterReadStream(test.getMainAsyncRunner());
                    characterReadStream.dispose();
                    test.assertErrorAsync(new IllegalArgumentException("characterReadStream.isDisposed() (true) must be false."), characterReadStream.readCharacterAsync());
                });

                runner.test("with no AsyncRunner provided to CharacterReadStream's constructor", (Test test) ->
                {
                    final InMemoryCharacterReadStream characterReadStream = new InMemoryCharacterReadStream();
                    test.assertErrorAsync(new IllegalArgumentException("Cannot invoke CharacterReadStream asynchronous functions when the CharacterReadStream hasn't been assigned an AsyncRunner."), characterReadStream.readCharacterAsync());
                });

                runner.test("with no characters to read", (Test test) ->
                {
                    final InMemoryCharacterReadStream characterReadStream = new InMemoryCharacterReadStream(test.getMainAsyncRunner());
                    test.assertSuccessAsync(null, characterReadStream.readCharacterAsync());
                });

                runner.test("with one character to read", (Test test) ->
                {
                    final InMemoryCharacterReadStream characterReadStream = new InMemoryCharacterReadStream("d", test.getMainAsyncRunner());
                    test.assertSuccessAsync('d', characterReadStream.readCharacterAsync());
                    test.assertSuccessAsync(null, characterReadStream.readCharacterAsync());
                });

                runner.test("with two characters to read", (Test test) ->
                {
                    final InMemoryCharacterReadStream characterReadStream = new InMemoryCharacterReadStream("hi", test.getMainAsyncRunner());
                    test.assertSuccessAsync('h', characterReadStream.readCharacterAsync());
                    test.assertSuccessAsync('i', characterReadStream.readCharacterAsync());
                    test.assertSuccessAsync(null, characterReadStream.readCharacterAsync());
                });
            });

            runner.testGroup("readCharacters(int)", () ->
            {
                runner.test("with disposed CharacterReadStream", (Test test) ->
                {
                    final InMemoryCharacterReadStream characterReadStream = new InMemoryCharacterReadStream();
                    characterReadStream.dispose();
                    test.assertError(new IllegalArgumentException("characterReadStream.isDisposed() (true) must be false."), characterReadStream.readCharacters(5));
                });

                runner.test("with no characters to read", (Test test) ->
                {
                    final InMemoryCharacterReadStream characterReadStream = new InMemoryCharacterReadStream();
                    test.assertSuccess(null, characterReadStream.readCharacters(5));
                });

                runner.test("with fewer available characters than charactersToRead", (Test test) ->
                {
                    final InMemoryCharacterReadStream characterReadStream = new InMemoryCharacterReadStream("abc");
                    test.assertSuccess(new char[] { 'a', 'b', 'c' }, characterReadStream.readCharacters(5));
                    test.assertSuccess(null, characterReadStream.readCharacters(1));
                });

                runner.test("with available characters equal to charactersToRead", (Test test) ->
                {
                    final InMemoryCharacterReadStream characterReadStream = new InMemoryCharacterReadStream("abcd");
                    test.assertSuccess(new char[] { 'a', 'b', 'c', 'd' }, characterReadStream.readCharacters(4));
                    test.assertSuccess(null, characterReadStream.readCharacters(1));
                });

                runner.test("with more available characters than charactersToRead", (Test test) ->
                {
                    final InMemoryCharacterReadStream characterReadStream = new InMemoryCharacterReadStream("abcdefghi");
                    test.assertSuccess(new char[] { 'a', 'b', 'c' }, characterReadStream.readCharacters(3));
                    test.assertSuccess(new char[] { 'd' }, characterReadStream.readCharacters(1));
                    test.assertSuccess(new char[] { 'e', 'f', 'g', 'h', 'i' }, characterReadStream.readCharacters(1000));
                });
            });

            runner.testGroup("readCharactersAsync(int)", () ->
            {
                runner.test("with disposed CharacterReadStream", (Test test) ->
                {
                    final InMemoryCharacterReadStream characterReadStream = new InMemoryCharacterReadStream(test.getMainAsyncRunner());
                    characterReadStream.dispose();
                    test.assertErrorAsync(new IllegalArgumentException("characterReadStream.isDisposed() (true) must be false."), characterReadStream.readCharactersAsync(5));
                });

                runner.test("with no AsyncRunner provided to CharacterReadStream's constructor", (Test test) ->
                {
                    final InMemoryCharacterReadStream characterReadStream = new InMemoryCharacterReadStream();
                    test.assertErrorAsync(new IllegalArgumentException("Cannot invoke CharacterReadStream asynchronous functions when the CharacterReadStream hasn't been assigned an AsyncRunner."), characterReadStream.readCharactersAsync(20));
                });

                runner.test("with no characters to read", (Test test) ->
                {
                    final InMemoryCharacterReadStream characterReadStream = new InMemoryCharacterReadStream(test.getMainAsyncRunner());
                    test.assertSuccessAsync(null, characterReadStream.readCharactersAsync(5));
                });

                runner.test("with fewer available characters than charactersToRead", (Test test) ->
                {
                    final InMemoryCharacterReadStream characterReadStream = new InMemoryCharacterReadStream("abc", test.getMainAsyncRunner());
                    test.assertSuccessAsync(new char[] { 'a', 'b', 'c' }, characterReadStream.readCharactersAsync(5));
                    test.assertSuccessAsync(null, characterReadStream.readCharactersAsync(1));
                });

                runner.test("with available characters equal to charactersToRead", (Test test) ->
                {
                    final InMemoryCharacterReadStream characterReadStream = new InMemoryCharacterReadStream("abcd", test.getMainAsyncRunner());
                    test.assertSuccessAsync(new char[] { 'a', 'b', 'c', 'd' }, characterReadStream.readCharactersAsync(4));
                    test.assertSuccessAsync(null, characterReadStream.readCharactersAsync(1));
                });

                runner.test("with more available characters than charactersToRead", (Test test) ->
                {
                    final InMemoryCharacterReadStream characterReadStream = new InMemoryCharacterReadStream("abcdefghi", test.getMainAsyncRunner());
                    test.assertSuccessAsync(new char[] { 'a', 'b', 'c' }, characterReadStream.readCharactersAsync(3));
                    test.assertSuccessAsync(new char[] { 'd' }, characterReadStream.readCharactersAsync(1));
                    test.assertSuccessAsync(new char[] { 'e', 'f', 'g', 'h', 'i' }, characterReadStream.readCharactersAsync(1000));
                });
            });

            runner.testGroup("readCharacters(char[])", () ->
            {
                runner.test("with disposed CharacterReadStream", (Test test) ->
                {
                    final InMemoryCharacterReadStream characterReadStream = new InMemoryCharacterReadStream();
                    characterReadStream.dispose();
                    final char[] outputCharacters = new char[5];
                    test.assertError(new IllegalArgumentException("characterReadStream.isDisposed() (true) must be false."), characterReadStream.readCharacters(outputCharacters));
                });

                runner.test("with null outputCharacters", (Test test) ->
                {
                    final InMemoryCharacterReadStream characterReadStream = new InMemoryCharacterReadStream();
                    final char[] outputCharacters = null;
                    test.assertError(new IllegalArgumentException("outputCharacters cannot be null."), characterReadStream.readCharacters(outputCharacters));
                });

                runner.test("with empty outputCharacters", (Test test) ->
                {
                    final InMemoryCharacterReadStream characterReadStream = new InMemoryCharacterReadStream();
                    final char[] outputCharacters = new char[0];
                    test.assertError(new IllegalArgumentException("outputCharacters.length (0) must be greater than 0."), characterReadStream.readCharacters(outputCharacters));
                });

                runner.test("with no characters to read.", (Test test) ->
                {
                    final InMemoryCharacterReadStream characterReadStream = new InMemoryCharacterReadStream();
                    final char[] outputCharacters = new char[5];
                    test.assertSuccess(null, characterReadStream.readCharacters(outputCharacters));
                });

                runner.test("with fewer characters to read than outputCharacters.length", (Test test) ->
                {
                    final InMemoryCharacterReadStream characterReadStream = new InMemoryCharacterReadStream("abc");
                    final char[] outputCharacters = new char[5];
                    test.assertSuccess(3, characterReadStream.readCharacters(outputCharacters));
                    test.assertEqual(new char[] { 'a', 'b', 'c', '\0', '\0' }, outputCharacters);
                    test.assertSuccess(null, characterReadStream.readCharacter());
                });

                runner.test("with equal characters to read to outputCharacters.length", (Test test) ->
                {
                    final InMemoryCharacterReadStream characterReadStream = new InMemoryCharacterReadStream("defgh");
                    final char[] outputCharacters = new char[5];
                    test.assertSuccess(5, characterReadStream.readCharacters(outputCharacters));
                    test.assertEqual(new char[] { 'd', 'e', 'f', 'g', 'h' }, outputCharacters);
                    test.assertSuccess(null, characterReadStream.readCharacter());
                });

                runner.test("with more characters to read than outputCharacters.length", (Test test) ->
                {
                    final InMemoryCharacterReadStream characterReadStream = new InMemoryCharacterReadStream("defghij");
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
                    final InMemoryCharacterReadStream characterReadStream = new InMemoryCharacterReadStream(test.getMainAsyncRunner());
                    characterReadStream.dispose();
                    final char[] outputCharacters = new char[5];
                    test.assertErrorAsync(new IllegalArgumentException("characterReadStream.isDisposed() (true) must be false."), characterReadStream.readCharactersAsync(outputCharacters));
                });

                runner.test("with no AsyncRunner provided to CharacterReadStream's constructor", (Test test) ->
                {
                    final InMemoryCharacterReadStream characterReadStream = new InMemoryCharacterReadStream();
                    final char[] outputCharacters = new char[5];
                    test.assertErrorAsync(new IllegalArgumentException("Cannot invoke CharacterReadStream asynchronous functions when the CharacterReadStream hasn't been assigned an AsyncRunner."), characterReadStream.readCharactersAsync(outputCharacters));
                });

                runner.test("with null outputCharacters", (Test test) ->
                {
                    final InMemoryCharacterReadStream characterReadStream = new InMemoryCharacterReadStream(test.getMainAsyncRunner());
                    final char[] outputCharacters = null;
                    test.assertErrorAsync(new IllegalArgumentException("outputCharacters cannot be null."), characterReadStream.readCharactersAsync(outputCharacters));
                });

                runner.test("with empty outputCharacters", (Test test) ->
                {
                    final InMemoryCharacterReadStream characterReadStream = new InMemoryCharacterReadStream(test.getMainAsyncRunner());
                    final char[] outputCharacters = new char[0];
                    test.assertErrorAsync(new IllegalArgumentException("outputCharacters.length (0) must be greater than 0."), characterReadStream.readCharactersAsync(outputCharacters));
                });

                runner.test("with no characters to read.", (Test test) ->
                {
                    final InMemoryCharacterReadStream characterReadStream = new InMemoryCharacterReadStream(test.getMainAsyncRunner());
                    final char[] outputCharacters = new char[5];
                    test.assertSuccessAsync(null, characterReadStream.readCharactersAsync(outputCharacters));
                });

                runner.test("with fewer characters to read than outputCharacters.length", (Test test) ->
                {
                    final InMemoryCharacterReadStream characterReadStream = new InMemoryCharacterReadStream("abc", test.getMainAsyncRunner());
                    final char[] outputCharacters = new char[5];
                    test.assertSuccessAsync(3, characterReadStream.readCharactersAsync(outputCharacters));
                    test.assertEqual(new char[] { 'a', 'b', 'c', '\0', '\0' }, outputCharacters);
                    test.assertSuccess(null, characterReadStream.readCharacter());
                });

                runner.test("with equal characters to read to outputCharacters.length", (Test test) ->
                {
                    final InMemoryCharacterReadStream characterReadStream = new InMemoryCharacterReadStream("defgh", test.getMainAsyncRunner());
                    final char[] outputCharacters = new char[5];
                    test.assertSuccessAsync(5, characterReadStream.readCharactersAsync(outputCharacters));
                    test.assertEqual(new char[] { 'd', 'e', 'f', 'g', 'h' }, outputCharacters);
                    test.assertSuccess(null, characterReadStream.readCharacter());
                });

                runner.test("with more characters to read than outputCharacters.length", (Test test) ->
                {
                    final InMemoryCharacterReadStream characterReadStream = new InMemoryCharacterReadStream("defghij", test.getMainAsyncRunner());
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
                    final InMemoryCharacterReadStream characterReadStream = new InMemoryCharacterReadStream();
                    characterReadStream.dispose();
                    final char[] outputCharacters = new char[5];
                    test.assertError(new IllegalArgumentException("characterReadStream.isDisposed() (true) must be false."), characterReadStream.readCharacters(outputCharacters, 2, 3));
                });

                runner.test("with null outputCharacters", (Test test) ->
                {
                    final InMemoryCharacterReadStream characterReadStream = new InMemoryCharacterReadStream();
                    final char[] outputCharacters = null;
                    test.assertError(new IllegalArgumentException("outputCharacters cannot be null."), characterReadStream.readCharacters(outputCharacters, 2, 3));
                });

                runner.test("with empty outputCharacters", (Test test) ->
                {
                    final InMemoryCharacterReadStream characterReadStream = new InMemoryCharacterReadStream();
                    final char[] outputCharacters = new char[0];
                    test.assertError(new IllegalArgumentException("outputCharacters.length (0) must be greater than 0."), characterReadStream.readCharacters(outputCharacters, 2, 3));
                });

                runner.test("with negative startIndex", (Test test) ->
                {
                    final InMemoryCharacterReadStream characterReadStream = new InMemoryCharacterReadStream();
                    final char[] outputCharacters = new char[6];
                    test.assertError(new IllegalArgumentException("startIndex (-10) must be between 0 and 5."), characterReadStream.readCharacters(outputCharacters, -10, 3));
                });

                runner.test("with startIndex equal to outputCharacters.length", (Test test) ->
                {
                    final InMemoryCharacterReadStream characterReadStream = new InMemoryCharacterReadStream();
                    final char[] outputCharacters = new char[6];
                    test.assertError(new IllegalArgumentException("startIndex (6) must be between 0 and 5."), characterReadStream.readCharacters(outputCharacters, outputCharacters.length, 3));
                });

                runner.test("with negative length", (Test test) ->
                {
                    final InMemoryCharacterReadStream characterReadStream = new InMemoryCharacterReadStream();
                    final char[] outputCharacters = new char[6];
                    test.assertError(new IllegalArgumentException("length (-50) must be between 1 and 4."), characterReadStream.readCharacters(outputCharacters, 2, -50));
                });

                runner.test("with zero length", (Test test) ->
                {
                    final InMemoryCharacterReadStream characterReadStream = new InMemoryCharacterReadStream();
                    final char[] outputCharacters = new char[6];
                    test.assertError(new IllegalArgumentException("length (0) must be between 1 and 4."), characterReadStream.readCharacters(outputCharacters, 2, 0));
                });

                runner.test("with length greater than outputCharacters.length - startIndex", (Test test) ->
                {
                    final InMemoryCharacterReadStream characterReadStream = new InMemoryCharacterReadStream();
                    final char[] outputCharacters = new char[6];
                    test.assertError(new IllegalArgumentException("length (5) must be between 1 and 4."), characterReadStream.readCharacters(outputCharacters, 2, 5));
                });

                runner.test("with no characters to read.", (Test test) ->
                {
                    final InMemoryCharacterReadStream characterReadStream = new InMemoryCharacterReadStream();
                    final char[] outputCharacters = new char[6];
                    test.assertSuccess(null, characterReadStream.readCharacters(outputCharacters, 2, 3));
                });

                runner.test("with fewer characters to read than length", (Test test) ->
                {
                    final InMemoryCharacterReadStream characterReadStream = new InMemoryCharacterReadStream("a");
                    final char[] outputCharacters = new char[6];
                    test.assertSuccess(1, characterReadStream.readCharacters(outputCharacters, 2, 3));
                    test.assertEqual(new char[] { '\0', '\0', 'a', '\0', '\0', '\0' }, outputCharacters);
                    test.assertSuccess(null, characterReadStream.readCharacter());
                });

                runner.test("with equal characters to read to length", (Test test) ->
                {
                    final InMemoryCharacterReadStream characterReadStream = new InMemoryCharacterReadStream("def");
                    final char[] outputCharacters = new char[6];
                    test.assertSuccess(3, characterReadStream.readCharacters(outputCharacters, 2, 3));
                    test.assertEqual(new char[] { '\0', '\0', 'd', 'e', 'f', '\0' }, outputCharacters);
                    test.assertSuccess(null, characterReadStream.readCharacter());
                });

                runner.test("with more characters to read than length", (Test test) ->
                {
                    final InMemoryCharacterReadStream characterReadStream = new InMemoryCharacterReadStream("defghij");
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
                    final InMemoryCharacterReadStream characterReadStream = new InMemoryCharacterReadStream(test.getMainAsyncRunner());
                    characterReadStream.dispose();
                    final char[] outputCharacters = new char[5];
                    test.assertErrorAsync(new IllegalArgumentException("characterReadStream.isDisposed() (true) must be false."), characterReadStream.readCharactersAsync(outputCharacters, 2, 3));
                });

                runner.test("with no AsyncRunner provided to CharacterReadStream's constructor", (Test test) ->
                {
                    final InMemoryCharacterReadStream characterReadStream = new InMemoryCharacterReadStream();
                    final char[] outputCharacters = new char[5];
                    test.assertErrorAsync(new IllegalArgumentException("Cannot invoke CharacterReadStream asynchronous functions when the CharacterReadStream hasn't been assigned an AsyncRunner."), characterReadStream.readCharactersAsync(outputCharacters, 2, 3));
                });

                runner.test("with null outputCharacters", (Test test) ->
                {
                    final InMemoryCharacterReadStream characterReadStream = new InMemoryCharacterReadStream(test.getMainAsyncRunner());
                    final char[] outputCharacters = null;
                    test.assertErrorAsync(new IllegalArgumentException("outputCharacters cannot be null."), characterReadStream.readCharactersAsync(outputCharacters, 2, 3));
                });

                runner.test("with empty outputCharacters", (Test test) ->
                {
                    final InMemoryCharacterReadStream characterReadStream = new InMemoryCharacterReadStream(test.getMainAsyncRunner());
                    final char[] outputCharacters = new char[0];
                    test.assertErrorAsync(new IllegalArgumentException("outputCharacters.length (0) must be greater than 0."), characterReadStream.readCharactersAsync(outputCharacters, 2, 3));
                });

                runner.test("with negative startIndex", (Test test) ->
                {
                    final InMemoryCharacterReadStream characterReadStream = new InMemoryCharacterReadStream(test.getMainAsyncRunner());
                    final char[] outputCharacters = new char[6];
                    test.assertErrorAsync(new IllegalArgumentException("startIndex (-10) must be between 0 and 5."), characterReadStream.readCharactersAsync(outputCharacters, -10, 3));
                });

                runner.test("with startIndex equal to outputCharacters.length", (Test test) ->
                {
                    final InMemoryCharacterReadStream characterReadStream = new InMemoryCharacterReadStream(test.getMainAsyncRunner());
                    final char[] outputCharacters = new char[6];
                    test.assertErrorAsync(new IllegalArgumentException("startIndex (6) must be between 0 and 5."), characterReadStream.readCharactersAsync(outputCharacters, outputCharacters.length, 3));
                });

                runner.test("with negative length", (Test test) ->
                {
                    final InMemoryCharacterReadStream characterReadStream = new InMemoryCharacterReadStream(test.getMainAsyncRunner());
                    final char[] outputCharacters = new char[6];
                    test.assertErrorAsync(new IllegalArgumentException("length (-50) must be between 1 and 4."), characterReadStream.readCharactersAsync(outputCharacters, 2, -50));
                });

                runner.test("with zero length", (Test test) ->
                {
                    final InMemoryCharacterReadStream characterReadStream = new InMemoryCharacterReadStream(test.getMainAsyncRunner());
                    final char[] outputCharacters = new char[6];
                    test.assertErrorAsync(new IllegalArgumentException("length (0) must be between 1 and 4."), characterReadStream.readCharactersAsync(outputCharacters, 2, 0));
                });

                runner.test("with length greater than outputCharacters.length - startIndex", (Test test) ->
                {
                    final InMemoryCharacterReadStream characterReadStream = new InMemoryCharacterReadStream(test.getMainAsyncRunner());
                    final char[] outputCharacters = new char[6];
                    test.assertErrorAsync(new IllegalArgumentException("length (5) must be between 1 and 4."), characterReadStream.readCharactersAsync(outputCharacters, 2, 5));
                });

                runner.test("with no characters to read.", (Test test) ->
                {
                    final InMemoryCharacterReadStream characterReadStream = new InMemoryCharacterReadStream(test.getMainAsyncRunner());
                    final char[] outputCharacters = new char[6];
                    test.assertSuccessAsync(null, characterReadStream.readCharactersAsync(outputCharacters, 2, 3));
                });

                runner.test("with fewer characters to read than length", (Test test) ->
                {
                    final InMemoryCharacterReadStream characterReadStream = new InMemoryCharacterReadStream("a", test.getMainAsyncRunner());
                    final char[] outputCharacters = new char[6];
                    test.assertSuccessAsync(1, characterReadStream.readCharactersAsync(outputCharacters, 2, 3));
                    test.assertEqual(new char[] { '\0', '\0', 'a', '\0', '\0', '\0' }, outputCharacters);
                    test.assertSuccess(null, characterReadStream.readCharacter());
                });

                runner.test("with equal characters to read to length", (Test test) ->
                {
                    final InMemoryCharacterReadStream characterReadStream = new InMemoryCharacterReadStream("def", test.getMainAsyncRunner());
                    final char[] outputCharacters = new char[6];
                    test.assertSuccessAsync(3, characterReadStream.readCharactersAsync(outputCharacters, 2, 3));
                    test.assertEqual(new char[] { '\0', '\0', 'd', 'e', 'f', '\0' }, outputCharacters);
                    test.assertSuccess(null, characterReadStream.readCharacter());
                });

                runner.test("with more characters to read than length", (Test test) ->
                {
                    final InMemoryCharacterReadStream characterReadStream = new InMemoryCharacterReadStream("defghij", test.getMainAsyncRunner());
                    final char[] outputCharacters = new char[6];
                    test.assertSuccessAsync(4, characterReadStream.readCharactersAsync(outputCharacters, 2, 4));
                    test.assertEqual(new char[] { '\0', '\0', 'd', 'e', 'f', 'g' }, outputCharacters);
                    test.assertSuccess('h', characterReadStream.readCharacter());
                });
            });

            runner.testGroup("readString(int)", () ->
            {
                runner.test("with disposed CharacterReadStream", (Test test) ->
                {
                    final InMemoryCharacterReadStream characterReadStream = new InMemoryCharacterReadStream();
                    characterReadStream.dispose();
                    test.assertError(new IllegalArgumentException("characterReadStream.isDisposed() (true) must be false."), characterReadStream.readString(5));
                });

                runner.test("with no characters to read", (Test test) ->
                {
                    final InMemoryCharacterReadStream characterReadStream = new InMemoryCharacterReadStream();
                    test.assertSuccess(null, characterReadStream.readString(5));
                });

                runner.test("with fewer available characters than charactersToRead", (Test test) ->
                {
                    final InMemoryCharacterReadStream characterReadStream = new InMemoryCharacterReadStream("abc");
                    test.assertSuccess("abc", characterReadStream.readString(5));
                    test.assertSuccess(null, characterReadStream.readString(1));
                });

                runner.test("with available characters equal to charactersToRead", (Test test) ->
                {
                    final InMemoryCharacterReadStream characterReadStream = new InMemoryCharacterReadStream("abcd");
                    test.assertSuccess("abcd", characterReadStream.readString(4));
                    test.assertSuccess(null, characterReadStream.readString(1));
                });

                runner.test("with more available characters than charactersToRead", (Test test) ->
                {
                    final InMemoryCharacterReadStream characterReadStream = new InMemoryCharacterReadStream("abcdefghi");
                    test.assertSuccess("abc", characterReadStream.readString(3));
                    test.assertSuccess("d", characterReadStream.readString(1));
                    test.assertSuccess("efghi", characterReadStream.readString(1000));
                });
            });

            runner.testGroup("readStringAsync(int)", () ->
            {
                runner.test("with disposed CharacterReadStream", (Test test) ->
                {
                    final InMemoryCharacterReadStream characterReadStream = new InMemoryCharacterReadStream(test.getMainAsyncRunner());
                    characterReadStream.dispose();
                    test.assertErrorAsync(new IllegalArgumentException("characterReadStream.isDisposed() (true) must be false."), characterReadStream.readStringAsync(5));
                });

                runner.test("with no AsyncRunner provided to CharacterReadStream's constructor", (Test test) ->
                {
                    final InMemoryCharacterReadStream characterReadStream = new InMemoryCharacterReadStream();
                    test.assertErrorAsync(new IllegalArgumentException("Cannot invoke CharacterReadStream asynchronous functions when the CharacterReadStream hasn't been assigned an AsyncRunner."), characterReadStream.readStringAsync(3));
                });

                runner.test("with no characters to read", (Test test) ->
                {
                    final InMemoryCharacterReadStream characterReadStream = new InMemoryCharacterReadStream(test.getMainAsyncRunner());
                    test.assertSuccessAsync(null, characterReadStream.readStringAsync(5));
                });

                runner.test("with fewer available characters than charactersToRead", (Test test) ->
                {
                    final InMemoryCharacterReadStream characterReadStream = new InMemoryCharacterReadStream("abc", test.getMainAsyncRunner());
                    test.assertSuccessAsync("abc", characterReadStream.readStringAsync(5));
                    test.assertSuccessAsync(null, characterReadStream.readStringAsync(1));
                });

                runner.test("with available characters equal to charactersToRead", (Test test) ->
                {
                    final InMemoryCharacterReadStream characterReadStream = new InMemoryCharacterReadStream("abcd", test.getMainAsyncRunner());
                    test.assertSuccessAsync("abcd", characterReadStream.readStringAsync(4));
                    test.assertSuccessAsync(null, characterReadStream.readStringAsync(1));
                });

                runner.test("with more available characters than charactersToRead", (Test test) ->
                {
                    final InMemoryCharacterReadStream characterReadStream = new InMemoryCharacterReadStream("abcdefghi", test.getMainAsyncRunner());
                    test.assertSuccessAsync("abc", characterReadStream.readStringAsync(3));
                    test.assertSuccessAsync("d", characterReadStream.readStringAsync(1));
                    test.assertSuccessAsync("efghi", characterReadStream.readStringAsync(1000));
                });
            });

            runner.testGroup("readLine()", () ->
            {
                runner.test("when closed", (Test test) ->
                {
                    final InMemoryCharacterReadStream characterReadStream = new InMemoryCharacterReadStream("test");
                    try
                    {
                        characterReadStream.close();
                    }
                    catch (Exception e)
                    {
                        test.fail(e);
                    }
                    test.assertError(new IllegalArgumentException("characterReadStream.isDisposed() (true) must be false."), characterReadStream.readLine());
                });

                final Action2<String,String[]> readLineTest = (String text, String[] expectedLines) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(text), (Test test) ->
                    {
                        try (final InMemoryCharacterReadStream characterReadStream = new InMemoryCharacterReadStream(text))
                        {
                            final int expectedLineCount = expectedLines == null ? 0 : expectedLines.length;
                            for (int i = 0; i < expectedLineCount; ++i)
                            {
                                final Result<String> readLineResult = characterReadStream.readLine();
                                test.assertSuccess(readLineResult);
                                test.assertEqual(Strings.escape(expectedLines[i]), Strings.escape(readLineResult.getValue()));
                            }
                        }
                    });
                };

                readLineTest.run("\n", new String[] { "" });
                readLineTest.run("\r\n", new String[] { "" });
                readLineTest.run("a\n", new String[] { "a" });
                readLineTest.run("a\r\n", new String[] { "a" });
                readLineTest.run("a\nb\n", new String[] { "a", "b" });
                readLineTest.run("a\r\nb\n", new String[] { "a", "b" });
                readLineTest.run("a\r\r\r\r\rb", new String[] { "a\r\r\r\r\rb"});
                readLineTest.run("a\r\r\r\r\r\n", new String[] { "a\r\r\r\r"});
                readLineTest.run("a\nb\r\nc\n", new String[] { "a", "b", "c" });
            });

            runner.test("asLineReadStream()", (Test test) ->
            {
                final InMemoryCharacterReadStream characterReadStream = new InMemoryCharacterReadStream();
                final LineReadStream lineReadStream = characterReadStream.asLineReadStream();
                test.assertNotNull(lineReadStream);
            });
        });
    }
}
