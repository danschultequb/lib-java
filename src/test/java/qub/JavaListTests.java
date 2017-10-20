package qub;

public class JavaListTests extends ListTests
{
    @Override
    protected List<Integer> createList(int count)
    {
        final List<Integer> list = JavaList.wrap(new java.util.ArrayList<Integer>());
        for (int i = 0; i < count; ++i)
        {
            list.add(i);
        }
        return list;
    }
}
