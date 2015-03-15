package com.foodvote.foodvote;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.foodvote.foodvote.R;
import com.foodvote.model.Room;
import com.foodvote.socket.SocketIO;
import com.github.nkzawa.engineio.client.Socket;

public class KeycodeActivity extends ActionBarActivity {

    EditText userNameEditText;
    EditText roomNameEditText;

    Button joinRoomButton;

    String userName;
    String roomName;

    SocketIO socket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keycode);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        socket = SocketIO.getInstance();
        socket.connect();

        socket.onJoinedRoom(this, new SocketIO.OnJoinedRoomListener() {
            @Override
            public void onJoinedRoom(Room room) {
                startLobbyActivity();
            }
        });
        socket.onRoomAvailable(this, new SocketIO.OnRoomAvailableListener() {
            @Override
            public void onRoomAvailable(String roomName) {
                startCreateRoomActivity();
            }
        });

        userNameEditText = (EditText) findViewById(R.id.user_name_edittext);
        roomNameEditText = (EditText) findViewById(R.id.room_name_edittext);

        joinRoomButton = (Button) findViewById(R.id.join_room_button);
        joinRoomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userName = userNameEditText.getText().toString();
                roomName = roomNameEditText.getText().toString();

                joinRoom();
            }
        });
    }

    private void joinRoom() {
        if (userName == null || roomName == null)
            return;
        if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(roomName))
            return;
        socket.joinRoom(userName, roomName);

    }

    private void startCreateRoomActivity() {
        Intent intent = new Intent(this, CreateRoomActivity.class);
        startActivity(intent);
    }

    private void startLobbyActivity() {
        Intent intent = new Intent(this, LobbyActivity.class);
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_keycode, menu);
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
