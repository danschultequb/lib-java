package qub;

/**
 * A class that provides details about a class file.
 */
public class JavaClassFile extends File
{
    /**
     * Create a new ClassFile object for the provided file.
     * @param file The file to create a ClassFile object for.
     */
    public JavaClassFile(File file)
    {
        super(file == null ? null : file.getFileSystem(), file == null ? null : file.getPath());
    }

    /**
     * Get whether or not this is a class file for an anonymous class.
     * @return Whether or not this is a class file for an anonymous class.
     */
    public boolean isAnonymousClass()
    {
        return JavaClassFile.isAnonymousClass(getPath());
    }

    /**
     * Get the simple name (without the package path) of this class file.
     * @return The simple name (without the package path) of this class file.
     */
    public String getTypeName()
    {
        return JavaClassFile.getTypeName(getPath());
    }

    /**
     * Get the full name (with the package path) of this class file.
     * @param outputFolder The root of the output folder that contains this class file.
     * @return The full name (with the package path of this class file.
     */
    public String getFullTypeName(Folder outputFolder)
    {
        PreCondition.assertNotNull(outputFolder, "outputFolder");

        return JavaClassFile.getFullTypeName(getPath(), outputFolder.getPath());
    }

    /**
     * Get whether or not the provided classFilePath points to an anonymous class file.
     * @param classFilePath The classFilePath to check.
     * @return Whether or not the provided classFilePath points to an anonymous class file.
     */
    public static boolean isAnonymousClass(String classFilePath)
    {
        PreCondition.assertNotNullAndNotEmpty(classFilePath, "classFilePath");

        return JavaClassFile.isAnonymousClass(Path.parse(classFilePath));
    }

    /**
     * Get whether or not the provided classFilePath points to an anonymous class file.
     * @param classFilePath The classFilePath to check.
     * @return Whether or not the provided classFilePath points to an anonymous class file.
     */
    public static boolean isAnonymousClass(Path classFilePath)
    {
        PreCondition.assertNotNull(classFilePath, "classFilePath");
        PreCondition.assertFalse(classFilePath.endsWith("\\"), "classFilePath.endsWith(\"\\\")");
        PreCondition.assertFalse(classFilePath.endsWith("/"), "classFilePath.endsWith(\"/\")");

        return classFilePath.withoutFileExtension().getSegments().last().contains("$");
    }

    /**
     * Get the simple name (without the package path) of the provided file path.
     * @return The simple name (without the package path) of the provided file path.
     */
    public static String getTypeName(String classFilePath)
    {
        PreCondition.assertNotNullAndNotEmpty(classFilePath, "classFilePath");

        return JavaClassFile.getTypeName(Path.parse(classFilePath));
    }

    /**
     * Get the simple name (without the package path) of the provided file path.
     * @return The simple name (without the package path) of the provided file path.
     */
    public static String getTypeName(Path classFilePath)
    {
        PreCondition.assertNotNull(classFilePath, "classFilePath");
        PreCondition.assertFalse(classFilePath.endsWith("\\"), "classFilePath.endsWith(\"\\\")");
        PreCondition.assertFalse(classFilePath.endsWith("/"), "classFilePath.endsWith(\"/\")");

        String result = classFilePath.withoutFileExtension().getSegments().last();
        int dollarSignIndex = result.indexOf('$');
        if (dollarSignIndex != -1)
        {
            result = result.substring(0, dollarSignIndex);
        }

        PostCondition.assertNotNullAndNotEmpty(result, "result");

        return result;
    }

    public static String getFullTypeName(String classFilePath)
    {
        PreCondition.assertNotNullAndNotEmpty(classFilePath, "classFilePath");

        return JavaClassFile.getFullTypeName(Path.parse(classFilePath));
    }

    public static String getFullTypeName(String classFilePath, String outputFolderPath)
    {
        PreCondition.assertNotNullAndNotEmpty(classFilePath, "classFilePath");
        PreCondition.assertNotNullAndNotEmpty(outputFolderPath, "outputFolderPath");

        return JavaClassFile.getFullTypeName(Path.parse(classFilePath), Path.parse(outputFolderPath));
    }

    public static String getFullTypeName(Path classFilePath)
    {
        PreCondition.assertNotNull(classFilePath, "classFilePath");
        PreCondition.assertFalse(classFilePath.isRooted(), "classFilePath.isRooted()");
        PreCondition.assertFalse(classFilePath.endsWith("\\"), "classFilePath.endsWith(\"\\\")");
        PreCondition.assertFalse(classFilePath.endsWith("/"), "classFilePath.endsWith(\"/\")");
        PreCondition.assertNotEqual(classFilePath, Path.parse("."), "classFilePath");

        final Iterable<String> segments = classFilePath.withoutFileExtension().getSegments();

        String result = Strings.join('.', segments.skipLast());
        if (!Strings.isNullOrEmpty(result))
        {
            result += '.';
        }

        String lastSegment = segments.last();
        final int dollarSignIndex = lastSegment.indexOf('$');
        if (dollarSignIndex != -1)
        {
            lastSegment = lastSegment.substring(0, dollarSignIndex);
        }
        result += lastSegment;

        PostCondition.assertNotNullAndNotEmpty(result, "result");

        return result;
    }

    public static String getFullTypeName(Path classFilePath, Path outputFolderPath)
    {
        PreCondition.assertNotNull(classFilePath, "classFilePath");
        PreCondition.assertTrue(classFilePath.isRooted(), "classFilePath.isRooted()");
        PreCondition.assertFalse(classFilePath.endsWith("\\"), "classFilePath.endsWith(\"\\\")");
        PreCondition.assertFalse(classFilePath.endsWith("/"), "classFilePath.endsWith(\"/\")");
        PreCondition.assertNotNull(outputFolderPath, "outputFolderPath");

        return JavaClassFile.getFullTypeName(classFilePath.relativeTo(outputFolderPath));
    }
}
