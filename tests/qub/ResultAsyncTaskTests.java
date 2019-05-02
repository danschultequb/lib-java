package qub;

public interface ResultAsyncTaskTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(ResultAsyncTask.class, () ->
        {
            runner.testGroup("constructor(AsyncScheduler,Action0)", () ->
            {
                runner.test("with null AsyncScheduler", (Test test) ->
                {
                    test.assertThrows(() -> new ResultAsyncTask<Integer>(null, () -> {}),
                        new PreConditionFailure("asyncScheduler cannot be null."));
                });

                runner.test("with null action", (Test test) ->
                {
                    test.assertThrows(() -> new ResultAsyncTask<Integer>(new ResultManualAsyncRunner(), (Action0)null),
                        new PreConditionFailure("action cannot be null."));
                });

                runner.test("with non-null action", (Test test) ->
                {
                    final Value<Integer> value = Value.create();
                    final ResultAsyncTask<Boolean> asyncTask = new ResultAsyncTask<>(new ResultManualAsyncRunner(), () -> value.set(5));
                    test.assertNotNull(asyncTask);
                    test.assertFalse(value.hasValue());
                    test.assertEqual(Iterable.create(), asyncTask.getNextTasks());
                });
            });

            runner.testGroup("constructor(AsyncScheduler,Result<?>,Action0)", () ->
            {
                runner.test("with null AsyncScheduler", (Test test) ->
                {
                    test.assertThrows(() -> new ResultAsyncTask<Integer>(null, Result.success(), () -> {}),
                        new PreConditionFailure("asyncScheduler cannot be null."));
                });

                runner.test("with null parentResult", (Test test) ->
                {
                    test.assertThrows(() -> new ResultAsyncTask<>(new ResultManualAsyncRunner(), null, () -> {}),
                        new PreConditionFailure("parentResult cannot be null."));
                });

                runner.test("with null action", (Test test) ->
                {
                    test.assertThrows(() -> new ResultAsyncTask<Integer>(null, Result.success(), (Action0)null),
                        new PreConditionFailure("asyncScheduler cannot be null."));
                });
            });

            runner.testGroup("constructor(AsyncScheduler,Function0<T>)", () ->
            {
                runner.test("with null AsyncScheduler", (Test test) ->
                {
                    test.assertThrows(() -> new ResultAsyncTask<>(null, () -> 11),
                        new PreConditionFailure("asyncScheduler cannot be null."));
                });

                runner.test("with null function", (Test test) ->
                {
                    test.assertThrows(() -> new ResultAsyncTask<>(new ResultManualAsyncRunner(), (Function0<Boolean>)null),
                        new PreConditionFailure("function cannot be null."));
                });

                runner.test("with non-null function", (Test test) ->
                {
                    final Value<Integer> value = Value.create();
                    final ResultAsyncTask<Integer> asyncTask = new ResultAsyncTask<>(new ResultManualAsyncRunner(), () ->
                    {
                        value.set(5);
                        return 7;
                    });
                    test.assertNotNull(asyncTask);
                    test.assertFalse(value.hasValue());
                    test.assertEqual(Iterable.create(), asyncTask.getNextTasks());
                });
            });

            runner.testGroup("constructor(AsyncScheduler,Result<?>,Function0<T>)", () ->
            {
                runner.test("with null AsyncScheduler", (Test test) ->
                {
                    test.assertThrows(() -> new ResultAsyncTask<>(null, Result.success(), () -> 11),
                        new PreConditionFailure("asyncScheduler cannot be null."));
                });

                runner.test("with null parentResult", (Test test) ->
                {
                    test.assertThrows(() -> new ResultAsyncTask<>(new ResultManualAsyncRunner(), null, () -> 8),
                        new PreConditionFailure("parentResult cannot be null."));
                });

                runner.test("with null function", (Test test) ->
                {
                    test.assertThrows(() -> new ResultAsyncTask<>(new ResultManualAsyncRunner(), Result.success(), (Function0<Integer>)null),
                        new PreConditionFailure("function cannot be null."));
                });
            });

            runner.testGroup("await()", () ->
            {
                runner.test("when AsyncTask hasn't been awaited yet", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value = IntegerValue.create(0);
                    final ResultAsyncTask<Void> asyncTask = asyncRunner.schedule((Action0)value::increment);

                    test.assertNull(asyncTask.await());
                    test.assertEqual(1, value.get());
                });

                runner.test("when AsyncTask has already been awaited", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value = IntegerValue.create(0);
                    final ResultAsyncTask<Void> asyncTask = asyncRunner.schedule((Action0)value::increment);

                    test.assertNull(asyncTask.await());
                    test.assertEqual(1, value.get());

                    test.assertNull(asyncTask.await());
                    test.assertEqual(1, value.get());
                });

                runner.test("when the action throws", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() -> { throw new NullPointerException("abc"); });
                    test.assertThrows(asyncTask::await,
                        new AwaitException(new NullPointerException("abc")));
                });

                runner.test("when the action throws and the AsyncTask has already been awaited", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() -> { throw new NullPointerException("abc"); });
                    test.assertThrows(asyncTask::await,
                        new AwaitException(new NullPointerException("abc")));
                    test.assertThrows(asyncTask::await,
                        new AwaitException(new NullPointerException("abc")));
                });

                runner.test("when parentResult doesn't throw but the AsyncTask expects an AwaitException", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final ResultAsyncTask<Void> parentResult = asyncRunner.schedule(() -> {});
                    final IntegerValue value = IntegerValue.create(0);
                    final Value<Throwable> value2 = Value.create();
                    final ResultAsyncTask<Void> asyncTask = parentResult.catchError(AwaitException.class, (AwaitException parentError) ->
                        {
                            value.increment();
                            value2.set(parentError);
                        });
                    test.assertNull(asyncTask.await());
                    test.assertEqual(0, value.get());
                    test.assertFalse(value2.hasValue());
                });

                runner.test("when the parentResult throws and the expectedErrorType is AwaitException", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final ResultAsyncTask<Void> parentResult = asyncRunner.schedule(() -> { throw new FolderNotFoundException("abc"); });
                    final IntegerValue value = IntegerValue.create(0);
                    final Value<Throwable> value2 = Value.create();
                    final ResultAsyncTask<Void> asyncTask = parentResult.catchError(AwaitException.class, (AwaitException parentError) ->
                        {
                            value.increment();
                            value2.set(parentError);
                        });
                    test.assertNull(asyncTask.await());
                    test.assertEqual(1, value.get());
                    test.assertEqual(new AwaitException(new FolderNotFoundException("abc")), value2.get());
                });

                runner.test("when the parentResult throws and the expectedErrorType is RuntimeException", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final ResultAsyncTask<Void> parentResult = asyncRunner.schedule(() -> { throw new FolderNotFoundException("abc"); });
                    final IntegerValue value = IntegerValue.create(0);
                    final Value<Throwable> value2 = Value.create();
                    final ResultAsyncTask<Void> asyncTask = parentResult.catchError(RuntimeException.class, (RuntimeException parentError) ->
                        {
                            value.increment();
                            value2.set(parentError);
                        });
                    test.assertNull(asyncTask.await());
                    test.assertEqual(1, value.get());
                    test.assertEqual(new AwaitException(new FolderNotFoundException("abc")), value2.get());
                });
            });

            runner.testGroup("await(Class<TError>)", () ->
            {
                runner.test("with null expectedErrorType", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value = IntegerValue.create(0);
                    final ResultAsyncTask<Void> asyncTask = asyncRunner.schedule((Action0)value::increment);
                    test.assertThrows(() -> asyncTask.await(null),
                        new PreConditionFailure("expectedErrorType cannot be null."));
                });

                runner.test("when AsyncTask hasn't been awaited yet", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value = IntegerValue.create(0);
                    final ResultAsyncTask<Void> asyncTask = asyncRunner.schedule((Action0)value::increment);

                    test.assertNull(asyncTask.await(NotFoundException.class));
                    test.assertEqual(1, value.get());
                });

                runner.test("when AsyncTask has already been awaited", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value = IntegerValue.create(0);
                    final ResultAsyncTask<Void> asyncTask = asyncRunner.schedule((Action0)value::increment);

                    test.assertNull(asyncTask.await(NotFoundException.class));
                    test.assertEqual(1, value.get());

                    test.assertNull(asyncTask.await(NotFoundException.class));
                    test.assertEqual(1, value.get());
                });

                runner.test("when the action throws a different error type", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() -> { throw new NullPointerException("abc"); });
                    test.assertThrows(() -> asyncTask.await(NotFoundException.class),
                        new AwaitException(new NullPointerException("abc")));
                });

                runner.test("when the action throws a different error type and the AsyncTask has already been awaited", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() -> { throw new NullPointerException("abc"); });
                    test.assertThrows(() -> asyncTask.await(NotFoundException.class),
                        new AwaitException(new NullPointerException("abc")));
                    test.assertThrows(() -> asyncTask.await(NotFoundException.class),
                        new AwaitException(new NullPointerException("abc")));
                });

                runner.test("when the action throws the same error type", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() -> { throw new NotFoundException("abc"); });
                    test.assertThrows(() -> asyncTask.await(NotFoundException.class),
                        new NotFoundException("abc"));
                });

                runner.test("when the action throws the same error type and the AsyncTask has already been awaited", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() -> { throw new NotFoundException("abc"); });
                    test.assertThrows(() -> asyncTask.await(NotFoundException.class),
                        new NotFoundException("abc"));
                    test.assertThrows(() -> asyncTask.await(NotFoundException.class),
                        new NotFoundException("abc"));
                });

                runner.test("when the action throws matching error type", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() -> { throw new FileNotFoundException("abc"); });
                    test.assertThrows(() -> asyncTask.await(NotFoundException.class),
                        new FileNotFoundException("abc"));
                });

                runner.test("when the action throws matching error type and the AsyncTask has already been awaited", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() -> { throw new FileNotFoundException("abc"); });
                    test.assertThrows(() -> asyncTask.await(NotFoundException.class),
                        new FileNotFoundException("abc"));
                    test.assertThrows(() -> asyncTask.await(NotFoundException.class),
                        new FileNotFoundException("abc"));
                });

                runner.test("when parentResult doesn't throw but the AsyncTask expects an AwaitException", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final ResultAsyncTask<Void> parentResult = asyncRunner.schedule(() -> {});
                    final IntegerValue value = IntegerValue.create(0);
                    final Value<Throwable> value2 = Value.create();
                    final ResultAsyncTask<Void> asyncTask = parentResult.catchError(AwaitException.class, (AwaitException parentError) ->
                        {
                            value.increment();
                            value2.set(parentError);
                        });
                    test.assertNull(asyncTask.await(AwaitException.class));
                    test.assertEqual(0, value.get());
                    test.assertFalse(value2.hasValue());
                });

                runner.test("when the parentResult throws and the expectedErrorType is AwaitException", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final ResultAsyncTask<Void> parentResult = asyncRunner.schedule(() -> { throw new FolderNotFoundException("abc"); });
                    final IntegerValue value = IntegerValue.create(0);
                    final Value<Throwable> value2 = Value.create();
                    final ResultAsyncTask<Void> asyncTask = parentResult.catchError(AwaitException.class, (AwaitException parentError) ->
                    {
                        value.increment();
                        value2.set(parentError);
                    });
                    test.assertNull(asyncTask.await(AwaitException.class));
                    test.assertEqual(1, value.get());
                    test.assertEqual(new AwaitException(new FolderNotFoundException("abc")), value2.get());
                });

                runner.test("when the parentResult throws and the expectedErrorType is RuntimeException", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final ResultAsyncTask<Void> parentResult = asyncRunner.schedule(() -> { throw new FolderNotFoundException("abc"); });
                    final IntegerValue value = IntegerValue.create(0);
                    final Value<Throwable> value2 = Value.create();
                    final ResultAsyncTask<Void> asyncTask = parentResult.catchError(RuntimeException.class, (RuntimeException parentError) ->
                    {
                        value.increment();
                        value2.set(parentError);
                    });
                    test.assertNull(asyncTask.await(RuntimeException.class));
                    test.assertEqual(1, value.get());
                    test.assertEqual(new AwaitException(new FolderNotFoundException("abc")), value2.get());
                });

                runner.test("when the parentResult throws an unhandled and unexpected error type", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final ResultAsyncTask<Void> parentResult = asyncRunner.schedule(() -> { throw new FolderNotFoundException("abc"); });
                    final IntegerValue value = IntegerValue.create(0);
                    final Value<Throwable> value2 = Value.create();
                    final ResultAsyncTask<Void> asyncTask = parentResult.catchError(QueueEmptyException.class, (QueueEmptyException parentError) ->
                    {
                        value.increment();
                        value2.set(parentError);
                    });
                    test.assertThrows(() -> asyncTask.await(QueueEmptyException.class), new AwaitException(new FolderNotFoundException("abc")));
                    test.assertEqual(0, value.get());
                    test.assertFalse(value2.hasValue());
                });

                runner.test("when the parentResult throws an unhandled but expected error type", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final ResultAsyncTask<Void> parentResult = asyncRunner.schedule(() -> { throw new FolderNotFoundException("abc"); });
                    final IntegerValue value = IntegerValue.create(0);
                    final Value<Throwable> value2 = Value.create();
                    final ResultAsyncTask<Void> asyncTask = parentResult.catchError(QueueEmptyException.class, (QueueEmptyException parentError) ->
                    {
                        value.increment();
                        value2.set(parentError);
                    });
                    test.assertThrows(() -> asyncTask.await(FolderNotFoundException.class), new FolderNotFoundException("abc"));
                    test.assertEqual(0, value.get());
                    test.assertFalse(value2.hasValue());
                });

                runner.test("when the parentResult throws and the error handler function throws", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final ResultAsyncTask<Void> parentResult = asyncRunner.schedule(() -> { throw new FolderNotFoundException("abc"); });
                    final IntegerValue value = IntegerValue.create(0);
                    final Value<Throwable> value2 = Value.create();
                    final ResultAsyncTask<Void> asyncTask = parentResult.catchError(NotFoundException.class, (Action1<NotFoundException>)(NotFoundException parentError) ->
                    {
                        value.increment();
                        value2.set(parentError);
                        throw new FileNotFoundException("blah");
                    });
                    test.assertThrows(() -> asyncTask.await(FileNotFoundException.class), new FileNotFoundException("blah"));
                    test.assertEqual(1, value.get());
                    test.assertEqual(new FolderNotFoundException("abc"), value2.get());
                });
            });

            runner.testGroup("then(Action0)", () ->
            {
                runner.test("with null action", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value = IntegerValue.create(0);
                    final ResultAsyncTask<Void> asyncTask = asyncRunner.schedule(() -> { value.increment(); });
                    test.assertThrows(() -> asyncTask.then((Action0)null),
                        new PreConditionFailure("action cannot be null."));
                });

                runner.test("with non-null action, await child after parent", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value1 = IntegerValue.create(0);
                    final ResultAsyncTask<Void> asyncTask = asyncRunner.schedule(() -> { value1.increment(); });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final Result<Void> result = asyncTask.then(() -> { value2.increment(); });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(0, value1.get());
                    test.assertEqual(10, value2.get());

                    test.assertNull(asyncTask.await());
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());

                    test.assertNull(result.await());
                    test.assertEqual(1, value1.get());
                    test.assertEqual(11, value2.get());
                });

                runner.test("with non-null action, await child before parent", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value1 = IntegerValue.create(0);
                    final ResultAsyncTask<Void> asyncTask = asyncRunner.schedule(() -> { value1.increment(); });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final Result<Void> result = asyncTask.then(() -> { value2.increment(); });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(0, value1.get());
                    test.assertEqual(10, value2.get());

                    test.assertNull(result.await());
                    test.assertEqual(1, value1.get());
                    test.assertEqual(11, value2.get());

                    test.assertNull(asyncTask.await());
                    test.assertEqual(1, value1.get());
                    test.assertEqual(11, value2.get());
                });

                runner.test("with non-null action, await child after parent when parent throws", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final ResultAsyncTask<Void> asyncTask = asyncRunner.schedule(() -> { throw new FileNotFoundException("blah"); });

                    final IntegerValue value = IntegerValue.create(10);
                    final Result<Void> result = asyncTask.then(() -> { value.increment(); });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(10, value.get());

                    test.assertThrows(asyncTask::await, new FileNotFoundException("blah"));
                    test.assertEqual(10, value.get());

                    test.assertThrows(result::await, new FileNotFoundException("blah"));
                    test.assertEqual(10, value.get());
                });

                runner.test("with non-null action, await child before parent when parent throws", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final ResultAsyncTask<Void> asyncTask = asyncRunner.schedule(() -> { throw new FileNotFoundException("blah"); });

                    final IntegerValue value = IntegerValue.create(10);
                    final Result<Void> result = asyncTask.then(() -> { value.increment(); });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(10, value.get());

                    test.assertThrows(result::await, new FileNotFoundException("blah"));
                    test.assertEqual(10, value.get());

                    test.assertThrows(asyncTask::await, new FileNotFoundException("blah"));
                    test.assertEqual(10, value.get());
                });

                runner.test("with non-null action, await child after parent when child throws", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value = IntegerValue.create(0);
                    final ResultAsyncTask<Void> asyncTask = asyncRunner.schedule(() -> { value.increment(); });

                    final Result<Void> result = asyncTask.then((Action0)() -> { throw new FolderNotFoundException("abc"); });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(0, value.get());

                    test.assertNull(asyncTask.await());
                    test.assertEqual(1, value.get());

                    test.assertThrows(result::await, new FolderNotFoundException("abc"));
                    test.assertEqual(1, value.get());
                });

                runner.test("with non-null action, await child before parent when child throws", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value = IntegerValue.create(0);
                    final ResultAsyncTask<Void> asyncTask = asyncRunner.schedule(() -> { value.increment(); });

                    final Result<Void> result = asyncTask.then((Action0)() -> { throw new FolderNotFoundException("abc"); });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(0, value.get());

                    test.assertThrows(result::await, new FolderNotFoundException("abc"));
                    test.assertEqual(1, value.get());

                    test.assertNull(asyncTask.await());
                    test.assertEqual(1, value.get());
                });
            });

            runner.testGroup("then(Action1<T>)", () ->
            {
                runner.test("with null action", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value = IntegerValue.create(0);
                    final ResultAsyncTask<IntegerValue> asyncTask = asyncRunner.schedule(() -> value.increment());
                    test.assertThrows(() -> asyncTask.then((Action1<IntegerValue>)null),
                        new PreConditionFailure("action cannot be null."));
                });

                runner.test("with non-null action, await child after parent", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value1 = IntegerValue.create(0);
                    final ResultAsyncTask<Integer> asyncTask = asyncRunner.schedule(() ->
                    {
                        value1.increment();
                        return 5;
                    });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final Result<Void> result = asyncTask.then((Integer parentValue) -> { value2.set(value2.get() + parentValue); });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(0, value1.get());
                    test.assertEqual(10, value2.get());

                    test.assertEqual(5, asyncTask.await());
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());

                    test.assertNull(result.await());
                    test.assertEqual(1, value1.get());
                    test.assertEqual(15, value2.get());
                });

                runner.test("with non-null action, await child before parent", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value1 = IntegerValue.create(0);
                    final ResultAsyncTask<Integer> asyncTask = asyncRunner.schedule(() ->
                    {
                        value1.increment();
                        return 5;
                    });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final Result<Void> result = asyncTask.then((Integer parentValue) -> { value2.set(value2.get() + parentValue); });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(0, value1.get());
                    test.assertEqual(10, value2.get());

                    test.assertNull(result.await());
                    test.assertEqual(1, value1.get());
                    test.assertEqual(15, value2.get());

                    test.assertEqual(5, asyncTask.await());
                    test.assertEqual(1, value1.get());
                    test.assertEqual(15, value2.get());
                });

                runner.test("with non-null action, await child after parent when parent throws", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final ResultAsyncTask<Integer> asyncTask = asyncRunner.schedule(() -> { throw new FileNotFoundException("blah"); });

                    final IntegerValue value = IntegerValue.create(10);
                    final Result<Void> result = asyncTask.then((Action1<Integer>)value::plusAssign);
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(10, value.get());

                    test.assertThrows(asyncTask::await, new FileNotFoundException("blah"));
                    test.assertEqual(10, value.get());

                    test.assertThrows(result::await, new FileNotFoundException("blah"));
                    test.assertEqual(10, value.get());
                });

                runner.test("with non-null action, await child before parent when parent throws", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final ResultAsyncTask<Integer> asyncTask = asyncRunner.schedule(() -> { throw new FileNotFoundException("blah"); });

                    final IntegerValue value = IntegerValue.create(10);
                    final Result<Void> result = asyncTask.then((Action1<Integer>)value::plusAssign);
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(10, value.get());

                    test.assertThrows(result::await, new FileNotFoundException("blah"));
                    test.assertEqual(10, value.get());

                    test.assertThrows(asyncTask::await, new FileNotFoundException("blah"));
                    test.assertEqual(10, value.get());
                });

                runner.test("with non-null action, await child after parent when child throws", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value = IntegerValue.create(0);
                    final ResultAsyncTask<Integer> asyncTask = asyncRunner.schedule(() ->
                    {
                        value.increment();
                        return 5;
                    });

                    final Result<Void> result = asyncTask.then((Action1<Integer>)(Integer parentValue) -> { throw new FolderNotFoundException("abc"); });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(0, value.get());

                    test.assertEqual(5, asyncTask.await());
                    test.assertEqual(1, value.get());

                    test.assertThrows(result::await, new FolderNotFoundException("abc"));
                    test.assertEqual(1, value.get());
                });

                runner.test("with non-null action, await child before parent when child throws", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value = IntegerValue.create(0);
                    final ResultAsyncTask<Integer> asyncTask = asyncRunner.schedule(() ->
                    {
                        value.increment();
                        return 5;
                    });

                    final Result<Void> result = asyncTask.then((Action1<Integer>)(Integer parentValue) -> { throw new FolderNotFoundException("abc"); });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(0, value.get());

                    test.assertThrows(result::await, new FolderNotFoundException("abc"));
                    test.assertEqual(1, value.get());

                    test.assertEqual(5, asyncTask.await());
                    test.assertEqual(1, value.get());
                });
            });

            runner.testGroup("then(Function0<U>)", () ->
            {
                runner.test("with null function", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value = IntegerValue.create(0);
                    final ResultAsyncTask<Void> asyncTask = asyncRunner.schedule(() -> { value.increment(); });
                    test.assertThrows(() -> asyncTask.then((Function0<String>)null),
                        new PreConditionFailure("function cannot be null."));
                });

                runner.test("with non-null function, await child after parent", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value1 = IntegerValue.create(0);
                    final ResultAsyncTask<Void> asyncTask = asyncRunner.schedule(() -> { value1.increment(); });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final Result<String> result = asyncTask.then(() -> {
                        value2.increment();
                        return "hello";
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(0, value1.get());
                    test.assertEqual(10, value2.get());

                    test.assertNull(asyncTask.await());
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());

                    test.assertEqual("hello", result.await());
                    test.assertEqual(1, value1.get());
                    test.assertEqual(11, value2.get());
                });

                runner.test("with non-null function, await child before parent", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value1 = IntegerValue.create(0);
                    final ResultAsyncTask<Void> asyncTask = asyncRunner.schedule(() -> { value1.increment(); });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final Result<String> result = asyncTask.then(() -> {
                        value2.increment();
                        return "hello";
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(0, value1.get());
                    test.assertEqual(10, value2.get());

                    test.assertEqual("hello", result.await());
                    test.assertEqual(1, value1.get());
                    test.assertEqual(11, value2.get());

                    test.assertNull(asyncTask.await());
                    test.assertEqual(1, value1.get());
                    test.assertEqual(11, value2.get());
                });

                runner.test("with non-null function, await child after parent when parent throws", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final ResultAsyncTask<Void> asyncTask = asyncRunner.schedule(() -> { throw new FileNotFoundException("blah"); });

                    final IntegerValue value = IntegerValue.create(10);
                    final Result<String> result = asyncTask.then(() ->
                    {
                        value.increment();
                        return "hello";
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(10, value.get());

                    test.assertThrows(asyncTask::await, new FileNotFoundException("blah"));
                    test.assertEqual(10, value.get());

                    test.assertThrows(result::await, new FileNotFoundException("blah"));
                    test.assertEqual(10, value.get());
                });

                runner.test("with non-null function, await child before parent when parent throws", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final ResultAsyncTask<Void> asyncTask = asyncRunner.schedule(() -> { throw new FileNotFoundException("blah"); });

                    final IntegerValue value = IntegerValue.create(10);
                    final Result<String> result = asyncTask.then(() ->
                    {
                        value.increment();
                        return "hello";
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(10, value.get());

                    test.assertThrows(result::await, new FileNotFoundException("blah"));
                    test.assertEqual(10, value.get());

                    test.assertThrows(asyncTask::await, new FileNotFoundException("blah"));
                    test.assertEqual(10, value.get());
                });

                runner.test("with non-null function, await child after parent when child throws", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value = IntegerValue.create(0);
                    final ResultAsyncTask<Void> asyncTask = asyncRunner.schedule(() -> { value.increment(); });

                    final Result<String> result = asyncTask.then((Function0<String>)() -> { throw new FolderNotFoundException("abc"); });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(0, value.get());

                    test.assertNull(asyncTask.await());
                    test.assertEqual(1, value.get());

                    test.assertThrows(result::await, new FolderNotFoundException("abc"));
                    test.assertEqual(1, value.get());
                });

                runner.test("with non-null function, await child before parent when child throws", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value = IntegerValue.create(0);
                    final ResultAsyncTask<Void> asyncTask = asyncRunner.schedule(() -> { value.increment(); });

                    final Result<String> result = asyncTask.then((Function0<String>)() -> { throw new FolderNotFoundException("abc"); });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(0, value.get());

                    test.assertThrows(result::await, new FolderNotFoundException("abc"));
                    test.assertEqual(1, value.get());

                    test.assertNull(asyncTask.await());
                    test.assertEqual(1, value.get());
                });
            });

            runner.testGroup("thenResult(Function0<Result<U>>)", () ->
            {
                runner.test("with null function", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value = IntegerValue.create(0);
                    final ResultAsyncTask<Void> asyncTask = asyncRunner.schedule(() -> { value.increment(); });
                    test.assertThrows(() -> asyncTask.thenResult((Function0<Result<String>>)null),
                        new PreConditionFailure("function cannot be null."));
                });

                runner.test("with non-null function, await child after parent", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value1 = IntegerValue.create(0);
                    final ResultAsyncTask<Void> asyncTask = asyncRunner.schedule(() -> { value1.increment(); });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final IntegerValue value3 = IntegerValue.create(100);
                    final Result<String> result = asyncTask.thenResult(() -> {
                        value2.increment();
                        return asyncRunner.schedule(() ->
                        {
                            value3.increment();
                            return "hello";
                        });
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(0, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertEqual(100, value3.get());

                    test.assertNull(asyncTask.await());
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertEqual(100, value3.get());

                    test.assertEqual("hello", result.await());
                    test.assertEqual(1, value1.get());
                    test.assertEqual(11, value2.get());
                    test.assertEqual(101, value3.get());
                });

                runner.test("with non-null function, await child before parent", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value1 = IntegerValue.create(0);
                    final ResultAsyncTask<Void> asyncTask = asyncRunner.schedule(() -> { value1.increment(); });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final IntegerValue value3 = IntegerValue.create(100);
                    final Result<String> result = asyncTask.thenResult(() -> {
                        value2.increment();
                        return asyncRunner.schedule(() ->
                        {
                            value3.increment();
                            return "hello";
                        });
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(0, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertEqual(100, value3.get());

                    test.assertEqual("hello", result.await());
                    test.assertEqual(1, value1.get());
                    test.assertEqual(11, value2.get());
                    test.assertEqual(101, value3.get());

                    test.assertNull(asyncTask.await());
                    test.assertEqual(1, value1.get());
                    test.assertEqual(11, value2.get());
                    test.assertEqual(101, value3.get());
                });

                runner.test("with non-null function, await child after parent when parent throws", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final ResultAsyncTask<Void> asyncTask = asyncRunner.schedule(() -> { throw new FileNotFoundException("blah"); });

                    final IntegerValue value = IntegerValue.create(10);
                    final IntegerValue value3 = IntegerValue.create(100);
                    final Result<String> result = asyncTask.thenResult(() ->
                    {
                        value.increment();
                        return asyncRunner.schedule(() ->
                        {
                            value3.increment();
                            return "hello";
                        });
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(10, value.get());
                    test.assertEqual(100, value3.get());

                    test.assertThrows(asyncTask::await, new FileNotFoundException("blah"));
                    test.assertEqual(10, value.get());
                    test.assertEqual(100, value3.get());

                    test.assertThrows(result::await, new FileNotFoundException("blah"));
                    test.assertEqual(10, value.get());
                    test.assertEqual(100, value3.get());
                });

                runner.test("with non-null function, await child before parent when parent throws", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final ResultAsyncTask<Void> asyncTask = asyncRunner.schedule(() -> { throw new FileNotFoundException("blah"); });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final IntegerValue value3 = IntegerValue.create(100);
                    final Result<String> result = asyncTask.thenResult(() ->
                    {
                        value2.increment();
                        return asyncRunner.schedule(() ->
                        {
                            value3.increment();
                            return "hello";
                        });
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(10, value2.get());
                    test.assertEqual(100, value3.get());

                    test.assertThrows(result::await, new FileNotFoundException("blah"));
                    test.assertEqual(10, value2.get());
                    test.assertEqual(100, value3.get());

                    test.assertThrows(asyncTask::await, new FileNotFoundException("blah"));
                    test.assertEqual(10, value2.get());
                    test.assertEqual(100, value3.get());
                });

                runner.test("with non-null function, await child after parent when child throws", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value = IntegerValue.create(0);
                    final ResultAsyncTask<Void> asyncTask = asyncRunner.schedule(() -> { value.increment(); });

                    final Result<String> result = asyncTask.thenResult((Function0<Result<String>>)() -> { throw new FolderNotFoundException("abc"); });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(0, value.get());

                    test.assertNull(asyncTask.await());
                    test.assertEqual(1, value.get());

                    test.assertThrows(result::await, new FolderNotFoundException("abc"));
                    test.assertEqual(1, value.get());
                });

                runner.test("with non-null function, await child before parent when child throws", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value = IntegerValue.create(0);
                    final ResultAsyncTask<Void> asyncTask = asyncRunner.schedule(() -> { value.increment(); });

                    final Result<String> result = asyncTask.thenResult((Function0<Result<String>>)() -> { throw new FolderNotFoundException("abc"); });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(0, value.get());

                    test.assertThrows(result::await, new FolderNotFoundException("abc"));
                    test.assertEqual(1, value.get());

                    test.assertNull(asyncTask.await());
                    test.assertEqual(1, value.get());
                });

                runner.test("with non-null function, await child after parent when inner-child throws", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value1 = IntegerValue.create(0);
                    final ResultAsyncTask<Void> asyncTask = asyncRunner.schedule(() -> { value1.increment(); });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final IntegerValue value3 = IntegerValue.create(100);
                    final Result<String> result = asyncTask.thenResult(() ->
                    {
                        value2.increment();
                        return asyncRunner.schedule(() ->
                        {
                            value3.increment();
                            throw new FileNotFoundException("blah");
                        });
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(0, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertEqual(100, value3.get());

                    test.assertNull(asyncTask.await());
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertEqual(100, value3.get());

                    test.assertThrows(result::await, new FileNotFoundException("blah"));
                    test.assertEqual(1, value1.get());
                    test.assertEqual(11, value2.get());
                    test.assertEqual(101, value3.get());
                });

                runner.test("with non-null function, await child before parent when inner-child throws", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value1 = IntegerValue.create(0);
                    final ResultAsyncTask<Void> asyncTask = asyncRunner.schedule(() -> { value1.increment(); });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final IntegerValue value3 = IntegerValue.create(100);
                    final Result<String> result = asyncTask.thenResult(() ->
                    {
                        value2.increment();
                        return asyncRunner.schedule(() ->
                        {
                            value3.increment();
                            throw new FileNotFoundException("blah");
                        });
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(0, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertEqual(100, value3.get());

                    test.assertThrows(result::await, new FileNotFoundException("blah"));
                    test.assertEqual(1, value1.get());
                    test.assertEqual(11, value2.get());
                    test.assertEqual(101, value3.get());

                    test.assertNull(asyncTask.await());
                    test.assertEqual(1, value1.get());
                    test.assertEqual(11, value2.get());
                    test.assertEqual(101, value3.get());
                });
            });

            runner.testGroup("then(Function1<T,U>)", () ->
            {
                runner.test("with null function", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value.increment();
                        return false;
                    });
                    test.assertThrows(() -> asyncTask.then((Function1<Boolean,String>)null),
                        new PreConditionFailure("function cannot be null."));
                });

                runner.test("with non-null function, await child after parent", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value1 = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value1.increment();
                        return true;
                    });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final BooleanValue value3 = BooleanValue.create();
                    final Result<String> result = asyncTask.then((Boolean parentValue) -> {
                        value3.set(parentValue);
                        value2.increment();
                        return "hello";
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(0, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());

                    test.assertTrue(asyncTask.await());
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());

                    test.assertEqual("hello", result.await());
                    test.assertEqual(1, value1.get());
                    test.assertEqual(11, value2.get());
                    test.assertTrue(value3.get());
                });

                runner.test("with non-null function, await child before parent", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value1 = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value1.increment();
                        return false;
                    });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final BooleanValue value3 = BooleanValue.create();
                    final Result<String> result = asyncTask.then((Boolean parentValue) -> {
                        value3.set(parentValue);
                        value2.increment();
                        return "hello";
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(0, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());

                    test.assertEqual("hello", result.await());
                    test.assertEqual(1, value1.get());
                    test.assertEqual(11, value2.get());
                    test.assertFalse(value3.get());

                    test.assertFalse(asyncTask.await());
                    test.assertEqual(1, value1.get());
                    test.assertEqual(11, value2.get());
                    test.assertFalse(value3.get());
                });

                runner.test("with non-null function, await child after parent when parent throws", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() -> { throw new FileNotFoundException("blah"); });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final BooleanValue value3 = BooleanValue.create();
                    final Result<String> result = asyncTask.then((Boolean parentValue) ->
                    {
                        value3.set(parentValue);
                        value2.increment();
                        return "hello";
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());

                    test.assertThrows(asyncTask::await, new FileNotFoundException("blah"));
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());

                    test.assertThrows(result::await, new FileNotFoundException("blah"));
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());
                });

                runner.test("with non-null function, await child before parent when parent throws", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() -> { throw new FileNotFoundException("blah"); });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final BooleanValue value3 = BooleanValue.create();
                    final Result<String> result = asyncTask.then((Boolean parentValue) ->
                    {
                        value3.set(parentValue);
                        value2.increment();
                        return "hello";
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());

                    test.assertThrows(result::await, new FileNotFoundException("blah"));
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());

                    test.assertThrows(asyncTask::await, new FileNotFoundException("blah"));
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());
                });

                runner.test("with non-null function, await child after parent when child throws", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value1 = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value1.increment();
                        return true;
                    });

                    final BooleanValue value3 = BooleanValue.create();
                    final Result<String> result = asyncTask.then((Function1<Boolean,String>)(Boolean parentValue) ->
                    {
                        value3.set(parentValue);
                        throw new FolderNotFoundException("abc");
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(0, value1.get());
                    test.assertFalse(value3.hasValue());

                    test.assertTrue(asyncTask.await());
                    test.assertEqual(1, value1.get());
                    test.assertFalse(value3.hasValue());

                    test.assertThrows(result::await, new FolderNotFoundException("abc"));
                    test.assertEqual(1, value1.get());
                    test.assertTrue(value3.get());
                });

                runner.test("with non-null function, await child before parent when child throws", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value1 = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value1.increment();
                        return true;
                    });

                    final BooleanValue value3 = BooleanValue.create();
                    final Result<String> result = asyncTask.then((Function1<Boolean,String>)(Boolean parentValue) ->
                    {
                        value3.set(parentValue);
                        throw new FolderNotFoundException("abc");
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(0, value1.get());
                    test.assertFalse(value3.hasValue());

                    test.assertThrows(result::await, new FolderNotFoundException("abc"));
                    test.assertEqual(1, value1.get());
                    test.assertTrue(value3.get());

                    test.assertTrue(asyncTask.await());
                    test.assertEqual(1, value1.get());
                    test.assertTrue(value3.get());
                });
            });

            runner.testGroup("thenResult(Function1<T,Result<U>>)", () ->
            {
                runner.test("with null function", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value.increment();
                        return true;
                    });
                    test.assertThrows(() -> asyncTask.thenResult((Function1<Boolean,Result<String>>)null),
                        new PreConditionFailure("function cannot be null."));
                });

                runner.test("with non-null function, await child after parent", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value1 = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value1.increment();
                        return false;
                    });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final IntegerValue value3 = IntegerValue.create(100);
                    final BooleanValue value4 = BooleanValue.create();
                    final Result<String> result = asyncTask.thenResult((Boolean parentValue) -> {
                        value2.increment();
                        value4.set(parentValue);
                        return asyncRunner.schedule(() ->
                        {
                            value3.increment();
                            return "hello";
                        });
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(0, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertEqual(100, value3.get());
                    test.assertFalse(value4.hasValue());

                    test.assertFalse(asyncTask.await());
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertEqual(100, value3.get());
                    test.assertFalse(value4.hasValue());

                    test.assertEqual("hello", result.await());
                    test.assertEqual(1, value1.get());
                    test.assertEqual(11, value2.get());
                    test.assertEqual(101, value3.get());
                    test.assertFalse(value4.get());
                });

                runner.test("with non-null function, await child before parent", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value1 = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value1.increment();
                        return true;
                    });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final IntegerValue value3 = IntegerValue.create(100);
                    final BooleanValue value4 = BooleanValue.create();
                    final Result<String> result = asyncTask.thenResult((Boolean parentValue) -> {
                        value2.increment();
                        value4.set(parentValue);
                        return asyncRunner.schedule(() ->
                        {
                            value3.increment();
                            return "hello";
                        });
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(0, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertEqual(100, value3.get());
                    test.assertFalse(value4.hasValue());

                    test.assertEqual("hello", result.await());
                    test.assertEqual(1, value1.get());
                    test.assertEqual(11, value2.get());
                    test.assertEqual(101, value3.get());
                    test.assertTrue(value4.get());

                    test.assertTrue(asyncTask.await());
                    test.assertEqual(1, value1.get());
                    test.assertEqual(11, value2.get());
                    test.assertEqual(101, value3.get());
                    test.assertTrue(value4.get());
                });

                runner.test("with non-null function, await child after parent when parent throws", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() -> { throw new FileNotFoundException("blah"); });

                    final IntegerValue value = IntegerValue.create(10);
                    final IntegerValue value3 = IntegerValue.create(100);
                    final BooleanValue value4 = BooleanValue.create();
                    final Result<String> result = asyncTask.thenResult((Boolean parentValue) ->
                    {
                        value.increment();
                        value4.set(parentValue);
                        return asyncRunner.schedule(() ->
                        {
                            value3.increment();
                            return "hello";
                        });
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(10, value.get());
                    test.assertEqual(100, value3.get());
                    test.assertFalse(value4.hasValue());

                    test.assertThrows(asyncTask::await, new FileNotFoundException("blah"));
                    test.assertEqual(10, value.get());
                    test.assertEqual(100, value3.get());
                    test.assertFalse(value4.hasValue());

                    test.assertThrows(result::await, new FileNotFoundException("blah"));
                    test.assertEqual(10, value.get());
                    test.assertEqual(100, value3.get());
                    test.assertFalse(value4.hasValue());
                });

                runner.test("with non-null function, await child before parent when parent throws", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() -> { throw new FileNotFoundException("blah"); });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final IntegerValue value3 = IntegerValue.create(100);
                    final BooleanValue value4 = BooleanValue.create();
                    final Result<String> result = asyncTask.thenResult((Boolean parentValue) ->
                    {
                        value2.increment();
                        value4.set(parentValue);
                        return asyncRunner.schedule(() ->
                        {
                            value3.increment();
                            return "hello";
                        });
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(10, value2.get());
                    test.assertEqual(100, value3.get());
                    test.assertFalse(value4.hasValue());

                    test.assertThrows(result::await, new FileNotFoundException("blah"));
                    test.assertEqual(10, value2.get());
                    test.assertEqual(100, value3.get());
                    test.assertFalse(value4.hasValue());

                    test.assertThrows(asyncTask::await, new FileNotFoundException("blah"));
                    test.assertEqual(10, value2.get());
                    test.assertEqual(100, value3.get());
                    test.assertFalse(value4.hasValue());
                });

                runner.test("with non-null function, await child after parent when child throws", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value1 = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value1.increment();
                        return false;
                    });

                    final BooleanValue value4 = BooleanValue.create();
                    final Result<String> result = asyncTask.thenResult((Function1<Boolean,Result<String>>)(Boolean parentValue) ->
                    {
                        value4.set(parentValue);
                        throw new FolderNotFoundException("abc");
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(0, value1.get());
                    test.assertFalse(value4.hasValue());

                    test.assertFalse(asyncTask.await());
                    test.assertEqual(1, value1.get());
                    test.assertFalse(value4.hasValue());

                    test.assertThrows(result::await, new FolderNotFoundException("abc"));
                    test.assertEqual(1, value1.get());
                    test.assertFalse(value4.get());
                });

                runner.test("with non-null function, await child before parent when child throws", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value1 = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value1.increment();
                        return true;
                    });

                    final BooleanValue value4 = BooleanValue.create();
                    final Result<String> result = asyncTask.thenResult((Function1<Boolean,Result<String>>)(Boolean parentValue) ->
                    {
                        value4.set(parentValue);
                        throw new FolderNotFoundException("abc");
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(0, value1.get());
                    test.assertFalse(value4.hasValue());

                    test.assertThrows(result::await, new FolderNotFoundException("abc"));
                    test.assertEqual(1, value1.get());
                    test.assertTrue(value4.get());

                    test.assertTrue(asyncTask.await());
                    test.assertEqual(1, value1.get());
                    test.assertTrue(value4.get());
                });

                runner.test("with non-null function, await child after parent when inner-child throws", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value1 = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value1.increment();
                        return false;
                    });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final IntegerValue value3 = IntegerValue.create(100);
                    final BooleanValue value4 = BooleanValue.create();
                    final Result<String> result = asyncTask.thenResult((Boolean parentValue) ->
                    {
                        value2.increment();
                        value4.set(parentValue);
                        return asyncRunner.schedule(() ->
                        {
                            value3.increment();
                            throw new FileNotFoundException("blah");
                        });
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(0, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertEqual(100, value3.get());
                    test.assertFalse(value4.hasValue());

                    test.assertFalse(asyncTask.await());
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertEqual(100, value3.get());
                    test.assertFalse(value4.hasValue());

                    test.assertThrows(result::await, new FileNotFoundException("blah"));
                    test.assertEqual(1, value1.get());
                    test.assertEqual(11, value2.get());
                    test.assertEqual(101, value3.get());
                    test.assertFalse(value4.get());
                });

                runner.test("with non-null function, await child before parent when inner-child throws", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value1 = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value1.increment();
                        return true;
                    });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final IntegerValue value3 = IntegerValue.create(100);
                    final BooleanValue value4 = BooleanValue.create();
                    final Result<String> result = asyncTask.thenResult((Boolean parentValue) ->
                    {
                        value2.increment();
                        value4.set(parentValue);
                        return asyncRunner.schedule(() ->
                        {
                            value3.increment();
                            throw new FileNotFoundException("blah");
                        });
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(0, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertEqual(100, value3.get());
                    test.assertFalse(value4.hasValue());

                    test.assertThrows(result::await, new FileNotFoundException("blah"));
                    test.assertEqual(1, value1.get());
                    test.assertEqual(11, value2.get());
                    test.assertEqual(101, value3.get());
                    test.assertTrue(value4.get());

                    test.assertTrue(asyncTask.await());
                    test.assertEqual(1, value1.get());
                    test.assertEqual(11, value2.get());
                    test.assertEqual(101, value3.get());
                    test.assertTrue(value4.get());
                });
            });

            runner.testGroup("onValue(Action0)", () ->
            {
                runner.test("with null action", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value = IntegerValue.create(0);
                    final ResultAsyncTask<Void> asyncTask = asyncRunner.schedule(() -> { value.increment(); });
                    test.assertThrows(() -> asyncTask.onValue((Action0)null),
                        new PreConditionFailure("action cannot be null."));
                });

                runner.test("with non-null action, await child after parent", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value1 = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value1.increment();
                        return false;
                    });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final Result<Boolean> result = asyncTask.onValue(() -> { value2.increment(); });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(0, value1.get());
                    test.assertEqual(10, value2.get());

                    test.assertFalse(asyncTask.await());
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());

                    test.assertFalse(result.await());
                    test.assertEqual(1, value1.get());
                    test.assertEqual(11, value2.get());
                });

                runner.test("with non-null action, await child before parent", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value1 = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value1.increment();
                        return true;
                    });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final Result<Boolean> result = asyncTask.onValue(() -> { value2.increment(); });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(0, value1.get());
                    test.assertEqual(10, value2.get());

                    test.assertTrue(result.await());
                    test.assertEqual(1, value1.get());
                    test.assertEqual(11, value2.get());

                    test.assertTrue(asyncTask.await());
                    test.assertEqual(1, value1.get());
                    test.assertEqual(11, value2.get());
                });

                runner.test("with non-null action, await child after parent when parent throws", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() -> { throw new FileNotFoundException("blah"); });

                    final IntegerValue value = IntegerValue.create(10);
                    final Result<Boolean> result = asyncTask.onValue(() -> { value.increment(); });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(10, value.get());

                    test.assertThrows(asyncTask::await, new FileNotFoundException("blah"));
                    test.assertEqual(10, value.get());

                    test.assertThrows(result::await, new FileNotFoundException("blah"));
                    test.assertEqual(10, value.get());
                });

                runner.test("with non-null action, await child before parent when parent throws", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() -> { throw new FileNotFoundException("blah"); });

                    final IntegerValue value = IntegerValue.create(10);
                    final Result<Boolean> result = asyncTask.onValue(() -> { value.increment(); });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(10, value.get());

                    test.assertThrows(result::await, new FileNotFoundException("blah"));
                    test.assertEqual(10, value.get());

                    test.assertThrows(asyncTask::await, new FileNotFoundException("blah"));
                    test.assertEqual(10, value.get());
                });

                runner.test("with non-null action, await child after parent when child throws", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value.increment();
                        return false;
                    });

                    final Result<Boolean> result = asyncTask.onValue((Action0)() -> { throw new FolderNotFoundException("abc"); });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(0, value.get());

                    test.assertFalse(asyncTask.await());
                    test.assertEqual(1, value.get());

                    test.assertThrows(result::await, new FolderNotFoundException("abc"));
                    test.assertEqual(1, value.get());
                });

                runner.test("with non-null action, await child before parent when child throws", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value.increment();
                        return true;
                    });

                    final Result<Boolean> result = asyncTask.onValue((Action0)() -> { throw new FolderNotFoundException("abc"); });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(0, value.get());

                    test.assertThrows(result::await, new FolderNotFoundException("abc"));
                    test.assertEqual(1, value.get());

                    test.assertTrue(asyncTask.await());
                    test.assertEqual(1, value.get());
                });
            });

            runner.testGroup("onValue(Action1<T>)", () ->
            {
                runner.test("with null action", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value.increment();
                        return true;
                    });
                    test.assertThrows(() -> asyncTask.onValue((Action1<Boolean>)null),
                        new PreConditionFailure("action cannot be null."));
                });

                runner.test("with non-null action, await child after parent", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value1 = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value1.increment();
                        return false;
                    });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final BooleanValue value3 = BooleanValue.create();
                    final Result<Boolean> result = asyncTask.onValue((Boolean parentValue) ->
                    {
                        value2.increment();
                        value3.set(parentValue);
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(0, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());

                    test.assertFalse(asyncTask.await());
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());

                    test.assertFalse(result.await());
                    test.assertEqual(1, value1.get());
                    test.assertEqual(11, value2.get());
                    test.assertFalse(value3.get());
                });

                runner.test("with non-null action, await child before parent", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value1 = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value1.increment();
                        return true;
                    });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final BooleanValue value3 = BooleanValue.create();
                    final Result<Boolean> result = asyncTask.onValue((Boolean parentValue) ->
                    {
                        value2.increment();
                        value3.set(parentValue);
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(0, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());

                    test.assertTrue(result.await());
                    test.assertEqual(1, value1.get());
                    test.assertEqual(11, value2.get());
                    test.assertTrue(value3.get());

                    test.assertTrue(asyncTask.await());
                    test.assertEqual(1, value1.get());
                    test.assertEqual(11, value2.get());
                    test.assertTrue(value3.get());
                });

                runner.test("with non-null action, await child after parent when parent throws", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() -> { throw new FileNotFoundException("blah"); });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final BooleanValue value3 = BooleanValue.create();
                    final Result<Boolean> result = asyncTask.onValue((Boolean parentValue) ->
                    {
                        value2.increment();
                        value3.set(parentValue);
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());

                    test.assertThrows(asyncTask::await, new FileNotFoundException("blah"));
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());

                    test.assertThrows(result::await, new FileNotFoundException("blah"));
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());
                });

                runner.test("with non-null action, await child before parent when parent throws", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() -> { throw new FileNotFoundException("blah"); });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final BooleanValue value3 = BooleanValue.create();
                    final Result<Boolean> result = asyncTask.onValue((Boolean parentValue) ->
                    {
                        value2.increment();
                        value3.set(parentValue);
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());

                    test.assertThrows(result::await, new FileNotFoundException("blah"));
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());

                    test.assertThrows(asyncTask::await, new FileNotFoundException("blah"));
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());
                });

                runner.test("with non-null action, await child after parent when child throws", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value.increment();
                        return false;
                    });

                    final BooleanValue value3 = BooleanValue.create();
                    final Result<Boolean> result = asyncTask.onValue((Boolean parentValue) ->
                    {
                        value3.set(parentValue);
                        throw new FolderNotFoundException("abc");
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(0, value.get());
                    test.assertFalse(value3.hasValue());

                    test.assertFalse(asyncTask.await());
                    test.assertEqual(1, value.get());
                    test.assertFalse(value3.hasValue());

                    test.assertThrows(result::await, new FolderNotFoundException("abc"));
                    test.assertEqual(1, value.get());
                    test.assertFalse(value3.get());
                });

                runner.test("with non-null action, await child before parent when child throws", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value.increment();
                        return true;
                    });

                    final BooleanValue value3 = BooleanValue.create();
                    final Result<Boolean> result = asyncTask.onValue((Boolean parentValue) ->
                    {
                        value3.set(parentValue);
                        throw new FolderNotFoundException("abc");
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(0, value.get());
                    test.assertFalse(value3.hasValue());

                    test.assertThrows(result::await, new FolderNotFoundException("abc"));
                    test.assertEqual(1, value.get());
                    test.assertTrue(value3.get());

                    test.assertTrue(asyncTask.await());
                    test.assertEqual(1, value.get());
                    test.assertTrue(value3.get());
                });
            });

            runner.testGroup("catchError(Class<TError>)", () ->
            {
                runner.test("with null errorType", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value.increment();
                        return true;
                    });
                    test.assertThrows(() -> asyncTask.catchError((Class<Throwable>)null),
                        new PreConditionFailure("errorType cannot be null."));
                });

                runner.test("with non-null errorType, await child after parent", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value1 = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value1.increment();
                        return false;
                    });

                    final Result<Boolean> result = asyncTask.catchError(NotFoundException.class);
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(0, value1.get());

                    test.assertFalse(asyncTask.await());
                    test.assertEqual(1, value1.get());

                    test.assertFalse(result.await());
                    test.assertEqual(1, value1.get());
                });

                runner.test("with non-null action, await child before parent", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value1 = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value1.increment();
                        return true;
                    });

                    final Result<Boolean> result = asyncTask.catchError(NotFoundException.class);
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(0, value1.get());

                    test.assertTrue(result.await());
                    test.assertEqual(1, value1.get());

                    test.assertTrue(asyncTask.await());
                    test.assertEqual(1, value1.get());
                });

                runner.test("with non-null action, await child after parent when parent throws", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() -> { throw new FileNotFoundException("blah"); });

                    final Result<Boolean> result = asyncTask.catchError(NotFoundException.class);
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertThrows(asyncTask::await, new FileNotFoundException("blah"));
                    test.assertNull(result.await());
                });

                runner.test("with non-null action, await child before parent when parent throws", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() -> { throw new FileNotFoundException("blah"); });

                    final Result<Boolean> result = asyncTask.catchError(NotFoundException.class);
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertNull(result.await());
                    test.assertThrows(asyncTask::await, new FileNotFoundException("blah"));
                });
            });

            runner.testGroup("catchError(Action0)", () ->
            {
                runner.test("with null action", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value.increment();
                        return true;
                    });
                    test.assertThrows(() -> asyncTask.catchError((Action0)null),
                        new PreConditionFailure("action cannot be null."));
                });

                runner.test("with non-null action, await child after parent", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value1 = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value1.increment();
                        return false;
                    });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final Result<Boolean> result = asyncTask.catchError(() -> { value2.increment(); });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(0, value1.get());
                    test.assertEqual(10, value2.get());

                    test.assertFalse(asyncTask.await());
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());

                    test.assertFalse(result.await());
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());
                });

                runner.test("with non-null action, await child before parent", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value1 = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value1.increment();
                        return true;
                    });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final Result<Boolean> result = asyncTask.catchError(() -> { value2.increment(); });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(0, value1.get());
                    test.assertEqual(10, value2.get());

                    test.assertTrue(result.await());
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());

                    test.assertTrue(asyncTask.await());
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());
                });

                runner.test("with non-null action, await child after parent when parent throws", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() -> { throw new FileNotFoundException("blah"); });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final Result<Boolean> result = asyncTask.catchError(() -> { value2.increment(); });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(10, value2.get());

                    test.assertThrows(asyncTask::await, new FileNotFoundException("blah"));
                    test.assertEqual(10, value2.get());

                    test.assertNull(result.await());
                    test.assertEqual(11, value2.get());
                });

                runner.test("with non-null action, await child before parent when parent throws", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() -> { throw new FileNotFoundException("blah"); });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final Result<Boolean> result = asyncTask.catchError(() -> { value2.increment(); });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(10, value2.get());

                    test.assertNull(result.await());
                    test.assertEqual(11, value2.get());

                    test.assertThrows(asyncTask::await, new FileNotFoundException("blah"));
                    test.assertEqual(11, value2.get());
                });

                runner.test("with non-null action, await child after parent when child throws", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value1 = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value1.increment();
                        return true;
                    });

                    final Result<Boolean> result = asyncTask.catchError((Action0)() -> { throw new FolderNotFoundException("blah"); });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(0, value1.get());

                    test.assertTrue(asyncTask.await());
                    test.assertEqual(1, value1.get());

                    test.assertTrue(result.await());
                    test.assertEqual(1, value1.get());
                });

                runner.test("with non-null action, await child before parent when child throws", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value1 = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value1.increment();
                        return true;
                    });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final Result<Boolean> result = asyncTask.catchError((Action0)() ->
                    {
                        value2.increment();
                        throw new FolderNotFoundException("blah");
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(0, value1.get());
                    test.assertEqual(10, value2.get());

                    test.assertTrue(result.await());
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());

                    test.assertTrue(asyncTask.await());
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());
                });

                runner.test("with non-null action, await child after parent when parent and child throw", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value1 = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value1.increment();
                        throw new FileNotFoundException("abc");
                    });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final Result<Boolean> result = asyncTask.catchError((Action0)() ->
                    {
                        value2.increment();
                        throw new FolderNotFoundException("blah");
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(0, value1.get());
                    test.assertEqual(10, value2.get());

                    test.assertThrows(asyncTask::await, new FileNotFoundException("abc"));
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());

                    test.assertThrows(result::await, new FolderNotFoundException("blah"));
                    test.assertEqual(1, value1.get());
                    test.assertEqual(11, value2.get());
                });

                runner.test("with non-null action, await child before parent when parent and child throw", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value1 = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value1.increment();
                        throw new FileNotFoundException("abc");
                    });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final Result<Boolean> result = asyncTask.catchError((Action0)() ->
                    {
                        value2.increment();
                        throw new FolderNotFoundException("blah");
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(0, value1.get());
                    test.assertEqual(10, value2.get());

                    test.assertThrows(result::await, new FolderNotFoundException("blah"));
                    test.assertEqual(1, value1.get());
                    test.assertEqual(11, value2.get());

                    test.assertThrows(asyncTask::await, new FileNotFoundException("abc"));
                    test.assertEqual(1, value1.get());
                    test.assertEqual(11, value2.get());
                });
            });

            runner.testGroup("catchError(Action1<Throwable>)", () ->
            {
                runner.test("with null action", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value.increment();
                        return true;
                    });
                    test.assertThrows(() -> asyncTask.catchError((Action1<Throwable>)null),
                        new PreConditionFailure("action cannot be null."));
                });

                runner.test("with non-null action, await child after parent", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value1 = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value1.increment();
                        return false;
                    });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final Value<Throwable> value3 = Value.create();
                    final Result<Boolean> result = asyncTask.catchError((Throwable parentError) ->
                    {
                        value2.increment();
                        value3.set(parentError);
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(0, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());

                    test.assertFalse(asyncTask.await());
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());

                    test.assertFalse(result.await());
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());
                });

                runner.test("with non-null action, await child before parent", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value1 = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value1.increment();
                        return true;
                    });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final Value<Throwable> value3 = Value.create();
                    final Result<Boolean> result = asyncTask.catchError((Throwable parentError) ->
                    {
                        value2.increment();
                        value3.set(parentError);
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(0, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());

                    test.assertTrue(result.await());
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());

                    test.assertTrue(asyncTask.await());
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());
                });

                runner.test("with non-null action, await child after parent when parent throws", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() -> { throw new FileNotFoundException("blah"); });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final Value<Throwable> value3 = Value.create();
                    final Result<Boolean> result = asyncTask.catchError((Throwable parentError) ->
                    {
                        value2.increment();
                        value3.set(parentError);
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());

                    test.assertThrows(asyncTask::await, new FileNotFoundException("blah"));
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());

                    test.assertNull(result.await());
                    test.assertEqual(11, value2.get());
                    test.assertEqual(new FileNotFoundException("blah"), value3.get());
                });

                runner.test("with non-null action, await child before parent when parent throws", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() -> { throw new FileNotFoundException("blah"); });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final Value<Throwable> value3 = Value.create();
                    final Result<Boolean> result = asyncTask.catchError((Throwable parentError) ->
                    {
                        value2.increment();
                        value3.set(parentError);
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());

                    test.assertNull(result.await());
                    test.assertEqual(11, value2.get());
                    test.assertEqual(new FileNotFoundException("blah"), value3.get());

                    test.assertThrows(asyncTask::await, new FileNotFoundException("blah"));
                    test.assertEqual(11, value2.get());
                    test.assertEqual(new FileNotFoundException("blah"), value3.get());
                });

                runner.test("with non-null action, await child after parent when child throws", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value1 = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value1.increment();
                        return true;
                    });

                    final Value<Throwable> value3 = Value.create();
                    final Result<Boolean> result = asyncTask.catchError((Action1<Throwable>)(Throwable parentError) ->
                    {
                        value3.set(parentError);
                        throw new FolderNotFoundException("blah");
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(0, value1.get());
                    test.assertFalse(value3.hasValue());

                    test.assertTrue(asyncTask.await());
                    test.assertEqual(1, value1.get());
                    test.assertFalse(value3.hasValue());

                    test.assertTrue(result.await());
                    test.assertEqual(1, value1.get());
                    test.assertFalse(value3.hasValue());
                });

                runner.test("with non-null action, await child before parent when child throws", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value1 = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value1.increment();
                        return true;
                    });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final Value<Throwable> value3 = Value.create();
                    final Result<Boolean> result = asyncTask.catchError((Action1<Throwable>)(Throwable parentError) ->
                    {
                        value2.increment();
                        value3.set(parentError);
                        throw new FolderNotFoundException("blah");
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(0, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());

                    test.assertTrue(result.await());
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());

                    test.assertTrue(asyncTask.await());
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());
                });

                runner.test("with non-null action, await child after parent when parent and child throw", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value1 = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value1.increment();
                        throw new FileNotFoundException("abc");
                    });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final Value<Throwable> value3 = Value.create();
                    final Result<Boolean> result = asyncTask.catchError((Action1<Throwable>)(Throwable parentError) ->
                    {
                        value2.increment();
                        value3.set(parentError);
                        throw new FolderNotFoundException("blah");
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(0, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());

                    test.assertThrows(asyncTask::await, new FileNotFoundException("abc"));
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());

                    test.assertThrows(result::await, new FolderNotFoundException("blah"));
                    test.assertEqual(1, value1.get());
                    test.assertEqual(11, value2.get());
                    test.assertEqual(new FileNotFoundException("abc"), value3.get());
                });

                runner.test("with non-null action, await child before parent when parent and child throw", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value1 = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value1.increment();
                        throw new FileNotFoundException("abc");
                    });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final Value<Throwable> value3 = Value.create();
                    final Result<Boolean> result = asyncTask.catchError((Action1<Throwable>)(Throwable parentError) ->
                    {
                        value2.increment();
                        value3.set(parentError);
                        throw new FolderNotFoundException("blah");
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(0, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());

                    test.assertThrows(result::await, new FolderNotFoundException("blah"));
                    test.assertEqual(1, value1.get());
                    test.assertEqual(11, value2.get());
                    test.assertEqual(new FileNotFoundException("abc"), value3.get());

                    test.assertThrows(asyncTask::await, new FileNotFoundException("abc"));
                    test.assertEqual(1, value1.get());
                    test.assertEqual(11, value2.get());
                    test.assertEqual(new FileNotFoundException("abc"), value3.get());
                });
            });

            runner.testGroup("catchError(Class<TError>,Action0)", () ->
            {
                runner.test("with null errorType", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value.increment();
                        return true;
                    });
                    test.assertThrows(() -> asyncTask.catchError(null, () -> {}),
                        new PreConditionFailure("errorType cannot be null."));
                });

                runner.test("with null action", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value.increment();
                        return false;
                    });
                    test.assertThrows(() -> asyncTask.catchError(FileNotFoundException.class, (Action0)null),
                        new PreConditionFailure("action cannot be null."));
                });

                runner.test("with non-null action, await child after parent", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value1 = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value1.increment();
                        return false;
                    });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final Result<Boolean> result = asyncTask.catchError(FileNotFoundException.class, () ->
                    {
                        value2.increment();
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(0, value1.get());
                    test.assertEqual(10, value2.get());

                    test.assertFalse(asyncTask.await());
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());

                    test.assertFalse(result.await());
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());
                });

                runner.test("with non-null action, await child before parent", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value1 = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value1.increment();
                        return true;
                    });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final Result<Boolean> result = asyncTask.catchError(FileNotFoundException.class, () ->
                    {
                        value2.increment();
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(0, value1.get());
                    test.assertEqual(10, value2.get());

                    test.assertTrue(result.await());
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());

                    test.assertTrue(asyncTask.await());
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());
                });

                runner.test("with non-null action, await child after parent when parent throws matching error", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() -> { throw new FileNotFoundException("blah"); });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final Result<Boolean> result = asyncTask.catchError(FileNotFoundException.class, () ->
                    {
                        value2.increment();
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(10, value2.get());

                    test.assertThrows(asyncTask::await, new FileNotFoundException("blah"));
                    test.assertEqual(10, value2.get());

                    test.assertNull(result.await());
                    test.assertEqual(11, value2.get());
                });

                runner.test("with non-null action, await child before parent when parent throws matching error", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() -> { throw new FileNotFoundException("blah"); });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final Result<Boolean> result = asyncTask.catchError(FileNotFoundException.class, () ->
                    {
                        value2.increment();
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(10, value2.get());

                    test.assertNull(result.await());
                    test.assertEqual(11, value2.get());

                    test.assertThrows(asyncTask::await, new FileNotFoundException("blah"));
                    test.assertEqual(11, value2.get());
                });

                runner.test("with non-null action, await child after parent when parent throws non-matching error", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() -> { throw new FileNotFoundException("blah"); });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final Result<Boolean> result = asyncTask.catchError(FolderNotFoundException.class, () ->
                    {
                        value2.increment();
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(10, value2.get());

                    test.assertThrows(asyncTask::await, new FileNotFoundException("blah"));
                    test.assertEqual(10, value2.get());

                    test.assertThrows(result::await, new FileNotFoundException("blah"));
                    test.assertEqual(10, value2.get());
                });

                runner.test("with non-null action, await child before parent when parent throws non-matching error", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() -> { throw new FileNotFoundException("blah"); });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final Result<Boolean> result = asyncTask.catchError(FolderNotFoundException.class, () ->
                    {
                        value2.increment();
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(10, value2.get());

                    test.assertThrows(result::await, new FileNotFoundException("blah"));
                    test.assertEqual(10, value2.get());

                    test.assertThrows(asyncTask::await, new FileNotFoundException("blah"));
                    test.assertEqual(10, value2.get());
                });

                runner.test("with non-null action, await child after parent when child throws", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value1 = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value1.increment();
                        return true;
                    });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final Result<Boolean> result = asyncTask.catchError(FileNotFoundException.class, (Action0)() ->
                    {
                        value2.increment();
                        throw new FolderNotFoundException("blah");
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(0, value1.get());
                    test.assertEqual(10, value2.get());

                    test.assertTrue(asyncTask.await());
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());

                    test.assertTrue(result.await());
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());
                });

                runner.test("with non-null action, await child before parent when child throws", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value1 = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value1.increment();
                        return true;
                    });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final Result<Boolean> result = asyncTask.catchError(FileNotFoundException.class, (Action0)() ->
                    {
                        value2.increment();
                        throw new FolderNotFoundException("blah");
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(0, value1.get());
                    test.assertEqual(10, value2.get());

                    test.assertTrue(result.await());
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());

                    test.assertTrue(asyncTask.await());
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());
                });

                runner.test("with non-null action, await child after parent when parent throws matching error and child throws", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value1 = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value1.increment();
                        throw new FileNotFoundException("abc");
                    });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final Result<Boolean> result = asyncTask.catchError(FileNotFoundException.class, (Action0)() ->
                    {
                        value2.increment();
                        throw new FolderNotFoundException("blah");
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(0, value1.get());
                    test.assertEqual(10, value2.get());

                    test.assertThrows(asyncTask::await, new FileNotFoundException("abc"));
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());

                    test.assertThrows(result::await, new FolderNotFoundException("blah"));
                    test.assertEqual(1, value1.get());
                    test.assertEqual(11, value2.get());
                });

                runner.test("with non-null action, await child before parent when parent throws matching error and child throws", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value1 = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value1.increment();
                        throw new FileNotFoundException("abc");
                    });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final Result<Boolean> result = asyncTask.catchError(FileNotFoundException.class, (Action0)() ->
                    {
                        value2.increment();
                        throw new FolderNotFoundException("blah");
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(0, value1.get());
                    test.assertEqual(10, value2.get());

                    test.assertThrows(result::await, new FolderNotFoundException("blah"));
                    test.assertEqual(1, value1.get());
                    test.assertEqual(11, value2.get());

                    test.assertThrows(asyncTask::await, new FileNotFoundException("abc"));
                    test.assertEqual(1, value1.get());
                    test.assertEqual(11, value2.get());
                });

                runner.test("with non-null action, await child after parent when parent throws non-matching error and child throws", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value1 = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value1.increment();
                        throw new FileNotFoundException("abc");
                    });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final Result<Boolean> result = asyncTask.catchError(QueueEmptyException.class, (Action0)() ->
                    {
                        value2.increment();
                        throw new FolderNotFoundException("blah");
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(0, value1.get());
                    test.assertEqual(10, value2.get());

                    test.assertThrows(asyncTask::await, new FileNotFoundException("abc"));
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());

                    test.assertThrows(result::await, new FileNotFoundException("abc"));
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());
                });

                runner.test("with non-null action, await child before parent when parent throws non-matching error and child throws", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value1 = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value1.increment();
                        throw new FileNotFoundException("abc");
                    });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final Result<Boolean> result = asyncTask.catchError(QueueEmptyException.class, (Action0)() ->
                    {
                        value2.increment();
                        throw new FolderNotFoundException("blah");
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(0, value1.get());
                    test.assertEqual(10, value2.get());

                    test.assertThrows(result::await, new FileNotFoundException("abc"));
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());

                    test.assertThrows(asyncTask::await, new FileNotFoundException("abc"));
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());
                });
            });

            runner.testGroup("catchError(Class<TError>,Action1<TError>)", () ->
            {
                runner.test("with null errorType", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value.increment();
                        return true;
                    });
                    test.assertThrows(() -> asyncTask.catchError(null, (FileNotFoundException error) -> {}),
                        new PreConditionFailure("errorType cannot be null."));
                });

                runner.test("with null action", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value.increment();
                        return false;
                    });
                    test.assertThrows(() -> asyncTask.catchError(FileNotFoundException.class, (Action1<FileNotFoundException>)null),
                        new PreConditionFailure("action cannot be null."));
                });

                runner.test("with non-null action, await child after parent", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value1 = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value1.increment();
                        return false;
                    });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final Value<Throwable> value3 = Value.create();
                    final Result<Boolean> result = asyncTask.catchError(FileNotFoundException.class, (FileNotFoundException parentError) ->
                    {
                        value2.increment();
                        value3.set(parentError);
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(0, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());

                    test.assertFalse(asyncTask.await());
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());

                    test.assertFalse(result.await());
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());
                });

                runner.test("with non-null action, await child before parent", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value1 = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value1.increment();
                        return true;
                    });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final Value<Throwable> value3 = Value.create();
                    final Result<Boolean> result = asyncTask.catchError(FileNotFoundException.class, (FileNotFoundException parentError) ->
                    {
                        value2.increment();
                        value3.set(parentError);
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(0, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());

                    test.assertTrue(result.await());
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());

                    test.assertTrue(asyncTask.await());
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());
                });

                runner.test("with non-null action, await child after parent when parent throws matching error", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() -> { throw new FileNotFoundException("blah"); });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final Value<Throwable> value3 = Value.create();
                    final Result<Boolean> result = asyncTask.catchError(FileNotFoundException.class, (FileNotFoundException parentError) ->
                    {
                        value2.increment();
                        value3.set(parentError);
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());

                    test.assertThrows(asyncTask::await, new FileNotFoundException("blah"));
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());

                    test.assertNull(result.await());
                    test.assertEqual(11, value2.get());
                    test.assertEqual(new FileNotFoundException("blah"), value3.get());
                });

                runner.test("with non-null action, await child before parent when parent throws matching error", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() -> { throw new FileNotFoundException("blah"); });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final Value<Throwable> value3 = Value.create();
                    final Result<Boolean> result = asyncTask.catchError(FileNotFoundException.class, (FileNotFoundException parentError) ->
                    {
                        value2.increment();
                        value3.set(parentError);
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());

                    test.assertNull(result.await());
                    test.assertEqual(11, value2.get());
                    test.assertEqual(new FileNotFoundException("blah"), value3.get());

                    test.assertThrows(asyncTask::await, new FileNotFoundException("blah"));
                    test.assertEqual(11, value2.get());
                    test.assertEqual(new FileNotFoundException("blah"), value3.get());
                });

                runner.test("with non-null action, await child after parent when parent throws non-matching error", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() -> { throw new FileNotFoundException("blah"); });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final Value<Throwable> value3 = Value.create();
                    final Result<Boolean> result = asyncTask.catchError(FolderNotFoundException.class, (FolderNotFoundException parentError) ->
                    {
                        value2.increment();
                        value3.set(parentError);
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());

                    test.assertThrows(asyncTask::await, new FileNotFoundException("blah"));
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());

                    test.assertThrows(result::await, new FileNotFoundException("blah"));
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());
                });

                runner.test("with non-null action, await child before parent when parent throws non-matching error", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() -> { throw new FileNotFoundException("blah"); });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final Value<Throwable> value3 = Value.create();
                    final Result<Boolean> result = asyncTask.catchError(FolderNotFoundException.class, (FolderNotFoundException parentError) ->
                    {
                        value2.increment();
                        value3.set(parentError);
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());

                    test.assertThrows(result::await, new FileNotFoundException("blah"));
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());

                    test.assertThrows(asyncTask::await, new FileNotFoundException("blah"));
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());
                });

                runner.test("with non-null action, await child after parent when child throws", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value1 = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value1.increment();
                        return true;
                    });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final Value<Throwable> value3 = Value.create();
                    final Result<Boolean> result = asyncTask.catchError(FileNotFoundException.class, (Action1<FileNotFoundException>)(FileNotFoundException parentError) ->
                    {
                        value2.increment();
                        value3.set(parentError);
                        throw new FolderNotFoundException("blah");
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(0, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());

                    test.assertTrue(asyncTask.await());
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());

                    test.assertTrue(result.await());
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());
                });

                runner.test("with non-null action, await child before parent when child throws", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value1 = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value1.increment();
                        return true;
                    });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final Value<Throwable> value3 = Value.create();
                    final Result<Boolean> result = asyncTask.catchError(FileNotFoundException.class, (Action1<FileNotFoundException>)(FileNotFoundException parentError) ->
                    {
                        value2.increment();
                        value3.set(parentError);
                        throw new FolderNotFoundException("blah");
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(0, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());

                    test.assertTrue(result.await());
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());

                    test.assertTrue(asyncTask.await());
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());
                });

                runner.test("with non-null action, await child after parent when parent throws matching error and child throws", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value1 = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value1.increment();
                        throw new FileNotFoundException("abc");
                    });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final Value<Throwable> value3 = Value.create();
                    final Result<Boolean> result = asyncTask.catchError(FileNotFoundException.class, (Action1<FileNotFoundException>)(FileNotFoundException parentError) ->
                    {
                        value2.increment();
                        value3.set(parentError);
                        throw new FolderNotFoundException("blah");
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(0, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());

                    test.assertThrows(asyncTask::await, new FileNotFoundException("abc"));
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());

                    test.assertThrows(result::await, new FolderNotFoundException("blah"));
                    test.assertEqual(1, value1.get());
                    test.assertEqual(11, value2.get());
                    test.assertEqual(new FileNotFoundException("abc"), value3.get());
                });

                runner.test("with non-null action, await child before parent when parent throws matching error and child throws", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value1 = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value1.increment();
                        throw new FileNotFoundException("abc");
                    });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final Value<Throwable> value3 = Value.create();
                    final Result<Boolean> result = asyncTask.catchError(FileNotFoundException.class, (Action1<FileNotFoundException>)(FileNotFoundException parentError) ->
                    {
                        value2.increment();
                        value3.set(parentError);
                        throw new FolderNotFoundException("blah");
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(0, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());

                    test.assertThrows(result::await, new FolderNotFoundException("blah"));
                    test.assertEqual(1, value1.get());
                    test.assertEqual(11, value2.get());
                    test.assertEqual(new FileNotFoundException("abc"), value3.get());

                    test.assertThrows(asyncTask::await, new FileNotFoundException("abc"));
                    test.assertEqual(1, value1.get());
                    test.assertEqual(11, value2.get());
                    test.assertEqual(new FileNotFoundException("abc"), value3.get());
                });

                runner.test("with non-null action, await child after parent when parent throws non-matching error and child throws", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value1 = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value1.increment();
                        throw new FileNotFoundException("abc");
                    });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final Value<Throwable> value3 = Value.create();
                    final Result<Boolean> result = asyncTask.catchError(QueueEmptyException.class, (Action1<QueueEmptyException>)(QueueEmptyException parentError) ->
                    {
                        value2.increment();
                        value3.set(parentError);
                        throw new FolderNotFoundException("blah");
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(0, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());

                    test.assertThrows(asyncTask::await, new FileNotFoundException("abc"));
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());

                    test.assertThrows(result::await, new FileNotFoundException("abc"));
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());
                });

                runner.test("with non-null action, await child before parent when parent throws non-matching error and child throws", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value1 = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value1.increment();
                        throw new FileNotFoundException("abc");
                    });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final Value<Throwable> value3 = Value.create();
                    final Result<Boolean> result = asyncTask.catchError(QueueEmptyException.class, (Action1<QueueEmptyException>)(QueueEmptyException parentError) ->
                    {
                        value2.increment();
                        value3.set(parentError);
                        throw new FolderNotFoundException("blah");
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(0, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());

                    test.assertThrows(result::await, new FileNotFoundException("abc"));
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());

                    test.assertThrows(asyncTask::await, new FileNotFoundException("abc"));
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());
                });
            });

            runner.testGroup("catchError(Function0<T>)", () ->
            {
                runner.test("with null function", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value.increment();
                        return true;
                    });
                    test.assertThrows(() -> asyncTask.catchError((Function0<Boolean>)null),
                        new PreConditionFailure("function cannot be null."));
                });

                runner.test("with non-null function, await child after parent", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value1 = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value1.increment();
                        return false;
                    });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final Result<Boolean> result = asyncTask.catchError(() ->
                    {
                        value2.increment();
                        return true;
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(0, value1.get());
                    test.assertEqual(10, value2.get());

                    test.assertFalse(asyncTask.await());
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());

                    test.assertFalse(result.await());
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());
                });

                runner.test("with non-null action, await child before parent", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value1 = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value1.increment();
                        return true;
                    });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final Result<Boolean> result = asyncTask.catchError(() ->
                    {
                        value2.increment();
                        return false;
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(0, value1.get());
                    test.assertEqual(10, value2.get());

                    test.assertTrue(result.await());
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());

                    test.assertTrue(asyncTask.await());
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());
                });

                runner.test("with non-null action, await child after parent when parent throws", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() -> { throw new FileNotFoundException("blah"); });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final Result<Boolean> result = asyncTask.catchError(() ->
                    {
                        value2.increment();
                        return true;
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(10, value2.get());

                    test.assertThrows(asyncTask::await, new FileNotFoundException("blah"));
                    test.assertEqual(10, value2.get());

                    test.assertTrue(result.await());
                    test.assertEqual(11, value2.get());
                });

                runner.test("with non-null action, await child before parent when parent throws", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() -> { throw new FileNotFoundException("blah"); });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final Result<Boolean> result = asyncTask.catchError(() ->
                    {
                        value2.increment();
                        return false;
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(10, value2.get());

                    test.assertFalse(result.await());
                    test.assertEqual(11, value2.get());

                    test.assertThrows(asyncTask::await, new FileNotFoundException("blah"));
                    test.assertEqual(11, value2.get());
                });

                runner.test("with non-null action, await child after parent when child throws", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value1 = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value1.increment();
                        return true;
                    });

                    final Result<Boolean> result = asyncTask.catchError((Function0<Boolean>)() -> { throw new FolderNotFoundException("blah"); });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(0, value1.get());

                    test.assertTrue(asyncTask.await());
                    test.assertEqual(1, value1.get());

                    test.assertTrue(result.await());
                    test.assertEqual(1, value1.get());
                });

                runner.test("with non-null action, await child before parent when child throws", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value1 = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value1.increment();
                        return true;
                    });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final Result<Boolean> result = asyncTask.catchError((Function0<Boolean>)() ->
                    {
                        value2.increment();
                        throw new FolderNotFoundException("blah");
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(0, value1.get());
                    test.assertEqual(10, value2.get());

                    test.assertTrue(result.await());
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());

                    test.assertTrue(asyncTask.await());
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());
                });

                runner.test("with non-null action, await child after parent when parent and child throw", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value1 = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value1.increment();
                        throw new FileNotFoundException("abc");
                    });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final Result<Boolean> result = asyncTask.catchError((Function0<Boolean>)() ->
                    {
                        value2.increment();
                        throw new FolderNotFoundException("blah");
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(0, value1.get());
                    test.assertEqual(10, value2.get());

                    test.assertThrows(asyncTask::await, new FileNotFoundException("abc"));
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());

                    test.assertThrows(result::await, new FolderNotFoundException("blah"));
                    test.assertEqual(1, value1.get());
                    test.assertEqual(11, value2.get());
                });

                runner.test("with non-null action, await child before parent when parent and child throw", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value1 = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value1.increment();
                        throw new FileNotFoundException("abc");
                    });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final Result<Boolean> result = asyncTask.catchError((Function0<Boolean>)() ->
                    {
                        value2.increment();
                        throw new FolderNotFoundException("blah");
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(0, value1.get());
                    test.assertEqual(10, value2.get());

                    test.assertThrows(result::await, new FolderNotFoundException("blah"));
                    test.assertEqual(1, value1.get());
                    test.assertEqual(11, value2.get());

                    test.assertThrows(asyncTask::await, new FileNotFoundException("abc"));
                    test.assertEqual(1, value1.get());
                    test.assertEqual(11, value2.get());
                });
            });

            runner.testGroup("catchError(Function1<Throwable,T>)", () ->
            {
                runner.test("with null function", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value.increment();
                        return true;
                    });
                    test.assertThrows(() -> asyncTask.catchError((Function1<Throwable,Boolean>)null),
                        new PreConditionFailure("function cannot be null."));
                });

                runner.test("with non-null function, await child after parent", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value1 = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value1.increment();
                        return false;
                    });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final Value<Throwable> value3 = Value.create();
                    final Result<Boolean> result = asyncTask.catchError((Throwable parentError) ->
                    {
                        value2.increment();
                        value3.set(parentError);
                        return true;
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(0, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());

                    test.assertFalse(asyncTask.await());
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());

                    test.assertFalse(result.await());
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());
                });

                runner.test("with non-null function, await child before parent", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value1 = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value1.increment();
                        return true;
                    });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final Value<Throwable> value3 = Value.create();
                    final Result<Boolean> result = asyncTask.catchError((Throwable parentError) ->
                    {
                        value2.increment();
                        value3.set(parentError);
                        return false;
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(0, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());

                    test.assertTrue(result.await());
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());

                    test.assertTrue(asyncTask.await());
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());
                });

                runner.test("with non-null function, await child after parent when parent throws", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() -> { throw new FileNotFoundException("blah"); });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final Value<Throwable> value3 = Value.create();
                    final Result<Boolean> result = asyncTask.catchError((Throwable parentError) ->
                    {
                        value2.increment();
                        value3.set(parentError);
                        return true;
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());

                    test.assertThrows(asyncTask::await, new FileNotFoundException("blah"));
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());

                    test.assertTrue(result.await());
                    test.assertEqual(11, value2.get());
                    test.assertEqual(new FileNotFoundException("blah"), value3.get());
                });

                runner.test("with non-null function, await child before parent when parent throws", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() -> { throw new FileNotFoundException("blah"); });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final Value<Throwable> value3 = Value.create();
                    final Result<Boolean> result = asyncTask.catchError((Throwable parentError) ->
                    {
                        value2.increment();
                        value3.set(parentError);
                        return false;
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());

                    test.assertFalse(result.await());
                    test.assertEqual(11, value2.get());
                    test.assertEqual(new FileNotFoundException("blah"), value3.get());

                    test.assertThrows(asyncTask::await, new FileNotFoundException("blah"));
                    test.assertEqual(11, value2.get());
                    test.assertEqual(new FileNotFoundException("blah"), value3.get());
                });

                runner.test("with non-null function, await child after parent when child throws", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value1 = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value1.increment();
                        return true;
                    });

                    final Value<Throwable> value3 = Value.create();
                    final Result<Boolean> result = asyncTask.catchError((Function1<Throwable,Boolean>)(Throwable parentError) ->
                    {
                        value3.set(parentError);
                        throw new FolderNotFoundException("blah");
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(0, value1.get());
                    test.assertFalse(value3.hasValue());

                    test.assertTrue(asyncTask.await());
                    test.assertEqual(1, value1.get());
                    test.assertFalse(value3.hasValue());

                    test.assertTrue(result.await());
                    test.assertEqual(1, value1.get());
                    test.assertFalse(value3.hasValue());
                });

                runner.test("with non-null function, await child before parent when child throws", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value1 = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value1.increment();
                        return true;
                    });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final Value<Throwable> value3 = Value.create();
                    final Result<Boolean> result = asyncTask.catchError((Function1<Throwable,Boolean>)(Throwable parentError) ->
                    {
                        value2.increment();
                        value3.set(parentError);
                        throw new FolderNotFoundException("blah");
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(0, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());

                    test.assertTrue(result.await());
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());

                    test.assertTrue(asyncTask.await());
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());
                });

                runner.test("with non-null function, await child after parent when parent and child throw", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value1 = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value1.increment();
                        throw new FileNotFoundException("abc");
                    });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final Value<Throwable> value3 = Value.create();
                    final Result<Boolean> result = asyncTask.catchError((Function1<Throwable,Boolean>)(Throwable parentError) ->
                    {
                        value2.increment();
                        value3.set(parentError);
                        throw new FolderNotFoundException("blah");
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(0, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());

                    test.assertThrows(asyncTask::await, new FileNotFoundException("abc"));
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());

                    test.assertThrows(result::await, new FolderNotFoundException("blah"));
                    test.assertEqual(1, value1.get());
                    test.assertEqual(11, value2.get());
                    test.assertEqual(new FileNotFoundException("abc"), value3.get());
                });

                runner.test("with non-null function, await child before parent when parent and child throw", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value1 = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value1.increment();
                        throw new FileNotFoundException("abc");
                    });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final Value<Throwable> value3 = Value.create();
                    final Result<Boolean> result = asyncTask.catchError((Function1<Throwable,Boolean>)(Throwable parentError) ->
                    {
                        value2.increment();
                        value3.set(parentError);
                        throw new FolderNotFoundException("blah");
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(0, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());

                    test.assertThrows(result::await, new FolderNotFoundException("blah"));
                    test.assertEqual(1, value1.get());
                    test.assertEqual(11, value2.get());
                    test.assertEqual(new FileNotFoundException("abc"), value3.get());

                    test.assertThrows(asyncTask::await, new FileNotFoundException("abc"));
                    test.assertEqual(1, value1.get());
                    test.assertEqual(11, value2.get());
                    test.assertEqual(new FileNotFoundException("abc"), value3.get());
                });
            });

            runner.testGroup("catchError(Class<TError>,Function0<T>)", () ->
            {
                runner.test("with null errorType", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value.increment();
                        return false;
                    });
                    test.assertThrows(() -> asyncTask.catchError(null, () -> false),
                        new PreConditionFailure("errorType cannot be null."));
                });

                runner.test("with null function", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value.increment();
                        return true;
                    });
                    test.assertThrows(() -> asyncTask.catchError(FileNotFoundException.class, (Function0<Boolean>)null),
                        new PreConditionFailure("function cannot be null."));
                });

                runner.test("with non-null function, await child after parent", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value1 = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value1.increment();
                        return false;
                    });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final Result<Boolean> result = asyncTask.catchError(FileNotFoundException.class, () ->
                    {
                        value2.increment();
                        return true;
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(0, value1.get());
                    test.assertEqual(10, value2.get());

                    test.assertFalse(asyncTask.await());
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());

                    test.assertFalse(result.await());
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());
                });

                runner.test("with non-null function, await child before parent", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value1 = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value1.increment();
                        return true;
                    });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final Result<Boolean> result = asyncTask.catchError(FileNotFoundException.class, () ->
                    {
                        value2.increment();
                        return false;
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(0, value1.get());
                    test.assertEqual(10, value2.get());

                    test.assertTrue(result.await());
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());

                    test.assertTrue(asyncTask.await());
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());
                });

                runner.test("with non-null function, await child after parent when parent throws matching error", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() -> { throw new FileNotFoundException("blah"); });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final Result<Boolean> result = asyncTask.catchError(FileNotFoundException.class, () ->
                    {
                        value2.increment();
                        return true;
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(10, value2.get());

                    test.assertThrows(asyncTask::await, new FileNotFoundException("blah"));
                    test.assertEqual(10, value2.get());

                    test.assertTrue(result.await());
                    test.assertEqual(11, value2.get());
                });

                runner.test("with non-null function, await child before parent when parent throws matching error", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() -> { throw new FileNotFoundException("blah"); });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final Result<Boolean> result = asyncTask.catchError(FileNotFoundException.class, () ->
                    {
                        value2.increment();
                        return false;
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(10, value2.get());

                    test.assertFalse(result.await());
                    test.assertEqual(11, value2.get());

                    test.assertThrows(asyncTask::await, new FileNotFoundException("blah"));
                    test.assertEqual(11, value2.get());
                });

                runner.test("with non-null function, await child after parent when parent throws non-matching error", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() -> { throw new FileNotFoundException("blah"); });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final Result<Boolean> result = asyncTask.catchError(FolderNotFoundException.class, () ->
                    {
                        value2.increment();
                        return true;
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(10, value2.get());

                    test.assertThrows(asyncTask::await, new FileNotFoundException("blah"));
                    test.assertEqual(10, value2.get());

                    test.assertThrows(result::await, new FileNotFoundException("blah"));
                    test.assertEqual(10, value2.get());
                });

                runner.test("with non-null function, await child before parent when parent throws non-matching error", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() -> { throw new FileNotFoundException("blah"); });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final Result<Boolean> result = asyncTask.catchError(FolderNotFoundException.class, () ->
                    {
                        value2.increment();
                        return false;
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(10, value2.get());

                    test.assertThrows(result::await, new FileNotFoundException("blah"));
                    test.assertEqual(10, value2.get());

                    test.assertThrows(asyncTask::await, new FileNotFoundException("blah"));
                    test.assertEqual(10, value2.get());
                });

                runner.test("with non-null function, await child after parent when child throws", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value1 = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value1.increment();
                        return true;
                    });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final Result<Boolean> result = asyncTask.catchError(FileNotFoundException.class, (Function0<Boolean>)() ->
                    {
                        value2.increment();
                        throw new FolderNotFoundException("blah");
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(0, value1.get());
                    test.assertEqual(10, value2.get());

                    test.assertTrue(asyncTask.await());
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());

                    test.assertTrue(result.await());
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());
                });

                runner.test("with non-null function, await child before parent when child throws", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value1 = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value1.increment();
                        return true;
                    });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final Result<Boolean> result = asyncTask.catchError(FileNotFoundException.class, (Function0<Boolean>)() ->
                    {
                        value2.increment();
                        throw new FolderNotFoundException("blah");
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(0, value1.get());
                    test.assertEqual(10, value2.get());

                    test.assertTrue(result.await());
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());

                    test.assertTrue(asyncTask.await());
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());
                });

                runner.test("with non-null function, await child after parent when parent throws matching error and child throws", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value1 = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value1.increment();
                        throw new FileNotFoundException("abc");
                    });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final Result<Boolean> result = asyncTask.catchError(FileNotFoundException.class, (Function0<Boolean>)() ->
                    {
                        value2.increment();
                        throw new FolderNotFoundException("blah");
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(0, value1.get());
                    test.assertEqual(10, value2.get());

                    test.assertThrows(asyncTask::await, new FileNotFoundException("abc"));
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());

                    test.assertThrows(result::await, new FolderNotFoundException("blah"));
                    test.assertEqual(1, value1.get());
                    test.assertEqual(11, value2.get());
                });

                runner.test("with non-null function, await child before parent when parent throws matching error and child throws", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value1 = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value1.increment();
                        throw new FileNotFoundException("abc");
                    });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final Result<Boolean> result = asyncTask.catchError(FileNotFoundException.class, (Function0<Boolean>)() ->
                    {
                        value2.increment();
                        throw new FolderNotFoundException("blah");
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(0, value1.get());
                    test.assertEqual(10, value2.get());

                    test.assertThrows(result::await, new FolderNotFoundException("blah"));
                    test.assertEqual(1, value1.get());
                    test.assertEqual(11, value2.get());

                    test.assertThrows(asyncTask::await, new FileNotFoundException("abc"));
                    test.assertEqual(1, value1.get());
                    test.assertEqual(11, value2.get());
                });

                runner.test("with non-null function, await child after parent when parent throws non-matching error and child throws", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value1 = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value1.increment();
                        throw new FileNotFoundException("abc");
                    });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final Result<Boolean> result = asyncTask.catchError(QueueEmptyException.class, (Function0<Boolean>)() ->
                    {
                        value2.increment();
                        throw new FolderNotFoundException("blah");
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(0, value1.get());
                    test.assertEqual(10, value2.get());

                    test.assertThrows(asyncTask::await, new FileNotFoundException("abc"));
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());

                    test.assertThrows(result::await, new FileNotFoundException("abc"));
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());
                });

                runner.test("with non-null function, await child before parent when parent throws non-matching error and child throws", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value1 = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value1.increment();
                        throw new FileNotFoundException("abc");
                    });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final Result<Boolean> result = asyncTask.catchError(QueueEmptyException.class, (Function0<Boolean>)() ->
                    {
                        value2.increment();
                        throw new FolderNotFoundException("blah");
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(0, value1.get());
                    test.assertEqual(10, value2.get());

                    test.assertThrows(result::await, new FileNotFoundException("abc"));
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());

                    test.assertThrows(asyncTask::await, new FileNotFoundException("abc"));
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());
                });
            });

            runner.testGroup("catchError(Class<TError>,Function1<TError,T>)", () ->
            {
                runner.test("with null errorType", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value.increment();
                        return false;
                    });
                    test.assertThrows(() -> asyncTask.catchError(null, (FileNotFoundException error) -> true),
                        new PreConditionFailure("errorType cannot be null."));
                });

                runner.test("with null function", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value.increment();
                        return true;
                    });
                    test.assertThrows(() -> asyncTask.catchError(FileNotFoundException.class, (Function1<FileNotFoundException,Boolean>)null),
                        new PreConditionFailure("function cannot be null."));
                });

                runner.test("with non-null function, await child after parent", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value1 = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value1.increment();
                        return false;
                    });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final Value<Throwable> value3 = Value.create();
                    final Result<Boolean> result = asyncTask.catchError(FileNotFoundException.class, (FileNotFoundException parentError) ->
                    {
                        value2.increment();
                        value3.set(parentError);
                        return true;
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(0, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());

                    test.assertFalse(asyncTask.await());
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());

                    test.assertFalse(result.await());
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());
                });

                runner.test("with non-null function, await child before parent", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value1 = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value1.increment();
                        return true;
                    });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final Value<Throwable> value3 = Value.create();
                    final Result<Boolean> result = asyncTask.catchError(FileNotFoundException.class, (FileNotFoundException parentError) ->
                    {
                        value2.increment();
                        value3.set(parentError);
                        return false;
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(0, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());

                    test.assertTrue(result.await());
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());

                    test.assertTrue(asyncTask.await());
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());
                });

                runner.test("with non-null function, await child after parent when parent throws matching error", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() -> { throw new FileNotFoundException("blah"); });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final Value<Throwable> value3 = Value.create();
                    final Result<Boolean> result = asyncTask.catchError(FileNotFoundException.class, (FileNotFoundException parentError) ->
                    {
                        value2.increment();
                        value3.set(parentError);
                        return true;
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());

                    test.assertThrows(asyncTask::await, new FileNotFoundException("blah"));
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());

                    test.assertTrue(result.await());
                    test.assertEqual(11, value2.get());
                    test.assertEqual(new FileNotFoundException("blah"), value3.get());
                });

                runner.test("with non-null function, await child before parent when parent throws matching error", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() -> { throw new FileNotFoundException("blah"); });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final Value<Throwable> value3 = Value.create();
                    final Result<Boolean> result = asyncTask.catchError(FileNotFoundException.class, (FileNotFoundException parentError) ->
                    {
                        value2.increment();
                        value3.set(parentError);
                        return false;
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());

                    test.assertFalse(result.await());
                    test.assertEqual(11, value2.get());
                    test.assertEqual(new FileNotFoundException("blah"), value3.get());

                    test.assertThrows(asyncTask::await, new FileNotFoundException("blah"));
                    test.assertEqual(11, value2.get());
                    test.assertEqual(new FileNotFoundException("blah"), value3.get());
                });

                runner.test("with non-null function, await child after parent when parent throws non-matching error", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() -> { throw new FileNotFoundException("blah"); });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final Value<Throwable> value3 = Value.create();
                    final Result<Boolean> result = asyncTask.catchError(FolderNotFoundException.class, (FolderNotFoundException parentError) ->
                    {
                        value2.increment();
                        value3.set(parentError);
                        return true;
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());

                    test.assertThrows(asyncTask::await, new FileNotFoundException("blah"));
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());

                    test.assertThrows(result::await, new FileNotFoundException("blah"));
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());
                });

                runner.test("with non-null function, await child before parent when parent throws non-matching error", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() -> { throw new FileNotFoundException("blah"); });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final Value<Throwable> value3 = Value.create();
                    final Result<Boolean> result = asyncTask.catchError(FolderNotFoundException.class, (FolderNotFoundException parentError) ->
                    {
                        value2.increment();
                        value3.set(parentError);
                        return false;
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());

                    test.assertThrows(result::await, new FileNotFoundException("blah"));
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());

                    test.assertThrows(asyncTask::await, new FileNotFoundException("blah"));
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());
                });

                runner.test("with non-null function, await child after parent when child throws", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value1 = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value1.increment();
                        return true;
                    });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final Value<Throwable> value3 = Value.create();
                    final Result<Boolean> result = asyncTask.catchError(FileNotFoundException.class, (Function1<FileNotFoundException,Boolean>)(FileNotFoundException parentError) ->
                    {
                        value2.increment();
                        value3.set(parentError);
                        throw new FolderNotFoundException("blah");
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(0, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());

                    test.assertTrue(asyncTask.await());
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());

                    test.assertTrue(result.await());
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());
                });

                runner.test("with non-null function, await child before parent when child throws", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value1 = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value1.increment();
                        return true;
                    });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final Value<Throwable> value3 = Value.create();
                    final Result<Boolean> result = asyncTask.catchError(FileNotFoundException.class, (Function1<FileNotFoundException,Boolean>)(FileNotFoundException parentError) ->
                    {
                        value2.increment();
                        value3.set(parentError);
                        throw new FolderNotFoundException("blah");
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(0, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());

                    test.assertTrue(result.await());
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());

                    test.assertTrue(asyncTask.await());
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());
                });

                runner.test("with non-null function, await child after parent when parent throws matching error and child throws", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value1 = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value1.increment();
                        throw new FileNotFoundException("abc");
                    });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final Value<Throwable> value3 = Value.create();
                    final Result<Boolean> result = asyncTask.catchError(FileNotFoundException.class, (Function1<FileNotFoundException,Boolean>)(FileNotFoundException parentError) ->
                    {
                        value2.increment();
                        value3.set(parentError);
                        throw new FolderNotFoundException("blah");
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(0, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());

                    test.assertThrows(asyncTask::await, new FileNotFoundException("abc"));
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());

                    test.assertThrows(result::await, new FolderNotFoundException("blah"));
                    test.assertEqual(1, value1.get());
                    test.assertEqual(11, value2.get());
                    test.assertEqual(new FileNotFoundException("abc"), value3.get());
                });

                runner.test("with non-null function, await child before parent when parent throws matching error and child throws", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value1 = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value1.increment();
                        throw new FileNotFoundException("abc");
                    });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final Value<Throwable> value3 = Value.create();
                    final Result<Boolean> result = asyncTask.catchError(FileNotFoundException.class, (Function1<FileNotFoundException,Boolean>)(FileNotFoundException parentError) ->
                    {
                        value2.increment();
                        value3.set(parentError);
                        throw new FolderNotFoundException("blah");
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(0, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());

                    test.assertThrows(result::await, new FolderNotFoundException("blah"));
                    test.assertEqual(1, value1.get());
                    test.assertEqual(11, value2.get());
                    test.assertEqual(new FileNotFoundException("abc"), value3.get());

                    test.assertThrows(asyncTask::await, new FileNotFoundException("abc"));
                    test.assertEqual(1, value1.get());
                    test.assertEqual(11, value2.get());
                    test.assertEqual(new FileNotFoundException("abc"), value3.get());
                });

                runner.test("with non-null function, await child after parent when parent throws non-matching error and child throws", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value1 = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value1.increment();
                        throw new FileNotFoundException("abc");
                    });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final Value<Throwable> value3 = Value.create();
                    final Result<Boolean> result = asyncTask.catchError(QueueEmptyException.class, (Function1<QueueEmptyException,Boolean>)(QueueEmptyException parentError) ->
                    {
                        value2.increment();
                        value3.set(parentError);
                        throw new FolderNotFoundException("blah");
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(0, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());

                    test.assertThrows(asyncTask::await, new FileNotFoundException("abc"));
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());

                    test.assertThrows(result::await, new FileNotFoundException("abc"));
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());
                });

                runner.test("with non-null function, await child before parent when parent throws non-matching error and child throws", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value1 = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value1.increment();
                        throw new FileNotFoundException("abc");
                    });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final Value<Throwable> value3 = Value.create();
                    final Result<Boolean> result = asyncTask.catchError(QueueEmptyException.class, (Function1<QueueEmptyException,Boolean>)(QueueEmptyException parentError) ->
                    {
                        value2.increment();
                        value3.set(parentError);
                        throw new FolderNotFoundException("blah");
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(0, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());

                    test.assertThrows(result::await, new FileNotFoundException("abc"));
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());

                    test.assertThrows(asyncTask::await, new FileNotFoundException("abc"));
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());
                });
            });

            runner.testGroup("catchErrorResult(Function0<Result<T>>)", () ->
            {
                runner.test("with null function", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value.increment();
                        return true;
                    });
                    test.assertThrows(() -> asyncTask.catchErrorResult((Function0<Result<Boolean>>)null),
                        new PreConditionFailure("function cannot be null."));
                });

                runner.test("with non-null function, await child after parent", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value1 = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value1.increment();
                        return false;
                    });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final IntegerValue value3 = IntegerValue.create(100);
                    final Result<Boolean> result = asyncTask.catchErrorResult(() ->
                    {
                        value2.increment();
                        return asyncRunner.schedule(() ->
                        {
                            value3.increment();
                            return true;
                        });
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(0, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertEqual(100, value3.get());

                    test.assertFalse(asyncTask.await());
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertEqual(100, value3.get());

                    test.assertFalse(result.await());
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertEqual(100, value3.get());
                });

                runner.test("with non-null function, await child before parent", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value1 = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value1.increment();
                        return true;
                    });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final IntegerValue value3 = IntegerValue.create(100);
                    final Result<Boolean> result = asyncTask.catchErrorResult(() ->
                    {
                        value2.increment();
                        return asyncRunner.schedule(() ->
                        {
                            value3.increment();
                            return false;
                        });
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(0, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertEqual(100, value3.get());

                    test.assertTrue(result.await());
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertEqual(100, value3.get());

                    test.assertTrue(asyncTask.await());
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertEqual(100, value3.get());
                });

                runner.test("with non-null function, await child after parent when parent throws", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() -> { throw new FileNotFoundException("blah"); });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final IntegerValue value3 = IntegerValue.create(100);
                    final Result<Boolean> result = asyncTask.catchErrorResult(() ->
                    {
                        value2.increment();
                        return asyncRunner.schedule(() ->
                        {
                            value3.increment();
                            return true;
                        });
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(10, value2.get());
                    test.assertEqual(100, value3.get());

                    test.assertThrows(asyncTask::await, new FileNotFoundException("blah"));
                    test.assertEqual(10, value2.get());
                    test.assertEqual(100, value3.get());

                    test.assertTrue(result.await());
                    test.assertEqual(11, value2.get());
                    test.assertEqual(101, value3.get());
                });

                runner.test("with non-null function, await child before parent when parent throws", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() -> { throw new FileNotFoundException("blah"); });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final IntegerValue value3 = IntegerValue.create(100);
                    final Result<Boolean> result = asyncTask.catchErrorResult(() ->
                    {
                        value2.increment();
                        return asyncRunner.schedule(() ->
                        {
                            value3.increment();
                            return false;
                        });
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(10, value2.get());
                    test.assertEqual(100, value3.get());

                    test.assertFalse(result.await());
                    test.assertEqual(11, value2.get());
                    test.assertEqual(101, value3.get());

                    test.assertThrows(asyncTask::await, new FileNotFoundException("blah"));
                    test.assertEqual(11, value2.get());
                    test.assertEqual(101, value3.get());
                });

                runner.test("with non-null function, await child after parent when child throws", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value1 = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value1.increment();
                        return true;
                    });

                    final Result<Boolean> result = asyncTask.catchErrorResult((Function0<Result<Boolean>>)() -> { throw new FolderNotFoundException("blah"); });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(0, value1.get());

                    test.assertTrue(asyncTask.await());
                    test.assertEqual(1, value1.get());

                    test.assertTrue(result.await());
                    test.assertEqual(1, value1.get());
                });

                runner.test("with non-null function, await child before parent when child throws", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value1 = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value1.increment();
                        return true;
                    });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final Result<Boolean> result = asyncTask.catchErrorResult((Function0<Result<Boolean>>)() ->
                    {
                        value2.increment();
                        throw new FolderNotFoundException("blah");
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(0, value1.get());
                    test.assertEqual(10, value2.get());

                    test.assertTrue(result.await());
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());

                    test.assertTrue(asyncTask.await());
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());
                });

                runner.test("with non-null function, await child after parent when parent and child throw", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value1 = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value1.increment();
                        throw new FileNotFoundException("abc");
                    });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final Result<Boolean> result = asyncTask.catchErrorResult((Function0<Result<Boolean>>)() ->
                    {
                        value2.increment();
                        throw new FolderNotFoundException("blah");
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(0, value1.get());
                    test.assertEqual(10, value2.get());

                    test.assertThrows(asyncTask::await, new FileNotFoundException("abc"));
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());

                    test.assertThrows(result::await, new FolderNotFoundException("blah"));
                    test.assertEqual(1, value1.get());
                    test.assertEqual(11, value2.get());
                });

                runner.test("with non-null function, await child before parent when parent and child throw", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value1 = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value1.increment();
                        throw new FileNotFoundException("abc");
                    });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final Result<Boolean> result = asyncTask.catchErrorResult((Function0<Result<Boolean>>)() ->
                    {
                        value2.increment();
                        throw new FolderNotFoundException("blah");
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(0, value1.get());
                    test.assertEqual(10, value2.get());

                    test.assertThrows(result::await, new FolderNotFoundException("blah"));
                    test.assertEqual(1, value1.get());
                    test.assertEqual(11, value2.get());

                    test.assertThrows(asyncTask::await, new FileNotFoundException("abc"));
                    test.assertEqual(1, value1.get());
                    test.assertEqual(11, value2.get());
                });

                runner.test("with non-null function, await child after parent when parent and inner-child throw", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() -> { throw new FolderNotFoundException("abc"); });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final IntegerValue value3 = IntegerValue.create(100);
                    final Result<Boolean> result = asyncTask.catchErrorResult(() ->
                    {
                        value2.increment();
                        return asyncRunner.schedule(() ->
                        {
                            value3.increment();
                            throw new FileNotFoundException("blah");
                        });
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(10, value2.get());
                    test.assertEqual(100, value3.get());

                    test.assertThrows(asyncTask::await, new FolderNotFoundException("abc"));
                    test.assertEqual(10, value2.get());
                    test.assertEqual(100, value3.get());

                    test.assertThrows(result::await, new FileNotFoundException("blah"));
                    test.assertEqual(11, value2.get());
                    test.assertEqual(101, value3.get());
                });

                runner.test("with non-null function, await child before parent when parent and inner-child throw", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() -> { throw new FolderNotFoundException("abc"); });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final IntegerValue value3 = IntegerValue.create(100);
                    final Result<Boolean> result = asyncTask.catchErrorResult(() ->
                    {
                        value2.increment();
                        return asyncRunner.schedule(() ->
                        {
                            value3.increment();
                            throw new FileNotFoundException("blah");
                        });
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(10, value2.get());
                    test.assertEqual(100, value3.get());

                    test.assertThrows(result::await, new FileNotFoundException("blah"));
                    test.assertEqual(11, value2.get());
                    test.assertEqual(101, value3.get());

                    test.assertThrows(asyncTask::await, new FolderNotFoundException("abc"));
                    test.assertEqual(11, value2.get());
                    test.assertEqual(101, value3.get());
                });
            });

            runner.testGroup("catchErrorResult(Function1<Throwable,Result<T>>)", () ->
            {
                runner.test("with null function", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value.increment();
                        return true;
                    });
                    test.assertThrows(() -> asyncTask.catchErrorResult((Function1<Throwable,Result<Boolean>>)null),
                        new PreConditionFailure("function cannot be null."));
                });

                runner.test("with non-null function, await child after parent", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value1 = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value1.increment();
                        return false;
                    });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final IntegerValue value3 = IntegerValue.create(100);
                    final Value<Throwable> value4 = Value.create();
                    final Result<Boolean> result = asyncTask.catchErrorResult((Throwable parentError) ->
                    {
                        value2.increment();
                        value4.set(parentError);
                        return asyncRunner.schedule(() ->
                        {
                            value3.increment();
                            return true;
                        });
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(0, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertEqual(100, value3.get());
                    test.assertFalse(value4.hasValue());

                    test.assertFalse(asyncTask.await());
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertEqual(100, value3.get());
                    test.assertFalse(value4.hasValue());

                    test.assertFalse(result.await());
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertEqual(100, value3.get());
                    test.assertFalse(value4.hasValue());
                });

                runner.test("with non-null function, await child before parent", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value1 = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value1.increment();
                        return true;
                    });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final IntegerValue value3 = IntegerValue.create(100);
                    final Value<Throwable> value4 = Value.create();
                    final Result<Boolean> result = asyncTask.catchErrorResult((Throwable parentError) ->
                    {
                        value2.increment();
                        value4.set(parentError);
                        return asyncRunner.schedule(() ->
                        {
                            value3.increment();
                            return false;
                        });
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(0, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertEqual(100, value3.get());
                    test.assertFalse(value4.hasValue());

                    test.assertTrue(result.await());
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertEqual(100, value3.get());
                    test.assertFalse(value4.hasValue());

                    test.assertTrue(asyncTask.await());
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertEqual(100, value3.get());
                    test.assertFalse(value4.hasValue());
                });

                runner.test("with non-null function, await child after parent when parent throws", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() -> { throw new FileNotFoundException("blah"); });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final IntegerValue value3 = IntegerValue.create(100);
                    final Value<Throwable> value4 = Value.create();
                    final Result<Boolean> result = asyncTask.catchErrorResult((Throwable parentError) ->
                    {
                        value2.increment();
                        value4.set(parentError);
                        return asyncRunner.schedule(() ->
                        {
                            value3.increment();
                            return true;
                        });
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(10, value2.get());
                    test.assertEqual(100, value3.get());
                    test.assertFalse(value4.hasValue());

                    test.assertThrows(asyncTask::await, new FileNotFoundException("blah"));
                    test.assertEqual(10, value2.get());
                    test.assertEqual(100, value3.get());
                    test.assertFalse(value4.hasValue());

                    test.assertTrue(result.await());
                    test.assertEqual(11, value2.get());
                    test.assertEqual(101, value3.get());
                    test.assertEqual(new FileNotFoundException("blah"), value4.get());
                });

                runner.test("with non-null function, await child before parent when parent throws", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() -> { throw new FileNotFoundException("blah"); });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final IntegerValue value3 = IntegerValue.create(100);
                    final Value<Throwable> value4 = Value.create();
                    final Result<Boolean> result = asyncTask.catchErrorResult((Throwable parentError) ->
                    {
                        value2.increment();
                        value4.set(parentError);
                        return asyncRunner.schedule(() ->
                        {
                            value3.increment();
                            return false;
                        });
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(10, value2.get());
                    test.assertEqual(100, value3.get());
                    test.assertFalse(value4.hasValue());

                    test.assertFalse(result.await());
                    test.assertEqual(11, value2.get());
                    test.assertEqual(101, value3.get());
                    test.assertEqual(new FileNotFoundException("blah"), value4.get());

                    test.assertThrows(asyncTask::await, new FileNotFoundException("blah"));
                    test.assertEqual(11, value2.get());
                    test.assertEqual(101, value3.get());
                    test.assertEqual(new FileNotFoundException("blah"), value4.get());
                });

                runner.test("with non-null function, await child after parent when child throws", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value1 = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value1.increment();
                        return true;
                    });

                    final Value<Throwable> value4 = Value.create();
                    final Result<Boolean> result = asyncTask.catchErrorResult((Function1<Throwable,Result<Boolean>>)(Throwable parentError) ->
                    {
                        value4.set(parentError);
                        throw new FolderNotFoundException("blah");
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(0, value1.get());
                    test.assertFalse(value4.hasValue());

                    test.assertTrue(asyncTask.await());
                    test.assertEqual(1, value1.get());
                    test.assertFalse(value4.hasValue());

                    test.assertTrue(result.await());
                    test.assertEqual(1, value1.get());
                    test.assertFalse(value4.hasValue());
                });

                runner.test("with non-null function, await child before parent when child throws", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value1 = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value1.increment();
                        return true;
                    });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final Value<Throwable> value4 = Value.create();
                    final Result<Boolean> result = asyncTask.catchErrorResult((Function1<Throwable,Result<Boolean>>)(Throwable parentError) ->
                    {
                        value2.increment();
                        value4.set(parentError);
                        throw new FolderNotFoundException("blah");
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(0, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value4.hasValue());

                    test.assertTrue(result.await());
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value4.hasValue());

                    test.assertTrue(asyncTask.await());
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value4.hasValue());
                });

                runner.test("with non-null function, await child after parent when parent and child throw", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value1 = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value1.increment();
                        throw new FileNotFoundException("abc");
                    });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final Value<Throwable> value4 = Value.create();
                    final Result<Boolean> result = asyncTask.catchErrorResult((Function1<Throwable,Result<Boolean>>)(Throwable parentError) ->
                    {
                        value2.increment();
                        value4.set(parentError);
                        throw new FolderNotFoundException("blah");
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(0, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value4.hasValue());

                    test.assertThrows(asyncTask::await, new FileNotFoundException("abc"));
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value4.hasValue());

                    test.assertThrows(result::await, new FolderNotFoundException("blah"));
                    test.assertEqual(1, value1.get());
                    test.assertEqual(11, value2.get());
                    test.assertEqual(new FileNotFoundException("abc"), value4.get());
                });

                runner.test("with non-null function, await child before parent when parent and child throw", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value1 = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value1.increment();
                        throw new FileNotFoundException("abc");
                    });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final Value<Throwable> value4 = Value.create();
                    final Result<Boolean> result = asyncTask.catchErrorResult((Function1<Throwable,Result<Boolean>>)(Throwable parentError) ->
                    {
                        value2.increment();
                        value4.set(parentError);
                        throw new FolderNotFoundException("blah");
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(0, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value4.hasValue());

                    test.assertThrows(result::await, new FolderNotFoundException("blah"));
                    test.assertEqual(1, value1.get());
                    test.assertEqual(11, value2.get());
                    test.assertEqual(new FileNotFoundException("abc"), value4.get());

                    test.assertThrows(asyncTask::await, new FileNotFoundException("abc"));
                    test.assertEqual(1, value1.get());
                    test.assertEqual(11, value2.get());
                    test.assertEqual(new FileNotFoundException("abc"), value4.get());
                });

                runner.test("with non-null function, await child after parent when parent and inner-child throw", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() -> { throw new FolderNotFoundException("abc"); });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final IntegerValue value3 = IntegerValue.create(100);
                    final Value<Throwable> value4 = Value.create();
                    final Result<Boolean> result = asyncTask.catchErrorResult((Throwable parentError) ->
                    {
                        value2.increment();
                        value4.set(parentError);
                        return asyncRunner.schedule(() ->
                        {
                            value3.increment();
                            throw new FileNotFoundException("blah");
                        });
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(10, value2.get());
                    test.assertEqual(100, value3.get());
                    test.assertFalse(value4.hasValue());

                    test.assertThrows(asyncTask::await, new FolderNotFoundException("abc"));
                    test.assertEqual(10, value2.get());
                    test.assertEqual(100, value3.get());
                    test.assertFalse(value4.hasValue());

                    test.assertThrows(result::await, new FileNotFoundException("blah"));
                    test.assertEqual(11, value2.get());
                    test.assertEqual(101, value3.get());
                    test.assertEqual(new FolderNotFoundException("abc"), value4.get());
                });

                runner.test("with non-null function, await child before parent when parent and inner-child throw", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() -> { throw new FolderNotFoundException("abc"); });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final IntegerValue value3 = IntegerValue.create(100);
                    final Value<Throwable> value4 = Value.create();
                    final Result<Boolean> result = asyncTask.catchErrorResult((Throwable parentError) ->
                    {
                        value2.increment();
                        value4.set(parentError);
                        return asyncRunner.schedule(() ->
                        {
                            value3.increment();
                            throw new FileNotFoundException("blah");
                        });
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(10, value2.get());
                    test.assertEqual(100, value3.get());
                    test.assertFalse(value4.hasValue());

                    test.assertThrows(result::await, new FileNotFoundException("blah"));
                    test.assertEqual(11, value2.get());
                    test.assertEqual(101, value3.get());
                    test.assertEqual(new FolderNotFoundException("abc"), value4.get());

                    test.assertThrows(asyncTask::await, new FolderNotFoundException("abc"));
                    test.assertEqual(11, value2.get());
                    test.assertEqual(101, value3.get());
                    test.assertEqual(new FolderNotFoundException("abc"), value4.get());
                });
            });

            runner.testGroup("catchErrorResult(Class<TError>,Function0<Result<T>>)", () ->
            {
                runner.test("with null errorType", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value.increment();
                        return false;
                    });
                    test.assertThrows(() -> asyncTask.catchErrorResult(null, () -> Result.successTrue()),
                        new PreConditionFailure("errorType cannot be null."));
                });

                runner.test("with null function", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value.increment();
                        return true;
                    });
                    test.assertThrows(() -> asyncTask.catchErrorResult(FileNotFoundException.class, (Function0<Result<Boolean>>)null),
                        new PreConditionFailure("function cannot be null."));
                });

                runner.test("with non-null function, await child after parent", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value1 = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value1.increment();
                        return false;
                    });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final IntegerValue value4 = IntegerValue.create(100);
                    final Result<Boolean> result = asyncTask.catchErrorResult(FileNotFoundException.class, () ->
                    {
                        value2.increment();
                        return asyncRunner.schedule(() ->
                        {
                            value4.increment();
                            return true;
                        });
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(0, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertEqual(100, value4.get());

                    test.assertFalse(asyncTask.await());
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertEqual(100, value4.get());

                    test.assertFalse(result.await());
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertEqual(100, value4.get());
                });

                runner.test("with non-null function, await child before parent", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value1 = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value1.increment();
                        return true;
                    });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final IntegerValue value4 = IntegerValue.create(100);
                    final Result<Boolean> result = asyncTask.catchErrorResult(FileNotFoundException.class, () ->
                    {
                        value2.increment();
                        return asyncRunner.schedule(() ->
                        {
                            value4.increment();
                            return false;
                        });
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(0, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertEqual(100, value4.get());

                    test.assertTrue(result.await());
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertEqual(100, value4.get());

                    test.assertTrue(asyncTask.await());
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertEqual(100, value4.get());
                });

                runner.test("with non-null function, await child after parent when parent throws matching error", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() -> { throw new FileNotFoundException("blah"); });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final IntegerValue value4 = IntegerValue.create(100);
                    final Result<Boolean> result = asyncTask.catchErrorResult(FileNotFoundException.class, () ->
                    {
                        value2.increment();
                        return asyncRunner.schedule(() ->
                        {
                            value4.increment();
                            return true;
                        });
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(10, value2.get());
                    test.assertEqual(100, value4.get());

                    test.assertThrows(asyncTask::await, new FileNotFoundException("blah"));
                    test.assertEqual(10, value2.get());
                    test.assertEqual(100, value4.get());

                    test.assertTrue(result.await());
                    test.assertEqual(11, value2.get());
                    test.assertEqual(101, value4.get());
                });

                runner.test("with non-null function, await child before parent when parent throws matching error", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() -> { throw new FileNotFoundException("blah"); });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final IntegerValue value4 = IntegerValue.create(100);
                    final Result<Boolean> result = asyncTask.catchErrorResult(FileNotFoundException.class, () ->
                    {
                        value2.increment();
                        return asyncRunner.schedule(() ->
                        {
                            value4.increment();
                            return false;
                        });
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(10, value2.get());
                    test.assertEqual(100, value4.get());

                    test.assertFalse(result.await());
                    test.assertEqual(11, value2.get());
                    test.assertEqual(101, value4.get());

                    test.assertThrows(asyncTask::await, new FileNotFoundException("blah"));
                    test.assertEqual(11, value2.get());
                    test.assertEqual(101, value4.get());
                });

                runner.test("with non-null function, await child after parent when parent throws non-matching error", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() -> { throw new FileNotFoundException("blah"); });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final IntegerValue value4 = IntegerValue.create(100);
                    final Result<Boolean> result = asyncTask.catchErrorResult(FolderNotFoundException.class, () ->
                    {
                        value2.increment();
                        return asyncRunner.schedule(() ->
                        {
                            value4.increment();
                            return true;
                        });
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(10, value2.get());
                    test.assertEqual(100, value4.get());

                    test.assertThrows(asyncTask::await, new FileNotFoundException("blah"));
                    test.assertEqual(10, value2.get());
                    test.assertEqual(100, value4.get());

                    test.assertThrows(result::await, new FileNotFoundException("blah"));
                    test.assertEqual(10, value2.get());
                    test.assertEqual(100, value4.get());
                });

                runner.test("with non-null function, await child before parent when parent throws non-matching error", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() -> { throw new FileNotFoundException("blah"); });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final IntegerValue value4 = IntegerValue.create(100);
                    final Result<Boolean> result = asyncTask.catchErrorResult(FolderNotFoundException.class, () ->
                    {
                        value2.increment();
                        return asyncRunner.schedule(() ->
                        {
                            value4.increment();
                            return false;
                        });
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(10, value2.get());
                    test.assertEqual(100, value4.get());

                    test.assertThrows(result::await, new FileNotFoundException("blah"));
                    test.assertEqual(10, value2.get());
                    test.assertEqual(100, value4.get());

                    test.assertThrows(asyncTask::await, new FileNotFoundException("blah"));
                    test.assertEqual(10, value2.get());
                    test.assertEqual(100, value4.get());
                });

                runner.test("with non-null function, await child after parent when child throws", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value1 = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value1.increment();
                        return true;
                    });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final Result<Boolean> result = asyncTask.catchErrorResult(FileNotFoundException.class, (Function0<Result<Boolean>>)() ->
                    {
                        value2.increment();
                        throw new FolderNotFoundException("blah");
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(0, value1.get());
                    test.assertEqual(10, value2.get());

                    test.assertTrue(asyncTask.await());
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());

                    test.assertTrue(result.await());
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());
                });

                runner.test("with non-null function, await child before parent when child throws", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value1 = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value1.increment();
                        return true;
                    });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final Result<Boolean> result = asyncTask.catchErrorResult(FileNotFoundException.class, (Function0<Result<Boolean>>)() ->
                    {
                        value2.increment();
                        throw new FolderNotFoundException("blah");
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(0, value1.get());
                    test.assertEqual(10, value2.get());

                    test.assertTrue(result.await());
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());

                    test.assertTrue(asyncTask.await());
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());
                });

                runner.test("with non-null function, await child after parent when parent throws matching error and child throws", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value1 = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value1.increment();
                        throw new FileNotFoundException("abc");
                    });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final Value<Throwable> value3 = Value.create();
                    final Result<Boolean> result = asyncTask.catchErrorResult(FileNotFoundException.class, (Function0<Result<Boolean>>)() ->
                    {
                        value2.increment();
                        throw new FolderNotFoundException("blah");
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(0, value1.get());
                    test.assertEqual(10, value2.get());

                    test.assertThrows(asyncTask::await, new FileNotFoundException("abc"));
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());

                    test.assertThrows(result::await, new FolderNotFoundException("blah"));
                    test.assertEqual(1, value1.get());
                    test.assertEqual(11, value2.get());
                });

                runner.test("with non-null function, await child before parent when parent throws matching error and child throws", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value1 = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value1.increment();
                        throw new FileNotFoundException("abc");
                    });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final Result<Boolean> result = asyncTask.catchErrorResult(FileNotFoundException.class, (Function0<Result<Boolean>>)() ->
                    {
                        value2.increment();
                        throw new FolderNotFoundException("blah");
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(0, value1.get());
                    test.assertEqual(10, value2.get());

                    test.assertThrows(result::await, new FolderNotFoundException("blah"));
                    test.assertEqual(1, value1.get());
                    test.assertEqual(11, value2.get());

                    test.assertThrows(asyncTask::await, new FileNotFoundException("abc"));
                    test.assertEqual(1, value1.get());
                    test.assertEqual(11, value2.get());
                });

                runner.test("with non-null function, await child after parent when parent throws non-matching error and child throws", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value1 = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value1.increment();
                        throw new FileNotFoundException("abc");
                    });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final Result<Boolean> result = asyncTask.catchErrorResult(QueueEmptyException.class, (Function0<Result<Boolean>>)() ->
                    {
                        value2.increment();
                        throw new FolderNotFoundException("blah");
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(0, value1.get());
                    test.assertEqual(10, value2.get());

                    test.assertThrows(asyncTask::await, new FileNotFoundException("abc"));
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());

                    test.assertThrows(result::await, new FileNotFoundException("abc"));
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());
                });

                runner.test("with non-null function, await child before parent when parent throws non-matching error and child throws", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value1 = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value1.increment();
                        throw new FileNotFoundException("abc");
                    });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final Result<Boolean> result = asyncTask.catchErrorResult(QueueEmptyException.class, (Function0<Result<Boolean>>)() ->
                    {
                        value2.increment();
                        throw new FolderNotFoundException("blah");
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(0, value1.get());
                    test.assertEqual(10, value2.get());

                    test.assertThrows(result::await, new FileNotFoundException("abc"));
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());

                    test.assertThrows(asyncTask::await, new FileNotFoundException("abc"));
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());
                });
            });

            runner.testGroup("catchErrorResult(Class<TError>,Function1<TError,Result<T>>)", () ->
            {
                runner.test("with null errorType", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value.increment();
                        return false;
                    });
                    test.assertThrows(() -> asyncTask.catchErrorResult(null, (FileNotFoundException error) -> Result.successTrue()),
                        new PreConditionFailure("errorType cannot be null."));
                });

                runner.test("with null function", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value.increment();
                        return true;
                    });
                    test.assertThrows(() -> asyncTask.catchErrorResult(FileNotFoundException.class, (Function1<FileNotFoundException,Result<Boolean>>)null),
                        new PreConditionFailure("function cannot be null."));
                });

                runner.test("with non-null function, await child after parent", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value1 = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value1.increment();
                        return false;
                    });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final Value<Throwable> value3 = Value.create();
                    final IntegerValue value4 = IntegerValue.create(100);
                    final Result<Boolean> result = asyncTask.catchErrorResult(FileNotFoundException.class, (FileNotFoundException parentError) ->
                    {
                        value2.increment();
                        value3.set(parentError);
                        return asyncRunner.schedule(() ->
                        {
                            value4.increment();
                            return true;
                        });
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(0, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());
                    test.assertEqual(100, value4.get());

                    test.assertFalse(asyncTask.await());
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());
                    test.assertEqual(100, value4.get());

                    test.assertFalse(result.await());
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());
                    test.assertEqual(100, value4.get());
                });

                runner.test("with non-null function, await child before parent", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value1 = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value1.increment();
                        return true;
                    });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final Value<Throwable> value3 = Value.create();
                    final IntegerValue value4 = IntegerValue.create(100);
                    final Result<Boolean> result = asyncTask.catchErrorResult(FileNotFoundException.class, (FileNotFoundException parentError) ->
                    {
                        value2.increment();
                        value3.set(parentError);
                        return asyncRunner.schedule(() ->
                        {
                            value4.increment();
                            return false;
                        });
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(0, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());
                    test.assertEqual(100, value4.get());

                    test.assertTrue(result.await());
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());
                    test.assertEqual(100, value4.get());

                    test.assertTrue(asyncTask.await());
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());
                    test.assertEqual(100, value4.get());
                });

                runner.test("with non-null function, await child after parent when parent throws matching error", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() -> { throw new FileNotFoundException("blah"); });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final Value<Throwable> value3 = Value.create();
                    final IntegerValue value4 = IntegerValue.create(100);
                    final Result<Boolean> result = asyncTask.catchErrorResult(FileNotFoundException.class, (FileNotFoundException parentError) ->
                    {
                        value2.increment();
                        value3.set(parentError);
                        return asyncRunner.schedule(() ->
                        {
                            value4.increment();
                            return true;
                        });
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());
                    test.assertEqual(100, value4.get());

                    test.assertThrows(asyncTask::await, new FileNotFoundException("blah"));
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());
                    test.assertEqual(100, value4.get());

                    test.assertTrue(result.await());
                    test.assertEqual(11, value2.get());
                    test.assertEqual(new FileNotFoundException("blah"), value3.get());
                    test.assertEqual(101, value4.get());
                });

                runner.test("with non-null function, await child before parent when parent throws matching error", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() -> { throw new FileNotFoundException("blah"); });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final Value<Throwable> value3 = Value.create();
                    final IntegerValue value4 = IntegerValue.create(100);
                    final Result<Boolean> result = asyncTask.catchErrorResult(FileNotFoundException.class, (FileNotFoundException parentError) ->
                    {
                        value2.increment();
                        value3.set(parentError);
                        return asyncRunner.schedule(() ->
                        {
                            value4.increment();
                            return false;
                        });
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());
                    test.assertEqual(100, value4.get());

                    test.assertFalse(result.await());
                    test.assertEqual(11, value2.get());
                    test.assertEqual(new FileNotFoundException("blah"), value3.get());
                    test.assertEqual(101, value4.get());

                    test.assertThrows(asyncTask::await, new FileNotFoundException("blah"));
                    test.assertEqual(11, value2.get());
                    test.assertEqual(new FileNotFoundException("blah"), value3.get());
                    test.assertEqual(101, value4.get());
                });

                runner.test("with non-null function, await child after parent when parent throws non-matching error", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() -> { throw new FileNotFoundException("blah"); });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final Value<Throwable> value3 = Value.create();
                    final IntegerValue value4 = IntegerValue.create(100);
                    final Result<Boolean> result = asyncTask.catchErrorResult(FolderNotFoundException.class, (FolderNotFoundException parentError) ->
                    {
                        value2.increment();
                        value3.set(parentError);
                        return asyncRunner.schedule(() ->
                        {
                            value4.increment();
                            return true;
                        });
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());
                    test.assertEqual(100, value4.get());

                    test.assertThrows(asyncTask::await, new FileNotFoundException("blah"));
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());
                    test.assertEqual(100, value4.get());

                    test.assertThrows(result::await, new FileNotFoundException("blah"));
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());
                    test.assertEqual(100, value4.get());
                });

                runner.test("with non-null function, await child before parent when parent throws non-matching error", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() -> { throw new FileNotFoundException("blah"); });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final Value<Throwable> value3 = Value.create();
                    final IntegerValue value4 = IntegerValue.create(100);
                    final Result<Boolean> result = asyncTask.catchErrorResult(FolderNotFoundException.class, (FolderNotFoundException parentError) ->
                    {
                        value2.increment();
                        value3.set(parentError);
                        return asyncRunner.schedule(() ->
                        {
                            value4.increment();
                            return false;
                        });
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());
                    test.assertEqual(100, value4.get());

                    test.assertThrows(result::await, new FileNotFoundException("blah"));
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());
                    test.assertEqual(100, value4.get());

                    test.assertThrows(asyncTask::await, new FileNotFoundException("blah"));
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());
                    test.assertEqual(100, value4.get());
                });

                runner.test("with non-null function, await child after parent when child throws", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value1 = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value1.increment();
                        return true;
                    });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final Value<Throwable> value3 = Value.create();
                    final Result<Boolean> result = asyncTask.catchErrorResult(FileNotFoundException.class, (Function1<FileNotFoundException,Result<Boolean>>)(FileNotFoundException parentError) ->
                    {
                        value2.increment();
                        value3.set(parentError);
                        throw new FolderNotFoundException("blah");
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(0, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());

                    test.assertTrue(asyncTask.await());
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());

                    test.assertTrue(result.await());
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());
                });

                runner.test("with non-null function, await child before parent when child throws", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value1 = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value1.increment();
                        return true;
                    });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final Value<Throwable> value3 = Value.create();
                    final Result<Boolean> result = asyncTask.catchErrorResult(FileNotFoundException.class, (Function1<FileNotFoundException,Result<Boolean>>)(FileNotFoundException parentError) ->
                    {
                        value2.increment();
                        value3.set(parentError);
                        throw new FolderNotFoundException("blah");
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(0, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());

                    test.assertTrue(result.await());
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());

                    test.assertTrue(asyncTask.await());
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());
                });

                runner.test("with non-null function, await child after parent when parent throws matching error and child throws", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value1 = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value1.increment();
                        throw new FileNotFoundException("abc");
                    });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final Value<Throwable> value3 = Value.create();
                    final Result<Boolean> result = asyncTask.catchErrorResult(FileNotFoundException.class, (Function1<FileNotFoundException,Result<Boolean>>)(FileNotFoundException parentError) ->
                    {
                        value2.increment();
                        value3.set(parentError);
                        throw new FolderNotFoundException("blah");
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(0, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());

                    test.assertThrows(asyncTask::await, new FileNotFoundException("abc"));
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());

                    test.assertThrows(result::await, new FolderNotFoundException("blah"));
                    test.assertEqual(1, value1.get());
                    test.assertEqual(11, value2.get());
                    test.assertEqual(new FileNotFoundException("abc"), value3.get());
                });

                runner.test("with non-null function, await child before parent when parent throws matching error and child throws", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value1 = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value1.increment();
                        throw new FileNotFoundException("abc");
                    });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final Value<Throwable> value3 = Value.create();
                    final Result<Boolean> result = asyncTask.catchErrorResult(FileNotFoundException.class, (Function1<FileNotFoundException,Result<Boolean>>)(FileNotFoundException parentError) ->
                    {
                        value2.increment();
                        value3.set(parentError);
                        throw new FolderNotFoundException("blah");
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(0, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());

                    test.assertThrows(result::await, new FolderNotFoundException("blah"));
                    test.assertEqual(1, value1.get());
                    test.assertEqual(11, value2.get());
                    test.assertEqual(new FileNotFoundException("abc"), value3.get());

                    test.assertThrows(asyncTask::await, new FileNotFoundException("abc"));
                    test.assertEqual(1, value1.get());
                    test.assertEqual(11, value2.get());
                    test.assertEqual(new FileNotFoundException("abc"), value3.get());
                });

                runner.test("with non-null function, await child after parent when parent throws non-matching error and child throws", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value1 = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value1.increment();
                        throw new FileNotFoundException("abc");
                    });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final Value<Throwable> value3 = Value.create();
                    final Result<Boolean> result = asyncTask.catchErrorResult(QueueEmptyException.class, (Function1<QueueEmptyException,Result<Boolean>>)(QueueEmptyException parentError) ->
                    {
                        value2.increment();
                        value3.set(parentError);
                        throw new FolderNotFoundException("blah");
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(0, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());

                    test.assertThrows(asyncTask::await, new FileNotFoundException("abc"));
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());

                    test.assertThrows(result::await, new FileNotFoundException("abc"));
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());
                });

                runner.test("with non-null function, await child before parent when parent throws non-matching error and child throws", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value1 = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value1.increment();
                        throw new FileNotFoundException("abc");
                    });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final Value<Throwable> value3 = Value.create();
                    final Result<Boolean> result = asyncTask.catchErrorResult(QueueEmptyException.class, (Function1<QueueEmptyException,Result<Boolean>>)(QueueEmptyException parentError) ->
                    {
                        value2.increment();
                        value3.set(parentError);
                        throw new FolderNotFoundException("blah");
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(0, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());

                    test.assertThrows(result::await, new FileNotFoundException("abc"));
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());

                    test.assertThrows(asyncTask::await, new FileNotFoundException("abc"));
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());
                });
            });

            runner.testGroup("onError(Action0)", () ->
            {
                runner.test("with null action", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value.increment();
                        return true;
                    });
                    test.assertThrows(() -> asyncTask.onError((Action0)null),
                        new PreConditionFailure("action cannot be null."));
                });

                runner.test("with non-null action, await child after parent", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value1 = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value1.increment();
                        return false;
                    });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final Result<Boolean> result = asyncTask.onError(() -> { value2.increment(); });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(0, value1.get());
                    test.assertEqual(10, value2.get());

                    test.assertFalse(asyncTask.await());
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());

                    test.assertFalse(result.await());
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());
                });

                runner.test("with non-null action, await child before parent", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value1 = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value1.increment();
                        return true;
                    });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final Result<Boolean> result = asyncTask.onError(() -> { value2.increment(); });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(0, value1.get());
                    test.assertEqual(10, value2.get());

                    test.assertTrue(result.await());
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());

                    test.assertTrue(asyncTask.await());
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());
                });

                runner.test("with non-null action, await child after parent when parent throws", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() -> { throw new FileNotFoundException("blah"); });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final Result<Boolean> result = asyncTask.onError(() -> { value2.increment(); });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(10, value2.get());

                    test.assertThrows(asyncTask::await, new FileNotFoundException("blah"));
                    test.assertEqual(10, value2.get());

                    test.assertThrows(result::await, new FileNotFoundException("blah"));
                    test.assertEqual(11, value2.get());
                });

                runner.test("with non-null action, await child before parent when parent throws", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() -> { throw new FileNotFoundException("blah"); });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final Result<Boolean> result = asyncTask.onError(() -> { value2.increment(); });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(10, value2.get());

                    test.assertThrows(result::await, new FileNotFoundException("blah"));
                    test.assertEqual(11, value2.get());

                    test.assertThrows(asyncTask::await, new FileNotFoundException("blah"));
                    test.assertEqual(11, value2.get());
                });

                runner.test("with non-null action, await child after parent when child throws", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value1 = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value1.increment();
                        return true;
                    });

                    final Result<Boolean> result = asyncTask.onError((Action0)() -> { throw new FolderNotFoundException("blah"); });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(0, value1.get());

                    test.assertTrue(asyncTask.await());
                    test.assertEqual(1, value1.get());

                    test.assertTrue(result.await());
                    test.assertEqual(1, value1.get());
                });

                runner.test("with non-null action, await child before parent when child throws", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value1 = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value1.increment();
                        return true;
                    });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final Result<Boolean> result = asyncTask.onError((Action0)() ->
                    {
                        value2.increment();
                        throw new FolderNotFoundException("blah");
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(0, value1.get());
                    test.assertEqual(10, value2.get());

                    test.assertTrue(result.await());
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());

                    test.assertTrue(asyncTask.await());
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());
                });

                runner.test("with non-null action, await child after parent when parent and child throw", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value1 = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value1.increment();
                        throw new FileNotFoundException("abc");
                    });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final Result<Boolean> result = asyncTask.onError((Action0)() ->
                    {
                        value2.increment();
                        throw new FolderNotFoundException("blah");
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(0, value1.get());
                    test.assertEqual(10, value2.get());

                    test.assertThrows(asyncTask::await, new FileNotFoundException("abc"));
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());

                    test.assertThrows(result::await, new FolderNotFoundException("blah"));
                    test.assertEqual(1, value1.get());
                    test.assertEqual(11, value2.get());
                });

                runner.test("with non-null action, await child before parent when parent and child throw", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value1 = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value1.increment();
                        throw new FileNotFoundException("abc");
                    });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final Result<Boolean> result = asyncTask.onError((Action0)() ->
                    {
                        value2.increment();
                        throw new FolderNotFoundException("blah");
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(0, value1.get());
                    test.assertEqual(10, value2.get());

                    test.assertThrows(result::await, new FolderNotFoundException("blah"));
                    test.assertEqual(1, value1.get());
                    test.assertEqual(11, value2.get());

                    test.assertThrows(asyncTask::await, new FileNotFoundException("abc"));
                    test.assertEqual(1, value1.get());
                    test.assertEqual(11, value2.get());
                });
            });

            runner.testGroup("onError(Action1<Throwable>)", () ->
            {
                runner.test("with null action", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value.increment();
                        return true;
                    });
                    test.assertThrows(() -> asyncTask.onError((Action1<Throwable>)null),
                        new PreConditionFailure("action cannot be null."));
                });

                runner.test("with non-null action, await child after parent", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value1 = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value1.increment();
                        return false;
                    });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final Value<Throwable> value3 = Value.create();
                    final Result<Boolean> result = asyncTask.onError((Throwable parentError) ->
                    {
                        value2.increment();
                        value3.set(parentError);
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(0, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());

                    test.assertFalse(asyncTask.await());
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());

                    test.assertFalse(result.await());
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());
                });

                runner.test("with non-null action, await child before parent", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value1 = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value1.increment();
                        return true;
                    });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final Value<Throwable> value3 = Value.create();
                    final Result<Boolean> result = asyncTask.onError((Throwable parentError) ->
                    {
                        value2.increment();
                        value3.set(parentError);
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(0, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());

                    test.assertTrue(result.await());
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());

                    test.assertTrue(asyncTask.await());
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());
                });

                runner.test("with non-null action, await child after parent when parent throws", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() -> { throw new FileNotFoundException("blah"); });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final Value<Throwable> value3 = Value.create();
                    final Result<Boolean> result = asyncTask.onError((Throwable parentError) ->
                    {
                        value2.increment();
                        value3.set(parentError);
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());

                    test.assertThrows(asyncTask::await, new FileNotFoundException("blah"));
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());

                    test.assertThrows(result::await, new FileNotFoundException("blah"));
                    test.assertEqual(11, value2.get());
                    test.assertEqual(new FileNotFoundException("blah"), value3.get());
                });

                runner.test("with non-null action, await child before parent when parent throws", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() -> { throw new FileNotFoundException("blah"); });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final Value<Throwable> value3 = Value.create();
                    final Result<Boolean> result = asyncTask.onError((Throwable parentError) ->
                    {
                        value2.increment();
                        value3.set(parentError);
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());

                    test.assertThrows(result::await, new FileNotFoundException("blah"));
                    test.assertEqual(11, value2.get());
                    test.assertEqual(new FileNotFoundException("blah"), value3.get());

                    test.assertThrows(asyncTask::await, new FileNotFoundException("blah"));
                    test.assertEqual(11, value2.get());
                    test.assertEqual(new FileNotFoundException("blah"), value3.get());
                });

                runner.test("with non-null action, await child after parent when child throws", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value1 = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value1.increment();
                        return true;
                    });

                    final Value<Throwable> value3 = Value.create();
                    final Result<Boolean> result = asyncTask.onError((Action1<Throwable>)(Throwable parentError) ->
                    {
                        value3.set(parentError);
                        throw new FolderNotFoundException("blah");
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(0, value1.get());
                    test.assertFalse(value3.hasValue());

                    test.assertTrue(asyncTask.await());
                    test.assertEqual(1, value1.get());
                    test.assertFalse(value3.hasValue());

                    test.assertTrue(result.await());
                    test.assertEqual(1, value1.get());
                    test.assertFalse(value3.hasValue());
                });

                runner.test("with non-null action, await child before parent when child throws", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value1 = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value1.increment();
                        return true;
                    });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final Value<Throwable> value3 = Value.create();
                    final Result<Boolean> result = asyncTask.onError((Action1<Throwable>)(Throwable parentError) ->
                    {
                        value2.increment();
                        value3.set(parentError);
                        throw new FolderNotFoundException("blah");
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(0, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());

                    test.assertTrue(result.await());
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());

                    test.assertTrue(asyncTask.await());
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());
                });

                runner.test("with non-null action, await child after parent when parent and child throw", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value1 = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value1.increment();
                        throw new FileNotFoundException("abc");
                    });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final Value<Throwable> value3 = Value.create();
                    final Result<Boolean> result = asyncTask.onError((Action1<Throwable>)(Throwable parentError) ->
                    {
                        value2.increment();
                        value3.set(parentError);
                        throw new FolderNotFoundException("blah");
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(0, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());

                    test.assertThrows(asyncTask::await, new FileNotFoundException("abc"));
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());

                    test.assertThrows(result::await, new FolderNotFoundException("blah"));
                    test.assertEqual(1, value1.get());
                    test.assertEqual(11, value2.get());
                    test.assertEqual(new FileNotFoundException("abc"), value3.get());
                });

                runner.test("with non-null action, await child before parent when parent and child throw", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value1 = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value1.increment();
                        throw new FileNotFoundException("abc");
                    });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final Value<Throwable> value3 = Value.create();
                    final Result<Boolean> result = asyncTask.onError((Action1<Throwable>)(Throwable parentError) ->
                    {
                        value2.increment();
                        value3.set(parentError);
                        throw new FolderNotFoundException("blah");
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(0, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());

                    test.assertThrows(result::await, new FolderNotFoundException("blah"));
                    test.assertEqual(1, value1.get());
                    test.assertEqual(11, value2.get());
                    test.assertEqual(new FileNotFoundException("abc"), value3.get());

                    test.assertThrows(asyncTask::await, new FileNotFoundException("abc"));
                    test.assertEqual(1, value1.get());
                    test.assertEqual(11, value2.get());
                    test.assertEqual(new FileNotFoundException("abc"), value3.get());
                });
            });

            runner.testGroup("onError(Class<TError>,Action0)", () ->
            {
                runner.test("with null errorType", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value.increment();
                        return false;
                    });
                    test.assertThrows(() -> asyncTask.onError(null, () -> {}),
                        new PreConditionFailure("errorType cannot be null."));
                });

                runner.test("with null action", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value.increment();
                        return true;
                    });
                    test.assertThrows(() -> asyncTask.onError(FileNotFoundException.class, (Action0)null),
                        new PreConditionFailure("action cannot be null."));
                });

                runner.test("with non-null action, await child after parent", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value1 = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value1.increment();
                        return false;
                    });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final Result<Boolean> result = asyncTask.onError(FileNotFoundException.class, () ->
                    {
                        value2.increment();
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(0, value1.get());
                    test.assertEqual(10, value2.get());

                    test.assertFalse(asyncTask.await());
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());

                    test.assertFalse(result.await());
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());
                });

                runner.test("with non-null action, await child before parent", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value1 = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value1.increment();
                        return true;
                    });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final Result<Boolean> result = asyncTask.onError(FileNotFoundException.class, () ->
                    {
                        value2.increment();
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(0, value1.get());
                    test.assertEqual(10, value2.get());

                    test.assertTrue(result.await());
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());

                    test.assertTrue(asyncTask.await());
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());
                });

                runner.test("with non-null action, await child after parent when parent throws matching error", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() -> { throw new FileNotFoundException("blah"); });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final Result<Boolean> result = asyncTask.onError(FileNotFoundException.class, () ->
                    {
                        value2.increment();
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(10, value2.get());

                    test.assertThrows(asyncTask::await, new FileNotFoundException("blah"));
                    test.assertEqual(10, value2.get());

                    test.assertThrows(result::await, new FileNotFoundException("blah"));
                    test.assertEqual(11, value2.get());
                });

                runner.test("with non-null action, await child before parent when parent throws matching error", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() -> { throw new FileNotFoundException("blah"); });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final Result<Boolean> result = asyncTask.onError(FileNotFoundException.class, () ->
                    {
                        value2.increment();
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(10, value2.get());

                    test.assertThrows(result::await, new FileNotFoundException("blah"));
                    test.assertEqual(11, value2.get());

                    test.assertThrows(asyncTask::await, new FileNotFoundException("blah"));
                    test.assertEqual(11, value2.get());
                });

                runner.test("with non-null action, await child after parent when parent throws non-matching error", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() -> { throw new FileNotFoundException("blah"); });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final Result<Boolean> result = asyncTask.onError(FolderNotFoundException.class, () ->
                    {
                        value2.increment();
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(10, value2.get());

                    test.assertThrows(asyncTask::await, new FileNotFoundException("blah"));
                    test.assertEqual(10, value2.get());

                    test.assertThrows(result::await, new FileNotFoundException("blah"));
                    test.assertEqual(10, value2.get());
                });

                runner.test("with non-null action, await child before parent when parent throws non-matching error", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() -> { throw new FileNotFoundException("blah"); });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final Result<Boolean> result = asyncTask.onError(FolderNotFoundException.class, () ->
                    {
                        value2.increment();
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(10, value2.get());

                    test.assertThrows(result::await, new FileNotFoundException("blah"));
                    test.assertEqual(10, value2.get());

                    test.assertThrows(asyncTask::await, new FileNotFoundException("blah"));
                    test.assertEqual(10, value2.get());
                });

                runner.test("with non-null action, await child after parent when child throws", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value1 = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value1.increment();
                        return true;
                    });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final Result<Boolean> result = asyncTask.onError(FileNotFoundException.class, (Action0)() ->
                    {
                        value2.increment();
                        throw new FolderNotFoundException("blah");
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(0, value1.get());
                    test.assertEqual(10, value2.get());

                    test.assertTrue(asyncTask.await());
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());

                    test.assertTrue(result.await());
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());
                });

                runner.test("with non-null action, await child before parent when child throws", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value1 = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value1.increment();
                        return true;
                    });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final Result<Boolean> result = asyncTask.onError(FileNotFoundException.class, (Action0)() ->
                    {
                        value2.increment();
                        throw new FolderNotFoundException("blah");
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(0, value1.get());
                    test.assertEqual(10, value2.get());

                    test.assertTrue(result.await());
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());

                    test.assertTrue(asyncTask.await());
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());
                });

                runner.test("with non-null action, await child after parent when parent throws matching error and child throws", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value1 = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value1.increment();
                        throw new FileNotFoundException("abc");
                    });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final Result<Boolean> result = asyncTask.onError(FileNotFoundException.class, (Action0)() ->
                    {
                        value2.increment();
                        throw new FolderNotFoundException("blah");
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(0, value1.get());
                    test.assertEqual(10, value2.get());

                    test.assertThrows(asyncTask::await, new FileNotFoundException("abc"));
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());

                    test.assertThrows(result::await, new FolderNotFoundException("blah"));
                    test.assertEqual(1, value1.get());
                    test.assertEqual(11, value2.get());
                });

                runner.test("with non-null action, await child before parent when parent throws matching error and child throws", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value1 = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value1.increment();
                        throw new FileNotFoundException("abc");
                    });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final Result<Boolean> result = asyncTask.onError(FileNotFoundException.class, (Action0)() ->
                    {
                        value2.increment();
                        throw new FolderNotFoundException("blah");
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(0, value1.get());
                    test.assertEqual(10, value2.get());

                    test.assertThrows(result::await, new FolderNotFoundException("blah"));
                    test.assertEqual(1, value1.get());
                    test.assertEqual(11, value2.get());

                    test.assertThrows(asyncTask::await, new FileNotFoundException("abc"));
                    test.assertEqual(1, value1.get());
                    test.assertEqual(11, value2.get());
                });

                runner.test("with non-null action, await child after parent when parent throws non-matching error and child throws", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value1 = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value1.increment();
                        throw new FileNotFoundException("abc");
                    });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final Result<Boolean> result = asyncTask.onError(QueueEmptyException.class, (Action0)() ->
                    {
                        value2.increment();
                        throw new FolderNotFoundException("blah");
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(0, value1.get());
                    test.assertEqual(10, value2.get());

                    test.assertThrows(asyncTask::await, new FileNotFoundException("abc"));
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());

                    test.assertThrows(result::await, new FileNotFoundException("abc"));
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());
                });

                runner.test("with non-null action, await child before parent when parent throws non-matching error and child throws", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value1 = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value1.increment();
                        throw new FileNotFoundException("abc");
                    });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final Result<Boolean> result = asyncTask.onError(QueueEmptyException.class, (Action0)() ->
                    {
                        value2.increment();
                        throw new FolderNotFoundException("blah");
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(0, value1.get());
                    test.assertEqual(10, value2.get());

                    test.assertThrows(result::await, new FileNotFoundException("abc"));
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());

                    test.assertThrows(asyncTask::await, new FileNotFoundException("abc"));
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());
                });
            });

            runner.testGroup("onError(Class<TError>,Action1<TError>)", () ->
            {
                runner.test("with null errorType", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value.increment();
                        return false;
                    });
                    test.assertThrows(() -> asyncTask.onError(null, (FileNotFoundException error) -> {}),
                        new PreConditionFailure("errorType cannot be null."));
                });

                runner.test("with null action", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value.increment();
                        return true;
                    });
                    test.assertThrows(() -> asyncTask.onError(FileNotFoundException.class, (Action1<FileNotFoundException>)null),
                        new PreConditionFailure("action cannot be null."));
                });

                runner.test("with non-null action, await child after parent", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value1 = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value1.increment();
                        return false;
                    });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final Value<Throwable> value3 = Value.create();
                    final Result<Boolean> result = asyncTask.onError(FileNotFoundException.class, (FileNotFoundException parentError) ->
                    {
                        value2.increment();
                        value3.set(parentError);
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(0, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());

                    test.assertFalse(asyncTask.await());
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());

                    test.assertFalse(result.await());
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());
                });

                runner.test("with non-null action, await child before parent", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value1 = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value1.increment();
                        return true;
                    });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final Value<Throwable> value3 = Value.create();
                    final Result<Boolean> result = asyncTask.onError(FileNotFoundException.class, (FileNotFoundException parentError) ->
                    {
                        value2.increment();
                        value3.set(parentError);
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(0, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());

                    test.assertTrue(result.await());
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());

                    test.assertTrue(asyncTask.await());
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());
                });

                runner.test("with non-null action, await child after parent when parent throws matching error", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() -> { throw new FileNotFoundException("blah"); });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final Value<Throwable> value3 = Value.create();
                    final Result<Boolean> result = asyncTask.onError(FileNotFoundException.class, (FileNotFoundException parentError) ->
                    {
                        value2.increment();
                        value3.set(parentError);
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());

                    test.assertThrows(asyncTask::await, new FileNotFoundException("blah"));
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());

                    test.assertThrows(result::await, new FileNotFoundException("blah"));
                    test.assertEqual(11, value2.get());
                    test.assertEqual(new FileNotFoundException("blah"), value3.get());
                });

                runner.test("with non-null action, await child before parent when parent throws matching error", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() -> { throw new FileNotFoundException("blah"); });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final Value<Throwable> value3 = Value.create();
                    final Result<Boolean> result = asyncTask.onError(FileNotFoundException.class, (FileNotFoundException parentError) ->
                    {
                        value2.increment();
                        value3.set(parentError);
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());

                    test.assertThrows(result::await, new FileNotFoundException("blah"));
                    test.assertEqual(11, value2.get());
                    test.assertEqual(new FileNotFoundException("blah"), value3.get());

                    test.assertThrows(asyncTask::await, new FileNotFoundException("blah"));
                    test.assertEqual(11, value2.get());
                    test.assertEqual(new FileNotFoundException("blah"), value3.get());
                });

                runner.test("with non-null action, await child after parent when parent throws non-matching error", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() -> { throw new FileNotFoundException("blah"); });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final Value<Throwable> value3 = Value.create();
                    final Result<Boolean> result = asyncTask.onError(FolderNotFoundException.class, (FolderNotFoundException parentError) ->
                    {
                        value2.increment();
                        value3.set(parentError);
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());

                    test.assertThrows(asyncTask::await, new FileNotFoundException("blah"));
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());

                    test.assertThrows(result::await, new FileNotFoundException("blah"));
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());
                });

                runner.test("with non-null action, await child before parent when parent throws non-matching error", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() -> { throw new FileNotFoundException("blah"); });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final Value<Throwable> value3 = Value.create();
                    final Result<Boolean> result = asyncTask.onError(FolderNotFoundException.class, (FolderNotFoundException parentError) ->
                    {
                        value2.increment();
                        value3.set(parentError);
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());

                    test.assertThrows(result::await, new FileNotFoundException("blah"));
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());

                    test.assertThrows(asyncTask::await, new FileNotFoundException("blah"));
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());
                });

                runner.test("with non-null action, await child after parent when child throws", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value1 = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value1.increment();
                        return true;
                    });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final Value<Throwable> value3 = Value.create();
                    final Result<Boolean> result = asyncTask.onError(FileNotFoundException.class, (Action1<FileNotFoundException>)(FileNotFoundException parentError) ->
                    {
                        value2.increment();
                        value3.set(parentError);
                        throw new FolderNotFoundException("blah");
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(0, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());

                    test.assertTrue(asyncTask.await());
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());

                    test.assertTrue(result.await());
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());
                });

                runner.test("with non-null action, await child before parent when child throws", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value1 = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value1.increment();
                        return true;
                    });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final Value<Throwable> value3 = Value.create();
                    final Result<Boolean> result = asyncTask.onError(FileNotFoundException.class, (Action1<FileNotFoundException>)(FileNotFoundException parentError) ->
                    {
                        value2.increment();
                        value3.set(parentError);
                        throw new FolderNotFoundException("blah");
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(0, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());

                    test.assertTrue(result.await());
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());

                    test.assertTrue(asyncTask.await());
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());
                });

                runner.test("with non-null action, await child after parent when parent throws matching error and child throws", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value1 = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value1.increment();
                        throw new FileNotFoundException("abc");
                    });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final Value<Throwable> value3 = Value.create();
                    final Result<Boolean> result = asyncTask.onError(FileNotFoundException.class, (Action1<FileNotFoundException>)(FileNotFoundException parentError) ->
                    {
                        value2.increment();
                        value3.set(parentError);
                        throw new FolderNotFoundException("blah");
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(0, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());

                    test.assertThrows(asyncTask::await, new FileNotFoundException("abc"));
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());

                    test.assertThrows(result::await, new FolderNotFoundException("blah"));
                    test.assertEqual(1, value1.get());
                    test.assertEqual(11, value2.get());
                    test.assertEqual(new FileNotFoundException("abc"), value3.get());
                });

                runner.test("with non-null action, await child before parent when parent throws matching error and child throws", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value1 = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value1.increment();
                        throw new FileNotFoundException("abc");
                    });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final Value<Throwable> value3 = Value.create();
                    final Result<Boolean> result = asyncTask.onError(FileNotFoundException.class, (Action1<FileNotFoundException>)(FileNotFoundException parentError) ->
                    {
                        value2.increment();
                        value3.set(parentError);
                        throw new FolderNotFoundException("blah");
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(0, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());

                    test.assertThrows(result::await, new FolderNotFoundException("blah"));
                    test.assertEqual(1, value1.get());
                    test.assertEqual(11, value2.get());
                    test.assertEqual(new FileNotFoundException("abc"), value3.get());

                    test.assertThrows(asyncTask::await, new FileNotFoundException("abc"));
                    test.assertEqual(1, value1.get());
                    test.assertEqual(11, value2.get());
                    test.assertEqual(new FileNotFoundException("abc"), value3.get());
                });

                runner.test("with non-null action, await child after parent when parent throws non-matching error and child throws", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value1 = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value1.increment();
                        throw new FileNotFoundException("abc");
                    });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final Value<Throwable> value3 = Value.create();
                    final Result<Boolean> result = asyncTask.onError(QueueEmptyException.class, (Action1<QueueEmptyException>)(QueueEmptyException parentError) ->
                    {
                        value2.increment();
                        value3.set(parentError);
                        throw new FolderNotFoundException("blah");
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(0, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());

                    test.assertThrows(asyncTask::await, new FileNotFoundException("abc"));
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());

                    test.assertThrows(result::await, new FileNotFoundException("abc"));
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());
                });

                runner.test("with non-null action, await child before parent when parent throws non-matching error and child throws", (Test test) ->
                {
                    final ResultManualAsyncRunner asyncRunner = new ResultManualAsyncRunner();
                    final IntegerValue value1 = IntegerValue.create(0);
                    final ResultAsyncTask<Boolean> asyncTask = asyncRunner.schedule(() ->
                    {
                        value1.increment();
                        throw new FileNotFoundException("abc");
                    });

                    final IntegerValue value2 = IntegerValue.create(10);
                    final Value<Throwable> value3 = Value.create();
                    final Result<Boolean> result = asyncTask.onError(QueueEmptyException.class, (Action1<QueueEmptyException>)(QueueEmptyException parentError) ->
                    {
                        value2.increment();
                        value3.set(parentError);
                        throw new FolderNotFoundException("blah");
                    });
                    test.assertNotNull(result);
                    test.assertEqual(Iterable.create(result), asyncTask.getNextTasks());

                    test.assertEqual(0, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());

                    test.assertThrows(result::await, new FileNotFoundException("abc"));
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());

                    test.assertThrows(asyncTask::await, new FileNotFoundException("abc"));
                    test.assertEqual(1, value1.get());
                    test.assertEqual(10, value2.get());
                    test.assertFalse(value3.hasValue());
                });
            });
        });
    }
}
