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
                constructorTest.run(Iterable.empty());
                constructorTest.run(Iterable.create("a"));
                constructorTest.run(Iterable.create("1", "2", "3 4"));
            });

            runner.testGroup("removeAt(int)", () ->
            {
                final Action3<Indexable<String>,Integer,Iterable<String>> removeAtTest = (Indexable<String> cells, Integer index, Iterable<String> expectedCells) ->
                {
                    runner.test("with " + cells + " at index " + index, (Test test) ->
                    {
                        final CSVRow row = new CSVRow(cells);
                        if (index < 0 || cells.getCount() <= index)
                        {
                            test.assertThrows(() -> row.removeAt(index));
                        }
                        else
                        {
                            test.assertEqual(cells.get(index), row.removeAt(index));
                        }
                        test.assertEqual(expectedCells, row);
                    });
                };

                removeAtTest.run(Indexable.empty(), -1, Iterable.empty());
                removeAtTest.run(Indexable.empty(), 0, Iterable.empty());
                removeAtTest.run(Indexable.empty(), 1, Iterable.empty());

                removeAtTest.run(Indexable.create("a"), -1, Iterable.create("a"));
                removeAtTest.run(Indexable.create("a"), 0, Iterable.empty());
                removeAtTest.run(Indexable.create("a"), 1, Iterable.create("a"));

                removeAtTest.run(Indexable.create("a", "b"), -1, Iterable.create("a", "b"));
                removeAtTest.run(Indexable.create("a", "b"), 0, Iterable.create("b"));
                removeAtTest.run(Indexable.create("a", "b"), 1, Iterable.create("a"));
                removeAtTest.run(Indexable.create("a", "b"), 2, Iterable.create("a", "b"));
            });

            runner.testGroup("set(int,String)", () ->
            {
                final Action4<Iterable<String>,Integer,String,Iterable<String>> setTest = (Iterable<String> cells, Integer index, String value, Iterable<String> expectedCells) ->
                {
                    runner.test("with " + cells + " at index " + index + " with value " + Strings.escapeAndQuote(value), (Test test) ->
                    {
                        final CSVRow row = new CSVRow(cells);
                        if (index < 0 || cells.getCount() <= index)
                        {
                            test.assertThrows(() -> row.set(index, value));
                        }
                        else
                        {
                            row.set(index, value);
                        }
                        test.assertEqual(expectedCells, row);
                    });
                };

                setTest.run(Iterable.empty(), -1, "z", Iterable.empty());
                setTest.run(Iterable.empty(), 0, "z", Iterable.empty());
                setTest.run(Iterable.empty(), 1, "z", Iterable.empty());

                setTest.run(Array.create(new String[] { "a" }), -1, "z", Array.create(new String[] { "a" }));
                setTest.run(Array.create(new String[] { "a" }), 0, "z", Array.create(new String[] { "z" }));
                setTest.run(Array.create(new String[] { "a" }), 1, "z", Array.create(new String[] { "a" }));

                setTest.run(Array.create(new String[] { "a", "b" }), -1, "z", Array.create(new String[] { "a", "b" }));
                setTest.run(Array.create(new String[] { "a", "b" }), 0, "z", Array.create(new String[] { "z", "b" }));
                setTest.run(Array.create(new String[] { "a", "b" }), 1, "z", Array.create(new String[] { "a", "z" }));
                setTest.run(Array.create(new String[] { "a", "b" }), 2, "z", Array.create(new String[] { "a", "b" }));
            });
        });
    }
}
