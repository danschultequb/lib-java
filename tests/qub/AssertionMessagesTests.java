package qub;

public interface AssertionMessagesTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(AssertionMessages.class, () ->
        {
            runner.testGroup("nullMessage(String)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertEqual("null must be null.", AssertionMessages.nullMessage(null));
                });

                runner.test("with empty", (Test test) ->
                {
                    test.assertEqual(" must be null.", AssertionMessages.nullMessage(""));
                });

                runner.test("with non-empty", (Test test) ->
                {
                    test.assertEqual("abc must be null.", AssertionMessages.nullMessage("abc"));
                });
            });

            runner.testGroup("notNull(String)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertEqual("null cannot be null.", AssertionMessages.notNull(null));
                });

                runner.test("with empty", (Test test) ->
                {
                    test.assertEqual(" cannot be null.", AssertionMessages.notNull(""));
                });

                runner.test("with non-empty", (Test test) ->
                {
                    test.assertEqual("abc cannot be null.", AssertionMessages.notNull("abc"));
                });
            });

            runner.testGroup("notEmpty(String)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertEqual("null cannot be empty.", AssertionMessages.notEmpty(null));
                });

                runner.test("with empty", (Test test) ->
                {
                    test.assertEqual(" cannot be empty.", AssertionMessages.notEmpty(""));
                });

                runner.test("with non-empty", (Test test) ->
                {
                    test.assertEqual("abc cannot be empty.", AssertionMessages.notEmpty("abc"));
                });
            });

            runner.testGroup("nullOrNotEmpty(String,String)", () ->
            {
                runner.test("with null value and null expressionName", (Test test) ->
                {
                    test.assertEqual("null (null) must be either null or not empty.", AssertionMessages.nullOrNotEmpty(null, null));
                });

                runner.test("with null value and empty expressionName", (Test test) ->
                {
                    test.assertEqual(" (null) must be either null or not empty.", AssertionMessages.nullOrNotEmpty(null, ""));
                });

                runner.test("with null value and non-empty expressionName", (Test test) ->
                {
                    test.assertEqual("hello (null) must be either null or not empty.", AssertionMessages.nullOrNotEmpty(null, "hello"));
                });

                runner.test("with empty value and null expressionName", (Test test) ->
                {
                    test.assertEqual("null (\"\") must be either null or not empty.", AssertionMessages.nullOrNotEmpty("", null));
                });

                runner.test("with empty value and empty expressionName", (Test test) ->
                {
                    test.assertEqual(" (\"\") must be either null or not empty.", AssertionMessages.nullOrNotEmpty("", ""));
                });

                runner.test("with empty value and non-empty expressionName", (Test test) ->
                {
                    test.assertEqual("hello (\"\") must be either null or not empty.", AssertionMessages.nullOrNotEmpty("", "hello"));
                });

                runner.test("with non-empty value and null expressionName", (Test test) ->
                {
                    test.assertEqual("null (\"abc\") must be either null or not empty.", AssertionMessages.nullOrNotEmpty("abc", null));
                });

                runner.test("with non-empty value and empty expressionName", (Test test) ->
                {
                    test.assertEqual(" (\"abc\") must be either null or not empty.", AssertionMessages.nullOrNotEmpty("abc", ""));
                });

                runner.test("with non-empty value and non-empty expressionName", (Test test) ->
                {
                    test.assertEqual("hello (\"abc\") must be either null or not empty.", AssertionMessages.nullOrNotEmpty("abc", "hello"));
                });
            });

            runner.testGroup("same(T,T,String)", () ->
            {
                runner.test("with null expectedValue", (Test test) ->
                {
                    test.assertEqual("abc (5) must be the same object as null.", AssertionMessages.same(null, 5, "abc"));
                });

                runner.test("with empty expectedValue", (Test test) ->
                {
                    test.assertEqual("abc (5) must be the same object as .", AssertionMessages.same("", 5, "abc"));
                });

                runner.test("with null actualValue", (Test test) ->
                {
                    test.assertEqual("abc (null) must be the same object as 7.", AssertionMessages.same(7, null, "abc"));
                });

                runner.test("with empty actualValue", (Test test) ->
                {
                    test.assertEqual("abc () must be the same object as 7.", AssertionMessages.same(7, "", "abc"));
                });

                runner.test("with null expressionName", (Test test) ->
                {
                    test.assertEqual("null (5) must be the same object as 7.", AssertionMessages.same(7, 5, null));
                });

                runner.test("with empty expressionName", (Test test) ->
                {
                    test.assertEqual(" (5) must be the same object as 7.", AssertionMessages.same(7, 5, ""));
                });

                runner.test("with non-empty expressionName", (Test test) ->
                {
                    test.assertEqual("abc (5) must be the same object as 7.", AssertionMessages.same(7, 5, "abc"));
                });
            });

            runner.testGroup("equal(T,T,String)", () ->
            {
                runner.test("with null expectedValue", (Test test) ->
                {
                    test.assertEqual("abc (5) must be null.", AssertionMessages.equal(null, 5, "abc"));
                });

                runner.test("with empty expectedValue", (Test test) ->
                {
                    test.assertEqual("abc (5) must be .", AssertionMessages.equal("", 5, "abc"));
                });

                runner.test("with null actualValue", (Test test) ->
                {
                    test.assertEqual("abc (null) must be 7.", AssertionMessages.equal(7, null, "abc"));
                });

                runner.test("with empty actualValue", (Test test) ->
                {
                    test.assertEqual("abc () must be 7.", AssertionMessages.equal(7, "", "abc"));
                });

                runner.test("with null expressionName", (Test test) ->
                {
                    test.assertEqual("null (5) must be 7.", AssertionMessages.equal(7, 5, null));
                });

                runner.test("with empty expressionName", (Test test) ->
                {
                    test.assertEqual(" (5) must be 7.", AssertionMessages.equal(7, 5, ""));
                });

                runner.test("with non-empty expressionName", (Test test) ->
                {
                    test.assertEqual("abc (5) must be 7.", AssertionMessages.equal(7, 5, "abc"));
                });
            });

            runner.testGroup("notEqual(T,T,String)", () ->
            {
                runner.test("with null expectedValue", (Test test) ->
                {
                    test.assertEqual("abc (5) must not be null.", AssertionMessages.notEqual(null, 5, "abc"));
                });

                runner.test("with empty expectedValue", (Test test) ->
                {
                    test.assertEqual("abc (5) must not be .", AssertionMessages.notEqual("", 5, "abc"));
                });

                runner.test("with null actualValue", (Test test) ->
                {
                    test.assertEqual("abc (null) must not be 7.", AssertionMessages.notEqual(7, null, "abc"));
                });

                runner.test("with empty actualValue", (Test test) ->
                {
                    test.assertEqual("abc () must not be 7.", AssertionMessages.notEqual(7, "", "abc"));
                });

                runner.test("with null expressionName", (Test test) ->
                {
                    test.assertEqual("null (5) must not be 7.", AssertionMessages.notEqual(7, 5, null));
                });

                runner.test("with empty expressionName", (Test test) ->
                {
                    test.assertEqual(" (5) must not be 7.", AssertionMessages.notEqual(7, 5, ""));
                });

                runner.test("with non-empty expressionName", (Test test) ->
                {
                    test.assertEqual("abc (5) must not be 7.", AssertionMessages.notEqual(7, 5, "abc"));
                });
            });

            runner.testGroup("lessThan(T,T,String)", () ->
            {
                runner.test("with null expectedValue", (Test test) ->
                {
                    test.assertEqual("abc (null) must be less than 5.", AssertionMessages.lessThan(null, 5, "abc"));
                });

                runner.test("with empty expectedValue", (Test test) ->
                {
                    test.assertEqual("abc () must be less than 5.", AssertionMessages.lessThan("", 5, "abc"));
                });

                runner.test("with null actualValue", (Test test) ->
                {
                    test.assertEqual("abc (7) must be less than null.", AssertionMessages.lessThan(7, null, "abc"));
                });

                runner.test("with empty actualValue", (Test test) ->
                {
                    test.assertEqual("abc (7) must be less than .", AssertionMessages.lessThan(7, "", "abc"));
                });

                runner.test("with null expressionName", (Test test) ->
                {
                    test.assertEqual("null (7) must be less than 5.", AssertionMessages.lessThan(7, 5, null));
                });

                runner.test("with empty expressionName", (Test test) ->
                {
                    test.assertEqual(" (7) must be less than 5.", AssertionMessages.lessThan(7, 5, ""));
                });

                runner.test("with non-empty expressionName", (Test test) ->
                {
                    test.assertEqual("abc (7) must be less than 5.", AssertionMessages.lessThan(7, 5, "abc"));
                });
            });

            runner.testGroup("lessThanOrEqualTo(T,T,String)", () ->
            {
                runner.test("with null expectedValue", (Test test) ->
                {
                    test.assertEqual("abc (null) must be less than or equal to 5.", AssertionMessages.lessThanOrEqualTo(null, 5, "abc"));
                });

                runner.test("with empty expectedValue", (Test test) ->
                {
                    test.assertEqual("abc () must be less than or equal to 5.", AssertionMessages.lessThanOrEqualTo("", 5, "abc"));
                });

                runner.test("with null actualValue", (Test test) ->
                {
                    test.assertEqual("abc (7) must be less than or equal to null.", AssertionMessages.lessThanOrEqualTo(7, null, "abc"));
                });

                runner.test("with empty actualValue", (Test test) ->
                {
                    test.assertEqual("abc (7) must be less than or equal to .", AssertionMessages.lessThanOrEqualTo(7, "", "abc"));
                });

                runner.test("with null expressionName", (Test test) ->
                {
                    test.assertEqual("null (7) must be less than or equal to 5.", AssertionMessages.lessThanOrEqualTo(7, 5, null));
                });

                runner.test("with empty expressionName", (Test test) ->
                {
                    test.assertEqual(" (7) must be less than or equal to 5.", AssertionMessages.lessThanOrEqualTo(7, 5, ""));
                });

                runner.test("with non-empty expressionName", (Test test) ->
                {
                    test.assertEqual("abc (7) must be less than or equal to 5.", AssertionMessages.lessThanOrEqualTo(7, 5, "abc"));
                });
            });

            runner.testGroup("nullOrGreaterThanOrEqualTo(T,T,String)", () ->
            {
                runner.test("with null expectedValue", (Test test) ->
                {
                    test.assertEqual("abc (null) must be null or greater than or equal to 5.", AssertionMessages.nullOrGreaterThanOrEqualTo(null, 5, "abc"));
                });

                runner.test("with empty expectedValue", (Test test) ->
                {
                    test.assertEqual("abc () must be null or greater than or equal to 5.", AssertionMessages.nullOrGreaterThanOrEqualTo("", 5, "abc"));
                });

                runner.test("with null actualValue", (Test test) ->
                {
                    test.assertEqual("abc (7) must be null or greater than or equal to null.", AssertionMessages.nullOrGreaterThanOrEqualTo(7, null, "abc"));
                });

                runner.test("with empty actualValue", (Test test) ->
                {
                    test.assertEqual("abc (7) must be null or greater than or equal to .", AssertionMessages.nullOrGreaterThanOrEqualTo(7, "", "abc"));
                });

                runner.test("with null expressionName", (Test test) ->
                {
                    test.assertEqual("null (7) must be null or greater than or equal to 5.", AssertionMessages.nullOrGreaterThanOrEqualTo(7, 5, null));
                });

                runner.test("with empty expressionName", (Test test) ->
                {
                    test.assertEqual(" (7) must be null or greater than or equal to 5.", AssertionMessages.nullOrGreaterThanOrEqualTo(7, 5, ""));
                });

                runner.test("with non-empty expressionName", (Test test) ->
                {
                    test.assertEqual("abc (7) must be null or greater than or equal to 5.", AssertionMessages.nullOrGreaterThanOrEqualTo(7, 5, "abc"));
                });
            });

            runner.testGroup("nullOrGreaterThan(T,T,String)", () ->
            {
                runner.test("with null expectedValue", (Test test) ->
                {
                    test.assertEqual("abc (null) must be null or greater than 5.", AssertionMessages.nullOrGreaterThan(null, 5, "abc"));
                });

                runner.test("with empty expectedValue", (Test test) ->
                {
                    test.assertEqual("abc () must be null or greater than 5.", AssertionMessages.nullOrGreaterThan("", 5, "abc"));
                });

                runner.test("with null actualValue", (Test test) ->
                {
                    test.assertEqual("abc (7) must be null or greater than null.", AssertionMessages.nullOrGreaterThan(7, null, "abc"));
                });

                runner.test("with empty actualValue", (Test test) ->
                {
                    test.assertEqual("abc (7) must be null or greater than .", AssertionMessages.nullOrGreaterThan(7, "", "abc"));
                });

                runner.test("with null expressionName", (Test test) ->
                {
                    test.assertEqual("null (7) must be null or greater than 5.", AssertionMessages.nullOrGreaterThan(7, 5, null));
                });

                runner.test("with empty expressionName", (Test test) ->
                {
                    test.assertEqual(" (7) must be null or greater than 5.", AssertionMessages.nullOrGreaterThan(7, 5, ""));
                });

                runner.test("with non-empty expressionName", (Test test) ->
                {
                    test.assertEqual("abc (7) must be null or greater than 5.", AssertionMessages.nullOrGreaterThan(7, 5, "abc"));
                });
            });

            runner.testGroup("greaterThanOrEqualTo(T,T,String)", () ->
            {
                runner.test("with null expectedValue", (Test test) ->
                {
                    test.assertEqual("abc (null) must be greater than or equal to 5.", AssertionMessages.greaterThanOrEqualTo(null, 5, "abc"));
                });

                runner.test("with empty expectedValue", (Test test) ->
                {
                    test.assertEqual("abc () must be greater than or equal to 5.", AssertionMessages.greaterThanOrEqualTo("", 5, "abc"));
                });

                runner.test("with null actualValue", (Test test) ->
                {
                    test.assertEqual("abc (7) must be greater than or equal to null.", AssertionMessages.greaterThanOrEqualTo(7, null, "abc"));
                });

                runner.test("with empty actualValue", (Test test) ->
                {
                    test.assertEqual("abc (7) must be greater than or equal to .", AssertionMessages.greaterThanOrEqualTo(7, "", "abc"));
                });

                runner.test("with null expressionName", (Test test) ->
                {
                    test.assertEqual("null (7) must be greater than or equal to 5.", AssertionMessages.greaterThanOrEqualTo(7, 5, null));
                });

                runner.test("with empty expressionName", (Test test) ->
                {
                    test.assertEqual(" (7) must be greater than or equal to 5.", AssertionMessages.greaterThanOrEqualTo(7, 5, ""));
                });

                runner.test("with non-empty expressionName", (Test test) ->
                {
                    test.assertEqual("abc (7) must be greater than or equal to 5.", AssertionMessages.greaterThanOrEqualTo(7, 5, "abc"));
                });
            });

            runner.testGroup("greaterThan(T,T,String)", () ->
            {
                runner.test("with null expectedValue", (Test test) ->
                {
                    test.assertEqual("abc (null) must be greater than 5.", AssertionMessages.greaterThan(null, 5, "abc"));
                });

                runner.test("with empty expectedValue", (Test test) ->
                {
                    test.assertEqual("abc () must be greater than 5.", AssertionMessages.greaterThan("", 5, "abc"));
                });

                runner.test("with null actualValue", (Test test) ->
                {
                    test.assertEqual("abc (7) must be greater than null.", AssertionMessages.greaterThan(7, null, "abc"));
                });

                runner.test("with empty actualValue", (Test test) ->
                {
                    test.assertEqual("abc (7) must be greater than .", AssertionMessages.greaterThan(7, "", "abc"));
                });

                runner.test("with null expressionName", (Test test) ->
                {
                    test.assertEqual("null (7) must be greater than 5.", AssertionMessages.greaterThan(7, 5, null));
                });

                runner.test("with empty expressionName", (Test test) ->
                {
                    test.assertEqual(" (7) must be greater than 5.",
                        AssertionMessages.greaterThan(7, 5, ""));
                });

                runner.test("with non-empty expressionName", (Test test) ->
                {
                    test.assertEqual("abc (7) must be greater than 5.",
                        AssertionMessages.greaterThan(7, 5, "abc"));
                });
            });

            runner.testGroup("between(T,T,T,String)", () ->
            {
                runner.test("with null lowerBound", (Test test) ->
                {
                    test.assertEqual("blah (1) must be between null and 2.", AssertionMessages.between(null, 1, 2, "blah"));
                });

                runner.test("with empty lowerBound", (Test test) ->
                {
                    test.assertEqual("blah (1) must be between  and 2.", AssertionMessages.between("", 1, 2, "blah"));
                });

                runner.test("with null value", (Test test) ->
                {
                    test.assertEqual("blah (null) must be between 0 and 2.", AssertionMessages.between(0, null, 2, "blah"));
                });

                runner.test("with empty value", (Test test) ->
                {
                    test.assertEqual("blah () must be between 0 and 2.", AssertionMessages.between(0, "", 2, "blah"));
                });

                runner.test("with null upperBound", (Test test) ->
                {
                    test.assertEqual("blah (1) must be between 0 and null.", AssertionMessages.between(0, 1, null, "blah"));
                });

                runner.test("with empty upperBound", (Test test) ->
                {
                    test.assertEqual("blah (1) must be between 0 and .", AssertionMessages.between(0, 1, "", "blah"));
                });

                runner.test("with null expressionName", (Test test) ->
                {
                    test.assertEqual("null (1) must be between 0 and 2.", AssertionMessages.between(0, 1, 2, null));
                });

                runner.test("with empty expressionName", (Test test) ->
                {
                    test.assertEqual(" (1) must be between 0 and 2.", AssertionMessages.between(0, 1, 2, ""));
                });

                runner.test("with non-empty expressionName", (Test test) ->
                {
                    test.assertEqual("blah (1) must be between 0 and 2.", AssertionMessages.between(0, 1, 2, "blah"));
                });
            });

            runner.testGroup("startsWith(String,String,String)", () ->
            {
                final Action4<String,String,String,String> startsWithTest = (String value, String prefix, String expressionName, String expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(value) + ", " + Strings.escapeAndQuote(prefix) + ", and " + Strings.escapeAndQuote(expressionName), (Test test) ->
                    {
                        test.assertEqual(expected, AssertionMessages.startsWith(value, prefix, expressionName));
                    });
                };

                startsWithTest.run(null, null, null, "Expected null (null) to start with null.");
                startsWithTest.run("", "", "", "Expected  (\"\") to start with \"\".");
                startsWithTest.run("a", "b", "c", "Expected c (\"a\") to start with \"b\".");
            });

            runner.testGroup("endsWith(String,String,String)", () ->
            {
                final Action4<String,String,String,String> endsWithTest = (String value, String prefix, String expressionName, String expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(value) + ", " + Strings.escapeAndQuote(prefix) + ", and " + Strings.escapeAndQuote(expressionName), (Test test) ->
                    {
                        test.assertEqual(expected, AssertionMessages.endsWith(value, prefix, expressionName));
                    });
                };

                endsWithTest.run(null, null, null, "Expected null (null) to end with null.");
                endsWithTest.run("", "", "", "Expected  (\"\") to end with \"\".");
                endsWithTest.run("a", "b", "c", "Expected c (\"a\") to end with \"b\".");
            });

            runner.testGroup("containsOnly(String,char[],String)", () ->
            {
                runner.test("with null value", (Test test) ->
                {
                    test.assertEqual("blah (null) must contain only ['a','b'].", AssertionMessages.containsOnly(null, new char[] { 'a', 'b' }, "blah"));
                });

                runner.test("with empty value", (Test test) ->
                {
                    test.assertEqual("blah () must contain only ['a','b'].", AssertionMessages.containsOnly("", new char[] { 'a', 'b' }, "blah"));
                });

                runner.test("with null characters", (Test test) ->
                {
                    test.assertEqual("blah (hello) must contain only null.", AssertionMessages.containsOnly("hello", null, "blah"));
                });

                runner.test("with empty characters", (Test test) ->
                {
                    test.assertEqual("blah (hello) must contain only [].", AssertionMessages.containsOnly("hello", new char[0], "blah"));
                });

                runner.test("with null expressionName", (Test test) ->
                {
                    test.assertEqual("null (hello) must contain only ['a','b'].", AssertionMessages.containsOnly("hello", new char[] { 'a', 'b' }, null));
                });

                runner.test("with empty expressionName", (Test test) ->
                {
                    test.assertEqual(" (hello) must contain only ['a','b'].", AssertionMessages.containsOnly("hello", new char[] { 'a', 'b' }, ""));
                });

                runner.test("with non-empty expressionName", (Test test) ->
                {
                    test.assertEqual("blah (hello) must contain only ['a','b'].", AssertionMessages.containsOnly("hello", new char[] { 'a', 'b' }, "blah"));
                });
            });

            runner.testGroup("instanceOf(Object,Class<?>,String)", () ->
            {
                runner.test("with null value", (Test test) ->
                {
                    test.assertThrows(() -> AssertionMessages.instanceOf(null, String.class, "blah"),
                        new PreConditionFailure("value cannot be null."));
                });

                runner.test("with empty value", (Test test) ->
                {
                    test.assertEqual("blah (java.lang.String) must be of type qub.IntegerValue.",
                        AssertionMessages.instanceOf("", IntegerValue.class, "blah"));
                });

                runner.test("with null type", (Test test) ->
                {
                    test.assertThrows(() -> AssertionMessages.instanceOf(20, null, "blah"),
                        new PreConditionFailure("type cannot be null."));
                });

                runner.test("with null expressionName", (Test test) ->
                {
                    test.assertEqual("null (java.lang.String) must be of type qub.IntegerValue.",
                        AssertionMessages.instanceOf("abc", IntegerValue.class, null));
                });

                runner.test("with empty expressionName", (Test test) ->
                {
                    test.assertEqual(" (java.lang.String) must be of type qub.IntegerValue.",
                        AssertionMessages.instanceOf("abc", IntegerValue.class, ""));
                });

                runner.test("with non-empty expressionName", (Test test) ->
                {
                    test.assertEqual("blah (java.lang.String) must be of type qub.IntegerValue.",
                        AssertionMessages.instanceOf("abc", IntegerValue.class, "blah"));
                });
            });

            runner.testGroup("oneOf(char,char[],String)", () ->
            {
                runner.test("with null values", (Test test) ->
                {
                    test.assertThrows(new java.lang.NullPointerException(),
                        () -> AssertionMessages.oneOf('a', (char[])null, "blah"));
                });

                runner.test("with empty values", (Test test) ->
                {
                    test.assertThrows(new java.lang.ArrayIndexOutOfBoundsException("-1"),
                        () -> AssertionMessages.oneOf('a', new char[0], "blah"));
                });

                runner.test("with one-element values", (Test test) ->
                {
                    test.assertEqual("blah (a) must be either or z.",
                        AssertionMessages.oneOf('a', new char[] { 'z' }, "blah"));
                });

                runner.test("with two-element values", (Test test) ->
                {
                    test.assertEqual("blah (a) must be either y or z.",
                        AssertionMessages.oneOf('a', new char[] { 'y', 'z' }, "blah"));
                });

                runner.test("with null expressionName", (Test test) ->
                {
                    test.assertEqual("null (a) must be either x, y, or z.",
                        AssertionMessages.oneOf('a', new char[] { 'x', 'y', 'z' }, null));
                });

                runner.test("with empty expressionName", (Test test) ->
                {
                    test.assertEqual(" (a) must be either x, y, or z.",
                        AssertionMessages.oneOf('a', new char[] { 'x', 'y', 'z' }, ""));
                });

                runner.test("with non-empty expressionName", (Test test) ->
                {
                    test.assertEqual("blah (a) must be either x, y, or z.",
                        AssertionMessages.oneOf('a', new char[] { 'x', 'y', 'z' }, "blah"));
                });
            });

            runner.testGroup("oneOf(int,int[],String)", () ->
            {
                runner.test("with null values", (Test test) ->
                {
                    test.assertThrows(new java.lang.NullPointerException(),
                        () -> AssertionMessages.oneOf(5, (int[])null, "blah"));
                });

                runner.test("with empty values", (Test test) ->
                {
                    test.assertThrows(new java.lang.ArrayIndexOutOfBoundsException("-1"),
                        () -> AssertionMessages.oneOf(5, new int[0], "blah"));
                });

                runner.test("with one-element values", (Test test) ->
                {
                    test.assertEqual("blah (5) must be either or 6.",
                        AssertionMessages.oneOf(5, new int[] { 6 }, "blah"));
                });

                runner.test("with two-element values", (Test test) ->
                {
                    test.assertEqual("blah (5) must be either 6 or 7.",
                        AssertionMessages.oneOf(5, new int[] { 6, 7 }, "blah"));
                });

                runner.test("with null expressionName", (Test test) ->
                {
                    test.assertEqual("null (5) must be either 6, 7, or 8.",
                        AssertionMessages.oneOf(5, new int[] { 6, 7, 8 }, null));
                });

                runner.test("with empty expressionName", (Test test) ->
                {
                    test.assertEqual(" (5) must be either 6, 7, or 8.",
                        AssertionMessages.oneOf(5, new int[] { 6, 7, 8 }, ""));
                });

                runner.test("with non-empty expressionName", (Test test) ->
                {
                    test.assertEqual("blah (5) must be either 6, 7, or 8.",
                        AssertionMessages.oneOf(5, new int[] { 6, 7, 8 }, "blah"));
                });
            });

            runner.testGroup("oneOf(long,long[],String)", () ->
            {
                runner.test("with null values", (Test test) ->
                {
                    test.assertThrows(new java.lang.NullPointerException(),
                        () -> AssertionMessages.oneOf(5, (long[])null, "blah"));
                });

                runner.test("with empty values", (Test test) ->
                {
                    test.assertThrows(new java.lang.ArrayIndexOutOfBoundsException("-1"),
                        () -> AssertionMessages.oneOf(5, new long[0], "blah"));
                });

                runner.test("with one-element values", (Test test) ->
                {
                    test.assertEqual("blah (5) must be either or 6.",
                        AssertionMessages.oneOf(5, new long[] { 6 }, "blah"));
                });

                runner.test("with two-element values", (Test test) ->
                {
                    test.assertEqual("blah (5) must be either 6 or 7.",
                        AssertionMessages.oneOf(5, new long[] { 6, 7 }, "blah"));
                });

                runner.test("with null expressionName", (Test test) ->
                {
                    test.assertEqual("null (5) must be either 6, 7, or 8.",
                        AssertionMessages.oneOf(5, new long[] { 6, 7, 8 }, null));
                });

                runner.test("with empty expressionName", (Test test) ->
                {
                    test.assertEqual(" (5) must be either 6, 7, or 8.",
                        AssertionMessages.oneOf(5, new long[] { 6, 7, 8 }, ""));
                });

                runner.test("with non-empty expressionName", (Test test) ->
                {
                    test.assertEqual("blah (5) must be either 6, 7, or 8.",
                        AssertionMessages.oneOf(5, new long[] { 6, 7, 8 }, "blah"));
                });
            });

            runner.testGroup("oneOf(T,T[],String)", () ->
            {
                runner.test("with null values", (Test test) ->
                {
                    test.assertThrows(new NullPointerException(),
                        () -> AssertionMessages.oneOf("a", (String[])null, "blah"));
                });

                runner.test("with empty values", (Test test) ->
                {
                    test.assertThrows(new ArrayIndexOutOfBoundsException("-1"),
                        () -> AssertionMessages.oneOf("a", new String[0], "blah"));
                });

                runner.test("with one-element values", (Test test) ->
                {
                    test.assertEqual("blah (a) must be b.",
                        AssertionMessages.oneOf("a", new String[] { "b" }, "blah"));
                });

                runner.test("with two-element values", (Test test) ->
                {
                    test.assertEqual("blah (a) must be either b or c.",
                        AssertionMessages.oneOf("a", new String[] { "b", "c" }, "blah"));
                });

                runner.test("with null expressionName", (Test test) ->
                {
                    test.assertEqual("null (a) must be either b, c, or d.",
                        AssertionMessages.oneOf("a", new String[] { "b", "c", "d" }, null));
                });

                runner.test("with empty expressionName", (Test test) ->
                {
                    test.assertEqual(" (a) must be either b, c, or d.",
                        AssertionMessages.oneOf("a", new String[] { "b", "c", "d" }, ""));
                });

                runner.test("with non-empty expressionName", (Test test) ->
                {
                    test.assertEqual("blah (a) must be either b, c, or d.",
                        AssertionMessages.oneOf("a", new String[] { "b", "c", "d" }, "blah"));
                });
            });

            runner.testGroup("oneOf(T,Iterable<T>,String)", () ->
            {
                runner.test("with null values", (Test test) ->
                {
                    test.assertThrows(new java.lang.NullPointerException(),
                        () -> AssertionMessages.oneOf("a", (Iterable<String>)null, "blah"));
                });

                runner.test("with empty values", (Test test) ->
                {
                    test.assertEqual("blah (a) must be either or null.",
                        AssertionMessages.oneOf("a", Iterable.create(), "blah"));
                });

                runner.test("with one-element values", (Test test) ->
                {
                    test.assertEqual("blah (a) must be b.",
                        AssertionMessages.oneOf("a", Iterable.create("b"), "blah"));
                });

                runner.test("with two-element values", (Test test) ->
                {
                    test.assertEqual("blah (a) must be either b or c.",
                        AssertionMessages.oneOf("a", Iterable.create("b", "c"), "blah"));
                });

                runner.test("with null expressionName", (Test test) ->
                {
                    test.assertEqual("null (a) must be either b, c, or d.",
                        AssertionMessages.oneOf("a", Iterable.create("b", "c", "d"), null));
                });

                runner.test("with empty expressionName", (Test test) ->
                {
                    test.assertEqual(" (a) must be either b, c, or d.",
                        AssertionMessages.oneOf("a", Iterable.create("b", "c", "d"), ""));
                });

                runner.test("with non-empty expressionName", (Test test) ->
                {
                    test.assertEqual("blah (a) must be either b, c, or d.",
                        AssertionMessages.oneOf("a", Iterable.create("b", "c", "d"), "blah"));
                });
            });
        });
    }
}
