package com.foodvote.foodvote;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

import com.foodvote.google.AlertDialogManager;
import com.foodvote.google.ConnectionDetector;
import com.foodvote.google.GPSTracker;
import com.foodvote.google.GooglePlaces;
import com.foodvote.google.PlaceDetails;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class SinglePlaceActivity extends ActionBarActivity {
    // flag for Internet connection status
    private Boolean isInternetPresent = false;

    // Connection detector class
    private ConnectionDetector cd;

    // Alert Dialog Manager
    private AlertDialogManager alert = new AlertDialogManager();

    // Google Places
    private GooglePlaces googlePlaces;

    // Place Details
    private PlaceDetails placeDetails;

    // Progress dialog
    private ProgressDialog pDialog;

    // KEY Strings
    public static String KEY_REFERENCE = "reference"; // id of the place

    private LatLng latlng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_view);

        // creating GPS Class object
        GPSTracker gps = new GPSTracker(this);

        AlertDialogManager alert = new AlertDialogManager();


        // default values to vancouver
        latlng = new LatLng(43, -129);

        // check if GPS location can get
        if (gps.canGetLocation()) {
            latlng = new LatLng(gps.getLatitude(), gps.getLongitude());
        } else {
            // Can't get user's current location
            alert.showAlertDialog(SinglePlaceActivity.this, "GPS Status",
                    "Couldn't get location information. Please enable GPS",
                    false);
        }

        Intent i = getIntent();

        // Place referece id
        String reference = i.getStringExtra(KEY_REFERENCE);

        // Calling a Async Background thread
        new LoadSinglePlaceDetails().execute(reference);
    }


    /**
     * Background Async Task to Load Google places
     * */
    class LoadSinglePlaceDetails extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(SinglePlaceActivity.this);
            pDialog.setMessage("Loading map ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * getting Profile JSON
         * */
        protected String doInBackground(String... args) {
            String reference = args[0];

            // creating Places class object
            googlePlaces = new GooglePlaces();

            // Check if used is connected to Internet
            try {
                placeDetails = googlePlaces.getPlaceDetails(reference);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    /**
                     * Updating parsed Places into LISTVIEW
                     * */
                    if (placeDetails != null) {
                        String status = placeDetails.status;

                        // check place deatils status
                        // Check for all possible status
                        if (status.equals("OK")) {
                            if (placeDetails.result != null) {
                                LatLng ltln = new LatLng(placeDetails.result.geometry.location.lat, placeDetails.result.geometry.location.lng);
                                GoogleMap map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
                                View mapView = getSupportFragmentManager().findFragmentById(R.id.map).getView();

                                MarkerOptions a = new MarkerOptions().position(latlng);
                                MarkerOptions b = new MarkerOptions().position(ltln);
                                Marker m = map.addMarker(a);
                                Marker m2 = map.addMarker(b);

                                LatLngBounds.Builder bld = new LatLngBounds.Builder();
                                bld.include(ltln);
                                bld.include(latlng);
                                LatLngBounds bounds = bld.build();
                                map.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 70));

                            }
                        }
                    }
                }
            });

        }

    }

}
