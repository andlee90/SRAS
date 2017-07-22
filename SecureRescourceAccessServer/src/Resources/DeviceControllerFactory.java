package Resources;

import CommModels.Device;

/**
 * Builds or gets an instance of an appropriate device controller.
 */
public class DeviceControllerFactory
{
    private volatile static DeviceController LEDController;
    private volatile static DeviceController ARMController;

    private static DeviceController createLEDController(Device device)
    {
        return new LEDController(device);
    }

    private static DeviceController createARMController()
    {
        return new ARMController();
    }

    public static DeviceController getDeviceController(Device device)
    {

        if(device.getDeviceType() == Device.DeviceType.LED)
        {
            if(LEDController == null)
            {
                synchronized (DeviceController.class)
                {
                    if(LEDController == null)
                    {
                        LEDController = createLEDController(device);
                    }
                }
            }
            return LEDController;
        }
        else if(device.getDeviceType() == Device.DeviceType.ARM)
        {
            if(ARMController == null)
            {
                synchronized (DeviceController.class)
                {
                    if(ARMController == null)
                    {
                        ARMController = createARMController();
                    }
                }
            }
            return ARMController;
        }
        else
        {
            return null;
        }
    }
}
