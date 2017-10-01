package Devices;

import CommModels.Device.Device;
import CommModels.Device.Led;
import CommModels.Device.RelayChannel;
import CommModels.Device.RgbLed;

import java.util.Hashtable;
import java.util.Map;

/**
 * Builds or gets an instance of an appropriate device controller. Ensures that only one instance of a controller is
 * built by storing controller object in a Hastable after they are built for the first time and then returning them as
 * needed.
 */
public class DeviceControllerFactory
{
    private volatile static Hashtable<DeviceController, Integer> controllerTable = new Hashtable<>();

    private static DeviceController createLedController(Device device)
    {
        LedController ledController = new LedController(device);
        controllerTable.put(ledController, device.getDeviceId());

        return ledController;
    }

    private static DeviceController createRgbLedController(Device device)
    {
        RgbLedController rgbLedController = new RgbLedController(device);
        controllerTable.put(rgbLedController, device.getDeviceId());

        return rgbLedController;
    }

    private static DeviceController createRelayModuleController(Device device)
    {
        RelayModuleController relayModuleController = new RelayModuleController(device);
        controllerTable.put(relayModuleController, device.getDeviceId());

        return relayModuleController;
    }

    public static synchronized DeviceController getDeviceController(Device device)
    {
        DeviceController dc = null;

        if(controllerTable.contains(device.getDeviceId()))
        {
            for (Map.Entry entry : controllerTable.entrySet())
            {
                if (entry.getValue().equals(device.getDeviceId()))
                {
                    dc = (DeviceController) entry.getKey();
                }
            }
            return dc;
        }

        else if(device instanceof Led)
        {
            return createLedController(device);
        }
        else if(device instanceof RgbLed)
        {
            return createRgbLedController(device);
        }
        else if(device instanceof RelayChannel)
        {
            return createRelayModuleController(device);
        }
        else
        {
            return null;
        }
    }
}
