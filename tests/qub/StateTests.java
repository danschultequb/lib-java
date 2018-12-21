package qub;

public class StateTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(State.class, () ->
        {
            runner.testGroup("constructor(String)", () ->
            {
                runner.test("with null name", (Test test) ->
                {
                    test.assertThrows(() -> new State(null), new PreConditionFailure("name cannot be null."));
                });

                runner.test("with empty name", (Test test) ->
                {
                    test.assertThrows(() -> new State(""), new PreConditionFailure("name cannot be empty."));
                });

                runner.test("with non-empty name", (Test test) ->
                {
                    final State state = new State("five");
                    test.assertEqual("five", state.getName());
                    test.assertFalse(state.isStartState());
                });
            });

            runner.test("setStartState(boolean)", (Test test) ->
            {
                final State state = new State("a");

                state.setStartState(true);
                test.assertTrue(state.isStartState());

                state.setStartState(false);
                test.assertFalse(state.isStartState());
            });

            runner.testGroup("addNextState(char,State)", () ->
            {
                runner.test("with null nextState", (Test test) ->
                {
                    final State state = new State("a");
                    test.assertThrows(() -> state.addNextState('1', null), new PreConditionFailure("nextState cannot be null."));
                });

                runner.test("with non-null nextState", (Test test) ->
                {
                    final State state1 = new State("a");
                    final State state2 = new State("b");
                    test.assertSame(state1, state1.addNextState('1', state2));
                    test.assertEqual(Array.fromValues(new State[] { state2 }), state1.getNextStates('1'));
                });

                runner.test("with already existing value", (Test test) ->
                {
                    final State state1 = new State("a");
                    final State state2 = new State("b");
                    final State state3 = new State("c");
                    test.assertSame(state1, state1.addNextState('1', state2));
                    test.assertSame(state1, state1.addNextState('1', state3));
                    test.assertEqual(Array.fromValues(new State[] { state2, state3 }), state1.getNextStates('1'));
                });
            });

            runner.testGroup("getNextStates(char)", () ->
            {
                runner.test("with non-existing value", (Test test) ->
                {
                    final State state = new State("a");
                    test.assertEqual(Iterable.empty(), state.getNextStates('1'));
                });

                runner.test("with existing value with different case", (Test test) ->
                {
                    final State state1 = new State("1");
                    final State state2 = new State("2");
                    state1.addNextState('a', state2);
                    test.assertEqual(Iterable.empty(), state1.getNextStates('A'));
                });

                runner.test("with existing value with same case", (Test test) ->
                {
                    final State state1 = new State("1");
                    final State state2 = new State("2");
                    state1.addNextState('a', state2);
                    test.assertEqual(Array.fromValues(new State[] { state2 }), state1.getNextStates('a'));
                });
            });
        });
    }
}
