package com.foodvote.foodvote;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.foodvote.foodvote.R;
import com.foodvote.model.Place;
import com.foodvote.model.PlaceManager;
import com.foodvote.model.Round;
import com.foodvote.socket.SocketIO;

import java.util.List;

public class WinnerActivity extends ActionBarActivity {

    Intent intent;

    String result;
    List<Round> rounds;

    PlaceManager pm;

    SocketIO socket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_winner);

        socket = SocketIO.getInstance();
        socket.destroy();


        pm = PlaceManager.getInstance();
        intent = getIntent();
        result = intent.getStringExtra("result");
        rounds = intent.getParcelableArrayListExtra("rounds");

        final Place winner = pm.findPlaceById(result);

        TextView tv = (TextView) findViewById(R.id.header);
        tv.setText(winner.getName());

        Button fab = (Button) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tel = "tel:" + winner.getPhone();
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse(tel));
                startActivity(callIntent);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_winner, menu);
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
