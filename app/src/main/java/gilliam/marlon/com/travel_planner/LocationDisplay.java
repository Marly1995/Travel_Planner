package gilliam.marlon.com.travel_planner;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class LocationDisplay extends Activity {

    Place myPlace = new Place();
    TextView location;
    TextView description;
    String id;
    Bundle extras;
    Cursor cursor;

    LinearLayout myGallery;

    static final int REQUEST_TAKE_PHOTO = 1;
    static final int REQUEST_IMAGE_CAPTURE = 1;

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
        myGallery = (LinearLayout) findViewById(R.id.photoGallery);

        description.setMovementMethod(new ScrollingMovementMethod());

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
        while(cursor.moveToNext())
        {
            myPlace.photos.add(BitmapFactory.decodeByteArray(cursor.getBlob(2), 0, cursor.getBlob(2).length));
            myPlace.photosID.add(cursor.getString(0));
        }
        location.setText(myPlace.location);
        description.setText(myPlace.description);

        for (int p = 0; p < myPlace.photos.size(); p++)
        {
            myGallery.addView(insertPhoto(myPlace.photos.get(p), p));
        }
    }

    public View insertPhoto(final Bitmap photo, int i)
    {
        LinearLayout layout = new LinearLayout(getApplicationContext());
        layout.setLayoutParams(new LinearLayout.LayoutParams(200, 200));
        layout.setGravity(Gravity.CENTER);

        final ImageView imageView = new ImageView(getApplicationContext());
        imageView.setLayoutParams(new LinearLayout.LayoutParams(200, 200));
        imageView.setPadding(24, 48, 12, 0);
        imageView.setCropToPadding(true);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageBitmap(photo);
        int newID = Integer.parseInt(myPlace.photosID.get(i));
        imageView.setId(newID);

        imageView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                ByteArrayOutputStream bs = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.JPEG, 50, bs);
                Intent popUp = new Intent(LocationDisplay.this, PhotoDisplay.class);
                popUp.putExtra("PHOTO_ID", bs.toByteArray());
                popUp.putExtra("ID", Integer.toString(imageView.getId()));
                popUp.putExtra("mode", "delete");
                popUp.putExtra("placeID", id);
                startActivity(popUp);
            }});

        layout.addView(imageView);
        return layout;
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

    public void invokeCamera(View view){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // ensure there is a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) !=null){
            // create file where the photo will go

            startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK)
        {
            Bundle photoExtra = data.getExtras();
            Bitmap myBitmap = (Bitmap) photoExtra.get("data");
            ByteArrayOutputStream bs = new ByteArrayOutputStream();
            myBitmap.compress(Bitmap.CompressFormat.JPEG, 50, bs);
            Home.myDb.insertPicture(myPlace.id, bs.toByteArray());
            Intent intent = new Intent(this, LocationDisplay.class);
            intent.putExtra("ID", myPlace.id);
            startActivity(intent);
        }
    }

    public void invokeEdit(View view)
    {
        Intent intent = new Intent(this, Location.class);
        intent.putExtra("mode", "update");
        intent.putExtra("ID", myPlace.id);
        intent.putExtra("lat", myPlace.latitude);
        intent.putExtra("lng", myPlace.longitude);
        intent.putExtra("NAME", myPlace.location);
        intent.putExtra("DESCRIPTION", myPlace.description);
        startActivity(intent);
    }

    public void invokeDelete(View view)
    {
        for (int i = 0; i < myPlace.photosID.size();i++)
        {
            Integer deletedPhoto = Home.myDb.deletePicture(myPlace.photosID.get(i));
        }
        Integer deletedRows = Home.myDb.deleteData(myPlace.id);
           if (deletedRows > 0)
           {
               Toast toast = Toast.makeText(LocationDisplay.this, "Data Deleted", Toast.LENGTH_LONG);
               toast.show();
               Intent intent = new Intent(this, LocationList.class);
               startActivity(intent);
           }
           else
           {
               Toast toast = Toast.makeText(LocationDisplay.this, "Data not Deleted", Toast.LENGTH_LONG);
               toast.show();
           }
    }
}
