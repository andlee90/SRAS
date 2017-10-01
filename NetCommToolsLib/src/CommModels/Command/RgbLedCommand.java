package CommModels.Command;

import java.io.Serializable;

/**
 * Command implementation, holds commands for an RGBLED.
 */
public class RgbLedCommand implements Command<RgbLedCommandType>, Serializable
{
    private RgbLedCommandType commandType; // Type of command

    @Override
    public RgbLedCommandType getCommandType()
    {
        return commandType;
    }

    @Override
    public void setCommandType(RgbLedCommandType ct)
    {
        this.commandType = ct;
    }
}
