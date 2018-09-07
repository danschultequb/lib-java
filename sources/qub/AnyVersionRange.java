package qub;

public class AnyVersionRange extends VersionRange
{
    AnyVersionRange()
    {
        super("*");
    }

    @Override
    public boolean matches(VersionNumber number)
    {
        return true;
    }
}
