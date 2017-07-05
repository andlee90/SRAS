package Resources;

/**
 * An interface for all controller classes.
 */
public interface DeviceController
{
    enum CommandType
    {
        POWER_ON, POWER_OFF, BLINK
    }

    boolean isAvailable();
    void issueCommand(CommandType ct) throws InterruptedException;
}
