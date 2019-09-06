package qub;

/**
 * A class that provides access to the contents of a Jar file.
 */
public class JarFile extends File implements Disposable
{
    private final java.util.jar.JarFile javaJarFile;
    private boolean disposed;

    /**
     * Create a new JarFile from the provided Java File object.
     * @param file The File object to the jar file.
     * @throws java.io.IOException
     */
    private JarFile(File file) throws java.io.IOException
    {
        super(file.getFileSystem(), file.getPath());

        PreCondition.assertNotNull(file, "file");

        final java.io.File javaFile = new java.io.File(file.getPath().toString());
        this.javaJarFile = new java.util.jar.JarFile(javaFile);
    }

    /**
     * Create a new JarFile from the provided File object.
     * @param file The File object that points to a Jar file.
     * @return A new JarFile from the provided File object.
     */
    public static Result<JarFile> open(File file)
    {
        PreCondition.assertNotNull(file, "file");

        Result<JarFile> result;
        try
        {
            result = Result.success(new JarFile(file));
        }
        catch (java.io.FileNotFoundException error)
        {
            result = Result.error(new FileNotFoundException(file));
        }
        catch (java.io.IOException error)
        {
            result = Result.error(error);
        }

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    @Override
    public boolean isDisposed()
    {
        return disposed;
    }

    @Override
    public Result<Boolean> dispose()
    {
        Result<Boolean> result;
        if (isDisposed())
        {
            result = Result.successFalse();
        }
        else
        {
            disposed = true;
            try
            {
                javaJarFile.close();
                result = Result.successTrue();
            }
            catch (Throwable e)
            {
                result = Result.error(e);
            }
        }

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    /**
     * Get the entries in this JarFile.
     * @return The entries in this JarFile.
     */
    public Iterable<JarFileEntry> getEntries()
    {
        final List<JarFileEntry> result = List.create();

        final java.util.Enumeration<java.util.jar.JarEntry> jarEntries = javaJarFile.entries();
        while (jarEntries.hasMoreElements())
        {
            final java.util.jar.JarEntry jarEntry = jarEntries.nextElement();
            result.add(new JarFileEntry(Path.parse(jarEntry.getName())));
        }

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    /**
     * Get the entries in this JarFile that point to class files.
     * @return The entries in this JarFile that point to class files.
     */
    public Iterable<JarFileEntry> getClassFileEntries()
    {
        return getEntries()
            .where((JarFileEntry entry) -> Comparer.equal(".class", entry.getRelativePath().getFileExtension()));
    }
}
