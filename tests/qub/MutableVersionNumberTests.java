package qub;

public interface MutableVersionNumberTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(MutableVersionNumber.class, () ->
        {
            runner.test("create()", (Test test) ->
            {
                final MutableVersionNumber version = MutableVersionNumber.create();
                test.assertNotNull(version);
                test.assertEqual(Iterable.create(), version.getParts());
                test.assertEqual(0, version.getPartCount());
                test.assertEqual("", version.getSuffix());
            });

            runner.testGroup("parse(String)", () ->
            {
                final Action2<String,Throwable> parseErrorTest = (String text, Throwable expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(text), (Test test) ->
                    {
                        test.assertThrows(() -> MutableVersionNumber.parse(text).await(),
                            expected);
                    });
                };

                parseErrorTest.run(null, new PreConditionFailure("text cannot be null."));

                final Action2<String,MutableVersionNumber> parseTest = (String text, MutableVersionNumber expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(text), (Test test) ->
                    {
                        test.assertEqual(expected, MutableVersionNumber.parse(text).await());
                    });
                };

                parseTest.run("", MutableVersionNumber.create());
                parseTest.run(".", MutableVersionNumber.create().setSuffix("."));
                parseTest.run("1", MutableVersionNumber.create().setMajor(1));
                parseTest.run("12", MutableVersionNumber.create().setMajor(12));
                parseTest.run("123", MutableVersionNumber.create().setMajor(123));
                parseTest.run("1.", MutableVersionNumber.create().setMajor(1).setSuffix("."));
                parseTest.run("1.2", MutableVersionNumber.create().setMajor(1).setMinor(2));
                parseTest.run("1.2.", MutableVersionNumber.create().setMajor(1).setMinor(2).setSuffix("."));
                parseTest.run("1.2.3", MutableVersionNumber.create().setMajor(1).setMinor(2).setPatch(3));
                parseTest.run("1.2.3.", MutableVersionNumber.create().setMajor(1).setMinor(2).setPatch(3).setSuffix("."));
                parseTest.run("1.2.3.4", MutableVersionNumber.create().setMajor(1).setMinor(2).setPatch(3).addPart(4));
                parseTest.run("1..2", MutableVersionNumber.create().setMajor(1).setSuffix("..2"));
                parseTest.run("1.2.3beta", MutableVersionNumber.create().setMajor(1).setMinor(2).setPatch(3).setSuffix("beta"));
                parseTest.run("1.2.3-beta", MutableVersionNumber.create().setMajor(1).setMinor(2).setPatch(3).setSuffix("-beta"));
                parseTest.run("1.2.3.beta", MutableVersionNumber.create().setMajor(1).setMinor(2).setPatch(3).setSuffix(".beta"));
                parseTest.run("hello", MutableVersionNumber.create().setSuffix("hello"));
            });

            runner.testGroup("any()", () ->
            {
                final Action2<MutableVersionNumber,Boolean> anyTest = (MutableVersionNumber version, Boolean expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(version.toString()), (Test test) ->
                    {
                        test.assertEqual(expected, version.any());
                    });
                };

                anyTest.run(MutableVersionNumber.create(), false);
                anyTest.run(MutableVersionNumber.create().setMajor(1), true);
                anyTest.run(MutableVersionNumber.create().setSuffix("a"), true);
            });

            runner.testGroup("addPart(int)", () ->
            {
                final Action3<MutableVersionNumber,Integer,Throwable> addPartErrorTest = (MutableVersionNumber version, Integer part, Throwable expected) ->
                {
                    runner.test("with " + English.andList(Strings.escapeAndQuote(version.toString()), part), (Test test) ->
                    {
                        final MutableVersionNumber backup = version.clone();
                        test.assertThrows(() -> version.addPart(part.intValue()),
                            expected);
                        test.assertEqual(backup, version);
                    });
                };

                addPartErrorTest.run(MutableVersionNumber.create(), -1, new PreConditionFailure("part (-1) must be greater than or equal to 0."));

                final Action3<MutableVersionNumber,Integer,MutableVersionNumber> addPartTest = (MutableVersionNumber version, Integer part, MutableVersionNumber expected) ->
                {
                    runner.test("with " + English.andList(Strings.escapeAndQuote(version.toString()), part), (Test test) ->
                    {
                        final MutableVersionNumber addPartResult = version.addPart(part.intValue());
                        test.assertSame(version, addPartResult);
                        test.assertEqual(expected, version);
                    });
                };

                addPartTest.run(MutableVersionNumber.create(), 1, MutableVersionNumber.create().setMajor(1));
                addPartTest.run(MutableVersionNumber.create().setMajor(1), 2, MutableVersionNumber.create().setMajor(1).setMinor(2));
                addPartTest.run(MutableVersionNumber.create().setMajor(1).setMinor(2), 3, MutableVersionNumber.create().setMajor(1).setMinor(2).setPatch(3));
                addPartTest.run(MutableVersionNumber.create().setSuffix("-beta"), 1, MutableVersionNumber.create().setMajor(1).setSuffix("-beta"));
            });

            runner.testGroup("addPart(Integer)", () ->
            {
                final Action3<MutableVersionNumber,Integer,Throwable> addPartErrorTest = (MutableVersionNumber version, Integer part, Throwable expected) ->
                {
                    runner.test("with " + English.andList(Strings.escapeAndQuote(version.toString()), part), (Test test) ->
                    {
                        final MutableVersionNumber backup = version.clone();
                        test.assertThrows(() -> version.addPart(part),
                            expected);
                        test.assertEqual(backup, version);
                    });
                };

                addPartErrorTest.run(MutableVersionNumber.create(), -1, new PreConditionFailure("part (-1) must be greater than or equal to 0."));

                final Action3<MutableVersionNumber,Integer,MutableVersionNumber> addPartTest = (MutableVersionNumber version, Integer part, MutableVersionNumber expected) ->
                {
                    runner.test("with " + English.andList(Strings.escapeAndQuote(version.toString()), part), (Test test) ->
                    {
                        final MutableVersionNumber addPartResult = version.addPart(part);
                        test.assertSame(version, addPartResult);
                        test.assertEqual(expected, version);
                    });
                };

                addPartTest.run(MutableVersionNumber.create(), 1, MutableVersionNumber.create().setMajor(1));
                addPartTest.run(MutableVersionNumber.create().setMajor(1), 2, MutableVersionNumber.create().setMajor(1).setMinor(2));
                addPartTest.run(MutableVersionNumber.create().setMajor(1).setMinor(2), 3, MutableVersionNumber.create().setMajor(1).setMinor(2).setPatch(3));
                addPartTest.run(MutableVersionNumber.create().setSuffix("-beta"), 1, MutableVersionNumber.create().setMajor(1).setSuffix("-beta"));
            });

            runner.testGroup("addParts(int...)", () ->
            {
                final Action3<MutableVersionNumber,int[],Throwable> addPartsErrorTest = (MutableVersionNumber version, int[] parts, Throwable expected) ->
                {
                    runner.test("with " + English.andList(Strings.escapeAndQuote(version), parts == null ? "null" : IntegerArray.create(parts).toString()), (Test test) ->
                    {
                        test.assertThrows(() -> version.addParts(parts), expected);
                    });
                };

                addPartsErrorTest.run(MutableVersionNumber.create(), null, new PreConditionFailure("parts cannot be null."));
                addPartsErrorTest.run(MutableVersionNumber.create(), new int[] { -1 }, new PreConditionFailure("part (-1) must be greater than or equal to 0."));

                final Action3<MutableVersionNumber,int[],MutableVersionNumber> addPartsTest = (MutableVersionNumber version, int[] parts, MutableVersionNumber expected) ->
                {
                    runner.test("with " + English.andList(Strings.escapeAndQuote(version), parts == null ? "null" : IntegerArray.create(parts).toString()), (Test test) ->
                    {
                        final MutableVersionNumber addPartsResult = version.addParts(parts);
                        test.assertSame(version, addPartsResult);
                        test.assertEqual(expected, version);
                    });
                };

                addPartsTest.run(MutableVersionNumber.create(), new int[0], MutableVersionNumber.create());
                addPartsTest.run(MutableVersionNumber.create(), new int[] { 1 }, MutableVersionNumber.create().setMajor(1));
                addPartsTest.run(MutableVersionNumber.create(), new int[] { 1, 2 }, MutableVersionNumber.create().setMajor(1).setMinor(2));
                addPartsTest.run(MutableVersionNumber.create().setMajor(1), new int[] { 2, 3, 4 }, MutableVersionNumber.create().setMajor(1).setMinor(2).setPatch(3).addPart(4));
            });

            runner.testGroup("addParts(Iterable<Integer>)", () ->
            {
                final Action3<MutableVersionNumber,Iterable<Integer>,Throwable> addPartsErrorTest = (MutableVersionNumber version, Iterable<Integer> parts, Throwable expected) ->
                {
                    runner.test("with " + English.andList(Strings.escapeAndQuote(version), parts == null ? "null" : parts.toString()), (Test test) ->
                    {
                        test.assertThrows(() -> version.addParts(parts), expected);
                    });
                };

                addPartsErrorTest.run(MutableVersionNumber.create(), null, new PreConditionFailure("parts cannot be null."));
                addPartsErrorTest.run(MutableVersionNumber.create(), Iterable.create(-1), new PreConditionFailure("part (-1) must be greater than or equal to 0."));

                final Action3<MutableVersionNumber,Iterable<Integer>,MutableVersionNumber> addPartsTest = (MutableVersionNumber version, Iterable<Integer> parts, MutableVersionNumber expected) ->
                {
                    runner.test("with " + English.andList(Strings.escapeAndQuote(version), parts == null ? "null" : parts.toString()), (Test test) ->
                    {
                        final MutableVersionNumber addPartsResult = version.addParts(parts);
                        test.assertSame(version, addPartsResult);
                        test.assertEqual(expected, version);
                    });
                };

                addPartsTest.run(MutableVersionNumber.create(), Iterable.create(), MutableVersionNumber.create());
                addPartsTest.run(MutableVersionNumber.create(), Iterable.create(1), MutableVersionNumber.create().setMajor(1));
                addPartsTest.run(MutableVersionNumber.create(), Iterable.create(1, 2), MutableVersionNumber.create().setMajor(1).setMinor(2));
                addPartsTest.run(MutableVersionNumber.create().setMajor(1), Iterable.create(2, 3, 4), MutableVersionNumber.create().setMajor(1).setMinor(2).setPatch(3).addPart(4));
            });

            runner.testGroup("setPart(int,int)", () ->
            {
                final Action4<MutableVersionNumber,Integer,Integer,Throwable> setPartErrorTest = (MutableVersionNumber version, Integer partIndex, Integer part, Throwable expected) ->
                {
                    runner.test("with " + English.andList(Strings.escapeAndQuote(version), partIndex.toString(), part.toString()), (Test test) ->
                    {
                        final MutableVersionNumber backup = version.clone();
                        test.assertThrows(() -> version.setPart(partIndex, part), expected);
                        test.assertEqual(backup, version);
                    });
                };

                setPartErrorTest.run(MutableVersionNumber.create(), -1, 0, new PreConditionFailure("partIndex (-1) must be equal to 0."));
                setPartErrorTest.run(MutableVersionNumber.create().setMajor(1), 0, -2, new PreConditionFailure("part (-2) must be greater than or equal to 0."));


                final Action4<MutableVersionNumber,Integer,Integer,MutableVersionNumber> setPartTest = (MutableVersionNumber version, Integer partIndex, Integer part, MutableVersionNumber expected) ->
                {
                    runner.test("with " + English.andList(Strings.escapeAndQuote(version), partIndex.toString(), part.toString()), (Test test) ->
                    {
                        final MutableVersionNumber setPartResult = version.setPart(partIndex, part);
                        test.assertSame(version, setPartResult);
                        test.assertEqual(expected, version);
                    });
                };

                setPartTest.run(MutableVersionNumber.create(), 0, 0, MutableVersionNumber.create().setMajor(0));
                setPartTest.run(MutableVersionNumber.create().setMajor(1), 0, 2, MutableVersionNumber.create().setMajor(2));
                setPartTest.run(MutableVersionNumber.create().setMajor(1), 1, 2, MutableVersionNumber.create().addParts(1, 2));
            });

            runner.testGroup("hasPart(int)", () ->
            {
                final Action3<MutableVersionNumber,Integer,Throwable> hasPartErrorTest = (MutableVersionNumber version, Integer partIndex, Throwable expected) ->
                {
                    runner.test("with " + English.andList(Strings.escapeAndQuote(version), partIndex.toString()), (Test test) ->
                    {
                        test.assertThrows(() -> version.hasPart(partIndex), expected);
                    });
                };

                hasPartErrorTest.run(MutableVersionNumber.create(), -1, new PreConditionFailure("partIndex (-1) must be greater than or equal to 0."));

                final Action3<MutableVersionNumber,Integer,Boolean> hasPartTest = (MutableVersionNumber version, Integer partIndex, Boolean expected) ->
                {
                    runner.test("with " + English.andList(Strings.escapeAndQuote(version), partIndex.toString()), (Test test) ->
                    {
                        test.assertEqual(expected, version.hasPart(partIndex));
                    });
                };

                hasPartTest.run(MutableVersionNumber.create(), 0, false);
                hasPartTest.run(MutableVersionNumber.create(), 1, false);
                hasPartTest.run(MutableVersionNumber.create(), 2, false);
                hasPartTest.run(MutableVersionNumber.create().setMajor(1), 0, true);
                hasPartTest.run(MutableVersionNumber.create().setMajor(1), 1, false);
                hasPartTest.run(MutableVersionNumber.create().addParts(1, 2, 3), 0, true);
                hasPartTest.run(MutableVersionNumber.create().addParts(1, 2, 3), 1, true);
                hasPartTest.run(MutableVersionNumber.create().addParts(1, 2, 3), 2, true);
                hasPartTest.run(MutableVersionNumber.create().addParts(1, 2, 3), 3, false);
            });

            runner.testGroup("getPart(int)", () ->
            {
                final Action3<MutableVersionNumber,Integer,Throwable> getPartErrorTest = (MutableVersionNumber version, Integer partIndex, Throwable expected) ->
                {
                    runner.test("with " + English.andList(Strings.escapeAndQuote(version), partIndex.toString()), (Test test) ->
                    {
                        test.assertThrows(() -> version.getPart(partIndex), expected);
                    });
                };

                getPartErrorTest.run(MutableVersionNumber.create(), -1, new PreConditionFailure("partIndex (-1) must be greater than or equal to 0."));
                getPartErrorTest.run(MutableVersionNumber.create(), 0, new PreConditionFailure("this.hasPart(partIndex) cannot be false."));
                getPartErrorTest.run(MutableVersionNumber.create(), 1, new PreConditionFailure("this.hasPart(partIndex) cannot be false."));

                final Action3<MutableVersionNumber,Integer,Integer> getPartTest = (MutableVersionNumber version, Integer partIndex, Integer expected) ->
                {
                    runner.test("with " + English.andList(Strings.escapeAndQuote(version), partIndex.toString()), (Test test) ->
                    {
                        test.assertEqual(expected, version.getPart(partIndex));
                    });
                };

                getPartTest.run(MutableVersionNumber.create().addParts(1), 0, 1);
                getPartTest.run(MutableVersionNumber.create().addParts(1, 2), 0, 1);
                getPartTest.run(MutableVersionNumber.create().addParts(1, 2), 1, 2);
            });

            runner.testGroup("setMajor(int)", () ->
            {
                final Action3<MutableVersionNumber,Integer,Throwable> setMajorErrorTest = (MutableVersionNumber version, Integer major, Throwable expected) ->
                {
                    runner.test("with " + English.andList(Strings.escapeAndQuote(version), major.toString()), (Test test) ->
                    {
                        final MutableVersionNumber backup = version.clone();
                        test.assertThrows(() -> version.setMajor(major), expected);
                        test.assertEqual(backup, version);
                    });
                };

                setMajorErrorTest.run(MutableVersionNumber.create(), -1, new PreConditionFailure("major (-1) must be greater than or equal to 0."));

                final Action3<MutableVersionNumber,Integer,MutableVersionNumber> setMajorTest = (MutableVersionNumber version, Integer major, MutableVersionNumber expected) ->
                {
                    runner.test("with " + English.andList(Strings.escapeAndQuote(version), major.toString()), (Test test) ->
                    {
                        final MutableVersionNumber setMajorResult = version.setMajor(major);
                        test.assertSame(version, setMajorResult);
                        test.assertEqual(expected, version);
                    });
                };

                setMajorTest.run(MutableVersionNumber.create(), 0, MutableVersionNumber.create().addPart(0));
                setMajorTest.run(MutableVersionNumber.create(), 1, MutableVersionNumber.create().addPart(1));
                setMajorTest.run(MutableVersionNumber.create().addParts(1, 2), 0, MutableVersionNumber.create().addParts(0, 2));
            });

            runner.testGroup("hasMajor()", () ->
            {
                final Action2<MutableVersionNumber,Boolean> hasMajorTest = (MutableVersionNumber version, Boolean expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(version), (Test test) ->
                    {
                        test.assertEqual(expected, version.hasMajor());
                    });
                };

                hasMajorTest.run(MutableVersionNumber.create(), false);
                hasMajorTest.run(MutableVersionNumber.create().setSuffix("hello"), false);
                hasMajorTest.run(MutableVersionNumber.create().addPart(1), true);
                hasMajorTest.run(MutableVersionNumber.create().setMajor(2), true);
            });

            runner.testGroup("getMajor()", () ->
            {
                final Action2<MutableVersionNumber,Throwable> getMajorErrorTest = (MutableVersionNumber version, Throwable expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(version), (Test test) ->
                    {
                        test.assertThrows(version::getMajor, expected);
                    });
                };

                getMajorErrorTest.run(MutableVersionNumber.create(), new PreConditionFailure("this.hasMajor() cannot be false."));

                final Action2<MutableVersionNumber,Integer> getMajorTest = (MutableVersionNumber version, Integer expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(version), (Test test) ->
                    {
                        test.assertEqual(expected, version.getMajor());
                    });
                };

                getMajorTest.run(MutableVersionNumber.create().setMajor(0), 0);
                getMajorTest.run(MutableVersionNumber.create().setMajor(1), 1);
                getMajorTest.run(MutableVersionNumber.create().addParts(10, 30), 10);
            });

            runner.testGroup("setMinor(int)", () ->
            {
                final Action3<MutableVersionNumber,Integer,Throwable> setMinorErrorTest = (MutableVersionNumber version, Integer major, Throwable expected) ->
                {
                    runner.test("with " + English.andList(Strings.escapeAndQuote(version), major.toString()), (Test test) ->
                    {
                        final MutableVersionNumber backup = version.clone();
                        test.assertThrows(() -> version.setMinor(major), expected);
                        test.assertEqual(backup, version);
                    });
                };

                setMinorErrorTest.run(MutableVersionNumber.create(), -1, new PreConditionFailure("minor (-1) must be greater than or equal to 0."));
                setMinorErrorTest.run(MutableVersionNumber.create(), 1, new PreConditionFailure("this.hasMajor() cannot be false."));
                setMinorErrorTest.run(MutableVersionNumber.create().setMajor(1), -1, new PreConditionFailure("minor (-1) must be greater than or equal to 0."));

                final Action3<MutableVersionNumber,Integer,MutableVersionNumber> setMinorTest = (MutableVersionNumber version, Integer minor, MutableVersionNumber expected) ->
                {
                    runner.test("with " + English.andList(Strings.escapeAndQuote(version), minor.toString()), (Test test) ->
                    {
                        final MutableVersionNumber setMinorResult = version.setMinor(minor);
                        test.assertSame(version, setMinorResult);
                        test.assertEqual(expected, version);
                    });
                };

                setMinorTest.run(MutableVersionNumber.create().addParts(1), 2, MutableVersionNumber.create().addParts(1, 2));
                setMinorTest.run(MutableVersionNumber.create().addParts(1, 2), 3, MutableVersionNumber.create().addParts(1, 3));
                setMinorTest.run(MutableVersionNumber.create().addParts(1, 2, 3), 4, MutableVersionNumber.create().addParts(1, 4, 3));
            });

            runner.testGroup("hasMinor()", () ->
            {
                final Action2<MutableVersionNumber,Boolean> hasMinorTest = (MutableVersionNumber version, Boolean expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(version), (Test test) ->
                    {
                        test.assertEqual(expected, version.hasMinor());
                    });
                };

                hasMinorTest.run(MutableVersionNumber.create(), false);
                hasMinorTest.run(MutableVersionNumber.create().setSuffix("hello"), false);
                hasMinorTest.run(MutableVersionNumber.create().addPart(1), false);
                hasMinorTest.run(MutableVersionNumber.create().setMajor(2), false);
                hasMinorTest.run(MutableVersionNumber.create().addParts(1, 2), true);
                hasMinorTest.run(MutableVersionNumber.create().setMajor(1).setMinor(2), true);
            });

            runner.testGroup("getMinor()", () ->
            {
                final Action2<MutableVersionNumber,Throwable> getMinorErrorTest = (MutableVersionNumber version, Throwable expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(version), (Test test) ->
                    {
                        test.assertThrows(version::getMinor, expected);
                    });
                };

                getMinorErrorTest.run(MutableVersionNumber.create(), new PreConditionFailure("this.hasMinor() cannot be false."));
                getMinorErrorTest.run(MutableVersionNumber.create().setMajor(1), new PreConditionFailure("this.hasMinor() cannot be false."));

                final Action2<MutableVersionNumber,Integer> getMinorTest = (MutableVersionNumber version, Integer expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(version), (Test test) ->
                    {
                        test.assertEqual(expected, version.getMinor());
                    });
                };

                getMinorTest.run(MutableVersionNumber.create().addParts(1, 2), 2);
                getMinorTest.run(MutableVersionNumber.create().setMajor(1).setMinor(5), 5);
                getMinorTest.run(MutableVersionNumber.create().addParts(10, 30, 50), 30);
            });

            runner.testGroup("setPatch(int)", () ->
            {
                final Action3<MutableVersionNumber,Integer,Throwable> setPatchErrorTest = (MutableVersionNumber version, Integer patch, Throwable expected) ->
                {
                    runner.test("with " + English.andList(Strings.escapeAndQuote(version), patch.toString()), (Test test) ->
                    {
                        final MutableVersionNumber backup = version.clone();
                        test.assertThrows(() -> version.setPatch(patch), expected);
                        test.assertEqual(backup, version);
                    });
                };

                setPatchErrorTest.run(MutableVersionNumber.create(), -1, new PreConditionFailure("patch (-1) must be greater than or equal to 0."));
                setPatchErrorTest.run(MutableVersionNumber.create(), 1, new PreConditionFailure("this.hasMinor() cannot be false."));
                setPatchErrorTest.run(MutableVersionNumber.create().addParts(1), 3, new PreConditionFailure("this.hasMinor() cannot be false."));

                final Action3<MutableVersionNumber,Integer,MutableVersionNumber> setPatchTest = (MutableVersionNumber version, Integer patch, MutableVersionNumber expected) ->
                {
                    runner.test("with " + English.andList(Strings.escapeAndQuote(version), patch.toString()), (Test test) ->
                    {
                        final MutableVersionNumber setPatchResult = version.setPatch(patch);
                        test.assertSame(version, setPatchResult);
                        test.assertEqual(expected, version);
                    });
                };

                setPatchTest.run(MutableVersionNumber.create().addParts(1, 2), 3, MutableVersionNumber.create().addParts(1, 2, 3));
                setPatchTest.run(MutableVersionNumber.create().addParts(1, 2, 3), 4, MutableVersionNumber.create().addParts(1, 2, 4));
                setPatchTest.run(MutableVersionNumber.create().addParts(1, 2, 3, 4), 5, MutableVersionNumber.create().addParts(1, 2, 5, 4));
            });

            runner.testGroup("hasPatch()", () ->
            {
                final Action2<MutableVersionNumber,Boolean> hasPatchTest = (MutableVersionNumber version, Boolean expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(version), (Test test) ->
                    {
                        test.assertEqual(expected, version.hasPatch());
                    });
                };

                hasPatchTest.run(MutableVersionNumber.create(), false);
                hasPatchTest.run(MutableVersionNumber.create().setSuffix("hello"), false);
                hasPatchTest.run(MutableVersionNumber.create().addPart(1), false);
                hasPatchTest.run(MutableVersionNumber.create().setMajor(2), false);
                hasPatchTest.run(MutableVersionNumber.create().addParts(1, 2), false);
                hasPatchTest.run(MutableVersionNumber.create().setMajor(1).setMinor(2), false);
                hasPatchTest.run(MutableVersionNumber.create().addParts(1, 2, 3), true);
                hasPatchTest.run(MutableVersionNumber.create().setMajor(1).setMinor(2).setPatch(3), true);
            });

            runner.testGroup("getPatch()", () ->
            {
                final Action2<MutableVersionNumber,Throwable> getPatchErrorTest = (MutableVersionNumber version, Throwable expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(version), (Test test) ->
                    {
                        test.assertThrows(version::getPatch, expected);
                    });
                };

                getPatchErrorTest.run(MutableVersionNumber.create(), new PreConditionFailure("this.hasPatch() cannot be false."));
                getPatchErrorTest.run(MutableVersionNumber.create().setMajor(1), new PreConditionFailure("this.hasPatch() cannot be false."));
                getPatchErrorTest.run(MutableVersionNumber.create().setMajor(1).setMinor(2), new PreConditionFailure("this.hasPatch() cannot be false."));

                final Action2<MutableVersionNumber,Integer> getPatchTest = (MutableVersionNumber version, Integer expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(version), (Test test) ->
                    {
                        test.assertEqual(expected, version.getPatch());
                    });
                };

                getPatchTest.run(MutableVersionNumber.create().addParts(1, 2, 3), 3);
                getPatchTest.run(MutableVersionNumber.create().setMajor(1).setMinor(5).setPatch(10), 10);
                getPatchTest.run(MutableVersionNumber.create().addParts(10, 30, 50, 70, 90), 50);
            });

            runner.testGroup("setSuffix(String)", () ->
            {
                final Action3<MutableVersionNumber,String,Throwable> setSuffixErrorTest = (MutableVersionNumber version, String suffix, Throwable expected) ->
                {
                    runner.test("with " + English.andList(Strings.escapeAndQuote(version), Strings.escapeAndQuote(suffix)), (Test test) ->
                    {
                        final MutableVersionNumber backup = version.clone();
                        test.assertThrows(() -> version.setSuffix(suffix), expected);
                        test.assertEqual(backup, version);
                    });
                };

                setSuffixErrorTest.run(MutableVersionNumber.create(), null, new PreConditionFailure("suffix cannot be null."));

                final Action2<MutableVersionNumber,String> setSuffixTest = (MutableVersionNumber version, String suffix) ->
                {
                    runner.test("with " + English.andList(Strings.escapeAndQuote(version), Strings.escapeAndQuote(suffix)), (Test test) ->
                    {
                        final MutableVersionNumber setSuffixResult = version.setSuffix(suffix);
                        test.assertSame(version, setSuffixResult);
                        test.assertEqual(suffix, version.getSuffix());
                    });
                };

                setSuffixTest.run(MutableVersionNumber.create(), "");
                setSuffixTest.run(MutableVersionNumber.create(), "hello");
                setSuffixTest.run(MutableVersionNumber.create(), "-beta");
                setSuffixTest.run(MutableVersionNumber.create(), ".preview");
            });

            runner.testGroup("hasSuffix()", () ->
            {
                final Action2<MutableVersionNumber,Boolean> hasSuffixTest = (MutableVersionNumber version, Boolean expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(version), (Test test) ->
                    {
                        test.assertEqual(expected, version.hasSuffix());
                    });
                };

                hasSuffixTest.run(MutableVersionNumber.create(), false);
                hasSuffixTest.run(MutableVersionNumber.create().setSuffix(""), false);
                hasSuffixTest.run(MutableVersionNumber.create().setSuffix("hello"), true);
            });

            runner.testGroup("equals(Object)", () ->
            {
                final Action3<MutableVersionNumber,Object,Boolean> equalsTest = (MutableVersionNumber lhs, Object rhs, Boolean expected) ->
                {
                    runner.test("with " + English.andList(Iterable.create(lhs, rhs).map(Strings::escapeAndQuote)), (Test test) ->
                    {
                        test.assertEqual(expected, lhs.equals(rhs));
                    });
                };

                equalsTest.run(
                    MutableVersionNumber.create(),
                    null,
                    false);
                equalsTest.run(
                    MutableVersionNumber.create(),
                    "hello",
                    false);
                equalsTest.run(
                    MutableVersionNumber.create(),
                    MutableVersionNumber.create(),
                    true);
                equalsTest.run(
                    MutableVersionNumber.create(),
                    MutableVersionNumber.create()
                        .addParts(1),
                    false);
                equalsTest.run(
                    MutableVersionNumber.create()
                        .addParts(1),
                    MutableVersionNumber.create()
                        .addParts(1),
                    true);
                equalsTest.run(
                    MutableVersionNumber.create()
                        .addParts(1),
                    MutableVersionNumber.create()
                        .addParts(2),
                    false);
                equalsTest.run(
                    MutableVersionNumber.create()
                        .addParts(1, 2, 3, 4),
                    MutableVersionNumber.create()
                        .addParts(1, 2, 3, 4),
                    true);
                equalsTest.run(
                    MutableVersionNumber.create()
                        .addParts(1, 2, 3, 4),
                    MutableVersionNumber.create()
                        .addParts(1, 2, 3, 5),
                    false);
                equalsTest.run(
                    MutableVersionNumber.create()
                        .addParts(1)
                        .setSuffix("alpha"),
                    MutableVersionNumber.create()
                        .addParts(1),
                    false);
                equalsTest.run(
                    MutableVersionNumber.create()
                        .addParts(1),
                    MutableVersionNumber.create()
                        .addParts(1)
                        .setSuffix("beta"),
                    false);
                equalsTest.run(
                    MutableVersionNumber.create()
                        .addParts(1)
                        .setSuffix("alpha"),
                    MutableVersionNumber.create()
                        .addParts(1)
                        .setSuffix("beta"),
                    false);
                equalsTest.run(
                    MutableVersionNumber.create()
                        .addParts(1)
                        .setSuffix("alpha"),
                    MutableVersionNumber.create()
                        .addParts(1)
                        .setSuffix("alpha"),
                    true);
                equalsTest.run(
                    MutableVersionNumber.create()
                        .addParts(1)
                        .setSuffix("ALPHA"),
                    MutableVersionNumber.create()
                        .addParts(1)
                        .setSuffix("alpha"),
                    false);
            });

            runner.testGroup("equals(MutableVersionNumber)", () ->
            {
                final Action3<MutableVersionNumber,MutableVersionNumber,Boolean> equalsTest = (MutableVersionNumber lhs, MutableVersionNumber rhs, Boolean expected) ->
                {
                    runner.test("with " + English.andList(Iterable.create(lhs, rhs).map(Strings::escapeAndQuote)), (Test test) ->
                    {
                        test.assertEqual(expected, lhs.equals(rhs));
                    });
                };

                equalsTest.run(
                    MutableVersionNumber.create(),
                    null,
                    false);
                equalsTest.run(
                    MutableVersionNumber.create(),
                    MutableVersionNumber.create(),
                    true);
                equalsTest.run(
                    MutableVersionNumber.create(),
                    MutableVersionNumber.create()
                        .addParts(1),
                    false);
                equalsTest.run(
                    MutableVersionNumber.create()
                        .addParts(1),
                    MutableVersionNumber.create()
                        .addParts(1),
                    true);
                equalsTest.run(
                    MutableVersionNumber.create()
                        .addParts(1),
                    MutableVersionNumber.create()
                        .addParts(2),
                    false);
                equalsTest.run(
                    MutableVersionNumber.create()
                        .addParts(1, 2, 3, 4),
                    MutableVersionNumber.create()
                        .addParts(1, 2, 3, 4),
                    true);
                equalsTest.run(
                    MutableVersionNumber.create()
                        .addParts(1, 2, 3, 4),
                    MutableVersionNumber.create()
                        .addParts(1, 2, 3, 5),
                    false);
                equalsTest.run(
                    MutableVersionNumber.create()
                        .addParts(1)
                        .setSuffix("alpha"),
                    MutableVersionNumber.create()
                        .addParts(1),
                    false);
                equalsTest.run(
                    MutableVersionNumber.create()
                        .addParts(1),
                    MutableVersionNumber.create()
                        .addParts(1)
                        .setSuffix("beta"),
                    false);
                equalsTest.run(
                    MutableVersionNumber.create()
                        .addParts(1)
                        .setSuffix("alpha"),
                    MutableVersionNumber.create()
                        .addParts(1)
                        .setSuffix("beta"),
                    false);
                equalsTest.run(
                    MutableVersionNumber.create()
                        .addParts(1)
                        .setSuffix("alpha"),
                    MutableVersionNumber.create()
                        .addParts(1)
                        .setSuffix("alpha"),
                    true);
                equalsTest.run(
                    MutableVersionNumber.create()
                        .addParts(1)
                        .setSuffix("ALPHA"),
                    MutableVersionNumber.create()
                        .addParts(1)
                        .setSuffix("alpha"),
                    false);
            });

            runner.testGroup("compareTo(MutableVersionNumber)", () ->
            {
                final Action3<MutableVersionNumber,MutableVersionNumber,Comparison> compareToTest = (MutableVersionNumber lhs, MutableVersionNumber rhs, Comparison expected) ->
                {
                    runner.test("with " + English.andList(Iterable.create(lhs, rhs).map(Strings::escapeAndQuote)), (Test test) ->
                    {
                        test.assertEqual(expected, lhs.compareWith(rhs));
                    });
                };

                compareToTest.run(
                    MutableVersionNumber.create(),
                    null,
                    Comparison.GreaterThan);
                compareToTest.run(
                    MutableVersionNumber.create(),
                    MutableVersionNumber.create(),
                    Comparison.Equal);
                compareToTest.run(
                    MutableVersionNumber.create(),
                    MutableVersionNumber.create()
                        .addParts(1),
                    Comparison.LessThan);
                compareToTest.run(
                    MutableVersionNumber.create(),
                    MutableVersionNumber.create()
                        .setSuffix("hello"),
                    Comparison.LessThan);
                compareToTest.run(
                    MutableVersionNumber.create()
                        .addParts(1),
                    MutableVersionNumber.create(),
                    Comparison.GreaterThan);
                compareToTest.run(
                    MutableVersionNumber.create()
                        .addParts(1, 2),
                    MutableVersionNumber.create(),
                    Comparison.GreaterThan);
                compareToTest.run(
                    MutableVersionNumber.create()
                        .addParts(1),
                    MutableVersionNumber.create()
                        .addParts(1),
                    Comparison.Equal);
                compareToTest.run(
                    MutableVersionNumber.create()
                        .addParts(1),
                    MutableVersionNumber.create()
                        .addParts(2),
                    Comparison.LessThan);
                compareToTest.run(
                    MutableVersionNumber.create()
                        .addParts(2),
                    MutableVersionNumber.create()
                        .addParts(1),
                    Comparison.GreaterThan);
                compareToTest.run(
                    MutableVersionNumber.create()
                        .addParts(1, 2, 3),
                    MutableVersionNumber.create()
                        .addParts(1, 2),
                    Comparison.GreaterThan);
                compareToTest.run(
                    MutableVersionNumber.create()
                        .addParts(1, 2),
                    MutableVersionNumber.create()
                        .addParts(1, 2, 3),
                    Comparison.LessThan);
                compareToTest.run(
                    MutableVersionNumber.create()
                        .addParts(1, 2)
                        .setSuffix(""),
                    MutableVersionNumber.create()
                        .addParts(1, 2)
                        .setSuffix(""),
                    Comparison.Equal);
                compareToTest.run(
                    MutableVersionNumber.create()
                        .addParts(1, 2)
                        .setSuffix("alpha"),
                    MutableVersionNumber.create()
                        .addParts(1, 2)
                        .setSuffix(""),
                    Comparison.GreaterThan);
                compareToTest.run(
                    MutableVersionNumber.create()
                        .addParts(1, 2)
                        .setSuffix(""),
                    MutableVersionNumber.create()
                        .addParts(1, 2)
                        .setSuffix("beta"),
                    Comparison.LessThan);
                compareToTest.run(
                    MutableVersionNumber.create()
                        .addParts(1, 2)
                        .setSuffix("alpha"),
                    MutableVersionNumber.create()
                        .addParts(1, 2)
                        .setSuffix("beta"),
                    Comparison.LessThan);
            });
        });
    }
}
