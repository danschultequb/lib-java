package qub;

public interface ExceptionsTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(Exceptions.class, () ->
        {
            runner.testGroup("asRuntime(Throwable)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> Exceptions.asRuntime(null),
                        new PreConditionFailure("error cannot be null."));
                });

                runner.test("with RuntimeException", (Test test) ->
                {
                    final RuntimeException error = new RuntimeException("blah");
                    test.assertSame(error, Exceptions.asRuntime(error));
                });

                runner.test("with NotFoundException", (Test test) ->
                {
                    final NotFoundException error = new NotFoundException("blah");
                    test.assertSame(error, Exceptions.asRuntime(error));
                });

                runner.test("with AwaitException", (Test test) ->
                {
                    final AwaitException error = new AwaitException(new RuntimeException("blah"));
                    test.assertSame(error, Exceptions.asRuntime(error));
                });

                runner.test("with Exception", (Test test) ->
                {
                    final Exception error = new Exception("blah");
                    test.assertEqual(new RuntimeException(error), Exceptions.asRuntime(error));
                });
            });

            runner.testGroup("instanceOf(Throwable,Class<TError>)", () ->
            {
                runner.test("with null error", (Test test) ->
                {
                    test.assertFalse(Exceptions.instanceOf(null, NotFoundException.class));
                });

                runner.test("with null type", (Test test) ->
                {
                    test.assertThrows(() -> Exceptions.instanceOf(new EndOfStreamException(), null),
                        new PreConditionFailure("type cannot be null."));
                });

                runner.test("with same type", (Test test) ->
                {
                    test.assertTrue(Exceptions.instanceOf(new EndOfStreamException(), EndOfStreamException.class));
                });

                runner.test("with derived type", (Test test) ->
                {
                    test.assertTrue(Exceptions.instanceOf(new EndOfStreamException(), RuntimeException.class));
                });

                runner.test("with parent type", (Test test) ->
                {
                    test.assertFalse(Exceptions.instanceOf(new RuntimeException("blah"), EndOfStreamException.class));
                });

                runner.test("with grandparent type", (Test test) ->
                {
                    test.assertTrue(Exceptions.instanceOf(new EndOfStreamException(), Exception.class));
                });

                runner.test("with great-grandparent type", (Test test) ->
                {
                    test.assertTrue(Exceptions.instanceOf(new EndOfStreamException(), Throwable.class));
                });

                runner.test("with unrelated type", (Test test) ->
                {
                    test.assertFalse(Exceptions.instanceOf(new EndOfStreamException(), java.io.IOException.class));
                });

                runner.test("with RuntimeException wrapped matching error", (Test test) ->
                {
                    test.assertTrue(Exceptions.instanceOf(new RuntimeException(new EndOfStreamException()), EndOfStreamException.class));
                });

                runner.test("with RuntimeException twice wrapped matching error", (Test test) ->
                {
                    test.assertTrue(Exceptions.instanceOf(new RuntimeException(new RuntimeException(new EndOfStreamException())), EndOfStreamException.class));
                });

                runner.test("with AwaitException wrapped matching error", (Test test) ->
                {
                    test.assertTrue(Exceptions.instanceOf(new AwaitException(new EndOfStreamException()), EndOfStreamException.class));
                });

                runner.test("with AwaitException twice wrapped matching error", (Test test) ->
                {
                    test.assertTrue(Exceptions.instanceOf(new AwaitException(new AwaitException(new EndOfStreamException())), EndOfStreamException.class));
                });
            });

            runner.testGroup("instanceOf(Throwable,Class<TError>,Iterable<Class<? extends Throwable>>)", () ->
            {
                runner.testGroup("with null errorTypesToGoPast", () ->
                {
                    runner.test("with null error", (Test test) ->
                    {
                        test.assertFalse(Exceptions.instanceOf(null, NotFoundException.class, null));
                    });

                    runner.test("with null type", (Test test) ->
                    {
                        test.assertThrows(() -> Exceptions.instanceOf(new EndOfStreamException(), null, null),
                            new PreConditionFailure("type cannot be null."));
                    });

                    runner.test("with same type", (Test test) ->
                    {
                        test.assertTrue(Exceptions.instanceOf(new EndOfStreamException(), EndOfStreamException.class, null));
                    });

                    runner.test("with derived type", (Test test) ->
                    {
                        test.assertTrue(Exceptions.instanceOf(new EndOfStreamException(), RuntimeException.class, null));
                    });

                    runner.test("with parent type", (Test test) ->
                    {
                        test.assertFalse(Exceptions.instanceOf(new RuntimeException("blah"), EndOfStreamException.class, null));
                    });

                    runner.test("with grandparent type", (Test test) ->
                    {
                        test.assertTrue(Exceptions.instanceOf(new EndOfStreamException(), Exception.class, null));
                    });

                    runner.test("with great-grandparent type", (Test test) ->
                    {
                        test.assertTrue(Exceptions.instanceOf(new EndOfStreamException(), Throwable.class, null));
                    });

                    runner.test("with unrelated type", (Test test) ->
                    {
                        test.assertFalse(Exceptions.instanceOf(new EndOfStreamException(), java.io.IOException.class, null));
                    });

                    runner.test("with RuntimeException wrapped matching error", (Test test) ->
                    {
                        test.assertFalse(Exceptions.instanceOf(new RuntimeException(new EndOfStreamException()), EndOfStreamException.class, null));
                    });

                    runner.test("with RuntimeException twice wrapped matching error", (Test test) ->
                    {
                        test.assertFalse(Exceptions.instanceOf(new RuntimeException(new RuntimeException(new EndOfStreamException())), EndOfStreamException.class, null));
                    });

                    runner.test("with AwaitException wrapped matching error", (Test test) ->
                    {
                        test.assertFalse(Exceptions.instanceOf(new AwaitException(new EndOfStreamException()), EndOfStreamException.class, null));
                    });

                    runner.test("with AwaitException twice wrapped matching error", (Test test) ->
                    {
                        test.assertFalse(Exceptions.instanceOf(new AwaitException(new AwaitException(new EndOfStreamException())), EndOfStreamException.class, null));
                    });
                });

                runner.testGroup("with empty errorTypesToGoPast", () ->
                {
                    runner.test("with null error", (Test test) ->
                    {
                        test.assertFalse(Exceptions.instanceOf(null, NotFoundException.class, Iterable.create()));
                    });

                    runner.test("with null type", (Test test) ->
                    {
                        test.assertThrows(() -> Exceptions.instanceOf(new EndOfStreamException(), null, Iterable.create()),
                            new PreConditionFailure("type cannot be null."));
                    });

                    runner.test("with same type", (Test test) ->
                    {
                        test.assertTrue(Exceptions.instanceOf(new EndOfStreamException(), EndOfStreamException.class, Iterable.create()));
                    });

                    runner.test("with derived type", (Test test) ->
                    {
                        test.assertTrue(Exceptions.instanceOf(new EndOfStreamException(), RuntimeException.class, Iterable.create()));
                    });

                    runner.test("with parent type", (Test test) ->
                    {
                        test.assertFalse(Exceptions.instanceOf(new RuntimeException("blah"), EndOfStreamException.class, Iterable.create()));
                    });

                    runner.test("with grandparent type", (Test test) ->
                    {
                        test.assertTrue(Exceptions.instanceOf(new EndOfStreamException(), Exception.class, Iterable.create()));
                    });

                    runner.test("with great-grandparent type", (Test test) ->
                    {
                        test.assertTrue(Exceptions.instanceOf(new EndOfStreamException(), Throwable.class, Iterable.create()));
                    });

                    runner.test("with unrelated type", (Test test) ->
                    {
                        test.assertFalse(Exceptions.instanceOf(new EndOfStreamException(), java.io.IOException.class, Iterable.create()));
                    });

                    runner.test("with RuntimeException wrapped matching error", (Test test) ->
                    {
                        test.assertFalse(Exceptions.instanceOf(new RuntimeException(new EndOfStreamException()), EndOfStreamException.class, Iterable.create()));
                    });

                    runner.test("with RuntimeException twice wrapped matching error", (Test test) ->
                    {
                        test.assertFalse(Exceptions.instanceOf(new RuntimeException(new RuntimeException(new EndOfStreamException())), EndOfStreamException.class, Iterable.create()));
                    });

                    runner.test("with AwaitException wrapped matching error", (Test test) ->
                    {
                        test.assertFalse(Exceptions.instanceOf(new AwaitException(new EndOfStreamException()), EndOfStreamException.class, Iterable.create()));
                    });

                    runner.test("with AwaitException twice wrapped matching error", (Test test) ->
                    {
                        test.assertFalse(Exceptions.instanceOf(new AwaitException(new AwaitException(new EndOfStreamException())), EndOfStreamException.class, Iterable.create()));
                    });
                });

                runner.testGroup("with RuntimeException errorTypesToGoPast", () ->
                {
                    runner.test("with null error", (Test test) ->
                    {
                        test.assertFalse(Exceptions.instanceOf(null, NotFoundException.class, Iterable.create(RuntimeException.class)));
                    });

                    runner.test("with null type", (Test test) ->
                    {
                        test.assertThrows(() -> Exceptions.instanceOf(new EndOfStreamException(), null, Iterable.create(RuntimeException.class)),
                            new PreConditionFailure("type cannot be null."));
                    });

                    runner.test("with same type", (Test test) ->
                    {
                        test.assertTrue(Exceptions.instanceOf(new EndOfStreamException(), EndOfStreamException.class, Iterable.create(RuntimeException.class)));
                    });

                    runner.test("with derived type", (Test test) ->
                    {
                        test.assertTrue(Exceptions.instanceOf(new EndOfStreamException(), RuntimeException.class, Iterable.create(RuntimeException.class)));
                    });

                    runner.test("with parent type", (Test test) ->
                    {
                        test.assertFalse(Exceptions.instanceOf(new RuntimeException("blah"), EndOfStreamException.class, Iterable.create(RuntimeException.class)));
                    });

                    runner.test("with grandparent type", (Test test) ->
                    {
                        test.assertTrue(Exceptions.instanceOf(new EndOfStreamException(), Exception.class, Iterable.create(RuntimeException.class)));
                    });

                    runner.test("with great-grandparent type", (Test test) ->
                    {
                        test.assertTrue(Exceptions.instanceOf(new EndOfStreamException(), Throwable.class, Iterable.create(RuntimeException.class)));
                    });

                    runner.test("with unrelated type", (Test test) ->
                    {
                        test.assertFalse(Exceptions.instanceOf(new EndOfStreamException(), java.io.IOException.class, Iterable.create(RuntimeException.class)));
                    });

                    runner.test("with RuntimeException wrapped matching error", (Test test) ->
                    {
                        test.assertTrue(Exceptions.instanceOf(new RuntimeException(new EndOfStreamException()), EndOfStreamException.class, Iterable.create(RuntimeException.class)));
                    });

                    runner.test("with RuntimeException twice wrapped matching error", (Test test) ->
                    {
                        test.assertTrue(Exceptions.instanceOf(new RuntimeException(new RuntimeException(new EndOfStreamException())), EndOfStreamException.class, Iterable.create(RuntimeException.class)));
                    });

                    runner.test("with AwaitException wrapped matching error", (Test test) ->
                    {
                        test.assertFalse(Exceptions.instanceOf(new AwaitException(new EndOfStreamException()), EndOfStreamException.class, Iterable.create(RuntimeException.class)));
                    });

                    runner.test("with AwaitException twice wrapped matching error", (Test test) ->
                    {
                        test.assertFalse(Exceptions.instanceOf(new AwaitException(new AwaitException(new EndOfStreamException())), EndOfStreamException.class, Iterable.create(RuntimeException.class)));
                    });
                });

                runner.testGroup("with AwaitException errorTypesToGoPast", () ->
                {
                    runner.test("with null error", (Test test) ->
                    {
                        test.assertFalse(Exceptions.instanceOf(null, NotFoundException.class, Iterable.create(AwaitException.class)));
                    });

                    runner.test("with null type", (Test test) ->
                    {
                        test.assertThrows(() -> Exceptions.instanceOf(new EndOfStreamException(), null, Iterable.create(AwaitException.class)),
                            new PreConditionFailure("type cannot be null."));
                    });

                    runner.test("with same type", (Test test) ->
                    {
                        test.assertTrue(Exceptions.instanceOf(new EndOfStreamException(), EndOfStreamException.class, Iterable.create(AwaitException.class)));
                    });

                    runner.test("with derived type", (Test test) ->
                    {
                        test.assertTrue(Exceptions.instanceOf(new EndOfStreamException(), RuntimeException.class, Iterable.create(AwaitException.class)));
                    });

                    runner.test("with parent type", (Test test) ->
                    {
                        test.assertFalse(Exceptions.instanceOf(new RuntimeException("blah"), EndOfStreamException.class, Iterable.create(AwaitException.class)));
                    });

                    runner.test("with grandparent type", (Test test) ->
                    {
                        test.assertTrue(Exceptions.instanceOf(new EndOfStreamException(), Exception.class, Iterable.create(AwaitException.class)));
                    });

                    runner.test("with great-grandparent type", (Test test) ->
                    {
                        test.assertTrue(Exceptions.instanceOf(new EndOfStreamException(), Throwable.class, Iterable.create(AwaitException.class)));
                    });

                    runner.test("with unrelated type", (Test test) ->
                    {
                        test.assertFalse(Exceptions.instanceOf(new EndOfStreamException(), java.io.IOException.class, Iterable.create(AwaitException.class)));
                    });

                    runner.test("with RuntimeException wrapped matching error", (Test test) ->
                    {
                        test.assertFalse(Exceptions.instanceOf(new RuntimeException(new EndOfStreamException()), EndOfStreamException.class, Iterable.create(AwaitException.class)));
                    });

                    runner.test("with RuntimeException twice wrapped matching error", (Test test) ->
                    {
                        test.assertFalse(Exceptions.instanceOf(new RuntimeException(new RuntimeException(new EndOfStreamException())), EndOfStreamException.class, Iterable.create(AwaitException.class)));
                    });

                    runner.test("with AwaitException wrapped matching error", (Test test) ->
                    {
                        test.assertTrue(Exceptions.instanceOf(new AwaitException(new EndOfStreamException()), EndOfStreamException.class, Iterable.create(AwaitException.class)));
                    });

                    runner.test("with AwaitException twice wrapped matching error", (Test test) ->
                    {
                        test.assertTrue(Exceptions.instanceOf(new AwaitException(new AwaitException(new EndOfStreamException())), EndOfStreamException.class, Iterable.create(AwaitException.class)));
                    });
                });

                runner.testGroup("with AwaitException and RuntimeException errorTypesToGoPast", () ->
                {
                    runner.test("with null error", (Test test) ->
                    {
                        test.assertFalse(Exceptions.instanceOf(null, NotFoundException.class, Iterable.create(AwaitException.class, RuntimeException.class)));
                    });

                    runner.test("with null type", (Test test) ->
                    {
                        test.assertThrows(() -> Exceptions.instanceOf(new EndOfStreamException(), null, Iterable.create(AwaitException.class, RuntimeException.class)),
                            new PreConditionFailure("type cannot be null."));
                    });

                    runner.test("with same type", (Test test) ->
                    {
                        test.assertTrue(Exceptions.instanceOf(new EndOfStreamException(), EndOfStreamException.class, Iterable.create(AwaitException.class, RuntimeException.class)));
                    });

                    runner.test("with derived type", (Test test) ->
                    {
                        test.assertTrue(Exceptions.instanceOf(new EndOfStreamException(), RuntimeException.class, Iterable.create(AwaitException.class, RuntimeException.class)));
                    });

                    runner.test("with parent type", (Test test) ->
                    {
                        test.assertFalse(Exceptions.instanceOf(new RuntimeException("blah"), EndOfStreamException.class, Iterable.create(AwaitException.class, RuntimeException.class)));
                    });

                    runner.test("with grandparent type", (Test test) ->
                    {
                        test.assertTrue(Exceptions.instanceOf(new EndOfStreamException(), Exception.class, Iterable.create(AwaitException.class, RuntimeException.class)));
                    });

                    runner.test("with great-grandparent type", (Test test) ->
                    {
                        test.assertTrue(Exceptions.instanceOf(new EndOfStreamException(), Throwable.class, Iterable.create(AwaitException.class, RuntimeException.class)));
                    });

                    runner.test("with unrelated type", (Test test) ->
                    {
                        test.assertFalse(Exceptions.instanceOf(new EndOfStreamException(), java.io.IOException.class, Iterable.create(AwaitException.class, RuntimeException.class)));
                    });

                    runner.test("with RuntimeException wrapped matching error", (Test test) ->
                    {
                        test.assertTrue(Exceptions.instanceOf(new RuntimeException(new EndOfStreamException()), EndOfStreamException.class, Iterable.create(AwaitException.class, RuntimeException.class)));
                    });

                    runner.test("with RuntimeException twice wrapped matching error", (Test test) ->
                    {
                        test.assertTrue(Exceptions.instanceOf(new RuntimeException(new RuntimeException(new EndOfStreamException())), EndOfStreamException.class, Iterable.create(AwaitException.class, RuntimeException.class)));
                    });

                    runner.test("with AwaitException wrapped matching error", (Test test) ->
                    {
                        test.assertTrue(Exceptions.instanceOf(new AwaitException(new EndOfStreamException()), EndOfStreamException.class, Iterable.create(AwaitException.class, RuntimeException.class)));
                    });

                    runner.test("with AwaitException twice wrapped matching error", (Test test) ->
                    {
                        test.assertTrue(Exceptions.instanceOf(new AwaitException(new AwaitException(new EndOfStreamException())), EndOfStreamException.class, Iterable.create(AwaitException.class, RuntimeException.class)));
                    });
                });
            });
        });
    }
}
