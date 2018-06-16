package qub;

public class CSVDocument extends ListBase<CSVRow>
{
    private final List<CSVRow> rows;

    public CSVDocument(CSVRow... rows)
    {
        this.rows = ArrayList.fromValues(rows);
    }

    public CSVDocument(Iterable<CSVRow> rows)
    {
        this.rows = ArrayList.fromValues(rows);
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
    public void add(CSVRow row)
    {
        rows.add(row);
    }

    @Override
    public Result<Boolean> insert(int insertIndex, CSVRow value)
    {
        return rows.insert(insertIndex, value);
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
}
