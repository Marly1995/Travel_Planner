package gilliam.marlon.com.travel_planner;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

/**
 * Created by Computing on 07/12/2016.
 */
public class PhotoDisplay extends Activity{

    private int contentView;

    Button delete;
    Button save;
    ImageView photoView;
    Byte[] imageBytes;
    Bitmap image;
    String id;
    Bundle extras;
    String placeID;

    String mode;
    String saveString = "save";
    String deleteString = "delete";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        extras = getIntent().getExtras();
        if(extras != null)
        {
            image = BitmapFactory.decodeByteArray(extras.getByteArray("PHOTO_ID"), 0, extras.getByteArray("PHOTO_ID").length);
            id = extras.getString("ID");
            mode = extras.getString("mode");
            placeID = extras.getString("placeID");
        }
        setContentView(R.layout.activity_photo_display);

        save = (Button) findViewById(R.id.saveButton);
        delete = (Button) findViewById(R.id.deleteButton);
        photoView = (ImageView )findViewById(R.id.photoView);

        photoView.setImageBitmap(image);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        double width = dm.widthPixels;
        double height = dm.heightPixels;

        getWindow().setLayout((int)(width*0.8), (int)(height*0.6));
        Toast toast = Toast.makeText(PhotoDisplay.this, "Enjoy your image", Toast.LENGTH_LONG);
        toast.show();

        if (mode.equals(saveString))
        {
            delete.setVisibility(View.INVISIBLE);
        }
        else if (mode.equals(deleteString))
        {
            save.setVisibility(View.INVISIBLE);
        }
    }

    public void invokeSavePhoto(View view)
    {
        ByteArrayOutputStream bs = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 50, bs);
        boolean save = Home.myDb.insertPicture(id, bs.toByteArray());
        if (save == true)
        {
            Toast toast = Toast.makeText(PhotoDisplay.this, "Saved!", Toast.LENGTH_LONG);
            toast.show();
        }
    }

    public void invokeDeletePhoto(View view)
    {
        Integer deletedPicture = Home.myDb.deletePicture(id);
        if (deletedPicture > 0)
        {
            Toast toast = Toast.makeText(PhotoDisplay.this, "Photo Deleted", Toast.LENGTH_LONG);
            toast.show();
            Intent intent = new Intent(PhotoDisplay.this, LocationDisplay.class);
            intent.putExtra("ID", placeID);
            startActivity(intent);
        }
        else
        {
            Toast toast = Toast.makeText(PhotoDisplay.this, "Data not Deleted", Toast.LENGTH_LONG);
            toast.show();
        }
    }
}

