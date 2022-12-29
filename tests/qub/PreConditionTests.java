package qub;

public interface PreConditionTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(PreCondition.class, () ->
        {
            runner.testGroup("assertNotNullAndNotEmpty(long[],String)", () ->
            {
                final Action3<long[],String,Throwable> assertNotNullAndNotEmptyErrorTest = (long[] array, String expressionName, Throwable expected) ->
                {
                    runner.test("with " + English.andList(array, expressionName), (Test test) ->
                    {
                        test.assertThrows(() -> PreCondition.assertNotNullAndNotEmpty(array, expressionName), expected);
                    });
                };

                assertNotNullAndNotEmptyErrorTest.run(null, "a", new PreConditionFailure("a cannot be null."));
                assertNotNullAndNotEmptyErrorTest.run(new long[0], "a", new PreConditionFailure("a cannot be empty."));
                assertNotNullAndNotEmptyErrorTest.run(new long[1], null, new PreConditionFailure("expressionName cannot be null."));
                assertNotNullAndNotEmptyErrorTest.run(new long[1], "", new PreConditionFailure("expressionName cannot be empty."));

                final Action2<long[],String> assertNotNullAndNotEmptyTest = (long[] array, String expressionName) ->
                {
                    runner.test("with " + English.andList(array, expressionName), (Test test) ->
                    {
                        PreCondition.assertNotNullAndNotEmpty(array, expressionName);
                    });
                };

                assertNotNullAndNotEmptyTest.run(new long[1], "a");
            });

            runner.testGroup("assertSame(T,T,String)", () ->
            {
                runner.test("with same reference", (Test test) ->
                {
                    final Integer value = 5;
                    PreCondition.assertSame(value, value, "value");
                });

                runner.test("with not same reference", (Test test) ->
                {
                    @SuppressWarnings("removal")
                    final Integer value1 = new Integer(5);
                    @SuppressWarnings("removal")
                    final Integer value2 = new Integer(5);
                    test.assertThrows(() -> PreCondition.assertSame(value1, value2, "value2"),
                        new PreConditionFailure("value2 (5) must be the same object as 5."));
                });

                runner.test("with constant String references", (Test test) ->
                {
                    final String value1 = "hello";
                    final String value2 = "hello";
                    PreCondition.assertSame(value1, value2, "value2");
                });

                runner.test("with constant Integer references", (Test test) ->
                {
                    final Integer value1 = 5;
                    final Integer value2 = 5;
                    PreCondition.assertSame(value1, value2, "value2");
                });
            });

            runner.testGroup("assertNotSame(T,T,String)", () ->
            {
                runner.test("with same reference", (Test test) ->
                {
                    final Integer value = 5;
                    test.assertThrows(() -> PreCondition.assertNotSame(value, value, "value"),
                        new PreConditionFailure("value (5) must not be the same object as 5."));
                });

                runner.test("with not same reference", (Test test) ->
                {
                    @SuppressWarnings("removal")
                    final Integer value1 = new Integer(5);
                    @SuppressWarnings("removal")
                    final Integer value2 = new Integer(5);
                    PreCondition.assertNotSame(value1, value2, "value2");
                });

                runner.test("with constant String references", (Test test) ->
                {
                    final String value1 = "hello";
                    final String value2 = "hello";
                    test.assertThrows(() -> PreCondition.assertNotSame(value1, value2, "value2"),
                        new PreConditionFailure("value2 (hello) must not be the same object as hello."));
                });

                runner.test("with constant Integer references", (Test test) ->
                {
                    final Integer value1 = 5;
                    final Integer value2 = 5;
                    test.assertThrows(() -> PreCondition.assertNotSame(value1, value2, "value2"),
                        new PreConditionFailure("value2 (5) must not be the same object as 5."));
                });
            });

            runner.testGroup("assertEqual(char,char,String)", () ->
            {
                final Action4<Character,Character,String,Throwable> assertEqualErrorTest = (Character expected, Character value, String expressionName, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(expected) + ", " + Strings.escapeAndQuote(value) + ", and " + Strings.escapeAndQuote(expressionName), (Test test) ->
                    {
                        test.assertThrows(() -> PreCondition.assertEqual(expected.charValue(), value.charValue(), expressionName),
                            expectedError);
                    });
                };

                assertEqualErrorTest.run('a', 'b', "c", new PreConditionFailure("c (b) must be a."));
                assertEqualErrorTest.run('a', 'A', "d", new PreConditionFailure("d (A) must be a."));

                final Action3<Character,Character,String> assertEqualTest = (Character expected, Character value, String expressionName) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(expected) + ", " + Strings.escapeAndQuote(value) + ", and " + Strings.escapeAndQuote(expressionName), (Test test) ->
                    {
                        PreCondition.assertEqual(expected.charValue(), value.charValue(), expressionName);
                    });
                };

                assertEqualTest.run('a', 'a', "myVariable");
            });

            runner.testGroup("assertEqual(char,Character,String)", () ->
            {
                final Action4<Character,Character,String,Throwable> assertEqualErrorTest = (Character expected, Character value, String expressionName, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(expected) + ", " + Strings.escapeAndQuote(value) + ", and " + Strings.escapeAndQuote(expressionName), (Test test) ->
                    {
                        test.assertThrows(() -> PreCondition.assertEqual(expected.charValue(), value, expressionName),
                            expectedError);
                    });
                };

                assertEqualErrorTest.run('a', null, "c", new PreConditionFailure("c (null) must be a."));
                assertEqualErrorTest.run('a', 'b', "c", new PreConditionFailure("c (b) must be a."));
                assertEqualErrorTest.run('a', 'A', "d", new PreConditionFailure("d (A) must be a."));

                final Action3<Character,Character,String> assertEqualTest = (Character expected, Character value, String expressionName) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(expected) + ", " + Strings.escapeAndQuote(value) + ", and " + Strings.escapeAndQuote(expressionName), (Test test) ->
                    {
                        PreCondition.assertEqual(expected.charValue(), value, expressionName);
                    });
                };

                assertEqualTest.run('a', 'a', "myVariable");
            });

            runner.testGroup("assertEqual(Character,char,String)", () ->
            {
                final Action4<Character,Character,String,Throwable> assertEqualErrorTest = (Character expected, Character value, String expressionName, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(expected) + ", " + Strings.escapeAndQuote(value) + ", and " + Strings.escapeAndQuote(expressionName), (Test test) ->
                    {
                        test.assertThrows(() -> PreCondition.assertEqual(expected, value.charValue(), expressionName),
                            expectedError);
                    });
                };

                assertEqualErrorTest.run(null, 'b', "c", new PreConditionFailure("c (b) must be null."));
                assertEqualErrorTest.run('a', 'b', "c", new PreConditionFailure("c (b) must be a."));
                assertEqualErrorTest.run('a', 'A', "d", new PreConditionFailure("d (A) must be a."));

                final Action3<Character,Character,String> assertEqualTest = (Character expected, Character value, String expressionName) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(expected) + ", " + Strings.escapeAndQuote(value) + ", and " + Strings.escapeAndQuote(expressionName), (Test test) ->
                    {
                        PreCondition.assertEqual(expected, value.charValue(), expressionName);
                    });
                };

                assertEqualTest.run('a', 'a', "myVariable");
            });

            runner.testGroup("assertStartIndex(int,int)", () ->
            {
                final Action3<Integer,Integer,Throwable> assertStartIndexErrorTest = (Integer startIndex, Integer arrayLength, Throwable expected) ->
                {
                    runner.test("with " + English.andList(startIndex, arrayLength), (Test test) ->
                    {
                        test.assertThrows(() -> PreCondition.assertStartIndex(startIndex, arrayLength), expected);
                    });
                };

                assertStartIndexErrorTest.run(5, -1, new PreConditionFailure("arrayLength (-1) must be greater than or equal to 0."));
                assertStartIndexErrorTest.run(-1, 5, new PreConditionFailure("startIndex (-1) must be between 0 and 4."));
                assertStartIndexErrorTest.run(5, 5, new PreConditionFailure("startIndex (5) must be between 0 and 4."));
                assertStartIndexErrorTest.run(1, 0, new PreConditionFailure("startIndex (1) must be equal to 0."));

                final Action2<Integer,Integer> assertStartIndexTest = (Integer startIndex, Integer arrayLength) ->
                {
                    runner.test("with " + English.andList(startIndex, arrayLength), (Test test) ->
                    {
                        PreCondition.assertStartIndex(startIndex, arrayLength);
                    });
                };

                assertStartIndexTest.run(0, 0);
                assertStartIndexTest.run(1, 3);
            });

            runner.testGroup("assertStartIndex(int,int,String)", () ->
            {
                final Action4<Integer,Integer,String,Throwable> assertStartIndexErrorTest = (Integer startIndex, Integer arrayLength, String variableName, Throwable expected) ->
                {
                    runner.test("with " + English.andList(startIndex, arrayLength, Strings.escapeAndQuote(variableName)), (Test test) ->
                    {
                        test.assertThrows(() -> PreCondition.assertStartIndex(startIndex, arrayLength, variableName), expected);
                    });
                };

                assertStartIndexErrorTest.run(0, 1, null, new PreConditionFailure("expressionName cannot be null."));
                assertStartIndexErrorTest.run(0, 1, "", new PreConditionFailure("expressionName cannot be empty."));
                assertStartIndexErrorTest.run(5, -1, "a", new PreConditionFailure("arrayLength (-1) must be greater than or equal to 0."));
                assertStartIndexErrorTest.run(-1, 5, "a", new PreConditionFailure("a (-1) must be between 0 and 4."));
                assertStartIndexErrorTest.run(5, 5, "a", new PreConditionFailure("a (5) must be between 0 and 4."));
                assertStartIndexErrorTest.run(1, 0, "a", new PreConditionFailure("a (1) must be equal to 0."));

                final Action3<Integer,Integer,String> assertStartIndexTest = (Integer startIndex, Integer arrayLength, String variableName) ->
                {
                    runner.test("with " + English.andList(startIndex, arrayLength, Strings.escapeAndQuote(variableName)), (Test test) ->
                    {
                        PreCondition.assertStartIndex(startIndex, arrayLength, variableName);
                    });
                };

                assertStartIndexTest.run(0, 0, "a");
                assertStartIndexTest.run(1, 3, "a");
            });

            runner.testGroup("assertStartIndex(long,long)", () ->
            {
                final Action3<Long,Long,Throwable> assertStartIndexErrorTest = (Long startIndex, Long arrayLength, Throwable expected) ->
                {
                    runner.test("with " + English.andList(startIndex, arrayLength), (Test test) ->
                    {
                        test.assertThrows(() -> PreCondition.assertStartIndex(startIndex, arrayLength), expected);
                    });
                };

                assertStartIndexErrorTest.run(5L, -1L, new PreConditionFailure("arrayLength (-1) must be greater than or equal to 0."));
                assertStartIndexErrorTest.run(-1L, 5L, new PreConditionFailure("startIndex (-1) must be between 0 and 4."));
                assertStartIndexErrorTest.run(5L, 5L, new PreConditionFailure("startIndex (5) must be between 0 and 4."));
                assertStartIndexErrorTest.run(1L, 0L, new PreConditionFailure("startIndex (1) must be equal to 0."));

                final Action2<Long,Long> assertStartIndexTest = (Long startIndex, Long arrayLength) ->
                {
                    runner.test("with " + English.andList(startIndex, arrayLength), (Test test) ->
                    {
                        PreCondition.assertStartIndex(startIndex, arrayLength);
                    });
                };

                assertStartIndexTest.run(0L, 0L);
                assertStartIndexTest.run(1L, 3L);
            });

            runner.testGroup("assertStartIndex(long,long,String)", () ->
            {
                final Action4<Long,Long,String,Throwable> assertStartIndexErrorTest = (Long startIndex, Long arrayLength, String variableName, Throwable expected) ->
                {
                    runner.test("with " + English.andList(startIndex, arrayLength, Strings.escapeAndQuote(variableName)), (Test test) ->
                    {
                        test.assertThrows(() -> PreCondition.assertStartIndex(startIndex, arrayLength, variableName), expected);
                    });
                };

                assertStartIndexErrorTest.run(0L, 1L, null, new PreConditionFailure("expressionName cannot be null."));
                assertStartIndexErrorTest.run(0L, 1L, "", new PreConditionFailure("expressionName cannot be empty."));
                assertStartIndexErrorTest.run(5L, -1L, "a", new PreConditionFailure("arrayLength (-1) must be greater than or equal to 0."));
                assertStartIndexErrorTest.run(-1L, 5L, "a", new PreConditionFailure("a (-1) must be between 0 and 4."));
                assertStartIndexErrorTest.run(5L, 5L, "a", new PreConditionFailure("a (5) must be between 0 and 4."));
                assertStartIndexErrorTest.run(1L, 0L, "a", new PreConditionFailure("a (1) must be equal to 0."));

                final Action3<Long,Long,String> assertStartIndexTest = (Long startIndex, Long arrayLength, String variableName) ->
                {
                    runner.test("with " + English.andList(startIndex, arrayLength, Strings.escapeAndQuote(variableName)), (Test test) ->
                    {
                        PreCondition.assertStartIndex(startIndex, arrayLength, variableName);
                    });
                };

                assertStartIndexTest.run(0L, 0L, "a");
                assertStartIndexTest.run(1L, 3L, "a");
            });

            runner.testGroup("assertNonEmptyStartIndex(int,int)", () ->
            {
                final Action3<Integer,Integer,Throwable> assertNonEmptyStartIndexErrorTest = (Integer startIndex, Integer arrayLength, Throwable expected) ->
                {
                    runner.test("with " + English.andList(startIndex, arrayLength), (Test test) ->
                    {
                        test.assertThrows(() -> PreCondition.assertNonEmptyStartIndex(startIndex, arrayLength), expected);
                    });
                };

                assertNonEmptyStartIndexErrorTest.run(5, -1, new PreConditionFailure("arrayLength (-1) must be greater than or equal to 1."));
                assertNonEmptyStartIndexErrorTest.run(1, 0, new PreConditionFailure("arrayLength (0) must be greater than or equal to 1."));
                assertNonEmptyStartIndexErrorTest.run(1, 1, new PreConditionFailure("startIndex (1) must be equal to 0."));
                assertNonEmptyStartIndexErrorTest.run(-1, 5, new PreConditionFailure("startIndex (-1) must be between 0 and 4."));
                assertNonEmptyStartIndexErrorTest.run(5, 5, new PreConditionFailure("startIndex (5) must be between 0 and 4."));

                final Action2<Integer,Integer> assertNonEmptyStartIndexTest = (Integer startIndex, Integer arrayLength) ->
                {
                    runner.test("with " + English.andList(startIndex, arrayLength), (Test test) ->
                    {
                        PreCondition.assertNonEmptyStartIndex(startIndex, arrayLength);
                    });
                };

                assertNonEmptyStartIndexTest.run(0, 1);
                assertNonEmptyStartIndexTest.run(1, 3);
            });

            runner.testGroup("assertNonEmptyStartIndex(int,int,String)", () ->
            {
                final Action4<Integer,Integer,String,Throwable> assertNonEmptyStartIndexErrorTest = (Integer startIndex, Integer arrayLength, String variableName, Throwable expected) ->
                {
                    runner.test("with " + English.andList(startIndex, arrayLength, Strings.escapeAndQuote(variableName)), (Test test) ->
                    {
                        test.assertThrows(() -> PreCondition.assertNonEmptyStartIndex(startIndex, arrayLength, variableName), expected);
                    });
                };

                assertNonEmptyStartIndexErrorTest.run(0, 1, null, new PreConditionFailure("expressionName cannot be null."));
                assertNonEmptyStartIndexErrorTest.run(0, 1, "", new PreConditionFailure("expressionName cannot be empty."));
                assertNonEmptyStartIndexErrorTest.run(5, -1, "a", new PreConditionFailure("arrayLength (-1) must be greater than or equal to 1."));
                assertNonEmptyStartIndexErrorTest.run(1, 0, "a", new PreConditionFailure("arrayLength (0) must be greater than or equal to 1."));
                assertNonEmptyStartIndexErrorTest.run(1, 1, "a", new PreConditionFailure("a (1) must be equal to 0."));
                assertNonEmptyStartIndexErrorTest.run(-1, 5, "a", new PreConditionFailure("a (-1) must be between 0 and 4."));
                assertNonEmptyStartIndexErrorTest.run(5, 5, "a", new PreConditionFailure("a (5) must be between 0 and 4."));

                final Action3<Integer,Integer,String> assertNonEmptyStartIndexTest = (Integer startIndex, Integer arrayLength, String variableName) ->
                {
                    runner.test("with " + English.andList(startIndex, arrayLength, Strings.escapeAndQuote(variableName)), (Test test) ->
                    {
                        PreCondition.assertNonEmptyStartIndex(startIndex, arrayLength, variableName);
                    });
                };

                assertNonEmptyStartIndexTest.run(0, 1, "a");
                assertNonEmptyStartIndexTest.run(1, 3, "a");
            });

            runner.testGroup("assertNonEmptyStartIndex(long,long)", () ->
            {
                final Action3<Long,Long,Throwable> assertNonEmptyStartIndexErrorTest = (Long startIndex, Long arrayLength, Throwable expected) ->
                {
                    runner.test("with " + English.andList(startIndex, arrayLength), (Test test) ->
                    {
                        test.assertThrows(() -> PreCondition.assertNonEmptyStartIndex(startIndex, arrayLength), expected);
                    });
                };

                assertNonEmptyStartIndexErrorTest.run(5L, -1L, new PreConditionFailure("arrayLength (-1) must be greater than or equal to 1."));
                assertNonEmptyStartIndexErrorTest.run(1L, 0L, new PreConditionFailure("arrayLength (0) must be greater than or equal to 1."));
                assertNonEmptyStartIndexErrorTest.run(1L, 1L, new PreConditionFailure("startIndex (1) must be equal to 0."));
                assertNonEmptyStartIndexErrorTest.run(-1L, 5L, new PreConditionFailure("startIndex (-1) must be between 0 and 4."));
                assertNonEmptyStartIndexErrorTest.run(5L, 5L, new PreConditionFailure("startIndex (5) must be between 0 and 4."));

                final Action2<Long,Long> assertNonEmptyStartIndexTest = (Long startIndex, Long arrayLength) ->
                {
                    runner.test("with " + English.andList(startIndex, arrayLength), (Test test) ->
                    {
                        PreCondition.assertNonEmptyStartIndex(startIndex, arrayLength);
                    });
                };

                assertNonEmptyStartIndexTest.run(0L, 1L);
                assertNonEmptyStartIndexTest.run(1L, 3L);
            });

            runner.testGroup("assertNonEmptyStartIndex(long,long,String)", () ->
            {
                final Action4<Long,Long,String,Throwable> assertNonEmptyStartIndexErrorTest = (Long startIndex, Long arrayLength, String variableName, Throwable expected) ->
                {
                    runner.test("with " + English.andList(startIndex, arrayLength, Strings.escapeAndQuote(variableName)), (Test test) ->
                    {
                        test.assertThrows(() -> PreCondition.assertNonEmptyStartIndex(startIndex, arrayLength, variableName), expected);
                    });
                };

                assertNonEmptyStartIndexErrorTest.run(0L, 1L, null, new PreConditionFailure("expressionName cannot be null."));
                assertNonEmptyStartIndexErrorTest.run(0L, 1L, "", new PreConditionFailure("expressionName cannot be empty."));
                assertNonEmptyStartIndexErrorTest.run(5L, -1L, "a", new PreConditionFailure("arrayLength (-1) must be greater than or equal to 1."));
                assertNonEmptyStartIndexErrorTest.run(1L, 0L, "a", new PreConditionFailure("arrayLength (0) must be greater than or equal to 1."));
                assertNonEmptyStartIndexErrorTest.run(1L, 1L, "a", new PreConditionFailure("a (1) must be equal to 0."));
                assertNonEmptyStartIndexErrorTest.run(-1L, 5L, "a", new PreConditionFailure("a (-1) must be between 0 and 4."));
                assertNonEmptyStartIndexErrorTest.run(5L, 5L, "a", new PreConditionFailure("a (5) must be between 0 and 4."));

                final Action3<Long,Long,String> assertNonEmptyStartIndexTest = (Long startIndex, Long arrayLength, String variableName) ->
                {
                    runner.test("with " + English.andList(startIndex, arrayLength, Strings.escapeAndQuote(variableName)), (Test test) ->
                    {
                        PreCondition.assertNonEmptyStartIndex(startIndex, arrayLength, variableName);
                    });
                };

                assertNonEmptyStartIndexTest.run(0L, 1L, "a");
                assertNonEmptyStartIndexTest.run(1L, 3L, "a");
            });

            runner.testGroup("assertLength(int,int,int)", () ->
            {
                final Action4<Integer,Integer,Integer,Throwable> assertLengthErrorTest = (Integer length, Integer startIndex, Integer arrayLength, Throwable expected) ->
                {
                    runner.test("with " + English.andList(length, startIndex, arrayLength), (Test test) ->
                    {
                        test.assertThrows(() -> PreCondition.assertLength(length, startIndex, arrayLength), expected);
                    });
                };

                assertLengthErrorTest.run(1, 1, -1, new PreConditionFailure("arrayLength (-1) must be greater than or equal to 0."));
                assertLengthErrorTest.run(1, -1, 1, new PreConditionFailure("startIndex (-1) must be equal to 0."));
                assertLengthErrorTest.run(1, -1, 2, new PreConditionFailure("startIndex (-1) must be between 0 and 1."));
                assertLengthErrorTest.run(1, 2, 2, new PreConditionFailure("startIndex (2) must be between 0 and 1."));
                assertLengthErrorTest.run(1, 3, 2, new PreConditionFailure("startIndex (3) must be between 0 and 1."));
                assertLengthErrorTest.run(-1, 0, 1, new PreConditionFailure("length (-1) must be between 0 and 1."));
                assertLengthErrorTest.run(2, 0, 1, new PreConditionFailure("length (2) must be between 0 and 1."));
                assertLengthErrorTest.run(1, 0, 0, new PreConditionFailure("length (1) must be equal to 0."));

                final Action3<Integer,Integer,Integer> assertLengthTest = (Integer length, Integer startIndex, Integer arrayLength) ->
                {
                    runner.test("with " + English.andList(length, startIndex, arrayLength), (Test test) ->
                    {
                        PreCondition.assertLength(length, startIndex, arrayLength);
                    });
                };

                assertLengthTest.run(0, 0, 0);
                assertLengthTest.run(3, 0, 3);
                assertLengthTest.run(1, 1, 3);
                assertLengthTest.run(2, 1, 3);
            });

            runner.testGroup("assertLength(int,int,int,String)", () ->
            {
                final Action5<Integer,Integer,Integer,String,Throwable> assertLengthErrorTest = (Integer length, Integer startIndex, Integer arrayLength, String variableName, Throwable expected) ->
                {
                    runner.test("with " + English.andList(length, startIndex, arrayLength, Strings.escapeAndQuote(variableName)), (Test test) ->
                    {
                        test.assertThrows(() -> PreCondition.assertLength(length, startIndex, arrayLength, variableName), expected);
                    });
                };

                assertLengthErrorTest.run(1, 1, 2, null, new PreConditionFailure("expressionName cannot be null."));
                assertLengthErrorTest.run(1, 1, 2, "", new PreConditionFailure("expressionName cannot be empty."));
                assertLengthErrorTest.run(1, 1, -1, "a", new PreConditionFailure("arrayLength (-1) must be greater than or equal to 0."));
                assertLengthErrorTest.run(1, -1, 1, "a", new PreConditionFailure("startIndex (-1) must be equal to 0."));
                assertLengthErrorTest.run(1, -1, 2, "a", new PreConditionFailure("startIndex (-1) must be between 0 and 1."));
                assertLengthErrorTest.run(1, 2, 2, "a", new PreConditionFailure("startIndex (2) must be between 0 and 1."));
                assertLengthErrorTest.run(1, 3, 2, "a", new PreConditionFailure("startIndex (3) must be between 0 and 1."));
                assertLengthErrorTest.run(-1, 0, 1, "a", new PreConditionFailure("a (-1) must be between 0 and 1."));
                assertLengthErrorTest.run(2, 0, 1, "a", new PreConditionFailure("a (2) must be between 0 and 1."));
                assertLengthErrorTest.run(1, 0, 0, "a", new PreConditionFailure("a (1) must be equal to 0."));

                final Action4<Integer,Integer,Integer,String> assertLengthTest = (Integer length, Integer startIndex, Integer arrayLength, String variableName) ->
                {
                    runner.test("with " + English.andList(length, startIndex, arrayLength, Strings.escapeAndQuote(variableName)), (Test test) ->
                    {
                        PreCondition.assertLength(length, startIndex, arrayLength, variableName);
                    });
                };

                assertLengthTest.run(0, 0, 0, "a");
                assertLengthTest.run(1, 1, 3, "a");
            });

            runner.testGroup("assertLength(long,long,int)", () ->
            {
                final Action4<Long,Long,Long,Throwable> assertLengthErrorTest = (Long length, Long startIndex, Long arrayLength, Throwable expected) ->
                {
                    runner.test("with " + English.andList(length, startIndex, arrayLength), (Test test) ->
                    {
                        test.assertThrows(() -> PreCondition.assertLength(length, startIndex, arrayLength), expected);
                    });
                };

                assertLengthErrorTest.run(1L, 1L, -1L, new PreConditionFailure("arrayLength (-1) must be greater than or equal to 0."));
                assertLengthErrorTest.run(1L, -1L, 1L, new PreConditionFailure("startIndex (-1) must be equal to 0."));
                assertLengthErrorTest.run(1L, -1L, 2L, new PreConditionFailure("startIndex (-1) must be between 0 and 1."));
                assertLengthErrorTest.run(1L, 2L, 2L, new PreConditionFailure("startIndex (2) must be between 0 and 1."));
                assertLengthErrorTest.run(1L, 3L, 2L, new PreConditionFailure("startIndex (3) must be between 0 and 1."));
                assertLengthErrorTest.run(-1L, 0L, 1L, new PreConditionFailure("length (-1) must be between 0 and 1."));
                assertLengthErrorTest.run(2L, 0L, 1L, new PreConditionFailure("length (2) must be between 0 and 1."));
                assertLengthErrorTest.run(1L, 0L, 0L, new PreConditionFailure("length (1) must be equal to 0."));

                final Action3<Long,Long,Long> assertLengthTest = (Long length, Long startIndex, Long arrayLength) ->
                {
                    runner.test("with " + English.andList(length, startIndex, arrayLength), (Test test) ->
                    {
                        PreCondition.assertLength(length, startIndex, arrayLength);
                    });
                };

                assertLengthTest.run(0L, 0L, 0L);
                assertLengthTest.run(3L, 0L, 3L);
                assertLengthTest.run(1L, 1L, 3L);
                assertLengthTest.run(2L, 1L, 3L);
            });

            runner.testGroup("assertLength(long,long,long,String)", () ->
            {
                final Action5<Long,Long,Long,String,Throwable> assertLengthErrorTest = (Long length, Long startIndex, Long arrayLength, String variableName, Throwable expected) ->
                {
                    runner.test("with " + English.andList(length, startIndex, arrayLength, Strings.escapeAndQuote(variableName)), (Test test) ->
                    {
                        test.assertThrows(() -> PreCondition.assertLength(length, startIndex, arrayLength, variableName), expected);
                    });
                };

                assertLengthErrorTest.run(1L, 1L, 2L, null, new PreConditionFailure("expressionName cannot be null."));
                assertLengthErrorTest.run(1L, 1L, 2L, "", new PreConditionFailure("expressionName cannot be empty."));
                assertLengthErrorTest.run(1L, 1L, -1L, "a", new PreConditionFailure("arrayLength (-1) must be greater than or equal to 0."));
                assertLengthErrorTest.run(1L, -1L, 1L, "a", new PreConditionFailure("startIndex (-1) must be equal to 0."));
                assertLengthErrorTest.run(1L, -1L, 2L, "a", new PreConditionFailure("startIndex (-1) must be between 0 and 1."));
                assertLengthErrorTest.run(1L, 2L, 2L, "a", new PreConditionFailure("startIndex (2) must be between 0 and 1."));
                assertLengthErrorTest.run(1L, 3L, 2L, "a", new PreConditionFailure("startIndex (3) must be between 0 and 1."));
                assertLengthErrorTest.run(-1L, 0L, 1L, "a", new PreConditionFailure("a (-1) must be between 0 and 1."));
                assertLengthErrorTest.run(2L, 0L, 1L, "a", new PreConditionFailure("a (2) must be between 0 and 1."));
                assertLengthErrorTest.run(1L, 0L, 0L, "a", new PreConditionFailure("a (1) must be equal to 0."));

                final Action4<Long,Long,Long,String> assertLengthTest = (Long length, Long startIndex, Long arrayLength, String variableName) ->
                {
                    runner.test("with " + English.andList(length, startIndex, arrayLength, Strings.escapeAndQuote(variableName)), (Test test) ->
                    {
                        PreCondition.assertLength(length, startIndex, arrayLength, variableName);
                    });
                };

                assertLengthTest.run(0L, 0L, 0L, "a");
                assertLengthTest.run(1L, 1L, 3L, "a");
            });

            runner.testGroup("assertNonEmptyLength(int,int,int)", () ->
            {
                final Action4<Integer,Integer,Integer,Throwable> assertNonEmptyLengthErrorTest = (Integer length, Integer startIndex, Integer arrayLength, Throwable expected) ->
                {
                    runner.test("with " + English.andList(length, startIndex, arrayLength), (Test test) ->
                    {
                        test.assertThrows(() -> PreCondition.assertNonEmptyLength(length, startIndex, arrayLength), expected);
                    });
                };

                assertNonEmptyLengthErrorTest.run(1, 5, -1, new PreConditionFailure("arrayLength (-1) must be greater than or equal to 1."));
                assertNonEmptyLengthErrorTest.run(1, 1, 0, new PreConditionFailure("arrayLength (0) must be greater than or equal to 1."));
                assertNonEmptyLengthErrorTest.run(1, 1, 1, new PreConditionFailure("startIndex (1) must be equal to 0."));
                assertNonEmptyLengthErrorTest.run(1, -1, 5, new PreConditionFailure("startIndex (-1) must be between 0 and 4."));
                assertNonEmptyLengthErrorTest.run(1, 5, 5, new PreConditionFailure("startIndex (5) must be between 0 and 4."));

                final Action3<Integer,Integer,Integer> assertNonEmptyLengthTest = (Integer length, Integer startIndex, Integer arrayLength) ->
                {
                    runner.test("with " + English.andList(length, startIndex, arrayLength), (Test test) ->
                    {
                        PreCondition.assertNonEmptyLength(length, startIndex, arrayLength);
                    });
                };

                assertNonEmptyLengthTest.run(1, 0, 1);
                assertNonEmptyLengthTest.run(1, 1, 3);
            });

            runner.testGroup("assertNonEmptyLength(int,int,int,String)", () ->
            {
                final Action5<Integer,Integer,Integer,String,Throwable> assertNonEmptyLengthErrorTest = (Integer length, Integer startIndex, Integer arrayLength, String variableName, Throwable expected) ->
                {
                    runner.test("with " + English.andList(length, startIndex, arrayLength, Strings.escapeAndQuote(variableName)), (Test test) ->
                    {
                        test.assertThrows(() -> PreCondition.assertNonEmptyLength(length, startIndex, arrayLength, variableName), expected);
                    });
                };

                assertNonEmptyLengthErrorTest.run(1, 0, 1, null, new PreConditionFailure("expressionName cannot be null."));
                assertNonEmptyLengthErrorTest.run(1, 0, 1, "", new PreConditionFailure("expressionName cannot be empty."));
                assertNonEmptyLengthErrorTest.run(1, 5, -1, "a", new PreConditionFailure("arrayLength (-1) must be greater than or equal to 1."));
                assertNonEmptyLengthErrorTest.run(1, 1, 0, "a", new PreConditionFailure("arrayLength (0) must be greater than or equal to 1."));
                assertNonEmptyLengthErrorTest.run(1, 1, 1, "a", new PreConditionFailure("startIndex (1) must be equal to 0."));
                assertNonEmptyLengthErrorTest.run(1, 3, 2, "a", new PreConditionFailure("startIndex (3) must be between 0 and 1."));
                assertNonEmptyLengthErrorTest.run(1, -1, 5, "a", new PreConditionFailure("startIndex (-1) must be between 0 and 4."));
                assertNonEmptyLengthErrorTest.run(2, 4, 5, "a", new PreConditionFailure("a (2) must be equal to 1."));

                final Action4<Integer,Integer,Integer,String> assertNonEmptyLengthTest = (Integer length, Integer startIndex, Integer arrayLength, String variableName) ->
                {
                    runner.test("with " + English.andList(length, startIndex, arrayLength, Strings.escapeAndQuote(variableName)), (Test test) ->
                    {
                        PreCondition.assertNonEmptyLength(length, startIndex, arrayLength, variableName);
                    });
                };

                assertNonEmptyLengthTest.run(1, 0, 1, "a");
                assertNonEmptyLengthTest.run(1, 1, 3, "a");
            });

            runner.testGroup("assertStartsWith(String,String,String)", () ->
            {
                final Action4<String,String,String,Throwable> assertStartsWithErrorTest = (String value, String prefix, String expressionName, Throwable expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(value) + ", " + Strings.escapeAndQuote(prefix) + ", and " + Strings.escapeAndQuote(expressionName), (Test test) ->
                    {
                        test.assertThrows(() -> PreCondition.assertStartsWith(value, prefix, expressionName),
                            expected);
                    });
                };

                assertStartsWithErrorTest.run(null, null, null, new PreConditionFailure("Expected null (null) to start with null."));
                assertStartsWithErrorTest.run("", "", "", new PreConditionFailure("Expected  (\"\") to start with \"\"."));
                assertStartsWithErrorTest.run("a", "b", "c", new PreConditionFailure("Expected c (\"a\") to start with \"b\"."));
                assertStartsWithErrorTest.run("hello", "H", "myVariable", new PreConditionFailure("Expected myVariable (\"hello\") to start with \"H\"."));

                final Action3<String,String,String> assertStartsWithTest = (String value, String prefix, String expressionName) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(value) + ", " + Strings.escapeAndQuote(prefix) + ", and " + Strings.escapeAndQuote(expressionName), (Test test) ->
                    {
                        PreCondition.assertStartsWith(value, prefix, expressionName);
                    });
                };

                assertStartsWithTest.run("hello", "h", "myVariable");
                assertStartsWithTest.run("hello", "hel", "myVariable");
                assertStartsWithTest.run("hello", "hello", "myVariable");
            });

            runner.testGroup("assertStartsWith(String,String,CharacterComparer,String)", () ->
            {
                final Action5<String,String,CharacterComparer,String,Throwable> assertStartsWithErrorTest = (String value, String prefix, CharacterComparer comparer, String expressionName, Throwable expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(value) + ", " + Strings.escapeAndQuote(prefix) + ", and " + Strings.escapeAndQuote(expressionName), (Test test) ->
                    {
                        test.assertThrows(() -> PreCondition.assertStartsWith(value, prefix, comparer, expressionName),
                            expected);
                    });
                };

                assertStartsWithErrorTest.run("a", "b", null, "c", new PreConditionFailure("characterComparer cannot be null."));
                assertStartsWithErrorTest.run(null, null, CharacterComparer.CaseInsensitive, null, new PreConditionFailure("Expected null (null) to start with null."));
                assertStartsWithErrorTest.run("", "", CharacterComparer.CaseInsensitive, "", new PreConditionFailure("Expected  (\"\") to start with \"\"."));
                assertStartsWithErrorTest.run("a", "b", CharacterComparer.CaseInsensitive, "c", new PreConditionFailure("Expected c (\"a\") to start with \"b\"."));
                assertStartsWithErrorTest.run("hello", "H", CharacterComparer.Exact, "myVariable", new PreConditionFailure("Expected myVariable (\"hello\") to start with \"H\"."));

                final Action4<String,String,CharacterComparer,String> assertStartsWithTest = (String value, String prefix, CharacterComparer comparer, String expressionName) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(value) + ", " + Strings.escapeAndQuote(prefix) + ", and " + Strings.escapeAndQuote(expressionName), (Test test) ->
                    {
                        PreCondition.assertStartsWith(value, prefix, comparer, expressionName);
                    });
                };

                assertStartsWithTest.run("hello", "h", CharacterComparer.Exact, "myVariable");
                assertStartsWithTest.run("hello", "hel", CharacterComparer.Exact, "myVariable");
                assertStartsWithTest.run("hello", "hello", CharacterComparer.Exact, "myVariable");

                assertStartsWithTest.run("hello", "h", CharacterComparer.CaseInsensitive, "myVariable");
                assertStartsWithTest.run("hello", "hel", CharacterComparer.CaseInsensitive, "myVariable");
                assertStartsWithTest.run("hello", "hello", CharacterComparer.CaseInsensitive, "myVariable");
                assertStartsWithTest.run("hello", "H", CharacterComparer.CaseInsensitive, "myVariable");
                assertStartsWithTest.run("hello", "HEL", CharacterComparer.CaseInsensitive, "myVariable");
                assertStartsWithTest.run("hello", "HELLO", CharacterComparer.CaseInsensitive, "myVariable");
            });

            runner.testGroup("assertEndsWith(String,String,String)", () ->
            {
                final Action4<String,String,String,Throwable> assertEndsWithErrorTest = (String value, String prefix, String expressionName, Throwable expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(value) + ", " + Strings.escapeAndQuote(prefix) + ", and " + Strings.escapeAndQuote(expressionName), (Test test) ->
                    {
                        test.assertThrows(() -> PreCondition.assertEndsWith(value, prefix, expressionName),
                            expected);
                    });
                };

                assertEndsWithErrorTest.run(null, null, null, new PreConditionFailure("text cannot be null."));
                assertEndsWithErrorTest.run("", "", "", new PreConditionFailure("suffix cannot be empty."));
                assertEndsWithErrorTest.run("a", "b", "c", new PreConditionFailure("Expected c (\"a\") to end with \"b\"."));
                assertEndsWithErrorTest.run("hello", "O", "myVariable", new PreConditionFailure("Expected myVariable (\"hello\") to end with \"O\"."));

                final Action3<String,String,String> assertEndsWithTest = (String value, String prefix, String expressionName) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(value) + ", " + Strings.escapeAndQuote(prefix) + ", and " + Strings.escapeAndQuote(expressionName), (Test test) ->
                    {
                        PreCondition.assertEndsWith(value, prefix, expressionName);
                    });
                };

                assertEndsWithTest.run("hello", "o", "myVariable");
                assertEndsWithTest.run("hello", "llo", "myVariable");
                assertEndsWithTest.run("hello", "hello", "myVariable");
            });
        });
    }
}
