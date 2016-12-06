package gilliam.marlon.com.travel_planner;

// added below imports
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import android.util.Log;

/**
 * Created by Computing on 01/11/2016.
 */

public class httpConnect {

    // helps for debugging
    final String TAG = "JsonParser.java";
    // where the returned json data will be stored
    static String json = "";

    public String getJSONFromUrl(String url){
        try{
            //this code block represents a connection to your REST service
            // it also represents an HTTP get request tpo get data form the rest service, not post
            URL u = new URL(url);
            HttpURLConnection restConnection = (HttpURLConnection) u.openConnection();
            restConnection.setRequestMethod("GET");
            restConnection.setRequestProperty("Content-length", "0");
            restConnection.setUseCaches(false);
            restConnection.setAllowUserInteraction(false);
            restConnection.setConnectTimeout(10000);
            restConnection.setReadTimeout(10000);
            restConnection.connect();
            int status = restConnection.getResponseCode();

            // switch statement to catch HTTP 200 and 201 errors
            switch(status) {
                case 200:
                case 201:
                    //live connection to your rest service is established here using get input stream
                    BufferedReader br = new BufferedReader(new InputStreamReader(restConnection.getInputStream()));

                    // create a new string builder to store json data returned from the Rest service
                    StringBuilder sb = new StringBuilder();
                    String line;

                    // loop through returned data lin eby line and append to string builder var
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    br.close();

                    // remember you are storing the json as a string
                    try {
                        json = sb.toString();
                    } catch (Exception e) {
                        Log.e(TAG, "Error parsing data " + e.toString());
                    }
                    // return json string containing data to tweet activity
                    return json;
            }
        } catch (MalformedURLException ex){
            Log.e(TAG, "Malformed URL ");
        } catch (IOException ex){
            Log.e(TAG, "IO Exception");
        }
        return null;
    }
}
