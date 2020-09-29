package qub;

public class BasicSaveableIterator<T> implements SaveableIterator<T>
{
    private final Iterator<T> innerIterator;
    private boolean hasStarted;
    private final List<T> buffer;
    private int bufferCurrentIndex;
    private final List<BasicSaveableIteratorSave> saves;

    private BasicSaveableIterator(Iterator<T> innerIterator)
    {
        PreCondition.assertNotNull(innerIterator, "innerIterator");

        this.innerIterator = innerIterator;
        this.hasStarted = this.innerIterator.hasStarted();
        this.buffer = List.create();
        if (this.innerIterator.hasCurrent())
        {
            this.buffer.add(this.innerIterator.getCurrent());
            this.bufferCurrentIndex = 0;
        }
        else
        {
            this.bufferCurrentIndex = -1;
        }
        this.saves = List.create();
    }

    public static <T> BasicSaveableIterator<T> create(Iterator<T> innerIterator)
    {
        return new BasicSaveableIterator<>(innerIterator);
    }

    public Iterable<T> getBuffer()
    {
        return this.buffer;
    }

    public int getBufferCurrentIndex()
    {
        return this.bufferCurrentIndex;
    }

    public int getSaveCount()
    {
        return this.saves.getCount();
    }

    @Override
    public boolean hasStarted()
    {
        return this.hasStarted;
    }

    @Override
    public boolean hasCurrent()
    {
        return this.hasStarted() && 0 <= this.bufferCurrentIndex && this.bufferCurrentIndex < this.buffer.getCount();
    }

    @Override
    public T getCurrent()
    {
        PreCondition.assertTrue(this.hasCurrent(), "this.hasCurrent()");

        return this.buffer.get(this.bufferCurrentIndex);
    }

    @Override
    public boolean next()
    {
        this.hasStarted = true;

        boolean result;
        final int bufferCount = this.buffer.getCount();
        if (this.bufferCurrentIndex < bufferCount)
        {
            this.bufferCurrentIndex++;
        }

        if (this.bufferCurrentIndex < bufferCount)
        {
            result = true;
        }
        else if (this.innerIterator.next())
        {
            if (!this.saves.any())
            {
                this.buffer.clear();
                this.bufferCurrentIndex = 0;
            }
            this.buffer.add(this.innerIterator.getCurrent());
            result = true;
        }
        else
        {
            result = false;
        }

        return result;
    }

    @Override
    public BasicSaveableIteratorSave save()
    {
        final BasicSaveableIteratorSave result = BasicSaveableIteratorSave.create(this);
        this.saves.add(result);

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    public void restoreSave(BasicSaveableIteratorSave save)
    {
        PreCondition.assertNotNull(save, "save");
        PreCondition.assertNotDisposed(save, "save");

        this.hasStarted = save.hasStarted();
        this.bufferCurrentIndex = save.getBufferCurrentIndex();
    }

    public void disposeSave(BasicSaveableIteratorSave save)
    {
        PreCondition.assertNotNull(save, "save");
        PreCondition.assertTrue(this.saves.contains(save), "this.saves.contains(save)");

        this.saves.remove(save);

        if (!this.saves.any() && this.getBufferCurrentIndex() >= this.buffer.getCount())
        {
            this.buffer.clear();
            this.bufferCurrentIndex = -1;
        }
    }
}
