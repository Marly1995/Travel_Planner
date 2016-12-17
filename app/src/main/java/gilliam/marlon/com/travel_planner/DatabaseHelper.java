package gilliam.marlon.com.travel_planner;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Computing on 17/12/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper{

    public static final String DATABASE_NAME = "Travel_Database.db";
    public static final String TABLE_NAME = "Locations_Table";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "Location";
    public static final String COL_3 = "Description";
    public static final String COL_4 = "Latitude";
    public static final String COL_5 = "Longitude";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(("create table " + TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, LOCATION TEXT, DESCRIPTION TEXT, LATITUDE FLOAT, LONGITUDE FLOAT)"));

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String location, String description, String latitude, String longitude)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, location);
        contentValues.put(COL_3, description);
        contentValues.put(COL_4, latitude);
        contentValues.put(COL_5, longitude);
        long result = db.insert(TABLE_NAME, null, contentValues);
        if (result == -1)
        {
            return false;
        }
        else { return true;}
    }

    public Cursor getData()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME, null);
        return res;
    }
}
