package com.sras.sras_androidclient.Services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

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

    @Override
    public IBinder onBind(Intent intent)
    {
        return mBinder;
    }

    /**
     * Establishes a lasting connection to a specified server.
     * @param addr the address of the server to connect to.
     * @param port the port of the server to connect to.
     * @param user the username of the user attempting to connect.
     * @param pass the password of the user attempting to connect.
     * @return A message describing the success or failure of the connection.
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws InterruptedException
     */
    public User establishConnection(String addr, int port, String user, String pass)
            throws IOException, ClassNotFoundException, InterruptedException
    {
        this.mServerAddress = addr;
        this.mServerPort = port;
        this.mUsername = user;
        this.mPassword = pass;

        EstablishConnectionThread ct = new EstablishConnectionThread();
        Thread t = new Thread(ct);
        t.start();
        t.join();

        return ct.getUser();
    }

    /**
     * Gathers a list of available devices present on the server.
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws InterruptedException
     */
    public Devices fetchDevices() throws IOException, ClassNotFoundException, InterruptedException
    {
        FetchDevicesThread fdt = new FetchDevicesThread();
        Thread t = new Thread(fdt);
        t.start();
        t.join();

        return fdt.getDevices();
    }

    /**
     * Notifies the server which device has been selected.
     * @param device the selected device.
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws InterruptedException
     */
    public void initiateController(Device device)
            throws IOException, ClassNotFoundException, InterruptedException
    {
        InitiateControllerThread frt = new InitiateControllerThread(device);
        Thread t = new Thread(frt);
        t.start();
        t.join();
    }

    /**
     * Sends a command to the sever to be executed.
     * @param command the command to be executed.
     * @return an updated to Device object containing any changes to the Device produced by the
     * command's execution.
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws InterruptedException
     */
    public Device issueCommand(Command command)
            throws IOException, ClassNotFoundException, InterruptedException
    {
        IssueCommandThread ict = new IssueCommandThread(command);
        Thread t = new Thread(ict);
        t.start();
        t.join();

        return ict.getDevice();
    }

    /**
     * Destroys the connection to the server.
     * @throws IOException
     */
    public void closeServer() throws IOException
    {
        mOutputStream.close();
        mInputStream.close();
        mSocket.close();
    }

    private class EstablishConnectionThread extends Thread
    {
        private volatile User user = null;

        @Override
        public void run()
        {
            try
            {
                mSocket = new Socket(mServerAddress, mServerPort);
                mOutputStream = new ObjectOutputStream(mSocket.getOutputStream());
                mInputStream = new ObjectInputStream(mSocket.getInputStream());

                User newUser = new User(0, mUsername, mPassword, "", "", "", "");
                mOutputStream.writeObject(newUser);

                user = (User) mInputStream.readObject();
            }
            catch (IOException | ClassNotFoundException e)
            {
                e.printStackTrace();
            }
        }

        User getUser()
        {
            return user;
        }
    }

    private class FetchDevicesThread extends Thread
    {
        private volatile Devices devices = new Devices();

        @Override
        public void run()
        {
            try
            {
                mOutputStream.writeObject(devices);
                devices = (Devices) mInputStream.readObject();
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

    private class InitiateControllerThread extends Thread
    {
        private volatile Device device;

        InitiateControllerThread(Device d)
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
}
