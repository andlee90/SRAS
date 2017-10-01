package Devices;

import CommModels.Device.*;
import Database.DBHelper;
import Main.Main;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;

/**
 * Controller implementation for a single channel on a standard relay.
 */
public class RelayModuleController implements DeviceController
{
    private volatile RelayChannel device;       // The device being controlled.
    private GpioController gpio;                // The controller for the device.
    private GpioPinDigitalOutput pin;           // The pin to which the device in connected.

    RelayModuleController(Device d)
    {
        this.device = (RelayChannel) d;

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

    @Override
    public Enum issueCommand(Enum ct) throws InterruptedException
    {
        if (device.getDeviceState() == RelayChannelState.ON)
        {
            pin.toggle();
            device.setDeviceState(RelayChannelState.OFF);
        }

        else if (device.getDeviceState() == RelayChannelState.OFF)
        {
            pin.toggle();
            device.setDeviceState(RelayChannelState.ON);
        }

        System.out.println("> [" + Main.getDate() + "] "
                + device.getDeviceName() + " on "
                + device.getDevicePin() + " is "
                + device.getDeviceState());

        DBHelper.updateDevice(device.getDeviceId(),
                device.getDevicePin(),
                device.getDeviceName(),
                DeviceType.RELAY_MOD.toString(),
                device.getDeviceStatus().toString(),
                device.getDeviceState().toString());

        return device.getDeviceState();
    }
}
