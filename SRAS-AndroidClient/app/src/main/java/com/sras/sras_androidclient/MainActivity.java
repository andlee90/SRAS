package com.sras.sras_androidclient;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity
{

    public static final String PREFERENCES = "Preferences";

    private String mHost;
    private String mUser;
    private String mPass;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences settings = getSharedPreferences(PREFERENCES, 0);
        mHost = settings.getString("host", "");
        mUser = settings.getString("user", "");
        mPass = settings.getString("pass", "");

        /**
         * Eventually, the user will be prompted to login at first use. After this, their info
         * will be stored w/ shared preferences and they will skip the login activity and default
         * to their available resource list instead.
         */

        if (settings.contains("host") && settings.contains("user") && settings.contains("pass"))
        {
            Intent intent = new Intent(getApplicationContext(), ResourceListActivity.class);
            startActivity(intent);
        }
        else
        {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        }

        //Intent intent = new Intent(getApplicationContext(), ResourceListActivity.class);
        //startActivity(intent);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        SharedPreferences settings = getSharedPreferences(PREFERENCES, 0);
        mHost = settings.getString("host", "");
        mUser = settings.getString("user", "");
        mPass = settings.getString("pass", "");

        if (settings.contains("host") && settings.contains("user") && settings.contains("pass"))
        {
            Intent intent = new Intent(getApplicationContext(), ResourceListActivity.class);
            startActivity(intent);
        }
        else
        {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        }
    }
}
