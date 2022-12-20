package qub;

/**
 * A method from a class.
 */
public class Method0<TType,TReturn> implements Function1<TType,TReturn>
{
    private final Class<TType> type;
    private final java.lang.reflect.Method method;

    /**
     * Create a new reference to a method.
     * @param type The type that the method exists on.
     * @param method The raw reference to the method.
     */
    private Method0(Class<TType> type, java.lang.reflect.Method method)
    {
        PreCondition.assertNotNull(type, "type");
        PreCondition.assertNotNull(method, "Method");

        this.type = type;
        this.method = method;
    }

    /**
     * Create a new reference to a method.
     * @param type The type that the method exists on.
     * @param staticMethod The method object that refers to the method.
     * @param <TType> The type of the type that contains the method.
     * @param <TReturn> The type of value that is returned from the method.
     * @return The method reference.
     */
    public static <TType,TReturn> Method0<TType,TReturn> create(Class<TType> type, java.lang.reflect.Method staticMethod)
    {
        return new Method0<>(type, staticMethod);
    }

    /**
     * Get the type that this method comes from.
     */
    public Class<TType> getType()
    {
        return this.type;
    }

    @Override
    @SuppressWarnings("unchecked")
    public TReturn run(TType caller)
    {
        try
        {
            return (TReturn)this.method.invoke(caller);
        }
        catch (java.lang.reflect.InvocationTargetException | java.lang.IllegalAccessException e)
        {
            throw Exceptions.asRuntime(e.getCause());
        }
    }
}
