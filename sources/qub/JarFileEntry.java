package qub;

/**
 * An entry in a Jar file.
 */
public class JarFileEntry
{
    private final Path relativePath;

    /**
     * Create a new JarFileEntry object with the provided details.
     * @param relativePath The relativePath of the JarFileEntry.
     */
    public JarFileEntry(Path relativePath)
    {
        PreCondition.assertNotNull(relativePath, "relativePath");
        PreCondition.assertFalse(relativePath.isRooted(), "relativePath.isRooted()");

        this.relativePath = relativePath;
    }

    /**
     * Get the relative path of this JarFileEntry.
     * @return The relative path of this JarFileEntry.
     */
    public Path getRelativePath()
    {
        final Path result = relativePath;

        PostCondition.assertNotNull(result, "result");

        return result;
    }
}
