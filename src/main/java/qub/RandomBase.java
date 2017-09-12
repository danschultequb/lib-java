package qub;

/**
 * A abstract base Random implementation of the Random interface where commonly implemented
 * functions will go.
 */
public abstract class RandomBase implements Random
{
    /**
     * Get a random integer between the provided lower and upper bounds. Both the lower and upper
     * bounds are inclusive.
     * @param lowerBound The inclusive lower bound.
     * @param upperBound The inclusive upper bound.
     * @return A random integer between the provided lower and upper bounds.
     */
    @Override
    public int getRandomIntegerBetween(int lowerBound, int upperBound)
    {
        if (upperBound < lowerBound)
        {
            final int temp = lowerBound;
            lowerBound = upperBound;
            upperBound = temp;
        }

        int result;

        final int rangeSize = upperBound - lowerBound;
        if (rangeSize == 0)
        {
            result = lowerBound;
        }
        else
        {
            final int randomInteger = getRandomInteger();
            final int scaledRandomInteger = randomInteger % (rangeSize + 1);
            result = scaledRandomInteger + lowerBound;
        }

        return result;
    }

    @Override
    public boolean getRandomBoolean()
    {
        return getRandomInteger() % 2 == 0;
    }
}
