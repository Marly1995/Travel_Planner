package gilliam.marlon.com.travel_planner;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class DisplayLocation extends Activity {

    Place myPlace;
    TextView location;
    TextView description;
    String id;
    Bundle extras;

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
        myPlace = Home.myDb.getPlace(id);
        location.setText(myPlace.location);
        description.setText(myPlace.description);
    }
}
