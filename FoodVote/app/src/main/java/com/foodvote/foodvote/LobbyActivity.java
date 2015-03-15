package com.foodvote.foodvote;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.foodvote.foodvote.R;
import com.foodvote.model.Room;
import com.foodvote.model.User;

import java.util.ArrayList;
import java.util.List;

import com.foodvote.socket.SocketIO;

public class LobbyActivity extends ActionBarActivity {

    List<User> userList;
    UserListAdapter ula;

    Button startButton;

    Intent intent;
    Boolean isCreator;

    SocketIO socket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);

        // create socket and listener
        socket = SocketIO.getInstance();


        userList = new ArrayList<User>();
        userList.add(new User("",""));
        userList.add(new User("",""));
        ula = new UserListAdapter(userList);

        intent = getIntent();
        isCreator = intent.getBooleanExtra("isCreator", false);

        startButton = (Button) findViewById(R.id.start_button);

        // create socket and listener
        socket = SocketIO.getInstance();


        // enable button only if creator
        if (isCreator) {
            startButton.setEnabled(true);
        } else {
            startButton.setEnabled(false);
            startButton.setText("Waiting for everyone...");
        }

        // all the sockets
        socket.onUserJoinedRoom(this, new SocketIO.OnUserJoinedRoomListener() {
            @Override
            public void onUserJoinedRoom(User new_user, List<User> users) {
                ula.replaceAll(users);
            }
        });

        socket.onVotingStart(this, new SocketIO.OnVotingStartListener() {
           @Override
            public void onVotingStart(Room room) {
               startVoteActivity();
           }
        });

        startButton = (Button) findViewById(R.id.start_button);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                creatorStart();
            }
        });

        //RecyclerView for list of users
        RecyclerView userRV = (RecyclerView) findViewById(R.id.user_list);
        userRV.addItemDecoration(new SimpleDividerItemDecoration(getApplicationContext()));         //divider
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        userRV.setLayoutManager(llm);

        //Testing values
        /*User u1 = new User("user 1", 1);
        User u2 = new User("user 2", 2);
        User u3 = new User("user 3", 3);
        List<User> userList = new ArrayList<User>();
        userList.add(u1);
        userList.add(u2);
        userList.add(u3);*/
/*
        UserListAdapter ula = new UserListAdapter(userList);*/
        userRV.setAdapter(ula);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_lobby, menu);
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

    private void startVoteActivity() {
        Intent intent = new Intent(this, VoteActivity.class);
        startActivity(intent);
    }

    private void creatorStart() {
        if (isCreator) {
            socket.creatorStart();
            startVoteActivity();
        }
    }
}
