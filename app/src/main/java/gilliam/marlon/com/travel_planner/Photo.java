package gilliam.marlon.com.travel_planner;

import android.graphics.Bitmap;

/**
 * Created by Computing on 05/12/2016.
 */
public class Photo {
    String id;
    String user;
    String secret;
    String server;
    String farm;
    String title;

    Bitmap thumbnail;
    Bitmap photo;

    public Photo(String ID, String USER, String SECRET, String SERVER, String FARM, String TITLE)
    {
        id = ID;
        user = USER;
        secret = SECRET;
        server = SERVER;
        farm = FARM;
        title = TITLE;
    }
}
