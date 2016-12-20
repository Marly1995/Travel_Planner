package gilliam.marlon.com.travel_planner;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class LocationList extends Activity {

    public ArrayList<Place> places = new ArrayList<Place>();
    public ArrayList<String> names = new ArrayList<String>();
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loaction_list);
        listView = (ListView) findViewById(R.id.placeList);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(LocationList.this, DisplayLocation.class);
                intent.putExtra("place", places.get(position).id);
                startActivity(intent);
            }
        });

        initializePlaceList();


    }

    public void initializePlaceList()
    {
        Cursor res = Home.myDb.getData();
        if(res.getCount() == 0)
        {
            // error message
            return;
        }
        int i = 0;
        while(res.moveToNext())
        {
            Place newPlace = new Place(res.getString(0), res.getString(1), res.getString(2), res.getString(3), res.getString(4));
            places.add(i, newPlace);
            names.add(i, newPlace.location);
            i++;
        }
        initializeListView();
    }

    public void initializeListView ()
    {
        ArrayAdapter<String> placeArrayAdapter = new ArrayAdapter<String>(LocationList.this, android.R.layout.simple_expandable_list_item_1, names);
        listView.setAdapter(placeArrayAdapter);
    }
}
