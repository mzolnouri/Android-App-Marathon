package com.inf8405.projet_final.marathon;


// INF8405 - Laboratoire 2
//Auteurs : Najib Arbaoui (1608366) && Youssef Zemmahi (1665843) && Zolnouri Mahdi (1593999)

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import android.app.FragmentManager;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;


public class IDisplayHistoricMarathon extends Activity {
    final int RQS_GooglePlayServices = 1;
    private GoogleMap fMap;
    double fStartPointLatitude = 45.5024062;
    double fStartPointLongitude = -73.6282954;
    double fEndPointLatitude = 45.5006058;
    double fEndPointLongitude = -73.6262171;
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
    MarkerOptions markerOptions;
    private GeocodingLocation fLocationAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_historic_marathon);
        Bundle marathonNameBundle = getIntent().getExtras();
        fMarathonName = marathonNameBundle.getString("marathonName");

        FragmentManager myFragmentManager = getFragmentManager();
        MapFragment myMapFragment = (MapFragment) myFragmentManager.findFragmentById(R.id.MAP_DM);
        fMap = myMapFragment.getMap();

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



        LatLng srcLatLng = new LatLng(fStartPointLatitude, fStartPointLongitude);
        LatLng destLatLng = new LatLng(fEndPointLatitude, fEndPointLongitude);

        fMap.addMarker(new MarkerOptions()
                .position(srcLatLng).title("Source place"));

        fMap.animateCamera(CameraUpdateFactory.newLatLng(srcLatLng));

        fMap.addMarker(new MarkerOptions()
                .position(destLatLng).title("Destination place"));

        // Enabling MyLocation in Google Map
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fMap.setMyLocationEnabled(true);
        fMap.getUiSettings().setZoomControlsEnabled(true);
        fMap.getUiSettings().setCompassEnabled(true);
        fMap.getUiSettings().setMyLocationButtonEnabled(true);
        fMap.getUiSettings().setAllGesturesEnabled(true);
        fMap.setTrafficEnabled(true);
        fMap.animateCamera(CameraUpdateFactory.newLatLngZoom(srcLatLng, 12));
        markerOptions = new MarkerOptions();


        // Polyline line = fMap.addPolyline(new PolylineOptions().add(srcLatLng, destLatLng).width(5).color(Color.RED));

        connectAsyncTask _connectAsyncTask = new connectAsyncTask();
        _connectAsyncTask.execute();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_menu_display_route, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_legalnotices:
                String LicenseInfo = GooglePlayServicesUtil.getOpenSourceSoftwareLicenseInfo(
                        getApplicationContext());
                AlertDialog.Builder LicenseDialog = new AlertDialog.Builder(IDisplayHistoricMarathon.this);
                LicenseDialog.setTitle("Legal Notices");
                LicenseDialog.setMessage(LicenseInfo);
                LicenseDialog.show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());

        if (resultCode == ConnectionResult.SUCCESS){
            Toast.makeText(getApplicationContext(),
                    "isGooglePlayServicesAvailable SUCCESS",
                    Toast.LENGTH_LONG).show();
        }else{
            GooglePlayServicesUtil.getErrorDialog(resultCode, this, RQS_GooglePlayServices);
        }

    }

    private class connectAsyncTask extends AsyncTask<Void, Void, Void>{

        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            progressDialog = new ProgressDialog(IDisplayHistoricMarathon.this);
            progressDialog.setMessage("Fetching route, Please wait...");
            progressDialog.setIndeterminate(true);
            progressDialog.show();
        }
        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            fetchData();
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            if(doc!=null){
                NodeList _nodelist = doc.getElementsByTagName("status");
                Node node1 = _nodelist.item(0);
                String _status1 = node1.getChildNodes().item(0).getNodeValue();
                if(_status1.equalsIgnoreCase("OK")){
                    NodeList _nodelist_path = doc.getElementsByTagName("overview_polyline");
                    Node node_path = _nodelist_path.item(0);
                    Element _status_path = (Element)node_path;
                    NodeList _nodelist_destination_path = _status_path.getElementsByTagName("points");
                    Node _nodelist_dest = _nodelist_destination_path.item(0);
                    String _path = _nodelist_dest.getChildNodes().item(0).getNodeValue();
                    List<LatLng> directionPoint = decodePoly(_path);

                    PolylineOptions rectLine = new PolylineOptions().width(10).color(Color.RED);
                    for (int i = 0; i < directionPoint.size(); i++) {
                        rectLine.add(directionPoint.get(i));
                    }
                    // Adding route on the map
                    fMap.addPolyline(rectLine);
                    markerOptions.position(new LatLng(fEndPointLatitude, fEndPointLongitude));
                    markerOptions.draggable(true);
                    fMap.addMarker(markerOptions);
                }else{
                    showAlert("Unable to find the route");
                }


            }else{
                showAlert("Unable to find the route");
            }

            progressDialog.dismiss();

        }

    }

    Document doc = null;
    private void fetchData()
    {
        StringBuilder urlString = new StringBuilder();
        urlString.append("http://maps.google.com/maps/api/directions/xml?origin=");
        urlString.append(fStartPointLatitude);
        urlString.append(",");
        urlString.append(fStartPointLongitude);
        urlString.append("&destination=");//to
        urlString.append(fEndPointLatitude);
        urlString.append(",");
        urlString.append(fEndPointLongitude);
        urlString.append("&sensor=true&mode=driving");
        Log.d("url","::"+urlString.toString());
        HttpURLConnection urlConnection= null;
        URL url = null;
        try
        {
            url = new URL(urlString.toString());
            urlConnection=(HttpURLConnection)url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.connect();
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            doc = (Document) db.parse(urlConnection.getInputStream());//Util.XMLfromString(response);
        }catch (MalformedURLException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }catch (ParserConfigurationException e){
            e.printStackTrace();
        }
        catch (SAXException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    private void showAlert(String message){
        AlertDialog.Builder alert = new AlertDialog.Builder(IDisplayHistoricMarathon.this);
        alert.setTitle("Error");
        alert.setCancelable(false);
        alert.setMessage(message);
        alert.setPositiveButton("Ok",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub

            }
        });
        alert.show();
    }

    private ArrayList<LatLng> decodePoly(String encoded) {
        ArrayList<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;
        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;
            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng position = new LatLng((double) lat / 1E5, (double) lng / 1E5);
            poly.add(position);
        }
        return poly;
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
                //fPoints.add(latLng);
                //if(fPoints.size() > 1)
                //    fRoute.drawRoute(fMap, getApplicationContext(), fPoints, "en", false);

            }else{
                LatLng latLng = new LatLng(fStartPointLatitude, fStartPointLongitude);
                MarkerOptions options = new MarkerOptions().position(latLng).title("Actual location");
                fMap.addMarker(options);
                float zoomLevel = 16; //This goes up to 21
                fMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel));
            }

        }
    }

}
