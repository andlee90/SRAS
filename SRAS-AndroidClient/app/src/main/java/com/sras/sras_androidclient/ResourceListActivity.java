package com.sras.sras_androidclient;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import static com.sras.sras_androidclient.MainActivity.PREFERENCES;

public class ResourceListActivity extends AppCompatActivity implements View.OnClickListener
{
    private TextView mHostView;
    private TextView mUserView;
    private TextView mPassView;

    private String mHost;
    private String mUser;
    private String mPass;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resource_list);

        SharedPreferences settings = getSharedPreferences(PREFERENCES, 0);
        mHost = settings.getString("host", "");
        mUser = settings.getString("user", "");
        mPass = settings.getString("pass", "");

        mHostView = (TextView) findViewById(R.id.host_view);
        mHostView.setText(mHost);

        mUserView = (TextView) findViewById(R.id.user_view);
        mUserView.setText(mUser);

        mPassView = (TextView) findViewById(R.id.pass_view);
        mPassView.setText(mPass);

        Button testButton = (Button) findViewById(R.id.button_test);
        testButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view)
    {
        if (view.getId() == R.id.button_test)
        {
            ServerConnectionManager manager = new ServerConnectionManager(mHost, mUser, mPass, new ServerConnectionManager.AsyncResponse(){

                @Override
                public void processFinish(String output)
                {
                    Toast finishToast = Toast.makeText(getApplicationContext(), output, Toast.LENGTH_LONG);
                    finishToast.show();
                }
            });
            manager.execute();

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_resource_list_activity, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getItemId() == R.id.login)
        {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
