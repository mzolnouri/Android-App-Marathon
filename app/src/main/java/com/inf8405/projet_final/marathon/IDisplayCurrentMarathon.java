package com.inf8405.projet_final.marathon;


// INF8405 - Laboratoire 2
//Auteurs : Najib Arbaoui (1608366) && Youssef Zemmahi (1665843) && Zolnouri Mahdi (1593999)

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


public class IDisplayCurrentMarathon extends Activity implements SensorEventListener {
    final int RQS_GooglePlayServices = 1;
    private GoogleMap fMap;
    double fStartPointLatitude = 45.5024062;
    double fStartPointLongitude = -73.6282954;
    double fEndPointLatitude = 45.5006058;
    double fEndPointLongitude = -73.6262171;
    private String fMarathonName;
    private TextView f1stNom = null;
    private TextView f2ndNom = null;
    private TextView f3rdNom = null;
    private TextView fMeNom = null;
    private TextView fMeAcceleration = null;
    private TextView fDistance = null;
    private TextView fTemp = null;
    private TextView fHumidity = null;
    private Button fBtnRevenirMM = null;
    private double fLatitudeFromAddress;
    private double fLongitudeFromAddress;
    private String fStartPoint = "2500, chemin de Polytechnique, Montreal, Canada";
    private String fBM = "5700 Chemin Cote-Des-Neiges, Montreal, Canada";
    private String fEndPoint = "13100 Boulevard Henri Fabre, Mirabel, Canada";
    private String latLongFromAdd = "";
    private String fMyname;
    private MarkerOptions markerOptions;
    private GeocodingLocation fLocationAddress;
    private String distanceFromXML = "";
    private boolean drawPathallowed = false;
    private Map<String,Marathon> fActualMarathonMap;
    private Marathon fActualMarathon;
    private Map<String,Participant> fWinnersParticipants;
    private ArrayList<Participant> fWinnersParticipantsList;
    private SensorManager sensorManager;
    private double ax,ay,az;   // these are the acceleration in x,y and z axis
    private double initialSpeed =0.0;
    private double speed =0.0;
    private Date timeStart;
    private double currentA = 0.0;
    private double lastA = 0.0;
    private boolean toggle = true;
    private int fCounter = 0;
    private LatLng fP1LatLng;
    private LatLng fP2LatLng;
    private LatLng fP3LatLng;
    private LatLng fSrcLatLng;
    private LatLng fDesLatLng;
    private  Marker fMarkerP1;
    private  Marker fMarkerP2;
    private  Marker fMarkerP3;
    // temperature et humidite
    private SensorManager mSensorManager;
    private Sensor sTemp, sHumidity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_current_marathon);
        Bundle marathonNameBundle = getIntent().getExtras();
        fMarathonName = marathonNameBundle.getString("marathonName");
        fWinnersParticipantsList = new ArrayList<>();
        /* Get actual marathon map */
        fActualMarathonMap = DBContent.getInstance().getListActualMarathon();
        // // Get an instance of the sensor service, and use that to get an instance of
        // a particular sensor.
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sTemp = mSensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        sHumidity = mSensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);

        for(Map.Entry<String, Marathon> entry : fActualMarathonMap.entrySet())
        {
            if(entry.getValue().getNom().equals(fMarathonName)){
                fActualMarathon = entry.getValue();
                fStartPointLatitude = entry.getValue().getPositionDepartLatitude();
                fStartPointLongitude = entry.getValue().getPositionDepartLongitude();
                fEndPointLatitude = entry.getValue().getPositionArriveeLatitude();
                fEndPointLongitude = entry.getValue().getPositionArriveeLongitude();
            }
        }


        FragmentManager myFragmentManager = getFragmentManager();
        MapFragment myMapFragment = (MapFragment) myFragmentManager.findFragmentById(R.id.MAP_DMA);
        fMap = myMapFragment.getMap();

        f1stNom = (TextView) findViewById(R.id.TV_1stNTS_Nom_Value_DMA);
        f2ndNom = (TextView) findViewById(R.id.TV_2ndNTS_Nom_Value_DMA);
        f3rdNom = (TextView) findViewById(R.id.TV_3rdNTS_Nom_Value_DMA);
        fMeNom = (TextView) findViewById(R.id.TV_MeNTS_Nom_Value_DMA);
        fDistance = (TextView) findViewById(R.id.TV_Dis_Value_DMA);
        fTemp = (TextView) findViewById(R.id.TV_Temp_Value_DMA);
        fHumidity = (TextView) findViewById(R.id.TV_Hum_Value_DMA);
        fMeAcceleration = (TextView) findViewById(R.id.TV_ACCEL_Value_DMA);

        /* Get the first 3 winners */
        fWinnersParticipants = DBContent.getInstance().getWinnersParticipant(fActualMarathon.getId());
        for(Map.Entry<String, Participant> entry : fWinnersParticipants.entrySet())
        {
            fWinnersParticipantsList.add(entry.getValue());
        }
        f1stNom.setText(fWinnersParticipantsList.get(0).getCourriel().substring(0, fWinnersParticipantsList.get(0).getCourriel().indexOf('@'))+", "+String.valueOf((int) fWinnersParticipantsList.get(0).getAverageSpeed())+" Km/h");
        f2ndNom.setText(fWinnersParticipantsList.get(1).getCourriel().substring(0, fWinnersParticipantsList.get(1).getCourriel().indexOf('@'))+", "+String.valueOf((int) fWinnersParticipantsList.get(1).getAverageSpeed())+" Km/h");
        f3rdNom.setText(fWinnersParticipantsList.get(2).getCourriel().substring(0, fWinnersParticipantsList.get(2).getCourriel().indexOf('@'))+", "+String.valueOf((int) fWinnersParticipantsList.get(2).getAverageSpeed())+" Km/h");
        fMyname = DBContent.getInstance().getActualParticipant().getCourriel().substring(0, DBContent.getInstance().getActualParticipant().getCourriel().indexOf('@'));
        fMeNom.setText(fMyname +", "+String.valueOf((int) DBContent.getInstance().getActualParticipant().getAverageSpeed())+" km/h");

        fDistance.setText("Pas encore calculé!");
        fHumidity.setText(fActualMarathon.getHumidity() + " %");
        fTemp.setText(fActualMarathon.getTemperature() + " C");

        fBtnRevenirMM = (Button) findViewById(R.id.BTN_Return_DMA);

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
        //fLocationAddress.getAddressFromLocation(fStartPoint, getApplicationContext(), new GeocoderHandler());

        /* get longitude and latitude of end point address */
        //fLocationAddress.getAddressFromLocation(fEndPoint, getApplicationContext(), new GeocoderHandler());

        /* Create start and end point of marathon path */
        fSrcLatLng = new LatLng(fStartPointLatitude, fStartPointLongitude);
        fDesLatLng = new LatLng(fEndPointLatitude, fEndPointLongitude);
        for(int k = 0 ; k < fWinnersParticipantsList.size(); k++){
            fWinnersParticipantsList.get(k).setPosition(DBContent.getInstance().GetUserPosition(fWinnersParticipantsList.get(k).getId(), fActualMarathon.getId()));
        }


        fP1LatLng = new LatLng(fWinnersParticipantsList.get(0).getPosition().getLatitude(), fWinnersParticipantsList.get(0).getPosition().getLongitude());
        fMarkerP1 = fMap.addMarker(new MarkerOptions().position(new LatLng(fP1LatLng.latitude, fP1LatLng.longitude)).title("First").snippet("participant"));
        fP2LatLng = new LatLng(fWinnersParticipantsList.get(1).getPosition().getLatitude(), fWinnersParticipantsList.get(1).getPosition().getLongitude());
        fMarkerP2 = fMap.addMarker(new MarkerOptions().position(new LatLng(fP2LatLng.latitude, fP2LatLng.longitude)).title("Second").snippet("participant"));
        fP3LatLng = new LatLng(fWinnersParticipantsList.get(2).getPosition().getLatitude(), fWinnersParticipantsList.get(2).getPosition().getLongitude());
        fMarkerP3 = fMap.addMarker(new MarkerOptions().position(new LatLng(fP3LatLng.latitude, fP3LatLng.longitude)).title("Third").snippet("participant"));

        fMap.addMarker(new MarkerOptions().position(fSrcLatLng).title("Start point"));

        fMap.animateCamera(CameraUpdateFactory.newLatLng(fSrcLatLng));

        fMap.addMarker(new MarkerOptions().position(fDesLatLng).title("Arrival point"));

        //fMap.addMarker(new MarkerOptions().position(fP1LatLng).title("First participant"));
        //fMap.addMarker(new MarkerOptions().position(fP2LatLng).title("Second participant"));
        //fMap.addMarker(new MarkerOptions().position(fP3LatLng).title("Third participant"));



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
        fMap.animateCamera(CameraUpdateFactory.newLatLngZoom(fSrcLatLng, 12));

        markerOptions = new MarkerOptions();

        /* Get speed from accelerator */
        sensorManager=(SensorManager) getSystemService(SENSOR_SERVICE);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        timeStart = new Date(System.currentTimeMillis());
        initialSpeed =0.0;


        // Polyline line = fMap.addPolyline(new PolylineOptions().add(srcLatLng, destLatLng).width(5).color(Color.RED));
        connectAsyncTask _connectAsyncTask = new connectAsyncTask();
        _connectAsyncTask.execute();
        //while(true)



    }


    private void runThread() {

        new Thread() {
            public void run() {
                while (fCounter++ < 1000) {
                    try {
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                fMarkerP1.remove();
                                fMarkerP2.remove();
                                fMarkerP3.remove();
                                fP1LatLng = new LatLng(fWinnersParticipantsList.get(0).getPosition().getLatitude(), fWinnersParticipantsList.get(0).getPosition().getLongitude());
                                fMarkerP1 = fMap.addMarker(new MarkerOptions().position(new LatLng(fP1LatLng.latitude, fP1LatLng.longitude)).title("First").snippet("participant"));
                                fP2LatLng = new LatLng(fWinnersParticipantsList.get(1).getPosition().getLatitude(), fWinnersParticipantsList.get(1).getPosition().getLongitude());
                                fMarkerP2 = fMap.addMarker(new MarkerOptions().position(new LatLng(fP2LatLng.latitude, fP2LatLng.longitude)).title("Second").snippet("participant"));
                                fP3LatLng = new LatLng(fWinnersParticipantsList.get(2).getPosition().getLatitude(), fWinnersParticipantsList.get(2).getPosition().getLongitude());
                                fMarkerP3 = fMap.addMarker(new MarkerOptions().position(new LatLng(fP3LatLng.latitude, fP3LatLng.longitude)).title("Third").snippet("participant"));

                                // TODO
                                // position.setTemp()
                                // position.setHum
                                // dbcontent.UpdateRemotePosition();
                                //


                            }
                        });
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
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
                AlertDialog.Builder LicenseDialog = new AlertDialog.Builder(IDisplayCurrentMarathon.this);
                LicenseDialog.setTitle("Legal Notices");
                LicenseDialog.setMessage(LicenseInfo);
                LicenseDialog.show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());

        if (resultCode == ConnectionResult.SUCCESS){
//            Toast.makeText(getApplicationContext(),
//                    "isGooglePlayServicesAvailable SUCCESS",
//                    Toast.LENGTH_LONG).show();
        }else{
            GooglePlayServicesUtil.getErrorDialog(resultCode, this, RQS_GooglePlayServices);
        }

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        double vitesse_x, vitesse_y, vitesse_z;
        Date timeNow = new Date(System.currentTimeMillis());

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            double time = (timeNow.getTime() - timeStart.getTime()) / 1000.0;
            ax = event.values[0];
            ay = event.values[1];
            az = event.values[2];
            fMeNom.setText(fMyname +", "+" x= " + ax + " y= " + ay + " z= " + az);
//            if(Math.abs(ax)>= 9){ // enlever la garvite = 9.81
//                ax = (Math.abs(ax)- 9.81);
//            }
//            if(Math.abs(ay)>= 9){ // enlever la garvite = 9.81
//                ay = (Math.abs(ay) - 9.81);
//            }
//            if(Math.abs(az)>= 9){ // enlever la garvite = 9.81
//                az = (Math.abs(az) - 9.81);
//            }

            double a = Math.sqrt(Math.pow(ax, 2) + Math.pow(ay, 2)
                    + Math.pow(az, 2));
            if (a > 9) { // enlever la garvite = 9.81
                a = a - 9;
            } else if (a > 0) {
                a = 0;
            }
//            a = a - 9.81;
            if (toggle) {
                currentA = a;
                toggle = !toggle;
            } else {
                lastA = a;
                toggle = !toggle;
            }

            double acceleration = 0.0;
            if (currentA > lastA) {
                acceleration = a;

            } else {
                acceleration = -a;
            }
            speed = initialSpeed + acceleration * time; // v= v0 + a*t (km par seconde)

            if (speed < 0) {
                speed = 0;
                initialSpeed = 0;
            }
            fMeNom.setText(fMyname + ", " +Double.toString(Math.floor(speed*3.6))+"Km/h"); //
            fMeAcceleration.setText(Double.toString(Math.floor(a))+" m/s2"); //
            initialSpeed = speed;
            if(ax == 0.0 && ay == 0.0 && az == 0.0)
                initialSpeed = 0.0;

            timeStart.setTime(System.currentTimeMillis());
        } else if (event.sensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE) {
            fTemp.setText(Double.toString(event.values[0]));
        }else if (event.sensor.getType() == Sensor.TYPE_RELATIVE_HUMIDITY) {
            fHumidity.setText(Double.toString(event.values[0]));
        }

        runThread();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private class connectAsyncTask extends AsyncTask<Void, Void, Void>{

        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(IDisplayCurrentMarathon.this);
            progressDialog.setMessage("Fetching route, Please wait...");
            progressDialog.setIndeterminate(true);
            progressDialog.show();
        }
        @Override
        protected Void doInBackground(Void... params) {
            fetchData();
            //getDuration();
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if(responseGetPath !=null){
                NodeList _nodelist = responseGetPath.getElementsByTagName("status");
                Node node1 = _nodelist.item(0);
                String _status1 = node1.getChildNodes().item(0).getNodeValue();
                if(_status1.equalsIgnoreCase("OK")){
                    NodeList nodeListLeg = responseGetPath.getElementsByTagName("leg");
                    Node _nodeLeg = nodeListLeg.item(0);
                    Element elementLeg = (Element) _nodeLeg;
                    int index = 0;
                    String dis = "";
                    String strDis[];
                    for(int i = 0; i < elementLeg.getChildNodes().getLength(); i++){
                        Node child = elementLeg.getChildNodes().item(i);
                        if(i%2!=0) {
                            if (child.getNodeName().equals("distance")) {
                                index = i;
                                dis = child.getTextContent();
                                strDis = dis.split("\n");
                                for(int j = 0; j < strDis.length; j++){
                                    if(strDis[j].contains("km")) {
                                        distanceFromXML = strDis[j];
                                        fDistance.setText(distanceFromXML);
                                    }
                                }
                            }
                        }
                    }

                    NodeList _nodelist_path = responseGetPath.getElementsByTagName("overview_polyline");
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
    Document responseGetDistance = null;
    private void getDuration(){

        StringBuilder urlStr = new StringBuilder();
        urlStr.append("https://maps.googleapis.com/maps/api/distancematrix/json?origins=Montreal&destinations=Mirabel&mode=walking&language=fr-FR");
        HttpURLConnection urlConnectionDis= null;
        URL urlDis = null;
        try
        {
            urlDis = new URL(urlStr.toString());
            urlConnectionDis=(HttpURLConnection)urlDis.openConnection();
            urlConnectionDis.setRequestMethod("GET");
            urlConnectionDis.setDoOutput(true);
            urlConnectionDis.setDoInput(true);
            urlConnectionDis.connect();
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            responseGetDistance = (Document) db.parse(urlConnectionDis.getInputStream());//Util.XMLfromString(response);
        }catch (MalformedURLException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }catch (ParserConfigurationException e){
            e.printStackTrace();
        }
        catch (SAXException e) {
            e.printStackTrace();
        }
    }

    Document responseGetPath = null;
    private void fetchData()
    {
        StringBuilder urlString = new StringBuilder();
        urlString.append("http://maps.googleapis.com/maps/api/directions/xml?origin=");
        urlString.append(fStartPointLatitude);
        urlString.append(",");
        urlString.append(fStartPointLongitude);
        urlString.append("&destination=");//to
        urlString.append(fEndPointLatitude);
        urlString.append(",");
        urlString.append(fEndPointLongitude);
        urlString.append("&sensor=true&mode=walking");
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
            responseGetPath = (Document) db.parse(urlConnection.getInputStream());//Util.XMLfromString(response);
            urlConnection.disconnect();
        }catch (MalformedURLException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }catch (ParserConfigurationException e){
            e.printStackTrace();
        }
        catch (SAXException e) {
            e.printStackTrace();
        }
    }


    private void showAlert(String message){
        AlertDialog.Builder alert = new AlertDialog.Builder(IDisplayCurrentMarathon.this);
        alert.setTitle("Error");
        alert.setCancelable(false);
        alert.setMessage(message);
        alert.setPositiveButton("Ok",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

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
                if(drawPathallowed) {
                    fStartPointLatitude = fLatitudeFromAddress;
                    fStartPointLongitude = fLongitudeFromAddress;
                }
                else{
                    fStartPointLatitude = fLatitudeFromAddress;
                    fStartPointLongitude = fLongitudeFromAddress;
                    drawPathallowed = true;
                }
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
