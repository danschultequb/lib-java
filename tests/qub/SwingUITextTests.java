package qub;

public interface SwingUITextTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(SwingUIText.class, () ->
        {
            final Function1<Test,SwingUIText> creator = (Test test) ->
            {
                final Display display = test.getDisplays().first();
                return SwingUIText.create(display);
            };

            UITextTests.test(runner, creator);
            
            runner.testGroup("setWidth(Distance)", () ->
            {
                runner.test("should return " + Types.getTypeName(SwingUIText.class), (Test test) ->
                {
                    final SwingUIText text = creator.run(test);
                    final SwingUIText setWidthResult = text.setWidth(Distance.inches(1));
                    test.assertSame(text, setWidthResult);
                });
            });

            runner.testGroup("setHeight(Distance)", () ->
            {
                runner.test("should return " + Types.getTypeName(SwingUIText.class), (Test test) ->
                {
                    final SwingUIText text = creator.run(test);
                    final SwingUIText setHeightResult = text.setHeight(Distance.inches(1));
                    test.assertSame(text, setHeightResult);
                });
            });

            runner.testGroup("setSize(Size2D)", () ->
            {
                runner.test("should return " + Types.getTypeName(SwingUIText.class), (Test test) ->
                {
                    final SwingUIText text = creator.run(test);
                    final SwingUIText setHeightResult = text.setSize(new Size2D(Distance.inches(2), Distance.inches(3)));
                    test.assertSame(text, setHeightResult);
                });
            });

            runner.testGroup("setSize(Distance,Distance)", () ->
            {
                runner.test("should return " + Types.getTypeName(SwingUIText.class), (Test test) ->
                {
                    final SwingUIText text = creator.run(test);
                    final SwingUIText setHeightResult = text.setSize(Distance.inches(2), Distance.inches(3));
                    test.assertSame(text, setHeightResult);
                });
            });

            runner.testGroup("setText(String)", () ->
            {
                runner.test("should return " + Types.getTypeName(SwingUIText.class), (Test test) ->
                {
                    final SwingUIText text = creator.run(test);
                    final SwingUIText setHeightResult = text.setText("hello");
                    test.assertSame(text, setHeightResult);
                });
            });

            runner.testGroup("setFontSize(Distance)", () ->
            {
                runner.test("should return " + Types.getTypeName(SwingUIButton.class), (Test test) ->
                {
                    final SwingUIText text = creator.run(test);
                    final SwingUIText setFontSizeResult = text.setFontSize(Distance.inches(1));
                    test.assertSame(text, setFontSizeResult);
                });
            });
        });
    }
}
