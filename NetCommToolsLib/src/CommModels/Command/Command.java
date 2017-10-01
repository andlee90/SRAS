package CommModels;

/**
 * Created by asmith on 9/29/17.
 */
public interface Command<E extends Enum<E>>
{
    public E getCommandType();
    public void setCommandType(E ct);
}
