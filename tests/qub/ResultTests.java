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

            runner.testGroup("then(Action0)", () ->
            {
                runner.test("with null action", (Test test) ->
                {
                    test.assertThrows(() -> Result.successTrue().then((Action0)null));
                });

                runner.test("with non-error Result and non-throwing action", (Test test) ->
                {
                    final Result<Boolean> result1 = Result.successTrue();
                    final Value<Boolean> value = new Value<>();
                    final Result<Void> result2 = result1.then(() -> value.set(false));
                    test.assertSuccess(null, result2);
                    test.assertEqual(false, value.get());
                });

                runner.test("with non-error Result and throwing action", (Test test) ->
                {
                    final Result<Boolean> result1 = Result.successTrue();
                    final Result<Void> result2 = result1.then((Action0)() ->
                    {
                        throw new RuntimeException("foo");
                    });
                    test.assertError(new RuntimeException("foo"), result2);
                });

                runner.test("with error Result and non-throwing action", (Test test) ->
                {
                    final Result<Character> result1 = Result.error(new RuntimeException("blah"));
                    final Value<Character> value = new Value<>();
                    final Result<Void> result2 = result1.then(() -> value.set('z'));
                    test.assertError(new RuntimeException("blah"), result2);
                    test.assertEqual(null, value.get());
                });

                runner.test("with error Result and throwing action", (Test test) ->
                {
                    final Result<Character> result1 = Result.error(new RuntimeException("blah"));
                    final Result<Void> result2 = result1.then((Action0)() ->
                    {
                        throw new RuntimeException("abc");
                    });
                    test.assertError(new RuntimeException("blah"), result2);
                });
            });

            runner.testGroup("then(Action1<T>)", () ->
            {
                runner.test("with null action", (Test test) ->
                {
                    test.assertThrows(() -> Result.successTrue().then((Action1<Boolean>)null));
                });

                runner.test("with non-error Result and non-throwing action", (Test test) ->
                {
                    final Result<Boolean> result1 = Result.successTrue();
                    final Value<Boolean> value = new Value<>();
                    final Result<Void> result2 = result1.then(value::set);
                    test.assertSuccess(null, result2);
                    test.assertEqual(true, value.get());
                });

                runner.test("with non-error Result and throwing action", (Test test) ->
                {
                    final Result<Boolean> result1 = Result.successTrue();
                    final Result<Void> result2 = result1.then((Action1<Boolean>)(Boolean value) ->
                    {
                        throw new RuntimeException("foo");
                    });
                    test.assertError(new RuntimeException("foo"), result2);
                });

                runner.test("with error Result and non-throwing action", (Test test) ->
                {
                    final Result<Character> result1 = Result.error(new RuntimeException("blah"));
                    final Value<Character> value = new Value<>();
                    final Result<Void> result2 = result1.then(value::set);
                    test.assertError(new RuntimeException("blah"), result2);
                    test.assertEqual(null, value.get());
                });

                runner.test("with error Result and throwing action", (Test test) ->
                {
                    final Result<Character> result1 = Result.error(new RuntimeException("blah"));
                    final Result<Void> result2 = result1.then((Action1<Character>)(Character c) ->
                    {
                        throw new RuntimeException("abc");
                    });
                    test.assertError(new RuntimeException("blah"), result2);
                });
            });

            runner.testGroup("then(Function0<U>)", () ->
            {
                runner.test("with null function", (Test test) ->
                {
                    test.assertThrows(() -> Result.successTrue().then((Function0<Integer>)null));
                });

                runner.test("with non-error Result and non-throwing function", (Test test) ->
                {
                    final Result<Boolean> result1 = Result.successTrue();
                    final Result<Integer> result2 = result1.then(() -> 1);
                    test.assertSuccess(1, result2);
                });

                runner.test("with non-error Result and throwing function", (Test test) ->
                {
                    final Result<Boolean> result1 = Result.successTrue();
                    final Result<Integer> result2 = result1.then(() ->
                    {
                        throw new RuntimeException("foo");
                    });
                    test.assertError(new RuntimeException("foo"), result2);
                });

                runner.test("with error Result and non-throwing function", (Test test) ->
                {
                    final Result<Character> result1 = Result.error(new RuntimeException("blah"));
                    final Result<Integer> result2 = result1.then(() -> 1);
                    test.assertError(new RuntimeException("blah"), result2);
                });

                runner.test("with error Result and throwing function", (Test test) ->
                {
                    final Result<Character> result1 = Result.error(new RuntimeException("blah"));
                    final Result<Integer> result2 = result1.then(() ->
                    {
                        throw new RuntimeException("abc");
                    });
                    test.assertError(new RuntimeException("blah"), result2);
                });
            });

            runner.testGroup("thenResult(Function0<Result<U>>)", () ->
            {
                runner.test("with null function", (Test test) ->
                {
                    test.assertThrows(() -> Result.successTrue().thenResult((Function0<Result<Integer>>)null));
                });

                runner.test("with non-error Result and non-throwing function", (Test test) ->
                {
                    final Result<Boolean> result1 = Result.successTrue();
                    final Result<Integer> result2 = result1.thenResult(() -> Result.success(1));
                    test.assertSuccess(1, result2);
                });

                runner.test("with non-error Result and throwing function", (Test test) ->
                {
                    final Result<Boolean> result1 = Result.successTrue();
                    final Result<Integer> result2 = result1.thenResult(() ->
                    {
                        throw new RuntimeException("foo");
                    });
                    test.assertError(new RuntimeException("foo"), result2);
                });

                runner.test("with error Result and non-throwing function", (Test test) ->
                {
                    final Result<Character> result1 = Result.error(new RuntimeException("blah"));
                    final Result<Integer> result2 = result1.thenResult(() -> Result.success(1));
                    test.assertError(new RuntimeException("blah"), result2);
                });

                runner.test("with error Result and throwing function", (Test test) ->
                {
                    final Result<Character> result1 = Result.error(new RuntimeException("blah"));
                    final Result<Integer> result2 = result1.thenResult(() ->
                    {
                        throw new RuntimeException("abc");
                    });
                    test.assertError(new RuntimeException("blah"), result2);
                });
            });

            runner.testGroup("then(Function1<T,U>)", () ->
            {
                runner.test("with null function", (Test test) ->
                {
                    test.assertThrows(() -> Result.successTrue().then((Function1<Boolean,Integer>)null));
                });

                runner.test("with non-error Result and non-throwing function", (Test test) ->
                {
                    final Result<Boolean> result1 = Result.successTrue();
                    final Result<Integer> result2 = result1.then((Boolean value) -> value ? 1 : 0);
                    test.assertSuccess(1, result2);
                });

                runner.test("with non-error Result and throwing function", (Test test) ->
                {
                    final Result<Boolean> result1 = Result.successTrue();
                    final Result<Integer> result2 = result1.then((Boolean value) ->
                    {
                        throw new RuntimeException("foo");
                    });
                    test.assertError(new RuntimeException("foo"), result2);
                });

                runner.test("with error Result and non-throwing function", (Test test) ->
                {
                    final Result<Character> result1 = Result.error(new RuntimeException("blah"));
                    final Result<Integer> result2 = result1.then(Object::hashCode);
                    test.assertError(new RuntimeException("blah"), result2);
                });

                runner.test("with error Result and throwing function", (Test test) ->
                {
                    final Result<Character> result1 = Result.error(new RuntimeException("blah"));
                    final Result<Integer> result2 = result1.then((Character c) ->
                    {
                        throw new RuntimeException("abc");
                    });
                    test.assertError(new RuntimeException("blah"), result2);
                });
            });

            runner.testGroup("thenResult(Function1<T,Result<U>>)", () ->
            {
                runner.test("with null function", (Test test) ->
                {
                    test.assertThrows(() -> Result.successTrue().then((Function1<Boolean,Result<Integer>>)null));
                });

                runner.test("with non-error Result and non-throwing function", (Test test) ->
                {
                    final Result<Boolean> result1 = Result.successTrue();
                    final Result<Integer> result2 = result1.thenResult((Boolean value) -> Result.success(value ? 1 : 0));
                    test.assertSuccess(1, result2);
                });

                runner.test("with non-error Result and throwing function", (Test test) ->
                {
                    final Result<Boolean> result1 = Result.successTrue();
                    final Result<Integer> result2 = result1.thenResult((Boolean value) ->
                    {
                        throw new RuntimeException("foo");
                    });
                    test.assertError(new RuntimeException("foo"), result2);
                });

                runner.test("with error Result and non-throwing function", (Test test) ->
                {
                    final Result<Character> result1 = Result.error(new RuntimeException("blah"));
                    final Result<Integer> result2 = result1.thenResult((Character c) -> Result.success(c.hashCode()));
                    test.assertError(new RuntimeException("blah"), result2);
                });

                runner.test("with error Result and throwing function", (Test test) ->
                {
                    final Result<Character> result1 = Result.error(new RuntimeException("blah"));
                    final Result<Integer> result2 = result1.thenResult((Character c) ->
                    {
                        throw new RuntimeException("abc");
                    });
                    test.assertError(new RuntimeException("blah"), result2);
                });
            });

            runner.testGroup("catchError(Action0)", () ->
            {
                runner.test("with null action", (Test test) ->
                {
                    test.assertThrows(() -> Result.success().catchError((Action0)null), new PreConditionFailure("action cannot be null."));
                });

                runner.test("with non-error Result and non-throwing action", (Test test) ->
                {
                    final Result<Boolean> result1 = Result.successTrue();
                    final Value<Integer> value = new Value<>(0);
                    final Result<Boolean> result2 = result1.catchError((Action0)() -> value.set(5));
                    test.assertSame(result2, result1);
                    test.assertEqual(0, value.get());
                });

                runner.test("with non-error Result and throwing action", (Test test) ->
                {
                    final Result<Boolean> result1 = Result.successTrue();
                    final Result<Boolean> result2 = result1.catchError((Action0)() -> { throw new RuntimeException("abc"); });
                    test.assertSame(result2, result1);
                });

                runner.test("with error Result and non-throwing action", (Test test) ->
                {
                    final Result<Boolean> result1 = Result.error(new RuntimeException("abc"));
                    final Value<Integer> value = new Value<>(0);
                    final Result<Boolean> result2 = result1.catchError((Action0)() -> value.set(5));
                    test.assertSuccess(null, result2);
                    test.assertEqual(5, value.get());
                });

                runner.test("with error Result and throwing action", (Test test) ->
                {
                    final Result<Boolean> result1 = Result.error(new RuntimeException("abc"));
                    final Result<Boolean> result2 = result1.catchError((Action0)() -> { throw new RuntimeException("xyz"); });
                    test.assertError(new RuntimeException("xyz"), result2);
                });
            });

            runner.testGroup("catchError(Action1<Throwable>)", () ->
            {
                runner.test("with null action", (Test test) ->
                {
                    test.assertThrows(() -> Result.success().catchError((Action1<Throwable>)null), new PreConditionFailure("action cannot be null."));
                });

                runner.test("with non-error Result and non-throwing action", (Test test) ->
                {
                    final Result<Boolean> result1 = Result.successTrue();
                    final Value<Integer> value = new Value<>(0);
                    final Result<Boolean> result2 = result1.catchError((Throwable error) -> value.set(5));
                    test.assertSame(result2, result1);
                    test.assertEqual(0, value.get());
                });

                runner.test("with non-error Result and throwing action", (Test test) ->
                {
                    final Result<Boolean> result1 = Result.successTrue();
                    final Result<Boolean> result2 = result1.catchError((Throwable error) -> { throw new RuntimeException("abc"); });
                    test.assertSame(result2, result1);
                });

                runner.test("with error Result and non-throwing action", (Test test) ->
                {
                    final Result<Boolean> result1 = Result.error(new RuntimeException("abc"));
                    final Value<Integer> value = new Value<>(0);
                    final Result<Boolean> result2 = result1.catchError((Throwable error) -> value.set(5));
                    test.assertSuccess(null, result2);
                    test.assertEqual(5, value.get());
                });

                runner.test("with error Result and throwing action", (Test test) ->
                {
                    final Result<Boolean> result1 = Result.error(new RuntimeException("abc"));
                    final Result<Boolean> result2 = result1.catchError((Action1<Throwable>)(Throwable error) -> { throw new RuntimeException("xyz"); });
                    test.assertError(new RuntimeException("xyz"), result2);
                });
            });

            runner.testGroup("catchResultError(Action1<Result<T>>)", () ->
            {
                runner.test("with null action", (Test test) ->
                {
                    test.assertThrows(() -> Result.<Void>success().catchResultError((Action1<Result<Void>>)null), new PreConditionFailure("action cannot be null."));
                });

                runner.test("with non-error Result and non-throwing action", (Test test) ->
                {
                    final Result<Boolean> result1 = Result.successTrue();
                    final Value<Integer> value = new Value<>(0);
                    final Result<Boolean> result2 = result1.catchResultError((Result<Boolean> errorResult) ->
                    {
                        test.assertSame(result1, errorResult);
                        value.set(5);
                    });
                    test.assertSame(result2, result1);
                    test.assertEqual(0, value.get());
                });

                runner.test("with non-error Result and throwing action", (Test test) ->
                {
                    final Result<Boolean> result1 = Result.successTrue();
                    final Result<Boolean> result2 = result1.catchResultError((Result<Boolean> errorResult) ->
                    {
                        test.assertSame(result1, errorResult);
                        throw new RuntimeException("abc");
                    });
                    test.assertSame(result2, result1);
                });

                runner.test("with error Result and non-throwing action", (Test test) ->
                {
                    final Result<Boolean> result1 = Result.error(new RuntimeException("abc"));
                    final Value<Integer> value = new Value<>(0);
                    final Result<Boolean> result2 = result1.catchResultError((Result<Boolean> resultError) ->
                    {
                        test.assertSame(result1, resultError);
                        value.set(5);
                    });
                    test.assertSuccess(null, result2);
                    test.assertEqual(5, value.get());
                });

                runner.test("with error Result and throwing action", (Test test) ->
                {
                    final Result<Boolean> result1 = Result.error(new RuntimeException("abc"));
                    final Result<Boolean> result2 = result1.catchResultError((Result<Boolean> resultError) ->
                    {
                        test.assertSame(result1, resultError);
                        throw new RuntimeException("xyz");
                    });
                    test.assertError(new RuntimeException("xyz"), result2);
                });
            });

            runner.testGroup("catchError(Class<TError>,Action0)", () ->
            {
                runner.test("with null errorType", (Test test) ->
                {
                    test.assertThrows(() -> Result.success().catchError(null, (Action0)null), new PreConditionFailure("errorType cannot be null."));
                });

                runner.test("with null action", (Test test) ->
                {
                    test.assertThrows(() -> Result.success().catchError(RuntimeException.class, (Action0)null), new PreConditionFailure("action cannot be null."));
                });

                runner.test("with non-error Result and non-throwing action", (Test test) ->
                {
                    final Result<Boolean> result1 = Result.successTrue();
                    final Value<Integer> value = new Value<>(0);
                    final Result<Boolean> result2 = result1.catchError(NullPointerException.class, (Action0)() -> value.set(5));
                    test.assertSame(result2, result1);
                    test.assertEqual(0, value.get());
                });

                runner.test("with non-error Result and throwing action", (Test test) ->
                {
                    final Result<Boolean> result1 = Result.successTrue();
                    final Result<Boolean> result2 = result1.catchError(NullPointerException.class, (Action0)() -> { throw new RuntimeException("abc"); });
                    test.assertSame(result2, result1);
                });

                runner.test("with error Result with wrong error type and non-throwing action", (Test test) ->
                {
                    final Result<Boolean> result1 = Result.error(new RuntimeException("abc"));
                    final Value<Integer> value = new Value<>(0);
                    final Result<Boolean> result2 = result1.catchError(NullPointerException.class, (Action0)() -> value.set(5));
                    test.assertSame(result2, result1);
                    test.assertEqual(0, value.get());
                });

                runner.test("with error Result with correct error type and non-throwing action", (Test test) ->
                {
                    final Result<Boolean> result1 = Result.error(new RuntimeException("abc"));
                    final Value<Integer> value = new Value<>(0);
                    final Result<Boolean> result2 = result1.catchError(RuntimeException.class, (Action0)() -> value.set(5));
                    test.assertSuccess(null, result2);
                    test.assertEqual(5, value.get());
                });

                runner.test("with error Result with wrong error type and throwing action", (Test test) ->
                {
                    final Result<Boolean> result1 = Result.error(new RuntimeException("abc"));
                    final Result<Boolean> result2 = result1.catchError(NullPointerException.class, (Action0)() -> { throw new NullPointerException("abc"); });
                    test.assertSame(result2, result1);
                });

                runner.test("with error Result with correct error type and throwing action", (Test test) ->
                {
                    final Result<Boolean> result1 = Result.error(new RuntimeException("abc"));
                    final Result<Boolean> result2 = result1.catchError(RuntimeException.class, (Action0)() -> { throw new NullPointerException("abc"); });
                    test.assertError(new NullPointerException("abc"), result2);
                });
            });

            runner.testGroup("catchError(Class<TError>,Action1<TError>)", () ->
            {
                runner.test("with null errorType", (Test test) ->
                {
                    test.assertThrows(() -> Result.success().catchError(null, (Action1<RuntimeException>)null), new PreConditionFailure("errorType cannot be null."));
                });

                runner.test("with null action", (Test test) ->
                {
                    test.assertThrows(() -> Result.success().catchError(RuntimeException.class, (Action1<RuntimeException>)null), new PreConditionFailure("action cannot be null."));
                });

                runner.test("with non-error Result and non-throwing action", (Test test) ->
                {
                    final Result<Boolean> result1 = Result.successTrue();
                    final Value<Integer> value = new Value<>(0);
                    final Result<Boolean> result2 = result1.catchError(NullPointerException.class, (Action1<NullPointerException>)error -> value.set(5));
                    test.assertSame(result2, result1);
                    test.assertEqual(0, value.get());
                });

                runner.test("with non-error Result and throwing action", (Test test) ->
                {
                    final Result<Boolean> result1 = Result.successTrue();
                    final Result<Boolean> result2 = result1.catchError(NullPointerException.class, (Action1<NullPointerException>)error -> { throw new RuntimeException("abc"); });
                    test.assertSame(result2, result1);
                });

                runner.test("with error Result with wrong error type and non-throwing action", (Test test) ->
                {
                    final Result<Boolean> result1 = Result.error(new RuntimeException("abc"));
                    final Value<Integer> value = new Value<>(0);
                    final Result<Boolean> result2 = result1.catchError(NullPointerException.class, (Action1<NullPointerException>)error -> value.set(5));
                    test.assertSame(result2, result1);
                    test.assertEqual(0, value.get());
                });

                runner.test("with error Result with correct error type and non-throwing action", (Test test) ->
                {
                    final Result<Boolean> result1 = Result.error(new RuntimeException("abc"));
                    final Value<Integer> value = new Value<>(0);
                    final Result<Boolean> result2 = result1.catchError(RuntimeException.class, (Action1<RuntimeException>)error -> value.set(5));
                    test.assertSuccess(null, result2);
                    test.assertEqual(5, value.get());
                });

                runner.test("with error Result with wrong error type and throwing action", (Test test) ->
                {
                    final Result<Boolean> result1 = Result.error(new RuntimeException("abc"));
                    final Result<Boolean> result2 = result1.catchError(NullPointerException.class, (Action1<NullPointerException>)error -> { throw new NullPointerException("abc"); });
                    test.assertSame(result2, result1);
                });

                runner.test("with error Result with correct error type and throwing action", (Test test) ->
                {
                    final Result<Boolean> result1 = Result.error(new RuntimeException("abc"));
                    final Result<Boolean> result2 = result1.catchError(RuntimeException.class, (Action1<RuntimeException>)error -> { throw new NullPointerException("abc"); });
                    test.assertError(new NullPointerException("abc"), result2);
                });
            });

            runner.testGroup("catchError(Function0<T>)", () ->
            {
                runner.test("with null function", (Test test) ->
                {
                    test.assertThrows(() -> Result.successTrue().catchError((Function0<Boolean>)null), new PreConditionFailure("function cannot be null."));
                });

                runner.test("with non-error Result and non-throwing function", (Test test) ->
                {
                    final Result<Boolean> result1 = Result.successTrue();
                    final Value<Integer> value = new Value<>(0);
                    final Result<Boolean> result2 = result1.catchError(() ->
                    {
                        value.set(5);
                        return false;
                    });
                    test.assertSame(result2, result1);
                    test.assertEqual(0, value.get());
                });

                runner.test("with non-error Result and throwing function", (Test test) ->
                {
                    final Result<Boolean> result1 = Result.successTrue();
                    final Result<Boolean> result2 = result1.catchError((Function0<Boolean>)() -> { throw new RuntimeException("abc"); });
                    test.assertSame(result2, result1);
                });

                runner.test("with error Result and null-returning function", (Test test) ->
                {
                    final Result<Boolean> result1 = Result.error(new RuntimeException("abc"));
                    final Value<Integer> value = new Value<>(0);
                    final Result<Boolean> result2 = result1.catchError((Function0<Boolean>)() ->
                    {
                        value.set(5);
                        return null;
                    });
                    test.assertSuccess(null, result2);
                    test.assertEqual(5, value.get());
                });

                runner.test("with error Result and non-throwing function", (Test test) ->
                {
                    final Result<Boolean> result1 = Result.error(new RuntimeException("abc"));
                    final Value<Integer> value = new Value<>(0);
                    final Result<Boolean> result2 = result1.catchError(() ->
                    {
                        value.set(5);
                        return false;
                    });
                    test.assertSuccess(false, result2);
                    test.assertEqual(5, value.get());
                });

                runner.test("with error Result and throwing function", (Test test) ->
                {
                    final Result<Boolean> result1 = Result.error(new RuntimeException("abc"));
                    final Result<Boolean> result2 = result1.catchError((Function0<Boolean>)() -> { throw new RuntimeException("xyz"); });
                    test.assertError(new RuntimeException("xyz"), result2);
                });
            });

            runner.testGroup("catchError(Function1<Throwable,T>)", () ->
            {
                runner.test("with null function", (Test test) ->
                {
                    test.assertThrows(() -> Result.successTrue().catchError((Function1<Throwable,Boolean>)null), new PreConditionFailure("function cannot be null."));
                });

                runner.test("with non-error Result and non-throwing function", (Test test) ->
                {
                    final Result<Boolean> result1 = Result.successTrue();
                    final Value<Integer> value = new Value<>(0);
                    final Result<Boolean> result2 = result1.catchError((Throwable error) ->
                    {
                        value.set(5);
                        return false;
                    });
                    test.assertSame(result2, result1);
                    test.assertEqual(0, value.get());
                });

                runner.test("with non-error Result and throwing function", (Test test) ->
                {
                    final Result<Boolean> result1 = Result.successTrue();
                    final Result<Boolean> result2 = result1.catchError((Function1<Throwable,Boolean>)(Throwable error) -> { throw new RuntimeException("abc"); });
                    test.assertSame(result2, result1);
                });

                runner.test("with error Result and null-returning function", (Test test) ->
                {
                    final Result<Boolean> result1 = Result.error(new RuntimeException("abc"));
                    final Value<Integer> value = new Value<>(0);
                    final Result<Boolean> result2 = result1.catchError((Throwable error) ->
                    {
                        value.set(5);
                        return null;
                    });
                    test.assertSuccess(null, result2);
                    test.assertEqual(5, value.get());
                });

                runner.test("with error Result and non-throwing function", (Test test) ->
                {
                    final Result<Boolean> result1 = Result.error(new RuntimeException("abc"));
                    final Value<Integer> value = new Value<>(0);
                    final Result<Boolean> result2 = result1.catchError((Throwable error) ->
                    {
                        value.set(5);
                        return false;
                    });
                    test.assertSuccess(false, result2);
                    test.assertEqual(5, value.get());
                });

                runner.test("with error Result and throwing function", (Test test) ->
                {
                    final Result<Boolean> result1 = Result.error(new RuntimeException("abc"));
                    final Result<Boolean> result2 = result1.catchError((Function1<Throwable,Boolean>)(Throwable error) -> { throw new RuntimeException("xyz"); });
                    test.assertError(new RuntimeException("xyz"), result2);
                });
            });

            runner.testGroup("catchError(Class<TError>,Function0<T>)", () ->
            {
                runner.test("with null errorType", (Test test) ->
                {
                    test.assertThrows(() -> Result.<Void>success().catchError(null, (Function0<Void>)null), new PreConditionFailure("errorType cannot be null."));
                });

                runner.test("with null function", (Test test) ->
                {
                    test.assertThrows(() -> Result.<Void>success().catchError(RuntimeException.class, (Function0<Void>)null), new PreConditionFailure("function cannot be null."));
                });

                runner.test("with non-error Result and non-throwing function", (Test test) ->
                {
                    final Result<Boolean> result1 = Result.successTrue();
                    final Value<Integer> value = new Value<>(0);
                    final Result<Boolean> result2 = result1.catchError(NullPointerException.class, () ->
                    {
                        value.set(5);
                        return false;
                    });
                    test.assertSame(result2, result1);
                    test.assertEqual(0, value.get());
                });

                runner.test("with non-error Result and throwing function", (Test test) ->
                {
                    final Result<Boolean> result1 = Result.successTrue();
                    final Result<Boolean> result2 = result1.catchError(NullPointerException.class, (Function0<Boolean>)() -> { throw new RuntimeException("abc"); });
                    test.assertSame(result2, result1);
                });

                runner.test("with error Result with wrong error type and non-throwing function", (Test test) ->
                {
                    final Result<Boolean> result1 = Result.error(new RuntimeException("abc"));
                    final Value<Integer> value = new Value<>(0);
                    final Result<Boolean> result2 = result1.catchError(NullPointerException.class, () ->
                    {
                        value.set(5);
                        return false;
                    });
                    test.assertSame(result2, result1);
                    test.assertEqual(0, value.get());
                });

                runner.test("with error Result with correct error type and non-throwing function", (Test test) ->
                {
                    final Result<Boolean> result1 = Result.error(new RuntimeException("abc"));
                    final Value<Integer> value = new Value<>(0);
                    final Result<Boolean> result2 = result1.catchError(RuntimeException.class, () ->
                    {
                        value.set(5);
                        return false;
                    });
                    test.assertSuccess(false, result2);
                    test.assertEqual(5, value.get());
                });

                runner.test("with error Result with wrong error type and throwing function", (Test test) ->
                {
                    final Result<Boolean> result1 = Result.error(new RuntimeException("abc"));
                    final Result<Boolean> result2 = result1.catchError(NullPointerException.class, (Function0<Boolean>)() -> { throw new NullPointerException("abc"); });
                    test.assertSame(result2, result1);
                });

                runner.test("with error Result with correct error type and throwing function", (Test test) ->
                {
                    final Result<Boolean> result1 = Result.error(new RuntimeException("abc"));
                    final Result<Boolean> result2 = result1.catchError(RuntimeException.class, (Function0<Boolean>)() -> { throw new NullPointerException("abc"); });
                    test.assertError(new NullPointerException("abc"), result2);
                });
            });

            runner.testGroup("catchError(Class<TError>,Function1<TError,T>)", () ->
            {
                runner.test("with null errorType", (Test test) ->
                {
                    test.assertThrows(() -> Result.<Void>success().catchError(null, (Function1<RuntimeException,Void>)null), new PreConditionFailure("errorType cannot be null."));
                });

                runner.test("with null function", (Test test) ->
                {
                    test.assertThrows(() -> Result.<Void>success().catchError(RuntimeException.class, (Function1<RuntimeException,Void>)null), new PreConditionFailure("function cannot be null."));
                });

                runner.test("with non-error Result and non-throwing function", (Test test) ->
                {
                    final Result<Boolean> result1 = Result.successTrue();
                    final Value<Integer> value = new Value<>(0);
                    final Result<Boolean> result2 = result1.catchError(NullPointerException.class, error ->
                    {
                        value.set(5);
                        return false;
                    });
                    test.assertSame(result2, result1);
                    test.assertEqual(0, value.get());
                });

                runner.test("with non-error Result and throwing function", (Test test) ->
                {
                    final Result<Boolean> result1 = Result.successTrue();
                    final Result<Boolean> result2 = result1.catchError(NullPointerException.class, (Function1<NullPointerException,Boolean>)error -> { throw new RuntimeException("abc"); });
                    test.assertSame(result2, result1);
                });

                runner.test("with error Result with wrong error type and non-throwing function", (Test test) ->
                {
                    final Result<Boolean> result1 = Result.error(new RuntimeException("abc"));
                    final Value<Integer> value = new Value<>(0);
                    final Result<Boolean> result2 = result1.catchError(NullPointerException.class, error ->
                    {
                        value.set(5);
                        return false;
                    });
                    test.assertSame(result2, result1);
                    test.assertEqual(0, value.get());
                });

                runner.test("with error Result with correct error type and non-throwing function", (Test test) ->
                {
                    final Result<Boolean> result1 = Result.error(new RuntimeException("abc"));
                    final Value<Integer> value = new Value<>(0);
                    final Result<Boolean> result2 = result1.catchError(RuntimeException.class, error ->
                    {
                        value.set(5);
                        return false;
                    });
                    test.assertSuccess(false, result2);
                    test.assertEqual(5, value.get());
                });

                runner.test("with error Result with wrong error type and throwing function", (Test test) ->
                {
                    final Result<Boolean> result1 = Result.error(new RuntimeException("abc"));
                    final Result<Boolean> result2 = result1.catchError(NullPointerException.class, (Function1<NullPointerException,Boolean>)error -> { throw new NullPointerException("abc"); });
                    test.assertSame(result2, result1);
                });

                runner.test("with error Result with correct error type and throwing function", (Test test) ->
                {
                    final Result<Boolean> result1 = Result.error(new RuntimeException("abc"));
                    final Result<Boolean> result2 = result1.catchError(RuntimeException.class, (Function1<RuntimeException,Boolean>)error -> { throw new NullPointerException("abc"); });
                    test.assertError(new NullPointerException("abc"), result2);
                });
            });

            runner.testGroup("catchErrorResult(Function0<Result<T>>)", () ->
            {
                runner.test("with null function", (Test test) ->
                {
                    test.assertThrows(() -> Result.successTrue().catchErrorResult((Function0<Result<Boolean>>)null), new PreConditionFailure("function cannot be null."));
                });

                runner.test("with non-error Result and non-throwing function", (Test test) ->
                {
                    final Result<Boolean> result1 = Result.successTrue();
                    final Value<Integer> value = new Value<>(0);
                    final Result<Boolean> result2 = result1.catchErrorResult(() ->
                    {
                        value.set(5);
                        return Result.successFalse();
                    });
                    test.assertSame(result2, result1);
                    test.assertEqual(0, value.get());
                });

                runner.test("with non-error Result and throwing function", (Test test) ->
                {
                    final Result<Boolean> result1 = Result.successTrue();
                    final Result<Boolean> result2 = result1.catchErrorResult((Function0<Result<Boolean>>)() -> { throw new RuntimeException("abc"); });
                    test.assertSame(result2, result1);
                });

                runner.test("with error Result and null-returning function", (Test test) ->
                {
                    final Result<Boolean> result1 = Result.error(new RuntimeException("abc"));
                    final Value<Integer> value = new Value<>(0);
                    test.assertThrows(() -> result1.catchErrorResult((Function0<Result<Boolean>>)() ->
                        {
                            value.set(5);
                            return null;
                        }),
                        new PostConditionFailure("result cannot be null."));
                    test.assertEqual(5, value.get());
                });

                runner.test("with error Result and non-throwing function", (Test test) ->
                {
                    final Result<Boolean> result1 = Result.error(new RuntimeException("abc"));
                    final Value<Integer> value = new Value<>(0);
                    final Result<Boolean> result2 = result1.catchErrorResult(() ->
                    {
                        value.set(5);
                        return Result.successFalse();
                    });
                    test.assertSuccess(false, result2);
                    test.assertEqual(5, value.get());
                });

                runner.test("with error Result and throwing function", (Test test) ->
                {
                    final Result<Boolean> result1 = Result.error(new RuntimeException("abc"));
                    final Result<Boolean> result2 = result1.catchErrorResult((Function0<Result<Boolean>>)() -> { throw new RuntimeException("xyz"); });
                    test.assertError(new RuntimeException("xyz"), result2);
                });
            });

            runner.testGroup("catchErrorResult(Function1<Throwable,Result<T>>)", () ->
            {
                runner.test("with null function", (Test test) ->
                {
                    test.assertThrows(() -> Result.successTrue().catchErrorResult((Function1<Throwable,Result<Boolean>>)null), new PreConditionFailure("function cannot be null."));
                });

                runner.test("with non-error Result and null-returning function", (Test test) ->
                {
                    final Result<Boolean> result1 = Result.successTrue();
                    final Value<Integer> value = new Value<>(0);
                    final Result<Boolean> result2 = result1.catchErrorResult((Throwable error) ->
                    {
                        value.set(5);
                        return null;
                    });
                    test.assertSame(result2, result1);
                    test.assertEqual(0, value.get());
                });

                runner.test("with non-error Result and non-throwing function", (Test test) ->
                {
                    final Result<Boolean> result1 = Result.successTrue();
                    final Value<Integer> value = new Value<>(0);
                    final Result<Boolean> result2 = result1.catchErrorResult((Throwable error) ->
                    {
                        value.set(5);
                        return Result.successFalse();
                    });
                    test.assertSame(result2, result1);
                    test.assertEqual(0, value.get());
                });

                runner.test("with non-error Result and throwing function", (Test test) ->
                {
                    final Result<Boolean> result1 = Result.successTrue();
                    final Result<Boolean> result2 = result1.catchErrorResult((Throwable error) -> { throw new RuntimeException("abc"); });
                    test.assertSame(result2, result1);
                });

                runner.test("with error Result and null-returning function", (Test test) ->
                {
                    final Result<Boolean> result1 = Result.error(new RuntimeException("abc"));
                    test.assertThrows(() -> result1.catchErrorResult((Throwable error) -> null), new PostConditionFailure("result cannot be null."));
                });

                runner.test("with error Result and non-throwing function", (Test test) ->
                {
                    final Result<Boolean> result1 = Result.error(new RuntimeException("abc"));
                    final Value<Integer> value = new Value<>(0);
                    final Result<Boolean> result2 = result1.catchErrorResult((Throwable error) ->
                    {
                        value.set(5);
                        return Result.successFalse();
                    });
                    test.assertSuccess(false, result2);
                    test.assertEqual(5, value.get());
                });

                runner.test("with error Result and throwing function", (Test test) ->
                {
                    final Result<Boolean> result1 = Result.error(new RuntimeException("abc"));
                    final Result<Boolean> result2 = result1.catchErrorResult((Throwable error) -> { throw new RuntimeException("xyz"); });
                    test.assertError(new RuntimeException("xyz"), result2);
                });
            });

            runner.testGroup("catchErrorResult(Class<TError>,Function0<Result<T>>)", () ->
            {
                runner.test("with null errorType", (Test test) ->
                {
                    test.assertThrows(() -> Result.<Void>success().catchErrorResult(null, (Function0<Result<Void>>)null), new PreConditionFailure("errorType cannot be null."));
                });

                runner.test("with null function", (Test test) ->
                {
                    test.assertThrows(() -> Result.<Void>success().catchErrorResult(RuntimeException.class, (Function0<Result<Void>>)null), new PreConditionFailure("function cannot be null."));
                });

                runner.test("with non-error Result and non-throwing function", (Test test) ->
                {
                    final Result<Boolean> result1 = Result.successTrue();
                    final Value<Integer> value = new Value<>(0);
                    final Result<Boolean> result2 = result1.catchErrorResult(NullPointerException.class, () ->
                    {
                        value.set(5);
                        return Result.successFalse();
                    });
                    test.assertSame(result2, result1);
                    test.assertEqual(0, value.get());
                });

                runner.test("with non-error Result and throwing function", (Test test) ->
                {
                    final Result<Boolean> result1 = Result.successTrue();
                    final Result<Boolean> result2 = result1.catchErrorResult(NullPointerException.class, (Function0<Result<Boolean>>)() -> { throw new RuntimeException("abc"); });
                    test.assertSame(result2, result1);
                });

                runner.test("with error Result with wrong error type and non-throwing function", (Test test) ->
                {
                    final Result<Boolean> result1 = Result.error(new RuntimeException("abc"));
                    final Value<Integer> value = new Value<>(0);
                    final Result<Boolean> result2 = result1.catchErrorResult(NullPointerException.class, () ->
                    {
                        value.set(5);
                        return Result.successFalse();
                    });
                    test.assertSame(result2, result1);
                    test.assertEqual(0, value.get());
                });

                runner.test("with error Result with correct error type and non-throwing function", (Test test) ->
                {
                    final Result<Boolean> result1 = Result.error(new RuntimeException("abc"));
                    final Value<Integer> value = new Value<>(0);
                    final Result<Boolean> result2 = result1.catchErrorResult(RuntimeException.class, () ->
                    {
                        value.set(5);
                        return Result.successFalse();
                    });
                    test.assertSuccess(false, result2);
                    test.assertEqual(5, value.get());
                });

                runner.test("with error Result with wrong error type and throwing function", (Test test) ->
                {
                    final Result<Boolean> result1 = Result.error(new RuntimeException("abc"));
                    final Result<Boolean> result2 = result1.catchErrorResult(NullPointerException.class, (Function0<Result<Boolean>>)() -> { throw new NullPointerException("abc"); });
                    test.assertSame(result2, result1);
                });

                runner.test("with error Result with correct error type and throwing function", (Test test) ->
                {
                    final Result<Boolean> result1 = Result.error(new RuntimeException("abc"));
                    final Result<Boolean> result2 = result1.catchErrorResult(RuntimeException.class, (Function0<Result<Boolean>>)() -> { throw new NullPointerException("abc"); });
                    test.assertError(new NullPointerException("abc"), result2);
                });
            });

            runner.testGroup("catchErrorResult(Class<TError>,Function1<TError,Result<T>>)", () ->
            {
                runner.test("with null errorType", (Test test) ->
                {
                    test.assertThrows(() -> Result.<Void>success().catchErrorResult(null, (Function1<RuntimeException,Result<Void>>)null), new PreConditionFailure("errorType cannot be null."));
                });

                runner.test("with null function", (Test test) ->
                {
                    test.assertThrows(() -> Result.<Void>success().catchErrorResult(RuntimeException.class, (Function1<RuntimeException,Result<Void>>)null), new PreConditionFailure("function cannot be null."));
                });

                runner.test("with non-error Result and non-throwing function", (Test test) ->
                {
                    final Result<Boolean> result1 = Result.successTrue();
                    final Value<Integer> value = new Value<>(0);
                    final Result<Boolean> result2 = result1.catchErrorResult(NullPointerException.class, error ->
                    {
                        value.set(5);
                        return Result.successFalse();
                    });
                    test.assertSame(result2, result1);
                    test.assertEqual(0, value.get());
                });

                runner.test("with non-error Result and throwing function", (Test test) ->
                {
                    final Result<Boolean> result1 = Result.successTrue();
                    final Result<Boolean> result2 = result1.catchErrorResult(NullPointerException.class, (Function1<NullPointerException,Result<Boolean>>)error -> { throw new RuntimeException("abc"); });
                    test.assertSame(result2, result1);
                });

                runner.test("with error Result with wrong error type and non-throwing function", (Test test) ->
                {
                    final Result<Boolean> result1 = Result.error(new RuntimeException("abc"));
                    final Value<Integer> value = new Value<>(0);
                    final Result<Boolean> result2 = result1.catchErrorResult(NullPointerException.class, error ->
                    {
                        value.set(5);
                        return Result.successFalse();
                    });
                    test.assertSame(result2, result1);
                    test.assertEqual(0, value.get());
                });

                runner.test("with error Result with correct error type and non-throwing function", (Test test) ->
                {
                    final Result<Boolean> result1 = Result.error(new RuntimeException("abc"));
                    final Value<Integer> value = new Value<>(0);
                    final Result<Boolean> result2 = result1.catchErrorResult(RuntimeException.class, error ->
                    {
                        value.set(5);
                        return Result.successFalse();
                    });
                    test.assertSuccess(false, result2);
                    test.assertEqual(5, value.get());
                });

                runner.test("with error Result with wrong error type and throwing function", (Test test) ->
                {
                    final Result<Boolean> result1 = Result.error(new RuntimeException("abc"));
                    final Result<Boolean> result2 = result1.catchErrorResult(NullPointerException.class, (Function1<NullPointerException,Result<Boolean>>)error -> { throw new NullPointerException("abc"); });
                    test.assertSame(result2, result1);
                });

                runner.test("with error Result with correct error type and throwing function", (Test test) ->
                {
                    final Result<Boolean> result1 = Result.error(new RuntimeException("abc"));
                    final Result<Boolean> result2 = result1.catchErrorResult(RuntimeException.class, (Function1<RuntimeException,Result<Boolean>>)error -> { throw new NullPointerException("abc"); });
                    test.assertError(new NullPointerException("abc"), result2);
                });
            });

            runner.testGroup("onError(Action0)", () ->
            {
                runner.test("with null action", (Test test) ->
                {
                    final Result<Integer> result = Result.success();
                    test.assertThrows(() -> result.onError((Action0)null),
                        new PreConditionFailure("action cannot be null."));
                });

                runner.test("with successful Result", (Test test) ->
                {
                    final Value<Integer> value = Value.create();
                    Result.success()
                        .onError(() -> value.set(5));
                    test.assertFalse(value.hasValue());
                });

                runner.test("with error Result", (Test test) ->
                {
                    final Value<Integer> value = Value.create();
                    Result.error(new NullPointerException())
                        .onError(() -> value.set(5));
                    test.assertEqual(5, value.get());
                });
            });

            runner.testGroup("onError(Action1<Throwable>)", () ->
            {
                runner.test("with null action", (Test test) ->
                {
                    final Result<Integer> result = Result.success();
                    test.assertThrows(() -> result.onError((Action1<Throwable>)null),
                        new PreConditionFailure("action cannot be null."));
                });

                runner.test("with successful Result", (Test test) ->
                {
                    final Value<Integer> value = Value.create();
                    Result.success()
                        .onError((Throwable error) -> value.set(5));
                    test.assertFalse(value.hasValue());
                });

                runner.test("with error Result", (Test test) ->
                {
                    final Value<Integer> value = Value.create();
                    Result.error(new NullPointerException("a"))
                        .onError((Throwable error) ->
                        {
                            test.assertEqual(new NullPointerException("a"), error);
                            value.set(5);
                        });
                    test.assertEqual(5, value.get());
                });
            });

            runner.testGroup("onError(Class<TError>,Action0)", () ->
            {
                runner.test("with null errorType", (Test test) ->
                {
                    final Result<Integer> result = Result.success();
                    test.assertThrows(() -> result.onError(null, () -> {}),
                        new PreConditionFailure("errorType cannot be null."));
                });

                runner.test("with null action", (Test test) ->
                {
                    final Result<Integer> result = Result.success();
                    test.assertThrows(() -> result.onError(NullPointerException.class, (Action0)null),
                        new PreConditionFailure("action cannot be null."));
                });

                runner.test("with successful Result", (Test test) ->
                {
                    final Value<Integer> value = Value.create();
                    Result.success()
                        .onError(NullPointerException.class, () -> value.set(5));
                    test.assertFalse(value.hasValue());
                });

                runner.test("with error Result with different error type", (Test test) ->
                {
                    final Value<Integer> value = Value.create();
                    Result.error(new NullPointerException())
                        .onError(NotFoundException.class, () -> value.set(5));
                    test.assertFalse(value.hasValue());
                });

                runner.test("with error Result with same error type", (Test test) ->
                {
                    final Value<Integer> value = Value.create();
                    Result.error(new NullPointerException())
                        .onError(NullPointerException.class, () -> value.set(5));
                    test.assertEqual(5, value.get());
                });

                runner.test("with error Result with super error type", (Test test) ->
                {
                    final Value<Integer> value = Value.create();
                    Result.error(new NullPointerException())
                        .onError(RuntimeException.class, () -> value.set(5));
                    test.assertEqual(5, value.get());
                });
            });

            runner.testGroup("onError(Class<TError>,Action1<TError>)", () ->
            {
                runner.test("with null errorType", (Test test) ->
                {
                    final Result<Integer> result = Result.success();
                    test.assertThrows(() -> result.onError(null, (NullPointerException error) -> {}),
                        new PreConditionFailure("errorType cannot be null."));
                });

                runner.test("with null action", (Test test) ->
                {
                    final Result<Integer> result = Result.success();
                    test.assertThrows(() -> result.onError(NullPointerException.class, (Action1<NullPointerException>)null),
                        new PreConditionFailure("action cannot be null."));
                });

                runner.test("with successful Result", (Test test) ->
                {
                    final Value<Integer> value = Value.create();
                    Result.success()
                        .onError(NullPointerException.class, (NullPointerException error) -> value.set(5));
                    test.assertFalse(value.hasValue());
                });

                runner.test("with error Result with different error type", (Test test) ->
                {
                    final Value<Integer> value = Value.create();
                    Result.error(new NullPointerException())
                        .onError(NotFoundException.class, (NotFoundException error) -> value.set(5));
                    test.assertFalse(value.hasValue());
                });

                runner.test("with error Result with same error type", (Test test) ->
                {
                    final Value<Integer> value = Value.create();
                    Result.error(new NullPointerException("a"))
                        .onError(NullPointerException.class, (NullPointerException error) ->
                        {
                            test.assertEqual(new NullPointerException("a"), error);
                            value.set(5);
                        });
                    test.assertEqual(5, value.get());
                });

                runner.test("with error Result with super error type", (Test test) ->
                {
                    final Value<Integer> value = Value.create();
                    Result.error(new NullPointerException("a"))
                        .onError(RuntimeException.class, (RuntimeException error) ->
                        {
                            test.assertEqual(new NullPointerException("a"), error);
                            value.set(5);
                        });
                    test.assertEqual(5, value.get());
                });
            });

            runner.testGroup("toString()", () ->
            {
                runner.test("with successful result with null value", (Test test) ->
                {
                    test.assertEqual("value: null", Result.success(null).toString());
                });

                runner.test("with successful result with non-null value", (Test test) ->
                {
                    test.assertEqual("value: 6", Result.success(6).toString());
                });

                runner.test("with error result", (Test test) ->
                {
                    test.assertEqual("error: java.lang.NullPointerException: oops", Result.error(new NullPointerException("oops")).toString());
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

            runner.testGroup("runWhile(Function0<Boolean>,Action0)", () ->
            {
                runner.test("with null condition", (Test test) ->
                {
                    test.assertThrows(() -> Result.runWhile(null, Action0.empty));
                });

                runner.test("with null body", (Test test) ->
                {
                    test.assertThrows(() -> Result.runWhile(() -> true, null));
                });

                runner.test("with condition that returns false", (Test test) ->
                {
                    final Value<Integer> value = new Value<>(0);
                    final Result<Void> result = Result.runWhile(() -> false, () -> value.set(5));
                    test.assertSuccess(result);
                    test.assertEqual(0, value.get());
                });

                runner.test("with condition that returns null", (Test test) ->
                {
                    final Value<Integer> value = new Value<>(0);
                    final Result<Void> result = Result.runWhile(() -> null, () -> value.set(5));
                    test.assertSuccess(result);
                    test.assertEqual(0, value.get());
                });

                runner.test("with condition that throws", (Test test) ->
                {
                    final Value<Integer> value = new Value<>(0);
                    final Result<Void> result = Result.runWhile(() -> { throw new RuntimeException("xyz"); }, () -> value.set(5));
                    test.assertError(new RuntimeException("xyz"), result);
                    test.assertEqual(0, value.get());
                });

                runner.test("with body that throws", (Test test) ->
                {
                    final Result<Void> result = Result.runWhile(() -> true, () ->
                    {
                        throw new RuntimeException("abc");
                    });
                    test.assertError(new RuntimeException("abc"), result);
                });

                runner.test("with condition that runs body a few times", (Test test) ->
                {
                    final Value<Integer> value = new Value<>(0);
                    final Result<Void> result = Result.runWhile(() -> value.get() < 5, () -> value.set(value.get() + 1));
                    test.assertSuccess(result);
                    test.assertEqual(5, value.get());
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
