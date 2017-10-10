package qub;

public class DoubleLinkListTests extends ListTests
{
    @Override
    protected List<Integer> createList(int count)
    {
        final DoubleLinkList<Integer> result = new DoubleLinkList<>();
        for (int i = 0; i < count; ++i)
        {
            result.add(i);
        }
        return result;
    }
}
