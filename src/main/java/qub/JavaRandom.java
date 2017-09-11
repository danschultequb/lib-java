package qub;

/**
 * A random number generator that uses Java's internal Random class.
 */
public class JavaRandom extends RandomBase
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
        return random.nextInt();
    }
}
