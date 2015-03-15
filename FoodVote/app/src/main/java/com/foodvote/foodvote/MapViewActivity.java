package com.foodvote.foodvote;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.foodvote.google.AlertDialogManager;
import com.foodvote.google.GPSTracker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapViewActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_view);

        // creating GPS Class object
        GPSTracker gps = new GPSTracker(this);

        AlertDialogManager alert = new AlertDialogManager();


        // default values to vancouver
        LatLng l = new LatLng(43, -129);

        // check if GPS location can get
        if (gps.canGetLocation()) {
            l = new LatLng(gps.getLatitude(), gps.getLongitude());
        } else {
            // Can't get user's current location
            alert.showAlertDialog(MapViewActivity.this, "GPS Status",
                    "Couldn't get location information. Please enable GPS",
                    false);
            // stop executing code by return
        }

        MarkerOptions a = new MarkerOptions().position(l);
        GoogleMap map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
        Marker m = map.addMarker(a);

        map.animateCamera(CameraUpdateFactory.newLatLngZoom(m.getPosition(), 14));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_map_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
