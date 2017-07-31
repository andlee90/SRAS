package CommModels;

import java.io.Serializable;

/**
 * Data model for holding info about a user including their username, password, first and last names, admin status and
 * associated schedule.
 */
public class User implements Serializable
{
    private int userId;
    private String userName;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private String role;
    private boolean isValid; // true if username and password have been validated (found in db)

    public User(int id, String un, String p, String e, String fn, String ln, String r)
    {
        this.userId = id;
        this.userName = un;
        this.password = p;
        this.email = e;
        this.firstName = fn;
        this.lastName = ln;
        this.role = r;
        this.isValid = false;
    }

    public int getUserId()
    {
        return this.userId;
    }

    public String getUserName()
    {
        return this.userName;
    }

    public String getPassword()
    {
        return this.password;
    }

    public String getEmail()
    {
        return this.email;
    }

    public String getFirstName()
    {
        return this.firstName;
    }

    public String getLastName()
    {
        return this.lastName;
    }

    public String getRole()
    {
        return this.role;
    }

    public boolean getValidity()
    {
        return this.isValid;
    }

    public void setEmail(String e)
    {
        this.email = e;
    }

    public void setFirstName(String fn)
    {
        this.firstName = fn;
    }

    public void setLastName(String ln)
    {
        this.lastName = ln;
    }

    public void setRole(String r)
    {
        this.role = r;
    }

    public void setValidity(boolean v)
    {
        this.isValid = v;
    }
}
