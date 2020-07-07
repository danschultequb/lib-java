package qub;

public interface AWTUIBaseTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(AWTUIBase.class, () ->
        {
            runner.testGroup("onSizeChanged(java.awt.Component,Action0)", () ->
            {
                runner.test("with null component", (Test test) ->
                {
                    final AWTUIBase uiBase = AWTUIBase.create(test.getProcess());
                    final java.awt.Component component = null;
                    final Action0 callback = () -> {};
                    test.assertThrows(() -> uiBase.onSizeChanged(component, callback),
                        new PreConditionFailure("component cannot be null."));
                });

                runner.test("with null callback", (Test test) ->
                {
                    final AWTUIBase uiBase = AWTUIBase.create(test.getProcess());
                    final java.awt.Component component = new javax.swing.JButton();
                    final Action0 callback = null;
                    test.assertThrows(() -> uiBase.onSizeChanged(component, callback),
                        new PreConditionFailure("callback cannot be null."));
                });

                runner.test("with valid arguments", (Test test) ->
                {
                    final AWTUIBase uiBase = AWTUIBase.create(test.getProcess());
                    final java.awt.Component component = new javax.swing.JButton();
                    final Action0 callback = () -> {};

                    final Disposable disposable = uiBase.onSizeChanged(component, callback);
                    test.assertNotNull(disposable);
                    test.assertFalse(disposable.isDisposed());

                    final java.awt.event.ComponentListener[] componentListenersBeforeDispose = component.getComponentListeners();
                    test.assertNotNull(componentListenersBeforeDispose);
                    test.assertEqual(1, componentListenersBeforeDispose.length);

                    test.assertTrue(disposable.dispose().await());

                    final java.awt.event.ComponentListener[] componentListenersAfterDispose = component.getComponentListeners();
                    test.assertNotNull(componentListenersAfterDispose);
                    test.assertEqual(0, componentListenersAfterDispose.length);
                });
            });
        });
    }
}
