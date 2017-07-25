package com.sras.sras_androidclient.Activites;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.sras.sras_androidclient.R;
import com.sras.sras_androidclient.Services.ServerConnectionService;

import java.io.IOException;

import CommModels.Command;
import CommModels.Device;
import CommModels.LedState;

public class LEDControllerActivity extends AppCompatActivity implements View.OnClickListener
{
    ServerConnectionService mService;
    boolean mBound = false;
    private ImageView mLEDView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_led_controller);

        Intent intent = getIntent();
        Device mDevice = (Device) intent.getSerializableExtra("device");
        setTitle(mDevice.getDeviceName() + " (pin " + mDevice.getDevicePin() +")");

        mLEDView = (ImageView) findViewById(R.id.image_led);
        mLEDView.setImageResource(R.drawable.led_off);

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
        Device device;
        if (view.getId() == R.id.button_toggle)
        {
            if(mBound)
            {
                try
                {
                    Command command = new Command(Command.CommandType.TOGGLE);
                    device = mService.issueCommand(command);
                    setImageState((LedState) device.getDeviceState());

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
                    device = mService.issueCommand(command);
                    setImageState((LedState) device.getDeviceState());

                } catch (IOException | ClassNotFoundException | InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    private void setImageState(LedState state) throws InterruptedException
    {
        if(state == LedState.ON)
        {
            mLEDView.setImageResource(R.drawable.led_on);
        }
        else if(state == LedState.OFF)
        {
            mLEDView.setImageResource(R.drawable.led_off);
        }
        else if(state == LedState.BLINKING)
        {
            mLEDView.setImageResource(R.drawable.blink);
            AnimationDrawable frameAnimation = (AnimationDrawable) mLEDView.getDrawable();
            frameAnimation.start();
        }
    }
}