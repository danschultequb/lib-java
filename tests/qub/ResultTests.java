package qub;

public class ResultTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(Result.class, () ->
        {
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
                    test.assertThrows(() -> Result.greaterThan(null, null, null), new PreConditionFailure("expressionName cannot be null."));
                });

                runner.test("with empty expression name", (Test test) ->
                {
                    test.assertThrows(() -> Result.greaterThan(null, null, ""), new PreConditionFailure("expressionName cannot be empty."));
                });

                runner.test("with equal values", (Test test) ->
                {
                    test.assertNull(Result.greaterThan(5, 5, "abc"));
                });

                runner.test("with not equal values", (Test test) ->
                {
                    test.assertError(new IllegalArgumentException("abc (6) must be 5."), Result.greaterThan(5, 6, "abc"));
                });
            });
        });
    }
}
