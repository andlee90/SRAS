package com.sras.sras_androidclient.Activites;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sras.sras_androidclient.Database.ServerDBHelper;
import com.sras.sras_androidclient.R;

public class AddServerActivity extends AppCompatActivity implements TextView.OnEditorActionListener,
        View.OnClickListener
{
    private String mServerName;
    private String mServerAddress;
    private int mServerPort = -1;

    private EditText mNameField;
    private EditText mAddressField;
    private EditText mPortField;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_server);
        setTitle("Add a New Server");

        mNameField = (EditText) findViewById(R.id.edit_server_name);
        mNameField.setOnEditorActionListener(this);

        mAddressField = (EditText) findViewById(R.id.edit_server_address);
        mAddressField.setOnEditorActionListener(this);

        mPortField = (EditText) findViewById(R.id.edit_server_port);
        mPortField.setOnEditorActionListener(this);

        Button acceptButton = (Button) findViewById(R.id.button_accept);
        acceptButton.setOnClickListener(this);

        Button cancelButton = (Button) findViewById(R.id.button_cancel);
        cancelButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.button_accept:
                if(mServerName != null && mServerAddress != null && mServerPort != -1)
                {
                    ServerDBHelper serverDBHelper = new ServerDBHelper(this);
                    serverDBHelper.insertServer(mServerName, mServerAddress, mServerPort, null, null);
                    finish();
                }
                else
                {
                    String toastContent = "Please fill out all fields and try again";
                    Toast toast = Toast.makeText(getApplicationContext(), toastContent, Toast.LENGTH_LONG);
                    toast.show();
                }
            case R.id.button_cancel:
                finish();
        }
    }

    @Override
    public boolean onEditorAction(TextView tv, int i, KeyEvent keyEvent)
    {
        switch(tv.getId())
        {
            case R.id.edit_server_name:
                if (i == EditorInfo.IME_ACTION_DONE)
                {
                    mServerName = mNameField.getText().toString();
                }
                break;

            case R.id.edit_server_address:
                if (i == EditorInfo.IME_ACTION_DONE)
                {
                    mServerAddress = mAddressField.getText().toString();
                }
                break;

            case R.id.edit_server_port:
                if (i == EditorInfo.IME_ACTION_DONE)
                {
                    mServerPort = Integer.parseInt(mPortField.getText().toString());
                }
                break;
        }
        return false;
    }
}
