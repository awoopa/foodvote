package com.foodvote.foodvote;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

<<<<<<< HEAD:FoodVote/app/src/main/java/com/foodvote/foodvote/MainActivity.java
import com.footvote.foodvote.R;
import com.footvote.foodvote.yelp.YelpAPI;
=======
import com.foodvote.model.Place;
import com.foodvote.model.Room;
import com.foodvote.model.User;
import com.foodvote.yelp.YelpAPI;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;
//import com.footvote.foodvote.R;
>>>>>>> 57ba3936f1ff1e7dfbb236649e34da964a254252:FoodVote/app/src/main/java/com/foodvote/foodvote/MainActivity.java


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //RecyclerView for list of rooms
        RecyclerView roomRV = (RecyclerView) findViewById(R.id.vote_list);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        roomRV.setLayoutManager(llm);

        //Testing value
        Room r1 = new Room("room1");
        Room r2 = new Room("room2", new ArrayList<Place>(), new ArrayList<User>(), true, new Place());
        List<Room> roomList = new ArrayList<Room>();
        roomList.add(r1);
        roomList.add(r2);

        RoomListAdapter rla = new RoomListAdapter(roomList);
        roomRV.setAdapter(rla);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    public void openMap(View v) {
        YelpAPI yelpAPI = new YelpAPI();

        String s = yelpAPI.searchForBusinessesByLocation("test", "Vancouver");
        System.out.println(s);

        s = yelpAPI.searchByBusinessId("coal-harbour-eye-centre-vancouver");
        System.out.println(s);

        startActivity(new Intent(this, MapActivity.class));
    }
}
