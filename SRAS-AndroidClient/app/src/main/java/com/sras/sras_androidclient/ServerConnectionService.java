package com.sras.sras_androidclient;


import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import CommModels.Devices;
import CommModels.Message;
import CommModels.User;

public class ServerConnectionService extends Service
{
    private String mServerAddress;
    private int mServerPort;
    private String mUsername;
    private String mPassword;

    private Socket mSocket;
    private ObjectOutputStream mOutputStream;
    private ObjectInputStream mInputStream;

    private final IBinder mBinder = new ServerConnectionBinder();

    public class ServerConnectionBinder extends Binder
    {
        ServerConnectionService getService()
        {
            return ServerConnectionService.this;
        }
    }

    private class ConnectionThread extends Thread
    {
        private volatile String result;
        private volatile Devices devices = new Devices();

        @Override
        public void run()
        {
            try
            {
                mSocket = new Socket(mServerAddress, mServerPort);
                mOutputStream = new ObjectOutputStream(mSocket.getOutputStream());
                mInputStream = new ObjectInputStream(mSocket.getInputStream());

                Message message = new Message("Hello");

                if (isAuthenticated(mUsername, mPassword))
                {
                    mOutputStream.writeObject(message); // Send message to server
                    message = (Message) mInputStream.readObject();
                    devices = (Devices) mInputStream.readObject();
                }

                //result = devices.getDeviceById(0).getDeviceName();
                //result = message.getMessage();

            } catch (IOException | ClassNotFoundException e)
            {
                e.printStackTrace();
            }
        }

        public String getResult()
        {
            return result;
        }

        public Devices getDevices()
        {
            return devices;
        }
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return mBinder;
    }

    public Devices connectToServer() throws IOException, ClassNotFoundException, InterruptedException
    {
        ConnectionThread ct = new ConnectionThread();
        Thread t = new Thread(ct);
        t.start();
        t.join();

        return ct.getDevices();
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
