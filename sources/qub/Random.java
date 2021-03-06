package qub;

/**
 * A random number generator.
 */
public interface Random
{
    /**
     * Get a random integer greater than or equal to 0.
     * @return A random integer greater than or equal to 0.
     */
    int getRandomInteger();

    /**
     * Get a random integer between the provided lower and upper bounds. Both the lower and upper
     * bounds are inclusive.
     * @param lowerBound The inclusive lower bound.
     * @param upperBound The inclusive upper bound.
     * @return A random integer between the provided lower and upper bounds.
     */
    default int getRandomIntegerBetween(int lowerBound, int upperBound)
    {
        if (upperBound < lowerBound)
        {
            final int temp = lowerBound;
            lowerBound = upperBound;
            upperBound = temp;
        }

        int result;

        final int rangeSize = upperBound - lowerBound + 1;
        if (rangeSize == 1)
        {
            result = lowerBound;
        }
        else
        {
            final int randomInteger = getRandomInteger();
            final int scaledRandomInteger = randomInteger % rangeSize;
            result = scaledRandomInteger + lowerBound;
        }

        return result;
    }

    /**
     * Get a random boolean value.
     * @return A random boolean value.
     */
    default boolean getRandomBoolean()
    {
        return getRandomInteger() % 2 == 0;
    }
}
