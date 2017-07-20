package Resources;

import CommModels.Command;

/**
 * Class for controlling a robot arm.
 */
//TODO: Implement

public class ARMController implements DeviceController
{
    @Override
    public boolean isAvailable()
    {
        return false;
    }

    @Override
    public void issueCommand(Command.CommandType ct)
    {

    }
}
