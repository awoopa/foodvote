package com.foodvote.foodvote;

import android.app.SearchManager;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.foodvote.foodvote.R;
import com.foodvote.google.AlertDialogManager;
import com.foodvote.google.GPSTracker;
import com.foodvote.model.PlaceParser;
import com.foodvote.yelp.YelpAPI;
import com.google.android.gms.maps.model.LatLng;

public class YelpSearchActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yelp_search);
        handleIntent(getIntent());
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);

            GPSTracker gps = new GPSTracker(this);
            AlertDialogManager alert = new AlertDialogManager();
            LatLng location = new LatLng(49, -123);

            if (gps.canGetLocation()) {
                location = new LatLng(gps.getLatitude(), gps.getLongitude());
            } else {
                alert.showAlertDialog(this, "GPS Status",
                        "Couldn't get location information. Please enable GPS",
                        false);
            }

            YelpAPI yelp = new YelpAPI();
            String queryResults = yelp.searchForBusinessesByLocation(query, location);
            PlaceParser parser = new PlaceParser();
            parser.parse(queryResults);
        }


    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_yelp_search, menu);
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
