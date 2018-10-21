package qub;

public class CSVRow implements List<String>
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

    @Override
    public void insert(int insertIndex, String value)
    {
        cells.insert(insertIndex, value);
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
