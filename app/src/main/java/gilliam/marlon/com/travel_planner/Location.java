package gilliam.marlon.com.travel_planner;

import android.app.Activity;
import android.app.AlertDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Location extends Activity {

    EditText editLocation;
    EditText editDescription;
    EditText editLatitude;
    EditText editLongitude;
    Button addData;
    Button viewData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        editLocation = (EditText) findViewById(R.id.editLocation);
        editDescription = (EditText) findViewById(R.id.editDescription);
        editLatitude = (EditText) findViewById(R.id.editLatitude);
        editLongitude = (EditText) findViewById(R.id.editLongitude);
        addData = (Button) findViewById(R.id.addData);
        viewData = (Button) findViewById(R.id.viewData);
        AddData();
        viewAll();
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
                        if (isInserted == true)
                        {
                            Toast toast = Toast.makeText(Location.this, "Data is in", Toast.LENGTH_LONG);
                        }
                        else
                        {
                            Toast toast = Toast.makeText(Location.this, "Data not in", Toast.LENGTH_LONG);
                        }
                    }
                }
        );
    }

    public void viewAll()
    {
        viewData.setOnClickListener(
                new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        Cursor res = Home.myDb.getData();
                        if(res.getCount() == 0)
                        {
                            showMessage("Error", "Nothing found");
                            return;
                        }
                        StringBuffer buffer = new StringBuffer();
                        while(res.moveToNext())
                        {
                            buffer.append("Id :" + res.getString(0) + "\n");
                            buffer.append("Location :" + res.getString(1) + "\n");
                            buffer.append("Description :" + res.getString(2) + "\n");
                            buffer.append("Latitude :" + res.getString(3) + "\n");
                            buffer.append("Longitude :" + res.getString(4) + "\n\n");
                        }

                        showMessage("Data", buffer.toString());
                    }
                }
        );
    }

    public void showMessage(String title, String message)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }
}
