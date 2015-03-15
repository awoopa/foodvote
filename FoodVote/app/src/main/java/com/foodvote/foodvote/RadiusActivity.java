package com.foodvote.foodvote;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;


import com.foodvote.google.AlertDialogManager;
import com.foodvote.google.GPSTracker;
import com.foodvote.model.PlaceManager;
import com.foodvote.model.PlaceParser;
import com.foodvote.yelp.YelpAPI;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Date;

import com.foodvote.model.Room;
import com.foodvote.socket.SocketIO;

public class RadiusActivity extends ActionBarActivity {

    NumberPicker radiusPicker;
    Button submitRadiusButton;
    int radius;

    SocketIO socket;

    PlaceManager pm;

    ArrayList<String> idArray;

    String name;
    Double lat;
    Double lon;

    String yelpResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radius);

        Intent in = getIntent();
        lat = in.getDoubleExtra("lat", 0.0);
        lon = in.getDoubleExtra("lon", 0.0);

        name = in.getStringExtra("name");

        radiusPicker = (NumberPicker) findViewById(R.id.numberPicker);
        submitRadiusButton = (Button) findViewById(R.id.button6);

        submitRadiusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               radius = radiusPicker.getValue();
            }
        });

        socket = SocketIO.getInstance();

        socket.onRoomCreated(this, new SocketIO.OnRoomCreatedListener() {
            public void onRoomCreated(Room room) {
                joinRoom();
            }
        });

        submitRadiusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDoneButton();
                socket.creatorSetup(name, radius, lat, lon, new Date(), idArray, yelpResults);
            }
        });
    }

    private void joinRoom() {
        Intent intent = new Intent(this, LobbyActivity.class);
        intent.putExtra("isCreator", true);
        startActivity(intent);
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

    public void onDoneButton() {
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
        yelpResults = queryResults;
        this.pm = PlaceManager.getInstance();
        idArray = new ArrayList<String>();
        for (int i=0; i<pm.getSize(); i++) {
            idArray.add(pm.get(i).getId());
        }

    }
}
