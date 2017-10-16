package qub;

public class LockedListMapTests extends MapTests
{
    @Override
    protected Map<Integer, Boolean> create()
    {
        return new LockedListMap<>();
    }
}
