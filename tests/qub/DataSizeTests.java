package qub;

public interface DataSizeTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(DataSize.class, () ->
        {
            runner.testGroup("constructor(double,DataSizeUnit)", () ->
            {
                runner.test("with null units", (Test test) ->
                {
                    test.assertThrows(() -> new DataSize(3, null),
                        new PreConditionFailure("units cannot be null."));
                });

                runner.test("with negative value", (Test test) ->
                {
                    final DataSize dataSize = new DataSize(-4, DataSizeUnit.Gigabytes);
                    test.assertEqual(-4, dataSize.getValue());
                    test.assertEqual(DataSizeUnit.Gigabytes, dataSize.getUnits());
                });

                runner.test("with zero value", (Test test) ->
                {
                    final DataSize dataSize = new DataSize(0, DataSizeUnit.Gigabytes);
                    test.assertEqual(0, dataSize.getValue());
                    test.assertEqual(DataSizeUnit.Gigabytes, dataSize.getUnits());
                });

                runner.test("with positive value", (Test test) ->
                {
                    final DataSize dataSize = new DataSize(13, DataSizeUnit.Gigabytes);
                    test.assertEqual(13, dataSize.getValue());
                    test.assertEqual(DataSizeUnit.Gigabytes, dataSize.getUnits());
                });
            });

            runner.test("yobibytes(double)", (Test test) ->
            {
                final DataSize dataSize = DataSize.yobibytes(1);
                test.assertEqual(1, dataSize.getValue());
                test.assertEqual(DataSizeUnit.Yobibytes, dataSize.getUnits());
            });

            runner.test("zebibytes(double)", (Test test) ->
            {
                final DataSize dataSize = DataSize.zebibytes(2);
                test.assertEqual(2, dataSize.getValue());
                test.assertEqual(DataSizeUnit.Zebibytes, dataSize.getUnits());
            });

            runner.test("exbibytes(double)", (Test test) ->
            {
                final DataSize dataSize = DataSize.exbibytes(3);
                test.assertEqual(3, dataSize.getValue());
                test.assertEqual(DataSizeUnit.Exbibytes, dataSize.getUnits());
            });

            runner.test("pebibytes(double)", (Test test) ->
            {
                final DataSize dataSize = DataSize.pebibytes(4);
                test.assertEqual(4, dataSize.getValue());
                test.assertEqual(DataSizeUnit.Pebibytes, dataSize.getUnits());
            });

            runner.test("tebibytes(double)", (Test test) ->
            {
                final DataSize dataSize = DataSize.tebibytes(5);
                test.assertEqual(5, dataSize.getValue());
                test.assertEqual(DataSizeUnit.Tebibytes, dataSize.getUnits());
            });

            runner.test("gibibytes(double)", (Test test) ->
            {
                final DataSize dataSize = DataSize.gibibytes(6);
                test.assertEqual(6, dataSize.getValue());
                test.assertEqual(DataSizeUnit.Gibibytes, dataSize.getUnits());
            });

            runner.test("mebibytes(double)", (Test test) ->
            {
                final DataSize dataSize = DataSize.mebibytes(7);
                test.assertEqual(7, dataSize.getValue());
                test.assertEqual(DataSizeUnit.Mebibytes, dataSize.getUnits());
            });

            runner.test("kibibytes(double)", (Test test) ->
            {
                final DataSize dataSize = DataSize.kibibytes(8);
                test.assertEqual(8, dataSize.getValue());
                test.assertEqual(DataSizeUnit.Kibibytes, dataSize.getUnits());
            });

            runner.test("yobibits(double)", (Test test) ->
            {
                final DataSize dataSize = DataSize.yobibits(1);
                test.assertEqual(1, dataSize.getValue());
                test.assertEqual(DataSizeUnit.Yobibits, dataSize.getUnits());
            });

            runner.test("zebibits(double)", (Test test) ->
            {
                final DataSize dataSize = DataSize.zebibits(2);
                test.assertEqual(2, dataSize.getValue());
                test.assertEqual(DataSizeUnit.Zebibits, dataSize.getUnits());
            });

            runner.test("exbibits(double)", (Test test) ->
            {
                final DataSize dataSize = DataSize.exbibits(3);
                test.assertEqual(3, dataSize.getValue());
                test.assertEqual(DataSizeUnit.Exbibits, dataSize.getUnits());
            });

            runner.test("pebibits(double)", (Test test) ->
            {
                final DataSize dataSize = DataSize.pebibits(4);
                test.assertEqual(4, dataSize.getValue());
                test.assertEqual(DataSizeUnit.Pebibits, dataSize.getUnits());
            });

            runner.test("tebibits(double)", (Test test) ->
            {
                final DataSize dataSize = DataSize.tebibits(5);
                test.assertEqual(5, dataSize.getValue());
                test.assertEqual(DataSizeUnit.Tebibits, dataSize.getUnits());
            });

            runner.test("gibibits(double)", (Test test) ->
            {
                final DataSize dataSize = DataSize.gibibits(6);
                test.assertEqual(6, dataSize.getValue());
                test.assertEqual(DataSizeUnit.Gibibits, dataSize.getUnits());
            });

            runner.test("mebibits(double)", (Test test) ->
            {
                final DataSize dataSize = DataSize.mebibits(7);
                test.assertEqual(7, dataSize.getValue());
                test.assertEqual(DataSizeUnit.Mebibits, dataSize.getUnits());
            });

            runner.test("kibibits(double)", (Test test) ->
            {
                final DataSize dataSize = DataSize.kibibits(8);
                test.assertEqual(8, dataSize.getValue());
                test.assertEqual(DataSizeUnit.Kibibits, dataSize.getUnits());
            });

            runner.test("yottabytes(double)", (Test test) ->
            {
                final DataSize dataSize = DataSize.yottabytes(1);
                test.assertEqual(1, dataSize.getValue());
                test.assertEqual(DataSizeUnit.Yottabytes, dataSize.getUnits());
            });

            runner.test("zettabytes(double)", (Test test) ->
            {
                final DataSize dataSize = DataSize.zettabytes(2);
                test.assertEqual(2, dataSize.getValue());
                test.assertEqual(DataSizeUnit.Zettabytes, dataSize.getUnits());
            });

            runner.test("exabytes(double)", (Test test) ->
            {
                final DataSize dataSize = DataSize.exabytes(3);
                test.assertEqual(3, dataSize.getValue());
                test.assertEqual(DataSizeUnit.Exabytes, dataSize.getUnits());
            });

            runner.test("petabytes(double)", (Test test) ->
            {
                final DataSize dataSize = DataSize.petabytes(4);
                test.assertEqual(4, dataSize.getValue());
                test.assertEqual(DataSizeUnit.Petabytes, dataSize.getUnits());
            });

            runner.test("terabytes(double)", (Test test) ->
            {
                final DataSize dataSize = DataSize.terabytes(5);
                test.assertEqual(5, dataSize.getValue());
                test.assertEqual(DataSizeUnit.Terabytes, dataSize.getUnits());
            });

            runner.test("gigabytes(double)", (Test test) ->
            {
                final DataSize dataSize = DataSize.gigabytes(6);
                test.assertEqual(6, dataSize.getValue());
                test.assertEqual(DataSizeUnit.Gigabytes, dataSize.getUnits());
            });

            runner.test("megabytes(double)", (Test test) ->
            {
                final DataSize dataSize = DataSize.megabytes(7);
                test.assertEqual(7, dataSize.getValue());
                test.assertEqual(DataSizeUnit.Megabytes, dataSize.getUnits());
            });

            runner.test("kilobytes(double)", (Test test) ->
            {
                final DataSize dataSize = DataSize.kilobytes(8);
                test.assertEqual(8, dataSize.getValue());
                test.assertEqual(DataSizeUnit.Kilobytes, dataSize.getUnits());
            });

            runner.test("yottabits(double)", (Test test) ->
            {
                final DataSize dataSize = DataSize.yottabits(1);
                test.assertEqual(1, dataSize.getValue());
                test.assertEqual(DataSizeUnit.Yottabits, dataSize.getUnits());
            });

            runner.test("zettabits(double)", (Test test) ->
            {
                final DataSize dataSize = DataSize.zettabits(2);
                test.assertEqual(2, dataSize.getValue());
                test.assertEqual(DataSizeUnit.Zettabits, dataSize.getUnits());
            });

            runner.test("exabits(double)", (Test test) ->
            {
                final DataSize dataSize = DataSize.exabits(3);
                test.assertEqual(3, dataSize.getValue());
                test.assertEqual(DataSizeUnit.Exabits, dataSize.getUnits());
            });

            runner.test("petabits(double)", (Test test) ->
            {
                final DataSize dataSize = DataSize.petabits(4);
                test.assertEqual(4, dataSize.getValue());
                test.assertEqual(DataSizeUnit.Petabits, dataSize.getUnits());
            });

            runner.test("terabits(double)", (Test test) ->
            {
                final DataSize dataSize = DataSize.terabits(5);
                test.assertEqual(5, dataSize.getValue());
                test.assertEqual(DataSizeUnit.Terabits, dataSize.getUnits());
            });

            runner.test("gigabits(double)", (Test test) ->
            {
                final DataSize dataSize = DataSize.gigabits(6);
                test.assertEqual(6, dataSize.getValue());
                test.assertEqual(DataSizeUnit.Gigabits, dataSize.getUnits());
            });

            runner.test("megabits(double)", (Test test) ->
            {
                final DataSize dataSize = DataSize.megabits(7);
                test.assertEqual(7, dataSize.getValue());
                test.assertEqual(DataSizeUnit.Megabits, dataSize.getUnits());
            });

            runner.test("kilobits(double)", (Test test) ->
            {
                final DataSize dataSize = DataSize.kilobits(8);
                test.assertEqual(8, dataSize.getValue());
                test.assertEqual(DataSizeUnit.Kilobits, dataSize.getUnits());
            });

            runner.test("bytes(double)", (Test test) ->
            {
                final DataSize dataSize = DataSize.bytes(9);
                test.assertEqual(9, dataSize.getValue());
                test.assertEqual(DataSizeUnit.Bytes, dataSize.getUnits());
            });

            runner.test("bits(double)", (Test test) ->
            {
                final DataSize dataSize = DataSize.bits(10);
                test.assertEqual(10, dataSize.getValue());
                test.assertEqual(DataSizeUnit.Bits, dataSize.getUnits());
            });

            runner.test("zero", (Test test) ->
            {
                final DataSize zero = DataSize.zero;
                test.assertNotNull(zero);
                test.assertEqual(0, zero.getValue());
                test.assertEqual(DataSizeUnit.Bytes, zero.getUnits());
            });

            runner.testGroup("convertTo(DataSizeUnit)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> DataSize.zero.convertTo(null),
                        new PreConditionFailure("destinationUnits cannot be null."));
                });

                final Action3<DataSize,DataSizeUnit,Double> convertToTest = (DataSize dataSize, DataSizeUnit units, Double expectedConvertedValue) ->
                {
                    runner.test("with " + English.andList(dataSize, units), (Test test) ->
                    {
                        final DataSize converted = dataSize.convertTo(units);
                        test.assertNotNull(converted);
                        test.assertEqual(expectedConvertedValue, converted.getValue());
                        test.assertEqual(units, converted.getUnits());
                    });
                };

                convertToTest.run(DataSize.yobibytes(1), DataSizeUnit.Yobibytes,                         1.0);
                convertToTest.run(DataSize.yobibytes(1), DataSizeUnit.Zebibytes,                      1024.0);
                convertToTest.run(DataSize.yobibytes(1), DataSizeUnit.Exbibytes,                   1048576.0);
                convertToTest.run(DataSize.yobibytes(1), DataSizeUnit.Pebibytes,                1073741824.0);
                convertToTest.run(DataSize.yobibytes(1), DataSizeUnit.Tebibytes,             1099511627776.0);
                convertToTest.run(DataSize.yobibytes(1), DataSizeUnit.Gibibytes,          1125899906842624.0);
                convertToTest.run(DataSize.yobibytes(1), DataSizeUnit.Mebibytes,       1152921504606846980.0);
                convertToTest.run(DataSize.yobibytes(1), DataSizeUnit.Kibibytes,    1180591620717411300000.0);
                convertToTest.run(DataSize.yobibytes(1), DataSizeUnit.Yobibits,                          8.0);
                convertToTest.run(DataSize.yobibytes(1), DataSizeUnit.Zebibits,                       8192.0);
                convertToTest.run(DataSize.yobibytes(1), DataSizeUnit.Exbibits,                    8388608.0);
                convertToTest.run(DataSize.yobibytes(1), DataSizeUnit.Pebibits,                 8589934592.0);
                convertToTest.run(DataSize.yobibytes(1), DataSizeUnit.Tebibits,              8796093022208.0);
                convertToTest.run(DataSize.yobibytes(1), DataSizeUnit.Gibibits,           9007199254740992.0);
                convertToTest.run(DataSize.yobibytes(1), DataSizeUnit.Mebibits,        9223372036854776000.0);
                convertToTest.run(DataSize.yobibytes(1), DataSizeUnit.Kibibits,     9444732965739290000000.0);
                convertToTest.run(DataSize.yobibytes(1), DataSizeUnit.Yottabytes,                        1.208925819614629);
                convertToTest.run(DataSize.yobibytes(1), DataSizeUnit.Zettabytes,                     1208.925819614629);
                convertToTest.run(DataSize.yobibytes(1), DataSizeUnit.Exabytes,                    1208925.8196146293);
                convertToTest.run(DataSize.yobibytes(1), DataSizeUnit.Petabytes,                1208925819.6146293);
                convertToTest.run(DataSize.yobibytes(1), DataSizeUnit.Terabytes,             1208925819614.6292);
                convertToTest.run(DataSize.yobibytes(1), DataSizeUnit.Gigabytes,          1208925819614629.2);
                convertToTest.run(DataSize.yobibytes(1), DataSizeUnit.Megabytes,       1208925819614629120.0);
                convertToTest.run(DataSize.yobibytes(1), DataSizeUnit.Kilobytes,    1208925819614629200000.0);
                convertToTest.run(DataSize.yobibytes(1), DataSizeUnit.Bytes,     1208925819614629200000000.0);
                convertToTest.run(DataSize.yobibytes(1), DataSizeUnit.Yottabits,                         9.671406556917033);
                convertToTest.run(DataSize.yobibytes(1), DataSizeUnit.Zettabits,                      9671.406556917033);
                convertToTest.run(DataSize.yobibytes(1), DataSizeUnit.Exabits,                     9671406.556917034);
                convertToTest.run(DataSize.yobibytes(1), DataSizeUnit.Petabits,                 9671406556.917034);
                convertToTest.run(DataSize.yobibytes(1), DataSizeUnit.Terabits,              9671406556917.033);
                convertToTest.run(DataSize.yobibytes(1), DataSizeUnit.Gigabits,           9671406556917034.0);
                convertToTest.run(DataSize.yobibytes(1), DataSizeUnit.Megabits,        9671406556917033000.0);
                convertToTest.run(DataSize.yobibytes(1), DataSizeUnit.Kilobits,     9671406556917034000000.0);
                convertToTest.run(DataSize.yobibytes(1), DataSizeUnit.Bits,      9671406556917033000000000.0);

                convertToTest.run(DataSize.zebibytes(1), DataSizeUnit.Yobibytes,                      0.0009765625);
                convertToTest.run(DataSize.zebibytes(1), DataSizeUnit.Zebibytes,                      1.0);
                convertToTest.run(DataSize.zebibytes(1), DataSizeUnit.Exbibytes,                   1024.0);
                convertToTest.run(DataSize.zebibytes(1), DataSizeUnit.Pebibytes,                1048576.0);
                convertToTest.run(DataSize.zebibytes(1), DataSizeUnit.Tebibytes,             1073741824.0);
                convertToTest.run(DataSize.zebibytes(1), DataSizeUnit.Gibibytes,          1099511627776.0);
                convertToTest.run(DataSize.zebibytes(1), DataSizeUnit.Mebibytes,       1125899906842624.0);
                convertToTest.run(DataSize.zebibytes(1), DataSizeUnit.Kibibytes,    1152921504606846980.0);
                convertToTest.run(DataSize.zebibytes(1), DataSizeUnit.Yobibits,                       0.0078125);
                convertToTest.run(DataSize.zebibytes(1), DataSizeUnit.Zebibits,                       8.0);
                convertToTest.run(DataSize.zebibytes(1), DataSizeUnit.Exbibits,                    8192.0);
                convertToTest.run(DataSize.zebibytes(1), DataSizeUnit.Pebibits,                 8388608.0);
                convertToTest.run(DataSize.zebibytes(1), DataSizeUnit.Tebibits,              8589934592.0);
                convertToTest.run(DataSize.zebibytes(1), DataSizeUnit.Gibibits,           8796093022208.0);
                convertToTest.run(DataSize.zebibytes(1), DataSizeUnit.Mebibits,        9007199254740992.0);
                convertToTest.run(DataSize.zebibytes(1), DataSizeUnit.Kibibits,     9223372036854776000.0);
                convertToTest.run(DataSize.zebibytes(1), DataSizeUnit.Yottabytes,                        0.0011805916207174112);
                convertToTest.run(DataSize.zebibytes(1), DataSizeUnit.Zettabytes,                     1.1805916207174112);
                convertToTest.run(DataSize.zebibytes(1), DataSizeUnit.Exabytes,                    1180.5916207174114);
                convertToTest.run(DataSize.zebibytes(1), DataSizeUnit.Petabytes,                1180591.6207174114);
                convertToTest.run(DataSize.zebibytes(1), DataSizeUnit.Terabytes,             1180591620.7174113);
                convertToTest.run(DataSize.zebibytes(1), DataSizeUnit.Gigabytes,          1180591620717.4114);
                convertToTest.run(DataSize.zebibytes(1), DataSizeUnit.Megabytes,       1180591620717411.2);
                convertToTest.run(DataSize.zebibytes(1), DataSizeUnit.Kilobytes,    1180591620717411330.0);
                convertToTest.run(DataSize.zebibytes(1), DataSizeUnit.Bytes,     1180591620717411300000.0);
                convertToTest.run(DataSize.zebibytes(1), DataSizeUnit.Yottabits,                         0.00944473296573929);
                convertToTest.run(DataSize.zebibytes(1), DataSizeUnit.Zettabits,                      9.44473296573929);
                convertToTest.run(DataSize.zebibytes(1), DataSizeUnit.Exabits,                     9444.732965739291);
                convertToTest.run(DataSize.zebibytes(1), DataSizeUnit.Petabits,                 9444732.965739291);
                convertToTest.run(DataSize.zebibytes(1), DataSizeUnit.Terabits,              9444732965.73929);
                convertToTest.run(DataSize.zebibytes(1), DataSizeUnit.Gigabits,           9444732965739.291);
                convertToTest.run(DataSize.zebibytes(1), DataSizeUnit.Megabits,        9444732965739290.0);
                convertToTest.run(DataSize.zebibytes(1), DataSizeUnit.Kilobits,     9444732965739290000.0);
                convertToTest.run(DataSize.zebibytes(1), DataSizeUnit.Bits,      9444732965739290000000.0);

                convertToTest.run(DataSize.exbibytes(1), DataSizeUnit.Yobibytes,                   0.00000095367431640625);
                convertToTest.run(DataSize.exbibytes(1), DataSizeUnit.Zebibytes,                   0.0009765625);
                convertToTest.run(DataSize.exbibytes(1), DataSizeUnit.Exbibytes,                   1.0);
                convertToTest.run(DataSize.exbibytes(1), DataSizeUnit.Pebibytes,                1024.0);
                convertToTest.run(DataSize.exbibytes(1), DataSizeUnit.Tebibytes,             1048576.0);
                convertToTest.run(DataSize.exbibytes(1), DataSizeUnit.Gibibytes,          1073741824.0);
                convertToTest.run(DataSize.exbibytes(1), DataSizeUnit.Mebibytes,       1099511627776.0);
                convertToTest.run(DataSize.exbibytes(1), DataSizeUnit.Kibibytes,    1125899906842624.0);
                convertToTest.run(DataSize.exbibytes(1), DataSizeUnit.Yobibits,                    0.00000762939453125);
                convertToTest.run(DataSize.exbibytes(1), DataSizeUnit.Zebibits,                    0.0078125);
                convertToTest.run(DataSize.exbibytes(1), DataSizeUnit.Exbibits,                    8.0);
                convertToTest.run(DataSize.exbibytes(1), DataSizeUnit.Pebibits,                 8192.0);
                convertToTest.run(DataSize.exbibytes(1), DataSizeUnit.Tebibits,              8388608.0);
                convertToTest.run(DataSize.exbibytes(1), DataSizeUnit.Gibibits,           8589934592.0);
                convertToTest.run(DataSize.exbibytes(1), DataSizeUnit.Mebibits,        8796093022208.0);
                convertToTest.run(DataSize.exbibytes(1), DataSizeUnit.Kibibits,     9007199254740992.0);
                convertToTest.run(DataSize.exbibytes(1), DataSizeUnit.Yottabytes,                  0.0000011529215046068469);
                convertToTest.run(DataSize.exbibytes(1), DataSizeUnit.Zettabytes,                  0.0011529215046068469);
                convertToTest.run(DataSize.exbibytes(1), DataSizeUnit.Exabytes,                    1.152921504606847);
                convertToTest.run(DataSize.exbibytes(1), DataSizeUnit.Petabytes,                1152.921504606847);
                convertToTest.run(DataSize.exbibytes(1), DataSizeUnit.Terabytes,             1152921.504606847);
                convertToTest.run(DataSize.exbibytes(1), DataSizeUnit.Gigabytes,          1152921504.606847);
                convertToTest.run(DataSize.exbibytes(1), DataSizeUnit.Megabytes,       1152921504606.847);
                convertToTest.run(DataSize.exbibytes(1), DataSizeUnit.Kilobytes,    1152921504606847.0);
                convertToTest.run(DataSize.exbibytes(1), DataSizeUnit.Bytes,     1152921504606846980.0);
                convertToTest.run(DataSize.exbibytes(1), DataSizeUnit.Yottabits,                   0.000009223372036854775);
                convertToTest.run(DataSize.exbibytes(1), DataSizeUnit.Zettabits,                   0.009223372036854775);
                convertToTest.run(DataSize.exbibytes(1), DataSizeUnit.Exabits,                     9.223372036854776);
                convertToTest.run(DataSize.exbibytes(1), DataSizeUnit.Petabits,                 9223.372036854777);
                convertToTest.run(DataSize.exbibytes(1), DataSizeUnit.Terabits,              9223372.036854776);
                convertToTest.run(DataSize.exbibytes(1), DataSizeUnit.Gigabits,           9223372036.854776);
                convertToTest.run(DataSize.exbibytes(1), DataSizeUnit.Megabits,        9223372036854.775);
                convertToTest.run(DataSize.exbibytes(1), DataSizeUnit.Kilobits,     9223372036854776.0);
                convertToTest.run(DataSize.exbibytes(1), DataSizeUnit.Bits,      9223372036854776000.0);

                convertToTest.run(DataSize.pebibytes(1), DataSizeUnit.Yobibytes,                0.0000000009313225746154785);
                convertToTest.run(DataSize.pebibytes(1), DataSizeUnit.Zebibytes,                0.00000095367431640625);
                convertToTest.run(DataSize.pebibytes(1), DataSizeUnit.Exbibytes,                0.0009765625);
                convertToTest.run(DataSize.pebibytes(1), DataSizeUnit.Pebibytes,                1.0);
                convertToTest.run(DataSize.pebibytes(1), DataSizeUnit.Tebibytes,             1024.0);
                convertToTest.run(DataSize.pebibytes(1), DataSizeUnit.Gibibytes,          1048576.0);
                convertToTest.run(DataSize.pebibytes(1), DataSizeUnit.Mebibytes,       1073741824.0);
                convertToTest.run(DataSize.pebibytes(1), DataSizeUnit.Kibibytes,    1099511627776.0);
                convertToTest.run(DataSize.pebibytes(1), DataSizeUnit.Yobibits,                 0.000000007450580596923828);
                convertToTest.run(DataSize.pebibytes(1), DataSizeUnit.Zebibits,                 0.00000762939453125);
                convertToTest.run(DataSize.pebibytes(1), DataSizeUnit.Exbibits,                 0.0078125);
                convertToTest.run(DataSize.pebibytes(1), DataSizeUnit.Pebibits,                 8.0);
                convertToTest.run(DataSize.pebibytes(1), DataSizeUnit.Tebibits,              8192.0);
                convertToTest.run(DataSize.pebibytes(1), DataSizeUnit.Gibibits,           8388608.0);
                convertToTest.run(DataSize.pebibytes(1), DataSizeUnit.Mebibits,        8589934592.0);
                convertToTest.run(DataSize.pebibytes(1), DataSizeUnit.Kibibits,     8796093022208.0);
                convertToTest.run(DataSize.pebibytes(1), DataSizeUnit.Yottabytes,               0.000000001125899906842624);
                convertToTest.run(DataSize.pebibytes(1), DataSizeUnit.Zettabytes,               0.000001125899906842624);
                convertToTest.run(DataSize.pebibytes(1), DataSizeUnit.Exabytes,                 0.001125899906842624);
                convertToTest.run(DataSize.pebibytes(1), DataSizeUnit.Petabytes,                1.125899906842624);
                convertToTest.run(DataSize.pebibytes(1), DataSizeUnit.Terabytes,             1125.899906842624);
                convertToTest.run(DataSize.pebibytes(1), DataSizeUnit.Gigabytes,          1125899.906842624);
                convertToTest.run(DataSize.pebibytes(1), DataSizeUnit.Megabytes,       1125899906.842624);
                convertToTest.run(DataSize.pebibytes(1), DataSizeUnit.Kilobytes,    1125899906842.624);
                convertToTest.run(DataSize.pebibytes(1), DataSizeUnit.Bytes,     1125899906842624.0);
                convertToTest.run(DataSize.pebibytes(1), DataSizeUnit.Yottabits,                0.000000009007199254740991);
                convertToTest.run(DataSize.pebibytes(1), DataSizeUnit.Zettabits,                0.000009007199254740991);
                convertToTest.run(DataSize.pebibytes(1), DataSizeUnit.Exabits,                  0.009007199254740993);
                convertToTest.run(DataSize.pebibytes(1), DataSizeUnit.Petabits,                 9.007199254740993);
                convertToTest.run(DataSize.pebibytes(1), DataSizeUnit.Terabits,              9007.199254740992);
                convertToTest.run(DataSize.pebibytes(1), DataSizeUnit.Gigabits,           9007199.254740993);
                convertToTest.run(DataSize.pebibytes(1), DataSizeUnit.Megabits,        9007199254.740992);
                convertToTest.run(DataSize.pebibytes(1), DataSizeUnit.Kilobits,     9007199254740.992);
                convertToTest.run(DataSize.pebibytes(1), DataSizeUnit.Bits,      9007199254740992.0);

                convertToTest.run(DataSize.tebibytes(1), DataSizeUnit.Yobibytes,             0.0000000000009094947017729282);
                convertToTest.run(DataSize.tebibytes(1), DataSizeUnit.Zebibytes,             0.0000000009313225746154785);
                convertToTest.run(DataSize.tebibytes(1), DataSizeUnit.Exbibytes,             0.00000095367431640625);
                convertToTest.run(DataSize.tebibytes(1), DataSizeUnit.Pebibytes,             0.0009765625);
                convertToTest.run(DataSize.tebibytes(1), DataSizeUnit.Tebibytes,             1.0);
                convertToTest.run(DataSize.tebibytes(1), DataSizeUnit.Gibibytes,          1024.0);
                convertToTest.run(DataSize.tebibytes(1), DataSizeUnit.Mebibytes,       1048576.0);
                convertToTest.run(DataSize.tebibytes(1), DataSizeUnit.Kibibytes,    1073741824.0);
                convertToTest.run(DataSize.tebibytes(1), DataSizeUnit.Yobibits,              0.000000000007275957614183426);
                convertToTest.run(DataSize.tebibytes(1), DataSizeUnit.Zebibits,              0.000000007450580596923828);
                convertToTest.run(DataSize.tebibytes(1), DataSizeUnit.Exbibits,              0.00000762939453125);
                convertToTest.run(DataSize.tebibytes(1), DataSizeUnit.Pebibits,              0.0078125);
                convertToTest.run(DataSize.tebibytes(1), DataSizeUnit.Tebibits,              8.0);
                convertToTest.run(DataSize.tebibytes(1), DataSizeUnit.Gibibits,           8192.0);
                convertToTest.run(DataSize.tebibytes(1), DataSizeUnit.Mebibits,        8388608.0);
                convertToTest.run(DataSize.tebibytes(1), DataSizeUnit.Kibibits,     8589934592.0);
                convertToTest.run(DataSize.tebibytes(1), DataSizeUnit.Yottabytes,            0.000000000001099511627776);
                convertToTest.run(DataSize.tebibytes(1), DataSizeUnit.Zettabytes,            0.000000001099511627776);
                convertToTest.run(DataSize.tebibytes(1), DataSizeUnit.Exabytes,              0.000001099511627776);
                convertToTest.run(DataSize.tebibytes(1), DataSizeUnit.Petabytes,             0.001099511627776);
                convertToTest.run(DataSize.tebibytes(1), DataSizeUnit.Terabytes,             1.099511627776);
                convertToTest.run(DataSize.tebibytes(1), DataSizeUnit.Gigabytes,          1099.511627776);
                convertToTest.run(DataSize.tebibytes(1), DataSizeUnit.Megabytes,       1099511.627776);
                convertToTest.run(DataSize.tebibytes(1), DataSizeUnit.Kilobytes,    1099511627.776);
                convertToTest.run(DataSize.tebibytes(1), DataSizeUnit.Bytes,     1099511627776.0);
                convertToTest.run(DataSize.tebibytes(1), DataSizeUnit.Yottabits,             0.000000000008796093022208);
                convertToTest.run(DataSize.tebibytes(1), DataSizeUnit.Zettabits,             0.000000008796093022208);
                convertToTest.run(DataSize.tebibytes(1), DataSizeUnit.Exabits,               0.000008796093022208);
                convertToTest.run(DataSize.tebibytes(1), DataSizeUnit.Petabits,              0.008796093022208);
                convertToTest.run(DataSize.tebibytes(1), DataSizeUnit.Terabits,              8.796093022208);
                convertToTest.run(DataSize.tebibytes(1), DataSizeUnit.Gigabits,           8796.093022208);
                convertToTest.run(DataSize.tebibytes(1), DataSizeUnit.Megabits,        8796093.022208);
                convertToTest.run(DataSize.tebibytes(1), DataSizeUnit.Kilobits,     8796093022.208);
                convertToTest.run(DataSize.tebibytes(1), DataSizeUnit.Bits,      8796093022208.0);

                convertToTest.run(DataSize.gibibytes(1), DataSizeUnit.Yobibytes,             0.0000000000009094947017729282);
                convertToTest.run(DataSize.gibibytes(1), DataSizeUnit.Zebibytes,             0.0000000009313225746154785);
                convertToTest.run(DataSize.gibibytes(1), DataSizeUnit.Exbibytes,             0.00000095367431640625);
                convertToTest.run(DataSize.gibibytes(1), DataSizeUnit.Pebibytes,             0.0009765625);
                convertToTest.run(DataSize.gibibytes(1), DataSizeUnit.Tebibytes,             1.0);
                convertToTest.run(DataSize.gibibytes(1), DataSizeUnit.Gibibytes,          1024.0);
                convertToTest.run(DataSize.gibibytes(1), DataSizeUnit.Mebibytes,       1048576.0);
                convertToTest.run(DataSize.gibibytes(1), DataSizeUnit.Kibibytes,    1073741824.0);
                convertToTest.run(DataSize.gibibytes(1), DataSizeUnit.Yobibits,              0.000000000007275957614183426);
                convertToTest.run(DataSize.gibibytes(1), DataSizeUnit.Zebibits,              0.000000007450580596923828);
                convertToTest.run(DataSize.gibibytes(1), DataSizeUnit.Exbibits,              0.00000762939453125);
                convertToTest.run(DataSize.gibibytes(1), DataSizeUnit.Pebibits,              0.0078125);
                convertToTest.run(DataSize.gibibytes(1), DataSizeUnit.Tebibits,              8.0);
                convertToTest.run(DataSize.gibibytes(1), DataSizeUnit.Gibibits,           8192.0);
                convertToTest.run(DataSize.gibibytes(1), DataSizeUnit.Mebibits,        8388608.0);
                convertToTest.run(DataSize.gibibytes(1), DataSizeUnit.Kibibits,     8589934592.0);
                convertToTest.run(DataSize.gibibytes(1), DataSizeUnit.Yottabytes,            0.000000000001099511627776);
                convertToTest.run(DataSize.gibibytes(1), DataSizeUnit.Zettabytes,            0.000000001099511627776);
                convertToTest.run(DataSize.gibibytes(1), DataSizeUnit.Exabytes,              0.000001099511627776);
                convertToTest.run(DataSize.gibibytes(1), DataSizeUnit.Petabytes,             0.001099511627776);
                convertToTest.run(DataSize.gibibytes(1), DataSizeUnit.Terabytes,             1.099511627776);
                convertToTest.run(DataSize.gibibytes(1), DataSizeUnit.Gigabytes,          1099.511627776);
                convertToTest.run(DataSize.gibibytes(1), DataSizeUnit.Megabytes,       1099511.627776);
                convertToTest.run(DataSize.gibibytes(1), DataSizeUnit.Kilobytes,    1099511627.776);
                convertToTest.run(DataSize.gibibytes(1), DataSizeUnit.Bytes,     1099511627776.0);
                convertToTest.run(DataSize.gibibytes(1), DataSizeUnit.Yottabits,             0.000000000008796093022208);
                convertToTest.run(DataSize.gibibytes(1), DataSizeUnit.Zettabits,             0.000000008796093022208);
                convertToTest.run(DataSize.gibibytes(1), DataSizeUnit.Exabits,               0.000008796093022208);
                convertToTest.run(DataSize.gibibytes(1), DataSizeUnit.Petabits,              0.008796093022208);
                convertToTest.run(DataSize.gibibytes(1), DataSizeUnit.Terabits,              8.796093022208);
                convertToTest.run(DataSize.gibibytes(1), DataSizeUnit.Gigabits,           8796.093022208);
                convertToTest.run(DataSize.gibibytes(1), DataSizeUnit.Megabits,        8796093.022208);
                convertToTest.run(DataSize.gibibytes(1), DataSizeUnit.Kilobits,     8796093022.208);
                convertToTest.run(DataSize.gibibytes(1), DataSizeUnit.Bits,      8796093022208.0);

                convertToTest.run(DataSize.mebibytes(1), DataSizeUnit.Yobibytes,             0.0000000000009094947017729282);
                convertToTest.run(DataSize.mebibytes(1), DataSizeUnit.Zebibytes,             0.0000000009313225746154785);
                convertToTest.run(DataSize.mebibytes(1), DataSizeUnit.Exbibytes,             0.00000095367431640625);
                convertToTest.run(DataSize.mebibytes(1), DataSizeUnit.Pebibytes,             0.0009765625);
                convertToTest.run(DataSize.mebibytes(1), DataSizeUnit.Tebibytes,             1.0);
                convertToTest.run(DataSize.mebibytes(1), DataSizeUnit.Gibibytes,          1024.0);
                convertToTest.run(DataSize.mebibytes(1), DataSizeUnit.Mebibytes,       1048576.0);
                convertToTest.run(DataSize.mebibytes(1), DataSizeUnit.Kibibytes,    1073741824.0);
                convertToTest.run(DataSize.mebibytes(1), DataSizeUnit.Yobibits,              0.000000000007275957614183426);
                convertToTest.run(DataSize.mebibytes(1), DataSizeUnit.Zebibits,              0.000000007450580596923828);
                convertToTest.run(DataSize.mebibytes(1), DataSizeUnit.Exbibits,              0.00000762939453125);
                convertToTest.run(DataSize.mebibytes(1), DataSizeUnit.Pebibits,              0.0078125);
                convertToTest.run(DataSize.mebibytes(1), DataSizeUnit.Tebibits,              8.0);
                convertToTest.run(DataSize.mebibytes(1), DataSizeUnit.Gibibits,           8192.0);
                convertToTest.run(DataSize.mebibytes(1), DataSizeUnit.Mebibits,        8388608.0);
                convertToTest.run(DataSize.mebibytes(1), DataSizeUnit.Kibibits,     8589934592.0);
                convertToTest.run(DataSize.mebibytes(1), DataSizeUnit.Yottabytes,            0.000000000001099511627776);
                convertToTest.run(DataSize.mebibytes(1), DataSizeUnit.Zettabytes,            0.000000001099511627776);
                convertToTest.run(DataSize.mebibytes(1), DataSizeUnit.Exabytes,              0.000001099511627776);
                convertToTest.run(DataSize.mebibytes(1), DataSizeUnit.Petabytes,             0.001099511627776);
                convertToTest.run(DataSize.mebibytes(1), DataSizeUnit.Terabytes,             1.099511627776);
                convertToTest.run(DataSize.mebibytes(1), DataSizeUnit.Gigabytes,          1099.511627776);
                convertToTest.run(DataSize.mebibytes(1), DataSizeUnit.Megabytes,       1099511.627776);
                convertToTest.run(DataSize.mebibytes(1), DataSizeUnit.Kilobytes,    1099511627.776);
                convertToTest.run(DataSize.mebibytes(1), DataSizeUnit.Bytes,     1099511627776.0);
                convertToTest.run(DataSize.mebibytes(1), DataSizeUnit.Yottabits,             0.000000000008796093022208);
                convertToTest.run(DataSize.mebibytes(1), DataSizeUnit.Zettabits,             0.000000008796093022208);
                convertToTest.run(DataSize.mebibytes(1), DataSizeUnit.Exabits,               0.000008796093022208);
                convertToTest.run(DataSize.mebibytes(1), DataSizeUnit.Petabits,              0.008796093022208);
                convertToTest.run(DataSize.mebibytes(1), DataSizeUnit.Terabits,              8.796093022208);
                convertToTest.run(DataSize.mebibytes(1), DataSizeUnit.Gigabits,           8796.093022208);
                convertToTest.run(DataSize.mebibytes(1), DataSizeUnit.Megabits,        8796093.022208);
                convertToTest.run(DataSize.mebibytes(1), DataSizeUnit.Kilobits,     8796093022.208);
                convertToTest.run(DataSize.mebibytes(1), DataSizeUnit.Bits,      8796093022208.0);

                convertToTest.run(DataSize.kibibytes(1), DataSizeUnit.Yobibytes,             0.0000000000009094947017729282);
                convertToTest.run(DataSize.kibibytes(1), DataSizeUnit.Zebibytes,             0.0000000009313225746154785);
                convertToTest.run(DataSize.kibibytes(1), DataSizeUnit.Exbibytes,             0.00000095367431640625);
                convertToTest.run(DataSize.kibibytes(1), DataSizeUnit.Pebibytes,             0.0009765625);
                convertToTest.run(DataSize.kibibytes(1), DataSizeUnit.Tebibytes,             1.0);
                convertToTest.run(DataSize.kibibytes(1), DataSizeUnit.Gibibytes,          1024.0);
                convertToTest.run(DataSize.kibibytes(1), DataSizeUnit.Mebibytes,       1048576.0);
                convertToTest.run(DataSize.kibibytes(1), DataSizeUnit.Kibibytes,    1073741824.0);
                convertToTest.run(DataSize.kibibytes(1), DataSizeUnit.Yobibits,              0.000000000007275957614183426);
                convertToTest.run(DataSize.kibibytes(1), DataSizeUnit.Zebibits,              0.000000007450580596923828);
                convertToTest.run(DataSize.kibibytes(1), DataSizeUnit.Exbibits,              0.00000762939453125);
                convertToTest.run(DataSize.kibibytes(1), DataSizeUnit.Pebibits,              0.0078125);
                convertToTest.run(DataSize.kibibytes(1), DataSizeUnit.Tebibits,              8.0);
                convertToTest.run(DataSize.kibibytes(1), DataSizeUnit.Gibibits,           8192.0);
                convertToTest.run(DataSize.kibibytes(1), DataSizeUnit.Mebibits,        8388608.0);
                convertToTest.run(DataSize.kibibytes(1), DataSizeUnit.Kibibits,     8589934592.0);
                convertToTest.run(DataSize.kibibytes(1), DataSizeUnit.Yottabytes,            0.000000000001099511627776);
                convertToTest.run(DataSize.kibibytes(1), DataSizeUnit.Zettabytes,            0.000000001099511627776);
                convertToTest.run(DataSize.kibibytes(1), DataSizeUnit.Exabytes,              0.000001099511627776);
                convertToTest.run(DataSize.kibibytes(1), DataSizeUnit.Petabytes,             0.001099511627776);
                convertToTest.run(DataSize.kibibytes(1), DataSizeUnit.Terabytes,             1.099511627776);
                convertToTest.run(DataSize.kibibytes(1), DataSizeUnit.Gigabytes,          1099.511627776);
                convertToTest.run(DataSize.kibibytes(1), DataSizeUnit.Megabytes,       1099511.627776);
                convertToTest.run(DataSize.kibibytes(1), DataSizeUnit.Kilobytes,    1099511627.776);
                convertToTest.run(DataSize.kibibytes(1), DataSizeUnit.Bytes,     1099511627776.0);
                convertToTest.run(DataSize.kibibytes(1), DataSizeUnit.Yottabits,             0.000000000008796093022208);
                convertToTest.run(DataSize.kibibytes(1), DataSizeUnit.Zettabits,             0.000000008796093022208);
                convertToTest.run(DataSize.kibibytes(1), DataSizeUnit.Exabits,               0.000008796093022208);
                convertToTest.run(DataSize.kibibytes(1), DataSizeUnit.Petabits,              0.008796093022208);
                convertToTest.run(DataSize.kibibytes(1), DataSizeUnit.Terabits,              8.796093022208);
                convertToTest.run(DataSize.kibibytes(1), DataSizeUnit.Gigabits,           8796.093022208);
                convertToTest.run(DataSize.kibibytes(1), DataSizeUnit.Megabits,        8796093.022208);
                convertToTest.run(DataSize.kibibytes(1), DataSizeUnit.Kilobits,     8796093022.208);
                convertToTest.run(DataSize.kibibytes(1), DataSizeUnit.Bits,      8796093022208.0);

                convertToTest.run(DataSize.yobibits(1), DataSizeUnit.Yobibytes,             0.0000000000009094947017729282);
                convertToTest.run(DataSize.yobibits(1), DataSizeUnit.Zebibytes,             0.0000000009313225746154785);
                convertToTest.run(DataSize.yobibits(1), DataSizeUnit.Exbibytes,             0.00000095367431640625);
                convertToTest.run(DataSize.yobibits(1), DataSizeUnit.Pebibytes,             0.0009765625);
                convertToTest.run(DataSize.yobibits(1), DataSizeUnit.Tebibytes,             1.0);
                convertToTest.run(DataSize.yobibits(1), DataSizeUnit.Gibibytes,          1024.0);
                convertToTest.run(DataSize.yobibits(1), DataSizeUnit.Mebibytes,       1048576.0);
                convertToTest.run(DataSize.yobibits(1), DataSizeUnit.Kibibytes,    1073741824.0);
                convertToTest.run(DataSize.yobibits(1), DataSizeUnit.Yobibits,              0.000000000007275957614183426);
                convertToTest.run(DataSize.yobibits(1), DataSizeUnit.Zebibits,              0.000000007450580596923828);
                convertToTest.run(DataSize.yobibits(1), DataSizeUnit.Exbibits,              0.00000762939453125);
                convertToTest.run(DataSize.yobibits(1), DataSizeUnit.Pebibits,              0.0078125);
                convertToTest.run(DataSize.yobibits(1), DataSizeUnit.Tebibits,              8.0);
                convertToTest.run(DataSize.yobibits(1), DataSizeUnit.Gibibits,           8192.0);
                convertToTest.run(DataSize.yobibits(1), DataSizeUnit.Mebibits,        8388608.0);
                convertToTest.run(DataSize.yobibits(1), DataSizeUnit.Kibibits,     8589934592.0);
                convertToTest.run(DataSize.yobibits(1), DataSizeUnit.Yottabytes,            0.000000000001099511627776);
                convertToTest.run(DataSize.yobibits(1), DataSizeUnit.Zettabytes,            0.000000001099511627776);
                convertToTest.run(DataSize.yobibits(1), DataSizeUnit.Exabytes,              0.000001099511627776);
                convertToTest.run(DataSize.yobibits(1), DataSizeUnit.Petabytes,             0.001099511627776);
                convertToTest.run(DataSize.yobibits(1), DataSizeUnit.Terabytes,             1.099511627776);
                convertToTest.run(DataSize.yobibits(1), DataSizeUnit.Gigabytes,          1099.511627776);
                convertToTest.run(DataSize.yobibits(1), DataSizeUnit.Megabytes,       1099511.627776);
                convertToTest.run(DataSize.yobibits(1), DataSizeUnit.Kilobytes,    1099511627.776);
                convertToTest.run(DataSize.yobibits(1), DataSizeUnit.Bytes,     1099511627776.0);
                convertToTest.run(DataSize.yobibits(1), DataSizeUnit.Yottabits,             0.000000000008796093022208);
                convertToTest.run(DataSize.yobibits(1), DataSizeUnit.Zettabits,             0.000000008796093022208);
                convertToTest.run(DataSize.yobibits(1), DataSizeUnit.Exabits,               0.000008796093022208);
                convertToTest.run(DataSize.yobibits(1), DataSizeUnit.Petabits,              0.008796093022208);
                convertToTest.run(DataSize.yobibits(1), DataSizeUnit.Terabits,              8.796093022208);
                convertToTest.run(DataSize.yobibits(1), DataSizeUnit.Gigabits,           8796.093022208);
                convertToTest.run(DataSize.yobibits(1), DataSizeUnit.Megabits,        8796093.022208);
                convertToTest.run(DataSize.yobibits(1), DataSizeUnit.Kilobits,     8796093022.208);
                convertToTest.run(DataSize.yobibits(1), DataSizeUnit.Bits,      8796093022208.0);

                convertToTest.run(DataSize.zebibits(1), DataSizeUnit.Yobibytes,             0.0000000000009094947017729282);
                convertToTest.run(DataSize.zebibits(1), DataSizeUnit.Zebibytes,             0.0000000009313225746154785);
                convertToTest.run(DataSize.zebibits(1), DataSizeUnit.Exbibytes,             0.00000095367431640625);
                convertToTest.run(DataSize.zebibits(1), DataSizeUnit.Pebibytes,             0.0009765625);
                convertToTest.run(DataSize.zebibits(1), DataSizeUnit.Tebibytes,             1.0);
                convertToTest.run(DataSize.zebibits(1), DataSizeUnit.Gibibytes,          1024.0);
                convertToTest.run(DataSize.zebibits(1), DataSizeUnit.Mebibytes,       1048576.0);
                convertToTest.run(DataSize.zebibits(1), DataSizeUnit.Kibibytes,    1073741824.0);
                convertToTest.run(DataSize.zebibits(1), DataSizeUnit.Yobibits,              0.000000000007275957614183426);
                convertToTest.run(DataSize.zebibits(1), DataSizeUnit.Zebibits,              0.000000007450580596923828);
                convertToTest.run(DataSize.zebibits(1), DataSizeUnit.Exbibits,              0.00000762939453125);
                convertToTest.run(DataSize.zebibits(1), DataSizeUnit.Pebibits,              0.0078125);
                convertToTest.run(DataSize.zebibits(1), DataSizeUnit.Tebibits,              8.0);
                convertToTest.run(DataSize.zebibits(1), DataSizeUnit.Gibibits,           8192.0);
                convertToTest.run(DataSize.zebibits(1), DataSizeUnit.Mebibits,        8388608.0);
                convertToTest.run(DataSize.zebibits(1), DataSizeUnit.Kibibits,     8589934592.0);
                convertToTest.run(DataSize.zebibits(1), DataSizeUnit.Yottabytes,            0.000000000001099511627776);
                convertToTest.run(DataSize.zebibits(1), DataSizeUnit.Zettabytes,            0.000000001099511627776);
                convertToTest.run(DataSize.zebibits(1), DataSizeUnit.Exabytes,              0.000001099511627776);
                convertToTest.run(DataSize.zebibits(1), DataSizeUnit.Petabytes,             0.001099511627776);
                convertToTest.run(DataSize.zebibits(1), DataSizeUnit.Terabytes,             1.099511627776);
                convertToTest.run(DataSize.zebibits(1), DataSizeUnit.Gigabytes,          1099.511627776);
                convertToTest.run(DataSize.zebibits(1), DataSizeUnit.Megabytes,       1099511.627776);
                convertToTest.run(DataSize.zebibits(1), DataSizeUnit.Kilobytes,    1099511627.776);
                convertToTest.run(DataSize.zebibits(1), DataSizeUnit.Bytes,     1099511627776.0);
                convertToTest.run(DataSize.zebibits(1), DataSizeUnit.Yottabits,             0.000000000008796093022208);
                convertToTest.run(DataSize.zebibits(1), DataSizeUnit.Zettabits,             0.000000008796093022208);
                convertToTest.run(DataSize.zebibits(1), DataSizeUnit.Exabits,               0.000008796093022208);
                convertToTest.run(DataSize.zebibits(1), DataSizeUnit.Petabits,              0.008796093022208);
                convertToTest.run(DataSize.zebibits(1), DataSizeUnit.Terabits,              8.796093022208);
                convertToTest.run(DataSize.zebibits(1), DataSizeUnit.Gigabits,           8796.093022208);
                convertToTest.run(DataSize.zebibits(1), DataSizeUnit.Megabits,        8796093.022208);
                convertToTest.run(DataSize.zebibits(1), DataSizeUnit.Kilobits,     8796093022.208);
                convertToTest.run(DataSize.zebibits(1), DataSizeUnit.Bits,      8796093022208.0);

                convertToTest.run(DataSize.exbibits(1), DataSizeUnit.Yobibytes,               0.0000000000009094947017729282);
                convertToTest.run(DataSize.exbibits(1), DataSizeUnit.Zebibytes,               0.0000000009313225746154785);
                convertToTest.run(DataSize.exbibits(1), DataSizeUnit.Exbibytes,               0.00000095367431640625);
                convertToTest.run(DataSize.exbibits(1), DataSizeUnit.Pebibytes,               0.0009765625);
                convertToTest.run(DataSize.exbibits(1), DataSizeUnit.Tebibytes,               1.0);
                convertToTest.run(DataSize.exbibits(1), DataSizeUnit.Gibibytes,            1024.0);
                convertToTest.run(DataSize.exbibits(1), DataSizeUnit.Mebibytes,         137438953472.0);
                convertToTest.run(DataSize.exbibits(1), DataSizeUnit.Kibibytes,      140737488355328.0);
                convertToTest.run(DataSize.exbibits(1), DataSizeUnit.Yobibits,                     0.00000095367431640625);
                convertToTest.run(DataSize.exbibits(1), DataSizeUnit.Zebibits,                     0.0009765625);
                convertToTest.run(DataSize.exbibits(1), DataSizeUnit.Exbibits,                     1.0);
                convertToTest.run(DataSize.exbibits(1), DataSizeUnit.Pebibits,                  1024.0);
                convertToTest.run(DataSize.exbibits(1), DataSizeUnit.Tebibits,               1048576.0);
                convertToTest.run(DataSize.exbibits(1), DataSizeUnit.Gibibits,            1073741824.0);
                convertToTest.run(DataSize.exbibits(1), DataSizeUnit.Mebibits,         1099511627776.0);
                convertToTest.run(DataSize.exbibits(1), DataSizeUnit.Kibibits,      1125899906842624.0);
                convertToTest.run(DataSize.exbibits(1), DataSizeUnit.Yottabytes,                   0.00000014411518807585586);
                convertToTest.run(DataSize.exbibits(1), DataSizeUnit.Zettabytes,                   0.00014411518807585586);
                convertToTest.run(DataSize.exbibits(1), DataSizeUnit.Exabytes,                     0.14411518807585588);
                convertToTest.run(DataSize.exbibits(1), DataSizeUnit.Petabytes,                  144.11518807585588);
                convertToTest.run(DataSize.exbibits(1), DataSizeUnit.Terabytes,               144115.18807585587);
                convertToTest.run(DataSize.exbibits(1), DataSizeUnit.Gigabytes,            144115188.07585588);
                convertToTest.run(DataSize.exbibits(1), DataSizeUnit.Megabytes,         144115188075.85587);
                convertToTest.run(DataSize.exbibits(1), DataSizeUnit.Kilobytes,      144115188075855.88);
                convertToTest.run(DataSize.exbibits(1), DataSizeUnit.Bytes,       144115188075855872.0);
                convertToTest.run(DataSize.exbibits(1), DataSizeUnit.Yottabits,                    0.0000011529215046068469);
                convertToTest.run(DataSize.exbibits(1), DataSizeUnit.Zettabits,                    0.0011529215046068469);
                convertToTest.run(DataSize.exbibits(1), DataSizeUnit.Exabits,                      1.152921504606847);
                convertToTest.run(DataSize.exbibits(1), DataSizeUnit.Petabits,                  1152.921504606847);
                convertToTest.run(DataSize.exbibits(1), DataSizeUnit.Terabits,               1152921.504606847);
                convertToTest.run(DataSize.exbibits(1), DataSizeUnit.Gigabits,            1152921504.606847);
                convertToTest.run(DataSize.exbibits(1), DataSizeUnit.Megabits,         1152921504606.847);
                convertToTest.run(DataSize.exbibits(1), DataSizeUnit.Kilobits,      1152921504606847.0);
                convertToTest.run(DataSize.exbibits(1), DataSizeUnit.Bits,       1152921504606846980.0);

                convertToTest.run(DataSize.pebibits(1), DataSizeUnit.Yobibytes,               0.00000000011641532182693481);
                convertToTest.run(DataSize.pebibits(1), DataSizeUnit.Zebibytes,               0.00000011920928955078125);
                convertToTest.run(DataSize.pebibits(1), DataSizeUnit.Exbibytes,               0.0001220703125);
                convertToTest.run(DataSize.pebibits(1), DataSizeUnit.Pebibytes,               0.125);
                convertToTest.run(DataSize.pebibits(1), DataSizeUnit.Tebibytes,               128.0);
                convertToTest.run(DataSize.pebibits(1), DataSizeUnit.Gibibytes,            131072.0);
                convertToTest.run(DataSize.pebibits(1), DataSizeUnit.Mebibytes,         134217728.0);
                convertToTest.run(DataSize.pebibits(1), DataSizeUnit.Kibibytes,      137438953472.0);
                convertToTest.run(DataSize.pebibits(1), DataSizeUnit.Yobibits,                  0.0000000009313225746154785);
                convertToTest.run(DataSize.pebibits(1), DataSizeUnit.Zebibits,                  0.00000095367431640625);
                convertToTest.run(DataSize.pebibits(1), DataSizeUnit.Exbibits,                  0.0009765625);
                convertToTest.run(DataSize.pebibits(1), DataSizeUnit.Pebibits,                  1.0);
                convertToTest.run(DataSize.pebibits(1), DataSizeUnit.Tebibits,               1024.0);
                convertToTest.run(DataSize.pebibits(1), DataSizeUnit.Gibibits,            1048576.0);
                convertToTest.run(DataSize.pebibits(1), DataSizeUnit.Mebibits,         1073741824.0);
                convertToTest.run(DataSize.pebibits(1), DataSizeUnit.Kibibits,      1099511627776.0);
                convertToTest.run(DataSize.pebibits(1), DataSizeUnit.Yottabytes,                0.000000000140737488355328);
                convertToTest.run(DataSize.pebibits(1), DataSizeUnit.Zettabytes,                0.000000140737488355328);
                convertToTest.run(DataSize.pebibits(1), DataSizeUnit.Exabytes,                  0.000140737488355328);
                convertToTest.run(DataSize.pebibits(1), DataSizeUnit.Petabytes,                 0.140737488355328);
                convertToTest.run(DataSize.pebibits(1), DataSizeUnit.Terabytes,               140.737488355328);
                convertToTest.run(DataSize.pebibits(1), DataSizeUnit.Gigabytes,            140737.488355328);
                convertToTest.run(DataSize.pebibits(1), DataSizeUnit.Megabytes,         140737488.355328);
                convertToTest.run(DataSize.pebibits(1), DataSizeUnit.Kilobytes,      140737488355.328);
                convertToTest.run(DataSize.pebibits(1), DataSizeUnit.Bytes,       140737488355328.0);
                convertToTest.run(DataSize.pebibits(1), DataSizeUnit.Yottabits,                 0.000000001125899906842624);
                convertToTest.run(DataSize.pebibits(1), DataSizeUnit.Zettabits,                 0.000001125899906842624);
                convertToTest.run(DataSize.pebibits(1), DataSizeUnit.Exabits,                   0.001125899906842624);
                convertToTest.run(DataSize.pebibits(1), DataSizeUnit.Petabits,                  1.125899906842624);
                convertToTest.run(DataSize.pebibits(1), DataSizeUnit.Terabits,               1125.899906842624);
                convertToTest.run(DataSize.pebibits(1), DataSizeUnit.Gigabits,            1125899.906842624);
                convertToTest.run(DataSize.pebibits(1), DataSizeUnit.Megabits,         1125899906.842624);
                convertToTest.run(DataSize.pebibits(1), DataSizeUnit.Kilobits,      1125899906842.624);
                convertToTest.run(DataSize.pebibits(1), DataSizeUnit.Bits,       1125899906842624.0);

                convertToTest.run(DataSize.tebibits(1), DataSizeUnit.Yobibytes,              0.00000000000011368683772161603);
                convertToTest.run(DataSize.tebibits(1), DataSizeUnit.Zebibytes,              0.00000000011641532182693481);
                convertToTest.run(DataSize.tebibits(1), DataSizeUnit.Exbibytes,              0.00000011920928955078125);
                convertToTest.run(DataSize.tebibits(1), DataSizeUnit.Pebibytes,              0.0001220703125);
                convertToTest.run(DataSize.tebibits(1), DataSizeUnit.Tebibytes,              0.125);
                convertToTest.run(DataSize.tebibits(1), DataSizeUnit.Gibibytes,            128.0);
                convertToTest.run(DataSize.tebibits(1), DataSizeUnit.Mebibytes,         131072.0);
                convertToTest.run(DataSize.tebibits(1), DataSizeUnit.Kibibytes,      134217728.0);
                convertToTest.run(DataSize.tebibits(1), DataSizeUnit.Yobibits,               0.0000000000009094947017729282);
                convertToTest.run(DataSize.tebibits(1), DataSizeUnit.Zebibits,               0.0000000009313225746154785);
                convertToTest.run(DataSize.tebibits(1), DataSizeUnit.Exbibits,               0.00000095367431640625);
                convertToTest.run(DataSize.tebibits(1), DataSizeUnit.Pebibits,               0.0009765625);
                convertToTest.run(DataSize.tebibits(1), DataSizeUnit.Tebibits,               1.0);
                convertToTest.run(DataSize.tebibits(1), DataSizeUnit.Gibibits,            1024.0);
                convertToTest.run(DataSize.tebibits(1), DataSizeUnit.Mebibits,         1048576.0);
                convertToTest.run(DataSize.tebibits(1), DataSizeUnit.Kibibits,      1073741824.0);
                convertToTest.run(DataSize.tebibits(1), DataSizeUnit.Yottabytes,             0.000000000000137438953472);
                convertToTest.run(DataSize.tebibits(1), DataSizeUnit.Zettabytes,             0.000000000137438953472);
                convertToTest.run(DataSize.tebibits(1), DataSizeUnit.Exabytes,               0.000000137438953472);
                convertToTest.run(DataSize.tebibits(1), DataSizeUnit.Petabytes,              0.000137438953472);
                convertToTest.run(DataSize.tebibits(1), DataSizeUnit.Terabytes,              0.137438953472);
                convertToTest.run(DataSize.tebibits(1), DataSizeUnit.Gigabytes,            137.438953472);
                convertToTest.run(DataSize.tebibits(1), DataSizeUnit.Megabytes,         137438.953472);
                convertToTest.run(DataSize.tebibits(1), DataSizeUnit.Kilobytes,      137438953.472);
                convertToTest.run(DataSize.tebibits(1), DataSizeUnit.Bytes,       137438953472.0);
                convertToTest.run(DataSize.tebibits(1), DataSizeUnit.Yottabits,              0.000000000001099511627776);
                convertToTest.run(DataSize.tebibits(1), DataSizeUnit.Zettabits,              0.000000001099511627776);
                convertToTest.run(DataSize.tebibits(1), DataSizeUnit.Exabits,                0.000001099511627776);
                convertToTest.run(DataSize.tebibits(1), DataSizeUnit.Petabits,               0.001099511627776);
                convertToTest.run(DataSize.tebibits(1), DataSizeUnit.Terabits,               1.099511627776);
                convertToTest.run(DataSize.tebibits(1), DataSizeUnit.Gigabits,            1099.511627776);
                convertToTest.run(DataSize.tebibits(1), DataSizeUnit.Megabits,         1099511.627776);
                convertToTest.run(DataSize.tebibits(1), DataSizeUnit.Kilobits,      1099511627.776);
                convertToTest.run(DataSize.tebibits(1), DataSizeUnit.Bits,       1099511627776.0);

                convertToTest.run(DataSize.gibibits(1), DataSizeUnit.Yobibytes,           0.00000000000000011102230246251565);
                convertToTest.run(DataSize.gibibits(1), DataSizeUnit.Zebibytes,           0.00000000000011368683772161603);
                convertToTest.run(DataSize.gibibits(1), DataSizeUnit.Exbibytes,           0.00000000011641532182693481);
                convertToTest.run(DataSize.gibibits(1), DataSizeUnit.Pebibytes,           0.00000011920928955078125);
                convertToTest.run(DataSize.gibibits(1), DataSizeUnit.Tebibytes,           0.0001220703125);
                convertToTest.run(DataSize.gibibits(1), DataSizeUnit.Gibibytes,           0.125);
                convertToTest.run(DataSize.gibibits(1), DataSizeUnit.Mebibytes,         128.0);
                convertToTest.run(DataSize.gibibits(1), DataSizeUnit.Kibibytes,      131072.0);
                convertToTest.run(DataSize.gibibits(1), DataSizeUnit.Yobibits,            0.0000000000000008881784197001252);
                convertToTest.run(DataSize.gibibits(1), DataSizeUnit.Zebibits,            0.0000000000009094947017729282);
                convertToTest.run(DataSize.gibibits(1), DataSizeUnit.Exbibits,            0.0000000009313225746154785);
                convertToTest.run(DataSize.gibibits(1), DataSizeUnit.Pebibits,            0.00000095367431640625);
                convertToTest.run(DataSize.gibibits(1), DataSizeUnit.Tebibits,            0.0009765625);
                convertToTest.run(DataSize.gibibits(1), DataSizeUnit.Gibibits,            1.0);
                convertToTest.run(DataSize.gibibits(1), DataSizeUnit.Mebibits,         1024.0);
                convertToTest.run(DataSize.gibibits(1), DataSizeUnit.Kibibits,      1048576.0);
                convertToTest.run(DataSize.gibibits(1), DataSizeUnit.Yottabytes,          0.000000000000000134217728);
                convertToTest.run(DataSize.gibibits(1), DataSizeUnit.Zettabytes,          0.000000000000134217728);
                convertToTest.run(DataSize.gibibits(1), DataSizeUnit.Exabytes,            0.000000000134217728);
                convertToTest.run(DataSize.gibibits(1), DataSizeUnit.Petabytes,           0.000000134217728);
                convertToTest.run(DataSize.gibibits(1), DataSizeUnit.Terabytes,           0.000134217728);
                convertToTest.run(DataSize.gibibits(1), DataSizeUnit.Gigabytes,           0.134217728);
                convertToTest.run(DataSize.gibibits(1), DataSizeUnit.Megabytes,         134.217728);
                convertToTest.run(DataSize.gibibits(1), DataSizeUnit.Kilobytes,      134217.728);
                convertToTest.run(DataSize.gibibits(1), DataSizeUnit.Bytes,       134217728.0);
                convertToTest.run(DataSize.gibibits(1), DataSizeUnit.Yottabits,           0.000000000000001073741824);
                convertToTest.run(DataSize.gibibits(1), DataSizeUnit.Zettabits,           0.000000000001073741824);
                convertToTest.run(DataSize.gibibits(1), DataSizeUnit.Exabits,             0.000000001073741824);
                convertToTest.run(DataSize.gibibits(1), DataSizeUnit.Petabits,            0.000001073741824);
                convertToTest.run(DataSize.gibibits(1), DataSizeUnit.Terabits,            0.001073741824);
                convertToTest.run(DataSize.gibibits(1), DataSizeUnit.Gigabits,            1.073741824);
                convertToTest.run(DataSize.gibibits(1), DataSizeUnit.Megabits,         1073.741824);
                convertToTest.run(DataSize.gibibits(1), DataSizeUnit.Kilobits,      1073741.824);
                convertToTest.run(DataSize.gibibits(1), DataSizeUnit.Bits,       1073741824.0);

                convertToTest.run(DataSize.mebibits(1), DataSizeUnit.Yobibytes,        0.00000000000000000010842021724855044);
                convertToTest.run(DataSize.mebibits(1), DataSizeUnit.Zebibytes,        0.00000000000000011102230246251565);
                convertToTest.run(DataSize.mebibits(1), DataSizeUnit.Exbibytes,        0.00000000000011368683772161603);
                convertToTest.run(DataSize.mebibits(1), DataSizeUnit.Pebibytes,        0.00000000011641532182693481);
                convertToTest.run(DataSize.mebibits(1), DataSizeUnit.Tebibytes,        0.00000011920928955078125);
                convertToTest.run(DataSize.mebibits(1), DataSizeUnit.Gibibytes,        0.0001220703125);
                convertToTest.run(DataSize.mebibits(1), DataSizeUnit.Mebibytes,        0.125);
                convertToTest.run(DataSize.mebibits(1), DataSizeUnit.Kibibytes,      128.0);
                convertToTest.run(DataSize.mebibits(1), DataSizeUnit.Yobibits,         0.0000000000000000008673617379884035);
                convertToTest.run(DataSize.mebibits(1), DataSizeUnit.Zebibits,         0.0000000000000008881784197001252);
                convertToTest.run(DataSize.mebibits(1), DataSizeUnit.Exbibits,         0.0000000000009094947017729282);
                convertToTest.run(DataSize.mebibits(1), DataSizeUnit.Pebibits,         0.0000000009313225746154785);
                convertToTest.run(DataSize.mebibits(1), DataSizeUnit.Tebibits,         0.00000095367431640625);
                convertToTest.run(DataSize.mebibits(1), DataSizeUnit.Gibibits,         0.0009765625);
                convertToTest.run(DataSize.mebibits(1), DataSizeUnit.Mebibits,         1.0);
                convertToTest.run(DataSize.mebibits(1), DataSizeUnit.Kibibits,      1024.0);
                convertToTest.run(DataSize.mebibits(1), DataSizeUnit.Yottabytes,       0.000000000000000000131072);
                convertToTest.run(DataSize.mebibits(1), DataSizeUnit.Zettabytes,       0.000000000000000131072);
                convertToTest.run(DataSize.mebibits(1), DataSizeUnit.Exabytes,         0.000000000000131072);
                convertToTest.run(DataSize.mebibits(1), DataSizeUnit.Petabytes,        0.000000000131072);
                convertToTest.run(DataSize.mebibits(1), DataSizeUnit.Terabytes,        0.000000131072);
                convertToTest.run(DataSize.mebibits(1), DataSizeUnit.Gigabytes,        0.000131072);
                convertToTest.run(DataSize.mebibits(1), DataSizeUnit.Megabytes,        0.131072);
                convertToTest.run(DataSize.mebibits(1), DataSizeUnit.Kilobytes,      131.072);
                convertToTest.run(DataSize.mebibits(1), DataSizeUnit.Bytes,       131072.0);
                convertToTest.run(DataSize.mebibits(1), DataSizeUnit.Yottabits,        0.000000000000000001048576);
                convertToTest.run(DataSize.mebibits(1), DataSizeUnit.Zettabits,        0.000000000000001048576);
                convertToTest.run(DataSize.mebibits(1), DataSizeUnit.Exabits,          0.000000000001048576);
                convertToTest.run(DataSize.mebibits(1), DataSizeUnit.Petabits,         0.000000001048576);
                convertToTest.run(DataSize.mebibits(1), DataSizeUnit.Terabits,         0.000001048576);
                convertToTest.run(DataSize.mebibits(1), DataSizeUnit.Gigabits,         0.001048576);
                convertToTest.run(DataSize.mebibits(1), DataSizeUnit.Megabits,         1.048576);
                convertToTest.run(DataSize.mebibits(1), DataSizeUnit.Kilobits,      1048.576);
                convertToTest.run(DataSize.mebibits(1), DataSizeUnit.Bits,       1048576.0);

                convertToTest.run(DataSize.kibibits(1), DataSizeUnit.Yobibytes,     0.00000000000000000000010587911840678754);
                convertToTest.run(DataSize.kibibits(1), DataSizeUnit.Zebibytes,     0.00000000000000000010842021724855044);
                convertToTest.run(DataSize.kibibits(1), DataSizeUnit.Exbibytes,     0.00000000000000011102230246251565);
                convertToTest.run(DataSize.kibibits(1), DataSizeUnit.Pebibytes,     0.00000000000011368683772161603);
                convertToTest.run(DataSize.kibibits(1), DataSizeUnit.Tebibytes,     0.00000000011641532182693481);
                convertToTest.run(DataSize.kibibits(1), DataSizeUnit.Gibibytes,     0.00000011920928955078125);
                convertToTest.run(DataSize.kibibits(1), DataSizeUnit.Mebibytes,     0.0001220703125);
                convertToTest.run(DataSize.kibibits(1), DataSizeUnit.Kibibytes,     0.125);
                convertToTest.run(DataSize.kibibits(1), DataSizeUnit.Yobibits,      0.0000000000000000000008470329472543003);
                convertToTest.run(DataSize.kibibits(1), DataSizeUnit.Zebibits,      0.0000000000000000008673617379884035);
                convertToTest.run(DataSize.kibibits(1), DataSizeUnit.Exbibits,      0.0000000000000008881784197001252);
                convertToTest.run(DataSize.kibibits(1), DataSizeUnit.Pebibits,      0.0000000000009094947017729282);
                convertToTest.run(DataSize.kibibits(1), DataSizeUnit.Tebibits,      0.0000000009313225746154785);
                convertToTest.run(DataSize.kibibits(1), DataSizeUnit.Gibibits,      0.00000095367431640625);
                convertToTest.run(DataSize.kibibits(1), DataSizeUnit.Mebibits,      0.0009765625);
                convertToTest.run(DataSize.kibibits(1), DataSizeUnit.Kibibits,      1.0);
                convertToTest.run(DataSize.kibibits(1), DataSizeUnit.Yottabytes,    0.000000000000000000000128);
                convertToTest.run(DataSize.kibibits(1), DataSizeUnit.Zettabytes,    0.000000000000000000128);
                convertToTest.run(DataSize.kibibits(1), DataSizeUnit.Exabytes,      0.000000000000000128);
                convertToTest.run(DataSize.kibibits(1), DataSizeUnit.Petabytes,     0.000000000000128);
                convertToTest.run(DataSize.kibibits(1), DataSizeUnit.Terabytes,     0.000000000128);
                convertToTest.run(DataSize.kibibits(1), DataSizeUnit.Gigabytes,     0.000000128);
                convertToTest.run(DataSize.kibibits(1), DataSizeUnit.Megabytes,     0.000128);
                convertToTest.run(DataSize.kibibits(1), DataSizeUnit.Kilobytes,     0.128);
                convertToTest.run(DataSize.kibibits(1), DataSizeUnit.Bytes,       128.0);
                convertToTest.run(DataSize.kibibits(1), DataSizeUnit.Yottabits,     0.000000000000000000001024);
                convertToTest.run(DataSize.kibibits(1), DataSizeUnit.Zettabits,     0.000000000000000001024);
                convertToTest.run(DataSize.kibibits(1), DataSizeUnit.Exabits,       0.000000000000001024);
                convertToTest.run(DataSize.kibibits(1), DataSizeUnit.Petabits,      0.000000000001024);
                convertToTest.run(DataSize.kibibits(1), DataSizeUnit.Terabits,      0.000000001024);
                convertToTest.run(DataSize.kibibits(1), DataSizeUnit.Gigabits,      0.000001024);
                convertToTest.run(DataSize.kibibits(1), DataSizeUnit.Megabits,      0.001024);
                convertToTest.run(DataSize.kibibits(1), DataSizeUnit.Kilobits,      1.024);
                convertToTest.run(DataSize.kibibits(1), DataSizeUnit.Bits,       1024.0);

                convertToTest.run(DataSize.yottabytes(1), DataSizeUnit.Yobibytes,                          0.8271806125530277);
                convertToTest.run(DataSize.yottabytes(1), DataSizeUnit.Zebibytes,                        847.0329472543003);
                convertToTest.run(DataSize.yottabytes(1), DataSizeUnit.Exbibytes,                     867361.7379884035);
                convertToTest.run(DataSize.yottabytes(1), DataSizeUnit.Pebibytes,                  888178419.7001252);
                convertToTest.run(DataSize.yottabytes(1), DataSizeUnit.Tebibytes,               909494701772.9282);
                convertToTest.run(DataSize.yottabytes(1), DataSizeUnit.Gibibytes,            931322574615478.5);
                convertToTest.run(DataSize.yottabytes(1), DataSizeUnit.Mebibytes,         953674316406249980.0);
                convertToTest.run(DataSize.yottabytes(1), DataSizeUnit.Kibibytes,      976562500000000000000.0);
                convertToTest.run(DataSize.yottabytes(1), DataSizeUnit.Yobibits,                           6.617444900424221);
                convertToTest.run(DataSize.yottabytes(1), DataSizeUnit.Zebibits,                        6776.263578034403);
                convertToTest.run(DataSize.yottabytes(1), DataSizeUnit.Exbibits,                     6938893.903907228);
                convertToTest.run(DataSize.yottabytes(1), DataSizeUnit.Pebibits,                  7105427357.601002);
                convertToTest.run(DataSize.yottabytes(1), DataSizeUnit.Tebibits,               7275957614183.426);
                convertToTest.run(DataSize.yottabytes(1), DataSizeUnit.Gibibits,            7450580596923828.0);
                convertToTest.run(DataSize.yottabytes(1), DataSizeUnit.Mebibits,         7629394531249999900.0);
                convertToTest.run(DataSize.yottabytes(1), DataSizeUnit.Kibibits,      7812500000000000000000.0);
                convertToTest.run(DataSize.yottabytes(1), DataSizeUnit.Yottabytes,                         1.0);
                convertToTest.run(DataSize.yottabytes(1), DataSizeUnit.Zettabytes,                      1000.0);
                convertToTest.run(DataSize.yottabytes(1), DataSizeUnit.Exabytes,                     1000000.0);
                convertToTest.run(DataSize.yottabytes(1), DataSizeUnit.Petabytes,                 1000000000.0);
                convertToTest.run(DataSize.yottabytes(1), DataSizeUnit.Terabytes,              1000000000000.0);
                convertToTest.run(DataSize.yottabytes(1), DataSizeUnit.Gigabytes,           1000000000000000.0);
                convertToTest.run(DataSize.yottabytes(1), DataSizeUnit.Megabytes,        1000000000000000000.0);
                convertToTest.run(DataSize.yottabytes(1), DataSizeUnit.Kilobytes,     1000000000000000000000.0);
                convertToTest.run(DataSize.yottabytes(1), DataSizeUnit.Bytes,      1000000000000000000000000.0);
                convertToTest.run(DataSize.yottabytes(1), DataSizeUnit.Yottabits,                          8.0);
                convertToTest.run(DataSize.yottabytes(1), DataSizeUnit.Zettabits,                       8000.0);
                convertToTest.run(DataSize.yottabytes(1), DataSizeUnit.Exabits,                      8000000.0);
                convertToTest.run(DataSize.yottabytes(1), DataSizeUnit.Petabits,                  8000000000.0);
                convertToTest.run(DataSize.yottabytes(1), DataSizeUnit.Terabits,               8000000000000.0);
                convertToTest.run(DataSize.yottabytes(1), DataSizeUnit.Gigabits,            8000000000000000.0);
                convertToTest.run(DataSize.yottabytes(1), DataSizeUnit.Megabits,         8000000000000000000.0);
                convertToTest.run(DataSize.yottabytes(1), DataSizeUnit.Kilobits,      8000000000000000000000.0);
                convertToTest.run(DataSize.yottabytes(1), DataSizeUnit.Bits,       8000000000000000000000000.0);

                convertToTest.run(DataSize.zettabytes(1), DataSizeUnit.Yobibytes,                       0.0008271806125530277);
                convertToTest.run(DataSize.zettabytes(1), DataSizeUnit.Zebibytes,                       0.8470329472543003);
                convertToTest.run(DataSize.zettabytes(1), DataSizeUnit.Exbibytes,                     867.3617379884035);
                convertToTest.run(DataSize.zettabytes(1), DataSizeUnit.Pebibytes,                  888178.4197001252);
                convertToTest.run(DataSize.zettabytes(1), DataSizeUnit.Tebibytes,               909494701.7729282);
                convertToTest.run(DataSize.zettabytes(1), DataSizeUnit.Gibibytes,            931322574615.4785);
                convertToTest.run(DataSize.zettabytes(1), DataSizeUnit.Mebibytes,         953674316406250.0);
                convertToTest.run(DataSize.zettabytes(1), DataSizeUnit.Kibibytes,      976562500000000000.0);
                convertToTest.run(DataSize.zettabytes(1), DataSizeUnit.Yobibits,                        0.006617444900424221);
                convertToTest.run(DataSize.zettabytes(1), DataSizeUnit.Zebibits,                        6.776263578034403);
                convertToTest.run(DataSize.zettabytes(1), DataSizeUnit.Exbibits,                     6938.893903907228);
                convertToTest.run(DataSize.zettabytes(1), DataSizeUnit.Pebibits,                  7105427.357601002);
                convertToTest.run(DataSize.zettabytes(1), DataSizeUnit.Tebibits,               7275957614.183426);
                convertToTest.run(DataSize.zettabytes(1), DataSizeUnit.Gibibits,            7450580596923.828);
                convertToTest.run(DataSize.zettabytes(1), DataSizeUnit.Mebibits,         7629394531250000.0);
                convertToTest.run(DataSize.zettabytes(1), DataSizeUnit.Kibibits,      7812500000000000000.0);
                convertToTest.run(DataSize.zettabytes(1), DataSizeUnit.Yottabytes,                      0.001);
                convertToTest.run(DataSize.zettabytes(1), DataSizeUnit.Zettabytes,                      1.0);
                convertToTest.run(DataSize.zettabytes(1), DataSizeUnit.Exabytes,                     1000.0);
                convertToTest.run(DataSize.zettabytes(1), DataSizeUnit.Petabytes,                 1000000.0);
                convertToTest.run(DataSize.zettabytes(1), DataSizeUnit.Terabytes,              1000000000.0);
                convertToTest.run(DataSize.zettabytes(1), DataSizeUnit.Gigabytes,           1000000000000.0);
                convertToTest.run(DataSize.zettabytes(1), DataSizeUnit.Megabytes,        1000000000000000.0);
                convertToTest.run(DataSize.zettabytes(1), DataSizeUnit.Kilobytes,     1000000000000000000.0);
                convertToTest.run(DataSize.zettabytes(1), DataSizeUnit.Bytes,      1000000000000000000000.0);
                convertToTest.run(DataSize.zettabytes(1), DataSizeUnit.Yottabits,                       0.008);
                convertToTest.run(DataSize.zettabytes(1), DataSizeUnit.Zettabits,                       8.0);
                convertToTest.run(DataSize.zettabytes(1), DataSizeUnit.Exabits,                      8000.0);
                convertToTest.run(DataSize.zettabytes(1), DataSizeUnit.Petabits,                  8000000.0);
                convertToTest.run(DataSize.zettabytes(1), DataSizeUnit.Terabits,               8000000000.0);
                convertToTest.run(DataSize.zettabytes(1), DataSizeUnit.Gigabits,            8000000000000.0);
                convertToTest.run(DataSize.zettabytes(1), DataSizeUnit.Megabits,         8000000000000000.0);
                convertToTest.run(DataSize.zettabytes(1), DataSizeUnit.Kilobits,      8000000000000000000.0);
                convertToTest.run(DataSize.zettabytes(1), DataSizeUnit.Bits,       8000000000000000000000.0);

                convertToTest.run(DataSize.exabytes(1), DataSizeUnit.Yobibytes,                    0.0000008271806125530277);
                convertToTest.run(DataSize.exabytes(1), DataSizeUnit.Zebibytes,                    0.0008470329472543003);
                convertToTest.run(DataSize.exabytes(1), DataSizeUnit.Exbibytes,                    0.8673617379884035);
                convertToTest.run(DataSize.exabytes(1), DataSizeUnit.Pebibytes,                  888.1784197001252);
                convertToTest.run(DataSize.exabytes(1), DataSizeUnit.Tebibytes,               909494.7017729282);
                convertToTest.run(DataSize.exabytes(1), DataSizeUnit.Gibibytes,            931322574.6154785);
                convertToTest.run(DataSize.exabytes(1), DataSizeUnit.Mebibytes,         953674316406.25);
                convertToTest.run(DataSize.exabytes(1), DataSizeUnit.Kibibytes,      976562500000000.0);
                convertToTest.run(DataSize.exabytes(1), DataSizeUnit.Yobibits,                     0.000006617444900424221);
                convertToTest.run(DataSize.exabytes(1), DataSizeUnit.Zebibits,                     0.006776263578034403);
                convertToTest.run(DataSize.exabytes(1), DataSizeUnit.Exbibits,                     6.938893903907228);
                convertToTest.run(DataSize.exabytes(1), DataSizeUnit.Pebibits,                  7105.427357601002);
                convertToTest.run(DataSize.exabytes(1), DataSizeUnit.Tebibits,               7275957.614183426);
                convertToTest.run(DataSize.exabytes(1), DataSizeUnit.Gibibits,            7450580596.923828);
                convertToTest.run(DataSize.exabytes(1), DataSizeUnit.Mebibits,         7629394531250.0);
                convertToTest.run(DataSize.exabytes(1), DataSizeUnit.Kibibits,      7812500000000000.0);
                convertToTest.run(DataSize.exabytes(1), DataSizeUnit.Yottabytes,                   0.000001);
                convertToTest.run(DataSize.exabytes(1), DataSizeUnit.Zettabytes,                   0.001);
                convertToTest.run(DataSize.exabytes(1), DataSizeUnit.Exabytes,                     1.0);
                convertToTest.run(DataSize.exabytes(1), DataSizeUnit.Petabytes,                 1000.0);
                convertToTest.run(DataSize.exabytes(1), DataSizeUnit.Terabytes,              1000000.0);
                convertToTest.run(DataSize.exabytes(1), DataSizeUnit.Gigabytes,           1000000000.0);
                convertToTest.run(DataSize.exabytes(1), DataSizeUnit.Megabytes,        1000000000000.0);
                convertToTest.run(DataSize.exabytes(1), DataSizeUnit.Kilobytes,     1000000000000000.0);
                convertToTest.run(DataSize.exabytes(1), DataSizeUnit.Bytes,      1000000000000000000.0);
                convertToTest.run(DataSize.exabytes(1), DataSizeUnit.Yottabits,                    0.000008);
                convertToTest.run(DataSize.exabytes(1), DataSizeUnit.Zettabits,                    0.008);
                convertToTest.run(DataSize.exabytes(1), DataSizeUnit.Exabits,                      8.0);
                convertToTest.run(DataSize.exabytes(1), DataSizeUnit.Petabits,                  8000.0);
                convertToTest.run(DataSize.exabytes(1), DataSizeUnit.Terabits,               8000000.0);
                convertToTest.run(DataSize.exabytes(1), DataSizeUnit.Gigabits,            8000000000.0);
                convertToTest.run(DataSize.exabytes(1), DataSizeUnit.Megabits,         8000000000000.0);
                convertToTest.run(DataSize.exabytes(1), DataSizeUnit.Kilobits,      8000000000000000.0);
                convertToTest.run(DataSize.exabytes(1), DataSizeUnit.Bits,       8000000000000000000.0);

                convertToTest.run(DataSize.petabytes(1), DataSizeUnit.Yobibytes,                 0.0000000008271806125530277);
                convertToTest.run(DataSize.petabytes(1), DataSizeUnit.Zebibytes,                 0.0000008470329472543003);
                convertToTest.run(DataSize.petabytes(1), DataSizeUnit.Exbibytes,                 0.0008673617379884035);
                convertToTest.run(DataSize.petabytes(1), DataSizeUnit.Pebibytes,                 0.8881784197001252);
                convertToTest.run(DataSize.petabytes(1), DataSizeUnit.Tebibytes,               909.4947017729282);
                convertToTest.run(DataSize.petabytes(1), DataSizeUnit.Gibibytes,            931322.5746154785);
                convertToTest.run(DataSize.petabytes(1), DataSizeUnit.Mebibytes,         953674316.40625);
                convertToTest.run(DataSize.petabytes(1), DataSizeUnit.Kibibytes,      976562500000.0);
                convertToTest.run(DataSize.petabytes(1), DataSizeUnit.Yobibits,                  0.000000006617444900424221);
                convertToTest.run(DataSize.petabytes(1), DataSizeUnit.Zebibits,                  0.000006776263578034403);
                convertToTest.run(DataSize.petabytes(1), DataSizeUnit.Exbibits,                  0.006938893903907228);
                convertToTest.run(DataSize.petabytes(1), DataSizeUnit.Pebibits,                  7.105427357601002);
                convertToTest.run(DataSize.petabytes(1), DataSizeUnit.Tebibits,               7275.957614183426);
                convertToTest.run(DataSize.petabytes(1), DataSizeUnit.Gibibits,            7450580.596923828);
                convertToTest.run(DataSize.petabytes(1), DataSizeUnit.Mebibits,         7629394531.25);
                convertToTest.run(DataSize.petabytes(1), DataSizeUnit.Kibibits,      7812500000000.0);
                convertToTest.run(DataSize.petabytes(1), DataSizeUnit.Yottabytes,                0.000000001);
                convertToTest.run(DataSize.petabytes(1), DataSizeUnit.Zettabytes,                0.000001);
                convertToTest.run(DataSize.petabytes(1), DataSizeUnit.Exabytes,                  0.001);
                convertToTest.run(DataSize.petabytes(1), DataSizeUnit.Petabytes,                 1.0);
                convertToTest.run(DataSize.petabytes(1), DataSizeUnit.Terabytes,              1000.0);
                convertToTest.run(DataSize.petabytes(1), DataSizeUnit.Gigabytes,           1000000.0);
                convertToTest.run(DataSize.petabytes(1), DataSizeUnit.Megabytes,        1000000000.0);
                convertToTest.run(DataSize.petabytes(1), DataSizeUnit.Kilobytes,     1000000000000.0);
                convertToTest.run(DataSize.petabytes(1), DataSizeUnit.Bytes,      1000000000000000.0);
                convertToTest.run(DataSize.petabytes(1), DataSizeUnit.Yottabits,                 0.000000008);
                convertToTest.run(DataSize.petabytes(1), DataSizeUnit.Zettabits,                 0.000008);
                convertToTest.run(DataSize.petabytes(1), DataSizeUnit.Exabits,                   0.008);
                convertToTest.run(DataSize.petabytes(1), DataSizeUnit.Petabits,                  8.0);
                convertToTest.run(DataSize.petabytes(1), DataSizeUnit.Terabits,               8000.0);
                convertToTest.run(DataSize.petabytes(1), DataSizeUnit.Gigabits,            8000000.0);
                convertToTest.run(DataSize.petabytes(1), DataSizeUnit.Megabits,         8000000000.0);
                convertToTest.run(DataSize.petabytes(1), DataSizeUnit.Kilobits,      8000000000000.0);
                convertToTest.run(DataSize.petabytes(1), DataSizeUnit.Bits,       8000000000000000.0);

                convertToTest.run(DataSize.terabytes(1), DataSizeUnit.Yobibytes,              0.0000000000008271806125530277);
                convertToTest.run(DataSize.terabytes(1), DataSizeUnit.Zebibytes,              0.0000000008470329472543003);
                convertToTest.run(DataSize.terabytes(1), DataSizeUnit.Exbibytes,              0.0000008673617379884035);
                convertToTest.run(DataSize.terabytes(1), DataSizeUnit.Pebibytes,              0.0008881784197001252);
                convertToTest.run(DataSize.terabytes(1), DataSizeUnit.Tebibytes,              0.9094947017729282);
                convertToTest.run(DataSize.terabytes(1), DataSizeUnit.Gibibytes,            931.3225746154785);
                convertToTest.run(DataSize.terabytes(1), DataSizeUnit.Mebibytes,         953674.31640625);
                convertToTest.run(DataSize.terabytes(1), DataSizeUnit.Kibibytes,      976562500.0);
                convertToTest.run(DataSize.terabytes(1), DataSizeUnit.Yobibits,               0.000000000006617444900424221);
                convertToTest.run(DataSize.terabytes(1), DataSizeUnit.Zebibits,               0.000000006776263578034403);
                convertToTest.run(DataSize.terabytes(1), DataSizeUnit.Exbibits,               0.000006938893903907228);
                convertToTest.run(DataSize.terabytes(1), DataSizeUnit.Pebibits,               0.007105427357601002);
                convertToTest.run(DataSize.terabytes(1), DataSizeUnit.Tebibits,               7.275957614183426);
                convertToTest.run(DataSize.terabytes(1), DataSizeUnit.Gibibits,            7450.580596923828);
                convertToTest.run(DataSize.terabytes(1), DataSizeUnit.Mebibits,         7629394.53125);
                convertToTest.run(DataSize.terabytes(1), DataSizeUnit.Kibibits,      7812500000.0);
                convertToTest.run(DataSize.terabytes(1), DataSizeUnit.Yottabytes,             0.000000000001);
                convertToTest.run(DataSize.terabytes(1), DataSizeUnit.Zettabytes,             0.000000001);
                convertToTest.run(DataSize.terabytes(1), DataSizeUnit.Exabytes,               0.000001);
                convertToTest.run(DataSize.terabytes(1), DataSizeUnit.Petabytes,              0.001);
                convertToTest.run(DataSize.terabytes(1), DataSizeUnit.Terabytes,              1.0);
                convertToTest.run(DataSize.terabytes(1), DataSizeUnit.Gigabytes,           1000.0);
                convertToTest.run(DataSize.terabytes(1), DataSizeUnit.Megabytes,        1000000.0);
                convertToTest.run(DataSize.terabytes(1), DataSizeUnit.Kilobytes,     1000000000.0);
                convertToTest.run(DataSize.terabytes(1), DataSizeUnit.Bytes,      1000000000000.0);
                convertToTest.run(DataSize.terabytes(1), DataSizeUnit.Yottabits,              0.000000000008);
                convertToTest.run(DataSize.terabytes(1), DataSizeUnit.Zettabits,              0.000000008);
                convertToTest.run(DataSize.terabytes(1), DataSizeUnit.Exabits,                0.000008);
                convertToTest.run(DataSize.terabytes(1), DataSizeUnit.Petabits,               0.008);
                convertToTest.run(DataSize.terabytes(1), DataSizeUnit.Terabits,               8.0);
                convertToTest.run(DataSize.terabytes(1), DataSizeUnit.Gigabits,            8000.0);
                convertToTest.run(DataSize.terabytes(1), DataSizeUnit.Megabits,         8000000.0);
                convertToTest.run(DataSize.terabytes(1), DataSizeUnit.Kilobits,      8000000000.0);
                convertToTest.run(DataSize.terabytes(1), DataSizeUnit.Bits,       8000000000000.0);

                convertToTest.run(DataSize.gigabytes(1), DataSizeUnit.Yobibytes,           0.0000000000000008271806125530277);
                convertToTest.run(DataSize.gigabytes(1), DataSizeUnit.Zebibytes,           0.0000000000008470329472543003);
                convertToTest.run(DataSize.gigabytes(1), DataSizeUnit.Exbibytes,           0.0000000008673617379884035);
                convertToTest.run(DataSize.gigabytes(1), DataSizeUnit.Pebibytes,           0.0000008881784197001252);
                convertToTest.run(DataSize.gigabytes(1), DataSizeUnit.Tebibytes,           0.0009094947017729282);
                convertToTest.run(DataSize.gigabytes(1), DataSizeUnit.Gibibytes,           0.9313225746154785);
                convertToTest.run(DataSize.gigabytes(1), DataSizeUnit.Mebibytes,         953.67431640625);
                convertToTest.run(DataSize.gigabytes(1), DataSizeUnit.Kibibytes,      976562.5);
                convertToTest.run(DataSize.gigabytes(1), DataSizeUnit.Yobibits,            0.0000000000000066174449004242214);
                convertToTest.run(DataSize.gigabytes(1), DataSizeUnit.Zebibits,            0.000000000006776263578034403);
                convertToTest.run(DataSize.gigabytes(1), DataSizeUnit.Exbibits,            0.000000006938893903907228);
                convertToTest.run(DataSize.gigabytes(1), DataSizeUnit.Pebibits,            0.000007105427357601002);
                convertToTest.run(DataSize.gigabytes(1), DataSizeUnit.Tebibits,            0.007275957614183426);
                convertToTest.run(DataSize.gigabytes(1), DataSizeUnit.Gibibits,            7.450580596923828);
                convertToTest.run(DataSize.gigabytes(1), DataSizeUnit.Mebibits,         7629.39453125);
                convertToTest.run(DataSize.gigabytes(1), DataSizeUnit.Kibibits,      7812500.0);
                convertToTest.run(DataSize.gigabytes(1), DataSizeUnit.Yottabytes,          0.000000000000001);
                convertToTest.run(DataSize.gigabytes(1), DataSizeUnit.Zettabytes,          0.000000000001);
                convertToTest.run(DataSize.gigabytes(1), DataSizeUnit.Exabytes,            0.000000001);
                convertToTest.run(DataSize.gigabytes(1), DataSizeUnit.Petabytes,           0.000001);
                convertToTest.run(DataSize.gigabytes(1), DataSizeUnit.Terabytes,           0.001);
                convertToTest.run(DataSize.gigabytes(1), DataSizeUnit.Gigabytes,           1.0);
                convertToTest.run(DataSize.gigabytes(1), DataSizeUnit.Megabytes,        1000.0);
                convertToTest.run(DataSize.gigabytes(1), DataSizeUnit.Kilobytes,     1000000.0);
                convertToTest.run(DataSize.gigabytes(1), DataSizeUnit.Bytes,      1000000000.0);
                convertToTest.run(DataSize.gigabytes(1), DataSizeUnit.Yottabits,           0.000000000000008);
                convertToTest.run(DataSize.gigabytes(1), DataSizeUnit.Zettabits,           0.000000000008);
                convertToTest.run(DataSize.gigabytes(1), DataSizeUnit.Exabits,             0.000000008);
                convertToTest.run(DataSize.gigabytes(1), DataSizeUnit.Petabits,            0.000008);
                convertToTest.run(DataSize.gigabytes(1), DataSizeUnit.Terabits,            0.008);
                convertToTest.run(DataSize.gigabytes(1), DataSizeUnit.Gigabits,            8.0);
                convertToTest.run(DataSize.gigabytes(1), DataSizeUnit.Megabits,         8000.0);
                convertToTest.run(DataSize.gigabytes(1), DataSizeUnit.Kilobits,      8000000.0);
                convertToTest.run(DataSize.gigabytes(1), DataSizeUnit.Bits,       8000000000.0);

                convertToTest.run(DataSize.megabytes(1), DataSizeUnit.Yobibytes,       0.0000000000000000008271806125530277);
                convertToTest.run(DataSize.megabytes(1), DataSizeUnit.Zebibytes,       0.0000000000000008470329472543003);
                convertToTest.run(DataSize.megabytes(1), DataSizeUnit.Exbibytes,       0.0000000000008673617379884035);
                convertToTest.run(DataSize.megabytes(1), DataSizeUnit.Pebibytes,       0.0000000008881784197001252);
                convertToTest.run(DataSize.megabytes(1), DataSizeUnit.Tebibytes,       0.0000009094947017729282);
                convertToTest.run(DataSize.megabytes(1), DataSizeUnit.Gibibytes,       0.0009313225746154785);
                convertToTest.run(DataSize.megabytes(1), DataSizeUnit.Mebibytes,       0.95367431640625);
                convertToTest.run(DataSize.megabytes(1), DataSizeUnit.Kibibytes,     976.5625);
                convertToTest.run(DataSize.megabytes(1), DataSizeUnit.Yobibits,        0.0000000000000000066174449004242214);
                convertToTest.run(DataSize.megabytes(1), DataSizeUnit.Zebibits,        0.000000000000006776263578034403);
                convertToTest.run(DataSize.megabytes(1), DataSizeUnit.Exbibits,        0.000000000006938893903907228);
                convertToTest.run(DataSize.megabytes(1), DataSizeUnit.Pebibits,        0.000000007105427357601002);
                convertToTest.run(DataSize.megabytes(1), DataSizeUnit.Tebibits,        0.000007275957614183426);
                convertToTest.run(DataSize.megabytes(1), DataSizeUnit.Gibibits,        0.007450580596923828);
                convertToTest.run(DataSize.megabytes(1), DataSizeUnit.Mebibits,        7.62939453125);
                convertToTest.run(DataSize.megabytes(1), DataSizeUnit.Kibibits,     7812.5);
                convertToTest.run(DataSize.megabytes(1), DataSizeUnit.Yottabytes,      0.000000000000000001);
                convertToTest.run(DataSize.megabytes(1), DataSizeUnit.Zettabytes,      0.000000000000001);
                convertToTest.run(DataSize.megabytes(1), DataSizeUnit.Exabytes,        0.000000000001);
                convertToTest.run(DataSize.megabytes(1), DataSizeUnit.Petabytes,       0.000000001);
                convertToTest.run(DataSize.megabytes(1), DataSizeUnit.Terabytes,       0.000001);
                convertToTest.run(DataSize.megabytes(1), DataSizeUnit.Gigabytes,       0.001);
                convertToTest.run(DataSize.megabytes(1), DataSizeUnit.Megabytes,       1.0);
                convertToTest.run(DataSize.megabytes(1), DataSizeUnit.Kilobytes,    1000.0);
                convertToTest.run(DataSize.megabytes(1), DataSizeUnit.Bytes,     1000000.0);
                convertToTest.run(DataSize.megabytes(1), DataSizeUnit.Yottabits,       0.000000000000000008);
                convertToTest.run(DataSize.megabytes(1), DataSizeUnit.Zettabits,       0.000000000000008);
                convertToTest.run(DataSize.megabytes(1), DataSizeUnit.Exabits,         0.000000000008);
                convertToTest.run(DataSize.megabytes(1), DataSizeUnit.Petabits,        0.000000008);
                convertToTest.run(DataSize.megabytes(1), DataSizeUnit.Terabits,        0.000008);
                convertToTest.run(DataSize.megabytes(1), DataSizeUnit.Gigabits,        0.008);
                convertToTest.run(DataSize.megabytes(1), DataSizeUnit.Megabits,        8.0);
                convertToTest.run(DataSize.megabytes(1), DataSizeUnit.Kilobits,     8000.0);
                convertToTest.run(DataSize.megabytes(1), DataSizeUnit.Bits,      8000000.0);

                convertToTest.run(DataSize.kilobytes(1), DataSizeUnit.Yobibytes,    0.0000000000000000000008271806125530277);
                convertToTest.run(DataSize.kilobytes(1), DataSizeUnit.Zebibytes,    0.0000000000000000008470329472543003);
                convertToTest.run(DataSize.kilobytes(1), DataSizeUnit.Exbibytes,    0.0000000000000008673617379884035);
                convertToTest.run(DataSize.kilobytes(1), DataSizeUnit.Pebibytes,    0.0000000000008881784197001252);
                convertToTest.run(DataSize.kilobytes(1), DataSizeUnit.Tebibytes,    0.0000000009094947017729282);
                convertToTest.run(DataSize.kilobytes(1), DataSizeUnit.Gibibytes,    0.0000009313225746154785);
                convertToTest.run(DataSize.kilobytes(1), DataSizeUnit.Mebibytes,    0.00095367431640625);
                convertToTest.run(DataSize.kilobytes(1), DataSizeUnit.Kibibytes,    0.9765625);
                convertToTest.run(DataSize.kilobytes(1), DataSizeUnit.Yobibits,     0.0000000000000000000066174449004242214);
                convertToTest.run(DataSize.kilobytes(1), DataSizeUnit.Zebibits,     0.000000000000000006776263578034403);
                convertToTest.run(DataSize.kilobytes(1), DataSizeUnit.Exbibits,     0.000000000000006938893903907228);
                convertToTest.run(DataSize.kilobytes(1), DataSizeUnit.Pebibits,     0.000000000007105427357601002);
                convertToTest.run(DataSize.kilobytes(1), DataSizeUnit.Tebibits,     0.000000007275957614183426);
                convertToTest.run(DataSize.kilobytes(1), DataSizeUnit.Gibibits,     0.000007450580596923828);
                convertToTest.run(DataSize.kilobytes(1), DataSizeUnit.Mebibits,     0.00762939453125);
                convertToTest.run(DataSize.kilobytes(1), DataSizeUnit.Kibibits,     7.8125);
                convertToTest.run(DataSize.kilobytes(1), DataSizeUnit.Yottabytes,   0.000000000000000000001);
                convertToTest.run(DataSize.kilobytes(1), DataSizeUnit.Zettabytes,   0.000000000000000001);
                convertToTest.run(DataSize.kilobytes(1), DataSizeUnit.Exabytes,     0.000000000000001);
                convertToTest.run(DataSize.kilobytes(1), DataSizeUnit.Petabytes,    0.000000000001);
                convertToTest.run(DataSize.kilobytes(1), DataSizeUnit.Terabytes,    0.000000001);
                convertToTest.run(DataSize.kilobytes(1), DataSizeUnit.Gigabytes,    0.000001);
                convertToTest.run(DataSize.kilobytes(1), DataSizeUnit.Megabytes,    0.001);
                convertToTest.run(DataSize.kilobytes(1), DataSizeUnit.Kilobytes,    1.0);
                convertToTest.run(DataSize.kilobytes(1), DataSizeUnit.Bytes,     1000.0);
                convertToTest.run(DataSize.kilobytes(1), DataSizeUnit.Yottabits,    0.000000000000000000008);
                convertToTest.run(DataSize.kilobytes(1), DataSizeUnit.Zettabits,    0.000000000000000008);
                convertToTest.run(DataSize.kilobytes(1), DataSizeUnit.Exabits,      0.000000000000008);
                convertToTest.run(DataSize.kilobytes(1), DataSizeUnit.Petabits,     0.000000000008);
                convertToTest.run(DataSize.kilobytes(1), DataSizeUnit.Terabits,     0.000000008);
                convertToTest.run(DataSize.kilobytes(1), DataSizeUnit.Gigabits,     0.000008);
                convertToTest.run(DataSize.kilobytes(1), DataSizeUnit.Megabits,     0.008);
                convertToTest.run(DataSize.kilobytes(1), DataSizeUnit.Kilobits,     8.0);
                convertToTest.run(DataSize.kilobytes(1), DataSizeUnit.Bits,      8000.0);

                convertToTest.run(DataSize.bytes(1), DataSizeUnit.Yobibytes,  0.0000000000000000000000008271806125530277);
                convertToTest.run(DataSize.bytes(1), DataSizeUnit.Zebibytes,  0.0000000000000000000008470329472543003);
                convertToTest.run(DataSize.bytes(1), DataSizeUnit.Exbibytes,  0.0000000000000000008673617379884035);
                convertToTest.run(DataSize.bytes(1), DataSizeUnit.Pebibytes,  0.0000000000000008881784197001252);
                convertToTest.run(DataSize.bytes(1), DataSizeUnit.Tebibytes,  0.0000000000009094947017729282);
                convertToTest.run(DataSize.bytes(1), DataSizeUnit.Gibibytes,  0.0000000009313225746154785);
                convertToTest.run(DataSize.bytes(1), DataSizeUnit.Mebibytes,  0.00000095367431640625);
                convertToTest.run(DataSize.bytes(1), DataSizeUnit.Kibibytes,  0.0009765625);
                convertToTest.run(DataSize.bytes(1), DataSizeUnit.Yobibits,   0.0000000000000000000000066174449004242214);
                convertToTest.run(DataSize.bytes(1), DataSizeUnit.Zebibits,   0.000000000000000000006776263578034403);
                convertToTest.run(DataSize.bytes(1), DataSizeUnit.Exbibits,   0.000000000000000006938893903907228);
                convertToTest.run(DataSize.bytes(1), DataSizeUnit.Pebibits,   0.000000000000007105427357601002);
                convertToTest.run(DataSize.bytes(1), DataSizeUnit.Tebibits,   0.000000000007275957614183426);
                convertToTest.run(DataSize.bytes(1), DataSizeUnit.Gibibits,   0.000000007450580596923828);
                convertToTest.run(DataSize.bytes(1), DataSizeUnit.Mebibits,   0.00000762939453125);
                convertToTest.run(DataSize.bytes(1), DataSizeUnit.Kibibits,   0.0078125);
                convertToTest.run(DataSize.bytes(1), DataSizeUnit.Yottabytes, 0.000000000000000000000001);
                convertToTest.run(DataSize.bytes(1), DataSizeUnit.Zettabytes, 0.000000000000000000001);
                convertToTest.run(DataSize.bytes(1), DataSizeUnit.Exabytes,   0.000000000000000001);
                convertToTest.run(DataSize.bytes(1), DataSizeUnit.Petabytes,  0.000000000000001);
                convertToTest.run(DataSize.bytes(1), DataSizeUnit.Terabytes,  0.000000000001);
                convertToTest.run(DataSize.bytes(1), DataSizeUnit.Gigabytes,  0.000000001);
                convertToTest.run(DataSize.bytes(1), DataSizeUnit.Megabytes,  0.000001);
                convertToTest.run(DataSize.bytes(1), DataSizeUnit.Kilobytes,  0.001);
                convertToTest.run(DataSize.bytes(1), DataSizeUnit.Bytes,      1.0);
                convertToTest.run(DataSize.bytes(1), DataSizeUnit.Yottabits,  0.000000000000000000000008);
                convertToTest.run(DataSize.bytes(1), DataSizeUnit.Zettabits,  0.000000000000000000008);
                convertToTest.run(DataSize.bytes(1), DataSizeUnit.Exabits,    0.000000000000000008);
                convertToTest.run(DataSize.bytes(1), DataSizeUnit.Petabits,   0.000000000000008);
                convertToTest.run(DataSize.bytes(1), DataSizeUnit.Terabits,   0.000000000008);
                convertToTest.run(DataSize.bytes(1), DataSizeUnit.Gigabits,   0.000000008);
                convertToTest.run(DataSize.bytes(1), DataSizeUnit.Megabits,   0.000008);
                convertToTest.run(DataSize.bytes(1), DataSizeUnit.Kilobits,   0.008);
                convertToTest.run(DataSize.bytes(1), DataSizeUnit.Bits,       8.0);

                convertToTest.run(DataSize.yottabits(1), DataSizeUnit.Yobibytes,                         0.10339757656912846);
                convertToTest.run(DataSize.yottabits(1), DataSizeUnit.Zebibytes,                       105.87911840678754);
                convertToTest.run(DataSize.yottabits(1), DataSizeUnit.Exbibytes,                    108420.21724855044);
                convertToTest.run(DataSize.yottabits(1), DataSizeUnit.Pebibytes,                 111022302.46251565);
                convertToTest.run(DataSize.yottabits(1), DataSizeUnit.Tebibytes,              113686837721.61603);
                convertToTest.run(DataSize.yottabits(1), DataSizeUnit.Gibibytes,           116415321826934.81);
                convertToTest.run(DataSize.yottabits(1), DataSizeUnit.Mebibytes,        119209289550781248.0);
                convertToTest.run(DataSize.yottabits(1), DataSizeUnit.Kibibytes,     122070312500000000000.0);
                convertToTest.run(DataSize.yottabits(1), DataSizeUnit.Yobibits,                          0.8271806125530277);
                convertToTest.run(DataSize.yottabits(1), DataSizeUnit.Zebibits,                        847.0329472543003);
                convertToTest.run(DataSize.yottabits(1), DataSizeUnit.Exbibits,                     867361.7379884035);
                convertToTest.run(DataSize.yottabits(1), DataSizeUnit.Pebibits,                  888178419.7001252);
                convertToTest.run(DataSize.yottabits(1), DataSizeUnit.Tebibits,               909494701772.9282);
                convertToTest.run(DataSize.yottabits(1), DataSizeUnit.Gibibits,            931322574615478.5);
                convertToTest.run(DataSize.yottabits(1), DataSizeUnit.Mebibits,         953674316406249980.0);
                convertToTest.run(DataSize.yottabits(1), DataSizeUnit.Kibibits,      976562500000000000000.0);
                convertToTest.run(DataSize.yottabits(1), DataSizeUnit.Yottabytes,                        0.125);
                convertToTest.run(DataSize.yottabits(1), DataSizeUnit.Zettabytes,                      125.0);
                convertToTest.run(DataSize.yottabits(1), DataSizeUnit.Exabytes,                     125000.0);
                convertToTest.run(DataSize.yottabits(1), DataSizeUnit.Petabytes,                 125000000.0);
                convertToTest.run(DataSize.yottabits(1), DataSizeUnit.Terabytes,              125000000000.0);
                convertToTest.run(DataSize.yottabits(1), DataSizeUnit.Gigabytes,           125000000000000.0);
                convertToTest.run(DataSize.yottabits(1), DataSizeUnit.Megabytes,        125000000000000000.0);
                convertToTest.run(DataSize.yottabits(1), DataSizeUnit.Kilobytes,     125000000000000000000.0);
                convertToTest.run(DataSize.yottabits(1), DataSizeUnit.Bytes,      125000000000000000000000.0);
                convertToTest.run(DataSize.yottabits(1), DataSizeUnit.Yottabits,                         1.0);
                convertToTest.run(DataSize.yottabits(1), DataSizeUnit.Zettabits,                      1000.0);
                convertToTest.run(DataSize.yottabits(1), DataSizeUnit.Exabits,                     1000000.0);
                convertToTest.run(DataSize.yottabits(1), DataSizeUnit.Petabits,                 1000000000.0);
                convertToTest.run(DataSize.yottabits(1), DataSizeUnit.Terabits,              1000000000000.0);
                convertToTest.run(DataSize.yottabits(1), DataSizeUnit.Gigabits,           1000000000000000.0);
                convertToTest.run(DataSize.yottabits(1), DataSizeUnit.Megabits,        1000000000000000000.0);
                convertToTest.run(DataSize.yottabits(1), DataSizeUnit.Kilobits,     1000000000000000000000.0);
                convertToTest.run(DataSize.yottabits(1), DataSizeUnit.Bits,      1000000000000000000000000.0);

                convertToTest.run(DataSize.zettabits(1), DataSizeUnit.Yobibytes,                      0.00010339757656912846);
                convertToTest.run(DataSize.zettabits(1), DataSizeUnit.Zebibytes,                      0.10587911840678754);
                convertToTest.run(DataSize.zettabits(1), DataSizeUnit.Exbibytes,                    108.42021724855044);
                convertToTest.run(DataSize.zettabits(1), DataSizeUnit.Pebibytes,                 111022.30246251565);
                convertToTest.run(DataSize.zettabits(1), DataSizeUnit.Tebibytes,              113686837.72161603);
                convertToTest.run(DataSize.zettabits(1), DataSizeUnit.Gibibytes,           116415321826.93481);
                convertToTest.run(DataSize.zettabits(1), DataSizeUnit.Mebibytes,        119209289550781.25);
                convertToTest.run(DataSize.zettabits(1), DataSizeUnit.Kibibytes,     122070312500000000.0);
                convertToTest.run(DataSize.zettabits(1), DataSizeUnit.Yobibits,                       0.0008271806125530277);
                convertToTest.run(DataSize.zettabits(1), DataSizeUnit.Zebibits,                       0.8470329472543003);
                convertToTest.run(DataSize.zettabits(1), DataSizeUnit.Exbibits,                     867.3617379884035);
                convertToTest.run(DataSize.zettabits(1), DataSizeUnit.Pebibits,                  888178.4197001252);
                convertToTest.run(DataSize.zettabits(1), DataSizeUnit.Tebibits,               909494701.7729282);
                convertToTest.run(DataSize.zettabits(1), DataSizeUnit.Gibibits,            931322574615.4785);
                convertToTest.run(DataSize.zettabits(1), DataSizeUnit.Mebibits,         953674316406250.0);
                convertToTest.run(DataSize.zettabits(1), DataSizeUnit.Kibibits,      976562500000000000.0);
                convertToTest.run(DataSize.zettabits(1), DataSizeUnit.Yottabytes,                     0.000125);
                convertToTest.run(DataSize.zettabits(1), DataSizeUnit.Zettabytes,                     0.125);
                convertToTest.run(DataSize.zettabits(1), DataSizeUnit.Exabytes,                     125.0);
                convertToTest.run(DataSize.zettabits(1), DataSizeUnit.Petabytes,                 125000.0);
                convertToTest.run(DataSize.zettabits(1), DataSizeUnit.Terabytes,              125000000.0);
                convertToTest.run(DataSize.zettabits(1), DataSizeUnit.Gigabytes,           125000000000.0);
                convertToTest.run(DataSize.zettabits(1), DataSizeUnit.Megabytes,        125000000000000.0);
                convertToTest.run(DataSize.zettabits(1), DataSizeUnit.Kilobytes,     125000000000000000.0);
                convertToTest.run(DataSize.zettabits(1), DataSizeUnit.Bytes,      125000000000000000000.0);
                convertToTest.run(DataSize.zettabits(1), DataSizeUnit.Yottabits,                      0.001);
                convertToTest.run(DataSize.zettabits(1), DataSizeUnit.Zettabits,                      1.0);
                convertToTest.run(DataSize.zettabits(1), DataSizeUnit.Exabits,                     1000.0);
                convertToTest.run(DataSize.zettabits(1), DataSizeUnit.Petabits,                 1000000.0);
                convertToTest.run(DataSize.zettabits(1), DataSizeUnit.Terabits,              1000000000.0);
                convertToTest.run(DataSize.zettabits(1), DataSizeUnit.Gigabits,           1000000000000.0);
                convertToTest.run(DataSize.zettabits(1), DataSizeUnit.Megabits,        1000000000000000.0);
                convertToTest.run(DataSize.zettabits(1), DataSizeUnit.Kilobits,     1000000000000000000.0);
                convertToTest.run(DataSize.zettabits(1), DataSizeUnit.Bits,      1000000000000000000000.0);

                convertToTest.run(DataSize.exabits(1), DataSizeUnit.Yobibytes,                  0.00000010339757656912846);
                convertToTest.run(DataSize.exabits(1), DataSizeUnit.Zebibytes,                  0.00010587911840678754);
                convertToTest.run(DataSize.exabits(1), DataSizeUnit.Exbibytes,                  0.10842021724855044);
                convertToTest.run(DataSize.exabits(1), DataSizeUnit.Pebibytes,                111.02230246251565);
                convertToTest.run(DataSize.exabits(1), DataSizeUnit.Tebibytes,             113686.83772161603);
                convertToTest.run(DataSize.exabits(1), DataSizeUnit.Gibibytes,          116415321.82693481);
                convertToTest.run(DataSize.exabits(1), DataSizeUnit.Mebibytes,       119209289550.78125);
                convertToTest.run(DataSize.exabits(1), DataSizeUnit.Kibibytes,    122070312500000.0);
                convertToTest.run(DataSize.exabits(1), DataSizeUnit.Yobibits,                    0.0000008271806125530277);
                convertToTest.run(DataSize.exabits(1), DataSizeUnit.Zebibits,                    0.0008470329472543003);
                convertToTest.run(DataSize.exabits(1), DataSizeUnit.Exbibits,                    0.8673617379884035);
                convertToTest.run(DataSize.exabits(1), DataSizeUnit.Pebibits,                  888.1784197001252);
                convertToTest.run(DataSize.exabits(1), DataSizeUnit.Tebibits,               909494.7017729282);
                convertToTest.run(DataSize.exabits(1), DataSizeUnit.Gibibits,            931322574.6154785);
                convertToTest.run(DataSize.exabits(1), DataSizeUnit.Mebibits,         953674316406.25);
                convertToTest.run(DataSize.exabits(1), DataSizeUnit.Kibibits,      976562500000000.0);
                convertToTest.run(DataSize.exabits(1), DataSizeUnit.Yottabytes,                  0.000000125);
                convertToTest.run(DataSize.exabits(1), DataSizeUnit.Zettabytes,                  0.000125);
                convertToTest.run(DataSize.exabits(1), DataSizeUnit.Exabytes,                    0.125);
                convertToTest.run(DataSize.exabits(1), DataSizeUnit.Petabytes,                 125.0);
                convertToTest.run(DataSize.exabits(1), DataSizeUnit.Terabytes,              125000.0);
                convertToTest.run(DataSize.exabits(1), DataSizeUnit.Gigabytes,           125000000.0);
                convertToTest.run(DataSize.exabits(1), DataSizeUnit.Megabytes,        125000000000.0);
                convertToTest.run(DataSize.exabits(1), DataSizeUnit.Kilobytes,     125000000000000.0);
                convertToTest.run(DataSize.exabits(1), DataSizeUnit.Bytes,      125000000000000000.0);
                convertToTest.run(DataSize.exabits(1), DataSizeUnit.Yottabits,                   0.000001);
                convertToTest.run(DataSize.exabits(1), DataSizeUnit.Zettabits,                   0.001);
                convertToTest.run(DataSize.exabits(1), DataSizeUnit.Exabits,                     1.0);
                convertToTest.run(DataSize.exabits(1), DataSizeUnit.Petabits,                 1000.0);
                convertToTest.run(DataSize.exabits(1), DataSizeUnit.Terabits,              1000000.0);
                convertToTest.run(DataSize.exabits(1), DataSizeUnit.Gigabits,           1000000000.0);
                convertToTest.run(DataSize.exabits(1), DataSizeUnit.Megabits,        1000000000000.0);
                convertToTest.run(DataSize.exabits(1), DataSizeUnit.Kilobits,     1000000000000000.0);
                convertToTest.run(DataSize.exabits(1), DataSizeUnit.Bits,      1000000000000000000.0);

                convertToTest.run(DataSize.petabits(1), DataSizeUnit.Yobibytes,                0.00000000010339757656912846);
                convertToTest.run(DataSize.petabits(1), DataSizeUnit.Zebibytes,                0.00000010587911840678754);
                convertToTest.run(DataSize.petabits(1), DataSizeUnit.Exbibytes,                0.00010842021724855044);
                convertToTest.run(DataSize.petabits(1), DataSizeUnit.Pebibytes,                0.11102230246251565);
                convertToTest.run(DataSize.petabits(1), DataSizeUnit.Tebibytes,              113.68683772161603);
                convertToTest.run(DataSize.petabits(1), DataSizeUnit.Gibibytes,           116415.32182693481);
                convertToTest.run(DataSize.petabits(1), DataSizeUnit.Mebibytes,        119209289.55078125);
                convertToTest.run(DataSize.petabits(1), DataSizeUnit.Kibibytes,     122070312500.0);
                convertToTest.run(DataSize.petabits(1), DataSizeUnit.Yobibits,                 0.0000000008271806125530277);
                convertToTest.run(DataSize.petabits(1), DataSizeUnit.Zebibits,                 0.0000008470329472543003);
                convertToTest.run(DataSize.petabits(1), DataSizeUnit.Exbibits,                 0.0008673617379884035);
                convertToTest.run(DataSize.petabits(1), DataSizeUnit.Pebibits,                 0.8881784197001252);
                convertToTest.run(DataSize.petabits(1), DataSizeUnit.Tebibits,               909.4947017729282);
                convertToTest.run(DataSize.petabits(1), DataSizeUnit.Gibibits,            931322.5746154785);
                convertToTest.run(DataSize.petabits(1), DataSizeUnit.Mebibits,         953674316.40625);
                convertToTest.run(DataSize.petabits(1), DataSizeUnit.Kibibits,      976562500000.0);
                convertToTest.run(DataSize.petabits(1), DataSizeUnit.Yottabytes,               0.000000000125);
                convertToTest.run(DataSize.petabits(1), DataSizeUnit.Zettabytes,               0.000000125);
                convertToTest.run(DataSize.petabits(1), DataSizeUnit.Exabytes,                 0.000125);
                convertToTest.run(DataSize.petabits(1), DataSizeUnit.Petabytes,                0.125);
                convertToTest.run(DataSize.petabits(1), DataSizeUnit.Terabytes,              125.0);
                convertToTest.run(DataSize.petabits(1), DataSizeUnit.Gigabytes,           125000.0);
                convertToTest.run(DataSize.petabits(1), DataSizeUnit.Megabytes,        125000000.0);
                convertToTest.run(DataSize.petabits(1), DataSizeUnit.Kilobytes,     125000000000.0);
                convertToTest.run(DataSize.petabits(1), DataSizeUnit.Bytes,      125000000000000.0);
                convertToTest.run(DataSize.petabits(1), DataSizeUnit.Yottabits,                0.000000001);
                convertToTest.run(DataSize.petabits(1), DataSizeUnit.Zettabits,                0.000001);
                convertToTest.run(DataSize.petabits(1), DataSizeUnit.Exabits,                  0.001);
                convertToTest.run(DataSize.petabits(1), DataSizeUnit.Petabits,                 1.0);
                convertToTest.run(DataSize.petabits(1), DataSizeUnit.Terabits,              1000.0);
                convertToTest.run(DataSize.petabits(1), DataSizeUnit.Gigabits,           1000000.0);
                convertToTest.run(DataSize.petabits(1), DataSizeUnit.Megabits,        1000000000.0);
                convertToTest.run(DataSize.petabits(1), DataSizeUnit.Kilobits,     1000000000000.0);
                convertToTest.run(DataSize.petabits(1), DataSizeUnit.Bits,      1000000000000000.0);

                convertToTest.run(DataSize.terabits(1), DataSizeUnit.Yobibytes,              0.00000000000010339757656912846);
                convertToTest.run(DataSize.terabits(1), DataSizeUnit.Zebibytes,              0.00000000010587911840678754);
                convertToTest.run(DataSize.terabits(1), DataSizeUnit.Exbibytes,              0.00000010842021724855044);
                convertToTest.run(DataSize.terabits(1), DataSizeUnit.Pebibytes,              0.00011102230246251565);
                convertToTest.run(DataSize.terabits(1), DataSizeUnit.Tebibytes,              0.11368683772161603);
                convertToTest.run(DataSize.terabits(1), DataSizeUnit.Gibibytes,            116.41532182693481);
                convertToTest.run(DataSize.terabits(1), DataSizeUnit.Mebibytes,         119209.28955078125);
                convertToTest.run(DataSize.terabits(1), DataSizeUnit.Kibibytes,      122070312.5);
                convertToTest.run(DataSize.terabits(1), DataSizeUnit.Yobibits,               0.0000000000008271806125530277);
                convertToTest.run(DataSize.terabits(1), DataSizeUnit.Zebibits,               0.0000000008470329472543003);
                convertToTest.run(DataSize.terabits(1), DataSizeUnit.Exbibits,               0.0000008673617379884035);
                convertToTest.run(DataSize.terabits(1), DataSizeUnit.Pebibits,               0.0008881784197001252);
                convertToTest.run(DataSize.terabits(1), DataSizeUnit.Tebibits,               0.9094947017729282);
                convertToTest.run(DataSize.terabits(1), DataSizeUnit.Gibibits,             931.3225746154785);
                convertToTest.run(DataSize.terabits(1), DataSizeUnit.Mebibits,          953674.31640625);
                convertToTest.run(DataSize.terabits(1), DataSizeUnit.Kibibits,       976562500.0);
                convertToTest.run(DataSize.terabits(1), DataSizeUnit.Yottabytes,             0.000000000000125);
                convertToTest.run(DataSize.terabits(1), DataSizeUnit.Zettabytes,             0.000000000125);
                convertToTest.run(DataSize.terabits(1), DataSizeUnit.Exabytes,               0.000000125);
                convertToTest.run(DataSize.terabits(1), DataSizeUnit.Petabytes,              0.000125);
                convertToTest.run(DataSize.terabits(1), DataSizeUnit.Terabytes,              0.125);
                convertToTest.run(DataSize.terabits(1), DataSizeUnit.Gigabytes,           125.0);
                convertToTest.run(DataSize.terabits(1), DataSizeUnit.Megabytes,        125000.0);
                convertToTest.run(DataSize.terabits(1), DataSizeUnit.Kilobytes,     125000000.0);
                convertToTest.run(DataSize.terabits(1), DataSizeUnit.Bytes,      125000000000.0);
                convertToTest.run(DataSize.terabits(1), DataSizeUnit.Yottabits,             0.000000000001);
                convertToTest.run(DataSize.terabits(1), DataSizeUnit.Zettabits,             0.000000001);
                convertToTest.run(DataSize.terabits(1), DataSizeUnit.Exabits,               0.000001);
                convertToTest.run(DataSize.terabits(1), DataSizeUnit.Petabits,              0.001);
                convertToTest.run(DataSize.terabits(1), DataSizeUnit.Terabits,              1.0);
                convertToTest.run(DataSize.terabits(1), DataSizeUnit.Gigabits,           1000.0);
                convertToTest.run(DataSize.terabits(1), DataSizeUnit.Megabits,        1000000.0);
                convertToTest.run(DataSize.terabits(1), DataSizeUnit.Kilobits,     1000000000.0);
                convertToTest.run(DataSize.terabits(1), DataSizeUnit.Bits,      1000000000000.0);

                convertToTest.run(DataSize.gigabits(1), DataSizeUnit.Yobibytes,          0.00000000000000010339757656912846);
                convertToTest.run(DataSize.gigabits(1), DataSizeUnit.Zebibytes,          0.00000000000010587911840678754);
                convertToTest.run(DataSize.gigabits(1), DataSizeUnit.Exbibytes,          0.00000000010842021724855044);
                convertToTest.run(DataSize.gigabits(1), DataSizeUnit.Pebibytes,          0.00000011102230246251565);
                convertToTest.run(DataSize.gigabits(1), DataSizeUnit.Tebibytes,          0.00011368683772161603);
                convertToTest.run(DataSize.gigabits(1), DataSizeUnit.Gibibytes,          0.11641532182693481);
                convertToTest.run(DataSize.gigabits(1), DataSizeUnit.Mebibytes,        119.20928955078125);
                convertToTest.run(DataSize.gigabits(1), DataSizeUnit.Kibibytes,     122070.3125);
                convertToTest.run(DataSize.gigabits(1), DataSizeUnit.Yobibits,           0.0000000000000008271806125530277);
                convertToTest.run(DataSize.gigabits(1), DataSizeUnit.Zebibits,           0.0000000000008470329472543003);
                convertToTest.run(DataSize.gigabits(1), DataSizeUnit.Exbibits,           0.0000000008673617379884035);
                convertToTest.run(DataSize.gigabits(1), DataSizeUnit.Pebibits,           0.0000008881784197001252);
                convertToTest.run(DataSize.gigabits(1), DataSizeUnit.Tebibits,           0.0009094947017729282);
                convertToTest.run(DataSize.gigabits(1), DataSizeUnit.Gibibits,           0.9313225746154785);
                convertToTest.run(DataSize.gigabits(1), DataSizeUnit.Mebibits,         953.67431640625);
                convertToTest.run(DataSize.gigabits(1), DataSizeUnit.Kibibits,      976562.5);
                convertToTest.run(DataSize.gigabits(1), DataSizeUnit.Yottabytes,         0.000000000000000125);
                convertToTest.run(DataSize.gigabits(1), DataSizeUnit.Zettabytes,         0.000000000000125);
                convertToTest.run(DataSize.gigabits(1), DataSizeUnit.Exabytes,           0.000000000125);
                convertToTest.run(DataSize.gigabits(1), DataSizeUnit.Petabytes,          0.000000125);
                convertToTest.run(DataSize.gigabits(1), DataSizeUnit.Terabytes,          0.000125);
                convertToTest.run(DataSize.gigabits(1), DataSizeUnit.Gigabytes,          0.125);
                convertToTest.run(DataSize.gigabits(1), DataSizeUnit.Megabytes,        125.0);
                convertToTest.run(DataSize.gigabits(1), DataSizeUnit.Kilobytes,     125000.0);
                convertToTest.run(DataSize.gigabits(1), DataSizeUnit.Bytes,      125000000.0);
                convertToTest.run(DataSize.gigabits(1), DataSizeUnit.Yottabits,          0.000000000000001);
                convertToTest.run(DataSize.gigabits(1), DataSizeUnit.Zettabits,          0.000000000001);
                convertToTest.run(DataSize.gigabits(1), DataSizeUnit.Exabits,            0.000000001);
                convertToTest.run(DataSize.gigabits(1), DataSizeUnit.Petabits,           0.000001);
                convertToTest.run(DataSize.gigabits(1), DataSizeUnit.Terabits,           0.001);
                convertToTest.run(DataSize.gigabits(1), DataSizeUnit.Gigabits,           1.0);
                convertToTest.run(DataSize.gigabits(1), DataSizeUnit.Megabits,        1000.0);
                convertToTest.run(DataSize.gigabits(1), DataSizeUnit.Kilobits,     1000000.0);
                convertToTest.run(DataSize.gigabits(1), DataSizeUnit.Bits,      1000000000.0);

                convertToTest.run(DataSize.megabits(1), DataSizeUnit.Yobibytes,      0.00000000000000000010339757656912846);
                convertToTest.run(DataSize.megabits(1), DataSizeUnit.Zebibytes,      0.00000000000000010587911840678754);
                convertToTest.run(DataSize.megabits(1), DataSizeUnit.Exbibytes,      0.00000000000010842021724855044);
                convertToTest.run(DataSize.megabits(1), DataSizeUnit.Pebibytes,      0.00000000011102230246251565);
                convertToTest.run(DataSize.megabits(1), DataSizeUnit.Tebibytes,      0.00000011368683772161603);
                convertToTest.run(DataSize.megabits(1), DataSizeUnit.Gibibytes,      0.00011641532182693481);
                convertToTest.run(DataSize.megabits(1), DataSizeUnit.Mebibytes,      0.11920928955078125);
                convertToTest.run(DataSize.megabits(1), DataSizeUnit.Kibibytes,    122.0703125);
                convertToTest.run(DataSize.megabits(1), DataSizeUnit.Yobibits,       0.0000000000000000008271806125530277);
                convertToTest.run(DataSize.megabits(1), DataSizeUnit.Zebibits,       0.0000000000000008470329472543003);
                convertToTest.run(DataSize.megabits(1), DataSizeUnit.Exbibits,       0.0000000000008673617379884035);
                convertToTest.run(DataSize.megabits(1), DataSizeUnit.Pebibits,       0.0000000008881784197001252);
                convertToTest.run(DataSize.megabits(1), DataSizeUnit.Tebibits,       0.0000009094947017729282);
                convertToTest.run(DataSize.megabits(1), DataSizeUnit.Gibibits,       0.0009313225746154785);
                convertToTest.run(DataSize.megabits(1), DataSizeUnit.Mebibits,       0.95367431640625);
                convertToTest.run(DataSize.megabits(1), DataSizeUnit.Kibibits,     976.5625);
                convertToTest.run(DataSize.megabits(1), DataSizeUnit.Yottabytes,     0.000000000000000000125);
                convertToTest.run(DataSize.megabits(1), DataSizeUnit.Zettabytes,     0.000000000000000125);
                convertToTest.run(DataSize.megabits(1), DataSizeUnit.Exabytes,       0.000000000000125);
                convertToTest.run(DataSize.megabits(1), DataSizeUnit.Petabytes,      0.000000000125);
                convertToTest.run(DataSize.megabits(1), DataSizeUnit.Terabytes,      0.000000125);
                convertToTest.run(DataSize.megabits(1), DataSizeUnit.Gigabytes,      0.000125);
                convertToTest.run(DataSize.megabits(1), DataSizeUnit.Megabytes,      0.125);
                convertToTest.run(DataSize.megabits(1), DataSizeUnit.Kilobytes,    125.0);
                convertToTest.run(DataSize.megabits(1), DataSizeUnit.Bytes,     125000.0);
                convertToTest.run(DataSize.megabits(1), DataSizeUnit.Yottabits,      0.000000000000000001);
                convertToTest.run(DataSize.megabits(1), DataSizeUnit.Zettabits,      0.000000000000001);
                convertToTest.run(DataSize.megabits(1), DataSizeUnit.Exabits,        0.000000000001);
                convertToTest.run(DataSize.megabits(1), DataSizeUnit.Petabits,       0.000000001);
                convertToTest.run(DataSize.megabits(1), DataSizeUnit.Terabits,       0.000001);
                convertToTest.run(DataSize.megabits(1), DataSizeUnit.Gigabits,       0.001);
                convertToTest.run(DataSize.megabits(1), DataSizeUnit.Megabits,       1.0);
                convertToTest.run(DataSize.megabits(1), DataSizeUnit.Kilobits,    1000.0);
                convertToTest.run(DataSize.megabits(1), DataSizeUnit.Bits,     1000000.0);

                convertToTest.run(DataSize.kilobits(1), DataSizeUnit.Yobibytes,   0.00000000000000000000010339757656912846);
                convertToTest.run(DataSize.kilobits(1), DataSizeUnit.Zebibytes,   0.00000000000000000010587911840678754);
                convertToTest.run(DataSize.kilobits(1), DataSizeUnit.Exbibytes,   0.00000000000000010842021724855044);
                convertToTest.run(DataSize.kilobits(1), DataSizeUnit.Pebibytes,   0.00000000000011102230246251565);
                convertToTest.run(DataSize.kilobits(1), DataSizeUnit.Tebibytes,   0.00000000011368683772161603);
                convertToTest.run(DataSize.kilobits(1), DataSizeUnit.Gibibytes,   0.00000011641532182693481);
                convertToTest.run(DataSize.kilobits(1), DataSizeUnit.Mebibytes,   0.00011920928955078125);
                convertToTest.run(DataSize.kilobits(1), DataSizeUnit.Kibibytes,   0.1220703125);
                convertToTest.run(DataSize.kilobits(1), DataSizeUnit.Yobibits,    0.0000000000000000000008271806125530277);
                convertToTest.run(DataSize.kilobits(1), DataSizeUnit.Zebibits,    0.0000000000000000008470329472543003);
                convertToTest.run(DataSize.kilobits(1), DataSizeUnit.Exbibits,    0.0000000000000008673617379884035);
                convertToTest.run(DataSize.kilobits(1), DataSizeUnit.Pebibits,    0.0000000000008881784197001252);
                convertToTest.run(DataSize.kilobits(1), DataSizeUnit.Tebibits,    0.0000000009094947017729282);
                convertToTest.run(DataSize.kilobits(1), DataSizeUnit.Gibibits,    0.0000009313225746154785);
                convertToTest.run(DataSize.kilobits(1), DataSizeUnit.Mebibits,    0.00095367431640625);
                convertToTest.run(DataSize.kilobits(1), DataSizeUnit.Kibibits,    0.9765625);
                convertToTest.run(DataSize.kilobits(1), DataSizeUnit.Yottabytes,  0.000000000000000000000125);
                convertToTest.run(DataSize.kilobits(1), DataSizeUnit.Zettabytes,  0.000000000000000000125);
                convertToTest.run(DataSize.kilobits(1), DataSizeUnit.Exabytes,    0.000000000000000125);
                convertToTest.run(DataSize.kilobits(1), DataSizeUnit.Petabytes,   0.000000000000125);
                convertToTest.run(DataSize.kilobits(1), DataSizeUnit.Terabytes,   0.000000000125);
                convertToTest.run(DataSize.kilobits(1), DataSizeUnit.Gigabytes,   0.000000125);
                convertToTest.run(DataSize.kilobits(1), DataSizeUnit.Megabytes,   0.000125);
                convertToTest.run(DataSize.kilobits(1), DataSizeUnit.Kilobytes,   0.125);
                convertToTest.run(DataSize.kilobits(1), DataSizeUnit.Bytes,     125.0);
                convertToTest.run(DataSize.kilobits(1), DataSizeUnit.Yottabits,   0.000000000000000000001);
                convertToTest.run(DataSize.kilobits(1), DataSizeUnit.Zettabits,   0.000000000000000001);
                convertToTest.run(DataSize.kilobits(1), DataSizeUnit.Exabits,     0.000000000000001);
                convertToTest.run(DataSize.kilobits(1), DataSizeUnit.Petabits,    0.000000000001);
                convertToTest.run(DataSize.kilobits(1), DataSizeUnit.Terabits,    0.000000001);
                convertToTest.run(DataSize.kilobits(1), DataSizeUnit.Gigabits,    0.000001);
                convertToTest.run(DataSize.kilobits(1), DataSizeUnit.Megabits,    0.001);
                convertToTest.run(DataSize.kilobits(1), DataSizeUnit.Kilobits,    1.0);
                convertToTest.run(DataSize.kilobits(1), DataSizeUnit.Bits,     1000.0);

                convertToTest.run(DataSize.bits(1), DataSizeUnit.Yobibytes,  0.00000000000000000000000010339757656912846);
                convertToTest.run(DataSize.bits(1), DataSizeUnit.Zebibytes,  0.00000000000000000000010587911840678754);
                convertToTest.run(DataSize.bits(1), DataSizeUnit.Exbibytes,  0.00000000000000000010842021724855044);
                convertToTest.run(DataSize.bits(1), DataSizeUnit.Pebibytes,  0.00000000000000011102230246251565);
                convertToTest.run(DataSize.bits(1), DataSizeUnit.Tebibytes,  0.00000000000011368683772161603);
                convertToTest.run(DataSize.bits(1), DataSizeUnit.Gibibytes,  0.00000000011641532182693481);
                convertToTest.run(DataSize.bits(1), DataSizeUnit.Mebibytes,  0.00000011920928955078125);
                convertToTest.run(DataSize.bits(1), DataSizeUnit.Kibibytes,  0.0001220703125);
                convertToTest.run(DataSize.bits(1), DataSizeUnit.Yobibits,   0.0000000000000000000000008271806125530277);
                convertToTest.run(DataSize.bits(1), DataSizeUnit.Zebibits,   0.0000000000000000000008470329472543003);
                convertToTest.run(DataSize.bits(1), DataSizeUnit.Exbibits,   0.0000000000000000008673617379884035);
                convertToTest.run(DataSize.bits(1), DataSizeUnit.Pebibits,   0.0000000000000008881784197001252);
                convertToTest.run(DataSize.bits(1), DataSizeUnit.Tebibits,   0.0000000000009094947017729282);
                convertToTest.run(DataSize.bits(1), DataSizeUnit.Gibibits,   0.0000000009313225746154785);
                convertToTest.run(DataSize.bits(1), DataSizeUnit.Mebibits,   0.00000095367431640625);
                convertToTest.run(DataSize.bits(1), DataSizeUnit.Kibibits,   0.0009765625);
                convertToTest.run(DataSize.bits(1), DataSizeUnit.Yottabytes, 0.000000000000000000000000125);
                convertToTest.run(DataSize.bits(1), DataSizeUnit.Zettabytes, 0.000000000000000000000125);
                convertToTest.run(DataSize.bits(1), DataSizeUnit.Exabytes,   0.000000000000000000125);
                convertToTest.run(DataSize.bits(1), DataSizeUnit.Petabytes,  0.000000000000000125);
                convertToTest.run(DataSize.bits(1), DataSizeUnit.Terabytes,  0.000000000000125);
                convertToTest.run(DataSize.bits(1), DataSizeUnit.Gigabytes,  0.000000000125);
                convertToTest.run(DataSize.bits(1), DataSizeUnit.Megabytes,  0.000000125);
                convertToTest.run(DataSize.bits(1), DataSizeUnit.Kilobytes,  0.000125);
                convertToTest.run(DataSize.bits(1), DataSizeUnit.Bytes,      0.125);
                convertToTest.run(DataSize.bits(1), DataSizeUnit.Yottabits,  0.000000000000000000000001);
                convertToTest.run(DataSize.bits(1), DataSizeUnit.Zettabits,  0.000000000000000000001);
                convertToTest.run(DataSize.bits(1), DataSizeUnit.Exabits,    0.000000000000000001);
                convertToTest.run(DataSize.bits(1), DataSizeUnit.Petabits,   0.000000000000001);
                convertToTest.run(DataSize.bits(1), DataSizeUnit.Terabits,   0.000000000001);
                convertToTest.run(DataSize.bits(1), DataSizeUnit.Gigabits,   0.000000001);
                convertToTest.run(DataSize.bits(1), DataSizeUnit.Megabits,   0.000001);
                convertToTest.run(DataSize.bits(1), DataSizeUnit.Kilobits,   0.001);
                convertToTest.run(DataSize.bits(1), DataSizeUnit.Bits,       1.0);
            });
        });
    }
}
