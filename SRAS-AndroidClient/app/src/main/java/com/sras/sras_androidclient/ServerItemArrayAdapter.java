package com.sras.sras_androidclient;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ServerItemArrayAdapter extends ArrayAdapter<ServerItem>
{
    private LayoutInflater mInflater;
    private List<ServerItem> servers = null;

    public ServerItemArrayAdapter(Context context, int resourceId, List<ServerItem> servers)
    {
        super(context, resourceId, servers);

        this.servers = servers;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if (convertView == null)
        {
            convertView = mInflater.inflate(R.layout.list_server_items, parent, false);
            convertView.setTag(new ViewHolder(convertView));
        }

        ServerItem server = servers.get(position);

        ViewHolder holder = (ViewHolder) convertView.getTag();
        holder.serverName.setText(server.getName());

        return convertView;
    }

    private class ViewHolder
    {
        private final TextView serverName;

        ViewHolder(View view)
        {
            serverName = (TextView) view.findViewById(R.id.serverName);
        }
    }
}
