package com.foodvote.yelp;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.scribe.builder.ServiceBuilder;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

import java.util.concurrent.ExecutionException;

/**
 * Code sample for accessing the Yelp API V2.
 * 
 * This program demonstrates the capability of the Yelp API version 2.0 by using the Search API to
 * query for businesses by a search term and location, and the Business API to query additional
 * information about the top result from the search query.
 * 
 * <p>
 * See <a href="http://www.yelp.com/developers/documentation">Yelp Documentation</a> for more info.
 * 
 */
public class YelpAPI {

  private static final String API_HOST = "api.yelp.com";
  private static final String DEFAULT_TERM = "dinner";
  private static final String DEFAULT_LOCATION = "San Francisco, CA";
  private static final int SEARCH_LIMIT = 10;
  private static final String SEARCH_PATH = "/v2/search";
  private static final String BUSINESS_PATH = "/v2/business";

  /*
   * Update OAuth credentials below from the Yelp Developers API site:
   * http://www.yelp.com/developers/getting_started/api_access
   */
  private static final String CONSUMER_KEY = "cMyiWA1GqAjFMhSFZ9u-aQ";
  private static final String CONSUMER_SECRET = "afvqKo1thnMKZiFqPdNBKeY6ubw";
  private static final String TOKEN = "aQASncaGiWECKJaFiID7qtXtdl0BwLgS";
  private static final String TOKEN_SECRET = "1ySktPeFjEA9BNdP-PuANZQtd60";

  private static final OAuthService service =
          new ServiceBuilder().provider(TwoStepOAuth.class).apiKey(CONSUMER_KEY)
                  .apiSecret(CONSUMER_SECRET).build();


  private static final Token accessToken = new Token(TOKEN, TOKEN_SECRET);

  /**
   * Setup the Yelp API OAuth credentials.
   *
   */
  public YelpAPI() {
  }

  /**
   * Creates and sends a request to the Search API by term and location.
   * <p>
   * See <a href="http://www.yelp.com/developers/documentation/v2/search_api">Yelp Search API V2</a>
   * for more info.
   * 
   * @param term <tt>String</tt> of the search term to be queried
   * @param ltlng  <tt>String</tt> of the location
   * @return <tt>String</tt> JSON Response
   */
  public String searchForBusinessesByLocation(String term, LatLng ltlng) {
      String s = "";
      try {
          String latlon = ltlng.latitude + "," + ltlng.longitude;
          HTTPAsyncTask h = new HTTPAsyncTask();
          AsyncTask j = h.execute(term, latlon, "location");
          s = (String) j.get();
          return s;
      } catch (InterruptedException e) {
          e.printStackTrace();
      } catch (ExecutionException e) {
          e.printStackTrace();
      }

      // shouldn't get here
      assert(false);
      return null;
  }

  /**
   * Creates and sends a request to the Business API by business ID.
   * <p>
   * See <a href="http://www.yelp.com/developers/documentation/v2/business">Yelp Business API V2</a>
   * for more info.
   * 
   * @param businessID <tt>String</tt> business ID of the requested business
   * @return <tt>String</tt> JSON Response
   */
  public String searchByBusinessId(String businessID) {
      try {
          return (new HTTPAsyncTask()).execute(businessID, null, "business").get();
      } catch (InterruptedException e) {
          e.printStackTrace();
      } catch (ExecutionException e) {
          e.printStackTrace();
      }

      // shouldn't get here
      assert(false);
      return null;

  }

  /**
   * Creates and returns an {@link OAuthRequest} based on the API endpoint specified.
   * 
   * @param path API endpoint to be queried
   * @return <tt>OAuthRequest</tt>
   */
  private OAuthRequest createOAuthRequest(String path) {
    OAuthRequest request = new OAuthRequest(Verb.GET, "http://" + API_HOST + path);
    return request;
  }

  /**
   * Sends an {@link OAuthRequest} and returns the {@link Response} body.
   * 
   * @param request {@link OAuthRequest} corresponding to the API request
   * @return <tt>String</tt> body of API response
   */
  private String sendRequestAndGetResponse(OAuthRequest request) {
    this.service.signRequest(this.accessToken, request);
    Response response = request.send();
    return response.getBody();
  }

    private class HTTPAsyncTask extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {
            OAuthRequest request;
            if (params[2] == "business") {
                request = createOAuthRequest(BUSINESS_PATH + "/" + params[0]);
            }
            else {
                request = createOAuthRequest(SEARCH_PATH);
                request.addQuerystringParameter("limit", String.valueOf(SEARCH_LIMIT));
                request.addQuerystringParameter("term", params[0]);
                request.addQuerystringParameter("ll", params[1]);
;
            }

            service.signRequest(accessToken, request);
            Log.d("YelpAPI", request.toString());
            Log.d("YelpAPI", request.getCompleteUrl());
            Log.d("YelpAPI", request.getOauthParameters().toString());
            Response response = request.send();
            return response.getBody();

        }

        protected void onPostExecute(Boolean result) {

        }
    }

}
