package qub;

public abstract class RandomBase implements Random
{
    @Override
    public int getRandomIntegerBetween(int lowerBound, int upperBound)
    {
        return RandomBase.getRandomIntegerBetween(this, lowerBound, upperBound);
    }

    @Override
    public boolean getRandomBoolean()
    {
        return RandomBase.getRandomBoolean(this);
    }

    /**
     * Get a random integer between the provided lower and upper bounds. Both the lower and upper
     * bounds are inclusive.
     * @param lowerBound The inclusive lower bound.
     * @param upperBound The inclusive upper bound.
     * @return A random integer between the provided lower and upper bounds.
     */
    public static int getRandomIntegerBetween(Random random, int lowerBound, int upperBound)
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
            final int randomInteger = random.getRandomInteger();
            final int scaledRandomInteger = randomInteger % rangeSize;
            result = scaledRandomInteger + lowerBound;
        }

        return result;
    }

    /**
     * Get a random boolean value.
     * @return A random boolean value.
     */
    public static boolean getRandomBoolean(Random random)
    {
        return random.getRandomInteger() % 2 == 0;
    }
}
