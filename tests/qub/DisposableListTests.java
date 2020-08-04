package qub;

public interface DisposableListTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(DisposableList.class, () ->
        {
            runner.testGroup("create(Disposable...)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> DisposableList.create((Disposable[])null),
                        new PreConditionFailure("disposables cannot be null."));
                });

                runner.test("with no arguments", (Test test) ->
                {
                    final DisposableList list = DisposableList.create();
                    test.assertFalse(list.isDisposed());
                    test.assertEqual(0, list.getCount());
                    test.assertFalse(list.isDisposed());

                    test.assertTrue(list.dispose().await());
                    test.assertTrue(list.isDisposed());

                    test.assertFalse(list.dispose().await());
                    test.assertTrue(list.isDisposed());
                });

                runner.test("with one argument", (Test test) ->
                {
                    final Disposable disposable = Disposable.create();
                    final DisposableList list = DisposableList.create(disposable);
                    test.assertNotNull(list);
                    test.assertEqual(Iterable.create(disposable), list);
                    test.assertFalse(list.isDisposed());
                    test.assertFalse(disposable.isDisposed());

                    test.assertTrue(list.dispose().await());
                    test.assertTrue(list.isDisposed());
                    test.assertTrue(disposable.isDisposed());

                    test.assertFalse(list.dispose().await());
                    test.assertTrue(list.isDisposed());
                    test.assertTrue(disposable.isDisposed());
                });

                runner.test("with multiple arguments", (Test test) ->
                {
                    final Disposable disposable1 = Disposable.create();
                    final Disposable disposable2 = Disposable.create();
                    final DisposableList list = DisposableList.create(disposable1, disposable2);
                    test.assertNotNull(list);
                    test.assertEqual(Iterable.create(disposable1, disposable2), list);
                    test.assertFalse(list.isDisposed());
                    test.assertFalse(disposable1.isDisposed());
                    test.assertFalse(disposable2.isDisposed());

                    test.assertTrue(disposable2.dispose().await());
                    test.assertFalse(list.isDisposed());
                    test.assertFalse(disposable1.isDisposed());
                    test.assertTrue(disposable2.isDisposed());

                    test.assertTrue(list.dispose().await());
                    test.assertTrue(list.isDisposed());
                    test.assertTrue(disposable1.isDisposed());
                    test.assertTrue(disposable2.isDisposed());

                    test.assertFalse(list.dispose().await());
                    test.assertTrue(list.isDisposed());
                    test.assertTrue(disposable1.isDisposed());
                    test.assertTrue(disposable2.isDisposed());
                });
            });
        });
    }
}
