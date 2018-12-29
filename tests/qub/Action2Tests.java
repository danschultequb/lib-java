package qub;

public class Action2Tests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(Action2.class, () ->
        {
            runner.testGroup("sequence(Action2<T1,T2>...)", () ->
            {
                runner.test("with no arguments", (Test test) ->
                {
                    final Action2<String,Integer> action = Action2.sequence();
                    test.assertNotNull(action);
                    action.run("hello", 20);
                });

                runner.test("with null argument array", (Test test) ->
                {
                    final Action2<String,Integer> action = Action2.sequence((Action2<String,Integer>[])null);
                    test.assertNotNull(action);
                    action.run("hello", 20);
                });

                runner.test("with empty argument array", (Test test) ->
                {
                    final Action2<String,Integer> action = Action2.sequence((Action2<String,Integer>[])null);
                    test.assertNotNull(action);
                    action.run("hello", 20);
                });

                runner.test("with one null argument", (Test test) ->
                {
                    final Action2<String,Integer> action = Action2.sequence((Action2<String,Integer>)null);
                    test.assertNotNull(action);
                    action.run("hello", 20);
                });

                runner.test("with one non-null argument", (Test test) ->
                {
                    final Value<String> value = new Value<>();
                    final Action2<String,Integer> setValue = (String s, Integer i) -> value.set(Strings.repeat(s, i));
                    final Action2<String,Integer> action = Action2.sequence(setValue);
                    test.assertSame(action, setValue);
                });

                runner.test("with null and null arguments", (Test test) ->
                {
                    final Action2<String,Integer> action = Action2.sequence(null, null);
                    test.assertNotNull(action);
                    action.run("hello", 20);
                });

                runner.test("with null and non-null arguments", (Test test) ->
                {
                    final Value<String> value = new Value<>();
                    final Action2<String,Integer> setValue = (String s, Integer i) -> value.set(Strings.repeat(s, i));
                    final Action2<String,Integer> action = Action2.sequence(null, setValue);
                    test.assertSame(action, setValue);
                });

                runner.test("with non-null and null arguments", (Test test) ->
                {
                    final Value<String> value = new Value<>();
                    final Action2<String,Integer> setValue = (String s, Integer i) -> value.set(Strings.repeat(s, i));
                    final Action2<String,Integer> action = Action2.sequence(setValue, null);
                    test.assertSame(action, setValue);
                });

                runner.test("with non-null and non-null arguments", (Test test) ->
                {
                    final Value<String> value1 = new Value<>();
                    final Value<String> value2 = new Value<>();
                    final Action2<String,Integer> action = Action2.sequence(
                        (String s, Integer i) -> value1.set(Strings.repeat(s, i)),
                        (String s, Integer i) -> value2.set(Strings.repeat(s, i + 1)));
                    test.assertNotNull(action);
                    action.run("oops", 2);
                    test.assertEqual("oopsoops", value1.get());
                    test.assertEqual("oopsoopsoops", value2.get());
                });
            });
        });
    }
}
