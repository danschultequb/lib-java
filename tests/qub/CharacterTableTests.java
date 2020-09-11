package qub;

public interface CharacterTableTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(CharacterTable.class, () ->
        {
            runner.test("create()", (Test test) ->
            {
                final CharacterTable table = CharacterTable.create();
                test.assertNotNull(table);
                test.assertEqual(Iterable.create(), table.getRows());
            });

            runner.testGroup("addRow(String...)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final CharacterTable table = CharacterTable.create();
                    test.assertThrows(() -> table.addRow((String[])null),
                        new PreConditionFailure("rowCells cannot be null."));
                    test.assertEqual(Iterable.create(), table.getRows());
                });

                runner.test("with no arguments", (Test test) ->
                {
                    final CharacterTable table = CharacterTable.create();
                    final CharacterTable addRowResult = table.addRow();
                    test.assertSame(table, addRowResult);
                    test.assertEqual(
                        Iterable.create(
                            Iterable.create()),
                        table.getRows());
                });

                runner.test("with one argument", (Test test) ->
                {
                    final CharacterTable table = CharacterTable.create();
                    final CharacterTable addRowResult = table.addRow("a");
                    test.assertSame(table, addRowResult);
                    test.assertEqual(
                        Iterable.create(
                            Iterable.create("a")),
                        table.getRows());
                });

                runner.test("with multiple arguments", (Test test) ->
                {
                    final CharacterTable table = CharacterTable.create();
                    final CharacterTable addRowResult = table.addRow("a", "b", "c");
                    test.assertSame(table, addRowResult);
                    test.assertEqual(
                        Iterable.create(
                            Iterable.create("a", "b", "c")),
                        table.getRows());
                });

                runner.test("with empty array", (Test test) ->
                {
                    final CharacterTable table = CharacterTable.create();
                    final CharacterTable addRowResult = table.addRow(new String[0]);
                    test.assertSame(table, addRowResult);
                    test.assertEqual(
                        Iterable.create(
                            Iterable.create()),
                        table.getRows());
                });

                runner.test("with one-element array", (Test test) ->
                {
                    final CharacterTable table = CharacterTable.create();
                    final CharacterTable addRowResult = table.addRow(new String[] { "a" });
                    test.assertSame(table, addRowResult);
                    test.assertEqual(
                        Iterable.create(
                            Iterable.create("a")),
                        table.getRows());
                });

                runner.test("with multiple-element array", (Test test) ->
                {
                    final CharacterTable table = CharacterTable.create();
                    final CharacterTable addRowResult = table.addRow(new String[] { "a", "b", "c" });
                    test.assertSame(table, addRowResult);
                    test.assertEqual(
                        Iterable.create(
                            Iterable.create("a", "b", "c")),
                        table.getRows());
                });
            });

            runner.testGroup("addRow(Iterable<String>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final CharacterTable table = CharacterTable.create();
                    test.assertThrows(() -> table.addRow((Iterable<String>)null),
                        new PreConditionFailure("rowCells cannot be null."));
                    test.assertEqual(Iterable.create(), table.getRows());
                });

                runner.test("with empty", (Test test) ->
                {
                    final CharacterTable table = CharacterTable.create();
                    final CharacterTable addRowResult = table.addRow(Iterable.create());
                    test.assertSame(table, addRowResult);
                    test.assertEqual(
                        Iterable.create(
                            Iterable.create()),
                        table.getRows());
                });

                runner.test("with one-element array", (Test test) ->
                {
                    final CharacterTable table = CharacterTable.create();
                    final CharacterTable addRowResult = table.addRow(Iterable.create("a"));
                    test.assertSame(table, addRowResult);
                    test.assertEqual(
                        Iterable.create(
                            Iterable.create("a")),
                        table.getRows());
                });

                runner.test("with multiple-element array", (Test test) ->
                {
                    final CharacterTable table = CharacterTable.create();
                    final CharacterTable addRowResult = table.addRow(Iterable.create("a", "b", "c"));
                    test.assertSame(table, addRowResult);
                    test.assertEqual(
                        Iterable.create(
                            Iterable.create("a", "b", "c")),
                        table.getRows());
                });
            });

            runner.testGroup("toString()", () ->
            {
                final Action2<CharacterTable,String> toStringTest = (CharacterTable table, String expected) ->
                {
                    runner.test("with " + table, (Test test) ->
                    {
                        test.assertEqual(expected, table.toString());
                    });
                };

                toStringTest.run(CharacterTable.create(), "");
                toStringTest.run(
                    CharacterTable.create()
                        .addRow(),
                    "[]");
                toStringTest.run(
                    CharacterTable.create()
                        .addRow("a"),
                    "[a]");
                toStringTest.run(
                    CharacterTable.create()
                        .addRow("a", "b"),
                    "[a,b]");
                toStringTest.run(
                    CharacterTable.create()
                        .addRow("a", "b", "c"),
                    "[a,b,c]");
                toStringTest.run(
                    CharacterTable.create()
                        .addRow("a")
                        .addRow("b"),
                    "[a],[b]");
                toStringTest.run(
                    CharacterTable.create()
                        .addRow("a")
                        .addRow("b")
                        .addRow("c"),
                    "[a],[b],[c]");
                toStringTest.run(
                    CharacterTable.create()
                        .addRow("a", "b", "c")
                        .addRow("d", "e", "f")
                        .addRow("g", "h", "i"),
                    "[a,b,c],[d,e,f],[g,h,i]");
                toStringTest.run(
                    CharacterTable.create()
                        .addRow("apples", "$5.00", "100")
                        .addRow("doo-dads", "$30.99", "5")
                        .addRow("glow-buds", "$3000.00", "17"),
                    "[apples   ,$5.00   ,100],[doo-dads ,$30.99  ,5  ],[glow-buds,$3000.00,17 ]");
            });

            runner.testGroup("toString(CharacterTableFormat)", () ->
            {
                final Action3<CharacterTable,CharacterTableFormat,Throwable> toStringErrorTest = (CharacterTable table, CharacterTableFormat format, Throwable expected) ->
                {
                    runner.test("with " + English.andList(table, format), (Test test) ->
                    {
                        test.assertThrows(() -> table.toString(format), expected);
                    });
                };

                toStringErrorTest.run(CharacterTable.create(), null, new PreConditionFailure("format cannot be null."));

                final Action3<CharacterTable,CharacterTableFormat,String> toStringTest = (CharacterTable table, CharacterTableFormat format, String expected) ->
                {
                    runner.test("with " + English.andList(table, format), (Test test) ->
                    {
                        test.assertEqual(expected, table.toString(format));
                    });
                };

                toStringTest.run(
                    CharacterTable.create(),
                    CharacterTableFormat.create(),
                    "");
                toStringTest.run(
                    CharacterTable.create()
                        .addRow("a"),
                    CharacterTableFormat.create(),
                    "a");
                toStringTest.run(
                    CharacterTable.create()
                        .addRow("a", "b", "c"),
                    CharacterTableFormat.create(),
                    "abc");
                toStringTest.run(
                    CharacterTable.create()
                        .addRow("a")
                        .addRow("b", "c")
                        .addRow("d", "e", "f"),
                    CharacterTableFormat.create(),
                    "a  bc def");

                toStringTest.run(
                    CharacterTable.create(),
                    CharacterTableFormat.consise,
                    "");
                toStringTest.run(
                    CharacterTable.create()
                        .addRow("a"),
                    CharacterTableFormat.consise,
                    "a");
                toStringTest.run(
                    CharacterTable.create()
                        .addRow("a", "b", "c"),
                    CharacterTableFormat.consise,
                    "a b c");
                toStringTest.run(
                    CharacterTable.create()
                        .addRow("a")
                        .addRow("b", "c")
                        .addRow("d", "e", "f"),
                    CharacterTableFormat.consise,
                    "a    \nb c  \nd e f");
                toStringTest.run(
                    CharacterTable.create()
                        .addRow("Tests run:", "500")
                        .addRow("Tests failed:", "3")
                        .addRow("Tests duration:", "5 Seconds"),
                    CharacterTableFormat.consise,
                    Strings.join('\n', Iterable.create(
                        "Tests run:      500      ",
                        "Tests failed:   3        ",
                        "Tests duration: 5 Seconds")));
                toStringTest.run(
                    CharacterTable.create()
                        .addRow("Tests run:", "500")
                        .addRow("Tests failed:", "3")
                        .addRow("Tests duration:", "5", "Seconds"),
                    CharacterTableFormat.consise,
                    Strings.join('\n', Iterable.create(
                        "Tests run:      500        ",
                        "Tests failed:   3          ",
                        "Tests duration: 5   Seconds")));

                toStringTest.run(
                    CharacterTable.create(),
                    CharacterTableFormat.create()
                        .setLeftBorder('|')
                        .setColumnSeparator('|')
                        .setRightBorder('|')
                        .setNewLine('\n'),
                    Strings.join('\n', Iterable.create(
                        "")));

                toStringTest.run(
                    CharacterTable.create()
                        .addRow("a"),
                    CharacterTableFormat.create()
                        .setLeftBorder('|')
                        .setColumnSeparator('|')
                        .setRightBorder('|')
                        .setNewLine('\n'),
                    Strings.join('\n', Iterable.create(
                        "|a|")));

                toStringTest.run(
                    CharacterTable.create()
                        .addRow("a", "b", "c"),
                    CharacterTableFormat.create()
                        .setLeftBorder('|')
                        .setColumnSeparator('|')
                        .setRightBorder('|')
                        .setNewLine('\n'),
                    Strings.join('\n', Iterable.create(
                        "|a|b|c|")));

                toStringTest.run(
                    CharacterTable.create()
                        .addRow("a")
                        .addRow("b", "c")
                        .addRow("d", "e", "f"),
                    CharacterTableFormat.create()
                        .setLeftBorder('|')
                        .setColumnSeparator('|')
                        .setRightBorder('|')
                        .setNewLine('\n'),
                    Strings.join('\n', Iterable.create(
                        "|a| | |",
                        "|b|c| |",
                        "|d|e|f|")));

                toStringTest.run(
                    CharacterTable.create()
                        .addRow("a")
                        .addRow("b", "c")
                        .addRow("d", "e", "f"),
                    CharacterTableFormat.create()
                        .setLeftBorder("xox")
                        .setColumnSeparator('|')
                        .setRightBorder("xox")
                        .setNewLine('\n'),
                    Strings.join('\n', Iterable.create(
                        "xoxa| | xox",
                        "xoxb|c| xox",
                        "xoxd|e|fxox")));

                toStringTest.run(
                    CharacterTable.create()
                        .addRow("a")
                        .addRow("b", "c")
                        .addRow("d", "e", "f"),
                    CharacterTableFormat.create()
                        .setLeftBorder('|')
                        .setColumnSeparator("||")
                        .setRightBorder('|')
                        .setNewLine('\n'),
                    Strings.join('\n', Iterable.create(
                        "|a|| || |",
                        "|b||c|| |",
                        "|d||e||f|")));

                toStringTest.run(
                    CharacterTable.create()
                        .addRow("a")
                        .addRow("b", "c")
                        .addRow("d", "e", "f"),
                    CharacterTableFormat.create()
                        .setRowSeparator('-')
                        .setNewLine('\n'),
                    Strings.join('\n', Iterable.create(
                        "a  ",
                        "---",
                        "bc ",
                        "---",
                        "def")));

                toStringTest.run(
                    CharacterTable.create()
                        .addRow("a")
                        .addRow("b", "c")
                        .addRow("d", "e", "f"),
                    CharacterTableFormat.create()
                        .setRowSeparator(Iterable.create('^', 'v'))
                        .setNewLine('\n'),
                    Strings.join('\n', Iterable.create(
                        "a  ",
                        "^^^",
                        "vvv",
                        "bc ",
                        "^^^",
                        "vvv",
                        "def")));

                toStringTest.run(
                    CharacterTable.create()
                        .addRow("a")
                        .addRow("b", "c")
                        .addRow("d", "e", "f"),
                    CharacterTableFormat.create()
                        .setRowSeparator(Iterable.create('^', 'v'))
                        .setNewLine('\n'),
                    Strings.join('\n', Iterable.create(
                        "a  ",
                        "^^^",
                        "vvv",
                        "bc ",
                        "^^^",
                        "vvv",
                        "def")));

                toStringTest.run(
                    CharacterTable.create()
                        .addRow("a")
                        .addRow("b", "c")
                        .addRow("d", "e", "f"),
                    CharacterTableFormat.create()
                        .setRowSeparator('-'),
                    Strings.join('\n', Iterable.create(
                        "a  ---bc ---def")));

                toStringTest.run(
                    CharacterTable.create(),
                    CharacterTableFormat.create()
                        .setTopBorder('^'),
                    Strings.join('\n', Iterable.create()));

                toStringTest.run(
                    CharacterTable.create(),
                    CharacterTableFormat.create()
                        .setTopBorder('^')
                        .setLeftBorder('<'),
                    Strings.join('\n', Iterable.create(
                        "^")));

                toStringTest.run(
                    CharacterTable.create(),
                    CharacterTableFormat.create()
                        .setTopBorder('^')
                        .setRightBorder('>'),
                    Strings.join('\n', Iterable.create(
                        "^")));

                toStringTest.run(
                    CharacterTable.create(),
                    CharacterTableFormat.create()
                        .setTopBorder('^')
                        .setLeftBorder('<')
                        .setRightBorder('>'),
                    Strings.join('\n', Iterable.create(
                        "^^")));

                toStringTest.run(
                    CharacterTable.create(),
                    CharacterTableFormat.create()
                        .setTopBorder('^')
                        .setLeftBorder('<')
                        .setRightBorder(">>"),
                    Strings.join('\n', Iterable.create(
                        "^^^")));

                toStringTest.run(
                    CharacterTable.create(),
                    CharacterTableFormat.create()
                        .setTopBorder(Iterable.create('^', '-'))
                        .setLeftBorder('<')
                        .setRightBorder(">>"),
                    Strings.join('\n', Iterable.create(
                        "^^^---")));

                toStringTest.run(
                    CharacterTable.create()
                        .addRow("a")
                        .addRow("b", "c")
                        .addRow("d", "e", "f"),
                    CharacterTableFormat.create()
                        .setTopBorder('-'),
                    Strings.join('\n', Iterable.create(
                        "---a  bc def")));

                toStringTest.run(
                    CharacterTable.create()
                        .addRow("a")
                        .addRow("b", "c")
                        .addRow("d", "e", "f"),
                    CharacterTableFormat.create()
                        .setTopBorder('^')
                        .setNewLine('\n'),
                    Strings.join('\n', Iterable.create(
                        "^^^",
                        "a  ",
                        "bc ",
                        "def")));

                toStringTest.run(
                    CharacterTable.create()
                        .addRow("a")
                        .addRow("b", "c")
                        .addRow("d", "e", "f"),
                    CharacterTableFormat.create()
                        .setTopBorder('^')
                        .setLeftBorder('<')
                        .setNewLine('\n'),
                    Strings.join('\n', Iterable.create(
                        "^^^^",
                        "<a  ",
                        "<bc ",
                        "<def")));

                toStringTest.run(
                    CharacterTable.create()
                        .addRow("a")
                        .addRow("b", "c")
                        .addRow("d", "e", "f"),
                    CharacterTableFormat.create()
                        .setTopBorder('^')
                        .setRightBorder('>')
                        .setNewLine('\n'),
                    Strings.join('\n', Iterable.create(
                        "^^^^",
                        "a  >",
                        "bc >",
                        "def>")));

                toStringTest.run(
                    CharacterTable.create()
                        .addRow("a")
                        .addRow("b", "c")
                        .addRow("d", "e", "f"),
                    CharacterTableFormat.create()
                        .setTopBorder('^')
                        .setLeftBorder('<')
                        .setRightBorder('>')
                        .setNewLine('\n'),
                    Strings.join('\n', Iterable.create(
                        "^^^^^",
                        "<a  >",
                        "<bc >",
                        "<def>")));

                toStringTest.run(
                    CharacterTable.create()
                        .addRow("a")
                        .addRow("b", "c")
                        .addRow("d", "e", "f"),
                    CharacterTableFormat.create()
                        .setTopBorder(Iterable.create('^', ' ', '^'))
                        .setLeftBorder("< <")
                        .setRightBorder("> >")
                        .setNewLine('\n'),
                    Strings.join('\n', Iterable.create(
                        "^^^^^^^^^",
                        "         ",
                        "^^^^^^^^^",
                        "< <a  > >",
                        "< <bc > >",
                        "< <def> >")));

                toStringTest.run(
                    CharacterTable.create(),
                    CharacterTableFormat.create()
                        .setBottomBorder('v'),
                    Strings.join('\n', Iterable.create()));

                toStringTest.run(
                    CharacterTable.create(),
                    CharacterTableFormat.create()
                        .setBottomBorder('v'),
                    Strings.join('\n', Iterable.create()));

                toStringTest.run(
                    CharacterTable.create(),
                    CharacterTableFormat.create()
                        .setLeftBorder('<')
                        .setBottomBorder('v'),
                    Strings.join('\n', Iterable.create(
                        "v")));

                toStringTest.run(
                    CharacterTable.create(),
                    CharacterTableFormat.create()
                        .setRightBorder('>')
                        .setBottomBorder('v'),
                    Strings.join('\n', Iterable.create(
                        "v")));

                toStringTest.run(
                    CharacterTable.create(),
                    CharacterTableFormat.create()
                        .setLeftBorder('<')
                        .setRightBorder('>')
                        .setBottomBorder('v'),
                    Strings.join('\n', Iterable.create(
                        "vv")));

                toStringTest.run(
                    CharacterTable.create(),
                    CharacterTableFormat.create()
                        .setTopBorder('^')
                        .setBottomBorder('v'),
                    Strings.join('\n', Iterable.create()));

                toStringTest.run(
                    CharacterTable.create(),
                    CharacterTableFormat.create()
                        .setTopBorder('^')
                        .setLeftBorder('<')
                        .setRightBorder('>')
                        .setBottomBorder('v'),
                    Strings.join('\n', Iterable.create(
                        "^^vv")));

                toStringTest.run(
                    CharacterTable.create(),
                    CharacterTableFormat.create()
                        .setTopBorder('^')
                        .setLeftBorder('<')
                        .setRightBorder('>')
                        .setBottomBorder('v')
                        .setNewLine('\n'),
                    Strings.join('\n', Iterable.create(
                        "^^",
                        "vv")));

                toStringTest.run(
                    CharacterTable.create()
                        .addRow("a")
                        .addRow("b", "c")
                        .addRow("d", "e", "f"),
                    CharacterTableFormat.create()
                        .setBottomBorder('v'),
                    Strings.join('\n', Iterable.create(
                        "a  bc defvvv")));

                toStringTest.run(
                    CharacterTable.create()
                        .addRow("a")
                        .addRow("b", "c")
                        .addRow("d", "e", "f"),
                    CharacterTableFormat.create()
                        .setBottomBorder('v')
                        .setLeftBorder('<')
                        .setNewLine('\n'),
                    Strings.join('\n', Iterable.create(
                        "<a  ",
                        "<bc ",
                        "<def",
                        "vvvv")));

                toStringTest.run(
                    CharacterTable.create()
                        .addRow("a")
                        .addRow("b", "c")
                        .addRow("d", "e", "f"),
                    CharacterTableFormat.create()
                        .setBottomBorder('v')
                        .setRightBorder('>')
                        .setNewLine('\n'),
                    Strings.join('\n', Iterable.create(
                        "a  >",
                        "bc >",
                        "def>",
                        "vvvv")));

                toStringTest.run(
                    CharacterTable.create()
                        .addRow("a")
                        .addRow("b", "c")
                        .addRow("d", "e", "f"),
                    CharacterTableFormat.create()
                        .setBottomBorder('v')
                        .setLeftBorder('<')
                        .setRightBorder('>')
                        .setNewLine('\n'),
                    Strings.join('\n', Iterable.create(
                        "<a  >",
                        "<bc >",
                        "<def>",
                        "vvvvv")));

                toStringTest.run(
                    CharacterTable.create()
                        .addRow("a")
                        .addRow("b", "c")
                        .addRow("d", "e", "f"),
                    CharacterTableFormat.create()
                        .setBottomBorder(Iterable.create('v', ' ', 'v'))
                        .setLeftBorder("< <")
                        .setRightBorder("> >")
                        .setNewLine('\n'),
                    Strings.join('\n', Iterable.create(
                        "< <a  > >",
                        "< <bc > >",
                        "< <def> >",
                        "vvvvvvvvv",
                        "         ",
                        "vvvvvvvvv")));

                toStringTest.run(
                    CharacterTable.create()
                        .addRow("a")
                        .addRow("b", "c")
                        .addRow("d", "e", "f"),
                    CharacterTableFormat.create()
                        .setTopBorder(Iterable.create('^', ' ', '^'))
                        .setBottomBorder(Iterable.create('v', ' ', 'v'))
                        .setLeftBorder("< <")
                        .setRightBorder("> >")
                        .setNewLine('\n'),
                    Strings.join('\n', Iterable.create(
                        "^^^^^^^^^",
                        "         ",
                        "^^^^^^^^^",
                        "< <a  > >",
                        "< <bc > >",
                        "< <def> >",
                        "vvvvvvvvv",
                        "         ",
                        "vvvvvvvvv")));

                toStringTest.run(
                    CharacterTable.create()
                        .addRow("a")
                        .addRow("b", "c")
                        .addRow("d", "e", "f"),
                    CharacterTableFormat.create()
                        .setLeftBorder('|')
                        .setColumnSeparator('|')
                        .setRightBorder('|')
                        .setRowSeparator('-')
                        .setNewLine('\n'),
                    Strings.join('\n', Iterable.create(
                        "|a| | |",
                        "|-----|",
                        "|b|c| |",
                        "|-----|",
                        "|d|e|f|")));

                toStringTest.run(
                    CharacterTable.create()
                        .addRow("a")
                        .addRow("b", "c")
                        .addRow("d", "e", "f"),
                    CharacterTableFormat.create()
                        .setLeftBorder('|')
                        .setColumnSeparator('|')
                        .setRightBorder('|')
                        .setRowSeparator(Iterable.create('-', '_'))
                        .setNewLine('\n'),
                    Strings.join('\n', Iterable.create(
                        "|a| | |",
                        "|-----|",
                        "|_____|",
                        "|b|c| |",
                        "|-----|",
                        "|_____|",
                        "|d|e|f|")));

                toStringTest.run(
                    CharacterTable.create(),
                    CharacterTableFormat.create()
                        .setTopBorder('-')
                        .setLeftBorder('|')
                        .setColumnSeparator('|')
                        .setRightBorder('|')
                        .setRowSeparator('-')
                        .setBottomBorder('-')
                        .setNewLine('\n'),
                    Strings.join('\n', Iterable.create(
                        "--",
                        "--")));
                toStringTest.run(
                    CharacterTable.create()
                        .addRow("a"),
                    CharacterTableFormat.create()
                        .setTopBorder('-')
                        .setLeftBorder('|')
                        .setColumnSeparator('|')
                        .setRightBorder('|')
                        .setRowSeparator('-')
                        .setBottomBorder('-')
                        .setNewLine('\n'),
                    Strings.join('\n', Iterable.create(
                        "---",
                        "|a|",
                        "---")));
                toStringTest.run(
                    CharacterTable.create()
                        .addRow("a", "b", "c"),
                    CharacterTableFormat.create()
                        .setTopBorder('-')
                        .setLeftBorder('|')
                        .setColumnSeparator('|')
                        .setRightBorder('|')
                        .setRowSeparator('-')
                        .setBottomBorder('-')
                        .setNewLine('\n'),
                    Strings.join('\n', Iterable.create(
                        "-------",
                        "|a|b|c|",
                        "-------")));
                toStringTest.run(
                    CharacterTable.create()
                        .addRow("a")
                        .addRow("b", "c")
                        .addRow("d", "e", "f"),
                    CharacterTableFormat.create()
                        .setTopBorder('-')
                        .setLeftBorder('|')
                        .setColumnSeparator('|')
                        .setRightBorder('|')
                        .setRowSeparator('-')
                        .setBottomBorder('-')
                        .setNewLine('\n'),
                    Strings.join('\n', Iterable.create(
                        "-------",
                        "|a| | |",
                        "|-----|",
                        "|b|c| |",
                        "|-----|",
                        "|d|e|f|",
                        "-------")));
            });

            runner.testGroup("toString(CharacterWriteStream)", () ->
            {
                final Action2<CharacterTable,String> toStringTest = (CharacterTable table, String expected) ->
                {
                    runner.test("with " + table, (Test test) ->
                    {
                        final InMemoryCharacterToByteStream stream = InMemoryCharacterToByteStream.create();
                        final int charactersWritten = table.toString(stream).await();
                        test.assertEqual(expected, stream.getText().await());
                        test.assertEqual(expected.length(), charactersWritten);
                    });
                };

                toStringTest.run(CharacterTable.create(), "");
                toStringTest.run(
                    CharacterTable.create()
                        .addRow(),
                    "[]");
                toStringTest.run(
                    CharacterTable.create()
                        .addRow("a"),
                    "[a]");
                toStringTest.run(
                    CharacterTable.create()
                        .addRow("a", "b"),
                    "[a,b]");
                toStringTest.run(
                    CharacterTable.create()
                        .addRow("a", "b", "c"),
                    "[a,b,c]");
                toStringTest.run(
                    CharacterTable.create()
                        .addRow("a")
                        .addRow("b"),
                    "[a],[b]");
                toStringTest.run(
                    CharacterTable.create()
                        .addRow("a")
                        .addRow("b")
                        .addRow("c"),
                    "[a],[b],[c]");
                toStringTest.run(
                    CharacterTable.create()
                        .addRow("a", "b", "c")
                        .addRow("d", "e", "f")
                        .addRow("g", "h", "i"),
                    "[a,b,c],[d,e,f],[g,h,i]");
                toStringTest.run(
                    CharacterTable.create()
                        .addRow("apples", "$5.00", "100")
                        .addRow("doo-dads", "$30.99", "5")
                        .addRow("glow-buds", "$3000.00", "17"),
                    "[apples   ,$5.00   ,100],[doo-dads ,$30.99  ,5  ],[glow-buds,$3000.00,17 ]");
            });

            runner.testGroup("toString(CharacterWriteStream,CharacterTableFormat)", () ->
            {
                runner.test("with null stream", (Test test) ->
                {
                    final CharacterTable table = CharacterTable.create();
                    final InMemoryCharacterToByteStream stream = null;
                    final CharacterTableFormat format = CharacterTableFormat.create();
                    test.assertThrows(() -> table.toString(stream, format),
                        new PreConditionFailure("writeStream cannot be null."));
                });

                runner.test("with null format", (Test test) ->
                {
                    final CharacterTable table = CharacterTable.create();
                    final InMemoryCharacterToByteStream stream = InMemoryCharacterToByteStream.create();
                    final CharacterTableFormat format = null;
                    test.assertThrows(() -> table.toString(stream, format),
                        new PreConditionFailure("format cannot be null."));
                });

                final Action3<CharacterTable,CharacterTableFormat,String> toStringTest = (CharacterTable table, CharacterTableFormat format, String expected) ->
                {
                    runner.test("with " + English.andList(table, format), (Test test) ->
                    {
                        final InMemoryCharacterToByteStream stream = InMemoryCharacterToByteStream.create();
                        final int charactersWritten = table.toString(stream, format).await();
                        test.assertEqual(expected, stream.getText().await());
                        test.assertEqual(expected.length(), charactersWritten);
                    });
                };

                toStringTest.run(
                    CharacterTable.create(),
                    CharacterTableFormat.create(),
                    "");
                toStringTest.run(
                    CharacterTable.create()
                        .addRow("a"),
                    CharacterTableFormat.create(),
                    "a");
                toStringTest.run(
                    CharacterTable.create()
                        .addRow("a", "b", "c"),
                    CharacterTableFormat.create(),
                    "abc");
                toStringTest.run(
                    CharacterTable.create()
                        .addRow("a")
                        .addRow("b", "c")
                        .addRow("d", "e", "f"),
                    CharacterTableFormat.create(),
                    "a  bc def");

                toStringTest.run(
                    CharacterTable.create(),
                    CharacterTableFormat.consise,
                    "");
                toStringTest.run(
                    CharacterTable.create()
                        .addRow("a"),
                    CharacterTableFormat.consise,
                    "a");
                toStringTest.run(
                    CharacterTable.create()
                        .addRow("a", "b", "c"),
                    CharacterTableFormat.consise,
                    "a b c");
                toStringTest.run(
                    CharacterTable.create()
                        .addRow("a")
                        .addRow("b", "c")
                        .addRow("d", "e", "f"),
                    CharacterTableFormat.consise,
                    "a    \nb c  \nd e f");
                toStringTest.run(
                    CharacterTable.create()
                        .addRow("Tests run:", "500")
                        .addRow("Tests failed:", "3")
                        .addRow("Tests duration:", "5 Seconds"),
                    CharacterTableFormat.consise,
                    Strings.join('\n', Iterable.create(
                        "Tests run:      500      ",
                        "Tests failed:   3        ",
                        "Tests duration: 5 Seconds")));
                toStringTest.run(
                    CharacterTable.create()
                        .addRow("Tests run:", "500")
                        .addRow("Tests failed:", "3")
                        .addRow("Tests duration:", "5", "Seconds"),
                    CharacterTableFormat.consise,
                    Strings.join('\n', Iterable.create(
                        "Tests run:      500        ",
                        "Tests failed:   3          ",
                        "Tests duration: 5   Seconds")));
                toStringTest.run(
                    CharacterTable.create()
                        .addRow("Tests run:", "500")
                        .addRow("Tests failed:", "3")
                        .addRow("Tests duration:", "5", "Seconds"),
                    CharacterTableFormat.create()
                        .setNewLine('\n')
                        .setColumnSeparator(' ')
                        .setColumnHorizontalAlignment(1, HorizontalAlignment.Center),
                    Strings.join('\n', Iterable.create(
                        "Tests run:      500        ",
                        "Tests failed:    3         ",
                        "Tests duration:  5  Seconds")));
                toStringTest.run(
                    CharacterTable.create()
                        .addRow("Tests run:", "500")
                        .addRow("Tests failed:", "3")
                        .addRow("Tests duration:", "5", "Seconds"),
                    CharacterTableFormat.create()
                        .setNewLine('\n')
                        .setColumnSeparator(' ')
                        .setColumnHorizontalAlignment(1, HorizontalAlignment.Right),
                    Strings.join('\n', Iterable.create(
                        "Tests run:      500        ",
                        "Tests failed:     3        ",
                        "Tests duration:   5 Seconds")));

                toStringTest.run(
                    CharacterTable.create(),
                    CharacterTableFormat.create()
                        .setLeftBorder('|')
                        .setColumnSeparator('|')
                        .setRightBorder('|')
                        .setNewLine('\n'),
                    Strings.join('\n', Iterable.create(
                        "")));

                toStringTest.run(
                    CharacterTable.create()
                        .addRow("a"),
                    CharacterTableFormat.create()
                        .setLeftBorder('|')
                        .setColumnSeparator('|')
                        .setRightBorder('|')
                        .setNewLine('\n'),
                    Strings.join('\n', Iterable.create(
                        "|a|")));

                toStringTest.run(
                    CharacterTable.create()
                        .addRow("a", "b", "c"),
                    CharacterTableFormat.create()
                        .setLeftBorder('|')
                        .setColumnSeparator('|')
                        .setRightBorder('|')
                        .setNewLine('\n'),
                    Strings.join('\n', Iterable.create(
                        "|a|b|c|")));

                toStringTest.run(
                    CharacterTable.create()
                        .addRow("a")
                        .addRow("b", "c")
                        .addRow("d", "e", "f"),
                    CharacterTableFormat.create()
                        .setLeftBorder('|')
                        .setColumnSeparator('|')
                        .setRightBorder('|')
                        .setNewLine('\n'),
                    Strings.join('\n', Iterable.create(
                        "|a| | |",
                        "|b|c| |",
                        "|d|e|f|")));

                toStringTest.run(
                    CharacterTable.create()
                        .addRow("a")
                        .addRow("b", "c")
                        .addRow("d", "e", "f"),
                    CharacterTableFormat.create()
                        .setLeftBorder("xox")
                        .setColumnSeparator('|')
                        .setRightBorder("xox")
                        .setNewLine('\n'),
                    Strings.join('\n', Iterable.create(
                        "xoxa| | xox",
                        "xoxb|c| xox",
                        "xoxd|e|fxox")));

                toStringTest.run(
                    CharacterTable.create()
                        .addRow("a")
                        .addRow("b", "c")
                        .addRow("d", "e", "f"),
                    CharacterTableFormat.create()
                        .setLeftBorder('|')
                        .setColumnSeparator("||")
                        .setRightBorder('|')
                        .setNewLine('\n'),
                    Strings.join('\n', Iterable.create(
                        "|a|| || |",
                        "|b||c|| |",
                        "|d||e||f|")));

                toStringTest.run(
                    CharacterTable.create()
                        .addRow("a")
                        .addRow("b", "c")
                        .addRow("d", "e", "f"),
                    CharacterTableFormat.create()
                        .setRowSeparator('-')
                        .setNewLine('\n'),
                    Strings.join('\n', Iterable.create(
                        "a  ",
                        "---",
                        "bc ",
                        "---",
                        "def")));

                toStringTest.run(
                    CharacterTable.create()
                        .addRow("a")
                        .addRow("b", "c")
                        .addRow("d", "e", "f"),
                    CharacterTableFormat.create()
                        .setRowSeparator(Iterable.create('^', 'v'))
                        .setNewLine('\n'),
                    Strings.join('\n', Iterable.create(
                        "a  ",
                        "^^^",
                        "vvv",
                        "bc ",
                        "^^^",
                        "vvv",
                        "def")));

                toStringTest.run(
                    CharacterTable.create()
                        .addRow("a")
                        .addRow("b", "c")
                        .addRow("d", "e", "f"),
                    CharacterTableFormat.create()
                        .setRowSeparator(Iterable.create('^', 'v'))
                        .setNewLine('\n'),
                    Strings.join('\n', Iterable.create(
                        "a  ",
                        "^^^",
                        "vvv",
                        "bc ",
                        "^^^",
                        "vvv",
                        "def")));

                toStringTest.run(
                    CharacterTable.create()
                        .addRow("a")
                        .addRow("b", "c")
                        .addRow("d", "e", "f"),
                    CharacterTableFormat.create()
                        .setRowSeparator('-'),
                    Strings.join('\n', Iterable.create(
                        "a  ---bc ---def")));

                toStringTest.run(
                    CharacterTable.create(),
                    CharacterTableFormat.create()
                        .setTopBorder('^'),
                    Strings.join('\n', Iterable.create()));

                toStringTest.run(
                    CharacterTable.create(),
                    CharacterTableFormat.create()
                        .setTopBorder('^')
                        .setLeftBorder('<'),
                    Strings.join('\n', Iterable.create(
                        "^")));

                toStringTest.run(
                    CharacterTable.create(),
                    CharacterTableFormat.create()
                        .setTopBorder('^')
                        .setRightBorder('>'),
                    Strings.join('\n', Iterable.create(
                        "^")));

                toStringTest.run(
                    CharacterTable.create(),
                    CharacterTableFormat.create()
                        .setTopBorder('^')
                        .setLeftBorder('<')
                        .setRightBorder('>'),
                    Strings.join('\n', Iterable.create(
                        "^^")));

                toStringTest.run(
                    CharacterTable.create(),
                    CharacterTableFormat.create()
                        .setTopBorder('^')
                        .setLeftBorder('<')
                        .setRightBorder(">>"),
                    Strings.join('\n', Iterable.create(
                        "^^^")));

                toStringTest.run(
                    CharacterTable.create()
                        .addRow("a")
                        .addRow("b", "c")
                        .addRow("d", "e", "f"),
                    CharacterTableFormat.create()
                        .setTopBorder('-'),
                    Strings.join('\n', Iterable.create(
                        "---a  bc def")));

                toStringTest.run(
                    CharacterTable.create()
                        .addRow("a")
                        .addRow("b", "c")
                        .addRow("d", "e", "f"),
                    CharacterTableFormat.create()
                        .setTopBorder('^')
                        .setNewLine('\n'),
                    Strings.join('\n', Iterable.create(
                        "^^^",
                        "a  ",
                        "bc ",
                        "def")));

                toStringTest.run(
                    CharacterTable.create()
                        .addRow("a")
                        .addRow("b", "c")
                        .addRow("d", "e", "f"),
                    CharacterTableFormat.create()
                        .setTopBorder('^')
                        .setLeftBorder('<')
                        .setNewLine('\n'),
                    Strings.join('\n', Iterable.create(
                        "^^^^",
                        "<a  ",
                        "<bc ",
                        "<def")));

                toStringTest.run(
                    CharacterTable.create()
                        .addRow("a")
                        .addRow("b", "c")
                        .addRow("d", "e", "f"),
                    CharacterTableFormat.create()
                        .setTopBorder('^')
                        .setRightBorder('>')
                        .setNewLine('\n'),
                    Strings.join('\n', Iterable.create(
                        "^^^^",
                        "a  >",
                        "bc >",
                        "def>")));

                toStringTest.run(
                    CharacterTable.create()
                        .addRow("a")
                        .addRow("b", "c")
                        .addRow("d", "e", "f"),
                    CharacterTableFormat.create()
                        .setTopBorder('^')
                        .setLeftBorder('<')
                        .setRightBorder('>')
                        .setNewLine('\n'),
                    Strings.join('\n', Iterable.create(
                        "^^^^^",
                        "<a  >",
                        "<bc >",
                        "<def>")));

                toStringTest.run(
                    CharacterTable.create()
                        .addRow("a")
                        .addRow("b", "c")
                        .addRow("d", "e", "f"),
                    CharacterTableFormat.create()
                        .setTopBorder(Iterable.create('^', ' ', '^'))
                        .setLeftBorder("< <")
                        .setRightBorder("> >")
                        .setNewLine('\n'),
                    Strings.join('\n', Iterable.create(
                        "^^^^^^^^^",
                        "         ",
                        "^^^^^^^^^",
                        "< <a  > >",
                        "< <bc > >",
                        "< <def> >")));

                toStringTest.run(
                    CharacterTable.create(),
                    CharacterTableFormat.create()
                        .setBottomBorder('v'),
                    Strings.join('\n', Iterable.create()));

                toStringTest.run(
                    CharacterTable.create(),
                    CharacterTableFormat.create()
                        .setBottomBorder('v'),
                    Strings.join('\n', Iterable.create()));

                toStringTest.run(
                    CharacterTable.create(),
                    CharacterTableFormat.create()
                        .setLeftBorder('<')
                        .setBottomBorder('v'),
                    Strings.join('\n', Iterable.create(
                        "v")));

                toStringTest.run(
                    CharacterTable.create(),
                    CharacterTableFormat.create()
                        .setRightBorder('>')
                        .setBottomBorder('v'),
                    Strings.join('\n', Iterable.create(
                        "v")));

                toStringTest.run(
                    CharacterTable.create(),
                    CharacterTableFormat.create()
                        .setLeftBorder('<')
                        .setRightBorder('>')
                        .setBottomBorder('v'),
                    Strings.join('\n', Iterable.create(
                        "vv")));

                toStringTest.run(
                    CharacterTable.create(),
                    CharacterTableFormat.create()
                        .setTopBorder('^')
                        .setBottomBorder('v'),
                    Strings.join('\n', Iterable.create()));

                toStringTest.run(
                    CharacterTable.create(),
                    CharacterTableFormat.create()
                        .setTopBorder('^')
                        .setLeftBorder('<')
                        .setRightBorder('>')
                        .setBottomBorder('v'),
                    Strings.join('\n', Iterable.create(
                        "^^vv")));

                toStringTest.run(
                    CharacterTable.create(),
                    CharacterTableFormat.create()
                        .setTopBorder('^')
                        .setLeftBorder('<')
                        .setRightBorder('>')
                        .setBottomBorder('v')
                        .setNewLine('\n'),
                    Strings.join('\n', Iterable.create(
                        "^^",
                        "vv")));

                toStringTest.run(
                    CharacterTable.create()
                        .addRow("a")
                        .addRow("b", "c")
                        .addRow("d", "e", "f"),
                    CharacterTableFormat.create()
                        .setBottomBorder('v'),
                    Strings.join('\n', Iterable.create(
                        "a  bc defvvv")));

                toStringTest.run(
                    CharacterTable.create()
                        .addRow("a")
                        .addRow("b", "c")
                        .addRow("d", "e", "f"),
                    CharacterTableFormat.create()
                        .setBottomBorder('v')
                        .setLeftBorder('<')
                        .setNewLine('\n'),
                    Strings.join('\n', Iterable.create(
                        "<a  ",
                        "<bc ",
                        "<def",
                        "vvvv")));

                toStringTest.run(
                    CharacterTable.create()
                        .addRow("a")
                        .addRow("b", "c")
                        .addRow("d", "e", "f"),
                    CharacterTableFormat.create()
                        .setBottomBorder('v')
                        .setRightBorder('>')
                        .setNewLine('\n'),
                    Strings.join('\n', Iterable.create(
                        "a  >",
                        "bc >",
                        "def>",
                        "vvvv")));

                toStringTest.run(
                    CharacterTable.create()
                        .addRow("a")
                        .addRow("b", "c")
                        .addRow("d", "e", "f"),
                    CharacterTableFormat.create()
                        .setBottomBorder('v')
                        .setLeftBorder('<')
                        .setRightBorder('>')
                        .setNewLine('\n'),
                    Strings.join('\n', Iterable.create(
                        "<a  >",
                        "<bc >",
                        "<def>",
                        "vvvvv")));

                toStringTest.run(
                    CharacterTable.create()
                        .addRow("a")
                        .addRow("b", "c")
                        .addRow("d", "e", "f"),
                    CharacterTableFormat.create()
                        .setBottomBorder(Iterable.create('v', ' ', 'v'))
                        .setLeftBorder("< <")
                        .setRightBorder("> >")
                        .setNewLine('\n'),
                    Strings.join('\n', Iterable.create(
                        "< <a  > >",
                        "< <bc > >",
                        "< <def> >",
                        "vvvvvvvvv",
                        "         ",
                        "vvvvvvvvv")));

                toStringTest.run(
                    CharacterTable.create()
                        .addRow("a")
                        .addRow("b", "c")
                        .addRow("d", "e", "f"),
                    CharacterTableFormat.create()
                        .setTopBorder(Iterable.create('^', ' ', '^'))
                        .setBottomBorder(Iterable.create('v', ' ', 'v'))
                        .setLeftBorder("< <")
                        .setRightBorder("> >")
                        .setNewLine('\n'),
                    Strings.join('\n', Iterable.create(
                        "^^^^^^^^^",
                        "         ",
                        "^^^^^^^^^",
                        "< <a  > >",
                        "< <bc > >",
                        "< <def> >",
                        "vvvvvvvvv",
                        "         ",
                        "vvvvvvvvv")));

                toStringTest.run(
                    CharacterTable.create()
                        .addRow("a")
                        .addRow("b", "c")
                        .addRow("d", "e", "f"),
                    CharacterTableFormat.create()
                        .setLeftBorder('|')
                        .setColumnSeparator('|')
                        .setRightBorder('|')
                        .setRowSeparator('-')
                        .setNewLine('\n'),
                    Strings.join('\n', Iterable.create(
                        "|a| | |",
                        "|-----|",
                        "|b|c| |",
                        "|-----|",
                        "|d|e|f|")));

                toStringTest.run(
                    CharacterTable.create()
                        .addRow("a")
                        .addRow("b", "c")
                        .addRow("d", "e", "f"),
                    CharacterTableFormat.create()
                        .setLeftBorder('|')
                        .setColumnSeparator('|')
                        .setRightBorder('|')
                        .setRowSeparator(Iterable.create('-', '_'))
                        .setNewLine('\n'),
                    Strings.join('\n', Iterable.create(
                        "|a| | |",
                        "|-----|",
                        "|_____|",
                        "|b|c| |",
                        "|-----|",
                        "|_____|",
                        "|d|e|f|")));

                toStringTest.run(
                    CharacterTable.create(),
                    CharacterTableFormat.create()
                        .setTopBorder('-')
                        .setLeftBorder('|')
                        .setColumnSeparator('|')
                        .setRightBorder('|')
                        .setRowSeparator('-')
                        .setBottomBorder('-')
                        .setNewLine('\n'),
                    Strings.join('\n', Iterable.create(
                        "--",
                        "--")));
                toStringTest.run(
                    CharacterTable.create()
                        .addRow("a"),
                    CharacterTableFormat.create()
                        .setTopBorder('-')
                        .setLeftBorder('|')
                        .setColumnSeparator('|')
                        .setRightBorder('|')
                        .setRowSeparator('-')
                        .setBottomBorder('-')
                        .setNewLine('\n'),
                    Strings.join('\n', Iterable.create(
                        "---",
                        "|a|",
                        "---")));
                toStringTest.run(
                    CharacterTable.create()
                        .addRow("a", "b", "c"),
                    CharacterTableFormat.create()
                        .setTopBorder('-')
                        .setLeftBorder('|')
                        .setColumnSeparator('|')
                        .setRightBorder('|')
                        .setRowSeparator('-')
                        .setBottomBorder('-')
                        .setNewLine('\n'),
                    Strings.join('\n', Iterable.create(
                        "-------",
                        "|a|b|c|",
                        "-------")));
                toStringTest.run(
                    CharacterTable.create()
                        .addRow("a")
                        .addRow("b", "c")
                        .addRow("d", "e", "f"),
                    CharacterTableFormat.create()
                        .setTopBorder('-')
                        .setLeftBorder('|')
                        .setColumnSeparator('|')
                        .setRightBorder('|')
                        .setRowSeparator('-')
                        .setBottomBorder('-')
                        .setNewLine('\n'),
                    Strings.join('\n', Iterable.create(
                        "-------",
                        "|a| | |",
                        "|-----|",
                        "|b|c| |",
                        "|-----|",
                        "|d|e|f|",
                        "-------")));
            });

            runner.testGroup("getColumnWidths(Iterable<? extends Iterable<String>>)", () ->
            {
                final Action2<Iterable<Iterable<String>>,Throwable> getColumnWidthsErrorTest = (Iterable<Iterable<String>> rows, Throwable expected) ->
                {
                    runner.test("with " + rows, (Test test) ->
                    {
                        test.assertThrows(() -> CharacterTable.getColumnWidths(rows), expected);
                    });
                };

                getColumnWidthsErrorTest.run(null, new PreConditionFailure("rows cannot be null."));

                final Action2<Iterable<Iterable<String>>,Indexable<Integer>> getColumnWidthsTest = (Iterable<Iterable<String>> rows, Indexable<Integer> expected) ->
                {
                    runner.test("with " + rows, (Test test) ->
                    {
                        test.assertEqual(expected, CharacterTable.getColumnWidths(rows));
                    });
                };

                getColumnWidthsTest.run(
                    Iterable.create(),
                    Indexable.create());
                getColumnWidthsTest.run(
                    Iterable.create(
                        Iterable.create()),
                    Indexable.create());
                getColumnWidthsTest.run(
                    Iterable.create(
                        Iterable.create(),
                        Iterable.create()),
                    Indexable.create());
                getColumnWidthsTest.run(
                    Iterable.create(
                        Iterable.create(
                            "a")),
                    Indexable.create(1));
                getColumnWidthsTest.run(
                    Iterable.create(
                        Iterable.create(
                            "abcd")),
                    Indexable.create(4));
                getColumnWidthsTest.run(
                    Iterable.create(
                        Iterable.create(
                            "a", "bc", "def")),
                    Indexable.create(1, 2, 3));
                getColumnWidthsTest.run(
                    Iterable.create(
                        Iterable.create(
                            "a", "bc", "def"),
                        Iterable.create(
                            "g"),
                        Iterable.create(
                            "h", "ijkl"),
                        Iterable.create(
                            "", "", "mnopq")),
                    Indexable.create(1, 4, 5));
                getColumnWidthsTest.run(
                    Iterable.create(
                        Iterable.create(
                            "apples", "$5.00", "100"),
                        Iterable.create(
                            "doo-dads", "$30.99", "5"),
                        Iterable.create(
                            "glow-buds", "$3000.00", "17")),
                    Indexable.create(9, 8, 3));
            });

            runner.testGroup("maximum(int...)", () ->
            {
                runner.test("with no values", (Test test) ->
                {
                    test.assertEqual(0, CharacterTable.maximum());
                });

                runner.test("with one value", (Test test) ->
                {
                    test.assertEqual(10, CharacterTable.maximum(10));
                });

                runner.test("with multiple values", (Test test) ->
                {
                    test.assertEqual(5, CharacterTable.maximum(1, 2, 5, 3, 4));
                });
            });

            runner.testGroup("equals(Object)", () ->
            {
                final Action3<CharacterTable,Object,Boolean> equalsTest = (CharacterTable table, Object rhs, Boolean expected) ->
                {
                    runner.test("with " + English.andList(table, rhs), (Test test) ->
                    {
                        test.assertEqual(expected, table.equals(rhs));
                    });
                };

                equalsTest.run(CharacterTable.create(), null, false);
                equalsTest.run(CharacterTable.create(), "hello", false);
                equalsTest.run(CharacterTable.create(), CharacterTable.create(), true);
                equalsTest.run(
                    CharacterTable.create()
                        .addRow("a", "b", "c"),
                    CharacterTable.create(),
                    false);
                equalsTest.run(
                    CharacterTable.create()
                        .addRow("a", "b", "c"),
                    CharacterTable.create()
                        .addRow()
                        .addRow("a", "b", "c"),
                    false);
                equalsTest.run(
                    CharacterTable.create()
                        .addRow("a", "b", "c"),
                    CharacterTable.create()
                        .addRow("a", "b", "c"),
                    true);
            });

            runner.testGroup("equals(CharacterTable)", () ->
            {
                final Action3<CharacterTable,CharacterTable,Boolean> equalsTest = (CharacterTable table, CharacterTable rhs, Boolean expected) ->
                {
                    runner.test("with " + English.andList(table, rhs), (Test test) ->
                    {
                        test.assertEqual(expected, table.equals(rhs));
                    });
                };

                equalsTest.run(CharacterTable.create(), null, false);
                equalsTest.run(CharacterTable.create(), CharacterTable.create(), true);
                equalsTest.run(
                    CharacterTable.create()
                        .addRow("a", "b", "c"),
                    CharacterTable.create(),
                    false);
                equalsTest.run(
                    CharacterTable.create()
                        .addRow("a", "b", "c"),
                    CharacterTable.create()
                        .addRow()
                        .addRow("a", "b", "c"),
                    false);
                equalsTest.run(
                    CharacterTable.create()
                        .addRow("a", "b", "c"),
                    CharacterTable.create()
                        .addRow("a", "b", "c"),
                    true);
            });
        });
    }
}
