package gilliam.marlon.com.travel_planner;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Computing on 05/12/2016.
 */
public class Flicker {

    // URL strings
    private String URL_BASE = "https://api.flickr.com/services/rest/?method=";
    private String SEARCH_PHOTOS = "flickr.photos.search";
    private String GET_PHOTO_SIZES = "flickr.photos.getSizes";
    private final int PHOTO_SEARCH_ID = 1;
    private final int SIZE_SEARCH_ID = 2;

    private String API_SEARCH_KEY = "9007c694ed9e35889907e0f6423d86e6";

    private String TAGS = "&tags=";
    private String PHOTO_ID = "&photo_id=";
    private String FORMAT = "&format=json";

    private static int CONNECT_TIMEOUT_MS = 5000;
    private static int READ_TIMEOUT_MS = 15000;

    private String createURL(int methodId, String search_parameter)
    {
        String method_type = "";
        String url = null;
        switch (methodId)
        {
            case PHOTO_SEARCH_ID:
                method_type = SEARCH_PHOTOS;
                url = URL_BASE + method_type + API_SEARCH_KEY + PHOTO_ID + search_parameter + FORMAT + "&per_page="
                        break;
            case SIZE_SEARCH_ID:
                method_type = GET_PHOTO_SIZES;
                url = URL_BASE + method_type + PHOTO_ID + search_parameter + API_SEARCH_KEY + FORMAT;
                break;
        }
        return url;
    }

    public void getImageURLS(Photo photo)
    {
        String url = createURL(SIZE_SEARCH_ID, photo.id);
        ByteArrayOutputStream output = readBytes(url);
        String json = output.toString();
        try
        {
            JSONObject root = new JSONObject(json.replace("jsonFlickrApi(", "").replace(")", ""));
            JSONObject sizes = root.getJSONObject("sizes");
            JSONArray size = sizes.getJSONArray("size");
            for (int i = 0; i < size.length(); i++)
            {
                JSONObject image = size.getJSONObject(i);
                if (image.getString("label").equals("Square"))
                {
                }
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public ByteArrayOutputStream readBytes(String urls)
    {
        ByteArrayOutputStream output = null;
        InputStream input = null;
        HttpURLConnection httpUrl = null;
        try
        {
            URL url = new URL(urls);
            Log.i("URL", url.toString());
            httpUrl = (HttpsURLConnection) url.openConnection();
            int response = httpUrl.getResponseCode();
            if (response == HttpURLConnection.HTTP_OK)
            {
                httpUrl.setConnectTimeout(CONNECT_TIMEOUT_MS);
                httpUrl.setReadTimeout(READ_TIMEOUT_MS);
                input = new BufferedInputStream((httpUrl.getInputStream()));

                int size  = 1024;
                byte[] buffer = new byte[size];

                output = new ByteArrayOutputStream();
                int read = 0;
                while ((read = input.read(buffer)) != -1)
                {
                    if (read > 0)
                    {
                        output.write(buffer, 0, read);
                        buffer = new byte[size];
                    }
                }
            }
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally
        {
            if (input != null)
            {
                try
                {
                    input.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            if (httpUrl != null)
            {
                try
                {
                    httpUrl.disconnect();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
        return output;
    }
}
