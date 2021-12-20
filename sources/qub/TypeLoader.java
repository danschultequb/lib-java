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
     * Get the static method on the provided type with the provided name and return type.
     * @param fullTypeName The full type name of the type to load.
     * @param methodName The name of the static method.
     * @return The matching {@link StaticMethod0} object.
     */
    default Result<StaticMethod0<?,?>> getStaticMethod0(String fullTypeName, String methodName)
    {
        PreCondition.assertNotNullAndNotEmpty(fullTypeName, "fullTypeName");
        PreCondition.assertNotNullAndNotEmpty(methodName, "methodName");

        return Result.create(() ->
        {
            final Class<?> type = this.getType(fullTypeName).await();
            return Types.getStaticMethod0(type, methodName).await();
        });
    }

    /**
     * Get the static method on the provided type with the provided name and return type.
     * @param fullTypeName The full type name of the type to load.
     * @param methodName The name of the static method.
     * @param returnType The return type of the static method.
     * @param <TReturn> The return type of the static method.
     * @return The matching {@link StaticMethod0} object.
     */
    default <TReturn> Result<StaticMethod0<?,TReturn>> getStaticMethod0(String fullTypeName, String methodName, Class<TReturn> returnType)
    {
        PreCondition.assertNotNullAndNotEmpty(fullTypeName, "fullTypeName");
        PreCondition.assertNotNullAndNotEmpty(methodName, "methodName");
        PreCondition.assertNotNull(returnType, "returnType");

        return Result.create(() ->
        {
            final Class<?> type = this.getType(fullTypeName).await();
            return Types.getStaticMethod0(type, methodName, returnType).await();
        });
    }

    /**
     * Get the static method on the provided type with the provided name, parameter type, and
     * unknown return type.
     * @param fullTypeName The type to get the static method from.
     * @param methodName The name of the static method.
     * @param arg1Type The type of the parameter.
     * @param <T1> The type of the parameter.
     * @return The matching {@link StaticMethod1} object.
     */
    default <T1> Result<StaticMethod1<?,T1,?>> getStaticMethod1(String fullTypeName, String methodName, Class<T1> arg1Type)
    {
        PreCondition.assertNotNullAndNotEmpty(fullTypeName, "fullTypeName");
        PreCondition.assertNotNullAndNotEmpty(methodName, "methodName");
        PreCondition.assertNotNull(arg1Type, "arg1Type");

        return Result.create(() ->
        {
            final Class<?> type = this.getType(fullTypeName).await();
            return Types.getStaticMethod1(type, methodName, arg1Type).await();
        });
    }

    /**
     * Get the static method on the provided type with the provided name, parameter type, and return type.
     * @param fullTypeName The type to get the static method from.
     * @param methodName The name of the static method.
     * @param arg1Type The type of the parameter.
     * @param returnType The return type of the static method.
     * @param <T1> The type of the parameter.
     * @param <TReturn> The return type of the static method.
     * @return The matching {@link StaticMethod1} object.
     */
    default <T1,TReturn> Result<StaticMethod1<?,T1,TReturn>> getStaticMethod1(String fullTypeName, String methodName, Class<T1> arg1Type, Class<TReturn> returnType)
    {
        PreCondition.assertNotNullAndNotEmpty(fullTypeName, "fullTypeName");
        PreCondition.assertNotNullAndNotEmpty(methodName, "methodName");
        PreCondition.assertNotNull(arg1Type, "arg1Type");
        PreCondition.assertNotNull(returnType, "returnType");

        return Result.create(() ->
        {
            final Class<?> type = this.getType(fullTypeName).await();
            return Types.getStaticMethod1(type, methodName, arg1Type, returnType).await();
        });
    }

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
