package qub;

/**
 * A static method from a class.
 */
public class StaticMethod0<TType,TReturn> implements Function0<TReturn>
{
    private final Class<TType> type;
    private final java.lang.reflect.Method staticMethod;

    /**
     * Create a new reference to a static method.
     * @param type The type that the static method exists on.
     * @param staticMethod The raw reference to the method.
     */
    private StaticMethod0(Class<TType> type, java.lang.reflect.Method staticMethod)
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
    public static <TType,TReturn> StaticMethod0<TType,TReturn> get(Class<TType> type, java.lang.reflect.Method staticMethod)
    {
        PreCondition.assertNotNull(type, "type");
        PreCondition.assertNotNull(staticMethod, "staticMethod");

        return new StaticMethod0<>(type, staticMethod);
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
    public TReturn run()
    {
        try
        {
            return (TReturn)staticMethod.invoke(null);
        }
        catch (java.lang.reflect.InvocationTargetException e)
        {
            throw Exceptions.asRuntime(e.getCause());
        }
        catch (Throwable e)
        {
            throw Exceptions.asRuntime(e);
        }
    }
}
