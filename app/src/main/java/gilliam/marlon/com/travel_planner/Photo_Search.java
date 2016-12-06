package gilliam.marlon.com.travel_planner;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Photo_Search extends Activity {

    private ArrayList<String> photoList;

    private Button download;
    private GridView gallery;
    private ImageView imgView;
    private EditText search;

    private Flicker flickr;

    String title;
    String id;
    String owner;
    String secret;
    String server;
    int farm;


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

    public void searchPhotos()
    {
        photoList.clear();
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

                JSONArray jsonArray = jsonObject.getJSONArray("photo");

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
                        photoList.add(json_message.getString("text"));
                    }
                }
            } catch (JSONException e)
            {
                e.printStackTrace();
            }
            return null;
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
}
