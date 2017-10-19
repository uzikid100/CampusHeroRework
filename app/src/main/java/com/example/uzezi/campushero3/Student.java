package com.example.uzezi.campushero3;

/**
 * Created by Christopher on 10/1/2017.
 */

/**
 * Created by Christopher on 9/30/2017.
 */

public class Student {
    /**
     * Item text
     */
    @com.google.gson.annotations.SerializedName("lastLongitude")
    private String mlastLongitude;

    /**
     * Item Id
     */
    @com.google.gson.annotations.SerializedName("id")
    private int mId;

    /**
     * Item mytext
     */
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

    @com.google.gson.annotations.SerializedName("profilePicture")
    private byte[] mprofilePicture;

    /**
     * UserItem constructor
     */
    public Student() {

    }

    @Override
    public String toString() {return getMfirstName();}

    /**
     * Initializes a new UserItem
     *
     * @param id
     *            The item id
     */
    public Student(int id) {
        this.setId(id);
    }


    public int getId() {
        return mId;
    }

    public final void setId(int id) {
        mId = id;
    }

    public String getMlastLongitude() {
        return mlastLongitude;
    }

    public void setMlastLongitude(String mlastLongitude) {
        this.mlastLongitude = mlastLongitude;
    }

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
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

    @Override
    public boolean equals(Object o) {
        return o instanceof Student && ((Student) o).mId == mId;
    }
}