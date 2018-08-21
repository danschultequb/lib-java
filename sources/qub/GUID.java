package qub;

public class GUID
{
    private final java.util.UUID uuid;

    private GUID(java.util.UUID uuid)
    {
        this.uuid = uuid;
    }

    @Override
    public boolean equals(Object rhs)
    {
        return rhs instanceof GUID && equals((GUID)rhs);
    }

    public boolean equals(GUID rhs)
    {
        return rhs != null && uuid.equals(rhs.uuid);
    }

    @Override
    public String toString()
    {
        return uuid.toString();
    }

    public static GUID createRandom()
    {
        return new GUID(java.util.UUID.randomUUID());
    }

    public static GUID parseString(String guidString)
    {
        return new GUID(java.util.UUID.fromString(guidString));
    }
}
