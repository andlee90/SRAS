package CommModels;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Data model for holding a list of devices.
 */
public class Devices implements Serializable
{
    private ArrayList<Device> devices;

    public Devices()
    {
        this.devices = new ArrayList<>();
    }

    public ArrayList<Device> getDevices()
    {
        return devices;
    }

    public void setDevices(ArrayList<Device> d)
    {
        devices = d;
    }

    public void addDevice(Device d)
    {
        devices.add(d);
        d.deviceId = devices.indexOf(d);
    }

    public Device getDeviceById(int id)
    {
        return devices.get(id);
    }
}
