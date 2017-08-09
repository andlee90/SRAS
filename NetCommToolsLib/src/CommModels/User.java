package CommModels;

import java.io.Serializable;
import java.util.Hashtable;

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
    private int rolePriority;
    private boolean isValid; // true if username and password have been validated (found in db)

    private Hashtable<String, String> rules = new Hashtable<>();

    public User(int id, String un, String p, String e, String fn, String ln, String r, int rp)
    {
        this.userId = id;
        this.userName = un;
        this.password = p;
        this.email = e;
        this.firstName = fn;
        this.lastName = ln;
        this.role = r;
        this.rolePriority = rp;
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

    public int getRolePriority()
    {
        return this.rolePriority;
    }

    public Hashtable<String, String> getRules()
    {
        return this.rules;
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

    public void setRolePriority(int rp)
    {
        this.rolePriority = rp;
    }

    public void setValidity(boolean v)
    {
        this.isValid = v;
    }

    public void setRules(Hashtable<String, String> r)
    {
        this.rules = r;
    }
}
