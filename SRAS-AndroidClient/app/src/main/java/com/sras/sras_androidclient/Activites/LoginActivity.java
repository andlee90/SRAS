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

import static com.sras.sras_androidclient.R.id.button_accept;
import static com.sras.sras_androidclient.R.id.button_cancel;
import static com.sras.sras_androidclient.R.id.edit_pass;
import static com.sras.sras_androidclient.R.id.edit_user;

public class LoginActivity extends AppCompatActivity implements TextView.OnEditorActionListener,
        View.OnClickListener, View.OnFocusChangeListener
{
    private int mServerId;

    private String mUser;
    private String mPass;

    private EditText mUserField;
    private EditText mPassField;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Intent intent = getIntent();
        mServerId = intent.getIntExtra(Intent.EXTRA_KEY_EVENT, 0);

        mUserField = (EditText) findViewById(edit_user);
        mUserField.setOnEditorActionListener(this);

        mPassField = (EditText) findViewById(edit_pass);
        mPassField.setOnEditorActionListener(this);

        Button acceptButton = (Button) findViewById(button_accept);
        acceptButton.setOnClickListener(this);

        Button cancelButton = (Button) findViewById(button_cancel);
        cancelButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view)
    {
        if (view.getId() == R.id.button_accept)
        {
            if (mUser != null && mPass != null)
            {
                Intent intent = new Intent(getApplicationContext(), ServerListActivity.class);
                startActivity(intent);
                ServerDBHelper serverDBHelper = new ServerDBHelper(getApplicationContext());
                serverDBHelper.updateServerWithUserAndPass(mServerId, mUser, mPass);
                finish();
            }
            else
            {
                String toastContent = "Please fill out all fields and try again";
                Toast toast = Toast.makeText(getApplicationContext(), toastContent, Toast.LENGTH_LONG);
                toast.show();
            }
        }
        else if (view.getId() == R.id.button_cancel)
        {
            finish();
        }
    }

    @Override
    public void onFocusChange(View view, boolean b)
    {
        if (view.getId() == R.id.edit_user)
        {
            if (mUser != null)
            {
                // TODO: Still gotta figure this one out.
            }
        }
        else if (view.getId() == R.id.edit_pass)
        {
            if (mPass != null)
            {

            }
        }
    }

    @Override
    public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent)
    {
        switch(textView.getId())
        {
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