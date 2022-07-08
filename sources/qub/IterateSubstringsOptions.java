package qub;

public class IterateSubstringsOptions
{
    private Iterable<String> separators;
    private boolean includeEmptySubstrings;
    private boolean includeSeparators;

    private IterateSubstringsOptions()
    {
        this.separators = Iterable.create();
    }

    public static IterateSubstringsOptions create()
    {
        return new IterateSubstringsOptions();
    }

    public Iterable<String> getSeparators()
    {
        return this.separators;
    }

    public IterateSubstringsOptions setSeparators(char... separators)
    {
        PreCondition.assertNotNull(separators, "separators");

        return this.setSeparators(CharacterArray.create(separators).map(Characters::toString).toList());
    }

    public IterateSubstringsOptions setSeparators(String... separators)
    {
        PreCondition.assertNotNull(separators, "separators");

        return this.setSeparators(Iterable.create(separators));
    }

    public IterateSubstringsOptions setSeparators(Iterable<String> separators)
    {
        PreCondition.assertNotNull(separators, "separators");

        this.separators = separators;

        return this;
    }

    public boolean getIncludeEmptySubstrings()
    {
        return this.includeEmptySubstrings;
    }

    public IterateSubstringsOptions setIncludeEmptySubstrings(boolean includeEmptySubstrings)
    {
        this.includeEmptySubstrings = includeEmptySubstrings;

        return this;
    }

    public boolean getIncludeSeparators()
    {
        return this.includeSeparators;
    }

    public IterateSubstringsOptions setIncludeSeparators(boolean includeSeparators)
    {
        this.includeSeparators = includeSeparators;

        return this;
    }

    @Override
    public String toString()
    {
        return JSONObject.create()
            .setArray("separators", JSONArray.create(this.separators.map((String separator) -> JSONString.get(Strings.escapeAndQuote(separator)))))
            .setBoolean("includeEmptySubstrings", this.includeEmptySubstrings)
            .setBoolean("includeSeparators", this.includeSeparators)
            .toString();
    }

    @Override
    public boolean equals(Object rhs)
    {
        return rhs instanceof IterateSubstringsOptions && this.equals((IterateSubstringsOptions)rhs);
    }

    public boolean equals(IterateSubstringsOptions rhs)
    {
        return rhs != null &&
            Comparer.equal(this.separators, rhs.separators) &&
            this.includeEmptySubstrings == rhs.includeEmptySubstrings &&
            this.includeSeparators == rhs.includeSeparators;
    }
}
