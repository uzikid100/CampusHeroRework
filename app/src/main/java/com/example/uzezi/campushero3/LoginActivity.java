package com.example.uzezi.campushero3;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by uzezi on 10/6/2017.
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText mEmail;
    private EditText mPassword;
    private EditText mReenterPassword;
    private Button mLogin;
    private TextView mSignUpTV;
    private TextView mLoginTv;

    private boolean mExistingUser = true;


    //TODO required fields missing depending on new/existing user
    //TODO validate email address

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmail = (EditText) findViewById(R.id.email_editText);
        mPassword = (EditText) findViewById(R.id.password_editText);
        mReenterPassword = (EditText) findViewById(R.id.reenter_password_editText);
        mLogin = (Button) findViewById(R.id.loginButton);
        mSignUpTV = (TextView) findViewById(R.id.sign_up_tv);
        mLoginTv = (TextView) findViewById(R.id.login_tv);
    }

    private void setCreateUserUI() {
        mReenterPassword.setVisibility(View.VISIBLE);
        mLogin.setText("Create New Account");
        mSignUpTV.setVisibility(View.INVISIBLE);
        mLoginTv.setVisibility(View.VISIBLE);
        mExistingUser = false;
    }

    private void setExistingUserUI() {
        mReenterPassword.setVisibility(View.GONE);
        mLogin.setText("Login");
        mSignUpTV.setVisibility(View.VISIBLE);
        mLoginTv.setVisibility(View.INVISIBLE);
        mExistingUser = true;
    }

    private boolean validateSamePassword() {
        String pass = mPassword.getText().toString();
        String passReenter = mReenterPassword.getText().toString();
        if (pass.matches(passReenter)) {
            return true;
        } else {
            mPassword.setText("");
            mReenterPassword.setText("");
            reportError("Passwords do not match!");
            return false;
        }
    }

    private void startMainActivity() {
        Context context = this;
        Class DestinationActivity = MainActivity.class;
        Intent intent = new Intent(context, DestinationActivity);
        startActivity(intent);
    }

    private void reportError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


    private void loginExistingUser() {
        startMainActivity();
    }

    private void createNewUser() {
//        startMainActivity();
        AlertDialog dLog = createHeroSelectionDialog();
        dLog.show();
    }

    private AlertDialog createHeroSelectionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(R.layout.hero_selection_dialog);
        return builder.create();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_tv:
                setExistingUserUI();
                break;
            case R.id.sign_up_tv:
                setCreateUserUI();
                break;
            case R.id.loginButton:
                //Authentication goes here....
                if (mExistingUser) {
                    loginExistingUser();
                } else if (!mExistingUser) {
                    if (validateSamePassword()) {
                        createNewUser();
                    }
                }
                break;
        }
    }
}
