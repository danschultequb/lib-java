package qub;

public class JavaDocument
{
    private final Iterable<JavaSegment> segments;

    public JavaDocument(Iterable<JavaSegment> segments)
    {
        this.segments = segments;
    }

    public Iterable<JavaSegment> getSegments()
    {
        return segments;
    }
}
