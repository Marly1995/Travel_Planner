package gilliam.marlon.com.travel_planner;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Computing on 20/12/2016.
 */

public class Place {
    String location;
    String description;
    String latitude;
    String longitude;
    ArrayList<Bitmap> photos;
    String id;

    Place(String newId, String loc, String des, String lat, String lng)
    {
        location = loc;
        description = des;
        latitude = lat;
        longitude = lng;
        id = newId;
        photos = new ArrayList<>();
    }

    Place()
    {
        location = "";
        description = "";
        latitude = "";
        longitude = "";
        id = "";
        photos = new ArrayList<>();
    }
}
