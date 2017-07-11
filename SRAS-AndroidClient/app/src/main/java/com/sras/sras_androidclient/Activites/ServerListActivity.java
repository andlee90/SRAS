package com.sras.sras_androidclient.Activites;

import android.app.LoaderManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.sras.sras_androidclient.Database.ServerDBLoader;
import com.sras.sras_androidclient.R;
import com.sras.sras_androidclient.Models.ServerItem;
import com.sras.sras_androidclient.Services.ServerConnectionService;

import java.io.IOException;
import java.util.List;

import CommModels.Devices;

public class ServerListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<ServerItem>>
{
    private static final int LOADER_ID = 1;

    private ListView mListView;
    private List<ServerItem> mServerList;

    ServerConnectionService mService;
    boolean mBound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_list);

        getLoaderManager().initLoader(LOADER_ID, null, this).forceLoad();

        mListView = (ListView)findViewById(R.id.listview);
        TextView emptyText = (TextView)findViewById(R.id.empty_listview);
        mListView.setEmptyView(emptyText);
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
    public Loader<List<ServerItem>> onCreateLoader(int id, Bundle args)
    {
        return new ServerDBLoader(getApplicationContext());
    }

    @Override
    public void onLoadFinished(Loader<List<ServerItem>> loader, List<ServerItem> data)
    {
        mServerList = data;

        ServerItemArrayAdapter adapter = new ServerItemArrayAdapter(getApplicationContext(),
                android.R.layout.simple_list_item_1, data);
        mListView.setAdapter(adapter);
    }

    @Override
    public void onLoaderReset(Loader<List<ServerItem>> loader)
    {
        //TODO: Implement method.
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_server_list_activity, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getItemId() == R.id.add_server)
        {
            Intent intent = new Intent(getApplicationContext(), AddServerActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
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

    private class ServerItemArrayAdapter extends ArrayAdapter<ServerItem>
    {
        private LayoutInflater mInflater;
        private List<ServerItem> servers = null;
        private Context mContext;

        ServerItemArrayAdapter(Context context, int resourceId, List<ServerItem> servers)
        {
            super(context, resourceId, servers);

            this.servers = servers;
            this.mContext = context;
            mInflater = LayoutInflater.from(context);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            ServerItem server = servers.get(position);
            int serverId = server.getId();

            if (convertView == null)
            {
                convertView = mInflater.inflate(R.layout.list_server_items, parent, false);
                convertView.setTag(new ViewHolder(convertView));
            }

            ViewHolder holder = (ViewHolder) convertView.getTag();
            holder.serverName.setText(server.getName());
            holder.serverName.setOnClickListener(view ->
            {
                if(server.getUsername() != null)
                {
                    if(mBound)
                    {
                        try
                        {
                            mService.setParams(
                                    server.getAddress(),
                                    server.getPort(),
                                    server.getUsername(),
                                    server.getPassword());

                            Devices devices = mService.connectToServer();
                            Intent intent = new Intent(getApplicationContext(), ResourceListActivity.class);
                            intent.putExtra("devices", devices);
                            startActivity(intent);

                        } catch (IOException | ClassNotFoundException | InterruptedException e)
                        {
                            e.printStackTrace();
                        }
                    }
                }
                else
                {
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    intent.putExtra(Intent.EXTRA_KEY_EVENT, serverId);
                    startActivity(intent);
                }
            });

            holder.serverSettings.setOnClickListener(view ->
            {
                Intent intent = new Intent(mContext, EditServerActivity.class);
                intent.putExtra("server_id", serverId);
                intent.putExtra("server_name", server.getName());
                intent.putExtra("server_address", server.getAddress());
                intent.putExtra("server_port", server.getPort());
                mContext.startActivity(intent);
            });

            return convertView;
        }

        private class ViewHolder
        {
            private final TextView serverName;
            private final ImageButton serverSettings;

            ViewHolder(View view)
            {
                serverName = (TextView) view.findViewById(R.id.serverName);
                serverSettings = (ImageButton) view.findViewById(R.id.server_settings_button);
            }
        }
    }
}