package com.example.uzezi.campushero3;

/**
 * Created by Christopher on 11/9/2017.
 */

public class Classes {
    /**
     * Item Id
     */
    @com.google.gson.annotations.SerializedName("id")
    private String mId;

    @com.google.gson.annotations.SerializedName("startTime")
    private String mstartTime;

    @com.google.gson.annotations.SerializedName("simpleName")
    private String msimpleName;

    @com.google.gson.annotations.SerializedName("longitude")
    private String mlongitude;

    @com.google.gson.annotations.SerializedName("latitude")
    private String mlatitude;

    @com.google.gson.annotations.SerializedName("buildingId")
    private String mbuildingId;

    @com.google.gson.annotations.SerializedName("StudentId")
    private String mStudentId;

    /**
     * UserItem constructor
     */
    public Classes() {

    }

    @Override
    public String toString() {return getMsimpleName();}

    /**
     * Initializes a new UserItem
     *
     * @param id
     *            The item id
     */
    public Classes(String id, String simpleName, String startTime, String studentId) {
        this.setId(id);
        this.setMsimpleName(simpleName);
        startTime = startTime.substring(11,16);
        this.setMstartTime(startTime);
        this.setmStudentId(studentId);
    }


    public String getId() {
        return mId;
    }

    public final void setId(String id) {
        mId = id;
    }

    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }

    public String getMstartTime() {
        return mstartTime;
    }

    public void setMstartTime(String mstartTime) {
        this.mstartTime = mstartTime;
    }

    public String getMsimpleName() {
        return msimpleName;
    }

    public void setMsimpleName(String msimpleName) {
        this.msimpleName = msimpleName;
    }

    public String getMlongitude() {
        return mlongitude;
    }

    public void setMlongitude(String mlongitude) {
        this.mlongitude = mlongitude;
    }

    public String getMlatitude() {
        return mlatitude;
    }

    public void setMlatitude(String mlatitude) {
        this.mlatitude = mlatitude;
    }

    public String getMbuildingId() {
        return mbuildingId;
    }

    public void setMbuildingId(String mbuildingId) {
        this.mbuildingId = mbuildingId;
    }

    public String getmStudentId() {
        return mStudentId;
    }

    public void setmStudentId(String mStudentId) {
        this.mStudentId = mStudentId;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Classes && ((Classes) o).mId == mId;
    }
}
