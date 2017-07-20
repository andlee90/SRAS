package Resources;

import CommModels.Device;

/**
 * Builds or gets an instance of an appropriate device controller.
 */
public class DeviceControllerFactory
{
    private volatile static DeviceController LEDController;
    private volatile static DeviceController ARMController;

    private static DeviceController createLEDController()
    {
        return new LEDController();
    }

    private static DeviceController createARMController()
    {
        return new ARMController();
    }

    public static DeviceController getDeviceController(Device.DeviceType dt)
    {
        if(dt == Device.DeviceType.LED)
        {
            if(LEDController == null)
            {
                synchronized (DeviceController.class)
                {
                    if(LEDController == null)
                    {
                        LEDController = createLEDController();
                    }
                }
            }
            return LEDController;
        }
        else if(dt == Device.DeviceType.ARM)
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
