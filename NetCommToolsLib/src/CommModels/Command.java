package CommModels;

import java.io.Serializable;

/**
 * Data model for holding a command.
 */
public class Command implements Serializable
{
    /**
     * Enum to hold different command types.
     */
    public enum CommandType
    {
        TOGGLE, BLINK
    }

    private CommandType commandType; // Type of command

    public Command(CommandType ct)
    {
        this.commandType = ct;
    }

    public CommandType getCommandType()
    {
        return commandType;
    }

    public void setCommandType(CommandType ct)
    {
        this.commandType = ct;
    }
}
