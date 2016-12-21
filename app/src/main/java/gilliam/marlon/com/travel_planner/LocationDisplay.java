package gilliam.marlon.com.travel_planner;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class LocationDisplay extends Activity {

    Place myPlace = new Place();
    TextView location;
    TextView description;
    ImageView imageView;
    String id;
    Bundle extras;
    Cursor cursor;

    Bitmap photo;

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
        imageView = (ImageView) findViewById(R.id.imageView);

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
        cursor = Home.myDb.getPicture(id);
        int i = 0;
        while(cursor.moveToNext())
        {
            myPlace.id = cursor.getString(0);
            myPlace.location = cursor.getString(1);
            photo = (BitmapFactory.decodeByteArray(cursor.getBlob(2), 0, cursor.getBlob(2).length));
            i++;
        }
        location.setText(myPlace.location);
        description.setText(myPlace.description);
        imageView.setImageBitmap(photo);
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
