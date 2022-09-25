package qub;

/**
 * An {@link InMemoryRoot} within an {@link InMemoryFileSystem}.
 */
public class InMemoryRoot extends InMemoryFolder
{
    private final Clock clock;
    private final DataSize totalDataSize;

    private InMemoryRoot(String name, Clock clock, DataSize totalDataSize)
    {
        super(null, name);

        PreCondition.assertNotNull(clock, "clock");
        PreCondition.assertNullOrGreaterThan(totalDataSize, DataSize.bytes(0), "totalDataSize");

        this.clock = clock;
        this.totalDataSize = totalDataSize;
    }

    /**
     * Create a new {@link InMemoryRoot} with the provided name/path.
     * @param name The name/path for the new {@link InMemoryRoot}.
     * @param clock The {@link Clock} that will be used to get the current time when
     * {@link InMemoryFile}s are created and modified.
     */
    public static InMemoryRoot create(String name, Clock clock)
    {
        return InMemoryRoot.create(name, clock, null);
    }

    /**
     * Create a new {@link InMemoryRoot} with the provided name/path.
     * @param name The name/path for the new {@link InMemoryRoot}.
     * @param clock The {@link Clock} that will be used to get the current time when
     * {@link InMemoryFile}s are created and modified.
     * @param totalDataSize The total {@link DataSize} of the new {@link InMemoryRoot}.
     */
    public static InMemoryRoot create(String name, Clock clock, DataSize totalDataSize)
    {
        return new InMemoryRoot(name, clock, totalDataSize);
    }

    /**
     * Get the path to this {@link InMemoryRoot}.
     */
    public Path getPath()
    {
        return Path.parse(this.getName());
    }

    /**
     * Get the current {@link DateTime} from the {@link Clock} that has been assigned to this
     * {@link InMemoryRoot}.
     */
    public DateTime getCurrentDateTime()
    {
        return this.clock.getCurrentDateTime();
    }

    /**
     * Get the total {@link DataSize} of this {@link InMemoryRoot}.
     */
    public DataSize getTotalDataSize()
    {
        return this.totalDataSize;
    }

    /**
     * Get the total unused/free {@link DataSize} of this {@link InMemoryRoot}.
     */
    public DataSize getUnusedDataSize()
    {
        final DataSize totalDataSize = this.getTotalDataSize();
        return totalDataSize == null ? null : totalDataSize.minus(this.getUsedDataSize());
    }
}
