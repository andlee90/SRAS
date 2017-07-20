package Resources;

import CommModels.Command;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

/**
 * Class for controlling a single LED.
 */
public class LEDController implements DeviceController
{
    //final GpioController gpio = GpioFactory.getInstance();
    //final GpioPinDigitalOutput pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01, "MyLED", PinState.LOW);

    @Override
    public boolean isAvailable()
    {
        return true;
    }

    @Override
    public void issueCommand(Command.CommandType ct)
    {
        //pin.setShutdownOptions(true, PinState.LOW);

        switch (ct)
        {
            case POWER_ON:
                //pin.toggle();
            case POWER_OFF:
                //pin.toggle();
            case BLINK:
                //pin.blink(100);
        }
    }
}
