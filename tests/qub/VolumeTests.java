package qub;

public interface VolumeTests
{
    static void test(TestRunner runner)
    {
        PreCondition.assertNotNull(runner, "runner");

        runner.testGroup(Volume.class, () ->
        {
            runner.test("kiloliters()", (Test test) ->
            {
                final Volume volume = Volume.kiloliters(1);
                test.assertEqual(1, volume.getValue());
                test.assertEqual(VolumeUnit.Kiloliters, volume.getUnits());
            });

            runner.test("liters()", (Test test) ->
            {
                final Volume volume = Volume.liters(2);
                test.assertEqual(2, volume.getValue());
                test.assertEqual(VolumeUnit.Liters, volume.getUnits());
            });

            runner.test("milliliters()", (Test test) ->
            {
                final Volume volume = Volume.milliliters(3);
                test.assertEqual(3, volume.getValue());
                test.assertEqual(VolumeUnit.Milliliters, volume.getUnits());
            });

            runner.test("usGallons()", (Test test) ->
            {
                final Volume volume = Volume.usGallons(-4);
                test.assertEqual(-4, volume.getValue());
                test.assertEqual(VolumeUnit.USGallons, volume.getUnits());
            });

            runner.test("usQuarts()", (Test test) ->
            {
                final Volume volume = Volume.usQuarts(5);
                test.assertEqual(5, volume.getValue());
                test.assertEqual(VolumeUnit.USQuarts, volume.getUnits());
            });

            runner.test("usPints()", (Test test) ->
            {
                final Volume volume = Volume.usPints(-6);
                test.assertEqual(-6, volume.getValue());
                test.assertEqual(VolumeUnit.USPints, volume.getUnits());
            });

            runner.test("usCups()", (Test test) ->
            {
                final Volume volume = Volume.usCups(239487);
                test.assertEqual(239487, volume.getValue());
                test.assertEqual(VolumeUnit.USCups, volume.getUnits());
            });

            runner.test("usFluidOunces()", (Test test) ->
            {
                final Volume volume = Volume.usFluidOunces(7);
                test.assertEqual(7, volume.getValue());
                test.assertEqual(VolumeUnit.USFluidOunces, volume.getUnits());
            });

            runner.test("usTablespoons()", (Test test) ->
            {
                final Volume volume = Volume.usTablespoons(8);
                test.assertEqual(8, volume.getValue());
                test.assertEqual(VolumeUnit.USTablespoons, volume.getUnits());
            });

            runner.test("usTeaspoons()", (Test test) ->
            {
                final Volume volume = Volume.usTeaspoons(9);
                test.assertEqual(9, volume.getValue());
                test.assertEqual(VolumeUnit.USTeaspoons, volume.getUnits());
            });

            runner.testGroup("convertTo(VolumeUnit)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final Volume volume = Volume.kiloliters(1);
                    test.assertThrows(() -> volume.convertTo(null),
                        new PreConditionFailure("units cannot be null."));
                });

                final Action3<Volume,VolumeUnit,Double> convertToTest = (Volume volume, VolumeUnit units, Double expectedValue) ->
                {
                    runner.test("with " + volume + " and " + units, (Test test) ->
                    {
                        final Volume convertedVolume = volume.convertTo(units);
                        test.assertNotNull(convertedVolume);
                        test.assertSame(units, convertedVolume.getUnits());
                        test.assertEqual(expectedValue, convertedVolume.getValue());
                        if (units == volume.getUnits())
                        {
                            test.assertSame(volume, convertedVolume);
                        }
                    });
                };

                convertToTest.run(Volume.kiloliters(1), VolumeUnit.Kiloliters, 1.0);
                convertToTest.run(Volume.kiloliters(1), VolumeUnit.Liters, 1000.0);
                convertToTest.run(Volume.kiloliters(1), VolumeUnit.Milliliters, 1000000.0);
                convertToTest.run(Volume.kiloliters(1), VolumeUnit.USGallons, 264.172176857989);
                convertToTest.run(Volume.kiloliters(1), VolumeUnit.USQuarts, 1056.688707431956);
                convertToTest.run(Volume.kiloliters(1), VolumeUnit.USPints, 2113.377414863912);
                convertToTest.run(Volume.kiloliters(1), VolumeUnit.USCups, 4226.754829727824);
                convertToTest.run(Volume.kiloliters(1), VolumeUnit.USFluidOunces, 34301.67934252829);
                convertToTest.run(Volume.kiloliters(1), VolumeUnit.USTablespoons, 68603.18961486338);
                convertToTest.run(Volume.kiloliters(1), VolumeUnit.USTeaspoons, 205809.56884459016);

                convertToTest.run(Volume.liters(1), VolumeUnit.Kiloliters, 0.001);
                convertToTest.run(Volume.liters(1), VolumeUnit.Liters, 1.0);
                convertToTest.run(Volume.liters(1), VolumeUnit.Milliliters, 1000.0);
                convertToTest.run(Volume.liters(1), VolumeUnit.USGallons, 0.26417217685798894);
                convertToTest.run(Volume.liters(1), VolumeUnit.USQuarts, 1.0566887074319558);
                convertToTest.run(Volume.liters(1), VolumeUnit.USPints, 2.1133774148639115);
                convertToTest.run(Volume.liters(1), VolumeUnit.USCups, 4.226754829727823);
                convertToTest.run(Volume.liters(1), VolumeUnit.USFluidOunces, 34.30167934252828);
                convertToTest.run(Volume.liters(1), VolumeUnit.USTablespoons, 68.60318961486337);
                convertToTest.run(Volume.liters(1), VolumeUnit.USTeaspoons, 205.8095688445901);

                convertToTest.run(Volume.milliliters(1), VolumeUnit.Kiloliters, 0.000001);
                convertToTest.run(Volume.milliliters(1), VolumeUnit.Liters, 0.001);
                convertToTest.run(Volume.milliliters(1), VolumeUnit.Milliliters, 1.0);
                convertToTest.run(Volume.milliliters(1), VolumeUnit.USGallons, 0.00026417217685798894);
                convertToTest.run(Volume.milliliters(1), VolumeUnit.USQuarts, 0.0010566887074319558);
                convertToTest.run(Volume.milliliters(1), VolumeUnit.USPints, 0.0021133774148639115);
                convertToTest.run(Volume.milliliters(1), VolumeUnit.USCups, 0.004226754829727823);
                convertToTest.run(Volume.milliliters(1), VolumeUnit.USFluidOunces, 0.034301679342528285);
                convertToTest.run(Volume.milliliters(1), VolumeUnit.USTablespoons, 0.06860318961486338);
                convertToTest.run(Volume.milliliters(1), VolumeUnit.USTeaspoons, 0.20580956884459012);

                convertToTest.run(Volume.usGallons(1), VolumeUnit.Kiloliters, 0.00378541);
                convertToTest.run(Volume.usGallons(1), VolumeUnit.Liters, 3.78541);
                convertToTest.run(Volume.usGallons(1), VolumeUnit.Milliliters, 3785.4100000000003);
                convertToTest.run(Volume.usGallons(1), VolumeUnit.USGallons, 1.0);
                convertToTest.run(Volume.usGallons(1), VolumeUnit.USQuarts, 4.0);
                convertToTest.run(Volume.usGallons(1), VolumeUnit.USPints, 8.0);
                convertToTest.run(Volume.usGallons(1), VolumeUnit.USCups, 16.0);
                convertToTest.run(Volume.usGallons(1), VolumeUnit.USFluidOunces, 129.84592);
                convertToTest.run(Volume.usGallons(1), VolumeUnit.USTablespoons, 259.6912);
                convertToTest.run(Volume.usGallons(1), VolumeUnit.USTeaspoons, 779.0735999999999);

                convertToTest.run(Volume.usQuarts(1), VolumeUnit.Kiloliters, 0.0009463525);
                convertToTest.run(Volume.usQuarts(1), VolumeUnit.Liters, 0.9463525);
                convertToTest.run(Volume.usQuarts(1), VolumeUnit.Milliliters, 946.3525000000001);
                convertToTest.run(Volume.usQuarts(1), VolumeUnit.USGallons, 0.25);
                convertToTest.run(Volume.usQuarts(1), VolumeUnit.USQuarts, 1.0);
                convertToTest.run(Volume.usQuarts(1), VolumeUnit.USPints, 2.0);
                convertToTest.run(Volume.usQuarts(1), VolumeUnit.USCups, 4.0);
                convertToTest.run(Volume.usQuarts(1), VolumeUnit.USFluidOunces, 32.46148);
                convertToTest.run(Volume.usQuarts(1), VolumeUnit.USTablespoons, 64.9228);
                convertToTest.run(Volume.usQuarts(1), VolumeUnit.USTeaspoons, 194.76839999999999);

                convertToTest.run(Volume.usPints(1), VolumeUnit.Kiloliters, 0.00047317625);
                convertToTest.run(Volume.usPints(1), VolumeUnit.Liters, 0.47317625);
                convertToTest.run(Volume.usPints(1), VolumeUnit.Milliliters, 473.17625000000004);
                convertToTest.run(Volume.usPints(1), VolumeUnit.USGallons, 0.125);
                convertToTest.run(Volume.usPints(1), VolumeUnit.USQuarts, 0.5);
                convertToTest.run(Volume.usPints(1), VolumeUnit.USPints, 1.0);
                convertToTest.run(Volume.usPints(1), VolumeUnit.USCups, 2.0);
                convertToTest.run(Volume.usPints(1), VolumeUnit.USFluidOunces, 16.23074);
                convertToTest.run(Volume.usPints(1), VolumeUnit.USTablespoons, 32.4614);
                convertToTest.run(Volume.usPints(1), VolumeUnit.USTeaspoons, 97.38419999999999);

                convertToTest.run(Volume.usCups(1), VolumeUnit.Kiloliters, 0.000236588125);
                convertToTest.run(Volume.usCups(1), VolumeUnit.Liters, 0.236588125);
                convertToTest.run(Volume.usCups(1), VolumeUnit.Milliliters, 236.58812500000002);
                convertToTest.run(Volume.usCups(1), VolumeUnit.USGallons, 0.0625);
                convertToTest.run(Volume.usCups(1), VolumeUnit.USQuarts, 0.25);
                convertToTest.run(Volume.usCups(1), VolumeUnit.USPints, 0.5);
                convertToTest.run(Volume.usCups(1), VolumeUnit.USCups, 1.0);
                convertToTest.run(Volume.usCups(1), VolumeUnit.USFluidOunces, 8.11537);
                convertToTest.run(Volume.usCups(1), VolumeUnit.USTablespoons, 16.2307);
                convertToTest.run(Volume.usCups(1), VolumeUnit.USTeaspoons, 48.692099999999996);

                convertToTest.run(Volume.usFluidOunces(1), VolumeUnit.Kiloliters, 0.000029153091602724213);
                convertToTest.run(Volume.usFluidOunces(1), VolumeUnit.Liters, 0.029153091602724217);
                convertToTest.run(Volume.usFluidOunces(1), VolumeUnit.Milliliters, 29.153091602724214);
                convertToTest.run(Volume.usFluidOunces(1), VolumeUnit.USGallons, 0.007701435670832014);
                convertToTest.run(Volume.usFluidOunces(1), VolumeUnit.USQuarts, 0.030805742683328054);
                convertToTest.run(Volume.usFluidOunces(1), VolumeUnit.USPints, 0.06161148536665611);
                convertToTest.run(Volume.usFluidOunces(1), VolumeUnit.USCups, 0.12322297073331222);
                convertToTest.run(Volume.usFluidOunces(1), VolumeUnit.USFluidOunces, 1.0);
                convertToTest.run(Volume.usFluidOunces(1), VolumeUnit.USTablespoons, 1.9999950710811705);
                convertToTest.run(Volume.usFluidOunces(1), VolumeUnit.USTeaspoons, 5.999985213243511);

                convertToTest.run(Volume.usTablespoons(1), VolumeUnit.Kiloliters, 0.000014576581724756172);
                convertToTest.run(Volume.usTablespoons(1), VolumeUnit.Liters, 0.014576581724756175);
                convertToTest.run(Volume.usTablespoons(1), VolumeUnit.Milliliters, 14.576581724756172);
                convertToTest.run(Volume.usTablespoons(1), VolumeUnit.USGallons, 0.0038507273253772173);
                convertToTest.run(Volume.usTablespoons(1), VolumeUnit.USQuarts, 0.01540290930150887);
                convertToTest.run(Volume.usTablespoons(1), VolumeUnit.USPints, 0.03080581860301774);
                convertToTest.run(Volume.usTablespoons(1), VolumeUnit.USCups, 0.06161163720603548);
                convertToTest.run(Volume.usTablespoons(1), VolumeUnit.USFluidOunces, 0.5000012322327442);
                convertToTest.run(Volume.usTablespoons(1), VolumeUnit.USTablespoons, 1.0);
                convertToTest.run(Volume.usTablespoons(1), VolumeUnit.USTeaspoons, 3.0);

                convertToTest.run(Volume.usTeaspoons(1), VolumeUnit.Kiloliters, 0.000004858860574918724);
                convertToTest.run(Volume.usTeaspoons(1), VolumeUnit.Liters, 0.004858860574918725);
                convertToTest.run(Volume.usTeaspoons(1), VolumeUnit.Milliliters, 4.858860574918725);
                convertToTest.run(Volume.usTeaspoons(1), VolumeUnit.USGallons, 0.0012835757751257391);
                convertToTest.run(Volume.usTeaspoons(1), VolumeUnit.USQuarts, 0.0051343031005029564);
                convertToTest.run(Volume.usTeaspoons(1), VolumeUnit.USPints, 0.010268606201005913);
                convertToTest.run(Volume.usTeaspoons(1), VolumeUnit.USCups, 0.020537212402011826);
                convertToTest.run(Volume.usTeaspoons(1), VolumeUnit.USFluidOunces, 0.16666707741091472);
                convertToTest.run(Volume.usTeaspoons(1), VolumeUnit.USTablespoons, 0.3333333333333333);
                convertToTest.run(Volume.usTeaspoons(1), VolumeUnit.USTeaspoons, 1.0);
            });

            runner.testGroup("toKiloliters()", () ->
            {
                final Action2<Volume,Double> toKilolitersTest = (Volume volume, Double expectedValue) ->
                {
                    runner.test("with " + volume, (Test test) ->
                    {
                        final Volume kiloliters = volume.toKiloliters();
                        test.assertNotNull(kiloliters);
                        test.assertEqual(VolumeUnit.Kiloliters, kiloliters.getUnits());
                        test.assertEqual(expectedValue, kiloliters.getValue());
                        if (volume.getUnits() == VolumeUnit.Kiloliters)
                        {
                            test.assertSame(volume, kiloliters);
                        }
                    });
                };

                toKilolitersTest.run(Volume.kiloliters(1), 1.0);
                toKilolitersTest.run(Volume.liters(1), 0.001);
                toKilolitersTest.run(Volume.milliliters(1), 0.000001);
                toKilolitersTest.run(Volume.usGallons(1), 0.00378541);
                toKilolitersTest.run(Volume.usQuarts(1), 0.0009463525);
                toKilolitersTest.run(Volume.usPints(1), 0.00047317625);
                toKilolitersTest.run(Volume.usCups(1), 0.000236588125);
                toKilolitersTest.run(Volume.usFluidOunces(1), 0.000029153091602724213);
                toKilolitersTest.run(Volume.usTablespoons(1), 0.000014576581724756172);
                toKilolitersTest.run(Volume.usTeaspoons(1), 0.000004858860574918724);
            });

            runner.testGroup("toLiters()", () ->
            {
                final Action2<Volume,Double> toLitersTest = (Volume volume, Double expectedValue) ->
                {
                    runner.test("with " + volume, (Test test) ->
                    {
                        final Volume liters = volume.toLiters();
                        test.assertNotNull(liters);
                        test.assertEqual(VolumeUnit.Liters, liters.getUnits());
                        test.assertEqual(expectedValue, liters.getValue());
                        if (volume.getUnits() == VolumeUnit.Liters)
                        {
                            test.assertSame(volume, liters);
                        }
                    });
                };

                toLitersTest.run(Volume.kiloliters(1), 1000.0);
                toLitersTest.run(Volume.liters(1), 1.0);
                toLitersTest.run(Volume.milliliters(1), 0.001);
                toLitersTest.run(Volume.usGallons(1), 3.78541);
                toLitersTest.run(Volume.usQuarts(1), 0.9463525);
                toLitersTest.run(Volume.usPints(1), 0.47317625);
                toLitersTest.run(Volume.usCups(1), 0.236588125);
                toLitersTest.run(Volume.usFluidOunces(1), 0.029153091602724217);
                toLitersTest.run(Volume.usTablespoons(1), 0.014576581724756175);
                toLitersTest.run(Volume.usTeaspoons(1), 0.004858860574918725);
            });

            runner.testGroup("toMilliliters()", () ->
            {
                final Action2<Volume,Double> toMillilitersTest = (Volume volume, Double expectedValue) ->
                {
                    runner.test("with " + volume, (Test test) ->
                    {
                        final Volume milliliters = volume.toMilliliters();
                        test.assertNotNull(milliliters);
                        test.assertEqual(VolumeUnit.Milliliters, milliliters.getUnits());
                        test.assertEqual(expectedValue, milliliters.getValue());
                        if (volume.getUnits() == VolumeUnit.Milliliters)
                        {
                            test.assertSame(volume, milliliters);
                        }
                    });
                };

                toMillilitersTest.run(Volume.kiloliters(1), 1000000.0);
                toMillilitersTest.run(Volume.liters(1), 1000.0);
                toMillilitersTest.run(Volume.milliliters(1), 1.0);
                toMillilitersTest.run(Volume.usGallons(1), 3785.4100000000003);
                toMillilitersTest.run(Volume.usQuarts(1), 946.3525000000001);
                toMillilitersTest.run(Volume.usPints(1), 473.17625000000004);
                toMillilitersTest.run(Volume.usCups(1), 236.58812500000002);
                toMillilitersTest.run(Volume.usFluidOunces(1), 29.153091602724214);
                toMillilitersTest.run(Volume.usTablespoons(1), 14.576581724756172);
                toMillilitersTest.run(Volume.usTeaspoons(1), 4.858860574918725);
            });

            runner.testGroup("toUSGallons()", () ->
            {
                final Action2<Volume,Double> toUSGallonsTest = (Volume volume, Double expectedValue) ->
                {
                    runner.test("with " + volume, (Test test) ->
                    {
                        final Volume usGallons = volume.toUSGallons();
                        test.assertNotNull(usGallons);
                        test.assertEqual(VolumeUnit.USGallons, usGallons.getUnits());
                        test.assertEqual(expectedValue, usGallons.getValue());
                        if (volume.getUnits() == VolumeUnit.USGallons)
                        {
                            test.assertSame(volume, usGallons);
                        }
                    });
                };

                toUSGallonsTest.run(Volume.kiloliters(1), 264.172176857989);
                toUSGallonsTest.run(Volume.liters(1), 0.26417217685798894);
                toUSGallonsTest.run(Volume.milliliters(1), 0.00026417217685798894);
                toUSGallonsTest.run(Volume.usGallons(1), 1.0);
                toUSGallonsTest.run(Volume.usQuarts(1), 0.25);
                toUSGallonsTest.run(Volume.usPints(1), 0.125);
                toUSGallonsTest.run(Volume.usCups(1), 0.0625);
                toUSGallonsTest.run(Volume.usFluidOunces(1), 0.007701435670832014);
                toUSGallonsTest.run(Volume.usTablespoons(1), 0.0038507273253772173);
                toUSGallonsTest.run(Volume.usTeaspoons(1), 0.0012835757751257391);
            });

            runner.testGroup("toUSQuarts()", () ->
            {
                final Action2<Volume,Double> toUSQuartsTest = (Volume volume, Double expectedValue) ->
                {
                    runner.test("with " + volume, (Test test) ->
                    {
                        final Volume usQuarts = volume.toUSQuarts();
                        test.assertNotNull(usQuarts);
                        test.assertEqual(VolumeUnit.USQuarts, usQuarts.getUnits());
                        test.assertEqual(expectedValue, usQuarts.getValue());
                        if (volume.getUnits() == VolumeUnit.USQuarts)
                        {
                            test.assertSame(volume, usQuarts);
                        }
                    });
                };

                toUSQuartsTest.run(Volume.kiloliters(1), 1056.688707431956);
                toUSQuartsTest.run(Volume.liters(1), 1.0566887074319558);
                toUSQuartsTest.run(Volume.milliliters(1), 0.0010566887074319558);
                toUSQuartsTest.run(Volume.usGallons(1), 4.0);
                toUSQuartsTest.run(Volume.usQuarts(1), 1.0);
                toUSQuartsTest.run(Volume.usPints(1), 0.5);
                toUSQuartsTest.run(Volume.usCups(1), 0.25);
                toUSQuartsTest.run(Volume.usFluidOunces(1), 0.030805742683328054);
                toUSQuartsTest.run(Volume.usTablespoons(1), 0.01540290930150887);
                toUSQuartsTest.run(Volume.usTeaspoons(1), 0.0051343031005029564);
            });

            runner.testGroup("toUSPints()", () ->
            {
                final Action2<Volume,Double> toUSPintsTest = (Volume volume, Double expectedValue) ->
                {
                    runner.test("with " + volume, (Test test) ->
                    {
                        final Volume usPints = volume.toUSPints();
                        test.assertNotNull(usPints);
                        test.assertEqual(VolumeUnit.USPints, usPints.getUnits());
                        test.assertEqual(expectedValue, usPints.getValue());
                        if (volume.getUnits() == VolumeUnit.USPints)
                        {
                            test.assertSame(volume, usPints);
                        }
                    });
                };

                toUSPintsTest.run(Volume.kiloliters(1), 2113.377414863912);
                toUSPintsTest.run(Volume.liters(1), 2.1133774148639115);
                toUSPintsTest.run(Volume.milliliters(1), 0.0021133774148639115);
                toUSPintsTest.run(Volume.usGallons(1), 8.0);
                toUSPintsTest.run(Volume.usQuarts(1), 2.0);
                toUSPintsTest.run(Volume.usPints(1), 1.0);
                toUSPintsTest.run(Volume.usCups(1), 0.5);
                toUSPintsTest.run(Volume.usFluidOunces(1), 0.06161148536665611);
                toUSPintsTest.run(Volume.usTablespoons(1), 0.03080581860301774);
                toUSPintsTest.run(Volume.usTeaspoons(1), 0.010268606201005913);
            });

            runner.testGroup("toUSCups()", () ->
            {
                final Action2<Volume,Double> toUSCupsTest = (Volume volume, Double expectedValue) ->
                {
                    runner.test("with " + volume, (Test test) ->
                    {
                        final Volume usCups = volume.toUSCups();
                        test.assertNotNull(usCups);
                        test.assertEqual(VolumeUnit.USCups, usCups.getUnits());
                        test.assertEqual(expectedValue, usCups.getValue());
                        if (volume.getUnits() == VolumeUnit.USCups)
                        {
                            test.assertSame(volume, usCups);
                        }
                    });
                };

                toUSCupsTest.run(Volume.kiloliters(1), 4226.754829727824);
                toUSCupsTest.run(Volume.liters(1), 4.226754829727823);
                toUSCupsTest.run(Volume.milliliters(1), 0.004226754829727823);
                toUSCupsTest.run(Volume.usGallons(1), 16.0);
                toUSCupsTest.run(Volume.usQuarts(1), 4.0);
                toUSCupsTest.run(Volume.usPints(1), 2.0);
                toUSCupsTest.run(Volume.usCups(1), 1.0);
                toUSCupsTest.run(Volume.usFluidOunces(1), 0.12322297073331222);
                toUSCupsTest.run(Volume.usTablespoons(1), 0.06161163720603548);
                toUSCupsTest.run(Volume.usTeaspoons(1), 0.020537212402011826);
            });

            runner.testGroup("toUSFluidOunces()", () ->
            {
                final Action2<Volume,Double> toUSFluidOunces = (Volume volume, Double expectedValue) ->
                {
                    runner.test("with " + volume, (Test test) ->
                    {
                        final Volume usFluidOunces = volume.toUSFluidOunces();
                        test.assertNotNull(usFluidOunces);
                        test.assertEqual(VolumeUnit.USFluidOunces, usFluidOunces.getUnits());
                        test.assertEqual(expectedValue, usFluidOunces.getValue());
                        if (volume.getUnits() == VolumeUnit.USFluidOunces)
                        {
                            test.assertSame(volume, usFluidOunces);
                        }
                    });
                };

                toUSFluidOunces.run(Volume.kiloliters(1), 34301.67934252829);
                toUSFluidOunces.run(Volume.liters(1), 34.30167934252828);
                toUSFluidOunces.run(Volume.milliliters(1), 0.034301679342528285);
                toUSFluidOunces.run(Volume.usGallons(1), 129.84592);
                toUSFluidOunces.run(Volume.usQuarts(1), 32.46148);
                toUSFluidOunces.run(Volume.usPints(1), 16.23074);
                toUSFluidOunces.run(Volume.usCups(1), 8.11537);
                toUSFluidOunces.run(Volume.usFluidOunces(1), 1.0);
                toUSFluidOunces.run(Volume.usTablespoons(1), 0.5000012322327442);
                toUSFluidOunces.run(Volume.usTeaspoons(1), 0.16666707741091472);
            });

            runner.testGroup("toUSTablespoons()", () ->
            {
                final Action2<Volume,Double> toUSTablespoonsTest = (Volume volume, Double expectedValue) ->
                {
                    runner.test("with " + volume, (Test test) ->
                    {
                        final Volume usTablespoons = volume.toUSTablespoons();
                        test.assertNotNull(usTablespoons);
                        test.assertEqual(VolumeUnit.USTablespoons, usTablespoons.getUnits());
                        test.assertEqual(expectedValue, usTablespoons.getValue());
                        if (volume.getUnits() == VolumeUnit.USTablespoons)
                        {
                            test.assertSame(volume, usTablespoons);
                        }
                    });
                };

                toUSTablespoonsTest.run(Volume.kiloliters(1), 68603.18961486338);
                toUSTablespoonsTest.run(Volume.liters(1), 68.60318961486337);
                toUSTablespoonsTest.run(Volume.milliliters(1), 0.06860318961486338);
                toUSTablespoonsTest.run(Volume.usGallons(1), 259.6912);
                toUSTablespoonsTest.run(Volume.usQuarts(1), 64.9228);
                toUSTablespoonsTest.run(Volume.usPints(1), 32.4614);
                toUSTablespoonsTest.run(Volume.usCups(1), 16.2307);
                toUSTablespoonsTest.run(Volume.usFluidOunces(1), 1.9999950710811705);
                toUSTablespoonsTest.run(Volume.usTablespoons(1), 1.0);
                toUSTablespoonsTest.run(Volume.usTeaspoons(1), 0.3333333333333333);
            });

            runner.testGroup("toUSTeaspoons()", () ->
            {
                final Action2<Volume,Double> toUSTeaspoonsTest = (Volume volume, Double expectedValue) ->
                {
                    runner.test("with " + volume, (Test test) ->
                    {
                        final Volume usTeaspoons = volume.toUSTeaspoons();
                        test.assertNotNull(usTeaspoons);
                        test.assertEqual(VolumeUnit.USTeaspoons, usTeaspoons.getUnits());
                        test.assertEqual(expectedValue, usTeaspoons.getValue());
                        if (volume.getUnits() == VolumeUnit.USTeaspoons)
                        {
                            test.assertSame(volume, usTeaspoons);
                        }
                    });
                };

                toUSTeaspoonsTest.run(Volume.kiloliters(1), 205809.56884459016);
                toUSTeaspoonsTest.run(Volume.liters(1), 205.8095688445901);
                toUSTeaspoonsTest.run(Volume.milliliters(1), 0.20580956884459012);
                toUSTeaspoonsTest.run(Volume.usGallons(1), 779.0735999999999);
                toUSTeaspoonsTest.run(Volume.usQuarts(1), 194.76839999999999);
                toUSTeaspoonsTest.run(Volume.usPints(1), 97.38419999999999);
                toUSTeaspoonsTest.run(Volume.usCups(1), 48.692099999999996);
                toUSTeaspoonsTest.run(Volume.usFluidOunces(1), 5.999985213243511);
                toUSTeaspoonsTest.run(Volume.usTablespoons(1), 3.0);
                toUSTeaspoonsTest.run(Volume.usTeaspoons(1), 1.0);
            });

            runner.testGroup("negate()",() ->
            {
                runner.test("with zero", (Test test) ->
                {
                    test.assertSame(Volume.zero, Volume.zero.negate());

                    final Volume zero = Volume.usQuarts(0);
                    test.assertSame(zero, zero.negate());
                });

                runner.test("with negative", (Test test) ->
                {
                    final Volume volume = Volume.usFluidOunces(-5).negate();
                    test.assertEqual(5, volume.getValue());
                    test.assertEqual(VolumeUnit.USFluidOunces, volume.getUnits());
                });

                runner.test("with positive", (Test test) ->
                {
                    final Volume volume = Volume.usFluidOunces(6).negate();
                    test.assertEqual(-6, volume.getValue());
                    test.assertEqual(VolumeUnit.USFluidOunces, volume.getUnits());
                });
            });

            runner.testGroup("plus(Volume)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> Volume.zero.plus(null),
                        new PreConditionFailure("rhs cannot be null."));
                });

                final Action3<Volume,Volume,Volume> plusTest = (Volume lhs, Volume rhs, Volume expected) ->
                {
                    runner.test("with " + lhs + " and " + rhs, (Test test) ->
                    {
                        final Volume sum = lhs.plus(rhs);
                        test.assertNotNull(sum);
                        test.assertEqual(expected.getValue(), sum.getValue());
                        test.assertEqual(expected.getUnits(), sum.getUnits());
                    });
                };

                plusTest.run(Volume.zero, Volume.zero, Volume.zero);
                plusTest.run(Volume.zero, Volume.milliliters(-1), Volume.liters(-0.001));
                plusTest.run(Volume.zero, Volume.kiloliters(2), Volume.liters(2000));
                plusTest.run(Volume.usPints(-3), Volume.zero, Volume.usPints(-3));
                plusTest.run(Volume.usQuarts(-3), Volume.usGallons(-1), Volume.usQuarts(-7));
                plusTest.run(Volume.usCups(-3), Volume.usQuarts(5), Volume.usCups(17));
                plusTest.run(Volume.liters(4), Volume.zero, Volume.liters(4));
                plusTest.run(Volume.milliliters(4), Volume.liters(-11), Volume.milliliters(-10996));
                plusTest.run(Volume.kiloliters(4), Volume.milliliters(5), Volume.kiloliters(4.000005));
            });

            runner.testGroup("times(double)", () ->
            {
                final Action3<Volume,Double,Volume> timesTest = (Volume volume, Double value, Volume expected) ->
                {
                    runner.test("with " + volume + " and " + value, (Test test) ->
                    {
                        final Volume product = volume.times(value);
                        test.assertNotNull(product);
                        test.assertEqual(expected.getValue(), product.getValue());
                        test.assertEqual(expected.getUnits(), product.getUnits());
                    });
                };

                timesTest.run(Volume.milliliters(1), -1.0, Volume.milliliters(-1));
                timesTest.run(Volume.liters(2), 0.0, Volume.zero);
                timesTest.run(Volume.liters(3), 10.0, Volume.liters(30));
                timesTest.run(Volume.zero, 49.0, Volume.zero);
                timesTest.run(Volume.usFluidOunces(0), -13.0, Volume.usFluidOunces(0));
            });

            runner.testGroup("dividedBy(double)", () ->
            {
                runner.test("with 1.0 Liters and 0.0", (Test test) ->
                {
                    test.assertThrows(() -> Volume.liters(1).dividedBy(0),
                        new PreConditionFailure("value (0.0) must not be 0.0."));
                });

                final Action3<Volume,Double,Volume> dividedByTest = (Volume volume, Double value, Volume expected) ->
                {
                    runner.test("with " + volume + " and " + value, (Test test) ->
                    {
                        final Volume quotient = volume.dividedBy(value);
                        test.assertNotNull(quotient);
                        test.assertEqual(expected.getValue(), quotient.getValue());
                        test.assertEqual(expected.getUnits(), quotient.getUnits());
                    });
                };

                dividedByTest.run(Volume.milliliters(1), -1.0, Volume.milliliters(-1));
                dividedByTest.run(Volume.liters(3), 10.0, Volume.liters(0.3));
                dividedByTest.run(Volume.zero, 49.0, Volume.zero);
                dividedByTest.run(Volume.usFluidOunces(0), -13.0, Volume.usFluidOunces(0));
            });
        });
    }
}
