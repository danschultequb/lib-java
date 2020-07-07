package qub;

/**
 * A read-only color interface.
 */
public interface Color
{
    /**
     * The minimum integer value that a color component can have.
     */
    int ComponentMin = 0;
    /**
     * The maximum integer value that a color component can have.
     */
    int ComponentMax = 255;

    Color white = Color.create(Color.ComponentMax, Color.ComponentMax, Color.ComponentMax);
    Color black = Color.create(Color.ComponentMin, Color.ComponentMin, Color.ComponentMin);
    Color red = Color.create(Color.ComponentMax, Color.ComponentMin, Color.ComponentMin);
    Color green = Color.create(Color.ComponentMin, Color.ComponentMax, Color.ComponentMin);
    Color blue = Color.create(Color.ComponentMin, Color.ComponentMin, Color.ComponentMax);
    Color transparent = Color.create(Color.ComponentMin, Color.ComponentMin, Color.ComponentMin, Color.ComponentMin);


    /**
     * Create a new opaque color using the provided red, green, and blue components (between 0 and
     * 255).
     * @param redComponent The red component of the new color.
     * @param greenComponent The green component of the new color.
     * @param blueComponent The blue component of the new color.
     * @return The new color.
     */
    static MutableColor create(int redComponent, int greenComponent, int blueComponent)
    {
        return MutableColor.create(redComponent, greenComponent, blueComponent, Color.ComponentMax);
    }

    /**
     * Create a new color using the provided red, green, blue, and alpha components (between 0 and
     * 255).
     * @param redComponent The red component of the new color.
     * @param greenComponent The green component of the new color.
     * @param blueComponent The blue component of the new color.
     * @param alphaComponent The alpha/opacity component of the new color.
     * @return The new color.
     */
    static MutableColor create(int redComponent, int greenComponent, int blueComponent, int alphaComponent)
    {
        return MutableColor.create(redComponent, greenComponent, blueComponent, alphaComponent);
    }

    /**
     * Get the red component of this color (between 0 and 255).
     * @return The red component of this color (between 0 and 255).
     */
    int getRedComponent();

    /**
     * Get the green component of this color (between 0 and 255).
     * @return The green component of this color (between 0 and 255).
     */
    int getGreenComponent();

    /**
     * Get the blue component of this color (between 0 and 255).
     * @return The blue component of this color (between 0 and 255).
     */
    int getBlueComponent();

    /**
     * Get the alpha (opacity) component of this color (between 0 and 255).
     * @return The alpha (opacity) component of this color (between 0 and 255).
     */
    int getAlphaComponent();

    /**
     * Create a copy of this Color that can be modified.
     * @return A modifiable copy of this Color.
     */
    MutableColor clone();

    /**
     * Get the JSON-string representation of the provided Color.
     * @param color The Color to get a JSON-string representation of.
     * @return The JSON-string representation of the provided Color.
     */
    static String toString(Color color)
    {
        PreCondition.assertNotNull(color, "color");

        return "{\"redComponent\":\"" + color.getRedComponent() + "\",\"greenComponent\":\"" + color.getGreenComponent() + "\",\"blueComponent\":\"" + color.getBlueComponent() + "\",\"alphaComponent\":\"" + color.getAlphaComponent() + "\"}";
    }

    /**
     * Get whether the provided Color is equal to the provided Object.
     * @param lhs The Color to compare against the Object.
     * @param rhs The Object to compare against the Color.
     * @return Whether the provided Color is equal to the provided Object.
     */
    static boolean equal(Color lhs, Object rhs)
    {
        return lhs != null && rhs instanceof Color && lhs.equals((Color)rhs);
    }

    /**
     * Get whether this Color is equal to the provided Color.
     * @param rhs The Color to compare against this Color.
     * @return Whether this Color is equal to the provided Color.
     */
    default boolean equals(Color rhs)
    {
        return rhs != null &&
            ((this.getAlphaComponent() == Color.ComponentMin && rhs.getAlphaComponent() == Color.ComponentMin) ||
             (this.getRedComponent() == rhs.getRedComponent() &&
              this.getGreenComponent() == rhs.getGreenComponent() &&
              this.getBlueComponent() == rhs.getBlueComponent() &&
              this.getAlphaComponent() == rhs.getAlphaComponent()));
    }

    /**
     * Get the hash code of the provided Color.
     * @param color The Color to get the hash code of.
     * @return The hash code of the provided Color.
     */
    static int hashCode(Color color)
    {
        return color == null
            ? 0
            : Hash.getHashCode(color.getRedComponent(), color.getGreenComponent(), color.getBlueComponent(), color.getAlphaComponent());
    }
}
