package qub;

import java.util.concurrent.atomic.AtomicInteger;

public class BasicTestRunnerTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(BasicTestRunner.class, () ->
        {
            runner.testGroup("constructor", () ->
            {
                runner.test("with null process", (Test test) ->
                {
                    test.assertThrows(() -> new BasicTestRunner(null, null),
                        new PreConditionFailure("process cannot be null."));
                });
            });

            runner.test("skip()", (Test test) ->
            {
                final BasicTestRunner btr = create(test);
                final Skip skip = btr.skip();
                test.assertNotNull(skip);
                test.assertSame(skip, btr.skip());
            });

            runner.testGroup("skip(boolean)", () ->
            {
                runner.test("with false", (Test test) ->
                {
                    final BasicTestRunner btr = create(test);
                    test.assertNull(btr.skip(false));
                });

                runner.test("with true", (Test test) ->
                {
                    final BasicTestRunner btr = create(test);
                    final Skip skip = btr.skip(true);
                    test.assertNotNull(skip);
                    test.assertSame(skip, btr.skip());
                    test.assertSame(skip, btr.skip(true));
                });
            });

            runner.testGroup("skip(boolean,String)", () ->
            {
                runner.test("with false and null", (Test test) ->
                {
                    final BasicTestRunner btr = create(test);
                    test.assertThrows(() -> btr.skip(false, null),
                        new PreConditionFailure("message cannot be null."));
                });

                runner.test("with false and empty", (Test test) ->
                {
                    final BasicTestRunner btr = create(test);
                    test.assertThrows(() -> btr.skip(false, ""),
                        new PreConditionFailure("message cannot be empty."));
                });

                runner.test("with false and non-empty", (Test test) ->
                {
                    final BasicTestRunner btr = create(test);
                    test.assertNull(btr.skip(false, "abc"));
                });

                runner.test("with true and null", (Test test) ->
                {
                    final BasicTestRunner btr = create(test);
                    test.assertThrows(() -> btr.skip(true, null),
                        new PreConditionFailure("message cannot be null."));
                });

                runner.test("with true and empty", (Test test) ->
                {
                    final BasicTestRunner btr = create(test);
                    test.assertThrows(() -> btr.skip(true, ""),
                        new PreConditionFailure("message cannot be empty."));
                });

                runner.test("with true and non-empty", (Test test) ->
                {
                    final BasicTestRunner btr = create(test);
                    final Skip skip = btr.skip(true, "abc");
                    test.assertNotNull(skip);
                    test.assertEqual("abc", skip.getMessage());
                });
            });

            runner.testGroup("skip(String)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final BasicTestRunner btr = create(test);
                    test.assertThrows(() -> btr.skip(null),
                        new PreConditionFailure("message cannot be null."));
                });

                runner.test("with empty", (Test test) ->
                {
                    final BasicTestRunner btr = create(test);
                    test.assertThrows(() -> btr.skip(""),
                        new PreConditionFailure("message cannot be empty."));
                });

                runner.test("with non-empty", (Test test) ->
                {
                    final BasicTestRunner btr = create(test);
                    final Skip skip = btr.skip("abc");
                    test.assertNotNull(skip);
                    test.assertEqual("abc", skip.getMessage());
                });
            });

            runner.testGroup("testGroup(Class<?>,Action0)", () ->
            {
                runner.test("with null class and null action", (Test test) ->
                {
                    final BasicTestRunner btr = create(test);
                    test.assertThrows(() -> btr.testGroup((Class<?>)null, null),
                        new PreConditionFailure("testClass cannot be null."));
                });

                runner.test("with null class and non-null action", (Test test) ->
                {
                    final BasicTestRunner btr = create(test);
                    test.assertThrows(() -> btr.testGroup((Class<?>)null, Action0.empty),
                        new PreConditionFailure("testClass cannot be null."));
                });

                runner.test("with non-null class and null action", (Test test) ->
                {
                    final BasicTestRunner btr = create(test);
                    test.assertThrows(() -> btr.testGroup(BasicTestRunnerTests.class, null),
                        new PreConditionFailure("testGroupAction cannot be null."));
                });

                runner.test("with no event actions", (Test test) ->
                {
                    final Value<Integer> value = Value.create(0);
                    final BasicTestRunner btr = create(test);
                    btr.testGroup(BasicTestRunnerTests.class, () -> value.set(value.get() + 1));
                    test.assertEqual(1, value.get());
                });

                runner.test("with event actions", (Test test) ->
                {
                    final BasicTestRunner btr = create(test);
                    final AtomicInteger counter = new AtomicInteger(0);
                    final Value<Integer> value = Value.create(0);
                    final MutableMap<Integer,TestGroup> beforeTestGroup = Map.create();
                    final MutableMap<Integer,TestGroup> afterTestGroupSkipped = Map.create();
                    final MutableMap<Integer,TestError> afterTestGroupError = Map.create();
                    final MutableMap<Integer,TestGroup> afterTestGroup = Map.create();
                    btr.beforeTestGroup((TestGroup tg) ->
                    {
                        beforeTestGroup.set(counter.incrementAndGet(), tg);
                    });
                    btr.afterTestGroupSkipped((TestGroup tg) ->
                    {
                        afterTestGroupSkipped.set(counter.incrementAndGet(), tg);
                    });
                    btr.afterTestGroupError((TestGroup tg, Throwable error) ->
                    {
                        afterTestGroupError.set(counter.incrementAndGet(), new TestError(tg.getFullName(), error));
                    });
                    btr.afterTestGroup((TestGroup tg) ->
                    {
                        afterTestGroup.set(counter.incrementAndGet(), tg);
                    });

                    btr.testGroup(BasicTestRunnerTests.class, () -> value.set(counter.incrementAndGet()));

                    test.assertEqual(
                        Iterable.create(MapEntry.create(1, new TestGroup("BasicTestRunnerTests", null, null))),
                        beforeTestGroup);
                    test.assertEqual(2, value.get());
                    test.assertEqual(
                        Iterable.create(),
                        afterTestGroupSkipped);
                    test.assertEqual(
                        Iterable.create(),
                        afterTestGroupError);
                    test.assertEqual(
                        Iterable.create(MapEntry.create(3, new TestGroup("BasicTestRunnerTests", null, null))),
                        afterTestGroup);
                });

                runner.test("with error during beforeTestGroup actions", (Test test) ->
                {
                    final BasicTestRunner btr = create(test);
                    final AtomicInteger counter = new AtomicInteger(0);
                    final Value<Integer> value = Value.create(0);
                    final MutableMap<Integer,TestGroup> beforeTestGroup = Map.create();
                    final MutableMap<Integer,TestGroup> afterTestGroupSkipped = Map.create();
                    final MutableMap<Integer,TestError> afterTestGroupError = Map.create();
                    final MutableMap<Integer,TestGroup> afterTestGroup = Map.create();
                    btr.beforeTestGroup((TestGroup tg) ->
                    {
                        throw new RuntimeException("abc");
                    });
                    btr.afterTestGroupSkipped((TestGroup tg) ->
                    {
                        afterTestGroupSkipped.set(counter.incrementAndGet(), tg);
                    });
                    btr.afterTestGroupError((TestGroup tg, Throwable error) ->
                    {
                        afterTestGroupError.set(counter.incrementAndGet(), new TestError(tg.getFullName(), error));
                    });
                    btr.afterTestGroup((TestGroup tg) ->
                    {
                        afterTestGroup.set(counter.incrementAndGet(), tg);
                    });

                    btr.testGroup(BasicTestRunnerTests.class, () -> value.set(counter.incrementAndGet()));

                    test.assertEqual(Iterable.create(), beforeTestGroup);
                    test.assertEqual(0, value.get());
                    test.assertEqual(Iterable.create(), afterTestGroupSkipped);
                    test.assertEqual(
                        Map.<Integer,TestError>create().set(1, new TestError("BasicTestRunnerTests", new RuntimeException("abc"))),
                        afterTestGroupError);
                    test.assertEqual(
                        Map.<Integer,TestGroup>create().set(2, new TestGroup("BasicTestRunnerTests", null, null)),
                        afterTestGroup);
                });
            });

            runner.testGroup("testGroup(String,Action0)", () ->
            {
                runner.test("with null and null action", (Test test) ->
                {
                    final BasicTestRunner btr = create(test);
                    test.assertThrows(() -> btr.testGroup((String)null, null),
                        new PreConditionFailure("testGroupName cannot be null."));
                });

                runner.test("with empty and non-null action", (Test test) ->
                {
                    final BasicTestRunner btr = create(test);
                    test.assertThrows(() -> btr.testGroup("", Action0.empty),
                        new PreConditionFailure("testGroupName cannot be empty."));
                });

                runner.test("with non-empty and null action", (Test test) ->
                {
                    final BasicTestRunner btr = create(test);
                    test.assertThrows(() -> btr.testGroup("abc", null),
                        new PreConditionFailure("testGroupAction cannot be null."));
                });

                runner.test("with no event actions", (Test test) ->
                {
                    final BasicTestRunner btr = create(test);
                    final Value<Integer> value = Value.create(0);
                    btr.testGroup("abc", () -> value.set(value.get() + 1));
                    test.assertEqual(1, value.get());
                });

                runner.test("with event actions", (Test test) ->
                {
                    final BasicTestRunner btr = create(test);
                    final AtomicInteger counter = new AtomicInteger(0);
                    final Value<Integer> value = Value.create(0);
                    final MutableMap<Integer,TestGroup> beforeTestGroup = Map.create();
                    btr.beforeTestGroup((TestGroup tg) ->
                    {
                        beforeTestGroup.set(counter.incrementAndGet(), tg);
                    });
                    final MutableMap<Integer,TestGroup> afterTestGroup = new ListMap<>();
                    btr.afterTestGroup((TestGroup tg) ->
                    {
                        afterTestGroup.set(counter.incrementAndGet(), tg);
                    });

                    btr.testGroup("abc", () -> value.set(counter.incrementAndGet()));

                    test.assertEqual(
                        Array.create(new MapEntry[] { MapEntry.create(1, new TestGroup("abc", null, null)) }),
                        beforeTestGroup);
                    test.assertEqual(2, value.get());
                    test.assertEqual(
                        Array.create(new MapEntry[] { MapEntry.create(3, new TestGroup("abc", null, null)) }),
                        afterTestGroup);
                });
            });
        });
    }

    private static BasicTestRunner create(Test test)
    {
        return new BasicTestRunner(test.getProcess(), null);
    }
}
