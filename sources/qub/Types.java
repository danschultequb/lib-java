package qub;

public interface Types
{
    static boolean containsMemberVariable(Object value, String memberVariableName)
    {
        PreCondition.assertNotNull(value, "value");
        PreCondition.assertNotNullAndNotEmpty(memberVariableName, "memberVariableName");

        return containsMemberVariable(value.getClass(), memberVariableName);
    }

    static boolean containsMemberVariable(Class<?> type, String memberVariableName)
    {
        PreCondition.assertNotNull(type, "type");
        PreCondition.assertNotNullAndNotEmpty(memberVariableName, "memberVariableName");

        return containsMemberVariable(type, (java.lang.reflect.Field field) -> field.getName().equals(memberVariableName));
    }

    static boolean containsMemberVariable(Object value, Function1<java.lang.reflect.Field,Boolean> condition)
    {
        PreCondition.assertNotNull(value, "value");
        PreCondition.assertNotNull(condition, "condition");

        return containsMemberVariable(value.getClass(), condition);
    }

    static boolean containsMemberVariable(Class<?> type, Function1<java.lang.reflect.Field,Boolean> condition)
    {
        PreCondition.assertNotNull(type, "type");
        PreCondition.assertNotNull(condition, "condition");

        final Iterable<java.lang.reflect.Field> fields = getMemberVariables(type);
        return fields.contains(condition);
    }

    static java.lang.reflect.Field getMemberVariable(Object value, String memberVariableName)
    {
        PreCondition.assertNotNull(value, "value");
        PreCondition.assertNotNullAndNotEmpty(memberVariableName, "memberVariableName");
        PreCondition.assertTrue(containsMemberVariable(value, memberVariableName), "containsMemberVariable(value, memberVariableName)");

        return getMemberVariable(value.getClass(), memberVariableName);
    }

    static java.lang.reflect.Field getMemberVariable(Class<?> type, final String memberVariableName)
    {
        PreCondition.assertNotNull(type, "type");
        PreCondition.assertNotNullAndNotEmpty(memberVariableName, "memberVariableName");
        PreCondition.assertTrue(containsMemberVariable(type, memberVariableName), "containsMemberVariable(type, memberVariableName)");

        return getMemberVariable(type, (java.lang.reflect.Field field) -> field.getName().equals(memberVariableName));
    }

    static java.lang.reflect.Field getMemberVariable(Object value, Function1<java.lang.reflect.Field,Boolean> condition)
    {
        PreCondition.assertNotNull(value, "value");
        PreCondition.assertNotNull(condition, "condition");
        PreCondition.assertTrue(containsMemberVariable(value, condition), "containsMemberVariable(value, condition)");

        return getMemberVariable(value.getClass(), condition);
    }

    static java.lang.reflect.Field getMemberVariable(Class<?> type, Function1<java.lang.reflect.Field,Boolean> condition)
    {
        PreCondition.assertNotNull(type, "type");
        PreCondition.assertNotNull(condition, "condition");
        PreCondition.assertTrue(containsMemberVariable(type, condition), "containsMemberVariable(type, condition)");

        return getMemberVariables(type).first(condition);
    }

    static Object getMemberVariableValue(Object value, String memberVariableName)
    {
        PreCondition.assertNotNull(value, "value");
        PreCondition.assertNotNullAndNotEmpty(memberVariableName, "memberVariableName");
        PreCondition.assertTrue(containsMemberVariable(value, memberVariableName), "containsMemberVariable(value, memberVariableName)");

        final java.lang.reflect.Field field = getMemberVariable(value, memberVariableName);
        Object result = null;
        try
        {
            result = field.get(value);
        }
        catch (IllegalAccessException e)
        {
            @SuppressWarnings("deprecation")
            final boolean accessible = field.isAccessible();
            field.setAccessible(true);
            try
            {
                result = field.get(value);
            }
            catch (IllegalAccessException e1)
            {
                throw Exceptions.asRuntime(e1);
            }
            finally
            {
                field.setAccessible(accessible);
            }
        }

        return result;
    }

    static void setMemberVariableValue(Object value, String memberVariableName, Object memberVariableValue)
    {
        PreCondition.assertNotNull(value, "value");
        PreCondition.assertNotNullAndNotEmpty(memberVariableName, "memberVariableName");
        PreCondition.assertTrue(containsMemberVariable(value, memberVariableName), "containsMemberVariable(value, memberVariableName)");

        final java.lang.reflect.Field field = getMemberVariable(value, memberVariableName);
        try
        {
            field.set(value, memberVariableValue);
        }
        catch (IllegalAccessException e)
        {
            @SuppressWarnings("deprecation")
            final boolean accessible = field.isAccessible();
            field.setAccessible(true);
            try
            {
                field.set(value, memberVariableValue);
            }
            catch (IllegalAccessException e1)
            {
                throw Exceptions.asRuntime(e1);
            }
            finally
            {
                field.setAccessible(accessible);
            }
        }
    }

    static Iterable<java.lang.reflect.Field> getMemberVariables(Object value)
    {
        PreCondition.assertNotNull(value, "value");

        return getMemberVariables(value.getClass());
    }

    static Iterable<java.lang.reflect.Field> getMemberVariables(Class<?> type)
    {
        PreCondition.assertNotNull(type, "type");

        final List<java.lang.reflect.Field> result = new ArrayList<>();
        while (type != null)
        {
            final java.lang.reflect.Field[] fields = type.getDeclaredFields();
            result.addAll(fields);
            type = type.getSuperclass();
        }

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    /**
     * Get the methods of the provided type.
     * @param type The type to get the methods for.
     * @return The methods of the provided type.
     */
    static Iterable<java.lang.reflect.Method> getMethods(Class<?> type)
    {
        PreCondition.assertNotNull(type, "type");

        final Iterable<java.lang.reflect.Method> result = Iterable.create(type.getMethods());

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    /**
     * Get whether or not the provided method is static.
     * @param method The method to check.
     * @return Whether or not the provided method is static.
     */
    static boolean isStaticMethod(java.lang.reflect.Method method)
    {
        PreCondition.assertNotNull(method, "method");

        return java.lang.reflect.Modifier.isStatic(method.getModifiers());
    }

    /**
     * Get the method signature for the provided type, methodName, and parameterTypes. The return
     * type of the method signature will be unknown (?).
     * @param type The type of object that the method exists on.
     * @param methodName The name of the method.
     * @param parameterTypes The types of the parameters of the method.
     * @return The String representation of the method signature.
     */
    static String getMethodSignature(Class<?> type, String methodName, Class<?>... parameterTypes)
    {
        PreCondition.assertNotNull(type, "type");
        PreCondition.assertNotNullAndNotEmpty(methodName, "methodName");
        PreCondition.assertNotNull(parameterTypes, "parameterTypes");

        final String result = Types.getMethodSignature(type, methodName, Iterable.create(parameterTypes));

        PostCondition.assertNotNullAndNotEmpty(result, "result");

        return result;
    }

    /**
     * Get the method signature for the provided type, methodName, and parameterTypes. The return
     * type of the method signature will be unknown (?).
     * @param type The type of object that the method exists on.
     * @param methodName The name of the method.
     * @param parameterTypes The types of the parameters of the method.
     * @return The String representation of the method signature.
     */
    static String getMethodSignature(Class<?> type, String methodName, Iterable<Class<?>> parameterTypes)
    {
        PreCondition.assertNotNull(type, "type");
        PreCondition.assertNotNullAndNotEmpty(methodName, "methodName");
        PreCondition.assertNotNull(parameterTypes, "parameterTypes");

        final String result = Types.getMethodSignature(type, methodName, parameterTypes, "?");

        PostCondition.assertNotNullAndNotEmpty(result, "result");

        return result;
    }

    /**
     * Get the method signature for the provided type, methodName, and parameterTypes. The return
     * type of the method signature will be unknown (?).
     * @param type The type of object that the method exists on.
     * @param methodName The name of the method.
     * @param parameterTypes The types of the parameters of the method.
     * @return The String representation of the method signature.
     */
    static String getMethodSignature(Class<?> type, String methodName, Iterable<Class<?>> parameterTypes, Class<?> returnType)
    {
        PreCondition.assertNotNull(type, "type");
        PreCondition.assertNotNullAndNotEmpty(methodName, "methodName");
        PreCondition.assertNotNull(parameterTypes, "parameterTypes");
        PreCondition.assertNotNull(returnType, "returnType");

        final String result = Types.getMethodSignature(type, methodName, parameterTypes, Types.getFullTypeName(returnType));

        PostCondition.assertNotNullAndNotEmpty(result, "result");

        return result;
    }

    /**
     * Get the method signature for the provided type, methodName, and parameterTypes. The return
     * type of the method signature will be unknown (?).
     * @param type The type of object that the method exists on.
     * @param methodName The name of the method.
     * @param parameterTypes The types of the parameters of the method.
     * @return The String representation of the method signature.
     */
    static String getMethodSignature(Class<?> type, String methodName, Iterable<Class<?>> parameterTypes, String returnTypeFullName)
    {
        PreCondition.assertNotNull(type, "type");
        PreCondition.assertNotNullAndNotEmpty(methodName, "methodName");
        PreCondition.assertNotNull(parameterTypes, "parameterTypes");
        PreCondition.assertNotNullAndNotEmpty(returnTypeFullName, "returnTypeFullName");

        final String result = Types.getFullTypeName(type) + "." + methodName + "(" + Strings.join(',', parameterTypes.map(Types::getFullTypeName)) + ") -> " + returnTypeFullName;

        PostCondition.assertNotNullAndNotEmpty(result, "result");

        return result;
    }

    /**
     * Get the raw method object on the provided type with the provided name.
     * @param type The class to get the method from.
     * @param methodName The name of the method to get.
     * @param parameterTypes The types of the parameters of the method to get.
     * @return The requested raw method object.
     */
    static Result<java.lang.reflect.Method> getRawMethod(Class<?> type, String methodName, Class<?>... parameterTypes)
    {
        PreCondition.assertNotNull(type, "type");
        PreCondition.assertNotNullAndNotEmpty(methodName, "methodName");
        PreCondition.assertNotNull(parameterTypes, "parameterTypes");

        Result<java.lang.reflect.Method> result;
        try
        {
            final java.lang.reflect.Method method = type.getMethod(methodName, parameterTypes);
            result = Result.success(method);
        }
        catch (Throwable e)
        {
            result = Result.error(new NotFoundException("No method with the signature " + Types.getMethodSignature(type, methodName, parameterTypes) + " could be found."));
        }

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    /**
     * Get the static method on the provided type with the provided name and return type.
     * @param type The type to get the static method from.
     * @param methodName The name of the static method.
     * @param <TType> The type to get the static method from.
     * @return The matching StaticMethod0 object.
     */
    static <TType> Result<StaticMethod0<TType,?>> getStaticMethod(Class<TType> type, String methodName)
    {
        PreCondition.assertNotNull(type, "type");
        PreCondition.assertNotNullAndNotEmpty(methodName, "methodName");

        final java.lang.reflect.Method rawMethod = Types.getRawMethod(type, methodName)
            .catchError(NotFoundException.class)
            .await();
        final StaticMethod0<TType,?> staticMethod = rawMethod != null && Types.isStaticMethod(rawMethod)
            ? StaticMethod0.get(type, rawMethod)
            : null;
        final Result<StaticMethod0<TType,?>> result = staticMethod != null
            ? Result.success(staticMethod)
            : Result.error(new NotFoundException("No static method with the signature " + Types.getMethodSignature(type, methodName) + " could be found."));

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    /**
     * Get the static method on the provided type with the provided name and return type.
     * @param type The type to get the static method from.
     * @param methodName The name of the static method.
     * @param returnType The return type of the static method.
     * @param <TType> The type to get the static method from.
     * @param <TReturn> The return type of the static method.
     * @return The matching StaticMethod0 object.
     */
    static <TType,TReturn> Result<StaticMethod0<TType,TReturn>> getStaticMethod(Class<TType> type, String methodName, Class<TReturn> returnType)
    {
        PreCondition.assertNotNull(type, "type");
        PreCondition.assertNotNullAndNotEmpty(methodName, "methodName");
        PreCondition.assertNotNull(returnType, "returnType");

        final java.lang.reflect.Method rawMethod = Types.getRawMethod(type, methodName)
            .catchError(NotFoundException.class)
            .await();
        final StaticMethod0<TType,TReturn> staticMethod = rawMethod != null && Types.isStaticMethod(rawMethod) && rawMethod.getReturnType() == returnType
            ? StaticMethod0.get(type, rawMethod)
            : null;
        final Result<StaticMethod0<TType,TReturn>> result = staticMethod != null
            ? Result.success(staticMethod)
            : Result.error(new NotFoundException("No static method with the signature " + Types.getMethodSignature(type, methodName, Iterable.create(), returnType) + " could be found."));

        PostCondition.assertNotNull(result, "result");

        return result;
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
     * @return The matching StaticMethod1 object.
     */
    static <TType,T1,TReturn> Result<StaticMethod1<TType,T1,TReturn>> getStaticMethod(Class<TType> type, String methodName, Class<T1> arg1Type, Class<TReturn> returnType)
    {
        PreCondition.assertNotNull(type, "type");
        PreCondition.assertNotNullAndNotEmpty(methodName, "methodName");
        PreCondition.assertNotNull(returnType, "returnType");

        final java.lang.reflect.Method rawMethod = Types.getRawMethod(type, methodName, arg1Type)
            .catchError(NotFoundException.class)
            .await();
        final StaticMethod1<TType,T1,TReturn> staticMethod = rawMethod != null && Types.isStaticMethod(rawMethod) && rawMethod.getReturnType() == returnType
            ? StaticMethod1.get(type, rawMethod)
            : null;
        final Result<StaticMethod1<TType,T1,TReturn>> result = staticMethod != null
            ? Result.success(staticMethod)
            : Result.error(new NotFoundException("No static method with the signature " + Types.getMethodSignature(type, methodName, Iterable.create(arg1Type), returnType) + " could be found."));

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    static boolean instanceOf(Object value, Class<?> type)
    {
        PreCondition.assertNotNull(type, "type");

        return value != null && instanceOf(value.getClass(), type);
    }

    static boolean instanceOf(Class<?> valueType, Class<?> type)
    {
        PreCondition.assertNotNull(type, "type");

        return valueType != null && type.isAssignableFrom(valueType);
    }

    @SuppressWarnings("unchecked")
    static <T> T as(Object value, Class<T> type)
    {
        T result;
        if (Types.instanceOf(value, type))
        {
            result = (T)value;
        }
        else
        {
            result = null;
        }
        return result;
    }

    /**
     * Get the Class object that is associated with the provided full class name.
     * @param fullClassName The full name of the class to get.
     * @return The Class object associated with the provided full class name.
     */
    static Result<Class<?>> getClass(String fullClassName)
    {
        PreCondition.assertNotNullAndNotEmpty(fullClassName, "fullClassName");

        Result<Class<?>> result;
        try
        {
            final Class<?> typesClass = Types.class;
            final ClassLoader classLoader = typesClass.getClassLoader();
            result = Result.success(classLoader.loadClass(fullClassName));
        }
        catch (ClassNotFoundException e)
        {
            result = Result.error(new NotFoundException("Could not load a class named " + Strings.escapeAndQuote(fullClassName) + "."));
        }

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    static String getTypeName(Class<?> type)
    {
        PreCondition.assertNotNull(type, "type");

        return type.getSimpleName();
    }

    static String getTypeName(Object value)
    {
        PreCondition.assertNotNull(value, "value");

        return getTypeName(value.getClass());
    }

    static String getFullTypeName(Class<?> type)
    {
        PreCondition.assertNotNull(type, "type");

        return type.getCanonicalName();
    }

    static String getFullTypeName(Object value)
    {
        PreCondition.assertNotNull(value, "value");

        return getFullTypeName(value.getClass());
    }
}
