package qub;

public class SpanTests
{
    public static void test(final TestRunner runner)
    {
        runner.testGroup("Span", new Action0()
        {
            @Override
            public void run()
            {
                runner.test("constructor", new Action1<Test>()
                {
                    @Override
                    public void run(Test test)
                    {
                        final Span span = new Span(1, 2);
                        test.assertEqual(1, span.getStartIndex());
                        test.assertEqual(2, span.getLength());
                        test.assertEqual("[1, 3)", span.toString());
                    }
                });
                
                runner.testGroup("getAfterEndIndex()", new Action0()
                {
                    @Override
                    public void run()
                    {
                        final Action3<Integer,Integer,Integer> getAfterEndIndexTest = new Action3<Integer, Integer, Integer>()
                        {
                            @Override
                            public void run(final Integer startIndex, final Integer length, final Integer expectedAfterEndIndex)
                            {
                                runner.test("with " + startIndex + " and " + length, new Action1<Test>()
                                {
                                    @Override
                                    public void run(Test test)
                                    {
                                        final Span span = new Span(startIndex, length);
                                        final int actualAfterEndIndex = span.getAfterEndIndex();
                                        test.assertEqual(expectedAfterEndIndex, actualAfterEndIndex);
                                    }
                                });
                            }
                        };

                        getAfterEndIndexTest.run(-10, -3, -13);
                        getAfterEndIndexTest.run(-10, 0, -10);
                        getAfterEndIndexTest.run(-10, 7, -3);
                        getAfterEndIndexTest.run(0, -4, -4);
                        getAfterEndIndexTest.run(0, 0, 0);
                        getAfterEndIndexTest.run(0, 3, 3);
                        getAfterEndIndexTest.run(1, -1, 0);
                        getAfterEndIndexTest.run(1, 0, 1);
                        getAfterEndIndexTest.run(1, 1, 2);
                    }
                });
                
                runner.testGroup("getEndIndex()", new Action0()
                {
                    @Override
                    public void run()
                    {
                        final Action3<Integer,Integer,Integer> getEndIndexTest = new Action3<Integer, Integer, Integer>()
                        {
                            @Override
                            public void run(final Integer startIndex, final Integer length, final Integer expectedEndIndex)
                            {
                                runner.test("with " + startIndex + " and " + length, new Action1<Test>()
                                {
                                    @Override
                                    public void run(Test test)
                                    {
                                        final Span span = new Span(startIndex, length);
                                        final int actualEndIndex = span.getEndIndex();
                                        test.assertEqual(expectedEndIndex, actualEndIndex);
                                    }
                                });
                            }
                        };

                        getEndIndexTest.run(-10, -3, -13);
                        getEndIndexTest.run(-10, 0, -10);
                        getEndIndexTest.run(-10, 1, -10);
                        getEndIndexTest.run(-10, 7, -4);
                        getEndIndexTest.run(0, -4, -4);
                        getEndIndexTest.run(0, 0, 0);
                        getEndIndexTest.run(0, 1, 0);
                        getEndIndexTest.run(0, 3, 2);
                        getEndIndexTest.run(1, -1, 0);
                        getEndIndexTest.run(1, 0, 1);
                        getEndIndexTest.run(1, 1, 1);
                        getEndIndexTest.run(1, 21, 21);
                    }
                });
            }
        });
    }
}
