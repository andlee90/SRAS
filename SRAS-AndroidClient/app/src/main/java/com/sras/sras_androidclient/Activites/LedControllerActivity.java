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

import CommModels.Command.LedCommand;
import CommModels.Command.LedCommandType;
import CommModels.Device.Device;
import CommModels.Device.DeviceStatus;
import CommModels.Device.LedState;
import CommModels.User.User;

//TODO: 1) Device state should be persisted through screen rotation
//TODO: 2) UI should be updated to properly display all content after rotation
//TODO: 3) Finally, remove this line from the manifest: android:screenOrientation="portrait"
public class LedControllerActivity extends AppCompatActivity implements View.OnClickListener
{
    ServerConnectionService mService;
    boolean mBound = false;
    private ImageView mLEDView;

    private User mUser;
    private Device mDevice;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_led_controller);

        Intent intent = getIntent();
        mDevice = (Device) intent.getSerializableExtra("device");
        mUser = (User) intent.getSerializableExtra("user");

        setTitle(mDevice.getDeviceName() + " (pin " + mDevice.getDevicePin() +")");

        mLEDView = (ImageView) findViewById(R.id.image_led);
        setImageState((LedState) mDevice.getDeviceState());

        Button toggleButton = (Button) findViewById(R.id.button_toggle);
        toggleButton.setOnClickListener(this);

        Button blinkButton = (Button) findViewById(R.id.button_blink);
        blinkButton.setOnClickListener(this);

        String permission = "";

        // Check whether user has appropriate privileges to modify device state.
        if(mUser.getRules().containsKey(mDevice.getDeviceName()))
        {
            permission = mUser.getRules().get(mDevice.getDeviceName());
        }

        if(permission.equals("VIEW_ONLY"))
        {
            toggleButton.setEnabled(false);
            blinkButton.setEnabled(false);
        }
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
    public void onBackPressed()
    {
        super.onBackPressed();

        mDevice.setDeviceStatus(DeviceStatus.AVAILABLE);
        mDevice.setDeviceUser(null);

        if(mBound)
        {
            try
            {
                mService.initiateController(mDevice);

            } catch (IOException | ClassNotFoundException | InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View view)
    {
        if (view.getId() == R.id.button_toggle)
        {
            if(mBound)
            {
                try
                {
                    LedCommand command = new LedCommand(LedCommandType.TOGGLE);
                    mDevice = mService.issueCommand(command);
                    setImageState((LedState) mDevice.getDeviceState());

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
                    LedCommand command = new LedCommand(LedCommandType.BLINK);
                    mDevice = mService.issueCommand(command);
                    setImageState((LedState) mDevice.getDeviceState());

                } catch (IOException | ClassNotFoundException | InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    private void setImageState(LedState state)
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
            frameAnimation.start(); // Animate the led drawable
        }
    }
}