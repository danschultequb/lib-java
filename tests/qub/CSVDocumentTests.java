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
                final Action5<CSVDocument,Integer,Integer,String,Throwable> getTest = (CSVDocument document, Integer rowIndex, Integer columnIndex, String expectedValue, Throwable expectedError) ->
                {
                    runner.test("with " + document + " at row " + rowIndex + " and column " + columnIndex, (Test test) ->
                    {
                        if (expectedError != null)
                        {
                            test.assertThrows(() -> document.get(rowIndex, columnIndex), expectedError);
                        }
                        else
                        {
                            test.assertEqual(expectedValue, document.get(rowIndex, columnIndex));
                        }
                    });
                };

                final CSVDocument emptyDocument = new CSVDocument();
                getTest.run(emptyDocument, -1, -1, null, new PreConditionFailure("Indexable length (0) must be greater than or equal to 1."));
                getTest.run(emptyDocument, -1, 0, null, new PreConditionFailure("Indexable length (0) must be greater than or equal to 1."));
                getTest.run(emptyDocument, -1, 1, null, new PreConditionFailure("Indexable length (0) must be greater than or equal to 1."));
                getTest.run(emptyDocument, 0, -1, null, new PreConditionFailure("Indexable length (0) must be greater than or equal to 1."));
                getTest.run(emptyDocument, 0, 0, null, new PreConditionFailure("Indexable length (0) must be greater than or equal to 1."));
                getTest.run(emptyDocument, 0, 1, null, new PreConditionFailure("Indexable length (0) must be greater than or equal to 1."));
                getTest.run(emptyDocument, 1, -1, null, new PreConditionFailure("Indexable length (0) must be greater than or equal to 1."));
                getTest.run(emptyDocument, 1, 0, null, new PreConditionFailure("Indexable length (0) must be greater than or equal to 1."));
                getTest.run(emptyDocument, 1, 1, null, new PreConditionFailure("Indexable length (0) must be greater than or equal to 1."));

                final CSVDocument singleEmptyRowDocument = new CSVDocument(new CSVRow());
                getTest.run(singleEmptyRowDocument, -1, -1, null, new PreConditionFailure("index (-1) must be equal to 0."));
                getTest.run(singleEmptyRowDocument, -1, 0, null, new PreConditionFailure("index (-1) must be equal to 0."));
                getTest.run(singleEmptyRowDocument, -1, 1, null, new PreConditionFailure("index (-1) must be equal to 0."));
                getTest.run(singleEmptyRowDocument, 0, -1, null, new PreConditionFailure("Indexable length (0) must be greater than or equal to 1."));
                getTest.run(singleEmptyRowDocument, 0, 0, null, new PreConditionFailure("Indexable length (0) must be greater than or equal to 1."));
                getTest.run(singleEmptyRowDocument, 0, 1, null, new PreConditionFailure("Indexable length (0) must be greater than or equal to 1."));
                getTest.run(singleEmptyRowDocument, 1, -1, null, new PreConditionFailure("index (1) must be equal to 0."));
                getTest.run(singleEmptyRowDocument, 1, 0, null, new PreConditionFailure("index (1) must be equal to 0."));
                getTest.run(singleEmptyRowDocument, 1, 1, null, new PreConditionFailure("index (1) must be equal to 0."));

                final CSVDocument singleRowSingleColumnDocument = new CSVDocument(new CSVRow("test"));
                getTest.run(singleRowSingleColumnDocument, -1, -1, null, new PreConditionFailure("index (-1) must be equal to 0."));
                getTest.run(singleRowSingleColumnDocument, -1, 0, null, new PreConditionFailure("index (-1) must be equal to 0."));
                getTest.run(singleRowSingleColumnDocument, -1, 1, null, new PreConditionFailure("index (-1) must be equal to 0."));
                getTest.run(singleRowSingleColumnDocument, 0, -1, null, new PreConditionFailure("index (-1) must be equal to 0."));
                getTest.run(singleRowSingleColumnDocument, 0, 0, "test", null);
                getTest.run(singleRowSingleColumnDocument, 0, 1, null, new PreConditionFailure("index (1) must be equal to 0."));
                getTest.run(singleRowSingleColumnDocument, 1, -1, null, new PreConditionFailure("index (1) must be equal to 0."));
                getTest.run(singleRowSingleColumnDocument, 1, 0, null, new PreConditionFailure("index (1) must be equal to 0."));
                getTest.run(singleRowSingleColumnDocument, 1, 1, null, new PreConditionFailure("index (1) must be equal to 0."));
            });

            runner.testGroup("set(int,CSVRow)", () ->
            {
                final Action5<CSVDocument,Integer,CSVRow,CSVDocument,Throwable> setTest = (CSVDocument document, Integer rowIndex, CSVRow row, CSVDocument expectedDocument, Throwable expectedError) ->
                {
                    runner.test("with " + document + " at row " + rowIndex + " with " + row, (Test test) ->
                    {
                        if (expectedError != null)
                        {
                            test.assertThrows(() -> document.set(rowIndex, row), expectedError);
                        }
                        else
                        {
                            document.set(rowIndex, row);
                            test.assertEqual(expectedDocument, document);
                        }
                    });
                };

                setTest.run(new CSVDocument(), -1, null, new CSVDocument(), new PreConditionFailure("Indexable length (0) must be greater than or equal to 1."));
                setTest.run(new CSVDocument(), 0, null, new CSVDocument(), new PreConditionFailure("Indexable length (0) must be greater than or equal to 1."));
                setTest.run(new CSVDocument(), 1, null, new CSVDocument(), new PreConditionFailure("Indexable length (0) must be greater than or equal to 1."));

                setTest.run(new CSVDocument(), -1, new CSVRow(), new CSVDocument(), new PreConditionFailure("Indexable length (0) must be greater than or equal to 1."));
                setTest.run(new CSVDocument(), 0, new CSVRow(), new CSVDocument(), new PreConditionFailure("Indexable length (0) must be greater than or equal to 1."));
                setTest.run(new CSVDocument(), 1, new CSVRow(), new CSVDocument(), new PreConditionFailure("Indexable length (0) must be greater than or equal to 1."));

                setTest.run(new CSVDocument(new CSVRow()), -1, null, new CSVDocument(new CSVRow()), new PreConditionFailure("index (-1) must be equal to 0."));
                setTest.run(new CSVDocument(new CSVRow()), 0, null, new CSVDocument((CSVRow)null), null);
                setTest.run(new CSVDocument(new CSVRow()), 1, null, new CSVDocument(new CSVRow()), new PreConditionFailure("index (1) must be equal to 0."));

                setTest.run(new CSVDocument(new CSVRow("a")), -1, new CSVRow("b"), new CSVDocument(new CSVRow("a")), new PreConditionFailure("index (-1) must be equal to 0."));
                setTest.run(new CSVDocument(new CSVRow("a")), 0, new CSVRow("b"), new CSVDocument(new CSVRow("b")), null);
                setTest.run(new CSVDocument(new CSVRow("a")), 1, new CSVRow("b"), new CSVDocument(new CSVRow("a")), new PreConditionFailure("index (1) must be equal to 0."));
            });

            runner.testGroup("set(int,int,String)", () ->
            {
                final Action6<CSVDocument,Integer,Integer,String,CSVDocument,Throwable> setTest = (CSVDocument document, Integer rowIndex, Integer columnIndex, String value, CSVDocument expectedDocument, Throwable expectedError) ->
                {
                    runner.test("with " + document + " at row " + rowIndex + " and column " + columnIndex + " with " + Strings.escapeAndQuote(value), (Test test) ->
                    {
                        if (expectedError != null)
                        {
                            test.assertThrows(() -> document.set(rowIndex, columnIndex, value), expectedError);
                        }
                        else
                        {
                            document.set(rowIndex, columnIndex, value);
                            test.assertEqual(expectedDocument, document);
                        }
                    });
                };

                setTest.run(new CSVDocument(), -1, -1, null, new CSVDocument(), new PreConditionFailure("Indexable length (0) must be greater than or equal to 1."));
                setTest.run(new CSVDocument(), -1, 0, null, new CSVDocument(), new PreConditionFailure("Indexable length (0) must be greater than or equal to 1."));
                setTest.run(new CSVDocument(), -1, 1, null, new CSVDocument(), new PreConditionFailure("Indexable length (0) must be greater than or equal to 1."));
                setTest.run(new CSVDocument(), 0, -1, null, new CSVDocument(), new PreConditionFailure("Indexable length (0) must be greater than or equal to 1."));
                setTest.run(new CSVDocument(), 0, 0, null, new CSVDocument(), new PreConditionFailure("Indexable length (0) must be greater than or equal to 1."));
                setTest.run(new CSVDocument(), 0, 1, null, new CSVDocument(), new PreConditionFailure("Indexable length (0) must be greater than or equal to 1."));
                setTest.run(new CSVDocument(), 1, -1, null, new CSVDocument(), new PreConditionFailure("Indexable length (0) must be greater than or equal to 1."));
                setTest.run(new CSVDocument(), 1, 0, null, new CSVDocument(), new PreConditionFailure("Indexable length (0) must be greater than or equal to 1."));
                setTest.run(new CSVDocument(), 1, 1, null, new CSVDocument(), new PreConditionFailure("Indexable length (0) must be greater than or equal to 1."));

                setTest.run(new CSVDocument(), -1, -1, "", new CSVDocument(), new PreConditionFailure("Indexable length (0) must be greater than or equal to 1."));
                setTest.run(new CSVDocument(), -1, 0, "", new CSVDocument(), new PreConditionFailure("Indexable length (0) must be greater than or equal to 1."));
                setTest.run(new CSVDocument(), -1, 1, "", new CSVDocument(), new PreConditionFailure("Indexable length (0) must be greater than or equal to 1."));
                setTest.run(new CSVDocument(), 0, -1, "", new CSVDocument(), new PreConditionFailure("Indexable length (0) must be greater than or equal to 1."));
                setTest.run(new CSVDocument(), 0, 0, "", new CSVDocument(), new PreConditionFailure("Indexable length (0) must be greater than or equal to 1."));
                setTest.run(new CSVDocument(), 0, 1, "", new CSVDocument(), new PreConditionFailure("Indexable length (0) must be greater than or equal to 1."));
                setTest.run(new CSVDocument(), 1, -1, "", new CSVDocument(), new PreConditionFailure("Indexable length (0) must be greater than or equal to 1."));
                setTest.run(new CSVDocument(), 1, 0, "", new CSVDocument(), new PreConditionFailure("Indexable length (0) must be greater than or equal to 1."));
                setTest.run(new CSVDocument(), 1, 1, "", new CSVDocument(), new PreConditionFailure("Indexable length (0) must be greater than or equal to 1."));

                setTest.run(new CSVDocument(new CSVRow()), -1, -1, null, new CSVDocument(new CSVRow()), new PreConditionFailure("index (-1) must be equal to 0."));
                setTest.run(new CSVDocument(new CSVRow()), -1, 0, null, new CSVDocument(new CSVRow()), new PreConditionFailure("index (-1) must be equal to 0."));
                setTest.run(new CSVDocument(new CSVRow()), -1, 1, null, new CSVDocument(new CSVRow()), new PreConditionFailure("index (-1) must be equal to 0."));
                setTest.run(new CSVDocument(new CSVRow()), 0, -1, null, new CSVDocument(new CSVRow()), new PreConditionFailure("Indexable length (0) must be greater than or equal to 1."));
                setTest.run(new CSVDocument(new CSVRow()), 0, 0, null, new CSVDocument(new CSVRow()), new PreConditionFailure("Indexable length (0) must be greater than or equal to 1."));
                setTest.run(new CSVDocument(new CSVRow()), 0, 1, null, new CSVDocument(new CSVRow()), new PreConditionFailure("Indexable length (0) must be greater than or equal to 1."));
                setTest.run(new CSVDocument(new CSVRow()), 1, -1, null, new CSVDocument(new CSVRow()), new PreConditionFailure("index (1) must be equal to 0."));
                setTest.run(new CSVDocument(new CSVRow()), 1, 0, null, new CSVDocument(new CSVRow()), new PreConditionFailure("index (1) must be equal to 0."));
                setTest.run(new CSVDocument(new CSVRow()), 1, 1, null, new CSVDocument(new CSVRow()), new PreConditionFailure("index (1) must be equal to 0."));

                setTest.run(new CSVDocument(new CSVRow("a")), -1, -1, "b", new CSVDocument(new CSVRow("a")), new PreConditionFailure("index (-1) must be equal to 0."));
                setTest.run(new CSVDocument(new CSVRow("a")), -1, 0, "b", new CSVDocument(new CSVRow("a")), new PreConditionFailure("index (-1) must be equal to 0."));
                setTest.run(new CSVDocument(new CSVRow("a")), -1, 1, "b", new CSVDocument(new CSVRow("a")), new PreConditionFailure("index (-1) must be equal to 0."));
                setTest.run(new CSVDocument(new CSVRow("a")), 0, -1, "b", new CSVDocument(new CSVRow("a")), new PreConditionFailure("index (-1) must be equal to 0."));
                setTest.run(new CSVDocument(new CSVRow("a")), 0, 0, "b", new CSVDocument(new CSVRow("b")), null);
                setTest.run(new CSVDocument(new CSVRow("a")), 0, 1, "b", new CSVDocument(new CSVRow("a")), new PreConditionFailure("index (1) must be equal to 0."));
                setTest.run(new CSVDocument(new CSVRow("a")), 1, -1, "b", new CSVDocument(new CSVRow("a")), new PreConditionFailure("index (1) must be equal to 0."));
                setTest.run(new CSVDocument(new CSVRow("a")), 1, 0, "b", new CSVDocument(new CSVRow("a")), new PreConditionFailure("index (1) must be equal to 0."));
                setTest.run(new CSVDocument(new CSVRow("a")), 1, 1, "b", new CSVDocument(new CSVRow("a")), new PreConditionFailure("index (1) must be equal to 0."));
            });

            runner.testGroup("removeAt(int)", () ->
            {
                final Action4<CSVDocument,Integer,CSVDocument,Throwable> removeAtTest = (CSVDocument document, Integer rowIndex, CSVDocument expectedDocument, Throwable expectedError) ->
                {
                    runner.test("with " + document + " at row index " + rowIndex, (Test test) ->
                    {
                        if (expectedError != null)
                        {
                            test.assertThrows(() -> document.removeAt(rowIndex), expectedError);
                        }
                        else
                        {
                            final CSVRow removedRow = document.get(rowIndex);
                            test.assertEqual(removedRow, document.removeAt(rowIndex));
                        }
                        test.assertEqual(expectedDocument, document);
                    });
                };

                removeAtTest.run(new CSVDocument(), -1, new CSVDocument(), new PreConditionFailure("Indexable length (0) must be greater than or equal to 1."));
                removeAtTest.run(new CSVDocument(), 0, new CSVDocument(), new PreConditionFailure("Indexable length (0) must be greater than or equal to 1."));
                removeAtTest.run(new CSVDocument(), 1, new CSVDocument(), new PreConditionFailure("Indexable length (0) must be greater than or equal to 1."));

                removeAtTest.run(new CSVDocument(new CSVRow()), -1, new CSVDocument(new CSVRow()), new PreConditionFailure("index (-1) must be equal to 0."));
                removeAtTest.run(new CSVDocument(new CSVRow()), 0, new CSVDocument(), null);
                removeAtTest.run(new CSVDocument(new CSVRow()), 1, new CSVDocument(new CSVRow()), new PreConditionFailure("index (1) must be equal to 0."));

                removeAtTest.run(new CSVDocument(new CSVRow("a"), new CSVRow("b")), -1, new CSVDocument(new CSVRow("a"), new CSVRow("b")), new PreConditionFailure("index (-1) must be between 0 and 1."));
                removeAtTest.run(new CSVDocument(new CSVRow("a"), new CSVRow("b")), 0, new CSVDocument(new CSVRow("b")), null);
                removeAtTest.run(new CSVDocument(new CSVRow("a"), new CSVRow("b")), 1, new CSVDocument(new CSVRow("a")), null);
                removeAtTest.run(new CSVDocument(new CSVRow("a"), new CSVRow("b")), 2, new CSVDocument(new CSVRow("a"), new CSVRow("b")), new PreConditionFailure("index (2) must be between 0 and 1."));
            });

            runner.testGroup("removeAt(int,int)", () ->
            {
                final Action5<CSVDocument,Integer,Integer,CSVDocument,Throwable> removeAtTest = (CSVDocument document, Integer rowIndex, Integer columnIndex, CSVDocument expectedDocument, Throwable expectedError) ->
                {
                    runner.test("with " + document + " at row index " + rowIndex + " and column index " + columnIndex, (Test test) ->
                    {
                        if (expectedError != null)
                        {
                            test.assertThrows(() -> document.removeAt(rowIndex, columnIndex), expectedError);
                        }
                        else
                        {
                            final String removedCell = document.get(rowIndex, columnIndex);
                            test.assertEqual(removedCell, document.removeAt(rowIndex, columnIndex));
                        }
                        test.assertEqual(expectedDocument, document);
                    });
                };

                removeAtTest.run(new CSVDocument(), -1, -1, new CSVDocument(), new PreConditionFailure("Indexable length (0) must be greater than or equal to 1."));
                removeAtTest.run(new CSVDocument(), -1, 0, new CSVDocument(), new PreConditionFailure("Indexable length (0) must be greater than or equal to 1."));
                removeAtTest.run(new CSVDocument(), -1, 1, new CSVDocument(), new PreConditionFailure("Indexable length (0) must be greater than or equal to 1."));
                removeAtTest.run(new CSVDocument(), 0, -1, new CSVDocument(), new PreConditionFailure("Indexable length (0) must be greater than or equal to 1."));
                removeAtTest.run(new CSVDocument(), 0, 0, new CSVDocument(), new PreConditionFailure("Indexable length (0) must be greater than or equal to 1."));
                removeAtTest.run(new CSVDocument(), 0, 1, new CSVDocument(), new PreConditionFailure("Indexable length (0) must be greater than or equal to 1."));
                removeAtTest.run(new CSVDocument(), 1, -1, new CSVDocument(), new PreConditionFailure("Indexable length (0) must be greater than or equal to 1."));
                removeAtTest.run(new CSVDocument(), 1, 0, new CSVDocument(), new PreConditionFailure("Indexable length (0) must be greater than or equal to 1."));
                removeAtTest.run(new CSVDocument(), 1, 1, new CSVDocument(), new PreConditionFailure("Indexable length (0) must be greater than or equal to 1."));

                removeAtTest.run(new CSVDocument(new CSVRow()), -1, -1, new CSVDocument(new CSVRow()), new PreConditionFailure("index (-1) must be equal to 0."));
                removeAtTest.run(new CSVDocument(new CSVRow()), -1, 0, new CSVDocument(new CSVRow()), new PreConditionFailure("index (-1) must be equal to 0."));
                removeAtTest.run(new CSVDocument(new CSVRow()), -1, 1, new CSVDocument(new CSVRow()), new PreConditionFailure("index (-1) must be equal to 0."));
                removeAtTest.run(new CSVDocument(new CSVRow()), 0, -1, new CSVDocument(new CSVRow()), new PreConditionFailure("Indexable length (0) must be greater than or equal to 1."));
                removeAtTest.run(new CSVDocument(new CSVRow()), 0, 0, new CSVDocument(new CSVRow()), new PreConditionFailure("Indexable length (0) must be greater than or equal to 1."));
                removeAtTest.run(new CSVDocument(new CSVRow()), 0, 1, new CSVDocument(new CSVRow()), new PreConditionFailure("Indexable length (0) must be greater than or equal to 1."));
                removeAtTest.run(new CSVDocument(new CSVRow()), 1, -1, new CSVDocument(new CSVRow()), new PreConditionFailure("index (1) must be equal to 0."));
                removeAtTest.run(new CSVDocument(new CSVRow()), 1, 0, new CSVDocument(new CSVRow()), new PreConditionFailure("index (1) must be equal to 0."));
                removeAtTest.run(new CSVDocument(new CSVRow()), 1, 1, new CSVDocument(new CSVRow()), new PreConditionFailure("index (1) must be equal to 0."));

                removeAtTest.run(new CSVDocument(new CSVRow("a")), -1, -1, new CSVDocument(new CSVRow("a")), new PreConditionFailure("index (-1) must be equal to 0."));
                removeAtTest.run(new CSVDocument(new CSVRow("a")), -1, 0, new CSVDocument(new CSVRow("a")), new PreConditionFailure("index (-1) must be equal to 0."));
                removeAtTest.run(new CSVDocument(new CSVRow("a")), -1, 1, new CSVDocument(new CSVRow("a")), new PreConditionFailure("index (-1) must be equal to 0."));
                removeAtTest.run(new CSVDocument(new CSVRow("a")), 0, -1, new CSVDocument(new CSVRow("a")), new PreConditionFailure("index (-1) must be equal to 0."));
                removeAtTest.run(new CSVDocument(new CSVRow("a")), 0, 0, new CSVDocument(new CSVRow()), null);
                removeAtTest.run(new CSVDocument(new CSVRow("a")), 0, 1, new CSVDocument(new CSVRow("a")), new PreConditionFailure("index (1) must be equal to 0."));
                removeAtTest.run(new CSVDocument(new CSVRow("a")), 1, -1, new CSVDocument(new CSVRow("a")), new PreConditionFailure("index (1) must be equal to 0."));
                removeAtTest.run(new CSVDocument(new CSVRow("a")), 1, 0, new CSVDocument(new CSVRow("a")), new PreConditionFailure("index (1) must be equal to 0."));
                removeAtTest.run(new CSVDocument(new CSVRow("a")), 1, 1, new CSVDocument(new CSVRow("a")), new PreConditionFailure("index (1) must be equal to 0."));
            });
        });
    }
}
