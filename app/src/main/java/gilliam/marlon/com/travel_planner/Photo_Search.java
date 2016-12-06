package gilliam.marlon.com.travel_planner;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class Photo_Search extends Activity {

    private ArrayList<Photo> photoList = new ArrayList<Photo>();
    private ArrayList<Bitmap> thumbList;

    private Button download;
    private GridView gallery;
    private ImageView imgView;
    private EditText search;

    private Flicker flickr = new Flicker();

    String title;
    String id;
    String owner;
    String secret;
    String server;
    int farm;

    Bitmap bit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo__search);

        download = (Button) findViewById(R.id.download);
        search = (EditText) findViewById(R.id.search);
        gallery = (GridView) findViewById(R.id.gallery);
        imgView = (ImageView) findViewById(R.id.imageView);

        //gallery.SetOnClickListener(imageListener);
    }

    public void searchPhotos(View view)
    {
        photoList.clear();
        new AsyncTaskParseJson().execute();
    }

    public void displayBitmap(View view)
    {
        Photo temp = photoList.get(1);
        Bitmap bit = getThumbnail(temp);
        imgView.setImageBitmap(bit);
    }

    public class AsyncTaskParseJson extends AsyncTask<String, String, String>
    {
        String tags = search.getText().toString();
        String url = flickr.createURL(1, tags);

        @Override
        protected void onPreExecute() {}

        @Override
        protected String doInBackground(String... arg0)
        {
            try
            {
                httpConnect jsonParser = new httpConnect();

                String json = jsonParser.getJSONFromUrl(url);

                JSONObject jsonObject = new JSONObject(json);

                JSONArray jsonArray = jsonObject.getJSONObject("photos").getJSONArray("photo");

                for (int i = 0; i < jsonArray.length(); i++)
                {
                    JSONObject json_message = jsonArray.getJSONObject(i);

                    if (json_message != null)
                    {
                        id = json_message.getString("id");
                        owner = json_message.getString("owner");
                        secret = json_message.getString("secret");
                        server = json_message.getString("server");
                        farm = json_message.getInt("farm");
                        title = json_message.getString("title");

                        Photo photo = new Photo(id, owner, secret, server, farm, title);
                        photo.setThumbUrl(photo.createUrl(1, photo));
                        bit = getThumbnail(photo);
                        photoList.add(photo);
                    }
                }
            } catch (JSONException e)
            {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String strFromDoInBg)
        {
            Toast toast = Toast.makeText(Photo_Search.this, "HELLO!", Toast.LENGTH_LONG);
            toast.show();

            Photo temp = photoList.get(1);

            imgView.setImageBitmap(bit);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_photo__search, menu);
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

    public Bitmap getThumbnail(Photo photo)
    {
        Bitmap bitmap = null;
        try
        {
            URL aURL = new URL(photo.URL_thumbnail);
            HttpURLConnection connection = (HttpURLConnection) aURL.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            BufferedInputStream buffInput = new BufferedInputStream(input);
            bitmap = BitmapFactory.decodeStream(buffInput);
            buffInput.close();
            input.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
