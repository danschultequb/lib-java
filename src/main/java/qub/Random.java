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
    int getRandomIntegerBetween(int lowerBound, int upperBound);
}
