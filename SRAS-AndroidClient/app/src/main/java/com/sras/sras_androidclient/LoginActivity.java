package com.sras.sras_androidclient;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;

import static com.sras.sras_androidclient.R.id.button_accept;
import static com.sras.sras_androidclient.R.id.edit_host;
import static com.sras.sras_androidclient.R.id.edit_pass;
import static com.sras.sras_androidclient.R.id.edit_user;

public class LoginActivity extends AppCompatActivity
{
    private String mHost;
    private String mUser;
    private String mPass;

    private EditText mHostField;
    private EditText mUserField;
    private EditText mPassField;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mHostField = (EditText) findViewById(edit_host);
        mHostField.setText(mHost);

        mUserField = (EditText) findViewById(edit_user);
        mUserField.setText(mUser);

        mPassField = (EditText) findViewById(edit_pass);

        Button acceptButton = (Button) findViewById(button_accept);
    }
}
