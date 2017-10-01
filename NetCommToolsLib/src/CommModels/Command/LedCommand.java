package CommModels.Command;

import java.io.Serializable;

/**
 * Command implementation, holds commands for single relay module.
 */
public class LedCommand implements Command<LedCommandType>, Serializable
{
    private LedCommandType commandType; // Type of command

    public LedCommand(LedCommandType ct)
    {
        this.commandType = ct;
    }

    @Override
    public LedCommandType getCommandType()
    {
        return commandType;
    }

    @Override
    public void setCommandType(LedCommandType ct)
    {
        this.commandType = ct;
    }
}
