package Resources;

import CommModels.Command;

/**
 * An interface for all controller classes.
 */
public interface DeviceController<E extends Enum<E>>
{
    boolean isAvailable();
    E issueCommand(Command.CommandType ct) throws InterruptedException;
}
