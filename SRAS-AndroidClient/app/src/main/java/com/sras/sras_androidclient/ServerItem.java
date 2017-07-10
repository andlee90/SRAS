package com.sras.sras_androidclient;

public class ServerItem
{
    private String name;
    private String address;
    private int port;
    private String username;
    private String password;

    public ServerItem(String n, String a, int p, String user, String pass)
    {
        this.name = n;
        this.address = a;
        this.port = p;
        this.username = user;
        this.password = pass;
    }

    public String getName()
    {
        return this.name;
    }

    public String getAddress()
    {
        return this.address;
    }

    public int getPort()
    {
        return this.port;
    }

    public String getUsername()
    {
        return this.username;
    }

    public void setName(String n)
    {
        this.name = n;
    }

    public void setAddress(String a)
    {
        this.address = a;
    }

    public void setPort(int p)
    {
        this.port = p;
    }

    public void setUsername(String user)
    {
        this.username = user;
    }

    public void setPassword(String pass)
    {
        this.password = pass;
    }
}
