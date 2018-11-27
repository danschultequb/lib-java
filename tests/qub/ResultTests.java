package qub;

public class ResultTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(Result.class, () ->
        {
            runner.testGroup("getErrorType()", () ->
            {
                runner.test("with no error", (Test test) ->
                {
                    test.assertNull(Result.successTrue().getErrorType());
                });

                runner.test("with error", (Test test) ->
                {
                    test.assertEqual(NullPointerException.class, Result.error(new NullPointerException()).getErrorType());
                });
            });

            runner.testGroup("getErrorMessage()", () ->
            {
                runner.test("with no error", (Test test) ->
                {
                    test.assertNull(Result.successTrue().getErrorMessage());
                });

                runner.test("with error", (Test test) ->
                {
                    test.assertEqual("abc", Result.error(new RuntimeException("abc")).getErrorMessage());
                });
            });

            runner.testGroup("throwError()", () ->
            {
                runner.test("with no error", (Test test) ->
                {
                    Result.successTrue().throwError();
                });

                runner.test("with RuntimeException", (Test test) ->
                {
                    test.assertThrows(() -> Result.error(new RuntimeException("abc")).throwError(), new RuntimeException("abc"));
                });

                runner.test("with Exception", (Test test) ->
                {
                    test.assertThrows(() -> Result.error(new Exception("abc")).throwError(), new RuntimeException(new Exception("abc")));
                });
            });

            runner.testGroup("throwErrorOrGetValue()", () ->
            {
                runner.test("with no error", (Test test) ->
                {
                    test.assertTrue(Result.successTrue().throwErrorOrGetValue());
                });

                runner.test("with RuntimeException", (Test test) ->
                {
                    test.assertThrows(() -> Result.error(new RuntimeException("abc")).throwErrorOrGetValue(), new RuntimeException("abc"));
                });

                runner.test("with Exception", (Test test) ->
                {
                    test.assertThrows(() -> Result.error(new Exception("abc")).throwErrorOrGetValue(), new RuntimeException(new Exception("abc")));
                });
            });

            runner.testGroup("convertError()", () ->
            {
                runner.test("with no error", (Test test) ->
                {
                    final Result<Boolean> result = Result.successTrue();
                    final Result<String> convertedResult = result.convertError();
                    test.assertNull(convertedResult);
                });

                runner.test("with no error", (Test test) ->
                {
                    final Result<Boolean> result = Result.error(new RuntimeException("xyz"));
                    final Result<String> convertedResult = result.convertError();
                    test.assertError(new RuntimeException("xyz"), convertedResult);
                });
            });

            runner.testGroup("toString()", () ->
            {
                runner.test("with null value and null error", (Test test) ->
                {
                    test.assertEqual("value: null", Result.done(null, null).toString());
                });

                runner.test("with non-null value and null error", (Test test) ->
                {
                    test.assertEqual("value: 6", Result.done(6, null).toString());
                });

                runner.test("with null value and non-null error", (Test test) ->
                {
                    test.assertEqual("error: java.lang.NullPointerException: oops", Result.done(null, new NullPointerException("oops")).toString());
                });

                runner.test("with non-null value and non-null error", (Test test) ->
                {
                    test.assertEqual("value: 20, error: java.lang.NullPointerException: oops", Result.done(20, new NullPointerException("oops")).toString());
                });
            });

            runner.testGroup("throwError(Class<T>)", () ->
            {
                runner.test("with null exception type", (Test test) ->
                {
                    test.assertThrows(() -> Result.successTrue().throwError(null), new PreConditionFailure("exceptionType cannot be null."));
                });

                runner.test("with non-null exception type but null error", (Test test) ->
                {
                    try
                    {
                        Result.successTrue().throwError(java.io.IOException.class);
                    }
                    catch (java.io.IOException e)
                    {
                        test.fail(e);
                    }
                });

                runner.test("with non-null exception type and same typed error", (Test test) ->
                {
                    try
                    {
                        Result.error(new java.io.IOException("abc")).throwError(java.io.IOException.class);
                        test.fail("Expected java.io.IOException to be thrown.");
                    }
                    catch (java.io.IOException e)
                    {
                        test.assertEqual(new java.io.IOException("abc"), e);
                    }
                });

                runner.test("with non-null exception type and different typed error", (Test test) ->
                {
                    try
                    {
                        Result.error(new NullPointerException("abc")).throwError(java.io.IOException.class);
                        test.fail("Expected NullPointerException to be thrown.");
                    }
                    catch (Throwable e)
                    {
                        test.assertEqual(new NullPointerException("abc"), e);
                    }
                });
            });

            runner.testGroup("equals(Object)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertFalse(Result.successTrue().equals((Object)null));
                });

                runner.test("with non-Result", (Test test) ->
                {
                    test.assertFalse(Result.successTrue().equals((Object)"test"));
                });

                runner.test("with different value", (Test test) ->
                {
                    test.assertFalse(Result.successTrue().equals((Object)Result.successFalse()));
                });

                runner.test("with same Result", (Test test) ->
                {
                    test.assertTrue(Result.successTrue().equals((Object)Result.successTrue()));
                });

                runner.test("with equal value", (Test test) ->
                {
                    test.assertTrue(Result.successTrue().equals((Object)Result.success(true)));
                });

                runner.test("with equal errors", (Test test) ->
                {
                    test.assertTrue(Result.error(new RuntimeException("M")).equals((Object)Result.error(new RuntimeException("M"))));
                });

                runner.test("with same type errors but different messages", (Test test) ->
                {
                    test.assertFalse(Result.error(new RuntimeException("M")).equals((Object)Result.error(new RuntimeException("O"))));
                });

                runner.test("with different type errors but same messages", (Test test) ->
                {
                    test.assertFalse(Result.error(new RuntimeException("M")).equals((Object)Result.error(new Exception("M"))));
                });
            });

            runner.testGroup("equals(Result<T>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertFalse(Result.successTrue().equals((Result<Boolean>)null));
                });

                runner.test("with different value", (Test test) ->
                {
                    test.assertFalse(Result.successTrue().equals(Result.successFalse()));
                });

                runner.test("with same Result", (Test test) ->
                {
                    test.assertTrue(Result.successTrue().equals(Result.successTrue()));
                });

                runner.test("with equal value", (Test test) ->
                {
                    test.assertTrue(Result.successTrue().equals(Result.success(true)));
                });

                runner.test("with equal errors", (Test test) ->
                {
                    test.assertTrue(Result.error(new RuntimeException("M")).equals(Result.error(new RuntimeException("M"))));
                });

                runner.test("with same type errors but different messages", (Test test) ->
                {
                    test.assertFalse(Result.error(new RuntimeException("M")).equals(Result.error(new RuntimeException("O"))));
                });

                runner.test("with different type errors but same messages", (Test test) ->
                {
                    test.assertFalse(Result.error(new RuntimeException("M")).equals(Result.error(new Exception("M"))));
                });
            });

            runner.testGroup("success(T)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertSuccess(null, Result.success(null));
                });

                runner.test("with non-null", (Test test) ->
                {
                    test.assertSuccess("hello", Result.success("hello"));
                });
            });

            runner.test("successFalse()", (Test test) ->
            {
                final Result<Boolean> result = Result.successFalse();
                test.assertSuccess(false, result);
                test.assertSame(result, Result.successFalse());
            });

            runner.test("successTrue()", (Test test) ->
            {
                final Result<Boolean> result = Result.successTrue();
                test.assertSuccess(true, result);
                test.assertSame(result, Result.successTrue());
            });

            runner.testGroup("error(Throwable)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> Result.error(null), new PreConditionFailure("error cannot be null."));
                });

                runner.test("with RuntimeException", (Test test) ->
                {
                    test.assertError(new RuntimeException("abc"), Result.error(new RuntimeException("abc")));
                });

                runner.test("with Exception", (Test test) ->
                {
                    test.assertError(new Exception("abc"), Result.error(new Exception("abc")));
                });
            });

            runner.testGroup("done(T,Throwable)", () ->
            {
                runner.test("with null value and null error", (Test test) ->
                {
                    test.assertDone(null, null, Result.done(null, null));
                });

                runner.test("with non-null value and null error", (Test test) ->
                {
                    test.assertDone(12, null, Result.done(12, null));
                });

                runner.test("with null value and non-null error", (Test test) ->
                {
                    test.assertDone(null, new java.io.IOException("blah"), Result.done(null, new java.io.IOException("blah")));
                });

                runner.test("with non-null value and non-null error", (Test test) ->
                {
                    test.assertDone('a', new RuntimeException("xyz"), Result.done('a', new RuntimeException("xyz")));
                });
            });

            runner.testGroup("isFalse(boolean,String)", () ->
            {
                runner.test("with null expression name", (Test test) ->
                {
                    test.assertThrows(() -> Result.isFalse(false, null), new PreConditionFailure("expressionName cannot be null."));
                });

                runner.test("with empty expression name", (Test test) ->
                {
                    test.assertThrows(() -> Result.isFalse(false, ""), new PreConditionFailure("expressionName cannot be empty."));
                });

                runner.test("with false", (Test test) ->
                {
                    test.assertNull(Result.isFalse(false, "blah"));
                });

                runner.test("with true", (Test test) ->
                {
                    test.assertError(new IllegalArgumentException("blah (true) must be false."), Result.isFalse(true, "blah"));
                });
            });

            runner.testGroup("isTrue(boolean,String)", () ->
            {
                runner.test("with null expression name", (Test test) ->
                {
                    test.assertThrows(() -> Result.isTrue(false, null), new PreConditionFailure("expressionName cannot be null."));
                });

                runner.test("with empty expression name", (Test test) ->
                {
                    test.assertThrows(() -> Result.isTrue(false, ""), new PreConditionFailure("expressionName cannot be empty."));
                });

                runner.test("with true", (Test test) ->
                {
                    test.assertNull(Result.isTrue(true, "blah"));
                });

                runner.test("with false", (Test test) ->
                {
                    test.assertError(new IllegalArgumentException("blah (false) must be true."), Result.isTrue(false, "blah"));
                });
            });

            runner.testGroup("equal(T,T,String)", () ->
            {
                runner.test("with null expression name", (Test test) ->
                {
                    test.assertThrows(() -> Result.equal(null, null, null), new PreConditionFailure("expressionName cannot be null."));
                });

                runner.test("with empty expression name", (Test test) ->
                {
                    test.assertThrows(() -> Result.equal(null, null, ""), new PreConditionFailure("expressionName cannot be empty."));
                });

                runner.test("with equal values", (Test test) ->
                {
                    test.assertNull(Result.equal(5, 5, "abc"));
                });

                runner.test("with not equal values", (Test test) ->
                {
                    test.assertError(new IllegalArgumentException("abc (6) must be 5."), Result.equal(5, 6, "abc"));
                });
            });

            runner.testGroup("notNull(Object,String)", () ->
            {
                runner.test("with null expression name", (Test test) ->
                {
                    test.assertThrows(() -> Result.notNull(false, null), new PreConditionFailure("expressionName cannot be null."));
                });

                runner.test("with empty expression name", (Test test) ->
                {
                    test.assertThrows(() -> Result.notNull(false, ""), new PreConditionFailure("expressionName cannot be empty."));
                });

                runner.test("with not null", (Test test) ->
                {
                    test.assertNull(Result.notNull(true, "blah"));
                });

                runner.test("with null", (Test test) ->
                {
                    test.assertError(new IllegalArgumentException("blah cannot be null."), Result.notNull(null, "blah"));
                });
            });

            runner.testGroup("notNullAndNotEmpty(String,String)", () ->
            {
                runner.test("with null expression name", (Test test) ->
                {
                    test.assertThrows(() -> Result.notNullAndNotEmpty(null, null), new PreConditionFailure("expressionName cannot be null."));
                });

                runner.test("with empty expression name", (Test test) ->
                {
                    test.assertThrows(() -> Result.notNullAndNotEmpty(null, ""), new PreConditionFailure("expressionName cannot be empty."));
                });

                runner.test("with null", (Test test) ->
                {
                    test.assertError(new IllegalArgumentException("blah cannot be null."), Result.notNullAndNotEmpty(null, "blah"));
                });

                runner.test("with empty", (Test test) ->
                {
                    test.assertError(new IllegalArgumentException("blah cannot be empty."), Result.notNullAndNotEmpty("", "blah"));
                });

                runner.test("with non-empty", (Test test) ->
                {
                    test.assertNull(Result.notNullAndNotEmpty("test", "blah"));
                });
            });

            runner.testGroup("greaterThan(int,int,String)", () ->
            {
                runner.test("with null expression name", (Test test) ->
                {
                    test.assertThrows(() -> Result.greaterThan(1, 2, null), new PreConditionFailure("expressionName cannot be null."));
                });

                runner.test("with empty expression name", (Test test) ->
                {
                    test.assertThrows(() -> Result.greaterThan(2, 3, ""), new PreConditionFailure("expressionName cannot be empty."));
                });

                runner.test("with greater than value", (Test test) ->
                {
                    test.assertNull(Result.greaterThan(5, 4, "abc"));
                });

                runner.test("with equal values", (Test test) ->
                {
                    test.assertError(new IllegalArgumentException("abc (5) must be greater than 5."), Result.greaterThan(5, 5, "abc"));
                });

                runner.test("with less than value", (Test test) ->
                {
                    test.assertError(new IllegalArgumentException("abc (5) must be greater than 6."), Result.greaterThan(5, 6, "abc"));
                });
            });

            runner.testGroup("greaterThanOrEqualTo(int,int,String)", () ->
            {
                runner.test("with null expression name", (Test test) ->
                {
                    test.assertThrows(() -> Result.greaterThanOrEqualTo(1, 2, null), new PreConditionFailure("expressionName cannot be null."));
                });

                runner.test("with empty expression name", (Test test) ->
                {
                    test.assertThrows(() -> Result.greaterThanOrEqualTo(2, 3, ""), new PreConditionFailure("expressionName cannot be empty."));
                });

                runner.test("with greater than value", (Test test) ->
                {
                    test.assertNull(Result.greaterThanOrEqualTo(5, 4, "abc"));
                });

                runner.test("with equal values", (Test test) ->
                {
                    test.assertNull(Result.greaterThanOrEqualTo(5, 5, "abc"));
                });

                runner.test("with less than value", (Test test) ->
                {
                    test.assertError(new IllegalArgumentException("abc (5) must be greater than or equal to 6."), Result.greaterThanOrEqualTo(5, 6, "abc"));
                });
            });

            runner.testGroup("between(int,int,int,String)", () ->
            {
                runner.test("with null expression name", (Test test) ->
                {
                    test.assertThrows(() -> Result.between(1, 2, 3, null), new PreConditionFailure("expressionName cannot be null."));
                });

                runner.test("with empty expression name", (Test test) ->
                {
                    test.assertThrows(() -> Result.between(2, 3, 4, ""), new PreConditionFailure("expressionName cannot be empty."));
                });

                runner.test("with value < lowerBound < upperBound", (Test test) ->
                {
                    test.assertError(new IllegalArgumentException("abc (3) must be between 5 and 7."), Result.between(5, 3, 7, "abc"));
                });

                runner.test("with lowerBound <= value < upperBound", (Test test) ->
                {
                    test.assertNull(Result.between(5, 5, 7, "abc"));
                });

                runner.test("with lowerBound <= value <= upperBound", (Test test) ->
                {
                    test.assertNull(Result.between(5, 5, 5, "abc"));
                });

                runner.test("with lowerBound == upperBound < value", (Test test) ->
                {
                    test.assertError(new IllegalArgumentException("abc (6) must be 5."), Result.between(5, 6, 5, "abc"));
                });

                runner.test("with lowerBound < value < upperBound", (Test test) ->
                {
                    test.assertNull(Result.between(5, 6, 7, "abc"));
                });

                runner.test("with lowerBound < value <= upperBound", (Test test) ->
                {
                    test.assertNull(Result.between(5, 6, 6, "abc"));
                });

                runner.test("with lowerBound < upperBound < value", (Test test) ->
                {
                    test.assertError(new IllegalArgumentException("abc (7) must be between 5 and 6."), Result.between(5, 7, 6, "abc"));
                });

                runner.test("with upperBound < value < lowerBound", (Test test) ->
                {
                    test.assertNull(Result.between(10, 5, 1, "abc"));
                });
            });
        });
    }
}
