package com.inf8405.projet_final.marathon;


// INF8405 - Laboratoire 2
//Auteurs : Najib Arbaoui (1608366) && Youssef Zemmahi (1665843) && Zolnouri Mahdi (1593999)

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
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
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
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
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


public class IDisplayCurrentMarathon extends Activity {
    final int RQS_GooglePlayServices = 1;
    private GoogleMap fMap;
    double fStartPointLatitude = 45.4877866;
    double fStartPointLongitude = -73.633085;
    double fEndPointLatitude = 45.5032186;
    double fEndPointLongitude = -73.6261043;
    private String fMarathonName;
    private TextView f1stNom = null;
    private TextView f1stSpeed = null;
    private TextView f2ndNom = null;
    private TextView f2ndSpeed = null;
    private TextView f3rdNom = null;
    private TextView f3rdSpeed = null;
    private TextView fMeNom = null;
    private TextView fMeSpeed = null;
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
    private MarkerOptions markerOptions;
    private GeocodingLocation fLocationAddress;
    private String distanceFromXML = "";
    private boolean drawPathallowed = false;
    private Map<String,Marathon> fActualMarathonMap;
    private Marathon fActualMarathon;
    Map<String,Participant> fWinnersParticipants;
    private ArrayList<Participant> fWinnersParticipantsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_current_marathon);
        Bundle marathonNameBundle = getIntent().getExtras();
        fMarathonName = marathonNameBundle.getString("marathonName");
        fWinnersParticipantsList = new ArrayList<>();
        /* Get actual marathon map */
        fActualMarathonMap = DBContent.getInstance().getListActualMarathon();
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
        f1stSpeed = (TextView) findViewById(R.id.TV_1stNTS_Speed_Value_DMA);
        f2ndNom = (TextView) findViewById(R.id.TV_2ndNTS_Nom_Value_DMA);
        f2ndSpeed = (TextView) findViewById(R.id.TV_2ndNTS_Speed_Value_DMA);
        f3rdNom = (TextView) findViewById(R.id.TV_3rdNTS_Nom_Value_DMA);
        f3rdSpeed = (TextView) findViewById(R.id.TV_3rdNTS_Speed_Value_DMA);
        fMeNom = (TextView) findViewById(R.id.TV_MeNTS_Nom_Value_DMA);
        fMeSpeed = (TextView) findViewById(R.id.TV_MeNTS_Speed_Value_DMA);
        fDistance = (TextView) findViewById(R.id.TV_Dis_Value_DMA);
        fTemp = (TextView) findViewById(R.id.TV_Temp_Value_DMA);
        fHumidity = (TextView) findViewById(R.id.TV_Hum_Value_DMA);

        /* Get the first 3 winners */
        fWinnersParticipants = DBContent.getInstance().getWinnersParticipant(fActualMarathon.getId());
        for(Map.Entry<String, Participant> entry : fWinnersParticipants.entrySet())
        {
            fWinnersParticipantsList.add(entry.getValue());
        }
        f1stNom.setText(fWinnersParticipantsList.get(0).getCourriel().substring(0, fWinnersParticipantsList.get(0).getCourriel().indexOf('@'))+", "+String.valueOf((int) fWinnersParticipantsList.get(0).getAverageSpeed())+" km/h");
        f1stSpeed.setText("");
        f2ndNom.setText(fWinnersParticipantsList.get(1).getCourriel().substring(0, fWinnersParticipantsList.get(1).getCourriel().indexOf('@'))+", "+String.valueOf((int) fWinnersParticipantsList.get(1).getAverageSpeed())+" km/h");
        f2ndSpeed.setText("");
        f3rdNom.setText(fWinnersParticipantsList.get(2).getCourriel().substring(0, fWinnersParticipantsList.get(2).getCourriel().indexOf('@'))+", "+String.valueOf((int) fWinnersParticipantsList.get(2).getAverageSpeed())+" km/h");
        f3rdSpeed.setText("");
        fMeNom.setText(DBContent.getInstance().getActualParticipant().getCourriel().substring(0, DBContent.getInstance().getActualParticipant().getCourriel().indexOf('@'))+", "+String.valueOf((int) DBContent.getInstance().getActualParticipant().getAverageSpeed())+" km/h");
        fMeSpeed.setText("");

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
        LatLng srcLatLng = new LatLng(fStartPointLatitude, fStartPointLongitude);
        LatLng destLatLng = new LatLng(fEndPointLatitude, fEndPointLongitude);

        fMap.addMarker(new MarkerOptions()
                .position(srcLatLng).title("Start point"));

        fMap.animateCamera(CameraUpdateFactory.newLatLng(srcLatLng));

        fMap.addMarker(new MarkerOptions()
                .position(destLatLng).title("Arrival point"));

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
//        urlStr.append(fStartPointLatitude);
//        urlStr.append(",");
//        urlStr.append(fStartPointLongitude);
//        urlStr.append("&destination=");//to
//        urlStr.append(fEndPointLatitude);
//        urlStr.append(",");
//        urlStr.append(fEndPointLongitude);
//        urlStr.append("&mode=walking&language=fr-FR");
//        Log.d("url","::"+urlStr.toString());
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
