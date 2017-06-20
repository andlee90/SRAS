package com.sras.sras_androidclient;

import android.os.AsyncTask;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import CommModels.Message;

public class ServerConnectionManager extends AsyncTask<String, Void, String>
{
    private AsyncResponse mDelegate = null;

    ServerConnectionManager(AsyncResponse delegate)
    {
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
        try
        {
            Socket socket = new Socket("192.168.1.6", 50873);

            ObjectOutputStream clientOutputStream = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream clientInputStream = new ObjectInputStream(socket.getInputStream());
            clientOutputStream.writeObject(message); // Send message to server
            message = (Message) clientInputStream.readObject();

            clientInputStream.close();
            clientOutputStream.close();
            socket.close();
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
        return message.getMessage();
    }

    @Override
    protected void onPostExecute(String status)
    {
        mDelegate.processFinish(status);
    }
}
