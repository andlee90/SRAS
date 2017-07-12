package com.sras.sras_androidclient.Activites;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.sras.sras_androidclient.R;

import java.util.List;

import CommModels.Device;
import CommModels.Devices;


public class ResourceListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener
{
    private ListView mListView;
    private List<Device> mResourceList;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resource_list);

        mListView = (ListView)findViewById(R.id.listview);
        mListView.setOnItemClickListener(this);

        Intent intent = getIntent();
        Devices devices = (Devices) intent.getSerializableExtra("devices");
        mResourceList = devices.getDevices();

        ResourceItemArrayAdapter adapter = new ResourceItemArrayAdapter(getApplicationContext(),
                android.R.layout.simple_list_item_1, mResourceList);
        mListView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {

    }

    public class ResourceItemArrayAdapter extends ArrayAdapter<Device>
    {
        private LayoutInflater mInflater;
        private List<Device> devices = null;

        public ResourceItemArrayAdapter(Context context, int resource, List<Device> devices)
        {
            super(context, resource, devices);

            this.devices = devices;
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            if (convertView == null)
            {
                convertView = mInflater.inflate(R.layout.list_resource_items, parent, false);
                convertView.setTag(new ViewHolder(convertView));
            }

            Device device = devices.get(position);

            ViewHolder holder = (ViewHolder) convertView.getTag();
            holder.resourceName.setText(device.getDeviceName());

            return convertView;
        }

        private class ViewHolder
        {
            private final TextView resourceName;

            ViewHolder(View view)
            {
                resourceName = (TextView) view.findViewById(R.id.resourceName);
            }
        }
    }
}