package qub;

public class CSVRow extends ListBase<String>
{
    private final List<String> cells;

    public CSVRow(String... cells)
    {
        this.cells = ArrayList.fromValues(cells);
    }

    public CSVRow(Iterable<String> cells)
    {
        this.cells = ArrayList.fromValues(cells);
    }

    public String get(int index)
    {
        return cells.get(index);
    }

    public void add(String cell)
    {
        cells.add(cell);
    }

    @Override
    public Result<Boolean> insert(int insertIndex, String value)
    {
        return cells.insert(insertIndex, value);
    }

    @Override
    public void set(int index, String value)
    {
        cells.set(index, value);
    }

    @Override
    public String removeAt(int index)
    {
        return cells.removeAt(index);
    }

    @Override
    public Iterator<String> iterate()
    {
        return cells.iterate();
    }
}
