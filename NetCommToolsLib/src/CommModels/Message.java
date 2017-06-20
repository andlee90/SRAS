package CommModels;

import java.io.Serializable;

/**
 * Class for holding a general message.
 */
public class Message implements Serializable
{
    private String message; // message to pass to client or server

    public Message(String m)
    {
        this.message = m;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String m)
    {
        this.message = m;
    }
}
