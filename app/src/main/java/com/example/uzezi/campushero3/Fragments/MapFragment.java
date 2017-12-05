package com.example.uzezi.campushero3.Fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.uzezi.campushero3.MyUrlTileProvider;
import com.example.uzezi.campushero3.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.location.places.ui.SupportPlaceAutocompleteFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PointOfInterest;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.TileOverlayOptions;

//
//TODO implement google 'PlacesService', 'DirectionService,' and 'DirectionRenderer'
//TODO research google.maps.infowindow
//
public class MapFragment extends Fragment
        implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener,
        GoogleMap.OnPoiClickListener{

    protected final String TAG = "MapFragment";

    private GoogleMap mMap;
    private Polyline line;
    private LatLng DEFAULT_LOCATION = new LatLng(33.465004, -86.790231);
    private final int DEFAULT_ZOOM = 15;
    private final static int Fine_Location_Request_Code = 1; //arbitrary #
    //
    private String mUrl = "http://a.tile.openstreetmap.org/{z}/{x}/{y}.png";
    //private String mUrl = "http://www.openstreetmap.org/#map={z}/{x}/{y}";




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
        return view;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        setMapSettings();
        //
        MyUrlTileProvider mTileProvider = new MyUrlTileProvider(256, 256, mUrl);
        mMap.addTileOverlay(new TileOverlayOptions().tileProvider(mTileProvider)).setTransparency(0.5f);
        goToCurrentLocation();
        drawPath();

        //TODO check if permission was granted or not
//        mMap.setOnCameraIdleListener(onCameraMoved);
//        mMap.setO
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
        //((AppCompatActivity) getActivity()).setSupportActionBar();

    }



    @Override
    public void onLocationChanged(Location location) {

    }
    //TODO: construct a url and request a JSON packet using HttpHandler.java.
    //TODO: parse the data and extract the coordinates.
    //TODO: store in a hashtable or arraylist.
    //TODO: display the coordinates as a polyline.
    public void drawPath(){
        PolylineOptions options = new PolylineOptions().width(5).color(Color.BLUE);
        options.add(new LatLng(33.465143,-86.790402));
        options.add(new LatLng(33.465152,-86.790383));
        options.add(new LatLng(33.465405,-86.791424));
        options.add(new LatLng(33.465583,-86.791531));
        options.add(new LatLng(33.464955,-86.792956));
        options.add(new LatLng(33.464950,-86.793053));
        line = mMap.addPolyline(options);
    }

    public void goToCurrentLocation() {
        //TODO remember to undo FAB click in getActivity();
        if (mLastKnownLocation != null) {
            LatLng ll = new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ll, DEFAULT_ZOOM));
            mMap.addMarker(new MarkerOptions().position(ll)
                    .title("You"));
        }
        //TODO: else move map to DEFAULT_POSITION
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
    }

    @Override
    public void onPoiClick(PointOfInterest pointOfInterest) {
        Toast.makeText(mContext, pointOfInterest.name, Toast.LENGTH_SHORT).show();
    }
}

