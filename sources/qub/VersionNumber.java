package qub;

public interface VersionNumber extends Comparable<VersionNumber>
{
    int majorPartIndex = 0;
    int minorPartIndex = 1;
    int patchPartIndex = 2;

    static MutableVersionNumber create()
    {
        return MutableVersionNumber.create();
    }

    static Result<? extends VersionNumber> parse(String text)
    {
        return MutableVersionNumber.parse(text);
    }

    default boolean any()
    {
        return this.hasMajor() || this.hasSuffix();
    }

    default int getPartCount()
    {
        return this.getParts().getCount();
    }

    Indexable<Integer> getParts();

    default boolean hasPart(int partIndex)
    {
        PreCondition.assertGreaterThanOrEqualTo(partIndex, 0, "partIndex");

        return partIndex < this.getPartCount();
    }

    default int getPart(int partIndex)
    {
        PreCondition.assertTrue(this.hasPart(partIndex), "this.hasPart(partIndex)");

        return this.getParts().get(partIndex);
    }

    default boolean hasMajor()
    {
        return this.hasPart(VersionNumber.majorPartIndex);
    }

    default int getMajor()
    {
        PreCondition.assertTrue(this.hasMajor(), "this.hasMajor()");

        return this.getPart(VersionNumber.majorPartIndex);
    }

    default boolean hasMinor()
    {
        return this.hasPart(VersionNumber.minorPartIndex);
    }

    default int getMinor()
    {
        PreCondition.assertTrue(this.hasMinor(), "this.hasMinor()");

        return this.getPart(VersionNumber.minorPartIndex);
    }

    default boolean hasPatch()
    {
        return this.hasPart(VersionNumber.patchPartIndex);
    }

    default int getPatch()
    {
        PreCondition.assertTrue(this.hasPatch(), "this.hasPatch()");

        return this.getPart(VersionNumber.patchPartIndex);
    }

    default boolean hasSuffix()
    {
        return !Strings.isNullOrEmpty(this.getSuffix());
    }

    String getSuffix();

    VersionNumber clone();

    static String toString(VersionNumber version)
    {
        PreCondition.assertNotNull(version, "version");

        final CharacterList list = CharacterList.create();

        list.addAll(Strings.join('.', version.getParts().map(Integers::toString)));
        if (version.hasSuffix())
        {
            list.addAll(version.getSuffix());
        }

        final String result = list.toString(true);

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    static boolean equals(VersionNumber lhs, Object rhs)
    {
        return lhs == rhs || (lhs != null && rhs instanceof VersionNumber && lhs.equals((VersionNumber)rhs));
    }

    default boolean equals(VersionNumber rhs)
    {
        return rhs != null &&
            this.getParts().equals(rhs.getParts()) &&
            this.getSuffix().equals(rhs.getSuffix());
    }

    @Override
    default Comparison compareTo(VersionNumber rhs)
    {
        Comparison result;

        if (rhs == null)
        {
            result = Comparison.GreaterThan;
        }
        else
        {
            result = Comparison.Equal;

            final int partCount = Math.maximum(this.getPartCount(), rhs.getPartCount());
            for (int i = 0; i < partCount; ++i)
            {
                if (!this.hasPart(i))
                {
                    result = Comparison.LessThan;
                }
                else if (!rhs.hasPart(i))
                {
                    result = Comparison.GreaterThan;
                }
                else
                {
                    result = Comparer.compare(this.getPart(i), rhs.getPart(i));
                }

                if (result != Comparison.Equal)
                {
                    break;
                }
            }

            if (result == Comparison.Equal)
            {
                if (!this.hasSuffix())
                {
                    if (rhs.hasSuffix())
                    {
                        result = Comparison.LessThan;
                    }
                }
                else if (!rhs.hasSuffix())
                {
                    result = Comparison.GreaterThan;
                }
                else
                {
                    result = Comparer.compare(this.getSuffix(), rhs.getSuffix());
                }
            }
        }

        return result;
    }
}
