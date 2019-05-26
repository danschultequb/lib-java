package qub;

public interface BooleanValueTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(BooleanValue.class, () ->
        {
            runner.test("constructor()", (Test test) ->
            {
                final BooleanValue value = new BooleanValue();
                test.assertFalse(value.hasValue());
                test.assertThrows(value::get, new PreConditionFailure("hasValue() cannot be false."));
                test.assertThrows(value::getAsBoolean, new PreConditionFailure("hasValue() cannot be false."));
            });

            runner.testGroup("constructor(boolean)", () ->
            {
                runner.test("with false", (Test test) ->
                {
                    final BooleanValue value = new BooleanValue(false);
                    test.assertTrue(value.hasValue());
                    test.assertFalse(value.get());
                    test.assertFalse(value.getAsBoolean());
                });

                runner.test("with true", (Test test) ->
                {
                    final BooleanValue value = new BooleanValue(true);
                    test.assertTrue(value.hasValue());
                    test.assertTrue(value.get());
                    test.assertTrue(value.getAsBoolean());
                });
            });

            runner.testGroup("constructor(java.lang.Boolean)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> new BooleanValue(null), new PreConditionFailure("value cannot be null."));
                });

                runner.test("with false", (Test test) ->
                {
                    final BooleanValue value = new BooleanValue(Boolean.valueOf(false));
                    test.assertTrue(value.hasValue());
                    test.assertFalse(value.get());
                    test.assertFalse(value.getAsBoolean());
                });

                runner.test("with true", (Test test) ->
                {
                    final BooleanValue value = new BooleanValue(Boolean.valueOf(true));
                    test.assertTrue(value.hasValue());
                    test.assertTrue(value.get());
                    test.assertTrue(value.getAsBoolean());
                });
            });

            runner.test("create()", (Test test) ->
            {
                final BooleanValue value = BooleanValue.create();
                test.assertNotNull(value);
                test.assertFalse(value.hasValue());
                test.assertThrows(value::get, new PreConditionFailure("hasValue() cannot be false."));
                test.assertThrows(value::getAsBoolean, new PreConditionFailure("hasValue() cannot be false."));
            });

            runner.testGroup("create(boolean)", () ->
            {
                runner.test("with false", (Test test) ->
                {
                    final BooleanValue value = BooleanValue.create(false);
                    test.assertNotNull(value);
                    test.assertTrue(value.hasValue());
                    test.assertFalse(value.get());
                });

                runner.test("with true", (Test test) ->
                {
                    final BooleanValue value = BooleanValue.create(true);
                    test.assertNotNull(value);
                    test.assertTrue(value.hasValue());
                    test.assertTrue(value.get());
                });
            });

            runner.testGroup("create(java.lang.Boolean)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> BooleanValue.create((java.lang.Boolean)null), new PreConditionFailure("value cannot be null."));
                });

                runner.test("with false", (Test test) ->
                {
                    final BooleanValue value = BooleanValue.create(java.lang.Boolean.valueOf(false));
                    test.assertNotNull(value);
                    test.assertTrue(value.hasValue());
                    test.assertFalse(value.get());
                });

                runner.test("with true", (Test test) ->
                {
                    final BooleanValue value = BooleanValue.create(java.lang.Boolean.valueOf(true));
                    test.assertNotNull(value);
                    test.assertTrue(value.hasValue());
                    test.assertTrue(value.get());
                });
            });

            runner.testGroup("getAsBoolean()", () ->
            {
                runner.test("with no value", (Test test) ->
                {
                    final BooleanValue value = BooleanValue.create();
                    test.assertThrows(() -> value.getAsBoolean(), new PreConditionFailure("hasValue() cannot be false."));
                });

                runner.test("with false", (Test test) ->
                {
                    final BooleanValue value = BooleanValue.create(false);
                    test.assertEqual(false, value.getAsBoolean());
                });

                runner.test("with true", (Test test) ->
                {
                    final BooleanValue value = BooleanValue.create(true);
                    test.assertEqual(true, value.getAsBoolean());
                });
            });

            runner.testGroup("get()", () ->
            {
                runner.test("with no value", (Test test) ->
                {
                    final BooleanValue value = BooleanValue.create();
                    test.assertThrows(() -> value.get(), new PreConditionFailure("hasValue() cannot be false."));
                });

                runner.test("with false", (Test test) ->
                {
                    final BooleanValue value = BooleanValue.create(false);
                    test.assertEqual(false, value.get());
                });

                runner.test("with true", (Test test) ->
                {
                    final BooleanValue value = BooleanValue.create(true);
                    test.assertEqual(true, value.get());
                });
            });

            runner.testGroup("set(boolean)", () ->
            {
                runner.test("with false", (Test test) ->
                {
                    final BooleanValue value = BooleanValue.create();
                    value.set(false);
                    test.assertTrue(value.hasValue());
                    test.assertFalse(value.get());
                });

                runner.test("with true", (Test test) ->
                {
                    final BooleanValue value = BooleanValue.create();
                    value.set(true);
                    test.assertTrue(value.hasValue());
                    test.assertTrue(value.get());
                });
            });

            runner.testGroup("set(java.lang.Boolean)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final BooleanValue value = BooleanValue.create();
                    test.assertThrows(() -> value.set(null), new PreConditionFailure("value cannot be null."));
                    test.assertFalse(value.hasValue());
                });

                runner.test("with false", (Test test) ->
                {
                    final BooleanValue value = BooleanValue.create();
                    value.set(Boolean.valueOf(false));
                    test.assertTrue(value.hasValue());
                    test.assertFalse(value.get());
                });

                runner.test("with true", (Test test) ->
                {
                    final BooleanValue value = BooleanValue.create();
                    value.set(Boolean.valueOf(true));
                    test.assertTrue(value.hasValue());
                    test.assertTrue(value.get());
                });
            });

            runner.testGroup("compareAndSet(boolean,boolean)", () ->
            {
                runner.test("when false with false and false", (Test test) ->
                {
                    final BooleanValue value = BooleanValue.create(false);
                    test.assertThrows(new PreConditionFailure("newValue (false) must not be false."),
                        () -> value.compareAndSet(false, false));
                    test.assertFalse(value.get());
                });

                runner.test("when false with false and true", (Test test) ->
                {
                    final BooleanValue value = BooleanValue.create(false);
                    test.assertTrue(value.compareAndSet(false, true));
                    test.assertTrue(value.get());
                });

                runner.test("when false with true and false", (Test test) ->
                {
                    final BooleanValue value = BooleanValue.create(false);
                    test.assertFalse(value.compareAndSet(true, false));
                    test.assertFalse(value.get());
                });

                runner.test("when true with false and true", (Test test) ->
                {
                    final BooleanValue value = BooleanValue.create(true);
                    test.assertFalse(value.compareAndSet(false, true));
                    test.assertTrue(value.get());
                });

                runner.test("when true with true and false", (Test test) ->
                {
                    final BooleanValue value = BooleanValue.create(true);
                    test.assertTrue(value.compareAndSet(true, false));
                    test.assertFalse(value.get());
                });
            });

            runner.testGroup("compareAndSet(boolean,java.lang.Boolean)", () ->
            {
                runner.test("with null newValue", (Test test) ->
                {
                    final BooleanValue value = BooleanValue.create(false);
                    test.assertThrows(new PreConditionFailure("newValue cannot be null."),
                        () -> value.compareAndSet(false, null));
                    test.assertFalse(value.get());
                });

                runner.test("when false with false and false", (Test test) ->
                {
                    final BooleanValue value = BooleanValue.create(false);
                    test.assertThrows(new PreConditionFailure("newValue (false) must not be false."),
                        () -> value.compareAndSet(false, java.lang.Boolean.FALSE));
                    test.assertFalse(value.get());
                });

                runner.test("when false with false and true", (Test test) ->
                {
                    final BooleanValue value = BooleanValue.create(false);
                    test.assertTrue(value.compareAndSet(false, java.lang.Boolean.TRUE));
                    test.assertTrue(value.get());
                });

                runner.test("when false with true and false", (Test test) ->
                {
                    final BooleanValue value = BooleanValue.create(false);
                    test.assertFalse(value.compareAndSet(true, java.lang.Boolean.FALSE));
                    test.assertFalse(value.get());
                });

                runner.test("when true with false and true", (Test test) ->
                {
                    final BooleanValue value = BooleanValue.create(true);
                    test.assertFalse(value.compareAndSet(false, java.lang.Boolean.TRUE));
                    test.assertTrue(value.get());
                });

                runner.test("when true with true and false", (Test test) ->
                {
                    final BooleanValue value = BooleanValue.create(true);
                    test.assertTrue(value.compareAndSet(true, java.lang.Boolean.FALSE));
                    test.assertFalse(value.get());
                });
            });

            runner.testGroup("compareAndSet(java.lang.Boolean,boolean)", () ->
            {
                runner.test("with null expectedValue", (Test test) ->
                {
                    final BooleanValue value = BooleanValue.create(false);
                    test.assertThrows(new PreConditionFailure("expectedValue cannot be null."),
                        () -> value.compareAndSet(null, false));
                    test.assertFalse(value.get());
                });

                runner.test("when false with false and false", (Test test) ->
                {
                    final BooleanValue value = BooleanValue.create(false);
                    test.assertThrows(new PreConditionFailure("newValue (false) must not be false."),
                        () -> value.compareAndSet(java.lang.Boolean.FALSE, false));
                    test.assertFalse(value.get());
                });

                runner.test("when false with false and true", (Test test) ->
                {
                    final BooleanValue value = BooleanValue.create(false);
                    test.assertTrue(value.compareAndSet(java.lang.Boolean.FALSE, true));
                    test.assertTrue(value.get());
                });

                runner.test("when false with true and false", (Test test) ->
                {
                    final BooleanValue value = BooleanValue.create(false);
                    test.assertFalse(value.compareAndSet(java.lang.Boolean.TRUE, false));
                    test.assertFalse(value.get());
                });

                runner.test("when true with false and true", (Test test) ->
                {
                    final BooleanValue value = BooleanValue.create(true);
                    test.assertFalse(value.compareAndSet(java.lang.Boolean.FALSE, true));
                    test.assertTrue(value.get());
                });

                runner.test("when true with true and false", (Test test) ->
                {
                    final BooleanValue value = BooleanValue.create(true);
                    test.assertTrue(value.compareAndSet(java.lang.Boolean.TRUE, false));
                    test.assertFalse(value.get());
                });
            });

            runner.testGroup("compareAndSet(java.lang.Boolean,java.lang.Boolean)", () ->
            {
                runner.test("with null expectedValue", (Test test) ->
                {
                    final BooleanValue value = BooleanValue.create(false);
                    test.assertThrows(new PreConditionFailure("expectedValue cannot be null."),
                        () -> value.compareAndSet(null, java.lang.Boolean.FALSE));
                    test.assertFalse(value.get());
                });

                runner.test("with null newValue", (Test test) ->
                {
                    final BooleanValue value = BooleanValue.create(false);
                    test.assertThrows(new PreConditionFailure("newValue cannot be null."),
                        () -> value.compareAndSet(java.lang.Boolean.FALSE, null));
                    test.assertFalse(value.get());
                });

                runner.test("when false with false and false", (Test test) ->
                {
                    final BooleanValue value = BooleanValue.create(false);
                    test.assertThrows(new PreConditionFailure("newValue (false) must not be false."),
                        () -> value.compareAndSet(java.lang.Boolean.FALSE, java.lang.Boolean.FALSE));
                    test.assertFalse(value.get());
                });

                runner.test("when false with false and true", (Test test) ->
                {
                    final BooleanValue value = BooleanValue.create(false);
                    test.assertTrue(value.compareAndSet(java.lang.Boolean.FALSE, java.lang.Boolean.TRUE));
                    test.assertTrue(value.get());
                });

                runner.test("when false with true and false", (Test test) ->
                {
                    final BooleanValue value = BooleanValue.create(false);
                    test.assertFalse(value.compareAndSet(java.lang.Boolean.TRUE, java.lang.Boolean.FALSE));
                    test.assertFalse(value.get());
                });

                runner.test("when true with false and true", (Test test) ->
                {
                    final BooleanValue value = BooleanValue.create(true);
                    test.assertFalse(value.compareAndSet(java.lang.Boolean.FALSE, java.lang.Boolean.TRUE));
                    test.assertTrue(value.get());
                });

                runner.test("when true with true and false", (Test test) ->
                {
                    final BooleanValue value = BooleanValue.create(true);
                    test.assertTrue(value.compareAndSet(java.lang.Boolean.TRUE, java.lang.Boolean.FALSE));
                    test.assertFalse(value.get());
                });
            });

            runner.testGroup("clear()", () ->
            {
                runner.test("with no value", (Test test) ->
                {
                    final BooleanValue value = BooleanValue.create();
                    value.clear();
                    test.assertFalse(value.hasValue());
                });

                runner.test("with false", (Test test) ->
                {
                    final BooleanValue value = BooleanValue.create(false);
                    value.clear();
                    test.assertFalse(value.hasValue());
                });

                runner.test("with true", (Test test) ->
                {
                    final BooleanValue value = BooleanValue.create(true);
                    value.clear();
                    test.assertFalse(value.hasValue());
                });
            });

            runner.testGroup("equals(Object)", () ->
            {
                runner.testGroup("with no value", () ->
                {
                    runner.test("with null", (Test test) ->
                    {
                        final BooleanValue value = BooleanValue.create();
                        test.assertFalse(value.equals((Object)null));
                    });

                    runner.test("with " + Strings.quote("true"), (Test test) ->
                    {
                        final BooleanValue value = BooleanValue.create();
                        test.assertFalse(value.equals((Object)"true"));
                    });

                    runner.test("with false", (Test test) ->
                    {
                        final BooleanValue value = BooleanValue.create();
                        test.assertFalse(value.equals((Object)false));
                    });

                    runner.test("with true", (Test test) ->
                    {
                        final BooleanValue value = BooleanValue.create();
                        test.assertFalse(value.equals((Object)true));
                    });

                    runner.test("with BooleanValue with no value", (Test test) ->
                    {
                        final BooleanValue value = BooleanValue.create();
                        test.assertTrue(value.equals((Object)BooleanValue.create()));
                    });

                    runner.test("with BooleanValue with false", (Test test) ->
                    {
                        final BooleanValue value = BooleanValue.create();
                        test.assertFalse(value.equals((Object)BooleanValue.create(false)));
                    });

                    runner.test("with BooleanValue with true", (Test test) ->
                    {
                        final BooleanValue value = BooleanValue.create();
                        test.assertFalse(value.equals((Object)BooleanValue.create(true)));
                    });
                });

                runner.testGroup("with false", () ->
                {
                    runner.test("with null", (Test test) ->
                    {
                        final BooleanValue value = BooleanValue.create(false);
                        test.assertFalse(value.equals((Object)null));
                    });

                    runner.test("with " + Strings.quote("true"), (Test test) ->
                    {
                        final BooleanValue value = BooleanValue.create(false);
                        test.assertFalse(value.equals((Object)"true"));
                    });

                    runner.test("with false", (Test test) ->
                    {
                        final BooleanValue value = BooleanValue.create(false);
                        test.assertTrue(value.equals((Object)false));
                    });

                    runner.test("with true", (Test test) ->
                    {
                        final BooleanValue value = BooleanValue.create(false);
                        test.assertFalse(value.equals((Object)true));
                    });

                    runner.test("with BooleanValue with no value", (Test test) ->
                    {
                        final BooleanValue value = BooleanValue.create(false);
                        test.assertFalse(value.equals((Object)BooleanValue.create()));
                    });

                    runner.test("with BooleanValue with false", (Test test) ->
                    {
                        final BooleanValue value = BooleanValue.create(false);
                        test.assertTrue(value.equals((Object)BooleanValue.create(false)));
                    });

                    runner.test("with BooleanValue with true", (Test test) ->
                    {
                        final BooleanValue value = BooleanValue.create(false);
                        test.assertFalse(value.equals((Object)BooleanValue.create(true)));
                    });
                });

                runner.testGroup("with true", () ->
                {
                    runner.test("with null", (Test test) ->
                    {
                        final BooleanValue value = BooleanValue.create(true);
                        test.assertFalse(value.equals((Object)null));
                    });

                    runner.test("with " + Strings.quote("true"), (Test test) ->
                    {
                        final BooleanValue value = BooleanValue.create(true);
                        test.assertFalse(value.equals((Object)"true"));
                    });

                    runner.test("with false", (Test test) ->
                    {
                        final BooleanValue value = BooleanValue.create(true);
                        test.assertFalse(value.equals((Object)false));
                    });

                    runner.test("with true", (Test test) ->
                    {
                        final BooleanValue value = BooleanValue.create(true);
                        test.assertTrue(value.equals((Object)true));
                    });

                    runner.test("with BooleanValue with no value", (Test test) ->
                    {
                        final BooleanValue value = BooleanValue.create(true);
                        test.assertFalse(value.equals((Object)BooleanValue.create()));
                    });

                    runner.test("with BooleanValue with false", (Test test) ->
                    {
                        final BooleanValue value = BooleanValue.create(true);
                        test.assertFalse(value.equals((Object)BooleanValue.create(false)));
                    });

                    runner.test("with BooleanValue with true", (Test test) ->
                    {
                        final BooleanValue value = BooleanValue.create(true);
                        test.assertTrue(value.equals((Object)BooleanValue.create(true)));
                    });
                });
            });

            runner.testGroup("equals(boolean)", () ->
            {
                runner.testGroup("with no value", () ->
                {
                    runner.test("with false", (Test test) ->
                    {
                        final BooleanValue value = BooleanValue.create();
                        test.assertFalse(value.equals(false));
                    });

                    runner.test("with true", (Test test) ->
                    {
                        final BooleanValue value = BooleanValue.create();
                        test.assertFalse(value.equals(true));
                    });
                });

                runner.testGroup("with false", () ->
                {
                    runner.test("with false", (Test test) ->
                    {
                        final BooleanValue value = BooleanValue.create(false);
                        test.assertTrue(value.equals(false));
                    });

                    runner.test("with true", (Test test) ->
                    {
                        final BooleanValue value = BooleanValue.create(false);
                        test.assertFalse(value.equals(true));
                    });
                });

                runner.testGroup("with true", () ->
                {
                    runner.test("with false", (Test test) ->
                    {
                        final BooleanValue value = BooleanValue.create(true);
                        test.assertFalse(value.equals(false));
                    });

                    runner.test("with true", (Test test) ->
                    {
                        final BooleanValue value = BooleanValue.create(true);
                        test.assertTrue(value.equals(true));
                    });
                });
            });

            runner.testGroup("equals(java.lang.Boolean)", () ->
            {
                runner.testGroup("with no value", () ->
                {
                    runner.test("with null", (Test test) ->
                    {
                        final BooleanValue value = BooleanValue.create();
                        test.assertFalse(value.equals((java.lang.Boolean)null));
                    });

                    runner.test("with Boolean.valueOf(false)", (Test test) ->
                    {
                        final BooleanValue value = BooleanValue.create();
                        test.assertFalse(value.equals(Boolean.valueOf(false)));
                    });

                    runner.test("with Boolean.valueOf(true)", (Test test) ->
                    {
                        final BooleanValue value = BooleanValue.create();
                        test.assertFalse(value.equals(Boolean.valueOf(true)));
                    });
                });

                runner.testGroup("with false", () ->
                {
                    runner.test("with null", (Test test) ->
                    {
                        final BooleanValue value = BooleanValue.create(false);
                        test.assertFalse(value.equals((java.lang.Boolean)null));
                    });

                    runner.test("with false", (Test test) ->
                    {
                        final BooleanValue value = BooleanValue.create(false);
                        test.assertTrue(value.equals(Boolean.valueOf(false)));
                    });

                    runner.test("with true", (Test test) ->
                    {
                        final BooleanValue value = BooleanValue.create(false);
                        test.assertFalse(value.equals(Boolean.valueOf(true)));
                    });
                });

                runner.testGroup("with true", () ->
                {
                    runner.test("with null", (Test test) ->
                    {
                        final BooleanValue value = BooleanValue.create(true);
                        test.assertFalse(value.equals((java.lang.Boolean)null));
                    });

                    runner.test("with false", (Test test) ->
                    {
                        final BooleanValue value = BooleanValue.create(true);
                        test.assertFalse(value.equals(Boolean.valueOf(false)));
                    });

                    runner.test("with true", (Test test) ->
                    {
                        final BooleanValue value = BooleanValue.create(true);
                        test.assertTrue(value.equals(Boolean.valueOf(true)));
                    });
                });
            });

            runner.testGroup("equals(BooleanValue)", () ->
            {
                runner.testGroup("with no value", () ->
                {
                    runner.test("with null", (Test test) ->
                    {
                        final BooleanValue value = BooleanValue.create();
                        test.assertFalse(value.equals((BooleanValue)null));
                    });

                    runner.test("with no value", (Test test) ->
                    {
                        final BooleanValue value = BooleanValue.create();
                        test.assertTrue(value.equals(BooleanValue.create()));
                    });

                    runner.test("with false", (Test test) ->
                    {
                        final BooleanValue value = BooleanValue.create();
                        test.assertFalse(value.equals(BooleanValue.create(false)));
                    });

                    runner.test("with true", (Test test) ->
                    {
                        final BooleanValue value = BooleanValue.create();
                        test.assertFalse(value.equals(BooleanValue.create(true)));
                    });
                });

                runner.testGroup("with false", () ->
                {
                    runner.test("with null", (Test test) ->
                    {
                        final BooleanValue value = BooleanValue.create(false);
                        test.assertFalse(value.equals((BooleanValue)null));
                    });

                    runner.test("with no value", (Test test) ->
                    {
                        final BooleanValue value = BooleanValue.create(false);
                        test.assertFalse(value.equals(BooleanValue.create()));
                    });

                    runner.test("with false", (Test test) ->
                    {
                        final BooleanValue value = BooleanValue.create(false);
                        test.assertTrue(value.equals(BooleanValue.create(false)));
                    });

                    runner.test("with true", (Test test) ->
                    {
                        final BooleanValue value = BooleanValue.create(false);
                        test.assertFalse(value.equals(BooleanValue.create(true)));
                    });
                });

                runner.testGroup("with true", () ->
                {
                    runner.test("with null", (Test test) ->
                    {
                        final BooleanValue value = BooleanValue.create(true);
                        test.assertFalse(value.equals((BooleanValue)null));
                    });

                    runner.test("with no value", (Test test) ->
                    {
                        final BooleanValue value = BooleanValue.create(true);
                        test.assertFalse(value.equals(BooleanValue.create()));
                    });

                    runner.test("with false", (Test test) ->
                    {
                        final BooleanValue value = BooleanValue.create(true);
                        test.assertFalse(value.equals(BooleanValue.create(false)));
                    });

                    runner.test("with true", (Test test) ->
                    {
                        final BooleanValue value = BooleanValue.create(true);
                        test.assertTrue(value.equals(BooleanValue.create(true)));
                    });
                });
            });

            runner.testGroup("toString()", () ->
            {
                runner.test("with no value", (Test test) ->
                {
                    test.assertEqual("no value", BooleanValue.create().toString());
                });

                runner.test("with false", (Test test) ->
                {
                    test.assertEqual("false", BooleanValue.create(false).toString());
                });

                runner.test("with true", (Test test) ->
                {
                    test.assertEqual("true", BooleanValue.create(true).toString());
                });
            });
        });
    }
}
