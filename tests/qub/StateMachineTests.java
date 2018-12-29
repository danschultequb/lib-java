package qub;

public class StateMachineTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(StateMachine.class, () ->
        {
            runner.test("constructor()", (Test test) ->
            {
                final StateMachine stateMachine = new StateMachine();
                test.assertEqual(0, stateMachine.getCount());
            });

            runner.testGroup("createState(String)", () ->
            {
                runner.test("with null stateName", (Test test) ->
                {
                    final StateMachine stateMachine = new StateMachine();
                    test.assertThrows(() -> stateMachine.createState(null), new PreConditionFailure("stateName cannot be null."));
                });

                runner.test("with empty stateName", (Test test) ->
                {
                    final StateMachine stateMachine = new StateMachine();
                    test.assertThrows(() -> stateMachine.createState(""), new PreConditionFailure("stateName cannot be empty."));
                });

                runner.test("with non-empty stateName", (Test test) ->
                {
                    final StateMachine stateMachine = new StateMachine();
                    final State s1 = stateMachine.createState("apples");
                    test.assertNotNull(s1);
                    test.assertEqual("apples", s1.getName());
                    test.assertEqual(1, stateMachine.getCount());
                    final State s2 = stateMachine.createState("apples");
                    test.assertNotNull(s2);
                    test.assertEqual("apples", s2.getName());
                    test.assertEqual(2, stateMachine.getCount());
                    test.assertNotSame(s1, s2);
                });

                runner.test("with existing stateName with different case", (Test test) ->
                {
                    final StateMachine stateMachine = new StateMachine();
                    final State state = stateMachine.createState("A");
                    final State state2 = stateMachine.createState("a");
                    test.assertNotSame(state, state2);
                    test.assertEqual("A", state.getName());
                    test.assertEqual("a", state2.getName());
                    test.assertEqual(2, stateMachine.getCount());
                });
            });

            runner.testGroup("isMatch(String)", () ->
            {
                runner.test("with null input", (Test test) ->
                {
                    final StateMachine stateMachine = new StateMachine();
                    test.assertThrows(() -> stateMachine.isMatch((String)null));
                });

                final Action2<String,Boolean> isMatchTest = (String input, Boolean expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(input), (Test test) ->
                    {
                        final StateMachine stateMachine = new StateMachine();
                        final State a = stateMachine.createState("a");
                        a.setStartState(true);
                        final State b = stateMachine.createState("b");
                        b.setEndState(true);
                        a.addNextState('1', b);
                        b.addNextState('0', b);
                        test.assertEqual(expected, stateMachine.isMatch(input));
                    });
                };

                isMatchTest.run("", false);
                isMatchTest.run("1", true);
                isMatchTest.run("10", true);
                isMatchTest.run("100", true);
                isMatchTest.run("11", false);

                runner.testGroup("with instant next states", () ->
                {
                    final Action2<String,Boolean> instantNextStatesTest = (String inputs, Boolean expected) ->
                    {
                        runner.test("with " + Strings.escapeAndQuote(inputs), (Test test) ->
                        {
                            final StateMachine stateMachine = new StateMachine();
                            final State start = stateMachine.createState("start");
                            final State waitingForA = stateMachine.createState("waiting for a");
                            final State waitingForB = stateMachine.createState("waiting for b");
                            final State end = stateMachine.createState("end");

                            start.setStartState(true);
                            end.setEndState(true);

                            start.addInstantNextState(waitingForA);
                            start.addInstantNextState(waitingForB);
                            waitingForA.addNextState('a', end);
                            waitingForB.addNextState('b', end);

                            test.assertEqual(expected, stateMachine.isMatch(inputs));
                        });
                    };

                    instantNextStatesTest.run("", false);
                    instantNextStatesTest.run("a", true);
                    instantNextStatesTest.run("b", true);
                    instantNextStatesTest.run("c", false);
                });
            });

            runner.testGroup("isMatch(Iterable<Character>)", () ->
            {
                runner.test("with null input", (Test test) ->
                {
                    final StateMachine stateMachine = new StateMachine();
                    test.assertThrows(() -> stateMachine.isMatch((Iterable<Character>)null));
                });

                final Action2<String,Boolean> isMatchTest = (String input, Boolean expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(input), (Test test) ->
                    {
                        final StateMachine stateMachine = new StateMachine();
                        final State a = stateMachine.createState("a");
                        a.setStartState(true);
                        final State b = stateMachine.createState("b");
                        b.setEndState(true);
                        a.addNextState('1', b);
                        b.addNextState('0', b);
                        test.assertEqual(expected, stateMachine.isMatch(Strings.iterable(input)));
                    });
                };

                isMatchTest.run("", false);
                isMatchTest.run("1", true);
                isMatchTest.run("10", true);
                isMatchTest.run("100", true);
                isMatchTest.run("11", false);
            });

            runner.testGroup("isMatch(Iterator<Character>)", () ->
            {
                runner.test("with null input", (Test test) ->
                {
                    final StateMachine stateMachine = new StateMachine();
                    test.assertThrows(() -> stateMachine.isMatch((Iterator<Character>)null));
                });

                final Action2<String,Boolean> isMatchTest = (String input, Boolean expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(input), (Test test) ->
                    {
                        final StateMachine stateMachine = new StateMachine();
                        final State a = stateMachine.createState("a");
                        a.setStartState(true);
                        final State b = stateMachine.createState("b");
                        b.setEndState(true);
                        a.addNextState('1', b);
                        b.addNextState('0', b);
                        test.assertEqual(expected, stateMachine.isMatch(Strings.iterate(input)));
                    });
                };

                isMatchTest.run("", false);
                isMatchTest.run("1", true);
                isMatchTest.run("10", true);
                isMatchTest.run("100", true);
                isMatchTest.run("11", false);
            });

            runner.testGroup("containsMatch(String)", () ->
            {
                runner.test("with null input", (Test test) ->
                {
                    final StateMachine stateMachine = new StateMachine();
                    test.assertThrows(() -> stateMachine.containsMatch((String)null));
                });

                final Action2<String,Boolean> containsMatchTest = (String input, Boolean expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(input), (Test test) ->
                    {
                        final StateMachine stateMachine = new StateMachine();
                        final State a = stateMachine.createState("a");
                        a.setStartState(true);
                        final State b = stateMachine.createState("b");
                        b.setEndState(true);
                        a.addNextState('1', b);
                        b.addNextState('0', b);
                        test.assertEqual(expected, stateMachine.containsMatch(input));
                    });
                };

                containsMatchTest.run("", false);
                containsMatchTest.run("5", false);
                containsMatchTest.run("15", true);
                containsMatchTest.run("51", true);
                containsMatchTest.run("1", true);
                containsMatchTest.run("10", true);
                containsMatchTest.run("100", true);
                containsMatchTest.run("11", true);
            });

            runner.testGroup("containsMatch(Iterable<Character>)", () ->
            {
                runner.test("with null input", (Test test) ->
                {
                    final StateMachine stateMachine = new StateMachine();
                    test.assertThrows(() -> stateMachine.containsMatch((Iterable<Character>)null));
                });

                final Action2<String,Boolean> containsMatchTest = (String input, Boolean expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(input), (Test test) ->
                    {
                        final StateMachine stateMachine = new StateMachine();
                        final State a = stateMachine.createState("a");
                        a.setStartState(true);
                        final State b = stateMachine.createState("b");
                        b.setEndState(true);
                        a.addNextState('1', b);
                        b.addNextState('0', b);
                        test.assertEqual(expected, stateMachine.containsMatch(Strings.iterable(input)));
                    });
                };

                containsMatchTest.run("", false);
                containsMatchTest.run("5", false);
                containsMatchTest.run("15", true);
                containsMatchTest.run("51", true);
                containsMatchTest.run("1", true);
                containsMatchTest.run("10", true);
                containsMatchTest.run("100", true);
                containsMatchTest.run("11", true);
            });

            runner.testGroup("containsMatch(Iterator<Character>)", () ->
            {
                runner.test("with null input", (Test test) ->
                {
                    final StateMachine stateMachine = new StateMachine();
                    test.assertThrows(() -> stateMachine.containsMatch((Iterator<Character>)null));
                });

                final Action2<String,Boolean> containsMatchTest = (String input, Boolean expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(input), (Test test) ->
                    {
                        final StateMachine stateMachine = new StateMachine();
                        final State a = stateMachine.createState("a");
                        a.setStartState(true);
                        final State b = stateMachine.createState("b");
                        b.setEndState(true);
                        a.addNextState('1', b);
                        b.addNextState('0', b);
                        test.assertEqual(expected, stateMachine.containsMatch(Strings.iterate(input)));
                    });
                };

                containsMatchTest.run("", false);
                containsMatchTest.run("5", false);
                containsMatchTest.run("15", true);
                containsMatchTest.run("51", true);
                containsMatchTest.run("1", true);
                containsMatchTest.run("10", true);
                containsMatchTest.run("100", true);
                containsMatchTest.run("11", true);
            });

            runner.testGroup("isMatch(String) with " + Strings.escapeAndQuote("abc*xyz"), () ->
            {
                final Action2<String,Boolean> isMatchTest = (String inputValues, Boolean expected) ->
                {
                    runner.test("and " + Strings.escapeAndQuote(inputValues), (Test test) ->
                    {
                        final StateMachine stateMachine = createABCStarXYZStateMachine();
                        test.assertEqual(expected, stateMachine.isMatch(inputValues));
                    });
                };

                isMatchTest.run("abcxyz", true);
                isMatchTest.run("abcxyxxyz", true);
                isMatchTest.run("abcxy", false);
                isMatchTest.run("abxyz", false);
            });

            runner.testGroup("getMatches(String) with " + Strings.escapeAndQuote("abc*xyz"), () ->
            {
                runner.test("and " + Strings.escapeAndQuote("abcxyz"), (Test test) ->
                {
                    final StateMachine stateMachine = createABCStarXYZStateMachine();
                    final Iterable<Match> matches = stateMachine.getMatches("abcxyz");
                    test.assertNotNull(matches);
                    test.assertEqual(1, matches.getCount());
                    test.assertEqual(Iterable.empty(), matches.first().getTrackedValues());
                });

                runner.test("and " + Strings.escapeAndQuote("abcxyxxyz"), (Test test) ->
                {
                    final StateMachine stateMachine = createABCStarXYZStateMachine();
                    final Iterable<Match> matches = Array.create(stateMachine.getMatches("abcxyxxyz"));
                    test.assertNotNull(matches);
                    test.assertEqual(1, matches.getCount());
                    final Array<Iterable<Character>> expectedTrackedValues = new Array<>(1);
                    expectedTrackedValues.set(0, Strings.iterable("xyx"));
                    test.assertEqual(expectedTrackedValues, matches.first().getTrackedValues());
                });

                runner.test("and " + Strings.escapeAndQuote("abcxy"), (Test test) ->
                {
                    final StateMachine stateMachine = createABCStarXYZStateMachine();
                    test.assertEqual(Iterable.empty(), stateMachine.getMatches("abcxy"));
                });

                runner.test("and " + Strings.escapeAndQuote("abxyz"), (Test test) ->
                {
                    final StateMachine stateMachine = createABCStarXYZStateMachine();
                    test.assertEqual(Iterable.empty(), stateMachine.getMatches("abxyz"));
                });
            });
        });
    }

    private static StateMachine createABCStarXYZStateMachine()
    {
        final StateMachine stateMachine = new StateMachine();
        final State start = stateMachine.createState().setStartState(true);
        final State a = stateMachine.createState("a");
        final State ab = stateMachine.createState("ab");
        final State abc = stateMachine.createState("abc");
        final State abcStar = stateMachine.createState("abc*").trackValues(true);
        final State abcStarx = stateMachine.createState("abc*x");
        final State abcStarxy = stateMachine.createState("abc*xy");
        final State abcStarxyz = stateMachine.createState("abc*xyz").setEndState(true);
        start.addNextState('a', a);
        a.addNextState('b', ab);
        ab.addNextState('c', abc);
        abc.addInstantNextState(abcStar);
        abcStar.addNextState(Characters.all, abcStar);
        abcStar.addNextState('x', abcStarx);
        abcStarx.addNextState('y', abcStarxy);
        abcStarxy.addNextState('z', abcStarxyz);
        return stateMachine;
    }
}
