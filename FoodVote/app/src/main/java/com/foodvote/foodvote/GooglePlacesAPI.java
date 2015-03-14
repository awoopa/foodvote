package com.foodvote.foodvote;

import android.app.ListActivity;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

import com.foodvote.google.GooglePlace;
import com.foodvote.google.MyLocation;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.ByteArrayBuffer;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class GooglePlacesAPI extends ListActivity {
    ArrayList venuesList;

	// the google key

	// ============== YOU SHOULD MAKE NEW KEYS ====================//
	final String GOOGLE_KEY = "AIzaSyC_ta35eIv2p7z-EAo8y64oBd0K0Jh2VvQ";

	// we will need to take the latitude and the logntitude from a certain point
	// this is the center of New York

	String latitude;
	String longitude;
    String searchTerm = "mcdonalds";

	ArrayAdapter myAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_places);

        MyLocation.LocationResult locationResult = new MyLocation.LocationResult(){
            @Override
            public void gotLocation(Location location){
                //Got the location
                latitude = String.valueOf(location.getLatitude());
                longitude = String.valueOf(location.getLongitude());
                System.out.println(latitude);
                System.out.println(longitude);

            }
        };
        MyLocation myLocation = new MyLocation();
        myLocation.getLocation(this, locationResult);

        while (latitude == null || longitude == null);

		// start the AsyncTask that makes the call for the venus search.
		new googleplaces().execute();
	}

	private class googleplaces extends AsyncTask<View, Void, String> {

		String temp;

		@Override
		protected String doInBackground(View... urls) {
			// make Call to the url
			temp = makeCall("https://maps.googleapis.com/maps/api/place/search/json?location=" + latitude + "," + longitude + "&radius=10000&keyword=" + searchTerm + "&sensor=true&key=" + GOOGLE_KEY);
			
			//print the call in the console
			System.out.println("https://maps.googleapis.com/maps/api/place/search/json?location=" + latitude + "," + longitude + "&radius=1\0000&keyword=" + searchTerm + "&sensor=true&key=" + GOOGLE_KEY);
			return "";
		}

		@Override
		protected void onPreExecute() {
			// we can start a progress bar here
		}

		@Override
		protected void onPostExecute(String result) {
			if (temp == null) {
				// we have an error to the call
				// we can also stop the progress bar
			} else {
				// all things went right

				// parse Google places search result
				venuesList = parseGoogleParse(temp);

				List listTitle = new ArrayList();

				for (int i = 0; i < venuesList.size(); i++) {
					// make a list of the venus that are loaded in the list.
					// show the name, the category and the city
                    GooglePlace g = (GooglePlace) venuesList.get(i);
					listTitle.add(i, g.getName() + "\nOpen Now: " + g.getOpenNow() + "\n(" + g.getCategory() + ")");
				}

				// set the results to the list
				// and show them in the xml
				myAdapter = new ArrayAdapter(GooglePlacesAPI.this, R.layout.row_layout, R.id.listText, listTitle);
				setListAdapter(myAdapter);
			}
		}
	}

	public static String makeCall(String url) {

		// string buffers the url
		StringBuffer buffer_string = new StringBuffer(url);
		String replyString = "";

		// instanciate an HttpClient
		HttpClient httpclient = new DefaultHttpClient();
		// instanciate an HttpGet
		HttpGet httpget = new HttpGet(buffer_string.toString());

		try {
			// get the responce of the httpclient execution of the url
			HttpResponse response = httpclient.execute(httpget);
			InputStream is = response.getEntity().getContent();

			// buffer input stream the result
			BufferedInputStream bis = new BufferedInputStream(is);
			ByteArrayBuffer baf = new ByteArrayBuffer(20);
			int current = 0;
			while ((current = bis.read()) != -1) {
				baf.append((byte) current);
			}
			// the result as a string is ready for parsing
			replyString = new String(baf.toByteArray());
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(replyString);

		// trim the whitespaces
		return replyString.trim();
	}

	private static ArrayList<GooglePlace> parseGoogleParse(final String response) {

		ArrayList temp = new ArrayList();
		try {

			// make an jsonObject in order to parse the response
			JSONObject jsonObject = new JSONObject(response);

			// make an jsonObject in order to parse the response
			if (jsonObject.has("results")) {

				JSONArray jsonArray = jsonObject.getJSONArray("results");

				for (int i = 0; i < jsonArray.length(); i++) {
					GooglePlace poi = new GooglePlace();
					if (jsonArray.getJSONObject(i).has("name")) {
						poi.setName(jsonArray.getJSONObject(i).optString("name"));
						poi.setRating(jsonArray.getJSONObject(i).optString("rating", " "));

						if (jsonArray.getJSONObject(i).has("opening_hours")) {
							if (jsonArray.getJSONObject(i).getJSONObject("opening_hours").has("open_now")) {
								if (jsonArray.getJSONObject(i).getJSONObject("opening_hours").getString("open_now").equals("true")) {
									poi.setOpenNow("YES");
								} else {
									poi.setOpenNow("NO");
								}
							}
						} else {
							poi.setOpenNow("Not Known");
						}
						if (jsonArray.getJSONObject(i).has("types")) {
							JSONArray typesArray = jsonArray.getJSONObject(i).getJSONArray("types");

							for (int j = 0; j < typesArray.length(); j++) {
								poi.setCategory(typesArray.getString(j) + ", " + poi.getCategory());
							}
						}
					}
					temp.add(poi);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList();
		}
		return temp;

	}
}