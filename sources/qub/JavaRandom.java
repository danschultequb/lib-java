package qub;

/**
 * A random number generator that uses Java's internal Random class.
 */
public class JavaRandom implements Random
{
    private final java.util.Random random;

    /**
     * Create a new JavaRandom object.
     */
    public JavaRandom()
    {
        random = new java.util.Random();
    }

    @Override
    public int getRandomInteger()
    {
        final int value = random.nextInt();
        return value < 0 ? -value : value;
    }
}
