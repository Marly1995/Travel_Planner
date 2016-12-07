package gilliam.marlon.com.travel_planner;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by Computing on 07/12/2016.
 */
public class ImageAdapter extends BaseAdapter {
    private Context mContext;

    public ArrayList<Photo> thumb = new ArrayList<>();

    public ImageAdapter(Context c, ArrayList<Photo> p)
    {
        mContext = c;
        thumb = p;
    }

    public int getCount()
    {
        return thumb.size();
    }

    public Object getItem(int position)
    {
        return null;
    }

    public long getItemId(int position)
    {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        ImageView imageView;

        if(convertView == null)
        {
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(200, 200));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(12, 12, 12, 12);
        }
        else
        {
            imageView = (ImageView) convertView;
        }
        imageView.setImageBitmap(thumb.get(position).thumbnail);
        return imageView;
    }
}
