package com.foodvote.foodvote;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.foodvote.google.AlertDialogManager;
import com.foodvote.google.GPSTracker;
import com.foodvote.model.PlaceManager;
import com.foodvote.model.PlaceParser;
import com.foodvote.yelp.YelpAPI;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class RadiusActivity extends ActionBarActivity {

    PlaceManager pm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radius);

        Intent in = getIntent();
        System.out.println(in.getExtras().getDouble("lat"));
        System.out.println(in.getExtras().getDouble("lon"));
        System.out.println(in.getExtras().getString("name"));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_radius, menu);
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

    public void onDoneButton(MenuItem menu) {
        String name = getIntent().getStringExtra("name");
        double lat = (double) getIntent().getExtras().get("lat");
        double lon = (double) getIntent().getExtras().get("lon");

        GPSTracker gps = new GPSTracker(this);
        AlertDialogManager alert = new AlertDialogManager();
        LatLng location = new LatLng(lat, lon);

        YelpAPI yelp = new YelpAPI();
        String queryResults = yelp.searchForBusinessesByLocation("", location);
        System.out.println(queryResults);
        PlaceParser parser = new PlaceParser();
        parser.parse(queryResults);
        this.pm = PlaceManager.getInstance();
        ArrayList<String> idArray = new ArrayList<String>();
        for (int i=0; i<pm.getSize(); i++) {
            idArray.add(pm.get(i).getId());
        }

    }
}
