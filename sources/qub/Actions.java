package qub;

public class Actions
{
    Actions()
    {
    }

    /**
     * An Action0 that does nothing.
     */
    public static final Action0 empty = new Action0()
    {
        @Override
        public void run()
        {
        }
    };
}
