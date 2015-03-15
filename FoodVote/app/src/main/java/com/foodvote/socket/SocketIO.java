package com.foodvote.socket;



import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;

import com.foodvote.model.Room;
import com.foodvote.model.Round;
import com.foodvote.model.User;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;
import com.github.nkzawa.socketio.client.IO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by kinwa91 on 15-03-14.
 */
public class SocketIO {

    public static final String SERVER = "http://foodvote.herokuapp.com";
    private Socket mSocket;
    private Map<String, Emitter.Listener> listeners;

    private static SocketIO instance;

    public static SocketIO getInstance() {
        if (instance == null) {
            instance = new SocketIO();
        }
        return instance;
    }

    private SocketIO() {
        listeners = new HashMap<>();
    }

    public void connect() {
        try {
            mSocket = IO.socket(SERVER);
            mSocket.open();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public void joinRoom(String userName, String roomName) {
        if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(roomName))
            return;
        Log.i("SocketIO", "Trying to emit: 'join room'");
        JSONObject joinRoomObject = new JSONObject();

        try {
            joinRoomObject.put("room_name", roomName);
            joinRoomObject.put("user_name", userName);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        mSocket.emit("join room", joinRoomObject);
    }

    public void creatorSetup(String roomName, int radius, double centerLat, double centerLon,
                             Date time, List<String> yelpIDs) {
        if (TextUtils.isEmpty(roomName))
            return;
        Log.i("SocketIO", "Trying to emit: 'creator setup'");
        JSONObject creatorSetupObject = new JSONObject();

        try {
            creatorSetupObject.put("room_name", roomName);
            creatorSetupObject.put("radius", radius);
            JSONObject latlonObject = new JSONObject();
            latlonObject.put("lat", centerLat);
            latlonObject.put("lon", centerLon);
            creatorSetupObject.put("center", latlonObject);
            creatorSetupObject.put("time", time.getTime());
            if (!yelpIDs.isEmpty())
                creatorSetupObject.put("options", new JSONArray(yelpIDs));

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        mSocket.emit("creator setup", creatorSetupObject);
    }

    public void addLocations(List<String> yelpIDs) {
        if (yelpIDs.isEmpty())
            return;

        Log.i("SocketIO", "Trying to emit: 'add locations'");
        JSONObject addLocationsObject = new JSONObject();
        try {
            addLocationsObject.put("locations", yelpIDs);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        mSocket.emit("add locations", addLocationsObject);
    }

    public void creatorStart() {
        Log.i("SocketIO", "Trying to emit: 'creator start'");
        mSocket.emit("creator start", new JSONObject());
    }

    public void registerVote(int roundNum, int score) {
        Log.i("SocketIO", "Trying to emit: 'register vote'");
        JSONObject registerVoteObject = new JSONObject();
        try {
            registerVoteObject.put("roundNum", roundNum);
            registerVoteObject.put("score", score);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        mSocket.emit("register vote", registerVoteObject);
    }

    public void onRoomAvailable(final Activity activity, final OnRoomAvailableListener listener) {
        Emitter.Listener onEmitListener = new Emitter.Listener() {

            @Override
            public void call(Object... args) {
                Log.i("SocketIO", "Receive message: room available");
                JSONObject data = (JSONObject) args[0];
                final String roomName;
                try {
                    roomName = data.getString("room_name");
                } catch (JSONException e) {
                    throw new RuntimeException();
                }
                activity.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        if (roomName != null) {
                            listener.onRoomAvailable(roomName);
                        }
                    }
                });
            }

        };
        listeners.put("room available", onEmitListener);
        mSocket.on("room available", onEmitListener);
    }

    /*
        This is only transmitted to this socket for confirmation on joining a room
     */
    public void onJoinedRoom(final Activity activity, final OnJoinedRoomListener listener) {
        Emitter.Listener onEmitListener = new Emitter.Listener() {

            @Override
            public void call(Object... args) {
                Log.i("SocketIO", "Receive message: joined room ");
                // data would be a Room.
                final Room r;
                try {
                    r = parseRoomFromJSON(((JSONObject) args[0]).getJSONObject("room"));
                } catch (JSONException e) {
                    throw new RuntimeException(e.getMessage());
                }
                activity.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        listener.onJoinedRoom(r);
                    }
                });

            }
        };

        listeners.put("joined room", onEmitListener);
        mSocket.on("joined room", onEmitListener);

    }


    /*
        This is received when other users in the room received the message
     */

    public void onUserJoinedRoom(final Activity activity, final OnUserJoinedRoomListener listener) {
        Emitter.Listener onEmitListener = new Emitter.Listener() {

            @Override
            public void call(Object... args) {
                Log.i("SocketIO", "Receive message: user joined room ");

                JSONObject data = (JSONObject) args[0];
                final User newUser = new User();
                final List<User> users = new ArrayList<User>();
                try {
                    JSONObject newUserObject = data.getJSONObject("new_user");
                    newUser.setName(newUserObject.getString("userName"));
                    newUser.setName(newUserObject.getString("socketId"));
                    // parse User objects
                    final JSONObject usersObjects = data.getJSONObject("all_users");
                    Iterator<String> iterator = usersObjects.keys();
                    while (iterator.hasNext()) {
                        String key = iterator.next();
                        JSONObject u = usersObjects.getJSONObject(key);
                        User user = new User(u.getString("socketId"), u.getString("userName"));
                        users.add(user);
                    }
                } catch (JSONException e) {
                    new RuntimeException(e);
                }
                activity.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        listener.onUserJoinedRoom(newUser, users);
                    }
                });

            }
        };

        listeners.put("user joined room", onEmitListener);
        mSocket.on("user joined room", onEmitListener);
    }
    public void onRoomCreated(final Activity activity, final OnRoomCreatedListener listener) {
        Emitter.Listener onEmitListener = new Emitter.Listener() {

            @Override
            public void call(Object... args) {
                Log.i("SocketIO", "Receive message: room created");
                // data would be a Room.
                final Room r;
                try {
                    r = parseRoomFromJSON(((JSONObject) args[0]).getJSONObject("room"));
                } catch (JSONException e) {
                    throw new RuntimeException(e.getMessage());
                }
                activity.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        listener.onRoomCreated(r);
                    }
                });
            }
        };

        listeners.put("room created", onEmitListener);
        mSocket.on("room created", onEmitListener);
    }

    public void onVotingStart(final Activity activity, final OnVotingStartListener listener) {
        Emitter.Listener onEmitListener = new Emitter.Listener() {

            @Override
            public void call(Object... args) {
                Log.i("SocketIO", "Receive message: voting start");
                // data would be a Room.
                final Room r;
                try {
                    r = parseRoomFromJSON(((JSONObject) args[0]).getJSONObject("room"));
                } catch (JSONException e) {
                    throw new RuntimeException(e.getMessage());
                }
                activity.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        listener.onVotingStart(r);
                    }
                });
            }
        };

        listeners.put("voting start", onEmitListener);
        mSocket.on("voting start", onEmitListener);
    }


    public void onNewRound(final Activity activity, final OnNewRoundListener listener) {
        Emitter.Listener onEmitListener = new Emitter.Listener() {

            @Override
            public void call(Object... args) {
                Log.i("SocketIO", "Receive message: new round");
                JSONObject jsonObject = (JSONObject) args[0];
                final int roundNum;
                final Round currentRound;
                try {
                    roundNum = jsonObject.getInt("roundNum");
                    JSONObject roundObj = jsonObject.getJSONObject("this_round");
                    currentRound = new Round(roundObj.getString("placeA"), roundObj.getString("placeB"));
                } catch (JSONException e) {
                    throw new RuntimeException(e.getMessage());
                }
                activity.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        listener.onNewRound(roundNum, currentRound);
                    }
                });
            }
        };

        listeners.put("new round", onEmitListener);
        mSocket.on("new round", onEmitListener);
    }

    public void onUserVoted(final Activity activity, final OnUserVotedListener listener) {
        Emitter.Listener onEmitListener = new Emitter.Listener() {

            @Override
            public void call(Object... args) {
                Log.i("SocketIO", "Receive message: user voted");
                JSONObject jsonObject = (JSONObject) args[0];
                final User user = new User();
                final Round round;

                try {
                    JSONObject userObj = jsonObject.getJSONObject("user");
                    user.setName(userObj.getString("userName"));
                    user.setSocketId(userObj.getString("socketId"));
                    JSONObject roundObj = jsonObject.getJSONObject("round");
                    round = new Round(roundObj.getString("placeA"), roundObj.getString("placeB"));
                } catch (JSONException e) {
                    throw new RuntimeException(e.getMessage());
                }


                activity.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        listener.onUserVoted(user, round);
                    }
                });
            }
        };
        listeners.put("user voted", onEmitListener);
        mSocket.on("user voted", onEmitListener);
    }
    public void onRoundEnded(final Activity activity, final OnRoundEndedListener listener) {
        Emitter.Listener onEmitListener = new Emitter.Listener() {

            @Override
            public void call(Object... args) {

                Log.i("SocketIO", "Receive message: round ended");
                JSONObject jsonObject = (JSONObject) args[0];
                final int result;
                final Round round;
                Log.i("SocketIO", "no problems up to try in onRoundEnded");
                try {
                    result = jsonObject.getInt("result");
                    JSONObject roundObj = jsonObject.getJSONObject("round");
                    round = new Round(roundObj.getString("placeA"), roundObj.getString("placeB"));
                } catch (JSONException e) {
                    throw new RuntimeException(e.getMessage());
                }
                Log.i("SocketIO", "no problems up to runOnUiThread in onRoundEnded");

                activity.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        listener.onRoundEnded(result, round);
                    }
                });
            }
        };

        listeners.put("round ended", onEmitListener);
        mSocket.on("round ended", onEmitListener);
    }

    public void onVotingEnd(final Activity activity, final OnVotingEndListener listener) {
        Emitter.Listener onEmitListener = new Emitter.Listener() {

            @Override
            public void call(Object... args) {

                Log.i("SocketIO", "Receive message: voting end");
                final List<Round> rounds = new ArrayList<Round>();
                final String place;
                JSONObject jsonObject = (JSONObject) args[0];
                try {
                    JSONArray roundsArray = jsonObject.getJSONArray("rounds");
                    JSONArray placesArray = jsonObject.getJSONArray("winners");
                    for (int i = 0; i < roundsArray.length(); i++) {
                        JSONObject roundObj = roundsArray.getJSONObject(i);
                        Round round = new Round(roundObj.getString("placeA"), roundObj.getString("placeB"));
                        rounds.add(round);
                    }
                    place = placesArray.getString(0);
                } catch (JSONException e) {
                    throw new RuntimeException(e.getMessage());
                }
                // has to call setCompleted for Room
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        listener.onVotingEnd(rounds, place);
                    }
                });
            }
        };
        listeners.put("voting end", onEmitListener);
        mSocket.on("voting end", onEmitListener);
    }

    public void destroy() {
        mSocket.disconnect();

    }

    public static Room parseRoomFromJSON(JSONObject json) {

        final String roomId;
        final String roomName;
        final long time;
        final int radius;
        final double lat;
        final double lon;
        final List<User> users = new ArrayList<User>();

        try {
            JSONObject data = (JSONObject) json;
            roomId = data.getString("roomId");
            roomName = data.getString("roomName");
            time = Long.parseLong(data.getString("time"));
            radius = data.getInt("radius");
            lat = data.getJSONObject("center").getDouble("lat");
            lon = data.getJSONObject("center").getDouble("lon");

            // parse User objects
            final JSONObject usersObjects = data.getJSONObject("users");
            final List<JSONObject> usersList = new ArrayList<JSONObject>();
            Iterator<String> iterator = usersObjects.keys();
            while (iterator.hasNext()) {
                String key = iterator.next();
                usersList.add(usersObjects.getJSONObject(key));
            }
            for (JSONObject u : usersList) {
                User user = new User(u.getString("socketId"), u.getString("userName"));
                users.add(user);
            }


        } catch (JSONException e) {
            throw new RuntimeException(e.getMessage());
        }

        Room room = new Room(roomId, roomName, radius, lat, lon, new java.util.Date(time*1000));
        return room;
    }


    public interface OnRoomAvailableListener {
        public void onRoomAvailable(String roomName);
    }
    public interface OnJoinedRoomListener {
        public void onJoinedRoom(Room room);
    }
    public interface OnUserJoinedRoomListener {
        public void onUserJoinedRoom(User newUser, List<User> allUsers);
    }
    public interface OnRoomCreatedListener {
        public void onRoomCreated(Room r);
    }
    public interface OnVotingStartListener {
        public void onVotingStart(Room r);
    }
    public interface OnNewRoundListener {
        public void onNewRound(int roundNum, Round currentRound);
    }
    public interface OnUserVotedListener {
        public void onUserVoted(User user, Round currentRound);
    }
    public interface OnRoundEndedListener {
        public void onRoundEnded(int result, Round currentRound);
    }
    public interface OnVotingEndListener {
        public void onVotingEnd(List<Round> rounds, String place);
    }

}
