package com.example.uzezi.campushero3;

/**
 * Created by Christopher on 10/1/2017.
 */

/**
 * Created by Christopher on 9/30/2017.
 */

public class Student {


    /**
     * Item Id
     */
    @com.google.gson.annotations.SerializedName("id")
    private String mId;

    @com.google.gson.annotations.SerializedName("lastLongitude")
    private String mlastLongitude;

    @com.google.gson.annotations.SerializedName("lastLatitude")
    private String mlastLatitude;

    @com.google.gson.annotations.SerializedName("schoolId")
    private int mschoolId;

    @com.google.gson.annotations.SerializedName("firstName")
    private String mfirstName;

    @com.google.gson.annotations.SerializedName("lastName")
    private String mlastName;

    @com.google.gson.annotations.SerializedName("email")
    private String memail;

    @com.google.gson.annotations.SerializedName("password")
    private String mpassword;

    @com.google.gson.annotations.SerializedName("gpa")
    private String mgpa;

    @com.google.gson.annotations.SerializedName("deleted")
    private boolean deleted;

    @com.google.gson.annotations.SerializedName("profilePicture")
    private byte[] mprofilePicture;

    /**
     * UserItem constructor
     */
    public Student() {

    }

    public Student(String email, String password, int school){
        memail = email;
        mpassword = password;
        mschoolId = school;
        deleted = false;
    }

    @Override
    public String toString() {return getMemail();}

    /**
     * Initializes a new UserItem
     *
     * @param id
     *            The item id
     */
    public Student(String id, String email, String password) {
        this.setId(id);
        this.setMemail(email);
        this.setMpassword(password);
    }


    public String getId() {
        return mId;
    }

    public final void setId(String id) {
        mId = id;
    }

    public String getMlastLongitude() {
        return mlastLongitude;
    }

    public void setMlastLongitude(String mlastLongitude) {
        this.mlastLongitude = mlastLongitude;
    }

    public String getMlastLatitude() {
        return mlastLatitude;
    }

    public void setMlastLatitude(String mlastLatitude) {
        this.mlastLatitude = mlastLatitude;
    }

    public int getMschoolId() {
        return mschoolId;
    }

    public void setMschoolId(int mschoolId) {
        this.mschoolId = mschoolId;
    }

    public String getMfirstName() {
        return mfirstName;
    }

    public void setMfirstName(String mfirstName) {
        this.mfirstName = mfirstName;
    }

    public String getMlastName() {
        return mlastName;
    }

    public void setMlastName(String mlastName) {
        this.mlastName = mlastName;
    }

    public String getMemail() {
        return memail;
    }

    public void setMemail(String memail) {
        this.memail = memail;
    }

    public String getMpassword() {
        return mpassword;
    }

    public void setMpassword(String mpassword) {
        this.mpassword = mpassword;
    }
/*
    public String getMgpa() {
        return mgpa;
    }

    public void setMgpa(String mgpa) {
        this.mgpa = mgpa;
    }

    public byte[] getMprofilePicture() {
        return mprofilePicture;
    }

    public void setMprofilePicture(byte[] mprofilePicture) {
        this.mprofilePicture = mprofilePicture;
    }
*/
    @Override
    public boolean equals(Object o) {
        return o instanceof Student && ((Student) o).mId == mId;
    }
}
