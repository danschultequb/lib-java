package qub;

public class MinimumMinorVersionRange extends VersionRange
{
    private final VersionNumber versionNumber;

    MinimumMinorVersionRange(String text, VersionNumber versionNumber)
    {
        super(text);

        PreCondition.assertNotNull(versionNumber, "versionNumber");
        PreCondition.assertNotNull(versionNumber.getMajor(), "versionNumber.getMajor()");

        this.versionNumber = versionNumber;
    }

    @Override
    public boolean matches(VersionNumber number)
    {
        PreCondition.assertNotNull(number, "number");

        boolean result = false;
        if (Integers.equal(versionNumber.getMajor(), number.getMajor()))
        {
            if (Integers.equal(versionNumber.getMinor(), number.getMinor()))
            {
                result = Integers.lessThanOrEqualTo(versionNumber.getPatch(), number.getPatch());
            }
            else
            {
                result = Integers.lessThan(versionNumber.getMinor(), number.getMinor());
            }
        }

        return result;
    }
}
