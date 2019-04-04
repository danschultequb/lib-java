package qub;

public class ResultTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(Result.class, () ->
        {
            runner.testGroup("await()", () ->
            {
                runner.test("with no error", (Test test) ->
                {
                    test.assertEqual(null, Result.success().await());
                });

                runner.test("with RuntimeException", (Test test) ->
                {
                    test.assertThrows(() -> Result.error(new RuntimeException("abc")).await(), new AwaitException(new RuntimeException("abc")));
                });

                runner.test("with Exception", (Test test) ->
                {
                    test.assertThrows(() -> Result.error(new Exception("abc")).await(), new AwaitException(new Exception("abc")));
                });
            });

            runner.testGroup("await()", () ->
            {
                runner.test("with no error", (Test test) ->
                {
                    test.assertEqual(null, Result.success().await());
                });

                runner.test("with RuntimeException", (Test test) ->
                {
                    test.assertThrows(() -> Result.error(new RuntimeException("abc")).await(), new RuntimeException("abc"));
                });

                runner.test("with Exception", (Test test) ->
                {
                    test.assertThrows(() -> Result.error(new Exception("abc")).await(), new RuntimeException(new Exception("abc")));
                });
            });

            runner.testGroup("then(Action0)", () ->
            {
                runner.test("with null action", (Test test) ->
                {
                    test.assertThrows(() -> Result.success().then((Action0)null), new PreConditionFailure("action cannot be null."));
                });

                runner.test("with non-error Result and non-throwing action", (Test test) ->
                {
                    final Result<Boolean> result1 = Result.successTrue();
                    final Value<Boolean> value = Value.create();
                    final Result<Void> result2 = result1.then(() -> value.set(false));
                    test.assertNull(result2.await());
                    test.assertEqual(false, value.get());
                });

                runner.test("with non-error Result and throwing action", (Test test) ->
                {
                    final Result<Boolean> result1 = Result.successTrue();
                    final Result<Void> result2 = result1.then((Action0)() ->
                    {
                        throw new RuntimeException("foo");
                    });
                    test.assertThrows(result2::await, new RuntimeException("foo"));
                });

                runner.test("with error Result and non-throwing action", (Test test) ->
                {
                    final Result<Character> result1 = Result.error(new RuntimeException("blah"));
                    final Value<Character> value = Value.create();
                    final Result<Void> result2 = result1.then(() -> value.set('z'));
                    test.assertError(new RuntimeException("blah"), result2);
                    test.assertFalse(value.hasValue());
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
                    test.assertThrows(() -> Result.successTrue().then((Action1<Boolean>)null), new PreConditionFailure("action cannot be null."));
                });

                runner.test("with non-error Result and non-throwing action", (Test test) ->
                {
                    final Result<Boolean> result1 = Result.successTrue();
                    final Value<Boolean> value = Value.create();
                    final Result<Void> result2 = result1.then(value::set);
                    test.assertSuccess(null, result2);
                    test.assertTrue(value.hasValue());
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
                    final Value<Character> value = Value.create();
                    final Result<Void> result2 = result1.then(value::set);
                    test.assertError(new RuntimeException("blah"), result2);
                    test.assertFalse(value.hasValue());
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
                    test.assertThrows(() -> Result.successTrue().then((Function0<Integer>)null), new PreConditionFailure("function cannot be null."));
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
                    test.assertThrows(() -> Result.successTrue().thenResult((Function0<Result<Integer>>)null), new PreConditionFailure("function cannot be null."));
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
                    test.assertThrows(() -> Result.successTrue().then((Function1<Boolean,Integer>)null), new PreConditionFailure("function cannot be null."));
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
                    test.assertThrows(() -> Result.successTrue().then((Function1<Boolean,Result<Integer>>)null), new PreConditionFailure("function cannot be null."));
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
                    final Value<Integer> value = Value.create(0);
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
                    final Value<Integer> value = Value.create(0);
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
                    final Value<Integer> value = Value.create(0);
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
                    final Value<Integer> value = Value.create(0);
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
                    final Value<Integer> value = Value.create(0);
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
                    final Value<Integer> value = Value.create(0);
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
                    final Value<Integer> value = Value.create(0);
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
                    final Value<Integer> value = Value.create(0);
                    final Result<Boolean> result2 = result1.catchError(NullPointerException.class, (Action0)() -> value.set(5));
                    test.assertSame(result2, result1);
                    test.assertEqual(0, value.get());
                });

                runner.test("with error Result with RuntimeException-wrapped wrong error type and non-throwing action", (Test test) ->
                {
                    final Result<Boolean> result1 = Result.error(new RuntimeException(new RuntimeException("abc")));
                    final Value<Integer> value = Value.create(0);
                    final Result<Boolean> result2 = result1.catchError(NullPointerException.class, (Action0)() -> value.set(5));
                    test.assertSame(result2, result1);
                    test.assertEqual(0, value.get());
                });

                runner.test("with error Result with AwaitException-wrapped wrong error type and non-throwing action", (Test test) ->
                {
                    final Result<Boolean> result1 = Result.error(new RuntimeException(new RuntimeException("abc")));
                    final Value<Integer> value = Value.create(0);
                    final Result<Boolean> result2 = result1.catchError(NullPointerException.class, (Action0)() -> value.set(5));
                    test.assertSame(result1, result2);
                    test.assertEqual(0, value.get());
                });

                runner.test("with error Result with correct error type and non-throwing action", (Test test) ->
                {
                    final Result<Boolean> result1 = Result.error(new RuntimeException("abc"));
                    final Value<Integer> value = Value.create(0);
                    final Result<Boolean> result2 = result1.catchError(RuntimeException.class, (Action0)() -> value.set(5));
                    test.assertNull(result2.await());
                    test.assertEqual(5, value.get());
                });

                runner.test("with error Result with RuntimeException-wrapped correct error type and non-throwing action", (Test test) ->
                {
                    final Result<Boolean> result1 = Result.error(new RuntimeException(new NotFoundException("abc")));
                    final Value<Integer> value = Value.create(0);
                    final Result<Boolean> result2 = result1.catchError(NotFoundException.class, (Action0)() -> value.set(5));
                    test.assertNull(result2.await());
                    test.assertEqual(5, value.get());
                });

                runner.test("with error Result with AwaitException-wrapped correct error type and non-throwing action", (Test test) ->
                {
                    final Result<Boolean> result1 = Result.error(new AwaitException(new NotFoundException("abc")));
                    final Value<Integer> value = Value.create(0);
                    final Result<Boolean> result2 = result1.catchError(NotFoundException.class, (Action0)() -> value.set(5));
                    test.assertNull(result2.await());
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
                    final Value<Integer> value = Value.create(0);
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
                    final Value<Integer> value = Value.create(0);
                    final Result<Boolean> result2 = result1.catchError(NullPointerException.class, (Action1<NullPointerException>)error -> value.set(5));
                    test.assertSame(result2, result1);
                    test.assertEqual(0, value.get());
                });

                runner.test("with error Result with correct error type and non-throwing action", (Test test) ->
                {
                    final Result<Boolean> result1 = Result.error(new RuntimeException("abc"));
                    final Value<Integer> value = Value.create(0);
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
                    final Value<Integer> value = Value.create(0);
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
                    final Value<Integer> value = Value.create(0);
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
                    final Value<Integer> value = Value.create(0);
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
                    final Value<Integer> value = Value.create(0);
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
                    final Value<Integer> value = Value.create(0);
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
                    final Value<Integer> value = Value.create(0);
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
                    final Value<Integer> value = Value.create(0);
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
                    final Value<Integer> value = Value.create(0);
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
                    final Value<Integer> value = Value.create(0);
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
                    final Value<Integer> value = Value.create(0);
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
                    final Value<Integer> value = Value.create(0);
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
                    final Value<Integer> value = Value.create(0);
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
                    final Value<Integer> value = Value.create(0);
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
                    final Value<Integer> value = Value.create(0);
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
                    final Value<Integer> value = Value.create(0);
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
                    final Value<Integer> value = Value.create(0);
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
                    final Value<Integer> value = Value.create(0);
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
                    final Value<Integer> value = Value.create(0);
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
                    final Value<Integer> value = Value.create(0);
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
                    final Value<Integer> value = Value.create(0);
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
                    final Value<Integer> value = Value.create(0);
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
                    final Value<Integer> value = Value.create(0);
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
                    final Value<Integer> value = Value.create(0);
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
                    final Value<Integer> value = Value.create(0);
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

            runner.testGroup("convertError(Function0<TErrorOut>)", () ->
            {
                runner.test("with null function", (Test test) ->
                {
                    final Result<Void> result = Result.success();
                    test.assertThrows(() -> result.convertError((Function0<NotFoundException>)null),
                        new PreConditionFailure("function cannot be null."));
                });

                runner.test("with successful Result", (Test test) ->
                {
                    final Result<Void> result = Result.success();
                    test.assertNull(result.convertError(() -> new NotFoundException("blah")).await());
                });

                runner.test("with error Result", (Test test) ->
                {
                    final Result<Void> result = Result.error(new NotFoundException("blah"));
                    test.assertThrows(() -> result.convertError(() -> new NotFoundException("blah2")).await(),
                        new NotFoundException("blah2"));
                });
            });

            runner.testGroup("convertError(Function1<Throwable,TErrorOut>)", () ->
            {
                runner.test("with null function", (Test test) ->
                {
                    final Result<Void> result = Result.success();
                    test.assertThrows(() -> result.convertError((Function1<Throwable,NotFoundException>)null),
                        new PreConditionFailure("function cannot be null."));
                });

                runner.test("with successful Result", (Test test) ->
                {
                    final Result<Void> result = Result.success();
                    final Value<Throwable> caughtError = Value.create();
                    test.assertNull(result.convertError((Throwable error) ->
                        {
                            caughtError.set(error);
                            return new NotFoundException("blah");
                        })
                        .await());
                    test.assertFalse(caughtError.hasValue());
                });

                runner.test("with error Result", (Test test) ->
                {
                    final Result<Void> result = Result.error(new NotFoundException("blah"));
                    final Value<Throwable> caughtError = Value.create();
                    test.assertThrows(() -> result.convertError((Throwable error) ->
                        {
                            caughtError.set(error);
                            return new NotFoundException("blah2");
                        })
                        .await(),
                        new NotFoundException("blah2"));
                    test.assertEqual(new NotFoundException("blah"), caughtError.get());
                });
            });

            runner.testGroup("convertError(Class<TErrorIn>,Function0<TErrorOut>)", () ->
            {
                runner.test("with null errorType", (Test test) ->
                {
                    final Result<Void> result = Result.success();
                    test.assertThrows(() -> result.convertError(null, () -> new NotFoundException("blah")),
                        new PreConditionFailure("errorType cannot be null."));
                });

                runner.test("with null function", (Test test) ->
                {
                    final Result<Void> result = Result.success();
                    test.assertThrows(() -> result.convertError(NotFoundException.class, (Function0<NotFoundException>)null),
                        new PreConditionFailure("function cannot be null."));
                });

                runner.test("with successful Result", (Test test) ->
                {
                    final Result<Void> result = Result.success();
                    test.assertNull(result.convertError(NotFoundException.class, () -> new NotFoundException("blah")).await());
                });

                runner.test("with error Result with different error type", (Test test) ->
                {
                    final Result<Void> result = Result.error(new NotFoundException("blah"));
                    test.assertThrows(() -> result.convertError(NullPointerException.class, () -> new EndOfStreamException()).await(),
                        new NotFoundException("blah"));
                });

                runner.test("with error Result with same error type", (Test test) ->
                {
                    final Result<Void> result = Result.error(new NotFoundException("blah"));
                    test.assertThrows(() -> result.convertError(NotFoundException.class, () -> new EndOfStreamException()).await(),
                        new EndOfStreamException());
                });

                runner.test("with error Result with parent error type", (Test test) ->
                {
                    final Result<Void> result = Result.error(new NotFoundException("blah"));
                    test.assertThrows(() -> result.convertError(RuntimeException.class, () -> new EndOfStreamException()).await(),
                        new EndOfStreamException());
                });
            });

            runner.testGroup("convertError(Class<TErrorIn>,Function1<TErrorIn,TErrorOut>)", () ->
            {
                runner.test("with null errorType", (Test test) ->
                {
                    final Result<Void> result = Result.success();
                    test.assertThrows(() -> result.convertError(null, (NullPointerException error) -> new NotFoundException("blah")),
                        new PreConditionFailure("errorType cannot be null."));
                });

                runner.test("with null function", (Test test) ->
                {
                    final Result<Void> result = Result.success();
                    test.assertThrows(() -> result.convertError(NullPointerException.class, (Function1<NullPointerException,NotFoundException>)null),
                        new PreConditionFailure("function cannot be null."));
                });

                runner.test("with successful Result", (Test test) ->
                {
                    final Result<Void> result = Result.success();
                    final Value<Throwable> caughtError = Value.create();
                    test.assertNull(result.convertError(NullPointerException.class, (NullPointerException error) ->
                    {
                        caughtError.set(error);
                        return new NotFoundException("blah");
                    })
                        .await());
                    test.assertFalse(caughtError.hasValue());
                });

                runner.test("with error Result with different error type", (Test test) ->
                {
                    final Result<Void> result = Result.error(new NullPointerException("blah"));
                    final Value<Throwable> caughtError = Value.create();
                    test.assertThrows(() -> result.convertError(EndOfStreamException.class, (EndOfStreamException error) ->
                        {
                            caughtError.set(error);
                            return new NotFoundException("blah2");
                        })
                            .await(),
                        new NullPointerException("blah"));
                    test.assertFalse(caughtError.hasValue());
                });

                runner.test("with error Result with same error type", (Test test) ->
                {
                    final Result<Void> result = Result.error(new NullPointerException("blah"));
                    final Value<NullPointerException> caughtError = Value.create();
                    test.assertThrows(() -> result.convertError(NullPointerException.class, (NullPointerException error) ->
                        {
                            caughtError.set(error);
                            return new NotFoundException("blah2");
                        })
                            .await(),
                        new NotFoundException("blah2"));
                    test.assertEqual(new NullPointerException("blah"), caughtError.get());
                });

                runner.test("with error Result with parent error type", (Test test) ->
                {
                    final Result<Void> result = Result.error(new NullPointerException("blah"));
                    final Value<Throwable> caughtError = Value.create();
                    test.assertThrows(() -> result.convertError(RuntimeException.class, (RuntimeException error) ->
                        {
                            caughtError.set(error);
                            return new NotFoundException("blah2");
                        })
                            .await(),
                        new NotFoundException("blah2"));
                    test.assertEqual(new NullPointerException("blah"), caughtError.get());
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
                    final Result<Integer> result = Result.error(new RuntimeException("abc"));
                    test.assertNotNull(result);
                    test.assertThrows(result::await, new RuntimeException("abc"));
                });

                runner.test("with Exception", (Test test) ->
                {
                    final Result<Integer> result = Result.error(new Exception("abc"));
                    test.assertNotNull(result);
                    test.assertThrows(result::await, new RuntimeException(new Exception("abc")));
                });
            });
        });
    }
}
