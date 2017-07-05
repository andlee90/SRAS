package Resources;

/**
 * Builds or gets an instance of an appropriate device controller.
 */
public class DeviceControllerFactory
{
    public enum DeviceType
    {
        LED, ARM
    }

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

    public static DeviceController getDeviceController(DeviceType dt)
    {
        if(dt == DeviceType.LED)
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
        else if(dt ==  DeviceType.ARM)
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
