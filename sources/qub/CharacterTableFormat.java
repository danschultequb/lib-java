package qub;

/**
 * A type that can be used format a CharacterTable as it is being converted to its string
 * representation.
 */
public class CharacterTableFormat
{
    /**
     * A CharacterTableFormat that will format a CharacterTable to a single line.<br/>
     * Example: [a,b,c],[d,e,f],[g,h,i]
     */
    public static final CharacterTableFormat singleLine = CharacterTableFormat.create()
        .setLeftBorder('[')
        .setColumnSeparator(',')
        .setRightBorder(']')
        .setNewLine(',');
    /**
     * A CharacterTableFormat that will format a CharacterTable to a consise 2D representation.<br/>
     * Example:<br/>
     * a b c<br/>
     * d e f<br/>
     * g h i
     */
    public static final CharacterTableFormat consise = CharacterTableFormat.create()
        .setColumnSeparator(' ')
        .setNewLine('\n');

    private String columnSeparator;
    private Iterable<Character> rowSeparator;
    private String newLine;
    private String leftBorder;
    private String rightBorder;
    private Iterable<Character> topBorder;
    private Iterable<Character> bottomBorder;
    private MutableMap<Integer,HorizontalAlignment> columnHorizontalAlignment;

    private CharacterTableFormat()
    {
    }

    /**
     * Create a new CharacterTableFormat object.
     * @return A new CharacterTableFormat object.
     */
    public static final CharacterTableFormat create()
    {
        return new CharacterTableFormat();
    }

    /**
     * Set the character that will separate columns.
     * @param columnSeparator The character that will separate columns.
     * @return This object for method chaining.
     */
    public CharacterTableFormat setColumnSeparator(char columnSeparator)
    {
        return this.setColumnSeparator(Characters.toString(columnSeparator));
    }

    /**
     * Set the string that will separate columns.
     * @param columnSeparator The string that will separate columns.
     * @return This object for method chaining.
     */
    public CharacterTableFormat setColumnSeparator(String columnSeparator)
    {
        PreCondition.assertNotNull(columnSeparator, "columnSeparator");

        this.columnSeparator = columnSeparator;

        return this;
    }

    /**
     * Get the string that will separate columns within the same row.
     * @return The string that will separate columns within the same row.
     */
    public String getColumnSeparator()
    {
        return this.columnSeparator;
    }

    /**
     * Set the character that will separate rows.
     * @param rowSeparator The character that will separate rows.
     * @return This object for method chaining.
     */
    public CharacterTableFormat setRowSeparator(char rowSeparator)
    {
        return this.setRowSeparator(Iterable.create(rowSeparator));
    }

    /**
     * Set the characters that will separate rows. Each character in the provided Iterable will be
     * written to a different line.
     * @param rowSeparator The characters that will separate rows. Each character in the provided
     *                     Iterable will be written to a different line.
     * @return This object for method chaining.
     */
    public CharacterTableFormat setRowSeparator(Iterable<Character> rowSeparator)
    {
        PreCondition.assertNotNull(rowSeparator, "rowSeparator");
        PreCondition.assertFalse(rowSeparator.contains((Character)null), "rowSeparator.contains((Character)null)");

        this.rowSeparator = rowSeparator;

        return this;
    }

    /**
     * Get the characters that will separate rows. Each character in the Iterable will be written
     * to a different line.
     * @return The characters that will separator rows.
     */
    public Iterable<Character> getRowSeparator()
    {
        return this.rowSeparator;
    }

    /**
     * Set the new line character that will be used.
     * @param newLine The new line character that will be used.
     * @return This object for method chaining.
     */
    public CharacterTableFormat setNewLine(char newLine)
    {
        this.newLine = Characters.toString(newLine);

        return this;
    }

    /**
     * Set the new line string that will be used.
     * @param newLine The new line string that will be used.
     * @return This object for method chaining.
     */
    public CharacterTableFormat setNewLine(String newLine)
    {
        PreCondition.assertNotNull(newLine, "newLine");

        this.newLine = newLine;

        return this;
    }

    /**
     * Get the new line string that will be used.
     * @return The new line string that will be used.
     */
    public String getNewLine()
    {
        return this.newLine;
    }

    /**
     * Set the string that will be used as a left border.
     * @param leftBorder The string that will be used as a left border.
     * @return This object for method chaining.
     */
    public CharacterTableFormat setLeftBorder(char leftBorder)
    {
        this.leftBorder = Characters.toString(leftBorder);

        return this;
    }

    /**
     * Set the string that will be used as a left border.
     * @param leftBorder The string that will be used as a left border.
     * @return This object for method chaining.
     */
    public CharacterTableFormat setLeftBorder(String leftBorder)
    {
        PreCondition.assertNotNull(leftBorder, "leftBorder");

        this.leftBorder = leftBorder;

        return this;
    }

    /**
     * Get the string that will be used as a left border.
     * @return The string that will be used as a left border.
     */
    public String getLeftBorder()
    {
        return this.leftBorder;
    }

    /**
     * Set the string that will be used as a right border.
     * @param rightBorder The string that will be used as a right border.
     * @return This object for method chaining.
     */
    public CharacterTableFormat setRightBorder(char rightBorder)
    {
        this.rightBorder = Characters.toString(rightBorder);

        return this;
    }

    /**
     * Set the string that will be used as a right border.
     * @param rightBorder The string that will be used as a right border.
     * @return This object for method chaining.
     */
    public CharacterTableFormat setRightBorder(String rightBorder)
    {
        PreCondition.assertNotNull(rightBorder, "rightBorder");

        this.rightBorder = rightBorder;

        return this;
    }

    /**
     * Get the string that will be used as a right border.
     * @return The string that will be used as a right border.
     */
    public String getRightBorder()
    {
        return this.rightBorder;
    }

    /**
     * Set the character that will be used as a top border.
     * @param topBorder The character that will be used as a top border.
     * @return This object for method chaining.
     */
    public CharacterTableFormat setTopBorder(char topBorder)
    {
        return this.setTopBorder(Iterable.create(topBorder));
    }

    /**
     * Set the characters that will be used as a top border. Each character in the provided
     * Iterable will be written to a different line.
     * @param topBorder The characters that will be used as a top border. Each character in the
     *                  provided Iterable will be written to a different line.
     * @return This object for method chaining.
     */
    public CharacterTableFormat setTopBorder(Iterable<Character> topBorder)
    {
        PreCondition.assertNotNull(topBorder, "topBorder");
        PreCondition.assertFalse(topBorder.contains((Character)null), "topBorder.contains((Character)null)");

        this.topBorder = topBorder;

        return this;
    }

    /**
     * Get the characters that will be used as a top border. Each character in the Iterable will be
     * written to a different line.
     * @return The characters that will be used as a top border.
     */
    public Iterable<Character> getTopBorder()
    {
        return this.topBorder;
    }

    /**
     * Set the character that will be used as a bottom border.
     * @param bottomBorder The character that will be used as a bottom border.
     * @return This object for method chaining.
     */
    public CharacterTableFormat setBottomBorder(char bottomBorder)
    {
        return this.setBottomBorder(Iterable.create(bottomBorder));
    }

    /**
     * Set the characters that will be used as a bottom border. Each character in the provided
     * Iterable will be written to a different line.
     * @param bottomBorder The characters that will be used as a bottom border. Each character in the
     *                  provided Iterable will be written to a different line.
     * @return This object for method chaining.
     */
    public CharacterTableFormat setBottomBorder(Iterable<Character> bottomBorder)
    {
        PreCondition.assertNotNull(bottomBorder, "bottomBorder");
        PreCondition.assertFalse(bottomBorder.contains((Character)null), "bottomBorder.contains((Character)null)");

        this.bottomBorder = bottomBorder;

        return this;
    }

    /**
     * Get the characters that will be used as a bottom border. Each character in the Iterable will
     * be written to a different line.
     * @return The characters that will be used as a bottom border.
     */
    public Iterable<Character> getBottomBorder()
    {
        return this.bottomBorder;
    }

    /**
     * Set the horizintal alignment of the cells within the provided column index.
     * @param columnIndex The index of the column to align.
     * @param horizontalAlignment The horizontal alignment of the cells within the provided column.
     * @return This object for method chaining.
     */
    public CharacterTableFormat setColumnHorizontalAlignment(int columnIndex, HorizontalAlignment horizontalAlignment)
    {
        PreCondition.assertGreaterThanOrEqualTo(columnIndex, 0, "columnIndex");
        PreCondition.assertNotNull(horizontalAlignment, "horizontalAlignment");

        if (this.columnHorizontalAlignment == null)
        {
            this.columnHorizontalAlignment = Map.create();
        }
        this.columnHorizontalAlignment.set(columnIndex, horizontalAlignment);

        return this;
    }

    /**
     * Get the horizontal alignment for the column at the provided index.
     * @param columnIndex The index of the column to get the horizontal alignment for.
     * @return The horizontal alignment for the provided index.
     */
    public Result<HorizontalAlignment> getColumnHorizontalAlignment(int columnIndex)
    {
        PreCondition.assertGreaterThanOrEqualTo(columnIndex, 0, "columnIndex");

        return Result.create(() ->
        {
            HorizontalAlignment result = this.columnHorizontalAlignment == null
                ? null
                : this.columnHorizontalAlignment.get(columnIndex)
                    .catchError(NotFoundException.class)
                    .await();
            if (result == null)
            {
                throw new NotFoundException("No horizontal alignment assigned to the column at index " + columnIndex + ".");
            }

            PostCondition.assertNotNull(result, "result");

            return result;
        });
    }

    public String padCell(int columnIndex, String cellText, int columnWidth)
    {
        PreCondition.assertGreaterThanOrEqualTo(columnIndex, 0, "columnIndex");
        PreCondition.assertNotNull(cellText, "cellText");
        PreCondition.assertGreaterThanOrEqualTo(columnWidth, 0, "columnWidth");

        final HorizontalAlignment columnHorizontalAlignment = this.getColumnHorizontalAlignment(columnIndex)
            .catchError(NotFoundException.class, () -> HorizontalAlignment.Left)
            .await();

        String result;

        final int cellTextLength = cellText.length();
        if (columnWidth <= cellTextLength)
        {
            result = cellText;
        }
        else
        {
            switch (columnHorizontalAlignment)
            {
                case Left:
                    result = Strings.padRight(cellText, columnWidth, ' ');
                    break;

                case Right:
                    result = Strings.padLeft(cellText, columnWidth, ' ');
                    break;

                default:
                    final int cellWidthDifference = columnWidth - cellTextLength;
                    result = Strings.padLeft(cellText, cellTextLength + (cellWidthDifference / 2), ' ');
                    result = Strings.padRight(result, columnWidth, ' ');
                    break;
            }
        }

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertGreaterThanOrEqualTo(result.length(), columnWidth, "result.length()");

        return result;
    }

    @Override
    public String toString()
    {
        final CharacterList result = CharacterList.create();
        result.add('{');

        CharacterTableFormat.addProperty(result, "columnSeparator", this.columnSeparator);
        CharacterTableFormat.addProperty(result, "rowSeparator", this.rowSeparator);
        CharacterTableFormat.addProperty(result, "newLine", this.newLine);
        CharacterTableFormat.addProperty(result, "leftBorder", this.leftBorder);
        CharacterTableFormat.addProperty(result, "rightBorder", this.rightBorder);
        CharacterTableFormat.addProperty(result, "topBorder", this.topBorder);
        CharacterTableFormat.addProperty(result, "bottomBorder", this.bottomBorder);
        if (!Iterable.isNullOrEmpty(this.columnHorizontalAlignment))
        {
            result.addAll(Strings.escapeAndQuote("columnHorizontalAlignment"));
            result.add(':');
            result.add('{');

            final Iterable<Integer> columnIndexes = this.columnHorizontalAlignment.getKeys()
                .order(Comparer::lessThan);
            for (final Integer columnIndex : columnIndexes)
            {
                CharacterTableFormat.addProperty(result, Integers.toString(columnIndex), this.getColumnHorizontalAlignment(columnIndex).await());
            }

            result.add('}');
        }

        result.add('}');
        return result.toString(true);
    }

    private static void addProperty(CharacterList list, String propertyName, Object propertyValue)
    {
        if (propertyValue != null)
        {
            final String propertyValueString = Objects.toString(propertyValue);
            if (!Strings.isNullOrEmpty(propertyValueString))
            {
                if (list.last() != '{')
                {
                    list.add(',');
                }

                list.addAll(Strings.escapeAndQuote(propertyName));
                list.add(':');
                list.addAll(Objects.toString(Strings.escapeAndQuote(propertyValue)));
            }
        }
    }

    @Override
    public boolean equals(Object rhs)
    {
        return rhs instanceof CharacterTableFormat && this.equals((CharacterTableFormat)rhs);
    }

    public boolean equals(CharacterTableFormat rhs)
    {
        return rhs != null &&
            Comparer.equal(this.columnSeparator, rhs.columnSeparator) &&
            Comparer.equal(this.rowSeparator, rhs.rowSeparator) &&
            Comparer.equal(this.newLine, rhs.newLine) &&
            Comparer.equal(this.leftBorder, rhs.leftBorder) &&
            Comparer.equal(this.rightBorder, rhs.rightBorder) &&
            Comparer.equal(this.topBorder, rhs.topBorder) &&
            Comparer.equal(this.bottomBorder, rhs.bottomBorder) &&
            ((this.columnHorizontalAlignment == rhs.columnHorizontalAlignment) ||
                (this.columnHorizontalAlignment != null && this.columnHorizontalAlignment.equals(rhs.columnHorizontalAlignment)));
    }
}
