package com.inf8405.projet_final.marathon;


// INF8405 - Laboratoire 2
//Auteurs : Najib Arbaoui (1608366) && Youssef Zemmahi (1665843) && Zolnouri Mahdi (1593999)

import android.Manifest;
import android.content.ContentResolver;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class IDisplayMarathon extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, AdapterView.OnItemSelectedListener {

    private GoogleApiClient fGoogleApiClient;
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private LocationRequest fLocationRequest;
    public static final String TAG = IDisplayMarathon.class.getSimpleName();
    private GoogleMap fMap;
    private String fMarathonName;
    private TextView f1stNom = null;
    private TextView fDistance = null;
    private TextView fTemp = null;
    private TextView fHumidity = null;
    private Button fBtnRevenirMM = null;
    private double fLatitudeFromAddress;
    private double fLongitudeFromAddress;
    private String fStartPoint = "2500, chemin de Polytechnique, Montreal, Canada";
    private String fEndPoint = "5700 Chemin Cote-Des-Neiges, Montreal, Canada";
    private String latLongFromAdd = "";
    private double fCurrentLatitude = 0.0;
    private double fCurrentLongitude = 0.0;
    private double fStartPointLatitude = 0.0;
    private double fStartPointLongitude = 0.0;
    private double fEndPointLatitude = 0.0;
    private double fEndPointLongitude = 0.0;
    private GeocodingLocation fLocationAddress;
    private ArrayList<LatLng> fPoints;
    private Route fRoute;

    /* Pop up */
    ContentResolver fResolver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_marathon);
        Bundle marathonNameBundle = getIntent().getExtras();
        fMarathonName = marathonNameBundle.getString("marathonName");

        // Create the LocationRequest object
        fLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.MAP_DM);
        mapFragment.getMapAsync(this);
        fGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();

        fResolver = this.getContentResolver();
        f1stNom = (TextView) findViewById(R.id.TV_1stNTS_Value_DM);
        fDistance = (TextView) findViewById(R.id.TV_Dis_Value_DM);
        fTemp = (TextView) findViewById(R.id.TV_Temp_Value_DM);
        fHumidity = (TextView) findViewById(R.id.TV_Hum_Value_DM);

        fDistance.setText("Pas encore calculé!");
        fHumidity.setText("Pas encore calculé!");
        fTemp.setText("Pas encore calculé!");
        fHumidity.setText("Pas encore calculé!");

        fBtnRevenirMM = (Button) findViewById(R.id.BTN_Return_DM);

        fBtnRevenirMM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                //startActivity(intent);
                finish();
            }
        });

        /* Display path */
         /* get longitude and latitude of start point address */
        fLocationAddress = new GeocodingLocation();
        fLocationAddress.getAddressFromLocation(fStartPoint, getApplicationContext(), new GeocoderHandler());
        /* Display start point flag */
//        fStartPointLatitude = fLatitudeFromAddress;
//        fStartPointLongitude = fLongitudeFromAddress;
//        LatLng latLngSP = new LatLng(fStartPointLatitude, fStartPointLongitude);
//        MarkerOptions optionsSP = new MarkerOptions().position(latLngSP).title("Start point");
//        fMap.addMarker(optionsSP);
//        float zoomLevelSP = 16; //This goes up to 21
//        fMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngSP, zoomLevelSP));

        /* get longitude and latitude of end point address */
        fLocationAddress.getAddressFromLocation(fEndPoint, getApplicationContext(), new GeocoderHandler());
        /* Display start point flag */
//        fEndPointLatitude = fLatitudeFromAddress;
//        fEndPointLongitude = fLongitudeFromAddress;
//        LatLng latLngEP = new LatLng(fEndPointLatitude, fEndPointLongitude);
//        MarkerOptions optionsEP = new MarkerOptions().position(latLngEP).title("End point");
//        fMap.addMarker(optionsEP);
//        float zoomLevelEP = 16; //This goes up to 21
//        fMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngEP, zoomLevelEP));

        fPoints = new ArrayList<>();
        fRoute = new Route();


    }

    @Override
    protected void onResume() {
        super.onResume();
        //setUpMapIfNeeded();
        fGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (fGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(fGoogleApiClient, (com.google.android.gms.location.LocationListener) this);
            fGoogleApiClient.disconnect();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        /* Do nothing at this moment */
        fMap = googleMap;
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.i(TAG, "Location services connected.");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(fGoogleApiClient);
        if (location == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(fGoogleApiClient, fLocationRequest, (com.google.android.gms.location.LocationListener) this);
        } else {
            handleNewLocation(location);
        }
        ;
    }

    private void handleNewLocation(Location location) {
        Log.d(TAG, location.toString());
        /* get longitude and latitude of current location */
        fCurrentLatitude = location.getLatitude();
        fCurrentLongitude = location.getLongitude();
        /* Display current location flag */
        LatLng latLngCL = new LatLng(fCurrentLatitude, fCurrentLongitude);
        MarkerOptions optionsCL = new MarkerOptions().position(latLngCL).title("Start point");
        fMap.addMarker(optionsCL);
        float zoomLevelCL = 16; //This goes up to 21
        fMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngCL, zoomLevelCL));

    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Location services suspended. Please reconnect.");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            Log.i(TAG, "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        handleNewLocation(location);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // TODO Auto-generated method stub
    }

    public String makeURL (double sourcelat, double sourcelog, double destlat, double destlog ){
        StringBuilder urlString = new StringBuilder();
        urlString.append("http://maps.googleapis.com/maps/api/directions/json");
        urlString.append("?origin=");// from
        urlString.append(Double.toString(sourcelat));
        urlString.append(",");
        urlString
                .append(Double.toString( sourcelog));
        urlString.append("&destination=");// to
        urlString
                .append(Double.toString( destlat));
        urlString.append(",");
        urlString.append(Double.toString( destlog));
        urlString.append("&sensor=false&mode=driving&alternatives=true");
        urlString.append("&key=YOUR_API_KEY");
        return urlString.toString();
    }

    private class GeocoderHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            String locationAddress;
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    locationAddress = bundle.getString("address");
                    break;
                default:
                    locationAddress = null;
            }
            latLongFromAdd = locationAddress;
            String[] str = latLongFromAdd.split(";");
            if(str.length > 0) {
                fLatitudeFromAddress = Double.valueOf(str[0]);
                fLongitudeFromAddress = Double.valueOf(str[1]);
                LatLng latLng = new LatLng(fLatitudeFromAddress, fLongitudeFromAddress);
                MarkerOptions options = new MarkerOptions().position(latLng).title("Point");
                fMap.addMarker(options);
                float zoomLevel = 16; //This goes up to 21
                fMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel));
                fPoints.add(latLng);
                //if(fPoints.size() > 1)
                //    fRoute.drawRoute(fMap, getApplicationContext(), fPoints, "en", false);

            }else{
                LatLng latLng = new LatLng(fCurrentLatitude, fCurrentLongitude);
                MarkerOptions options = new MarkerOptions().position(latLng).title("Actual location");
                fMap.addMarker(options);
                float zoomLevel = 16; //This goes up to 21
                fMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel));
            }

        }
    }


}
