package Resources;

import CommModels.*;
import Main.Main;
import com.pi4j.io.gpio.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * Implementation for controlling a single LED.
 * Commented lines are commented to allow testing outside of a Raspbian environment. Uncomment before testing on a Pi.
 */
public class LEDController implements DeviceController
{
    private volatile Led device;            // The device being controlled.
    private GpioController gpio;            // The controller for the device.
    private GpioPinDigitalOutput pin;       // The pin to which the device in connected.
    private boolean state = false;          // True if ON, false if OFF

    LEDController(Device d)
    {
        this.device = (Led) d;

        //gpio = GpioFactory.getInstance();
        //pin = gpio.provisionDigitalOutputPin(getGpioPin(device.getDevicePin()), device.getDeviceName(), PinState.LOW);
        //pin.setShutdownOptions(true, PinState.LOW);
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
    public LedState issueCommand(Command.CommandType ct)
    {
        if (ct == Command.CommandType.TOGGLE)
        {
            //pin.toggle();

            if (device.getDeviceState() == LedState.BLINKING)
            {
                // If the device is blinking, first turn it off.
                // pin = gpio.provisionDigitalOutputPin(getGpioPin(device.getDevicePin()), device.getDeviceName(), PinState.LOW);
                state = true;
            }
            if (state)
            {
                device.setDeviceState(LedState.OFF);
                state = false;
            }
            else
            {
                device.setDeviceState(LedState.ON);
                state = true;
            }

            System.out.println("> [" + Main.getDate() + "] "
                    + device.getDeviceName() + " on "
                    + device.getDevicePin() + " is "
                    + device.getDeviceState());
        }
        else if (ct == Command.CommandType.BLINK)
        {
            device.setDeviceState(LedState.BLINKING);

            //pin.blink(100);

            System.out.println("> [" + Main.getDate() + "] "
                    + device.getDeviceName() + " on "
                    + device.getDevicePin() + " is "
                    + device.getDeviceState());
        }
        return device.getDeviceState();
    }

    /**
     * Converts Device.pin (int) to GPIO.pin (Pin).
     * @param x pin int from Device to be converted
     * @return GPIO Pin used for issuing commands
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
