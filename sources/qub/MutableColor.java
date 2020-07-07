package qub;

/**
 * A color object that can be modified.
 */
public class MutableColor implements Color
{
    private int redComponent;
    private int greenComponent;
    private int blueComponent;
    private int alphaComponent;

    private MutableColor(int redComponent, int greenComponent, int blueComponent, int alphaComponent)
    {
        this.setRedComponent(redComponent);
        this.setGreenComponent(greenComponent);
        this.setBlueComponent(blueComponent);
        this.setAlphaComponent(alphaComponent);
    }

    /**
     * Create a new opaque color using the provided red, green, and blue components (between 0 and
     * 255).
     * @param redComponent The red component of the new color.
     * @param greenComponent The green component of the new color.
     * @param blueComponent The blue component of the new color.
     * @return The new color.
     */
    public static MutableColor create(int redComponent, int greenComponent, int blueComponent)
    {
        return MutableColor.create(redComponent, greenComponent, blueComponent, MutableColor.ComponentMax);
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
    public static MutableColor create(int redComponent, int greenComponent, int blueComponent, int alphaComponent)
    {
        return new MutableColor(redComponent, greenComponent, blueComponent, alphaComponent);
    }

    @Override
    public int getRedComponent()
    {
        return this.redComponent;
    }

    /**
     * Set the red component of this color (between 0 and 255).
     * @param redComponent The red component of this color (between 0 and 255).
     * @return This object for method chaining.
     */
    public MutableColor setRedComponent(int redComponent)
    {
        PreCondition.assertBetween(MutableColor.ComponentMin, redComponent, MutableColor.ComponentMax, "redComponent");

        this.redComponent = redComponent;

        return this;
    }

    @Override
    public int getGreenComponent()
    {
        return this.greenComponent;
    }

    /**
     * Set the green component of this color (between 0 and 255).
     * @param greenComponent The green component of this color (between 0 and 255).
     * @return This object for method chaining.
     */
    public MutableColor setGreenComponent(int greenComponent)
    {
        PreCondition.assertBetween(MutableColor.ComponentMin, greenComponent, MutableColor.ComponentMax, "greenComponent");

        this.greenComponent = greenComponent;

        return this;
    }

    @Override
    public int getBlueComponent()
    {
        return this.blueComponent;
    }

    /**
     * Set the blue component of this color (between 0 and 255).
     * @param blueComponent The blue component of this color (between 0 and 255).
     * @return This object for method chaining.
     */
    public MutableColor setBlueComponent(int blueComponent)
    {
        PreCondition.assertBetween(MutableColor.ComponentMin, blueComponent, MutableColor.ComponentMax, "blueComponent");

        this.blueComponent = blueComponent;

        return this;
    }

    @Override
    public int getAlphaComponent()
    {
        return this.alphaComponent;
    }

    @Override
    public MutableColor clone()
    {
        return MutableColor.create(this.redComponent, this.greenComponent, this.blueComponent, this.alphaComponent);
    }

    /**
     * Set the alpha component of this color (between 0 and 255).
     * @param alphaComponent The alpha/opacity component of this color (between 0 and 255).
     * @return This object for method chaining.
     */
    public MutableColor setAlphaComponent(int alphaComponent)
    {
        PreCondition.assertBetween(MutableColor.ComponentMin, alphaComponent, MutableColor.ComponentMax, "alphaComponent");

        this.alphaComponent = alphaComponent;

        return this;
    }

    @Override
    public String toString()
    {
        return Color.toString(this);
    }

    @Override
    public boolean equals(Object rhs)
    {
        return Color.equal(this, rhs);
    }

    @Override
    public int hashCode()
    {
        return Color.hashCode(this);
    }
}
