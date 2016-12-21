package gilliam.marlon.com.travel_planner;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class LocationDisplay extends Activity {

    Place myPlace = new Place();
    TextView location;
    TextView description;
    String id;
    Bundle extras;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_location);

        extras = getIntent().getExtras();
        if(extras != null)
        {
            id = extras.getString("ID");

        }

        location = (TextView) findViewById(R.id.locationText);
        description = (TextView) findViewById(R.id.descriptionText);

        initializeLocation();
    }

    public void initializeLocation()
    {
        cursor = Home.myDb.getPlace(id);
        if(cursor.getCount() == 0)
        {
            Toast toast = Toast.makeText(LocationDisplay.this, "No data found", Toast.LENGTH_LONG);
            toast.show();
            return;
        }
        while(cursor.moveToNext())
        {
            myPlace.id = cursor.getString(0);
            myPlace.location = cursor.getString(1);
            myPlace.description = cursor.getString(2);
            myPlace.latitude = cursor.getString(3);
            myPlace.longitude = cursor.getString(4);
        }
        location.setText(myPlace.location);
        description.setText(myPlace.description);
    }

    public void invokeShowOnMap(View view)
    {
        Intent intent = new Intent(LocationDisplay.this, MapDisplay.class);
        intent.putExtra("LAT", myPlace.latitude);
        intent.putExtra("LONG", myPlace.longitude);
        startActivity(intent);
    }

    public void invokePhotoSearch(View view)
    {
        Intent intent = new Intent(LocationDisplay.this, PhotoSearch.class);
        intent.putExtra("LAT", myPlace.latitude);
        intent.putExtra("LONG", myPlace.longitude);
        intent.putExtra("ID", myPlace.id);
        startActivity(intent);
    }
}
