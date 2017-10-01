package CommModels;

public interface Device<T, E extends Enum<E>>
{
    public int getDeviceId();
    public int getDeviceListId();
    public T getDevicePin();
    public String getDeviceName();
    public DeviceStatus getDeviceStatus();
    public E getDeviceState();
    public User getDeviceUser();

    public void setDeviceListId(int id);
    public void setDevicePin(T pin);
    public void setDeviceName(String deviceName);
    public void setDeviceStatus(DeviceStatus deviceStatus);
    public void setDeviceState(E deviceState);
    public void setDeviceUser(User deviceUser);
}
