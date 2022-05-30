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
                    test.assertFalse(Exceptions.instanceOf(new EmptyException(), null));
                });

                runner.test("with same type", (Test test) ->
                {
                    test.assertTrue(Exceptions.instanceOf(new EmptyException(), EmptyException.class));
                });

                runner.test("with derived type", (Test test) ->
                {
                    test.assertTrue(Exceptions.instanceOf(new EmptyException(), RuntimeException.class));
                });

                runner.test("with parent type", (Test test) ->
                {
                    test.assertFalse(Exceptions.instanceOf(new RuntimeException("blah"), EmptyException.class));
                });

                runner.test("with grandparent type", (Test test) ->
                {
                    test.assertTrue(Exceptions.instanceOf(new EmptyException(), Exception.class));
                });

                runner.test("with great-grandparent type", (Test test) ->
                {
                    test.assertTrue(Exceptions.instanceOf(new EmptyException(), Throwable.class));
                });

                runner.test("with unrelated type", (Test test) ->
                {
                    test.assertFalse(Exceptions.instanceOf(new EmptyException(), java.io.IOException.class));
                });

                runner.test("with RuntimeException wrapped matching error", (Test test) ->
                {
                    test.assertTrue(Exceptions.instanceOf(new RuntimeException(new EmptyException()), EmptyException.class));
                });

                runner.test("with RuntimeException twice wrapped matching error", (Test test) ->
                {
                    test.assertTrue(Exceptions.instanceOf(new RuntimeException(new RuntimeException(new EmptyException())), EmptyException.class));
                });

                runner.test("with AwaitException wrapped matching error", (Test test) ->
                {
                    test.assertTrue(Exceptions.instanceOf(new AwaitException(new EmptyException()), EmptyException.class));
                });

                runner.test("with AwaitException twice wrapped matching error", (Test test) ->
                {
                    test.assertTrue(Exceptions.instanceOf(new AwaitException(new AwaitException(new EmptyException())), EmptyException.class));
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
                        test.assertFalse(Exceptions.instanceOf(new EmptyException(), null, null));
                    });

                    runner.test("with same type", (Test test) ->
                    {
                        test.assertTrue(Exceptions.instanceOf(new EmptyException(), EmptyException.class, null));
                    });

                    runner.test("with derived type", (Test test) ->
                    {
                        test.assertTrue(Exceptions.instanceOf(new EmptyException(), RuntimeException.class, null));
                    });

                    runner.test("with parent type", (Test test) ->
                    {
                        test.assertFalse(Exceptions.instanceOf(new RuntimeException("blah"), EmptyException.class, null));
                    });

                    runner.test("with grandparent type", (Test test) ->
                    {
                        test.assertTrue(Exceptions.instanceOf(new EmptyException(), Exception.class, null));
                    });

                    runner.test("with great-grandparent type", (Test test) ->
                    {
                        test.assertTrue(Exceptions.instanceOf(new EmptyException(), Throwable.class, null));
                    });

                    runner.test("with unrelated type", (Test test) ->
                    {
                        test.assertFalse(Exceptions.instanceOf(new EmptyException(), java.io.IOException.class, null));
                    });

                    runner.test("with RuntimeException wrapped matching error", (Test test) ->
                    {
                        test.assertFalse(Exceptions.instanceOf(new RuntimeException(new EmptyException()), EmptyException.class, null));
                    });

                    runner.test("with RuntimeException twice wrapped matching error", (Test test) ->
                    {
                        test.assertFalse(Exceptions.instanceOf(new RuntimeException(new RuntimeException(new EmptyException())), EmptyException.class, null));
                    });

                    runner.test("with AwaitException wrapped matching error", (Test test) ->
                    {
                        test.assertFalse(Exceptions.instanceOf(new AwaitException(new EmptyException()), EmptyException.class, null));
                    });

                    runner.test("with AwaitException twice wrapped matching error", (Test test) ->
                    {
                        test.assertFalse(Exceptions.instanceOf(new AwaitException(new AwaitException(new EmptyException())), EmptyException.class, null));
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
                        test.assertFalse(Exceptions.instanceOf(new EmptyException(), null, Iterable.create()));
                    });

                    runner.test("with same type", (Test test) ->
                    {
                        test.assertTrue(Exceptions.instanceOf(new EmptyException(), EmptyException.class, Iterable.create()));
                    });

                    runner.test("with derived type", (Test test) ->
                    {
                        test.assertTrue(Exceptions.instanceOf(new EmptyException(), RuntimeException.class, Iterable.create()));
                    });

                    runner.test("with parent type", (Test test) ->
                    {
                        test.assertFalse(Exceptions.instanceOf(new RuntimeException("blah"), EmptyException.class, Iterable.create()));
                    });

                    runner.test("with grandparent type", (Test test) ->
                    {
                        test.assertTrue(Exceptions.instanceOf(new EmptyException(), Exception.class, Iterable.create()));
                    });

                    runner.test("with great-grandparent type", (Test test) ->
                    {
                        test.assertTrue(Exceptions.instanceOf(new EmptyException(), Throwable.class, Iterable.create()));
                    });

                    runner.test("with unrelated type", (Test test) ->
                    {
                        test.assertFalse(Exceptions.instanceOf(new EmptyException(), java.io.IOException.class, Iterable.create()));
                    });

                    runner.test("with RuntimeException wrapped matching error", (Test test) ->
                    {
                        test.assertFalse(Exceptions.instanceOf(new RuntimeException(new EmptyException()), EmptyException.class, Iterable.create()));
                    });

                    runner.test("with RuntimeException twice wrapped matching error", (Test test) ->
                    {
                        test.assertFalse(Exceptions.instanceOf(new RuntimeException(new RuntimeException(new EmptyException())), EmptyException.class, Iterable.create()));
                    });

                    runner.test("with AwaitException wrapped matching error", (Test test) ->
                    {
                        test.assertFalse(Exceptions.instanceOf(new AwaitException(new EmptyException()), EmptyException.class, Iterable.create()));
                    });

                    runner.test("with AwaitException twice wrapped matching error", (Test test) ->
                    {
                        test.assertFalse(Exceptions.instanceOf(new AwaitException(new AwaitException(new EmptyException())), EmptyException.class, Iterable.create()));
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
                        test.assertFalse(Exceptions.instanceOf(new EmptyException(), null, Iterable.create(RuntimeException.class)));
                    });

                    runner.test("with same type", (Test test) ->
                    {
                        test.assertTrue(Exceptions.instanceOf(new EmptyException(), EmptyException.class, Iterable.create(RuntimeException.class)));
                    });

                    runner.test("with derived type", (Test test) ->
                    {
                        test.assertTrue(Exceptions.instanceOf(new EmptyException(), RuntimeException.class, Iterable.create(RuntimeException.class)));
                    });

                    runner.test("with parent type", (Test test) ->
                    {
                        test.assertFalse(Exceptions.instanceOf(new RuntimeException("blah"), EmptyException.class, Iterable.create(RuntimeException.class)));
                    });

                    runner.test("with grandparent type", (Test test) ->
                    {
                        test.assertTrue(Exceptions.instanceOf(new EmptyException(), Exception.class, Iterable.create(RuntimeException.class)));
                    });

                    runner.test("with great-grandparent type", (Test test) ->
                    {
                        test.assertTrue(Exceptions.instanceOf(new EmptyException(), Throwable.class, Iterable.create(RuntimeException.class)));
                    });

                    runner.test("with unrelated type", (Test test) ->
                    {
                        test.assertFalse(Exceptions.instanceOf(new EmptyException(), java.io.IOException.class, Iterable.create(RuntimeException.class)));
                    });

                    runner.test("with RuntimeException wrapped matching error", (Test test) ->
                    {
                        test.assertTrue(Exceptions.instanceOf(new RuntimeException(new EmptyException()), EmptyException.class, Iterable.create(RuntimeException.class)));
                    });

                    runner.test("with RuntimeException twice wrapped matching error", (Test test) ->
                    {
                        test.assertTrue(Exceptions.instanceOf(new RuntimeException(new RuntimeException(new EmptyException())), EmptyException.class, Iterable.create(RuntimeException.class)));
                    });

                    runner.test("with AwaitException wrapped matching error", (Test test) ->
                    {
                        test.assertFalse(Exceptions.instanceOf(new AwaitException(new EmptyException()), EmptyException.class, Iterable.create(RuntimeException.class)));
                    });

                    runner.test("with AwaitException twice wrapped matching error", (Test test) ->
                    {
                        test.assertFalse(Exceptions.instanceOf(new AwaitException(new AwaitException(new EmptyException())), EmptyException.class, Iterable.create(RuntimeException.class)));
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
                        test.assertFalse(Exceptions.instanceOf(new EmptyException(), null, Iterable.create(AwaitException.class)));
                    });

                    runner.test("with same type", (Test test) ->
                    {
                        test.assertTrue(Exceptions.instanceOf(new EmptyException(), EmptyException.class, Iterable.create(AwaitException.class)));
                    });

                    runner.test("with derived type", (Test test) ->
                    {
                        test.assertTrue(Exceptions.instanceOf(new EmptyException(), RuntimeException.class, Iterable.create(AwaitException.class)));
                    });

                    runner.test("with parent type", (Test test) ->
                    {
                        test.assertFalse(Exceptions.instanceOf(new RuntimeException("blah"), EmptyException.class, Iterable.create(AwaitException.class)));
                    });

                    runner.test("with grandparent type", (Test test) ->
                    {
                        test.assertTrue(Exceptions.instanceOf(new EmptyException(), Exception.class, Iterable.create(AwaitException.class)));
                    });

                    runner.test("with great-grandparent type", (Test test) ->
                    {
                        test.assertTrue(Exceptions.instanceOf(new EmptyException(), Throwable.class, Iterable.create(AwaitException.class)));
                    });

                    runner.test("with unrelated type", (Test test) ->
                    {
                        test.assertFalse(Exceptions.instanceOf(new EmptyException(), java.io.IOException.class, Iterable.create(AwaitException.class)));
                    });

                    runner.test("with RuntimeException wrapped matching error", (Test test) ->
                    {
                        test.assertFalse(Exceptions.instanceOf(new RuntimeException(new EmptyException()), EmptyException.class, Iterable.create(AwaitException.class)));
                    });

                    runner.test("with RuntimeException twice wrapped matching error", (Test test) ->
                    {
                        test.assertFalse(Exceptions.instanceOf(new RuntimeException(new RuntimeException(new EmptyException())), EmptyException.class, Iterable.create(AwaitException.class)));
                    });

                    runner.test("with AwaitException wrapped matching error", (Test test) ->
                    {
                        test.assertTrue(Exceptions.instanceOf(new AwaitException(new EmptyException()), EmptyException.class, Iterable.create(AwaitException.class)));
                    });

                    runner.test("with AwaitException twice wrapped matching error", (Test test) ->
                    {
                        test.assertTrue(Exceptions.instanceOf(new AwaitException(new AwaitException(new EmptyException())), EmptyException.class, Iterable.create(AwaitException.class)));
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
                        test.assertFalse(Exceptions.instanceOf(new EmptyException(), null, Iterable.create(AwaitException.class, RuntimeException.class)));
                    });

                    runner.test("with same type", (Test test) ->
                    {
                        test.assertTrue(Exceptions.instanceOf(new EmptyException(), EmptyException.class, Iterable.create(AwaitException.class, RuntimeException.class)));
                    });

                    runner.test("with derived type", (Test test) ->
                    {
                        test.assertTrue(Exceptions.instanceOf(new EmptyException(), RuntimeException.class, Iterable.create(AwaitException.class, RuntimeException.class)));
                    });

                    runner.test("with parent type", (Test test) ->
                    {
                        test.assertFalse(Exceptions.instanceOf(new RuntimeException("blah"), EmptyException.class, Iterable.create(AwaitException.class, RuntimeException.class)));
                    });

                    runner.test("with grandparent type", (Test test) ->
                    {
                        test.assertTrue(Exceptions.instanceOf(new EmptyException(), Exception.class, Iterable.create(AwaitException.class, RuntimeException.class)));
                    });

                    runner.test("with great-grandparent type", (Test test) ->
                    {
                        test.assertTrue(Exceptions.instanceOf(new EmptyException(), Throwable.class, Iterable.create(AwaitException.class, RuntimeException.class)));
                    });

                    runner.test("with unrelated type", (Test test) ->
                    {
                        test.assertFalse(Exceptions.instanceOf(new EmptyException(), java.io.IOException.class, Iterable.create(AwaitException.class, RuntimeException.class)));
                    });

                    runner.test("with RuntimeException wrapped matching error", (Test test) ->
                    {
                        test.assertTrue(Exceptions.instanceOf(new RuntimeException(new EmptyException()), EmptyException.class, Iterable.create(AwaitException.class, RuntimeException.class)));
                    });

                    runner.test("with RuntimeException twice wrapped matching error", (Test test) ->
                    {
                        test.assertTrue(Exceptions.instanceOf(new RuntimeException(new RuntimeException(new EmptyException())), EmptyException.class, Iterable.create(AwaitException.class, RuntimeException.class)));
                    });

                    runner.test("with AwaitException wrapped matching error", (Test test) ->
                    {
                        test.assertTrue(Exceptions.instanceOf(new AwaitException(new EmptyException()), EmptyException.class, Iterable.create(AwaitException.class, RuntimeException.class)));
                    });

                    runner.test("with AwaitException twice wrapped matching error", (Test test) ->
                    {
                        test.assertTrue(Exceptions.instanceOf(new AwaitException(new AwaitException(new EmptyException())), EmptyException.class, Iterable.create(AwaitException.class, RuntimeException.class)));
                    });
                });
            });

            runner.testGroup("unwrap()", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> Exceptions.unwrap(null),
                        new PreConditionFailure("error cannot be null."));
                });

                runner.test("with RuntimeException with null cause", (Test test) ->
                {
                    final RuntimeException error = new RuntimeException("spam");
                    test.assertSame(error, Exceptions.unwrap(error));
                });

                runner.test("with RuntimeException with non-null cause", (Test test) ->
                {
                    final Exception cause = new Exception("hello");
                    final RuntimeException error = new RuntimeException(cause);
                    test.assertSame(cause, Exceptions.unwrap(error));
                });

                runner.test("with AwaitException with null cause", (Test test) ->
                {
                    final AwaitException error = new AwaitException(null);
                    test.assertSame(error, Exceptions.unwrap(error));
                });

                runner.test("with AwaitException with non-null cause", (Test test) ->
                {
                    final Exception cause = new Exception("hello");
                    final AwaitException error = new AwaitException(cause);
                    test.assertSame(cause, Exceptions.unwrap(error));
                });

                runner.test("with Exception with null cause", (Test test) ->
                {
                    final Exception error = new Exception("spam");
                    test.assertSame(error, Exceptions.unwrap(error));
                });
            });
        });
    }
}
