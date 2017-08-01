package com.sras.sras_androidclient.Activites;

import android.content.Intent;
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

public class EditServerActivity extends AppCompatActivity implements TextView.OnEditorActionListener,
        View.OnClickListener
{
    private int mServerId;
    private String mServerName;
    private String mServerAddress;
    private int mServerPort = -1;

    private EditText mNameField;
    private EditText mAddressField;
    private EditText mPortField;

    private ServerDBHelper mServerDBHelper = new ServerDBHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_server);

        Intent intent = getIntent();
        mServerId = intent.getIntExtra("server_id", 0);
        mServerName = intent.getStringExtra("server_name");
        mServerAddress = intent.getStringExtra("server_address");
        mServerPort = intent.getIntExtra("server_port", 0);

        setTitle("Edit Server: " + mServerName);

        mNameField = (EditText) findViewById(R.id.edit_server_name);
        mNameField.setText(mServerName);
        mNameField.setOnEditorActionListener(this);

        mAddressField = (EditText) findViewById(R.id.edit_server_address);
        mAddressField.setText(mServerAddress);
        mAddressField.setOnEditorActionListener(this);

        mPortField = (EditText) findViewById(R.id.edit_server_port);
        mPortField.setText("" + mServerPort);
        mPortField.setOnEditorActionListener(this);

        Button updateButton = (Button) findViewById(R.id.button_update);
        updateButton.setOnClickListener(this);

        Button deleteButton = (Button) findViewById(R.id.button_delete);
        deleteButton.setOnClickListener(this);

        Button closeButton = (Button) findViewById(R.id.button_close);
        closeButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view)
    {
        // For some reason I don't quite understand, switches don't seem to work in onClick()

        if (view.getId() == R.id.button_update)
        {
            if(mServerName != null && mServerAddress != null && mServerPort != -1)
            {
                mServerDBHelper.updateServer(mServerId, mServerName, mServerAddress, mServerPort, null, null);
                Toast toast = Toast.makeText(getApplicationContext(), mServerName + " Updated", Toast.LENGTH_LONG);
                toast.show();
                finish();
            }
            else
            {
                String toastContent = "Please fill out all fields and try again";
                Toast toast = Toast.makeText(getApplicationContext(), toastContent, Toast.LENGTH_LONG);
                toast.show();
            }
        }
        else if (view.getId() == R.id.button_delete)
        {
            mServerDBHelper.deleteServer(mServerId);
            Toast toast = Toast.makeText(getApplicationContext(), mServerName + " Removed", Toast.LENGTH_LONG);
            toast.show();
            finish();
        }
        else if (view.getId() == R.id.button_close)
        {
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
