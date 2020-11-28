package qub;

/**
 * A TypeLoader object that uses the real JRE ClassLoaders to get runtime information about types.
 */
public class JavaTypeLoader implements TypeLoader
{
    private JavaTypeLoader()
    {
    }

    public static JavaTypeLoader create()
    {
        return new JavaTypeLoader();
    }

    @Override
    public Result<Class<?>> getType(String fullTypeName)
    {
        PreCondition.assertNotNullAndNotEmpty(fullTypeName, "fullTypeName");

        return Result.create(() ->
        {
            final java.lang.ClassLoader classLoader = JavaTypeLoader.class.getClassLoader();
            final Class<?> result;
            try
            {
                result = classLoader.loadClass(fullTypeName);
            }
            catch (ClassNotFoundException e)
            {
                throw new NotFoundException("Could not load a class named " + Strings.escapeAndQuote(fullTypeName) + ".");
            }

            PostCondition.assertNotNull(result, "result");

            return result;
        });
    }

    @Override
    public Result<String> getTypeContainerPathString(String fullTypeName)
    {
        PreCondition.assertNotNullAndNotEmpty(fullTypeName, "fullTypeName");

        return Result.create(() ->
        {
            final java.lang.Class<?> type = this.getType(fullTypeName)
                .convertError(NotFoundException.class, () -> new NotFoundException("Could not find a type container for a type named " + Strings.escapeAndQuote(fullTypeName) + "."))
                .await();
            return this.getTypeContainerPathString(type).await();
        });
    }

    @Override
    public Result<String> getTypeContainerPathString(Class<?> type)
    {
        PreCondition.assertNotNull(type, "type");

        return Result.create(() ->
        {
            String result = null;
            final java.security.ProtectionDomain protectionDomain = type.getProtectionDomain();
            if (protectionDomain != null)
            {
                final java.security.CodeSource codeSource = protectionDomain.getCodeSource();
                if (codeSource != null)
                {
                    final java.net.URL location = codeSource.getLocation();
                    if (location != null)
                    {
                        result = location.toString();
                        final String filePrefix = "file:/";
                        if (result.startsWith(filePrefix))
                        {
                            result = result.substring(filePrefix.length());
                        }
                    }
                }
            }

            if (result == null)
            {
                final java.lang.ClassLoader classLoader = type.getClassLoader();
                if (classLoader != null)
                {

                }
            }

            if (result == null)
            {
                throw new NotFoundException("Could not find a type container for a type named " + Strings.escapeAndQuote(Types.getFullTypeName(type)) + ".");
            }

            return result;
        });
    }
}
