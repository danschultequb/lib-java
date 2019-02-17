package qub;

public class CSVDocument implements List<CSVRow>
{
    private final List<CSVRow> rows;

    public CSVDocument(CSVRow... rows)
    {
        this.rows = List.create(rows);
    }

    public CSVDocument(Iterable<CSVRow> rows)
    {
        this.rows = List.create(rows);
    }

    @Override
    public Iterator<CSVRow> iterate()
    {
        return rows.iterate();
    }

    @Override
    public int getCount()
    {
        return rows.getCount();
    }

    @Override
    public CSVRow get(int rowIndex)
    {
        return rows.get(rowIndex);
    }

    public String get(int rowIndex, int columnIndex)
    {
        final CSVRow row = get(rowIndex);
        return row == null ? null : row.get(columnIndex);
    }

    @Override
    public void insert(int insertIndex, CSVRow value)
    {
        rows.insert(insertIndex, value);
    }

    @Override
    public void set(int rowIndex, CSVRow row)
    {
        rows.set(rowIndex, row);
    }

    public void set(int rowIndex, int columnIndex, String value)
    {
        final CSVRow row = get(rowIndex);
        if (row != null)
        {
            row.set(columnIndex, value);
        }
    }

    @Override
    public CSVRow removeAt(int rowIndex)
    {
        return rows.removeAt(rowIndex);
    }

    public String removeAt(int rowIndex, int columnIndex)
    {
        final CSVRow row = get(rowIndex);
        return row == null ? null : row.removeAt(columnIndex);
    }

    @Override
    public boolean equals(Object rhs)
    {
        return Iterable.equals(this, rhs);
    }

    @Override
    public String toString()
    {
        return Iterable.toString(this);
    }
}
