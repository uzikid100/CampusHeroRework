package com.example.uzezi.campushero3;

import android.content.Context;
import android.widget.Toast;

import java.util.Stack;

/**
 * Created by uzezi on 10/23/2017.
 */

public class UiErrorLog {

    private String mMessage;
    private int mErrorId;
    private Context mContext;
    private Stack<String> mPending;

    public enum LogCode
    {BAD_ENTRY, PASSWORD_MISMATCH, BAD_EMAIL,
    BAD_PASSWORD, BAD_AUTHENTICATION, EMPTY_FIELD}

    public UiErrorLog(Context context) {
        this.mContext = context;
        mPending = new Stack<String>();
    }

    public void log(LogCode code) {
        switch (code) {
            case BAD_EMAIL:
                mMessage = "Invalid Email";
                break;
            case BAD_PASSWORD:
                mMessage = "Invalid Password";
                break;
            case PASSWORD_MISMATCH:
                mMessage = "Passwords Do Not Match";
                break;
            case BAD_ENTRY:
                mMessage = "Username or Password Incorrect";
                break;
            case BAD_AUTHENTICATION:
                mMessage = "Authentication Unsuccessful";
                break;
            case EMPTY_FIELD:
                mMessage = "Missing Username or Password";
                break;
        }
    }

    public void DisplayPending() {
        if (mMessage == null) {
            show("No messages pending...");
        } else {
            show(mMessage);
        }
        mMessage = null;
    }

    private void show(String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }
}
