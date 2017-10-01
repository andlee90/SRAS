package Devices;

/**
 * An interface for all controller classes.
 */
public interface DeviceController<E extends Enum<E>>
{
    boolean isAvailable();
    E issueCommand(Enum<E> commandType) throws InterruptedException;
}
