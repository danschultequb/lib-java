package qub;

public class CSVDocumentTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(CSVDocument.class, () ->
        {
            runner.test("constructor()", (Test test) ->
            {
                final CSVDocument document = new CSVDocument();
                test.assertEqual(0, document.getCount());
            });

            runner.testGroup("constructor(Iterable<CSVRow>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final CSVDocument document = new CSVDocument((Iterable<CSVRow>)null);
                    test.assertEqual(0, document.getCount());
                });

                runner.test("with empty", (Test test) ->
                {
                    final List<CSVRow> list = new ArrayList<>();
                    final CSVDocument document = new CSVDocument(list);
                    test.assertEqual(0, document.getCount());

                    document.add(new CSVRow());
                    test.assertEqual(1, document.getCount());
                    test.assertEqual(0, list.getCount());
                });
            });

            runner.testGroup("iterate()", () ->
            {
                runner.test("with no rows", (Test test) ->
                {
                    final CSVDocument document = new CSVDocument();
                    test.assertEqual(0, document.iterate().getCount());
                });

                runner.test("with 1 row", (Test test) ->
                {
                    final CSVDocument document = new CSVDocument(
                        new CSVRow("a", "b", "c"));
                    test.assertEqual(1, document.iterate().getCount());
                });
            });

            runner.testGroup("get(int,int)", () ->
            {
                final Action4<CSVDocument,Integer,Integer,String> getTest = (CSVDocument document, Integer rowIndex, Integer columnIndex, String expectedValue) ->
                {
                    runner.test("with " + document + " at row " + rowIndex + " and column " + columnIndex, (Test test) ->
                    {
                        test.assertEqual(expectedValue, document.get(rowIndex, columnIndex));
                    });
                };

                final CSVDocument emptyDocument = new CSVDocument();
                getTest.run(emptyDocument, -1, -1, null);
                getTest.run(emptyDocument, -1, 0, null);
                getTest.run(emptyDocument, -1, 1, null);
                getTest.run(emptyDocument, 0, -1, null);
                getTest.run(emptyDocument, 0, 0, null);
                getTest.run(emptyDocument, 0, 1, null);
                getTest.run(emptyDocument, 1, -1, null);
                getTest.run(emptyDocument, 1, 0, null);
                getTest.run(emptyDocument, 1, 1, null);

                final CSVDocument singleEmptyRowDocument = new CSVDocument(new CSVRow());
                getTest.run(singleEmptyRowDocument, -1, -1, null);
                getTest.run(singleEmptyRowDocument, -1, 0, null);
                getTest.run(singleEmptyRowDocument, -1, 1, null);
                getTest.run(singleEmptyRowDocument, 0, -1, null);
                getTest.run(singleEmptyRowDocument, 0, 0, null);
                getTest.run(singleEmptyRowDocument, 0, 1, null);
                getTest.run(singleEmptyRowDocument, 1, -1, null);
                getTest.run(singleEmptyRowDocument, 1, 0, null);
                getTest.run(singleEmptyRowDocument, 1, 1, null);

                final CSVDocument singleRowSingleColumnDocument = new CSVDocument(new CSVRow("test"));
                getTest.run(singleRowSingleColumnDocument, -1, -1, null);
                getTest.run(singleRowSingleColumnDocument, -1, 0, null);
                getTest.run(singleRowSingleColumnDocument, -1, 1, null);
                getTest.run(singleRowSingleColumnDocument, 0, -1, null);
                getTest.run(singleRowSingleColumnDocument, 0, 0, "test");
                getTest.run(singleRowSingleColumnDocument, 0, 1, null);
                getTest.run(singleRowSingleColumnDocument, 1, -1, null);
                getTest.run(singleRowSingleColumnDocument, 1, 0, null);
                getTest.run(singleRowSingleColumnDocument, 1, 1, null);
            });

            runner.testGroup("set(int,CSVRow)", () ->
            {
                final Action4<CSVDocument,Integer,CSVRow,CSVDocument> setTest = (CSVDocument document, Integer rowIndex, CSVRow row, CSVDocument expectedDocument) ->
                {
                    runner.test("with " + document + " at row " + rowIndex + " with " + row, (Test test) ->
                    {
                        document.set(rowIndex, row);
                        test.assertEqual(expectedDocument, document);
                    });
                };

                setTest.run(new CSVDocument(), -1, null, new CSVDocument());
                setTest.run(new CSVDocument(), 0, null, new CSVDocument());
                setTest.run(new CSVDocument(), 1, null, new CSVDocument());

                setTest.run(new CSVDocument(), -1, new CSVRow(), new CSVDocument());
                setTest.run(new CSVDocument(), 0, new CSVRow(), new CSVDocument());
                setTest.run(new CSVDocument(), 1, new CSVRow(), new CSVDocument());

                setTest.run(new CSVDocument(new CSVRow()), -1, null, new CSVDocument(new CSVRow()));
                setTest.run(new CSVDocument(new CSVRow()), 0, null, new CSVDocument((CSVRow)null));
                setTest.run(new CSVDocument(new CSVRow()), 1, null, new CSVDocument(new CSVRow()));

                setTest.run(new CSVDocument(new CSVRow("a")), -1, new CSVRow("b"), new CSVDocument(new CSVRow("a")));
                setTest.run(new CSVDocument(new CSVRow("a")), 0, new CSVRow("b"), new CSVDocument(new CSVRow("b")));
                setTest.run(new CSVDocument(new CSVRow("a")), 1, new CSVRow("b"), new CSVDocument(new CSVRow("a")));
            });

            runner.testGroup("set(int,int,String)", () ->
            {
                final Action5<CSVDocument,Integer,Integer,String,CSVDocument> setTest = (CSVDocument document, Integer rowIndex, Integer columnIndex, String value, CSVDocument expectedDocument) ->
                {
                    runner.test("with " + document + " at row " + rowIndex + " and column " + columnIndex + " with " + Strings.escapeAndQuote(value), (Test test) ->
                    {
                        document.set(rowIndex, columnIndex, value);
                        test.assertEqual(expectedDocument, document);
                    });
                };

                setTest.run(new CSVDocument(), -1, -1, null, new CSVDocument());
                setTest.run(new CSVDocument(), -1, 0, null, new CSVDocument());
                setTest.run(new CSVDocument(), -1, 1, null, new CSVDocument());
                setTest.run(new CSVDocument(), 0, -1, null, new CSVDocument());
                setTest.run(new CSVDocument(), 0, 0, null, new CSVDocument());
                setTest.run(new CSVDocument(), 0, 1, null, new CSVDocument());
                setTest.run(new CSVDocument(), 1, -1, null, new CSVDocument());
                setTest.run(new CSVDocument(), 1, 0, null, new CSVDocument());
                setTest.run(new CSVDocument(), 1, 1, null, new CSVDocument());

                setTest.run(new CSVDocument(), -1, -1, "", new CSVDocument());
                setTest.run(new CSVDocument(), -1, 0, "", new CSVDocument());
                setTest.run(new CSVDocument(), -1, 1, "", new CSVDocument());
                setTest.run(new CSVDocument(), 0, -1, "", new CSVDocument());
                setTest.run(new CSVDocument(), 0, 0, "", new CSVDocument());
                setTest.run(new CSVDocument(), 0, 1, "", new CSVDocument());
                setTest.run(new CSVDocument(), 1, -1, "", new CSVDocument());
                setTest.run(new CSVDocument(), 1, 0, "", new CSVDocument());
                setTest.run(new CSVDocument(), 1, 1, "", new CSVDocument());

                setTest.run(new CSVDocument(new CSVRow()), -1, -1, null, new CSVDocument(new CSVRow()));
                setTest.run(new CSVDocument(new CSVRow()), -1, 0, null, new CSVDocument(new CSVRow()));
                setTest.run(new CSVDocument(new CSVRow()), -1, 1, null, new CSVDocument(new CSVRow()));
                setTest.run(new CSVDocument(new CSVRow()), 0, -1, null, new CSVDocument(new CSVRow()));
                setTest.run(new CSVDocument(new CSVRow()), 0, 0, null, new CSVDocument(new CSVRow()));
                setTest.run(new CSVDocument(new CSVRow()), 0, 1, null, new CSVDocument(new CSVRow()));
                setTest.run(new CSVDocument(new CSVRow()), 1, -1, null, new CSVDocument(new CSVRow()));
                setTest.run(new CSVDocument(new CSVRow()), 1, 0, null, new CSVDocument(new CSVRow()));
                setTest.run(new CSVDocument(new CSVRow()), 1, 1, null, new CSVDocument(new CSVRow()));

                setTest.run(new CSVDocument(new CSVRow("a")), -1, -1, "b", new CSVDocument(new CSVRow("a")));
                setTest.run(new CSVDocument(new CSVRow("a")), -1, 0, "b", new CSVDocument(new CSVRow("a")));
                setTest.run(new CSVDocument(new CSVRow("a")), -1, 1, "b", new CSVDocument(new CSVRow("a")));
                setTest.run(new CSVDocument(new CSVRow("a")), 0, -1, "b", new CSVDocument(new CSVRow("a")));
                setTest.run(new CSVDocument(new CSVRow("a")), 0, 0, "b", new CSVDocument(new CSVRow("b")));
                setTest.run(new CSVDocument(new CSVRow("a")), 0, 1, "b", new CSVDocument(new CSVRow("a")));
                setTest.run(new CSVDocument(new CSVRow("a")), 1, -1, "b", new CSVDocument(new CSVRow("a")));
                setTest.run(new CSVDocument(new CSVRow("a")), 1, 0, "b", new CSVDocument(new CSVRow("a")));
                setTest.run(new CSVDocument(new CSVRow("a")), 1, 1, "b", new CSVDocument(new CSVRow("a")));
            });

            runner.testGroup("removeAt(int)", () ->
            {
                final Action3<CSVDocument,Integer,CSVDocument> removeAtTest = (CSVDocument document, Integer rowIndex, CSVDocument expectedDocument) ->
                {
                    runner.test("with " + document + " at row index " + rowIndex, (Test test) ->
                    {
                        final CSVRow removedRow = document.get(rowIndex);
                        test.assertEqual(removedRow, document.removeAt(rowIndex));
                        test.assertEqual(expectedDocument, document);
                    });
                };

                removeAtTest.run(new CSVDocument(), -1, new CSVDocument());
                removeAtTest.run(new CSVDocument(), 0, new CSVDocument());
                removeAtTest.run(new CSVDocument(), 1, new CSVDocument());

                removeAtTest.run(new CSVDocument(new CSVRow()), -1, new CSVDocument(new CSVRow()));
                removeAtTest.run(new CSVDocument(new CSVRow()), 0, new CSVDocument());
                removeAtTest.run(new CSVDocument(new CSVRow()), 1, new CSVDocument(new CSVRow()));

                removeAtTest.run(new CSVDocument(new CSVRow("a"), new CSVRow("b")), -1, new CSVDocument(new CSVRow("a"), new CSVRow("b")));
                removeAtTest.run(new CSVDocument(new CSVRow("a"), new CSVRow("b")), 0, new CSVDocument(new CSVRow("b")));
                removeAtTest.run(new CSVDocument(new CSVRow("a"), new CSVRow("b")), 1, new CSVDocument(new CSVRow("a")));
                removeAtTest.run(new CSVDocument(new CSVRow("a"), new CSVRow("b")), 2, new CSVDocument(new CSVRow("a"), new CSVRow("b")));
            });

            runner.testGroup("removeAt(int,int)", () ->
            {
                final Action4<CSVDocument,Integer,Integer,CSVDocument> removeAtTest = (CSVDocument document, Integer rowIndex, Integer columnIndex, CSVDocument expectedDocument) ->
                {
                    runner.test("with " + document + " at row index " + rowIndex + " and column index " + columnIndex, (Test test) ->
                    {
                        final String removedCell = document.get(rowIndex, columnIndex);
                        test.assertEqual(removedCell, document.removeAt(rowIndex, columnIndex));
                        test.assertEqual(expectedDocument, document);
                    });
                };

                removeAtTest.run(new CSVDocument(), -1, -1, new CSVDocument());
                removeAtTest.run(new CSVDocument(), -1, 0, new CSVDocument());
                removeAtTest.run(new CSVDocument(), -1, 1, new CSVDocument());
                removeAtTest.run(new CSVDocument(), 0, -1, new CSVDocument());
                removeAtTest.run(new CSVDocument(), 0, 0, new CSVDocument());
                removeAtTest.run(new CSVDocument(), 0, 1, new CSVDocument());
                removeAtTest.run(new CSVDocument(), 1, -1, new CSVDocument());
                removeAtTest.run(new CSVDocument(), 1, 0, new CSVDocument());
                removeAtTest.run(new CSVDocument(), 1, 1, new CSVDocument());

                removeAtTest.run(new CSVDocument(new CSVRow()), -1, -1, new CSVDocument(new CSVRow()));
                removeAtTest.run(new CSVDocument(new CSVRow()), -1, 0, new CSVDocument(new CSVRow()));
                removeAtTest.run(new CSVDocument(new CSVRow()), -1, 1, new CSVDocument(new CSVRow()));
                removeAtTest.run(new CSVDocument(new CSVRow()), 0, -1, new CSVDocument(new CSVRow()));
                removeAtTest.run(new CSVDocument(new CSVRow()), 0, 0, new CSVDocument(new CSVRow()));
                removeAtTest.run(new CSVDocument(new CSVRow()), 0, 1, new CSVDocument(new CSVRow()));
                removeAtTest.run(new CSVDocument(new CSVRow()), 1, -1, new CSVDocument(new CSVRow()));
                removeAtTest.run(new CSVDocument(new CSVRow()), 1, 0, new CSVDocument(new CSVRow()));
                removeAtTest.run(new CSVDocument(new CSVRow()), 1, 1, new CSVDocument(new CSVRow()));

                removeAtTest.run(new CSVDocument(new CSVRow("a")), -1, -1, new CSVDocument(new CSVRow("a")));
                removeAtTest.run(new CSVDocument(new CSVRow("a")), -1, 0, new CSVDocument(new CSVRow("a")));
                removeAtTest.run(new CSVDocument(new CSVRow("a")), -1, 1, new CSVDocument(new CSVRow("a")));
                removeAtTest.run(new CSVDocument(new CSVRow("a")), 0, -1, new CSVDocument(new CSVRow("a")));
                removeAtTest.run(new CSVDocument(new CSVRow("a")), 0, 0, new CSVDocument(new CSVRow()));
                removeAtTest.run(new CSVDocument(new CSVRow("a")), 0, 1, new CSVDocument(new CSVRow("a")));
                removeAtTest.run(new CSVDocument(new CSVRow("a")), 1, -1, new CSVDocument(new CSVRow("a")));
                removeAtTest.run(new CSVDocument(new CSVRow("a")), 1, 0, new CSVDocument(new CSVRow("a")));
                removeAtTest.run(new CSVDocument(new CSVRow("a")), 1, 1, new CSVDocument(new CSVRow("a")));
            });
        });
    }
}
