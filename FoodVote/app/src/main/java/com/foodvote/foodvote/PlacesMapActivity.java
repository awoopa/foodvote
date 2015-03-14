package com.foodvote.foodvote;

import android.content.Intent;
import android.os.Bundle;

import com.footvote.foodvote.R;

/**
 * Created by NWHacks on 14/03/2015.
 */
public class PlacesMapActivity extends MapActivity {
    double latitude;
    double longitude;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_places);

        Intent i = getIntent();

        // Users current geo location
        String user_latitude = i.getStringExtra("user_latitude");
        String user_longitude = i.getStringExtra("user_longitude");
    }
}
