package qub;

public class IntegerToUnicodeCodePointIterator implements UnicodeCodePointIterator
{
    private final Iterator<Integer> integers;
    private Integer currentUnicodeCodePoint;
    private boolean returnByteOrderMark;
    private Boolean foundByteOrderMark;
    private boolean throwOnNullIntegers;

    private IntegerToUnicodeCodePointIterator(Iterator<Integer> integers)
    {
        PreCondition.assertNotNull(integers, "integers");

        this.integers = integers;
        this.throwOnNullIntegers = true;
    }

    public static IntegerToUnicodeCodePointIterator create(int... integers)
    {
        PreCondition.assertNotNull(integers, "integers");

        return IntegerToUnicodeCodePointIterator.create(Iterator.create(integers));
    }

    public static IntegerToUnicodeCodePointIterator create(Iterator<Integer> integers)
    {
        PreCondition.assertNotNull(integers, "integers");

        return new IntegerToUnicodeCodePointIterator(integers);
    }

    @Override
    public IntegerToUnicodeCodePointIterator setReturnByteOrderMark(boolean returnByteOrderMark)
    {
        this.returnByteOrderMark = returnByteOrderMark;

        return this;
    }

    @Override
    public boolean getReturnByteOrderMark()
    {
        return this.returnByteOrderMark;
    }

    public IntegerToUnicodeCodePointIterator setThrowOnNullIntegers(boolean throwOnNullIntegers)
    {
        this.throwOnNullIntegers = throwOnNullIntegers;

        return this;
    }

    public boolean getThrowOnNullIntegers()
    {
        return this.throwOnNullIntegers;
    }

    @Override
    public Boolean foundByteOrderMark()
    {
        if (this.foundByteOrderMark == null && this.integers.hasStarted())
        {
            if (this.integers.hasCurrent())
            {
                this.populateCurrentUnicodeCodePoint();
            }
            else
            {
                this.foundByteOrderMark = false;
            }
        }
        return this.foundByteOrderMark;
    }

    @Override
    public boolean hasStarted()
    {
        return this.integers.hasStarted();
    }

    @Override
    public boolean hasCurrent()
    {
        return this.currentUnicodeCodePoint != null || this.integers.hasCurrent();
    }

    @Override
    public Integer getCurrent()
    {
        PreCondition.assertTrue(this.hasCurrent(), "this.hasCurrent()");

        if (this.currentUnicodeCodePoint == null)
        {
            this.populateCurrentUnicodeCodePoint();
        }
        return this.currentUnicodeCodePoint;
    }

    @Override
    public boolean next()
    {
        this.currentUnicodeCodePoint = null;
        if (this.integers.next())
        {
            this.populateCurrentUnicodeCodePoint();
        }
        else if (this.foundByteOrderMark == null)
        {
            this.foundByteOrderMark = false;
        }

        PostCondition.assertNotNull(this.foundByteOrderMark(), "this.foundByteOrderMark()");

        return this.hasCurrent();
    }

    private void populateCurrentUnicodeCodePoint()
    {
        PreCondition.assertTrue(this.integers.hasCurrent(), "this.integers.hasCurrent()");

        boolean done = false;
        while (!done)
        {
            this.currentUnicodeCodePoint = this.integers.getCurrent();
            if (this.currentUnicodeCodePoint == null && this.throwOnNullIntegers)
            {
                throw new IllegalArgumentException("Cannot have a null Unicode code point.");
            }

            if (this.foundByteOrderMark != null)
            {
                done = true;
            }
            else
            {
                this.foundByteOrderMark = (this.currentUnicodeCodePoint != null && this.currentUnicodeCodePoint == 0xFEFF);

                if (!this.foundByteOrderMark || this.returnByteOrderMark)
                {
                    done = true;
                }
                else if (!this.integers.next())
                {
                    done = true;
                    this.currentUnicodeCodePoint = null;
                }
            }
        }
    }
}
