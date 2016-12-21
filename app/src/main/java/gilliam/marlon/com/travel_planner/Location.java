package gilliam.marlon.com.travel_planner;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

public class Location extends Activity {

    EditText editLocation;
    EditText editDescription;
    EditText editLatitude;
    EditText editLongitude;

    Button addData;
    Button updateData;

    LatLng latLong;
    String description;
    String location;
    String lat;
    String lng;
    String id;
    Bundle extras;

    String mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        extras = getIntent().getExtras();
        if(extras != null)
        {
            id = extras.getString("ID");
            lat = extras.getString("lat");
            lng = extras.getString("lng");
            location = extras.getString("NAME");
            description = extras.getString("DESCRIPTION");
            mode = extras.getString("mode");
        }

        editLocation = (EditText) findViewById(R.id.editLocation);
        editDescription = (EditText) findViewById(R.id.editDescription);
        editLatitude = (EditText) findViewById(R.id.editLatitude);
        editLongitude = (EditText) findViewById(R.id.editLongitude);
        addData = (Button) findViewById(R.id.saveData);
        updateData = (Button) findViewById(R.id.updateData);
        AddData();
        UpdateData();
        editLatitude.setText(lat);
        editLongitude.setText(lng);
        editLocation.setText(location);
        editDescription.setText(description);

        if (mode.equals("add"))
        {
            updateData.setVisibility(View.INVISIBLE);
        }
        else if (mode.equals("update"))
        {
            addData.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_location, menu);
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

    public void AddData()
    {
        addData.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean isInserted = Home.myDb.insertData(editLocation.getText().toString(),
                                editDescription.getText().toString(),
                                editLatitude.getText().toString(),
                                editLongitude.getText().toString());
                        Cursor res = Home.myDb.getData();
                        String openId = "";
                        if(res.getCount() == 0)
                        {
                            // error message
                            return;
                        }
                        while(res.moveToNext())
                        {
                            openId = res.getString(0);
                        }
                        if (isInserted == true)
                        {
                            Toast toast = Toast.makeText(Location.this, "Data is in", Toast.LENGTH_LONG);
                            toast.show();
                            Intent intent = new Intent(Location.this, LocationDisplay.class);
                            intent.putExtra("ID", openId);
                            startActivity(intent);
                        }
                        else
                        {
                            Toast toast = Toast.makeText(Location.this, "Data not in", Toast.LENGTH_LONG);
                            toast.show();
                        }
                    }
                }

        );
    }

    public void UpdateData()
    {
        updateData.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean isUpdated = Home.myDb.updateData(id,
                                editLocation.getText().toString(),
                                editDescription.getText().toString(),
                                editLatitude.getText().toString(),
                                editLongitude.getText().toString());
                        if (isUpdated == true)
                        {
                            Toast toast = Toast.makeText(Location.this, "Data Updated", Toast.LENGTH_LONG);
                            toast.show();
                            Intent intent = new Intent(Location.this, LocationDisplay.class);
                            intent.putExtra("ID", id);
                            startActivity(intent);
                        }
                        else
                        {
                            Toast toast = Toast.makeText(Location.this, "Data not Updated", Toast.LENGTH_LONG);
                            toast.show();
                        }
                    }
                }
        );
    }

//    public void ViewData()
//    {
//        viewData.setOnClickListener(
//                new View.OnClickListener()
//                {
//                    @Override
//                    public void onClick(View v)
//                    {
//                        Cursor res = Home.myDb.getData();
//                        if(res.getCount() == 0)
//                        {
//                            showMessage("Error", "Nothing found");
//                            return;
//                        }
//                        StringBuffer buffer = new StringBuffer();
//                        while(res.moveToNext())
//                        {
//                            buffer.append("Id :" + res.getString(0) + "\n");
//                            buffer.append("Location :" + res.getString(1) + "\n");
//                            buffer.append("Description :" + res.getString(2) + "\n");
//                            buffer.append("Latitude :" + res.getString(3) + "\n");
//                            buffer.append("Longitude :" + res.getString(4) + "\n\n");
//                        }
//
//                        showMessage("Data", buffer.toString());
//                    }
//                }
//        );
//    }

//    public void DeleteData()
//    {
//        deleteData.setOnClickListener(
//                new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Integer deletedRows = Home.myDb.deleteData(editId);
//                        if (deletedRows > 0)
//                        {
//                            Toast toast = Toast.makeText(Location.this, "Data Deleted", Toast.LENGTH_LONG);
//                            toast.show();
//                        }
//                        else
//                        {
//                            Toast toast = Toast.makeText(Location.this, "Data not Deleted", Toast.LENGTH_LONG);
//                            toast.show();
//                        }
//                    }
//                }
//        );
//    }

    public void showMessage(String title, String message)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }
}
