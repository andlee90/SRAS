package Devices;

import CommModels.ArmState;
import CommModels.Command;
import CommModels.Device;

/**
 * Class for controlling a robot arm.
 */
//TODO: Implement

public class ARMController implements DeviceController
{
    private Device device;

    ARMController(Device d)
    {
        this.device = d;
    }

    @Override
    public boolean isAvailable()
    {
        return false;
    }

    @Override
    public ArmState issueCommand(Command.CommandType ct)
    {
        return ArmState.ON;
    }
}
