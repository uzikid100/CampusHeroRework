package com.example.uzezi.campushero3.Login;

import android.annotation.SuppressLint;
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
import android.widget.ProgressBar;

import com.example.uzezi.campushero3.Classes;
import com.example.uzezi.campushero3.DatabaseHelper;
import com.example.uzezi.campushero3.MainActivity;
import com.example.uzezi.campushero3.PointsOfInterest;
import com.example.uzezi.campushero3.R;
import com.example.uzezi.campushero3.Student;
import com.example.uzezi.campushero3.UiErrorLog;
import com.microsoft.windowsazure.mobileservices.http.OkHttpClientFactory;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.squareup.okhttp.OkHttpClient;


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
    private TextView mCampusTv;
    private ProgressBar mProgressBar2;

    private MobileServiceClient mClient;
    private MobileServiceTable<Student> mUserTable;
    private MobileServiceTable<Classes> mClassTable;
    private MobileServiceTable<PointsOfInterest> mPOITable;
    private Student mStudent = new Student();
    private Classes mClass = new Classes();
    private PointsOfInterest mPoi = new PointsOfInterest();

    private Context mContext;
    private AlertDialog mAlertDialog;
    private Toast mToast;
    private boolean mExistingUser = true;
    private UiErrorLog mLogger;
    public ArrayList<Student> mStudents = new ArrayList<>();
    public ArrayList<Classes> mClasses = new ArrayList<>();
    public ArrayList<PointsOfInterest> mPOIs = new ArrayList<>();

    public DatabaseHelper db = new DatabaseHelper(this);


    //TODO required fields missing depending on new/existing user

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        try {
            mClient = new MobileServiceClient(
                    "https://campushero1.azurewebsites.net",
                    this
            );

            // Extend timeout from default of 10s to 30s
            mClient.setAndroidHttpClientFactory(new OkHttpClientFactory() {
                @Override
                public OkHttpClient createOkHttpClient() {
                    OkHttpClient client = new OkHttpClient();
                    client.setReadTimeout(40, TimeUnit.SECONDS);
                    client.setWriteTimeout(40, TimeUnit.SECONDS);
                    return client;
                }
            });


            mUserTable = mClient.getTable(Student.class);
            mClassTable = mClient.getTable(Classes.class);
            mPOITable = mClient.getTable(PointsOfInterest.class);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        mLogger = new UiErrorLog(getApplicationContext());
        mContext = this.getApplicationContext();

        mEmail = (EditText) findViewById(R.id.email_editText);
        mPassword = (EditText) findViewById(R.id.password_editText);
        mReenterPassword = (EditText) findViewById(R.id.reenter_password_editText);
        mLoginButton = (Button) findViewById(R.id.loginButton);
        mCreateNewButton = (Button) findViewById(R.id.create_new_button);
        mSignUpTV = (TextView) findViewById(R.id.sign_up_tv);
        mLoginTv = (TextView) findViewById(R.id.login_tv);
        mCampusTv = (TextView) findViewById(R.id.campus_tv);
        mProgressBar2 = (ProgressBar) findViewById(R.id.progressBar2);
        mProgressBar2.setVisibility(ProgressBar.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mProgressBar2.setVisibility(ProgressBar.GONE);
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

    private boolean validateUser(boolean existingUser) {
        try {
            boolean validEmailAndPass = validateEmailAndPass();
            if (!existingUser) {
                boolean reenterValid = validateReenterPassword();
                if(reenterValid){
                    addUser();
                }
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
        addUser();
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
                    authenticateUser();
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

    /*
    Add User:
       This method adds a new user to the Student database table, based on the inputted credentials.
     */
    private void addUser(){
        if (mClient == null) {
            //authSuccess = false;
            return;
        }

        // Create a new Student with the specified username and password.
        mStudent.setMemail(mEmail.getText().toString());
        mStudent.setMpassword(mPassword.getText().toString());

        /*
         Insert the new mStudent into the database. The AsyncTask variable runs in the background.
        */
        @SuppressLint("StaticFieldLeak")
        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... params) {
                try {
                     mUserTable.insert(mStudent).get();
                } catch (final Exception e) {
                    e.printStackTrace();
                }

                return null;
            }
            /*
            unused method.
             */
            @Override
            protected void onPostExecute(Void result) {
                mProgressBar2.setMax(100);
                mProgressBar2.setProgress(50);
            }

            /*
            This method runs before doInBackground. In this case, it just displays the loading
                symbol to the user.
             */
            @Override
            protected void onPreExecute() {
                if (mProgressBar2 != null) mProgressBar2.setVisibility(ProgressBar.VISIBLE);
            }

            /*
            unused method.
             */
            @Override
            protected void onProgressUpdate(Void... values) {
            }
        };

        runAsyncTask(task);

    }

    /*
    Get Classes:
        This method is called when the credentials entered by the user are correct and the SQLite
            database can be populated with the student's classes.
     */
    private void getClasses(){
        if(mClient == null){
            return;
        }

        final String studentId = mStudent.getId();

        @SuppressLint("StaticFieldLeak")
        final AsyncTask<Void, Void, Void> classes = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    mClasses = mClassTable.where().field("studentId").eq(studentId).execute().get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

                return null;
            }
            @Override
            protected void onPostExecute(Void result){
                //if (!mClasses.isEmpty()) {
                    String id = db.getStudentId(studentId);
                    db.rebuildDB(mStudent.getId());
                        db.InsertStudent(mStudent);
                        db.InsertClasses(mClasses);
                for (int i = 0; i < mPOIs.size(); i++) {
                    db.InsertPoi(mPOIs.get(i));
                }
                    mProgressBar2.setProgress(85);
                    startMainActivity();
            }

        };
        runAsyncTask(classes);
    }

    private void getPOIs(){
        if(mClient == null){
            return;
        }

        @SuppressLint("StaticFieldLeak")
        final AsyncTask<Void, Void, Void> classes = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    mPOIs = mPOITable.select().execute().get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

                return null;
            }
            @Override
            protected void onPostExecute(Void result){
                getClasses();
                mProgressBar2.setProgress(75);
                }

        };
        runAsyncTask(classes);
    }

    /*
    Authenticate User:
        This method is called when the credentials entered by the user are ready to be compared to
         the database Student table.
     */
    private void authenticateUser(){
        if (mClient == null) {
            return;
        }

        //Store the value in the email field in a new variable, which will be passed to the database
        final String email = mEmail.getText().toString();

        /*
         Compare the email to those in the database's Student table. TAsyncTask is an abstract class
          provided by Android which helps us to use the UI thread properly. This class allows us to
          perform long/background operations and show its result on the UI thread without having to
          manipulate threads. Here doInBackground task will be implemented in background and
          onPostExecute will be shown on GUI.
          */
        @SuppressLint("StaticFieldLeak")
        final AsyncTask<Void, Void, Void> authenticate = new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    mStudents = mUserTable.where().field("email").eq(email).execute().get();

                } catch (final Exception e) {
                    mToast.makeText(mContext, "An error occurred", Toast.LENGTH_SHORT).show();
                    mProgressBar2.setProgress(0);
                    e.printStackTrace();
                }
                return null;
            }

            /*
            This method runs when the doInBackground method finishes. It first checks if the email
                was in the database and then checks if the password entered by the user matches the
                one for the email in the database. Returns an error message to the user if either of
                these are false.
             */
            @Override
            protected void onPostExecute(Void result) {
                if (!mStudents.isEmpty()) {
                    mStudent = mStudents.get(0);
                    if (mStudent.getMpassword().equals(mPassword.getText().toString())) {
                        //db.InsertStudent(mStudent);
                        mProgressBar2.setProgress(50);
                        getPOIs();
                    }
                    else {
                        mToast.makeText(mContext, "Wrong password", Toast.LENGTH_SHORT).show();
                        mProgressBar2.setProgress(0);
                    }
                } else {
                    mToast.makeText(mContext, "Authentication Failed. Try again.", Toast.LENGTH_SHORT).show();
                    mProgressBar2.setProgress(0);
                }
            }

            /*
            This method runs before doInBackground. In this case, it just displays the loading
                symbol to the user.
             */
            @Override
            protected void onPreExecute() {
                //if (mProgressBar != null) mProgressBar.setVisibility(ProgressBar.VISIBLE);
                if (mProgressBar2 != null) mProgressBar2.setVisibility(ProgressBar.VISIBLE);
                mProgressBar2.setProgress(25);
            }

            /*
            unused method.
             */
            @Override
            protected void onProgressUpdate(Void... values) {
            }
        };
        runAsyncTask(authenticate);
    }
    /*
    Run Async Task:
        This method takes a task and executes it. If the OS version is less than or equal to
            Honeycomb, it has to execute a little differently, but otherwise it just executes.
     */
    private AsyncTask<Void, Void, Void> runAsyncTask(AsyncTask<Void, Void, Void> task) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            return task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            return task.execute();
        }
    }
}