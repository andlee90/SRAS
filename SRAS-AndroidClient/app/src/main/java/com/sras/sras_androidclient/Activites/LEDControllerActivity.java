package com.sras.sras_androidclient.Activites;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.sras.sras_androidclient.R;
import com.sras.sras_androidclient.Services.ServerConnectionService;

import java.io.IOException;

import CommModels.Command;
import CommModels.Device;
import CommModels.Message;

public class LEDControllerActivity extends AppCompatActivity implements View.OnClickListener
{
    ServerConnectionService mService;
    boolean mBound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_led_controller);

        Intent intent = getIntent();
        Device mDevice = (Device) intent.getSerializableExtra("device");
        setTitle(mDevice.getDeviceName());

        TextView pinView = (TextView) findViewById(R.id.textview_pin);
        pinView.setText("LED is on pin: " + mDevice.getDevicePin());

        TextView statusView = (TextView) findViewById(R.id.textview_status);
        statusView.setText("Device status is " + mDevice.getDeviceStatus());

        Button toggleButton = (Button) findViewById(R.id.button_toggle);
        toggleButton.setOnClickListener(this);

        Button blinkButton = (Button) findViewById(R.id.button_blink);
        blinkButton.setOnClickListener(this);
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        Intent intent = new Intent(this, ServerConnectionService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        if (mBound)
        {
            unbindService(mConnection);
            mBound = false;
        }
    }

    private ServiceConnection mConnection = new ServiceConnection()
    {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service)
        {
            ServerConnectionService.ServerConnectionBinder binder = (ServerConnectionService.ServerConnectionBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0)
        {
            unbindService(mConnection);
            mBound = false;
        }
    };

    @Override
    public void onClick(View view)
    {
        if (view.getId() == R.id.button_toggle)
        {
            if(mBound)
            {
                try
                {
                    Command command = new Command(Command.CommandType.TOGGLE);
                    Message message = mService.issueCommand(command);
                    Log.d("Incoming message: ", message.getMessage());

                } catch (IOException | ClassNotFoundException | InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        }

        else if (view.getId() == R.id.button_blink)
        {
            if(mBound)
            {
                try
                {
                    Command command = new Command(Command.CommandType.BLINK);
                    Message message = mService.issueCommand(command);
                    Log.d("Incoming message: ", message.getMessage());

                } catch (IOException | ClassNotFoundException | InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
}