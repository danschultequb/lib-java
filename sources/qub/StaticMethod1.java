package qub;

/**
 * A static method from a class.
 */
public class StaticMethod1<TType,T1,TReturn> implements Function1<T1,TReturn>
{
    private final Class<TType> type;
    private final java.lang.reflect.Method staticMethod;

    /**
     * Create a new reference to a static method.
     * @param type The type that the static method exists on.
     * @param staticMethod The raw reference to the method.
     */
    private StaticMethod1(Class<TType> type, java.lang.reflect.Method staticMethod)
    {
        this.type = type;
        this.staticMethod = staticMethod;
    }

    /**
     * Create a new reference to a static method.
     * @param type The type that the static method exists on.
     * @param staticMethod The method object that refers to the static method.
     * @param <TType> The type of the type that contains the static method.
     * @param <TReturn> The type of value that is returned from the static method.
     * @return The static method reference.
     */
    public static <TType,T1,TReturn> StaticMethod1<TType,T1,TReturn> get(Class<TType> type, java.lang.reflect.Method staticMethod)
    {
        PreCondition.assertNotNull(type, "type");
        PreCondition.assertNotNull(staticMethod, "staticMethod");

        return new StaticMethod1<>(type, staticMethod);
    }

    /**
     * Get the type that this static method comes from.
     * @return The type that this static method comes from.
     */
    public Class<TType> getType()
    {
        return type;
    }

    @Override
    @SuppressWarnings("unchecked")
    public TReturn run(T1 arg1)
    {
        try
        {
            return (TReturn)staticMethod.invoke(null, arg1);
        }
        catch (java.lang.reflect.InvocationTargetException | java.lang.IllegalAccessException e)
        {
            throw Exceptions.asRuntime(e.getCause());
        }
    }
}
