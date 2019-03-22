package qub;

public class CSVRowTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(CSVRow.class, () ->
        {
            runner.testGroup("constructor(String...)", () ->
            {
                runner.test("with no arguments", (Test test) ->
                {
                    final CSVRow row = new CSVRow();
                    test.assertEqual(0, row.getCount());
                });

                runner.test("with 1 argument", (Test test) ->
                {
                    final CSVRow row = new CSVRow("apples");
                    test.assertEqual(1, row.getCount());
                    test.assertEqual("apples", row.get(0));
                });

                runner.test("with multiple arguments", (Test test) ->
                {
                    final CSVRow row = new CSVRow("11", "22", "33");
                    test.assertEqual(3, row.getCount());
                    test.assertEqual("11", row.get(0));
                    test.assertEqual("22", row.get(1));
                    test.assertEqual("33", row.get(2));
                });
            });

            runner.testGroup("constructor(Iterable<String>)", () ->
            {
                final Action1<Iterable<String>> constructorTest = (Iterable<String> cells) ->
                {
                    runner.test("with " + (cells == null ? "null" : cells.toString()), (Test test) ->
                    {
                        final CSVRow row = new CSVRow(cells);
                        final int expectedCellCount = (cells == null ? 0 : cells.getCount());
                        test.assertEqual(expectedCellCount, row.getCount());
                        if (cells != null)
                        {
                            test.assertEqual(cells, row);
                        }
                    });
                };

                constructorTest.run(null);
                constructorTest.run(Iterable.create());
                constructorTest.run(Iterable.create("a"));
                constructorTest.run(Iterable.create("1", "2", "3 4"));
            });

            runner.testGroup("removeAt(int)", () ->
            {
                final Action4<Indexable<String>,Integer,Iterable<String>,Throwable> removeAtTest = (Indexable<String> cells, Integer index, Iterable<String> expectedCells, Throwable expectedError) ->
                {
                    runner.test("with " + cells + " at index " + index, (Test test) ->
                    {
                        final CSVRow row = new CSVRow(cells);
                        if (expectedError != null)
                        {
                            test.assertThrows(() -> row.removeAt(index), expectedError);
                        }
                        else
                        {
                            test.assertEqual(cells.get(index), row.removeAt(index));
                        }
                        test.assertEqual(expectedCells, row);
                    });
                };

                removeAtTest.run(Indexable.create(), -1, Iterable.create(), new PreConditionFailure("Indexable length (0) must be greater than or equal to 1."));
                removeAtTest.run(Indexable.create(), 0, Iterable.create(), new PreConditionFailure("Indexable length (0) must be greater than or equal to 1."));
                removeAtTest.run(Indexable.create(), 1, Iterable.create(), new PreConditionFailure("Indexable length (0) must be greater than or equal to 1."));

                removeAtTest.run(Indexable.create("a"), -1, Iterable.create("a"), new PreConditionFailure("index (-1) must be equal to 0."));
                removeAtTest.run(Indexable.create("a"), 0, Iterable.create(), null);
                removeAtTest.run(Indexable.create("a"), 1, Iterable.create("a"), new PreConditionFailure("index (1) must be equal to 0."));

                removeAtTest.run(Indexable.create("a", "b"), -1, Iterable.create("a", "b"), new PreConditionFailure("index (-1) must be between 0 and 1."));
                removeAtTest.run(Indexable.create("a", "b"), 0, Iterable.create("b"), null);
                removeAtTest.run(Indexable.create("a", "b"), 1, Iterable.create("a"), null);
                removeAtTest.run(Indexable.create("a", "b"), 2, Iterable.create("a", "b"), new PreConditionFailure("index (2) must be between 0 and 1."));
            });

            runner.testGroup("set(int,String)", () ->
            {
                final Action5<Iterable<String>,Integer,String,Iterable<String>,Throwable> setTest = (Iterable<String> cells, Integer index, String value, Iterable<String> expectedCells, Throwable expectedError) ->
                {
                    runner.test("with " + cells + " at index " + index + " with value " + Strings.escapeAndQuote(value), (Test test) ->
                    {
                        final CSVRow row = new CSVRow(cells);
                        if (expectedError != null)
                        {
                            test.assertThrows(() -> row.set(index, value), expectedError);
                        }
                        else
                        {
                            row.set(index, value);
                        }
                        test.assertEqual(expectedCells, row);
                    });
                };

                setTest.run(Iterable.create(), -1, "z", Iterable.create(), new PreConditionFailure("Indexable length (0) must be greater than or equal to 1."));
                setTest.run(Iterable.create(), 0, "z", Iterable.create(), new PreConditionFailure("Indexable length (0) must be greater than or equal to 1."));
                setTest.run(Iterable.create(), 1, "z", Iterable.create(), new PreConditionFailure("Indexable length (0) must be greater than or equal to 1."));

                setTest.run(Iterable.create("a"), -1, "z", Iterable.create("a"), new PreConditionFailure("index (-1) must be equal to 0."));
                setTest.run(Iterable.create("a"), 0, "z", Iterable.create("z"), null);
                setTest.run(Iterable.create("a"), 1, "z", Iterable.create("a"), new PreConditionFailure("index (1) must be equal to 0."));

                setTest.run(Iterable.create("a", "b"), -1, "z", Iterable.create("a", "b"), new PreConditionFailure("index (-1) must be between 0 and 1."));
                setTest.run(Iterable.create("a", "b"), 0, "z", Iterable.create("z", "b"), null);
                setTest.run(Iterable.create("a", "b"), 1, "z", Iterable.create("a", "z"), null);
                setTest.run(Iterable.create("a", "b"), 2, "z", Iterable.create("a", "b"), new PreConditionFailure("index (2) must be between 0 and 1."));
            });
        });
    }
}
