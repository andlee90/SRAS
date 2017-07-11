package com.sras.sras_androidclient;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

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
}
