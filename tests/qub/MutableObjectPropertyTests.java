package qub;

public interface MutableObjectPropertyTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(MutableObjectProperty.class, () ->
        {
            MutablePropertyTests.test(runner, MutableObjectProperty::create);
        });
    }
}
