package qub;

public interface StringEnumTests
{
    public static class CaseSensitiveStringEnum extends StringEnum<CaseSensitiveStringEnum>
    {
        private static final StringEnumData<CaseSensitiveStringEnum> data = StringEnumData.create(CaseSensitiveStringEnum.class);

        public static Result<CaseSensitiveStringEnum> get(String value)
        {
            return CaseSensitiveStringEnum.data.get(value);
        }

        public static Result<CaseSensitiveStringEnum> getOrCreate(String value)
        {
            return CaseSensitiveStringEnum.data.getOrCreate(value);
        }

        public static Iterator<CaseSensitiveStringEnum> iterateValues()
        {
            return CaseSensitiveStringEnum.data.iterateValues();
        }

        public static final CaseSensitiveStringEnum LowerCaseA = CaseSensitiveStringEnum.getOrCreate("a").await();
        public static final CaseSensitiveStringEnum UpperCaseA = CaseSensitiveStringEnum.getOrCreate("A").await();
    }

    public static class CaseInsensitiveStringEnum extends StringEnum<CaseInsensitiveStringEnum>
    {
        private static final StringEnumData<CaseInsensitiveStringEnum> data = StringEnumData.create(CaseInsensitiveStringEnum.class)
            .setGetValueKeyFunction(Strings::toLowerCase);

        public static Result<CaseInsensitiveStringEnum> get(String value)
        {
            return CaseInsensitiveStringEnum.data.get(value);
        }

        public static Result<CaseInsensitiveStringEnum> getOrCreate(String value)
        {
            return CaseInsensitiveStringEnum.data.getOrCreate(value);
        }

        public static Iterator<CaseInsensitiveStringEnum> iterateValues()
        {
            return CaseInsensitiveStringEnum.data.iterateValues();
        }

        public static final CaseInsensitiveStringEnum LowerCaseA = CaseInsensitiveStringEnum.getOrCreate("a").await();
        public static final CaseInsensitiveStringEnum UpperCaseA = CaseInsensitiveStringEnum.getOrCreate("A").await();
    }

    public static void test(TestRunner runner)
    {
        runner.testGroup(StringEnum.class, () ->
        {
            runner.test(CaseSensitiveStringEnum.class, (Test test) ->
            {
                test.assertNotSame(CaseSensitiveStringEnum.LowerCaseA, CaseSensitiveStringEnum.UpperCaseA);

                test.assertEqual(
                    Iterable.create(
                        CaseSensitiveStringEnum.LowerCaseA,
                        CaseSensitiveStringEnum.UpperCaseA),
                    CaseSensitiveStringEnum.iterateValues().toList());

                test.assertSame(CaseSensitiveStringEnum.LowerCaseA, CaseSensitiveStringEnum.get("a").await());
                test.assertSame(CaseSensitiveStringEnum.UpperCaseA, CaseSensitiveStringEnum.get("A").await());
                test.assertThrows(() -> CaseSensitiveStringEnum.get("nope").await(),
                    new NotFoundException("No CaseSensitiveStringEnum enum value found for \"nope\"."));

                test.assertEqual(
                    Iterable.create(
                        CaseSensitiveStringEnum.LowerCaseA,
                        CaseSensitiveStringEnum.UpperCaseA),
                    CaseSensitiveStringEnum.iterateValues().toList());

                final CaseSensitiveStringEnum caseSensitiveLowerCaseB = CaseSensitiveStringEnum.getOrCreate("b").await();
                test.assertNotNull(caseSensitiveLowerCaseB);
                test.assertEqual("b", caseSensitiveLowerCaseB.toString());
                test.assertSame(caseSensitiveLowerCaseB, CaseSensitiveStringEnum.get("b").await());
                test.assertSame(caseSensitiveLowerCaseB, CaseSensitiveStringEnum.getOrCreate("b").await());
                test.assertThrows(() -> CaseSensitiveStringEnum.get("B").await(),
                    new NotFoundException("No CaseSensitiveStringEnum enum value found for \"B\"."));

                test.assertEqual(
                    Iterable.create(
                        CaseSensitiveStringEnum.LowerCaseA,
                        CaseSensitiveStringEnum.UpperCaseA,
                        caseSensitiveLowerCaseB),
                    CaseSensitiveStringEnum.iterateValues().toList());
            });

            runner.test(CaseInsensitiveStringEnum.class, (Test test) ->
            {
                test.assertSame(CaseInsensitiveStringEnum.LowerCaseA, CaseInsensitiveStringEnum.UpperCaseA);

                test.assertEqual(
                    Iterable.create(
                        CaseInsensitiveStringEnum.LowerCaseA),
                    CaseInsensitiveStringEnum.iterateValues().toList());

                test.assertSame(CaseInsensitiveStringEnum.LowerCaseA, CaseInsensitiveStringEnum.get("a").await());
                test.assertSame(CaseInsensitiveStringEnum.UpperCaseA, CaseInsensitiveStringEnum.get("A").await());
                test.assertThrows(() -> CaseInsensitiveStringEnum.get("nope").await(),
                    new NotFoundException("No CaseInsensitiveStringEnum enum value found for \"nope\"."));

                test.assertEqual(
                    Iterable.create(
                        CaseInsensitiveStringEnum.LowerCaseA),
                    CaseInsensitiveStringEnum.iterateValues().toList());

                final CaseInsensitiveStringEnum caseInsensitiveLowerCaseB = CaseInsensitiveStringEnum.getOrCreate("b").await();
                test.assertNotNull(caseInsensitiveLowerCaseB);
                test.assertEqual("b", caseInsensitiveLowerCaseB.toString());
                test.assertSame(caseInsensitiveLowerCaseB, CaseInsensitiveStringEnum.get("b").await());
                test.assertSame(caseInsensitiveLowerCaseB, CaseInsensitiveStringEnum.getOrCreate("b").await());
                test.assertSame(caseInsensitiveLowerCaseB, CaseInsensitiveStringEnum.get("B").await());
                test.assertSame(caseInsensitiveLowerCaseB, CaseInsensitiveStringEnum.getOrCreate("B").await());

                final CaseInsensitiveStringEnum caseInsensitiveUpperCaseC = CaseInsensitiveStringEnum.getOrCreate("C").await();
                test.assertNotNull(caseInsensitiveUpperCaseC);
                test.assertEqual("C", caseInsensitiveUpperCaseC.toString());
                test.assertSame(caseInsensitiveUpperCaseC, CaseInsensitiveStringEnum.get("c").await());
                test.assertSame(caseInsensitiveUpperCaseC, CaseInsensitiveStringEnum.getOrCreate("c").await());
                test.assertSame(caseInsensitiveUpperCaseC, CaseInsensitiveStringEnum.get("C").await());
                test.assertSame(caseInsensitiveUpperCaseC, CaseInsensitiveStringEnum.getOrCreate("C").await());

                test.assertEqual(
                    Iterable.create(
                        CaseInsensitiveStringEnum.LowerCaseA,
                        caseInsensitiveLowerCaseB,
                        caseInsensitiveUpperCaseC),
                    CaseInsensitiveStringEnum.iterateValues().toList());
            });
        });
    }
}
