package com.foodvote.foodvote;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;

public class RadiusActivity extends ActionBarActivity {

    NumberPicker radiusPicker;
    Button submitRadiusButton;
    int radius;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radius);

        Intent in = getIntent();
        System.out.println(in.getExtras().getDouble("lat"));
        System.out.println(in.getExtras().getDouble("lon"));
        System.out.println(in.getExtras().getString("name"));

        radiusPicker = (NumberPicker) findViewById(R.id.numberPicker);
        submitRadiusButton = (Button) findViewById(R.id.button6);

        submitRadiusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               radius = radiusPicker.getValue();
            }
        });
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
}
