package qub;

public class JavaMouseEvent implements MouseEvent
{
    private final java.awt.event.MouseEvent event;

    public JavaMouseEvent(java.awt.event.MouseEvent event)
    {
        PreCondition.assertNotNull(event, "event");

        this.event = event;
    }

    public java.awt.event.MouseEvent getJavaEvent()
    {
        return event;
    }
}
