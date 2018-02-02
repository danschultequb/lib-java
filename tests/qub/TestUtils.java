package qub;

public class TestUtils
{
    public static final Action0 nullAction0 = null;

    public static final Action1<Integer> nullAction1 = null;

    public static final Function0<Integer> nullFunction0 = null;

    public static final Function1<Integer,Integer> nullFunction1 = null;

    public static final Action0 emptyAction0 = new Action0()
    {
        @Override
        public void run()
        {
        }
    };

    public static final Action1<Integer> emptyAction1 = new Action1<Integer>()
    {
        @Override
        public void run(Integer arg1)
        {
        }
    };

    public static final Function0<Integer> emptyFunction0 = emptyFunction0(0);

    public static Function0<Integer> emptyFunction0(final int returnValue)
    {
        return new Function0<Integer>()
        {
            @Override
            public Integer run()
            {
                return returnValue;
            }
        };
    }

    public static final Function1<Integer,Integer> emptyFunction1 = emptyFunction1(0);

    public static Function1<Integer,Integer> emptyFunction1(final int returnValue)
    {
        return new Function1<Integer, Integer>()
        {
            @Override
            public Integer run(Integer arg1)
            {
                return returnValue;
            }
        };
    }

    public static Action0 setValueAction0(final Value<Integer> value, final int valueToSet)
    {
        return new Action0()
        {
            @Override
            public void run()
            {
                value.set(valueToSet);
            }
        };
    }

    public static Function0<Integer> setValueFunction0(Value<Integer> value, int valueToSet)
    {
        return setValueFunction0(value, valueToSet, valueToSet);
    }

    public static Function0<Integer> setValueFunction0(final Value<Integer> value, final int valueToSet, final int valueToReturn)
    {
        return new Function0<Integer>()
        {
            @Override
            public Integer run()
            {
                value.set(valueToSet);
                return valueToReturn;
            }
        };
    }
}
