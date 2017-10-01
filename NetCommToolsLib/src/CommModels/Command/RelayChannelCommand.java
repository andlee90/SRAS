package CommModels.Command;

import java.io.Serializable;

/**
 * Command implementation, holds commands for an LED.
 */
public class RelayChannelCommand implements Command<RelayChannelCommandType>, Serializable
{
    private RelayChannelCommandType commandType; // Type of command

    @Override
    public RelayChannelCommandType getCommandType()
    {
        return commandType;
    }

    @Override
    public void setCommandType(RelayChannelCommandType ct)
    {
        this.commandType = ct;
    }
}
