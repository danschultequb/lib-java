package qub;

/**
 * A table that is structured by characters.
 */
public class CharacterTable
{
    private final List<List<String>> rows;

    private CharacterTable()
    {
        this.rows = List.create();
    }

    /**
     * Create a new CharacterTable.
     * @return A new CharacterTable.
     */
    public static CharacterTable create()
    {
        return new CharacterTable();
    }

    /**
     * Add a row with the provided cells to this table.
     * @param rowCells The cells of the row to add to this table.
     * @return This object for method chaining.
     */
    public CharacterTable addRow(String... rowCells)
    {
        PreCondition.assertNotNull(rowCells, "rowCells");

        return this.addRow(Iterable.create(rowCells));
    }

    /**
     * Add a row with the provided cells to this table.
     * @param rowCells The cells of the row to add to this table.
     * @return This object for method chaining.
     */
    public CharacterTable addRow(Iterable<String> rowCells)
    {
        PreCondition.assertNotNull(rowCells, "rowCells");

        this.rows.add(List.create(rowCells));

        return this;
    }

    /**
     * Get the rows of this table.
     * @return The rows of this table.
     */
    public Indexable<? extends Indexable<String>> getRows()
    {
        return this.rows;
    }

    @Override
    public String toString()
    {
        return this.toString(CharacterTableFormat.singleLine);
    }

    /**
     * Get the string representation of this CharacterTable using the provided format.
     * @param format The format to use when getting the string representation of this
     *               CharacterTable.
     * @return The string representation of this CharacterTable.
     */
    public String toString(CharacterTableFormat format)
    {
        PreCondition.assertNotNull(format, "format");

        final InMemoryCharacterToByteStream characterStream = InMemoryCharacterToByteStream.create();
        this.toString(characterStream, format).await();
        return characterStream.getText().await();
    }

    /**
     * Write the string representation of this CharacterTable to the provided CharacterWriteStream.
     * @param writeStream The stream to write the string representation of this CharacterTable to.
     * @return The number of characters that were written.
     */
    public Result<Integer> toString(CharacterWriteStream writeStream)
    {
        PreCondition.assertNotNull(writeStream, "writeStream");

        return this.toString(writeStream, CharacterTableFormat.singleLine);
    }

    /**
     * Write the string representation of this CharacterTable to the provided CharacterWriteStream.
     * @param writeStream The stream to write the string representation of this CharacterTable to.
     * @return The number of characters that were written.
     */
    public Result<Integer> toString(CharacterWriteStream writeStream, CharacterTableFormat format)
    {
        PreCondition.assertNotNull(writeStream, "writeStream");
        PreCondition.assertNotNull(format, "format");

        return Result.create(() ->
        {
            int result = 0;

            final Indexable<Integer> columnWidths = CharacterTable.getColumnWidths(this.rows);
            final int columnCount = columnWidths.getCount();

            final String newLine = format.getNewLine();
            final boolean hasNewLine = !Strings.isNullOrEmpty(newLine);

            final String columnSeparator = format.getColumnSeparator();
            final int columnSeparatorWidth = Strings.getLength(columnSeparator);
            final boolean hasColumnSeparator = columnSeparatorWidth > 0;

            final String leftBorder = format.getLeftBorder();
            final int leftBorderWidth = Strings.getLength(leftBorder);
            final boolean hasLeftBorder = leftBorderWidth > 0;

            final String rightBorder = format.getRightBorder();
            final int rightBorderWidth = Strings.getLength(rightBorder);
            final boolean hasRightBorder = rightBorderWidth > 0;

            final int columnsAndColumnSeparatorsTotalWidth = Integers.sum(columnWidths) + (columnCount <= 1 ? 0 : ((columnCount - 1) * columnSeparatorWidth));

            final Iterable<Character> rowSeparator = format.getRowSeparator();
            final Iterable<String> rowSeparatorLines = rowSeparator == null
                ? Iterable.create()
                : rowSeparator
                    .map((Character rowSeparatorCharacter) ->
                        (hasLeftBorder ? leftBorder : "") +
                        Strings.repeat(rowSeparatorCharacter, columnsAndColumnSeparatorsTotalWidth) +
                        (hasRightBorder ? rightBorder : ""))
                    .toList();
            final boolean hasRowSeparatorLines = rowSeparatorLines.any();

            final int tableWidth = leftBorderWidth + columnsAndColumnSeparatorsTotalWidth + rightBorderWidth;

            final Iterable<Character> topBorder = format.getTopBorder();
            final Iterable<String> topBorderLines = topBorder == null
                ? Iterable.create()
                : topBorder
                    .map((Character topBorderCharacter) ->
                        Strings.repeat(topBorderCharacter, tableWidth))
                    .toList();

            if (tableWidth > 0)
            {
                for (final String topBorderLine : topBorderLines)
                {
                    if (result > 0 && hasNewLine)
                    {
                        result += writeStream.write(newLine).await();
                    }
                    result += writeStream.write(topBorderLine).await();
                }
            }

            boolean firstRow = true;
            for (final Iterable<String> row : this.rows)
            {
                if (result > 0 && hasNewLine)
                {
                    result += writeStream.write(newLine).await();
                }

                if (!firstRow && hasRowSeparatorLines)
                {
                    for (final String rowSeparatorLine : rowSeparatorLines)
                    {
                        result += writeStream.write(rowSeparatorLine).await();
                        if (hasNewLine)
                        {
                            result += writeStream.write(newLine).await();
                        }
                    }
                }

                if (hasLeftBorder)
                {
                    result += writeStream.write(leftBorder).await();
                }

                final Iterator<String> cellIterator = row.iterate();
                for (int columnIndex = 0; columnIndex < columnWidths.getCount(); ++columnIndex)
                {
                    if (columnIndex > 0 && hasColumnSeparator)
                    {
                        result += writeStream.write(columnSeparator).await();
                    }

                    final int columnWidth = columnWidths.get(columnIndex);

                    final String cell = cellIterator.next()
                        ? cellIterator.getCurrent()
                        : "";
                    final String paddedCell = format.padCell(columnIndex, cell, columnWidth);

                    result += writeStream.write(paddedCell).await();
                }

                if (hasRightBorder)
                {
                    result += writeStream.write(rightBorder).await();
                }

                firstRow = false;
            }

            final Iterable<Character> bottomBorder = format.getBottomBorder();
            final Iterable<String> bottomBorderLines = bottomBorder == null
                ? Iterable.create()
                : bottomBorder
                    .map((Character bottomBorderCharacter) ->
                        Strings.repeat(bottomBorderCharacter, tableWidth))
                    .toList();

            if (tableWidth > 0)
            {
                for (final String bottomBorderLine : bottomBorderLines)
                {
                    if (result > 0 && hasNewLine)
                    {
                        result += writeStream.write(newLine).await();
                    }
                    result += writeStream.write(bottomBorderLine).await();
                }
            }

            return result;
        });
    }

    /**
     * Get the widths of the columns based on the provided rows.
     * @param rows The rows or cells.
     * @return The widths of the columns.
     */
    public static Indexable<Integer> getColumnWidths(Iterable<? extends Iterable<String>> rows)
    {
        PreCondition.assertNotNull(rows, "rows");

        final List<Integer> result = List.create();
        for (final Iterable<String> row : rows)
        {
            int columnIndex = 0;
            for (final String cell : row)
            {
                final int cellLength = Strings.getLength(cell);
                if (columnIndex >= result.getCount())
                {
                    result.addAll(cellLength);
                }
                else if (cellLength > result.get(columnIndex))
                {
                    result.set(columnIndex, cellLength);
                }

                ++columnIndex;
            }
        }

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    /**
     * Get the maximum value from the provided values. If no values are provided, then 0 will be
     * returned.
     * @param values The collection of values.
     * @return The maximum value from the provided values, or 0 if no values are provided.
     */
    public static int maximum(int... values)
    {
        int result = 0;
        for (final int value : values)
        {
            if (value > result)
            {
                result = value;
            }
        }
        return result;
    }

    @Override
    public boolean equals(Object rhs)
    {
        return rhs instanceof CharacterTable && this.equals((CharacterTable)rhs);
    }

    /**
     * Get whether or not this CharacterTable equals the provided CharacterTable.
     * @param rhs The CharacterTable to compare against this CharacterTable.
     * @return Whether or not this CharacterTable equals the provided CharacterTable.
     */
    public boolean equals(CharacterTable rhs)
    {
        return rhs != null &&
            this.rows.equals(rhs.rows);
    }
}
