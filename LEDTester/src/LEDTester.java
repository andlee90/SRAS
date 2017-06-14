/**
 * Created by andrewsmith on 6/5/17.
 */

/**
 * This example code demonstrates how to perform simple state
 * control of a GPIO pin on the Raspberry Pi.
 *
 * @author Robert Savage
 */

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

public class LEDTester
{
    public static void main(String[] args) throws InterruptedException
    {
        System.out.println("<--Pi4J--> LEDTester ... started.");

        // create gpio controller
        final GpioController gpio = GpioFactory.getInstance();

        // provision gpio pins as an output pins and turn off
        final GpioPinDigitalOutput pin1 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01, "MyLED", PinState.LOW);
        final GpioPinDigitalOutput pin2 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_02, "MyLED", PinState.LOW);
        final GpioPinDigitalOutput pin3 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_03, "MyLED", PinState.LOW);
        final GpioPinDigitalOutput pin4 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_04, "MyLED", PinState.LOW);
        final GpioPinDigitalOutput pin5 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_05, "MyLED", PinState.LOW);
        final GpioPinDigitalOutput pin14 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_14, "MyLED", PinState.LOW);
        final GpioPinDigitalOutput pin7 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_07, "MyLED", PinState.LOW);
        final GpioPinDigitalOutput pin8 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_08, "MyLED", PinState.LOW);

        // set shutdown state for pins
        pin1.setShutdownOptions(true, PinState.LOW);
        pin2.setShutdownOptions(true, PinState.LOW);
        pin3.setShutdownOptions(true, PinState.LOW);
        pin4.setShutdownOptions(true, PinState.LOW);
        pin5.setShutdownOptions(true, PinState.LOW);
        pin14.setShutdownOptions(true, PinState.LOW);
        pin7.setShutdownOptions(true, PinState.LOW);
        pin8.setShutdownOptions(true, PinState.LOW);

        System.out.println("Turning on pins, one by one.");

        // turn pins on
        pin1.toggle();
        Thread.sleep(5000);
        pin2.toggle();
        Thread.sleep(5000);
        pin3.toggle();
        Thread.sleep(5000);
        pin4.toggle();
        Thread.sleep(5000);
        pin5.toggle();
        Thread.sleep(5000);
        pin14.toggle();
        Thread.sleep(5000);
        pin7.toggle();
        Thread.sleep(5000);
        pin8.toggle();
        Thread.sleep(5000);

        System.out.println("Turning off pins, one by one.");

        // turn pins off
        pin1.toggle();
        Thread.sleep(5000);
        pin2.toggle();
        Thread.sleep(5000);
        pin3.toggle();
        Thread.sleep(5000);
        pin4.toggle();
        Thread.sleep(5000);
        pin5.toggle();
        Thread.sleep(5000);
        pin14.toggle();
        Thread.sleep(5000);
        pin7.toggle();
        Thread.sleep(5000);
        pin8.toggle();
        Thread.sleep(20000);

        System.out.println("Blinky blinky.");

        // blink pins
        pin1.blink(100);
        pin2.blink(100);
        pin3.blink(100);
        pin4.blink(100);
        pin5.blink(100);
        pin14.blink(100);
        pin7.blink(100);
        pin8.blink(100);

        Thread.sleep(200000);

        System.out.println("Shuttin er down.");

        // shut er down
        gpio.shutdown();

        System.out.println("Exiting LEDTester");
    }
}


