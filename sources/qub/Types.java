package qub;

/**
 * A collection of functions for interacting with types.
 */
public interface Types
{
    /**
     * Get whether the provided method is static.
     * @param method The method to check.
     */
    public static boolean isStaticMethod(java.lang.reflect.Method method)
    {
        PreCondition.assertNotNull(method, "method");

        return java.lang.reflect.Modifier.isStatic(method.getModifiers());
    }

    /**
     * Get the method signature for the provided type and methodName. The return type of the method
     * signature will be unknown (?).
     * @param type The type of object that the method exists on.
     * @param methodName The name of the method.
     */
    public static String getMethodSignature(Class<?> type, String methodName)
    {
        return Types.getMethodSignature(type, methodName, Iterable.create());
    }

    /**
     * Get the method signature for the provided type, methodName, and parameterTypes. The return
     * type of the method signature will be unknown (?).
     * @param type The type of object that the method exists on.
     * @param methodName The name of the method.
     * @param parameterTypes The types of the parameters of the method.
     */
    public static String getMethodSignature(Class<?> type, String methodName, Iterable<Class<?>> parameterTypes)
    {
        return Types.getMethodSignature(type, methodName, parameterTypes, "?");
    }

    /**
     * Get the method signature for the provided type, methodName, parameterTypes, and return type.
     * @param type The type of object that the method exists on.
     * @param methodName The name of the method.
     * @param parameterTypes The types of the parameters of the method.
     * @param returnType The return type of the method.
     */
    public static String getMethodSignature(Class<?> type, String methodName, Iterable<Class<?>> parameterTypes, Class<?> returnType)
    {
        PreCondition.assertNotNull(type, "type");
        PreCondition.assertNotNullAndNotEmpty(methodName, "methodName");
        PreCondition.assertNotNull(parameterTypes, "parameterTypes");
        PreCondition.assertNotNull(returnType, "returnType");

        return Types.getMethodSignature(type, methodName, parameterTypes, Types.getFullTypeName(returnType));
    }

    /**
     * Get the method signature for the provided type, methodName, parameterTypes, and return type.
     * @param type The type of object that the method exists on.
     * @param methodName The name of the method.
     * @param parameterTypes The types of the parameters of the method.
     * @param returnTypeFullName The full name of the return type.
     */
    public static String getMethodSignature(Class<?> type, String methodName, Iterable<Class<?>> parameterTypes, String returnTypeFullName)
    {
        PreCondition.assertNotNull(type, "type");
        PreCondition.assertNotNullAndNotEmpty(methodName, "methodName");
        PreCondition.assertNotNull(parameterTypes, "parameterTypes");
        PreCondition.assertNotNullAndNotEmpty(returnTypeFullName, "returnTypeFullName");

        return Types.getFullTypeName(type) + "." + methodName + "(" + Strings.join(',', parameterTypes.map(Types::getFullTypeName)) + ") -> " + returnTypeFullName;
    }

    /**
     * Get the method object on the provided type with the provided name and parameter types.
     * @param type The class to get the method from.
     * @param methodName The name of the method to get.
     * @param parameterTypes The types of the parameters of the method to get.
     * @exception NotFoundException if the method is not found.
     */
    public static Result<java.lang.reflect.Method> getMethod(Class<?> type, String methodName, Iterable<Class<?>> parameterTypes)
    {
        PreCondition.assertNotNull(type, "type");
        PreCondition.assertNotNullAndNotEmpty(methodName, "methodName");
        PreCondition.assertNotNull(parameterTypes, "parameterTypes");

        return Result.create(() ->
        {
            java.lang.reflect.Method result;
            try
            {
                final Class<?>[] parameterTypesArray = new Class<?>[parameterTypes.getCount()];
                Array.toArray(parameterTypes, parameterTypesArray);
                result = type.getMethod(methodName, parameterTypesArray);
            }
            catch (NoSuchMethodException e)
            {
                throw new NotFoundException("Could not find a method with the signature " + Types.getMethodSignature(type, methodName, parameterTypes) + ".");
            }
            return result;
        });
    }

    /**
     * Get the static method on the provided type with the provided name and return type.
     * @param type The type to get the static method from.
     * @param methodName The name of the static method.
     * @param <TType> The type to get the static method from.
     * @exception NotFoundException if the static method is not found.
     */
    public static <TType> Result<StaticMethod0<TType,?>> getStaticMethod0(Class<TType> type, String methodName)
    {
        PreCondition.assertNotNull(type, "type");
        PreCondition.assertNotNullAndNotEmpty(methodName, "methodName");

        return Result.create(() ->
        {
            final java.lang.reflect.Method rawMethod = Types.getMethod(type, methodName, Iterable.create())
                .catchError(NotFoundException.class)
                .await();
            if (rawMethod == null ||
                !Types.isStaticMethod(rawMethod))
            {
                throw new NotFoundException("Could not find a static method with the signature " + Types.getMethodSignature(type, methodName) + ".");
            }
            return StaticMethod0.get(type, rawMethod);
        });
    }

    /**
     * Get the static method on the provided type with the provided name and return type.
     * @param type The type to get the static method from.
     * @param methodName The name of the static method.
     * @param returnType The return type of the static method.
     * @param <TType> The type to get the static method from.
     * @param <TReturn> The return type of the static method.
     * @exception NotFoundException if the method is not found.
     */
    public static <TType,TReturn> Result<StaticMethod0<TType,TReturn>> getStaticMethod0(Class<TType> type, String methodName, Class<TReturn> returnType)
    {
        PreCondition.assertNotNull(type, "type");
        PreCondition.assertNotNullAndNotEmpty(methodName, "methodName");
        PreCondition.assertNotNull(returnType, "returnType");

        return Result.create(() ->
        {
            final java.lang.reflect.Method rawMethod = Types.getMethod(type, methodName, Iterable.create())
                .catchError(NotFoundException.class)
                .await();
            if (rawMethod == null ||
                !Types.isStaticMethod(rawMethod) ||
                rawMethod.getReturnType() != returnType)
            {
                throw new NotFoundException("Could not find a static method with the signature " + Types.getMethodSignature(type, methodName, Iterable.create(), returnType) + ".");
            }
            return StaticMethod0.get(type, rawMethod);
        });
    }

    /**
     * Get the static method on the provided type with the provided name, parameter type, and
     * unknown return type.
     * @param type The type to get the static method from.
     * @param methodName The name of the static method.
     * @param arg1Type The type of the parameter.
     * @param <TType> The type to get the static method from.
     * @param <T1> The type of the parameter.
     * @exception NotFoundException if the method is not found.
     */
    public static <TType,T1> Result<StaticMethod1<TType,T1,?>> getStaticMethod1(Class<TType> type, String methodName, Class<T1> arg1Type)
    {
        PreCondition.assertNotNull(type, "type");
        PreCondition.assertNotNullAndNotEmpty(methodName, "methodName");

        return Result.create(() ->
        {
            final java.lang.reflect.Method method = Types.getMethod(type, methodName, Iterable.create(arg1Type))
                .catchError(NotFoundException.class)
                .await();
            if (method == null || !Types.isStaticMethod(method))
            {
                throw new NotFoundException("Could not find a static method with the signature " + Types.getMethodSignature(type, methodName, Iterable.create(arg1Type)) + ".");
            }
            return StaticMethod1.get(type, method);
        });
    }

    /**
     * Get the static method on the provided type with the provided name, parameter type, and return type.
     * @param type The type to get the static method from.
     * @param methodName The name of the static method.
     * @param arg1Type The type of the parameter.
     * @param returnType The return type of the static method.
     * @param <TType> The type to get the static method from.
     * @param <T1> The type of the parameter.
     * @param <TReturn> The return type of the static method.
     * @exception NotFoundException if the method is not found.
     */
    static <TType,T1,TReturn> Result<StaticMethod1<TType,T1,TReturn>> getStaticMethod1(Class<TType> type, String methodName, Class<T1> arg1Type, Class<TReturn> returnType)
    {
        PreCondition.assertNotNull(type, "type");
        PreCondition.assertNotNullAndNotEmpty(methodName, "methodName");
        PreCondition.assertNotNull(returnType, "returnType");

        return Result.create(() ->
        {
            final java.lang.reflect.Method rawMethod = Types.getMethod(type, methodName, Iterable.create(arg1Type))
                .catchError(NotFoundException.class)
                .await();
            if (rawMethod == null ||
                !Types.isStaticMethod(rawMethod) ||
                rawMethod.getReturnType() != returnType)
            {
                throw new NotFoundException("Could not find a static method with the signature " + Types.getMethodSignature(type, methodName, Iterable.create(arg1Type), returnType) + ".");
            }
            return StaticMethod1.get(type, rawMethod);
        });
    }

    /**
     * Get the type of the provided value, or null if the provided value is null.
     * @param value The value to get the type of.
     * @return The type of the provided value, or null if the provided value is null.
     */
    public static Class<?> getType(Object value)
    {
        return value == null ? null : value.getClass();
    }

    /**
     * Get whether the provided value is an instance of the provided type.
     * @param value The value to check.
     * @param type The type to check.
     */
    public static boolean instanceOf(Object value, Class<?> type)
    {
        return Types.instanceOf(Types.getType(value), type);
    }

    /**
     * Get whether the provided subType is an instance of the provided type.
     * @param subType The subType to check.
     * @param type The type to check.
     */
    public static boolean instanceOf(Class<?> subType, Class<?> type)
    {
        return (subType == type) || (subType != null && type != null && type.isAssignableFrom(subType));
    }

    /**
     * Get whether the provided value is an instance of one of the provided types.
     * @param value The value to check.
     * @param types The types to check.
     */
    static boolean instanceOf(Object value, Iterable<Class<?>> types)
    {
        PreCondition.assertNotNullAndNotEmpty(types, "types");

        return Types.instanceOf(Types.getType(value), types);
    }

    /**
     * Get whether the provided subType is an instance of one of the provided types.
     * @param subType The subType to check.
     * @param types The types to check.
     */
    static boolean instanceOf(Class<?> subType, Iterable<Class<?>> types)
    {
        PreCondition.assertNotNullAndNotEmpty(types, "types");

        return types.contains((Class<?> type) -> Types.instanceOf(subType, type));
    }

    /**
     * Get the provided value cast as type {@link T} if it is an instance of {@link T}, or null if
     * it is not an instance of {@link T}.
     * @param value The value to cast.
     * @param type The type to cast the value to.
     * @param <T> The type to cast the value to.
     */
    @SuppressWarnings("unchecked")
    public static <T> T as(Object value, Class<T> type)
    {
        PreCondition.assertNotNull(type, "type");

        return Types.instanceOf(value, type)
            ? (T)value
            : null;
    }

    /**
     * Get the simple name (without the package path) of the provided full type name.
     * @param fullTypeName The full type name to get the simple name of.
     */
    public static String getTypeNameFromFullTypeName(String fullTypeName)
    {
        PreCondition.assertNotNullAndNotEmpty(fullTypeName, "fullTypeName");

        final int lastDot = fullTypeName.lastIndexOf('.');
        return lastDot < 0 ? fullTypeName : fullTypeName.substring(lastDot + 1);
    }

    /**
     * Get the simple name (without the package path) of the provided type.
     * @param type The type to get the simple name of.
     */
    public static String getTypeName(Class<?> type)
    {
        return type == null ? "null" : type.getSimpleName();
    }

    /**
     * Get the simple name (without the package path) of the runtime type of the provided value.
     * @param value The value to get the simple name of the runtime type of.
     */
    public static String getTypeName(Object value)
    {
        return Types.getTypeName(value == null ? null : value.getClass());
    }

    /**
     * Get the full name of the provided type.
     * @param type The type to get the full name of.
     */
    public static String getFullTypeName(Class<?> type)
    {
        return type == null ? "null" : type.getCanonicalName();
    }

    /**
     * Get the full name of the runtime type of the provided value.
     * @param value The value to get the full name of the runtime type of.
     */
    public static String getFullTypeName(Object value)
    {
        return Types.getFullTypeName(value == null ? null : value.getClass());
    }
}
