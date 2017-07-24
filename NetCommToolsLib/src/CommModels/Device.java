package CommModels;

public interface Device<T, E extends Enum<E>>
{
    public int getDeviceId();
    public T getDevicePin();
    public String getDeviceName();
    public DeviceStatus getDeviceStatus();
    public E getDeviceState();

    public void setDeviceId(int id);
    public void setDevicePin(T pin);
    public void setDeviceName(String deviceName);
    public void setDeviceStatus(DeviceStatus deviceStatus);
    public void setDeviceState(E deviceState);
}
