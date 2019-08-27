package qub;

public interface CharacterComparerTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(CharacterComparer.class, () ->
        {
            runner.testGroup("Exact", () ->
            {
                runner.test("compare()", (Test test) ->
                {
                    final CharacterComparer comparer = CharacterComparer.Exact;
                    test.assertNotNull(comparer);
                    test.assertEqual(Comparison.Equal, comparer.compare('a', 'a'));
                    test.assertEqual(Comparison.LessThan, comparer.compare('a', 'b'));
                    test.assertEqual(Comparison.GreaterThan, comparer.compare('y', 'n'));
                    test.assertEqual(Comparison.GreaterThan, comparer.compare('a', 'A'));
                });

                runner.test("equal()", (Test test) ->
                {
                    final CharacterComparer comparer = CharacterComparer.Exact;
                    test.assertNotNull(comparer);
                    test.assertTrue(comparer.equal('a', 'a'));
                    test.assertFalse(comparer.equal('a', 'b'));
                    test.assertFalse(comparer.equal('y', 'n'));
                    test.assertFalse(comparer.equal('a', 'A'));
                });
            });

            runner.testGroup("CaseInsensitive", () ->
            {
                runner.test("compare()", (Test test) ->
                {
                    final CharacterComparer comparer = CharacterComparer.CaseInsensitive;
                    test.assertNotNull(comparer);
                    test.assertEqual(Comparison.Equal, comparer.compare('a', 'a'));
                    test.assertEqual(Comparison.LessThan, comparer.compare('a', 'b'));
                    test.assertEqual(Comparison.GreaterThan, comparer.compare('y', 'n'));
                    test.assertEqual(Comparison.Equal, comparer.compare('a', 'A'));
                });

                runner.test("equal()", (Test test) ->
                {
                    final CharacterComparer comparer = CharacterComparer.CaseInsensitive;
                    test.assertNotNull(comparer);
                    test.assertTrue(comparer.equal('a', 'a'));
                    test.assertFalse(comparer.equal('a', 'b'));
                    test.assertFalse(comparer.equal('y', 'n'));
                    test.assertTrue(comparer.equal('a', 'A'));
                });
            });
        });
    }
}
