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

            runner.test("createState()", (Test test) ->
            {
                final StateMachine stateMachine = new StateMachine();
                stateMachine.createState();
                test.assertEqual(1, stateMachine.getCount());

                stateMachine.createState();
                test.assertEqual(2, stateMachine.getCount());
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
                    test.assertSuccess(stateMachine.createState("apples"),
                        (State state) ->
                        {
                            test.assertNotNull(state);
                            test.assertEqual("apples", state.getName());
                        });
                    test.assertEqual(1, stateMachine.getCount());
                });

                runner.test("with already existing stateName", (Test test) ->
                {
                    final StateMachine stateMachine = new StateMachine();
                    stateMachine.createState("A");
                    test.assertError(new AlreadyExistsException("A", "A state with the name \"A\" already exists."), stateMachine.createState("A"));
                    test.assertEqual(1, stateMachine.getCount());
                });
            });
        });
    }
}
