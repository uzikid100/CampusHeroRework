package com.example.uzezi.campushero3.Fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.uzezi.campushero3.DatabaseHelper;
import com.example.uzezi.campushero3.HttpHandler;
import com.example.uzezi.campushero3.MyUrlTileProvider;
import com.example.uzezi.campushero3.PointsOfInterest;
import com.example.uzezi.campushero3.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.SupportPlaceAutocompleteFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PointOfInterest;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.TileOverlayOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

//
//TODO implement google 'PlacesService', 'DirectionService,' and 'DirectionRenderer'
//TODO research google.maps.infowindow
//
public class MapFragment extends Fragment
        implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener,
        GoogleMap.OnPoiClickListener,
        View.OnClickListener{

    protected final String TAG = "MapFragment";

    private GoogleMap mMap;
    private Polyline line;
    private LatLng DEFAULT_LOCATION = new LatLng(33.465004, -86.790231);
    private final int DEFAULT_ZOOM = 15;
    private final static int Fine_Location_Request_Code = 1; //arbitrary #

    private String mTileUrl = "http://a.tile.openstreetmap.org/{z}/{x}/{y}.png";
    private static String routeUrlTemplate = "https://graphhopper.com/api/1/route?point={a}&point={b}&vehicle=foot&points_encoded=false&type=json&locale=de&key=e07736ac-c40a-4b8d-b566-57aa5a9f23ec";
    private static String mRouteUrl;
    //https://graphhopper.com/api/1/route?point=33.465017%2C-86.790308&point=33.465271%2C-86.793076&vehicle=foot&points_encoded=false&type=json&locale=de&key=e07736ac-c40a-4b8d-b566-57aa5a9f23ec
    private String startingPoint;
    private String destinationPoint;
    private AutoCompleteTextView startTV;
    private AutoCompleteTextView endTV;
    private ArrayList<HashMap<String, Double>> coordinatesList;
    private ImageButton searchButton;
    private DatabaseHelper db;



    private LocationRequest mLocationRequest;
    private Location mLastKnownLocation;
    private FusedLocationProviderApi mFusedLocationApi;
    private GoogleApiClient mGoogleApiClient;
    private Context mContext;

    private boolean isLocationFabVisible = false;

    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        SupportPlaceAutocompleteFragment supportPlaceAutocompleteFragment = (SupportPlaceAutocompleteFragment) getFragmentManager()
                .findFragmentById(R.id.place_autocomplete_fragment);


        mFusedLocationApi = LocationServices.FusedLocationApi;
        mContext = this.getActivity().getApplicationContext();

        mGoogleApiClient = new GoogleApiClient.Builder(this.getActivity().getApplicationContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage(this.getActivity(), this)
                .build();

        //TODO set to 10 seconds and balanced on a real device...
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        db = new DatabaseHelper(getActivity());
        mRouteUrl = routeUrlTemplate;
        startTV = (AutoCompleteTextView) getActivity().findViewById(R.id.autoTvFrom);
        endTV = (AutoCompleteTextView) getActivity().findViewById(R.id.autoTvTo);
        String[] POIs = db.getAllPois();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.drop_down_layout, POIs);
        startTV.setAdapter(adapter);
        endTV.setAdapter(adapter);
        searchButton = (ImageButton) getActivity().findViewById(R.id.invertTextButton);
        searchButton.setOnClickListener(this);

        return view;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        setMapSettings();
        //
//        MyUrlTileProvider mTileProvider = new MyUrlTileProvider(256, 256, mTileUrl);
//        mMap.addTileOverlay(new TileOverlayOptions().tileProvider(mTileProvider)).setTransparency(0.5f);
        goToCurrentLocation();

        //TODO check if permission was granted or not
    }


    private void setMapSettings() {
        if (!mMap.equals(null)) {
            mMap.getUiSettings().setAllGesturesEnabled(true);
            mMap.getUiSettings().setMapToolbarEnabled(false);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
            mMap.setOnPoiClickListener(this); //must explicitly set PoiClickListener to 'this' instance of Google maps
        }
        if (ActivityCompat.checkSelfPermission(this.mContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        }
        else {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Fine_Location_Request_Code);
        }

        //TODO try to setSupportActionBar if I can
        //TODO uISettings.setMapToolbarEnabled(true);
    }

    @Override
    public void onLocationChanged(Location location) {

    }
    //Chritopher Kawell, 12/5/2017
    //TODO: construct a url and request a JSON packet using HttpHandler.java.
    //TODO: parse the data and extract the coordinates.
    //TODO: store in a hashtable or arraylist.
    //TODO: display the coordinates as a polyline.
    public void drawPath(){
        if(line != null)
            line.remove();
        PolylineOptions options = new PolylineOptions().width(5).color(Color.BLUE);
        for (int i = 0; i < coordinatesList.size(); i++) {
            options.add(new LatLng(coordinatesList.get(i).get("lat"), coordinatesList.get(i).get("long")));
        }
        line = mMap.addPolyline(options);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.invertTextButton:
                startingPoint = startTV.getText().toString();
                destinationPoint = endTV.getText().toString();

                new GetCoordinates().execute();
                break;
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class GetCoordinates extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
                PointsOfInterest startPoi = db.getPoi(startingPoint);
                PointsOfInterest endPoi = db.getPoi(destinationPoint);


                startingPoint = startPoi.getMlatitude() + "%2C" + startPoi.getMlongitude();
                destinationPoint = endPoi.getMlatitude() + "%2C" + endPoi.getMlongitude();
                mRouteUrl = mRouteUrl.replace("{a}", startingPoint);
                mRouteUrl =  mRouteUrl.replace("{b}", destinationPoint);
            }catch(Exception e){
                Toast.makeText(getContext(),
                        "Invalid Entry.",
                        Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected Void doInBackground(Void... voids) {
            coordinatesList = new ArrayList<>();
            HttpHandler sh = new HttpHandler();

            String jsonStr = sh.makeServiceCall(mRouteUrl);

            Log.e(TAG, "Response from url: " + jsonStr);

            if(jsonStr != null){
                try{
                    JSONObject jsonObject = new JSONObject(jsonStr);

                    //getting json
                    JSONArray paths = jsonObject.getJSONArray("paths");
                    JSONObject instructions = paths.getJSONObject(0);
                    JSONObject points = instructions.getJSONObject("points");
                    JSONArray coordinates = points.getJSONArray("coordinates");

                    //looping through all contacts
                    for(int i = 0; i < coordinates.length(); i++){
                        JSONArray c = coordinates.getJSONArray(i);

                        double longitude = c.getDouble(0);
                        double latitude = c.getDouble(1);

                        HashMap<String, Double> coordinate = new HashMap<>();

                        //adding each child node to hashmap
                        coordinate.put("long", longitude);
                        coordinate.put("lat", latitude);

                        //adding contact to contact list
                        coordinatesList.add(coordinate);
                    }
                }catch (final JSONException e){
                    Log.e(TAG, "JSON parsing error: " + e.getMessage());
                    getActivity().runOnUiThread(new Runnable(){
                        @Override
                        public void run(){
                            Toast.makeText(getContext(), "Json", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(),
                                "Couldn't get json from server.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            drawPath();
            resetRoutUrl();
        }
    }

    public void resetRoutUrl(){
        mRouteUrl = routeUrlTemplate;
    }

    public void goToCurrentLocation() {
        //TODO remember to undo FAB click in getActivity();
        if (mLastKnownLocation != null) {
            LatLng ll = new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ll, DEFAULT_ZOOM));
//            mMap.addMarker(new MarkerOptions().position(ll)
//                    .title("You"));
        }else{
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(DEFAULT_LOCATION, DEFAULT_ZOOM));
//            mMap.addMarker(new MarkerOptions().position(DEFAULT_LOCATION)
//                    .title("You"));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case Fine_Location_Request_Code:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(this.mContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        mMap.setMyLocationEnabled(true);
                        mMap.getUiSettings().setMyLocationButtonEnabled(true);
                    }
                } else {
                    Toast.makeText(mContext, "This app requires location permissions to be granted!", Toast.LENGTH_SHORT).show();
                    this.getActivity().finish();
                }
                break;
        }
    }

    public void ChangeMapType(int type) {
        switch (type) {
            case GoogleMap.MAP_TYPE_NORMAL:
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                break;
            case GoogleMap.MAP_TYPE_HYBRID:
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                break;
            case GoogleMap.MAP_TYPE_TERRAIN:
                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                break;
            case GoogleMap.MAP_TYPE_SATELLITE:
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                break;
        }
    }

    //Used to request locationUpdates from the GoogleApiClient
    private void requestLocationUpdates() {
        PendingResult<Status> resultStatus;
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            resultStatus = LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            resultStatus.setResultCallback(new ResultCallback<Status>() {
                @Override
                public void onResult(@NonNull Status status) {
                    if (status.isSuccess()) {
                        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ) {
                            mLastKnownLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                        }
                        goToCurrentLocation();
                    }
                }
            });
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.e(TAG, "API Client Connection Succcesful!");
        requestLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.e(TAG, "API Client Connection Suspended!");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e(TAG, "API Client Connection Failed!");
    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mGoogleApiClient.isConnected()) {
            requestLocationUpdates();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    @Override
    public void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
        db.close();
    }

    @Override
    public void onPoiClick(PointOfInterest pointOfInterest) {
        Toast.makeText(mContext, pointOfInterest.name, Toast.LENGTH_SHORT).show();
    }
}

