package Resources;

import CommModels.Command;
import CommModels.Device;
import CommModels.DeviceStatus;
import CommModels.Led;
import Main.Main;
import com.pi4j.io.gpio.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * Implementation for controlling a single LED.
 */
public class LEDController implements DeviceController
{
    private Led device;                  // The Device being controlled
    private GpioController gpio;
    private GpioPinDigitalOutput pin;       // The pin to which the device in connected

    LEDController(Device d)
    {
        this.device = (Led) d;

        //gpio = GpioFactory.getInstance();
        //pin = gpio.provisionDigitalOutputPin(getGpioPin(device.getDevicePin()), "MyLED", PinState.LOW);
    }

    @Override
    public boolean isAvailable()
    {
        return device.getDeviceStatus().equals(DeviceStatus.AVAILABLE);
    }

    @Override
    public void issueCommand(Command.CommandType ct)
    {
        //pin.setShutdownOptions(true, PinState.LOW);

        if (ct == Command.CommandType.TOGGLE)
        {
            System.out.println("> [" + Main.getDate() + "] "
                    + device.getDeviceName() + " on "
                    + device.getDevicePin() + ": pin state toggled.");
            //pin.toggle();
        }
        else if (ct == Command.CommandType.BLINK)
        {
            System.out.println("> [" + Main.getDate() + "] "
                    + device.getDeviceName() + " on "
                    + device.getDevicePin() + ": pin is blinking.");
            //pin.blink(100);
        }
    }

    /**
     * Converts Device.pin to gpio.pin
     * @param x pin int from Device to be converted
     * @return GPIO pin used for issuing commands
     */
    private Pin getGpioPin(int x)
    {
        Pin resultPin = null;
        Pin[] p = RaspiPin.allPins();
        ArrayList<Pin> pins = new ArrayList<>(Arrays.asList(p));
        Collections.sort(pins);

        for (Pin pin : pins)
        {
            String pinString = pin.toString();

            if(Integer.toString(x).length() == 2)
            {
                if (pinString.substring(pinString.length() - 2).equals(Integer.toString(x)))
                {
                    resultPin = pin;
                    break;
                }
                break;
            }
            else if (Integer.toString(x).length() == 1)
            {
                if (pinString.substring(pinString.length() - 1).equals(Integer.toString(x)))
                {
                    resultPin = pin;
                    break;
                }
            }
        }
        return resultPin;
    }
}
