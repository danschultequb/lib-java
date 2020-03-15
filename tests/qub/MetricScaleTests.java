package qub;

public interface MetricScaleTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(MetricScale.class, () ->
        {
            runner.testGroup("getConversionMultiplier(MetricScaleUnit,MetricScaleUnit)", () ->
            {
                final Action3<MetricScaleUnit,MetricScaleUnit,Throwable> getConversionMultiplierErrorTest = (MetricScaleUnit from, MetricScaleUnit to, Throwable expected) ->
                {
                    runner.test("with " + English.andList(from, to), (Test test) ->
                    {
                        test.assertThrows(() -> MetricScale.getConversionMultiplier(from, to), expected);
                    });
                };

                getConversionMultiplierErrorTest.run(null, null, new PreConditionFailure("from cannot be null."));
                getConversionMultiplierErrorTest.run(null, MetricScaleUnit.Mega, new PreConditionFailure("from cannot be null."));
                getConversionMultiplierErrorTest.run(MetricScaleUnit.Kilo, null, new PreConditionFailure("to cannot be null."));

                final Action3<MetricScaleUnit,MetricScaleUnit,Double> getConversionMultiplierTest = (MetricScaleUnit from, MetricScaleUnit to, Double expected) ->
                {
                    runner.test("with " + English.andList(from, to), (Test test) ->
                    {
                        test.assertEqual(expected, MetricScale.getConversionMultiplier(from, to));
                    });
                };

                getConversionMultiplierTest.run(MetricScaleUnit.Yotta, MetricScaleUnit.Yotta,                                 1.0);
                getConversionMultiplierTest.run(MetricScaleUnit.Yotta, MetricScaleUnit.Zetta,                              1000.0);
                getConversionMultiplierTest.run(MetricScaleUnit.Yotta, MetricScaleUnit.Exa,                             1000000.0);
                getConversionMultiplierTest.run(MetricScaleUnit.Yotta, MetricScaleUnit.Peta,                         1000000000.0);
                getConversionMultiplierTest.run(MetricScaleUnit.Yotta, MetricScaleUnit.Tera,                      1000000000000.0);
                getConversionMultiplierTest.run(MetricScaleUnit.Yotta, MetricScaleUnit.Giga,                   1000000000000000.0);
                getConversionMultiplierTest.run(MetricScaleUnit.Yotta, MetricScaleUnit.Mega,                1000000000000000000.0);
                getConversionMultiplierTest.run(MetricScaleUnit.Yotta, MetricScaleUnit.Kilo,             1000000000000000000000.0);
                getConversionMultiplierTest.run(MetricScaleUnit.Yotta, MetricScaleUnit.Uni,           1000000000000000000000000.0);
                getConversionMultiplierTest.run(MetricScaleUnit.Yotta, MetricScaleUnit.Deci,         10000000000000000000000000.0);
                getConversionMultiplierTest.run(MetricScaleUnit.Yotta, MetricScaleUnit.Centi,       100000000000000000000000000.0);
                getConversionMultiplierTest.run(MetricScaleUnit.Yotta, MetricScaleUnit.Milli,      1000000000000000000000000000.0);
                getConversionMultiplierTest.run(MetricScaleUnit.Yotta, MetricScaleUnit.Micro,   1000000000000000000000000000000.0);
                getConversionMultiplierTest.run(MetricScaleUnit.Yotta, MetricScaleUnit.Nano, 1000000000000000000000000000000000.0);

                getConversionMultiplierTest.run(MetricScaleUnit.Zetta, MetricScaleUnit.Yotta,                              0.001);
                getConversionMultiplierTest.run(MetricScaleUnit.Zetta, MetricScaleUnit.Zetta,                              1.0);
                getConversionMultiplierTest.run(MetricScaleUnit.Zetta, MetricScaleUnit.Exa,                             1000.0);
                getConversionMultiplierTest.run(MetricScaleUnit.Zetta, MetricScaleUnit.Peta,                         1000000.0);
                getConversionMultiplierTest.run(MetricScaleUnit.Zetta, MetricScaleUnit.Tera,                      1000000000.0);
                getConversionMultiplierTest.run(MetricScaleUnit.Zetta, MetricScaleUnit.Giga,                   1000000000000.0);
                getConversionMultiplierTest.run(MetricScaleUnit.Zetta, MetricScaleUnit.Mega,                1000000000000000.0);
                getConversionMultiplierTest.run(MetricScaleUnit.Zetta, MetricScaleUnit.Kilo,             1000000000000000000.0);
                getConversionMultiplierTest.run(MetricScaleUnit.Zetta, MetricScaleUnit.Uni,           1000000000000000000000.0);
                getConversionMultiplierTest.run(MetricScaleUnit.Zetta, MetricScaleUnit.Deci,         10000000000000000000000.0);
                getConversionMultiplierTest.run(MetricScaleUnit.Zetta, MetricScaleUnit.Centi,       100000000000000000000000.0);
                getConversionMultiplierTest.run(MetricScaleUnit.Zetta, MetricScaleUnit.Milli,      1000000000000000000000000.0);
                getConversionMultiplierTest.run(MetricScaleUnit.Zetta, MetricScaleUnit.Micro,   1000000000000000000000000000.0);
                getConversionMultiplierTest.run(MetricScaleUnit.Zetta, MetricScaleUnit.Nano, 1000000000000000000000000000000.0);

                getConversionMultiplierTest.run(MetricScaleUnit.Exa, MetricScaleUnit.Yotta,                           0.000001);
                getConversionMultiplierTest.run(MetricScaleUnit.Exa, MetricScaleUnit.Zetta,                           0.001);
                getConversionMultiplierTest.run(MetricScaleUnit.Exa, MetricScaleUnit.Exa,                             1.0);
                getConversionMultiplierTest.run(MetricScaleUnit.Exa, MetricScaleUnit.Peta,                         1000.0);
                getConversionMultiplierTest.run(MetricScaleUnit.Exa, MetricScaleUnit.Tera,                      1000000.0);
                getConversionMultiplierTest.run(MetricScaleUnit.Exa, MetricScaleUnit.Giga,                   1000000000.0);
                getConversionMultiplierTest.run(MetricScaleUnit.Exa, MetricScaleUnit.Mega,                1000000000000.0);
                getConversionMultiplierTest.run(MetricScaleUnit.Exa, MetricScaleUnit.Kilo,             1000000000000000.0);
                getConversionMultiplierTest.run(MetricScaleUnit.Exa, MetricScaleUnit.Uni,           1000000000000000000.0);
                getConversionMultiplierTest.run(MetricScaleUnit.Exa, MetricScaleUnit.Deci,         10000000000000000000.0);
                getConversionMultiplierTest.run(MetricScaleUnit.Exa, MetricScaleUnit.Centi,       100000000000000000000.0);
                getConversionMultiplierTest.run(MetricScaleUnit.Exa, MetricScaleUnit.Milli,      1000000000000000000000.0);
                getConversionMultiplierTest.run(MetricScaleUnit.Exa, MetricScaleUnit.Micro,   1000000000000000000000000.0);
                getConversionMultiplierTest.run(MetricScaleUnit.Exa, MetricScaleUnit.Nano, 1000000000000000000000000000.0);

                getConversionMultiplierTest.run(MetricScaleUnit.Peta, MetricScaleUnit.Yotta,                         0.000000001);
                getConversionMultiplierTest.run(MetricScaleUnit.Peta, MetricScaleUnit.Zetta,                         0.000001);
                getConversionMultiplierTest.run(MetricScaleUnit.Peta, MetricScaleUnit.Exa,                           0.001);
                getConversionMultiplierTest.run(MetricScaleUnit.Peta, MetricScaleUnit.Peta,                          1.0);
                getConversionMultiplierTest.run(MetricScaleUnit.Peta, MetricScaleUnit.Tera,                       1000.0);
                getConversionMultiplierTest.run(MetricScaleUnit.Peta, MetricScaleUnit.Giga,                    1000000.0);
                getConversionMultiplierTest.run(MetricScaleUnit.Peta, MetricScaleUnit.Mega,                 1000000000.0);
                getConversionMultiplierTest.run(MetricScaleUnit.Peta, MetricScaleUnit.Kilo,              1000000000000.0);
                getConversionMultiplierTest.run(MetricScaleUnit.Peta, MetricScaleUnit.Uni,            1000000000000000.0);
                getConversionMultiplierTest.run(MetricScaleUnit.Peta, MetricScaleUnit.Deci,          10000000000000000.0);
                getConversionMultiplierTest.run(MetricScaleUnit.Peta, MetricScaleUnit.Centi,        100000000000000000.0);
                getConversionMultiplierTest.run(MetricScaleUnit.Peta, MetricScaleUnit.Milli,       1000000000000000000.0);
                getConversionMultiplierTest.run(MetricScaleUnit.Peta, MetricScaleUnit.Micro,    1000000000000000000000.0);
                getConversionMultiplierTest.run(MetricScaleUnit.Peta, MetricScaleUnit.Nano,  1000000000000000000000000.0);

                getConversionMultiplierTest.run(MetricScaleUnit.Tera, MetricScaleUnit.Yotta,                      0.000000000001);
                getConversionMultiplierTest.run(MetricScaleUnit.Tera, MetricScaleUnit.Zetta,                      0.000000001);
                getConversionMultiplierTest.run(MetricScaleUnit.Tera, MetricScaleUnit.Exa,                        0.000001);
                getConversionMultiplierTest.run(MetricScaleUnit.Tera, MetricScaleUnit.Peta,                       0.001);
                getConversionMultiplierTest.run(MetricScaleUnit.Tera, MetricScaleUnit.Tera,                       1.0);
                getConversionMultiplierTest.run(MetricScaleUnit.Tera, MetricScaleUnit.Giga,                    1000.0);
                getConversionMultiplierTest.run(MetricScaleUnit.Tera, MetricScaleUnit.Mega,                 1000000.0);
                getConversionMultiplierTest.run(MetricScaleUnit.Tera, MetricScaleUnit.Kilo,              1000000000.0);
                getConversionMultiplierTest.run(MetricScaleUnit.Tera, MetricScaleUnit.Uni,            1000000000000.0);
                getConversionMultiplierTest.run(MetricScaleUnit.Tera, MetricScaleUnit.Deci,          10000000000000.0);
                getConversionMultiplierTest.run(MetricScaleUnit.Tera, MetricScaleUnit.Centi,        100000000000000.0);
                getConversionMultiplierTest.run(MetricScaleUnit.Tera, MetricScaleUnit.Milli,       1000000000000000.0);
                getConversionMultiplierTest.run(MetricScaleUnit.Tera, MetricScaleUnit.Micro,    1000000000000000000.0);
                getConversionMultiplierTest.run(MetricScaleUnit.Tera, MetricScaleUnit.Nano,  1000000000000000000000.0);

                getConversionMultiplierTest.run(MetricScaleUnit.Giga, MetricScaleUnit.Yotta,                   0.000000000000001);
                getConversionMultiplierTest.run(MetricScaleUnit.Giga, MetricScaleUnit.Zetta,                   0.000000000001);
                getConversionMultiplierTest.run(MetricScaleUnit.Giga, MetricScaleUnit.Exa,                     0.000000001);
                getConversionMultiplierTest.run(MetricScaleUnit.Giga, MetricScaleUnit.Peta,                    0.000001);
                getConversionMultiplierTest.run(MetricScaleUnit.Giga, MetricScaleUnit.Tera,                    0.001);
                getConversionMultiplierTest.run(MetricScaleUnit.Giga, MetricScaleUnit.Giga,                    1.0);
                getConversionMultiplierTest.run(MetricScaleUnit.Giga, MetricScaleUnit.Mega,                 1000.0);
                getConversionMultiplierTest.run(MetricScaleUnit.Giga, MetricScaleUnit.Kilo,              1000000.0);
                getConversionMultiplierTest.run(MetricScaleUnit.Giga, MetricScaleUnit.Uni,            1000000000.0);
                getConversionMultiplierTest.run(MetricScaleUnit.Giga, MetricScaleUnit.Deci,          10000000000.0);
                getConversionMultiplierTest.run(MetricScaleUnit.Giga, MetricScaleUnit.Centi,        100000000000.0);
                getConversionMultiplierTest.run(MetricScaleUnit.Giga, MetricScaleUnit.Milli,       1000000000000.0);
                getConversionMultiplierTest.run(MetricScaleUnit.Giga, MetricScaleUnit.Micro,    1000000000000000.0);
                getConversionMultiplierTest.run(MetricScaleUnit.Giga, MetricScaleUnit.Nano,  1000000000000000000.0);

                getConversionMultiplierTest.run(MetricScaleUnit.Mega, MetricScaleUnit.Yotta,                0.000000000000000001);
                getConversionMultiplierTest.run(MetricScaleUnit.Mega, MetricScaleUnit.Zetta,                0.000000000000001);
                getConversionMultiplierTest.run(MetricScaleUnit.Mega, MetricScaleUnit.Exa,                  0.000000000001);
                getConversionMultiplierTest.run(MetricScaleUnit.Mega, MetricScaleUnit.Peta,                 0.000000001);
                getConversionMultiplierTest.run(MetricScaleUnit.Mega, MetricScaleUnit.Tera,                 0.000001);
                getConversionMultiplierTest.run(MetricScaleUnit.Mega, MetricScaleUnit.Giga,                 0.001);
                getConversionMultiplierTest.run(MetricScaleUnit.Mega, MetricScaleUnit.Mega,                 1.0);
                getConversionMultiplierTest.run(MetricScaleUnit.Mega, MetricScaleUnit.Kilo,              1000.0);
                getConversionMultiplierTest.run(MetricScaleUnit.Mega, MetricScaleUnit.Uni,            1000000.0);
                getConversionMultiplierTest.run(MetricScaleUnit.Mega, MetricScaleUnit.Deci,          10000000.0);
                getConversionMultiplierTest.run(MetricScaleUnit.Mega, MetricScaleUnit.Centi,        100000000.0);
                getConversionMultiplierTest.run(MetricScaleUnit.Mega, MetricScaleUnit.Milli,       1000000000.0);
                getConversionMultiplierTest.run(MetricScaleUnit.Mega, MetricScaleUnit.Micro,    1000000000000.0);
                getConversionMultiplierTest.run(MetricScaleUnit.Mega, MetricScaleUnit.Nano,  1000000000000000.0);

                getConversionMultiplierTest.run(MetricScaleUnit.Kilo, MetricScaleUnit.Yotta,             0.000000000000000000001);
                getConversionMultiplierTest.run(MetricScaleUnit.Kilo, MetricScaleUnit.Zetta,             0.000000000000000001);
                getConversionMultiplierTest.run(MetricScaleUnit.Kilo, MetricScaleUnit.Exa,               0.000000000000001);
                getConversionMultiplierTest.run(MetricScaleUnit.Kilo, MetricScaleUnit.Peta,              0.000000000001);
                getConversionMultiplierTest.run(MetricScaleUnit.Kilo, MetricScaleUnit.Tera,              0.000000001);
                getConversionMultiplierTest.run(MetricScaleUnit.Kilo, MetricScaleUnit.Giga,              0.000001);
                getConversionMultiplierTest.run(MetricScaleUnit.Kilo, MetricScaleUnit.Mega,              0.001);
                getConversionMultiplierTest.run(MetricScaleUnit.Kilo, MetricScaleUnit.Kilo,              1.0);
                getConversionMultiplierTest.run(MetricScaleUnit.Kilo, MetricScaleUnit.Uni,            1000.0);
                getConversionMultiplierTest.run(MetricScaleUnit.Kilo, MetricScaleUnit.Deci,          10000.0);
                getConversionMultiplierTest.run(MetricScaleUnit.Kilo, MetricScaleUnit.Centi,        100000.0);
                getConversionMultiplierTest.run(MetricScaleUnit.Kilo, MetricScaleUnit.Milli,       1000000.0);
                getConversionMultiplierTest.run(MetricScaleUnit.Kilo, MetricScaleUnit.Micro,    1000000000.0);
                getConversionMultiplierTest.run(MetricScaleUnit.Kilo, MetricScaleUnit.Nano,  1000000000000.0);

                getConversionMultiplierTest.run(MetricScaleUnit.Uni, MetricScaleUnit.Yotta,          0.000000000000000000000001);
                getConversionMultiplierTest.run(MetricScaleUnit.Uni, MetricScaleUnit.Zetta,          0.000000000000000000001);
                getConversionMultiplierTest.run(MetricScaleUnit.Uni, MetricScaleUnit.Exa,            0.000000000000000001);
                getConversionMultiplierTest.run(MetricScaleUnit.Uni, MetricScaleUnit.Peta,           0.000000000000001);
                getConversionMultiplierTest.run(MetricScaleUnit.Uni, MetricScaleUnit.Tera,           0.000000000001);
                getConversionMultiplierTest.run(MetricScaleUnit.Uni, MetricScaleUnit.Giga,           0.000000001);
                getConversionMultiplierTest.run(MetricScaleUnit.Uni, MetricScaleUnit.Mega,           0.000001);
                getConversionMultiplierTest.run(MetricScaleUnit.Uni, MetricScaleUnit.Kilo,           0.001);
                getConversionMultiplierTest.run(MetricScaleUnit.Uni, MetricScaleUnit.Uni,            1.0);
                getConversionMultiplierTest.run(MetricScaleUnit.Uni, MetricScaleUnit.Deci,          10.0);
                getConversionMultiplierTest.run(MetricScaleUnit.Uni, MetricScaleUnit.Centi,        100.0);
                getConversionMultiplierTest.run(MetricScaleUnit.Uni, MetricScaleUnit.Milli,       1000.0);
                getConversionMultiplierTest.run(MetricScaleUnit.Uni, MetricScaleUnit.Micro,    1000000.0);
                getConversionMultiplierTest.run(MetricScaleUnit.Uni, MetricScaleUnit.Nano,  1000000000.0);

                getConversionMultiplierTest.run(MetricScaleUnit.Deci, MetricScaleUnit.Yotta,         0.0000000000000000000000001);
                getConversionMultiplierTest.run(MetricScaleUnit.Deci, MetricScaleUnit.Zetta,         0.0000000000000000000001);
                getConversionMultiplierTest.run(MetricScaleUnit.Deci, MetricScaleUnit.Exa,           0.0000000000000000001);
                getConversionMultiplierTest.run(MetricScaleUnit.Deci, MetricScaleUnit.Peta,          0.0000000000000001);
                getConversionMultiplierTest.run(MetricScaleUnit.Deci, MetricScaleUnit.Tera,          0.0000000000001);
                getConversionMultiplierTest.run(MetricScaleUnit.Deci, MetricScaleUnit.Giga,          0.0000000001);
                getConversionMultiplierTest.run(MetricScaleUnit.Deci, MetricScaleUnit.Mega,          0.0000001);
                getConversionMultiplierTest.run(MetricScaleUnit.Deci, MetricScaleUnit.Kilo,          0.0001);
                getConversionMultiplierTest.run(MetricScaleUnit.Deci, MetricScaleUnit.Uni,           0.1);
                getConversionMultiplierTest.run(MetricScaleUnit.Deci, MetricScaleUnit.Deci,          1.0);
                getConversionMultiplierTest.run(MetricScaleUnit.Deci, MetricScaleUnit.Centi,        10.0);
                getConversionMultiplierTest.run(MetricScaleUnit.Deci, MetricScaleUnit.Milli,       100.0);
                getConversionMultiplierTest.run(MetricScaleUnit.Deci, MetricScaleUnit.Micro,    100000.0);
                getConversionMultiplierTest.run(MetricScaleUnit.Deci, MetricScaleUnit.Nano,  100000000.0);

                getConversionMultiplierTest.run(MetricScaleUnit.Centi, MetricScaleUnit.Yotta,        0.00000000000000000000000001);
                getConversionMultiplierTest.run(MetricScaleUnit.Centi, MetricScaleUnit.Zetta,        0.00000000000000000000001);
                getConversionMultiplierTest.run(MetricScaleUnit.Centi, MetricScaleUnit.Exa,          0.00000000000000000001);
                getConversionMultiplierTest.run(MetricScaleUnit.Centi, MetricScaleUnit.Peta,         0.00000000000000001);
                getConversionMultiplierTest.run(MetricScaleUnit.Centi, MetricScaleUnit.Tera,         0.00000000000001);
                getConversionMultiplierTest.run(MetricScaleUnit.Centi, MetricScaleUnit.Giga,         0.00000000001);
                getConversionMultiplierTest.run(MetricScaleUnit.Centi, MetricScaleUnit.Mega,         0.00000001);
                getConversionMultiplierTest.run(MetricScaleUnit.Centi, MetricScaleUnit.Kilo,         0.00001);
                getConversionMultiplierTest.run(MetricScaleUnit.Centi, MetricScaleUnit.Uni,          0.01);
                getConversionMultiplierTest.run(MetricScaleUnit.Centi, MetricScaleUnit.Deci,         0.1);
                getConversionMultiplierTest.run(MetricScaleUnit.Centi, MetricScaleUnit.Centi,        1.0);
                getConversionMultiplierTest.run(MetricScaleUnit.Centi, MetricScaleUnit.Milli,       10.0);
                getConversionMultiplierTest.run(MetricScaleUnit.Centi, MetricScaleUnit.Micro,    10000.0);
                getConversionMultiplierTest.run(MetricScaleUnit.Centi, MetricScaleUnit.Nano,  10000000.0);

                getConversionMultiplierTest.run(MetricScaleUnit.Milli, MetricScaleUnit.Yotta,       0.000000000000000000000000001);
                getConversionMultiplierTest.run(MetricScaleUnit.Milli, MetricScaleUnit.Zetta,       0.000000000000000000000001);
                getConversionMultiplierTest.run(MetricScaleUnit.Milli, MetricScaleUnit.Exa,         0.000000000000000000001);
                getConversionMultiplierTest.run(MetricScaleUnit.Milli, MetricScaleUnit.Peta,        0.000000000000000001);
                getConversionMultiplierTest.run(MetricScaleUnit.Milli, MetricScaleUnit.Tera,        0.000000000000001);
                getConversionMultiplierTest.run(MetricScaleUnit.Milli, MetricScaleUnit.Giga,        0.000000000001);
                getConversionMultiplierTest.run(MetricScaleUnit.Milli, MetricScaleUnit.Mega,        0.000000001);
                getConversionMultiplierTest.run(MetricScaleUnit.Milli, MetricScaleUnit.Kilo,        0.000001);
                getConversionMultiplierTest.run(MetricScaleUnit.Milli, MetricScaleUnit.Uni,         0.001);
                getConversionMultiplierTest.run(MetricScaleUnit.Milli, MetricScaleUnit.Deci,        0.01);
                getConversionMultiplierTest.run(MetricScaleUnit.Milli, MetricScaleUnit.Centi,       0.1);
                getConversionMultiplierTest.run(MetricScaleUnit.Milli, MetricScaleUnit.Milli,       1.0);
                getConversionMultiplierTest.run(MetricScaleUnit.Milli, MetricScaleUnit.Micro,    1000.0);
                getConversionMultiplierTest.run(MetricScaleUnit.Milli, MetricScaleUnit.Nano,  1000000.0);

                getConversionMultiplierTest.run(MetricScaleUnit.Micro, MetricScaleUnit.Yotta,    0.000000000000000000000000000001);
                getConversionMultiplierTest.run(MetricScaleUnit.Micro, MetricScaleUnit.Zetta,    0.000000000000000000000000001);
                getConversionMultiplierTest.run(MetricScaleUnit.Micro, MetricScaleUnit.Exa,      0.000000000000000000000001);
                getConversionMultiplierTest.run(MetricScaleUnit.Micro, MetricScaleUnit.Peta,     0.000000000000000000001);
                getConversionMultiplierTest.run(MetricScaleUnit.Micro, MetricScaleUnit.Tera,     0.000000000000000001);
                getConversionMultiplierTest.run(MetricScaleUnit.Micro, MetricScaleUnit.Giga,     0.000000000000001);
                getConversionMultiplierTest.run(MetricScaleUnit.Micro, MetricScaleUnit.Mega,     0.000000000001);
                getConversionMultiplierTest.run(MetricScaleUnit.Micro, MetricScaleUnit.Kilo,     0.000000001);
                getConversionMultiplierTest.run(MetricScaleUnit.Micro, MetricScaleUnit.Uni,      0.000001);
                getConversionMultiplierTest.run(MetricScaleUnit.Micro, MetricScaleUnit.Deci,     0.00001);
                getConversionMultiplierTest.run(MetricScaleUnit.Micro, MetricScaleUnit.Centi,    0.0001);
                getConversionMultiplierTest.run(MetricScaleUnit.Micro, MetricScaleUnit.Milli,    0.001);
                getConversionMultiplierTest.run(MetricScaleUnit.Micro, MetricScaleUnit.Micro,    1.0);
                getConversionMultiplierTest.run(MetricScaleUnit.Micro, MetricScaleUnit.Nano,  1000.0);

                getConversionMultiplierTest.run(MetricScaleUnit.Nano, MetricScaleUnit.Yotta, 0.000000000000000000000000000000001);
                getConversionMultiplierTest.run(MetricScaleUnit.Nano, MetricScaleUnit.Zetta, 0.000000000000000000000000000001);
                getConversionMultiplierTest.run(MetricScaleUnit.Nano, MetricScaleUnit.Exa,   0.000000000000000000000000001);
                getConversionMultiplierTest.run(MetricScaleUnit.Nano, MetricScaleUnit.Peta,  0.000000000000000000000001);
                getConversionMultiplierTest.run(MetricScaleUnit.Nano, MetricScaleUnit.Tera,  0.000000000000000000001);
                getConversionMultiplierTest.run(MetricScaleUnit.Nano, MetricScaleUnit.Giga,  0.000000000000000001);
                getConversionMultiplierTest.run(MetricScaleUnit.Nano, MetricScaleUnit.Mega,  0.000000000000001);
                getConversionMultiplierTest.run(MetricScaleUnit.Nano, MetricScaleUnit.Kilo,  0.000000000001);
                getConversionMultiplierTest.run(MetricScaleUnit.Nano, MetricScaleUnit.Uni,   0.000000001);
                getConversionMultiplierTest.run(MetricScaleUnit.Nano, MetricScaleUnit.Deci,  0.00000001);
                getConversionMultiplierTest.run(MetricScaleUnit.Nano, MetricScaleUnit.Centi, 0.0000001);
                getConversionMultiplierTest.run(MetricScaleUnit.Nano, MetricScaleUnit.Milli, 0.000001);
                getConversionMultiplierTest.run(MetricScaleUnit.Nano, MetricScaleUnit.Micro, 0.001);
                getConversionMultiplierTest.run(MetricScaleUnit.Nano, MetricScaleUnit.Nano,  1.0);
            });
        });
    }
}
