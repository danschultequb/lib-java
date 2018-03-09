package qub;

public class CSV
{
    public static CSVDocument parse(String text)
    {
        return parse(new StringIterator(text));
    }

    public static CSVDocument parse(Iterator<Character> characters)
    {
        final CSVDocument document = new CSVDocument();
        CSVRow row = new CSVRow();
        final StringBuilder cell = new StringBuilder();

        final Lexer lexer = new Lexer(characters);
        lexer.ensureHasStarted();

        LexType previousLexType = null;
        LexType currentQuote = null;

        while (lexer.hasCurrent())
        {
            final LexType currentLexType = lexer.getCurrent().getType();
            if (currentQuote != null)
            {
                addToCell(cell, lexer.takeCurrent());
                if (currentQuote == currentLexType)
                {
                    currentQuote = null;
                }
            }
            else
            {
                switch (currentLexType)
                {
                    case SingleQuote:
                    case DoubleQuote:
                        addToCell(cell, lexer.takeCurrent());
                        currentQuote = currentLexType;
                        break;

                    case Comma:
                        lexer.next();
                        addCell(row, cell);
                        break;

                    case NewLine:
                    case CarriageReturnNewLine:
                        lexer.next();
                        addRow(document, row, cell, previousLexType);
                        row = new CSVRow();
                        break;

                    default:
                        addToCell(cell, lexer.takeCurrent());
                        break;
                }
            }
            previousLexType = currentLexType;
        }

        if (previousLexType != LexType.NewLine && previousLexType != LexType.CarriageReturnNewLine)
        {
            addRow(document, row, cell, previousLexType);
        }

        return document;
    }

    private static void addToCell(StringBuilder cell, Lex lex)
    {
        cell.append(lex.toString());
    }

    private static void addCell(CSVRow row, StringBuilder cell)
    {
        row.add(cell.toString());
        cell.setLength(0);
    }

    private static void addRow(CSVDocument document, CSVRow row, StringBuilder cell, LexType previousLexType)
    {
        if (cell.length() > 0 || previousLexType == LexType.Comma)
        {
            addCell(row, cell);
        }
        document.add(row);
    }
}
