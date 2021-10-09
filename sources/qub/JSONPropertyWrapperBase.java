package qub;

public abstract class JSONPropertyWrapperBase implements JSONPropertyWrapper
{
    private final JSONProperty innerProperty;

    protected JSONPropertyWrapperBase(JSONProperty innerProperty)
    {
        PreCondition.assertNotNull(innerProperty, "innerProperty");

        this.innerProperty = innerProperty;
    }

    @Override
    public JSONProperty toJson()
    {
        return this.innerProperty;
    }

    @Override
    public String toString()
    {
        return JSONPropertyWrapper.toString(this);
    }

    @Override
    public boolean equals(Object rhs)
    {
        return JSONPropertyWrapper.equals(this, rhs);
    }
}
