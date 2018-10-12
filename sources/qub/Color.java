package qub;

public class Color
{
    private final double redComponent;
    private final double greenComponent;
    private final double blueComponent;
    private final double alphaComponent;

    private Color(double red, double green, double blue, double alpha)
    {
        PreCondition.assertBetween(0, red, 1, "red");
        PreCondition.assertBetween(0, green, 1, "green");
        PreCondition.assertBetween(0, blue, 1, "blue");
        PreCondition.assertBetween(0, alpha, 1, "alpha");

        this.redComponent = red;
        this.greenComponent = green;
        this.blueComponent = blue;
        this.alphaComponent = alpha;
    }

    /**
     * Get the red component of this color (between 0 and 1).
     * @return The red component of this color (between 0 and 1).
     */
    public double getRed()
    {
        return redComponent;
    }

    /**
     * Get the green component of this color (between 0 and 1).
     * @return The green component of this color (between 0 and 1).
     */
    public double getGreen()
    {
        return greenComponent;
    }

    /**
     * Get the blue component of this color (between 0 and 1).
     * @return The blue component of this color (between 0 and 1).
     */
    public double getBlue()
    {
        return blueComponent;
    }

    /**
     * Get the alpha (opacity) component of this color (between 0 and 1).
     * @return The alpha (opacity) component of this color (between 0 and 1).
     */
    public double getAlpha()
    {
        return alphaComponent;
    }

    /**
     * Create a new opaque color using the provided red, green, and blue components (between 0 and
     * 1).
     * @param red The red component of the new color.
     * @param green The green component of the new color.
     * @param blue The blue component of the new color.
     * @return The new color.
     */
    public static Color rgb(double red, double green, double blue)
    {
        return rgba(red, green, blue, 1);
    }

    /**
     * Create a new color using the provided red, green, blue, and alpha components (between 0 and
     * 1).
     * @param red The red component of the new color.
     * @param green The green component of the new color.
     * @param blue The blue component of the new color.
     * @param alpha The alpha component of the new color.
     * @return The new color.
     */
    public static Color rgba(double red, double green, double blue, double alpha)
    {
        return new Color(red, green, blue, alpha);
    }

    @Override
    public String toString()
    {
        return "{\"red\":\"" + redComponent + "\",\"green\":\"" + greenComponent + "\",\"blue\":\"" + blueComponent + "\",\"alpha\":\"" + alphaComponent + "\"}";
    }

    @Override
    public boolean equals(Object rhs)
    {
        return rhs instanceof Color && equals((Color)rhs);
    }

    public boolean equals(Color rhs)
    {
        return rhs != null &&
            redComponent == rhs.redComponent &&
            greenComponent == rhs.greenComponent &&
            blueComponent == rhs.blueComponent &&
            alphaComponent == rhs.alphaComponent;
    }

    @Override
    public int hashCode()
    {
        return Hash.getHashCode(redComponent, greenComponent, blueComponent, alphaComponent);
    }

    public static final Color white = rgb(1, 1, 1);
    public static final Color black = rgb(0, 0, 0);
    public static final Color red = rgb(1, 0, 0);
    public static final Color green = rgb(0, 1, 0);
    public static final Color blue = rgb(0, 0, 1);
}
