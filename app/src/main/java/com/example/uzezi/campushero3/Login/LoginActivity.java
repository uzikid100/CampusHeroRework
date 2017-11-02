package com.example.uzezi.campushero3.Login;

import android.content.Context;
import android.content.DialogInterface;
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
import com.example.uzezi.campushero3.UiErrorLog;
import com.google.common.util.concurrent.ExecutionError;
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
    private Button mLoginButton;
    private Button mCreateNewButton;
    private TextView mSignUpTV;
    private TextView mLoginTv;

    private MobileServiceClient mClient;
    private MobileServiceTable<Student> mToDoTable;
    private boolean authSuccess = false;
    private Student mStudent = new Student();

    private Context mContext;
    private AlertDialog mAlertDialog;
    private Toast mToast;
    private boolean mExistingUser = true;
    private UiErrorLog mLogger;


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
        mLogger = new UiErrorLog(getApplicationContext());
        mContext = this.getApplicationContext();

        mEmail = (EditText) findViewById(R.id.email_editText);
        mPassword = (EditText) findViewById(R.id.password_editText);
        mReenterPassword = (EditText) findViewById(R.id.reenter_password_editText);
        mLoginButton = (Button) findViewById(R.id.loginButton);
        mCreateNewButton = (Button) findViewById(R.id.create_new_button);
        mSignUpTV = (TextView) findViewById(R.id.sign_up_tv);
        mLoginTv = (TextView) findViewById(R.id.login_tv);
    }

    private void setNewUserUI() {
        mReenterPassword.setVisibility(View.VISIBLE);
        mCreateNewButton.setVisibility(View.VISIBLE);
        mSignUpTV.setVisibility(View.INVISIBLE);
        mLoginTv.setVisibility(View.VISIBLE);
        mLoginButton.setVisibility(View.INVISIBLE);
        mExistingUser = false;
    }

    private void setExistingUserUI() {
        mLoginButton.setVisibility(View.VISIBLE);
        mReenterPassword.setVisibility(View.GONE);
        mCreateNewButton.setVisibility(View.GONE);
        mSignUpTV.setVisibility(View.VISIBLE);
        mLoginTv.setVisibility(View.INVISIBLE);
        mExistingUser = true;
    }

    private boolean validateReenterPassword() throws Exception {
        String pass = mPassword.getText().toString();
        String passReenter = mReenterPassword.getText().toString();
        if (pass == null || passReenter == null || pass.equals("") || passReenter.equals("")) {
            throw new Exception("Password Required");
        }
        else if (!pass.matches(passReenter)) {
            throw new Exception("Passwords Do Not Match");
        } else return true;
    }

    private void startMainActivity() {
        Context context = this;
        Class DestinationActivity = MainActivity.class;
        Intent intent = new Intent(context, DestinationActivity);
        startActivity(intent);
    }

    private void authenticateUser() {
        authenticateItem();
    }

    private boolean validateUser(boolean existingUser) {
        try {
            boolean validEmailAndPass = validateEmailAndPass();
            if (!existingUser) {
                boolean reenterValid = validateReenterPassword();
                return validEmailAndPass && reenterValid;
            }
            return validEmailAndPass;
        } catch (Exception e) {
            mToast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
            clearPasswords();
            return false;
        }
    }

    private void clearPasswords() {
        if (mPassword.getText().toString() != "") {
            mPassword.setText("");
        }
        if (mReenterPassword.getText().toString() != "") {
            mReenterPassword.setText("");
        }
    }

    private boolean validateEmailAndPass() throws Exception {
        String pass = mPassword.getText().toString();
        String email = mEmail.getText().toString();

        if (email.isEmpty() || pass.isEmpty()) {
            throw new Exception("Email or Password Empty");
        }
        else if (!email.contains("@") || !email.contains(".edu") && !email.contains(".com")) {
            throw new Exception("Invalid Email Address");
        }
        else return true;
    }


    private void createNewUser() {
//        startMainActivity();
        addItem();
        mAlertDialog = createHeroSelectionDialog();
        mAlertDialog.show();
    }

    private AlertDialog createHeroSelectionDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(R.layout.hero_selection_dialog);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if champ selected go to main activity
                startMainActivity();
                clearPasswords();
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        return builder.create();
    }

    @Override
    public void onClick(View v) {
        boolean validated = false;
        switch (v.getId()) {
            case R.id.login_tv:
                setExistingUserUI();
                break;
            case R.id.sign_up_tv:
                setNewUserUI();
                break;
            case R.id.loginButton:
                validated = validateUser(mExistingUser);
                if (validated) {
                    //TODO make 'auhtSuccess' local or a return value of the authenticateUser() method perhaps?
                    authenticateUser();
                    if (authSuccess) {
                        startMainActivity();
                    } else { //if authentication fails...
                        mToast.makeText(mContext, "Authentication Failed", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.create_new_button:
                validated = validateUser(mExistingUser);
                if (validated) {
                    createNewUser();
                }
                break;
//            case R.id.cancel_dialog_button:
//                if(mAlertDialog.isShowing())
//                    mAlertDialog.cancel();
//                break;
//            case R.id.ok_dialog_button:
//                //TODO: Check if they have selected a champ then close Dialog
//                startMainActivity();
//                clearPasswords();
//                break;
        }
    }

    private void addItem(){

        if (mClient == null) {
            return;
        }

        // Create a new mStudent
        final Student mStudent1 = new Student();

        mStudent1.setMemail(mEmail.getText().toString());
        mStudent1.setMpassword(mPassword.getText().toString());
        //mStudent1.setMschoolId(1001);
        //mStudent1.setmId(1032);

        //mStudent.setText(mInsertText.getText().toString());

        // Insert the new mStudent
        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    //final Student entity =
                            mToDoTable.insert(mStudent1).get();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //mInsertInfo.setText(entity.getMemail());
                            //mAdapter.add(entity);
                        }
                    });
                } catch (final Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        };

        runAsyncTask(task);
    }


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
                    //System.out.println("placeholder");
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
