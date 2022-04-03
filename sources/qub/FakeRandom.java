package qub;

/**
 * A Random implementation that a value based on a provided function.
 */
public class FakeRandom implements Random
{
    private Function0<Integer> creator;

    private FakeRandom(Function0<Integer> creator)
    {
        PreCondition.assertNotNull(creator, "creator");

        this.creator = creator;
    }

    public static FakeRandom create(Function0<Integer> creator)
    {
        return new FakeRandom(creator);
    }

    public static FakeRandom create(int value)
    {
        return FakeRandom.create(() -> value);
    }

    public static FakeRandom create()
    {
        return FakeRandom.create(0);
    }

    public FakeRandom setCreator(Function0<Integer> creator)
    {
        PreCondition.assertNotNull(creator, "creator");

        this.creator = creator;
        
        return this;
    }

    /**
     * Set the value that will be returned.
     * @param value The value that will be returned.
     */
    public FakeRandom setValue(int value)
    {
        return this.setCreator(() -> value);
    }

    @Override
    public int getRandomInteger()
    {
        final Integer result = this.creator.run();

        PostCondition.assertNotNull(result, "result");

        return result.intValue();
    }
}
