package com.example.uzezi.campushero3.Login;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.uzezi.campushero3.MainActivity;
import com.example.uzezi.campushero3.R;
import com.example.uzezi.campushero3.Student;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;
import com.microsoft.windowsazure.mobileservices.http.NextServiceFilterCallback;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilter;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterRequest;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterResponse;


import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;

import java.net.MalformedURLException;
import java.util.List;


import com.microsoft.windowsazure.mobileservices.MobileServiceClient;



/**
 * Created by uzezi on 10/6/2017.
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mEmail;
    private EditText mPassword;
    private EditText mReenterPassword;
    private Button mLogin;
    private TextView mSignUpTV;
    private TextView mLoginTv;

    private MobileServiceClient mClient;
    private MobileServiceTable<Student> mToDoTable;
    private boolean authSuccess = false;
    private Student mStudent = new Student();

    private AlertDialog mAlertDialog;

    private boolean mExistingUser = true;


    //TODO required fields missing depending on new/existing user
    //TODO validate email address

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        try {
            mClient = new MobileServiceClient(
                    "https://campushero1.azurewebsites.net",
                    this).withFilter(new ProgressFilter()
            );
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        mToDoTable = mClient.getTable(Student.class);

        mEmail = (EditText) findViewById(R.id.email_editText);
        mPassword = (EditText) findViewById(R.id.password_editText);
        mReenterPassword = (EditText) findViewById(R.id.reenter_password_editText);
        mLogin = (Button) findViewById(R.id.loginButton);
        mSignUpTV = (TextView) findViewById(R.id.sign_up_tv);
        mLoginTv = (TextView) findViewById(R.id.login_tv);
//        mErrorInfo = (TextView) findViewById(R.id.errorInfo);
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

    private void authenticateUser() {
        authenticateItem();
    }

    private boolean validateUser(boolean existingUser) {
        return true;
    }

    private void createNewUser() {
//        startMainActivity();
        mAlertDialog = createHeroSelectionDialog();
        mAlertDialog.show();
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
                boolean validated = validateUser(mExistingUser);
                if (validated && !mExistingUser) {
                    createNewUser();
                }
                else if (validated && mExistingUser) {
                    authenticateUser();
                    if (authSuccess) {

                    } else {
                        reportError("Username or Password Incorrect.");
                    }
                    //TODO
                    // I think we should make 'auhtSuccess' local or a return value of the authenticateUser() method perhaps?
                    // Thoughts?
                } else {
                    reportError("Something was wrong with your entry.");
                }
                break;
            case R.id.cancel_dialog_button:
                mAlertDialog.cancel();
                break;
            case R.id.ok_dialog_button:
                //Check if they have selected a champ then close Dialog
                startMainActivity();
                break;
        }
    }
    /*
    private void addItem(){

        if (mClient == null) {
            return;
        }

        // Create a new mStudent
        final UserItem mStudent = new UserItem();

        mStudent.setText(mInsertText.getText().toString());

        // Insert the new mStudent
        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    final UserItem entity = mToDoTable.insert(mStudent).get();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mInsertInfo.setText(entity.getText());
                            //mAdapter.add(entity);
                        }
                    });
                } catch (final Exception e) {

                }
                return null;
            }
        };

        runAsyncTask(task);
    }
    */

    private void authenticateItem(){
        if (mClient == null) {
            authSuccess = false;
            return;
        }

        // Create a new mStudent
        //final Student mStudent = new Student();
        final String email = mEmail.getText().toString();
        final String password = mPassword.getText().toString();

        //mStudent.setText(mEmail.getText().toString());
        //mStudent.setMytext(mPassword.getText().toString());


        // Insert the new mStudent
        AsyncTask<Void, Void, Void> authenticate = new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    final List<Student> students = mToDoTable.where().field("email").eq(email).execute().get();
                    //final List<Student> passwordEntity = mToDoTable.where().field("password").eq(password).execute().get();


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(!students.isEmpty() && (password.equals(students.get(0).getMpassword()))){
                                mStudent = students.get(0);
                                authSuccess = true;
                                //mErrorInfo.setText("Success!");
                            }else{
                                authSuccess = false;
                                //mErrorInfo.setText("Username or Password Incorrect.");
                            }
                        }
                    });
                } catch (final Exception e) {
                    e.printStackTrace();
                }

                return null;
            }
        };

        runAsyncTask(authenticate);
    }

    private AsyncTask<Void, Void, Void> runAsyncTask(AsyncTask<Void, Void, Void> task) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            return task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            return task.execute();
        }
    }

    private class ProgressFilter implements ServiceFilter {

        @Override
        public ListenableFuture<ServiceFilterResponse> handleRequest(ServiceFilterRequest request, NextServiceFilterCallback nextServiceFilterCallback) {

            final SettableFuture<ServiceFilterResponse> resultFuture = SettableFuture.create();


            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    //if (mProgressBar != null) mProgressBar.setVisibility(ProgressBar.VISIBLE);
                }
            });

            ListenableFuture<ServiceFilterResponse> future = nextServiceFilterCallback.onNext(request);

            Futures.addCallback(future, new FutureCallback<ServiceFilterResponse>() {
                @Override
                public void onFailure(Throwable e) {
                    resultFuture.setException(e);
                }

                @Override
                public void onSuccess(ServiceFilterResponse response) {
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            //if (mProgressBar != null) mProgressBar.setVisibility(ProgressBar.GONE);
                        }
                    });

                    resultFuture.set(response);
                }
            });

            return resultFuture;
        }
    }


}
