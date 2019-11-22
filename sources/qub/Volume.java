package qub;

/**
 * A measurement of quantity.
 */
public class Volume implements Comparable<Volume>
{
    private static final double KilolitersToLiters = 1000;
    private static final double LitersToMilliliters = 1000;
    private static final double USGallonsToLiters = 3.78541;
    private static final double USGallonsToUSQuarts = 4;
    private static final double USQuartsToUSPints = 2;
    private static final double USPintsToUSCups = 2;
    private static final double USCupsToUSFluidOunces = 8.11537;
    private static final double USCupsToUSTablespoons = 16.2307;
    private static final double USTablespoonsToUSTeaspoons = 3;

    private static final double KilolitersToMilliliters = Volume.KilolitersToLiters * Volume.LitersToMilliliters;
    private static final double KilolitersToUSGallons = Volume.KilolitersToLiters / Volume.USGallonsToLiters;
    private static final double KilolitersToUSQuarts = Volume.KilolitersToUSGallons * Volume.USGallonsToUSQuarts;
    private static final double KilolitersToUSPints = Volume.KilolitersToUSQuarts * Volume.USQuartsToUSPints;
    private static final double KilolitersToUSCups = Volume.KilolitersToUSPints * Volume.USPintsToUSCups;
    private static final double KilolitersToUSFluidOunces = Volume.KilolitersToUSCups * Volume.USCupsToUSFluidOunces;
    private static final double KilolitersToUSTablespoons = Volume.KilolitersToUSCups * Volume.USCupsToUSTablespoons;
    private static final double KilolitersToUSTeaspoons = Volume.KilolitersToUSTablespoons * Volume.USTablespoonsToUSTeaspoons;

    private static final double LitersToKiloliters = 1.0 / Volume.KilolitersToLiters;
    private static final double LitersToUSGallons = 1.0 / Volume.USGallonsToLiters;
    private static final double LitersToUSQuarts = Volume.LitersToUSGallons * Volume.USGallonsToUSQuarts;
    private static final double LitersToUSPints = Volume.LitersToUSQuarts * Volume.USQuartsToUSPints;
    private static final double LitersToUSCups = Volume.LitersToUSPints * Volume.USPintsToUSCups;
    private static final double LitersToUSFluidOunces = Volume.LitersToUSCups * Volume.USCupsToUSFluidOunces;
    private static final double LitersToUSTablespoons = Volume.LitersToUSCups * Volume.USCupsToUSTablespoons;
    private static final double LitersToUSTeaspoons = Volume.LitersToUSTablespoons * Volume.USTablespoonsToUSTeaspoons;

    private static final double MillilitersToKiloliters = 1.0 / Volume.KilolitersToMilliliters;
    private static final double MillilitersToLiters = 1.0 / Volume.LitersToMilliliters;
    private static final double MillilitersToUSGallons = Volume.MillilitersToLiters * Volume.LitersToUSGallons;
    private static final double MillilitersToUSQuarts = Volume.MillilitersToUSGallons * Volume.USGallonsToUSQuarts;
    private static final double MillilitersToUSPints = Volume.MillilitersToUSQuarts * Volume.USQuartsToUSPints;
    private static final double MillilitersToUSCups = Volume.MillilitersToUSPints * Volume.USPintsToUSCups;
    private static final double MillilitersToUSFluidOunces = Volume.MillilitersToUSCups * Volume.USCupsToUSFluidOunces;
    private static final double MillilitersToUSTablespoons = Volume.MillilitersToUSCups * Volume.USCupsToUSTablespoons;
    private static final double MillilitersToUSTeaspoons = Volume.MillilitersToUSTablespoons * Volume.USTablespoonsToUSTeaspoons;

    private static final double USGallonsToKiloliters = 1.0 / Volume.KilolitersToUSGallons;
    private static final double USGallonsToMilliliters = 1.0 / Volume.MillilitersToUSGallons;
    private static final double USGallonsToUSPints = Volume.USGallonsToUSQuarts * Volume.USQuartsToUSPints;
    private static final double USGallonsToUSCups = Volume.USGallonsToUSPints * Volume.USPintsToUSCups;
    private static final double USGallonsToUSFluidOunces = Volume.USGallonsToUSCups * Volume.USCupsToUSFluidOunces;
    private static final double USGallonsToUSTablespoons = Volume.USGallonsToUSCups * Volume.USCupsToUSTablespoons;
    private static final double USGallonsToUSTeaspoons = Volume.USGallonsToUSTablespoons * Volume.USTablespoonsToUSTeaspoons;

    private static final double USQuartsToKiloliters = 1.0 / Volume.KilolitersToUSQuarts;
    private static final double USQuartsToLiters = 1.0 / Volume.LitersToUSQuarts;
    private static final double USQuartsToMilliliters = 1.0 / Volume.MillilitersToUSQuarts;
    private static final double USQuartsToUSGallons = 1.0 / Volume.USGallonsToUSQuarts;
    private static final double USQuartsToUSCups = Volume.USQuartsToUSPints * Volume.USPintsToUSCups;
    private static final double USQuartsToUSFluidOunces = Volume.USQuartsToUSCups * Volume.USCupsToUSFluidOunces;
    private static final double USQuartsToUSTablespoons = Volume.USQuartsToUSCups * Volume.USCupsToUSTablespoons;
    private static final double USQuartsToUSTeaspoons = Volume.USQuartsToUSTablespoons * Volume.USTablespoonsToUSTeaspoons;

    private static final double USPintsToKiloliters = 1.0 / Volume.KilolitersToUSPints;
    private static final double USPintsToLiters = 1.0 / Volume.LitersToUSPints;
    private static final double USPintsToMilliliters = 1.0 / Volume.MillilitersToUSPints;
    private static final double USPintsToUSGallons = 1.0 / Volume.USGallonsToUSPints;
    private static final double USPintsToUSQuarts = 1.0 / Volume.USQuartsToUSPints;
    private static final double USPintsToUSFluidOunces = Volume.USPintsToUSCups * Volume.USCupsToUSFluidOunces;
    private static final double USPintsToUSTablespoons = Volume.USPintsToUSCups * Volume.USCupsToUSTablespoons;
    private static final double USPintsToUSTeaspoons = Volume.USPintsToUSTablespoons * Volume.USTablespoonsToUSTeaspoons;

    private static final double USCupsToKiloliters = 1.0 / Volume.KilolitersToUSCups;
    private static final double USCupsToLiters = 1.0 / Volume.LitersToUSCups;
    private static final double USCupsToMilliliters = 1.0 / Volume.MillilitersToUSCups;
    private static final double USCupsToUSGallons = 1.0 / Volume.USGallonsToUSCups;
    private static final double USCupsToUSQuarts = 1.0 / Volume.USQuartsToUSCups;
    private static final double USCupsToUSPints = 1.0 / Volume.USPintsToUSCups;
    private static final double USCupsToUSTeaspoons = Volume.USCupsToUSTablespoons * Volume.USTablespoonsToUSTeaspoons;

    private static final double USFluidOuncesToKiloliters = 1.0 / Volume.KilolitersToUSFluidOunces;
    private static final double USFluidOuncesToLiters = 1.0 / Volume.LitersToUSFluidOunces;
    private static final double USFluidOuncesToMilliliters = 1.0 / Volume.MillilitersToUSFluidOunces;
    private static final double USFluidOuncesToUSGallons = 1.0 / Volume.USGallonsToUSFluidOunces;
    private static final double USFluidOuncesToUSQuarts = 1.0 / Volume.USQuartsToUSFluidOunces;
    private static final double USFluidOuncesToUSPints = 1.0 / Volume.USPintsToUSFluidOunces;
    private static final double USFluidOuncesToUSCups = 1.0 / Volume.USCupsToUSFluidOunces;
    private static final double USFluidOuncesToUSTablespoons = Volume.USFluidOuncesToUSCups * Volume.USCupsToUSTablespoons;
    private static final double USFluidOuncesToUSTeaspoons = Volume.USFluidOuncesToUSTablespoons * Volume.USTablespoonsToUSTeaspoons;

    private static final double USTablespoonsToKiloliters = 1.0 / Volume.KilolitersToUSTablespoons;
    private static final double USTablespoonsToLiters = 1.0 / Volume.LitersToUSTablespoons;
    private static final double USTablespoonsToMilliliters = 1.0 / Volume.MillilitersToUSTablespoons;
    private static final double USTablespoonsToUSGallons = 1.0 / Volume.USGallonsToUSTablespoons;
    private static final double USTablespoonsToUSQuarts = 1.0 / Volume.USQuartsToUSTablespoons;
    private static final double USTablespoonsToUSPints = 1.0 / Volume.USPintsToUSTablespoons;
    private static final double USTablespoonsToUSCups = 1.0 / Volume.USCupsToUSTablespoons;
    private static final double USTablespoonsToUSFluidOunces = 1.0 / Volume.USFluidOuncesToUSTablespoons;

    private static final double USTeaspoonsToKiloliters = 1.0 / Volume.KilolitersToUSTeaspoons;
    private static final double USTeaspoonsToLiters = 1.0 / Volume.LitersToUSTeaspoons;
    private static final double USTeaspoonsToMilliliters = 1.0 / Volume.MillilitersToUSTeaspoons;
    private static final double USTeaspoonsToUSGallons = 1.0 / Volume.USGallonsToUSTeaspoons;
    private static final double USTeaspoonsToUSQuarts = 1.0 / Volume.USQuartsToUSTeaspoons;
    private static final double USTeaspoonsToUSPints = 1.0 / Volume.USPintsToUSTeaspoons;
    private static final double USTeaspoonsToUSCups = 1.0 / Volume.USCupsToUSTeaspoons;
    private static final double USTeaspoonsToUSFluidOunces = 1.0 / Volume.USFluidOuncesToUSTeaspoons;
    private static final double USTeaspoonsToUSTablespoons = 1.0 / Volume.USTablespoonsToUSTeaspoons;

    /**
     * The standard empty volume quantity.
     */
    public static final Volume zero = Volume.liters(0);

    /**
     * Create a new kiloliter volume quantity.
     * @param value The number of kiloliters.
     * @return The new kiloliter volume quantity.
     */
    public static Volume kiloliters(double value)
    {
        return new Volume(value, VolumeUnit.Kiloliters);
    }

    /**
     * Create a new liter volume quantity.
     * @param value The number of liters.
     * @return The new liter volume quantity.
     */
    public static Volume liters(double value)
    {
        return new Volume(value, VolumeUnit.Liters);
    }

    /**
     * Create a new milliliter volume quantity.
     * @param value The number of milliliters.
     * @return The new milliliter volume quantity.
     */
    public static Volume milliliters(double value)
    {
        return new Volume(value, VolumeUnit.Milliliters);
    }

    /**
     * Create a new US gallon volume quantity.
     * @param value The number of US gallons.
     * @return The new US gallon volume quantity.
     */
    public static Volume usGallons(double value)
    {
        return new Volume(value, VolumeUnit.USGallons);
    }

    /**
     * Create a new US quart volume quantity.
     * @param value The number of US quarts.
     * @return The new US quart volume quantity.
     */
    public static Volume usQuarts(double value)
    {
        return new Volume(value, VolumeUnit.USQuarts);
    }

    /**
     * Create a new US pint volume quantity.
     * @param value The number of US pints.
     * @return The new US pint volume quantity.
     */
    public static Volume usPints(double value)
    {
        return new Volume(value, VolumeUnit.USPints);
    }

    /**
     * Create a new US cup volume quantity.
     * @param value The number of US cups.
     * @return The new US cup volume quantity.
     */
    public static Volume usCups(double value)
    {
        return new Volume(value, VolumeUnit.USCups);
    }

    /**
     * Create a new US fluid ounce volume quantity.
     * @param value The number of US fluid ounces.
     * @return The new US fluid ounce volume quantity.
     */
    public static Volume usFluidOunces(double value)
    {
        return new Volume(value, VolumeUnit.USFluidOunces);
    }

    /**
     * Create a new US tablespoon volume quantity.
     * @param value The number of US tablespoons.
     * @return The new US tablespoon volume quantity.
     */
    public static Volume usTablespoons(double value)
    {
        return new Volume(value, VolumeUnit.USTablespoons);
    }

    /**
     * Create a new US teaspoon volume quantity.
     * @param value The number of US teaspoons.
     * @return The new US teaspoon volume quantity.
     */
    public static Volume usTeaspoons(double value)
    {
        return new Volume(value, VolumeUnit.USTeaspoons);
    }

    private final double value;
    private final VolumeUnit units;

    /**
     * Create a new Volume quantity.
     * @param value The value of the Volume quantity.
     * @param units The units of the Volume quantity.
     */
    public Volume(double value, VolumeUnit units)
    {
        PreCondition.assertNotNull(units, "units");

        this.value = value;
        this.units = units;
    }

    /**
     * Get the value of this volume quantity.
     * @return The value of this volume quantity.
     */
    public double getValue()
    {
        return this.value;
    }

    /**
     * Get the units that this volume quantity is in.
     * @return The units that this volume quantity is in.
     */
    public VolumeUnit getUnits()
    {
        return this.units;
    }

    /**
     * Convert this volume to the provided units.
     * @param units The units to convert this volume to.
     * @return The converted volume quantity.
     */
    public Volume convertTo(VolumeUnit units)
    {
        PreCondition.assertNotNull(units, "units");

        Volume result = this;
        switch (this.units)
        {
            case Kiloliters:
                switch (units)
                {
                    case Liters:
                        result = new Volume(this.value * Volume.KilolitersToLiters, units);
                        break;

                    case Milliliters:
                        result = new Volume(this.value * Volume.KilolitersToMilliliters, units);
                        break;

                    case USGallons:
                        result = new Volume(this.value * Volume.KilolitersToUSGallons, units);
                        break;

                    case USQuarts:
                        result = new Volume(this.value * Volume.KilolitersToUSQuarts, units);
                        break;

                    case USPints:
                        result = new Volume(this.value * Volume.KilolitersToUSPints, units);
                        break;

                    case USCups:
                        result = new Volume(this.value * Volume.KilolitersToUSCups, units);
                        break;

                    case USFluidOunces:
                        result = new Volume(this.value * Volume.KilolitersToUSFluidOunces, units);
                        break;

                    case USTablespoons:
                        result = new Volume(this.value * Volume.KilolitersToUSTablespoons, units);
                        break;

                    case USTeaspoons:
                        result = new Volume(this.value * Volume.KilolitersToUSTeaspoons, units);
                        break;
                }
                break;

            case Liters:
                switch (units)
                {
                    case Kiloliters:
                        result = new Volume(this.value * Volume.LitersToKiloliters, units);
                        break;

                    case Milliliters:
                        result = new Volume(this.value * Volume.LitersToMilliliters, units);
                        break;

                    case USGallons:
                        result = new Volume(this.value * Volume.LitersToUSGallons, units);
                        break;

                    case USQuarts:
                        result = new Volume(this.value * Volume.LitersToUSQuarts, units);
                        break;

                    case USPints:
                        result = new Volume(this.value * Volume.LitersToUSPints, units);
                        break;

                    case USCups:
                        result = new Volume(this.value * Volume.LitersToUSCups, units);
                        break;

                    case USFluidOunces:
                        result = new Volume(this.value * Volume.LitersToUSFluidOunces, units);
                        break;

                    case USTablespoons:
                        result = new Volume(this.value * Volume.LitersToUSTablespoons, units);
                        break;

                    case USTeaspoons:
                        result = new Volume(this.value * Volume.LitersToUSTeaspoons, units);
                        break;
                }
                break;

            case Milliliters:
                switch (units)
                {
                    case Kiloliters:
                        result = new Volume(this.value * Volume.MillilitersToKiloliters, units);
                        break;

                    case Liters:
                        result = new Volume(this.value * Volume.MillilitersToLiters, units);
                        break;

                    case USGallons:
                        result = new Volume(this.value * Volume.MillilitersToUSGallons, units);
                        break;

                    case USQuarts:
                        result = new Volume(this.value * Volume.MillilitersToUSQuarts, units);
                        break;

                    case USPints:
                        result = new Volume(this.value * Volume.MillilitersToUSPints, units);
                        break;

                    case USCups:
                        result = new Volume(this.value * Volume.MillilitersToUSCups, units);
                        break;

                    case USFluidOunces:
                        result = new Volume(this.value * Volume.MillilitersToUSFluidOunces, units);
                        break;

                    case USTablespoons:
                        result = new Volume(this.value * Volume.MillilitersToUSTablespoons, units);
                        break;

                    case USTeaspoons:
                        result = new Volume(this.value * Volume.MillilitersToUSTeaspoons, units);
                        break;
                }
                break;

            case USGallons:
                switch (units)
                {
                    case Kiloliters:
                        result = new Volume(this.value * Volume.USGallonsToKiloliters, units);
                        break;

                    case Liters:
                        result = new Volume(this.value * Volume.USGallonsToLiters, units);
                        break;

                    case Milliliters:
                        result = new Volume(this.value * Volume.USGallonsToMilliliters, units);
                        break;

                    case USQuarts:
                        result = new Volume(this.value * Volume.USGallonsToUSQuarts, units);
                        break;

                    case USPints:
                        result = new Volume(this.value * Volume.USGallonsToUSPints, units);
                        break;

                    case USCups:
                        result = new Volume(this.value * Volume.USGallonsToUSCups, units);
                        break;

                    case USFluidOunces:
                        result = new Volume(this.value * Volume.USGallonsToUSFluidOunces, units);
                        break;

                    case USTablespoons:
                        result = new Volume(this.value * Volume.USGallonsToUSTablespoons, units);
                        break;

                    case USTeaspoons:
                        result = new Volume(this.value * Volume.USGallonsToUSTeaspoons, units);
                        break;
                }
                break;

            case USQuarts:
                switch (units)
                {
                    case Kiloliters:
                        result = new Volume(this.value * Volume.USQuartsToKiloliters, units);
                        break;

                    case Liters:
                        result = new Volume(this.value * Volume.USQuartsToLiters, units);
                        break;

                    case Milliliters:
                        result = new Volume(this.value * Volume.USQuartsToMilliliters, units);
                        break;

                    case USGallons:
                        result = new Volume(this.value * Volume.USQuartsToUSGallons, units);
                        break;

                    case USPints:
                        result = new Volume(this.value * Volume.USQuartsToUSPints, units);
                        break;

                    case USCups:
                        result = new Volume(this.value * Volume.USQuartsToUSCups, units);
                        break;

                    case USFluidOunces:
                        result = new Volume(this.value * Volume.USQuartsToUSFluidOunces, units);
                        break;

                    case USTablespoons:
                        result = new Volume(this.value * Volume.USQuartsToUSTablespoons, units);
                        break;

                    case USTeaspoons:
                        result = new Volume(this.value * Volume.USQuartsToUSTeaspoons, units);
                        break;
                }
                break;

            case USPints:
                switch (units)
                {
                    case Kiloliters:
                        result = new Volume(this.value * Volume.USPintsToKiloliters, units);
                        break;

                    case Liters:
                        result = new Volume(this.value * Volume.USPintsToLiters, units);
                        break;

                    case Milliliters:
                        result = new Volume(this.value * Volume.USPintsToMilliliters, units);
                        break;

                    case USGallons:
                        result = new Volume(this.value * Volume.USPintsToUSGallons, units);
                        break;

                    case USQuarts:
                        result = new Volume(this.value * Volume.USPintsToUSQuarts, units);
                        break;

                    case USCups:
                        result = new Volume(this.value * Volume.USPintsToUSCups, units);
                        break;

                    case USFluidOunces:
                        result = new Volume(this.value * Volume.USPintsToUSFluidOunces, units);
                        break;

                    case USTablespoons:
                        result = new Volume(this.value * Volume.USPintsToUSTablespoons, units);
                        break;

                    case USTeaspoons:
                        result = new Volume(this.value * Volume.USPintsToUSTeaspoons, units);
                        break;
                }
                break;

            case USCups:
                switch (units)
                {
                    case Kiloliters:
                        result = new Volume(this.value * Volume.USCupsToKiloliters, units);
                        break;

                    case Liters:
                        result = new Volume(this.value * Volume.USCupsToLiters, units);
                        break;

                    case Milliliters:
                        result = new Volume(this.value * Volume.USCupsToMilliliters, units);
                        break;

                    case USGallons:
                        result = new Volume(this.value * Volume.USCupsToUSGallons, units);
                        break;

                    case USQuarts:
                        result = new Volume(this.value * Volume.USCupsToUSQuarts, units);
                        break;

                    case USPints:
                        result = new Volume(this.value * Volume.USCupsToUSPints, units);
                        break;

                    case USFluidOunces:
                        result = new Volume(this.value * Volume.USCupsToUSFluidOunces, units);
                        break;

                    case USTablespoons:
                        result = new Volume(this.value * Volume.USCupsToUSTablespoons, units);
                        break;

                    case USTeaspoons:
                        result = new Volume(this.value * Volume.USCupsToUSTeaspoons, units);
                        break;
                }
                break;

            case USFluidOunces:
                switch (units)
                {
                    case Kiloliters:
                        result = new Volume(this.value * Volume.USFluidOuncesToKiloliters, units);
                        break;

                    case Liters:
                        result = new Volume(this.value * Volume.USFluidOuncesToLiters, units);
                        break;

                    case Milliliters:
                        result = new Volume(this.value * Volume.USFluidOuncesToMilliliters, units);
                        break;

                    case USGallons:
                        result = new Volume(this.value * Volume.USFluidOuncesToUSGallons, units);
                        break;

                    case USQuarts:
                        result = new Volume(this.value * Volume.USFluidOuncesToUSQuarts, units);
                        break;

                    case USPints:
                        result = new Volume(this.value * Volume.USFluidOuncesToUSPints, units);
                        break;

                    case USCups:
                        result = new Volume(this.value * Volume.USFluidOuncesToUSCups, units);
                        break;

                    case USTablespoons:
                        result = new Volume(this.value * Volume.USFluidOuncesToUSTablespoons, units);
                        break;

                    case USTeaspoons:
                        result = new Volume(this.value * Volume.USFluidOuncesToUSTeaspoons, units);
                        break;
                }
                break;

            case USTablespoons:
                switch (units)
                {
                    case Kiloliters:
                        result = new Volume(this.value * Volume.USTablespoonsToKiloliters, units);
                        break;

                    case Liters:
                        result = new Volume(this.value * Volume.USTablespoonsToLiters, units);
                        break;

                    case Milliliters:
                        result = new Volume(this.value * Volume.USTablespoonsToMilliliters, units);
                        break;

                    case USGallons:
                        result = new Volume(this.value * Volume.USTablespoonsToUSGallons, units);
                        break;

                    case USQuarts:
                        result = new Volume(this.value * Volume.USTablespoonsToUSQuarts, units);
                        break;

                    case USPints:
                        result = new Volume(this.value * Volume.USTablespoonsToUSPints, units);
                        break;

                    case USCups:
                        result = new Volume(this.value * Volume.USTablespoonsToUSCups, units);
                        break;

                    case USFluidOunces:
                        result = new Volume(this.value * Volume.USTablespoonsToUSFluidOunces, units);
                        break;

                    case USTeaspoons:
                        result = new Volume(this.value * Volume.USTablespoonsToUSTeaspoons, units);
                        break;
                }
                break;

            case USTeaspoons:
                switch (units)
                {
                    case Kiloliters:
                        result = new Volume(this.value * Volume.USTeaspoonsToKiloliters, units);
                        break;

                    case Liters:
                        result = new Volume(this.value * Volume.USTeaspoonsToLiters, units);
                        break;

                    case Milliliters:
                        result = new Volume(this.value * Volume.USTeaspoonsToMilliliters, units);
                        break;

                    case USGallons:
                        result = new Volume(this.value * Volume.USTeaspoonsToUSGallons, units);
                        break;

                    case USQuarts:
                        result = new Volume(this.value * Volume.USTeaspoonsToUSQuarts, units);
                        break;

                    case USPints:
                        result = new Volume(this.value * Volume.USTeaspoonsToUSPints, units);
                        break;

                    case USCups:
                        result = new Volume(this.value * Volume.USTeaspoonsToUSCups, units);
                        break;

                    case USFluidOunces:
                        result = new Volume(this.value * Volume.USTeaspoonsToUSFluidOunces, units);
                        break;

                    case USTablespoons:
                        result = new Volume(this.value * Volume.USTeaspoonsToUSTablespoons, units);
                        break;
                }
                break;
        }

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(units, result.getUnits(), "result.getUnits()");

        return result;
    }

    /**
     * Convert this Volume to kiloliters.
     * @return This Volume converted to kiloliters.
     */
    public Volume toKiloliters()
    {
        return this.convertTo(VolumeUnit.Kiloliters);
    }

    /**
     * Convert this Volume to liters.
     * @return This Volume converted to liters.
     */
    public Volume toLiters()
    {
        return this.convertTo(VolumeUnit.Liters);
    }

    /**
     * Convert this Volume to milliliters.
     * @return This Volume converted to milliliters.
     */
    public Volume toMilliliters()
    {
        return this.convertTo(VolumeUnit.Milliliters);
    }

    /**
     * Convert this Volume to US gallons.
     * @return This Volume converted to US gallons.
     */
    public Volume toUSGallons()
    {
        return this.convertTo(VolumeUnit.USGallons);
    }

    /**
     * Convert this Volume to US quarts.
     * @return This Volume converted to US quarts.
     */
    public Volume toUSQuarts()
    {
        return this.convertTo(VolumeUnit.USQuarts);
    }

    /**
     * Convert this Volume to US pints.
     * @return This Volume converted to US pints.
     */
    public Volume toUSPints()
    {
        return this.convertTo(VolumeUnit.USPints);
    }

    /**
     * Convert this Volume to US cups.
     * @return This Volume converted to US cups.
     */
    public Volume toUSCups()
    {
        return this.convertTo(VolumeUnit.USCups);
    }

    /**
     * Convert this Volume to US fluid ounces.
     * @return This Volume converted to US fluid ounces.
     */
    public Volume toUSFluidOunces()
    {
        return this.convertTo(VolumeUnit.USFluidOunces);
    }

    /**
     * Convert this Volume to US tablespoons.
     * @return This Volume converted to US tablespoons.
     */
    public Volume toUSTablespoons()
    {
        return this.convertTo(VolumeUnit.USTablespoons);
    }

    /**
     * Convert this Volume to US teaspoons.
     * @return This Volume converted to US teaspoons.
     */
    public Volume toUSTeaspoons()
    {
        return this.convertTo(VolumeUnit.USTeaspoons);
    }

    /**
     * Get the negative version of this Volume.
     * @return The negative version of this Volume.
     */
    public Volume negate()
    {
        return this.value == 0
            ? this
            : new Volume(-this.value, this.units);
    }

    public Volume plus(Volume rhs)
    {
        PreCondition.assertNotNull(rhs, "rhs");

        Volume result;
        if (rhs.value == 0)
        {
            result = this;
        }
        else
        {
            final Volume convertedRhs = rhs.convertTo(this.units);
            if (this.value == 0)
            {
                result = convertedRhs;
            }
            else
            {
                result = new Volume(this.value + convertedRhs.value, this.units);
            }
        }

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(units, result.getUnits(), "result.getUnits()");

        return result;
    }

    public Volume times(double value)
    {
        Volume result;
        if (value == 0)
        {
            result = Volume.zero;
        }
        else if (this.value == 0 || value == 1)
        {
            result = this;
        }
        else
        {
            result = new Volume(this.value * value, this.units);
        }

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(this.units, result.getUnits(), "result.getUnits()");

        return result;
    }

    public Volume dividedBy(double value)
    {
        PreCondition.assertNotEqual(value, 0, "value");

        Volume result;
        if (this.value == 0 || value == 1)
        {
            result = this;
        }
        else
        {
            result = new Volume(this.value / value, this.units);
        }

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(this.units, result.getUnits(), "result.getUnits()");

        return result;
    }

    @Override
    public String toString()
    {
        return value + " " + units;
    }

    public String toString(String format)
    {
        PreCondition.assertNotNull(format, "format");

        return new java.text.DecimalFormat(format).format(value) + " " + units;
    }

    @Override
    public boolean equals(Object value)
    {
        return value instanceof Volume && this.equals((Volume)value);
    }

    public boolean equals(Volume rhs)
    {
        return rhs != null && rhs.convertTo(this.units).value == value;
    }

    @Override
    public int hashCode()
    {
        return Doubles.hashCode(this.toLiters().value);
    }

    @Override
    public Comparison compareTo(Volume rhs)
    {
        return rhs == null ? Comparison.GreaterThan : Comparison.from(this.value - rhs.convertTo(this.units).value);
    }
}
