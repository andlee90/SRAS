package Resources;

import CommModels.Device;

import java.util.Hashtable;
import java.util.Map;

/**
 * Builds or gets an instance of an appropriate device controller.
 */
public class DeviceControllerFactory
{
    private volatile static Hashtable<DeviceController, Integer> controllerTable = new Hashtable<>();

    private static DeviceController createLEDController(Device device)
    {
        LEDController ledController = new LEDController(device);
        controllerTable.put(ledController, device.getDeviceId());

        return ledController;
    }

    private static DeviceController createARMController(Device device)
    {
        ARMController armController = new ARMController(device);
        controllerTable.put(armController, device.getDeviceId());

        return armController;
    }

    public static DeviceController getDeviceController(Device device)
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

        else if(device.getDeviceType() == Device.DeviceType.LED)
        {
            return createLEDController(device);
        }
        else if(device.getDeviceType() == Device.DeviceType.ARM)
        {
            return createARMController(device);
        }
        else
        {
            return null;
        }
    }
}
