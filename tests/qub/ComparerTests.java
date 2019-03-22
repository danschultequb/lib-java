package qub;

public class ComparerTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(Comparer.class, () ->
        {
            runner.testGroup("compare(int,int)", () ->
            {
                final Action3<Integer,Integer,Comparison> compareTest = (Integer lhs, Integer rhs, Comparison expected) ->
                {
                    runner.test("with " + lhs + " and " + rhs, (Test test) ->
                    {
                        test.assertEqual(expected, Comparer.compare(lhs.intValue(), rhs.intValue()));
                    });
                };

                compareTest.run(5, 7, Comparison.LessThan);
                compareTest.run(4, 4, Comparison.Equal);
                compareTest.run(10, 1, Comparison.GreaterThan);
            });

            runner.testGroup("compare(int,Integer)", () ->
            {
                final Action3<Integer,Integer,Comparison> compareTest = (Integer lhs, Integer rhs, Comparison expected) ->
                {
                    runner.test("with " + lhs + " and " + rhs, (Test test) ->
                    {
                        test.assertEqual(expected, Comparer.compare(lhs.intValue(), rhs));
                    });
                };

                compareTest.run(20, null, Comparison.GreaterThan);
                compareTest.run(5, 7, Comparison.LessThan);
                compareTest.run(4, 4, Comparison.Equal);
                compareTest.run(10, 1, Comparison.GreaterThan);
            });

            runner.testGroup("compare(Integer,int)", () ->
            {
                final Action3<Integer,Integer,Comparison> compareTest = (Integer lhs, Integer rhs, Comparison expected) ->
                {
                    runner.test("with " + lhs + " and " + rhs, (Test test) ->
                    {
                        test.assertEqual(expected, Comparer.compare(lhs, rhs.intValue()));
                    });
                };

                compareTest.run(null, -5, Comparison.LessThan);
                compareTest.run(5, 7, Comparison.LessThan);
                compareTest.run(4, 4, Comparison.Equal);
                compareTest.run(10, 1, Comparison.GreaterThan);
            });

            runner.testGroup("compare(Integer,Integer)", () ->
            {
                final Action3<Integer,Integer,Comparison> compareTest = (Integer lhs, Integer rhs, Comparison expected) ->
                {
                    runner.test("with " + lhs + " and " + rhs, (Test test) ->
                    {
                        test.assertEqual(expected, Comparer.compare(lhs, rhs));
                    });
                };

                compareTest.run(null, null, Comparison.Equal);
                compareTest.run(null, -5, Comparison.LessThan);
                compareTest.run(0, null, Comparison.GreaterThan);
                compareTest.run(5, 7, Comparison.LessThan);
                compareTest.run(4, 4, Comparison.Equal);
                compareTest.run(10, 1, Comparison.GreaterThan);
            });

            runner.testGroup("compare(T,T)", () ->
            {
                final Action3<Distance,Distance,Comparison> compareTest = (Distance lhs, Distance rhs, Comparison expected) ->
                {
                    runner.test("with " + lhs + " and " + rhs, (Test test) ->
                    {
                        test.assertEqual(expected, Comparer.compare(lhs, rhs));
                    });
                };

                compareTest.run(null, null, Comparison.Equal);
                compareTest.run(null, Distance.inches(-5), Comparison.LessThan);
                compareTest.run(Distance.zero, null, Comparison.GreaterThan);
                compareTest.run(Distance.feet(5), Distance.feet(7), Comparison.LessThan);
                compareTest.run(Distance.feet(4), Distance.feet(4), Comparison.Equal);
                compareTest.run(Distance.miles(10), Distance.miles(1), Comparison.GreaterThan);
            });

            runner.testGroup("lessThan(int,int)", () ->
            {
                final Action3<Integer,Integer,Boolean> lessThanTest = (Integer lhs, Integer rhs, Boolean expected) ->
                {
                    runner.test("with " + lhs + " and " + rhs, (Test test) ->
                    {
                        test.assertEqual(expected, Comparer.lessThan(lhs.intValue(), rhs.intValue()));
                    });
                };

                lessThanTest.run(5, 7, true);
                lessThanTest.run(4, 4, false);
                lessThanTest.run(10, 1, false);
            });

            runner.testGroup("lessThan(int,Integer)", () ->
            {
                final Action3<Integer,Integer,Boolean> lessThanTest = (Integer lhs, Integer rhs, Boolean expected) ->
                {
                    runner.test("with " + lhs + " and " + rhs, (Test test) ->
                    {
                        test.assertEqual(expected, Comparer.lessThan(lhs.intValue(), rhs));
                    });
                };

                lessThanTest.run(20, null, false);
                lessThanTest.run(5, 7, true);
                lessThanTest.run(4, 4, false);
                lessThanTest.run(10, 1, false);
            });

            runner.testGroup("lessThan(Integer,int)", () ->
            {
                final Action3<Integer,Integer,Boolean> lessThanTest = (Integer lhs, Integer rhs, Boolean expected) ->
                {
                    runner.test("with " + lhs + " and " + rhs, (Test test) ->
                    {
                        test.assertEqual(expected, Comparer.lessThan(lhs, rhs.intValue()));
                    });
                };

                lessThanTest.run(null, -5, true);
                lessThanTest.run(5, 7, true);
                lessThanTest.run(4, 4, false);
                lessThanTest.run(10, 1, false);
            });

            runner.testGroup("lessThan(Integer,Integer)", () ->
            {
                final Action3<Integer,Integer,Boolean> lessThanTest = (Integer lhs, Integer rhs, Boolean expected) ->
                {
                    runner.test("with " + lhs + " and " + rhs, (Test test) ->
                    {
                        test.assertEqual(expected, Comparer.lessThan(lhs, rhs));
                    });
                };

                lessThanTest.run(null, null, false);
                lessThanTest.run(null, -5, true);
                lessThanTest.run(0, null, false);
                lessThanTest.run(5, 7, true);
                lessThanTest.run(4, 4, false);
                lessThanTest.run(10, 1, false);
            });

            runner.testGroup("lessThanOrEqualTo(int,int)", () ->
            {
                final Action3<Integer,Integer,Boolean> lessThanOrEqualToTest = (Integer lhs, Integer rhs, Boolean expected) ->
                {
                    runner.test("with " + lhs + " and " + rhs, (Test test) ->
                    {
                        test.assertEqual(expected, Comparer.lessThanOrEqualTo(lhs.intValue(), rhs.intValue()));
                    });
                };

                lessThanOrEqualToTest.run(5, 7, true);
                lessThanOrEqualToTest.run(4, 4, true);
                lessThanOrEqualToTest.run(10, 1, false);
            });

            runner.testGroup("lessThanOrEqualTo(int,Integer)", () ->
            {
                final Action3<Integer,Integer,Boolean> lessThanOrEqualToTest = (Integer lhs, Integer rhs, Boolean expected) ->
                {
                    runner.test("with " + lhs + " and " + rhs, (Test test) ->
                    {
                        test.assertEqual(expected, Comparer.lessThanOrEqualTo(lhs.intValue(), rhs));
                    });
                };

                lessThanOrEqualToTest.run(20, null, false);
                lessThanOrEqualToTest.run(5, 7, true);
                lessThanOrEqualToTest.run(4, 4, true);
                lessThanOrEqualToTest.run(10, 1, false);
            });

            runner.testGroup("lessThanOrEqualTo(Integer,int)", () ->
            {
                final Action3<Integer,Integer,Boolean> lessThanOrEqualToTest = (Integer lhs, Integer rhs, Boolean expected) ->
                {
                    runner.test("with " + lhs + " and " + rhs, (Test test) ->
                    {
                        test.assertEqual(expected, Comparer.lessThanOrEqualTo(lhs, rhs.intValue()));
                    });
                };

                lessThanOrEqualToTest.run(null, -5, true);
                lessThanOrEqualToTest.run(5, 7, true);
                lessThanOrEqualToTest.run(4, 4, true);
                lessThanOrEqualToTest.run(10, 1, false);
            });

            runner.testGroup("lessThanOrEqualTo(Integer,Integer)", () ->
            {
                final Action3<Integer,Integer,Boolean> lessThanOrEqualToTest = (Integer lhs, Integer rhs, Boolean expected) ->
                {
                    runner.test("with " + lhs + " and " + rhs, (Test test) ->
                    {
                        test.assertEqual(expected, Comparer.lessThanOrEqualTo(lhs, rhs));
                    });
                };

                lessThanOrEqualToTest.run(null, null, true);
                lessThanOrEqualToTest.run(null, -5, true);
                lessThanOrEqualToTest.run(0, null, false);
                lessThanOrEqualToTest.run(5, 7, true);
                lessThanOrEqualToTest.run(4, 4, true);
                lessThanOrEqualToTest.run(10, 1, false);
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
                equalTest.run(new NotFoundException("a"), new EndOfStreamException(), false);
                equalTest.run(new NotFoundException("a"), new NotFoundException("b"), false);
                equalTest.run(new NotFoundException("a"), new NotFoundException("a"), true);
            });

            runner.testGroup("equal(Throwable,Throwable)", () ->
            {
                final Action3<Throwable,Throwable,Boolean> equalTest = (Throwable lhs, Throwable rhs, Boolean expected) ->
                {
                    runner.test("with " + lhs + " and " + rhs, (Test test) ->
                    {
                        test.assertEqual(expected, Comparer.equal(lhs, rhs));
                    });
                };

                equalTest.run(null, null, true);
                equalTest.run(new NotFoundException("a"), null, false);
                equalTest.run(null, new NotFoundException("b"), false);
                equalTest.run(new NotFoundException("a"), new EndOfStreamException(), false);
                equalTest.run(new NotFoundException("a"), new NotFoundException("b"), false);
                equalTest.run(new NotFoundException("a"), new NotFoundException("a"), true);
            });

            runner.testGroup("minimum(Iterable<T>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> Comparer.minimum(null),
                        new PreConditionFailure("values cannot be null."));
                });

                runner.test("with empty", (Test test) ->
                {
                    test.assertNull(Comparer.minimum(Iterable.<VersionNumber>create()));
                });

                runner.test("with one value", (Test test) ->
                {
                    test.assertEqual(VersionNumber.parse("1"), Comparer.minimum(Iterable.create(VersionNumber.parse("1"))));
                });

                runner.test("with two values", (Test test) ->
                {
                    test.assertEqual(VersionNumber.parse("1"), Comparer.minimum(Iterable.create(VersionNumber.parse("1"), VersionNumber.parse("2"))));
                });

                runner.test("with three values", (Test test) ->
                {
                    test.assertEqual(VersionNumber.parse("0"), Comparer.minimum(Iterable.create(VersionNumber.parse("1"), VersionNumber.parse("0"), VersionNumber.parse("2"))));
                });
            });

            runner.testGroup("minimum(Iterable<T>,Function2<T,T,Comparison>)", () ->
            {
                runner.test("with null values", (Test test) ->
                {
                    test.assertThrows(() -> Comparer.minimum((Iterable<Integer>)null, Integers::compare),
                        new PreConditionFailure("values cannot be null."));
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
                    test.assertThrows(() -> Comparer.maximum((Iterable<VersionNumber>)null),
                        new PreConditionFailure("values cannot be null."));
                });

                runner.test("with empty", (Test test) ->
                {
                    test.assertNull(Comparer.maximum(Iterable.<VersionNumber>create()));
                });

                runner.test("with one value", (Test test) ->
                {
                    test.assertEqual(VersionNumber.parse("1"), Comparer.maximum(Iterable.create(VersionNumber.parse("1"))));
                });

                runner.test("with two values", (Test test) ->
                {
                    test.assertEqual(VersionNumber.parse("2"), Comparer.maximum(Iterable.create(VersionNumber.parse("1"), VersionNumber.parse("2"))));
                });

                runner.test("with three values", (Test test) ->
                {
                    test.assertEqual(VersionNumber.parse("2"), Comparer.maximum(Iterable.create(VersionNumber.parse("1"), VersionNumber.parse("0"), VersionNumber.parse("2"))));
                });
            });

            runner.testGroup("maximum(Iterable<T>,Function2<T,T,Comparison>)", () ->
            {
                runner.test("with null values", (Test test) ->
                {
                    test.assertThrows(() -> Comparer.maximum((Iterable<Integer>)null, Integers::compare),
                        new PreConditionFailure("values cannot be null."));
                });
            });
        });
    }
}
