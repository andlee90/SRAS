package com.sras.sras_androidclient;

import android.app.LoaderManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

public class ServerListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener,
        LoaderManager.LoaderCallbacks<List<ServerItem>>
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
        mListView.setOnItemClickListener(this);
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        Intent intent = new Intent(this, ServerConnectionService.class);
        //startService(intent);
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        ServerItem server = mServerList.get(position);
        int serverId = server.getId();

        if(server.getUsername() != null)
        {
            if(mBound)
            {
                try
                {
                    mService.setParams(server.getAddress(), server.getPort(), server.getUsername(), server.getPassword());
                    String result = mService.connectToServer();
                    //String toastContent = "All good under the hood!";
                    Toast toast = Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG);
                    toast.show();
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
}
