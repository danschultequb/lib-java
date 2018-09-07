package qub;

public class MinimumPatchVersionRange extends VersionRange
{
    private final VersionNumber versionNumber;

    MinimumPatchVersionRange(String text, VersionNumber versionNumber)
    {
        super(text);

        PreCondition.assertNotNull(versionNumber, "versionNumber");
        PreCondition.assertNotNull(versionNumber.getMajor(), "versionNumber.getMajor()");
        PreCondition.assertNotNull(versionNumber.getMinor(), "versionNumber.getMinor()");

        this.versionNumber = versionNumber;
    }

    @Override
    public boolean matches(VersionNumber number)
    {
        PreCondition.assertNotNull(number, "number");

        return Integers.equal(versionNumber.getMajor(), number.getMajor()) &&
                   Integers.equal(versionNumber.getMinor(), number.getMinor()) &&
                   Integers.lessThanOrEqualTo(versionNumber.getPatch(), number.getPatch());
    }
}