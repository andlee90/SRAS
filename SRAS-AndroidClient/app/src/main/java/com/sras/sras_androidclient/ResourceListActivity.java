package com.sras.sras_androidclient;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import static android.R.id.message;

public class ResourceListActivity extends AppCompatActivity implements View.OnClickListener
{
    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resource_list);

        mTextView = (TextView) findViewById(message);

        Button testButton = (Button) findViewById(R.id.button_test);
        testButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view)
    {
        if (view.getId() == R.id.button_test)
        {
            ServerConnectionManager manager = new ServerConnectionManager(new ServerConnectionManager.AsyncResponse(){

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
}
