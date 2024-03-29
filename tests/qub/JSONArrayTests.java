package qub;

public interface JSONArrayTests
{
    public static void test(TestRunner runner)
    {
        PreCondition.assertNotNull(runner, "runner");

        runner.testGroup(JSONArray.class, () ->
        {
            runner.testGroup("create(JSONSegment...)", () ->
            {
                runner.test("with no arguments", (Test test) ->
                {
                    final JSONArray array = JSONArray.create();
                    test.assertEqual(Iterable.create(), array);
                });

                runner.test("with non-empty arguments", (Test test) ->
                {
                    final JSONArray array = JSONArray.create(JSONBoolean.trueSegment, JSONBoolean.falseSegment);
                    test.assertEqual(Iterable.create(JSONBoolean.trueSegment, JSONBoolean.falseSegment), array);
                });

                runner.test("with null array", (Test test) ->
                {
                    test.assertThrows(() -> JSONArray.create((JSONSegment[])null),
                        new PreConditionFailure("elements cannot be null."));
                });

                runner.test("with empty array", (Test test) ->
                {
                    final JSONArray array = JSONArray.create(new JSONSegment[0]);
                    test.assertEqual(Iterable.create(), array);
                });

                runner.test("with non-empty array", (Test test) ->
                {
                    final JSONArray array = JSONArray.create(new JSONSegment[] { JSONBoolean.trueSegment, JSONBoolean.falseSegment });
                    test.assertEqual(Iterable.create(JSONBoolean.trueSegment, JSONBoolean.falseSegment), array);
                });
            });

            runner.testGroup("create(JSONSegment...)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> JSONArray.create((Indexable<JSONSegment>)null),
                        new PreConditionFailure("elements cannot be null."));
                });

                runner.test("with empty", (Test test) ->
                {
                    final JSONArray array = JSONArray.create(Indexable.create());
                    test.assertEqual(Iterable.create(), array);
                });

                runner.test("with non-empty", (Test test) ->
                {
                    final JSONArray array = JSONArray.create(Indexable.create(JSONBoolean.trueSegment, JSONBoolean.falseSegment));
                    test.assertEqual(Iterable.create(JSONBoolean.trueSegment, JSONBoolean.falseSegment), array);
                });
            });

            runner.testGroup("get(int)", () ->
            {
                final Action3<Indexable<JSONSegment>,Integer,Throwable> getErrorTest = (Indexable<JSONSegment> elements, Integer index, Throwable expectedError) ->
                {
                    runner.test("with " + elements + " and " + index, (Test test) ->
                    {
                        final JSONArray array = JSONArray.create(elements);
                        test.assertThrows(() -> array.get(index), expectedError);
                    });
                };

                getErrorTest.run(Indexable.create(), -1,
                    new PreConditionFailure("Indexable length (0) must be greater than or equal to 1."));
                getErrorTest.run(Indexable.create(), 0,
                    new PreConditionFailure("Indexable length (0) must be greater than or equal to 1."));
                getErrorTest.run(Indexable.create(), 1,
                    new PreConditionFailure("Indexable length (0) must be greater than or equal to 1."));

                getErrorTest.run(Indexable.create(JSONNull.segment, JSONBoolean.falseSegment), -1,
                    new PreConditionFailure("index (-1) must be between 0 and 1."));
                getErrorTest.run(Indexable.create(JSONNull.segment, JSONBoolean.falseSegment), 2,
                    new PreConditionFailure("index (2) must be between 0 and 1."));

                final Action3<Indexable<JSONSegment>,Integer,JSONSegment> getTest = (Indexable<JSONSegment> elements, Integer index, JSONSegment expected) ->
                {
                    runner.test("with " + elements + " and " + index, (Test test) ->
                    {
                        final JSONArray array = JSONArray.create(elements);
                        test.assertEqual(expected, array.get(index));
                    });
                };

                getTest.run(Indexable.create(JSONNull.segment, JSONBoolean.falseSegment), 0, JSONNull.segment);
                getTest.run(Indexable.create(JSONNull.segment, JSONBoolean.falseSegment), 1, JSONBoolean.falseSegment);
            });

            runner.testGroup("toString()", () ->
            {
                final Action2<Indexable<JSONSegment>,String> toStringTest = (Indexable<JSONSegment> elements, String expected) ->
                {
                    runner.test("with " + elements, (Test test) ->
                    {
                        final JSONArray array = JSONArray.create(elements);
                        test.assertEqual(expected, array.toString());
                    });
                };

                toStringTest.run(Indexable.create(), "[]");
                toStringTest.run(Indexable.create(JSONNull.segment), "[null]");
                toStringTest.run(Indexable.create(JSONBoolean.trueSegment, JSONNumber.create(50)), "[true,50]");
            });

            runner.testGroup("toString(JSONFormat)", () ->
            {
                final Action3<Indexable<JSONSegment>,JSONFormat,String> toStringTest = (Indexable<JSONSegment> elements, JSONFormat format, String expected) ->
                {
                    runner.test("with " + elements, (Test test) ->
                    {
                        final JSONArray array = JSONArray.create(elements);
                        test.assertEqual(expected, array.toString(format));
                    });
                };

                toStringTest.run(Indexable.create(), JSONFormat.consise, "[]");
                toStringTest.run(Indexable.create(JSONNull.segment), JSONFormat.consise, "[null]");
                toStringTest.run(Indexable.create(JSONBoolean.trueSegment, JSONNumber.create(50)), JSONFormat.consise, "[true,50]");
                toStringTest.run(Indexable.create(JSONObject.create()), JSONFormat.consise, "[{}]");
                toStringTest.run(Indexable.create(JSONObject.create(), JSONObject.create()), JSONFormat.consise, "[{},{}]");

                toStringTest.run(Indexable.create(), JSONFormat.pretty, "[]");
                toStringTest.run(Indexable.create(JSONNull.segment), JSONFormat.pretty, "[\n  null\n]");
                toStringTest.run(Indexable.create(JSONBoolean.trueSegment, JSONNumber.create(50)), JSONFormat.pretty, "[\n  true,\n  50\n]");
                toStringTest.run(Indexable.create(JSONObject.create()), JSONFormat.pretty, "[\n  {}\n]");
                toStringTest.run(Indexable.create(JSONObject.create(), JSONObject.create()), JSONFormat.pretty, "[\n  {},\n  {}\n]");
            });

            runner.testGroup("equals(Object)", () ->
            {
                final Action3<Indexable<JSONSegment>,Object,Boolean> equalsTest = (Indexable<JSONSegment> elements, Object rhs, Boolean expected) ->
                {
                    runner.test("with " + elements + " and " + rhs, (Test test) ->
                    {
                        final JSONArray array = JSONArray.create(elements);
                        test.assertEqual(expected, array.equals(rhs));
                    });
                };

                equalsTest.run(Indexable.create(), null, false);
                equalsTest.run(Indexable.create(), "hello", false);
                equalsTest.run(Indexable.create(), JSONArray.create(), true);
                equalsTest.run(Indexable.create(), JSONArray.create(JSONNull.segment, JSONNumber.create(50.0)), false);
                equalsTest.run(Indexable.create(JSONNull.segment, JSONNumber.create(50.0)), JSONArray.create(JSONNull.segment, JSONNumber.create(50.0)), true);
            });

            runner.testGroup("equals(JSONArray)", () ->
            {
                final Action3<Indexable<JSONSegment>,JSONArray,Boolean> equalsTest = (Indexable<JSONSegment> elements, JSONArray rhs, Boolean expected) ->
                {
                    runner.test("with " + elements + " and " + rhs, (Test test) ->
                    {
                        final JSONArray array = JSONArray.create(elements);
                        test.assertEqual(expected, array.equals(rhs));
                    });
                };

                equalsTest.run(Indexable.create(), null, false);
                equalsTest.run(Indexable.create(), JSONArray.create(), true);
                equalsTest.run(Indexable.create(), JSONArray.create(JSONNull.segment, JSONNumber.create(50.0)), false);
                equalsTest.run(Indexable.create(JSONNull.segment, JSONNumber.create(50.0)), JSONArray.create(JSONNull.segment, JSONNumber.create(50.0)), true);
            });

            runner.testGroup("insert(int,JSONSegment)", () ->
            {
                final Action4<JSONArray,Integer,JSONSegment,Throwable> insertErrorTest = (JSONArray array, Integer index, JSONSegment element, Throwable expected) ->
                {
                    runner.test("with " + English.andList(array, index, element), (Test test) ->
                    {
                        test.assertThrows(() -> array.insert(index, element), expected);
                    });
                };

                insertErrorTest.run(JSONArray.create(), -1, JSONBoolean.falseSegment, new PreConditionFailure("insertIndex (-1) must be equal to 0."));
                insertErrorTest.run(JSONArray.create(JSONBoolean.trueSegment), -1, JSONBoolean.falseSegment, new PreConditionFailure("insertIndex (-1) must be between 0 and 1."));
                insertErrorTest.run(JSONArray.create(), 1, JSONBoolean.falseSegment, new PreConditionFailure("insertIndex (1) must be equal to 0."));
                insertErrorTest.run(JSONArray.create(), 0, null, new PreConditionFailure("value cannot be null."));

                final Action4<JSONArray,Integer,JSONSegment,JSONArray> insertTest = (JSONArray array, Integer index, JSONSegment element, JSONArray expected) ->
                {
                    runner.test("with " + English.andList(array, index, element), (Test test) ->
                    {
                        final JSONArray insertResult = array.insert(index, element);
                        test.assertSame(array, insertResult);
                        test.assertEqual(expected, array);
                    });
                };

                insertTest.run(JSONArray.create(), 0, JSONBoolean.falseSegment, JSONArray.create(JSONBoolean.falseSegment));
                insertTest.run(JSONArray.create(JSONNull.segment), 0, JSONBoolean.falseSegment, JSONArray.create(JSONBoolean.falseSegment, JSONNull.segment));
                insertTest.run(JSONArray.create(JSONNull.segment), 1, JSONBoolean.falseSegment, JSONArray.create(JSONNull.segment, JSONBoolean.falseSegment));
            });

            runner.testGroup("add(JSONSegment)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final JSONArray array = JSONArray.create();
                    test.assertThrows(() -> array.add(null),
                        new PreConditionFailure("value cannot be null."));
                    test.assertEqual(JSONArray.create(), array);
                });

                final Action3<JSONArray,JSONSegment,JSONArray> addTest = (JSONArray array, JSONSegment value, JSONArray expected) ->
                {
                    runner.test("with " + English.andList(array, value), (Test test) ->
                    {
                        final JSONArray addResult = array.add(value);
                        test.assertSame(array, addResult);
                        test.assertEqual(expected, array);
                    });
                };

                addTest.run(
                    JSONArray.create(),
                    JSONNumber.create(1),
                    JSONArray.create(JSONNumber.create(1)));
                addTest.run(
                    JSONArray.create(JSONNumber.create(1)),
                    JSONNumber.create(2),
                    JSONArray.create(JSONNumber.create(1), JSONNumber.create(2)));
            });

            runner.testGroup("addString(String)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final JSONArray array = JSONArray.create();
                    test.assertThrows(() -> array.addString(null),
                        new PreConditionFailure("value cannot be null."));
                    test.assertEqual(JSONArray.create(), array);
                });
            });

            runner.testGroup("addStrings(String...)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final JSONArray array = JSONArray.create();
                    test.assertThrows(() -> array.addStrings((String[])null),
                        new PreConditionFailure("values cannot be null."));
                    test.assertEqual(JSONArray.create(), array);
                });
            });

            runner.testGroup("addStrings(Iterable<String>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final JSONArray array = JSONArray.create();
                    test.assertThrows(() -> array.addStrings((Iterable<String>)null),
                        new PreConditionFailure("values cannot be null."));
                    test.assertEqual(JSONArray.create(), array);
                });
            });

            runner.testGroup("addNumber(long)", () ->
            {
                final Action3<JSONArray,Long,JSONArray> addNumberTest = (JSONArray array, Long value, JSONArray expected) ->
                {
                    runner.test("with " + English.andList(array, value), (Test test) ->
                    {
                        final JSONArray addNumberResult = array.addNumber(value);
                        test.assertSame(array, addNumberResult);
                        test.assertEqual(expected, array);
                    });
                };

                addNumberTest.run(
                    JSONArray.create(),
                    5L,
                    JSONArray.create(JSONNumber.create(5)));
                addNumberTest.run(
                    JSONArray.create(JSONNumber.create(5)),
                    10L,
                    JSONArray.create(JSONNumber.create(5), JSONNumber.create(10)));
            });

            runner.testGroup("addNumbers(long...)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final JSONArray array = JSONArray.create();
                    test.assertThrows(() -> array.addNumbers((long[])null),
                        new PreConditionFailure("values cannot be null."));
                    test.assertEqual(JSONArray.create(), array);
                });

                runner.test("with one int value", (Test test) ->
                {
                    final JSONArray array = JSONArray.create();
                    final JSONArray addNumbersResult = array.addNumbers(1);
                    test.assertSame(array, addNumbersResult);
                    test.assertEqual(
                        JSONArray.create(JSONNumber.create(1)),
                        array);
                });

                runner.test("with two int values", (Test test) ->
                {
                    final JSONArray array = JSONArray.create().addNumber(1);
                    final JSONArray addNumbersResult = array.addNumbers(2, 3);
                    test.assertSame(array, addNumbersResult);
                    test.assertEqual(
                        JSONArray.create(
                            JSONNumber.create(1),
                            JSONNumber.create(2),
                            JSONNumber.create(3)),
                        array);
                });
            });

            runner.testGroup("addNumbers(double...)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final JSONArray array = JSONArray.create();
                    test.assertThrows(() -> array.addNumbers((double[])null),
                        new PreConditionFailure("values cannot be null."));
                    test.assertEqual(JSONArray.create(), array);
                });

                runner.test("with one float value", (Test test) ->
                {
                    final JSONArray array = JSONArray.create();
                    final JSONArray addNumbersResult = array.addNumbers(1f);
                    test.assertSame(array, addNumbersResult);
                    test.assertEqual(
                        JSONArray.create(JSONNumber.create(1.0)),
                        array);
                });

                runner.test("with two float values", (Test test) ->
                {
                    final JSONArray array = JSONArray.create().addNumber(1f);
                    final JSONArray addNumbersResult = array.addNumbers(2f, 3f);
                    test.assertSame(array, addNumbersResult);
                    test.assertEqual(
                        JSONArray.create(
                            JSONNumber.create(1.0),
                            JSONNumber.create(2.0),
                            JSONNumber.create(3.0)),
                        array);
                });
            });

            runner.testGroup("addNumbers(Iterable<? extends Number>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final JSONArray array = JSONArray.create();
                    test.assertThrows(() -> array.addNumbers((Iterable<? extends Number>)null),
                        new PreConditionFailure("values cannot be null."));
                    test.assertEqual(JSONArray.create(), array);
                });

                runner.test("with one float value", (Test test) ->
                {
                    final JSONArray array = JSONArray.create();
                    final JSONArray addNumbersResult = array.addNumbers(Iterable.create(1f));
                    test.assertSame(array, addNumbersResult);
                    test.assertEqual(
                        JSONArray.create(JSONNumber.create(1.0)),
                        array);
                });

                runner.test("with two float values", (Test test) ->
                {
                    final JSONArray array = JSONArray.create().addNumber(1f);
                    final JSONArray addNumbersResult = array.addNumbers(Iterable.create(2f, 3f));
                    test.assertSame(array, addNumbersResult);
                    test.assertEqual(
                        JSONArray.create(
                            JSONNumber.create(1.0),
                            JSONNumber.create(2.0),
                            JSONNumber.create(3.0)),
                        array);
                });

                runner.test("with one float value and one byte", (Test test) ->
                {
                    final JSONArray array = JSONArray.create().addNumber(1f);
                    final JSONArray addNumbersResult = array.addNumbers(Iterable.create(2f, (byte)3));
                    test.assertSame(array, addNumbersResult);
                    test.assertEqual(
                        JSONArray.create(
                            JSONNumber.create(1.0),
                            JSONNumber.create(2.0),
                            JSONNumber.create(3)),
                        array);
                });
            });

            runner.testGroup("removeAt(int)", () ->
            {
                final Action3<JSONArray,Integer,Throwable> removeAtErrorTest = (JSONArray array, Integer index, Throwable expected) ->
                {
                    runner.test("with " + array + " and " + index, (Test test) ->
                    {
                        test.assertThrows(() -> array.removeAt(index), expected);
                    });
                };

                removeAtErrorTest.run(JSONArray.create(), -1, new PreConditionFailure("Indexable length (0) must be greater than or equal to 1."));
                removeAtErrorTest.run(JSONArray.create(JSONBoolean.trueSegment), -1, new PreConditionFailure("index (-1) must be equal to 0."));
                removeAtErrorTest.run(JSONArray.create(), 1, new PreConditionFailure("Indexable length (0) must be greater than or equal to 1."));
                removeAtErrorTest.run(JSONArray.create(JSONBoolean.trueSegment), 2, new PreConditionFailure("index (2) must be equal to 0."));

                final Action4<JSONArray,Integer,JSONSegment,JSONArray> removeAtTest = (JSONArray array, Integer index, JSONSegment expectedResult, JSONArray expectedArray) ->
                {
                    runner.test("with " + array + " and " + index, (Test test) ->
                    {
                        test.assertEqual(expectedResult, array.removeAt(index));
                        test.assertEqual(expectedArray, array);
                    });
                };

                removeAtTest.run(JSONArray.create(JSONNull.segment), 0, JSONNull.segment, JSONArray.create());
                removeAtTest.run(JSONArray.create(JSONNull.segment, JSONBoolean.falseSegment), 0, JSONNull.segment, JSONArray.create(JSONBoolean.falseSegment));
                removeAtTest.run(JSONArray.create(JSONNull.segment, JSONBoolean.falseSegment), 1, JSONBoolean.falseSegment, JSONArray.create(JSONNull.segment));
            });

            runner.testGroup("set(int,JSONSegment)", () ->
            {
                final Action4<JSONArray,Integer,JSONSegment,Throwable> setErrorTest = (JSONArray array, Integer index, JSONSegment value, Throwable expected) ->
                {
                    runner.test("with " + array + ", " + index + ", and " + value, (Test test) ->
                    {
                        test.assertThrows(() -> array.set(index, value), expected);
                    });
                };

                setErrorTest.run(JSONArray.create(), -1, JSONNull.segment, new PreConditionFailure("Indexable length (0) must be greater than or equal to 1."));
                setErrorTest.run(JSONArray.create(JSONBoolean.trueSegment), -1, JSONNull.segment, new PreConditionFailure("index (-1) must be equal to 0."));
                setErrorTest.run(JSONArray.create(), 1, JSONNull.segment, new PreConditionFailure("Indexable length (0) must be greater than or equal to 1."));
                setErrorTest.run(JSONArray.create(JSONBoolean.trueSegment), 2, JSONNull.segment, new PreConditionFailure("index (2) must be equal to 0."));

                final Action4<JSONArray,Integer,JSONSegment,JSONArray> setTest = (JSONArray array, Integer index, JSONSegment value, JSONArray expectedArray) ->
                {
                    runner.test("with " + array + " and " + index, (Test test) ->
                    {
                        final JSONArray setResult = array.set(index, value);
                        test.assertSame(array, setResult);
                        test.assertEqual(expectedArray, array);
                    });
                };

                setTest.run(JSONArray.create(JSONNull.segment), 0, JSONBoolean.trueSegment, JSONArray.create(JSONBoolean.trueSegment));
                setTest.run(JSONArray.create(JSONNull.segment, JSONBoolean.falseSegment), 0, JSONNumber.create(5), JSONArray.create(JSONNumber.create(5), JSONBoolean.falseSegment));
                setTest.run(JSONArray.create(JSONNull.segment, JSONBoolean.falseSegment), 1, JSONString.get("hello"), JSONArray.create(JSONNull.segment, JSONString.get("hello")));
            });
        });
    }
}
