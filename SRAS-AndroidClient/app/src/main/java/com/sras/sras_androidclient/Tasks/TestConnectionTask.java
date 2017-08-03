package com.sras.sras_androidclient.Tasks;

import android.os.AsyncTask;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import CommModels.Message;

public class TestConnectionTask extends AsyncTask<Void, Void, Boolean>
{
    private String mIp;
    private int mPort;

    private ResultListener mListener;

    public TestConnectionTask(String ip, int port, ResultListener listener)
    {
        this.mIp = ip;
        this.mPort = port;
        this.mListener = listener;
    }

    @Override
    protected Boolean doInBackground(Void... params)
    {
        boolean exists = false;

        try (Socket socket = new Socket(mIp, mPort))
        {
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());

            Message message = new Message("test");
            outputStream.writeObject(message);

            exists = true;

            // TODO: This may be the cause of a Connection Reset Exception being thrown. Needs fixing.
            socket.close();
            outputStream.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return exists;
    }

    @Override
    protected void onPostExecute(Boolean result)
    {
        if(mListener != null)
        {
            mListener.getResult(result);
        }
    }

    public interface ResultListener
    {
        void getResult(boolean result);
    }
}


