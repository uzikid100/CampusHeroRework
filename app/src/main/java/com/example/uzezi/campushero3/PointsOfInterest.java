package com.example.uzezi.campushero3;

/**
 * Created by Christopher on 12/8/2017.
 */

public class PointsOfInterest {
    /**
     * Item Id
     */
    @com.google.gson.annotations.SerializedName("id")
    private String mId;

    @com.google.gson.annotations.SerializedName("name")
    private String msimpleName;

    @com.google.gson.annotations.SerializedName("longitude")
    private String mlongitude;

    @com.google.gson.annotations.SerializedName("latitude")
    private String mlatitude;

    /**
     * POI constructor
     */
    public PointsOfInterest() {

    }

    @Override
    public String toString() {return getMsimpleName();}

    /**
     * Initializes a new UserItem
     *
     * @param id
     *            The item id
     */
    public PointsOfInterest(String id, String longitude, String latitude, String simpleName) {
        this.setId(id);
        this.setMlongitude(longitude);
        this.setMlatitude(latitude);
        this.setMsimpleName(simpleName);
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

    @Override
    public boolean equals(Object o) {
        return o instanceof PointsOfInterest && ((PointsOfInterest) o).mId == mId;
    }
}
