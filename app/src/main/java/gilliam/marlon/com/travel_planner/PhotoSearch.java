package gilliam.marlon.com.travel_planner;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

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
import java.net.URLConnection;
import java.util.ArrayList;

public class PhotoSearch extends Activity {

    private ArrayList<Photo> photoList = new ArrayList<Photo>();
    private ArrayList<Bitmap> thumbList;

    private Button download;
    private GridView gallery;
    private EditText search;

    private Flicker flickr = new Flicker();

    String title;
    String id;
    String owner;
    String secret;
    String server;
    int farm;

    String placeID;
    String placeLAT;
    String placeLONG;

    Bitmap bit;
    Bundle extras;
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo__search);

        extras = getIntent().getExtras();
        if(extras != null)
        {
            placeID = extras.getString("ID");
            placeLAT = extras.getString("LAT");
            placeLONG = extras.getString("LONG");
        }

        download = (Button) findViewById(R.id.download);
        search = (EditText) findViewById(R.id.search);
        gallery = (GridView) findViewById(R.id.gallery);
        //gallery.SetOnClickListener(imageListener);

        gallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bitmap img = photoList.get(position).photo;
                ByteArrayOutputStream bs = new ByteArrayOutputStream();
                img.compress(Bitmap.CompressFormat.JPEG, 50, bs);
                Intent popUp = new Intent(PhotoSearch.this, PhotoDisplay.class);
                popUp.putExtra("PHOTO_ID", bs.toByteArray());
                popUp.putExtra("ID", placeID);
                popUp.putExtra("mode", "save");
                startActivity(popUp);
            }
        });
    }

    public void searchPhotos(View view)
    {
        photoList.clear();
        new AsyncTaskParseJson().execute();
    }

    public class AsyncTaskParseJson extends AsyncTask<String, String, String>
    {
        String tags = search.getText().toString();
        String url = flickr.createURL(1, tags, placeLAT, placeLONG);

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
                        photo.setPhotoUrl(photo.createUrl(2, photo));
                        bit = getThumbnail(photo);
                        photo.thumbnail = bit;
                        bit = getImage(photo);
                        photo.photo = bit;
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
        protected void onPostExecute(String strFromDoInBg) {
            Toast toast = Toast.makeText(PhotoSearch.this, "HELLO!", Toast.LENGTH_LONG);
            toast.show();

            ImageAdapter imgAdpt = new ImageAdapter(context, photoList);
            gallery.setAdapter(imgAdpt);
            //gallery.setImageBitmap(bit);
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

    public Bitmap getImage(Photo photo)
    {
        Bitmap bitmap = null;
        try
        {
            URL aURL = new URL(photo.URL_photo);
            URLConnection connection = aURL.openConnection();
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
        if (bitmap != null)
        {
            return bitmap;
        }
        else
        {
            return bitmap = getThumbnail(photo);
        }

    }
}
