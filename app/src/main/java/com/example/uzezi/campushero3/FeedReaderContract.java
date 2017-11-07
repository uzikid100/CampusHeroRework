package com.example.uzezi.campushero3;

import android.provider.BaseColumns;
import android.database.sqlite.*;
import android.content.Context;


/**
 * Created by Christopher on 11/1/2017.
 */

public final class FeedReaderContract {

    private FeedReaderContract(){}

    public static class feedUsers implements BaseColumns{
        public static String TABLE_NAME = "users";
        public static String COLUMN_NAME_SCHOOL = "school";
        public static String COLUMN_NAME_FNAME = "firstName";
        public static String COLUMN_NAME_LNAME = "lastName";
        public static String COLUMN_NAME_EMAIL = "email";
        public String SQL_CREATE_ENTRIES;
        public String SQL_DELETE_ENTRIES;

        public String createUsers() {
            SQL_CREATE_ENTRIES =
                    "CREATE TABLE " + feedUsers.TABLE_NAME + " (" +
                            feedUsers._ID + " INTEGER PRIMARY KEY," +
                            feedUsers.COLUMN_NAME_SCHOOL+ " TEXT," +
                            feedUsers.COLUMN_NAME_FNAME + " TEXT)" +
                            feedUsers.COLUMN_NAME_LNAME + " TEXT)" +
                            feedUsers.COLUMN_NAME_EMAIL + " TEXT)";
            return SQL_CREATE_ENTRIES;
        }

        public String deleteUsers() {
            SQL_DELETE_ENTRIES =
                    "DROP TABLE IF EXISTS " + feedUsers.TABLE_NAME;
            return SQL_DELETE_ENTRIES;
        }

        public class CampusHeroDbHelper extends SQLiteOpenHelper {
            // If you change the database schema, you must increment the database version.
            public static final int DATABASE_VERSION = 1;
            public static final String DATABASE_NAME = "CampusHero.db";

            public CampusHeroDbHelper(Context context) {
                super(context, DATABASE_NAME, null, DATABASE_VERSION);
            }
            public void onCreate(SQLiteDatabase db) {
                db.execSQL(SQL_CREATE_ENTRIES);
            }
            public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
                // This database is only a cache for online data, so its upgrade policy is
                // to simply to discard the data and start over
                db.execSQL(SQL_DELETE_ENTRIES);
                onCreate(db);
            }
            public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
                onUpgrade(db, oldVersion, newVersion);
            }
        }

    }
}
