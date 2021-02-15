package qub;

public abstract class JSONObjectWrapperBase implements JSONObjectWrapper
{
    protected final JSONObject json;

    protected JSONObjectWrapperBase(JSONObject json)
    {
        PreCondition.assertNotNull(json, "json");

        this.json = json;
    }

    @Override
    public JSONObject toJson()
    {
        return this.json;
    }

    @Override
    public String toString()
    {
        return JSONObjectWrapper.toString(this);
    }

    @Override
    public boolean equals(Object rhs)
    {
        return JSONObjectWrapper.equals(this, rhs);
    }
}
