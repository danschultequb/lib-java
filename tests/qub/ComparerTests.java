package qub;

public class ComparerTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(Comparer.class, () ->
        {
            runner.test("constructor()", (Test test) ->
            {
                test.assertNotNull(new Comparer());
            });

            runner.testGroup("equal()", () ->
            {
                final Action3<Object,Object,Boolean> equalTest = (Object lhs, Object rhs, Boolean expected) ->
                {
                    runner.test("with " + lhs + " and " + rhs, (Test test) ->
                    {
                        test.assertEqual(expected, Comparer.equal(lhs, rhs));
                    });
                };

                equalTest.run(null, null, true);
                equalTest.run(null, false, false);
                equalTest.run(false, false, true);
                equalTest.run(false, true, false);
                equalTest.run(20, 20, true);
                equalTest.run(new char[0], new char[0], true);
            });

            runner.testGroup("minimum(Iterable<T>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> Comparer.minimum(null));
                });

                runner.test("with empty", (Test test) ->
                {
                    test.assertNull(Comparer.minimum(new Array<VersionNumber>(0)));
                });

                runner.test("with one value", (Test test) ->
                {
                    test.assertEqual(VersionNumber.parse("1"), Comparer.minimum(Array.fromValues(new VersionNumber[] { VersionNumber.parse("1") })));
                });

                runner.test("with two values", (Test test) ->
                {
                    test.assertEqual(VersionNumber.parse("1"), Comparer.minimum(Array.fromValues(new VersionNumber[] { VersionNumber.parse("1"), VersionNumber.parse("2") })));
                });

                runner.test("with three values", (Test test) ->
                {
                    test.assertEqual(VersionNumber.parse("0"), Comparer.minimum(Array.fromValues(new VersionNumber[] { VersionNumber.parse("1"), VersionNumber.parse("0"), VersionNumber.parse("2") })));
                });
            });

            runner.testGroup("minimum(Iterable<T>,Function2<T,T,Comparison>)", () ->
            {
                runner.test("with null values", (Test test) ->
                {
                    test.assertThrows(() -> Comparer.minimum((Iterable<Integer>)null, Integers::compare));
                });
            });

            runner.testGroup("maximum(T...)", () ->
            {
                runner.test("with no arguments", (Test test) ->
                {
                    test.assertNull(Comparer.maximum());
                });

                runner.test("with one argument", (Test test) ->
                {
                    test.assertEqual(Distance.zero, Comparer.maximum(Distance.zero));
                });

                runner.test("with multiple arguments", (Test test) ->
                {
                    test.assertEqual(Distance.inches(5), Comparer.maximum(Distance.inches(1), Distance.inches(5)));
                });
            });

            runner.testGroup("maximum(Iterable<T>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> Comparer.maximum((Iterable<VersionNumber>)null));
                });

                runner.test("with empty", (Test test) ->
                {
                    test.assertNull(Comparer.maximum(new Array<VersionNumber>(0)));
                });

                runner.test("with one value", (Test test) ->
                {
                    test.assertEqual(VersionNumber.parse("1"), Comparer.maximum(Array.fromValues(new VersionNumber[] { VersionNumber.parse("1") })));
                });

                runner.test("with two values", (Test test) ->
                {
                    test.assertEqual(VersionNumber.parse("2"), Comparer.maximum(Array.fromValues(new VersionNumber[] { VersionNumber.parse("1"), VersionNumber.parse("2") })));
                });

                runner.test("with three values", (Test test) ->
                {
                    test.assertEqual(VersionNumber.parse("2"), Comparer.maximum(Array.fromValues(new VersionNumber[] { VersionNumber.parse("1"), VersionNumber.parse("0"), VersionNumber.parse("2") })));
                });
            });

            runner.testGroup("maximum(Iterable<T>,Function2<T,T,Comparison>)", () ->
            {
                runner.test("with null values", (Test test) ->
                {
                    test.assertThrows(() -> Comparer.maximum((Iterable<Integer>)null, Integers::compare));
                });
            });
        });
    }
}
