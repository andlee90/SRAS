package com.sras.sras_androidclient.Services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Inet4Address;
import java.net.Socket;

import CommModels.*;

public class ServerConnectionService extends Service
{
    private String mServerAddress;
    private int mServerPort;
    private String mUsername;
    private String mPassword;

    private Socket mSocket;
    private volatile ObjectOutputStream mOutputStream;
    private volatile ObjectInputStream mInputStream;

    private final IBinder mBinder = new ServerConnectionBinder();

    public class ServerConnectionBinder extends Binder
    {
        public ServerConnectionService getService()
        {
            return ServerConnectionService.this;
        }
    }

    private class EstablishConnectionThread extends Thread
    {
        private volatile Devices devices = new Devices();

        @Override
        public void run()
        {
            try
            {
                mSocket = new Socket(mServerAddress, mServerPort);
                mOutputStream = new ObjectOutputStream(mSocket.getOutputStream());
                mInputStream = new ObjectInputStream(mSocket.getInputStream());

                Message message = new Message(Inet4Address.getLocalHost().getHostAddress());

                if (isAuthenticated(mUsername, mPassword))
                {
                    mOutputStream.writeObject(message);
                    devices = (Devices) mInputStream.readObject();
                }

            }
            catch (IOException | ClassNotFoundException e)
            {
                e.printStackTrace();
            }
        }

        Devices getDevices()
        {
            return devices;
        }
    }

    private class FetchResourcesThread extends Thread
    {
        private volatile Device device;
        Message message = new Message("");

        FetchResourcesThread(Device d)
        {
            this.device = d;
        }

        @Override
        public void run()
        {
            try
            {
                mOutputStream.writeObject(device);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        Message getMessage()
        {
            return message;
        }
    }

    private class IssueCommandThread extends Thread
    {
        private volatile Command command;
        private volatile Device device = null;

        IssueCommandThread(Command c)
        {
            this.command = c;
        }

        @Override
        public void run()
        {
            try
            {
                mOutputStream.writeObject(command);
                device = (Device) mInputStream.readObject();

            }
            catch (IOException | ClassNotFoundException e)
            {
                e.printStackTrace();
            }
        }

        Device getDevice()
        {
            return device;
        }
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return mBinder;
    }

    public Devices connectToServer() throws IOException, ClassNotFoundException, InterruptedException
    {
        EstablishConnectionThread ct = new EstablishConnectionThread();
        Thread t = new Thread(ct);
        t.start();
        t.join(10000);

        return ct.getDevices();
    }

    public Message fetchResources(Device device) throws IOException, ClassNotFoundException, InterruptedException
    {
        FetchResourcesThread frt = new FetchResourcesThread(device);
        Thread t = new Thread(frt);
        t.start();
        t.join(10000);

        return frt.getMessage();
    }

    public Device issueCommand(Command command) throws IOException, ClassNotFoundException, InterruptedException
    {
        IssueCommandThread ict = new IssueCommandThread(command);
        Thread t = new Thread(ict);
        t.start();
        t.join(10000);

        return ict.getDevice();
    }

    public void setParams(String addr, int port, String user, String pass)
    {
        this.mServerAddress = addr;
        this.mServerPort = port;
        this.mUsername = user;
        this.mPassword = pass;
    }

    private boolean isAuthenticated(String un, String p) throws IOException, ClassNotFoundException
    {
        User user = new User(un, p, "", "", "", "");
        mOutputStream.writeObject(user);
        user = (User)mInputStream.readObject();

        return user.getValidity();
    }

    public void closeServer() throws IOException
    {
        mOutputStream.close();
        mInputStream.close();
        mSocket.close();
    }
}
