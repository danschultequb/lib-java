package qub;

public interface CharacterTableFormatTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(CharacterTableFormat.class, () ->
        {
            runner.test("singleLine", (Test test) ->
            {
                test.assertEqual(
                    CharacterTableFormat.create()
                        .setLeftBorder('[')
                        .setColumnSeparator(',')
                        .setRightBorder(']')
                        .setNewLine(','),
                    CharacterTableFormat.singleLine);
            });

            runner.test("consise", (Test test) ->
            {
                test.assertEqual(
                    CharacterTableFormat.create()
                        .setColumnSeparator(' ')
                        .setNewLine('\n'),
                    CharacterTableFormat.consise);
            });

            runner.test("create()", (Test test) ->
            {
                final CharacterTableFormat format = CharacterTableFormat.create();
                test.assertNotNull(format);
                test.assertEqual(null, format.getColumnSeparator());
                test.assertEqual(null, format.getRowSeparator());
                test.assertEqual(null, format.getNewLine());
                test.assertEqual(null, format.getTopBorder());
                test.assertEqual(null, format.getBottomBorder());
                test.assertEqual(null, format.getLeftBorder());
                test.assertEqual(null, format.getRightBorder());
            });

            runner.testGroup("setColumnSeparator(char)", () ->
            {
                final Action1<Character> setColumnSeparatorTest = (Character columnSeparator) ->
                {
                    runner.test("with " + Characters.escapeAndQuote(columnSeparator), (Test test) ->
                    {
                        final CharacterTableFormat format = CharacterTableFormat.create();
                        final CharacterTableFormat setColumnSeparatorResult = format.setColumnSeparator(columnSeparator);
                        test.assertSame(format, setColumnSeparatorResult);
                        test.assertEqual(Characters.toString(columnSeparator), format.getColumnSeparator());
                    });
                };

                setColumnSeparatorTest.run(' ');
                setColumnSeparatorTest.run('|');
                setColumnSeparatorTest.run('f');
            });

            runner.testGroup("setColumnSeparator(String)", () ->
            {
                final Action2<String,Throwable> setColumnSeparatorErrorTest = (String columnSeparator, Throwable expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(columnSeparator), (Test test) ->
                    {
                        final CharacterTableFormat format = CharacterTableFormat.create();
                        test.assertThrows(() -> format.setColumnSeparator(columnSeparator), expected);
                        test.assertEqual(null, format.getColumnSeparator());
                    });
                };

                setColumnSeparatorErrorTest.run(null, new PreConditionFailure("columnSeparator cannot be null."));

                final Action1<String> setColumnSeparatorTest = (String columnSeparator) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(columnSeparator), (Test test) ->
                    {
                        final CharacterTableFormat format = CharacterTableFormat.create();
                        final CharacterTableFormat setColumnSeparatorResult = format.setColumnSeparator(columnSeparator);
                        test.assertSame(format, setColumnSeparatorResult);
                        test.assertEqual(columnSeparator, format.getColumnSeparator());
                    });
                };


                setColumnSeparatorTest.run(" ");
                setColumnSeparatorTest.run("|");
                setColumnSeparatorTest.run("f");
                setColumnSeparatorTest.run("||");
                setColumnSeparatorTest.run("     ");
            });

            runner.testGroup("setRowSeparator(char)", () ->
            {
                final Action1<Character> setRowSeparatorTest = (Character rowSeparator) ->
                {
                    runner.test("with " + Characters.escapeAndQuote(rowSeparator), (Test test) ->
                    {
                        final CharacterTableFormat format = CharacterTableFormat.create();
                        final CharacterTableFormat setRowSeparatorResult = format.setRowSeparator(rowSeparator);
                        test.assertSame(format, setRowSeparatorResult);
                        test.assertEqual(Iterable.create(rowSeparator), format.getRowSeparator());
                    });
                };

                setRowSeparatorTest.run(' ');
                setRowSeparatorTest.run('|');
                setRowSeparatorTest.run('f');
            });

            runner.testGroup("setRowSeparator(String)", () ->
            {
                final Action2<Iterable<Character>,Throwable> setRowSeparatorErrorTest = (Iterable<Character> rowSeparator, Throwable expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(rowSeparator), (Test test) ->
                    {
                        final CharacterTableFormat format = CharacterTableFormat.create();
                        test.assertThrows(() -> format.setRowSeparator(rowSeparator), expected);
                        test.assertEqual(null, format.getRowSeparator());
                    });
                };

                setRowSeparatorErrorTest.run(null, new PreConditionFailure("rowSeparator cannot be null."));

                final Action1<Iterable<Character>> setRowSeparatorTest = (Iterable<Character> rowSeparator) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(rowSeparator), (Test test) ->
                    {
                        final CharacterTableFormat format = CharacterTableFormat.create();
                        final CharacterTableFormat setRowSeparatorResult = format.setRowSeparator(rowSeparator);
                        test.assertSame(format, setRowSeparatorResult);
                        test.assertEqual(rowSeparator, format.getRowSeparator());
                    });
                };

                setRowSeparatorTest.run(Iterable.create());
                setRowSeparatorTest.run(Iterable.create(' '));
                setRowSeparatorTest.run(Iterable.create('|'));
                setRowSeparatorTest.run(Iterable.create('f'));
                setRowSeparatorTest.run(Iterable.create('|', '|'));
                setRowSeparatorTest.run(Iterable.create(' ', ' '));
            });

            runner.testGroup("setNewLine(char)", () ->
            {
                final Action1<Character> setNewLineTest = (Character newLine) ->
                {
                    runner.test("with " + Characters.escapeAndQuote(newLine), (Test test) ->
                    {
                        final CharacterTableFormat format = CharacterTableFormat.create();
                        final CharacterTableFormat setNewLineResult = format.setNewLine(newLine);
                        test.assertSame(format, setNewLineResult);
                        test.assertEqual(Characters.toString(newLine), format.getNewLine());
                    });
                };

                setNewLineTest.run('\n');
                setNewLineTest.run('\r');
                setNewLineTest.run(' ');
                setNewLineTest.run('|');
                setNewLineTest.run('f');
            });

            runner.testGroup("setNewLine(String)", () ->
            {
                final Action2<String,Throwable> setNewLineErrorTest = (String newLine, Throwable expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(newLine), (Test test) ->
                    {
                        final CharacterTableFormat format = CharacterTableFormat.create();
                        test.assertThrows(() -> format.setNewLine(newLine), expected);
                        test.assertEqual(null, format.getNewLine());
                    });
                };

                setNewLineErrorTest.run(null, new PreConditionFailure("newLine cannot be null."));

                final Action1<String> setNewLineTest = (String newLine) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(newLine), (Test test) ->
                    {
                        final CharacterTableFormat format = CharacterTableFormat.create();
                        final CharacterTableFormat setNewLineResult = format.setNewLine(newLine);
                        test.assertSame(format, setNewLineResult);
                        test.assertEqual(newLine, format.getNewLine());
                    });
                };

                setNewLineTest.run("\n");
                setNewLineTest.run("\r");
                setNewLineTest.run("\r\n");
                setNewLineTest.run(" ");
                setNewLineTest.run("|");
                setNewLineTest.run("f");
                setNewLineTest.run("||");
                setNewLineTest.run("     ");
            });

            runner.testGroup("setLeftBorder(char)", () ->
            {
                final Action1<Character> setLeftBorderTest = (Character leftBorder) ->
                {
                    runner.test("with " + Characters.escapeAndQuote(leftBorder), (Test test) ->
                    {
                        final CharacterTableFormat format = CharacterTableFormat.create();
                        final CharacterTableFormat setLeftBorderResult = format.setLeftBorder(leftBorder);
                        test.assertSame(format, setLeftBorderResult);
                        test.assertEqual(Characters.toString(leftBorder), format.getLeftBorder());
                    });
                };

                setLeftBorderTest.run('\n');
                setLeftBorderTest.run('\r');
                setLeftBorderTest.run(' ');
                setLeftBorderTest.run('|');
                setLeftBorderTest.run('f');
            });

            runner.testGroup("setLeftBorder(String)", () ->
            {
                final Action2<String,Throwable> setLeftBorderErrorTest = (String leftBorder, Throwable expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(leftBorder), (Test test) ->
                    {
                        final CharacterTableFormat format = CharacterTableFormat.create();
                        test.assertThrows(() -> format.setLeftBorder(leftBorder), expected);
                        test.assertEqual(null, format.getLeftBorder());
                    });
                };

                setLeftBorderErrorTest.run(null, new PreConditionFailure("leftBorder cannot be null."));

                final Action1<String> setLeftBorderTest = (String leftBorder) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(leftBorder), (Test test) ->
                    {
                        final CharacterTableFormat format = CharacterTableFormat.create();
                        final CharacterTableFormat setLeftBorderResult = format.setLeftBorder(leftBorder);
                        test.assertSame(format, setLeftBorderResult);
                        test.assertEqual(leftBorder, format.getLeftBorder());
                    });
                };

                setLeftBorderTest.run("\n");
                setLeftBorderTest.run("\r");
                setLeftBorderTest.run("\r\n");
                setLeftBorderTest.run(" ");
                setLeftBorderTest.run("|");
                setLeftBorderTest.run("f");
                setLeftBorderTest.run("||");
                setLeftBorderTest.run("     ");
            });

            runner.testGroup("setRightBorder(char)", () ->
            {
                final Action1<Character> setRightBorderTest = (Character rightBorder) ->
                {
                    runner.test("with " + Characters.escapeAndQuote(rightBorder), (Test test) ->
                    {
                        final CharacterTableFormat format = CharacterTableFormat.create();
                        final CharacterTableFormat setRightBorderResult = format.setRightBorder(rightBorder);
                        test.assertSame(format, setRightBorderResult);
                        test.assertEqual(Characters.toString(rightBorder), format.getRightBorder());
                    });
                };

                setRightBorderTest.run('\n');
                setRightBorderTest.run('\r');
                setRightBorderTest.run(' ');
                setRightBorderTest.run('|');
                setRightBorderTest.run('f');
            });

            runner.testGroup("setRightBorder(String)", () ->
            {
                final Action2<String,Throwable> setRightBorderErrorTest = (String rightBorder, Throwable expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(rightBorder), (Test test) ->
                    {
                        final CharacterTableFormat format = CharacterTableFormat.create();
                        test.assertThrows(() -> format.setRightBorder(rightBorder), expected);
                        test.assertEqual(null, format.getRightBorder());
                    });
                };

                setRightBorderErrorTest.run(null, new PreConditionFailure("rightBorder cannot be null."));

                final Action1<String> setRightBorderTest = (String rightBorder) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(rightBorder), (Test test) ->
                    {
                        final CharacterTableFormat format = CharacterTableFormat.create();
                        final CharacterTableFormat setRightBorderResult = format.setRightBorder(rightBorder);
                        test.assertSame(format, setRightBorderResult);
                        test.assertEqual(rightBorder, format.getRightBorder());
                    });
                };

                setRightBorderTest.run("\n");
                setRightBorderTest.run("\r");
                setRightBorderTest.run("\r\n");
                setRightBorderTest.run(" ");
                setRightBorderTest.run("|");
                setRightBorderTest.run("f");
                setRightBorderTest.run("||");
                setRightBorderTest.run("     ");
            });

            runner.testGroup("setTopBorder(char)", () ->
            {
                final Action1<Character> setTopBorderTest = (Character topBorder) ->
                {
                    runner.test("with " + Characters.escapeAndQuote(topBorder), (Test test) ->
                    {
                        final CharacterTableFormat format = CharacterTableFormat.create();
                        final CharacterTableFormat setTopBorderResult = format.setTopBorder(topBorder);
                        test.assertSame(format, setTopBorderResult);
                        test.assertEqual(Iterable.create(topBorder), format.getTopBorder());
                    });
                };

                setTopBorderTest.run('\n');
                setTopBorderTest.run('\r');
                setTopBorderTest.run(' ');
                setTopBorderTest.run('|');
                setTopBorderTest.run('f');
            });

            runner.testGroup("setTopBorder(Iterable<Character>)", () ->
            {
                final Action2<Iterable<Character>,Throwable> setTopBorderErrorTest = (Iterable<Character> topBorder, Throwable expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(topBorder), (Test test) ->
                    {
                        final CharacterTableFormat format = CharacterTableFormat.create();
                        test.assertThrows(() -> format.setTopBorder(topBorder), expected);
                        test.assertEqual(null, format.getTopBorder());
                    });
                };

                setTopBorderErrorTest.run(null, new PreConditionFailure("topBorder cannot be null."));

                final Action1<Iterable<Character>> setTopBorderTest = (Iterable<Character> topBorder) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(topBorder), (Test test) ->
                    {
                        final CharacterTableFormat format = CharacterTableFormat.create();
                        final CharacterTableFormat setTopBorderResult = format.setTopBorder(topBorder);
                        test.assertSame(format, setTopBorderResult);
                        test.assertEqual(topBorder, format.getTopBorder());
                    });
                };

                setTopBorderTest.run(Iterable.create('\n'));
                setTopBorderTest.run(Iterable.create('\r'));
                setTopBorderTest.run(Iterable.create('\r', '\n'));
                setTopBorderTest.run(Iterable.create(' '));
                setTopBorderTest.run(Iterable.create('|'));
                setTopBorderTest.run(Iterable.create('f'));
                setTopBorderTest.run(Iterable.create('|', '|'));
                setTopBorderTest.run(Iterable.create(' ', ' ', ' ', ' '));
            });

            runner.testGroup("setBottomBorder(char)", () ->
            {
                final Action1<Character> setBottomBorderTest = (Character bottomBorder) ->
                {
                    runner.test("with " + Characters.escapeAndQuote(bottomBorder), (Test test) ->
                    {
                        final CharacterTableFormat format = CharacterTableFormat.create();
                        final CharacterTableFormat setBottomBorderResult = format.setBottomBorder(bottomBorder);
                        test.assertSame(format, setBottomBorderResult);
                        test.assertEqual(Iterable.create(bottomBorder), format.getBottomBorder());
                    });
                };

                setBottomBorderTest.run('\n');
                setBottomBorderTest.run('\r');
                setBottomBorderTest.run(' ');
                setBottomBorderTest.run('|');
                setBottomBorderTest.run('f');
            });

            runner.testGroup("setBottomBorder(Iterable<Character>)", () ->
            {
                final Action2<Iterable<Character>,Throwable> setBottomBorderErrorTest = (Iterable<Character> bottomBorder, Throwable expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(bottomBorder), (Test test) ->
                    {
                        final CharacterTableFormat format = CharacterTableFormat.create();
                        test.assertThrows(() -> format.setBottomBorder(bottomBorder), expected);
                        test.assertEqual(null, format.getBottomBorder());
                    });
                };

                setBottomBorderErrorTest.run(null, new PreConditionFailure("bottomBorder cannot be null."));

                final Action1<Iterable<Character>> setBottomBorderTest = (Iterable<Character> bottomBorder) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(bottomBorder), (Test test) ->
                    {
                        final CharacterTableFormat format = CharacterTableFormat.create();
                        final CharacterTableFormat setBottomBorderResult = format.setBottomBorder(bottomBorder);
                        test.assertSame(format, setBottomBorderResult);
                        test.assertEqual(bottomBorder, format.getBottomBorder());
                    });
                };

                setBottomBorderTest.run(Iterable.create('\n'));
                setBottomBorderTest.run(Iterable.create('\r'));
                setBottomBorderTest.run(Iterable.create('\r', '\n'));
                setBottomBorderTest.run(Iterable.create(' '));
                setBottomBorderTest.run(Iterable.create('|'));
                setBottomBorderTest.run(Iterable.create('f'));
                setBottomBorderTest.run(Iterable.create('|', '|'));
                setBottomBorderTest.run(Iterable.create(' ', ' ', ' ', ' '));
            });

            runner.testGroup("setColumnHorizontalAlignment(int,HorizontalAlignment)", () ->
            {
                final Action3<Integer,HorizontalAlignment,Throwable> setColumnHorizontalAlignmentErrorTest = (Integer columnIndex, HorizontalAlignment horizontalAlignment, Throwable expected) ->
                {
                    runner.test("with " + English.andList(columnIndex, horizontalAlignment), (Test test) ->
                    {
                        final CharacterTableFormat format = CharacterTableFormat.create();
                        test.assertThrows(() -> format.setColumnHorizontalAlignment(columnIndex, horizontalAlignment), expected);
                    });
                };

                setColumnHorizontalAlignmentErrorTest.run(-1, HorizontalAlignment.Left,
                    new PreConditionFailure("columnIndex (-1) must be greater than or equal to 0."));
                setColumnHorizontalAlignmentErrorTest.run(0, null,
                    new PreConditionFailure("horizontalAlignment cannot be null."));

                final Action2<Integer,HorizontalAlignment> setColumnHorizontalAlignmentTest = (Integer columnIndex, HorizontalAlignment horizontalAlignment) ->
                {
                    runner.test("with " + English.andList(columnIndex, horizontalAlignment), (Test test) ->
                    {
                        final CharacterTableFormat format = CharacterTableFormat.create();
                        final CharacterTableFormat setColumnHorizontalAlignmentResult = format.setColumnHorizontalAlignment(columnIndex, horizontalAlignment);
                        test.assertSame(format, setColumnHorizontalAlignmentResult);
                        test.assertEqual(horizontalAlignment, format.getColumnHorizontalAlignment(columnIndex).await());
                    });
                };

                setColumnHorizontalAlignmentTest.run(0, HorizontalAlignment.Left);
                setColumnHorizontalAlignmentTest.run(0, HorizontalAlignment.Center);
                setColumnHorizontalAlignmentTest.run(0, HorizontalAlignment.Right);
                setColumnHorizontalAlignmentTest.run(1, HorizontalAlignment.Left);
                setColumnHorizontalAlignmentTest.run(2, HorizontalAlignment.Center);

                runner.test("with columnIndex that already has alignment", (Test test) ->
                {
                    final CharacterTableFormat format = CharacterTableFormat.create();

                    format.setColumnHorizontalAlignment(0, HorizontalAlignment.Left);
                    test.assertEqual(HorizontalAlignment.Left, format.getColumnHorizontalAlignment(0).await());

                    format.setColumnHorizontalAlignment(0, HorizontalAlignment.Right);
                    test.assertEqual(HorizontalAlignment.Right, format.getColumnHorizontalAlignment(0).await());
                });
            });

            runner.testGroup("getColumnHorizontalAlignment(int)", () ->
            {
                runner.test("when no alignments have been set", (Test test) ->
                {
                    final CharacterTableFormat format = CharacterTableFormat.create();
                    test.assertThrows(() -> format.getColumnHorizontalAlignment(0).await(),
                        new NotFoundException("No horizontal alignment assigned to the column at index 0."));
                });

                runner.test("when a different column has had its alignment set", (Test test) ->
                {
                    final CharacterTableFormat format = CharacterTableFormat.create()
                        .setColumnHorizontalAlignment(1, HorizontalAlignment.Center);
                    test.assertThrows(() -> format.getColumnHorizontalAlignment(0).await(),
                        new NotFoundException("No horizontal alignment assigned to the column at index 0."));
                });

                runner.test("when an alignment exists for the requested column", (Test test) ->
                {
                    final CharacterTableFormat format = CharacterTableFormat.create()
                        .setColumnHorizontalAlignment(1, HorizontalAlignment.Center);
                    test.assertEqual(HorizontalAlignment.Center, format.getColumnHorizontalAlignment(1).await());
                });
            });

            runner.testGroup("padCell(int,String,int)", () ->
            {
                final Action4<Integer,String,Integer,Throwable> padCellErrorTest = (Integer columnIndex, String text, Integer columnWidth, Throwable expected) ->
                {
                    runner.test("with " + English.andList(columnIndex, text, columnWidth), (Test test) ->
                    {
                        final CharacterTableFormat format = CharacterTableFormat.create();
                        test.assertThrows(() -> format.padCell(columnIndex, text, columnWidth), expected);
                    });
                };

                padCellErrorTest.run(-1, "", 5, new PreConditionFailure("columnIndex (-1) must be greater than or equal to 0."));
                padCellErrorTest.run(0, null, 5, new PreConditionFailure("cellText cannot be null."));
                padCellErrorTest.run(0, "hello", -1, new PreConditionFailure("columnWidth (-1) must be greater than or equal to 0."));

                final Action5<HorizontalAlignment,Integer,String,Integer,String> padCellTest = (HorizontalAlignment columnHorizontalAlignment, Integer columnIndex, String cellText, Integer columnWidth, String expected) ->
                {
                    runner.test("with " + English.andList(columnHorizontalAlignment, columnIndex, cellText, columnWidth), (Test test) ->
                    {
                        final CharacterTableFormat format = CharacterTableFormat.create();
                        if (columnHorizontalAlignment != null)
                        {
                            format.setColumnHorizontalAlignment(columnIndex, columnHorizontalAlignment);
                        }
                        test.assertEqual(expected, format.padCell(columnIndex, cellText, columnWidth));
                    });
                };

                padCellTest.run(null, 0, "", 0, "");
                padCellTest.run(HorizontalAlignment.Left, 0, "", 0, "");
                padCellTest.run(HorizontalAlignment.Center, 0, "", 0, "");
                padCellTest.run(HorizontalAlignment.Right, 0, "", 0, "");

                padCellTest.run(null, 1, "abc", 0, "abc");
                padCellTest.run(HorizontalAlignment.Left, 1, "abc", 0, "abc");
                padCellTest.run(HorizontalAlignment.Center, 1, "abc", 0, "abc");
                padCellTest.run(HorizontalAlignment.Right, 1, "abc", 0, "abc");

                padCellTest.run(null, 2, "abc", 3, "abc");
                padCellTest.run(HorizontalAlignment.Left, 2, "abc", 3, "abc");
                padCellTest.run(HorizontalAlignment.Center, 2, "abc", 3, "abc");
                padCellTest.run(HorizontalAlignment.Right, 2, "abc", 3, "abc");

                padCellTest.run(null, 3, "abc", 4, "abc ");
                padCellTest.run(HorizontalAlignment.Left, 3, "abc", 4, "abc ");
                padCellTest.run(HorizontalAlignment.Center, 3, "abc", 4, "abc ");
                padCellTest.run(HorizontalAlignment.Right, 3, "abc", 4, " abc");

                padCellTest.run(null, 4, "abc", 5, "abc  ");
                padCellTest.run(HorizontalAlignment.Left, 4, "abc", 5, "abc  ");
                padCellTest.run(HorizontalAlignment.Center, 4, "abc", 5, " abc ");
                padCellTest.run(HorizontalAlignment.Right, 4, "abc", 5, "  abc");

                padCellTest.run(null, 5, "abc", 6, "abc   ");
                padCellTest.run(HorizontalAlignment.Left, 5, "abc", 6, "abc   ");
                padCellTest.run(HorizontalAlignment.Center, 5, "abc", 6, " abc  ");
                padCellTest.run(HorizontalAlignment.Right, 5, "abc", 6, "   abc");

                padCellTest.run(null, 6, "abc", 7, "abc    ");
                padCellTest.run(HorizontalAlignment.Left, 6, "abc", 7, "abc    ");
                padCellTest.run(HorizontalAlignment.Center, 6, "abc", 7, "  abc  ");
                padCellTest.run(HorizontalAlignment.Right, 6, "abc", 7, "    abc");
            });

            runner.testGroup("toString()", () ->
            {
                runner.test("singleLine", (Test test) ->
                {
                    test.assertEqual(
                        "{\"columnSeparator\":\",\",\"newLine\":\",\",\"leftBorder\":\"[\",\"rightBorder\":\"]\"}",
                        CharacterTableFormat.singleLine.toString());
                });

                runner.test("consise", (Test test) ->
                {
                    test.assertEqual(
                        "{\"columnSeparator\":\" \",\"newLine\":\"\\n\"}",
                        CharacterTableFormat.consise.toString());
                });

                runner.test("no properties", (Test test) ->
                {
                    test.assertEqual(
                        "{}",
                        CharacterTableFormat.create().toString());
                });

                runner.test("with empty property", (Test test) ->
                {
                    test.assertEqual(
                        "{}",
                        CharacterTableFormat.create().setColumnSeparator("").toString());
                });

                runner.test("with one column horizontal alignment", (Test test) ->
                {
                    test.assertEqual(
                        "{\"columnHorizontalAlignment\":{\"6\":\"Right\"}}",
                        CharacterTableFormat.create().setColumnHorizontalAlignment(6, HorizontalAlignment.Right).toString());
                });

                runner.test("with multiple column horizontal alignments", (Test test) ->
                {
                    test.assertEqual(
                        "{\"columnHorizontalAlignment\":{\"4\":\"Center\",\"6\":\"Right\"}}",
                        CharacterTableFormat.create()
                            .setColumnHorizontalAlignment(6, HorizontalAlignment.Right)
                            .setColumnHorizontalAlignment(4, HorizontalAlignment.Center)
                            .toString());
                });
            });

            runner.testGroup("equals(Object)", () ->
            {
                final Action3<CharacterTableFormat,Object,Boolean> equalsTest = (CharacterTableFormat format, Object rhs, Boolean expected) ->
                {
                    runner.test("with " + English.andList(format, rhs), (Test test) ->
                    {
                        test.assertEqual(expected, format.equals(rhs));
                    });
                };

                equalsTest.run(CharacterTableFormat.create(), null, false);
                equalsTest.run(CharacterTableFormat.create(), "hello", false);
                equalsTest.run(CharacterTableFormat.create(), CharacterTableFormat.create(), true);
                equalsTest.run(
                    CharacterTableFormat.create().setColumnSeparator('a'),
                    CharacterTableFormat.create(),
                    false);
                equalsTest.run(
                    CharacterTableFormat.create(),
                    CharacterTableFormat.create().setColumnSeparator('a'),
                    false);
                equalsTest.run(
                    CharacterTableFormat.create().setColumnSeparator('a'),
                    CharacterTableFormat.create().setColumnSeparator('b'),
                    false);
                equalsTest.run(
                    CharacterTableFormat.create().setColumnSeparator('a'),
                    CharacterTableFormat.create().setColumnSeparator('a'),
                    true);
                equalsTest.run(
                    CharacterTableFormat.create().setRowSeparator('a'),
                    CharacterTableFormat.create(),
                    false);
                equalsTest.run(
                    CharacterTableFormat.create(),
                    CharacterTableFormat.create().setRowSeparator('a'),
                    false);
                equalsTest.run(
                    CharacterTableFormat.create().setRowSeparator('a'),
                    CharacterTableFormat.create().setRowSeparator('b'),
                    false);
                equalsTest.run(
                    CharacterTableFormat.create().setRowSeparator('a'),
                    CharacterTableFormat.create().setRowSeparator('a'),
                    true);
                equalsTest.run(
                    CharacterTableFormat.create().setNewLine('a'),
                    CharacterTableFormat.create(),
                    false);
                equalsTest.run(
                    CharacterTableFormat.create(),
                    CharacterTableFormat.create().setNewLine('a'),
                    false);
                equalsTest.run(
                    CharacterTableFormat.create().setNewLine('a'),
                    CharacterTableFormat.create().setNewLine('b'),
                    false);
                equalsTest.run(
                    CharacterTableFormat.create().setNewLine('a'),
                    CharacterTableFormat.create().setNewLine('a'),
                    true);
                equalsTest.run(
                    CharacterTableFormat.create().setLeftBorder('a'),
                    CharacterTableFormat.create(),
                    false);
                equalsTest.run(
                    CharacterTableFormat.create(),
                    CharacterTableFormat.create().setLeftBorder('a'),
                    false);
                equalsTest.run(
                    CharacterTableFormat.create().setLeftBorder('a'),
                    CharacterTableFormat.create().setLeftBorder('b'),
                    false);
                equalsTest.run(
                    CharacterTableFormat.create().setLeftBorder('a'),
                    CharacterTableFormat.create().setLeftBorder('a'),
                    true);
                equalsTest.run(
                    CharacterTableFormat.create().setRightBorder('a'),
                    CharacterTableFormat.create(),
                    false);
                equalsTest.run(
                    CharacterTableFormat.create(),
                    CharacterTableFormat.create().setRightBorder('a'),
                    false);
                equalsTest.run(
                    CharacterTableFormat.create().setRightBorder('a'),
                    CharacterTableFormat.create().setRightBorder('b'),
                    false);
                equalsTest.run(
                    CharacterTableFormat.create().setRightBorder('a'),
                    CharacterTableFormat.create().setRightBorder('a'),
                    true);
                equalsTest.run(
                    CharacterTableFormat.create().setTopBorder('a'),
                    CharacterTableFormat.create(),
                    false);
                equalsTest.run(
                    CharacterTableFormat.create(),
                    CharacterTableFormat.create().setTopBorder('a'),
                    false);
                equalsTest.run(
                    CharacterTableFormat.create().setTopBorder('a'),
                    CharacterTableFormat.create().setTopBorder('b'),
                    false);
                equalsTest.run(
                    CharacterTableFormat.create().setTopBorder('a'),
                    CharacterTableFormat.create().setTopBorder('a'),
                    true);
                equalsTest.run(
                    CharacterTableFormat.create().setBottomBorder('a'),
                    CharacterTableFormat.create(),
                    false);
                equalsTest.run(
                    CharacterTableFormat.create(),
                    CharacterTableFormat.create().setBottomBorder('a'),
                    false);
                equalsTest.run(
                    CharacterTableFormat.create().setBottomBorder('a'),
                    CharacterTableFormat.create().setBottomBorder('b'),
                    false);
                equalsTest.run(
                    CharacterTableFormat.create().setBottomBorder('a'),
                    CharacterTableFormat.create().setBottomBorder('a'),
                    true);
                equalsTest.run(
                    CharacterTableFormat.create().setColumnHorizontalAlignment(0, HorizontalAlignment.Center),
                    CharacterTableFormat.create(),
                    false);
                equalsTest.run(
                    CharacterTableFormat.create(),
                    CharacterTableFormat.create().setColumnHorizontalAlignment(0, HorizontalAlignment.Center),
                    false);
                equalsTest.run(
                    CharacterTableFormat.create().setColumnHorizontalAlignment(0, HorizontalAlignment.Left),
                    CharacterTableFormat.create().setColumnHorizontalAlignment(0, HorizontalAlignment.Center),
                    false);
                equalsTest.run(
                    CharacterTableFormat.create().setColumnHorizontalAlignment(0, HorizontalAlignment.Center),
                    CharacterTableFormat.create().setColumnHorizontalAlignment(1, HorizontalAlignment.Center),
                    false);
                equalsTest.run(
                    CharacterTableFormat.create().setColumnHorizontalAlignment(0, HorizontalAlignment.Center),
                    CharacterTableFormat.create().setColumnHorizontalAlignment(0, HorizontalAlignment.Center),
                    true);
            });

            runner.testGroup("equals(CharacterTableFormat)", () ->
            {
                final Action3<CharacterTableFormat,CharacterTableFormat,Boolean> equalsTest = (CharacterTableFormat format, CharacterTableFormat rhs, Boolean expected) ->
                {
                    runner.test("with " + English.andList(format, rhs), (Test test) ->
                    {
                        test.assertEqual(expected, format.equals(rhs));
                    });
                };

                equalsTest.run(CharacterTableFormat.create(), null, false);
                equalsTest.run(CharacterTableFormat.create(), CharacterTableFormat.create(), true);
                equalsTest.run(
                    CharacterTableFormat.create().setColumnSeparator('a'),
                    CharacterTableFormat.create(),
                    false);
                equalsTest.run(
                    CharacterTableFormat.create(),
                    CharacterTableFormat.create().setColumnSeparator('a'),
                    false);
                equalsTest.run(
                    CharacterTableFormat.create().setColumnSeparator('a'),
                    CharacterTableFormat.create().setColumnSeparator('b'),
                    false);
                equalsTest.run(
                    CharacterTableFormat.create().setColumnSeparator('a'),
                    CharacterTableFormat.create().setColumnSeparator('a'),
                    true);
                equalsTest.run(
                    CharacterTableFormat.create().setRowSeparator('a'),
                    CharacterTableFormat.create(),
                    false);
                equalsTest.run(
                    CharacterTableFormat.create(),
                    CharacterTableFormat.create().setRowSeparator('a'),
                    false);
                equalsTest.run(
                    CharacterTableFormat.create().setRowSeparator('a'),
                    CharacterTableFormat.create().setRowSeparator('b'),
                    false);
                equalsTest.run(
                    CharacterTableFormat.create().setRowSeparator('a'),
                    CharacterTableFormat.create().setRowSeparator('a'),
                    true);
                equalsTest.run(
                    CharacterTableFormat.create().setNewLine('a'),
                    CharacterTableFormat.create(),
                    false);
                equalsTest.run(
                    CharacterTableFormat.create(),
                    CharacterTableFormat.create().setNewLine('a'),
                    false);
                equalsTest.run(
                    CharacterTableFormat.create().setNewLine('a'),
                    CharacterTableFormat.create().setNewLine('b'),
                    false);
                equalsTest.run(
                    CharacterTableFormat.create().setNewLine('a'),
                    CharacterTableFormat.create().setNewLine('a'),
                    true);
                equalsTest.run(
                    CharacterTableFormat.create().setLeftBorder('a'),
                    CharacterTableFormat.create(),
                    false);
                equalsTest.run(
                    CharacterTableFormat.create(),
                    CharacterTableFormat.create().setLeftBorder('a'),
                    false);
                equalsTest.run(
                    CharacterTableFormat.create().setLeftBorder('a'),
                    CharacterTableFormat.create().setLeftBorder('b'),
                    false);
                equalsTest.run(
                    CharacterTableFormat.create().setLeftBorder('a'),
                    CharacterTableFormat.create().setLeftBorder('a'),
                    true);
                equalsTest.run(
                    CharacterTableFormat.create().setRightBorder('a'),
                    CharacterTableFormat.create(),
                    false);
                equalsTest.run(
                    CharacterTableFormat.create(),
                    CharacterTableFormat.create().setRightBorder('a'),
                    false);
                equalsTest.run(
                    CharacterTableFormat.create().setRightBorder('a'),
                    CharacterTableFormat.create().setRightBorder('b'),
                    false);
                equalsTest.run(
                    CharacterTableFormat.create().setRightBorder('a'),
                    CharacterTableFormat.create().setRightBorder('a'),
                    true);
                equalsTest.run(
                    CharacterTableFormat.create().setTopBorder('a'),
                    CharacterTableFormat.create(),
                    false);
                equalsTest.run(
                    CharacterTableFormat.create(),
                    CharacterTableFormat.create().setTopBorder('a'),
                    false);
                equalsTest.run(
                    CharacterTableFormat.create().setTopBorder('a'),
                    CharacterTableFormat.create().setTopBorder('b'),
                    false);
                equalsTest.run(
                    CharacterTableFormat.create().setTopBorder('a'),
                    CharacterTableFormat.create().setTopBorder('a'),
                    true);
                equalsTest.run(
                    CharacterTableFormat.create().setBottomBorder('a'),
                    CharacterTableFormat.create(),
                    false);
                equalsTest.run(
                    CharacterTableFormat.create(),
                    CharacterTableFormat.create().setBottomBorder('a'),
                    false);
                equalsTest.run(
                    CharacterTableFormat.create().setBottomBorder('a'),
                    CharacterTableFormat.create().setBottomBorder('b'),
                    false);
                equalsTest.run(
                    CharacterTableFormat.create().setBottomBorder('a'),
                    CharacterTableFormat.create().setBottomBorder('a'),
                    true);
            });
        });
    }
}
