package Resources;

import CommModels.Arm;
import CommModels.Device;
import CommModels.Led;

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

        else if(device instanceof Led)
        {
            return createLEDController(device);
        }
        else if(device instanceof Arm)
        {
            return createARMController(device);
        }
        else
        {
            return null;
        }
    }
}
