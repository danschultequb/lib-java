package qub;

public class MutableVersionNumber implements Comparable<MutableVersionNumber>
{
    private static final int majorPartIndex = 0;
    private static final int minorPartIndex = 1;
    private static final int patchPartIndex = 2;

    private final List<Integer> parts;
    private String suffix;

    private MutableVersionNumber()
    {
        this.parts = List.create();
        this.suffix = "";
    }

    public static MutableVersionNumber create()
    {
        return new MutableVersionNumber();
    }

    public static Result<MutableVersionNumber> parse(String text)
    {
        PreCondition.assertNotNull(text, "text");

        return Result.create(() ->
        {
            final MutableVersionNumber result = MutableVersionNumber.create();

            final CharacterList part = CharacterList.create();
            Character previousCharacter = null;
            boolean inSuffix = false;

            for (final char c : Strings.iterate(text))
            {
                if (inSuffix)
                {
                    part.add(c);
                }
                else
                {
                    if (Character.isDigit(c))
                    {
                        part.add(c);
                    }
                    else if (c == '.')
                    {
                        if (previousCharacter == null || previousCharacter == '.')
                        {
                            if (previousCharacter != null)
                            {
                                part.add(previousCharacter);
                            }
                            part.add(c);
                            inSuffix = true;
                        }
                        else
                        {
                            result.addPart(Integers.parse(part.toString(true)).await());
                            part.clear();
                        }
                    }
                    else
                    {
                        if (part.any())
                        {
                            result.addPart(Integers.parse(part.toString(true)).await());
                            part.clear();
                        }

                        if (Comparer.equal(previousCharacter, '.'))
                        {
                            part.add('.');
                        }
                        part.add(c);
                        inSuffix = true;
                    }
                }

                previousCharacter = c;
            }

            if (inSuffix)
            {
                result.setSuffix(part.toString(true));
            }
            else if (part.any())
            {
                result.addPart(Integers.parse(part.toString(true)).await());
            }
            else if (Comparer.equal(previousCharacter, '.'))
            {
                result.setSuffix(".");
            }

            return result;
        });
    }

    public boolean any()
    {
        return this.hasMajor() || this.hasSuffix();
    }

    public int getPartCount()
    {
        return this.parts.getCount();
    }

    public Indexable<Integer> getParts()
    {
        return this.parts;
    }

    public MutableVersionNumber addPart(int part)
    {
        PreCondition.assertGreaterThanOrEqualTo(part, 0, "part");

        this.parts.add(part);

        return this;
    }

    public MutableVersionNumber addPart(Integer part)
    {
        PreCondition.assertNotNull(part, "part");

        return this.addPart(part.intValue());
    }

    public MutableVersionNumber addParts(int... parts)
    {
        PreCondition.assertNotNull(parts, "parts");

        for (final int part : parts)
        {
            this.addPart(part);
        }

        return this;
    }

    public MutableVersionNumber addParts(Iterable<Integer> parts)
    {
        PreCondition.assertNotNull(parts, "parts");

        for (final Integer part : parts)
        {
            this.addPart(part);
        }

        return this;
    }

    public MutableVersionNumber setPart(int partIndex, int part)
    {
        PreCondition.assertBetween(0, partIndex, this.getPartCount(), "partIndex");
        PreCondition.assertGreaterThanOrEqualTo(part, 0, "part");

        if (this.hasPart(partIndex))
        {
            this.parts.set(partIndex, part);
        }
        else
        {
            this.addPart(part);
        }

        return this;
    }

    public boolean hasPart(int partIndex)
    {
        PreCondition.assertGreaterThanOrEqualTo(partIndex, 0, "partIndex");

        return partIndex < this.parts.getCount();
    }

    public int getPart(int partIndex)
    {
        PreCondition.assertTrue(this.hasPart(partIndex), "this.hasPart(partIndex)");

        return this.parts.get(partIndex);
    }

    public MutableVersionNumber setMajor(int major)
    {
        PreCondition.assertGreaterThanOrEqualTo(major, 0, "major");

        return this.setPart(MutableVersionNumber.majorPartIndex, major);
    }

    public boolean hasMajor()
    {
        return this.hasPart(MutableVersionNumber.majorPartIndex);
    }

    public int getMajor()
    {
        PreCondition.assertTrue(this.hasMajor(), "this.hasMajor()");

        return this.getPart(MutableVersionNumber.majorPartIndex);
    }

    public MutableVersionNumber setMinor(int minor)
    {
        PreCondition.assertGreaterThanOrEqualTo(minor, 0, "minor");
        PreCondition.assertTrue(this.hasMajor(), "this.hasMajor()");

        return this.setPart(MutableVersionNumber.minorPartIndex, minor);
    }

    public boolean hasMinor()
    {
        return this.hasPart(MutableVersionNumber.minorPartIndex);
    }

    public int getMinor()
    {
        PreCondition.assertTrue(this.hasMinor(), "this.hasMinor()");

        return this.getPart(MutableVersionNumber.minorPartIndex);
    }

    public MutableVersionNumber setPatch(int patch)
    {
        PreCondition.assertGreaterThanOrEqualTo(patch, 0, "patch");
        PreCondition.assertTrue(this.hasMinor(), "this.hasMinor()");

        return this.setPart(MutableVersionNumber.patchPartIndex, patch);
    }

    public boolean hasPatch()
    {
        return this.hasPart(MutableVersionNumber.patchPartIndex);
    }

    public int getPatch()
    {
        PreCondition.assertTrue(this.hasPatch(), "this.hasPatch()");

        return this.getPart(MutableVersionNumber.patchPartIndex);
    }

    public MutableVersionNumber setSuffix(String suffix)
    {
        PreCondition.assertNotNull(suffix, "suffix");

        this.suffix = suffix;

        return this;
    }

    public boolean hasSuffix()
    {
        return !Strings.isNullOrEmpty(this.suffix);
    }

    public String getSuffix()
    {
        final String result = this.suffix;

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    @Override
    public MutableVersionNumber clone()
    {
        return MutableVersionNumber.create()
            .addParts(this.getParts())
            .setSuffix(this.getSuffix());
    }

    @Override
    public String toString()
    {
        final CharacterList list = CharacterList.create();

        list.addAll(Strings.join('.', this.getParts().map(Integers::toString)));
        if (this.hasSuffix())
        {
            list.addAll(this.getSuffix());
        }

        final String result = list.toString(true);

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    @Override
    public boolean equals(Object rhs)
    {
        return rhs instanceof MutableVersionNumber && this.equals((MutableVersionNumber)rhs);
    }

    public boolean equals(MutableVersionNumber rhs)
    {
        return rhs != null &&
            this.parts.equals(rhs.parts) &&
            this.suffix.equals(rhs.suffix);
    }

    @Override
    public Comparison compareTo(MutableVersionNumber rhs)
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
