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
    int farm;
    String title;

    String URL_thumbnail;
    String URL_photo;

    Bitmap thumbnail;
    Bitmap photo;

    public Photo(String ID, String USER, String SECRET, String SERVER, int FARM, String TITLE)
    {
        id = ID;
        user = USER;
        secret = SECRET;
        server = SERVER;
        farm = FARM;
        title = TITLE;
    }

    public void setThumbUrl(String url)
    {
        URL_thumbnail = url;
    }

    public void setPhotoUrl(String url)
    {
        URL_photo = url;
    }

    public String createUrl(int photoType, Photo photo)
    {
        String temp = "http://farm" + photo.farm + ".staticflickr.com/" + photo.server + "/" + photo.id + "_" + photo.secret;
        switch(photoType)
        {
            case 1:
                temp += "_s";
                break;
            case 2:
                temp += "_b";
                break;
        }
        temp += ".jpg";
        return temp;
    }
}
