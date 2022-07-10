package qub;

public interface MutableMapEntryTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(MutableMapEntry.class, () ->
        {
            runner.testGroup("create(TKey,TValue)", () ->
            {
                final Action2<String,Integer> createTest = (String key, Integer value) ->
                {
                    runner.test("with " + English.andList(key, value), (Test test) ->
                    {
                        final MutableMapEntry<String,Integer> entry = MutableMapEntry.create(key, value);
                        test.assertNotNull(entry);
                        test.assertSame(key, entry.getKey());
                        test.assertSame(value, entry.getValue());
                    });
                };

                createTest.run(null, null);
                createTest.run(null, 0);
                createTest.run("hello", null);
                createTest.run("abc", 3);
            });

            runner.testGroup("setValue(TValue)", () ->
            {
                final Action2<MutableMapEntry<Character,Boolean>,Boolean> setValueTest = (MutableMapEntry<Character,Boolean> entry, Boolean value) ->
                {
                    runner.test("with " + English.andList(entry, value), (Test test) ->
                    {
                        final MutableMapEntry<Character,Boolean> setValueResult = entry.setValue(value);
                        test.assertSame(entry, setValueResult);
                        test.assertSame(value, entry.getValue());
                    });
                };

                setValueTest.run(MutableMapEntry.create('a', false), false);
                setValueTest.run(MutableMapEntry.create('a', false), true);
                setValueTest.run(MutableMapEntry.create('a', false), null);
            });

            runner.testGroup("toString()", () ->
            {
                final Action2<MutableMapEntry<Integer,Double>,String> toStringTest = (MutableMapEntry<Integer,Double> entry, String expected) ->
                {
                    runner.test("with " + entry.toString(), (Test test) ->
                    {
                        test.assertEqual(expected, entry.toString());
                    });
                };

                toStringTest.run(MutableMapEntry.create(null, null), "null:null");
                toStringTest.run(MutableMapEntry.create(5, null), "5:null");
                toStringTest.run(MutableMapEntry.create(null, 3.2), "null:3.2");
                toStringTest.run(MutableMapEntry.create(7, 1.9), "7:1.9");
            });

            runner.testGroup("equals(Object)", () ->
            {
                final Action3<MutableMapEntry<Boolean,Character>,Object,Boolean> equalsTest = (MutableMapEntry<Boolean,Character> entry, Object rhs, Boolean expected) ->
                {
                    runner.test("with " + English.andList(entry, rhs), (Test test) ->
                    {
                        test.assertEqual(expected, entry.equals(rhs));
                    });
                };

                equalsTest.run(MutableMapEntry.create(false, 'f'), null, false);
                equalsTest.run(MutableMapEntry.create(false, 'f'), "hi", false);
                equalsTest.run(MutableMapEntry.create(false, 'f'), MutableMapEntry.create(false, 'f'), true);
                equalsTest.run(MutableMapEntry.create(false, 'f'), MutableMapEntry.create(null, 'f'), false);
                equalsTest.run(MutableMapEntry.create(false, 'f'), MutableMapEntry.create(true, 'f'), false);
                equalsTest.run(MutableMapEntry.create(false, 'f'), MutableMapEntry.create(false, 'g'), false);
                equalsTest.run(MutableMapEntry.create(false, 'f'), MutableMapEntry.create(null, 'g'), false);
                equalsTest.run(MutableMapEntry.create(false, 'f'), MutableMapEntry.create(true, 'g'), false);
            });

            runner.testGroup("equals(MapEntry<TKey,TValue>)", () ->
            {
                final Action3<MutableMapEntry<Boolean,Character>,MapEntry<Boolean,Character>,Boolean> equalsTest = (MutableMapEntry<Boolean,Character> entry, MapEntry<Boolean,Character> rhs, Boolean expected) ->
                {
                    runner.test("with " + English.andList(entry, rhs), (Test test) ->
                    {
                        test.assertEqual(expected, entry.equals(rhs));
                    });
                };

                equalsTest.run(MutableMapEntry.create(false, 'f'), null, false);
                equalsTest.run(MutableMapEntry.create(false, 'f'), MutableMapEntry.create(false, 'f'), true);
                equalsTest.run(MutableMapEntry.create(false, 'f'), MutableMapEntry.create(null, 'f'), false);
                equalsTest.run(MutableMapEntry.create(false, 'f'), MutableMapEntry.create(true, 'f'), false);
                equalsTest.run(MutableMapEntry.create(false, 'f'), MutableMapEntry.create(false, 'g'), false);
                equalsTest.run(MutableMapEntry.create(false, 'f'), MutableMapEntry.create(null, 'g'), false);
                equalsTest.run(MutableMapEntry.create(false, 'f'), MutableMapEntry.create(true, 'g'), false);
            });
        });
    }
}
