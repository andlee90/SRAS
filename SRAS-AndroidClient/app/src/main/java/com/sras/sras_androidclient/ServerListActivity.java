package com.sras.sras_androidclient;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

public class ServerListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener,
        LoaderManager.LoaderCallbacks<List<ServerItem>>
{
    private static final int LOADER_ID = 1;

    private ListView mListView;
    private List<ServerItem> mServerList;

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
    protected void onResume()
    {
        super.onResume();
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
            // Start new service
            String toastContent = "All good under the hood!";
            Toast toast = Toast.makeText(getApplicationContext(), toastContent, Toast.LENGTH_LONG);
            toast.show();
        }
        else
        {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            intent.putExtra(Intent.EXTRA_KEY_EVENT, serverId);
            startActivity(intent);
        }
    }
}
