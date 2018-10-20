package qub;

public class Types
{
    Types()
    {
    }

    public static boolean containsMemberVariable(Object value, String memberVariableName)
    {
        PreCondition.assertNotNull(value, "value");
        PreCondition.assertNotNullAndNotEmpty(memberVariableName, "memberVariableName");

        return containsMemberVariable(value.getClass(), memberVariableName);
    }

    public static boolean containsMemberVariable(Class<?> type, final String memberVariableName)
    {
        PreCondition.assertNotNull(type, "type");
        PreCondition.assertNotNullAndNotEmpty(memberVariableName, "memberVariableName");

        return containsMemberVariable(type, (java.lang.reflect.Field field) -> field.getName().equals(memberVariableName));
    }

    public static boolean containsMemberVariable(Object value, Function1<java.lang.reflect.Field,Boolean> condition)
    {
        PreCondition.assertNotNull(value, "value");
        PreCondition.assertNotNull(condition, "condition");

        return containsMemberVariable(value.getClass(), condition);
    }

    public static boolean containsMemberVariable(Class<?> type, Function1<java.lang.reflect.Field,Boolean> condition)
    {
        PreCondition.assertNotNull(type, "type");
        PreCondition.assertNotNull(condition, "condition");

        final Iterable<java.lang.reflect.Field> fields = getMemberVariables(type);
        return fields.contains(condition);
    }

    public static java.lang.reflect.Field getMemberVariable(Object value, String memberVariableName)
    {
        PreCondition.assertNotNull(value, "value");
        PreCondition.assertNotNullAndNotEmpty(memberVariableName, "memberVariableName");
        PreCondition.assertTrue(containsMemberVariable(value, memberVariableName), "containsMemberVariable(value, memberVariableName)");

        return getMemberVariable(value.getClass(), memberVariableName);
    }

    public static java.lang.reflect.Field getMemberVariable(Class<?> type, final String memberVariableName)
    {
        PreCondition.assertNotNull(type, "type");
        PreCondition.assertNotNullAndNotEmpty(memberVariableName, "memberVariableName");
        PreCondition.assertTrue(containsMemberVariable(type, memberVariableName), "containsMemberVariable(type, memberVariableName)");

        return getMemberVariable(type, (java.lang.reflect.Field field) -> field.getName().equals(memberVariableName));
    }

    public static java.lang.reflect.Field getMemberVariable(Object value, Function1<java.lang.reflect.Field,Boolean> condition)
    {
        PreCondition.assertNotNull(value, "value");
        PreCondition.assertNotNull(condition, "condition");
        PreCondition.assertTrue(containsMemberVariable(value, condition), "containsMemberVariable(value, condition)");

        return getMemberVariable(value.getClass(), condition);
    }

    public static java.lang.reflect.Field getMemberVariable(Class<?> type, Function1<java.lang.reflect.Field,Boolean> condition)
    {
        PreCondition.assertNotNull(type, "type");
        PreCondition.assertNotNull(condition, "condition");
        PreCondition.assertTrue(containsMemberVariable(type, condition), "containsMemberVariable(type, condition)");

        return getMemberVariables(type).first(condition);
    }

    public static Object getMemberVariableValue(Object value, String memberVariableName)
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

    public static void setMemberVariableValue(Object value, String memberVariableName, Object memberVariableValue)
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

    public static Iterable<java.lang.reflect.Field> getMemberVariables(Object value)
    {
        PreCondition.assertNotNull(value, "value");

        return getMemberVariables(value.getClass());
    }

    public static Iterable<java.lang.reflect.Field> getMemberVariables(Class<?> type)
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

    public static boolean instanceOf(Object value, Class<?> type)
    {
        PreCondition.assertNotNull(type, "type");

        return value != null && instanceOf(value.getClass(), type);
    }

    public static boolean instanceOf(Class<?> valueType, Class<?> type)
    {
        PreCondition.assertNotNull(type, "type");

        return valueType != null && type.isAssignableFrom(valueType);
    }

    @SuppressWarnings("unchecked")
    public static <T> T as(Object value, Class<T> type)
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

    public static String getTypeName(Class<?> type)
    {
        PreCondition.assertNotNull(type, "type");

        return type.getSimpleName();
    }

    public static String getTypeName(Object value)
    {
        PreCondition.assertNotNull(value, "value");

        return getTypeName(value.getClass());
    }

    public static String getFullTypeName(Class<?> type)
    {
        PreCondition.assertNotNull(type, "type");

        return type.getCanonicalName();
    }

    public static String getFullTypeName(Object value)
    {
        PreCondition.assertNotNull(value, "value");

        return getFullTypeName(value.getClass());
    }
}
