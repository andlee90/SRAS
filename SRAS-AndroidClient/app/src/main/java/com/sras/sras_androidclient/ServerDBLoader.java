package com.sras.sras_androidclient;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

public class ServerDBLoader extends AsyncTaskLoader<List<ServerItem>>
{
    private Context mContext;

    public ServerDBLoader(Context context)
    {
        super(context);
        mContext = context;
    }

    @Override
    public List<ServerItem> loadInBackground()
    {
        ServerDBHelper dbHelper = new ServerDBHelper(mContext);
        Cursor cursor = dbHelper.getAllServers();

        ArrayList<ServerItem> servers = new ArrayList<>();

        while (cursor.moveToNext())
        {
            servers.add(new ServerItem(cursor.getInt(cursor.getColumnIndex("_id")),
                    cursor.getString(cursor.getColumnIndex("server_name")),
                    cursor.getString(cursor.getColumnIndex("server_address")),
                    cursor.getInt(cursor.getColumnIndex("server_port")),
                    cursor.getString(cursor.getColumnIndex("server_username")),
                    cursor.getString(cursor.getColumnIndex("server_password"))));
        }
        return servers;

    }
}
