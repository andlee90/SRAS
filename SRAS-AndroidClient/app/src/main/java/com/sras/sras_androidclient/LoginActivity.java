package com.sras.sras_androidclient;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import static com.sras.sras_androidclient.MainActivity.PREFERENCES;
import static com.sras.sras_androidclient.R.id.button_accept;
import static com.sras.sras_androidclient.R.id.edit_host;
import static com.sras.sras_androidclient.R.id.edit_pass;
import static com.sras.sras_androidclient.R.id.edit_user;

public class LoginActivity extends AppCompatActivity implements TextView.OnEditorActionListener,
        View.OnClickListener, View.OnFocusChangeListener
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

        SharedPreferences settings = getSharedPreferences(PREFERENCES, 0);
        mHost = settings.getString("host", "");
        mUser = settings.getString("user", "");
        mPass = settings.getString("pass", "");

        mHostField = (EditText) findViewById(edit_host);
        mHostField.setText(mHost);
        mHostField.setOnEditorActionListener(this);

        mUserField = (EditText) findViewById(edit_user);
        mUserField.setText(mUser);
        mUserField.setOnEditorActionListener(this);

        mPassField = (EditText) findViewById(edit_pass);
        mPassField.setOnEditorActionListener(this);

        Button acceptButton = (Button) findViewById(button_accept);
        acceptButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view)
    {
        if (view.getId() == R.id.button_accept)
        {
            if (mHost != null && mUser != null && mPass != null)
            {
                SharedPreferences settings = getSharedPreferences(PREFERENCES, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("host", mHost);
                editor.putString("user", mUser);
                editor.putString("pass", mPass);
                editor.apply();

                Intent intent = new Intent(getApplicationContext(), ResourceListActivity.class);
                startActivity(intent);

            }
            else
            {
                String toastContent = "Please fill out all fields and try again";
                Toast toast = Toast.makeText(getApplicationContext(), toastContent, Toast.LENGTH_LONG);
                toast.show();
            }
        }
    }

    @Override
    public void onFocusChange(View view, boolean b)
    {
        if (view.getId() == R.id.edit_host)
        {
            if (mHost != null)
            {
                SharedPreferences settings = getSharedPreferences(PREFERENCES, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("host", mHost);
                editor.apply();
                finish();
            }
        }
        else if (view.getId() == R.id.edit_user)
        {
            if (mUser != null)
            {
                SharedPreferences settings = getSharedPreferences(PREFERENCES, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("host", mUser);
                editor.apply();
                finish();
            }
        }
        else if (view.getId() == R.id.edit_pass)
        {
            if (mPass != null)
            {
                SharedPreferences settings = getSharedPreferences(PREFERENCES, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("host", mPass);
                editor.apply();
                finish();
            }
        }
    }

    @Override
    public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent)
    {
        switch(textView.getId())
        {
            case R.id.edit_host:
                if (actionId == EditorInfo.IME_ACTION_DONE)
                {
                    mHost = mHostField.getText().toString();
                }
                break;

            case R.id.edit_user:
                if (actionId == EditorInfo.IME_ACTION_DONE)
                {
                    mUser = mUserField.getText().toString();
                }
                break;

            case R.id.edit_pass:
                if (actionId == EditorInfo.IME_ACTION_DONE)
                {
                    mPass = mPassField.getText().toString();
                }
                break;
        }
        return false;
    }
}
