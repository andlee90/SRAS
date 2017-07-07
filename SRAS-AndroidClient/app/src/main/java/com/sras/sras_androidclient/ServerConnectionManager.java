package com.sras.sras_androidclient;

import android.os.AsyncTask;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import CommModels.Devices;
import CommModels.Message;
import CommModels.User;

public class ServerConnectionManager extends AsyncTask<String, Void, String>
{
    private AsyncResponse mDelegate = null;
    private String mHost;
    private String mUser;
    private String mPass;

    private ObjectOutputStream mOutputStream;
    private ObjectInputStream mInputStream;

    ServerConnectionManager(String h, String u, String p, AsyncResponse delegate)
    {
        this.mHost = h;
        this.mUser = u;
        this.mPass = p;
        this.mDelegate = delegate;
    }

    interface AsyncResponse
    {
        void processFinish(String output);
    }

    @Override
    protected String doInBackground(String... strings)
    {
        Message message = new Message("Hello");
        Devices devices = new Devices();
        //Device d = new Device(2, "Thing", null, null);

        try
        {
            Socket socket = new Socket(mHost, 50873);

            mOutputStream = new ObjectOutputStream(socket.getOutputStream());
            mInputStream = new ObjectInputStream(socket.getInputStream());

            if (isAuthenticated(mUser, mPass))
            {
                mOutputStream.writeObject(message); // Send message to server
                message = (Message) mInputStream.readObject();
                devices = (Devices) mInputStream.readObject();

                //devices.addDevice(d);
            }

            mOutputStream.close();
            mInputStream.close();
            socket.close();
        }
        catch (ClassNotFoundException | IOException e)
        {
            e.printStackTrace();
        }
        //return message.getMessage();

        return devices.getDeviceById(0).getDeviceName();
    }

    @Override
    protected void onPostExecute(String status)
    {
        mDelegate.processFinish(status);
    }

    private boolean isAuthenticated(String un, String p) throws IOException, ClassNotFoundException
    {
        User user = new User(un, p, "", "", "", "");

        mOutputStream.writeObject(user);
        user = (User)mInputStream.readObject();

        return user.getValidity();
    }
}
