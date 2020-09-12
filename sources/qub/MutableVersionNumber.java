package qub;

public class MutableVersionNumber implements VersionNumber
{
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

    static Result<MutableVersionNumber> parse(String text)
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

    @Override
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

    public MutableVersionNumber setMajor(int major)
    {
        PreCondition.assertGreaterThanOrEqualTo(major, 0, "major");

        return this.setPart(VersionNumber.majorPartIndex, major);
    }

    public MutableVersionNumber setMinor(int minor)
    {
        PreCondition.assertGreaterThanOrEqualTo(minor, 0, "minor");
        PreCondition.assertTrue(this.hasMajor(), "this.hasMajor()");

        return this.setPart(VersionNumber.minorPartIndex, minor);
    }

    public MutableVersionNumber setPatch(int patch)
    {
        PreCondition.assertGreaterThanOrEqualTo(patch, 0, "patch");
        PreCondition.assertTrue(this.hasMinor(), "this.hasMinor()");

        return this.setPart(VersionNumber.patchPartIndex, patch);
    }

    public MutableVersionNumber setSuffix(String suffix)
    {
        PreCondition.assertNotNull(suffix, "suffix");

        this.suffix = suffix;

        return this;
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
    public boolean equals(Object rhs)
    {
        return VersionNumber.equals(this, rhs);
    }
}
