package qub;

public interface StackTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(Stack.class, () ->
        {
            runner.testGroup("create(T...)", () ->
            {
                runner.test("with null array", (Test test) ->
                {
                    test.assertThrows(() -> Stack.create((String[])null),
                        new PreConditionFailure("values cannot be null."));
                });

                runner.test("with no arguments", (Test test) ->
                {
                    final Stack<String> stack = Stack.create();
                    test.assertEqual(0, stack.getCount());
                    test.assertEqual("[]", stack.toString());
                });

                runner.test("with one argument", (Test test) ->
                {
                    final Stack<String> stack = Stack.create("hello");
                    test.assertEqual(1, stack.getCount());
                    test.assertEqual("hello", stack.peek().await());
                    test.assertEqual("[hello]", stack.toString());
                });

                runner.test("with two arguments", (Test test) ->
                {
                    final Stack<String> stack = Stack.create("hello", "there");
                    test.assertEqual(2, stack.getCount());
                    test.assertEqual("there", stack.peek().await());
                    test.assertEqual("[hello,there]", stack.toString());
                });
            });

            runner.testGroup("create(Iterable<T>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> Stack.create((Iterable<String>)null),
                        new PreConditionFailure("values cannot be null."));
                });

                runner.test("with no values", (Test test) ->
                {
                    final Stack<String> stack = Stack.create(Iterable.create());
                    test.assertEqual(0, stack.getCount());
                    test.assertEqual("[]", stack.toString());
                });

                runner.test("with one value", (Test test) ->
                {
                    final Stack<String> stack = Stack.create(Iterable.create("hello"));
                    test.assertEqual(1, stack.getCount());
                    test.assertEqual("hello", stack.peek().await());
                    test.assertEqual("[hello]", stack.toString());
                });

                runner.test("with two values", (Test test) ->
                {
                    final Stack<String> stack = Stack.create(Iterable.create("hello", "there"));
                    test.assertEqual(2, stack.getCount());
                    test.assertEqual("there", stack.peek().await());
                    test.assertEqual("[hello,there]", stack.toString());
                });
            });

            StackTests.test(runner, Stack::create);
        });
    }

    public static void test(TestRunner runner, Function0<? extends Stack<Double>> creator)
    {
        runner.testGroup(Stack.class, () ->
        {
            runner.test("push(T)", (Test test) ->
            {
                final Stack<Double> stack = creator.run();
                final Stack<Double> pushResult = stack.push(5.6);
                test.assertSame(stack, pushResult);
                test.assertTrue(stack.any());
                test.assertEqual(1, stack.getCount());
                test.assertEqual(5.6, stack.peek().await());
            });

            runner.testGroup("pop()", () ->
            {
                runner.test("when empty", (Test test) ->
                {
                    final Stack<Double> stack = creator.run();
                    test.assertThrows(() -> stack.pop().await(),
                        new EmptyException());
                });

                runner.test("when not empty", (Test test) ->
                {
                    final Stack<Double> stack = creator.run();
                    stack.push(1.2).push(3.4);
                    test.assertEqual(3.4, stack.pop().await());
                    test.assertEqual(1, stack.getCount());

                    test.assertEqual(1.2, stack.pop().await());
                    test.assertEqual(0, stack.getCount());

                    test.assertThrows(() -> stack.pop().await(),
                        new EmptyException());
                });
            });

            runner.testGroup("peek()", () ->
            {
                runner.test("when empty", (Test test) ->
                {
                    final Stack<Double> stack = creator.run();
                    test.assertThrows(() -> stack.peek().await(),
                        new EmptyException());
                });

                runner.test("when not empty", (Test test) ->
                {
                    final Stack<Double> stack = creator.run();
                    stack.push(1.2);
                    for (int i = 0; i < 3; ++i)
                    {
                        test.assertEqual(1.2, stack.peek().await());
                    }

                    stack.push(3.4);
                    for (int i = 0; i < 3; ++i)
                    {
                        test.assertEqual(3.4, stack.peek().await());
                    }

                    stack.pop().await();
                    for (int i = 0; i < 3; ++i)
                    {
                        test.assertEqual(1.2, stack.peek().await());
                    }
                });
            });

            runner.testGroup("contains()", () ->
            {
                runner.test("with null when empty", (Test test) ->
                {
                    final Stack<Double> stack = creator.run();
                    test.assertFalse(stack.contains(null));
                });

                runner.test("with non-null when empty", (Test test) ->
                {
                    final Stack<Double> stack = creator.run();
                    test.assertFalse(stack.contains(5.0));
                });

                runner.test("with null when non-empty", (Test test) ->
                {
                    final Stack<Double> stack = creator.run();
                    stack.pushAll(Iterable.create(1.0, 2.0, 3.0, 4.0));
                    test.assertFalse(stack.contains(null));
                });

                runner.test("with non-existing when non-empty", (Test test) ->
                {
                    final Stack<Double> stack = creator.run();
                    stack.pushAll(Iterable.create(1.0, 2.0, 3.0, 4.0));
                    test.assertFalse(stack.contains(5.0));
                });

                runner.test("with existing when non-empty", (Test test) ->
                {
                    final Stack<Double> stack = creator.run();
                    stack.pushAll(Iterable.create(1.0, 2.0, 3.0, 4.0));
                    test.assertTrue(stack.contains(1.0));
                });
            });
        });
    }
}
