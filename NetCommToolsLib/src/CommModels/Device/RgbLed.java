package CommModels.Device;

import CommModels.User.User;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Data model for holding info about each RgbLed device including it's name, type and pins
 */
public class RgbLed implements Device<ArrayList<Integer>, RgbLedState>, Serializable
{
    private int deviceId;
    private int deviceListId;
    private ArrayList<Integer> devicePin;
    private String deviceName;
    private DeviceStatus deviceStatus;
    private RgbLedState deviceState;
    private User deviceUser;

    public RgbLed(int id, ArrayList<Integer> pin, String name, DeviceStatus status, RgbLedState state)
    {
        this.deviceId = id;
        this.deviceListId = -1;
        this.devicePin = pin;
        this.deviceName = name;
        this.deviceStatus = status;
        this.deviceState = state;
        this.deviceUser = null;
    }

    @Override
    public int getDeviceId()
    {
        return deviceId;
    }

    @Override
    public int getDeviceListId()
    {
        return deviceListId;
    }

    @Override
    public ArrayList<Integer> getDevicePin()
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
    public RgbLedState getDeviceState()
    {
        return deviceState;
    }

    @Override
    public User getDeviceUser()
    {
        return deviceUser;
    }

    @Override
    public void setDeviceListId(int id)
    {
        deviceListId = id;
    }

    @Override
    public void setDevicePin(ArrayList<Integer> p)
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
    public void setDeviceState(RgbLedState rls)
    {
        this.deviceState = rls;
    }

    @Override
    public void setDeviceUser(User deviceUser)
    {
        this.deviceUser = deviceUser;
    }
}
