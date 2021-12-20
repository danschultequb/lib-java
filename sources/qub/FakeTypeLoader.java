package qub;

public class FakeTypeLoader implements TypeLoader
{
    private final MutableMap<String,Class<?>> typeMap;
    private final MutableMap<String,String> typeContainerPathMap;

    private FakeTypeLoader()
    {
        this.typeMap = Map.create();
        this.typeContainerPathMap = Map.create();
    }

    public static FakeTypeLoader create()
    {
        return new FakeTypeLoader();
    }

    public FakeTypeLoader addType(String fullTypeName, Class<?> type)
    {
        PreCondition.assertNotNullAndNotEmpty(fullTypeName, "fullTypeName");
        PreCondition.assertNotNull(type, "type");

        this.typeMap.set(fullTypeName, type);

        return this;
    }

    public FakeTypeLoader addType(Class<?> type)
    {
        PreCondition.assertNotNull(type, "type");

        return this.addType(Types.getFullTypeName(type), type);
    }

    @Override
    public Result<Class<?>> getType(String fullTypeName)
    {
        PreCondition.assertNotNullAndNotEmpty(fullTypeName, "fullTypeName");

        return this.typeMap.get(fullTypeName)
            .convertError(NotFoundException.class, () -> new NotFoundException("Could not load a class named " + Strings.escapeAndQuote(fullTypeName) + "."));
    }

    public FakeTypeLoader addTypeContainer(String fullTypeName, String typeContainerPath)
    {
        PreCondition.assertNotNullAndNotEmpty(fullTypeName, "fullTypeName");
        PreCondition.assertNotNullAndNotEmpty(typeContainerPath, "typeContainerPath");

        this.typeContainerPathMap.set(fullTypeName, typeContainerPath);

        return this;
    }

    public FakeTypeLoader addTypeContainer(String fullTypeName, Path typeContainerPath)
    {
        PreCondition.assertNotNullAndNotEmpty(fullTypeName, "fullTypeName");
        PreCondition.assertNotNull(typeContainerPath, "typeContainerPath");
        PreCondition.assertTrue(typeContainerPath.isRooted(), "typeContainerPath.isRooted()");

        return this.addTypeContainer(fullTypeName, typeContainerPath.toString());
    }

    public FakeTypeLoader addTypeContainer(String fullTypeName, File typeContainer)
    {
        PreCondition.assertNotNullAndNotEmpty(fullTypeName, "fullTypeName");
        PreCondition.assertNotNull(typeContainer, "typeContainer");

        return this.addTypeContainer(fullTypeName, typeContainer.toString());
    }

    public FakeTypeLoader addTypeContainer(String fullTypeName, Folder typeContainer)
    {
        PreCondition.assertNotNullAndNotEmpty(fullTypeName, "fullTypeName");
        PreCondition.assertNotNull(typeContainer, "typeContainer");

        return this.addTypeContainer(fullTypeName, typeContainer.toString());
    }

    public FakeTypeLoader addTypeContainer(java.lang.Class<?> type, String typeContainerPath)
    {
        PreCondition.assertNotNull(type, "type");
        PreCondition.assertNotNullAndNotEmpty(typeContainerPath, "typeContainerPath");

        return this.addTypeContainer(Types.getFullTypeName(type), typeContainerPath);
    }

    public FakeTypeLoader addTypeContainer(java.lang.Class<?> type, Path typeContainerPath)
    {
        PreCondition.assertNotNull(type, "type");
        PreCondition.assertNotNull(typeContainerPath, "typeContainerPath");

        return this.addTypeContainer(Types.getFullTypeName(type), typeContainerPath);
    }

    public FakeTypeLoader addTypeContainer(java.lang.Class<?> type, File typeContainer)
    {
        PreCondition.assertNotNull(type, "type");
        PreCondition.assertNotNull(typeContainer, "typeContainer");

        return this.addTypeContainer(Types.getFullTypeName(type), typeContainer);
    }

    public FakeTypeLoader addTypeContainer(java.lang.Class<?> type, Folder typeContainer)
    {
        PreCondition.assertNotNull(type, "type");
        PreCondition.assertNotNull(typeContainer, "typeContainer");

        return this.addTypeContainer(Types.getFullTypeName(type), typeContainer);
    }

    @Override
    public Result<String> getTypeContainerPathString(String fullTypeName)
    {
        PreCondition.assertNotNullAndNotEmpty(fullTypeName, "fullTypeName");

        return this.typeContainerPathMap.get(fullTypeName)
            .convertError(NotFoundException.class, () -> new NotFoundException("Could not find a type container for a type named " + Strings.escapeAndQuote(fullTypeName) + "."));
    }
}
