package qub;

public class ExactVersionRange extends VersionRange
{
    public ExactVersionRange(String text)
    {
        super(text);
    }

    @Override
    public boolean matches(VersionNumber number)
    {
        PreCondition.assertNotNull(number, "number");

        return this.toString().equals(number.toString());
    }
}