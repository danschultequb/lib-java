package qub;

public interface ComparerTests
{
    static void test(TestRunner runner)
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

            runner.testGroup("compare(String,String)", () ->
            {
                final Action3<String,String,Comparison> compareTest = (String lhs, String rhs, Comparison expected) ->
                {
                    runner.test("with " + English.andList(Iterable.create(lhs, rhs).map(Strings::escapeAndQuote)), (Test test) ->
                    {
                        test.assertEqual(expected, Comparer.compare(lhs, rhs));
                    });
                };

                compareTest.run(null, null, Comparison.Equal);
                compareTest.run(null, "", Comparison.LessThan);
                compareTest.run(null, "a", Comparison.LessThan);
                compareTest.run(null, "abc", Comparison.LessThan);
                compareTest.run(null, "defg", Comparison.LessThan);

                compareTest.run("", null, Comparison.GreaterThan);
                compareTest.run("", "", Comparison.Equal);
                compareTest.run("", "a", Comparison.LessThan);
                compareTest.run("", "abc", Comparison.LessThan);
                compareTest.run("", "defg", Comparison.LessThan);

                compareTest.run("a", null, Comparison.GreaterThan);
                compareTest.run("a", "", Comparison.GreaterThan);
                compareTest.run("a", "a", Comparison.Equal);
                compareTest.run("a", "abc", Comparison.LessThan);
                compareTest.run("a", "defg", Comparison.LessThan);

                compareTest.run("abc", null, Comparison.GreaterThan);
                compareTest.run("abc", "", Comparison.GreaterThan);
                compareTest.run("abc", "a", Comparison.GreaterThan);
                compareTest.run("abc", "abc", Comparison.Equal);
                compareTest.run("abc", "defg", Comparison.LessThan);
            });

            runner.testGroup("compare(Comparable<T>,T)", () ->
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
                equalTest.run(new NotFoundException("a"), new EmptyException(), false);
                equalTest.run(new NotFoundException("a"), new NotFoundException("b"), false);
                equalTest.run(new NotFoundException("a"), new NotFoundException("a"), true);
            });

            runner.testGroup("equalIgnoreCase(String,String)", () ->
            {
                final Action3<String,String,Boolean> equalIgnoreCaseTest = (String lhs, String rhs, Boolean expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(lhs) + " and " + Strings.escapeAndQuote(rhs), (Test test) ->
                    {
                        test.assertEqual(expected, Comparer.equalIgnoreCase(lhs, rhs));
                    });
                };

                equalIgnoreCaseTest.run(null, null, true);
                equalIgnoreCaseTest.run(null, "", false);
                equalIgnoreCaseTest.run(null, "abc", false);
                equalIgnoreCaseTest.run("", null, false);
                equalIgnoreCaseTest.run("", "", true);
                equalIgnoreCaseTest.run("", "abc", false);
                equalIgnoreCaseTest.run("abc", null, false);
                equalIgnoreCaseTest.run("abc", "", false);
                equalIgnoreCaseTest.run("abc", "abc", true);
                equalIgnoreCaseTest.run("abc", "ABC", true);
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
                equalTest.run(new NotFoundException("a"), new EmptyException(), false);
                equalTest.run(new NotFoundException("a"), new NotFoundException("b"), false);
                equalTest.run(new NotFoundException("a"), new NotFoundException("a"), true);
                equalTest.run(new RuntimeException(new NotFoundException("a")), new NotFoundException("a"), false);
                equalTest.run(new NotFoundException("a"), new RuntimeException(new NotFoundException("a")), false);
                equalTest.run(new RuntimeException(new NotFoundException("a")), new RuntimeException(new NotFoundException("a")), true);
                equalTest.run(new AwaitException(new NotFoundException("a")), new NotFoundException("a"), false);
                equalTest.run(new NotFoundException("a"), new AwaitException(new NotFoundException("a")), false);
                equalTest.run(new AwaitException(new NotFoundException("a")), new AwaitException(new NotFoundException("a")), true);
            });

            runner.testGroup("minimum(Iterable<T>)", () ->
            {
                final Action2<Iterable<Distance>,Throwable> minimumErrorTest = (Iterable<Distance> values, Throwable expected) ->
                {
                    runner.test("with " + values, (Test test) ->
                    {
                        test.assertThrows(() -> Comparer.minimum(values).await(),
                            expected);
                    });
                };

                minimumErrorTest.run(null, new PreConditionFailure("values cannot be null."));
                minimumErrorTest.run(Iterable.create(), new EmptyException());

                final Action2<Iterable<Distance>,Distance> minimumTest = (Iterable<Distance> values, Distance expected) ->
                {
                    runner.test("with " + values, (Test test) ->
                    {
                        test.assertEqual(expected, Comparer.minimum(values).await());
                    });
                };

                minimumTest.run(Iterable.create(1).map(Distance::meters), Distance.meters(1));
                minimumTest.run(Iterable.create(1, 2).map(Distance::meters), Distance.meters(1));
                minimumTest.run(Iterable.create(1, 2, 3).map(Distance::meters), Distance.meters(1));
            });

            runner.testGroup("minimum(Iterable<T>,CompareFunction<T>)", () ->
            {
                final Action4<String,Iterable<Distance>,CompareFunction<Distance>,Throwable> minimumErrorTest = (String testName, Iterable<Distance> values, CompareFunction<Distance> compareFunction, Throwable expected) ->
                {
                    runner.test("with " + values, (Test test) ->
                    {
                        test.assertThrows(() -> Comparer.minimum(values, compareFunction).await(),
                            expected);
                    });
                };

                minimumErrorTest.run("with null values", null, Comparer::compare, new PreConditionFailure("values cannot be null."));
                minimumErrorTest.run("with null compare function", Iterable.create(), null, new PreConditionFailure("comparer cannot be null."));
                minimumErrorTest.run("with empty values", Iterable.create(), Comparer::compare, new EmptyException());

                final Action4<String,Iterable<Distance>,CompareFunction<Distance>,Distance> minimumTest = (String testName, Iterable<Distance> values, CompareFunction<Distance> compareFunction, Distance expected) ->
                {
                    runner.test(testName, (Test test) ->
                    {
                        test.assertEqual(expected, Comparer.minimum(values, compareFunction).await());
                    });
                };

                minimumTest.run(
                    "with one value and normal comparer",
                    Iterable.create(1).map(Distance::meters),
                    Comparer::compare,
                    Distance.meters(1));
                minimumTest.run(
                    "with two values and normal comparer",
                    Iterable.create(1, 2).map(Distance::meters),
                    Comparer::compare,
                    Distance.meters(1));
                minimumTest.run(
                    "with three values and normal comparer",
                    Iterable.create(1, 2, 3).map(Distance::meters),
                    Comparer::compare,
                    Distance.meters(1));

                minimumTest.run(
                    "with one value and reverse comparer",
                    Iterable.create(1).map(Distance::meters),
                    (Distance lhs, Distance rhs) -> Comparison.invert(Comparer.compare(lhs, rhs)),
                    Distance.meters(1));
                minimumTest.run(
                    "with two values and reverse comparer",
                    Iterable.create(1, 2).map(Distance::meters),
                    (Distance lhs, Distance rhs) -> Comparison.invert(Comparer.compare(lhs, rhs)),
                    Distance.meters(2));
                minimumTest.run(
                    "with three values and reverse comparer",
                    Iterable.create(1, 2, 3).map(Distance::meters),
                    (Distance lhs, Distance rhs) -> Comparison.invert(Comparer.compare(lhs, rhs)),
                    Distance.meters(3));
            });

            runner.testGroup("minimum(Iterator<T>)", () ->
            {
                final Action3<String,Iterator<Distance>,Throwable> minimumErrorTest = (String testName, Iterator<Distance> values, Throwable expected) ->
                {
                    runner.test(testName, (Test test) ->
                    {
                        test.assertThrows(() -> Comparer.minimum(values).await(),
                            expected);
                    });
                };

                minimumErrorTest.run("with null", null, new PreConditionFailure("values cannot be null."));
                minimumErrorTest.run("with empty", Iterator.create(), new EmptyException());

                final Action2<Iterable<Distance>,Distance> minimumTest = (Iterable<Distance> values, Distance expected) ->
                {
                    runner.test("with " + values, (Test test) ->
                    {
                        test.assertEqual(expected, Comparer.minimum(values.iterate()).await());
                    });
                };

                minimumTest.run(Iterable.create(1).map(Distance::meters), Distance.meters(1));
                minimumTest.run(Iterable.create(1, 2).map(Distance::meters), Distance.meters(1));
                minimumTest.run(Iterable.create(1, 2, 3).map(Distance::meters), Distance.meters(1));
            });

            runner.testGroup("minimum(Iterator<T>,CompareFunction<T>)", () ->
            {
                final Action4<String,Iterator<Distance>,CompareFunction<Distance>,Throwable> minimumErrorTest = (String testName, Iterator<Distance> values, CompareFunction<Distance> compareFunction, Throwable expected) ->
                {
                    runner.test("with " + values, (Test test) ->
                    {
                        test.assertThrows(() -> Comparer.minimum(values, compareFunction).await(),
                            expected);
                    });
                };

                minimumErrorTest.run("with null values", null, Comparer::compare, new PreConditionFailure("values cannot be null."));
                minimumErrorTest.run("with null compare function", Iterator.create(), null, new PreConditionFailure("comparer cannot be null."));
                minimumErrorTest.run("with empty values", Iterator.create(), Comparer::compare, new EmptyException());

                final Action4<String,Iterator<Distance>,CompareFunction<Distance>,Distance> minimumTest = (String testName, Iterator<Distance> values, CompareFunction<Distance> compareFunction, Distance expected) ->
                {
                    runner.test(testName, (Test test) ->
                    {
                        test.assertEqual(expected, Comparer.minimum(values, compareFunction).await());
                    });
                };

                minimumTest.run(
                    "with one value and normal comparer",
                    Iterator.create(1).map(Distance::meters),
                    Comparer::compare,
                    Distance.meters(1));
                minimumTest.run(
                    "with two values and normal comparer",
                    Iterator.create(1, 2).map(Distance::meters),
                    Comparer::compare,
                    Distance.meters(1));
                minimumTest.run(
                    "with three values and normal comparer",
                    Iterator.create(1, 2, 3).map(Distance::meters),
                    Comparer::compare,
                    Distance.meters(1));

                minimumTest.run(
                    "with one value and reverse comparer",
                    Iterator.create(1).map(Distance::meters),
                    (Distance lhs, Distance rhs) -> Comparison.invert(Comparer.compare(lhs, rhs)),
                    Distance.meters(1));
                minimumTest.run(
                    "with two values and reverse comparer",
                    Iterator.create(1, 2).map(Distance::meters),
                    (Distance lhs, Distance rhs) -> Comparison.invert(Comparer.compare(lhs, rhs)),
                    Distance.meters(2));
                minimumTest.run(
                    "with three values and reverse comparer",
                    Iterator.create(1, 2, 3).map(Distance::meters),
                    (Distance lhs, Distance rhs) -> Comparison.invert(Comparer.compare(lhs, rhs)),
                    Distance.meters(3));
            });

            runner.testGroup("maximum(Iterable<T>)", () ->
            {
                final Action2<Iterable<Distance>,Throwable> maximumErrorTest = (Iterable<Distance> values, Throwable expected) ->
                {
                    runner.test("with " + values, (Test test) ->
                    {
                        test.assertThrows(() -> Comparer.maximum(values).await(),
                            expected);
                    });
                };

                maximumErrorTest.run(null, new PreConditionFailure("values cannot be null."));
                maximumErrorTest.run(Iterable.create(), new EmptyException());

                final Action2<Iterable<Distance>,Distance> maximumTest = (Iterable<Distance> values, Distance expected) ->
                {
                    runner.test("with " + values, (Test test) ->
                    {
                        test.assertEqual(expected, Comparer.maximum(values).await());
                    });
                };

                maximumTest.run(Iterable.create(1).map(Distance::meters), Distance.meters(1));
                maximumTest.run(Iterable.create(1, 2).map(Distance::meters), Distance.meters(2));
                maximumTest.run(Iterable.create(1, 2, 3).map(Distance::meters), Distance.meters(3));
            });

            runner.testGroup("maximum(Iterable<T>,CompareFunction<T>)", () ->
            {
                final Action4<String,Iterable<Distance>,CompareFunction<Distance>,Throwable> maximumErrorTest = (String testName, Iterable<Distance> values, CompareFunction<Distance> compareFunction, Throwable expected) ->
                {
                    runner.test("with " + values, (Test test) ->
                    {
                        test.assertThrows(() -> Comparer.maximum(values, compareFunction).await(),
                            expected);
                    });
                };

                maximumErrorTest.run("with null values", null, Comparer::compare, new PreConditionFailure("values cannot be null."));
                maximumErrorTest.run("with null compare function", Iterable.create(), null, new PreConditionFailure("comparer cannot be null."));
                maximumErrorTest.run("with empty values", Iterable.create(), Comparer::compare, new EmptyException());

                final Action4<String,Iterable<Distance>,CompareFunction<Distance>,Distance> maximumTest = (String testName, Iterable<Distance> values, CompareFunction<Distance> compareFunction, Distance expected) ->
                {
                    runner.test(testName, (Test test) ->
                    {
                        test.assertEqual(expected, Comparer.maximum(values, compareFunction).await());
                    });
                };

                maximumTest.run(
                    "with one value and normal comparer",
                    Iterable.create(1).map(Distance::meters),
                    Comparer::compare,
                    Distance.meters(1));
                maximumTest.run(
                    "with two values and normal comparer",
                    Iterable.create(1, 2).map(Distance::meters),
                    Comparer::compare,
                    Distance.meters(2));
                maximumTest.run(
                    "with three values and normal comparer",
                    Iterable.create(1, 2, 3).map(Distance::meters),
                    Comparer::compare,
                    Distance.meters(3));

                maximumTest.run(
                    "with one value and reverse comparer",
                    Iterable.create(1).map(Distance::meters),
                    (Distance lhs, Distance rhs) -> Comparison.invert(Comparer.compare(lhs, rhs)),
                    Distance.meters(1));
                maximumTest.run(
                    "with two values and reverse comparer",
                    Iterable.create(1, 2).map(Distance::meters),
                    (Distance lhs, Distance rhs) -> Comparison.invert(Comparer.compare(lhs, rhs)),
                    Distance.meters(1));
                maximumTest.run(
                    "with three values and reverse comparer",
                    Iterable.create(1, 2, 3).map(Distance::meters),
                    (Distance lhs, Distance rhs) -> Comparison.invert(Comparer.compare(lhs, rhs)),
                    Distance.meters(1));
            });

            runner.testGroup("maximum(Iterator<T>)", () ->
            {
                final Action3<String,Iterator<Distance>,Throwable> maximumErrorTest = (String testName, Iterator<Distance> values, Throwable expected) ->
                {
                    runner.test(testName, (Test test) ->
                    {
                        test.assertThrows(() -> Comparer.maximum(values).await(),
                            expected);
                    });
                };

                maximumErrorTest.run("with null", null, new PreConditionFailure("values cannot be null."));
                maximumErrorTest.run("with empty", Iterator.create(), new EmptyException());

                final Action2<Iterable<Distance>,Distance> maximumTest = (Iterable<Distance> values, Distance expected) ->
                {
                    runner.test("with " + values, (Test test) ->
                    {
                        test.assertEqual(expected, Comparer.maximum(values.iterate()).await());
                    });
                };

                maximumTest.run(Iterable.create(1).map(Distance::meters), Distance.meters(1));
                maximumTest.run(Iterable.create(1, 2).map(Distance::meters), Distance.meters(2));
                maximumTest.run(Iterable.create(1, 2, 3).map(Distance::meters), Distance.meters(3));
            });

            runner.testGroup("maximum(Iterator<T>,CompareFunction<T>)", () ->
            {
                final Action4<String,Iterator<Distance>,CompareFunction<Distance>,Throwable> maximumErrorTest = (String testName, Iterator<Distance> values, CompareFunction<Distance> compareFunction, Throwable expected) ->
                {
                    runner.test("with " + values, (Test test) ->
                    {
                        test.assertThrows(() -> Comparer.maximum(values, compareFunction).await(),
                            expected);
                    });
                };

                maximumErrorTest.run("with null values", null, Comparer::compare, new PreConditionFailure("values cannot be null."));
                maximumErrorTest.run("with null compare function", Iterator.create(), null, new PreConditionFailure("comparer cannot be null."));
                maximumErrorTest.run("with empty values", Iterator.create(), Comparer::compare, new EmptyException());

                final Action4<String,Iterator<Distance>,CompareFunction<Distance>,Distance> maximumTest = (String testName, Iterator<Distance> values, CompareFunction<Distance> compareFunction, Distance expected) ->
                {
                    runner.test(testName, (Test test) ->
                    {
                        test.assertEqual(expected, Comparer.maximum(values, compareFunction).await());
                    });
                };

                maximumTest.run(
                    "with one value and normal comparer",
                    Iterator.create(1).map(Distance::meters),
                    Comparer::compare,
                    Distance.meters(1));
                maximumTest.run(
                    "with two values and normal comparer",
                    Iterator.create(1, 2).map(Distance::meters),
                    Comparer::compare,
                    Distance.meters(2));
                maximumTest.run(
                    "with three values and normal comparer",
                    Iterator.create(1, 2, 3).map(Distance::meters),
                    Comparer::compare,
                    Distance.meters(3));

                maximumTest.run(
                    "with one value and reverse comparer",
                    Iterator.create(1).map(Distance::meters),
                    (Distance lhs, Distance rhs) -> Comparison.invert(Comparer.compare(lhs, rhs)),
                    Distance.meters(1));
                maximumTest.run(
                    "with two values and reverse comparer",
                    Iterator.create(1, 2).map(Distance::meters),
                    (Distance lhs, Distance rhs) -> Comparison.invert(Comparer.compare(lhs, rhs)),
                    Distance.meters(1));
                maximumTest.run(
                    "with three values and reverse comparer",
                    Iterator.create(1, 2, 3).map(Distance::meters),
                    (Distance lhs, Distance rhs) -> Comparison.invert(Comparer.compare(lhs, rhs)),
                    Distance.meters(1));
            });
        });
    }
}
