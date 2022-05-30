package qub;

public interface ListStackTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(ListStack.class, () ->
        {
            runner.testGroup("create(T...)", () ->
            {
                runner.test("with null array", (Test test) ->
                {
                    test.assertThrows(() -> ListStack.create((String[])null),
                        new PreConditionFailure("values cannot be null."));
                });

                runner.test("with no arguments", (Test test) ->
                {
                    final ListStack<String> stack = ListStack.create();
                    test.assertEqual(0, stack.getCount());
                    test.assertEqual("[]", stack.toString());
                });

                runner.test("with one argument", (Test test) ->
                {
                    final ListStack<String> stack = ListStack.create("hello");
                    test.assertEqual(1, stack.getCount());
                    test.assertEqual("hello", stack.peek().await());
                    test.assertEqual("[hello]", stack.toString());
                });

                runner.test("with two arguments", (Test test) ->
                {
                    final ListStack<String> stack = ListStack.create("hello", "there");
                    test.assertEqual(2, stack.getCount());
                    test.assertEqual("there", stack.peek().await());
                    test.assertEqual("[hello,there]", stack.toString());
                });
            });

            runner.testGroup("create(Iterable<T>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> ListStack.create((Iterable<String>)null),
                        new PreConditionFailure("values cannot be null."));
                });

                runner.test("with no values", (Test test) ->
                {
                    final ListStack<String> stack = ListStack.create(Iterable.create());
                    test.assertEqual(0, stack.getCount());
                    test.assertEqual("[]", stack.toString());
                });

                runner.test("with one value", (Test test) ->
                {
                    final ListStack<String> stack = ListStack.create(Iterable.create("hello"));
                    test.assertEqual(1, stack.getCount());
                    test.assertEqual("hello", stack.peek().await());
                    test.assertEqual("[hello]", stack.toString());
                });

                runner.test("with two values", (Test test) ->
                {
                    final ListStack<String> stack = ListStack.create(Iterable.create("hello", "there"));
                    test.assertEqual(2, stack.getCount());
                    test.assertEqual("there", stack.peek().await());
                    test.assertEqual("[hello,there]", stack.toString());
                });
            });

            StackTests.test(runner, ListStack::create);
        });
    }
}
