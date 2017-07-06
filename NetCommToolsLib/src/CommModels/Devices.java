package CommModels;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Data model for holding a list of devices as well as info about each device including it's name, type and pins.
 */
public class Devices implements Serializable
{
    private static ArrayList<Device> devices = new ArrayList<>();

    public Devices(ArrayList<Device> d)
    {
        devices = d;
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

    public class Device
    {
        private int deviceId;
        private int devicePin;
        private String deviceName;
        private String deviceType;
        private String deviceStatus;

        public Device(int dp, String dn, String dt, String ds)
        {
            deviceId = -1;
            this.devicePin = dp;
            this.deviceName = dn;
            this.deviceType = dt;
            this.deviceStatus = ds;
        }

        public int getDeviceId()
        {
            return deviceId;
        }

        public int getDevicePin()
        {
            return devicePin;
        }

        public String getDeviceName()
        {
            return deviceName;
        }

        public String getDeviceType()
        {
            return deviceType;
        }

        public String getDeviceStatus()
        {
            return deviceStatus;
        }

        public void setDevicePin(int p)
        {
            this.devicePin = p;
        }

        public void setDeviceName(String dn)
        {
            this.deviceName = dn;
        }

        public void setDeviceType(String dt)
        {
            this.deviceType = dt;
        }

        public void setDeviceStatus(String ds)
        {
            this.deviceStatus = ds;
        }
    }
}
