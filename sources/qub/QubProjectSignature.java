package qub;

/**
 * The signature of a published Qub project.
 */
public class QubProjectSignature
{
    private final String publisher;
    private final String project;
    private final String version;

    /**
     * Create a new QubProjectSignature object.
     * @param publisher The publisher of the project.
     * @param project The name of the project.
     * @param version The version of the published project.
     */
    public QubProjectSignature(String publisher, String project, String version)
    {
        PreCondition.assertNotNullAndNotEmpty(publisher, "publisher");
        PreCondition.assertNotNullAndNotEmpty(project, "project");
        PreCondition.assertNotNullAndNotEmpty(version, "version");

        this.publisher = publisher;
        this.project = project;
        this.version = version;
    }

    /**
     * Get the publisher of the project.
     * @return The publisher of the project.
     */
    public String getPublisher()
    {
        return this.publisher;
    }

    /**
     * Get the name of the project.
     * @return The name of the project.
     */
    public String getProject()
    {
        return this.project;
    }

    /**
     * Get the version of the published project.
     * @return The version of the published project.
     */
    public String getVersion()
    {
        return this.version;
    }

    @Override
    public String toString()
    {
        return this.publisher + "/" + this.project + ":" + this.version;
    }

    @Override
    public boolean equals(Object rhs)
    {
        return rhs instanceof QubProjectSignature && this.equals((QubProjectSignature)rhs);
    }

    /**
     * Get whether or not this project signature is equal to the provided project signature.
     * @param rhs The project signature to compare to this project signature.
     * @return Whether or not this project signature is equal to the provided project signature.
     */
    public boolean equals(QubProjectSignature rhs)
    {
        return rhs != null &&
            Comparer.equal(this.publisher, rhs.publisher) &&
            Comparer.equal(this.project, rhs.project) &&
            Comparer.equal(this.version, rhs.version);
    }

    /**
     * Parse a QubProjectSignature object from the provided text.
     * @param text The text to parse into a QubProjectSignature.
     * @return The parsed QubProjectSignature object.
     */
    public static Result<QubProjectSignature> parse(String text)
    {
        PreCondition.assertNotNullAndNotEmpty(text, "text");

        return Result.create(() ->
        {
            final int slashIndex = text.indexOf('/');
            if (slashIndex == 0)
            {
                throw new ParseException("No publisher found in " + Strings.escapeAndQuote(text) + ".");
            }

            if (slashIndex == -1 || slashIndex == text.length() - 1)
            {
                throw new ParseException("No project found in " + Strings.escapeAndQuote(text) + ".");
            }

            final int colonIndex = text.indexOf(':', slashIndex + 1);
            if (colonIndex == slashIndex + 1)
            {
                throw new ParseException("No project found in " + Strings.escapeAndQuote(text) + ".");
            }

            if (colonIndex == -1 || colonIndex == text.length() - 1)
            {
                throw new ParseException("No version found in " + Strings.escapeAndQuote(text) + ".");
            }

            final String publisher = text.substring(0, slashIndex);
            final String project = text.substring(slashIndex + 1, colonIndex);
            final String version = text.substring(colonIndex + 1);
            return new QubProjectSignature(publisher, project, version);
        });
    }
}
