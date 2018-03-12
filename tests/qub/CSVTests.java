package qub;

public class CSVTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(CSV.class, () ->
        {
            runner.test("constructor()", (Test test) ->
            {
                final CSV csv = new CSV();
                test.assertNotNull(csv);
            });

            runner.testGroup("parse(String)", () ->
            {
                final Action2<String,String[][]> parseTest = (String text, String[][] expectedRows) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(text), (Test test) ->
                    {
                        final CSVDocument document = CSV.parse(text);
                        test.assertNotNull(document);
                        test.assertEqual(expectedRows.length, document.getCount(), "Expected document to have " + expectedRows.length + " rows, but only found " + document.getCount() + ".");

                        for (int rowIndex = 0; rowIndex < expectedRows.length; ++rowIndex)
                        {
                            final CSVRow row = document.get(rowIndex);
                            test.assertNotNull(row);

                            final String[] expectedRow = expectedRows[rowIndex];
                            test.assertEqual(expectedRow.length, row.getCount(), "Expected row " + rowIndex + " to have " + expectedRow.length + " cells, but only found " + row.getCount() + ".");

                            for (int columnIndex = 0; columnIndex < expectedRow.length; ++columnIndex)
                            {
                                test.assertEqual(expectedRow[columnIndex], row.get(columnIndex));
                            }
                        }
                    });
                };

                parseTest.run(
                    null,
                    new String[][]
                    {
                        new String[0]
                    });

                parseTest.run(
                    "",
                    new String[][]
                    {
                        new String[0]
                    });

                parseTest.run(
                    "hello",
                    new String[][]
                    {
                        new String[] { "hello" }
                    });

                parseTest.run(
                    "a,",
                    new String[][]
                    {
                        new String[] { "a", "" }
                    });

                parseTest.run(
                    "a,b",
                    new String[][]
                    {
                        new String[] { "a", "b" }
                    });

                parseTest.run(
                    "a,b,c,d",
                    new String[][]
                    {
                        new String[] { "a", "b", "c", "d" }
                    });

                parseTest.run(
                    "\n",
                    new String[][]
                    {
                        new String[0]
                    });

                parseTest.run(
                    "\r\n",
                    new String[][]
                        {
                            new String[0]
                        });

                parseTest.run(
                    "a\nb\nc",
                    new String[][]
                    {
                        new String[] { "a" },
                        new String[] { "b" },
                        new String[] { "c" }
                    });

                parseTest.run(
                    "a\nb\nc\n",
                    new String[][]
                    {
                        new String[] { "a" },
                        new String[] { "b" },
                        new String[] { "c" }
                    });

                parseTest.run(
                    "a\r\n\n\nb,c\n\n",
                    new String[][]
                    {
                        new String[] { "a" },
                        new String[0],
                        new String[0],
                        new String[] { "b", "c" },
                        new String[0]
                    });

                parseTest.run(
                    "\"",
                    new String[][]
                    {
                        new String[] { "\"" }
                    });

                parseTest.run(
                    "\"\"",
                    new String[][]
                    {
                        new String[] { "\"\"" }
                    });

                parseTest.run(
                    "\"'\"",
                    new String[][]
                    {
                        new String[] { "\"'\"" }
                    });

                parseTest.run(
                    "'",
                    new String[][]
                    {
                        new String[] { "'" }
                    });

                parseTest.run(
                    "''",
                    new String[][]
                    {
                        new String[] { "''" }
                    });

                parseTest.run(
                    "'\"'",
                    new String[][]
                    {
                        new String[] { "'\"'" }
                    });

                parseTest.run(
                    "\"hello, there\nmy friend\"",
                    new String[][]
                    {
                        new String[] { "\"hello, there\nmy friend\"" }
                    });
            });
        });
    }
}
