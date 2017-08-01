package com.sras.sras_androidclient.Activites;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sras.sras_androidclient.R;
import com.sras.sras_androidclient.Services.ServerConnectionService;

import java.io.IOException;
import java.util.List;

import CommModels.*;

public class DeviceListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener
{
    private List<Device> mDeviceList;
    private ListView mListView;

    ServerConnectionService mService;
    boolean mBound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list);

        mListView = (ListView) findViewById(R.id.listview);
        mListView.setOnItemClickListener(this);

        //Intent intent = getIntent();
        //Devices devices = (Devices) intent.getSerializableExtra("devices");
        //mDeviceList = devices.getDevices();
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

    @Override
    public void onBackPressed()
    {
        try
        {
            mService.closeServer(); // Disconnect from server
            Toast.makeText(this.getApplicationContext(), "Logged out successfully", Toast.LENGTH_SHORT).show();
            finish();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        Device device = mDeviceList.get(position);

        if(mBound)
        {
            try
            {
                mService.initiateController(device);
                Intent intent = new Intent(getApplicationContext(), LEDControllerActivity.class);
                intent.putExtra("device", (Led)device);
                startActivity(intent);

            }
            catch (IOException | ClassNotFoundException | InterruptedException e)
            {
                e.printStackTrace();
            }
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

           try
            {
                Devices devices = mService.fetchDevices();
                mDeviceList = devices.getDevices();
            }
            catch (IOException | ClassNotFoundException | InterruptedException e)
            {
                e.printStackTrace();
            }

            ResourceItemArrayAdapter adapter = new ResourceItemArrayAdapter(getApplicationContext(),
                    android.R.layout.simple_list_item_1, mDeviceList);
            mListView.setAdapter(adapter);
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0)
        {
            unbindService(mConnection);
            mBound = false;
        }
    };

    private class ResourceItemArrayAdapter extends ArrayAdapter<Device>
    {
        private LayoutInflater mInflater;
        private List<Device> devices = null;

        ResourceItemArrayAdapter(Context context, int resource, List<Device> devices)
        {
            super(context, resource, devices);

            this.devices = devices;
            mInflater = LayoutInflater.from(context);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            if (convertView == null)
            {
                convertView = mInflater.inflate(R.layout.list_device_items, parent, false);
                convertView.setTag(new ViewHolder(convertView));
            }

            Device device = devices.get(position);

            ViewHolder holder = (ViewHolder) convertView.getTag();
            holder.deviceName.setText(device.getDeviceName());
            holder.deviceState.setText(device.getDeviceState().toString());

            if (device.getDeviceState().toString().equals("ON") ||
                    device.getDeviceState().toString().equals("BLINKING"))
            {
                holder.deviceState.setTextColor(Color.GREEN);
            }
            else if (device.getDeviceState().toString().equals("OFF"))
            {
                holder.deviceState.setTextColor(Color.RED);
            }
            return convertView;
        }

        private class ViewHolder
        {
            private final TextView deviceName;
            private final TextView deviceState;

            ViewHolder(View view)
            {
                deviceName = (TextView) view.findViewById(R.id.deviceName);
                deviceState = (TextView) view.findViewById(R.id.deviceState);
            }
        }
    }
}
