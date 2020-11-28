package qub;

/**
 * An object that can be used to get information about and load types.
 */
public interface TypeLoader
{
    /**
     * Get the Class object that is associated with the provided full type name.
     * @param fullTypeName The full name of the type to get.
     * @return The Class object associated with the provided full type name.
     */
    Result<Class<?>> getType(String fullTypeName);

    /**
     * Get the path to the type container that contains a type with the provided full name.
     * @param fullTypeName The full name of the type to find.
     * @return The path to the type container that contains a type with the provided full name.
     */
    Result<String> getTypeContainerPathString(String fullTypeName);

    /**
     * Get the path to the type container that contains the provided type.
     * @param type The type to find.
     * @return The path to the type container that contains the provided type.
     */
    default Result<String> getTypeContainerPathString(Class<?> type)
    {
        PreCondition.assertNotNull(type, "type");

        return this.getTypeContainerPathString(Types.getFullTypeName(type));
    }

    /**
     * Get the path to the type container that contains a type with the provided full name.
     * @param fullTypeName The full name of the type to find.
     * @return The path to the type container that contains a type with the provided full name.
     */
    default Result<Path> getTypeContainerPath(String fullTypeName)
    {
        PreCondition.assertNotNullAndNotEmpty(fullTypeName, "fullTypeName");

        return Result.create(() ->
        {
            final Path result = Path.parse(this.getTypeContainerPathString(fullTypeName).await());

            PostCondition.assertNotNull(result, "result");
            PostCondition.assertTrue(result.isRooted(), "result.isRooted()");

            return result;
        });
    }

    /**
     * Get the path to the type container that contains the provided type.
     * @param type The type to find.
     * @return The path to the type container that contains the provided type.
     */
    default Result<Path> getTypeContainerPath(Class<?> type)
    {
        return Result.create(() ->
        {
            final Path result = Path.parse(this.getTypeContainerPathString(type).await());

            PostCondition.assertNotNull(result, "result");
            PostCondition.assertTrue(result.isRooted(), "result.isRooted()");

            return result;
        });
    }
}
