package Resources;

import CommModels.Command;

/**
 * An interface for all controller classes.
 */
public interface DeviceController
{
    boolean isAvailable();
    void issueCommand(Command.CommandType ct) throws InterruptedException;
}
