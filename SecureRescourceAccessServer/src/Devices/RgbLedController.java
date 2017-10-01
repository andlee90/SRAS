package Devices;

import CommModels.Command.RgbLedCommandType;
import CommModels.Device.*;
import Database.DBHelper;
import Main.Main;
import com.pi4j.io.gpio.*;
import com.pi4j.wiringpi.SoftPwm;

/**
 * Controller implementation for an rgb led.
 */

public class RgbLedController implements DeviceController
{
    private volatile RgbLed device;            // The device being controlled.
    private GpioController gpio;               // The controller for the device.
    private GpioPinDigitalOutput redPin;       // The first pin to which the device in connected.
    private GpioPinDigitalOutput greenPin;     // The second pin to which the device in connected.
    private GpioPinDigitalOutput bluePin;      // The third pin to which the device in connected.


    RgbLedController(Device d)
    {
        this.device = (RgbLed) d;

        int r = device.getDevicePin().get(0);
        int g = device.getDevicePin().get(1);
        int b = device.getDevicePin().get(2);

        gpio = GpioFactory.getInstance();

        redPin = gpio.provisionDigitalOutputPin(Main.getGpioPin(r), device.getDeviceName(), PinState.LOW);
        redPin.setShutdownOptions(true, PinState.LOW);

        greenPin = gpio.provisionDigitalOutputPin(Main.getGpioPin(g), device.getDeviceName(), PinState.LOW);
        greenPin.setShutdownOptions(true, PinState.LOW);

        bluePin = gpio.provisionDigitalOutputPin(Main.getGpioPin(b), device.getDeviceName(), PinState.LOW);
        bluePin.setShutdownOptions(true, PinState.LOW);
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
        // If the device is on or blinking, first turn it off
        redPin.blink(0);
        greenPin.blink(0);
        bluePin.blink(0);
        redPin.setState(PinState.LOW);
        greenPin.setState(PinState.LOW);
        bluePin.setState(PinState.LOW);
        device.setDeviceState(RgbLedState.OFF);

        switch((RgbLedCommandType)ct)
        {
            case TOGGLE_RED:
                redPin.toggle();
                device.setDeviceState(RgbLedState.ON_RED);
            case TOGGLE_GREEN:
                greenPin.toggle();
                device.setDeviceState(RgbLedState.ON_GREEN);
            case TOGGLE_BLUE:
                bluePin.toggle();
                device.setDeviceState(RgbLedState.ON_BLUE);
            case TOGGLE_MAGENTA:
                bluePin.toggle();
                redPin.toggle();
                device.setDeviceState(RgbLedState.ON_MAGENTA);
            case TOGGLE_YELLOW:
                redPin.toggle();
                greenPin.toggle();
                device.setDeviceState(RgbLedState.ON_YELLOW);
            case TOGGLE_CYAN:
                bluePin.toggle();
                greenPin.toggle();
                device.setDeviceState(RgbLedState.ON_CYAN);
            case TOGGLE_WHITE:
                redPin.toggle();
                greenPin.toggle();
                bluePin.toggle();
                device.setDeviceState(RgbLedState.ON_WHITE);
            case BLINK_RED:
                redPin.blink(100);
                device.setDeviceState(RgbLedState.BLINKING_RED);
            case BLINK_GREEN:
                greenPin.blink(100);
                device.setDeviceState(RgbLedState.BLINKING_GREEN);
            case BLINK_BLUE:
                bluePin.blink(100);
                device.setDeviceState(RgbLedState.BLINKING_BLUE);
            case BLINK_MAGENTA:
                redPin.blink(100);
                bluePin.blink(100);
                device.setDeviceState(RgbLedState.BLINKING_MAGENTA);
            case BLINK_YELLOW:
                redPin.blink(100);
                greenPin.blink(100);
                device.setDeviceState(RgbLedState.BLINKING_YELLOW);
            case BLINK_CYAN:
                greenPin.blink(100);
                bluePin.blink(100);
                device.setDeviceState(RgbLedState.BLINKING_CYAN);
            case BLINK_WHITE:
                redPin.blink(100);
                greenPin.blink(100);
                bluePin.blink(100);
                device.setDeviceState(RgbLedState.BLINKING_WHITE);
            case SPECTRUM_CYCLE:
                int r = device.getDevicePin().get(0);
                int g = device.getDevicePin().get(1);
                int b = device.getDevicePin().get(2);
                try
                {
                    SoftPwm.softPwmCreate(r,0, 100);
                    SoftPwm.softPwmCreate(g,0, 100);
                    SoftPwm.softPwmCreate(b,0, 100);

                    int i = 0;
                    while (i <= 384)
                    {
                        int[] rgb = colorWheel(i);
                        SoftPwm.softPwmWrite(r, (rgb[0]/128)*100);
                        SoftPwm.softPwmWrite(g, (rgb[1]/128)*100);
                        SoftPwm.softPwmWrite(b, (rgb[2]/128)*100);
                        Thread.sleep(100);
                        i++;
                    }
                }
                catch (Exception e)
                {
                     e.printStackTrace();
                }
        }

        System.out.println("> [" + Main.getDate() + "] "
                + device.getDeviceName() + " on "
                + device.getDevicePin() + " is "
                + device.getDeviceState());

        DBHelper.updateDevice(device.getDeviceId(),
                -1,
                device.getDeviceName(),
                DeviceType.RGB_LED.toString(),
                device.getDeviceStatus().toString(),
                device.getDeviceState().toString());

        return device.getDeviceState();
    }

    private int[] colorWheel(int position)
    {
        int r, g, b;

        if (position < 0) {position = 0;}
        if (position > 384) {position = 384;}

        if (position < 128)
        {
            r = 127 - position % 128;
            g = position % 128;
            b = 0;
        }
        else if(position < 256)
        {
            r = 0;
            g = 127 - position % 128;
            b = position % 128;
        }
        else
        {
            r = position % 128;
            g = 0;
            b = 127 - position % 128;
        }

        return new int[]{r, g, b};
    }
}
