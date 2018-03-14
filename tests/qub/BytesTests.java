package qub;

public class BytesTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(Bytes.class, () ->
        {
            runner.test("constructor()", (Test test) ->
            {
                test.assertNotNull(new Bytes());
            });

            runner.testGroup("toUnsignedInt(byte)", () ->
            {
                final Action2<Integer,Integer> toUnsignedIntTest = (Integer byteValue, Integer expectedUnsignedInteger) ->
                {
                    runner.test("with " + byteValue, (Test test) ->
                    {
                        test.assertEqual(expectedUnsignedInteger, Bytes.toUnsignedInt(byteValue.byteValue()));
                    });
                };

                toUnsignedIntTest.run(0, 0);
                toUnsignedIntTest.run(1, 1);
                toUnsignedIntTest.run(127, 127);
                toUnsignedIntTest.run(128, 128);
                toUnsignedIntTest.run(129, 129);
                toUnsignedIntTest.run(254, 254);
                toUnsignedIntTest.run(255, 255);
                toUnsignedIntTest.run(256, 0);
                toUnsignedIntTest.run(-1, 255);
                toUnsignedIntTest.run(-2, 254);
                toUnsignedIntTest.run(-126, 130);
                toUnsignedIntTest.run(-127, 129);
                toUnsignedIntTest.run(-128, 128);
                toUnsignedIntTest.run(-129, 127);
            });
        });
    }
}
