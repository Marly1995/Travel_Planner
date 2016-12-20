package gilliam.marlon.com.travel_planner;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageView;

import java.util.ArrayList;

public class Home extends Activity {

    public static DatabaseHelper myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        myDb = new DatabaseHelper(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
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

    public void invokePhotoSearch(View view)
    {
        Intent intent = new Intent(this, Photo_Search.class);
        startActivity(intent);
    }

    public void invokeDatabaseLocation(View view)
    {
        Intent intent = new Intent(this, Location.class);
        startActivity(intent);
    }

    public void invokeMap(View view)
    {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }
}
