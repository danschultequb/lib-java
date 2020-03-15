package qub;

/**
 * A scale unit that operates on a metric base (10).
 */
public enum MetricScaleUnit
{
    /**
     * Size: 10^24 (1,000,000,000,000,000,000,000,000)
     */
    Yotta,
    /**
     * Size: 10^21 (1,000,000,000,000,000,000,000)
     */
    Zetta,
    /**
     * Size: 10^18 (1,000,000,000,000,000,000)
     */
    Exa,
    /**
     * Size: 10^15 (1,000,000,000,000,000)
     */
    Peta,
    /**
     * Size: 10^12 (1,000,000,000,000)
     */
    Tera,
    /**
     * Size: 10^9 (1,000,000,000)
     */
    Giga,
    /**
     * Size: 10^6 (1,000,000)
     */
    Mega,
    /**
     * Size: 10^3 (1,000)
     */
    Kilo,
    /**
     * Size: 10^0 (1)
     */
    Uni,
    /**
     * Size: 10^-1 (0.1)
     */
    Deci,
    /**
     * Size: 10^-2 (0.01)
     */
    Centi,
    /**
     * Size: 10^-3 (0.001)
     */
    Milli,
    /**
     * Size: 10^-6 (0.000001)
     */
    Micro,
    /**
     * Size: 10^-9 (0.000000001)
     */
    Nano,
}
