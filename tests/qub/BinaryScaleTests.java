package qub;

public interface BinaryScaleTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(BinaryScale.class, () ->
        {
            final Action3<Double,String,Double> constantTest = (Double constantValue, String constantName, Double expected) ->
            {
                runner.test(constantName, (Test test) ->
                {
                    test.assertEqual(expected, constantValue);
                });
            };

            constantTest.run(BinaryScale.yobiToZebi, "yobiToZebi", 1024.0);
            constantTest.run(BinaryScale.yobiToExbi, "yobiToExbi", 1048576.0);
            constantTest.run(BinaryScale.yobiToPebi, "yobiToPebi", 1073741824.0);
            constantTest.run(BinaryScale.yobiToTebi, "yobiToTebi", 1099511627776.0);
            constantTest.run(BinaryScale.yobiToGibi, "yobiToGibi", 1125899906842624.0);
            constantTest.run(BinaryScale.yobiToMebi, "yobiToMebi", 1152921504606846980.0);
            constantTest.run(BinaryScale.yobiToKibi, "yobiToKibi", 1180591620717411300000.0);
            constantTest.run(BinaryScale.yobiToUni, "yobiToUni", 1208925819614629200000000.0);

            constantTest.run(BinaryScale.zebiToYobi, "zebiToYobi", 0.0009765625);
            constantTest.run(BinaryScale.zebiToExbi, "zebiToExbi", 1024.0);
            constantTest.run(BinaryScale.zebiToPebi, "zebiToPebi", 1048576.0);
            constantTest.run(BinaryScale.zebiToTebi, "zebiToTebi", 1073741824.0);
            constantTest.run(BinaryScale.zebiToGibi, "zebiToGibi", 1099511627776.0);
            constantTest.run(BinaryScale.zebiToMebi, "zebiToMebi", 1125899906842624.0);
            constantTest.run(BinaryScale.zebiToKibi, "zebiToKibi", 1152921504606846980.0);
            constantTest.run(BinaryScale.zebiToUni, "zebiToUni", 1180591620717411300000.0);

            constantTest.run(BinaryScale.exbiToYobi, "exbiToYobi", 0.00000095367431640625);
            constantTest.run(BinaryScale.exbiToZebi, "exbiToZebi", 0.0009765625);
            constantTest.run(BinaryScale.exbiToPebi, "exbiToPebi", 1024.0);
            constantTest.run(BinaryScale.exbiToTebi, "exbiToTebi", 1048576.0);
            constantTest.run(BinaryScale.exbiToGibi, "exbiToGibi", 1073741824.0);
            constantTest.run(BinaryScale.exbiToMebi, "exbiToMebi", 1099511627776.0);
            constantTest.run(BinaryScale.exbiToKibi, "exbiToKibi", 1125899906842624.0);
            constantTest.run(BinaryScale.exbiToUni, "exbiToUni", 1152921504606846980.0);

            constantTest.run(BinaryScale.pebiToYobi, "pebiToYobi", 0.0000000009313225746154785);
            constantTest.run(BinaryScale.pebiToZebi, "pebiToZebi", 0.00000095367431640625);
            constantTest.run(BinaryScale.pebiToExbi, "pebiToExbi", 0.0009765625);
            constantTest.run(BinaryScale.pebiToTebi, "pebiToTebi", 1024.0);
            constantTest.run(BinaryScale.pebiToGibi, "pebiToGibi", 1048576.0);
            constantTest.run(BinaryScale.pebiToMebi, "pebiToMebi", 1073741824.0);
            constantTest.run(BinaryScale.pebiToKibi, "pebiToKibi", 1099511627776.0);
            constantTest.run(BinaryScale.pebiToUni, "pebiToUni", 1125899906842624.0);

            constantTest.run(BinaryScale.tebiToYobi, "tebiToYobi", 0.0000000000009094947017729282);
            constantTest.run(BinaryScale.tebiToZebi, "tebiToZebi", 0.0000000009313225746154785);
            constantTest.run(BinaryScale.tebiToExbi, "tebiToExbi", 0.00000095367431640625);
            constantTest.run(BinaryScale.tebiToPebi, "tebiToPebi", 0.0009765625);
            constantTest.run(BinaryScale.tebiToGibi, "tebiToGibi", 1024.0);
            constantTest.run(BinaryScale.tebiToMebi, "tebiToMebi", 1048576.0);
            constantTest.run(BinaryScale.tebiToKibi, "tebiToKibi", 1073741824.0);
            constantTest.run(BinaryScale.tebiToUni, "tebiToUni", 1099511627776.0);

            constantTest.run(BinaryScale.gibiToYobi, "gibiToYobi", 0.0000000000000008881784197001252);
            constantTest.run(BinaryScale.gibiToZebi, "gibiToZebi", 0.0000000000009094947017729282);
            constantTest.run(BinaryScale.gibiToExbi, "gibiToExbi", 0.0000000009313225746154785);
            constantTest.run(BinaryScale.gibiToPebi, "gibiToPebi", 0.00000095367431640625);
            constantTest.run(BinaryScale.gibiToTebi, "gibiToTebi", 0.0009765625);
            constantTest.run(BinaryScale.gibiToMebi, "gibiToMebi", 1024.0);
            constantTest.run(BinaryScale.gibiToKibi, "gibiToKibi", 1048576.0);
            constantTest.run(BinaryScale.gibiToUni, "gibiToUni", 1073741824.0);

            constantTest.run(BinaryScale.mebiToYobi, "mebiToYobi", 0.0000000000000000008673617379884035);
            constantTest.run(BinaryScale.mebiToZebi, "mebiToZebi", 0.0000000000000008881784197001252);
            constantTest.run(BinaryScale.mebiToExbi, "mebiToExbi", 0.0000000000009094947017729282);
            constantTest.run(BinaryScale.mebiToPebi, "mebiToPebi", 0.0000000009313225746154785);
            constantTest.run(BinaryScale.mebiToTebi, "mebiToTebi", 0.00000095367431640625);
            constantTest.run(BinaryScale.mebiToGibi, "mebiToGibi", 0.0009765625);
            constantTest.run(BinaryScale.mebiToKibi, "mebiToKibi", 1024.0);
            constantTest.run(BinaryScale.mebiToUni, "mebiToUni", 1048576.0);

            constantTest.run(BinaryScale.kibiToYobi, "kibiToYobi", 0.0000000000000000000008470329472543003);
            constantTest.run(BinaryScale.kibiToZebi, "kibiToZebi", 0.0000000000000000008673617379884035);
            constantTest.run(BinaryScale.kibiToExbi, "kibiToExbi", 0.0000000000000008881784197001252);
            constantTest.run(BinaryScale.kibiToPebi, "kibiToPebi", 0.0000000000009094947017729282);
            constantTest.run(BinaryScale.kibiToTebi, "kibiToTebi", 0.0000000009313225746154785);
            constantTest.run(BinaryScale.kibiToGibi, "kibiToGibi", 0.00000095367431640625);
            constantTest.run(BinaryScale.kibiToMebi, "kibiToMebi", 0.0009765625);
            constantTest.run(BinaryScale.kibiToUni, "kibiToUni", 1024.0);

            constantTest.run(BinaryScale.uniToYobi, "uniToYobi", 0.0000000000000000000000008271806125530277);
            constantTest.run(BinaryScale.uniToZebi, "uniToZebi", 0.0000000000000000000008470329472543003);
            constantTest.run(BinaryScale.uniToExbi, "uniToExbi", 0.0000000000000000008673617379884035);
            constantTest.run(BinaryScale.uniToPebi, "uniToPebi", 0.0000000000000008881784197001252);
            constantTest.run(BinaryScale.uniToTebi, "uniToTebi", 0.0000000000009094947017729282);
            constantTest.run(BinaryScale.uniToGibi, "uniToGibi", 0.0000000009313225746154785);
            constantTest.run(BinaryScale.uniToMebi, "uniToMebi", 0.00000095367431640625);
            constantTest.run(BinaryScale.uniToKibi, "uniToKibi", 0.0009765625);

            runner.testGroup("getConversionMultiplier(BinaryScaleUnit,BinaryScaleUnit)", () ->
            {
                final Action3<BinaryScaleUnit,BinaryScaleUnit,Throwable> getConversionMultiplierErrorTest = (BinaryScaleUnit from, BinaryScaleUnit to, Throwable expected) ->
                {
                    runner.test("with " + English.andList(from, to), (Test test) ->
                    {
                        test.assertThrows(() -> BinaryScale.getConversionMultiplier(from, to), expected);
                    });
                };

                getConversionMultiplierErrorTest.run(null, null, new PreConditionFailure("from cannot be null."));
                getConversionMultiplierErrorTest.run(null, BinaryScaleUnit.Mebi, new PreConditionFailure("from cannot be null."));
                getConversionMultiplierErrorTest.run(BinaryScaleUnit.Kibi, null, new PreConditionFailure("to cannot be null."));

                final Action3<BinaryScaleUnit,BinaryScaleUnit,Double> getConversionMultiplierTest = (BinaryScaleUnit from, BinaryScaleUnit to, Double expected) ->
                {
                    runner.test("with " + English.andList(from, to), (Test test) ->
                    {
                        test.assertEqual(expected, BinaryScale.getConversionMultiplier(from, to));
                    });
                };

                getConversionMultiplierTest.run(BinaryScaleUnit.Yobi, BinaryScaleUnit.Yobi, 1.0);
                getConversionMultiplierTest.run(BinaryScaleUnit.Yobi, BinaryScaleUnit.Zebi, 1024.0);
                getConversionMultiplierTest.run(BinaryScaleUnit.Yobi, BinaryScaleUnit.Exbi, 1048576.0);
                getConversionMultiplierTest.run(BinaryScaleUnit.Yobi, BinaryScaleUnit.Pebi, 1073741824.0);
                getConversionMultiplierTest.run(BinaryScaleUnit.Yobi, BinaryScaleUnit.Tebi, 1099511627776.0);
                getConversionMultiplierTest.run(BinaryScaleUnit.Yobi, BinaryScaleUnit.Gibi, 1125899906842624.0);
                getConversionMultiplierTest.run(BinaryScaleUnit.Yobi, BinaryScaleUnit.Mebi, 1152921504606846980.0);
                getConversionMultiplierTest.run(BinaryScaleUnit.Yobi, BinaryScaleUnit.Kibi, 1180591620717411300000.0);
                getConversionMultiplierTest.run(BinaryScaleUnit.Yobi, BinaryScaleUnit.Uni, 1208925819614629200000000.0);

                getConversionMultiplierTest.run(BinaryScaleUnit.Zebi, BinaryScaleUnit.Yobi, 0.0009765625);
                getConversionMultiplierTest.run(BinaryScaleUnit.Zebi, BinaryScaleUnit.Zebi, 1.0);
                getConversionMultiplierTest.run(BinaryScaleUnit.Zebi, BinaryScaleUnit.Exbi, 1024.0);
                getConversionMultiplierTest.run(BinaryScaleUnit.Zebi, BinaryScaleUnit.Pebi, 1048576.0);
                getConversionMultiplierTest.run(BinaryScaleUnit.Zebi, BinaryScaleUnit.Tebi, 1073741824.0);
                getConversionMultiplierTest.run(BinaryScaleUnit.Zebi, BinaryScaleUnit.Gibi, 1099511627776.0);
                getConversionMultiplierTest.run(BinaryScaleUnit.Zebi, BinaryScaleUnit.Mebi, 1125899906842624.0);
                getConversionMultiplierTest.run(BinaryScaleUnit.Zebi, BinaryScaleUnit.Kibi, 1152921504606846980.0);
                getConversionMultiplierTest.run(BinaryScaleUnit.Zebi, BinaryScaleUnit.Uni, 1180591620717411300000.0);

                getConversionMultiplierTest.run(BinaryScaleUnit.Exbi, BinaryScaleUnit.Yobi, 0.00000095367431640625);
                getConversionMultiplierTest.run(BinaryScaleUnit.Exbi, BinaryScaleUnit.Zebi, 0.0009765625);
                getConversionMultiplierTest.run(BinaryScaleUnit.Exbi, BinaryScaleUnit.Exbi, 1.0);
                getConversionMultiplierTest.run(BinaryScaleUnit.Exbi, BinaryScaleUnit.Pebi, 1024.0);
                getConversionMultiplierTest.run(BinaryScaleUnit.Exbi, BinaryScaleUnit.Tebi, 1048576.0);
                getConversionMultiplierTest.run(BinaryScaleUnit.Exbi, BinaryScaleUnit.Gibi, 1073741824.0);
                getConversionMultiplierTest.run(BinaryScaleUnit.Exbi, BinaryScaleUnit.Mebi, 1099511627776.0);
                getConversionMultiplierTest.run(BinaryScaleUnit.Exbi, BinaryScaleUnit.Kibi, 1125899906842624.0);
                getConversionMultiplierTest.run(BinaryScaleUnit.Exbi, BinaryScaleUnit.Uni, 1152921504606846980.0);

                getConversionMultiplierTest.run(BinaryScaleUnit.Pebi, BinaryScaleUnit.Yobi, 0.0000000009313225746154785);
                getConversionMultiplierTest.run(BinaryScaleUnit.Pebi, BinaryScaleUnit.Zebi, 0.00000095367431640625);
                getConversionMultiplierTest.run(BinaryScaleUnit.Pebi, BinaryScaleUnit.Exbi, 0.0009765625);
                getConversionMultiplierTest.run(BinaryScaleUnit.Pebi, BinaryScaleUnit.Pebi, 1.0);
                getConversionMultiplierTest.run(BinaryScaleUnit.Pebi, BinaryScaleUnit.Tebi, 1024.0);
                getConversionMultiplierTest.run(BinaryScaleUnit.Pebi, BinaryScaleUnit.Gibi, 1048576.0);
                getConversionMultiplierTest.run(BinaryScaleUnit.Pebi, BinaryScaleUnit.Mebi, 1073741824.0);
                getConversionMultiplierTest.run(BinaryScaleUnit.Pebi, BinaryScaleUnit.Kibi, 1099511627776.0);
                getConversionMultiplierTest.run(BinaryScaleUnit.Pebi, BinaryScaleUnit.Uni, 1125899906842624.0);

                getConversionMultiplierTest.run(BinaryScaleUnit.Tebi, BinaryScaleUnit.Yobi, 0.0000000000009094947017729282);
                getConversionMultiplierTest.run(BinaryScaleUnit.Tebi, BinaryScaleUnit.Zebi, 0.0000000009313225746154785);
                getConversionMultiplierTest.run(BinaryScaleUnit.Tebi, BinaryScaleUnit.Exbi, 0.00000095367431640625);
                getConversionMultiplierTest.run(BinaryScaleUnit.Tebi, BinaryScaleUnit.Pebi, 0.0009765625);
                getConversionMultiplierTest.run(BinaryScaleUnit.Tebi, BinaryScaleUnit.Tebi, 1.0);
                getConversionMultiplierTest.run(BinaryScaleUnit.Tebi, BinaryScaleUnit.Gibi, 1024.0);
                getConversionMultiplierTest.run(BinaryScaleUnit.Tebi, BinaryScaleUnit.Mebi, 1048576.0);
                getConversionMultiplierTest.run(BinaryScaleUnit.Tebi, BinaryScaleUnit.Kibi, 1073741824.0);
                getConversionMultiplierTest.run(BinaryScaleUnit.Tebi, BinaryScaleUnit.Uni, 1099511627776.0);

                getConversionMultiplierTest.run(BinaryScaleUnit.Gibi, BinaryScaleUnit.Yobi, 0.0000000000000008881784197001252);
                getConversionMultiplierTest.run(BinaryScaleUnit.Gibi, BinaryScaleUnit.Zebi, 0.0000000000009094947017729282);
                getConversionMultiplierTest.run(BinaryScaleUnit.Gibi, BinaryScaleUnit.Exbi, 0.0000000009313225746154785);
                getConversionMultiplierTest.run(BinaryScaleUnit.Gibi, BinaryScaleUnit.Pebi, 0.00000095367431640625);
                getConversionMultiplierTest.run(BinaryScaleUnit.Gibi, BinaryScaleUnit.Tebi, 0.0009765625);
                getConversionMultiplierTest.run(BinaryScaleUnit.Gibi, BinaryScaleUnit.Gibi, 1.0);
                getConversionMultiplierTest.run(BinaryScaleUnit.Gibi, BinaryScaleUnit.Mebi, 1024.0);
                getConversionMultiplierTest.run(BinaryScaleUnit.Gibi, BinaryScaleUnit.Kibi, 1048576.0);
                getConversionMultiplierTest.run(BinaryScaleUnit.Gibi, BinaryScaleUnit.Uni, 1073741824.0);

                getConversionMultiplierTest.run(BinaryScaleUnit.Mebi, BinaryScaleUnit.Yobi, 0.0000000000000000008673617379884035);
                getConversionMultiplierTest.run(BinaryScaleUnit.Mebi, BinaryScaleUnit.Zebi, 0.0000000000000008881784197001252);
                getConversionMultiplierTest.run(BinaryScaleUnit.Mebi, BinaryScaleUnit.Exbi, 0.0000000000009094947017729282);
                getConversionMultiplierTest.run(BinaryScaleUnit.Mebi, BinaryScaleUnit.Pebi, 0.0000000009313225746154785);
                getConversionMultiplierTest.run(BinaryScaleUnit.Mebi, BinaryScaleUnit.Tebi, 0.00000095367431640625);
                getConversionMultiplierTest.run(BinaryScaleUnit.Mebi, BinaryScaleUnit.Gibi, 0.0009765625);
                getConversionMultiplierTest.run(BinaryScaleUnit.Mebi, BinaryScaleUnit.Mebi, 1.0);
                getConversionMultiplierTest.run(BinaryScaleUnit.Mebi, BinaryScaleUnit.Kibi, 1024.0);
                getConversionMultiplierTest.run(BinaryScaleUnit.Mebi, BinaryScaleUnit.Uni, 1048576.0);

                getConversionMultiplierTest.run(BinaryScaleUnit.Kibi, BinaryScaleUnit.Yobi, 0.0000000000000000000008470329472543003);
                getConversionMultiplierTest.run(BinaryScaleUnit.Kibi, BinaryScaleUnit.Zebi, 0.0000000000000000008673617379884035);
                getConversionMultiplierTest.run(BinaryScaleUnit.Kibi, BinaryScaleUnit.Exbi, 0.0000000000000008881784197001252);
                getConversionMultiplierTest.run(BinaryScaleUnit.Kibi, BinaryScaleUnit.Pebi, 0.0000000000009094947017729282);
                getConversionMultiplierTest.run(BinaryScaleUnit.Kibi, BinaryScaleUnit.Tebi, 0.0000000009313225746154785);
                getConversionMultiplierTest.run(BinaryScaleUnit.Kibi, BinaryScaleUnit.Gibi, 0.00000095367431640625);
                getConversionMultiplierTest.run(BinaryScaleUnit.Kibi, BinaryScaleUnit.Mebi, 0.0009765625);
                getConversionMultiplierTest.run(BinaryScaleUnit.Kibi, BinaryScaleUnit.Kibi, 1.0);
                getConversionMultiplierTest.run(BinaryScaleUnit.Kibi, BinaryScaleUnit.Uni, 1024.0);

                getConversionMultiplierTest.run(BinaryScaleUnit.Uni, BinaryScaleUnit.Yobi, 0.0000000000000000000000008271806125530277);
                getConversionMultiplierTest.run(BinaryScaleUnit.Uni, BinaryScaleUnit.Zebi, 0.0000000000000000000008470329472543003);
                getConversionMultiplierTest.run(BinaryScaleUnit.Uni, BinaryScaleUnit.Exbi, 0.0000000000000000008673617379884035);
                getConversionMultiplierTest.run(BinaryScaleUnit.Uni, BinaryScaleUnit.Pebi, 0.0000000000000008881784197001252);
                getConversionMultiplierTest.run(BinaryScaleUnit.Uni, BinaryScaleUnit.Tebi, 0.0000000000009094947017729282);
                getConversionMultiplierTest.run(BinaryScaleUnit.Uni, BinaryScaleUnit.Gibi, 0.0000000009313225746154785);
                getConversionMultiplierTest.run(BinaryScaleUnit.Uni, BinaryScaleUnit.Mebi, 0.00000095367431640625);
                getConversionMultiplierTest.run(BinaryScaleUnit.Uni, BinaryScaleUnit.Kibi, 0.0009765625);
                getConversionMultiplierTest.run(BinaryScaleUnit.Uni, BinaryScaleUnit.Uni, 1.0);
            });
        });
    }
}
