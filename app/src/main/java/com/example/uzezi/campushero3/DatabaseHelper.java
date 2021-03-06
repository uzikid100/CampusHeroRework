package com.example.uzezi.campushero3;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by Christopher on 11/6/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final  int DATABASE_VERSION = 23;

    static final String dbName = "campushero";

    //variable names for the Student table columns.
    static final String student_Table = "Student";
    static final String student_ID = "id";
    static final String student_CreatedAt = "createdAt";
    static final String student_UpdatedAt = "updatedAt";
    static final String student_Version = "version";
    static final String student_Deleted = "deleted";
    static final String student_LastLongitude = "lastLongitude";
    static final String student_LastLatitude= "lastLatitude";
    static final String student_FirstName = "firstName";
    static final String student_LastName = "lastName";
    static final String student_Email = "email";
    static final String student_Password = "password";
    static final String student_GPA = "gpa";
    static final String student_ProfilePicture = "profilePicture";
    static final String student_SchoolId= "schoolId";

    //variable names for the Classes table columns.
    static final String class_Table = "Classes";
    static final String class_ID = "id";
    static final String class_CreatedAt = "createdAt";
    static final String class_UpdatedAt = "updatedAt";
    static final String class_Version = "version";
    static final String class_Deleted = "deleted";
    static final String class_Longitude = "longitude";
    static final String class_Latitude= "latitude";
    static final String class_StartTime = "startTime";
    static final String class_SimpleName = "simpleName";
    static final String class_StudentId = "studentId";
    static final String class_BuildingId = "buildingId";

    //variable names for the POI table columns.
    static final String poi_Table = "PointsOfInterest";
    static final String poi_ID = "id";
    static final String poi_CreatedAt = "createdAt";
    static final String poi_UpdatedAt = "updatedAt";
    static final String poi_Version = "version";
    static final String poi_Deleted = "deleted";
    static final String poi_Longitude = "longitude";
    static final String poi_Latitude= "latitude";
    static final String poi_SimpleName = "simpleName";


    static final String viewStudents = "ViewStudents";

    public DatabaseHelper(Context context) {
        super(context, dbName, null,DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub

        db.execSQL("CREATE TABLE IF NOT EXISTS "+student_Table+" ("+student_ID+ " TEXT, "+
                student_CreatedAt+ " TEXT, "+student_UpdatedAt+" TEXT, "+student_Version+
                " TEXT ,"+student_Deleted+ " BOOLEAN, "+
                student_LastLongitude+ " TEXT, "+student_LastLatitude+
                " TEXT, "+student_FirstName+" TEXT, "+student_LastName+" TEXT, "+student_Email+
                " TEXT, "+student_Password+" TEXT, "+student_GPA+" TEXT, "+student_ProfilePicture+
                " TEXT, "+student_SchoolId+" TEXT)");

        db.execSQL("CREATE TABLE IF NOT EXISTS "+poi_Table+" ("+poi_ID+ " TEXT, "+
                poi_CreatedAt+ " TEXT, "+poi_UpdatedAt+" TEXT, "+poi_Version+
                " TEXT ,"+poi_Deleted+ " BOOLEAN, "+
                poi_Longitude+ " TEXT, "+poi_Latitude+
                " TEXT, "+poi_SimpleName+
                " TEXT)");

        db.execSQL("CREATE TABLE IF NOT EXISTS "+class_Table+" ("+class_ID+ " TEXT, "+
                class_CreatedAt+ " TEXT, "+class_UpdatedAt+" TEXT, "+class_Version+
                " TEXT ,"+class_Deleted+ " BOOLEAN, "+
                class_Longitude+ " TEXT, "+class_Latitude+
                " TEXT, "+class_StartTime+" TEXT, "+class_SimpleName+
                " TEXT, "+class_BuildingId+" TEXT, "+class_StudentId+" TEXT)");

    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub

        db.execSQL("DROP TABLE IF EXISTS "+student_Table);
        db.execSQL("DROP TABLE IF EXISTS "+class_Table);
        db.execSQL("DROP TABLE IF EXISTS "+poi_Table);
        //db.execSQL("DROP TABLE IF EXISTS "+studentToClass_Table);

        //db.execSQL("DROP TRIGGER IF EXISTS fk_studenttoclass_classid");
        //db.execSQL("DROP TRIGGER IF EXISTS fk_studenttoclass_studentid");
        //db.execSQL("DROP VIEW IF EXISTS "+viewStudents);
        onCreate(db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            // Enable foreign key constraints
            String query = String.format ("PRAGMA foreign_keys = %s","ON");
            db.execSQL(query);
        }
    }

    public void rebuildDB(String studentId){
        SQLiteDatabase db = this.getReadableDatabase();
//        if(studentId != getFirstStudent().getId()){
            db.execSQL("DROP TABLE IF EXISTS "+student_Table);
            db.execSQL("DROP TABLE IF EXISTS "+class_Table);
            db.execSQL("DROP TABLE IF EXISTS "+poi_Table);
            onCreate(db);
//        }
    }

    public void InsertStudent(Student student)
    {
        SQLiteDatabase db=this.getWritableDatabase();

        ContentValues cv=new ContentValues();
        cv.put(student_ID, student.getId());
        cv.put(student_Password, student.getMpassword());
        cv.put(student_Email, student.getMemail());
        db.insert(student_Table, null, cv);
        db.close();
    }

    public void InsertPoi(PointsOfInterest poi)
    {
        SQLiteDatabase db=this.getWritableDatabase();

        ContentValues cv=new ContentValues();
        cv.put(poi_ID, poi.getId());
        cv.put(poi_Longitude, poi.getMlongitude());
        cv.put(poi_Latitude, poi.getMlatitude());
        cv.put(poi_SimpleName, poi.getMsimpleName());
        db.insert(poi_Table, null, cv);
        db.close();
    }

    public int UpdateStudent(Student student)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(student_Email, student.getMemail());
        cv.put(student_Password, student.getMpassword());
        cv.put(student_FirstName, student.getMfirstName());
        return db.update(student_Table, cv, student_ID+"=?",
                new String []{String.valueOf(student.getId())});

    }

    public void DeleteStudent(Student student)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        db.delete(student_Table,student_ID+"=?", new String [] {String.valueOf(student.getId())});
        db.close();
    }

    public Student getStudent(String index)
    {
        SQLiteDatabase db=this.getReadableDatabase();

        Cursor cur=db.query(student_Table, new String[] {student_ID, student_Email, student_Password}, student_ID + "=?",
            new String[]{index}, null, null, null, null);
        if (cur != null)
            cur.moveToFirst();

    Student student = new Student(cur.getString(0), cur.getString(1), cur.getString(2));
        cur.close();
        return student;
    }

    public PointsOfInterest getPoi(String name)
    {
        SQLiteDatabase db=this.getReadableDatabase();

        Cursor cur=db.query(poi_Table, new String[] {poi_ID, poi_Longitude, poi_Latitude, poi_SimpleName}, poi_SimpleName + "=?",
                new String[]{name}, null, null, null, null);
        if (cur != null)
            cur.moveToFirst();

        PointsOfInterest poi = new PointsOfInterest(cur.getString(0), cur.getString(1), cur.getString(2), cur.getString(3));
        cur.close();
        return poi;
    }


    public PointsOfInterest getFirstPoi()
    {
        SQLiteDatabase db=this.getReadableDatabase();
        PointsOfInterest poi = new PointsOfInterest();

        Cursor cur=db.query(poi_Table, new String[] {poi_ID, poi_Longitude, poi_Latitude, poi_SimpleName}, null,
                new String[]{}, null, null, null, null);
        if (cur != null) {
            cur.moveToFirst();
            int colCount = cur.getCount();
            if(colCount != 0){
                poi = new PointsOfInterest(cur.getString(0), cur.getString(1), cur.getString(2), cur.getString(3));
            }
        }
        cur.close();
        return poi;
    }

    public String[] getAllPois()
    {
        SQLiteDatabase db=this.getReadableDatabase();
        String[] names = new String[]{};
        Cursor cur=db.query(poi_Table, new String[] {poi_SimpleName}, null,
                null, null, null, null, null);
        if (cur != null) {
            names = new String[cur.getCount()];
            cur.moveToFirst();
            int colCount = cur.getCount();
            for (int i = 0; i < colCount; i++) {
                names[i] = cur.getString(0);
                cur.moveToNext();
            }
        }
        cur.close();
        return names;
    }

    public Student getFirstStudent()
    {
        SQLiteDatabase db=this.getReadableDatabase();
        Student student = new Student();

        Cursor cur=db.query(student_Table, new String[] {student_ID, student_Email, student_Password}, null,
                new String[]{}, null, null, null, null);
        if (cur != null) {
            cur.moveToFirst();
            int colCount = cur.getCount();
            if(colCount != 0){
                student = new Student(cur.getString(0), cur.getString(1), cur.getString(2));
            }
        }
        cur.close();
        return student;
    }

    public String getStudentId(String studentId){
        String id = "";
        SQLiteDatabase db=this.getReadableDatabase();
        try {
            Cursor cur = db.query(student_Table, new String[]{student_ID}, student_ID+"=?",
                    new String[]{studentId}, null, null, null, null);
            if (cur != null)
                cur.moveToFirst();
            id = cur.getString(0);
        }catch (Exception e){

        }
        db.close();
        return id;
    }

    public void InsertClasses(ArrayList<Classes> classes) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (int i = 0; i < classes.size(); i++) {
            ContentValues cv = new ContentValues();
            cv.put(class_ID, classes.get(i).getId());
            cv.put(class_SimpleName, classes.get(i).getMsimpleName());
            cv.put(class_StartTime, classes.get(i).getMstartTime());
            cv.put(class_StudentId, classes.get(i).getmStudentId());
            db.insert(class_Table, null, cv);
        }
        db.close();
    }

    public void DeleteClass(Classes aClass)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        db.delete(class_Table,class_ID+"=?", new String [] {String.valueOf(aClass.getId())});
        db.close();
    }

    public ArrayList<Classes> getUserClasses(String index)
    {
        SQLiteDatabase db=this.getReadableDatabase();
        ArrayList<Classes> classes = new ArrayList<>();

        Cursor cur=db.query(class_Table, new String[] {class_ID, class_SimpleName, class_StartTime,class_StudentId}, class_StudentId+"=?",
                new String[]{index}, null, null, null, null);
        if (cur != null)
            cur.moveToFirst();

        if(cur.getCount()!=0) {
            do {
                Classes classes1 = new Classes(cur.getString(0), cur.getString(1), cur.getString(2), cur.getString(3));
                classes.add(classes1);
            } while (cur.moveToNext());
        }

        db.close();
        return classes;
    }

    public String[] getUserClassNames(String index) {
        //SQLiteDatabase db = this.getReadableDatabase();

        ArrayList<Classes> classes = getUserClasses(index);
        int counter = classes.size();
        String[] names = new String[counter];

        for (int i = 0; i < counter; i++) {
            names[i] = classes.get(i).getMsimpleName();
        }

        return names;
    }
}