package qub;

public interface DisposableTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(Disposable.class, () ->
        {
            runner.testGroup("create(Disposable...)", () ->
            {
                runner.test("with no arguments", (Test test) ->
                {
                    final Disposable disposable = Disposable.create();
                    test.assertNotNull(disposable);
                    test.assertFalse(disposable.isDisposed());

                    test.assertTrue(disposable.dispose().await());
                    test.assertTrue(disposable.isDisposed());

                    test.assertFalse(disposable.dispose().await());
                    test.assertTrue(disposable.isDisposed());
                });

                runner.test("with null array", (Test test) ->
                {
                    test.assertThrows(() -> Disposable.create((Disposable[])null),
                        new PreConditionFailure("disposables cannot be null."));
                });

                runner.test("with one value", (Test test) ->
                {
                    final Disposable disposable1 = Disposable.create();
                    final Disposable disposable2 = Disposable.create(disposable1);

                    test.assertTrue(disposable2.dispose().await());
                    test.assertTrue(disposable1.isDisposed());
                    test.assertTrue(disposable2.isDisposed());

                    test.assertFalse(disposable2.dispose().await());
                    test.assertTrue(disposable1.isDisposed());
                    test.assertTrue(disposable2.isDisposed());
                });
            });

            runner.testGroup("create(Iterable<? extends Disposable>)", () ->
            {
                runner.test("with null Iterable", (Test test) ->
                {
                    test.assertThrows(() -> Disposable.create((Iterable<Disposable>)null),
                        new PreConditionFailure("disposables cannot be null."));
                });

                runner.test("with empty Iterable", (Test test) ->
                {
                    final Disposable disposable = Disposable.create(Iterable.create());
                    test.assertNotNull(disposable);
                    test.assertFalse(disposable.isDisposed());

                    test.assertTrue(disposable.dispose().await());
                    test.assertTrue(disposable.isDisposed());

                    test.assertFalse(disposable.dispose().await());
                    test.assertTrue(disposable.isDisposed());
                });

                runner.test("with one Disposable", (Test test) ->
                {
                    final Disposable disposable1 = Disposable.create();
                    final Disposable disposable2 = Disposable.create(Iterable.create(disposable1));

                    test.assertTrue(disposable2.dispose().await());
                    test.assertTrue(disposable1.isDisposed());
                    test.assertTrue(disposable2.isDisposed());

                    test.assertFalse(disposable2.dispose().await());
                    test.assertTrue(disposable1.isDisposed());
                    test.assertTrue(disposable2.isDisposed());
                });

                runner.test("with one Disposable subtype", (Test test) ->
                {
                    final InMemoryByteStream stream = InMemoryByteStream.create();
                    final Disposable disposable = Disposable.create(Iterable.create(stream));

                    test.assertTrue(disposable.dispose().await());
                    test.assertTrue(stream.isDisposed());
                    test.assertTrue(disposable.isDisposed());

                    test.assertFalse(disposable.dispose().await());
                    test.assertTrue(stream.isDisposed());
                    test.assertTrue(disposable.isDisposed());
                });
            });
        });
    }
}
