package qub;

public interface SaveableIterator<T> extends Saveable, Iterator<T>
{
    static <T> SaveableIterator<T> create(Iterator<T> innerIterator)
    {
        return BasicSaveableIterator.create(innerIterator);
    }
}
