package com.sras.sras_androidclient;

import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.List;

public class ServerListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<ServerItem>>
{
    private static final int LOADER_ID = 1;

    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_list);

        getLoaderManager().initLoader(LOADER_ID, null, this).forceLoad();

        mListView = (ListView)findViewById(R.id.listview);
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
            // Add new server activity
        }

        return super.onOptionsItemSelected(item);
    }
}
