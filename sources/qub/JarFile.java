package qub;

import java.io.IOException;

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

        return Result.create(() ->
        {
            JarFile result;
            try
            {
                result = new JarFile(file);
            }
            catch (java.nio.file.NoSuchFileException error)
            {
                throw new FileNotFoundException(file);
            }
            catch (Throwable error)
            {
                throw Exceptions.asRuntime(error);
            }
            return result;
        });
    }

    @Override
    public boolean isDisposed()
    {
        return this.disposed;
    }

    @Override
    public Result<Boolean> dispose()
    {
        return Result.create(() ->
        {
            boolean result = false;
            if (!this.disposed)
            {
                this.disposed = true;
                try
                {
                    this.javaJarFile.close();
                }
                catch (java.io.IOException e)
                {
                    throw Exceptions.asRuntime(e);
                }

                result = true;
            }
            return result;
        });
    }

    /**
     * Get the entries in this JarFile.
     * @return The entries in this JarFile.
     */
    public Iterable<JarFileEntry> getEntries()
    {
        final List<JarFileEntry> result = List.create();

        final java.util.Enumeration<java.util.jar.JarEntry> jarEntries = this.javaJarFile.entries();
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
        return this.getEntries()
            .where((JarFileEntry entry) -> Comparer.equal(".class", entry.getRelativePath().getFileExtension()));
    }
}
