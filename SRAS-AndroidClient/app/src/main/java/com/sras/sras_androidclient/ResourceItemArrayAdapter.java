package com.sras.sras_androidclient;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import CommModels.Device;

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
