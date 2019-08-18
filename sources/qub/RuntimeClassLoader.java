package qub;

import java.net.MalformedURLException;

/**
 * A ClassLoader that can have Jar files and class folders added at runtime.
 */
public class RuntimeClassLoader implements Disposable
{
    private boolean isDisposed;
    private final java.net.URLClassLoader urlClassLoader;
    private final Iterable<FileSystemEntry> classSources;

    /**
     * Create a new RuntimeClassLoader.
     * @param classSources The sources that classes will be loaded from.
     */
    public RuntimeClassLoader(FileSystemEntry... classSources)
    {
        this(Iterable.create(classSources));
    }

    /**
     * Create a new RuntimeClassLoader.
     * @param classSources The sources that classes will be loaded from.
     */
    public RuntimeClassLoader(Iterable<FileSystemEntry> classSources)
    {
        this(classSources, ClassLoader.getSystemClassLoader());
    }

    /**
     * Create a new RuntimeClassLoader with the provided parentClassLoader.
     * @param classSources The sources that classes will be loaded from.
     * @param parentClassLoader The parent ClassLoader that will be searched for the requested class
     *                          before this RuntimeClassLoader is searched.
     */
    public RuntimeClassLoader(Iterable<FileSystemEntry> classSources, java.lang.ClassLoader parentClassLoader)
    {
        PreCondition.assertNotNull(classSources, "classSources");
        PreCondition.assertNotNull(parentClassLoader, "parentClassLoader");

        final java.net.URL[] classSourceURLs = getJavaURLsFromClassSources(classSources);
        urlClassLoader = new java.net.URLClassLoader(classSourceURLs, parentClassLoader);
        this.classSources = classSources;
    }

    /**
     * Get the Java URL equivalent of the provided class source.
     * @param classSource The FileSystemEntry object that should be used as a class source.
     * @return The JavaURL equivalent of the provided class source.
     */
    public static java.net.URL getJavaURLFromClassSource(FileSystemEntry classSource)
    {
        PreCondition.assertNotNull(classSource, "classSource");

        String classSourceURLString = "file://" + classSource;
        if (classSource instanceof Folder)
        {
            classSourceURLString += "/";
        }
        java.net.URL result;
        try
        {
            result = new java.net.URL(classSourceURLString);
        }
        catch (MalformedURLException e)
        {
            throw Exceptions.asRuntime(e);
        }

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    /**
     * Get the Java URL equivalents of the provided class sources.
     * @param classSources The FileSystemEntry objects that should be used as a class sources.
     * @return The JavaURL equivalents of the provided class sources.
     */
    public static java.net.URL[] getJavaURLsFromClassSources(Iterable<FileSystemEntry> classSources)
    {
        PreCondition.assertNotNull(classSources, "classSources");

        final java.net.URL[] result = new java.net.URL[classSources.getCount()];
        int index = 0;
        for (final FileSystemEntry classSource : classSources)
        {
            result[index] = getJavaURLFromClassSource(classSource);
            ++index;
        }

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    /**
     * Get the sources that classes will be loaded from.
     * @return The sources that classes will be loaded from.
     */
    public Iterable<FileSystemEntry> getClassSources()
    {
        return classSources;
    }

    /**
     * Load the class object with the provided full class name.
     * @param fullClassName The full name of the class to load.
     * @return The result of loading the class object with the provided full class name.
     */
    public Result<Class<?>> loadClass(String fullClassName)
    {
        PreCondition.assertNotNullAndNotEmpty(fullClassName, "fullClassName");

        Result<Class<?>> result;
        try
        {
            result = Result.success(urlClassLoader.loadClass(fullClassName));
        }
        catch (ClassNotFoundException e)
        {
            result = Result.error(new NotFoundException("Could not load a class with the name " + Strings.escapeAndQuote(fullClassName) + " from " + classSources + "."));
        }

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    @Override
    public boolean isDisposed()
    {
        return isDisposed;
    }

    @Override
    public Result<Boolean> dispose()
    {
        Result<Boolean> result;
        if (isDisposed)
        {
            result = Result.successFalse();
        }
        else
        {
            try
            {
                urlClassLoader.close();
                isDisposed = true;
                result = Result.successTrue();
            }
            catch (java.io.IOException e)
            {
                result = Result.error(e);
            }

        }

        PostCondition.assertNotNull(result, "result");

        return result;
    }
}
