package qub;

public class BasicUIBuilder implements UIBuilder
{
    private final MutableMap<Class<? extends UIElement>,Function0<? extends UIElement>> uiCreators;

    protected BasicUIBuilder()
    {
        this.uiCreators = Map.create();
    }

    public static BasicUIBuilder create()
    {
        return new BasicUIBuilder();
    }

    @Override
    public <U extends UIElement, T extends U> UIBuilder setCreator(Class<? extends U> uiElementType, Function0<T> uiElementCreator)
    {
        PreCondition.assertNotNull(uiElementType, "uiElementType");
        PreCondition.assertNotNull(uiElementCreator, "uiElementCreator");

        this.uiCreators.set(uiElementType, uiElementCreator);

        return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends UIElement> Result<T> create(Class<T> uiElementType)
    {
        PreCondition.assertNotNull(uiElementType, "uiElementType");

        return this.uiCreators.get(uiElementType)
            .convertError(NotFoundException.class, () -> new NotFoundException("No UI creator function registered for UIElement type " + Types.getFullTypeName(uiElementType) + "."))
            .then((Function0<? extends UIElement> creator) -> (T)creator.run());
    }
}
