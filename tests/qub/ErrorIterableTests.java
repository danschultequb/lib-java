package qub;

public interface ErrorIterableTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(ErrorIterable.class, () ->
        {
            runner.testGroup("create(Throwable...)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> ErrorIterable.create((Throwable[])null),
                        new PreConditionFailure("errors cannot be null."));
                });

                runner.test("with no arguments", (Test test) ->
                {
                    final RuntimeException e = ErrorIterable.create();
                    test.assertNotNull(e);
                    test.assertEqual(ErrorIterable.class, e.getClass());
                    test.assertEqual(0, ((ErrorIterable)e).getCount());
                });

                runner.test("with one argument", (Test test) ->
                {
                    final RuntimeException e = new RuntimeException("hello");
                    final RuntimeException result = ErrorIterable.create(e);
                    test.assertSame(e, result);
                });

                runner.test("with two arguments", (Test test) ->
                {
                    final RuntimeException e1 = new RuntimeException("1");
                    final RuntimeException e2 = new RuntimeException("2");
                    final RuntimeException result = ErrorIterable.create(e1, e2);
                    test.assertNotNull(result);
                    test.assertEqual(ErrorIterable.class, result.getClass());
                    test.assertEqual(Iterable.create(e1, e2), result);
                    test.assertEqual("[java.lang.RuntimeException: 1,java.lang.RuntimeException: 2]", result.toString());
                });

                runner.test("with three arguments", (Test test) ->
                {
                    final RuntimeException e1 = new RuntimeException("1");
                    final RuntimeException e2 = new RuntimeException("2");
                    final RuntimeException e3 = new RuntimeException("3");
                    final RuntimeException result = ErrorIterable.create(e1, e2, e3);
                    test.assertNotNull(result);
                    test.assertEqual(ErrorIterable.class, result.getClass());
                    test.assertEqual(Iterable.create(e1, e2, e3), result);
                    test.assertEqual("[java.lang.RuntimeException: 1,java.lang.RuntimeException: 2,java.lang.RuntimeException: 3]", result.toString());
                });
            });

            runner.testGroup("create(Iterable<Throwable>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> ErrorIterable.create((Iterable<Throwable>)null),
                        new PreConditionFailure("errors cannot be null."));
                });

                runner.test("with empty", (Test test) ->
                {
                    final RuntimeException e = ErrorIterable.create(Iterable.create());
                    test.assertNotNull(e);
                    test.assertEqual(ErrorIterable.class, e.getClass());
                    test.assertEqual(0, ((ErrorIterable)e).getCount());
                });

                runner.test("with one argument", (Test test) ->
                {
                    final RuntimeException e = new RuntimeException("hello");
                    final RuntimeException result = ErrorIterable.create(Iterable.create(e));
                    test.assertSame(e, result);
                });

                runner.test("with two arguments", (Test test) ->
                {
                    final RuntimeException e1 = new RuntimeException("1");
                    final RuntimeException e2 = new RuntimeException("2");
                    final RuntimeException result = ErrorIterable.create(Iterable.create(e1, e2));
                    test.assertNotNull(result);
                    test.assertInstanceOf(result, ErrorIterable.class);
                    test.assertEqual(Iterable.create(e1, e2), result);
                    test.assertEqual("[java.lang.RuntimeException: 1,java.lang.RuntimeException: 2]", result.toString());
                });

                runner.test("with three arguments", (Test test) ->
                {
                    final RuntimeException e1 = new RuntimeException("1");
                    final RuntimeException e2 = new RuntimeException("2");
                    final RuntimeException e3 = new RuntimeException("3");
                    final RuntimeException result = ErrorIterable.create(Iterable.create(e1, e2, e3));
                    test.assertNotNull(result);
                    test.assertInstanceOf(result, ErrorIterable.class);
                    test.assertEqual(Iterable.create(e1, e2, e3), result);
                    test.assertEqual("[java.lang.RuntimeException: 1,java.lang.RuntimeException: 2,java.lang.RuntimeException: 3]", result.toString());
                });
            });
        });
    }
}
