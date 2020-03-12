package qub;

public interface MetricScaleTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(MetricScale.class, () ->
        {
            final Action3<Double,String,Double> constantTest = (Double constantValue, String constantName, Double expected) ->
            {
                runner.test(constantName, (Test test) ->
                {
                    test.assertEqual(expected, constantValue);
                });
            };

            constantTest.run(MetricScale.yottaToZetta, "yottaToZetta", 1000.0);
            constantTest.run(MetricScale.yottaToExa, "yottaToExa", 1000000.0);
            constantTest.run(MetricScale.yottaToPeta, "yottaToPeta", 1000000000.0);
            constantTest.run(MetricScale.yottaToTera, "yottaToTera", 1000000000000.0);
            constantTest.run(MetricScale.yottaToGiga, "yottaToGiga", 1000000000000000.0);
            constantTest.run(MetricScale.yottaToMega, "yottaToMega", 1000000000000000000.0);
            constantTest.run(MetricScale.yottaToKilo, "yottaToKilo", 1000000000000000000000.0);
            constantTest.run(MetricScale.yottaToUni, "yottaToUni", 1000000000000000000000000.0);

            constantTest.run(MetricScale.zettaToYotta, "zettaToYotta", 0.001);
            constantTest.run(MetricScale.zettaToExa, "zettaToExa", 1000.0);
            constantTest.run(MetricScale.zettaToPeta, "zettaToPeta", 1000000.0);
            constantTest.run(MetricScale.zettaToTera, "zettaToTera", 1000000000.0);
            constantTest.run(MetricScale.zettaToGiga, "zettaToGiga", 1000000000000.0);
            constantTest.run(MetricScale.zettaToMega, "zettaToMega", 1000000000000000.0);
            constantTest.run(MetricScale.zettaToKilo, "zettaToKilo", 1000000000000000000.0);
            constantTest.run(MetricScale.zettaToUni, "zettaToUni", 1000000000000000000000.0);

            constantTest.run(MetricScale.exaToYotta, "exaToYotta", 0.000001);
            constantTest.run(MetricScale.exaToZetta, "exaToZetta", 0.001);
            constantTest.run(MetricScale.exaToPeta, "exaToPeta", 1000.0);
            constantTest.run(MetricScale.exaToTera, "exaToTera", 1000000.0);
            constantTest.run(MetricScale.exaToGiga, "exaToGiga", 1000000000.0);
            constantTest.run(MetricScale.exaToMega, "exaToMega", 1000000000000.0);
            constantTest.run(MetricScale.exaToKilo, "exaToKilo", 1000000000000000.0);
            constantTest.run(MetricScale.exaToUni, "exaToUni", 1000000000000000000.0);

            constantTest.run(MetricScale.petaToYotta, "petaToYotta", 0.000000001);
            constantTest.run(MetricScale.petaToZetta, "petaToZetta", 0.000001);
            constantTest.run(MetricScale.petaToExa, "petaToExa", 0.001);
            constantTest.run(MetricScale.petaToTera, "petaToTera", 1000.0);
            constantTest.run(MetricScale.petaToGiga, "petaToGiga", 1000000.0);
            constantTest.run(MetricScale.petaToMega, "petaToMega", 1000000000.0);
            constantTest.run(MetricScale.petaToKilo, "petaToKilo", 1000000000000.0);
            constantTest.run(MetricScale.petaToUni, "petaToUni", 1000000000000000.0);

            constantTest.run(MetricScale.teraToYotta, "teraToYotta", 0.000000000001);
            constantTest.run(MetricScale.teraToZetta, "teraToZetta", 0.000000001);
            constantTest.run(MetricScale.teraToExa, "teraToExa", 0.000001);
            constantTest.run(MetricScale.teraToPeta, "teraToPeta", 0.001);
            constantTest.run(MetricScale.teraToGiga, "teraToGiga", 1000.0);
            constantTest.run(MetricScale.teraToMega, "teraToMega", 1000000.0);
            constantTest.run(MetricScale.teraToKilo, "teraToKilo", 1000000000.0);
            constantTest.run(MetricScale.teraToUni, "teraToUni", 1000000000000.0);

            constantTest.run(MetricScale.gigaToYotta, "gigaToYotta", 0.000000000000001);
            constantTest.run(MetricScale.gigaToZetta, "gigaToZetta", 0.000000000001);
            constantTest.run(MetricScale.gigaToExa, "gigaToExa", 0.000000001);
            constantTest.run(MetricScale.gigaToPeta, "gigaToPeta", 0.000001);
            constantTest.run(MetricScale.gigaToTera, "gigaToTera", 0.001);
            constantTest.run(MetricScale.gigaToMega, "gigaToMega", 1000.0);
            constantTest.run(MetricScale.gigaToKilo, "gigaToKilo", 1000000.0);
            constantTest.run(MetricScale.gigaToUni, "gigaToUni", 1000000000.0);

            constantTest.run(MetricScale.megaToYotta, "megaToYotta", 0.000000000000000001);
            constantTest.run(MetricScale.megaToZetta, "megaToZetta", 0.000000000000001);
            constantTest.run(MetricScale.megaToExa, "megaToExa", 0.000000000001);
            constantTest.run(MetricScale.megaToPeta, "megaToPeta", 0.000000001);
            constantTest.run(MetricScale.megaToTera, "megaToTera", 0.000001);
            constantTest.run(MetricScale.megaToGiga, "megaToGiga", 0.001);
            constantTest.run(MetricScale.megaToKilo, "megaToKilo", 1000.0);
            constantTest.run(MetricScale.megaToUni, "megaToUni", 1000000.0);

            constantTest.run(MetricScale.kiloToYotta, "kiloToYotta", 0.000000000000000000001);
            constantTest.run(MetricScale.kiloToZetta, "kiloToZetta", 0.000000000000000001);
            constantTest.run(MetricScale.kiloToExa, "kiloToExa", 0.000000000000001);
            constantTest.run(MetricScale.kiloToPeta, "kiloToPeta", 0.000000000001);
            constantTest.run(MetricScale.kiloToTera, "kiloToTera", 0.000000001);
            constantTest.run(MetricScale.kiloToGiga, "kiloToGiga", 0.000001);
            constantTest.run(MetricScale.kiloToMega, "kiloToMega", 0.001);
            constantTest.run(MetricScale.kiloToUni, "kiloToUni", 1000.0);

            constantTest.run(MetricScale.uniToYotta, "uniToYotta", 0.000000000000000000000001);
            constantTest.run(MetricScale.uniToZetta, "uniToZetta", 0.000000000000000000001);
            constantTest.run(MetricScale.uniToExa, "uniToExa", 0.000000000000000001);
            constantTest.run(MetricScale.uniToPeta, "uniToPeta", 0.000000000000001);
            constantTest.run(MetricScale.uniToTera, "uniToTera", 0.000000000001);
            constantTest.run(MetricScale.uniToGiga, "uniToGiga", 0.000000001);
            constantTest.run(MetricScale.uniToMega, "uniToMega", 0.000001);
            constantTest.run(MetricScale.uniToKilo, "uniToKilo", 0.001);

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

                getConversionMultiplierTest.run(MetricScaleUnit.Yotta, MetricScaleUnit.Yotta, 1.0);
                getConversionMultiplierTest.run(MetricScaleUnit.Yotta, MetricScaleUnit.Zetta, 1000.0);
                getConversionMultiplierTest.run(MetricScaleUnit.Yotta, MetricScaleUnit.Exa, 1000000.0);
                getConversionMultiplierTest.run(MetricScaleUnit.Yotta, MetricScaleUnit.Peta, 1000000000.0);
                getConversionMultiplierTest.run(MetricScaleUnit.Yotta, MetricScaleUnit.Tera, 1000000000000.0);
                getConversionMultiplierTest.run(MetricScaleUnit.Yotta, MetricScaleUnit.Giga, 1000000000000000.0);
                getConversionMultiplierTest.run(MetricScaleUnit.Yotta, MetricScaleUnit.Mega, 1000000000000000000.0);
                getConversionMultiplierTest.run(MetricScaleUnit.Yotta, MetricScaleUnit.Kilo, 1000000000000000000000.0);
                getConversionMultiplierTest.run(MetricScaleUnit.Yotta, MetricScaleUnit.Uni, 1000000000000000000000000.0);

                getConversionMultiplierTest.run(MetricScaleUnit.Zetta, MetricScaleUnit.Yotta, 0.001);
                getConversionMultiplierTest.run(MetricScaleUnit.Zetta, MetricScaleUnit.Zetta, 1.0);
                getConversionMultiplierTest.run(MetricScaleUnit.Zetta, MetricScaleUnit.Exa, 1000.0);
                getConversionMultiplierTest.run(MetricScaleUnit.Zetta, MetricScaleUnit.Peta, 1000000.0);
                getConversionMultiplierTest.run(MetricScaleUnit.Zetta, MetricScaleUnit.Tera, 1000000000.0);
                getConversionMultiplierTest.run(MetricScaleUnit.Zetta, MetricScaleUnit.Giga, 1000000000000.0);
                getConversionMultiplierTest.run(MetricScaleUnit.Zetta, MetricScaleUnit.Mega, 1000000000000000.0);
                getConversionMultiplierTest.run(MetricScaleUnit.Zetta, MetricScaleUnit.Kilo, 1000000000000000000.0);
                getConversionMultiplierTest.run(MetricScaleUnit.Zetta, MetricScaleUnit.Uni, 1000000000000000000000.0);

                getConversionMultiplierTest.run(MetricScaleUnit.Exa, MetricScaleUnit.Yotta, 0.000001);
                getConversionMultiplierTest.run(MetricScaleUnit.Exa, MetricScaleUnit.Zetta, 0.001);
                getConversionMultiplierTest.run(MetricScaleUnit.Exa, MetricScaleUnit.Exa, 1.0);
                getConversionMultiplierTest.run(MetricScaleUnit.Exa, MetricScaleUnit.Peta, 1000.0);
                getConversionMultiplierTest.run(MetricScaleUnit.Exa, MetricScaleUnit.Tera, 1000000.0);
                getConversionMultiplierTest.run(MetricScaleUnit.Exa, MetricScaleUnit.Giga, 1000000000.0);
                getConversionMultiplierTest.run(MetricScaleUnit.Exa, MetricScaleUnit.Mega, 1000000000000.0);
                getConversionMultiplierTest.run(MetricScaleUnit.Exa, MetricScaleUnit.Kilo, 1000000000000000.0);
                getConversionMultiplierTest.run(MetricScaleUnit.Exa, MetricScaleUnit.Uni, 1000000000000000000.0);

                getConversionMultiplierTest.run(MetricScaleUnit.Peta, MetricScaleUnit.Yotta, 0.000000001);
                getConversionMultiplierTest.run(MetricScaleUnit.Peta, MetricScaleUnit.Zetta, 0.000001);
                getConversionMultiplierTest.run(MetricScaleUnit.Peta, MetricScaleUnit.Exa, 0.001);
                getConversionMultiplierTest.run(MetricScaleUnit.Peta, MetricScaleUnit.Peta, 1.0);
                getConversionMultiplierTest.run(MetricScaleUnit.Peta, MetricScaleUnit.Tera, 1000.0);
                getConversionMultiplierTest.run(MetricScaleUnit.Peta, MetricScaleUnit.Giga, 1000000.0);
                getConversionMultiplierTest.run(MetricScaleUnit.Peta, MetricScaleUnit.Mega, 1000000000.0);
                getConversionMultiplierTest.run(MetricScaleUnit.Peta, MetricScaleUnit.Kilo, 1000000000000.0);
                getConversionMultiplierTest.run(MetricScaleUnit.Peta, MetricScaleUnit.Uni, 1000000000000000.0);

                getConversionMultiplierTest.run(MetricScaleUnit.Tera, MetricScaleUnit.Yotta, 0.000000000001);
                getConversionMultiplierTest.run(MetricScaleUnit.Tera, MetricScaleUnit.Zetta, 0.000000001);
                getConversionMultiplierTest.run(MetricScaleUnit.Tera, MetricScaleUnit.Exa, 0.000001);
                getConversionMultiplierTest.run(MetricScaleUnit.Tera, MetricScaleUnit.Peta, 0.001);
                getConversionMultiplierTest.run(MetricScaleUnit.Tera, MetricScaleUnit.Tera, 1.0);
                getConversionMultiplierTest.run(MetricScaleUnit.Tera, MetricScaleUnit.Giga, 1000.0);
                getConversionMultiplierTest.run(MetricScaleUnit.Tera, MetricScaleUnit.Mega, 1000000.0);
                getConversionMultiplierTest.run(MetricScaleUnit.Tera, MetricScaleUnit.Kilo, 1000000000.0);
                getConversionMultiplierTest.run(MetricScaleUnit.Tera, MetricScaleUnit.Uni, 1000000000000.0);

                getConversionMultiplierTest.run(MetricScaleUnit.Giga, MetricScaleUnit.Yotta, 0.000000000000001);
                getConversionMultiplierTest.run(MetricScaleUnit.Giga, MetricScaleUnit.Zetta, 0.000000000001);
                getConversionMultiplierTest.run(MetricScaleUnit.Giga, MetricScaleUnit.Exa, 0.000000001);
                getConversionMultiplierTest.run(MetricScaleUnit.Giga, MetricScaleUnit.Peta, 0.000001);
                getConversionMultiplierTest.run(MetricScaleUnit.Giga, MetricScaleUnit.Tera, 0.001);
                getConversionMultiplierTest.run(MetricScaleUnit.Giga, MetricScaleUnit.Giga, 1.0);
                getConversionMultiplierTest.run(MetricScaleUnit.Giga, MetricScaleUnit.Mega, 1000.0);
                getConversionMultiplierTest.run(MetricScaleUnit.Giga, MetricScaleUnit.Kilo, 1000000.0);
                getConversionMultiplierTest.run(MetricScaleUnit.Giga, MetricScaleUnit.Uni, 1000000000.0);

                getConversionMultiplierTest.run(MetricScaleUnit.Mega, MetricScaleUnit.Yotta, 0.000000000000000001);
                getConversionMultiplierTest.run(MetricScaleUnit.Mega, MetricScaleUnit.Zetta, 0.000000000000001);
                getConversionMultiplierTest.run(MetricScaleUnit.Mega, MetricScaleUnit.Exa, 0.000000000001);
                getConversionMultiplierTest.run(MetricScaleUnit.Mega, MetricScaleUnit.Peta, 0.000000001);
                getConversionMultiplierTest.run(MetricScaleUnit.Mega, MetricScaleUnit.Tera, 0.000001);
                getConversionMultiplierTest.run(MetricScaleUnit.Mega, MetricScaleUnit.Giga, 0.001);
                getConversionMultiplierTest.run(MetricScaleUnit.Mega, MetricScaleUnit.Mega, 1.0);
                getConversionMultiplierTest.run(MetricScaleUnit.Mega, MetricScaleUnit.Kilo, 1000.0);
                getConversionMultiplierTest.run(MetricScaleUnit.Mega, MetricScaleUnit.Uni, 1000000.0);

                getConversionMultiplierTest.run(MetricScaleUnit.Kilo, MetricScaleUnit.Yotta, 0.000000000000000000001);
                getConversionMultiplierTest.run(MetricScaleUnit.Kilo, MetricScaleUnit.Zetta, 0.000000000000000001);
                getConversionMultiplierTest.run(MetricScaleUnit.Kilo, MetricScaleUnit.Exa, 0.000000000000001);
                getConversionMultiplierTest.run(MetricScaleUnit.Kilo, MetricScaleUnit.Peta, 0.000000000001);
                getConversionMultiplierTest.run(MetricScaleUnit.Kilo, MetricScaleUnit.Tera, 0.000000001);
                getConversionMultiplierTest.run(MetricScaleUnit.Kilo, MetricScaleUnit.Giga, 0.000001);
                getConversionMultiplierTest.run(MetricScaleUnit.Kilo, MetricScaleUnit.Mega, 0.001);
                getConversionMultiplierTest.run(MetricScaleUnit.Kilo, MetricScaleUnit.Kilo, 1.0);
                getConversionMultiplierTest.run(MetricScaleUnit.Kilo, MetricScaleUnit.Uni, 1000.0);

                getConversionMultiplierTest.run(MetricScaleUnit.Uni, MetricScaleUnit.Yotta, 0.000000000000000000000001);
                getConversionMultiplierTest.run(MetricScaleUnit.Uni, MetricScaleUnit.Zetta, 0.000000000000000000001);
                getConversionMultiplierTest.run(MetricScaleUnit.Uni, MetricScaleUnit.Exa, 0.000000000000000001);
                getConversionMultiplierTest.run(MetricScaleUnit.Uni, MetricScaleUnit.Peta, 0.000000000000001);
                getConversionMultiplierTest.run(MetricScaleUnit.Uni, MetricScaleUnit.Tera, 0.000000000001);
                getConversionMultiplierTest.run(MetricScaleUnit.Uni, MetricScaleUnit.Giga, 0.000000001);
                getConversionMultiplierTest.run(MetricScaleUnit.Uni, MetricScaleUnit.Mega, 0.000001);
                getConversionMultiplierTest.run(MetricScaleUnit.Uni, MetricScaleUnit.Kilo, 0.001);
                getConversionMultiplierTest.run(MetricScaleUnit.Uni, MetricScaleUnit.Uni, 1.0);
            });
        });
    }
}
