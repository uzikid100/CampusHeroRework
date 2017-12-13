package com.example.uzezi.campushero3.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.uzezi.campushero3.ClassAdapter;
import com.example.uzezi.campushero3.Classes;
import com.example.uzezi.campushero3.DatabaseHelper;
import com.example.uzezi.campushero3.R;
import com.example.uzezi.campushero3.Student;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.http.OkHttpClientFactory;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.squareup.okhttp.OkHttpClient;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * Created by uzezi on 10/2/2017.
 */

public class ProfileFragment extends Fragment {
    private static final String TAG = "ProfileFragment";
    private MobileServiceClient mClient;
    private MobileServiceTable<Classes> mClassTable;
    private ClassAdapter mAdapter;
    ListView myListView;
    String[] classNames;
    EditText name;
    ListView lv;
    EditText number;
    EditText time;
    Button addButton;
    Context context;
    TextView noItems;
    Classes insertClass;

    public DatabaseHelper db;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        context = inflater.getContext();
        db = new DatabaseHelper(context);
        addButton = (Button) view.findViewById(R.id.addButton);
        name = (EditText) view.findViewById(R.id.NameeditText);
        number = (EditText) view.findViewById(R.id.NumbereditText);
        time = (EditText) view.findViewById(R.id.TimeeditText);

        try {
            mClient = new MobileServiceClient(
                    "https://campushero1.azurewebsites.net",
                    getActivity()
            );
            // Extend timeout from default of 10s to 30s
            mClient.setAndroidHttpClientFactory(new OkHttpClientFactory() {
                @Override
                public OkHttpClient createOkHttpClient() {
                    OkHttpClient client = new OkHttpClient();
                    client.setReadTimeout(10, TimeUnit.SECONDS);
                    client.setWriteTimeout(10, TimeUnit.SECONDS);
                    return client;
                }
            });
            mClassTable = mClient.getTable(Classes.class);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        noItems = (TextView) view.findViewById(R.id.noItems_tv);
        noItems.setVisibility(noItems.GONE);

        lv = (ListView) view.findViewById(R.id.List1);
        refreshListView();
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addClassToUser();
            }
        });

        return view;
    }

    private void addClassToUser(){
        if (mClient == null) {
            //authSuccess = false;
            return;
        }
        /*
         Insert the new mStudent into the database. The AsyncTask variable runs in the background.
        */
        @SuppressLint("StaticFieldLeak")
        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>(){
            /*
            This method runs before doInBackground. In this case, it just displays the loading
                symbol to the user.
             */
            @Override
            protected void onPreExecute() {

                insertClass = new Classes();
                insertClass.setMsimpleName(name.getText().toString());
                insertClass.setMstartTime(time.getText().toString());
                insertClass.setmStudentId(db.getFirstStudent().getId());
            }

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    mClassTable.insert(insertClass).get();
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
                ArrayList<Classes> classToAdd = new ArrayList<>();
                classToAdd.add(insertClass);
                db.InsertClasses(classToAdd);
                refreshListView();
            }
        };
        runAsyncTask(task);

    }

    private void refreshListView(){
        ArrayList<Classes> classList = GetlistClasses();
        if (classList.isEmpty())
            noItems.setVisibility(noItems.VISIBLE);
        lv.setAdapter(new ClassAdapter(getActivity(), classList));
    }

    private ArrayList<Classes> GetlistClasses(){
        ArrayList<Classes> classeslist = new ArrayList<>();
        String studentId = db.getFirstStudent().getId();
        if(studentId.equals(""))
            try {
                wait(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        classeslist = db.getUserClasses(studentId);

        return classeslist;
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
