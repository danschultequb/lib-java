package qub;

public class SimplePathPatternTests extends PathPatternTests
{
    @Override
    protected SimplePathPattern parse(String text)
    {
        return SimplePathPattern.parse(text);
    }
}
