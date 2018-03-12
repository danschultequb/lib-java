package qub;

public class StringIteratorTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup("StringIterator", () ->
        {
            runner.testGroup("constructor", () ->
            {
                runner.test("with null", test ->
                {
                    final StringIterator iterator = new StringIterator(null);
                    assertIterator(test, iterator, false, null, 0);

                    test.assertFalse(iterator.next());
                    assertIterator(test, iterator, true, null, 0);

                    test.assertFalse(iterator.next());
                    assertIterator(test, iterator, true, null, 0);
                });

                runner.test("with empty", test ->
                {
                    final StringIterator iterator = new StringIterator("");
                    assertIterator(test, iterator, false, null, 0);

                    test.assertFalse(iterator.next());
                    assertIterator(test, iterator, true, null, 0);

                    test.assertFalse(iterator.next());
                    assertIterator(test, iterator, true, null, 0);
                });

                runner.test("with non-empty", test ->
                {
                    final StringIterator iterator = new StringIterator("ab");
                    assertIterator(test, iterator, false, null, 0);

                    test.assertTrue(iterator.next());
                    assertIterator(test, iterator, true, 'a', 0);

                    test.assertTrue(iterator.next());
                    assertIterator(test, iterator, true, 'b', 1);

                    test.assertFalse(iterator.next());
                    assertIterator(test, iterator, true, null, 2);

                    test.assertFalse(iterator.next());
                    assertIterator(test, iterator, true, null, 2);
                });
            });

            runner.testGroup("setCurrentIndex()", () ->
            {
                runner.test("with " + Strings.escapeAndQuote("hello") + " and " + -10, test ->
                {
                    final StringIterator iterator = new StringIterator("hello");

                    iterator.setCurrentIndex(-10);
                    assertIterator(test, iterator, true, 'h', 0);
                });

                runner.test("with " + Strings.escapeAndQuote("hello") + " and " + 0, test ->
                {
                    final StringIterator iterator = new StringIterator("hello");

                    iterator.setCurrentIndex(0);
                    assertIterator(test, iterator, true, 'h', 0);
                });

                runner.test("with " + Strings.escapeAndQuote("hello") + " and " + 1, test ->
                {
                    final StringIterator iterator = new StringIterator("hello");

                    iterator.setCurrentIndex(1);
                    assertIterator(test, iterator, true, 'e', 1);
                });

                runner.test("with " + Strings.escapeAndQuote("hello") + " and " + 4, test ->
                {
                    final StringIterator iterator = new StringIterator("hello");

                    iterator.setCurrentIndex(4);
                    assertIterator(test, iterator, true, 'o', 4);
                });

                runner.test("with " + Strings.escapeAndQuote("hello") + " and " + 5, test ->
                {
                    final StringIterator iterator = new StringIterator("hello");

                    iterator.setCurrentIndex(5);
                    assertIterator(test, iterator, true, null, 5);
                });

                runner.test("with " + Strings.escapeAndQuote("hello") + " and " + 6, test ->
                {
                    final StringIterator iterator = new StringIterator("hello");

                    iterator.setCurrentIndex(6);
                    assertIterator(test, iterator, true, null, 5);
                });
            });
        });
    }

    private static void assertIterator(Test test, StringIterator iterator, boolean hasStarted, Character current, int currentIndex)
    {
        test.assertEqual(hasStarted, iterator.hasStarted());
        test.assertEqual(current != null, iterator.hasCurrent());
        test.assertEqual(current, iterator.getCurrent());
        test.assertEqual(currentIndex, iterator.getCurrentIndex());
    }
}
