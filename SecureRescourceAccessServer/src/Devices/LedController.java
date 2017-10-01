package Devices;

import CommModels.Command.LedCommandType;
import CommModels.Device.Device;
import CommModels.Device.DeviceStatus;
import CommModels.Device.Led;
import CommModels.Device.LedState;
import Database.DBHelper;
import Main.Main;
import com.pi4j.io.gpio.*;

/**
 * Implementation for controlling a single LED. Commented-out lines are commented to allow testing outside of a
 * Raspbian environment (In this case, no GPIO pins are available and an exception is thrown when these lines are
 * called). Uncomment before testing on a Pi.
 */
public class LedController implements DeviceController
{
    private volatile Led device;            // The device being controlled.
    private GpioController gpio;            // The controller for the device.
    private GpioPinDigitalOutput pin;       // The pin to which the device in connected.

    LedController(Device d)
    {
        this.device = (Led) d;

        gpio = GpioFactory.getInstance();
        pin = gpio.provisionDigitalOutputPin(Main.getGpioPin(device.getDevicePin()), device.getDeviceName(), PinState.LOW);
        pin.setShutdownOptions(true, PinState.LOW);
    }

    /**
     * Notifies the user about whether or not the device is available for use.
     */
    @Override
    public boolean isAvailable()
    {
        return device.getDeviceStatus().equals(DeviceStatus.AVAILABLE);
    }


    /**
     * Issues a command to the device.
     * @param ct the type of command being issued.
     */
    @Override
    public Enum issueCommand(Enum ct)
    {
        if (ct == LedCommandType.TOGGLE)
        {
            if (device.getDeviceState() == LedState.BLINKING)
            {
                // If the device is blinking, first turn it off
                pin.blink(0);
                pin.setState(PinState.LOW);
                device.setDeviceState(LedState.OFF);
            }

            else if (device.getDeviceState() == LedState.ON)
            {
                pin.toggle();
                device.setDeviceState(LedState.OFF);
            }

            else if (device.getDeviceState() == LedState.OFF)
            {
                pin.toggle();
                device.setDeviceState(LedState.ON);
            }

            System.out.println("> [" + Main.getDate() + "] "
                    + device.getDeviceName() + " on "
                    + device.getDevicePin() + " is "
                    + device.getDeviceState());
        }
        else if (ct == LedCommandType.BLINK)
        {
            device.setDeviceState(LedState.BLINKING);

            pin.blink(100);

            System.out.println("> [" + Main.getDate() + "] "
                    + device.getDeviceName() + " on "
                    + device.getDevicePin() + " is "
                    + device.getDeviceState());
        }

        DBHelper.updateDevice(device.getDeviceId(),
                device.getDevicePin(),
                device.getDeviceName(),
                "LED",
                device.getDeviceStatus().toString(),
                device.getDeviceState().toString());

        return device.getDeviceState();
    }
}
