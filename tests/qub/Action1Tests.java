package qub;

public class Action1Tests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(Action1.class, () ->
        {
            runner.testGroup("add(Class<T2>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final IntegerValue counter = IntegerValue.create(0);
                    final Action1<Integer> action1 = counter::plusAssign;
                    test.assertThrows(() -> action1.add(null),
                        new PreConditionFailure("parameterType cannot be null."));
                });

                runner.test("with non-null", (Test test) ->
                {
                    final IntegerValue counter = IntegerValue.create(2);
                    final Action1<Integer> action1 = counter::plusAssign;
                    final Action2<Integer,String> action2 = action1.add(String.class);
                    test.assertNotNull(action2);

                    action2.run(5, "abc");

                    test.assertEqual(7, counter.get());
                });
            });

            runner.testGroup("insert0(Class<T0>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final IntegerValue counter = IntegerValue.create(0);
                    final Action1<Integer> action1 = counter::plusAssign;
                    test.assertThrows(() -> action1.insert0(null),
                        new PreConditionFailure("parameterType cannot be null."));
                });

                runner.test("with non-null", (Test test) ->
                {
                    final IntegerValue counter = IntegerValue.create(1);
                    final Action1<Integer> action1 = counter::plusAssign;
                    final Action2<String,Integer> action2 = action1.insert0(String.class);
                    test.assertNotNull(action2);

                    action2.run("abc", 5);

                    test.assertEqual(6, counter.get());
                });
            });

            runner.test("bind1(T1)", (Test test) ->
            {
                final IntegerValue counter = IntegerValue.create(3);
                final Action1<Integer> action1 = counter::plusAssign;
                final Action0 action0 = action1.bind1(10);
                test.assertNotNull(action0);

                action0.run();
                test.assertEqual(13, counter.get());

                action0.run();
                test.assertEqual(23, counter.get());
            });

            runner.test("bindReturn(TReturn)", (Test test) ->
            {
                final IntegerValue counter = IntegerValue.create(3);
                final Action1<Integer> action1 = counter::plusAssign;
                final Function1<Integer,String> function1 = action1.bindReturn("hello");
                test.assertNotNull(function1);

                test.assertEqual("hello", function1.run(10));
                test.assertEqual(13, counter.get());

                test.assertEqual("hello", function1.run(4));
                test.assertEqual(17, counter.get());
            });

            runner.testGroup("sequence(Action1<T>...)", () ->
            {
                runner.test("with no arguments", (Test test) ->
                {
                    test.assertThrows(() -> Action1.sequence(),
                        new PreConditionFailure("actions cannot be empty."));
                });

                runner.test("with null argument array", (Test test) ->
                {
                    test.assertThrows(() -> Action1.sequence((Action1<String>[])null),
                        new PreConditionFailure("actions cannot be null."));
                });

                runner.test("with one null argument", (Test test) ->
                {
                    final Action1<String> action = Action1.sequence((Action1<String>)null);
                    test.assertNotNull(action);
                    action.run("hello");
                });

                runner.test("with one non-null argument", (Test test) ->
                {
                    final Value<String> value = Value.create();
                    final Action1<String> setValue = value::set;
                    final Action1<String> action = Action1.sequence(setValue);
                    test.assertSame(action, setValue);
                });

                runner.test("with null and null arguments", (Test test) ->
                {
                    final Action1<String> action = Action1.sequence(null, null);
                    test.assertNotNull(action);
                    action.run("hello");
                });

                runner.test("with null and non-null arguments", (Test test) ->
                {
                    final Value<String> value = Value.create();
                    final Action1<String> setValue = value::set;
                    final Action1<String> action = Action1.sequence(null, setValue);
                    test.assertSame(action, setValue);
                });

                runner.test("with non-null and null arguments", (Test test) ->
                {
                    final Value<String> value = Value.create();
                    final Action1<String> setValue = value::set;
                    final Action1<String> action = Action1.sequence(setValue, null);
                    test.assertSame(action, setValue);
                });

                runner.test("with non-null and non-null arguments", (Test test) ->
                {
                    final Value<String> value1 = Value.create();
                    final Value<String> value2 = Value.create();
                    final Action1<String> action = Action1.sequence(value1::set, value2::set);
                    test.assertNotNull(action);
                    action.run("oops");
                    test.assertEqual("oops", value1.get());
                    test.assertEqual("oops", value2.get());
                });
            });
        });
    }
}
