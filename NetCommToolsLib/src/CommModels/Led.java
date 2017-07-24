package CommModels;

import java.io.Serializable;

/**
 * Data model for holding info about each device including it's name, type and pins
 */
public class Led implements Device<Integer, LedState>, Serializable
{
    int deviceId;
    private int devicePin;
    private String deviceName;
    private DeviceStatus deviceStatus;
    private LedState deviceState;

    public Led(int pin, String name, DeviceStatus status, LedState state)
    {
        deviceId = -1;
        this.devicePin = pin;
        this.deviceName = name;
        this.deviceStatus = status;
        this.deviceState = state;
    }

    @Override
    public int getDeviceId()
    {
        return deviceId;
    }

    @Override
    public Integer getDevicePin()
    {
        return devicePin;
    }

    @Override
    public String getDeviceName()
    {
        return deviceName;
    }

    @Override
    public DeviceStatus getDeviceStatus()
    {
        return deviceStatus;
    }

    @Override
    public LedState getDeviceState()
    {
        return deviceState;
    }

    @Override
    public void setDeviceId(int id)
    {
        deviceId = id;
    }

    @Override
    public void setDevicePin(Integer p)
    {
        this.devicePin = p;
    }

    @Override
    public void setDeviceName(String dn)
    {
        this.deviceName = dn;
    }

    @Override
    public void setDeviceStatus(DeviceStatus ds)
    {
        this.deviceStatus = ds;
    }

    @Override
    public void setDeviceState(LedState ls)
    {
        this.deviceState = ls;
    }
}
