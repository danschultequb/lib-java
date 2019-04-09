package qub;

public class StackTests
{
    public static void test(final TestRunner runner)
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

            runner.test("push()", (Test test) ->
            {
                final Stack<Double> stack = Stack.create();
                stack.push(5.6);
                test.assertTrue(stack.any());
                test.assertEqual(1, stack.getCount());
                test.assertEqual(5.6, stack.peek().await());
            });

            runner.testGroup("pop()", () ->
            {
                runner.test("when empty", (Test test) ->
                {
                    final Stack<Double> stack = Stack.create();
                    test.assertThrows(() -> stack.pop().await(),
                        new StackEmptyException());
                });

                runner.test("when not empty", (Test test) ->
                {
                    final Stack<Double> stack = Stack.create();
                    stack.push(1.2);
                    stack.push(3.4);
                    test.assertEqual(3.4, stack.pop().await());
                    test.assertEqual(1, stack.getCount());

                    test.assertEqual(1.2, stack.pop().await());
                    test.assertEqual(0, stack.getCount());

                    test.assertThrows(() -> stack.pop().await(),
                        new StackEmptyException());
                });
            });

            runner.testGroup("peek()", () ->
            {
                runner.test("when empty", (Test test) ->
                {
                    final Stack<Double> stack = Stack.create();
                    test.assertThrows(() -> stack.peek().await(),
                        new StackEmptyException());
                });

                runner.test("when not empty", (Test test) ->
                {
                    final Stack<Double> stack = Stack.create();

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
                    final Stack<Integer> stack = Stack.create();
                    test.assertFalse(stack.contains(null));
                });

                runner.test("with non-null when empty", (Test test) ->
                {
                    final Stack<Integer> stack = Stack.create();
                    test.assertFalse(stack.contains(5));
                });

                runner.test("with null when non-empty", (Test test) ->
                {
                    final Stack<Integer> stack = Stack.create(1, 2, 3, 4);
                    test.assertFalse(stack.contains(null));
                });

                runner.test("with non-existing when non-empty", (Test test) ->
                {
                    final Stack<Integer> stack = Stack.create(1, 2, 3, 4);
                    test.assertFalse(stack.contains(5));
                });

                runner.test("with existing when non-empty", (Test test) ->
                {
                    final Stack<Integer> stack = Stack.create(1, 2, 3, 4);
                    test.assertTrue(stack.contains(1));
                });
            });

            runner.testGroup("doesNotContain()", () ->
            {
                runner.test("with null when empty", (Test test) ->
                {
                    final Stack<Integer> stack = Stack.create();
                    test.assertTrue(stack.doesNotContain(null));
                });

                runner.test("with non-null when empty", (Test test) ->
                {
                    final Stack<Integer> stack = Stack.create();
                    test.assertTrue(stack.doesNotContain(5));
                });

                runner.test("with null when non-empty", (Test test) ->
                {
                    final Stack<Integer> stack = Stack.create(1, 2, 3, 4);
                    test.assertTrue(stack.doesNotContain(null));
                });

                runner.test("with non-existing when non-empty", (Test test) ->
                {
                    final Stack<Integer> stack = Stack.create(1, 2, 3, 4);
                    test.assertTrue(stack.doesNotContain(5));
                });

                runner.test("with existing when non-empty", (Test test) ->
                {
                    final Stack<Integer> stack = Stack.create(1, 2, 3, 4);
                    test.assertFalse(stack.doesNotContain(1));
                });
            });
        });
    }
}
