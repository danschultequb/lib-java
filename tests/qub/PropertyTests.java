package qub;

public interface PropertyTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(Property.class, () ->
        {
            runner.test("create()", (Test test) ->
            {
                final MutableProperty<Integer> property = Property.create();
                test.assertNotNull(property);
                test.assertFalse(property.hasValue());
            });

            runner.testGroup("create(T)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final MutableProperty<String> property = Property.create(null);
                    test.assertNotNull(property);
                    test.assertTrue(property.hasValue());
                    test.assertEqual(null, property.get());
                });

                runner.test("with non-null", (Test test) ->
                {
                    final MutableProperty<String> property = Property.create("hello");
                    test.assertNotNull(property);
                    test.assertTrue(property.hasValue());
                    test.assertEqual("hello", property.get());
                });
            });
        });
    }
}
