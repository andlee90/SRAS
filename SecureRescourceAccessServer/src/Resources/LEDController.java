package Resources;

import CommModels.Command;
import CommModels.Device;
import Main.Main;
import com.pi4j.io.gpio.*;

import java.util.ArrayList;

/**
 * Implementation for controlling a single LED.
 */
public class LEDController implements DeviceController
{
    private Device device;                  // The Device being controlled
    private GpioPinDigitalOutput pin;       // The pin to which the device in connected

    LEDController(Device d)
    {
        this.device = d;

        GpioController gpio = GpioFactory.getInstance();
        pin = gpio.provisionDigitalOutputPin(getGpioPin(device.getDevicePin()), "MyLED", PinState.LOW);
    }

    @Override
    public boolean isAvailable()
    {
        return device.getDeviceStatus().equals("available");
    }

    @Override
    public void issueCommand(Command.CommandType ct)
    {
        pin.setShutdownOptions(true, PinState.LOW);

        if (ct == Command.CommandType.TOGGLE)
        {
            System.out.println("> [" + Main.getDate() + "] "
                    + device.getDeviceName() + " on "
                    + device.getDevicePin() + ": pin state toggled.");
            pin.toggle();
        }
        else if (ct == Command.CommandType.BLINK)
        {
            System.out.println("> [" + Main.getDate() + "] "
                    + device.getDeviceName() + " on "
                    + device.getDevicePin() + ": pin is blinking.");
            pin.blink(100);
        }
    }

    /**
     * Converts Device.pin to gpio.pin
     * @param x pin int from Device to be converted
     * @return GPIO pin used for issuing commands
     */
    private Pin getGpioPin(int x)
    {
        Pin pin = null;
        ArrayList<Pin> pins = new ArrayList<>();
        pins.add(RaspiPin.GPIO_01);

        for (Pin pin1 : pins)
        {
            String pinString = pin1.toString();
            if (pinString.substring(pinString.length() - 1).equals(Integer.toString(x)))
            {
                pin = pin1;
            }
        }
        return pin;
    }
}
